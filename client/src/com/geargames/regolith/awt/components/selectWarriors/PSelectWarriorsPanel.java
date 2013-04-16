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
import com.geargames.regolith.network.ClientRequestHelper;
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
    private boolean hasListening;

    public PSelectWarriorsPanel(PObject prototype) {
        super(prototype);
        battleGroup = null;
        hasListening = false;
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

//    @Override
//    public void onCancel() {
//        if (hasListening) {
//            ClientRequestHelper.doNotListenToBattle(ClientConfigurationFactory.getConfiguration().getBattle(), this,
//                    LocalizedStrings.SELECT_WARRIORS_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION);
//            ClientConfigurationFactory.getConfiguration().setBattle(null);
//        }
//    }

    public void showPanel(BattleGroup battleGroup/*, DrawablePPanel callerPanel*/) {
        Debug.debug("Dialog 'Select warriors'");
        this.battleGroup = battleGroup;

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();

        Battle battle = battleGroup.getAlliance().getBattle();
        int accountId = clientConfiguration.getAccount().getId();
        hasListening = false;
        if (clientConfiguration.getBattle() == null) {
            // Если клиент еще не подписан на битву, то подпишемся на нее
//      if (battle.getAuthor().getId() != accountId && (battleGroup.getAccount() == null || battleGroup.getAccount().getId() != accountId)) {
            // Клиент не является владельцем битвы и не входит ни в один из альянсов битвы, поэтому, сначало нужно подписаться на эту битву
            hasListening = ClientRequestHelper.listenToBattle(battle, this, LocalizedStrings.SELECT_WARRIORS_MSG_LISTEN_TO_BATTLE_EXCEPTION);
            if (hasListening) {
                clientConfiguration.setBattle(battle);
            } else {
                return;
            }
        } else {
            if (clientConfiguration.getBattle().getId() != battleGroup.getAlliance().getBattle().getId()) {
                Debug.error("PSelectWarriorsPanel.showPanel(): The client is not listened of battle, but is trying to join the battle (listened battle id = " + clientConfiguration.getBattle().getId() + "; joined battle id = " + battleGroup.getAlliance().getBattle().getId() + ")");
                NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION, this);
                return;
            }
        }

        if (battleGroup.getAccount() == null) {
            // Если клиент еще не занял эту боевую группу, то он должен ее занять
            boolean result = ClientRequestHelper.joinToAlliance(battleGroup, this, LocalizedStrings.SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION);
            if (!result) {
                if (hasListening) {
                    ClientRequestHelper.doNotListenToBattle(battle, this, LocalizedStrings.SELECT_WARRIORS_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION);
                    ClientConfigurationFactory.getConfiguration().setBattle(null);
                }
                return;
            }
        } else
        if (battleGroup.getAccount().getId() == accountId) {
            // Клиент повторно хочет выбрать бойцов.
            if (!ClientRequestHelper.disbandBattleGroup(battleGroup, this, LocalizedStrings.SELECT_WARRIORS_MSG_DISBAND_GROUP_EXCEPTION)) {
                return;
            }
        } else {
            Debug.error("");
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

        if (!ClientRequestHelper.completeBattleGroup(battleGroup, initWarriors, this, LocalizedStrings.SELECT_WARRIORS_MSG_COMPLETE_GROUP_EXCEPTION)) {
            return;
        }

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//        panelManager.hide(panelManager.getSelectWarriorsWindow());
//        panelManager.show(panelManager.getMainMenu());
        panelManager.getBattlesPanel().showPanel(battleGroup.getAlliance().getBattle(), battleGroup, panelManager.getSelectWarriorsWindow());
    }

}
