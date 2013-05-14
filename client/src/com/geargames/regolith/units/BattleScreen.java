package com.geargames.regolith.units;

import com.geargames.awt.Screen;
import com.geargames.common.env.Environment;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.DataMessageListener;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.common.packer.PSprite;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.util.Mathematics;
import com.geargames.common.Graphics;
import com.geargames.regolith.*;
import com.geargames.regolith.application.Event;
import com.geargames.regolith.awt.components.ElementActionMenuDrawablePPanel;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.*;

/**
 * Users: mkutuzov, abarakov
 * Date: 13.02.12
 */
public class BattleScreen extends Screen implements TimerListener, DataMessageListener {
    private MapCorrector corrector;
    private Finder cellFinder;       // Определяет номер клетки из коорддинат на карте
    private Finder coordinateFinder; // Переводит номер клетки в координаты центра клетки на карте

    private int mapX;
    private int mapY;

    private int touchedX;
    private int touchedY;
    private int backupMapX;
    private int backupMapY;

    //todo: Избавиться от 4 x Pair
    private Pair topCenter;
    private Pair centerRight;
    private Pair bottomCenter;
    private Pair centerLeft;

    private int b1;
    private int b2;
    private int b3;
    private int b4;

    private ClientConfiguration configuration;
    private ClientBattleContext battleContext;

    private short[] listenedTypes;
    private int logicUpdateTimerId; // Таймер для апдейта логики всех динамических элементов на карте
    private int finishTurnTimerId;  // Таймер сигнализирующий о завершении хода, может срабатывать раньше, чем соответствующее сообщение придет от сервера

    private PSprite groundSprite;
    private PSprite shadowSprite;
    private PSprite reachableCellSprite;
    private PSprite unreachableCellSprite;
    private PObject symbolOfProtectionObject;
    private PObject iconHeightOfBarriersObject;

    private ElementActionMenuDrawablePPanel actionMenuWindow; // Панелька с кнопками для выполнения действий над выделенным элементом карты

    public BattleScreen() {
        logicUpdateTimerId = TimerManager.NULL_TIMER;
        finishTurnTimerId = TimerManager.NULL_TIMER;
        configuration = ClientConfigurationFactory.getConfiguration();
        battleContext = configuration.getBattleContext();
        listenedTypes = new short[]{Packets.MOVE_ALLY, Packets.MOVE_ENEMY, Packets.SHOOT, Packets.CHANGE_ACTIVE_ALLIANCE,
                Packets.INITIALLY_OBSERVED_ENEMIES, Packets.BATTLE_SERVICE_NEW_CLIENT_LOGIN};
        cellFinder = configuration.getCellFinder();
        coordinateFinder = configuration.getCoordinateFinder();

        groundSprite = Environment.getRender().getSprite(Graph.SPR_GROUND);
        shadowSprite = Environment.getRender().getSprite(Graph.SPR_SHADOW);
        reachableCellSprite = Environment.getRender().getSprite(Graph.SPR_DISTANCE);
        unreachableCellSprite = Environment.getRender().getSprite(Graph.SPR_DISTANCE + 1);
        symbolOfProtectionObject = Environment.getRender().getObject(Graph.OBJ_BAR + 2);
        iconHeightOfBarriersObject = Environment.getRender().getObject(Graph.OBJ_BAR + 3);
    }

    public void draw(Graphics graphics) {
        drawGround(graphics);
        drawBattleMap(graphics);
    }

    /**
     * Прорисовываем содержимое клетки и изометрическую клетку карты, по координатам её центра (x;y) на
     * графическом контексте graphics.
     *
     * @param graphics
     * @param x
     * @param y
     * @param cell
     */
    //todo: Аргументы cellX и cellY здесь временно, незабыть убрать
    private void drawCell(Graphics graphics, int x, int y, BattleCell cell, short cellX, short cellY) {
        if (battleContext.isMyTurn()) {
            boolean isReachableCell = cell.getOrder() != BattleMapHelper.UN_ROUTED;
            if (isReachableCell) {
                reachableCellSprite.draw(graphics, x, y);
            } else {
//                if (BattleMapHelper.isBarrier(cell)) {
//                    unreachableCellSprite.draw(graphics, x, y);
//                }
            }
            DynamicCellElement element = battleContext.getSelectedElement();
            if (element != null && battleContext.getSelectedElementCellX() == cellX && battleContext.getSelectedElementCellY() == cellY) {
                unreachableCellSprite.draw(graphics, x, y);
            }
            if (BattleMapHelper.isShortestPathCell(cell, battleContext.getActiveUnit())) {
                shadowSprite.draw(graphics, x - 29/*width*/, y - 20/*height*/);
            }
            if (isReachableCell) {
                graphics.drawString("" + cell.getOrder(), x, y, com.geargames.common.Graphics.HCENTER);
            }
        }
//        final byte BARRIER_NONE = 0;
//        final byte BARRIER_HALF_HEIGHT = 1;
//        final byte BARRIER_FULL_HEIGHT = 2;
//        byte barrierType = BARRIER_NONE;
        CellElement[] elements = cell.getElements();
        for (int i = 0; i < cell.getSize(); i++) {
            CellElement element = elements[i];
            if (element.getElementType() == CellElementTypes.HUMAN) {
                drawSymbolOfProtection(graphics, (ClientWarriorElement) element);
            }
            ((DrawableElement) element).draw(graphics, x, y);
//            if (element.isBarrier()) {
//                if (!element.isHalfLong()) {
//                    barrierType = BARRIER_FULL_HEIGHT;
//                } else if (barrierType == BARRIER_NONE) {
//                    barrierType = BARRIER_HALF_HEIGHT;
//                }
//            }
        }
        /*
        if (battleContext.isMyTurn()) {
            switch (barrierType) {
                case BARRIER_FULL_HEIGHT: {
                    Index index = iconHeightOfBarriersObject.getIndexBySlot(0);
                    index.draw(graphics, x, y);
                    break;
                }
                case BARRIER_HALF_HEIGHT: {
                    Index index = iconHeightOfBarriersObject.getIndexBySlot(1);
                    index.draw(graphics, x, y);
                    break;
                }
            }
        }
        */
    }

