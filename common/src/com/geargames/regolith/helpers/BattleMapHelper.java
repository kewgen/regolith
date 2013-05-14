package com.geargames.regolith.helpers;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.map.PairAndElement;
import com.geargames.regolith.map.observer.LineViewCaster;
import com.geargames.regolith.map.observer.ShootBarriersFinder;
import com.geargames.regolith.units.map.CellElement;
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

    public static void putIn(CellElement element, BattleCell cell) {
        cell.addElement(element);
    }

    /**
     * Поднять вещь с ячейки карты cell.
     *
     * @param cell
     * @return вернуть поднятую вещь
     */
    public static CellElement putOut(BattleCell cell) {
        CellElement element = BattleCellHelper.getElementFromLayer(cell, CellElementLayers.TACKLE);
        if (element != null) {
            cell.removeElement(element);
        }
        return element;
    }

    /**
     * Получить объект заданного типа из ячейки cell.
     *
     * @param cell
     * @param type
     * @return null если тип не представлен в ячейке.
     */
    public static CellElement getElementByType(BattleCell cell, int type) {
        CellElement[] elements = cell.getElements();
        int length = cell.getSize();
        for (int i = length - 1; i >= 0; i--) {
            CellElement element = elements[i];
            if (element.getElementType() == type) {
                return element;
            }
        }
        return null;
    }

    /**
     * На карте, с проставленными из начальной точки (где стоит warrior) стоимостями достижимости, выбрать кратчайший путь
     * из точки (toX;toY) в начальную точку и отметить каждую ячейку этого пути, как часть кратчайшего пути.
     *
     * @param cells
     * @param toX
     * @param toY
     * @param warrior
     */
    public static void makeShortestRoute(BattleCell[][] cells, int toX, int toY, Warrior warrior) {
        int x = toX;
        int y = toY;
        int length = cells.length;
        while (true) {
            setShortestPathCell(cells[x][y], warrior);
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
     * Установить начальную точку пути для бойца warrior.
     *
     * @param warrior
     */
    public static void setZeroOrder(Warrior warrior, BattleCell[][] cells) {
        cells[warrior.getCellX()][warrior.getCellY()].setOrder((byte) 0);
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
     * Отметить ячейку cell как часть кратчайшего пути бойца warrior.
     *
     * @param warrior
     * @param cell
     */
    public static void setShortestPathCell(BattleCell cell, Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        cell.setOptimalPath(alliance, (short) (cell.getOptimalPath(alliance) | warrior.getNumber()));
    }

    /**
     * Является ли ячейка cell частью кротчайшего пути бойца warrior.
     *
     * @param warrior
     * @param cell
     * @return
     */
    public static boolean isShortestPathCell(BattleCell cell, Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        return (cell.getOptimalPath(alliance) & warrior.getNumber()) != 0;
    }

    public static boolean isEndPoint(BattleCell[][] cells, Pair pair, Warrior warrior) {
        int x = pair.getX();
        int y = pair.getY();
        int count = 0;
        if (isShortestPathCell(cells[x - 1][y], warrior)) {
            count++;
        }
        if (isShortestPathCell(cells[x - 1][y - 1], warrior)) {
            count++;
        }
        if (isShortestPathCell(cells[x][y - 1], warrior) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x + 1][y - 1], warrior) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x + 1][y], warrior) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x + 1][y + 1], warrior) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x][y + 1], warrior) && count < 2) {
            count++;
        }
        if (isShortestPathCell(cells[x - 1][y + 1], warrior) && count < 2) {
            count++;
        }
        return count == 1;
    }

    /**
     * Зачистить окрестность точки (x;y) от отметок кратчайшего пути бойца warrior.
     *
     * @param cells
     * @param warrior
     */
    public static void resetShortestPath(BattleCell[][] cells, Warrior warrior, BattleConfiguration battleConfiguration) {
        //todo-asap: исследовать как заполняется и чистится область достижимости, строится путь и прочее. Учесть передачу хода другому альянсу, смену активного бойца и др.
        int radius = WarriorHelper.getRoutableRadius(warrior, battleConfiguration); //todo: Использовать всегда максимальное значение радиуса
        int length = cells.length;
        int x0 = warrior.getCellX() - radius;
        x0 = x0 < 0 ? 0 : x0;
        int y0 = warrior.getCellY() - radius;
        y0 = y0 < 0 ? 0 : y0;
        int x1 = warrior.getCellX() + radius;
        x1 = x1 > length - 1 ? length - 1 : x1;
        int y1 = warrior.getCellY() + radius;
        y1 = y1 > length - 1 ? length - 1 : y1;
        for (int i = x0; i <= x1; i++) {
            for (int j = y0; j <= y1; j++) {
                resetShortestCell(cells[i][j], warrior);
            }
        }
    }

    /**
     * Снять отметку с ячейки cell о том, что она часть кратчайшего пути бойца с warrior.
     *
     * @param cell
     * @param warrior
     */
    public static void resetShortestCell(BattleCell cell, Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        cell.setOptimalPath(alliance, (short) (cell.getOptimalPath(alliance) & ~warrior.getNumber()));
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
     * Зачистить карту battleMap от маршрутов бойца warrior начиная с точки (x;y).
     *
     * @param warrior
     * @param x
     * @param y
     */
    public static void clearRoutes(BattleCell[][] cells, Warrior warrior, int x, int y, BattleConfiguration battleConfiguration) {
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
                cells[i][j].setOrder(UN_ROUTED);
            }
        }
    }

    /**
     * Показать ячейку cell бойцу warrior.
     * Заодно установим, что осматривали эту клетку карты и выполним некоторое действие если мы - первооткрыватели клетки.
     *
     * @param cell
     * @param warrior
     */
    public static void show(BattleCell cell, Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        cell.setVisibility(alliance, (short) (cell.getVisibility(alliance) | warrior.getNumber()));
        cell.setVisited(alliance, true);
    }

    /**
     * Скрыть ячейку cell от бойца warrior.
     *
     * @param cell
     * @param warrior
     */
    public static void hide(BattleCell cell, Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        cell.setVisibility(alliance, (byte) (cell.getVisibility(alliance) & (~warrior.getNumber())));
    }

    /**
     * Видима ли ячейка cell бойцом warrior.
     *
     * @param cell
     * @param warrior
     * @return
     */
    public static boolean isVisible(BattleCell cell, Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        return (cell.getVisibility(alliance) & warrior.getNumber()) != 0;
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
     * Скрыть все ячейки карты от бойца warrior.
     * Очистка проходит в пределах радиуса видимости бойца warrior.
     *
     * @param cells
     * @param warrior
     */
    public static void clearViewAround(BattleCell[][] cells, Warrior warrior) {
        int length = cells.length;
        int x = warrior.getCellX();
        int y = warrior.getCellY();
        int radius = WarriorHelper.getObservingRadius(warrior);
        SecurityOperationManager manager = warrior.getBattleGroup().getAccount().getSecurity();
        System.out.println("I am clearing a view of warrior " + warrior.getName());
        for (int i = x - radius; i <= 2 * radius + 1; i++) {
            if (i >= 0 && i < length) {
                for (int j = y - radius; j <= 2 * radius + 1; j++) {
                    if (j >= 0 && j < length) {
                        manager.adjustObserve(-(i + j));
                        BattleMapHelper.hide(cells[i][j], warrior);
                    }
                }
            }
        }
    }

    /**
     * Выгрузить бойца warrior с карты.
     *
     * @param warrior
     */
    public static void exitFromTheMap(Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        ExitZone exit = alliance.getExit();
        BattleCell[][] cells = alliance.getBattle().getMap().getCells();
        if (exit.isWithIn(warrior.getCellX(), warrior.getCellY())) {
            cells[warrior.getCellX()][warrior.getCellY()].removeElement(warrior); //todo: этого достаточно?
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


    /**
     * Вернуть расстояние между двумя точками карты.
     *
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @return
     */
    public static double getDistance(int x0, int y0, int x1, int y1) {
        return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
    }


    /**
     * Вернуть расстояние между двумя бойцами.
     *
     * @param unit0
     * @param unit1
     * @return
     */
    public static double getDistance(Warrior unit0, Warrior unit1) {
        return Math.sqrt((unit0.getCellX() - unit1.getCellX()) * (unit0.getCellX() - unit1.getCellX()) +
                (unit0.getCellY() - unit1.getCellY()) * (unit0.getCellY() - unit1.getCellY()));
    }


    /**
     * Вернёт преграду для выстрела которая закрывает бойца victim лучше всего и при этом ближе всего к hunter.
     * Если на пути нет преград, то вернутся координаты victim и сам объект victim.
     *
     * @param hunter
     * @param victim
     * @param map
     * @return
     */
    public static PairAndElement findShotBarrier(Warrior hunter, Warrior victim, BattleMap map) {
        int x0 = hunter.getCellX();
        int y0 = hunter.getCellY();

        int x1 = victim.getCellX();
        int y1 = victim.getCellY();

        ShootBarriersFinder finder = new ShootBarriersFinder();

        if (Math.abs(y1 - y0) <= Math.abs(x1 - x0)) {
            if (x1 > x0) {
                LineViewCaster.instance.castViewRight(x0, y0, x1, y1, map, hunter, finder);
            } else if (x1 < x0) {
                LineViewCaster.instance.castViewLeft(x0, y0, x1, y1, map, hunter, finder);
            }
        } else {
            if (y1 > y0) {
                LineViewCaster.instance.castViewDown(x0, y0, x1, y1, map, hunter, finder);
            } else if (y0 < y1) {
                LineViewCaster.instance.castViewUp(x0, y0, x1, y1, map, hunter, finder);
            }
        }
        return finder.getCoordinates();
    }

    public static boolean ableToPut(Warrior warrior, BattleCell[][] cells, short x, short y) {
        return isNear(warrior, x, y) && isAbleToWalkThrough(cells[x][y]) && !(BattleCellHelper.getElementFromLayer(cells[x][y], CellElementLayers.TACKLE) != null);
    }

    public static boolean ableToPeek(Warrior warrior, BattleCell[][] cells, short x, short y) {
        return isNear(warrior, x, y) && isAbleToWalkThrough(cells[x][y]) && BattleCellHelper.getElementFromLayer(cells[x][y], CellElementLayers.TACKLE) != null;
    }

    public static boolean isNear(Warrior warrior, short x, short y) {
        short xDifference = (short) (warrior.getCellX() - x);
        short yDifference = (short) (warrior.getCellY() - y);
        return xDifference < -1 || xDifference > 1 || yDifference < -1 || yDifference > 1;
    }

    /**
     * Поднять предмет с ячейки карты.
     *
     * @param warrior
     * @param x
     * @param y
     * @return
     */
    public static StateTackle pickUpStateTackle(Warrior warrior, BattleCell[][] cells, short x, short y) {
        if (isNear(warrior, x, y)) {
            return (StateTackle) putOut(cells[x][y]);
        }
        return null;
    }

    public static Medikit pickUpMedikit(Warrior warrior, BattleCell[][] cells, short x, short y) {
        if (isNear(warrior, x, y)) {
            return (Medikit) putOut(cells[x][y]);
        }
        return null;
    }

    public static Magazine pickUpMagazine(Warrior warrior, BattleCell[][] cells, short x, short y) {
        if (isNear(warrior, x, y)) {
            return (Magazine) putOut(cells[x][y]);
        }
        return null;
    }


    /**
     * Переместить предмет из одной точки на карте в другую (находящуюся рядом с бойцом warrior).
     *
     * @param warrior
     * @param cells
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean moveBetweenCells(Warrior warrior, BattleCell[][] cells, short x1, short y1, short x2, short y2) {
        if (ableToPeek(warrior, cells, x1, y1) && ableToPut(warrior, cells, x2, y2)) {
            CellElement element = putOut(cells[x1][y1]);
            putIn(element, cells[x2][y2]);
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

}
