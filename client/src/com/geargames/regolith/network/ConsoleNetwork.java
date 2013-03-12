package com.geargames.regolith.network;

import com.geargames.ConsoleDebug;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.util.Lock;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.application.Application;
import com.geargames.regolith.application.Manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private Application application;
    public boolean downloading, uploading;
    private Lock fakeLock;
    private ClientConfiguration configuration;

    @Override
    protected Lock getMessageLock() {
        return fakeLock;
    }

    public ConsoleNetwork(ClientConfiguration configuration) {
        this.configuration = configuration;
        this.connected = false;
        fakeLock = new Lock(){
                @Override
                public void lock() {
                }

                @Override
                public void release() {
                }
            };
    }

    private int port;
    private String address;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public synchronized boolean connect(String address, int port) {
        try {
            SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Network.connect: address:").concatC(address).concatC(" port:").concatI(port));
            SocketAddress socketAddress = new InetSocketAddress(address, port);
            socket = new Socket();
            socket.connect(socketAddress);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            receiver = new Receiver(this, configuration);
            receiver.startReceive(dis);
            sender = new Sender(this, configuration);
            sender.setOutput(dos);
            sender.startSending();

            this.port = port;
            this.address = address;
            connected = true;
            SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Network.connect: TCP/IP Session established"));
            return true;
        } catch (UnknownHostException e) {
            ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).trace(com.geargames.common.String.valueOfC("Connector does not respond! exception: "), e);
        } catch (IOException e) {
            ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).trace(com.geargames.common.String.valueOfC("Network.connect() exception: "), e);
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
            Manager.paused(100);//даём остановиться сокету

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
                SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Socket closed"));
            } catch (Throwable t) {
                SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Network.disconnect: socket.close: "));
            }

            SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Disconnected"));

        } catch (Exception e) {
            ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).trace(com.geargames.common.String.valueOfC("Network.disconnect exception "), e);
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

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public void setUploading(boolean uploading) {
        this.uploading = uploading;
    }
}
