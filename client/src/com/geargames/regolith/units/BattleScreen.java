package com.geargames.regolith.units;

import com.geargames.awt.Screen;
import com.geargames.common.env.Environment;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.DataMessageListener;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.util.ArrayList;
import com.geargames.common.util.Mathematics;
import com.geargames.common.Graphics;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.application.Event;
import com.geargames.regolith.Graph;
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

import java.util.Vector;

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
    public static double TANGENS = (VERTICAL_RADIUS + 0.0) / (HORIZONTAL_RADIUS + 0.0);

    private ArrayList enemies;
    private ArrayList allies;
    private ArrayList group;

    private Vector steps;
    private Battle battle;
    private BattleGroup battleGroup;

    private BattleUnit user;
    private MapCorrector corrector;
    private Finder cellFinder;
    private Finder coordinateFinder;
    private boolean myTurn;
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

    private int timerId;

    public BattleScreen() {
        steps = new Vector();
        timerId = -1;
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
            graphics.drawLine(x, y - VERTICAL_RADIUS, x + HORIZONTAL_RADIUS, y);
            graphics.drawLine(x + HORIZONTAL_RADIUS, y, x, y + VERTICAL_RADIUS);
            graphics.drawLine(x, y + VERTICAL_RADIUS, x - HORIZONTAL_RADIUS, y);
            graphics.drawLine(x - HORIZONTAL_RADIUS, y, x, y - VERTICAL_RADIUS);
            graphics.drawString("" + cell.getOrder(), x, y, com.geargames.common.Graphics.HCENTER);
        }
        CellElement[] elements = cell.getElements();
        for (int i = 0; i < cell.getSize(); i++) {
            PObject obj = Environment.getRender().getObject(elements[i].getFrameId());
            if (obj != null) {
                obj.draw(graphics, x, y);
            }
        }
        if (path) {
            Environment.getRender().getSprite(Graph.SPR_SHADOW).draw(graphics, x, y);
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
        for (int yCell = 0; yCell < length; yCell++) {
            y = yCell * VERTICAL_RADIUS;
            x = (length - 1 + yCell) * HORIZONTAL_RADIUS;
            for (int xCell = 0; xCell < length; xCell++) {
                if (isOnTheScreen(x, y)) {
                    if (BattleMapHelper.isShortestPathCell(cells[yCell][xCell], user.getUnit().getWarrior())) {
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
            ((Step) steps.elementAt(i)).onTick();
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
                if (myTurn) {
                    if (isOnTheMap(x, y)) {
                        if (Mathematics.abs(touchedXX - x) <= SPOT && Mathematics.abs(touchedYY - y) <= SPOT) {
                            Pair cell = cellFinder.find(x + mapX, y + mapY, this);
                            BattleCell battleCell = battle.getMap().getCells()[cell.getX()][cell.getY()];
                            CellElement element = battleCell.getElement();
                            if (element != null && element.getElementType() == CellElementTypes.HUMAN && ((Warrior) element).getBattleGroup() == battleGroup) {
                                user = ClientBattleHelper.getBattleUnitByWarrior(group, (Warrior) element);
                                ClientBattleHelper.route(user.getUnit().getWarrior(), ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
                                Debug.debug("the current user number = " + user.getUnit().getWarrior().getNumber());
                            } else if (BattleMapHelper.isReachable(battleCell) && !user.getUnit().getWarrior().isMoving()) {
                                ClientBattleHelper.trace(user.getUnit().getWarrior(), cell.getX(), cell.getY());
                                Debug.debug("trace for a user " + user.getUnit().getWarrior().getNumber());
                            }
                        }
                    }
                }
                break;
            case Event.EVENT_TOUCH_DOUBLE_CLICK:
                if (myTurn) {
                    if (isOnTheMap(x, y)) {
                        Pair cell = cellFinder.find(x + mapX, y + mapY, this);
                        if (BattleMapHelper.isShortestPathCell(battle.getMap().getCells()[cell.getX()][cell.getY()], user.getUnit().getWarrior())) {
                            //ClientBattleHelper.move(this, cell.getX(), cell.getY());
                            System.out.println("move an user " + user.getUnit().getWarrior().getNumber());
                            moveUser(cell.getX(), cell.getY());
                        }
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
        return new short[]{Packets.MOVE_ALLY, Packets.MOVE_ENEMY, Packets.SHOOT, Packets.CHANGE_ACTIVE_ALLIANCE};
    }

    @Override
    public void onReceive(ClientDeSerializedMessage message, short type) {
        switch (type) {
            case Packets.CHANGE_ACTIVE_ALLIANCE:
                ClientChangeActiveAllianceAnswer change = (ClientChangeActiveAllianceAnswer) message;
                myTurn = WarriorHelper.isAlly(change.getAlliance(), ClientConfigurationFactory.getConfiguration().getAccount());
                if (myTurn) {
                    ClientBattleHelper.resetActionScores(group);
                }
                break;
            case Packets.MOVE_ALLY:
                break;
        }

    }

    private Step getStep(BattleUnit unit) {
        for (int i = 0; i < steps.size(); i++) {
            if (((Step) steps.elementAt(i)).getBattleUnit() == unit) {
                return (Step) steps.elementAt(i);
            }
        }
        return null;
    }

    public void moveUser(int x, int y) {
        getStep(user).init();
    }

    /**
     * Разрешить двинуть бойца warrior принадлежащего союзуснику в точку x;y
     *
     * @param warrior
     * @param x
     * @param y
     */
    public void moveAlly(Warrior warrior, int x, int y) {
        ClientBattleHelper.trace(warrior, x, y);
        getStep(ClientBattleHelper.getBattleUnitByWarrior(allies, warrior)).init();
    }

    /**
     * Разрешить двинуть бойца warrior пренадлежащего врагу в точку x;y
     *
     * @param warrior
     * @param x
     * @param y
     */
    public void moveEnemy(Warrior warrior, short[] x, short[] y) {

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
        return -TANGENS * xx + b4 <= yy
                &&
                TANGENS * xx + b1 <= yy
                &&
                -TANGENS * xx + b2 >= yy
                &&
                TANGENS * xx + b3 >= yy;
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
     * Вернуть цвет сетки.
     *
     * @return
     */
    public int getNetColor() {
        return netColor;
    }

    public void setNetColor(int netColor) {
        this.netColor = netColor;
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
     * Возвращаем класс Finder(поисковик индексов ячеек, для данной сетки)
     *
     * @return
     */
    public Finder getCellFinder() {
        return cellFinder;
    }

    public void setCellFinder(Finder cellFinder) {
        this.cellFinder = cellFinder;
    }

    public Finder getCoordinateFinder() {
        return coordinateFinder;
    }

    public void setCoordinateFinder(Finder coordinateFinder) {
        this.coordinateFinder = coordinateFinder;
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
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
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

            battleGroup = ((BattleUnit) group.get(0)).getUnit().getWarrior().getBattleGroup();

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

            b1 = (int) (topCenter.getY() - (topCenter.getX()) * TANGENS) - VERTICAL_RADIUS;

            b2 = (int) (centerRight.getY() + centerRight.getX() * TANGENS) + VERTICAL_RADIUS;

            b3 = (int) (bottomCenter.getY() - bottomCenter.getX() * TANGENS) + VERTICAL_RADIUS;

            b4 = (int) (centerLeft.getY() + centerLeft.getX() * TANGENS) - VERTICAL_RADIUS;
            center = new Pair();
            showGrid = true;

            BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
            BattleAlliance alliance = battleGroup.getAlliance();
            ExitZone exit = alliance.getExit();
            setCellCenter(exit.getX(), exit.getY());
            BattleGroupCollection clients = alliance.getAllies();
            for (int j = 0; j < clients.size(); j++) {
                WarriorCollection warriors = clients.get(j).getWarriors();
                for (int i = 0; i < warriors.size(); i++) {
                    user = ClientBattleHelper.getBattleUnitByWarrior(group, warriors.get(i));
                    ClientBattleHelper.observe(user.getUnit().getWarrior(), battleConfiguration);
                    ClientBattleHelper.initMapXY(this, user);
                    Step step = new Step();
                    step.setScreen(this);
                    step.setBattleUnit(user);
                    steps.addElement(step);
                }
            }
            ClientBattleHelper.route(user.getUnit().getWarrior(), battleConfiguration);
            timerId = TimerManager.setPeriodicTimer(100, this);

            setNetColor(255);
        }
    }

    @Override
    public void onHide() {
        TimerManager.killTimer(timerId);
    }
}
