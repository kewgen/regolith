package com.geargames.regolith.serializers;

/**
 * User: mkutuzov
 * Date: 04.04.12
 */
public class SimpleDeserializer {
    public static char deserializeChar(MicroByteBuffer buffer) {
        int big = buffer.get() & SimpleSerializer.BYTE_MASK;
        int small = buffer.get() & SimpleSerializer.BYTE_MASK;
        char result = (char) ((big << 8) | small);
        return result;
    }

    public static short deserializeShort(MicroByteBuffer buffer) {
        int big = buffer.get() & SimpleSerializer.BYTE_MASK;
        int small = buffer.get() & SimpleSerializer.BYTE_MASK;
        short result = (short) (((big << 8)) | small);
        return result;
    }

    public static int deserializeInt(MicroByteBuffer buffer) {
        int big = deserializeShort(buffer) & SimpleSerializer.CHAR_MASK;
        int small = deserializeShort(buffer) & SimpleSerializer.CHAR_MASK;
        return big << 16 | small;
    }

    public static long deserializeLong(MicroByteBuffer buffer) {
        long big = deserializeInt(buffer) & SimpleSerializer.INT_MASK;
        long small = deserializeInt(buffer) & SimpleSerializer.INT_MASK;
        return big << 32 | small;
    }

    public static double deserializeDouble(MicroByteBuffer buffer) {
        return deserializeLong(buffer) + ((double) deserializeLong(buffer)) / SimpleSerializer.DECIMAL_SCALE;
    }

    public static float deserializeFloat(MicroByteBuffer buffer) {
        int big = deserializeInt(buffer);
        int small = deserializeInt(buffer);
        return big + ((float) small) / SimpleSerializer.DECIMAL_SCALE;
    }

    public static boolean deserializeBoolean(MicroByteBuffer buffer) {
        return buffer.get() == 1;
    }

    public static byte[] deserializeBytes(MicroByteBuffer buffer) {
        short length = deserializeShort(buffer);
        byte[] values = new byte[length];
        for (int i = 0; i < length; i++) {
            values[i] = buffer.get();
        }
        return values;
    }

    public static char[] deserializeChars(MicroByteBuffer buffer) {
        short length = deserializeShort(buffer);
        char[] values = new char[length];
        for (int i = 0; i < length; i++) {
            values[i] = deserializeChar(buffer);
        }
        return values;
    }

    public static short[] deserializeShorts(MicroByteBuffer buffer) {
        short length = deserializeShort(buffer);
        short[] values = new short[length];
        for (int i = 0; i < length; i++) {
            values[i] = deserializeShort(buffer);
        }
        return values;
    }

    public static int[] deserializeInts(MicroByteBuffer buffer) {
        short length = deserializeShort(buffer);
        int[] values = new int[length];
        for (int i = 0; i < length; i++) {
            values[i] = deserializeInt(buffer);
        }
        return values;
    }

    public static String deserializeString(MicroByteBuffer buffer) {
        return new String(deserializeChars(buffer));
    }

}
