package com.geargames.regolith.awt.components.battleCreate;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.components.*;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.localization.LocalizedStrings;

/**
 * User: abarakov
 * Date: 04.03.13
 * Панель создания битвы.
 */
public class PBattleCreatePanel extends PRootContentPanel {

    private PRadioGroup groupSide;
    private PRadioGroup groupPlayer;
    private PRadioGroup groupFighter;

    private PRadioButton buttonSide1;
    private PRadioButton buttonSide2;
    private PRadioButton buttonSide3;
    private PRadioButton buttonSide4;

    private PRadioButton buttonPlayer1;
    private PRadioButton buttonPlayer2;
    private PRadioButton buttonPlayer3;
    private PRadioButton buttonPlayer4;

    private PRadioButton buttonFighter1;
    private PRadioButton buttonFighter2;
    private PRadioButton buttonFighter3;
    private PRadioButton buttonFighter4;

    private PEntitledToggleButton cbRandomMap;

    public PBattleCreatePanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
//                IndexObject view = (IndexObject) prototype.getIndexBySlot(53);
//
//                Account account = ClientConfigurationFactory.getConfiguration().getAccount();
//                PObject object = (PObject) view.getPrototype();
                break;

            // ----- Количество команд на битву ------------------------------------------------------------------------

            case 15: {
                // Заголовок группы кнопок
                PSimpleLabel label = new PSimpleLabel(index);
                label.setText(LocalizedStrings.BATTLE_CREATE_GROUP_SIDES);
                label.setFont(PFontCollection.getFontLabel());
                addPassiveChild(label, index);
                break;
            }
            case 1:
                // Кнопка выбора количества команд - 1 команда
                if (groupSide == null) {
                    groupSide = new PRadioGroup(4);
                }
                //todo: Число команд должно быть минимум две
                buttonSide1 = new PButtonSide((PObject) index.getPrototype(), (byte)1);
                addActiveChild(buttonSide1, index);
                groupSide.addButton(buttonSide1);
                break;
            case 2:
                // Кнопка выбора количества команд - 2 команды
                if (groupSide == null) {
                    groupSide = new PRadioGroup(4);
                }
                buttonSide2 = new PButtonSide((PObject) index.getPrototype(), (byte)2);
                buttonSide2.setChecked(true); // По умолчанию в битве учавствуют две команды
                addActiveChild(buttonSide2, index);
                groupSide.addButton(buttonSide2);
                break;
            case 3:
                // Кнопка выбора количества команд - 3 команды
                if (groupSide == null) {
                    groupSide = new PRadioGroup(4);
                }
                buttonSide3 = new PButtonSide((PObject) index.getPrototype(), (byte)3);
                addActiveChild(buttonSide3, index);
                groupSide.addButton(buttonSide3);
                break;
            case 4:
                // Кнопка выбора количества команд - 4 команды
                if (groupSide == null) {
                    groupSide = new PRadioGroup(4);
                }
                buttonSide4 = new PButtonSide((PObject) index.getPrototype(), (byte)4);
                addActiveChild(buttonSide4, index);
                groupSide.addButton(buttonSide4);
                break;

            // ----- Количество игроков на команду ---------------------------------------------------------------------

