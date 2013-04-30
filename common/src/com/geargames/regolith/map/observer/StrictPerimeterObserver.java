package com.geargames.regolith.map.observer;

import com.geargames.common.logging.Debug;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.AllyCollection;
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

    public StrictPerimeterObserver(AllyCollection allies) {
        lineViewCaster = new LineViewCaster();
        visibilityMaintainer = new VisibilityMaintainer(allies);
    }

    public AllyCollection observe(Ally warrior) {
        System.out.println("a warrior named " + warrior.getName() + " is observing a territory");

        visibilityMaintainer.getAllies().clear();
        BattleGroup group = warrior.getBattleGroup();
        BattleAlliance alliance = group.getAlliance();
        Battle battle = alliance.getBattle();
        BattleMap battleMap = battle.getMap();
        int radius = WarriorHelper.getObservingRadius(warrior);
        int x = warrior.getX();
        int y = warrior.getY();

        int rightBottomX = x + radius;
        int rightBottomY = y + radius;
        for (int i = 0; i < 2 * radius + 1; i++) {
            lineViewCaster.castViewRight(x, y, rightBottomX, rightBottomY - i, battleMap, warrior, visibilityMaintainer);
        }
        int leftTopX = x - radius;
        int leftTopY = y - radius;
        for (int i = 0; i < 2 * radius + 1; i++) {
            lineViewCaster.castViewLeft(x, y, leftTopX, leftTopY + i, battleMap, warrior, visibilityMaintainer);
        }
        int leftBottomX = x - radius + 1;
        int leftBottomY = y + radius;
        for (int i = 0; i <= 2 * (radius - 1); i++) {
            lineViewCaster.castViewDown(x, y, leftBottomX + i, leftBottomY, battleMap, warrior, visibilityMaintainer);
        }
        int rightTopX = x + radius - 1;
        int rightTopY = y - radius;
        for (int i = 0; i <= 2 * (radius - 1); i++) {
            lineViewCaster.castViewUp(x, y, rightTopX - i, rightTopY, battleMap, warrior, visibilityMaintainer);
        }
        return visibilityMaintainer.getAllies();
    }
}
