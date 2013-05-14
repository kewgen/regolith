package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerDataBaseHelper;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Date: 09.01.13
 * Time: 13:27
 */
public class ServerTackleStoreHouseToBagRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        Warrior warrior = ServerDataBaseHelper.getWarriorById(SimpleDeserializer.deserializeInt(from));
        if (warrior != null) {
            short number = SimpleDeserializer.deserializeShort(from);
            SimpleDeserializer.deserializeShort(from);
            SimpleDeserializer.deserializeShort(from);
            int tackleId = SimpleDeserializer.deserializeInt(from);

            BaseConfiguration baseConfiguration = MainServerConfigurationFactory.getConfiguration().getRegolithConfiguration().getBaseConfiguration();
            StoreHouse storeHouse = client.getAccount().getBase().getStoreHouse();
            StateTackle tackle = TackleTransitionHelper.moveStateTackleStoreHouse2Bag(storeHouse, number, warrior, baseConfiguration);
            if (tackle != null && tackle.getId() == tackleId) {
                return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.TAKE_TACKLE_FROM_STORE_HOUSE_PUT_INTO_BAG);
            } else {
                return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_TACKLE_FROM_STORE_HOUSE_PUT_INTO_BAG);
            }
        }
        throw new RegolithException();
    }
}
