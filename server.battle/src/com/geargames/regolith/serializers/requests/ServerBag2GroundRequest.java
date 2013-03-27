package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerBag2GroundAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 26.09.12
 * Time: 17:40
 */
public abstract class ServerBag2GroundRequest extends ServerRequest {
    private ServerBattle serverBattle;
    private short type;

    public ServerBag2GroundRequest(ServerBattle serverBattle, short type) {
        this.serverBattle = serverBattle;
        this.type = type;
    }

    public abstract Element putOut(short number, Warrior warrior);

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short x = SimpleDeserializer.deserializeShort(from);
        short y = SimpleDeserializer.deserializeShort(from);
        short number = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        BattleMap map = serverBattle.getBattle().getMap();
        BattleCell[][] cells = map.getCells();
        BattleCell cell = cells[x][y];
        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(1);

        if (BattleMapHelper.ableToPut(warrior, cells, x, y)) {
            Element element = putOut(number, warrior);
            if (element != null) {
                BattleMapHelper.putIn(element,map,x,y);

                Set<Client> others = new HashSet<Client>();
                others.addAll(serverBattle.getClients());
                others.remove(client);

                messages.add(new BattleMessageToClient(
                        BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerSuccess(to, type).serialize()
                ));

                messages.add(new BattleMessageToClient(
                    BattleServiceRequestUtils.getRecipients(others),
                    (new ServerBag2GroundAnswer(to, type, element, x, y)).serialize()
                ));

            } else {
                messages.add(new BattleMessageToClient(
                    BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerFailure(to, type).serialize()
                ));
            }
        } else {
            messages.add(new BattleMessageToClient(
                    BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, type).serialize()
            ));
        }

        return messages;
    }
}
