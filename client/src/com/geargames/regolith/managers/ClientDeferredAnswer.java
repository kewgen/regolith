package com.geargames.regolith.managers;

import com.geargames.regolith.application.Manager;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;

/**
 * User: mkutuzov
 * Date: 05.07.12
 */
public class ClientDeferredAnswer {
    private ClientDeSerializedMessage deSerializedMessage;

    public ClientDeferredAnswer(){
    }

    public void setDeSerializedMessage(ClientDeSerializedMessage deSerializedMessage) {
        this.deSerializedMessage = deSerializedMessage;
    }

    public ClientDeferredAnswer(ClientDeSerializedMessage deSerializedMessage) {
        this.deSerializedMessage = deSerializedMessage;
    }

    public ClientDeSerializedMessage getAnswer() {
        if (deSerializedMessage.ready()) {
            return deSerializedMessage;
        } else {
            return null;
        }
    }

    public boolean retrieve(int attempt) throws Exception {
        int i = 0;
        while (getAnswer() == null) {
            if (i++ >= attempt) {
                return false;
            }
            Manager.pause(100);
        }
        getAnswer().deSerialize();
        return true;
    }

}
