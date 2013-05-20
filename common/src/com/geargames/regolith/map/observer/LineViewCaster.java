package com.geargames.regolith.map.observer;

import com.geargames.common.util.Mathematics;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public class LineViewCaster extends ViewCaster {
    public static LineViewCaster instance = new LineViewCaster();

/*
    private boolean markXPlusVisible(BattleCell[][] cells, Warrior warrior, boolean hidden, int x, int y, BattleCellMaintainer cellMaintainer) {
        int xx = x % Mathematics.PRECISION;
        xx = xx > Mathematics.HALF_PRECISION  ? x/Mathematics.PRECISION + 1 : x/Mathematics.PRECISION;

        if (xx >= 0 && xx < cells.length) {
            hidden = cellMaintainer.maintain(cells, warrior, hidden, xx, y);
        }
        return hidden;
    }

    private boolean markXMinusVisible(BattleCell[][] cells, Warrior warrior, boolean hidden, int x, int y, BattleCellMaintainer cellMaintainer) {
        int xx =  x % Mathematics.PRECISION;
        xx = xx > Mathematics.HALF_PRECISION ? x/Mathematics.PRECISION : x/Mathematics.PRECISION - 1;
        if (xx >= 0 && xx < cells.length) {
            hidden = cellMaintainer.maintain(cells, warrior, hidden, xx, y);
        }
        return hidden;
    }
*/

    public void castViewToLesserY(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer cellMaintainer) {
        int k = 0;
        if (x1 != x0) {
            k = ((y1 - y0) * Mathematics.PRECISION) / (x1 - x0);
        }
        int b = y0 * Mathematics.PRECISION - k * x0;
        BattleCell[][] cells = battleMap.getCells();
        boolean hidden = false;
        if (x1 > x0) {
            for (int y = y0 - 1; y > y1 && !hidden; y--) {
                int x = ((y * Mathematics.PRECISION /*+ Mathematics.HALF_PRECISION*/ - b) * Mathematics.PRECISION) / k;
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x/Mathematics.PRECISION, y);
                //hidden = markXPlusVisible(cells, warrior, hidden, x, y, cellMaintainer);
/*
                if (!hidden) {
                    x = ((y * Mathematics.PRECISION - Mathematics.HALF_PRECISION - b) * Mathematics.PRECISION) / k;
                    //hidden = cellMaintainer.maintain(cells, warrior, hidden, x/Mathematics.PRECISION, y);
                    hidden = markXPlusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                }
*/
            }
            if(!hidden){
                cellMaintainer.maintain(cells, warrior, hidden, x1, y1);
            }
        } else if (x1 < x0) {
            for (int y = y0 - 1; y > y1 && !hidden; y--) {
                int x = ((y * Mathematics.PRECISION /*+ Mathematics.HALF_PRECISION*/ - b) * Mathematics.PRECISION) / k;
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x/Mathematics.PRECISION, y);
/*                hidden = markXMinusVisible(cells, warrior, hidden, x, y, cellMaintainer);*/
/*
                if (!hidden) {
                    x = ((y * Mathematics.PRECISION - Mathematics.HALF_PRECISION - b) * Mathematics.PRECISION) / k;
                    //hidden = cellMaintainer.maintain(cells, warrior, hidden, x/Mathematics.PRECISION, y);
                    hidden = markXMinusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                }
*/
            }
            if(!hidden){
                cellMaintainer.maintain(cells, warrior, hidden, x1, y1);
            }
        } else {
            for (int y = y0 - 1; y >= y1 && !hidden; y--) {
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x0, y);
            }
        }
    }


    public void castViewToBiggerY(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer cellMaintainer) {
        int k = 0;
        if (x1 != x0) {
            k = ((y1 - y0) * Mathematics.PRECISION) / (x1 - x0);
        }
        int b = y0 * Mathematics.PRECISION - k * x0;
        BattleCell[][] cells = battleMap.getCells();
        boolean hidden = false;
        if (x0 < x1) {
            for (int y = y0 + 1; y < y1 && !hidden; y++) {
                int x = ((y * Mathematics.PRECISION /*- Mathematics.HALF_PRECISION*/ - b) * Mathematics.PRECISION) / k;
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x/Mathematics.PRECISION, y);
/*
                hidden = markXPlusVisible(cells, warrior, hidden, x, y, cellMaintainer);

                if (!hidden) {
                    x = ((y * Mathematics.PRECISION + Mathematics.HALF_PRECISION - b) * Mathematics.PRECISION )/ k;
                    //hidden = cellMaintainer.maintain(cells, warrior, hidden, x/Mathematics.PRECISION, y);
                    hidden = markXPlusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                }
*/
            }
            if(!hidden){
                cellMaintainer.maintain(cells, warrior, hidden, x1, y1);
            }
        } else if (x0 > x1) {
            for (int y = y0 + 1; y < y1 && !hidden; y++) {
                int x = ((y * Mathematics.PRECISION /*- Mathematics.HALF_PRECISION*/ - b) * Mathematics.PRECISION)/ k;
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x/Mathematics.PRECISION, y);
/*
                hidden = markXMinusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    x = ((y * Mathematics.PRECISION + Mathematics.HALF_PRECISION - b) * Mathematics.PRECISION)/ k;
                    hidden = markXMinusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                    //hidden = cellMaintainer.maintain(cells, warrior, hidden, x/Mathematics.PRECISION, y);
                }
*/
            }
            if(!hidden){
                cellMaintainer.maintain(cells, warrior, hidden, x1, y1);
            }
        } else {
            for (int y = y0 + 1; y <= y1 && !hidden; y++) {
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x0, y);
            }
        }
    }


