package com.geargames.regolith.service.clientstates;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.*;

/**
 * Абстрактный класс "состояние клиента" на сервере. В зависимости от реализации данного класса зачитывается
 * клиетский буфер сообщений и посылаются ответные сообщения.
 */
public abstract class ClientState {

    /**
     * Сообщение обслуживается в зависимости от его типа.
     * @param from
     * @param
     * client
     * @throws RegolithException
     */
    public void execute(MicroByteBuffer from, Client client) throws RegolithException {
        short type = SimpleDeserializer.deserializeShort(from);
        execute(from, client, type);
    }

    protected abstract void execute(MicroByteBuffer from, Client client, short type) throws RegolithException;

    protected abstract MicroByteBuffer getWriteBuffer();

}
