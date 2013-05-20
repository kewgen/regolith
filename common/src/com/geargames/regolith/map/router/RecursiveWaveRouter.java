package com.geargames.regolith.map.router;

import com.geargames.common.logging.Debug;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: mkutuzov, abarakov
 * Date: 24.02.12
 */
public class RecursiveWaveRouter extends Router {
    private static final int RIGHT = 1;
    private static final int RIGHT_TOP = 2;
    private static final int TOP = 4;
    private static final int LEFT_TOP = 8;
    private static final int LEFT = 16;
    private static final int LEFT_BOTTOM = 32;
    private static final int BOTTOM = 64;
    private static final int RIGHT_BOTTOM = 128;

    private BattleCell[][] cells;
    private BattleAlliance battleAlliance;

    @Override
    public void route(Warrior warrior, BattleConfiguration battleConfiguration) {
        BattleMap battleMap = warrior.getBattleGroup().getAlliance().getBattle().getMap();
        int radius = WarriorHelper.getRoutableRadius(warrior, battleConfiguration);
        if (radius >= BattleMapHelper.UN_ROUTED) {
            Debug.critical("Routable radius exceeds " + BattleMapHelper.UN_ROUTED + " cells");
        }
        cells = battleMap.getCells();
        int x0 = warrior.getCellX();
        int y0 = warrior.getCellY();

        BattleMapHelper.setZeroOrder(warrior, cells);
        battleAlliance = warrior.getBattleGroup().getAlliance();
        tryNeighbours(x0, y0, radius, 0);
    }

    private void tryNeighbours(int x, int y, int radius, int direction) {
        if (radius <= 0) {
            return;
        }
        int length = cells.length;
        byte order = (byte) (BattleMapHelper.getOrder(cells[x][y]) + 1);

        BattleCell cell0 = null;
        if (x + 1 < length && ((direction & RIGHT) == 0)) {
            cell0 = cells[x + 1][y];
            if (BattleMapHelper.setOrder(cell0, order, battleAlliance)) {
                tryNeighbours(x + 1, y, radius - 1, LEFT_TOP | LEFT | LEFT_BOTTOM);
            }
        }
        BattleCell cell1 = null;
        if (x - 1 >= 0 && ((direction & LEFT) == 0)) {
            cell1 = cells[x - 1][y];
            if (BattleMapHelper.setOrder(cell1, order, battleAlliance)) {
                tryNeighbours(x - 1, y, radius - 1, RIGHT | RIGHT_TOP | RIGHT_BOTTOM);
            }
        }
        BattleCell cell2 = null;
        if (y + 1 < length && ((direction & BOTTOM) == 0)) {
            cell2 = cells[x][y + 1];
            if (BattleMapHelper.setOrder(cell2, order, battleAlliance)) {
                tryNeighbours(x, y + 1, radius - 1, RIGHT_TOP | TOP | LEFT_TOP);
            }
        }
        BattleCell cell3 = null;
        if (y - 1 >= 0 && ((direction & TOP) == 0)) {
            cell3 = cells[x][y - 1];
            if (BattleMapHelper.setOrder(cell3, order, battleAlliance)) {
                tryNeighbours(x, y - 1, radius - 1, LEFT_BOTTOM | BOTTOM | RIGHT_BOTTOM);
            }
        }
        if (((direction & RIGHT_BOTTOM) == 0) && cell0 != null && BattleMapHelper.isReachable(cell0) && cell2 != null && BattleMapHelper.isReachable(cell2)) {
            if (BattleMapHelper.setOrder(cells[x + 1][y + 1], order, battleAlliance)) {
                tryNeighbours(x + 1, y + 1, radius - 1, TOP | LEFT_TOP | LEFT);
            }
        }
        if (((direction & RIGHT_TOP) == 0) && cell0 != null && BattleMapHelper.isReachable(cell0) && cell3 != null && BattleMapHelper.isReachable(cell3)) {
            if (BattleMapHelper.setOrder(cells[x + 1][y - 1], order, battleAlliance)) {
                tryNeighbours(x + 1, y - 1, radius - 1, LEFT | LEFT_BOTTOM | BOTTOM);
            }
        }
        if (((direction & LEFT_BOTTOM) == 0) && cell1 != null && BattleMapHelper.isReachable(cell1) && cell2 != null && BattleMapHelper.isReachable(cell2)) {
            if (BattleMapHelper.setOrder(cells[x - 1][y + 1], order, battleAlliance)) {
                tryNeighbours(x - 1, y + 1, radius - 1, RIGHT | RIGHT | TOP);
            }
        }
        if (((direction & LEFT_TOP) == 0) && cell1 != null && BattleMapHelper.isReachable(cell1) && cell3 != null && BattleMapHelper.isReachable(cell3)) {
            if (BattleMapHelper.setOrder(cells[x - 1][y - 1], order, battleAlliance)) {
                tryNeighbours(x - 1, y - 1, radius - 1, RIGHT | RIGHT_BOTTOM | BOTTOM);
            }
        }
    }

}
