package com.geargames.regolith;

import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Human;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.ClientBarrier;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 03.03.12
 */
public class MotionTest {
    private TestClientWarriorElement warrior;
    private BattleMap battleMap;

    @Before
    public void before() {
        ClientTestConfigurationFactory.getDefaultConfiguration();
        Barrier barrier = new ClientBarrier();
        barrier.setAbleToLookThrough(false);
        warrior = new TestClientWarriorElement();
        warrior.setName("Вася");
        warrior.setActionScore((short) 5);
        warrior.setMembershipType(Human.WARRIOR);
        warrior.setDirection(Direction.DOWN_UP);

        BattleGroup battleGroup = new BattleGroup();

        Account account = new Account();
        SecurityOperationManager operationManager = new SecurityOperationManager();
        account.setSecurity(operationManager);
        operationManager.setAccount(account);

        battleGroup.setAccount(account);
        battleGroup.setWarriors(new ClientWarriorCollection(new Vector()));
        WarriorHelper.addWarriorToGroup(battleGroup, warrior);

        Battle battle = ClientTestHelper.createBattle("test", "test");
        BattleAlliance battleAlliance = battle.getAlliances()[0];
        WarriorHelper.addGroupIntoAlliance(battleAlliance, battleGroup);

        battleMap = battle.getMap();
    }

    @Test
    public void move() {
        WarriorHelper.putWarriorIntoMap(battleMap.getCells(), warrior, 2, 2);
        TestHelper.printRouteMap(battleMap, warrior);
        BattleMapHelper.prepare(battleMap.getCells());
        TestHelper.printRouteMap(battleMap, warrior);
        BattleConfiguration battleConfiguration = ClientTestConfigurationFactory.getDefaultConfiguration().getBattleConfiguration();
        battleConfiguration.getRouter().route(warrior, battleConfiguration);
        TestHelper.printRouteMap(battleMap, warrior);
        MoveOneStepListener listener = new MoveOneStepListener() {
            @Override
            public void onStep(Warrior warrior, int x, int y) {

            }
        };
        WarriorHelper.move(battleMap.getCells(), warrior, 5, 4, listener, ClientTestConfigurationFactory.getDefaultConfiguration().getBattleConfiguration());
        TestHelper.printRouteMap(battleMap, warrior);
    }

}
