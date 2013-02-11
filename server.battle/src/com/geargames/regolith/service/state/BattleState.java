package com.geargames.regolith.service.state;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.clientstates.ClientState;

/**
 * User: mikhail v. kutuzov
 * Date: 29.08.12
 * Time: 23:43
 */
public abstract class BattleState extends ClientState {
    public static  ThreadLocal<MicroByteBuffer> MICRO_BYTE_BUFFER = new ThreadLocal<MicroByteBuffer>(){
        @Override
        protected MicroByteBuffer initialValue() {
            return new MicroByteBuffer(new byte[BattleServiceConfigurationFactory.getConfiguration().getMessageBufferSize()]);
        }
    };

    @Override
    protected MicroByteBuffer getWriteBuffer() {
        MicroByteBuffer buffer = MICRO_BYTE_BUFFER.get();
        buffer.clear();
        return buffer;
    }
}
