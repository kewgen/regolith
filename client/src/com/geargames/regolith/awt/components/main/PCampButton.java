package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.*;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.map.observer.StrictPerimeterObserver;
import com.geargames.regolith.map.router.RecursiveWaveRouter;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.SubordinationDamage;
import com.geargames.regolith.units.Underload;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.BattleTypeCollection;
import com.geargames.regolith.units.dictionaries.ClientAllyCollection;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.ExitZone;

import java.util.Vector;

/**
 * User: m.v.kutuzov
 * Date: 07.04.13
 */
public class PCampButton extends PTouchButton {

    public PCampButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        Account account = clientConfiguration.getAccount();
        BattleTypeCollection types = ClientConfigurationFactory.getConfiguration().getBaseConfiguration().getBattleTypes();
        BattleType type = null;
        for (int i = 0; i < types.size(); i++) {
            type = types.get(i);
            if (type.getAllianceAmount() == 2 && type.getAllianceSize() == 1 && type.getGroupSize() == 1) {
                break;
            }
            type = null;
        }
        if (type != null) {
            Battle battle = new Battle();

            Account enemyAccount = new Account();
            enemyAccount.setId(-1);

            battle.setAlliances(new BattleAlliance[2]);
            for(int i = 0; i < battle.getAlliances().length; i++){
                BattleAlliance alliance = new BattleAlliance();
                ClientBattleGroupCollection battleGroups = new ClientBattleGroupCollection();
                alliance.setAllies(battleGroups);
                BattleGroup group = new BattleGroup();
                if(i == 0){
                    group.setAccount(account);
                }else{
                    group.setAccount(enemyAccount);
                }
                battleGroups.add(group);
                group.setAlliance(alliance);
                group.setWarriors(new ClientWarriorCollection(new Vector()));
                alliance.setBattle(battle);
                alliance.setNumber((byte) i);
                battle.getAlliances()[i] = alliance;
            }
            battle.setBattleType(type);
            BattleMap battleMap = ClientBattleHelper.createBattleMap(20);
            battle.setMap(battleMap);
            BattleType[] tmp = new BattleType[1];
            tmp[0] = type;
            battleMap.setPossibleBattleTypes(tmp);

            Barrier barrier = new ClientBarrier();
            barrier.setAbleToLookThrough(false);
            barrier.setFrameId(1216);

            battleMap.getCells()[1][2].addElement(barrier);
            battleMap.getCells()[2][2].addElement(barrier);
            battleMap.getCells()[3][2].addElement(barrier);
            battleMap.getCells()[3][3].addElement(barrier);
            battleMap.getCells()[3][4].addElement(barrier);
            battleMap.getCells()[3][5].addElement(barrier);

            Warrior mine = account.getWarriors().get(0);
            mine.setDirection(Direction.LEFT_RIGHT);
            Warrior another = account.getWarriors().get(1);
            another.setDirection(Direction.LEFT_RIGHT);

            ExitZone[] exits = new ExitZone[2];
            battleMap.setExits(exits);
            ExitZone exit = new ExitZone();
            exit.setX((short)1);
            exit.setY((short)1);
            exit.setxRadius((byte)1);
            exit.setyRadius((byte)1);
            WarriorHelper.putWarriorIntoMap(mine, battleMap, 0, 0);
            mine.setNumber((short)1);
            mine.setActionScore((short)100);
            mine.setSpeed((byte)8);
            battle.getAlliances()[0].setExit(exit);
            exits[0] = exit;

            exit = new ExitZone();
            exit.setX((short)18);
            exit.setY((short)18);
            exit.setxRadius((byte)1);
            exit.setyRadius((byte)1);
            battle.getAlliances()[1].setExit(exit);
            WarriorHelper.putWarriorIntoMap(another, battleMap, 18, 18);
            another.setNumber((short)2);
            another.setActionScore((short)100);
            another.setSpeed((byte)10);
            exits[1] = exit;

            WarriorHelper.addWarriorToGroup(battle.getAlliances()[0].getAllies().get(0), mine);
            WarriorHelper.addWarriorToGroup(battle.getAlliances()[1].getAllies().get(0), another);


            BattleConfiguration battleConfiguration = new BattleConfiguration();
            battleConfiguration.setAbilityMax((short) 1000);
            battleConfiguration.setAccurateShootFix(1.3);
            ActionFees actionFees = new ActionFees();
            actionFees.setChangeWeapon((byte) 1);
            actionFees.setMove((byte) 2);
            actionFees.setPickupTackle((byte) 3);
            actionFees.setRechargeWeapon((byte) 4);
            actionFees.setShutOrOpen((byte) 5);
            actionFees.setSitOrStand((byte) 3);

            battleConfiguration.setActionFees(actionFees);
            battleConfiguration.setActiveTime(60);
            battleConfiguration.setArmorSpoiling(4);
            battleConfiguration.setCriticalBarrierToVictimDistance(10);
            battleConfiguration.setKillExperienceMultiplier(2);
            Underload[] underloads = new Underload[2];

            Underload underload = new Underload();
            underload.setAction((byte) 10);
            underload.setMax((byte) 1);
            underload.setMin((byte) 1);
            underloads[0] = underload;

            underload = new Underload();
            underload.setAction((byte) 2);
            underload.setMin((byte) 11);
            underload.setMax((byte) 20);
            underloads[1] = underload;

            battleConfiguration.setUnderloads(underloads);
            battleConfiguration.setMinimalMapSize(20);
            battleConfiguration.setQuickShootFix(1.2);
            battleConfiguration.setSitHunterShootFix(1.3);
            battleConfiguration.setStandHunterShootFix(1.5);
            battleConfiguration.setSitVictimShootFix(0.8);
            battleConfiguration.setStandVictimShootFix(1);
            battleConfiguration.setWeaponSpoiling(3);
            battleConfiguration.setWalkSpeed(5);
            SubordinationDamage[] subordinationDamages = new SubordinationDamage[2];
            SubordinationDamage subordinationDamage = new SubordinationDamage();
            subordinationDamage.setDamage((byte) 3);
            subordinationDamage.setMaxRankDifference((byte) 2);
            subordinationDamage.setMinRankDifference((byte) 0);
            subordinationDamages[0] = subordinationDamage;

            subordinationDamage = new SubordinationDamage();
            subordinationDamage.setDamage((byte) 2);
            subordinationDamage.setMaxRankDifference((byte) 6);
            subordinationDamage.setMinRankDifference((byte) 3);
            subordinationDamages[1] = subordinationDamage;

            battleConfiguration.setSubordinationDamage(subordinationDamages);

            ClientAllyCollection allies = new ClientAllyCollection();
            allies.setAllies(new Vector());
            allies.add(mine);
            battleConfiguration.setObserver(new StrictPerimeterObserver(allies));
            battleConfiguration.setRouter(new RecursiveWaveRouter());
            ClientConfigurationFactory.getConfiguration().setBattleConfiguration(battleConfiguration);
            account.setSecurity(new SecurityOperationManager());
            account.getSecurity().setAccount(account);

            PRegolithPanelManager manager = PRegolithPanelManager.getInstance();
            manager.hideAll();
            manager.getBattleScreen().setBattle(battle);
            manager.show(manager.getBattleScreen());
            manager.show(manager.getBattleMenuWindow());
            manager.show(manager.getBattleSelectWarriorWindow());
            manager.show(manager.getBattleWeaponMenuWindow());
        }
    }
}
