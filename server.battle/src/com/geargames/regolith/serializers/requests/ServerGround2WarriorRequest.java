package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerGround2WarriorAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.dictionaries.ServerHumanElementCollection;
import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.HumanElement;
import com.geargames.regolith.units.tackle.StateTackle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mkutuzov
 * Date: 24.07.12
 */
public abstract class ServerGround2WarriorRequest extends ServerRequest {
    private short type;
    private ServerBattle serverBattle;

    public ServerBattle getServerBattle() {
        return serverBattle;
    }

    protected ServerGround2WarriorRequest(ServerBattle serverBattle, short type) {
        this.serverBattle = serverBattle;
        this.type = type;
    }

    protected abstract boolean putStateTackle(CellElement element, BattleCell cell, Warrior warrior);

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short x = SimpleDeserializer.deserializeShort(from);
        short y = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        if (warrior == null) {
            throw new RegolithException("Warrior was not found");
        }

        ServerHumanElementCollection units = serverBattle.getHumanElements();
        HumanElement unit = BattleMapHelper.getHumanElementByHuman(units, warrior);

        StateTackle tackle = BattleMapHelper.peekStateTackle(unit, group.getAlliance().getBattle().getMap().getCells(), x, y);

        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(2);
        if (tackle != null && putStateTackle(tackle, group.getAlliance().getBattle().getMap().getCells()[x][y], warrior)) {
            Set<Client> clients = new HashSet<Client>();
            clients.addAll(serverBattle.getClients());
            clients.remove(client);

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerSuccess(writeBuffer, type).serialize()));

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                    new ServerGround2WarriorAnswer(writeBuffer, type, x, y, warrior).serialize()));

        } else {
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(writeBuffer, type).serialize()));
        }
        return messages;
    }

}