    private class Dir {
        public static final byte NONE = 0;         // без направления
        public static final byte NORTH = 1;        // север
        public static final byte NORTHEAST = 2;    // северо-восток
        public static final byte EAST = 4;         // восток
        public static final byte SOUTHEAST = 8;    // юго-восток
        public static final byte SOUTH = 16;       // юг
        public static final byte SOUTHWEST = 32;   // юго-запад
        public static final byte WEST = 64;        // запад
        public static final byte NORTHWEST = -128; // северо-запад
    }

    /**
     * Рисуем индикатор защиты бойца.
     *
     * @param graphics
     */

    private void drawSymbolOfProtection(Graphics graphics, ClientWarriorElement warrior) {
        if (battleContext.isMyTurn() && battleContext.getActiveUnit() == warrior) {
            byte barrierBits = Dir.NONE;
            BattleCell[][] battleCells = battleContext.getBattle().getMap().getCells();
            int sizeMinusOne = battleCells.length - 1;
            short x = warrior.getCellX();
            short y = warrior.getCellY();
            if (x > 0 && y > 0 && BattleMapHelper.isBarrier(battleCells[x - 1][y - 1])) {
                barrierBits |= Dir.NORTHWEST;
            }
            if (y > 0 && BattleMapHelper.isBarrier(battleCells[x][y - 1])) {
                barrierBits |= Dir.WEST;
            }
            if (x < sizeMinusOne && y > 0 && BattleMapHelper.isBarrier(battleCells[x + 1][y - 1])) {
                barrierBits |= Dir.SOUTHWEST;
            }
            if (x < sizeMinusOne && BattleMapHelper.isBarrier(battleCells[x + 1][y])) {
                barrierBits |= Dir.SOUTH;
            }
            if (x < sizeMinusOne && y < sizeMinusOne && BattleMapHelper.isBarrier(battleCells[x + 1][y + 1])) {
                barrierBits |= Dir.SOUTHEAST;
            }
            if (y < sizeMinusOne && BattleMapHelper.isBarrier(battleCells[x][y + 1])) {
                barrierBits |= Dir.EAST;
            }
            if (x > 0 && y < sizeMinusOne && BattleMapHelper.isBarrier(battleCells[x - 1][y + 1])) {
                barrierBits |= Dir.NORTHEAST;
            }
            if (x > 0 && BattleMapHelper.isBarrier(battleCells[x - 1][y])) {
                barrierBits |= Dir.NORTH;
            }
            Index index = null;
            if ((barrierBits & (Dir.WEST | Dir.NORTHWEST | Dir.NORTH)) == (Dir.WEST | Dir.NORTHWEST | Dir.NORTH)) {
                index = symbolOfProtectionObject.getIndexBySlot(1);
            } else if ((barrierBits & (Dir.NORTH | Dir.NORTHEAST | Dir.EAST)) == (Dir.NORTH | Dir.NORTHEAST | Dir.EAST)) {
                index = symbolOfProtectionObject.getIndexBySlot(3);
            } else if ((barrierBits & (Dir.EAST | Dir.SOUTHEAST | Dir.SOUTH)) == (Dir.EAST | Dir.SOUTHEAST | Dir.SOUTH)) {
                index = symbolOfProtectionObject.getIndexBySlot(5);
            } else if ((barrierBits & (Dir.SOUTH | Dir.SOUTHWEST | Dir.WEST)) == (Dir.SOUTH | Dir.SOUTHWEST | Dir.WEST)) {
                index = symbolOfProtectionObject.getIndexBySlot(7);
            } else if ((barrierBits & Dir.NORTH) == Dir.NORTH) {
                index = symbolOfProtectionObject.getIndexBySlot(2);
            } else if ((barrierBits & Dir.EAST) == Dir.EAST) {
                index = symbolOfProtectionObject.getIndexBySlot(4);
            } else if ((barrierBits & Dir.SOUTH) == Dir.SOUTH) {
                index = symbolOfProtectionObject.getIndexBySlot(6);
            } else if ((barrierBits & Dir.WEST) == Dir.WEST) {
                index = symbolOfProtectionObject.getIndexBySlot(8);
            }
            if (index != null) {
                Pair pair = coordinateFinder.find(warrior.getCellX(), warrior.getCellY(), this);
                index.draw(graphics, pair.getX() - mapX, pair.getY() - mapY);
            }
        }
    }

