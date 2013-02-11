package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerGetFrameAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfigurationFactory;

import java.io.File;

/**
 * User: mikhail v. kutuzov
 * Разобрать запрос на получение изображения фрейма и приготовить ответ на основе файла.
 */
public class ServerGetFrameRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        String id = "" + SimpleDeserializer.deserializeInt(from);
        String directoryName = id.substring(0,2);
        String fileName = id.substring(3, id.length()-1);
        String name = MainServerConfigurationFactory.getConfiguration().getImageDirectory()
                + File.pathSeparator + directoryName
                + File.pathSeparator + fileName;
        return new ServerGetFrameAnswer(writeBuffer, new File(name));
    }
}
