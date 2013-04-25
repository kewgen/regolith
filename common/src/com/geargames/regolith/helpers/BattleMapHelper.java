package com.geargames.regolith.helpers;

import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.ExitZone;
import com.geargames.regolith.units.tackle.Magazine;
import com.geargames.regolith.units.tackle.Medikit;
import com.geargames.regolith.units.tackle.StateTackle;


/**
 * User: mkutuzov
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
     * На карте, с проставленными из начальной точки(где стоит warrior) стоимостями достижимости, выбрать кратчайший путь
     * из точки toX toY в начальную точку и отметить каждую ячейку этого пути как часть кратчайшего пути.
     *
     * @param toX
     * @param toY
     * @param warrior
     */
    public static void makeShortestRoute(int toX, int toY, Warrior warrior) {
        BattleCell[][] cells = warrior.getBattleGroup().getAlliance().getBattle().getMap().getCells();
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
    public static void setZeroOrder(Warrior warrior) {
        warrior.getBattleGroup().getAlliance().getBattle().getMap().
                getCells()[warrior.getX()][warrior.getY()].setOrder((byte) 0);
    }

    /**
     * Установить наименьшее количество ходов, которое понадобится текущему бойцу для того,
     * чтоб дойти до ячейки cell.
     *
     * @param cell
     * @param order
     * @return
     */
    public static boolean setOrder(BattleCell cell, byte order) {
        if ((cell.getElement() == null || cell.getElement().isAbleToWalkThrough()) && cell.getOrder() > order) {
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
     * Является ли ячейка cell частью кратчайшего пути бойца warrior.
     *
     * @param warrior
     * @param cell
     * @return
     */
    public static boolean isShortestPathCell(BattleCell cell, Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        return (cell.getOptimalPath(alliance) & warrior.getNumber()) != 0;
    }

    public static boolean isEndPoint(Pair pair, Warrior warrior) {
        int x = pair.getX();
        int y = pair.getY();
        BattleCell[][] cells = warrior.getBattleGroup().getAlliance().getBattle().getMap().getCells();
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
     * Зачистить окрестность точки x y  от отметок кратчайшего пути пользователя warrior.
     *
     * @param warrior
     * @param x
     * @param y
     */
    public static void resetShortestPath(Warrior warrior, int x, int y) {
        int radius = WarriorHelper.getRoutableRadius(warrior);
        BattleCell[][] cells = warrior.getBattleGroup().getAlliance().getBattle().getMap().getCells();
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
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
                resetShortestCell(cells[i][j], alliance, warrior);
            }
        }
    }

    /**
     * Снять отметку с ячейки cell о том что она часть кротчайшего пути бойца с warrior.
     *
     * @param warrior
     * @param cell
     */
    public static void resetShortestCell(BattleCell cell, BattleAlliance alliance, Warrior warrior) {
        cell.setOptimalPath(alliance, (short) (cell.getOptimalPath(alliance) & ~warrior.getNumber()));
    }

    /**
     * Заполнить карту battleMap признаком не расчитанного пути.
     *
     * @param battleMap
     */
    public static void prepare(BattleMap battleMap) {
        BattleCell[][] cells = battleMap.getCells();
        int length = cells.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                cells[i][j].setOrder(UN_ROUTED);
            }
        }
    }

    /**
     * Зачистить карту battleMap от маршрутов бойца warrior начиная с точки x;y.
     *
     * @param warrior
     * @param x
     * @param y
     */
    public static void clearRoutes(Warrior warrior, int x, int y) {
        int radius = WarriorHelper.getRoutableRadius(warrior);
        BattleCell[][] cells = warrior.getBattleGroup().getAlliance().getBattle().getMap().getCells();
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
    public static void show(BattleCell cell, Ally warrior) {
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
     * Видима ли ячейка cell бойцом отряда warrior.
     *
     * @param cell
     * @param warrior
     * @return
     */
    public static boolean isAimed(BattleCell cell, Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        return (cell.getVisibility(alliance) & warrior.getNumber()) != 0;
    }

    /**
     * Видима ли ячеейка cell бойцами военного союза .
     *
     * @param cell
     * @return
     */
    public static boolean isVisible(BattleCell cell, BattleAlliance alliance) {
        return cell.getVisibility(alliance) != 0;
    }

    /**
     * Определить находится ли точка на границе видимости для союза alliance в точке x:y.
     * @param cells массив ячеек карты
     * @param x первая координата в массиве ячеек
     * @param y вторая координата в массиве ячеек
     * @param alliance
     * @return
     */
    public static boolean isVisibilityTerminator(BattleCell[][] cells, int x, int y, BattleAlliance alliance){
        if(isVisible(cells[x][y], alliance)){
            //проверяем
            return true;
        }else{
            return false;
        }
    }

    /**
     * Скрыть все ячейки карты от бойца warrior.
     * Очистка проходит в пределах радиуса видимости бойца warrior.
     *
     * @param warrior
     */
    public static void clearViewAround(Warrior warrior) {
        BattleMap map = warrior.getBattleGroup().getAlliance().getBattle().getMap();
        BattleCell[][] cells = map.getCells();
        int length = cells.length;
        int x = warrior.getX();
        int y = warrior.getY();
        int radius = WarriorHelper.getObservingRadius(warrior);
        SecurityOperationManager manager = warrior.getBattleGroup().getAccount().getSecurity();
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
     * Выгрузить игрока warrior с карты.
     *
     * @param warrior
     */
    public static void exitFromTheMap(Warrior warrior) {
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        ExitZone exit = alliance.getExit();
        BattleCell[][] cells = alliance.getBattle().getMap().getCells();
        if (exit.isWithIn(warrior.getX(), warrior.getY())) {
            cells[warrior.getX()][warrior.getY()] = null;
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


    public static boolean ableToPut(Warrior warrior, BattleCell[][] cells, short x, short y) {
        return isNear(warrior, x, y) && cells[x][y].getElement() == null;
    }

    public static boolean ableToPeek(Warrior warrior, BattleCell[][] cells, short x, short y) {
        return isNear(warrior, x, y) && cells[x][y].getElement() != null;
    }

    public static boolean isNear(Warrior warrior, short x, short y) {
        short xDifference = (short) (warrior.getX() - x);
        short yDifference = (short) (warrior.getY() - y);
        return xDifference < -1 || xDifference > 1 || yDifference < -1 || yDifference > 1;
    }

    /**
     * Убрать предмет с ячейки карты.
     *
     * @param warrior
     * @param x
     * @param y
     * @return
     */
    public static StateTackle peekStateTackle(Warrior warrior, BattleCell[][] cells, short x, short y) {
        if (isNear(warrior, x, y)) {
            return (StateTackle) cells[x][y].getElement();
        }
        return null;
    }

    public static Medikit peekMedikit(Warrior warrior, BattleCell[][] cells, short x, short y) {
        if (isNear(warrior, x, y)) {
            return (Medikit) cells[x][y].getElement();
        }
        return null;
    }

    public static Magazine peekMagazine(Warrior warrior, BattleCell[][] cells, short x, short y) {
        if (isNear(warrior, x, y)) {
            return (Magazine) cells[x][y].getElement();
        }
        return null;
    }


    /**
     * Переместить предмет из одной точки на катре в другую(находящуюся рядом с бойцом warrior).
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
        if (isNear(warrior, x1, y1) && isNear(warrior, x2, y2) && cells[x2][y2].getElement() == null) {
            cells[x2][y2].addElement(cells[x1][y1].getElement());
            cells[x1][y1].addElement(null);
            return true;
        } else {
            return false;
        }
    }
}

