package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerBag2BoxAnswer;
import com.geargames.regolith.service.BattleClient;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 27.09.12
 * Time: 9:42
 */
public abstract class ServerBag2BoxRequest extends ServerRequest {
    private ServerBattle serverBattle;
    private short type;

    public ServerBag2BoxRequest(ServerBattle serverBattle, short type) {
        this.serverBattle = serverBattle;
        this.type = type;
    }

    protected abstract CellElement moveBag2Box(short bagNumber, Box box, Warrior warrior) throws RegolithException;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short x = SimpleDeserializer.deserializeShort(from);
        short y = SimpleDeserializer.deserializeShort(from);
        short bagNumber = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        BattleMap map = serverBattle.getBattle().getMap();
        BattleCell[][] cells = map.getCells();
        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(2);

        Box box = (Box) BattleMapHelper.getElementByType(cells[x][y], CellElementTypes.BOX);

        CellElement element = moveBag2Box(bagNumber, box, warrior);
        if (element != null) {
            Set<BattleClient> others = new HashSet<BattleClient>();
            others.addAll(serverBattle.getClients());
            others.remove(client);

            messages.add(new BattleMessageToClient(
                    BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerSuccess(to, type).serialize()
            ));

            messages.add(new BattleMessageToClient(
                    BattleServiceRequestUtils.getRecipients(others),
                    new ServerBag2BoxAnswer(to, element, x, y, type).serialize()
            ));
        } else {
            messages.add(new BattleMessageToClient(
                    BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerSuccess(to, type).serialize()
            ));
        }

        return messages;
    }
}
