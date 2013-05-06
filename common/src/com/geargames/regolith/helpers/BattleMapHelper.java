package com.geargames.regolith.helpers;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.dictionaries.HumanElementCollection;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.tackle.Magazine;
import com.geargames.regolith.units.tackle.Medikit;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * Users: mkutuzov, abarakov
 * Date: 19.02.12
 */
public class BattleMapHelper {
    private static final int VISIT_BIT = (1 << 7);
    public static final byte UN_ROUTED = 100;

    public static void putIn(CellElement element, BattleMap map, int x, int y) {
        BattleCell cell = map.getCells()[x][y];
        cell.addElement(element);
    }

    public static CellElement putOut(BattleMap map, int x, int y) {
        BattleCell cell = map.getCells()[x][y];
        CellElement element = cell.getElement();
        cell.addElement(null);
        return element;
    }

    /**
     * На карте, с проставленными из начальной точки (где стоит unit) стоимостями достижимости, выбрать кратчайший путь
     * из точки (toX;toY) в начальную точку и отметить каждую ячейку этого пути, как часть кратчайшего пути.
     *
     * @param cells
     * @param toX
     * @param toY
     * @param unit
     */
    public static void makeShortestRoute(BattleCell[][] cells, int toX, int toY, HumanElement unit) {
        int x = toX;
        int y = toY;
        int length = cells.length;
        while (true) {
            setShortestPathCell(cells[x][y], unit);
            byte order = getOrder(cells[x][y]);
            if (y - 1 >= 0 && getOrder(cells[x][y - 1]) + 1 == order) {
                y--;
            } else if (y + 1 < length && getOrder(cells[x][y + 1]) + 1 == order) {
                y++;
            } else if (x - 1 >= 0 && getOrder(cells[x - 1][y]) + 1 == order) {
                x--;
            } else if (x + 1 < length && getOrder(cells[x + 1][y]) + 1 == order) {
                x++;
            } else if (x - 1 >= 0 && y + 1 < length && getOrder(cells[x - 1][y + 1]) + 1 == order) {
                x--;
                y++;
            } else if (x + 1 < length && y + 1 < length && getOrder(cells[x + 1][y + 1]) + 1 == order) {
                x++;
                y++;
            } else if (x + 1 < length && y - 1 >= 0 && getOrder(cells[x + 1][y - 1]) + 1 == order) {
                x++;
                y--;
            } else if (x - 1 >= 0 && y - 1 >= 0 && getOrder(cells[x - 1][y - 1]) + 1 == order) {
                x--;
                y--;
            } else {
                break;
            }
        }
    }

    /**
     * Вернуть число означающее цену достижимости точки cell бойцом с индексом index в группе.
     *
     * @param cell
     * @return
     */
    public static byte getOrder(BattleCell cell) {
        return (byte) (cell.getOrder() & 127);
    }

    /**
     * Установить начальную точку пути для бойца unit.
     *
     * @param unit
     */
    public static void setZeroOrder(HumanElement unit) {
        unit.getHuman().getBattleGroup().getAlliance().getBattle().getMap().
                getCells()[unit.getCellX()][unit.getCellY()].setOrder((byte) 0);
    }

    /**
     * Установить наименьшее количество ходов, которое понадобится текущему бойцу для того, чтобы дойти до ячейки cell.
     *
     * @param cell
     * @param order
     * @return
     */
    public static boolean setOrder(BattleCell cell, byte order) {
        if (isAbleToWalkThrough(cell) && cell.getOrder() > order) {
            cell.setOrder(order);
            return true;
        }
        return false;
    }

    /**
     * Достижима ли сейчас точка cell карты текущим бойцом.
     *
     * @param cell
     * @return
     */
    public static boolean isReachable(BattleCell cell) {
        return cell.getOrder() != UN_ROUTED;
    }

