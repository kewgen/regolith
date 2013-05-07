package com.geargames.regolith.serializers;

import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 14.08.12
 * Time: 17:30
 */
public class BattleServiceRequestUtils {

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

    public static List<SocketChannel> getRecipients(Collection<? extends Client> clients) {
        List<SocketChannel> recipients = new LinkedList<SocketChannel>();
        for (Client client : clients) {
            recipients.add(client.getChannel());
        }
        return recipients;
    }

    public static BattleGroup getBattleGroupFromServerBattle(ServerBattle serverBattle, byte allianceNumber, int groupId) {
        Battle battle = serverBattle.getBattle();
        BattleAlliance alliance = battle.getAlliances()[allianceNumber];
        ServerBattleGroupCollection groups = (ServerBattleGroupCollection) alliance.getAllies();

        for (BattleGroup group : groups.getBattleGroups()) {
            if (group.getId() == groupId) {
                return group;
            }
        }
        return null;
    }

}
