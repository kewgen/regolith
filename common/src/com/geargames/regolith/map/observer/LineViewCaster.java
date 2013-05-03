package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.HumanElement;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public class LineViewCaster extends ViewCaster {
    public static LineViewCaster instance = new LineViewCaster();

    private boolean markXPlusVisible(BattleCell[][] cells, HumanElement unit, boolean hidden, double x, int y, BattleCellMaintainer cellMaintainer) {
        int xx = (int) x;
        xx = xx + 0.5 < x ? xx + 1 : xx;
        if (xx >= 0 && xx < cells.length) {
            hidden = cellMaintainer.maintain(cells, unit, hidden, xx, y);
        }
        return hidden;
    }

    private boolean markXMinusVisible(BattleCell[][] cells, HumanElement unit, boolean hidden, double x, int y, BattleCellMaintainer cellMaintainer) {
        int xx = (int) x;
        xx = xx - 0.5 < x ? xx : xx - 1;
        if (xx >= 0 && xx < cells.length) {
            hidden = cellMaintainer.maintain(cells, unit, hidden, xx, y);
        }
        return hidden;
    }

    public void castViewUp(int x0, int y0, int x1, int y1, BattleMap battleMap, HumanElement unit, BattleCellMaintainer cellMaintainer) {
        double k = 0;
        if (x1 != x0) {
            k = (double) (y1 - y0) / (double) (x1 - x0);
        }
        double b = (double) y0 - k * x0;
        BattleCell[][] cells = battleMap.getCells();
        int length = cells.length;
        boolean hidden = false;

        if (x1 > x0) {
            for (int y = y0 - 1; y >= y1 && y >= 0 && !hidden; y--) {
                double x = (y + 0.5 - b) / k;
                hidden = markXPlusVisible(cells, unit, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    x = (y - 0.5 - b) / k;
                    hidden = markXPlusVisible(cells, unit, hidden, x, y, cellMaintainer);
                }
            }
        } else if (x1 < x0) {
            for (int y = y0 - 1; y >= y1 && y >= 0 && !hidden; y--) {
                double x = (y + 0.5 - b) / k;
                hidden = markXMinusVisible(cells, unit, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    x = (y - 0.5 - b) / k;
                    hidden = markXMinusVisible(cells, unit, hidden, x, y, cellMaintainer);
                }
            }
        } else {
            for (int y = y0 - 1; y >= y1 && y >= 0 && !hidden; y--) {
                hidden = cellMaintainer.maintain(cells, unit, hidden, x0, y);
            }
        }
    }


    public void castViewDown(int x0, int y0, int x1, int y1, BattleMap battleMap, HumanElement unit, BattleCellMaintainer cellMaintainer) {
        double k = 0;
        if (x1 != x0) {
            k = (double) (y1 - y0) / (double) (x1 - x0);
        }
        double b = (double) y0 - k * x0;
        BattleCell[][] cells = battleMap.getCells();
        int length = cells.length;
        boolean hidden = false;

        if (x0 < x1) {
            for (int y = y0 + 1; y <= y1 && y < length && !hidden; y++) {
                double x = (y - 0.5 - b) / k;
                hidden = markXPlusVisible(cells, unit, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    x = (y + 0.5 - b) / k;
                    hidden = markXPlusVisible(cells, unit, hidden, x, y, cellMaintainer);
                }
            }
        } else if (x0 > x1) {
            for (int y = y0 + 1; y <= y1 && y < length && !hidden; y++) {
                double x = (y - 0.5 - b) / k;
                hidden = markXMinusVisible(cells, unit, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    x = (y + 0.5 - b) / k;
                    hidden = markXMinusVisible(cells, unit, hidden, x, y, cellMaintainer);
                }
            }
        } else {
            for (int y = y0 + 1; y <= y1 && y < length && !hidden; y++) {
                hidden = cellMaintainer.maintain(cells, unit, hidden, x0, y);
            }
        }
    }


    private boolean markYPlusVisible(BattleCell[][] cells, HumanElement unit, boolean hidden, int x, double y, BattleCellMaintainer cellMaintainer) {
        int yy = (int) y;
        yy = yy + 0.5 < y ? yy + 1 : yy;
        if (yy >= 0 && yy < cells.length) {
            hidden = cellMaintainer.maintain(cells, unit, hidden, x, yy);
        }
        return hidden;
    }

    private boolean markYMinusVisible(BattleCell[][] cells, HumanElement unit, boolean hidden, int x, double y, BattleCellMaintainer cellMaintainer) {
        int yy = (int) y;
        yy = yy - 0.5 < y ? yy : yy - 1;
        if (yy >= 0 && yy < cells.length) {
            hidden = cellMaintainer.maintain(cells, unit, hidden, x, yy);
        }
        return hidden;
    }

    public void castViewRight(int x0, int y0, int x1, int y1, BattleMap battleMap, HumanElement unit, BattleCellMaintainer cellMaintainer) {
        double k = (double) (y1 - y0) / (double) (x1 - x0);
        double b = (double) y0 - k * x0;
        BattleCell[][] cells = battleMap.getCells();
        int length = cells.length;
        boolean hidden = false;
        if (y1 > y0) {
            for (int x = x0 + 1; x <= x1 && x < length && !hidden; x++) {
                double y = k * (x - 0.5) + b;
                hidden = markYPlusVisible(cells, unit, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    y = k * (x + 0.5) + b;
                    hidden = markYPlusVisible(cells, unit, hidden, x, y, cellMaintainer);
                }
            }
        } else if (y1 < y0) {
            for (int x = x0 + 1; x <= x1 && x < length && !hidden; x++) {
                double y = k * (x - 0.5) + b;
                hidden = markYMinusVisible(cells, unit, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    y = k * (x + 0.5) + b;
                    hidden = markYMinusVisible(cells, unit, hidden, x, y, cellMaintainer);
                }
            }
        } else {
            for (int x = x0 + 1; x <= x1 && x < length && !hidden; x++) {
                hidden = cellMaintainer.maintain(cells, unit, hidden, x, y0);
            }
        }
    }

    public void castViewLeft(int x0, int y0, int x1, int y1, BattleMap battleMap, HumanElement unit, BattleCellMaintainer cellMaintainer) {
        double k = (double) (y1 - y0) / (double) (x1 - x0);
        double b = (double) y0 - k * x0;
        BattleCell[][] cells = battleMap.getCells();
        boolean hidden = false;
        if (y1 > y0) {
            for (int x = x0 - 1; x >= x1 && x >= 0 && !hidden; x--) {
                double y = k * (x + 0.5) + b;
                hidden = markYPlusVisible(cells, unit, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    y = k * (x - 0.5) + b;
                    hidden = markYPlusVisible(cells, unit, hidden, x, y, cellMaintainer);
                }
            }
        } else if (y1 < y0) {
            for (int x = x0 - 1; x >= x1 && x >= 0 && !hidden; x--) {
                double y = k * (x + 0.5) + b;
                hidden = markYMinusVisible(cells, unit, hidden, x, y, cellMaintainer);
                if (!hidden) {
                    y = k * (x - 0.5) + b;
                    hidden = markYMinusVisible(cells, unit, hidden, x, y, cellMaintainer);
                }
            }
        } else {
            for (int x = x0 - 1; x >= x1 && x >= 0 && !hidden; x--) {
                hidden = cellMaintainer.maintain(cells, unit, hidden, x, y0);
            }
        }
    }

}
