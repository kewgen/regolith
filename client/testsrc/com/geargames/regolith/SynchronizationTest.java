package com.geargames.regolith;

import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.Unit;
import com.geargames.regolith.units.battle.ClientBorder;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.finder.ProjectionFinder;
import com.geargames.regolith.units.map.finder.ReverseProjectionFinder;
import com.geargames.regolith.units.map.verifier.CubeBorderCorrector;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.ExitZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 20.03.12
 */
public class SynchronizationTest {
    private Warrior warrior;
    private BattleMap battleMap;
    private Battle battle;

    @Before
    public void before() {
        Account account  = new Account();
        account.setWarriors(new ClientWarriorCollection(new Vector()));
        account.setSecurity(new SecurityOperationManager());
        account.getSecurity().setAccount(account);

        ClientTestConfigurationFactory.getDefaultConfiguration();
        Border border = new ClientBorder();
        border.setAbleToLookThrough(false);
        warrior = new Warrior();
        account.getWarriors().add(warrior);
        warrior.setName("Вася");
        warrior.setActionScore((short)10);

        BattleGroup battleGroup = new BattleGroup();
        battleGroup.setWarriors(new ClientWarriorCollection(new Vector()));
        battleGroup.setAccount(account);
        WarriorHelper.addWarriorToGroup(battleGroup, warrior);

        battle = ClientTestHelper.createBattle("test", "test");
        BattleAlliance battleAlliance = battle.getAlliances()[0];
        WarriorHelper.addGroupIntoAlliance(battleAlliance, battleGroup);

        battleMap = battle.getMap();
        battleMap.getCells()[1][2].setElement(border);
        battleMap.getCells()[2][2].setElement(border);
        battleMap.getCells()[3][2].setElement(border);
        battleMap.getCells()[3][3].setElement(border);
        battleMap.getCells()[3][4].setElement(border);
        battleMap.getCells()[3][5].setElement(border);
    }

    @Test
    public void clientServerMotion() {
        ExitZone exit = new ExitZone();
        exit.setY((short)1);
        exit.setY((short)1);
        exit.setxRadius((byte)1);
        exit.setyRadius((byte)1);
        battle.getAlliances()[0].setExit(exit);

        SecurityOperationManager manager = warrior.getBattleGroup().getAccount().getSecurity();
        manager.setObserve(0);
        manager.setX(0);
        manager.setY(0);

        WarriorHelper.putWarriorIntoMap(warrior, battleMap, 0, 0);
        BattleMapHelper.clearRoutes(warrior, 0, 0);
        BattleMapHelper.clearViewAround(warrior);
        BattleMapHelper.prepare(battle.getMap());
        BattleConfiguration battleConfiguration = ClientTestConfigurationFactory.getDefaultConfiguration().getBattleConfiguration();
        battleConfiguration.getObserver().observe(warrior);
        MoveOneStepListener listener = new MoveOneStepListener() {
            @Override
            public void onStep(Warrior warrior, int x, int y) {

            }
        };
        WarriorHelper.move(warrior,10, 10, listener, battleConfiguration);
        int valO = manager.getObserve();

        BattleMapHelper.clearRoutes(warrior, 0, 0);
        BattleMapHelper.clearViewAround(warrior);
        manager.setObserve(0);
        manager.setX(0);
        manager.setY(0);

        WarriorHelper.putWarriorIntoMap(warrior, battleMap, 0, 0);
        BattleMapHelper.clearRoutes(warrior, 0, 0);
        BattleMapHelper.clearViewAround(warrior);

        Unit[] group = new Unit[1];
        Unit unit = new Unit();
        group[0] = unit;
        unit.setWarrior(warrior);
        BattleScreen screen = new BattleScreen();
        screen.setCoordinateFinder(new ReverseProjectionFinder());
        screen.setCellFinder(new ProjectionFinder());
        screen.setCorrector(new CubeBorderCorrector());
        screen.setMyTurn(true);
        screen.setBattle(battle);
        screen.setGroup(group);
        screen.initMap();
        screen.initAllies();

        screen.moveUser(10,10);
        while (warrior.isMoving()) {
            screen.onTimer(0);
        }

        Assert.assertEquals(valO, manager.getObserve());
    }

}
