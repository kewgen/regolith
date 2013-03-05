package com.geargames.regolith.awt.components.battleCreate;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PRadioButton;
import com.geargames.awt.components.PRadioGroup;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.units.Account;

/**
 * User: abarakov
 * Date: 04.03.13
 * Панель создания битвы.
 */
public class PBattleCreatePanel extends PContentPanel {

    private PRadioGroup groupSide;
    private PRadioGroup groupPlayer;
    private PRadioGroup groupFighter;

    private PRadioButton buttonPlayer1;
    private PRadioButton buttonPlayer2;
    private PRadioButton buttonPlayer3;
    private PRadioButton buttonPlayer4;

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

            case 1:
                // Кнопка выбора количества команд - 1 команда
                if (groupSide == null) {
                    groupSide = new PRadioGroup(4);
                }
                //todo: Число команд должно быть минимум две
                PButtonSide buttonSide1 = new PButtonSide((PObject) index.getPrototype(), (byte)1);
                addActiveChild(buttonSide1, index);
                groupSide.addButton(buttonSide1);
                break;
            case 2:
                // Кнопка выбора количества команд - 2 команды
                if (groupSide == null) {
                    groupSide = new PRadioGroup(4);
                }
                PButtonSide buttonSide2 = new PButtonSide((PObject) index.getPrototype(), (byte)2);
                buttonSide2.setChecked(true); // По умолчанию в битве учавствуют две команды
                addActiveChild(buttonSide2, index);
                groupSide.addButton(buttonSide2);
                break;
            case 3:
                // Кнопка выбора количества команд - 3 команды
                if (groupSide == null) {
                    groupSide = new PRadioGroup(4);
                }
                PButtonSide buttonSide3 = new PButtonSide((PObject) index.getPrototype(), (byte)3);
                addActiveChild(buttonSide3, index);
                groupSide.addButton(buttonSide3);
                break;
            case 4:
                // Кнопка выбора количества команд - 4 команды
                if (groupSide == null) {
                    groupSide = new PRadioGroup(4);
                }
                PButtonSide buttonSide4 = new PButtonSide((PObject) index.getPrototype(), (byte)4);
                addActiveChild(buttonSide4, index);
                groupSide.addButton(buttonSide4);
                break;

            // ----- Количество игроков на команду ---------------------------------------------------------------------

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

            case 9:
                // Кнопка выбора количества бойцов на каждого игрока - 1 боец
                if (groupFighter == null) {
                    groupFighter = new PRadioGroup(4);
                }
                PButtonFighter buttonFighter1 = new PButtonFighter((PObject) index.getPrototype(), (byte)1);
                buttonFighter1.setChecked(true); // По умолчанию один игрок в команде
                addActiveChild(buttonFighter1, index);
                groupFighter.addButton(buttonFighter1);
                break;
            case 10:
                // Кнопка выбора количества бойцов на каждого игрока - 2 бойца
                if (groupFighter == null) {
                    groupFighter = new PRadioGroup(4);
                }
                PButtonFighter buttonFighter2 = new PButtonFighter((PObject) index.getPrototype(), (byte)2);
                addActiveChild(buttonFighter2, index);
                groupFighter.addButton(buttonFighter2);
                break;
            case 11:
                // Кнопка выбора количества бойцов на каждого игрока - 3 бойца
                if (groupFighter == null) {
                    groupFighter = new PRadioGroup(4);
                }
                PButtonFighter buttonFighter3 = new PButtonFighter((PObject) index.getPrototype(), (byte)3);
                addActiveChild(buttonFighter3, index);
                groupFighter.addButton(buttonFighter3);
                break;
            case 12:
                // Кнопка выбора количества бойцов на каждого игрока - 4 бойца
                if (groupFighter == null) {
                    groupFighter = new PRadioGroup(4);
                }
                PButtonFighter buttonFighter4 = new PButtonFighter((PObject) index.getPrototype(), (byte)4);
                addActiveChild(buttonFighter4, index);
                groupFighter.addButton(buttonFighter4);
                break;

            // ----- Общие компоненты ----------------------------------------------------------------------------------

            case 13:
                // Кнопка Ok
                PButtonOk buttonOk = new PButtonOk((PObject) index.getPrototype(), LocalizedStrings.BUTTON_OK);
                buttonOk.setText(LocalizedStrings.BATTLE_CREATE_TITLE);
                buttonOk.setFont(PFontCollection.getFontFormTitle());
                addActiveChild(buttonOk, index);
                break;
            case 19:
                // Заголовок окна
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.BATTLE_CREATE_TITLE);
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

}
