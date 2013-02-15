package com.geargames.regolith;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.battle.ClientBorder;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.BattleMapHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 03.03.12
 */
public class MotionTest {
    private Warrior warrior;
    private BattleMap battleMap;

    @Before
    public void befor(){
        ClientTestConfigurationFactory.getDefaultConfiguration();
        Border border = new ClientBorder();
        border.setAbleToLookThrough(false);
        warrior = new Warrior();
        warrior.setName("Вася");
        warrior.setActionScore((short)5);

        BattleGroup battleGroup = new BattleGroup();

        Account account = new Account();
        SecurityOperationManager operationManager = new SecurityOperationManager();
        account.setSecurity(operationManager);
        operationManager.setAccount(account);

        battleGroup.setAccount(account);
        battleGroup.setWarriors(new ClientWarriorCollection(new Vector()));
        WarriorHelper.addWarriorToGroup(battleGroup, warrior);

        Battle battle = ClientBattleHelper.createBattle("test", "test");
        BattleAlliance battleAlliance = battle.getAlliances()[0];
        WarriorHelper.addGroupIntoAlliance(battleAlliance, battleGroup);

        battleMap = battle.getMap();
    }

    @Test
    public void move() {
        WarriorHelper.putWarriorIntoMap(warrior, battleMap, 2, 2);
        TestHelper.printRouteMap(battleMap, warrior);
        BattleMapHelper.prepare(battleMap);
        TestHelper.printRouteMap(battleMap, warrior);
        BattleConfiguration battleConfiguration = ClientTestConfigurationFactory.getDefaultConfiguration().getBattleConfiguration();
        battleConfiguration.getRouter().route(warrior);
        TestHelper.printRouteMap(battleMap, warrior);
        MoveOneStepListener listener = new MoveOneStepListener(){
            @Override
            public void onStep(Warrior warrior, int x, int y) {

            }
        };
        WarriorHelper.move(warrior, 5, 4, listener, ClientTestConfigurationFactory.getDefaultConfiguration().getBattleConfiguration());
        TestHelper.printRouteMap(battleMap, warrior);
    }

}