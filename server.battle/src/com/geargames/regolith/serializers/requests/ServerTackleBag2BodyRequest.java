package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BagHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerTackleBag2BodyAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.BodyParticles;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.Weapon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 28.09.12
 * Time: 23:04
 */
public class ServerTackleBag2BodyRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerTackleBag2BodyRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short number = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        Bag bag = warrior.getBag();

        if (bag.getTackles().size() <= number) {
            throw new RegolithException();
        }

        StateTackle tackle = BagHelper.putOut(bag, number);
        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(2);

        if (tackle instanceof Armor) {
            Armor armor = (Armor) tackle;
            switch (armor.getArmorType().getBodyParticle()) {
                case BodyParticles.HEAD:
                    warrior.setHeadArmor(armor);
                    break;
                case BodyParticles.TORSO:
                    warrior.setTorsoArmor(armor);
                    break;
                case BodyParticles.LEGS:
                    warrior.setLegsArmor(armor);
                    break;
                default:
                    throw new RegolithException();
            }
        } else {
            Weapon weapon = (Weapon) tackle;
            warrior.setWeapon(weapon);
        }

        Set<Client> clients = new HashSet<Client>();
        clients.addAll(serverBattle.getClients());
        clients.remove(client);

        messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                ServerConfirmationAnswer.answerSuccess(to, Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_BODY).serialize()));
        messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                new ServerTackleBag2BodyAnswer(to, warrior, tackle).serialize()));
        return messages;
    }
}
