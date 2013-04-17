package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerDataBaseHelper;
import com.geargames.regolith.helpers.StoreHouseHelper;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Переместить расходник со склада пользователя в сумку бойца.
 */
public class ServerAmmunitionStoreHouse2BagRequest extends MainOneToClientRequest {

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Warrior warrior = ServerDataBaseHelper.getWarriorById(SimpleDeserializer.deserializeInt(from));
        if (warrior != null) {
            short number = SimpleDeserializer.deserializeShort(from);
            int amount = SimpleDeserializer.deserializeShort(from);
            int realAmount = SimpleDeserializer.deserializeShort(from);

            AmmunitionPacket packet = StoreHouseHelper.getAmmunition(client.getAccount().getBase().getStoreHouse(), number);
            if (packet != null && packet.getAmmunition().getId() == SimpleDeserializer.deserializeInt(from)) {
                amount = TackleTransitionHelper.moveAmmunitionBag2StoreHouse(warrior, number, amount, client.getAccount().getBase().getStoreHouse());
                if (realAmount == amount) {
                    return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE);
                } else {
                    return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE);
                }
            } else {
                return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE);
            }
        } else {
            throw new RegolithException();
        }
    }

}
