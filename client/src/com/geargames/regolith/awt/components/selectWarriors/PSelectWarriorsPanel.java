package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.platform.util.StringFormatter;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.managers.ClientBattleCreationManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.network.RequestHelper;
import com.geargames.regolith.serializers.answers.ClientCompleteGroupAnswer;
import com.geargames.regolith.serializers.answers.ClientJoinToBattleAllianceAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

/**
 * User: abarakov
 * Date: 26.03.13
 * Панель просмотра доступных для найма бойцов и выбора одного или нескольких из них для битвы.
 */
public class PSelectWarriorsPanel extends PRootContentPanel {

    private PWarriorList warriorList;
    private BattleGroup battleGroup;

    public PSelectWarriorsPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 1: {
                // Заголовок списка
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.SELECT_WARRIORS_LIST_TITLE);
                labelTitle.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
            case 2: {
                // Список бойцов
                warriorList = new PWarriorList((PObject) index.getPrototype());
                addActiveChild(warriorList, index);
                break;
            }

            // ----- Общие компоненты ----------------------------------------------------------------------------------

            case 3:
                // Кнопка Ok
                PButtonOk buttonOk = new PButtonOk((PObject) index.getPrototype());
                buttonOk.setText(LocalizedStrings.BUTTON_OK);
                buttonOk.setFont(PFontCollection.getFontButtonCaption());
                addActiveChild(buttonOk, index);
                break;
            case 109: {
                // Заголовок окна
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.SELECT_WARRIORS_PANEL_TITLE);
                labelTitle.setFont(PFontCollection.getFontFormTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
        }
    }

    @Override
    public void onShow() {
        Debug.debug("Dialog 'Select warriors': onShow");
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        warriorList.setWarriorList(clientConfiguration.getAccount().getWarriors());
    }

    @Override
    public void onHide() {
        Debug.debug("Dialog 'Select warriors': onHide");
    }

    public void showPanel(BattleGroup battleGroup/*, DrawablePPanel callerPanel*/) {
        Debug.debug("Dialog 'Select warriors'");
        this.battleGroup = battleGroup;

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();

        Battle battle = battleGroup.getAlliance().getBattle();
        int accountId = clientConfiguration.getAccount().getId();
        if (battle.getAuthor().getId() != accountId && (battleGroup.getAccount() == null || battleGroup.getAccount().getId() != accountId)) {
            // Клиент не является владельцем битвы и не входит ни в один из альянсов битвы, поэтому, сначало нужно подписаться эту битву
            try {
                Debug.debug("Trying to connect to the battle for listening (battle id = " + battle.getId() + ")...");
                ClientListenToBattleAnswer listenToBattleAnswer = battleMarketManager.listenToBattle(battle);
                if (!listenToBattleAnswer.isSuccess()) {
                    Debug.error("ListenToBattle: Request rejected (battle id = " + battle.getId() + ")");
                    NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_LISTEN_TO_BATTLE_EXCEPTION, this);
                    return;
                }
                Debug.debug("The client listens to the battle (battle id = " + battle.getId() + ")");
                clientConfiguration.setBattle(battle);
            } catch (Exception e) {
                Debug.error("ListenToBattle: Send request and receive answer is failed (battle id = " + battle.getId() + ")", e);
                NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_LISTEN_TO_BATTLE_EXCEPTION, this);
                return;
            }
        }

        if (battleGroup.getAccount() == null || battleGroup.getAccount().getId() != accountId) {
            // Если клиент еще не занял эту боевую группу то он должен ее занять
            try {
                Debug.debug("The client is trying join to an alliance (alliance id = " + battleGroup.getAlliance().getId() + "; number = " + battleGroup.getAlliance().getNumber() + ")...");
                ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = battleCreationManager.joinToAlliance(battleGroup.getAlliance());
                if (!joinToBattleAllianceAnswer.isSuccess()) {
                    Debug.error("JoinToAlliance: Request rejected (alliance id = " + battleGroup.getAlliance().getId() + ")");
                    NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION, this);
                    return;
                }
                //todo: battleGroup и joinToBattleAllianceAnswer.getBattleGroup() разные объекты, потому battleGroup может иметь устаревшее содержимое после десериализации ответа
//               battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
                Debug.debug("Client joined to the alliance (alliance id = " + battleGroup.getAlliance().getId() + ")");
            } catch (Exception e) {
                Debug.error("JoinToAlliance: Send request and receive answer is failed (alliance id = " + battleGroup.getAlliance().getId() + ")", e);
                NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION, this);
                return;
            }
        } else {
            // Клиент повторно хочет выбрать бойцов.
            if (!RequestHelper.disbandBattleGroup(battleGroup, this)) {
                return;
            }
        }

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//        panelManager.hide(callerPanel);
        panelManager.showModal(panelManager.getSelectWarriorsWindow());

        WarriorCollection warriors = battleGroup.getWarriors();
        if (warriors != null) {
            // Попытаемся выделить тех бойцов, которых игрок уже выбирал ранее
            for (int i = 0; i < warriorList.getItemsAmount(); i++) {
                PWarriorListItem item = warriorList.getItem(i);
                boolean exists = false;
                for (int j = 0; j < warriors.size(); j++) {
                    if (item.getWarrior().getId() == warriors.get(j).getId()) {
                        exists = true;
                        break;
                    }
                }
                item.setChecked(exists);
            }
        }
    }

    /**
     * Обработчик нажатия на кнопку Ok.
     */
    public void onButtonOkClick() {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
//        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();

        int amount = 0;
        for (int i = 0; i < warriorList.getItemsAmount(); i++) {
            if (warriorList.getItem(i).getChecked()) {
                amount++;
            }
        }

        int requiredAmount = battleGroup.getAlliance().getBattle().getBattleType().getGroupSize();
        if (amount != requiredAmount) {
            NotificationBox.warning(
                    StringFormatter.format(LocalizedStrings.SELECT_WARRIORS_MSG_NOT_ENOUGH_WARRIORS, requiredAmount), this);
            return;
        }

        int j = 0;
        Warrior[] initWarriors = new Warrior[amount];
        for (int i = 0; i < warriorList.getItemsAmount(); i++) {
            PWarriorListItem item = warriorList.getItem(i);
            if (item.getChecked()) {
                initWarriors[j++] = item.getWarrior();
            }
        }

        try {
            Debug.debug("The client is trying to complete the battle group (battle group id = " + battleGroup.getId() + ")...");
            ClientCompleteGroupAnswer completeGroupAnswer = battleCreationManager.completeGroup(battleGroup, initWarriors);
            if (!completeGroupAnswer.isSuccess()) {
                Debug.critical("CompleteGroup: Request rejected (battle group id = " + battleGroup.getId() + ")");
                NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_COMPLETE_GROUP_EXCEPTION, this);
                return;
            }
            Debug.debug("Client completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        } catch (Exception e) {
            Debug.error("CompleteGroup: Send request and receive answer is failed (battle group id = " + battleGroup.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_COMPLETE_GROUP_EXCEPTION, this);
            return;
        }

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//        panelManager.hide(panelManager.getSelectWarriorsWindow());
//        panelManager.show(panelManager.getMainMenu());
        panelManager.getBattlesPanel().showPanel(battleGroup.getAlliance().getBattle(), panelManager.getSelectWarriorsWindow());
    }

}
