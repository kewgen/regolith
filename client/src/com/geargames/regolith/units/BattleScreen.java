package com.geargames.regolith.units;

import com.geargames.awt.Drawable;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.regolith.Port;
import com.geargames.common.Graphics;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.Event;
import com.geargames.regolith.application.Graph;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.*;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 13.02.12
 */
public class BattleScreen extends Drawable implements TimerListener {
    public static final int GROUND_WIDTH = 348;
    public static final int GROUND_HEIGHT = 174;
    public static final int HORIZONTAL_DIAGONAL = GROUND_WIDTH / 3;
    public static final int VERTICAL_DIAGONAL = GROUND_HEIGHT / 3;
    public static final int SPOT = 10;
    public static int HORIZONTAL_RADIUS = HORIZONTAL_DIAGONAL / 2;
    public static int VERTICAL_RADIUS = VERTICAL_DIAGONAL / 2;
    public static double TANGENS = (VERTICAL_RADIUS + 0.0) / (HORIZONTAL_RADIUS + 0.0);

    private BattleUnit[] enemies;
    private BattleUnit[] allies;

    private Vector steps;
    private Battle battle;
    private BattleUnit[] group;
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

    public BattleScreen() {
        steps = new Vector();
        TimerManager.setPeriodicTimer(100, this);
    }


    public void initAllies() {
        BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
        BattleAlliance alliance = group[0].getWarrior().getBattleGroup().getAlliance();
        ExitZone exit = alliance.getExit();
        setCellCenter(exit.getX(), exit.getY());
        BattleGroupCollection clients = alliance.getAllies();
        for (int j = 0; j < clients.size(); j++) {
            WarriorCollection warriors = clients.get(j).getWarriors();
            for (int i = 0; i < warriors.size(); i++) {
                user = getGroupUnitByWarrior(warriors.get(i));
                ClientBattleHelper.observe(user.getWarrior(), battleConfiguration);
                ClientBattleHelper.initMapXY(this, user);
                Step step = new Step();
                step.setScreen(this);
                step.setUnit(user);
                steps.addElement(step);
            }
        }
        ClientBattleHelper.route(user.getWarrior(), battleConfiguration);
    }

    public void initMap() {
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
        showGrid = false;
    }

    public void draw(Graphics graphics) {
        drawGround(graphics);
        if (showGrid) {
            drawGrid(graphics);
        }
        drawGroup(graphics);
    }

    public int getX() {
        return 0;
    }

    public void setX(int x) {
    }

    public int getY() {
        return 0;
    }

    public void setY(int y) {
    }

    public int getHeight() {
        return Port.getScreenH();
    }

    public int getWidth() {
        return Port.getScreenW();
    }

    /**
     * Прорисовываем изометрическую клетку карты, по координатам её центра(x;y) на
     * графическом контексте graphics.
     *
     * @param graphics
     * @param x
     * @param y
     */
    private void drawCell(Graphics graphics, int x, int y, boolean path) {
        graphics.drawLine(x, y - VERTICAL_RADIUS, x + HORIZONTAL_RADIUS, y);
        graphics.drawLine(x + HORIZONTAL_RADIUS, y, x, y + VERTICAL_RADIUS);
        graphics.drawLine(x, y + VERTICAL_RADIUS, x - HORIZONTAL_RADIUS, y);
        graphics.drawLine(x - HORIZONTAL_RADIUS, y, x, y - VERTICAL_RADIUS);
        if (path) {
            graphics.getRender().getSprite(Graph.SPR_SHADOW).draw(graphics, x, y);
        }
    }

    /**
     * Рисуем бойцов которые принадлежат клиентскому приложению.
     *
     * @param graphics
     */
    private void drawGroup(Graphics graphics) {
        for (int i = 0; i < group.length; i++) {
            BattleUnit unit = group[i];
            if (isOnTheScreen(unit.getMapX(), unit.getMapY())) {
                drawUnit(graphics, unit);
            }
        }
    }


    /**
     * Рисуем бойца.
     *
     * @param graphics
     */
    private void drawUnit(Graphics graphics, BattleUnit unit) {
/*
        unit.getState().getPUnitScript(unit.getWarrior(), graphics.getRender()).draw(graphics, unit.getMapX() - mapX, unit.getMapY() - mapY, unit.getWarrior());
        Graph.UN_MAN_DOWN_SHOT_RIFLE_1
*/
    }


