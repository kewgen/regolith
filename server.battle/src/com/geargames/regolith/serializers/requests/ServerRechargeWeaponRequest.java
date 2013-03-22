package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 29.09.12
 * Time: 9:54
 */
public class ServerRechargeWeaponRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerRechargeWeaponRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);

        if(WarriorHelper.rechargeWeapon(warrior, warrior.getWeapon().getProjectile())){
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(serverBattle.getClients()),
                    ServerConfirmationAnswer.answerSuccess(to, Packets.RECHARGE_WEAPON).serialize()));
        }else{
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(serverBattle.getClients()),
                    ServerConfirmationAnswer.answerFailure(to, Packets.RECHARGE_WEAPON).serialize()));
        }

        return messages;
    }
}