            case 16: {
                // Заголовок группы кнопок
                PSimpleLabel label = new PSimpleLabel(index);
                label.setText(LocalizedStrings.BATTLE_CREATE_GROUP_PLAYERS);
                label.setFont(PFontCollection.getFontLabel());
                addPassiveChild(label, index);
                break;
            }
            case 5:
                // Кнопка выбора количества игроков в каждой команде - 1 игрок
                if (groupPlayer == null) {
                    groupPlayer = new PRadioGroup(4);
                }
                buttonPlayer1 = new PButtonPlayer((PObject) index.getPrototype(), (byte)1);
                buttonPlayer1.setChecked(true); // По умолчанию один игрок в команде
                addActiveChild(buttonPlayer1, index);
                groupPlayer.addButton(buttonPlayer1);
                break;
            case 6:
                // Кнопка выбора количества игроков в каждой команде - 2 игрока
                if (groupPlayer == null) {
                    groupPlayer = new PRadioGroup(4);
                }
                buttonPlayer2 = new PButtonPlayer((PObject) index.getPrototype(), (byte)2);
                addActiveChild(buttonPlayer2, index);
                groupPlayer.addButton(buttonPlayer2);
                break;
            case 7:
                // Кнопка выбора количества игроков в каждой команде - 3 игрока
                if (groupPlayer == null) {
                    groupPlayer = new PRadioGroup(4);
                }
                buttonPlayer3 = new PButtonPlayer((PObject) index.getPrototype(), (byte)3);
                addActiveChild(buttonPlayer3, index);
                groupPlayer.addButton(buttonPlayer3);
                break;
            case 8:
                // Кнопка выбора количества игроков в каждой команде - 4 игрока
                if (groupPlayer == null) {
                    groupPlayer = new PRadioGroup(4);
                }
                buttonPlayer4 = new PButtonPlayer((PObject) index.getPrototype(), (byte)4);
                addActiveChild(buttonPlayer4, index);
                groupPlayer.addButton(buttonPlayer4);
                break;

            // ----- Количество бойцов на игрока -----------------------------------------------------------------------

            case 17: {
                // Заголовок группы кнопок
                PSimpleLabel label = new PSimpleLabel(index);
                label.setText(LocalizedStrings.BATTLE_CREATE_GROUP_WARRIORS);
                label.setFont(PFontCollection.getFontLabel());
                addPassiveChild(label, index);
                break;
            }
            case 9:
                // Кнопка выбора количества бойцов на каждого игрока - 1 боец
                if (groupFighter == null) {
                    groupFighter = new PRadioGroup(4);
                }
                buttonFighter1 = new PButtonFighter((PObject) index.getPrototype(), (byte)1);
                buttonFighter1.setChecked(true); // По умолчанию один игрок в команде
                addActiveChild(buttonFighter1, index);
                groupFighter.addButton(buttonFighter1);
                break;
            case 10:
                // Кнопка выбора количества бойцов на каждого игрока - 2 бойца
                if (groupFighter == null) {
                    groupFighter = new PRadioGroup(4);
                }
                buttonFighter2 = new PButtonFighter((PObject) index.getPrototype(), (byte)2);
                addActiveChild(buttonFighter2, index);
                groupFighter.addButton(buttonFighter2);
                break;
            case 11:
                // Кнопка выбора количества бойцов на каждого игрока - 3 бойца
                if (groupFighter == null) {
                    groupFighter = new PRadioGroup(4);
                }
                buttonFighter3 = new PButtonFighter((PObject) index.getPrototype(), (byte)3);
                addActiveChild(buttonFighter3, index);
                groupFighter.addButton(buttonFighter3);
                break;
            case 12:
                // Кнопка выбора количества бойцов на каждого игрока - 4 бойца
                if (groupFighter == null) {
                    groupFighter = new PRadioGroup(4);
                }
                buttonFighter4 = new PButtonFighter((PObject) index.getPrototype(), (byte)4);
                addActiveChild(buttonFighter4, index);
                groupFighter.addButton(buttonFighter4);
                break;
            case 18:
                // CheckBox для указания использования случайной карты
                cbRandomMap = new PEntitledToggleButton((PObject) index.getPrototype());
                cbRandomMap.setText(LocalizedStrings.BATTLE_CREATE_BUTTON_USE_RANDOM_MAP);
                cbRandomMap.setFont(PFontCollection.getFontButtonCaption());
//                cbRandomMap.setChecked(true);
                addActiveChild(cbRandomMap, index);
                break;

            // ----- Общие компоненты ----------------------------------------------------------------------------------

