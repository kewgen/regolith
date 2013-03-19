package com.geargames.regolith;

import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PUnresolvedFrameManager;
import com.geargames.platform.packer.Image;
import com.geargames.regolith.network.Network;
import com.geargames.regolith.serializers.answers.ClientGetFrameAnswer;
import com.geargames.regolith.serializers.requests.ClientFrameRequest;

import java.io.IOException;
import java.util.Hashtable;

/**
 * User: mikhail v. kutuzov
 * Класс управляет фреймами которые подгружены с сервера по запросу.
 * В случае отсутствия фрейма (не подгружен) возвращается пустой фрейм NULL_FRAME.
 */
public class RemoteFrameLoader extends PUnresolvedFrameManager {
    private Hashtable images;
    private PFrame NULL_FRAME;
    private Network network;
    private ClientGetFrameAnswer answer;

    public RemoteFrameLoader() {
        images = new Hashtable();
        NULL_FRAME = new PFrame(0, 0, 0, 0);
        network = ClientConfigurationFactory.getConfiguration().getNetwork();
        answer = new ClientGetFrameAnswer();
    }

    public PFrame getFrame(int frameId) {
        PFrame frame = (PFrame) images.get(frameId);
        if (frame != null) {
            if (frame == NULL_FRAME) {
                PFrame found = null;
                while (network.getAsynchronousAnswer(answer, Packets.FRAME_MESSAGE)) {
                    frame = answer.getFrame();
                    images.put(frame.getPID(), frame);
                    if (frame.getPID() == frameId) {
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