/*
    private boolean markYPlusVisible(BattleCell[][] cells, Warrior warrior, boolean hidden, int x, int y, BattleCellMaintainer cellMaintainer) {
        int yy = y%Mathematics.PRECISION;
        yy = yy > Mathematics.HALF_PRECISION ? y/Mathematics.PRECISION + 1 : y/Mathematics.PRECISION;
        if (yy >= 0 && yy < cells.length) {
            hidden = cellMaintainer.maintain(cells, warrior, hidden, x, yy);
        }
        return hidden;
    }

    private boolean markYMinusVisible(BattleCell[][] cells, Warrior warrior, boolean hidden, int x, int y, BattleCellMaintainer cellMaintainer) {
        int yy = y%Mathematics.PRECISION;
        yy = yy > Mathematics.HALF_PRECISION ? y/Mathematics.PRECISION : y/Mathematics.PRECISION - 1;
        if (yy >= 0 && yy < cells.length) {
            hidden = cellMaintainer.maintain(cells, warrior, hidden, x, yy);
        }
        return hidden;
    }
*/

    public void castViewToBiggerX(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer cellMaintainer) {
        int k = ((y1 - y0) * Mathematics.PRECISION) / (x1 - x0);
        int b = y0 * Mathematics.PRECISION - k * x0;
        BattleCell[][] cells = battleMap.getCells();
        boolean hidden = false;
        if (y1 > y0) {
            for (int x = x0 + 1; x < x1 && !hidden; x++) {
                int y = ((k * (x * Mathematics.PRECISION /*- Mathematics.HALF_PRECISION*/)) / Mathematics.PRECISION + b);
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y/Mathematics.PRECISION);
/*
                hidden = markYPlusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    y = ((k * (x * Mathematics.PRECISION + Mathematics.HALF_PRECISION)) / Mathematics.PRECISION + b);
                    //hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y/Mathematics.PRECISION);
                    hidden = markYPlusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                }
*/
            }
            if(!hidden){
                cellMaintainer.maintain(cells, warrior, hidden, x1, y1);
            }
        } else if (y1 < y0) {
            for (int x = x0 + 1; x < x1 && !hidden; x++) {
                int y = ((k * (x * Mathematics.PRECISION /*- Mathematics.HALF_PRECISION*/)) / Mathematics.PRECISION + b);
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y/Mathematics.PRECISION);
/*
                hidden = markYMinusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    y = ((k * (x * Mathematics.PRECISION + Mathematics.HALF_PRECISION)) / Mathematics.PRECISION + b);
                    //hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y/Mathematics.PRECISION);
                    hidden = markYMinusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                }
*/
            }
            if(!hidden){
                cellMaintainer.maintain(cells, warrior, hidden, x1, y1);
            }
        } else {
            for (int x = x0 + 1; x <= x1 && !hidden; x++) {
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y0);
            }
        }
    }

    public void castViewToLesserX(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer cellMaintainer) {
        int k = ((y1 - y0) * Mathematics.PRECISION) / (x1 - x0);
        int b = y0 * Mathematics.PRECISION - k * x0;
        BattleCell[][] cells = battleMap.getCells();
        boolean hidden = false;
        if (y1 > y0) {
            for (int x = x0 - 1; x > x1 && !hidden; x--) {
                int y = ((k * (x * Mathematics.PRECISION /*+ Mathematics.HALF_PRECISION*/)) / Mathematics.PRECISION + b);
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y/Mathematics.PRECISION);
/*
                hidden = markYPlusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    y = ((k * (x * Mathematics.PRECISION - Mathematics.HALF_PRECISION)) / Mathematics.PRECISION + b);
                    //hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y/Mathematics.PRECISION);
                    hidden = markYPlusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                }
*/
            }
            if(!hidden){
                cellMaintainer.maintain(cells, warrior, hidden, x1, y1);
            }
        } else if (y1 < y0) {
            for (int x = x0 - 1; x > x1 && !hidden; x--) {
                int y = ((k * (x * Mathematics.PRECISION /*+ Mathematics.HALF_PRECISION*/)) / Mathematics.PRECISION + b);
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y/Mathematics.PRECISION);
/*
                hidden = markYMinusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    y = ((k * (x * Mathematics.PRECISION - Mathematics.HALF_PRECISION)) / Mathematics.PRECISION + b);
                    //hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y/Mathematics.PRECISION);
                    hidden = markYMinusVisible(cells, warrior, hidden, x, y, cellMaintainer);
                }
*/
            }
            if(!hidden){
                cellMaintainer.maintain(cells, warrior, hidden, x1, y1);
            }
        } else {
            for (int x = x0 - 1; x >= x1 && !hidden; x--) {
                hidden = cellMaintainer.maintain(cells, warrior, hidden, x, y0);
            }
        }
    }

}
