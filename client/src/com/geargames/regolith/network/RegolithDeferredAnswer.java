package com.geargames.regolith.network;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.common.serialization.ClientDeSerializedMessage;

/**
 * User: m.v.kutuzov
 * Date: 27.03.13
 */
public class RegolithDeferredAnswer extends ClientDeferredAnswer {
    @Override
    public void setDeSerializedMessage(ClientDeSerializedMessage deSerializedMessage) {
        super.setDeSerializedMessage(deSerializedMessage);
        deSerializedMessage.setBuffer(null);
    }
}
