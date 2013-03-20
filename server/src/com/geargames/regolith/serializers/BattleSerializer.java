package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Human;
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

    public static void serializeAlly(Ally ally, MicroByteBuffer buffer) {
        serializeHuman(ally, buffer);
        SimpleSerializer.serialize(ally.getX(), buffer);
        SimpleSerializer.serialize(ally.getY(), buffer);
        SimpleSerializer.serialize(ally.getNumber(), buffer);
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
                SimpleSerializer.serialize(SimpleSerializer.NO, buffer);
                for (Ally ally : ((ServerWarriorCollection) battleGroup.getWarriors()).getWarriors()) {
                    serializeAlly(ally, buffer);
                }
            } else {
                SimpleSerializer.serialize(SimpleSerializer.YES, buffer);
                for (Warrior mine : ((ServerWarriorCollection) battleGroup.getWarriors()).getWarriors()) {
                    SerializeHelper.serializeEntityReference(mine, buffer);
                }
            }
        }
    }


    public static void serialize(Battle battle, Account account, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battle, buffer);
        SimpleSerializer.serialize(battle.getName(), buffer);
        SerializeHelper.serializeEntityReference(battle.getBattleType(), buffer);
        if (battle.getAlliances() != null) {
            SimpleSerializer.serialize((byte)battle.getAlliances().length, buffer);
            for (BattleAlliance alliance : battle.getAlliances()) {
                if (WarriorHelper.isAlly(alliance, account)) {
                    SimpleSerializer.serialize(SimpleSerializer.ALLY, buffer);
                    serializeAllies(alliance, account, buffer);
                } else {
                    SimpleSerializer.serialize(SimpleSerializer.ENEMY, buffer);
                    serializeEnemies(alliance, buffer);
                }
            }
        } else {
            SimpleSerializer.serialize(SerializeHelper.NULL_REFERENCE, buffer);
        }
        if (battle.getMap() != null) {
            BattleMapSerializer.serialize(battle.getMap(), account, buffer);
        } else {
            SimpleSerializer.serialize(SerializeHelper.NULL_REFERENCE, buffer);
        }
    }

}
