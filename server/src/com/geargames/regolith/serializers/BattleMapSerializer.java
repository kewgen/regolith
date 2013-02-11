package com.geargames.regolith.serializers;

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
        SimpleSerializer.serializeEntityReference(warrior, buffer);
    }

    private static void serialize(Border border, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(border, buffer);
    }

    public static void serialize(Magazine magazine, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(magazine, buffer);
        SimpleSerializer.serializeEntityReference(magazine.getProjectile(), buffer);
        SimpleSerializer.serialize(magazine.getCount(), buffer);
    }

    private static void serialize(Box box, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(box, buffer);
        SimpleSerializer.serialize(box.getFrameId(), buffer);
        SimpleSerializer.serialize((byte) box.getTackles().size(), buffer);
        StateTackleCollection states = box.getTackles();
        for (int i = 0; i < states.size(); i++) {
            StateTackle tackle = states.get(i);
            if (tackle instanceof Armor) {
                SimpleSerializer.serialize(SimpleSerializer.findTypeId("Armor"), buffer);
                TackleSerializer.serializeArmor((Armor) tackle, buffer);
            } else if (tackle instanceof Weapon) {
                SimpleSerializer.serialize(SimpleSerializer.findTypeId("Weapon"), buffer);
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
                    SimpleSerializer.serialize(SimpleSerializer.findTypeId("Warrior"), buffer);
                    serialize(warrior, buffer);
                } else if (WarriorHelper.isAlly(warrior, account)) {
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SimpleSerializer.findTypeId("Ally"), buffer);
                    serialize(warrior, buffer);
                }
            } else if (element instanceof Border) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SimpleSerializer.findTypeId("Border"), buffer);
                serialize((Border) element, buffer);
            } else if (element instanceof Armor) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SimpleSerializer.findTypeId("Armor"), buffer);
                TackleSerializer.serializeArmor((Armor) element, buffer);
            } else if (element instanceof Weapon) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SimpleSerializer.findTypeId("Weapon"), buffer);
                TackleSerializer.serializeWeapon((Weapon) element, buffer);
            } else if (element instanceof Medikit) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SimpleSerializer.findTypeId("Medikit"), buffer);
                TackleSerializer.serializeMedikit((Medikit) element, buffer);
            } else if (element instanceof Magazine) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SimpleSerializer.findTypeId("Magazine"), buffer);
                serialize((Magazine) element, buffer);
            } else if (element instanceof Box) {
                SimpleSerializer.serialize(x, buffer);
                SimpleSerializer.serialize(y, buffer);
                SimpleSerializer.serialize(SimpleSerializer.findTypeId("Box"), buffer);
                serialize((Box) element, buffer);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void serialize(BattleMap battleMap, Account account, MicroByteBuffer buffer) {
        BattleCell[][] cells = battleMap.getCells();
        MicroByteBuffer byteBuffer = new MicroByteBuffer(new byte[buffer.size()]);
        SimpleSerializer.serializeEntityReference(battleMap, buffer);
        BattleType[] possibilities = battleMap.getPossibleBattleTypes();
        SimpleSerializer.serialize((byte) possibilities.length, buffer);
        for (int i = 0; i < possibilities.length; i++) {
            SimpleSerializer.serializeEntityReference(possibilities[i], buffer);
        }
        SimpleSerializer.serialize(battleMap.getName(), buffer);
        SimpleSerializer.serialize((short) cells.length, buffer);
        byteBuffer.position(0);
        byteBuffer.limit(byteBuffer.size() - 1);
        byteBuffer.mark();
        for (short i = 0; i < cells.length; i++) {
            for (short j = 0; j < cells.length; j++) {
                serialize(cells, i, j, account, byteBuffer);
            }
        }
        SimpleSerializer.serialize(byteBuffer.position(), buffer);
        byteBuffer.limit(byteBuffer.position());
        byteBuffer.reset();
        for (int i = 0; i < byteBuffer.limit(); i++) {
            buffer.put(byteBuffer.get());
        }
    }
}
