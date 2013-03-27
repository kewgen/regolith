package com.geargames.regolith.managers;

import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.BattleManagerContext;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.helpers.BattleHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.BattleCell;

import java.io.IOException;
import java.util.Set;

/**
 * Класс менеджер для тренировочных битв.
 */
public class ServerTrainingBattleCreationManager {

    private MainServerConfiguration configuration;

    public ServerTrainingBattleCreationManager() {
        configuration = MainServerConfigurationFactory.getConfiguration();
    }

    public boolean evictAccount(BattleAlliance alliance, Account account) {
        for (BattleGroup battleGroup : ((ServerBattleGroupCollection) alliance.getAllies()).getBattleGroups()) {
            if (battleGroup.getAccount() == account) {
                doNotListenToBattle(alliance.getBattle(), account);
                isNotReady(battleGroup);
                battleGroup.setAccount(null);
                ((ServerWarriorCollection) battleGroup.getWarriors()).getWarriors().clear();
                // Не забыть изменить стейт у клиента
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

    public Battle startBattle(Account author) {
        BattleManagerContext battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        Battle battle = battleManagerContext.getCreatedBattles().get(author);

        if (battleManagerContext.getCompleteGroups().get(battle).size() < battle.getBattleType().getAllianceAmount() * battle.getBattleType().getAllianceSize() * battle.getBattleType().getGroupSize()) {
            return null;
        }
        if (!((ServerBattleType) (battle.getBattleType())).haveToStart(battle)) {
            return null;
        }
        try {
            BattleCell[][] cells = BattleHelper.deserializeBattleCells(battle.getMap().getContent());
            battle.getMap().setCells(cells);
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
        BattleHelper.spreadAlliancesOnTheMap(battle);

        return battle;
    }

    public void cancelBattle(Account author) {
        BattleManagerContext battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        Battle battle = battleManagerContext.getCreatedBattles().get(author);
        battleManagerContext.getCreatedBattles().remove(author);
        battleManagerContext.getBattlesById().remove(battle.getId());
        battleManagerContext.getCompleteGroups().remove(battle);
        battleManagerContext.getBattleListeners().remove(battle);
    }

    public boolean completeGroup(BattleGroup group, Warrior[] warriors) {
        if (warriors.length > group.getAlliance().getBattle().getBattleType().getGroupSize()) {
            return false;
        }
        for (Warrior warrior : warriors) {
            WarriorHelper.addWarriorToGroup(group, warrior);
        }
        return true;
    }

    public void doNotListenToBattle(Battle battle, Account account) {
        configuration.getServerContext().getBattleManagerContext().getBattleListeners().get(battle).remove(account);
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

    public boolean isNotReady(BattleGroup group) {
        Battle battle = group.getAlliance().getBattle();
        Set<BattleGroup> groups = configuration.getServerContext().getBattleManagerContext().getCompleteGroups().get(battle);
        return groups != null && groups.contains(group) && groups.remove(group);
    }

}
