package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;

import java.awt.*;
import java.io.*;

/**
 * User: mikhail v. kutuzov
 * Предназначен для зачитывания файла изо
 */
public class ServerGetFrameAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private File file;

    public ServerGetFrameAnswer(MicroByteBuffer buffer, File file) {
        this.buffer = buffer;
        this.file = file;
    }

    @Override
    public short getType() {
        return Packets.FRAME_MESSAGE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            int size = (int) file.length();
            buffer.position(0);
            String name = file.getName();
            name = name.substring(0, name.indexOf(".") - 1 );
            int id = Integer.valueOf(file.getParent() + name);
            SimpleSerializer.serialize(id, buffer);
            input.read(buffer.getBytes(), buffer.position(), size);
            buffer.position(size + 4);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
}
