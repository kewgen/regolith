package com.geargames.regolith.network;

import com.geargames.common.logging.Debug;
import com.geargames.common.util.Lock;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.application.Manager;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import java.io.BufferedInputStream;
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
    public boolean downloading, uploading;
    private ClientConfiguration configuration;

    public MeNetwork(ClientConfiguration configuration) {
        this.configuration = configuration;
        this.connected = false;
    }

    protected Lock getAsynchronousLock() {
        return null;
    }

    private int port;
    private String address;

    public synchronized boolean connect(String address, int port) {
        try {
            Debug.info(com.geargames.common.String.valueOfC("Network.connect: address:").concatC(address).concatC(" port:").concatI(port));

            socket = (SocketConnection) Connector.open("socket://" + address + ":" + port);

            socket.setSocketOption(SocketConnection.KEEPALIVE, 1);

            dis = new DataInputStream(new BufferedInputStream(socket.openInputStream()));
            dos = socket.openDataOutputStream();

            receiver = new Receiver(this, configuration);
            receiver.startReceive(dis);
            sender = new Sender(this, configuration);
            sender.setOutput(dos);
            sender.startSending();

            this.port = port;
            this.address = address;
            connected = true;
            Debug.info(com.geargames.common.String.valueOfC("Network.connect: TCP/IP Session established"));
            return true;
        } catch (Exception e) {
            Debug.error(com.geargames.common.String.valueOfC("Network.connect() exception: "), e);
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

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public void setUploading(boolean uploading) {
        this.uploading = uploading;
    }
}
