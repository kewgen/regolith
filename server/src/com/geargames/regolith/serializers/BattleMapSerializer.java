package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.*;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mkutuzov
 * Date: 26.03.12
 */
public class BattleMapSerializer {

    private static void serializeWarrior(Warrior warrior, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(warrior, buffer);
        SimpleSerializer.serialize(warrior.getDirection().getNumber(), buffer);
    }

    private static void serialize(Barrier barrier, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(barrier, buffer);
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
            if (tackle.getElementType() == CellElementTypes.ARMOR) {
                SimpleSerializer.serialize(SerializeHelper.ARMOR, buffer);
                TackleSerializer.serializeArmor((Armor) tackle, buffer);
            } else if (tackle.getElementType() == CellElementTypes.WEAPON) {
                SimpleSerializer.serialize(SerializeHelper.WEAPON, buffer);
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
    private static void serialize(BattleCell[][] cells, short x, short y, Account account, MicroByteBuffer buffer) {
        CellElement element = cells[x][y].getElement();
        if (element != null) {
            switch (element.getElementType()) {
                case CellElementTypes.HUMAN:
                    Warrior warrior = (Warrior) element;
                    if (WarriorHelper.isMine(warrior.getBattleGroup(), account)) {
                        SimpleSerializer.serialize(x, buffer);
                        SimpleSerializer.serialize(y, buffer);
                        SimpleSerializer.serialize(SerializeHelper.WARRIOR, buffer);
                        serializeWarrior(warrior, buffer);
                    } else if (WarriorHelper.isAlly(warrior, account)) {
                        SimpleSerializer.serialize(x, buffer);
                        SimpleSerializer.serialize(y, buffer);
                        SimpleSerializer.serialize(SerializeHelper.ALLY, buffer);
                        serializeWarrior(warrior, buffer);
                    }
                    break;
                case CellElementTypes.BARRIER:
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SerializeHelper.BARRIER, buffer);
                    serialize((Barrier) element, buffer);
                    break;
                case CellElementTypes.ARMOR:
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SerializeHelper.ARMOR, buffer);
                    TackleSerializer.serializeArmor((Armor) element, buffer);
                    break;
                case CellElementTypes.WEAPON:
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SerializeHelper.WEAPON, buffer);
                    TackleSerializer.serializeWeapon((Weapon) element, buffer);
                    break;
                case CellElementTypes.MEDIKIT:
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SerializeHelper.MEDIKIT, buffer);
                    TackleSerializer.serializeMedikit((Medikit) element, buffer);
                    break;
                case CellElementTypes.MAGAZINE:
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SerializeHelper.MAGAZINE, buffer);
                    serialize((Magazine) element, buffer);
                    break;
                case CellElementTypes.BOX:
                    SimpleSerializer.serialize(x, buffer);
                    SimpleSerializer.serialize(y, buffer);
                    SimpleSerializer.serialize(SerializeHelper.BOX, buffer);
                    serialize((Box) element, buffer);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    public static void serializeBattleMap(BattleMap battleMap, Account account, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battleMap, buffer);
        if (battleMap == null) {
            //todo: Нельзя просто так завершать метод
            return;
        }
        BattleCell[][] cells = battleMap.getCells();
        short size = (short) cells.length;
        MicroByteBuffer byteBuffer = new MicroByteBuffer(new byte[buffer.size()]);
        BattleType[] possibilities = battleMap.getPossibleBattleTypes();
        SimpleSerializer.serialize((byte) possibilities.length, buffer);
        for (int i = 0; i < possibilities.length; i++) {
            SerializeHelper.serializeEntityReference(possibilities[i], buffer);
        }
        SimpleSerializer.serialize(battleMap.getName(), buffer);
        SimpleSerializer.serialize(size, buffer);
        byteBuffer.setPosition(0);
        byteBuffer.setLimit(byteBuffer.size());
        for (short x = 0; x < size; x++) {
            for (short y = 0; y < size; y++) {
                serialize(cells, x, y, account, byteBuffer);
            }
        }
        SimpleSerializer.serialize(byteBuffer.getPosition(), buffer);
        byteBuffer.flip();
        buffer.put(byteBuffer);
    }
}
