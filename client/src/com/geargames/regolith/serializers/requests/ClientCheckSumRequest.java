package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.units.Account;

/**
 * User: mvkutuzov
 * Date: 22.04.13
 * Time: 12:45
 */
public class ClientCheckSumRequest extends ClientSerializedMessage {
    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public short getType() {
        return Packets.CONTROL_SUM;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(account.getSecurity().getObserve(), buffer);
    }
}
