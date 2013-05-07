package com.geargames.regolith;

import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.ClientBarrier;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.map.verifier.CubeBorderCorrector;
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
    private TestClientHumanElement unit;
    private BattleMap battleMap;
    private Battle battle;

    @Before
    public void before() {
        Account account = new Account();
        account.setWarriors(new ClientWarriorCollection(new Vector()));
        account.setSecurity(new SecurityOperationManager());
        account.getSecurity().setAccount(account);

        ClientTestConfigurationFactory.getDefaultConfiguration();
        Barrier barrier = new ClientBarrier();
        barrier.setAbleToLookThrough(false);
        warrior = new Warrior();
        account.getWarriors().add(warrior);
        warrior.setName("Вася");
        warrior.setActionScore((short) 10);
        warrior.setMembershipType(Human.WARRIOR);
        unit = new TestClientHumanElement();
        unit.setHuman(warrior);

        BattleGroup battleGroup = new BattleGroup();
        battleGroup.setWarriors(new ClientWarriorCollection(new Vector()));
        battleGroup.setAccount(account);
        WarriorHelper.addWarriorToGroup(battleGroup, warrior);

        battle = ClientTestHelper.createBattle("test", "test");
        BattleAlliance battleAlliance = battle.getAlliances()[0];
        WarriorHelper.addGroupIntoAlliance(battleAlliance, battleGroup);

        battleMap = battle.getMap();
        battleMap.getCells()[1][2].addElement(barrier);
        battleMap.getCells()[2][2].addElement(barrier);
        battleMap.getCells()[3][2].addElement(barrier);
        battleMap.getCells()[3][3].addElement(barrier);
        battleMap.getCells()[3][4].addElement(barrier);
        battleMap.getCells()[3][5].addElement(barrier);
    }

    @Test
    public void clientServerMotion() {
        ExitZone exit = new ExitZone();
        exit.setY((short) 1);
        exit.setY((short) 1);
        exit.setxRadius((byte) 1);
        exit.setyRadius((byte) 1);
        battle.getAlliances()[0].setExit(exit);

        SecurityOperationManager manager = warrior.getBattleGroup().getAccount().getSecurity();
        manager.setObserve(0);
        manager.setX(0);
        manager.setY(0);

        BattleConfiguration battleConfiguration = ClientTestConfigurationFactory.getDefaultConfiguration().getBattleConfiguration();

        WarriorHelper.putWarriorIntoMap(battleMap.getCells(), unit, 0, 0);
        BattleMapHelper.clearRoutes(battleMap.getCells(), unit, 0, 0, battleConfiguration);
        BattleMapHelper.clearViewAround(battleMap.getCells(), unit);
        BattleMapHelper.prepare(battleMap.getCells());
        battleConfiguration.getObserver().observe(unit);
        MoveOneStepListener listener = new MoveOneStepListener() {
            @Override
            public void onStep(HumanElement unit, int x, int y) {

            }
        };
        WarriorHelper.move(battleMap.getCells(), unit, 10, 10, listener, battleConfiguration);
        int valO = manager.getObserve();

        BattleMapHelper.clearRoutes(battleMap.getCells(), unit, 0, 0, battleConfiguration);
        BattleMapHelper.clearViewAround(battleMap.getCells(), unit);
        manager.setObserve(0);
        manager.setX(0);
        manager.setY(0);

        WarriorHelper.putWarriorIntoMap(battleMap.getCells(), unit, 0, 0);
        BattleMapHelper.clearRoutes(battleMap.getCells(), unit, 0, 0, battleConfiguration);
        BattleMapHelper.clearViewAround(battleMap.getCells(), unit);

        BattleScreen screen = new BattleScreen();
        screen.setCorrector(new CubeBorderCorrector());
        screen.setActiveAlliance(warrior.getBattleGroup().getAlliance());
        screen.setBattle(battle);
        screen.onShow();

        screen.moveUser(10, 10);
        unit.getLogic().quicklyCompleteAllCommands();

        Assert.assertEquals(valO, manager.getObserve());
    }

}
