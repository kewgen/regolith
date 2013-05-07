package com.geargames.regolith;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Weapon;
import junit.framework.Assert;
import org.junit.Test;

/**
 * User: mikhail v. kutuzov
 * Date: 06.02.13
 * Time: 15:26
 */
public class ClientSerializationTest {

    @Test
    public void batchMessageTest() {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();

        BatchRequest batchRequest = new BatchRequest(configuration);
        batchRequest.setRequests(new ArrayList());

        Weapon weapon = new Weapon();
        weapon.setId(1);
        Warrior warrior = new Warrior();
        warrior.setId(1);
        warrior.setMembershipType(Human.WARRIOR);

        ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(configuration);
        move.setNumber((short) 0);
        move.setWarrior(warrior);
        move.setAmount((short) 1);
        move.setRealAmount((short) 1);
        move.setTackle(weapon);
        move.setType(Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_STORE_HOUSE);


        batchRequest.getRequests().add(move);
        batchRequest.getRequests().add(move);
        batchRequest.getRequests().add(move);

        byte[] bytes = batchRequest.serialize();
        MicroByteBuffer buffer = new MicroByteBuffer(bytes);

        short bLength = SimpleDeserializer.deserializeShort(buffer);
        short bType = SimpleDeserializer.deserializeShort(buffer);

        Assert.assertEquals("it is not BATCH_MESSAGE", Packets.BATCH_MESSAGE, bType);
        Assert.assertEquals("a batch message length does not match", 18 * 3, bLength);

        bLength = SimpleDeserializer.deserializeShort(buffer);
        bType = SimpleDeserializer.deserializeShort(buffer);

        Assert.assertEquals(Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_STORE_HOUSE, bType);
        Assert.assertEquals(18 - 4, bLength);
    }
}
