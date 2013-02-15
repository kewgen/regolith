package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.AbstractTackle;

/**
 * User: mikhail v. kutuzov
 * Запрос на перемещение вещи либо из сумки бойца(карман n) на склад или на бойца либо со склада пользователя(ячейка n)
 * на бойца или в его сумку.
 */
public class ClientMoveTackleByNumber extends ClientSerializedMessage {
    private Warrior warrior;
    private short number;
    private short amount;
    private short realAmount;
    private short type;
    private AbstractTackle tackle;

    public ClientMoveTackleByNumber(ClientConfiguration configuration) {
        super(configuration);
    }

    /**
     * Боец вещь которого перемещается.
     * @return
     */
    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    /**
     * Номер ячейки склада или кармана сумки где лежит вещь.
     * @return
     */
    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    /**
     * Количество фактически перемещённых вещей.
     * @return
     */
    public short getAmount() {
        return amount;
    }

    public void setAmount(short amount) {
        this.amount = amount;
    }

    public short getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(short realAmount) {
        this.realAmount = realAmount;
    }

    public AbstractTackle getTackle() {
        return tackle;
    }

    public void setTackle(AbstractTackle tackle) {
        this.tackle = tackle;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(warrior, buffer);
        SimpleSerializer.serialize(number, buffer);
        SimpleSerializer.serialize(amount, buffer);
        SimpleSerializer.serialize(realAmount, buffer);
        SimpleSerializer.serializeEntityReference(tackle, buffer);
    }
}
