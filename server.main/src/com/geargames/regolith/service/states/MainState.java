package com.geargames.regolith.service.states;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.service.clientstates.ClientState;

/**
 * User: mikhail v. kutuzov
 * Date: 30.08.12
 * Time: 13:20
 */
public abstract class MainState extends ClientState {
    public static  ThreadLocal<MicroByteBuffer> MICRO_BYTE_BUFFER = new ThreadLocal<MicroByteBuffer>(){
        @Override
        protected MicroByteBuffer initialValue() {
            return new MicroByteBuffer(new byte[MainServerConfigurationFactory.getConfiguration().getMessageBufferSize()]);
        }
    };

    @Override
    protected MicroByteBuffer getWriteBuffer() {
        MicroByteBuffer buffer = MICRO_BYTE_BUFFER.get();
        buffer.clear();
        return buffer;
    }

}
