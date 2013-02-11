package com.geargames.regolith;

import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PUnresolvedFrameManger;
import com.geargames.packer.Image;
import com.geargames.regolith.network.DataMessage;
import com.geargames.regolith.network.Network;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.answers.ClientGetFrameAnswer;
import com.geargames.regolith.serializers.requests.ClientFrameRequest;

import java.io.IOException;
import java.util.Hashtable;

/**
 * User: mikhail v. kutuzov
 * Класс управляет фреймами которые подгружены с сервера по запросу.
 * В случае отсутсвия фрейма(не подгружен) возвращается пустой фрейм NULL_FRAME.
 */
public class RemoteFrameLoader extends PUnresolvedFrameManger {
    private Hashtable images;
    private PFrame NULL_FRAME;
    private Network network;
    private ClientGetFrameAnswer answer;
    private MicroByteBuffer buffer;

    public RemoteFrameLoader() {
        images = new Hashtable();
        NULL_FRAME = new PFrame(0, 0, 0, 0);
        network = ClientConfigurationFactory.getConfiguration().getNetwork();
        buffer = ClientConfigurationFactory.getConfiguration().getAnswersBuffer();
        answer = new ClientGetFrameAnswer();
    }

    public PFrame getFrame(int frameId) {
        PFrame frame = (PFrame) images.get(frameId);
        if (frame != null) {
            if (frame == NULL_FRAME) {
                DataMessage message = null;
                PFrame found = null;
                while ((message = network.getAsynchronousMessageByType(Packets.FRAME_MESSAGE)) != null) {
                    buffer.initiate(message.getData());
                    answer.setBuffer(buffer);
                    answer.deSerialize();
                    frame = answer.getFrame();
                    images.put(frame.getBid(), frame);
                    if (frame.getBid() == frameId) {
                        found = frame;
                    }
                }
                if (found != null) {
                    return found;
                }
            }
            return frame;
        } else {
            images.put(frameId, NULL_FRAME);
            ClientFrameRequest message = new ClientFrameRequest(ClientConfigurationFactory.getConfiguration());
            network.sendMessage(message);
            return NULL_FRAME;
        }
    }

    public void putFrameImage(byte[] data, int frameId) {
        try {
            images.put(frameId, Image.createImage(data, 0, data.length));
        } catch (IOException e) {
        }
    }
}
