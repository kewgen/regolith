package com.geargames.regolith.units;

import com.geargames.awt.Screen;
import com.geargames.common.env.Environment;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.DataMessageListener;
import com.geargames.common.packer.PObject;
import com.geargames.common.packer.PSprite;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.util.ArrayList;
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
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.*;

/**
 * User: mkutuzov
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

    private ArrayList enemies;
    private ArrayList allies;
    private ArrayList group;

    private ArrayList steps;
    private Battle battle;
    private BattleGroup battleGroup;

    private BattleUnit user;
    private MapCorrector corrector;
    private Finder cellFinder;
    private Finder coordinateFinder;
    private boolean showGrid;

    private int mapX;
    private int mapY;
    private int netColor;

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
    private PSprite areaAttainabilitySprite;

    public BattleScreen() {
        steps = new ArrayList();
        timerId = TimerManager.NULL_TIMER;
        configuration = ClientConfigurationFactory.getConfiguration();
        listenedTypes = new short[]{Packets.MOVE_WARRIOR, Packets.MOVE_ALLY, Packets.MOVE_ENEMY, Packets.SHOOT, Packets.CHANGE_ACTIVE_ALLIANCE};
        cellFinder = configuration.getCellFinder();
        coordinateFinder = configuration.getCoordinateFinder();
        netColor = 255;

        shadowSprite = Environment.getRender().getSprite(Graph.SPR_SHADOW);
        areaAttainabilitySprite = Environment.getRender().getSprite(Graph.SPR_DISTANCE);
    }

    public void draw(Graphics graphics) {
        drawGround(graphics);
        drawBattleMap(graphics);
        drawGroup(graphics);
    }

    /**
     * Прорисовываем содержимое клетки и изометрическую клетку карты, по координатам её центра(x;y) на
     * графическом контексте graphics.
     *
     * @param graphics
     * @param x
     * @param y
     * @param cell
     * @param path
     */
    private void drawCell(Graphics graphics, int x, int y, BattleCell cell, boolean path) {
        if (showGrid) {
            if (isMyTurn() && cell.getOrder() != BattleMapHelper.UN_ROUTED) {
                areaAttainabilitySprite.draw(graphics, x, y);
            }
            graphics.drawLine(x, y - VERTICAL_RADIUS, x + HORIZONTAL_RADIUS, y);
            graphics.drawLine(x + HORIZONTAL_RADIUS, y, x, y + VERTICAL_RADIUS);
            graphics.drawLine(x, y + VERTICAL_RADIUS, x - HORIZONTAL_RADIUS, y);
            graphics.drawLine(x - HORIZONTAL_RADIUS, y, x, y - VERTICAL_RADIUS);
        }
        if (path) {
            shadowSprite.draw(graphics, x - 29/*width*/, y - 20/*height*/);
        }
        if (showGrid) {
            graphics.drawString("" + cell.getOrder(), x, y, com.geargames.common.Graphics.HCENTER);
        }
        CellElement[] elements = cell.getElements();
        for (int i = 0; i < cell.getSize(); i++) {
            PObject obj = Environment.getRender().getObject(elements[i].getFrameId());
            if (obj != null) {
                obj.draw(graphics, x, y);
            }
        }
    }

    /**
     * Рисуем бойцов которые принадлежат клиентскому приложению.
     *
     * @param graphics
     */
    private void drawGroup(Graphics graphics) {
        for (int i = 0; i < group.size(); i++) {
            BattleUnit battleUnit = (BattleUnit) group.get(i);
            if (isOnTheScreen(battleUnit.getMapX(), battleUnit.getMapY())) {
                battleUnit.getUnit().draw(graphics, battleUnit.getMapX() - mapX, battleUnit.getMapY() - mapY);
            }
        }
    }

    private void drawBattleMap(Graphics graphics) {
        graphics.setColor(netColor);
        BattleCell[][] cells = battle.getMap().getCells();
        int length = cells.length;
        int x, y;
        Warrior warrior = user.getUnit().getWarrior();
        for (int yCell = 0; yCell < length; yCell++) {
            y = yCell * VERTICAL_RADIUS;
            x = (length - 1 + yCell) * HORIZONTAL_RADIUS;
            for (int xCell = 0; xCell < length; xCell++) {
                if (isOnTheScreen(x, y)) {
                    if (BattleMapHelper.isShortestPathCell(cells[yCell][xCell], warrior)) {
                        drawCell(graphics, x - mapX, y - mapY, cells[yCell][xCell], true);
                    } else {
                        drawCell(graphics, x - mapX, y - mapY, cells[yCell][xCell], false);
                    }
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
        for (int i = 0; i < steps.size(); i++) {
            ((Step) steps.get(i)).onTick();
        }
        for (int i = 0; i < group.size(); i++) {
            ((BattleUnit) group.get(i)).getUnit().next();
        }
        for (int i = 0; i < allies.size(); i++) {
            ((BattleUnit) allies.get(i)).getUnit().next();
        }
        for (int i = 0; i < enemies.size(); i++) {
            ((BattleUnit) enemies.get(i)).getUnit().next();
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
                            Pair cell = cellFinder.find(x + mapX, y + mapY, this);
                            BattleCell battleCell = battle.getMap().getCells()[cell.getX()][cell.getY()];
                            CellElement element = battleCell.getElement();
                            Warrior warrior = user.getUnit().getWarrior();
                            if (element != null && element.getElementType() == CellElementTypes.HUMAN && ((Warrior) element).getBattleGroup() == battleGroup) {
                                doChangeActiveWarrior((Warrior) element);
//                                user = ClientBattleHelper.findBattleUnitByWarrior(group, (Warrior) element);
//                                ClientBattleHelper.route(warrior, ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
//                                Debug.debug("the current user number = " + warrior.getNumber());
                            } else if (BattleMapHelper.isReachable(battleCell) && !warrior.isMoving()) {
                                Debug.debug("trace for a user " + warrior.getNumber());
                                ClientBattleHelper.trace(user.getUnit().getWarrior(), cell.getX(), cell.getY());
                            } else {
                                Debug.debug("point " + cell.getX() + ":" + cell.getY() + " is not reachable");
                            }
                            Debug.debug("is warrior " + warrior.getName() + " moving? = " + warrior.isMoving());
                        }
                    }
                }
                break;
            case Event.EVENT_TOUCH_DOUBLE_CLICK:
                if (isMyTurn()) {
                    Debug.debug("my turn & i want to move " + x + ":" + y);
                    if (isOnTheMap(x, y)) {
                        Pair cell = cellFinder.find(x + mapX, y + mapY, this);
                        Debug.debug("a cell to go " + cell.getX() + ":" + cell.getY() + " action scores " + user.getUnit().getWarrior().getActionScore());
                        if (BattleMapHelper.isShortestPathCell(battle.getMap().getCells()[cell.getX()][cell.getY()], user.getUnit().getWarrior())) {
                            System.out.println("move an user " + user.getUnit().getWarrior().getNumber());
                            moveUser(cell.getX(), cell.getY());
                        } else {
                            Debug.debug("Not a shortest path");
                        }
                    } else {
                        Debug.debug("out of the map touch");
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
                ClientBattleHelper.immediateMoveAllyies(group, battle, battleConfiguration, this);
                ClientBattleHelper.immediateMoveAllyies(allies, battle, battleConfiguration, this);
                ClientBattleHelper.immediateMoveEnemies(enemies, battle, this);

                ClientChangeActiveAllianceAnswer change = (ClientChangeActiveAllianceAnswer) message;
                if (isMyTurn()) {
                    Debug.debug("my turn has been finished");
                    configuration.getBattleServiceManager().checkSum();
                }
                activeAlliance = change.getAlliance();
                if (isMyTurn()) {
                    Debug.debug("my turn has begun");
                    ClientBattleHelper.resetActionScores(group, configuration.getBaseConfiguration());
                }
                onChangeActiveAlliance(change.getAlliance());
                break;
            case Packets.MOVE_WARRIOR:

                break;
            case Packets.MOVE_ALLY:
                break;
            case Packets.MOVE_ENEMY:
                break;
        }
    }

    private Step getStep(BattleUnit unit) {
        for (int i = 0; i < steps.size(); i++) {
            if (((Step) steps.get(i)).getBattleUnit() == unit) {
                return (Step) steps.get(i);
            }
        }
        return null;
    }

    public void moveUser(int x, int y) {
//        try {
//            Warrior warrior = user.getUnit().getWarrior();
//            ClientMoveMyWarriorAnswer move = (ClientMoveMyWarriorAnswer) configuration.getBattleServiceManager().move(warrior, (short) x, (short) y);
//            short xx = move.getX();
//            short yy = move.getY();
//            if (xx != x || yy != y) {
//                ClientBattleHelper.trace(warrior, xx, yy);
//            }
            getStep(user).init();
//        } catch (Exception e) {
//            NotificationBox.error(LocalizedStrings.MOVEMENT_RESTRICTION);
//            Debug.error(LocalizedStrings.MOVEMENT_RESTRICTION, e);
//        }
    }

    /**
     * Разрешить двинуть бойца warrior принадлежащего союзнику в точку (x;y)
     *
     * @param warrior
     * @param x
     * @param y
     */
    private void moveAlly(Warrior warrior, int x, int y) {
        ClientBattleHelper.trace(warrior, x, y);
        getStep(ClientBattleHelper.findBattleUnitByWarrior(allies, warrior)).init();
    }


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
    public BattleUnit getUser() {
        return user;
    }

    public ArrayList getGroup() {
        return group;
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

            group = ClientBattleHelper.getBattleUnits(battle, account);
            allies = ClientBattleHelper.getAllyBattleUnits(battle, account);
            enemies = ClientBattleHelper.getEnemyBattleUnits(battle, account);

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
            BattleGroupCollection clients = alliance.getAllies();
            steps.clear();
            for (int j = 0; j < clients.size(); j++) {
                if (battleGroup == clients.get(j)) {
                    WarriorCollection warriors = battleGroup.getWarriors();
                    for (int i = 0; i < warriors.size(); i++) {
                        user = ClientBattleHelper.findBattleUnitByWarrior(group, warriors.get(i));
                        initBattleUnit(user, battleConfiguration);
                    }
                    ClientBattleHelper.route(user.getUnit().getWarrior(), battleConfiguration);
                } else {
                    WarriorCollection warriors = clients.get(j).getWarriors();
                    for (int i = 0; i < warriors.size(); i++) {
                        BattleUnit unit = ClientBattleHelper.findBattleUnitByWarrior(allies, warriors.get(i));
                        initBattleUnit(unit, battleConfiguration);
                    }
                }
            }
            timerId = TimerManager.setPeriodicTimer(100, this);

            ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
        }
    }

    private void initBattleUnit(BattleUnit unit, BattleConfiguration battleConfiguration) {
        ClientBattleHelper.observe(unit.getUnit().getWarrior(), battleConfiguration);
        ClientBattleHelper.initMapXY(this, unit);
        Step step = new AllyStep();
        step.setScreen(this);
        step.setBattleUnit(unit);
        steps.add(step);
    }

    public void putEnemyInPosition(BattleUnit enemy, int x, int y) {
        Pair coordinates = coordinateFinder.find(x, y, this);
        WarriorHelper.putWarriorIntoMap(enemy.getUnit().getWarrior(), battle.getMap(), x, y);
        enemy.setMapX(coordinates.getX());
        enemy.setMapY(coordinates.getY());
    }

    @Override
    public void onHide() {
        TimerManager.killTimer(timerId);
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
    }

    /**
     * Обработчик события об изменении активного военного союза, того чей, в данный момент, ход.
     */
    public void onChangeActiveAlliance(BattleAlliance alliance) {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getHeadlinePanel().setActiveAlliance(alliance);
        panelManager.getBattleMenuPanel().onActiveAllianceChanged(alliance);
        if (isMyTurn()) {
            panelManager.show(panelManager.getBattleWarriorListWindow());
            panelManager.show(panelManager.getBattleWeaponMenuWindow());
            panelManager.show(panelManager.getBattleWarriorMenuWindow());
            panelManager.show(panelManager.getBattleShotMenuWindow());
        } else {
            panelManager.hide(panelManager.getBattleWarriorListWindow());
            panelManager.hide(panelManager.getBattleWeaponMenuWindow());
            panelManager.hide(panelManager.getBattleWarriorMenuWindow());
            panelManager.hide(panelManager.getBattleShotMenuWindow());
        }
    }

    public void doChangeActiveWarrior(Warrior warrior) {
        setActiveUnit(ClientBattleHelper.findBattleUnitByWarrior(group, warrior));
    }

    /**
     * Обработчик события об изменении активного бойца.
     * @param unit
     */
    public void setActiveUnit(BattleUnit unit) {
        if (this.user != unit) {
            Debug.debug("The current user number = " + unit.getUnit().getWarrior().getNumber());
            this.user = unit;
            doMapReachabilityUpdate();
            PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
            panelManager.getBattleWarriorListPanel().onActiveUnitChanged(user);
            panelManager.getBattleMenuPanel().onActiveUnitChanged(user);
            panelManager.getBattleWeaponMenuPanel().onActiveUnitChanged(user);
            panelManager.getBattleWarriorMenuPanel().onActiveUnitChanged(user);
            panelManager.getBattleShotMenuPanel().onActiveUnitChanged(user);
        }
    }

    public void doMapReachabilityUpdate() {
        ClientBattleHelper.route(user.getUnit().getWarrior(), ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
    }

    /**
     * Центрировать область просмотра на ячейке карты.
     */
    public void displayCell(int cellX, int cellY) {

    }

    /**
     * Центрировать область просмотра на бойце warrior.
     * @param warrior
     */
    public void displayWarrior(Warrior warrior) {

    }

    public ArrayList getAllies() {
        return allies;
    }

    public ArrayList getEnemies() {
        return enemies;
    }

}
