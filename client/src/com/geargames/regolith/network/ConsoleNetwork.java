package com.geargames.regolith.network;

import com.geargames.common.logging.Debug;
import com.geargames.common.util.Lock;
import com.geargames.platform.util.JavaLock;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.application.Manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;

/**
 * User: mkutuzov
 * Date: 14.06.12
 */
public class ConsoleNetwork extends Network {
    private boolean connected;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Receiver receiver;
    private Sender sender;
    private Lock asynchronousLock;
    private MessageLock synchronousMessageLock;

    private ClientConfiguration configuration;

    @Override
    public MessageLock getMessageLock() {
        return synchronousMessageLock;
    }

    @Override
    protected Lock getAsynchronousLock() {
        return asynchronousLock;
    }

    public ConsoleNetwork(ClientConfiguration configuration) {
        this.configuration = configuration;
        this.connected = false;
        asynchronousLock = new Lock() {
			@Override
			public void lock() {
			}

			@Override
			public void release() {
			}
		};
        synchronousMessageLock = new MessageLock();
        synchronousMessageLock.setLock(new JavaLock());
        synchronousMessageLock.setMessageType(Packets.MESSAGE_TYPE_NULL);
    }

    private int port;
    private String address;

    public synchronized boolean connect(String address, int port) {
        try {
            Debug.info(com.geargames.common.String.valueOfC("Network.connect(): address: ").concatC(address).concatC(" port: ").concatI(port));
            SocketAddress socketAddress = new InetSocketAddress(address, port);
            socket = new Socket();
            socket.connect(socketAddress);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            receiver = new Receiver(this, configuration);
            receiver.startReceive(dis);
            sender = new Sender(this, configuration);
            sender.setOutput(dos);
            sender.startSending();

            this.port = port;
            this.address = address;
            connected = true;
            Debug.info(com.geargames.common.String.valueOfC("Network.connect(): TCP/IP Session established"));
            return true;
        } catch (Exception e) {
            Debug.error(com.geargames.common.String.valueOfC("Network.connect(): exception."), e);
        }

        return false;
    }

    public synchronized void disconnect() {
        if (!connected) {
            return;
        }
        try {
            connected = false;

            sender.stopSending();
            receiver.stopReceiving();
            Manager.pause(100);//даём остановиться сокету

            try {
                if (dos != null) dos.close();
            } catch (Throwable t) {
            }
            try {
                if (dis != null) dis.close();
            } catch (Throwable t) {
            }

            try {
                if (socket != null) socket.close();
                Debug.info(com.geargames.common.String.valueOfC("Socket closed"));
            } catch (Throwable t) {
                Debug.error(com.geargames.common.String.valueOfC("Network.disconnect: socket.close: "));
            }

            Debug.info(com.geargames.common.String.valueOfC("Disconnected"));

        } catch (Exception e) {
            Debug.error(com.geargames.common.String.valueOfC("Network.disconnect exception "), e);
        }
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }


    protected Receiver getReceiver() {
        return receiver;
    }

    protected Sender getSender() {
        return sender;
    }

}
