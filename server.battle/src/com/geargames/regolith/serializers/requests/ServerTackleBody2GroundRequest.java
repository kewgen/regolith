package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerTackleBody2GroundAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.BodyParticles;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerHumanElementCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.HumanElement;
import com.geargames.regolith.units.tackle.StateTackle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 17.08.12
 * Time: 20:30
 */
public class ServerTackleBody2GroundRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerTackleBody2GroundRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short x = SimpleDeserializer.deserializeShort(from);
        short y = SimpleDeserializer.deserializeShort(from);
        byte bodyParticle = from.get();

        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(2);
        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);
        BattleMap map = serverBattle.getBattle().getMap();
        BattleCell[][] cells = map.getCells();
        StateTackle stateTackle = null;

        ServerHumanElementCollection units = serverBattle.getHumanElements();
        HumanElement unit = BattleMapHelper.getHumanElementByHuman(units, warrior);

        if (BattleMapHelper.isNear(unit, x, y) && cells[x][y].getElement() == null) {
            if (BodyParticles.HEAD == bodyParticle) {
                stateTackle = warrior.getHeadArmor();
            } else if (BodyParticles.TORSO == bodyParticle) {
                stateTackle = warrior.getTorsoArmor();
            } else if (BodyParticles.LEGS == bodyParticle) {
                stateTackle = warrior.getLegsArmor();
            } else {
                stateTackle = warrior.getWeapon();
            }
            BattleMapHelper.putIn(stateTackle, map, x, y);
        }

        if (stateTackle != null) {
            Set<Client> clients = new HashSet<Client>();
            clients.addAll(serverBattle.getClients());
            clients.remove(client);

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerSuccess(to, Packets.TAKE_TACKLE_FROM_BODY_PUT_ON_GROUND).serialize()));
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                    new ServerTackleBody2GroundAnswer(to, warrior, stateTackle, x, y).serialize()));
        } else {
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_TACKLE_FROM_BODY_PUT_ON_GROUND).serialize()));
        }
        return messages;
    }

}
