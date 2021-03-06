package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Human;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.map.ExitZone;

/**
 * @author Mikhail_Kutuzov
 *         created: 31.03.12  16:27
 */
public class BattleSerializer {

    public static void serializeHuman(Human human, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(human, buffer);
        SimpleSerializer.serialize(human.getName(), buffer);
        SimpleSerializer.serialize(human.getNumber(), buffer);
        SerializeHelper.serializeEntityReference(human.getRank(), buffer);
        SimpleSerializer.serialize(human.getFrameId(), buffer);
        SimpleSerializer.serialize(human.getHealth(), buffer);
        if (human.getHeadArmor() != null) {
            SimpleSerializer.serialize(SimpleSerializer.YES, buffer);
            TackleSerializer.serializeArmor(human.getHeadArmor(), buffer);
        } else {
            SimpleSerializer.serialize(SimpleSerializer.NO, buffer);
        }
        if (human.getTorsoArmor() != null) {
            SimpleSerializer.serialize(SimpleSerializer.YES, buffer);
            TackleSerializer.serializeArmor(human.getTorsoArmor(), buffer);
        } else {
            SimpleSerializer.serialize(SimpleSerializer.NO, buffer);
        }
        if (human.getLegsArmor() != null) {
            SimpleSerializer.serialize(SimpleSerializer.YES, buffer);
            TackleSerializer.serializeArmor(human.getLegsArmor(), buffer);
        } else {
            SimpleSerializer.serialize(SimpleSerializer.NO, buffer);
        }
        TackleSerializer.serializeWeapon(human.getWeapon(), buffer);
    }

    public static void serializeEnemies(BattleAlliance battleAlliance, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battleAlliance, buffer);
        SimpleSerializer.serialize(battleAlliance.getNumber(), buffer);
        serialize(battleAlliance.getExit(), buffer);
        for (BattleGroup battleGroup : ((ServerBattleGroupCollection) battleAlliance.getAllies()).getBattleGroups()) {
            SerializeHelper.serializeEntityReference(battleGroup, buffer);
            for (Human human : ((ServerWarriorCollection) battleGroup.getWarriors()).getWarriors()) {
                serializeHuman(human, buffer);
            }
        }
    }

    public static void serializeWarrior(Warrior warrior, MicroByteBuffer buffer) {
        serializeHuman(warrior, buffer);
//        SimpleSerializer.serialize(warrior.getCellX(), buffer);
//        SimpleSerializer.serialize(warrior.getCellY(), buffer);
    }

    private static void serialize(ExitZone exitZone, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(exitZone, buffer);
        SimpleSerializer.serialize(exitZone.getX(), buffer);
        SimpleSerializer.serialize(exitZone.getY(), buffer);
        SimpleSerializer.serialize(exitZone.getxRadius(), buffer);
        SimpleSerializer.serialize(exitZone.getyRadius(), buffer);
    }

    public static void serializeAllies(BattleAlliance battleAlliance, Account account, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battleAlliance, buffer);
        SimpleSerializer.serialize(battleAlliance.getNumber(), buffer);
        serialize(battleAlliance.getExit(), buffer);
        for (BattleGroup battleGroup : ((ServerBattleGroupCollection) battleAlliance.getAllies()).getBattleGroups()) {
            SerializeHelper.serializeEntityReference(battleGroup, buffer);
            if (!WarriorHelper.isMine(battleGroup, account)) {
                // Сериализация союзных бойцов
                SimpleSerializer.serialize(SimpleSerializer.NO, buffer);
                for (Warrior ally : ((ServerWarriorCollection) battleGroup.getWarriors()).getWarriors()) {
                    serializeWarrior(ally, buffer);
                }
            } else {
                // Сериализация своих бойцов
                SimpleSerializer.serialize(SimpleSerializer.YES, buffer);
                for (Warrior mine : ((ServerWarriorCollection) battleGroup.getWarriors()).getWarriors()) {
                    SerializeHelper.serializeEntityReference(mine, buffer);
                    SimpleSerializer.serialize(mine.getNumber(), buffer);
//                    SimpleSerializer.serialize(mine.getDirection().getNumber(), buffer);
                }
            }
        }
    }

    public static void serializeBattle(Battle battle, Account account, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battle, buffer);
        SimpleSerializer.serialize(battle.getName(), buffer);
        SerializeHelper.serializeEntityReference(battle.getBattleType(), buffer);
        if (battle.getAlliances() != null) {
            SimpleSerializer.serialize((byte) battle.getAlliances().length, buffer);
            for (BattleAlliance alliance : battle.getAlliances()) {
                if (WarriorHelper.isAlly(alliance, account)) {
                    SimpleSerializer.serialize(SerializeHelper.ALLY, buffer);
                    serializeAllies(alliance, account, buffer);
                } else {
                    SimpleSerializer.serialize(SerializeHelper.ENEMY, buffer);
                    serializeEnemies(alliance, buffer);
                }
            }
        } else {
            SimpleSerializer.serialize((byte) 0, buffer);
        }
        BattleMapSerializer.serializeBattleMap(battle.getMap(), account, buffer);

        SerializeHelper.serializeEntityReference(battle.getAuthor(), buffer);
        SimpleSerializer.serialize(battle.getAuthor().getName(), buffer);
        SimpleSerializer.serialize(battle.getAuthor().getFrameId(), buffer);
    }

}
