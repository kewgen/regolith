package com.geargames.regolith.network;

import com.geargames.common.util.Lock;
import com.geargames.regolith.managers.ClientDeferredAnswer;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;

import java.util.Vector;

/**
 * Класс запускает потоки приёма и передачи сообщений между клиентом и сервером.
 */
public abstract class Network {
    private Vector asynchronousMessages;
    private MicroByteBuffer buffer;

    protected Network() {
        asynchronousMessages = new Vector();
        buffer = new MicroByteBuffer();
    }

    public abstract boolean connect(String address, int port);

    public abstract void disconnect();

    public abstract int getPort();

    public abstract String getAddress();

    protected abstract Sender getSender();

    protected abstract Receiver getReceiver();

    /**
     * Вернём симафор для доступа очереди асинхронных ответов.
     * @return
     */
    protected abstract Lock getAsynchronousLock();

    /**
     * Вернём блокировку синхронных сообщений.
     * @return
     */
    public abstract MessageLock getMessageLock();

    public void sendMessage(SerializedMessage message) {
        getSender().sendMessage(message);
    }

    public int getAsynchronousMessagesSize() {
        getAsynchronousLock().lock();
        int result = asynchronousMessages.size();
        getAsynchronousLock().release();
        return result;
    }

    public DataMessage getAsynchronousMessageByType(short type) {
        getAsynchronousLock().lock();
        DataMessage dataMessage = null;
        for (int i = 0; i < asynchronousMessages.size(); i++) {
            dataMessage = (DataMessage) asynchronousMessages.get(i);
            if (dataMessage.getMessageType() == type) {
                asynchronousMessages.removeElementAt(i);
                break;
            }
            dataMessage = null;
        }
        getAsynchronousLock().release();
        return dataMessage;
    }

    public synchronized boolean getAsynchronousAnswer(ClientDeSerializedMessage answer, short messageType) {
        DataMessage message = getAsynchronousMessageByType(messageType);
        if (message != null) {
            //todo: использовать ClientConfigurationFactory.getConfiguration().getAnswersBuffer() вместо buffer?
            buffer.initiate(message.getData());
            answer.setBuffer(buffer);
            try {
                answer.deSerialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
            answer.setBuffer(null);
            return true;
        } else {
            return false;
        }
    }

    public void addAsynchronousMessage(DataMessage dataMessage) {
        getAsynchronousLock().lock();
        for (int i = 0; i < asynchronousMessages.size(); i++) {
            DataMessage message = (DataMessage) asynchronousMessages.get(i);
            if (message.getMessageType() == dataMessage.getMessageType()) {
                asynchronousMessages.setElementAt(dataMessage, i);
                getAsynchronousLock().release();
                return;
            }
        }
        asynchronousMessages.addElement(dataMessage);
        getAsynchronousLock().release();
    }

    public ClientDeferredAnswer sendSynchronousMessage(SerializedMessage request, ClientDeSerializedMessage answer){
        MessageLock lock = getMessageLock();
        lock.setMessageType(request.getType());
        lock.setMessage(answer);
        getSender().sendMessage(request);

        return new ClientDeferredAnswer(answer);
    }

    public boolean isReceiving() {
        return getReceiver() != null && getReceiver().isRunning();
    }

}