    private void drawGrid(Graphics graphics) {
        graphics.setColor(netColor);
        BattleCell[][] cells = battle.getMap().getCells();
        int length = cells.length;
        int x, y;
        for (int j = 0; j < length; j++) {
            y = j * VERTICAL_RADIUS;
            x = (length - 1 + j) * HORIZONTAL_RADIUS;
            for (int i = 0; i < length; i++) {
                if (isOnTheScreen(x, y)) {
                    if (BattleMapHelper.isShortestPathCell(cells[j][i], user.getWarrior())) {
                        drawCell(graphics, x - mapX, y - mapY, true);
                    } else {
                        drawCell(graphics, x - mapX, y - mapY, false);
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
        while (x < Port.getScreenW()) {
            int y = tmp;
            while (y < Port.getScreenH()) {
                graphics.getRender().getSprite(1).draw(graphics, x, y);
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
        //todo: Реализовать функциональноть, стартующую и останавливающую таймер
        for (int i = 0; i < steps.size(); i++) {
            ((Step) steps.elementAt(i)).onTick();
        }
        if (isMyTurn()) {
            for (int i = 0; i < group.length; i++) {
                group[i].getState().next();
            }
        }
        for (int i = 0; i < allies.length; i++) {
            allies[i].getState().next();
        }
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].getState().next();
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
                        if (Math.abs(touchedXX - x) <= SPOT && Math.abs(touchedYY - y) <= SPOT) {
                            Pair cell = cellFinder.find(x + mapX, y + mapY, this);
                            BattleCell battleCell = battle.getMap().getCells()[cell.getX()][cell.getY()];
                            Element element = battleCell.getElement();
                            if (element instanceof Warrior && ((Warrior) element).getBattleGroup() == group[0].getWarrior().getBattleGroup()) {
                                user = getGroupUnitByWarrior((Warrior) element);
                                ClientBattleHelper.route(user.getWarrior(), ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
                                System.out.println("the current user number = " + user.getWarrior().getNumber());
                            } else if (BattleMapHelper.isReachable(battleCell) && !user.getWarrior().isMoving()) {
                                ClientBattleHelper.trace(user.getWarrior(), cell.getX(), cell.getY());
                                System.out.println("trace for a user " + user.getWarrior().getNumber());
                            }
                        }
                    }
                }
                break;
            case Event.EVENT_TOUCH_DOUBLE_CLICK:
                if (isMyTurn()) {
                    if (isOnTheMap(x, y)) {
                        Pair cell = cellFinder.find(x + mapX, y + mapY, this);
                        if (BattleMapHelper.isShortestPathCell(battle.getMap().getCells()[cell.getX()][cell.getY()], user.getWarrior())) {
                            //ClientBattleHelper.move(this, cell.getX(), cell.getY());
                            System.out.println("move an user " + user.getWarrior().getNumber());
                            moveUser(cell.getX(), cell.getY());
                        }
                    }
                }
                break;
        }
        return true;
    }

    private BattleUnit getGroupUnitByWarrior(Warrior warrior) {
        for (int i = 0; i < group.length; i++) {
            if (warrior == group[i].getWarrior()) {
                return group[i];
            }
        }
        return null;
    }

    private BattleUnit getAllyUnitByWarrior(Warrior warrior) {
        for (int i = 0; i < allies.length; i++) {
            if (warrior == allies[i].getWarrior()) {
                return allies[i];
            }
        }
        return null;
    }

    private BattleUnit getEnemyUnitByWarrior(Warrior warrior) {
        for (int i = 0; i < enemies.length; i++) {
            if (warrior == enemies[i].getWarrior()) {
                return enemies[i];
            }
        }
        return null;
    }


    private Step getStep(BattleUnit unit) {
        for (int i = 0; i < steps.size(); i++) {
            if (((Step) steps.elementAt(i)).getUnit() == unit) {
                return (Step) steps.elementAt(i);
            }
        }
        return null;
    }

    public void moveUser(int x, int y) {
        System.out.println(x + ":" + y);
        getStep(user).init();
    }

    /**
     * Разрешить двинуть бойца warrior принадлежащего основной группе в точку x;y
     *
     * @param warior
     * @param x
     * @param y
     */
    public void moveAlly(Warrior warior, int x, int y) {
        ClientBattleHelper.trace(warior, x, y);
        getStep(getAllyUnitByWarrior(warior)).init();
    }

    /**
     * Разрешить двинуть бойца warior не пренадлежащего основной группе в точку x;y
     *
     * @param warrior
     * @param x
     * @param y
     */
    public void moveEnemy(Warrior warrior, int x, int y) {

    }


    /**
     * Покрывает ли наш экран эту точку карты?
     *
     * @param x координата карты
     * @param y координата карты
     * @return
     */
    public boolean isOnTheScreen(int x, int y) {
        return mapX <= x + HORIZONTAL_RADIUS && x - HORIZONTAL_RADIUS <= mapX + Port.getW() && mapY <= y + VERTICAL_RADIUS && y - VERTICAL_RADIUS <= mapY + Port.getH();
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
     * Вернуть группу своих бойцов участвующих в битве.
     *
     * @return
     */
    public BattleUnit[] getGroup() {
        return group;
    }

    public void setGroup(BattleUnit[] group) {
        this.group = group;
    }

    /**
     * Вернуть своего активного бойца.
     *
     * @return
     */
    public BattleUnit getUser() {
        return user;
    }
}
