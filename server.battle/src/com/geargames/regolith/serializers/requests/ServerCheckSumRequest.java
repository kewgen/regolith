package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.managers.CommonBattleManager;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.regolith.serializers.answers.BattleClientIsCheating;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.state.ClientActivationAwaiting;
import com.geargames.regolith.units.battle.ServerBattle;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 25.08.12
 * Time: 22:28
 */
public class ServerCheckSumRequest extends ServerRequest {

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        if (SimpleDeserializer.deserializeInt(from) != client.getAccount().getSecurity().getObserve()) {

            ServerBattle serverBattle = ((BattleClient) client).getServerBattle();
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(serverBattle.getClients()),
                    new BattleClientIsCheating(to, client.getAccount()).serialize()));
            CommonBattleManager.cheaterOnBattle(serverBattle, client.getAccount());
            CommonBattleManager.closeBattle(serverBattle);
        } else {
            client.setState(new ClientActivationAwaiting());
        }
        return messages;
    }

}
