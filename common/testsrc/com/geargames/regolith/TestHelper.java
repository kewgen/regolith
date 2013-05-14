package com.geargames.regolith;

import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.map.ExitZone;

/**
 * User: mkutuzov
 * Date: 03.03.12
 */
public class TestHelper {


    public static void printViewMap(BattleMap battleMap, BattleAlliance alliance) {
        //todo: использование счетчиков i и j явно напутано, использовать вместо них названия x и y соответственно
        for (int i = 0; i < battleMap.getCells().length; i++) {
            System.out.print('|');
            for (int j = 0; j < battleMap.getCells()[i].length; j++) {
                if (battleMap.getCells()[j][i].getElement() != null) {
                    if (battleMap.getCells()[j][i].getElement() instanceof Warrior) {
                        System.out.print('w');
                    } else {
                        System.out.print('b');
                    }
                } else {
                    if (BattleMapHelper.isVisible(battleMap.getCells()[j][i], alliance)) {
                        System.out.print('x');
                    } else {
                        System.out.print(' ');
                    }
                }
            }
            System.out.println('|');
        }
    }

    public static void printRouteMap(BattleMap battleMap, Warrior unit) {
        //todo: использование счетчиков i и j явно напутано, использовать вместо них названия x и y соответственно
        for (int i = 0; i < battleMap.getCells().length; i++) {
            System.out.print('|');
            for (int j = 0; j < battleMap.getCells()[i].length; j++) {
                if (battleMap.getCells()[j][i].getElement() != null) {
                    if (battleMap.getCells()[j][i].getElement() instanceof Warrior) {
                        System.out.print('w');
                    } else {
                        System.out.print('b');
                    }
                } else {
                    if (BattleMapHelper.isShortestPathCell(battleMap.getCells()[j][i], unit)) {
                        System.out.print('x');
                    } else {
                        if (BattleMapHelper.isReachable(battleMap.getCells()[j][i])) {
                            System.out.print(battleMap.getCells()[j][i].getOrder());
                        } else {
                            System.out.print('*');
                        }
                    }
                }
            }
            System.out.println('|');
        }
    }

    public static void printExitZones(BattleMap battleMap) {
        ExitZone[] exits = battleMap.getExits();
        //todo: использование счетчиков i и j явно напутано, использовать вместо них названия x и y соответственно
        for (int i = 0; i < battleMap.getCells().length; i++) {
            System.out.print('|');
            for (int j = 0; j < battleMap.getCells()[i].length; j++) {
                if (battleMap.getCells()[j][i].getElement() != null) {
                    if (battleMap.getCells()[j][i].getElement() instanceof Warrior) {
                        System.out.print('w');
                    } else {
                        System.out.print('b');
                    }
                } else {
                    boolean found = false;
                    for (ExitZone exit : exits) {
                        if (Math.abs(j - exit.getX()) <= exit.getxRadius() && Math.abs(i - exit.getY()) <= exit.getyRadius()) {
                            System.out.print('*');
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.print('.');
                    }
                }
            }
            System.out.println('|');
        }
    }


}
