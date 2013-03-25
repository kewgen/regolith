package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.*;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mkutuzov
 * Date: 26.03.12
 */
public class BattleMapSerializer {

    private static void serialize(Warrior warrior, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(warrior, buffer);
    }

    private static void serialize(Border border, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(border, buffer);
    }

    public static void serialize(Magazine magazine, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(magazine, buffer);
        SerializeHelper.serializeEntityReference(magazine.getProjectile(), buffer);
        SimpleSerializer.serialize(magazine.getCount(), buffer);
    }

    private static void serialize(Box box, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(box, buffer);
        SimpleSerializer.serialize(box.getFrameId(), buffer);
        SimpleSerializer.serialize((byte) box.getTackles().size(), buffer);
        StateTackleCollection states = box.getTackles();
        for (int i = 0; i < states.size(); i++) {
            StateTackle tackle = states.get(i);
            if (tackle instanceof Armor) {
                SimpleSerializer.serialize(SerializeHelper.findTypeId("Armor"), buffer);
                TackleSerializer.serializeArmor((Armor) tackle, buffer);
            } else if (tackle instanceof Weapon) {
                SimpleSerializer.serialize(SerializeHelper.findTypeId("Weapon"), buffer);
                TackleSerializer.serializeWeapon((Weapon) tackle, buffer);
            }
        }
        SimpleSerializer.serialize((byte) box.getMedikits().size(), buffer);
        MedikitCollection medikits = box.getMedikits();
        for (int i = 0; i < medikits.size(); i++) {
            Medikit medikit = medikits.get(i);
            TackleSerializer.serializeMedikit(medikit, buffer);
        }
        SimpleSerializer.serialize((byte) box.getMagazines().size(), buffer);
        MagazineCollection magazines = box.getMagazines();
        for (int i = 0; i < magazines.size(); i++) {
            Magazine magazine = magazines.get(i);
            serialize(magazine, buffer);
        }
    }

    /**
     * Записываем данные из ячейки cells[x][y] для пользователя account в байтовый массив buffer.
     *
     * @param cells
     * @param x
     * @param y
     * @param account
     * @param buffer
     */
    public static void serialize(BattleCell[][] cells, short x, short y, Account account, MicroByteBuffer buffer) {
        Element element = cells[x][y].getElement();
        if (element != null) {
            if (element instanceof Warrior) {
                Warrior warrior = (Warrior) element;
                if (warrior.getBattleGroup().getAccount().getId() == account.getId()) {
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SerializeHelper.findTypeId("Warrior"), buffer);
                    serialize(warrior, buffer);
                } else if (WarriorHelper.isAlly(warrior, account)) {
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SerializeHelper.findTypeId("Ally"), buffer);
                    serialize(warrior, buffer);
                }
            } else if (element instanceof Border) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SerializeHelper.findTypeId("Border"), buffer);
                serialize((Border) element, buffer);
            } else if (element instanceof Armor) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SerializeHelper.findTypeId("Armor"), buffer);
                TackleSerializer.serializeArmor((Armor) element, buffer);
            } else if (element instanceof Weapon) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SerializeHelper.findTypeId("Weapon"), buffer);
                TackleSerializer.serializeWeapon((Weapon) element, buffer);
            } else if (element instanceof Medikit) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SerializeHelper.findTypeId("Medikit"), buffer);
                TackleSerializer.serializeMedikit((Medikit) element, buffer);
            } else if (element instanceof Magazine) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SerializeHelper.findTypeId("Magazine"), buffer);
                serialize((Magazine) element, buffer);
            } else if (element instanceof Box) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SerializeHelper.findTypeId("Box"), buffer);
                serialize((Box) element, buffer);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void serialize(BattleMap battleMap, Account account, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battleMap, buffer);
        if (battleMap == null) {
            return;
        }
        BattleCell[][] cells = battleMap.getCells();
        MicroByteBuffer byteBuffer = new MicroByteBuffer(new byte[buffer.size()]);
        BattleType[] possibilities = battleMap.getPossibleBattleTypes();
        SimpleSerializer.serialize((byte) possibilities.length, buffer);
        for (int i = 0; i < possibilities.length; i++) {
            SerializeHelper.serializeEntityReference(possibilities[i], buffer);
        }
        SimpleSerializer.serialize(battleMap.getName(), buffer);
        SimpleSerializer.serialize((short) cells.length, buffer);
        byteBuffer.setPosition(0);
        byteBuffer.setLimit(byteBuffer.size());
        for (short i = 0; i < cells.length; i++) {
            for (short j = 0; j < cells.length; j++) {
                serialize(cells, i, j, account, byteBuffer);
            }
        }
        SimpleSerializer.serialize(byteBuffer.getPosition(), buffer);
        byteBuffer.flip();
        buffer.put(byteBuffer);
    }
}
