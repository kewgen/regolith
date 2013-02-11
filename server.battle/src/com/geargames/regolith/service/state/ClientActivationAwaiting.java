package com.geargames.regolith.service.state;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.answers.ServerMessageIsLateAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;

/**
 * User: mikhail v. kutuzov
 * Date: 24.08.12
 * Time: 14:20
 */
public class ClientActivationAwaiting extends BattleState {
    private ClientAtBattle clientAtBattle;

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        BattleServiceConfigurationFactory.getConfiguration().
                getWriter().addMessageToClient(
                    new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                            new ServerMessageIsLateAnswer(getWriteBuffer()).serialize())
        );
    }

    public ClientAtBattle getClientAtBattle() {
        return clientAtBattle;
    }

    public void setClientAtBattle(ClientAtBattle clientAtBattle) {
        this.clientAtBattle = clientAtBattle;
    }
}
