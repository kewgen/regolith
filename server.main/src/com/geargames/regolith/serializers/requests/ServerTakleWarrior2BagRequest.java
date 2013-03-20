package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.ServerDataBaseHelper;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Date: 09.01.13
 * Time: 14:50
 * Обработка запроса: снять кладь с бойца и положить в сумку.
 */
public class ServerTakleWarrior2BagRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Warrior warrior = ServerDataBaseHelper.getWarriorById(SimpleDeserializer.deserializeInt(from));

        if (warrior != null) {
            StateTackle tackle = TackleTransitionHelper.findTackleOnWarrior(warrior, SimpleDeserializer.deserializeInt(from));
            if (tackle != null) {
                TackleTransitionHelper.moveStateTackleWarrior2Bag(warrior, tackle);
                return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG);
            } else {
                return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG);
            }
        } else {
            throw new RegolithException();
        }
    }
}
