package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.awt.utils.motions.ElasticInertMotionListener;
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
import com.geargames.regolith.serializers.answers.ClientCancelBattleAnswer;
import com.geargames.regolith.serializers.answers.ClientCompleteGroupAnswer;
import com.geargames.regolith.serializers.answers.ClientJoinToBattleAllianceAnswer;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

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

                ElasticInertMotionListener motionListener = new ElasticInertMotionListener();
                warriorList.setMotionListener(motionListener);
//                CenteredElasticInertMotionListener motionListener = new CenteredElasticInertMotionListener();
//                motionListener.setInstinctPosition(false);
//                warriorList.setMotionListener(
//                        ScrollHelper.adjustHorizontalCenteredMenuMotionListener(
//                                motionListener,
//                                warriorList.getDrawRegion(),
//                                warriorList.getItemsAmount(),
//                                warriorList.getItemSize(),
//                                warriorList.getPrototype().getDrawRegion().getMinX()
//                        )
//                );
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
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        warriorList.setWarriorList(clientConfiguration.getAccount().getWarriors());
    }

    @Override
    public void onHide() {

    }

    public void showPanel(BattleGroup battleGroup, DrawablePPanel callerPanel) {
        this.battleGroup = battleGroup;

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();

        Debug.debug("The client is trying join to an alliance (alliance id = " + battleGroup.getAlliance().getId() + "; number = " + battleGroup.getAlliance().getNumber() + ")...");
        ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer;
        try {
            joinToBattleAllianceAnswer = battleCreationManager.joinToAlliance(battleGroup.getAlliance());
        } catch (Exception e) {
            Debug.error("The client could not join to the alliance (alliance id = " + battleGroup.getAlliance().getId() + ")", e);
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION, this);
            return;
        }
        if (!joinToBattleAllianceAnswer.isSuccess()) {
            Debug.error("The client could not join to the alliance (alliance id = " + battleGroup.getAlliance().getId() + ")");
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION, this);
            return;
        }
        //todo: battleGroup и joinToBattleAllianceAnswer.getBattleGroup() разные объекты, потому battleGroup может иметь устаревшее содержимое после десериализации ответа
//        battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Debug.debug("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (alliance id = " + battleGroup.getAlliance().getId() + ")");

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(callerPanel);
        panelManager.show(panelManager.getSelectWarriors());
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

        Debug.debug("The client is trying to complete the battle group (battle group id = " + battleGroup.getId() + ")...");
        ClientCompleteGroupAnswer completeGroupAnswer;
        try {
            completeGroupAnswer = battleCreationManager.completeGroup(battleGroup, initWarriors);
        } catch (Exception e) {
            Debug.error("Failed attempt to complete the group", e);
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_COMPLETE_GROUP_EXCEPTION, this);
            return;
        }
        if (!completeGroupAnswer.isSuccess()) {
            Debug.critical("The client could not complete the battle group (battle group id = " + battleGroup.getId() + ")");
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_COMPLETE_GROUP_EXCEPTION, this);
            return;
        }
        Debug.debug("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");

        Debug.debug("Trying to cancelation the battle (battle id = " + battleGroup.getAlliance().getBattle().getId() + ")...");
        ClientCancelBattleAnswer cancelBattleAnswer;
        try {
            cancelBattleAnswer = battleCreationManager.cancelBattle();
        } catch (Exception e) {
            Debug.error("The client is not able to cancel the battle (battle id = " + battleGroup.getAlliance().getBattle().getId() + ")", e);
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_CANCEL_BATTLE_EXCEPTION, this);
            return;
        }
        if (!cancelBattleAnswer.isSuccess()) {
            Debug.critical("The client is not able to cancel the battle (battle id = " + battleGroup.getAlliance().getBattle().getId() + ")");
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_CANCEL_BATTLE_EXCEPTION, this);
            return;
        }
        Debug.debug("The client canceled battle (battle id = " + battleGroup.getAlliance().getBattle().getId() + ")");

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(panelManager.getSelectWarriors());
        panelManager.show(panelManager.getMainMenu());
    }

}
