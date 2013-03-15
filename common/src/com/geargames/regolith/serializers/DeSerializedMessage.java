package com.geargames.regolith.serializers;

import com.geargames.common.String;
import com.geargames.common.logging.Debug;

/**
 * User: mkutuzov, abarakov
 * Date: 04.07.12
 */
public abstract class DeSerializedMessage {

    protected abstract void deSerialize(MicroByteBuffer buffer);

    public abstract MicroByteBuffer getBuffer();

    public void deSerialize() {
        MicroByteBuffer buffer = getBuffer();
        deSerialize(buffer);
        // Проверка того, что сообщение было считано полностью и не считано лишних данных
        if (buffer.position() - 1 != buffer.limit()) {
            Debug.error(String.valueOfC("DeSerializedMessage.deSerialize(): The expected position does not coincide with the actual position (").
                            concat("expected=").concatI(buffer.limit()).
                            concatC("; actual=").concatI(buffer.position() - 1).
//                          concatC("; msg type=").concatI(messageType).
        concatC(")"));
        }
    }

}
