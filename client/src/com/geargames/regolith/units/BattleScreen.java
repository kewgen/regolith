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
import com.geargames.regolith.serializers.answers.ClientChangeActiveAllianceAnswer;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientHumanElementCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.*;

/**
 * Users: mkutuzov, abarakov
 * Date: 13.02.12
 */
public class BattleScreen extends Screen implements TimerListener, DataMessageListener {
    public static final int GROUND_WIDTH = 348;
    public static final int GROUND_HEIGHT = 174;
    public static final int HORIZONTAL_DIAGONAL = GROUND_WIDTH / 3;
    public static final int VERTICAL_DIAGONAL = GROUND_HEIGHT / 3;
    public static final int SPOT = 10;
    public static int HORIZONTAL_RADIUS = HORIZONTAL_DIAGONAL / 2;
    public static int VERTICAL_RADIUS = VERTICAL_DIAGONAL / 2;
    public static double TANGENT = (VERTICAL_RADIUS + 0.0) / (HORIZONTAL_RADIUS + 0.0);

    private ClientHumanElementCollection enemyUnits; // вражеские BattleUnit
    private ClientHumanElementCollection allyUnits;  // союзные BattleUnit
    private ClientHumanElementCollection groupUnits; // BattleUnit текущего клиента

    private Battle battle;
    private BattleGroup battleGroup;

    private ClientHumanElement activeUnit;
    private MapCorrector corrector;
    private Finder cellFinder;       // Определяет номер клетки из коорддинат на карте
    private Finder coordinateFinder; // Переводит номер клетки в координаты центра клетки на карте
    private boolean showGrid;

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

    private short[] listenedTypes;
    private int timerId;

    private BattleAlliance activeAlliance;

    private PSprite shadowSprite;
    private PSprite reachableCellSprite;
    private PSprite unreachableCellSprite;
    private PObject symbolOfProtectionObject;
    private PObject iconHeightOfBarriersObject;