    private void drawBattleMap(Graphics graphics) {
        BattleCell[][] cells = battleContext.getBattle().getMap().getCells();
        int size = cells.length;
        for (short xCell = 0; xCell < size; xCell++) {
            int x = (size - 1 - xCell) * ClientBattleContext.HORIZONTAL_RADIUS;
            int y = xCell * ClientBattleContext.VERTICAL_RADIUS;
            for (short yCell = 0; yCell < size; yCell++) {
                if (isOnTheScreen(x, y)) {
                    drawCell(graphics, x - mapX, y - mapY, cells[xCell][yCell], xCell, yCell);
                }
                x += ClientBattleContext.HORIZONTAL_RADIUS;
                y += ClientBattleContext.VERTICAL_RADIUS;
            }
        }
    }

    private void drawGround(Graphics graphics) {
        int x = -mapX % ClientBattleContext.GROUND_WIDTH - ClientBattleContext.HORIZONTAL_RADIUS * 3; //todo-asap: Использовать getMapMinX()
        int tmp = -mapY % ClientBattleContext.GROUND_HEIGHT - ClientBattleContext.VERTICAL_RADIUS * 5; //todo-asap: Использовать getMapMinY()
        while (x < getWidth()) {
            int y = tmp;
            while (y < getHeight()) {
                groundSprite.draw(graphics, x, y);
                y += ClientBattleContext.GROUND_HEIGHT;
            }
            x += ClientBattleContext.GROUND_WIDTH;
        }
        // Рисуем границу вокруг игрового поля
        graphics.setColor(0x0000FF);
        BattleCell[][] battleCells = battleContext.getBattle().getMap().getCells();
        int sizeMinusOne = battleCells.length - 1;
        int[] points = new int[8];
        Pair pair = coordinateFinder.find(0, 0, this);
        points[0] = pair.getX() - mapX;
        points[1] = pair.getY() - ClientBattleContext.VERTICAL_RADIUS - mapY;
        pair = coordinateFinder.find(0, sizeMinusOne, this);
        points[2] = pair.getX() + ClientBattleContext.HORIZONTAL_RADIUS - mapX;
        points[3] = pair.getY() - mapY;
        pair = coordinateFinder.find(sizeMinusOne, sizeMinusOne, this);
        points[4] = pair.getX() - mapX;
        points[5] = pair.getY() + ClientBattleContext.VERTICAL_RADIUS - mapY;
        pair = coordinateFinder.find(sizeMinusOne, 0, this);
        points[6] = pair.getX() - ClientBattleContext.HORIZONTAL_RADIUS - mapX;
        points[7] = pair.getY() - mapY;
        graphics.drawLine(points[0], points[1], points[2], points[3]);
        graphics.drawLine(points[2], points[3], points[4], points[5]);
        graphics.drawLine(points[4], points[5], points[6], points[7]);
        graphics.drawLine(points[6], points[7], points[0], points[1]);
        graphics.setColor(0xFFFFFF);
    }

    /**
     * Переместить центр экрана к координате карты.
     *
     * @param x координата карты
     * @param y координата карты
     */
    public void setCenter(int x, int y) {
        Pair pair = corrector.correct(x - (getWidth() / 2), y - (getHeight() / 2), this);
        scrollingMapTo(pair.getX(), pair.getY());
    }

    /**
     * Центрировать область просмотра на ячейке карты.
     */
    public void displayCell(int cellX, int cellY) {
        Pair pair = coordinateFinder.find(cellX, cellY, this);
        setCenter(pair.getX(), pair.getY());
    }

    /**
     * Центрировать область просмотра на бойце unit.
     *
     * @param unit
     */
    public void displayWarrior(ClientWarriorElement unit) {
        displayCell(unit.getCellX(), unit.getCellY());
    }

    private void callTickForCollection(ClientWarriorCollection collection) {
        for (int i = 0; i < collection.size(); i++) {
            ((ClientWarriorElement) collection.get(i)).onTick();
        }
    }

    public void onTimer(int timerId) {
        if (timerId == logicUpdateTimerId) {
            callTickForCollection(battleContext.getGroupUnits());
            callTickForCollection(battleContext.getAllyUnits());
            callTickForCollection(battleContext.getEnemyUnits());
        } else if (timerId == finishTurnTimerId) {
            finishTurnTimerId = TimerManager.NULL_TIMER;
            doChangeActiveAlliance(null);
        }
    }

