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
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.*;

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
        BaseConfiguration baseConfiguration = ClientConfigurationFactory.getConfiguration().getBaseConfiguration();
        Account account = clientConfiguration.getAccount();
        BattleTypeCollection types = baseConfiguration.getBattleTypes();
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
            for (int i = 0; i < battle.getAlliances().length; i++) {
                BattleAlliance alliance = new BattleAlliance();
                ClientBattleGroupCollection battleGroups = new ClientBattleGroupCollection();
                alliance.setAllies(battleGroups);
                BattleGroup group = new BattleGroup();
                if (i == 0) {
                    group.setAccount(account);
                } else {
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

            Barrier barBarrier0 = baseConfiguration.getBarriers().get(0);
            Barrier barBarrier1 = baseConfiguration.getBarriers().get(1);
            Barrier fenceBarrier0l = baseConfiguration.getBarriers().get(2);
            Barrier fenceBarrier0r = baseConfiguration.getBarriers().get(3);
            Barrier fenceBarrier1l = baseConfiguration.getBarriers().get(4);
            Barrier fenceBarrier1r = baseConfiguration.getBarriers().get(5);

            battleMap.getCells()[3][0].addElement(barBarrier0);
            battleMap.getCells()[3][1].addElement(fenceBarrier1l);
            battleMap.getCells()[4][1].addElement(fenceBarrier1r);
            battleMap.getCells()[5][1].addElement(fenceBarrier1l);
            battleMap.getCells()[6][1].addElement(fenceBarrier1r);
            battleMap.getCells()[7][1].addElement(barBarrier1);
            battleMap.getCells()[7][2].addElement(barBarrier1);
            battleMap.getCells()[9][1].addElement(barBarrier1);
            battleMap.getCells()[9][2].addElement(barBarrier1);
            battleMap.getCells()[4][2].addElement(fenceBarrier0r);
            battleMap.getCells()[4][3].addElement(fenceBarrier0l);
            battleMap.getCells()[4][4].addElement(barBarrier1);
            battleMap.getCells()[5][4].addElement(barBarrier1);
            battleMap.getCells()[6][4].addElement(barBarrier1);
            battleMap.getCells()[6][5].addElement(fenceBarrier0r);
            battleMap.getCells()[6][6].addElement(fenceBarrier0l);
            battleMap.getCells()[8][4].addElement(barBarrier1);
            battleMap.getCells()[9][4].addElement(barBarrier1);
            battleMap.getCells()[8][5].addElement(fenceBarrier0r);
            battleMap.getCells()[8][6].addElement(fenceBarrier0l);

            battleMap.getCells()[0][3].addElement(fenceBarrier1l);
            battleMap.getCells()[1][3].addElement(fenceBarrier1r);
            battleMap.getCells()[2][3].addElement(barBarrier0);
            battleMap.getCells()[2][4].addElement(barBarrier0);
            battleMap.getCells()[2][5].addElement(barBarrier0);
            battleMap.getCells()[1][5].addElement(barBarrier1);

            battleMap.getCells()[1][8].addElement(barBarrier1);
            battleMap.getCells()[1][7].addElement(barBarrier1);
            battleMap.getCells()[2][7].addElement(barBarrier1);
            battleMap.getCells()[3][7].addElement(fenceBarrier1l);
            battleMap.getCells()[4][7].addElement(fenceBarrier1r);
            battleMap.getCells()[4][6].addElement(barBarrier0);
            battleMap.getCells()[4][8].addElement(barBarrier0);

            battleMap.getCells()[6][8].addElement(fenceBarrier0r);
            battleMap.getCells()[6][9].addElement(fenceBarrier0l);
            battleMap.getCells()[8][7].addElement(barBarrier1);

            HumanElement unitMine = new ClientHumanElement();
            Warrior mineWarrior = account.getWarriors().get(0);
            unitMine.setHuman(mineWarrior);
            unitMine.setDirection(Direction.LEFT_RIGHT);
            HumanElement anotherMine = new ClientHumanElement();
            Warrior anotherWarrior = account.getWarriors().get(1);
            anotherMine.setHuman(anotherWarrior);
            anotherMine.setDirection(Direction.LEFT_RIGHT);

            ExitZone[] exits = new ExitZone[2];
            battleMap.setExits(exits);
            ExitZone exit = new ExitZone();
            exit.setX((short) 1);
            exit.setY((short) 1);
            exit.setxRadius((byte) 1);
            exit.setyRadius((byte) 1);
            WarriorHelper.putWarriorIntoMap(battleMap.getCells(), unitMine, 0, 0);
            mineWarrior.setNumber((short) 1);
            mineWarrior.setActionScore((short) 100);
            mineWarrior.setSpeed((byte) 100);
            battle.getAlliances()[0].setExit(exit);
            exits[0] = exit;

            exit = new ExitZone();
            exit.setX((short) 18);
            exit.setY((short) 18);
            exit.setxRadius((byte) 1);
            exit.setyRadius((byte) 1);
            battle.getAlliances()[1].setExit(exit);
            WarriorHelper.putWarriorIntoMap(battleMap.getCells(), anotherMine, 18, 18);
            anotherWarrior.setNumber((short) 2);
            anotherWarrior.setActionScore((short) 100);
            anotherWarrior.setSpeed((byte) 100);
            exits[1] = exit;

            WarriorHelper.addWarriorToGroup(battle.getAlliances()[0].getAllies().get(0), mineWarrior);
            WarriorHelper.addWarriorToGroup(battle.getAlliances()[1].getAllies().get(0), anotherWarrior);


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

            ClientHumanElementCollection allies = new ClientHumanElementCollection();
            allies.setElements(new Vector());
            allies.add(unitMine);
            battleConfiguration.setObserver(new StrictPerimeterObserver(allies));
            battleConfiguration.setRouter(new RecursiveWaveRouter());
            ClientConfigurationFactory.getConfiguration().setBattleConfiguration(battleConfiguration);

            PRegolithPanelManager manager = PRegolithPanelManager.getInstance();
            manager.hideAll();
            manager.getBattleScreen().setBattle(battle);
            manager.show(manager.getBattleScreen());
            manager.show(manager.getBattleMenuWindow());
            manager.show(manager.getBattleWarriorListWindow());
            manager.show(manager.getBattleWeaponMenuWindow());
            manager.show(manager.getBattleWarriorMenuWindow());
            manager.show(manager.getBattleShotMenuWindow());
            manager.getBattleScreen().onMyTurnStarted();
        }
    }

}
