package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.ServerDataBaseHelper;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Date: 09.01.13
 * Time: 15:25
 */
public class ServerTackleWarrior2StoreHouseRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Warrior warrior = ServerDataBaseHelper.getWarriorById(SimpleDeserializer.deserializeInt(from));
        if (warrior != null) {
            int tackleId = SimpleDeserializer.deserializeInt(from);

            StoreHouse house = client.getAccount().getBase().getStoreHouse();
            StateTackle tackle = TackleTransitionHelper.findTackleOnWarrior(warrior, tackleId);
            if (tackle != null) {
                if (TackleTransitionHelper.moveStateTackleWarrior2StoreHouse(warrior, tackle, house)) {
                    return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_STORE_HOUSE);
                } else {
                    return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_STORE_HOUSE);
                }
            } else {
                 return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_STORE_HOUSE);
            }
        } else {
            throw new RegolithException();
        }
    }
}
