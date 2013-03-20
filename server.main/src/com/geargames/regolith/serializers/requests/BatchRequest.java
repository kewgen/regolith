package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.answers.BatchAnswer;
import com.geargames.regolith.service.Client;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: mikhail v. kutuzov
 * Date: 10.01.13
 * Time: 9:45
 * Обслуживает запрос на обработку связки сообщений. С помощью простых расширений класса MainOneToClientRequest
 * обрабатываются конкретные сообщения клиента, а за тем ,сдесь же, происходит склеивание.
 */
public class BatchRequest extends MainOneToClientRequest {
    HashMap<Short, ServerRequest> processors;

    public BatchRequest(HashMap<Short, ServerRequest> processors) {
        this.processors = processors;
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        LinkedList<SerializedMessage> messages = new LinkedList<SerializedMessage>();
        BatchAnswer answer = new BatchAnswer(writeBuffer, messages);

        while (from.position() < from.limit()) {
            SimpleDeserializer.deserializeShort(from);
            MainOneToClientRequest request = (MainOneToClientRequest) processors.get(SimpleDeserializer.deserializeShort(from));
            if (request != null) {
                messages.add(request.clientRequest(from, writeBuffer, client));
            }
        }
        return answer;
    }
}

