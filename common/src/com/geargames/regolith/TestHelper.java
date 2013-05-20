package com.geargames.regolith;

import com.geargames.regolith.helpers.BattleCellHelper;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.CellElementLayers;
import com.geargames.regolith.units.map.ExitZone;

/**
 * User: mvkutuzov
 * Date: 15.05.13
 * Time: 13:38
 */
public class TestHelper {

    public static void printViewMap(BattleMap battleMap, Warrior warrior) {
        for (int y = 0; y < battleMap.getCells().length; y++) {
            System.out.print('|');
            for (int x = 0; x < battleMap.getCells()[y].length; x++) {
                if (BattleCellHelper.isAnythingUpperOrEqualPresented(battleMap.getCells()[x][y], CellElementLayers.DYNAMIC)) {
                    if (BattleCellHelper.getElementFromLayer(battleMap.getCells()[x][y], CellElementLayers.HUMAN) != null) {
                        System.out.print('w');
                    } else {
                        System.out.print('b');
                    }
                } else {
                    if (BattleMapHelper.isVisible(battleMap.getCells()[x][y], warrior)) {
                        System.out.print('x');
                    } else {
                        System.out.print(' ');
                    }
                }
            }
            System.out.println('|');
        }
    }


    public static void printViewMap(BattleMap battleMap, BattleAlliance alliance) {
        byte size = (byte) battleMap.getCells().length;
        for (int x = 0; x < size; x++) {
            System.out.print('|');
            for (int y = 0; y < size; y++) {
                if (BattleCellHelper.isAnythingUpperOrEqualPresented(battleMap.getCells()[x][y], CellElementLayers.DYNAMIC)) {
                    if (BattleCellHelper.getElementFromLayer(battleMap.getCells()[x][y], CellElementLayers.HUMAN) != null) {
                        System.out.print('w');
                    } else {
                        System.out.print('b');
                    }
                } else {
                    if (BattleMapHelper.isVisible(battleMap.getCells()[x][y], alliance)) {
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
        byte size = (byte) battleMap.getCells().length;
        for (int x = 0; x < size; x++) {
            System.out.print('|');
            for (int y = 0; y < size; y++) {
                if (BattleCellHelper.isAnythingUpperOrEqualPresented(battleMap.getCells()[x][y], CellElementLayers.DYNAMIC)) {
                    if (BattleCellHelper.getElementFromLayer(battleMap.getCells()[x][y], CellElementLayers.HUMAN) != null) {
                        System.out.print('w');
                    } else {
                        System.out.print('b');
                    }
                } else {
                    if (BattleMapHelper.isShortestPathCell(battleMap.getCells()[x][y], unit)) {
                        System.out.print('x');
                    } else {
                        if (BattleMapHelper.isReachable(battleMap.getCells()[x][y])) {
                            System.out.print(battleMap.getCells()[x][y].getOrder());
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
        byte size = (byte) battleMap.getCells().length;
        for (int x = 0; x < size; x++) {
            System.out.print('|');
            for (int y = 0; y < size; y++) {
                if (BattleCellHelper.isAnythingUpperOrEqualPresented(battleMap.getCells()[x][y], CellElementLayers.DYNAMIC)) {
                    if (BattleCellHelper.getElementFromLayer(battleMap.getCells()[x][y], CellElementLayers.HUMAN) != null) {
                        System.out.print('w');
                    } else {
                        System.out.print('b');
                    }
                } else {
                    boolean found = false;
                    for (ExitZone exit : exits) {
                        if (Math.abs(x - exit.getX()) <= exit.getxRadius() && Math.abs(y - exit.getY()) <= exit.getyRadius()) {
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
