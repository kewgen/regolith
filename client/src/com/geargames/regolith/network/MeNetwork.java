package com.geargames.regolith.network;

import com.geargames.ConsoleDebug;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.util.Lock;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.application.Application;
import com.geargames.regolith.application.Manager;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * User: mkutuzov
 * Date: 14.06.12
 */
public class MeNetwork extends Network {
    private boolean connected;
    private SocketConnection socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Receiver receiver;
    private Sender sender;
    private Application application;
    public boolean downloading, uploading;
    private ClientConfiguration configuration;

    public MeNetwork(ClientConfiguration configuration) {
        this.configuration = configuration;
        this.connected = false;
    }

    protected Lock getMessageLock() {
        return null;
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

            socket = (SocketConnection) Connector.open("socket://" + address + ":" + port);

            socket.setSocketOption(SocketConnection.KEEPALIVE, 1);

            dis = socket.openDataInputStream();
            dos = socket.openDataOutputStream();

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
        } catch (ConnectionNotFoundException e) {
            ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).trace(com.geargames.common.String.valueOfC("Connector not respond! exception: "), e);
        } catch (Exception e) {
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
