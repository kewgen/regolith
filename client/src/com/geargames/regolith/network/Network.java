package com.geargames.regolith.network;

import com.geargames.common.util.Lock;
import com.geargames.regolith.serializers.SerializedMessage;

import java.util.Vector;

/**
 * Класс запускает потоки приёма и передачи сообщений между клиентом и сервером.
 */
public abstract class Network {
    private Vector asynchronousMessages;

    protected Network() {
        asynchronousMessages = new Vector();
    }

    public abstract boolean connect(String address, int port);

    public abstract void disconnect();

    public abstract int getPort();

    public abstract String getAddress();

    protected abstract Sender getSender();

    protected abstract Receiver getReceiver();

    public abstract void setDownloading(boolean downloading);

    public abstract void setUploading(boolean uploading);

    protected abstract Lock getMessageLock();

    public void sendMessage(SerializedMessage message) {
        getSender().sendMessage(message);
    }

    public int getAsynchronousMessagesSize() {
        getMessageLock().lock();
        int result = asynchronousMessages.size();
        getMessageLock().release();
        return result;
    }

    public DataMessage getAsynchronousMessageByType(short type) {
        getMessageLock().lock();
        DataMessage dataMessage = null;
        for (int i = 0; i < asynchronousMessages.size(); i++) {
            dataMessage = (DataMessage) asynchronousMessages.get(i);
            if (dataMessage.getMessageType() == type) {
                asynchronousMessages.removeElementAt(i);
                break;
            }
            dataMessage = null;
        }
        getMessageLock().release();
        return dataMessage;
    }

    public void addAsynchronousMessage(DataMessage dataMessage) {
        getMessageLock().lock();
        for (int i = 0; i < asynchronousMessages.size(); i++) {
            DataMessage message = (DataMessage) asynchronousMessages.get(i);
            if (message.getMessageType() == dataMessage.getMessageType()) {
                asynchronousMessages.setElementAt(dataMessage, i);
                getMessageLock().release();
                return;
            }
        }
        asynchronousMessages.addElement(dataMessage);
        getMessageLock().release();
    }

    public boolean isReceiving() {
        return getReceiver() != null && getReceiver().isRunning();
    }


}