            case 13:
                // Кнопка Ok
                PButtonOk buttonOk = new PButtonOk((PObject) index.getPrototype());
                buttonOk.setText(LocalizedStrings.BUTTON_OK);
                buttonOk.setFont(PFontCollection.getFontButtonCaption());
                addActiveChild(buttonOk, index);
                break;
            case 109:
                // Заголовок окна
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.BATTLE_CREATE_PANEL_TITLE);
                labelTitle.setFont(PFontCollection.getFontFormTitle());
                addPassiveChild(labelTitle, index);
                break;
        }
    }

    // setMaxPlayers
    public void setSideCount(byte number) {
        buttonPlayer1.setEnabled(true);
        buttonPlayer2.setEnabled(true);
        buttonPlayer3.setEnabled(number <= 3);
        buttonPlayer3.setChecked(buttonPlayer3.getChecked() && buttonPlayer3.getEnabled());
        buttonPlayer4.setEnabled(number <= 2);
        buttonPlayer4.setChecked(buttonPlayer4.getChecked() && buttonPlayer4.getEnabled());
    }

    /**
     * Вернуть количество команд (союзов) участвующих в битве.
     * @return
     */
    public byte getAllianceAmount() {
        byte allianceAmount = 0;
        if (buttonSide1.getChecked()) {
            allianceAmount = 1;
        } else
        if (buttonSide2.getChecked()) {
            allianceAmount = 2;
        } else
        if (buttonSide3.getChecked()) {
            allianceAmount = 3;
        } else
        if (buttonSide4.getChecked()) {
            allianceAmount = 4;
        }
        if (allianceAmount < 2) {
            Debug.warning("allianceAmount is an invalid value (allianceAmount = "+allianceAmount+")");
        }
        return allianceAmount;
    }

    /**
     * Вернуть количество игроков участвующих в каждой команде во время битвы.
     * @return
     */
    public byte getAllianceSize() {
        byte allianceSize = 0;
        if (buttonPlayer1.getChecked()) {
            allianceSize = 1;
        } else
        if (buttonPlayer2.getChecked()) {
            allianceSize = 2;
        } else
        if (buttonPlayer3.getChecked()) {
            allianceSize = 3;
        } else
        if (buttonPlayer4.getChecked()) {
            allianceSize = 4;
        } else {
            Debug.warning("allianceSize is an invalid value (allianceSize = 0)");
        }
        return allianceSize;
    }

    /**
     * Вернуть количество бойцов (юнитов) сражающихся за каждого игрока в битве.
     * @return
     */
    public byte getGroupSize() {
        byte groupSize = 0;
        if (buttonFighter1.getChecked()) {
            groupSize = 1;
        } else
        if (buttonFighter2.getChecked()) {
            groupSize = 2;
        } else
        if (buttonFighter3.getChecked()) {
            groupSize = 3;
        } else
        if (buttonFighter4.getChecked()) {
            groupSize = 4;
        } else {
            Debug.warning("groupSize is an invalid value (groupSize = 0)");
        }
        return groupSize;
    }

    public boolean getIsRandomMap() {
        return cbRandomMap.getChecked();
    }

    @Override
    public void onShow() {
        Debug.debug("Dialog 'Create battle': onShow");
    }

    @Override
    public void onHide() {
        Debug.debug("Dialog 'Create battle': onHide");
    }

    public void showPanel(DrawablePPanel callerPanel) {
        Debug.debug("Dialog 'Create battle'");

//        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
//        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
//
//        try {
//            Debug.debug("The client go to the battle market...");
//            ClientConfirmationAnswer confirm = baseManager.goBattleMarket();
//            if (!confirm.isConfirm()) {
//                Debug.critical("The client could not go to the battle market");
//                //to do: Сообщить пользователю об ошибке
//                return;
//            }
//        } catch (Exception e) {
//            Debug.critical("Could not get the map to create a battle", e);
//        }

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(callerPanel);
        panelManager.show(panelManager.getBattleCreateWindow());
    }

    /**
     * Обработчик нажатия на кнопку Ok.
     */
    public void onButtonOkClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getSelectMapPanel().showPanel(
                getAllianceAmount(), getAllianceSize(), getGroupSize(), getIsRandomMap(), panelManager.getBattleCreateWindow());
    }

}
