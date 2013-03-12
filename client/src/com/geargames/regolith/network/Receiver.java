package com.geargames.regolith.network;

import com.geargames.ConsoleDebug;
import com.geargames.common.*;
import com.geargames.common.String;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.serializers.MicroByteBuffer;


import java.io.DataInputStream;
import java.io.IOException;

/**
 * Функциональность Daemon-потока, читающего Socket
 * и выделяющего сообщения.
 * Сообщения записываются в порядке поступления в очередь сообщений
 * Очередь сообщений доступна - см. getMsgQueue()
 */
public final class Receiver extends Thread {
    private ClientConfiguration configuration;
    private Network network;
    private DataInputStream dis;
    private volatile boolean running;
    private int errors;
    private static final int NETWORK_ERRORS_THRESHOLD = 3;
    // -------------------------------------------

    public Receiver(Network network, ClientConfiguration configuration) {
        this.network = network;
        this.configuration = configuration;
    }

    public synchronized void startReceive(DataInputStream dis) {
        this.dis = dis;
        start();
    }

    /**
     * Stop receiving bytes
     * <p/>
     * Note: the method dis NOT synchronized, otherwice it blocks
     * due to reading from Socket.InputStream.
     * any read() on Socket.InputStream blocks
     */
    public void stopReceiving() {
        running = false;
        Thread.yield();
    }

    private void init() {
        running = true;
    }

    public void run() {
        init();

        short type = 0;  // ID сообщения
        int length = 0;  // Длина данных сообщения

        try {
            while (running) {
                network.setDownloading(false);
                while (dis != null && dis.available() == 0) {
                    if (!running) {
                        break;
                    }
                    Manager.paused(50);
                }
                if (!running || dis == null) {
                    break;
                }
                network.setDownloading(true);

                length = dis.readShort() & 0xffff;
                type = dis.readShort();
                int res = 0;

                MessageLock messageLock = getMessageLockIfItExists(type);
                if (messageLock != null) {
                    res = read(dis, configuration.getIncomingMessage(), 0, length);
                    if (res != length) {
                        throw new RegolithException();
                    }
                    MicroByteBuffer buffer = configuration.getAnswersBuffer();
                    buffer.initiate(configuration.getIncomingMessage(), length);
                    messageLock.getMessage().setBuffer(buffer);
                    messageLock.setMessageType(Packets.MESSAGE_TYPE_NULL);
                    messageLock.getLock().release();
                } else {
                    byte[] data = new byte[length];
                    res = read(dis, data, 0, length);
                    if (res != length) {
                        throw new RegolithException();
                    }
                    DataMessage dataMessage = new DataMessage();
                    dataMessage.setData(data);
                    dataMessage.setLength(length);
                    dataMessage.setMessageType(type);
                    network.addAsynchronousMessage(dataMessage);
                }
                SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Receiver: received message, type:").concatI(type).concatC("(").concatI((type & 0xff)).concatC("), len:").concatI(length).concatC(", res:").concatI(res));
                if (length != res) {//считанное колво байт не равно заявленной длине!
                    SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Error received len, type:").concatI(type).concatC("(").concatI((type & 0xff)).concatC("), len:").concatI(length).concatC(" != res:").concatI(res));
                    continue;
                } else if (res == -1) {//Receiver: received message, type:-19(237), len:42309
                    SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Error received, type:").concatI(type).concatC("(").concatI((type & 0xff)).concatC("), len:").concatI(length));
                    continue;
                }

                Manager.paused(10);
            }
        } catch (Exception e) {
            errors++;
            if (running) {
                Manager.paused(2000);
            }
            e.printStackTrace();
            ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).trace(String.valueOfC("Receiver Exception: "), e);
            if (checkErrors()){
                return;
            }
        }
    }

    private MessageLock getMessageLockIfItExists(short type) {
        MessageLock messageLock = configuration.getMessageLock();
        return messageLock.getMessageType() == type ? messageLock : null;
    }

    private boolean checkErrors() {
        if (errors > NETWORK_ERRORS_THRESHOLD) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("Receiver: too many errors, disconnecting"));
            network.disconnect();
            return true;
        }
        return false;
    }

    private int read(DataInputStream dis, byte bytes[], int off, int len) throws IOException {
        if (bytes == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > bytes.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = dis.read();
        if (c == -1) {
            return -1;
        }
        bytes[off] = (byte) c;

        int i = 1;
        try {
            for (; i < len; i++) {
                c = dis.read();
                if (c == -1) {
                    SystemEnvironment.getInstance().getDebug().trace(String.valueOfC(" read, c:").concatI(c).concatC("(").concatI(i).concatC(")"));
                }
                bytes[off + i] = (byte) c;
            }
        } catch (IOException ee) {
        }
        return i;
    }

    public boolean isRunning() {
        return running;
    }
}
