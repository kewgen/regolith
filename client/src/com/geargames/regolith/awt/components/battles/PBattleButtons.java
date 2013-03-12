package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;

import java.util.Hashtable;

/**
 * User: mikhail v. kutuzov
 * Класс управляет отображением кнопок на родительской панели, учитывая тип битвы отображённый сейчас на ней.
 */
public class PBattleButtons {
    private Battle battle;
    private ArrayList activeBattleButtons;

    private ArrayList type1x1x1;
    private ArrayList type1x1;
    private ArrayList type1x1x1x1;

    private ArrayList type2x2;
    private ArrayList type2x2x2;
    private ArrayList type2x2x2x2;

    private ArrayList type3x3;
    private ArrayList type3x3x3;

    private ArrayList type4x4;
    private Hashtable table;

    public PBattleButtons(PObject prototype, PContentPanel container) {
        type1x1 = new ArrayList();
        type1x1x1 = new ArrayList();
        type1x1x1x1 = new ArrayList();

        type2x2x2x2 = new ArrayList();
        type2x2x2 = new ArrayList();
        type2x2 = new ArrayList();

        type3x3x3 = new ArrayList();
        type3x3 = new ArrayList();

        type4x4 = new ArrayList();

        table = new Hashtable();

        table.put("1x1", type1x1);
        table.put("1x1x1", type1x1x1);
        table.put("1x1x1x1", type1x1x1x1);
        table.put("2x2", type2x2);
        table.put("2x2x2", type2x2x2);
        table.put("2x2x2x2", type2x2x2x2);
        table.put("3x3", type3x3);
        table.put("3x3x3", type3x3x3);
        table.put("4x4", type4x4);

        ArrayList indexes = prototype.getIndexes();
        for (int i = 0; i < indexes.size(); i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            PPlayerButton button = new PPlayerButton((PObject) index.getPrototype());
            container.addActiveChild(button, index);
            button.setVisible(false);
            switch (index.getSlot()) {
                case 10:
                case 11:
                case 12:
                    type1x1x1.add(button);
                    break;
                case 14:
                case 15:
                    type1x1.add(button);
                    type1x1x1x1.add(button);
                    break;
                case 13:
                case 16:
                    type1x1x1x1.add(button);
                    break;
                case 30:
                case 31:
                case 32:
                    type2x2x2.add(button);
                    break;
                case 34:
                case 35:
                    type2x2.add(button);
                    type2x2x2x2.add(button);
                    break;
                case 33:
                case 36:
                    type2x2x2x2.add(button);
                    break;
                case 40:
                case 41:
                    type3x3.add(button);
                    break;
                case 42:
                case 43:
                case 44:
                    type3x3x3.add(button);
                    break;
                case 50:
                case 51:
                    type4x4.add(button);
                    break;
            }
        }
    }

    /**
     * Установить битву для отображения на панели.
     *
     * @param battle
     */
    public void setBattle(Battle battle) {
        if (activeBattleButtons != null) {
            visibility(activeBattleButtons, false);
        }
        ArrayList list = (ArrayList) table.get(battle.getBattleType().getName());
        visibility(list, true);
        activeBattleButtons = list;

        BattleAlliance[] alliances = battle.getAlliances();
        for (int i = 0; i < alliances.length; i++) {
            if (alliances[i] != null) {
                BattleGroupCollection groups = alliances[i].getAllies();
                for (int j = 0; j < groups.size(); j++) {
                    if (groups.get(j) != null) {
                        ((PPlayerButton) activeBattleButtons.get(i + j)).setAccount(groups.get(j).getAccount());
                    }
                }
            }
        }
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    /**
     * Установить аккаунт для отрисовки на кнопке игрока.
     * @param allianceNumber номер союза из которого берётся аккаунт
     * @param groupNumber номер боевой группы внутри союза из которой берётся аккаунт
     */
    public void resetButtonAccount(int allianceNumber, int groupNumber) {
        ((PPlayerButton) activeBattleButtons.get(allianceNumber*battle.getBattleType().getGroupSize() + groupNumber))
                .setAccount(battle.getAlliances()[allianceNumber].getAllies().get(groupNumber).getAccount());
    }


    public Account getButtonAccount(int allianceNumber, int groupNumber){
        return ((PPlayerButton) activeBattleButtons.get(allianceNumber*battle.getBattleType().getGroupSize() + groupNumber)).getAccount();
    }


    private void visibility(ArrayList list, boolean visible) {
        for (int i = 0; i < list.size(); i++) {
            ((PPlayerButton) list.get(i)).setVisible(visible);
        }
    }
}