    public boolean onEvent(int code, int param, int x, int y) {
        switch (code) {
            case Event.EVENT_TOUCH_PRESSED:
                touchedX = x;
                touchedY = y;
                backupMapX = mapX;
                backupMapY = mapY;
//                Debug.debug("BattleScreen.onEvent(): code = Event.EVENT_TOUCH_PRESSED {x=" + x + "; y=" + y + "; mapX=" + mapX + "; mapY=" + mapY + "}");
                break;
            case Event.EVENT_TOUCH_MOVED:
                int dx = touchedX - x;
                int dy = touchedY - y;
                Pair pair = corrector.correct(backupMapX + dx, backupMapY + dy, this);
                scrollingMapTo(pair.getX(), pair.getY());
//                Debug.debug("BattleScreen.onEvent(): code = Event.EVENT_TOUCH_MOVED {x=" + x + "; y=" + y + "; newMapX=" + mapX + "; newMapY=" + mapY + "}");
                break;
            case Event.EVENT_TOUCH_RELEASED:
                if (battleContext.isMyTurn()) {
                    Debug.debug("BattleScreen.onEvent(): code = Event.EVENT_TOUCH_RELEASED");
                    if (isOnTheMap(x, y)) {
                        if (Mathematics.abs(touchedX - x) <= ClientBattleContext.SPOT && Mathematics.abs(touchedY - y) <= ClientBattleContext.SPOT) {
                            if (battleContext.getActiveUnit().getLogic().isIdle()) {
                                Pair cellCoordinate = cellFinder.find(x + mapX, y + mapY, this);
                                BattleCell battleCell = battleContext.getBattle().getMap().getCells()[cellCoordinate.getX()][cellCoordinate.getY()];
                                CellElement[] elements = battleCell.getElements();
                                boolean wasSelectedElement = false;
                                for (int i = battleCell.getSize() - 1; i >= 0; i--) {
                                    CellElement element = elements[i];
                                    switch (element.getElementType()) {
                                        case CellElementTypes.HUMAN:
                                            ClientWarriorElement warrior = (ClientWarriorElement) element;
                                            Debug.debug("Is warrior " + warrior.getName() + " (id=" + warrior.getId() + ")"
                                                    + " [" + cellCoordinate.getX() + ":" + cellCoordinate.getY() + "]");
                                            if (warrior.getBattleGroup() == battleContext.getBattleGroup()) {
                                                setActiveUnit(warrior);
                                                setSelectedElement(null, -1, -1);
                                            } else {
                                                setSelectedElement(warrior, cellCoordinate.getX(), cellCoordinate.getY());
                                            }
                                            wasSelectedElement = true;
                                            break;
                                        case CellElementTypes.DOOR:
                                        case CellElementTypes.REGOLITH:
                                        case CellElementTypes.BOX:
                                            setSelectedElement((DynamicCellElement) element, cellCoordinate.getX(), cellCoordinate.getY());
                                            wasSelectedElement = true;
                                            break;
                                    }
                                    if (wasSelectedElement) {
                                        break;
                                    }
                                }
                                if (!wasSelectedElement) {
                                    setSelectedElement(null, -1, -1);
                                    if (BattleMapHelper.isReachable(battleCell)) {
                                        Debug.debug("Trace for a user " + battleContext.getActiveUnit().getNumber()
                                                + " from [" + battleContext.getActiveUnit().getCellX() + ":" + battleContext.getActiveUnit().getCellY() + "]"
                                                + " to [" + cellCoordinate.getX() + ":" + cellCoordinate.getY() + "]");
                                        ClientBattleHelper.trace(battleContext.getBattle().getMap().getCells(), battleContext.getActiveUnit(), cellCoordinate.getX(), cellCoordinate.getY(),
                                                ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
                                    } else {
                                        Debug.debug("Cell [" + cellCoordinate.getX() + ":" + cellCoordinate.getY() + "] is not reachable");
                                    }
                                }
                            } else {
                                Debug.debug("Unit " + battleContext.getActiveUnit().getName() + " is not idle");
                            }
                        }
                    }
                }
                break;
            case Event.EVENT_TOUCH_DOUBLE_CLICK:
                if (battleContext.isMyTurn()) {
                    Debug.debug("BattleScreen.onEvent(): code = Event.EVENT_TOUCH_DOUBLE_CLICK");
//                    Debug.debug("My turn & i want to move " + x + ":" + y);
                    if (isOnTheMap(x, y)) {
                        Pair cellCoordinate = cellFinder.find(x + mapX, y + mapY, this);
                        Debug.debug("A cell to go [" + cellCoordinate.getX() + ":" + cellCoordinate.getY() + "] remaining action scores = " + battleContext.getActiveUnit().getActionScore());
                        //todo: Проверять, что ячейка не занята
                        if (BattleMapHelper.isShortestPathCell(battleContext.getBattle().getMap().getCells()[cellCoordinate.getX()][cellCoordinate.getY()], battleContext.getActiveUnit())) {
                            Debug.debug("Move an user " + battleContext.getActiveUnit().getNumber());
                            moveWarrior((short) cellCoordinate.getX(), (short) cellCoordinate.getY());
                        } else {
                            Debug.debug("Not a shortest path");
                        }
                    } else {
                        Debug.debug("Out of the map touch");
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public int getInterval() {
        return 100;
    }

    @Override
    public short[] getTypes() {
        return listenedTypes;
    }

    @Override
    public void onReceive(ClientDeSerializedMessage message, short type) {
        switch (type) {
            case Packets.BATTLE_SERVICE_NEW_CLIENT_LOGIN:
                ClientNewBattleLogin newLogin = (ClientNewBattleLogin) message;
                Debug.debug(LocalizedStrings.NEW_CLINET_BATTLE_LOGIN + newLogin.getGroup().getAccount().getName());
                break;
            case Packets.INITIALLY_OBSERVED_ENEMIES:
                Debug.debug("INITIALLY_OBSERVED_ENEMIES");
                ClientInitiallyObservedEnemies init = (ClientInitiallyObservedEnemies) message;
                ClientWarriorCollection observed = init.getEnemies();
                for (int i = 0; i < observed.size(); i++) {
                    ClientWarriorElement enemyUnit = (ClientWarriorElement) observed.get(i);
                    ClientBattleHelper.initMapXY(this, enemyUnit);
                }
                break;
            case Packets.CHANGE_ACTIVE_ALLIANCE:
                ClientChangeActiveAllianceAnswer change = (ClientChangeActiveAllianceAnswer) message;
                doChangeActiveAlliance(change.getAlliance());
                break;
            case Packets.MOVE_ALLY:
                Debug.debug("MOVE_ALLY");
                ClientMoveAllyAnswer moveAlly = (ClientMoveAllyAnswer) message;
                moveAlly(moveAlly.getAlly(), moveAlly.getX(), moveAlly.getY(), moveAlly.getEnemies());
                break;
            case Packets.MOVE_ENEMY:
                Debug.debug("MOVE_ENEMY");
                ClientMoveEnemyAnswer moveEnemy = (ClientMoveEnemyAnswer) message;
                moveEnemy(moveEnemy.getEnemy());
                break;
            case Packets.SHOOT:
                Debug.debug("SHOOT");
                break;
        }
    }

    private void quicklyCompleteAllCommandsForUnits(ClientWarriorCollection collection) {
        for (int i = 0; i < collection.size(); i++) {
            ClientWarriorElement unit = (ClientWarriorElement) collection.get(i);
            unit.getLogic().quicklyCompleteAllCommands();
        }
    }

    public void moveWarrior(short x, short y) {
        try {
            ClientMoveWarriorAnswer move = configuration.getBattleServiceManager().move(battleContext.getActiveUnit(), x, y);
            if (move.isSuccess()) {
                short xx = move.getX();
                short yy = move.getY();
                if (xx != x || yy != y) {
                    ClientBattleHelper.trace(battleContext.getBattle().getMap().getCells(), battleContext.getActiveUnit(), xx, yy, configuration.getBattleConfiguration());
                }
                battleContext.getActiveUnit().getLogic().doRun();
                ClientWarriorCollection warriorCollection = move.getEnemies();
                for (int i = 0; i < warriorCollection.size(); i++) {
                    ClientWarriorElement enemy = (ClientWarriorElement) warriorCollection.get(i);
                    ClientBattleHelper.initMapXY(this, enemy);
                }
            } else {
                NotificationBox.error(LocalizedStrings.MOVEMENT_RESTRICTION);
            }
        } catch (Exception e) {
            NotificationBox.error(LocalizedStrings.MOVEMENT_RESTRICTION);
            Debug.error(LocalizedStrings.MOVEMENT_RESTRICTION, e);
        }
    }

    /**
     * Разрешить двинуть бойца ally принадлежащего союзнику в точку (x;y)
     *
     * @param ally
     * @param x
     * @param y
     * @param enemyCollection противники которых наш товарищ засветил
     */
    public void moveAlly(ClientWarriorElement ally, int x, int y, ClientWarriorCollection enemyCollection) {
        BattleCell[][] cells = battleContext.getBattle().getMap().getCells();
        ClientBattleHelper.route(cells, ally, configuration.getBattleConfiguration().getRouter(), configuration.getBattleConfiguration());
        ClientBattleHelper.trace(cells, ally, x, y, configuration.getBattleConfiguration());
        ally.getLogic().doRun();
        for (int i = 0; i < enemyCollection.size(); i++) {
            ClientWarriorElement enemyUnit = (ClientWarriorElement) enemyCollection.get(i);
            ClientBattleHelper.initMapXY(this, enemyUnit);
        }
    }

    /**
     * Двинуть противника по его пути.
     *
     * @param enemy
     */
    public void moveEnemy(ClientWarriorElement enemy) {
        enemy.getLogic().doRun();
    }

    /**
     * Покрывает ли наш экран эту точку карты?
     *
     * @param x координата карты
     * @param y координата карты
     * @return
     */
    public boolean isOnTheScreen(int x, int y) {
        return mapX <= x + ClientBattleContext.HORIZONTAL_RADIUS
                && x - ClientBattleContext.HORIZONTAL_RADIUS <= mapX + getWidth()
                && mapY <= y + ClientBattleContext.VERTICAL_RADIUS
                && y - ClientBattleContext.VERTICAL_RADIUS <= mapY + getHeight();
    }

    /**
     * Лежит ли эта точка экрана на сетке карты?
     *
     * @param x экранная координата
     * @param y экранная координата
     * @return
     */
    public boolean isOnTheMap(int x, int y) {
        int xx = x + mapX;
        int yy = y + mapY;
        return -ClientBattleContext.TANGENT * xx + b4 <= yy
                &&
                ClientBattleContext.TANGENT * xx + b1 <= yy
                &&
                -ClientBattleContext.TANGENT * xx + b2 >= yy
                &&
                ClientBattleContext.TANGENT * xx + b3 >= yy;
    }

    /**
     * Обработчик события прокрутки игровой карты.
     */
    private void onScrollingMapChanged() {
        if (actionMenuWindow != null) {
            actionMenuWindow.onScrollingMapChanged();
        }
    }

    public void scrollingMapTo(int mapX, int mapY) {
        this.mapX = mapX;
        this.mapY = mapY;
        onScrollingMapChanged();
    }

    /**
     * Вернуть X координату точки карты которая отображается в левом верхнем углу экрана.
     */
    public int getMapX() {
        return mapX;
    }

    public void setMapX(int mapX) {
        scrollingMapTo(mapX, this.mapY);
    }

    /**
     * Вернуть Y координату точки карты которая отображается в левом верхнем углу экрана.
     */
    public int getMapY() {
        return mapY;
    }

    public void setMapY(int mapY) {
        scrollingMapTo(this.mapX, mapY);
    }

    /**
     * Возвращаем класс занимающийся ограничением координат углов экрана на карте.
     *
     * @return
     */
    public MapCorrector getCorrector() {
        return corrector;
    }

    public void setCorrector(MapCorrector corrector) {
        this.corrector = corrector;
    }

    /**
     * Возвращаем центр верхнего ромба(клетки) изометричекской сетки
     *
     * @return
     */
    public Pair getTopCenter() {
        return topCenter;
    }

    /**
     * Возвращаем центр правого ромба(клетки) изометричекской сетки
     *
     * @return
     */
    public Pair getCenterRight() {
        return centerRight;
    }

    /**
     * Возвращаем центр нижнего ромба(клетки) изометричекской сетки
     *
     * @return
     */
    public Pair getBottomCenter() {
        return bottomCenter;
    }

    /**
     * Возвращаем центр левого ромба(клетки) изометричекской сетки
     *
     * @return
     */
    public Pair getCenterLeft() {
        return centerLeft;
    }

    /**
     * Возвращаем b из формулы y = kx + b , для правого верхнего края сетки
     */
    public int getB1() {
        return b1;
    }

    /**
     * Возвращаем b из формулы y = kx + b , для левого верхнего края сетки
     */
    public int getB4() {
        return b4;
    }

    /**
     * Возвращаем b из формулы y = kx + b , для правого нижнего края сетки
     */
    public int getB2() {
        return b2;
    }

    /**
     * Возвращаем b из формулы y = kx + b , для левого нижнего края сетки
     */
    public int getB3() {
        return b3;
    }

    @Override
    public void onShow() {
        Account account = ClientConfigurationFactory.getConfiguration().getAccount();
        //todo: тут ли устанавливать SecurityOperationManager ?
        account.setSecurity(new SecurityOperationManager());
        account.getSecurity().setAccount(account);

//        groupUnits = ClientBattleHelper.getGroupBattleUnits(battle, account);
//        allyUnits = ClientBattleHelper.getAllyBattleUnits(battle, account);
//        enemyUnits = ClientBattleHelper.getEnemyBattleUnits(battle, account);

        int size = battleContext.getBattle().getMap().getCells().length;

        topCenter = new Pair();
        topCenter.setX((size - 1) * ClientBattleContext.HORIZONTAL_RADIUS);
        topCenter.setY(0);

        centerRight = new Pair();
        centerRight.setX((size - 1) * ClientBattleContext.HORIZONTAL_DIAGONAL);
        centerRight.setY(topCenter.getY() + (size - 1) * ClientBattleContext.VERTICAL_RADIUS);

        bottomCenter = new Pair();
        bottomCenter.setX(topCenter.getX());
        bottomCenter.setY(topCenter.getY() + (size - 1) * ClientBattleContext.VERTICAL_DIAGONAL);

        centerLeft = new Pair();
        centerLeft.setX(0);
        centerLeft.setY(centerRight.getY());

        b1 = (int) (topCenter.getY() - (topCenter.getX()) * ClientBattleContext.TANGENT) - ClientBattleContext.VERTICAL_RADIUS;
        b2 = (int) (centerRight.getY() + centerRight.getX() * ClientBattleContext.TANGENT) + ClientBattleContext.VERTICAL_RADIUS;
        b3 = (int) (bottomCenter.getY() - bottomCenter.getX() * ClientBattleContext.TANGENT) + ClientBattleContext.VERTICAL_RADIUS;
        b4 = (int) (centerLeft.getY() + centerLeft.getX() * ClientBattleContext.TANGENT) - ClientBattleContext.VERTICAL_RADIUS;

        BattleAlliance alliance = battleContext.getBattleGroup().getAlliance();
        ExitZone exit = alliance.getExit();
        displayCell(exit.getX(), exit.getY());

        BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
        initUnitFromCollection(battleContext.getGroupUnits(), battleConfiguration);
        initUnitFromCollection(battleContext.getAllyUnits(), battleConfiguration);
        initUnitFromCollectionSimple(battleContext.getEnemyUnits());

        battleContext.setActiveUnit(null);
        setActiveUnit(getFirstLiveUnit()); //todo: нужно ли?

        logicUpdateTimerId = TimerManager.setPeriodicTimer(100, this);
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
    }

    @Override
    public void onHide() {
        TimerManager.killTimer(logicUpdateTimerId);
        if (finishTurnTimerId != TimerManager.NULL_TIMER) {
            TimerManager.killTimer(finishTurnTimerId);
        }
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().unregister(this);
    }

    private void initUnitFromCollection(ClientWarriorCollection collection, BattleConfiguration battleConfiguration) {
        for (int i = 0; i < collection.size(); i++) {
            ClientWarriorElement unit = (ClientWarriorElement) collection.get(i);
            unit.initiate();
            ClientBattleHelper.initMapXY(this, unit);
            battleConfiguration.getObserver().observe(unit);
        }
    }

    private void initUnitFromCollectionSimple(ClientWarriorCollection collection) {
        for (int i = 0; i < collection.size(); i++) {
            ClientWarriorElement unit = (ClientWarriorElement) collection.get(i);
            unit.initiate();
        }
    }

    public void putEnemyInPosition(ClientWarriorElement unit, int x, int y) {
        Pair coordinates = coordinateFinder.find(x, y, this);
        WarriorHelper.putWarriorIntoMap(battleContext.getBattle().getMap().getCells(), unit, x, y);
        unit.setMapX((short) coordinates.getX());
        unit.setMapY((short) coordinates.getY());
    }

    public ClientWarriorElement getFirstLiveUnit() {
        return (ClientWarriorElement) battleContext.getGroupUnits().get(0);
    }

    private void doChangeActiveAlliance(BattleAlliance alliance) {
//        BattleConfiguration battleConfiguration = configuration.getBattleConfiguration();
//        //to do: Вызов следующих трех методов под вопросом
        //todo: Все динамические элементы (юниты, двери) должны обновляться паралельно
        //todo: Это вообще не требуется. Новый ход клиент не начнет, пока все пользователи не подтвердят свое завершение хода. Но и сервер не должен давать возможность выполнять ходы за пределами 30-секундного хода
        quicklyCompleteAllCommandsForUnits(battleContext.getGroupUnits());
        quicklyCompleteAllCommandsForUnits(battleContext.getAllyUnits());
        quicklyCompleteAllCommandsForUnits(battleContext.getEnemyUnits());
//        ClientBattleHelper.immediateMoveAllies(groupUnits, battle, battleConfiguration, this);
//        ClientBattleHelper.immediateMoveAllies(allyUnits, battle, battleConfiguration, this);
//        ClientBattleHelper.immediateMoveEnemies(enemyUnits, battle, this);

        if (battleContext.isMyTurn()) {
            onMyTurnFinished();
        }
        battleContext.setActiveAlliance(alliance);
        if (battleContext.isMyTurn()) {
            onMyTurnStarted();
        }
        onActiveAllianceChanged(alliance);
    }

    /**
     * Обработчик события сообщающего о начале хода.
     */
    public void onMyTurnStarted() {
        Debug.debug("My turn has begun");
        finishTurnTimerId = TimerManager.setSingleTimer(30000, this);

        ClientBattleHelper.resetActionScores(battleContext.getGroupUnits(), configuration.getBaseConfiguration());
        setActiveUnit(getFirstLiveUnit());

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.show(panelManager.getBattleWarriorListWindow());
        panelManager.show(panelManager.getBattleWeaponMenuWindow());
        panelManager.show(panelManager.getBattleWarriorMenuWindow());
//        panelManager.show(panelManager.getBattleShotMenuWindow());
    }

    /**
     * Обработчик события сообщающего о завершении хода.
     */
    public void onMyTurnFinished() {
        Debug.debug("My turn has been finished");
        if (finishTurnTimerId != TimerManager.NULL_TIMER) {
            TimerManager.killTimer(finishTurnTimerId);
            finishTurnTimerId = TimerManager.NULL_TIMER;
        }
        setSelectedElement(null, -1, -1);
        configuration.getBattleServiceManager().checkSum();

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(panelManager.getBattleWarriorListWindow());
        panelManager.hide(panelManager.getBattleWeaponMenuWindow());
        panelManager.hide(panelManager.getBattleWarriorMenuWindow());
        panelManager.hide(panelManager.getBattleShotMenuWindow());
    }

    /**
     * Обработчик события об изменении активного военного союза, того чей, в данный момент, ход.
     */
    private void onActiveAllianceChanged(BattleAlliance alliance) {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getHeadlinePanel().setActiveAlliance(alliance);
        panelManager.getBattleMenuPanel().onActiveAllianceChanged(alliance);
    }

    /**
     * Установить активного бойца.
     *
     * @param unit
     */
    public void setActiveUnit(ClientWarriorElement unit) {
//        if (this.activeUnit != unit) {
        battleContext.setActiveUnit(unit);
        onActiveUnitChanged(unit);
//        }
    }

    /**
     * Обработчик события сообщающего об изменении активного бойца.
     */
    private void onActiveUnitChanged(ClientWarriorElement unit) {
        Debug.debug("The current user number = " + unit.getNumber());
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattleWarriorListPanel().onActiveUnitChanged(unit);
        panelManager.getBattleMenuPanel().onActiveUnitChanged(unit);
        panelManager.getBattleWeaponMenuPanel().onActiveUnitChanged(unit);
        panelManager.getBattleWarriorMenuPanel().onActiveUnitChanged(unit);
        panelManager.getBattleShotMenuPanel().onActiveUnitChanged(unit);
        doMapReachabilityUpdate();
    }

    //todo: Избавиться от этого метода
    public void doMapReachabilityUpdate() {
        Debug.debug("Route for unit " + battleContext.getActiveUnit().getName());
        ClientBattleHelper.route(battleContext.getBattle().getMap().getCells(), battleContext.getActiveUnit(),
                ClientConfigurationFactory.getConfiguration().getBattleConfiguration().getRouter(),
                ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
    }

    public void setSelectedElement(DynamicCellElement element, int cellX, int cellY) {
        // element - что за объект                  как реагировать на выбор element-а:
        // * null, пустая клетка                    - убрать все action-кнопки и просто протрейсить путь до клетки
        // * союзный warrior                        - отобразить панельку AllyWarriorMenu ("Инфа о бойце")
        // * вражеский warrior                      - отобразить панельку EnemyWarriorMenu ("Инфа о бойце", "Выстрел наспех", "Прицельный выстрел")
        // * дверь                                  - отобразить панельку DoorMenu ("Открыть/Закрыть дверь")
        // * кучка реголита                         - отобразить панельку RegolithMenu ("Собрать реголит")
        // * ящик (с амуницией, боеприпасами)       - отобразить панельку BoxMenu ("Посмотреть содержимое ящика")
        // * предмет (оружие, какая-нибудь плюшка)  - отобразить панельку TackleMenu ("Подобрать предмет")

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        if (actionMenuWindow != null) {
            panelManager.hide(actionMenuWindow);
        }

        battleContext.setSelectedElementCellX((short) cellX);
        battleContext.setSelectedElementCellY((short) cellY);

        if (element != null) {
            switch (element.getElementType()) {
                case CellElementTypes.HUMAN:
                    ClientWarriorElement warrior = (ClientWarriorElement) element;
                    if (warrior.getMembershipType() == WarriorMembershipType.ENEMY) {
                        actionMenuWindow = panelManager.getBattleShotMenuWindow();
                        panelManager.getBattleShotMenuPanel().onSelectedElementChanged(warrior); // onSelectedWarriorChanged
                    } else {
//                        actionMenuWindow = panelManager.getBattleAllyWarriorMenuWindow();
//                        panelManager.getBattleAllyWarriorMenuPanel().onSelectedElementChanged(warrior); // onSelectedWarriorChanged
                    }
                    break;
//                case CellElementTypes.DOOR:
//                    ClientDoor door = (ClientDoor) element;
//                    actionMenuWindow = panelManager.getBattleDoorMenuWindow();
//                    panelManager.getBattleDoorMenuPanel().onSelectedElementChanged(door); // onSelectedDoorChanged
//                    break;
//                case CellElementTypes.REGOLITH:
//                    ClientRegolithElement regolithElement = (ClientRegolithElement) element;
//                    actionMenuWindow = panelManager.getBattleRegolithMenuWindow();
//                    panelManager.getBattleRegolithMenuPanel().onSelectedElementChanged(regolithElement);
//                    break;
                default:
                    actionMenuWindow = null;
            }

            if (actionMenuWindow != null) {
                actionMenuWindow.setCellXY(cellX, cellY);
                panelManager.show(actionMenuWindow);
            }
            battleContext.setSelectedElement(element);
            onSelectedElementChanged(element);
        }
    }

    /**
     * Обработчик события сообщающего об выборе нового элемента на карте.
     */
    private void onSelectedElementChanged(DynamicCellElement element) {
        Debug.debug("The current selected element '" + element.getElementType() + "'");
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattleWarriorMenuPanel().onSelectedElementChanged(element);
        panelManager.getBattleShotMenuPanel().onSelectedElementChanged(element);
    }

    /**
     * Обработчик события сообщающего об окончании выполнения активным бойцом всех своих операций, боец сейчас находится
     * в состоянии ожидания команд.
     */
    public void onOperationOfUnitIsCompleted(ClientWarriorElement unit) {
        // здесь должен быть код, который должен определить что находится вокруг бойца и, в зависимости от его окружения,
        // может быть отображена соответствующая кнопка. Например, если рядом с бойцом находится дверь, то должна
        // появиться кнопка открытия/закрытия двери.

        // Что может находиться рядом с бойцом                 с каким действием появится кнопка
        // * дверь                                             - открыть/закрыть дверь
        // * кучка реголита                                    - собрать реголит
        // * вражеский боец + выбрано оружие "Нож"             - удар ножом
        // * зона выхода с карты                               - покинуть поле боя
        // * предмет (аптечка, ящик с амуницией, боеприпасами) - подобрать предмет
    }

    /**
     * Обработчик события сообщающего об изменении состояния стоит/сидит активного бойца.
     */
    public void onChangeWarriorSitting(AbstractClientWarriorElement warrior) {
        if (battleContext.getActiveUnit() == warrior) {
            PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
            panelManager.getBattleWarriorMenuPanel().onActiveUnitSittingChanged(battleContext.getActiveUnit());
        }
    }

}
