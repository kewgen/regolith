package com.geargames.regolith;

import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.helpers.BattleHelper;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.battle.ServerBorder;
import com.geargames.regolith.units.map.ExitZone;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

/**
 * User: mkutuzov
 * Date: 07.03.12
 */
public class GroupsSpreadTest {
    private Battle battle;


    @Before
    public void befor() throws Exception{
        ServerTestConfigurationFactory.getDefaultConfiguration().getBattleConfiguration();
        Border border = new ServerBorder();
        border.setAbleToLookThrough(false);

        Warrior warrior = new Warrior();
        warrior.setName("Вася1");
        warrior.setActionScore((short)5);
        Warrior warrior1 = new Warrior();
        warrior1.setName("Петя1");
        warrior1.setActionScore((short)5);
        BattleGroup battleGroup = new BattleGroup();
        battleGroup.setWarriors(new ServerWarriorCollection(new LinkedList<Warrior>()));
        battleGroup.getWarriors().add(warrior);
        battleGroup.getWarriors().add(warrior1);


        warrior = new Warrior();
        warrior.setName("Вася2");
        warrior.setActionScore((short)5);
        warrior1 = new Warrior();
        warrior1.setName("Петя2");
        warrior1.setActionScore((short)5);
        BattleGroup battleGroup1 = new BattleGroup();
        battleGroup1.setWarriors(new ServerWarriorCollection(new LinkedList<Warrior>()));
        battleGroup1.getWarriors().add(warrior);
        battleGroup1.getWarriors().add(warrior1);

        warrior = new Warrior();
        warrior.setName("Вася3");
        warrior.setActionScore((short)5);
        warrior1 = new Warrior();
        warrior1.setName("Петя3");
        warrior1.setActionScore((short)5);
        BattleGroup battleGroup2 = new BattleGroup();
        battleGroup2.setWarriors(new ServerWarriorCollection(new LinkedList<Warrior>()));
        battleGroup2.getWarriors().add(warrior);
        battleGroup2.getWarriors().add(warrior1);

        warrior = new Warrior();
        warrior.setName("Вася4");
        warrior.setActionScore((short)5);
        warrior1 = new Warrior();
        warrior1.setName("Петя4");
        warrior1.setActionScore((short)5);
        BattleGroup battleGroup3 = new BattleGroup();
        battleGroup3.setWarriors(new ServerWarriorCollection(new LinkedList<Warrior>()));
        battleGroup3.getWarriors().add(warrior);
        battleGroup3.getWarriors().add(warrior1);

        BattleMap battleMap = BattleHelper.createBattleMap(20);

        BattleType[] battleTypes = new BattleType[1];
        BattleType battleType = new TrainingBattle();
        battleType.setId(0);

        battleType.setName("1:1");
        battleType.setScores((byte) 5);
        battleType.setAllianceAmount((short)2);
        battleType.setAllianceSize((short)1);
        battleType.setGroupSize((short)1);
        battleTypes[0] = battleType;

        battleMap.setExits(new ExitZone[]{new ExitZone(), new ExitZone()});
        ExitZone exit = battleMap.getExits()[0];
        exit.setX((short) 1);
        exit.setY((short) 3);
        exit.setxRadius((byte) 1);
        exit.setyRadius((byte)2);

        exit = battleMap.getExits()[1];
        exit.setX((short) 5);
        exit.setxRadius((byte)2);
        exit.setY((short) 18);
        exit.setyRadius((byte)1);

        battle = BattleHelper.createBattle("test", battleMap, battleType);
        BattleHelper.prepareBattle(battle);

        BattleAlliance battleAlliance = battle.getAlliances()[0];
        BattleAlliance battleAlliance1 = battle.getAlliances()[1];

        WarriorHelper.addGroupIntoAlliance(battleAlliance, battleGroup);
        WarriorHelper.addGroupIntoAlliance(battleAlliance, battleGroup1);

        WarriorHelper.addGroupIntoAlliance(battleAlliance1, battleGroup2);
        WarriorHelper.addGroupIntoAlliance(battleAlliance1, battleGroup3);

    }

    @Test
    public void spread() {
        BattleMap map = battle.getMap();
        BattleHelper.spreadAlliancesOnTheMap(battle);
        TestHelper.printExitZones(battle.getMap());

        ExitZone[] exits = map.getExits();

        BattleCell[][] cells = battle.getMap().getCells();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[j][i].getElement() instanceof Warrior) {
                        Assert.assertTrue(
                                exits[0].isWithIn(j,i)
                                ||
                                exits[1].isWithIn(j,i)
                        );
                }
            }
        }
    }

}
