package com.geargames.regolith.serializers;

import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.BattleManagerContext;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.ServerContext;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;

import java.nio.channels.SocketChannel;
import java.util.*;

public class MainServerRequestUtils {

    public static List<SocketChannel> recipientsByCreatedBattle(Battle battle) {
        ServerContext serverContext = MainServerConfigurationFactory.getConfiguration().getServerContext();
        BattleManagerContext battleManagerContext = serverContext.getBattleManagerContext();
        Set<Account> accounts = battleManagerContext.getBattleListeners().get(battle);
        List<SocketChannel> channels = new ArrayList<SocketChannel>(accounts.size());
        for (Account listener : accounts) {
            channels.add(serverContext.getChannel(listener));
        }
        return channels;
    }

    public static List<SocketChannel> singleRecipientByClient(Client client) {
        List<SocketChannel> recipients = new ArrayList<SocketChannel>(1);
        recipients.add(client.getChannel());
        return recipients;
    }

    public static Warrior getWarriorFromGroup(BattleGroup group, int warriorId) {
        for (Warrior warrior : ((ServerWarriorCollection) group.getWarriors()).getWarriors()) {
            if (warrior.getId() == warriorId) {
                return warrior;
            }
        }
        return null;
    }

    public static Set<Client> getBattleClients(Battle battle, ServerContext context) {
        Set<Client> clients = new HashSet<Client>();
        for (BattleAlliance alliance : battle.getAlliances()) {
            Set<Client> allianceClients = new HashSet<Client>(battle.getBattleType().getAllianceSize());
            BattleGroupCollection groups = alliance.getAllies();
            for (int i = 0; i < groups.size(); i++) {
                allianceClients.add(context.getClient(context.getChannel(groups.get(i).getAccount())));
            }
            clients.addAll(allianceClients);
        }
        return clients;
    }

    public static List<SocketChannel> getPassiveListenerChannels(Set<Client> players, ServerContext context, Battle battle) {
        Set<Account> listeners = context.getBattleManagerContext().getBattleListeners().get(battle);

        for (Client player : players) {
            listeners.remove(player.getAccount());
        }
        List<SocketChannel> channels = new ArrayList<SocketChannel>(listeners.size());
        for (Account account : listeners) {
            channels.add(context.getChannel(account));
        }

        return channels;
    }

    public static List<SocketChannel> getChannelsByClients(Collection<Client> clients){
        List<SocketChannel> channels = new ArrayList<SocketChannel>(clients.size());
        for(Client client : clients){
            channels.add(client.getChannel());
        }
        return channels;
    }
}
