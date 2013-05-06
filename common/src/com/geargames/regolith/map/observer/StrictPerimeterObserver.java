package com.geargames.regolith.map.observer;

import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.HumanElementCollection;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.HumanElement;

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

    public StrictPerimeterObserver(HumanElementCollection allies) {
        lineViewCaster = new LineViewCaster();
        visibilityMaintainer = new VisibilityMaintainer(allies);
    }

    @Override
    public HumanElementCollection observe(HumanElement unit) {
        System.out.println("A warrior named " + unit.getHuman().getName() + " is observing a territory");

        visibilityMaintainer.getAllies().clear();
        BattleGroup group = unit.getHuman().getBattleGroup();
        BattleAlliance alliance = group.getAlliance();
        Battle battle = alliance.getBattle();
        BattleMap battleMap = battle.getMap();
        int radius = WarriorHelper.getObservingRadius(unit);
        int x = unit.getCellX();
        int y = unit.getCellY();

        int rightBottomX = x + radius;
        int rightBottomY = y + radius;
        for (int i = 0; i < 2 * radius + 1; i++) {
            lineViewCaster.castViewRight(x, y, rightBottomX, rightBottomY - i, battleMap, unit, visibilityMaintainer);
        }
        int leftTopX = x - radius;
        int leftTopY = y - radius;
        for (int i = 0; i < 2 * radius + 1; i++) {
            lineViewCaster.castViewLeft(x, y, leftTopX, leftTopY + i, battleMap, unit, visibilityMaintainer);
        }
        int leftBottomX = x - radius + 1;
        int leftBottomY = y + radius;
        for (int i = 0; i <= 2 * (radius - 1); i++) {
            lineViewCaster.castViewDown(x, y, leftBottomX + i, leftBottomY, battleMap, unit, visibilityMaintainer);
        }
        int rightTopX = x + radius - 1;
        int rightTopY = y - radius;
        for (int i = 0; i <= 2 * (radius - 1); i++) {
            lineViewCaster.castViewUp(x, y, rightTopX - i, rightTopY, battleMap, unit, visibilityMaintainer);
        }
        return visibilityMaintainer.getAllies();
    }

}
