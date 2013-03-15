package com.geargames.regolith.network;

import com.geargames.common.logging.Debug;
import com.geargames.common.String;
import com.geargames.common.util.Lock;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.application.MELock;
import com.geargames.regolith.serializers.SerializedMessage;

import java.io.DataOutputStream;
import java.util.Vector;

/**
 * Класс отвечает за пересылку сообщений на сервер.
 */
public final class Sender extends Thread {
    private Lock workLock;
    private Network network;
    private static Vector messageQueue;
    private DataOutputStream output;
    private volatile boolean running;
    private int errorCounter;
    private ClientConfiguration configuration;

    public Sender(Network network, ClientConfiguration clientConfiguration) {
        this.network = network;
        messageQueue = new Vector();
        errorCounter = 0;
        configuration = clientConfiguration;
        workLock = new MELock();
        workLock.lock();
    }

    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    /**
     * Запустить поток.
     */
    public void startSending() {
        running = true;
        start();
    }

    /**
     * Остановить поток-демон.
     */
    public void stopSending() {
        running = false;
        workLock.release();
        configuration.getMessageLock().getLock().release();
        Thread.yield();
    }

    public void run() {
        while (running) {
            network.setUploading(false);
            if (messageQueue.isEmpty()) {
                workLock.lock();
                if(messageQueue.isEmpty()) {
                    workLock.lock();
                }
                if (!running) {
                    break;
                }
            }
            network.setUploading(true);

            try {
                SerializedMessage message = (SerializedMessage) messageQueue.elementAt(0);
                output.write(message.serialize());
                output.flush();
                messageQueue.removeElementAt(0);
                MessageLock messageLock = configuration.getMessageLock();
                messageLock.setMessageType(message.getType());
                messageLock.getLock().lock();
            } catch (Exception e) {
                errorCounter++;
                if (errorCounter > configuration.getMaxErrorsAmount()) {
                    Debug.error(String.valueOfC("Sender: too many errors, disconnecting"), e);
                    network.disconnect();
                    return;
                }
                Debug.error(String.valueOfC("Sender exception"), e);
            }
        }
    }

    public void sendMessage(SerializedMessage message) {
        messageQueue.addElement(message);
        workLock.release();
    }

}
