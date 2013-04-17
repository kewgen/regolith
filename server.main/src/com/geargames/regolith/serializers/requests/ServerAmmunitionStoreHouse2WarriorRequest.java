package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.*;
import com.geargames.regolith.helpers.ServerDataBaseHelper;
import com.geargames.regolith.helpers.StoreHouseHelper;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Date: 09.01.13
 * Time: 10:20
 */
public class ServerAmmunitionStoreHouse2WarriorRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Warrior warrior = ServerDataBaseHelper.getWarriorById(SimpleDeserializer.deserializeInt(from));
        if (warrior != null) {
            short number = SimpleDeserializer.deserializeShort(from);
            int amount = SimpleDeserializer.deserializeShort(from);
            int realAmount = SimpleDeserializer.deserializeShort(from);
            int tackleId = SimpleDeserializer.deserializeInt(from);
            StoreHouse storeHouse = client.getAccount().getBase().getStoreHouse();
            AmmunitionPacket packet = StoreHouseHelper.getAmmunition(storeHouse, number);
            Ammunition ammunition = packet.getAmmunition();

            if (packet != null && tackleId == ammunition.getId()) {
                BaseConfiguration configuration = MainServerConfigurationFactory.getConfiguration().getRegolithConfiguration().getBaseConfiguration();
                if (realAmount == TackleTransitionHelper.moveAmmunitionStoreHouse2Warrior(storeHouse, number, amount, warrior, configuration)) {
                    return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_ONTO_WARRIOR);
                } else {
                    return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_ONTO_WARRIOR);
                }
            } else {
                return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_ONTO_WARRIOR);
            }
        }
        throw new RegolithException();
    }
}
