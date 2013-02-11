package com.geargames.regolith.serializers;

import com.geargames.regolith.units.*;
import com.geargames.regolith.units.base.*;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerAmmunitionPacketCollection;
import com.geargames.regolith.units.dictionaries.ServerStateTackleCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.tackle.*;

/**
 * @author Mikhail_Kutuzov
 *         created: 31.03.12  20:16
 */
public class AccountSerializer {

    private static void serialize(StateTackle tackle, MicroByteBuffer buffer) {
        if (tackle instanceof Armor) {
            SimpleSerializer.serialize(SimpleSerializer.findTypeId("Armor"), buffer);
            TackleSerializer.serializeArmor((Armor) tackle, buffer);
        } else if (tackle instanceof Weapon) {
            SimpleSerializer.serialize(SimpleSerializer.findTypeId("Weapon"), buffer);
            TackleSerializer.serializeWeapon((Weapon) tackle, buffer);
        }
    }

    private static void serialize(Bag bag, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(bag, buffer);
        SimpleSerializer.serialize(bag.getWeight(), buffer);
        SimpleSerializer.serialize((short)bag.getTackles().size(), buffer);
        for (StateTackle tackle : ((ServerStateTackleCollection)bag.getTackles()).getTackles()) {
            serialize(tackle, buffer);
        }
    }

    private static void serialize(AmmunitionPacket packet, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(packet, buffer);
        SimpleSerializer.serialize(packet.getCount(), buffer);
        Ammunition tackle = packet.getAmmunition();
        if (tackle instanceof Projectile) {
            SimpleSerializer.serialize(SimpleSerializer.findTypeId("Projectile"), buffer);
            TackleSerializer.serializeProjectile((Projectile) tackle, buffer);
        } else if (tackle instanceof Medikit) {
            SimpleSerializer.serialize(SimpleSerializer.findTypeId("Medikit"), buffer);
            TackleSerializer.serializeMedikit((Medikit) tackle, buffer);
        }
    }

    private static void serialize(AmmunitionBag bag, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(bag, buffer);
        SimpleSerializer.serialize(bag.getSize(), buffer);
        ServerAmmunitionPacketCollection packets = (ServerAmmunitionPacketCollection)bag.getPackets();
        for (AmmunitionPacket packet : packets.getPackets()) {
            serialize(packet, buffer);
        }
    }

    public static void serialize(Warrior warrior, MicroByteBuffer buffer) {
        BattleSerializer.serializeAlly(warrior, buffer);

        SimpleSerializer.serialize(warrior.getStrength(), buffer);
        SimpleSerializer.serialize(warrior.getSpeed(), buffer);
        SimpleSerializer.serialize(warrior.getMarksmanship(), buffer);
        SimpleSerializer.serialize(warrior.getCraftiness(), buffer);
        SimpleSerializer.serialize(warrior.getBirthDate().getTime(), buffer);
        SimpleSerializer.serialize(warrior.getVitality(), buffer);
        SimpleSerializer.serialize(warrior.getSkillScores(), buffer);
        SimpleSerializer.serialize(warrior.getActionScore(), buffer);
        SimpleSerializer.serialize(warrior.getExperience(), buffer);
        serialize(warrior.getBag(), buffer);
        serialize(warrior.getAmmunitionBag(), buffer);
    }

    public static void serialize(Account account, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(account, buffer);
        SimpleSerializer.serialize(account.getName(), buffer);
        SimpleSerializer.serialize(account.getFrameId(), buffer);
        SimpleSerializer.serialize(account.getBreadwinner(), buffer);
        SimpleSerializer.serialize(account.getCoach(), buffer);
        SimpleSerializer.serialize(account.getEconomist(), buffer);
        SimpleSerializer.serialize(account.getExperience(), buffer);
        SimpleSerializer.serialize(account.getFortunate(), buffer);
        SimpleSerializer.serialize(account.getSpecialist(), buffer);
        SimpleSerializer.serialize(account.getMoney(), buffer);
        SimpleSerializer.serialize((byte) account.getWarriors().size(), buffer);
        for (Warrior warrior : ((ServerWarriorCollection) account.getWarriors()).getWarriors()) {
            serialize(warrior, buffer);
        }
        serialize(account.getBase(), buffer);
    }

    public static void serialize(Clan clan, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(clan, buffer);
        SimpleSerializer.serialize(clan.getName(), buffer);
    }

    public static void serialize(ClearingShop clearingShop, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(clearingShop.getLevel(), buffer);
    }

    public static void serialize(Hospital hospital, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(hospital.getLevel(), buffer);
    }

    public static void serialize(RestHouse restHouse, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(restHouse.getLevel(), buffer);
    }

    public static void serialize(ShootingRange shootingRange, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(shootingRange.getLevel(), buffer);
    }

    public static void serialize(StoreHouse storeHouse, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(storeHouse.getLevel(), buffer);
        serialize(storeHouse.getBag(), buffer);
        serialize(storeHouse.getAmmunitionBag(), buffer);
    }

    public static void serialize(TrainingCenter trainingCenter, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(trainingCenter.getLevel(), buffer);
    }

    public static void serialize(WorkShop workShop, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(workShop.getLevel(), buffer);
    }

    public static void serialize(Base base, MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(base, buffer);
        serialize(base.getClearingShop(), buffer);
        serialize(base.getHospital(), buffer);
        serialize(base.getRestHouse(), buffer);
        serialize(base.getShootingRange(), buffer);
        serialize(base.getStoreHouse(), buffer);
        serialize(base.getTrainingCenter(), buffer);
        serialize(base.getWorkShop(), buffer);
    }

}
