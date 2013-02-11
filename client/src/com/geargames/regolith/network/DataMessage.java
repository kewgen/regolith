package com.geargames.regolith.network;

/**
 * User: mkutuzov
 * Date: 04.07.12
 */
public class DataMessage {
    private int length;
    private byte[] data;
    private short messageType;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public short getMessageType() {
        return messageType;
    }

    public void setMessageType(short messageType) {
        this.messageType = messageType;
    }
}