    public BattleScreen() {
        timerId = TimerManager.NULL_TIMER;
        configuration = ClientConfigurationFactory.getConfiguration();
        listenedTypes = new short[]{Packets.MOVE_WARRIOR, Packets.MOVE_ALLY, Packets.MOVE_ENEMY, Packets.SHOOT, Packets.CHANGE_ACTIVE_ALLIANCE};
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
        if (isMyTurn()) {
            boolean isReachableCell = cell.getOrder() != BattleMapHelper.UN_ROUTED;
            if (isReachableCell) {
                reachableCellSprite.draw(graphics, x, y);
            } else {
                if (BattleMapHelper.isBarrier(cell)) {
                    unreachableCellSprite.draw(graphics, x, y);
                }
            }
            if (BattleMapHelper.isShortestPathCell(cell, activeUnit)) {
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
            ((DrawableElement) element).draw(graphics, x, y);
//            if (element.getElementType() == CellElementTypes.HUMAN) {
//                BattleUnit unit = ClientBattleHelper.findBattleUnitByWarrior(groupUnits, (Warrior) element);
//                drawUnit(graphics, unit);
//            } else {
//                PObject obj = Environment.getRender().getObject(element.getFrameId());
//                if (obj != null) {
//                    obj.draw(graphics, x, y);
//                }
            if (element.isBarrier()) {
                if (!element.isHalfLong()) {
                    barrierType = BARRIER_FULL_HEIGHT;
                } else if (barrierType == BARRIER_NONE) {
                    barrierType = BARRIER_HALF_HEIGHT;
                }
            }
//            }
        }
        if (isMyTurn()) {
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
        public static final byte NONE = 0;    // без направления
        public static final byte NORTH = 1;    // север
        public static final byte NORTHEAST = 2;    // северо-восток
        public static final byte EAST = 4;    // восток
        public static final byte SOUTHEAST = 8;    // юго-восток
        public static final byte SOUTH = 16;   // юг
        public static final byte SOUTHWEST = 32;   // юго-запад
        public static final byte WEST = 64;   // запад
        public static final byte NORTHWEST = -128; // северо-запад
    }
    /**       128
     *     64      1
     *  32            2
     *     16      4
     *         8
     */

    /**
     * Рисуем бойца.
     *
     * @param graphics
     */
    /*
    private void drawUnit(Graphics graphics, BattleUnit unit) {
        if (isMyTurn() && activeUnit == unit) {
            byte barrierBits = Dir.NONE;
            Warrior warrior = unit.getUnit().getWarrior();
            BattleCell[][] battleCells = battle.getMap().getCells();
            if (warrior.getX() > 0 && warrior.getY() > 0 && BattleMapHelper.isBarrier(battleCells[warrior.getX() - 1][warrior.getY() - 1])) {
                barrierBits |= Dir.NORTHWEST;
            }
            if (warrior.getY() > 0 && BattleMapHelper.isBarrier(battleCells[warrior.getX()][warrior.getY() - 1])) {
                barrierBits |= Dir.NORTH;
            }
            if (warrior.getX() < battleCells.length - 1 && warrior.getY() > 0 && BattleMapHelper.isBarrier(battleCells[warrior.getX() + 1][warrior.getY() - 1])) {
                barrierBits |= Dir.NORTHEAST;
            }
            if (warrior.getX() < battleCells.length - 1 && BattleMapHelper.isBarrier(battleCells[warrior.getX() + 1][warrior.getY()])) {
                barrierBits |= Dir.EAST;
            }
            if (warrior.getX() < battleCells.length - 1 && warrior.getY() < battleCells[0].length - 1 && BattleMapHelper.isBarrier(battleCells[warrior.getX() + 1][warrior.getY() + 1])) {
                barrierBits |= Dir.SOUTHEAST;
            }
            if (warrior.getY() < battleCells[0].length - 1 && BattleMapHelper.isBarrier(battleCells[warrior.getX()][warrior.getY() + 1])) {
                barrierBits |= Dir.SOUTH;
            }
            if (warrior.getX() > 0 && warrior.getY() < battleCells[0].length - 1 && BattleMapHelper.isBarrier(battleCells[warrior.getX() - 1][warrior.getY() + 1])) {
                barrierBits |= Dir.SOUTHWEST;
            }
            if (warrior.getX() > 0 && BattleMapHelper.isBarrier(battleCells[warrior.getX() - 1][warrior.getY()])) {
                barrierBits |= Dir.WEST;
            }
            Index index = null;
            if ((barrierBits & (Dir.WEST | Dir.NORTHWEST | Dir.NORTH)) == (Dir.WEST | Dir.NORTHWEST | Dir.NORTH)) {
                index = symbolOfProtectionObject.getIndexBySlot(1);
            } else
            if ((barrierBits & (Dir.NORTH | Dir.NORTHEAST | Dir.EAST)) == (Dir.NORTH | Dir.NORTHEAST | Dir.EAST)) {
                index = symbolOfProtectionObject.getIndexBySlot(3);
            } else
            if ((barrierBits & (Dir.EAST | Dir.SOUTHEAST | Dir.SOUTH)) == (Dir.EAST | Dir.SOUTHEAST | Dir.SOUTH)) {
                index = symbolOfProtectionObject.getIndexBySlot(5);
            } else
            if ((barrierBits & (Dir.SOUTH | Dir.SOUTHWEST | Dir.WEST)) == (Dir.SOUTH | Dir.SOUTHWEST | Dir.WEST)) {
                index = symbolOfProtectionObject.getIndexBySlot(7);
            } else
            if ((barrierBits & Dir.NORTH) == Dir.NORTH) {
                index = symbolOfProtectionObject.getIndexBySlot(2);
            } else
            if ((barrierBits & Dir.EAST) == Dir.EAST) {
                index = symbolOfProtectionObject.getIndexBySlot(4);
            } else
            if ((barrierBits & Dir.SOUTH) == Dir.SOUTH) {
                index = symbolOfProtectionObject.getIndexBySlot(6);
            } else
            if ((barrierBits & Dir.WEST) == Dir.WEST) {
                index = symbolOfProtectionObject.getIndexBySlot(8);
            }
            if (index != null) {
                Pair pair = coordinateFinder.find(warrior.getY(), warrior.getX(), this);
                index.draw(graphics, pair.getX() - mapX, pair.getY() - mapY);
            }
        }
        unit.getUnit().draw(graphics, unit.getMapX() - mapX, unit.getMapY() - mapY);
    }
    */
    private void drawBattleMap(Graphics graphics) {
        BattleCell[][] cells = battle.getMap().getCells();
        int length = cells.length;
        int x, y;
        for (int yCell = 0; yCell < length; yCell++) {
            y = yCell * VERTICAL_RADIUS;
            x = (length - 1 + yCell) * HORIZONTAL_RADIUS;
            for (int xCell = 0; xCell < length; xCell++) {
                if (isOnTheScreen(x, y)) {
                    drawCell(graphics, x - mapX, y - mapY, cells[yCell][xCell]);
                }
                x -= HORIZONTAL_RADIUS;
                y += VERTICAL_RADIUS;
            }
        }
    }

    private void drawGround(Graphics graphics) {
        int x = -mapX % GROUND_WIDTH - HORIZONTAL_RADIUS;
        int tmp = -mapY % GROUND_HEIGHT - VERTICAL_RADIUS;
        while (x < getWidth()) {
            int y = tmp;
            while (y < getHeight()) {
                Environment.getRender().getSprite(1).draw(graphics, x, y);
                y += GROUND_HEIGHT;
            }
            x += GROUND_WIDTH;
        }
        // Рисуем границу вокруг игрового поля
        graphics.setColor(0x0000FF);
        BattleCell[][] battleCells = battle.getMap().getCells();
        int[] points = new int[8];
        Pair pair = coordinateFinder.find(0, 0, this);
        points[0] = pair.getX() - mapX;
        points[1] = pair.getY() - VERTICAL_RADIUS - mapY;
        pair = coordinateFinder.find(0, battleCells[0].length - 1, this);
        points[2] = pair.getX() + HORIZONTAL_RADIUS - mapX;
        points[3] = pair.getY() - mapY;
        pair = coordinateFinder.find(battleCells.length - 1, battleCells[0].length - 1, this);
        points[4] = pair.getX() - mapX;
        points[5] = pair.getY() + VERTICAL_RADIUS - mapY;
        pair = coordinateFinder.find(battleCells.length - 1, 0, this);
        points[6] = pair.getX() - HORIZONTAL_RADIUS - mapX;
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

    public void onTimer(int timerId) {
        for (int i = 0; i < groupUnits.size(); i++) {
            ((ClientHumanElement) groupUnits.get(i)).onTick();
        }
        for (int i = 0; i < allyUnits.size(); i++) {
            ((ClientHumanElement) allyUnits.get(i)).onTick();
        }
        for (int i = 0; i < enemyUnits.size(); i++) {
            ((ClientHumanElement) enemyUnits.get(i)).onTick();
        }
    }

    public boolean onEvent(int code, int param, int x, int y) {
        switch (code) {
            case Event.EVENT_KEY_UP:
                showGrid = false;
                break;
            case Event.EVENT_KEY_DOWN:
                showGrid = true;
                break;
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
                if (isMyTurn()) {
                    if (isOnTheMap(x, y)) {
                        if (Mathematics.abs(touchedXX - x) <= SPOT && Mathematics.abs(touchedYY - y) <= SPOT) {
                            Pair cellCoordinate = cellFinder.find(x + mapX, y + mapY, this);
                            BattleCell battleCell = battle.getMap().getCells()[cellCoordinate.getX()][cellCoordinate.getY()];
                            CellElement[] elements = battleCell.getElements();
                            for (int i = 0; i < battleCell.getSize(); i++) {
                                CellElement element = elements[i];
                                if (element.getElementType() == CellElementTypes.HUMAN) {
                                    ClientHumanElement humanElement = (ClientHumanElement) element;
                                    Debug.debug("Is warrior " + humanElement.getHuman().getName());
                                    if (humanElement.getHuman().getBattleGroup() == battleGroup) {
                                        setActiveUnit(humanElement);
                                        break;
                                    }
                                } else if (BattleMapHelper.isReachable(battleCell) && activeUnit.getLogic().isIdle()) {
                                    Debug.debug("Trace for a user " + activeUnit.getHuman().getNumber());
                                    ClientBattleHelper.trace(battle.getMap().getCells(), activeUnit, cellCoordinate.getX(), cellCoordinate.getY(),
                                            ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
                                } else {
                                    Debug.debug("Cell " + cellCoordinate.getX() + ":" + cellCoordinate.getY() + " is not reachable");
                                }
                            }
                        }
                    }
                }
                break;
            case Event.EVENT_TOUCH_DOUBLE_CLICK:
                if (isMyTurn()) {
                    Debug.debug("My turn & i want to move " + x + ":" + y);
                    if (isOnTheMap(x, y)) {
                        Pair cellCoordinate = cellFinder.find(x + mapX, y + mapY, this);
                        Debug.debug("A cell to go " + cellCoordinate.getX() + ":" + cellCoordinate.getY() + " action scores " + ((Warrior) activeUnit.getHuman()).getActionScore());
                        if (BattleMapHelper.isShortestPathCell(battle.getMap().getCells()[cellCoordinate.getX()][cellCoordinate.getY()], activeUnit)) {
                            Debug.debug("Move an user " + activeUnit.getHuman().getNumber());
                            moveUser(cellCoordinate.getX(), cellCoordinate.getY());
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
            case Packets.CHANGE_ACTIVE_ALLIANCE:
                BattleConfiguration battleConfiguration = configuration.getBattleConfiguration();
//                //to do: Вызов следующих трех методов под вопросом
                //todo: Все динамические элементы (юниты, двери) должны обновляться паралельно
                //todo: Это вообще не требуется. Новый ход не начнется, пока все пользователи не подтвердят свое завершение хода. Но и сервер не должен давать возможность выполнять ходы за пределами 30-секундного хода
                quicklyCompleteAllCommandsForUnits(groupUnits);
                quicklyCompleteAllCommandsForUnits(allyUnits);
                quicklyCompleteAllCommandsForUnits(enemyUnits);
//                ClientBattleHelper.immediateMoveAllies(groupUnits, battle, battleConfiguration, this);
//                ClientBattleHelper.immediateMoveAllies(allyUnits, battle, battleConfiguration, this);
//                ClientBattleHelper.immediateMoveEnemies(enemyUnits, battle, this);

                ClientChangeActiveAllianceAnswer change = (ClientChangeActiveAllianceAnswer) message;
                if (isMyTurn()) {
                    onMyTurnFinished();
                }
                activeAlliance = change.getAlliance();
                if (isMyTurn()) {
                    onMyTurnStarted();
                }
                onChangeActiveAlliance(activeAlliance);
                break;
            case Packets.MOVE_WARRIOR:
                break;
            case Packets.MOVE_ALLY:
                break;
            case Packets.MOVE_ENEMY:
                break;
        }
    }

    private void quicklyCompleteAllCommandsForUnits(ClientHumanElementCollection collection) {
        for (int i = 0; i < collection.size(); i++) {
            ClientHumanElement unit = (ClientHumanElement) collection.get(i);
            unit.getLogic().quicklyCompleteAllCommands();
        }
    }

    public void moveUser(int x, int y) {
//        try {
//            Warrior warrior = activeUnit.getUnit().getWarrior();
//            ClientMoveMyWarriorAnswer move = (ClientMoveMyWarriorAnswer) configuration.getBattleServiceManager().move(warrior, (short) x, (short) y);
//            short xx = move.getX();
//            short yy = move.getY();
//            if (xx != x || yy != y) {
//                ClientBattleHelper.trace(warrior, xx, yy);
//            }
        activeUnit.getLogic().doRun();
//            getStep(activeUnit).init();
//        } catch (Exception e) {
//            NotificationBox.error(LocalizedStrings.MOVEMENT_RESTRICTION);
//            Debug.error(LocalizedStrings.MOVEMENT_RESTRICTION, e);
//        }
    }

//    /**
//     * Разрешить двинуть бойца warrior принадлежащего союзнику в точку (x;y)
//     *
//     * @param warrior
//     * @param x
//     * @param y
//     */
//    private void moveAlly(Warrior warrior, int x, int y) throws RegolithException {
//        ClientBattleHelper.trace(warrior, x, y, ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
//        getStep(BattleMapHelper.getHumanElementByHuman(allyUnits, warrior)).init();
//    }


    /**
     * Покрывает ли наш экран эту точку карты?
     *
     * @param x координата карты
     * @param y координата карты
     * @return
     */
    public boolean isOnTheScreen(int x, int y) {
        return mapX <= x + HORIZONTAL_RADIUS && x - HORIZONTAL_RADIUS <= mapX + getWidth() && mapY <= y + VERTICAL_RADIUS && y - VERTICAL_RADIUS <= mapY + getHeight();
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
        return -TANGENT * xx + b4 <= yy
                &&
                TANGENT * xx + b1 <= yy
                &&
                -TANGENT * xx + b2 >= yy
                &&
                TANGENT * xx + b3 >= yy;
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

    /**
     * Вернуть объект битва отрисовка которой происходит с помощью экрана.
     *
     * @return
     */
    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    /**
     * Моя ли очередь ходить?
     *
     * @return
     */
    public boolean isMyTurn() {
        return battleGroup.getAlliance() == activeAlliance;
    }

    /**
     * Вернуть активный боевой союз.
     *
     * @return
     */
    public BattleAlliance getActiveAlliance() {
        return activeAlliance;
    }

    public void setActiveAlliance(BattleAlliance activeAlliance) {
        this.activeAlliance = activeAlliance;
    }

    /**
     * Вернуть своего активного бойца.
     *
     * @return
     */
    public ClientHumanElement getActiveUnit() {
        return activeUnit;
    }

    public ClientHumanElementCollection getGroupUnits() {
        return groupUnits;
    }

    public ClientHumanElementCollection getAllyUnits() {
        return allyUnits;
    }

    public ClientHumanElementCollection getEnemyUnits() {
        return enemyUnits;
    }

    public void setGroupUnits(ClientHumanElementCollection list) {
        groupUnits = list;
    }

    public void setAllyUnits(ClientHumanElementCollection list) {
        allyUnits = list;
    }

    public void setEnemyUnits(ClientHumanElementCollection list) {
        enemyUnits = list;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean value) {
        showGrid = value;
    }

    @Override
    public void onShow() {
        if (battle != null) {
            Account account = ClientConfigurationFactory.getConfiguration().getAccount();
            //todo тут ли устанавливать SecurityOperationManager
            account.setSecurity(new SecurityOperationManager());
            account.getSecurity().setAccount(account);

//            groupUnits = ClientBattleHelper.getBattleUnits(battle, account);
//            allyUnits = ClientBattleHelper.getAllyBattleUnits(battle, account);
//            enemyUnits = ClientBattleHelper.getEnemyBattleUnits(battle, account);

            battleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(battle, account.getId());

            int length = battle.getMap().getCells().length;

            topCenter = new Pair();
            topCenter.setX((length - 1) * BattleScreen.HORIZONTAL_RADIUS);
            topCenter.setY(0);

            centerRight = new Pair();
            centerRight.setX((length - 1) * BattleScreen.HORIZONTAL_DIAGONAL);
            centerRight.setY(topCenter.getY() + (length - 1) * BattleScreen.VERTICAL_RADIUS);

            bottomCenter = new Pair();
            bottomCenter.setX(topCenter.getX());
            bottomCenter.setY(topCenter.getY() + (length - 1) * BattleScreen.VERTICAL_DIAGONAL);

            centerLeft = new Pair();
            centerLeft.setX(0);
            centerLeft.setY(centerRight.getY());

            b1 = (int) (topCenter.getY() - (topCenter.getX()) * TANGENT) - VERTICAL_RADIUS;
            b2 = (int) (centerRight.getY() + centerRight.getX() * TANGENT) + VERTICAL_RADIUS;
            b3 = (int) (bottomCenter.getY() - bottomCenter.getX() * TANGENT) + VERTICAL_RADIUS;
            b4 = (int) (centerLeft.getY() + centerLeft.getX() * TANGENT) - VERTICAL_RADIUS;
            center = new Pair();
            showGrid = true;

            BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
            BattleAlliance alliance = battleGroup.getAlliance();
            ExitZone exit = alliance.getExit();
            setCellCenter(exit.getX(), exit.getY());
            activeUnit = null;
            initUnitFromCollection(groupUnits, battleConfiguration);
            initUnitFromCollection(allyUnits, battleConfiguration);

//            BattleGroupCollection clients = alliance.getAllies();
//            for (int j = 0; j < clients.size(); j++) {
//                if (battleGroup == clients.get(j)) {
//                    WarriorCollection warriors = battleGroup.getWarriors();
//                    for (int i = 0; i < warriors.size(); i++) {
//                        ClientHumanElement unit = BattleMapHelper.getHumanElementByHuman(groupUnits, warriors.get(i));
//                        initBattleUnit(unit, battleConfiguration);
//                        if (newActiveUnit == null) {
//                            newActiveUnit = unit;
//                        }
//                    }
//                } else {
//                    WarriorCollection warriors = clients.get(j).getWarriors();
//                    for (int i = 0; i < warriors.size(); i++) {
//                        ClientHumanElement unit = BattleMapHelper.getHumanElementByHuman(allyUnits, warriors.get(i));
//                        initBattleUnit(unit, battleConfiguration);
//                    }
//                }
//            }
            setActiveUnit(getFirstLiveUnit()); //todo: нужно ли?
            timerId = TimerManager.setPeriodicTimer(100, this);

            ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
        }
    }

    @Override
    public void onHide() {
        TimerManager.killTimer(timerId);
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
    }

    private void initUnitFromCollection(ClientHumanElementCollection collection, BattleConfiguration battleConfiguration) {
        for (int i = 0; i < collection.size(); i++) {
            ClientHumanElement unit = (ClientHumanElement) collection.get(i);
            ClientBattleHelper.observe(unit, battleConfiguration);
            ClientBattleHelper.initMapXY(this, unit);
        }
    }

    public void putEnemyInPosition(HumanElement unit, int x, int y) {
        Pair coordinates = coordinateFinder.find(x, y, this); //todo: Здесь x и y не перепутаны местами?
        WarriorHelper.putWarriorIntoMap(battle.getMap().getCells(), unit, x, y);
        unit.setMapX((short) coordinates.getX());
        unit.setMapY((short) coordinates.getY());
    }

    public ClientHumanElement getFirstLiveUnit() {
        return (ClientHumanElement) groupUnits.get(0);
    }

    /**
     * Обработчик события сообщающего о начале хода.
     */
    public void onMyTurnStarted() {
        Debug.debug("My turn has begun");
        ClientBattleHelper.resetActionScores(groupUnits, configuration.getBaseConfiguration());
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
    public void setActiveUnit(ClientHumanElement unit) {
//        if (this.activeUnit != unit) {
        this.activeUnit = unit;
        onChangeActiveUnit();
//        }
    }

    /**
     * Обработчик события сообщающего об изменении активного бойца.
     */
    private void onChangeActiveUnit() {
        Debug.debug("The current user number = " + activeUnit.getHuman().getNumber());
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattleWarriorListPanel().onActiveUnitChanged(activeUnit);
        panelManager.getBattleMenuPanel().onActiveUnitChanged(activeUnit);
        panelManager.getBattleWeaponMenuPanel().onActiveUnitChanged(activeUnit);
        panelManager.getBattleWarriorMenuPanel().onActiveUnitChanged(activeUnit);
        panelManager.getBattleShotMenuPanel().onActiveUnitChanged(activeUnit);
        doMapReachabilityUpdate();
    }

    //todo: Избавиться от этого метода
    public void doMapReachabilityUpdate() {
        ClientBattleHelper.route(this.battle.getMap().getCells(), activeUnit,
                ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
    }

    /**
     * Обработчик события сообщающего об окончании выполнения активным бойцом всех своих операций, боец сейчас находится
     * в состоянии ожидания команд.
     */
    public void onOperationOfUnitIsCompleted(ClientHumanElement unit) {
        // здесь должен быть код, который должен определить что находится вокруг бойца и, в зависимости от его окружения,
        // может быть отображена соответствующая кнопка. Например, если рядом с бойцом находится дверь, то должна
        // появиться кнопка открытия/закрытия двери.

        // Что может находится рядом с бойцом                  кнопка с каким действием появится
        // * дверь                                             - открыть/закрыть дверь
        // * кучка реголита                                    - собрать реголит
        // * вражеский боец + выбрано оружие "Нож"             - удар ножом
        // * зона выхода с карты                               - покинуть поле боя
        // * предмет (аптечка, ящик с амуницией, боеприпасами) - подобрать предмет
    }

    /**
     * Центрировать область просмотра на ячейке карты.
     */
    public void displayCell(int cellX, int cellY) {

    }

    /**
     * Центрировать область просмотра на бойце battleUnit.
     *
     * @param unit
     */
    public void displayWarrior(ClientHumanElement unit) {

    }

}
