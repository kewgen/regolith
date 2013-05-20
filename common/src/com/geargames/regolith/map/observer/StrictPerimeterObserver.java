package com.geargames.regolith.map.observer;

import com.geargames.common.util.Mathematics;
import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.TestHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Реализация "строгово" и "грубого" обозревателя. Проводятся прямые линии из центра ячейки где стоит боец до крайних
 * точек квадрата-обозрения, если ячейка пересекается линией, то она считается видимой.
 * Алгоритм бросания взгляда оптимизирован тем, что взгляд бросается по разному в разные стороны.
 * "Грубый" - линия взгляда проводится только к центрам самых дальних точек.
 * "Строгий" - преграда занимает всю ячейку и если такая ячейка пересечна взглядом то остальные точки лежащие на
 * линии взгляда помечаются невидимыми.
 * User: mkutuzov
 * Date: 14.03.12
 */
public class StrictPerimeterObserver extends Observer {
    private LineViewCaster lineViewCaster;
    private VisibilityMaintainer visibilityMaintainer;

    public StrictPerimeterObserver(WarriorCollection allies) {
        lineViewCaster = new LineViewCaster();
        visibilityMaintainer = new VisibilityMaintainer(allies);
    }

    @Override
    public WarriorCollection observe(Warrior warrior) {
        System.out.println("A warrior named " + warrior.getName() + " is observing a territory");

        visibilityMaintainer.getEnemies().clear();
        BattleGroup group = warrior.getBattleGroup();
        BattleAlliance alliance = group.getAlliance();
        Battle battle = alliance.getBattle();
        BattleMap battleMap = battle.getMap();
        int radius = WarriorHelper.getObservingRadius(warrior);
        int x = warrior.getCellX();
        int y = warrior.getCellY();

        SecurityOperationManager manager = warrior.getBattleGroup().getAccount().getSecurity();

        System.out.println("BEFORE OBSERVATION " + manager.getObserve());
        TestHelper.printViewMap(battleMap, warrior);

        visibilityMaintainer.maintain(battleMap.getCells(), warrior, false, x, y);

        int length = battleMap.getCells().length;

        int rightBottomX = Mathematics.min(x + radius, length - 1);
        int rightBottomY = Mathematics.min(y + radius, length - 1);
        int amount = rightBottomY - Mathematics.max(y - radius, 0);
        if (x != rightBottomX)
            for (int i = 0; i <= amount; i++) {
                lineViewCaster.castViewToBiggerX(x, y, rightBottomX, rightBottomY - i, battleMap, warrior, visibilityMaintainer);
            }
        int leftTopX = Mathematics.max(x - radius, 0);
        int leftTopY = Mathematics.max(y - radius, 0);
        if (x != leftTopX) {
            amount = Mathematics.min(y + radius, length - 1) - leftTopY;
            for (int i = 0; i <= amount; i++) {
                lineViewCaster.castViewToLesserX(x, y, leftTopX, leftTopY + i, battleMap, warrior, visibilityMaintainer);
            }
        }
        int leftBottomX = Mathematics.max(x - radius + 1, 0);
        int leftBottomY = Mathematics.min(y + radius, length - 1);
        if (y != leftBottomY) {
            amount = Mathematics.min(x + radius - 1, length - 1) - leftBottomX;
            for (int i = 0; i <= amount; i++) {
                lineViewCaster.castViewToBiggerY(x, y, leftBottomX + i, leftBottomY, battleMap, warrior, visibilityMaintainer);
            }
        }
        int rightTopX = Mathematics.min(x + radius - 1, length - 1);
        int rightTopY = Mathematics.max(y - radius, 0);
        if (y != rightTopY) {
            amount = rightTopX - Mathematics.max(x - radius + 1, 0);
            for (int i = 0; i <= amount; i++) {
                lineViewCaster.castViewToLesserY(x, y, rightTopX - i, rightTopY, battleMap, warrior, visibilityMaintainer);
            }
        }
        WarriorCollection oldEnemies = warrior.getDetectedEnemies();
        WarriorCollection newEnemies = visibilityMaintainer.getEnemies();

        oldEnemies.retainAll(newEnemies);
        newEnemies.removeAll(oldEnemies);
        oldEnemies.addAll(newEnemies);

        System.out.println("AFTER OBSERVATION " + manager.getObserve());
        TestHelper.printViewMap(battleMap, warrior);

        return newEnemies;
    }

}
