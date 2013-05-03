package com.geargames.regolith.managers;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.BattleManagerContext;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerHumanElementCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.BattleCell;

import java.io.IOException;
import java.util.Set;

/**
 * Класс менеджер для тренировочных битв.
 * Users: m.v.kutuzov, abarakov
 */
public class ServerTrainingBattleCreationManager {

    private MainServerConfiguration configuration;

    public ServerTrainingBattleCreationManager() {
        configuration = MainServerConfigurationFactory.getConfiguration();
    }

    public boolean evictAccount(BattleAlliance alliance, Account account) {
        for (BattleGroup battleGroup : ((ServerBattleGroupCollection) alliance.getAllies()).getBattleGroups()) {
            if (battleGroup.getAccount() == account) {
                isNotReady(battleGroup);
                battleGroup.setAccount(null);
                ((ServerWarriorCollection) battleGroup.getWarriors()).getWarriors().clear();
                return true;
            }
        }
        return false;
    }

    public BattleGroup joinToAlliance(BattleAlliance alliance, Account participant) {
        for (BattleGroup group : ((ServerBattleGroupCollection) alliance.getAllies()).getBattleGroups()) {
            if (group.getAccount() == null) {
                group.setAccount(participant);
                return group;
            }
        }
        return null;
    }

    public Battle startBattle(Account author) throws RegolithException {
        BattleManagerContext battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        Battle battle = battleManagerContext.getCreatedBattles().get(author);
        BattleType battleType = battle.getBattleType();

        if (battleManagerContext.getCompleteGroups().get(battle).size() < battleType.getAllianceAmount() * battleType.getAllianceSize()) {
            throw new RegolithException("There are incomplete groups.");
        }
        if (!((ServerBattleType) (battleType)).haveToStart(battle)) {
            throw new RegolithException("Warriors amount is not valid.");
        }
        try {
            BattleCell[][] cells = ServerBattleHelper.deserializeBattleCells(battle.getMap().getContent());
            battle.getMap().setCells(cells);
        } catch (IOException e) {
            throw new RegolithException(e);
        } catch (ClassNotFoundException e) {
            throw new RegolithException(e);
        }
        ServerHumanElementCollection units = ServerBattleHelper.getBattleUnits(battle); //todo: ????? где-то сохранить units (в ServerBattle)
        ServerBattleHelper.spreadAlliancesOnTheMap(battle, units);

        for (BattleAlliance alliance : battle.getAlliances()) {
            ServerBattleGroupCollection groups = (ServerBattleGroupCollection) alliance.getAllies();
            for (BattleGroup group : groups.getBattleGroups()) {
                WarriorHelper.numerateWarriors(alliance, group);
            }
        }
        return battle;
    }

    public void cancelBattle(Account author) {
        BattleManagerContext battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        Battle battle = battleManagerContext.getCreatedBattles().remove(author);
        battleManagerContext.getBattlesById().remove(battle.getId());
        battleManagerContext.getBattlesByAccount().remove(author);
        battleManagerContext.getBattleListeners().remove(battle);
        battleManagerContext.getCompleteGroups().remove(battle);
    }

    /**
     * Метод добавляет бойцов в боевую группу.
     *
     * @param group
     * @param warriors
     * @return false, если какой-то боец уже находился в группе или число бойцов в группе превысило предел.
     */
    public boolean addWarriors(BattleGroup group, Warrior[] warriors) {
        if (group.getWarriors().size() > 0 || warriors.length != group.getAlliance().getBattle().getBattleType().getGroupSize()) {
            return false;
        } else {
            for (Warrior warrior : warriors) {
                WarriorHelper.addWarriorToGroup(group, warrior);
            }
            return true;
        }
    }

    public void removeWarriors(BattleGroup group) {
        for (Warrior warrior : ((ServerWarriorCollection) group.getWarriors()).getWarriors()) {
            warrior.setBattleGroup(null);
        }
        ((ServerWarriorCollection) group.getWarriors()).getWarriors().clear();
    }

    public boolean doNotListenToBattle(Battle battle, Account account) {
        BattleManagerContext battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        battleManagerContext.getBattlesByAccount().remove(account);
        return battleManagerContext.getBattleListeners().get(battle).remove(account);
    }

    public boolean isReady(BattleGroup group) {
        Battle battle = group.getAlliance().getBattle();
        Set<BattleGroup> groups = configuration.getServerContext().getBattleManagerContext().getCompleteGroups().get(battle);

        if (groups != null && !groups.contains(group)) {
            WarriorCollection warriors = group.getWarriors();
            if (battle.getBattleType().getGroupSize() == warriors.size()) {
                for (int i = 0; i < warriors.size(); i++) {
                    Warrior warrior = warriors.get(i);
                    if (warrior == null || warrior.getHealth() <= 0) {
                        return false;
                    }
                }
                return groups.add(group);
            }
        }
        return false;
    }

    /**
     * Исключить группу из списка готовых к битве.
     *
     * @param group
     * @return
     */
    public boolean isNotReady(BattleGroup group) {
        Battle battle = group.getAlliance().getBattle();
        Set<BattleGroup> groups = configuration.getServerContext().getBattleManagerContext().getCompleteGroups().get(battle);
        return groups != null && groups.contains(group) && groups.remove(group);
    }

}
