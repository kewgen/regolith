package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.answers.ServerCancelBattleAnswer;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.Account;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public class ServerCancelBattleRequest extends ServerOneToBattleClientsRequest {
    private ServerTrainingBattleCreationManager battleCreationManager;

    public ServerCancelBattleRequest(Account account) {
        super(MainServerConfigurationFactory.getConfiguration().getServerContext().getBattleManagerContext().getCreatedBattles().get(account));
        this.battleCreationManager = MainServerConfigurationFactory.getConfiguration().getBattleCreationManager();
    }

    @Override
    protected SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        if (battleManagerContext.getCreatedBattles().get(client.getAccount()) != null) {
            battleCreationManager.cancelBattle(client.getAccount());
        } else {
            throw new RegolithException();
        }
        return new ServerCancelBattleAnswer(writeBuffer);
    }
}
