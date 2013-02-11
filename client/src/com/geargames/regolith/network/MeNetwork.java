package com.geargames.regolith.network;

import com.geargames.Debug;
import com.geargames.common.util.Lock;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.app.Application;
import com.geargames.regolith.app.Manager;

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
            Debug.trace("Network.connect: address:" + address + " port:" + port);

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
            Debug.trace("Network.connect: TCP/IP Session established");
            return true;
        } catch (ConnectionNotFoundException e) {
            Debug.trace("Connector not respond! exception: " + e);
        } catch (Exception e) {
            Debug.trace("Network.connect() exception: " + e);
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
                Debug.trace("Socket closed");
            } catch (Throwable t) {
                Debug.trace("Network.disconnect: socket.close: ");
            }

            Debug.trace("Disconnected");

        } catch (Exception e) {
            Debug.trace("Network.disconnect exception " + e);
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
