package com.geargames.regolith.serializers.answers;

import com.geargames.Debug;
import com.geargames.common.packer.PFrame;
import com.geargames.packer.Image;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleDeserializer;

import java.io.IOException;

/**
 * User: mikhail v. kutuzov
 *
 */
public class ClientGetFrameAnswer extends ClientDeSerializedMessage {
    private PFrame frame;

    public PFrame getFrame() {
        return frame;
    }

    protected void deSerialize(MicroByteBuffer buffer) {
        int id = SimpleDeserializer.deserializeInt(buffer);
        byte[] data = SimpleDeserializer.deserializeBytes(buffer);
        try {
            Image image = Image.createImage(data,0,data.length);
            PFrame frame = new PFrame(0,0,image.getWidth(),image.getHeight());
            frame.setImage(image);
            frame.setBid(id);
            frame.setPid(id);
        } catch (IOException e) {
            Debug.log(com.geargames.common.String.valueOfC("Ошибка при считывании серверной картинки"));
            Debug.logEx(e);
        }
    }
}
