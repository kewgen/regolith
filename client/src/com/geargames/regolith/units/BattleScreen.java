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
    private int touchedXX;
    private int touchedYY;

    private Pair topCenter;
    private Pair centerRight;
    private Pair bottomCenter;
    private Pair centerLeft;

    private int b1;
    private int b2;
    private int b3;
    private int b4;

    private Pair center;
    private ClientConfiguration configuration;
    private ClientBattleContext battleContext;

    private short[] listenedTypes;
    private int timerId;

    private PSprite shadowSprite;
    private PSprite reachableCellSprite;
    private PSprite unreachableCellSprite;
    private PObject symbolOfProtectionObject;
    private PObject iconHeightOfBarriersObject;

    public BattleScreen() {
        timerId = TimerManager.NULL_TIMER;
        configuration = ClientConfigurationFactory.getConfiguration();
        battleContext = configuration.getBattleContext();
        listenedTypes = new short[]{Packets.MOVE_ALLY, Packets.MOVE_ENEMY, Packets.SHOOT, Packets.CHANGE_ACTIVE_ALLIANCE,
                Packets.INITIALLY_OBSERVED_ENEMIES, Packets.BATTLE_SERVICE_NEW_CLIENT_LOGIN};
        cellFinder = configuration.getCellFinder();
        coordinateFinder = configuration.getCoordinateFinder();

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
    private void drawCell(Graphics graphics, int x, int y, BattleCell cell) {
        if (battleContext.isMyTurn()) {
            boolean isReachableCell = cell.getOrder() != BattleMapHelper.UN_ROUTED;
            if (isReachableCell) {
                reachableCellSprite.draw(graphics, x, y);
            } else {
                if (BattleMapHelper.isBarrier(cell)) {
                    unreachableCellSprite.draw(graphics, x, y);
                }
            }
            if (BattleMapHelper.isShortestPathCell(cell, battleContext.getActiveUnit())) {
                shadowSprite.draw(graphics, x - 29/*width*/, y - 20/*height*/);
            }
            if (isReachableCell) {
                graphics.drawString("" + cell.getOrder(), x, y, com.geargames.common.Graphics.HCENTER);
            }
        }
        final byte BARRIER_NONE = 0;
        final byte BARRIER_HALF_HEIGHT = 1;
        final byte BARRIER_FULL_HEIGHT = 2;
        byte barrierType = BARRIER_NONE;
        CellElement[] elements = cell.getElements();
        for (int i = 0; i < cell.getSize(); i++) {
            CellElement element = elements[i];
            if (element.getElementType() == CellElementTypes.HUMAN) {
                drawSymbolOfProtection(graphics, (ClientWarriorElement) element);
            }
            ((DrawableElement) element).draw(graphics, x, y);
            if (element.isBarrier()) {
                if (!element.isHalfLong()) {
                    barrierType = BARRIER_FULL_HEIGHT;
                } else if (barrierType == BARRIER_NONE) {
                    barrierType = BARRIER_HALF_HEIGHT;
                }
            }
        }
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
            if (warrior.getCellX() > 0 && warrior.getCellY() > 0 && BattleMapHelper.isBarrier(battleCells[warrior.getCellX() - 1][warrior.getCellY() - 1])) {
                barrierBits |= Dir.NORTHWEST;
            }
            if (warrior.getCellY() > 0 && BattleMapHelper.isBarrier(battleCells[warrior.getCellX()][warrior.getCellY() - 1])) {
                barrierBits |= Dir.NORTH;
            }
            if (warrior.getCellX() < battleCells.length - 1 && warrior.getCellY() > 0 && BattleMapHelper.isBarrier(battleCells[warrior.getCellX() + 1][warrior.getCellY() - 1])) {
                barrierBits |= Dir.NORTHEAST;
            }
            if (warrior.getCellX() < battleCells.length - 1 && BattleMapHelper.isBarrier(battleCells[warrior.getCellX() + 1][warrior.getCellY()])) {
                barrierBits |= Dir.EAST;
            }
            if (warrior.getCellX() < battleCells.length - 1 && warrior.getCellY() < battleCells[0].length - 1 && BattleMapHelper.isBarrier(battleCells[warrior.getCellX() + 1][warrior.getCellY() + 1])) {
                barrierBits |= Dir.SOUTHEAST;
            }
            if (warrior.getCellY() < battleCells[0].length - 1 && BattleMapHelper.isBarrier(battleCells[warrior.getCellX()][warrior.getCellY() + 1])) {
                barrierBits |= Dir.SOUTH;
            }
            if (warrior.getCellX() > 0 && warrior.getCellY() < battleCells[0].length - 1 && BattleMapHelper.isBarrier(battleCells[warrior.getCellX() - 1][warrior.getCellY() + 1])) {
                barrierBits |= Dir.SOUTHWEST;
            }
            if (warrior.getCellX() > 0 && BattleMapHelper.isBarrier(battleCells[warrior.getCellX() - 1][warrior.getCellY()])) {
                barrierBits |= Dir.WEST;
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
                Pair pair = coordinateFinder.find(warrior.getCellY(), warrior.getCellX(), this);
                index.draw(graphics, pair.getX() - mapX, pair.getY() - mapY);
            }
        }
    }

    private void drawBattleMap(Graphics graphics) {
        BattleCell[][] cells = battleContext.getBattle().getMap().getCells();
        int length = cells.length;
        for (int yCell = 0; yCell < length; yCell++) {
            int y = yCell * ClientBattleContext.VERTICAL_RADIUS;
            int x = (length - 1 + yCell) * ClientBattleContext.HORIZONTAL_RADIUS;
            for (int xCell = 0; xCell < length; xCell++) {
                if (isOnTheScreen(x, y)) {
                    drawCell(graphics, x - mapX, y - mapY, cells[yCell][xCell]);
                }
                x -= ClientBattleContext.HORIZONTAL_RADIUS;
                y += ClientBattleContext.VERTICAL_RADIUS;
            }
        }
    }

    private void drawGround(Graphics graphics) {
        int x = -mapX % ClientBattleContext.GROUND_WIDTH - ClientBattleContext.HORIZONTAL_RADIUS;
        int tmp = -mapY % ClientBattleContext.GROUND_HEIGHT - ClientBattleContext.VERTICAL_RADIUS;
        while (x < getWidth()) {
            int y = tmp;
            while (y < getHeight()) {
                Environment.getRender().getSprite(1).draw(graphics, x, y);
                y += ClientBattleContext.GROUND_HEIGHT;
            }
            x += ClientBattleContext.GROUND_WIDTH;
        }
        // Рисуем границу вокруг игрового поля
        graphics.setColor(0x0000FF);
        BattleCell[][] battleCells = battleContext.getBattle().getMap().getCells();
        int[] points = new int[8];
        Pair pair = coordinateFinder.find(0, 0, this);
        points[0] = pair.getX() - mapX;
        points[1] = pair.getY() - ClientBattleContext.VERTICAL_RADIUS - mapY;
        pair = coordinateFinder.find(0, battleCells[0].length - 1, this);
        points[2] = pair.getX() + ClientBattleContext.HORIZONTAL_RADIUS - mapX;
        points[3] = pair.getY() - mapY;
        pair = coordinateFinder.find(battleCells.length - 1, battleCells[0].length - 1, this);
        points[4] = pair.getX() - mapX;
        points[5] = pair.getY() + ClientBattleContext.VERTICAL_RADIUS - mapY;
        pair = coordinateFinder.find(battleCells.length - 1, 0, this);
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
        setMapX(x - (getWidth() >> 1));
        setMapY(y - (getHeight() >> 1));
        center.setX(x);
        center.setY(y);
        corrector.correct(getMapX(), getMapY(), this);
    }

    /**
     * Переместить центр экрана к ячейке карты.
     *
     * @param x правый номер сетки
     * @param y левый номер сетки
     */
    public void setCellCenter(int x, int y) {
        Pair pair = coordinateFinder.find(y, x, this);
        setCenter(pair.getX(), pair.getY());
    }

    /**
     * Центрировать область просмотра на ячейке карты.
     */
    public void displayCell(int cellX, int cellY) {
        setCellCenter(cellX, cellY);
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
        callTickForCollection(battleContext.getGroupUnits());
        callTickForCollection(battleContext.getAllyUnits());
        callTickForCollection(battleContext.getEnemyUnits());
    }

    public boolean onEvent(int code, int param, int x, int y) {
        switch (code) {
            case Event.EVENT_TOUCH_PRESSED:
                touchedX = x;
                touchedY = y;
                touchedXX = x;
                touchedYY = y;
                break;
            case Event.EVENT_TOUCH_MOVED:
                int dx = touchedX - x;
                int dy = touchedY - y;
                setMapX(mapX + dx);
                setMapY(mapY + dy);
                touchedX = x;
                touchedY = y;
                corrector.correct(mapX, mapY, this);
                break;
            case Event.EVENT_TOUCH_RELEASED:
                if (battleContext.isMyTurn()) {
                    Debug.debug("BattleScreen.onEvent(): code = Event.EVENT_TOUCH_RELEASED");
                    if (isOnTheMap(x, y)) {
                        if (Mathematics.abs(touchedXX - x) <= ClientBattleContext.SPOT && Mathematics.abs(touchedYY - y) <= ClientBattleContext.SPOT) {
                            if (battleContext.getActiveUnit().getLogic().isIdle()) {
                                Pair cellCoordinate = cellFinder.find(x + mapX, y + mapY, this);
                                BattleCell battleCell = battleContext.getBattle().getMap().getCells()[cellCoordinate.getX()][cellCoordinate.getY()];
                                CellElement[] elements = battleCell.getElements();
                                boolean hasHumanInCell = false;
                                for (int i = 0; i < battleCell.getSize(); i++) {
                                    CellElement element = elements[i];
                                    if (element.getElementType() == CellElementTypes.HUMAN) {
                                        ClientWarriorElement warrior = (ClientWarriorElement) element;
                                        Debug.debug("Is warrior " + warrior.getName() + " (id=" + warrior.getId() + ")"
                                                + " [" + cellCoordinate.getX() + ":" + cellCoordinate.getY() + "]");
                                        if (warrior.getBattleGroup() == battleContext.getBattleGroup()) {
                                            setActiveUnit(warrior);
                                        } else {
                                            // Показать инфу о союзном или вражеском бойце
                                        }
                                        hasHumanInCell = true;
                                        break;
                                    }
                                }
                                if (!hasHumanInCell) {
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
//                BattleConfiguration battleConfiguration = configuration.getBattleConfiguration();
//                //to do: Вызов следующих трех методов под вопросом
                //todo: Все динамические элементы (юниты, двери) должны обновляться паралельно
                //todo: Это вообще не требуется. Новый ход не начнется, пока все пользователи не подтвердят свое завершение хода. Но и сервер не должен давать возможность выполнять ходы за пределами 30-секундного хода
                quicklyCompleteAllCommandsForUnits(battleContext.getGroupUnits());
                quicklyCompleteAllCommandsForUnits(battleContext.getAllyUnits());
                quicklyCompleteAllCommandsForUnits(battleContext.getEnemyUnits());
//                ClientBattleHelper.immediateMoveAllies(groupUnits, battle, battleConfiguration, this);
//                ClientBattleHelper.immediateMoveAllies(allyUnits, battle, battleConfiguration, this);
//                ClientBattleHelper.immediateMoveEnemies(enemyUnits, battle, this);

                ClientChangeActiveAllianceAnswer change = (ClientChangeActiveAllianceAnswer) message;
                if (battleContext.isMyTurn()) {
                    onMyTurnFinished();
                }
                battleContext.setActiveAlliance(change.getAlliance());
                if (battleContext.isMyTurn()) {
                    onMyTurnStarted();
                }
                onChangeActiveAlliance(change.getAlliance());
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
     * Вернуть X координату точки карты которая отображается в левом верхнем углу экрана.
     */
    public int getMapX() {
        return mapX;
    }

    public void setMapX(int mapX) {
        this.mapX = mapX;
    }

    /**
     * Вернуть Y координату точки карты которая отображается в левом верхнем углу экрана.
     */
    public int getMapY() {
        return mapY;
    }

    public void setMapY(int mapY) {
        this.mapY = mapY;
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

        int length = battleContext.getBattle().getMap().getCells().length;

        topCenter = new Pair();
        topCenter.setX((length - 1) * ClientBattleContext.HORIZONTAL_RADIUS);
        topCenter.setY(0);

        centerRight = new Pair();
        centerRight.setX((length - 1) * ClientBattleContext.HORIZONTAL_DIAGONAL);
        centerRight.setY(topCenter.getY() + (length - 1) * ClientBattleContext.VERTICAL_RADIUS);

        bottomCenter = new Pair();
        bottomCenter.setX(topCenter.getX());
        bottomCenter.setY(topCenter.getY() + (length - 1) * ClientBattleContext.VERTICAL_DIAGONAL);

        centerLeft = new Pair();
        centerLeft.setX(0);
        centerLeft.setY(centerRight.getY());

        b1 = (int) (topCenter.getY() - (topCenter.getX()) * ClientBattleContext.TANGENT) - ClientBattleContext.VERTICAL_RADIUS;
        b2 = (int) (centerRight.getY() + centerRight.getX() * ClientBattleContext.TANGENT) + ClientBattleContext.VERTICAL_RADIUS;
        b3 = (int) (bottomCenter.getY() - bottomCenter.getX() * ClientBattleContext.TANGENT) + ClientBattleContext.VERTICAL_RADIUS;
        b4 = (int) (centerLeft.getY() + centerLeft.getX() * ClientBattleContext.TANGENT) - ClientBattleContext.VERTICAL_RADIUS;
        center = new Pair();

        BattleAlliance alliance = battleContext.getBattleGroup().getAlliance();
        ExitZone exit = alliance.getExit();
        setCellCenter(exit.getX(), exit.getY());

        BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
        initUnitFromCollection(battleContext.getGroupUnits(), battleConfiguration);
        initUnitFromCollection(battleContext.getAllyUnits(), battleConfiguration);
        initUnitFromCollectionSimple(battleContext.getEnemyUnits());

        battleContext.setActiveUnit(null);
        setActiveUnit(getFirstLiveUnit()); //todo: нужно ли?

        timerId = TimerManager.setPeriodicTimer(100, this);
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
    }

    @Override
    public void onHide() {
        TimerManager.killTimer(timerId);
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
        Pair coordinates = coordinateFinder.find(x, y, this); //todo: Здесь x и y не перепутаны местами?
        WarriorHelper.putWarriorIntoMap(battleContext.getBattle().getMap().getCells(), unit, x, y);
        unit.setMapX((short) coordinates.getX());
        unit.setMapY((short) coordinates.getY());
    }

    public ClientWarriorElement getFirstLiveUnit() {
        return (ClientWarriorElement) battleContext.getGroupUnits().get(0);
    }

    /**
     * Обработчик события сообщающего о начале хода.
     */
    public void onMyTurnStarted() {
        Debug.debug("My turn has begun");
        ClientBattleHelper.resetActionScores(battleContext.getGroupUnits(), configuration.getBaseConfiguration());
        setActiveUnit(getFirstLiveUnit());

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.show(panelManager.getBattleWarriorListWindow());
        panelManager.show(panelManager.getBattleWeaponMenuWindow());
        panelManager.show(panelManager.getBattleWarriorMenuWindow());
        panelManager.show(panelManager.getBattleShotMenuWindow());
    }

    /**
     * Обработчик события сообщающего о завершении хода.
     */
    public void onMyTurnFinished() {
        Debug.debug("My turn has been finished");
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
    private void onChangeActiveAlliance(BattleAlliance alliance) {
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
        onChangeActiveUnit();
//        }
    }

    /**
     * Обработчик события сообщающего об изменении активного бойца.
     */
    private void onChangeActiveUnit() {
        ClientWarriorElement unit = battleContext.getActiveUnit();
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
