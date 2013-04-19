package com.geargames.regolith;

import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.helpers.BattleHelper;
import com.geargames.regolith.units.dictionaries.ServerAllyCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.map.observer.Observer;
import com.geargames.regolith.map.observer.StrictPerimeterObserver;
import com.geargames.regolith.map.router.RecursiveWaveRouter;
import com.geargames.regolith.map.router.Router;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.battle.ServerBarrier;
import com.geargames.regolith.units.map.ExitZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

/**
 * User: mkutuzov
 * Date: 21.02.12
 */
public class ObserverTest {
    private Warrior warrior;
    private BattleMap battleMap;
    private Barrier barrier;
    private Observer observer;
    private Router router;

    @Before
    public void befor() throws Exception{
        Account account = new Account();
        SecurityOperationManager manager = new SecurityOperationManager();
        account.setSecurity(manager);
        manager.setAccount(account);

        observer = new StrictPerimeterObserver(new ServerAllyCollection());
        barrier = new ServerBarrier();
        barrier.setAbleToLookThrough(false);
        warrior = new Warrior();
        warrior.setName("Вася");


        BattleGroup battleGroup = new BattleGroup();
        battleGroup.setAccount(account);
        battleGroup.setWarriors(new ServerWarriorCollection(new LinkedList<Warrior>()));

        battleGroup.getWarriors().add(warrior);

        account.setWarriors(battleGroup.getWarriors());
        for(Warrior tmp : ((ServerWarriorCollection)account.getWarriors()).getWarriors()){
            tmp.setBattleGroup(battleGroup);
        }

        battleMap = BattleHelper.createBattleMap(20);
        battleMap.setExits(new ExitZone[2]);

        ExitZone zone = new ExitZone();
        zone.setId(0);
        battleMap.getExits()[0] = zone;

        zone = new ExitZone();
        zone.setId(1);
        battleMap.getExits()[1] = zone;

        BattleType[] battleTypes = new BattleType[1];
        BattleType battleType = new TrainingBattle();
        battleType.setId(0);

        battleType.setName("1:1");
        battleType.setScores((byte) 5);
        battleType.setAllianceAmount((short)2);
        battleType.setAllianceSize((short)1);
        battleType.setGroupSize((short)1);
        battleTypes[0] = battleType;

        battleMap.setPossibleBattleTypes(battleTypes);

        Battle battle = BattleHelper.createBattle("test", battleMap, battleType);
        BattleHelper.prepareBattle(battle);

        BattleAlliance battleAlliance = battle.getAlliances()[0];

        WarriorHelper.addGroupIntoAlliance(battleAlliance, battleGroup);

        router = new RecursiveWaveRouter();
    }

    @Test
    public void cubeObserver(){
        WarriorHelper.putWarriorIntoMap(warrior, battleMap, 5, 5);
        BattleCell[][] cells = battleMap.getCells();
        observer.observe(warrior);
        TestHelper.printViewMap(battleMap, warrior.getBattleGroup().getAlliance());
    }

    @Test
    public void observe(){
        WarriorHelper.putWarriorIntoMap(warrior, battleMap, 0, 5);
        BattleCell[][] cells = battleMap.getCells();
        cells[0][2].addElement(barrier);
        cells[1][2].addElement(barrier);
        cells[2][2].addElement(barrier);
        BattleMapHelper.clearViewAround(warrior);
        observer.observe(warrior);
        TestHelper.printViewMap(battleMap,warrior.getBattleGroup().getAlliance());
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        Assert.assertTrue("Точка [3][2] должна быть видима", BattleMapHelper.isVisible(battleMap.getCells()[3][2], alliance));
        Assert.assertTrue("Точка [4][2] должна быть видима", BattleMapHelper.isVisible(battleMap.getCells()[4][2], alliance));
        Assert.assertTrue("Точка [4][3] должна быть видима", BattleMapHelper.isVisible(battleMap.getCells()[4][1], alliance));
        Assert.assertTrue("Точка [0][1] должна быть не видима", !BattleMapHelper.isVisible(battleMap.getCells()[0][1], alliance));
    }

    @Test
    public void route(){
        WarriorHelper.putWarriorIntoMap(warrior, battleMap, 1, 5);
        BattleCell[][] cells = battleMap.getCells();
        cells[0][2].addElement(barrier);
        cells[1][2].addElement(barrier);
        cells[2][2].addElement(barrier);

        cells[1][4].addElement(barrier);
        cells[2][4].addElement(barrier);
        cells[3][4].addElement(barrier);
        cells[4][4].addElement(barrier);
        cells[5][4].addElement(barrier);

        warrior.setActionScore((short)10);
        BattleMapHelper.prepare(battleMap);
        TestHelper.printRouteMap(battleMap,warrior);
        router.route(warrior);
        TestHelper.printRouteMap(battleMap,warrior);
        BattleMapHelper.makeShortestRoute(2, 0, warrior);
        TestHelper.printRouteMap(battleMap,warrior);
        Assert.assertTrue("часть пути",  BattleMapHelper.isShortestPathCell(cells[2][0], warrior));
        Assert.assertTrue("часть пути",  BattleMapHelper.isShortestPathCell(cells[3][1], warrior));
        Assert.assertTrue("часть пути",  BattleMapHelper.isShortestPathCell(cells[3][2], warrior));
        Assert.assertTrue("часть пути",  BattleMapHelper.isShortestPathCell(cells[3][3], warrior));

        BattleMapHelper.resetShortestPath(warrior, warrior.getX(), warrior.getY());
        Assert.assertFalse("не часть пути",  BattleMapHelper.isShortestPathCell(cells[2][0], warrior));
        Assert.assertFalse("не часть пути",  BattleMapHelper.isShortestPathCell(cells[3][1], warrior));
        Assert.assertFalse("не часть пути",  BattleMapHelper.isShortestPathCell(cells[3][2], warrior));
        Assert.assertFalse("не часть пути",  BattleMapHelper.isShortestPathCell(cells[3][3], warrior));

        TestHelper.printRouteMap(battleMap,warrior);
        BattleMapHelper.clearRoutes(warrior, warrior.getX(), warrior.getY());

        Assert.assertFalse("расчёта для точки нет",  BattleMapHelper.isReachable(cells[2][0]));
        Assert.assertFalse("расчёта для точки нет",  BattleMapHelper.isReachable(cells[3][1]));
        Assert.assertFalse("расчёта для точки нет",  BattleMapHelper.isReachable(cells[3][2]));
        Assert.assertFalse("расчёта для точки нет",  BattleMapHelper.isReachable(cells[3][3]));

        TestHelper.printRouteMap(battleMap, warrior);
    }
}
