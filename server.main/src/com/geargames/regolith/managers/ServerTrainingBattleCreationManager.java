package com.geargames.regolith.managers;

import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.BattleManagerContext;
import com.geargames.regolith.service.states.ClientAtBattleMarket;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;

import java.io.IOException;
import java.nio.channels.SocketChannel;
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
                battleGroup.setAccount(null);
                ((ServerWarriorCollection) battleGroup.getWarriors()).getWarriors().clear();

                //todo: Верно ли подобрано место для смены клиентского стейта?
                SocketChannel channel = configuration.getServerContext().getChannel(account);
                Client client = configuration.getServerContext().getClient(channel);
                client.setState(new ClientAtBattleMarket());
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
        if (!((ServerBattleType)(battle.getBattleType())).haveToStart(battle)) {
            return null;
        }
        try {
            battle.getMap().setCells(BattleHelper.deserializeBattleCells(battle.getMap().getContent()));
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
        return groups != null && !groups.contains(group) && groups.add(group);
    }

    public boolean isNotReady(BattleGroup group) {
        Battle battle = group.getAlliance().getBattle();
        Set<BattleGroup> groups = configuration.getServerContext().getBattleManagerContext().getCompleteGroups().get(battle);
        return groups != null && groups.contains(group) && groups.remove(group);
    }

}