    /**
     * Отметить ячейку cell как часть кратчайшего пути бойца unit.
     *
     * @param unit
     * @param cell
     */
    public static void setShortestPathCell(BattleCell cell, HumanElement unit) {
        BattleAlliance alliance = unit.getHuman().getBattleGroup().getAlliance();
        cell.setOptimalPath(alliance, (short) (cell.getOptimalPath(alliance) | unit.getHuman().getNumber()));
    }

    /**
     * Является ли ячейка cell частью кротчайшего пути бойца unit.
     *
     * @param unit
     * @param cell
     * @return
     */
    public static boolean isShortestPathCell(BattleCell cell, HumanElement unit) {
        BattleAlliance alliance = unit.getHuman().getBattleGroup().getAlliance();
        return (cell.getOptimalPath(alliance) & unit.getHuman().getNumber()) != 0;
    }

    public static boolean isEndPoint(BattleCell[][] cells, Pair pair, HumanElement unit) {
        int x = pair.getX();
        int y = pair.getY();
        int count = 0;
        if (isShortestPathCell(cells[x - 1][y], unit)) {
            count++;
        }
        if (isShortestPathCell(cells[x - 1][y - 1], unit)) {
            count++;
        }
        if (isShortestPathCell(cells[x][y - 1], unit) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x + 1][y - 1], unit) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x + 1][y], unit) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x + 1][y + 1], unit) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x][y + 1], unit) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x - 1][y + 1], unit) && count < 2) {
            count++;
        }
        return count == 1;
    }

    /**
     * Зачистить окрестность точки (x;y) от отметок кратчайшего пути бойца unit.
     *
     * @param cells
     * @param unit
     * @param x
     * @param y
     */
    public static void resetShortestPath(BattleCell[][] cells, HumanElement unit, int x, int y, BattleConfiguration battleConfiguration) {
        Warrior warrior = (Warrior) unit.getHuman();
        int radius = WarriorHelper.getRoutableRadius(warrior, battleConfiguration);
        int length = cells.length;
        int x0 = x - radius;
        x0 = x0 < 0 ? 0 : x0;
        int y0 = y - radius;
        y0 = y0 < 0 ? 0 : y0;
        int x1 = x + radius;
        x1 = x1 > length - 1 ? length - 1 : x1;
        int y1 = y + radius;
        y1 = y1 > length - 1 ? length - 1 : y1;
        for (int i = x0; i <= x1; i++) {
            for (int j = y0; j <= y1; j++) {
                resetShortestCell(cells[i][j], warrior);
            }
        }
    }

    /**
     * Снять отметку с ячейки cell о том, что она часть кратчайшего пути бойца с human.
     *
     * @param cell
     * @param human
     */
    public static void resetShortestCell(BattleCell cell, Human human) {
        BattleAlliance alliance = human.getBattleGroup().getAlliance();
        cell.setOptimalPath(alliance, (short) (cell.getOptimalPath(alliance) & ~human.getNumber()));
    }

    /**
     * Заполнить карту battleMap признаком не расчитанного пути.
     *
     * @param cells
     */
    public static void prepare(BattleCell[][] cells) {
        int length = cells.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                cells[i][j].setOrder(UN_ROUTED);
            }
        }
    }

    /**
     * Зачистить карту battleMap от маршрутов бойца unit начиная с точки (x;y).
     *
     * @param unit
     * @param x
     * @param y
     */
    public static void clearRoutes(BattleCell[][] cells, HumanElement unit, int x, int y, BattleConfiguration battleConfiguration) {
        int radius = WarriorHelper.getRoutableRadius((Warrior) unit.getHuman(), battleConfiguration);
        int length = cells.length;
        int x0 = x - radius;
        x0 = x0 < 0 ? 0 : x0;
        int y0 = y - radius;
        y0 = y0 < 0 ? 0 : y0;
        int x1 = x + radius;
        x1 = x1 > length - 1 ? length - 1 : x1;
        int y1 = y + radius;
        y1 = y1 > length - 1 ? length - 1 : y1;
        for (int i = x0; i <= x1; i++) {
            for (int j = y0; j <= y1; j++) {
                cells[i][j].setOrder(UN_ROUTED);
            }
        }
    }

    /**
     * Показать ячейку cell бойцу human.
     * Заодно установим, что осматривали эту клетку карты и выполним некоторое действие если мы - первооткрыватели клетки.
     *
     * @param cell
     * @param human
     */
    public static void show(BattleCell cell, Human human) {
        BattleAlliance alliance = human.getBattleGroup().getAlliance();
        cell.setVisibility(alliance, (short) (cell.getVisibility(alliance) | human.getNumber()));
        cell.setVisited(alliance, true);
    }

    /**
     * Скрыть ячейку cell от бойца human.
     *
     * @param cell
     * @param human
     */
    public static void hide(BattleCell cell, Human human) {
        BattleAlliance alliance = human.getBattleGroup().getAlliance();
        cell.setVisibility(alliance, (byte) (cell.getVisibility(alliance) & (~human.getNumber())));
    }

    /**
     * Видима ли ячейка cell бойцом human.
     *
     * @param cell
     * @param human
     * @return
     */
    public static boolean isVisible(BattleCell cell, Human human) {
        BattleAlliance alliance = human.getBattleGroup().getAlliance();
        return (cell.getVisibility(alliance) & human.getNumber()) != 0;
    }

    /**
     * Видима ли ячейка cell бойцами военного союза.
     *
     * @param cell
     * @return
     */
    public static boolean isVisible(BattleCell cell, BattleAlliance alliance) {
        return cell.getVisibility(alliance) != 0;
    }

    /**
     * Скрыть все ячейки карты от бойца unit.
     * Очистка проходит в пределах радиуса видимости бойца unit.
     *
     * @param cells
     * @param unit
     */
    public static void clearViewAround(BattleCell[][] cells, HumanElement unit) {
        int length = cells.length;
        int x = unit.getCellX();
        int y = unit.getCellY();
        int radius = WarriorHelper.getObservingRadius(unit);
        SecurityOperationManager manager = unit.getHuman().getBattleGroup().getAccount().getSecurity();
        System.out.println("I am clearing a view of warrior " + unit.getHuman().getName());
        for (int i = x - radius; i <= 2 * radius + 1; i++) {
            if (i >= 0 && i < length) {
                for (int j = y - radius; j <= 2 * radius + 1; j++) {
                    if (j >= 0 && j < length) {
                        manager.adjustObserve(-(i + j));
                        BattleMapHelper.hide(cells[i][j], unit.getHuman());
                    }
                }
            }
        }
    }

    /**
     * Выгрузить бойца unit с карты.
     *
     * @param unit
     */
    public static void exitFromTheMap(HumanElement unit) {
        BattleAlliance alliance = unit.getHuman().getBattleGroup().getAlliance();
        ExitZone exit = alliance.getExit();
        BattleCell[][] cells = alliance.getBattle().getMap().getCells();
        if (exit.isWithIn(unit.getCellX(), unit.getCellY())) {
            cells[unit.getCellX()][unit.getCellY()].removeElement(unit); //todo: этого достаточно?
        }
    }

    /**
     * Проверка размерности карты битвы.
     *
     * @param battle
     * @return
     */
    public static void verifyBattleSize(Battle battle, RegolithConfiguration regolithConfiguration) {
        BattleCell[][] cells = battle.getMap().getCells();
        if (cells.length < regolithConfiguration.getBattleConfiguration().getMinimalMapSize() || cells.length != cells[0].length) {
            throw new IllegalArgumentException();
        }
    }

    public static boolean ableToPut(HumanElement unit, BattleCell[][] cells, short x, short y) {
        return isNear(unit, x, y) && cells[x][y].getElement() == null; // isAbleToWalkThrough(cells[x][y])
    }

    public static boolean ableToPeek(HumanElement unit, BattleCell[][] cells, short x, short y) {
        return isNear(unit, x, y) && cells[x][y].getElement() != null; // !isAbleToWalkThrough(cells[x][y])
    }

    public static boolean isNear(HumanElement unit, short x, short y) {
        short xDifference = (short) (unit.getCellX() - x);
        short yDifference = (short) (unit.getCellY() - y);
        return xDifference < -1 || xDifference > 1 || yDifference < -1 || yDifference > 1;
    }

    /**
     * Убрать предмет с ячейки карты.
     *
     * @param unit
     * @param x
     * @param y
     * @return
     */
    //todo: Неверный коментарий и название функции
    public static StateTackle peekStateTackle(HumanElement unit, BattleCell[][] cells, short x, short y) {
        if (isNear(unit, x, y)) {
            return (StateTackle) cells[x][y].getElement();
        }
        return null;
    }

    public static Medikit peekMedikit(HumanElement unit, BattleCell[][] cells, short x, short y) {
        if (isNear(unit, x, y)) {
            return (Medikit) cells[x][y].getElement();
        }
        return null;
    }

    public static Magazine peekMagazine(HumanElement unit, BattleCell[][] cells, short x, short y) {
        if (isNear(unit, x, y)) {
            return (Magazine) cells[x][y].getElement();
        }
        return null;
    }


    /**
     * Переместить предмет из одной точки на карте в другую (находящуюся рядом с бойцом unit).
     *
     * @param unit
     * @param cells
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean moveBetweenCells(HumanElement unit, BattleCell[][] cells, short x1, short y1, short x2, short y2) {
        if (isNear(unit, x1, y1) && isNear(unit, x2, y2) && cells[x2][y2].getElement() == null) {
            cells[x2][y2].addElement(cells[x1][y1].getElement());
            cells[x1][y1].addElement(null); //todo: remove()
            return true;
        } else {
            return false;
        }
    }

    /**
     * Вернет true, если через ячейку можно видеть.
     *
     * @return
     */
    public static boolean isAbleToLookThrough(BattleCell cell) {
        CellElement[] elements = cell.getElements();
        for (int i = cell.getSize() - 1; i >= 0; i--) {
            if (!elements[i].isAbleToLookThrough()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если через ячейку можно ходить.
     *
     * @return
     */
    public static boolean isAbleToWalkThrough(BattleCell cell) {
        CellElement[] elements = cell.getElements();
        for (int i = cell.getSize() - 1; i >= 0; i--) {
            if (!elements[i].isAbleToWalkThrough()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если ячейка является простреливаемой оружием данного вида.
     *
     * @return
     */
    public static boolean isAbleToShootThrough(BattleCell cell, WeaponCategory weaponCategory) {
        CellElement[] elements = cell.getElements();
        for (int i = cell.getSize() - 1; i >= 0; i--) {
            if (!elements[i].isAbleToShootThrough(weaponCategory)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если на ячейке карты есть элемент-барьер.
     *
     * @return
     */
    public static boolean isBarrier(BattleCell cell) {
        CellElement[] elements = cell.getElements();
        for (int i = cell.getSize() - 1; i >= 0; i--) {
            if (elements[i].isBarrier()) {
                return true;
            }
        }
        return false;
    }

    public static HumanElement getHumanElementByHuman(HumanElementCollection units, Human human) throws RegolithException {
        for (int i = 0; i < units.size(); i++) {
            HumanElement unit = units.get(i);
            if (human == unit.getHuman()) {
                return unit;
            }
        }
        throw new RegolithException("Human element for a warrior was not found (id = " + human.getId() + ")");
    }

    public static HumanElement getHumanElementByHumanId(HumanElementCollection units, int humanId) throws RegolithException {
        for (int i = 0; i < units.size(); i++) {
            HumanElement unit = units.get(i);
            if (humanId == unit.getHuman().getId()) {
                return unit;
            }
        }
        throw new RegolithException("Human element for a warrior was not found (id = " + humanId + ")");
    }

}
