package com.geargames.regolith.units.map;

import com.geargames.regolith.Game;
import com.geargames.regolith.Port;
import com.geargames.common.Graphics;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.app.Event;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.Unit;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.common.String;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 13.02.12
 */
public class BattleScreen {
    public static final int GROUNR_WIDTH = 348;
    public static final int GROUNR_HEIGHT = 174;
    public static final int HORIZONTAL_DIAGONAL = 116;
    public static final int VERTICAL_DIAGONAL = 58;
    public static final int SPOT = 10;
    public static int HORIZONTAL_RADIUS = 58;
    public static int VERTICAL_RADIUS = 29;
    public static double TANGENS = (VERTICAL_RADIUS + 0.0) / (HORIZONTAL_RADIUS + 0.0);

    private Unit[] enemies;
    private Unit[] allies;

    private Vector steps;
    private Battle battle;
    private Unit[] group;
    private Unit user;
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
    }


    public void initAllies() {
        BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
        BattleAlliance alliance = group[0].getWarrior().getBattleGroup().getAlliance();
        ExitZone exit = alliance.getExit();
        setCellCenter(exit.getX(), exit.getY());
        BattleGroupCollection clients = alliance.getAllies();
        for (int j = 0; j < clients.size(); j++) {
            for (int i = 0; i < clients.get(j).getWarriors().size(); i++) {
                user = getGroupUnitByWarrior(clients.get(j).getWarriors().get(i));
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

    /**
     * Прорисовываем изометрическую клетку карты, по координатам её центра(x;y) на
     * графическом контексте graphics.
     *
     * @param graphics
     * @param x
     * @param y
     */
    private void drawCell(String data, Graphics graphics, int x, int y) {
        graphics.drawLine(x, y - VERTICAL_RADIUS, x + HORIZONTAL_RADIUS, y);
        graphics.drawLine(x + HORIZONTAL_RADIUS, y, x, y + VERTICAL_RADIUS);
        graphics.drawLine(x, y + VERTICAL_RADIUS, x - HORIZONTAL_RADIUS, y);
        graphics.drawLine(x - HORIZONTAL_RADIUS, y, x, y - VERTICAL_RADIUS);
        graphics.drawString(data, x, y, 0);
    }

    /**
     * Рисуем бойцов которые принадлежат клиентскому приложению.
     * @param graphics
     */
    private void drawGroup(Graphics graphics) {
        for (int i = 0; i < group.length; i++) {
            Unit unit = group[i];
            if (isOnTheScreen(unit.getMapX(), unit.getMapY())) {
                drawUnit(graphics, unit);
            }
        }
    }

    /**
     * Рисуем бойца исходя из следующих предпосылок:
     * Боец имеет базовый идентификатор изображения warrior.getFrameId.
     * Состояние бойца отражено на рисунках сдвинутых одинакого относительно базового для всех юнитов.
     * @param graphics
     * @param unit
     */
    private void drawUnit(Graphics graphics, Unit unit){
        Warrior warrior = unit.getWarrior();
        int offset = 0;
        if(warrior.isMoving()){
            offset = Warrior.MOVING_OFFSET;
        }else{
            if(warrior.isShooting()){
                if(warrior.isSitting()){
                    offset = Warrior.SITTING_SHOOTING_OFFSET;
                }else{
                    offset = Warrior.STANDING_SHOOTING_OFFSET;
                }
            }else{
                if(warrior.isSitting()){
                    offset = Warrior.SITTING_OFFSET;
                }
            }
        }
        offset += (unit.getWarrior().getDirection().getNumber() + 1)%8;
        Game.getInstance().getRender().getUnit(unit.getWarrior().getFrameId() + offset).draw(graphics, unit.getMapX() - mapX, unit.getMapY() - mapY, null);
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
                        drawCell(String.valueOfC("*"), graphics, x - mapX, y - mapY);
                    } else {
                        drawCell(String.valueOfC("" + cells[j][i].getOrder()), graphics, x - mapX, y - mapY);
                    }
                }
                x -= HORIZONTAL_RADIUS;
                y += VERTICAL_RADIUS;
            }
        }
    }

    private void drawGround(Graphics graphics) {
        int x = -mapX % GROUNR_WIDTH - HORIZONTAL_RADIUS;
        int tmp = -mapY % GROUNR_HEIGHT - VERTICAL_RADIUS;
        while (x < Port.getScreenW()) {
            int y = tmp;
            while (y < Port.getScreenH()) {
                Game.getInstance().getRender().getSprite(1).draw(graphics, x, y);
                y += GROUNR_HEIGHT;
            }
            x += GROUNR_WIDTH;
        }
    }

    /**
     * Переместить центр экрана к координате карты.
     *
     * @param x координата карты
     * @param y координата карты
     */
    public void setCenter(int x, int y) {
        setMapX(x - (Port.getScreenW() >> 1));
        setMapY(y - (Port.getScreenH() >> 1));
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

    public boolean event(int code, int param, int x, int y) {
        switch (code) {
            case Event.EVENT_KEY_UP:
                showGrid = false;
                break;
            case Event.EVENT_KEY_DOWN:
                showGrid = true;
                break;
            case Event.EVENT_TICK:
                for (int i = 0; i < steps.size(); i++) {
                    ((Step) steps.elementAt(i)).onTick();
                }
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
            case Event.EVENT_TOUCH_DOUBBLE_CLICK:
                if (isMyTurn()) {
                    if (isOnTheMap(x, y)) {
                        Pair cell = cellFinder.find(x + mapX, y + mapY, this);
                        if(BattleMapHelper.isShortestPathCell(battle.getMap().getCells()[cell.getX()][cell.getY()], user.getWarrior())){
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

    private Unit getGroupUnitByWarrior(Warrior warrior){
        for(int i = 0 ; i < group.length; i++){
            if(warrior == group[i].getWarrior()){
                return group[i];
            }
        }
        throw new IllegalArgumentException();
    }

    private Unit getAllyUnitByWarrior(Warrior warrior){
        for(int i = 0 ; i < allies.length; i++){
            if(warrior == allies[i].getWarrior()){
                return allies[i];
            }
        }
        throw new IllegalArgumentException();
    }

    private Unit getEnemyUnitByWarrior(Warrior warrior){
        for(int i = 0 ; i < enemies.length; i++){
            if(warrior == enemies[i].getWarrior()){
                return enemies[i];
            }
        }
        throw new IllegalArgumentException();
    }


    private Step getStep(Unit unit) {
        for (int i = 0; i < steps.size(); i++) {
            if (((Step) steps.elementAt(i)).getUnit() == unit) {
                return (Step) steps.elementAt(i);
            }
        }
        return null;
    }

    public void moveUser(int x, int y){
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

    public void setBattleMap(Battle battle) {
        this.battle = battle;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public Unit[] getGroup() {
        return group;
    }

    public void setGroup(Unit[] group) {
        this.group = group;
    }

    public Unit getUser() {
        return user;
    }
}
