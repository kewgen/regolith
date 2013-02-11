package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.ServerDataBaseHelper;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Переместить предмет из сумки бойца на склад пользователя.
 */
public class ServerTackleBag2StoreHouseRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Warrior warrior = ServerDataBaseHelper.getWarriorById(SimpleDeserializer.deserializeInt(from));
        if (warrior != null) {
            short number = SimpleDeserializer.deserializeShort(from);
            SimpleDeserializer.deserializeShort(from);
            SimpleDeserializer.deserializeShort(from);
            int tackleId = SimpleDeserializer.deserializeInt(from);

            StateTackle tackle = TackleTransitionHelper.moveStateTackleBag2StoreHouse(warrior, number, client.getAccount().getBase().getStoreHouse());
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
