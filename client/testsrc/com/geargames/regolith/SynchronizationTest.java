package com.geargames.regolith;

import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.battle.Human;
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
    private TestClientWarriorElement warrior;
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
        warrior = new TestClientWarriorElement();
        account.getWarriors().add(warrior);
        warrior.setName("Вася");
        warrior.setActionScore((short) 10);
        warrior.setMembershipType(Human.WARRIOR);
        warrior.setDirection(Direction.DOWN_UP);

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

        WarriorHelper.putWarriorIntoMap(battleMap.getCells(), warrior, 0, 0);
        BattleMapHelper.clearRoutes(battleMap.getCells(), warrior, 0, 0, battleConfiguration);
        BattleMapHelper.clearViewAround(battleMap.getCells(), warrior);
        BattleMapHelper.prepare(battleMap.getCells());
        battleConfiguration.getObserver().observe(warrior);
        MoveOneStepListener listener = new MoveOneStepListener() {
            @Override
            public void onStep(Warrior warrior, int x, int y) {

            }
        };
        WarriorHelper.move(battleMap.getCells(), warrior, 10, 10, listener, battleConfiguration);
        int valO = manager.getObserve();

        BattleMapHelper.clearRoutes(battleMap.getCells(), warrior, 0, 0, battleConfiguration);
        BattleMapHelper.clearViewAround(battleMap.getCells(), warrior);
        manager.setObserve(0);
        manager.setX(0);
        manager.setY(0);

        WarriorHelper.putWarriorIntoMap(battleMap.getCells(), warrior, 0, 0);
        BattleMapHelper.clearRoutes(battleMap.getCells(), warrior, 0, 0, battleConfiguration);
        BattleMapHelper.clearViewAround(battleMap.getCells(), warrior);

        ClientBattleContext battleContext = ClientConfigurationFactory.getConfiguration().getBattleContext();
        battleContext.initiate(battle);

        BattleScreen screen = new BattleScreen();
        screen.setCorrector(new CubeBorderCorrector());
        screen.onShow();

        screen.moveWarrior((short) 10, (short) 10);
        warrior.getLogic().quicklyCompleteAllCommands();

        Assert.assertEquals(valO, manager.getObserve());
    }

}
