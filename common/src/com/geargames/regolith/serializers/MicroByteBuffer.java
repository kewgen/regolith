package com.geargames.regolith.serializers;

/**
 * Класс наподобие nio.ByteBuffer, чтоб работать с массивами байтов разной длинны из одного массива.
 * User: mkutuzov
 * Date: 27.03.12
 */
public class MicroByteBuffer {
    private int position;
    private int limit;
    private int mark;

    private byte[] buffer;

    public MicroByteBuffer(){
    }

    public MicroByteBuffer initiate(byte[] buffer, int length) {
        this.buffer = buffer;
        limit(length - 1);
        position(0);
        return this;
    }

    public MicroByteBuffer initiate(byte[] buffer) {
        this.buffer = buffer;
        limit(buffer.length - 1);
        position(0);
        return this;
    }

    public MicroByteBuffer(byte[] buffer) {
        this.buffer = buffer;
        limit(buffer.length - 1);
        position(0);
    }

    public void clear(){
        position = 0;
        limit = size();
    }

    public void flip(){
        limit = position;
        position = 0;
    }

    private void check(int position) {
        if (position < 0 || position > limit) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Считать байт из массива по индексу position.
     * Если position > limit будет вызвано исключение.
     * @param position
     * @return
     */
    public byte get(int position) {
        check(position);
        return buffer[position];
    }

    /**
     * Считать текущий байт из массива. Нарастить счётчик массива.
     * Если этот счётчик > limit, то будет вызвано исключение.
     * @return
     */
    public byte get() {
        check(position);
        return buffer[position++];
    }

    public MicroByteBuffer put(byte value, int position) {
        check(position);
        buffer[position] = value;
        return this;
    }

    public MicroByteBuffer put(byte value) {
        check(position);
        buffer[position] = value;
        position++;
        return this;
    }

    public MicroByteBuffer mark() {
        mark = position;
        return this;
    }

    public MicroByteBuffer reset() {
        position = mark;
        return this;
    }

    public int size() {
        return buffer.length;
    }

    public int position() {
        return position;
    }

    public MicroByteBuffer position(int position) {
        this.position = position;
        return this;
    }

    public MicroByteBuffer limit(int limit) {
        if (limit < 0 || limit >= size()) {
            throw new IndexOutOfBoundsException();
        }
        this.limit = limit;
        return this;
    }

    public int limit() {
        return limit;
    }

    public byte[] getBytes(){
        return buffer;
    }

}
