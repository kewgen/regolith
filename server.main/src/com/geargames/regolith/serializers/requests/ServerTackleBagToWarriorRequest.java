package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerDataBaseHelper;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Надеть предмет из сумки война.
 */
public class ServerTackleBagToWarriorRequest extends MainOneToClientRequest {

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Warrior warrior = ServerDataBaseHelper.getWarriorById(SimpleDeserializer.deserializeInt(from));
        if (warrior != null) {
            short number = SimpleDeserializer.deserializeShort(from);
            SimpleDeserializer.deserializeShort(from);
            SimpleDeserializer.deserializeShort(from);
            int tackleId = SimpleDeserializer.deserializeInt(from);

            StateTackle tackle = TackleTransitionHelper.moveStateTackleBag2Warrior(warrior, number);
            if (tackle != null && tackle.getId() == tackleId) {
                return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE);
            } else {
                return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE);
            }
        } else {
            throw new RegolithException();
        }
    }
}
