package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.helpers.ClientHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.*;
import com.geargames.regolith.units.base.*;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientStateTackleCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.tackle.*;

import java.util.Date;
import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 16.04.12
 */
public class AccountDeserializer {

    private static StateTackle getStateTackleById(int id, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        StateTackle tackle = null;
        if (id == SerializeHelper.findTypeId("Weapon")) {
            tackle = new Weapon();
            TackleDeserializer.deSerialize((Weapon) tackle, buffer, baseConfiguration);
        } else if (id == SerializeHelper.findTypeId("Armor")) {
            tackle = new Armor();
            TackleDeserializer.deSerialize((Armor) tackle, buffer, baseConfiguration);
        }
        return tackle;
    }

    private static Ammunition getAmmunitionById(short id, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        Ammunition tackle = null;
        if (id == SerializeHelper.findTypeId("Projectile")) {
            tackle = new Projectile();
            TackleDeserializer.deSerializeProjectile((Projectile) tackle, buffer, baseConfiguration);
        } else if (id == SerializeHelper.findTypeId("Medikit")) {
            tackle = new Medikit();
            TackleDeserializer.deserializeMedikit((Medikit) tackle, buffer, baseConfiguration);
        }
        return tackle;
    }

    private static void deserialize(Bag bag, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        bag.setId(SimpleDeserializer.deserializeInt(buffer));
        bag.setWeight(SimpleDeserializer.deserializeShort(buffer));
        short size = SimpleDeserializer.deserializeShort(buffer);
        for (int i = 0; i < size; i++) {
            bag.getTackles().add(getStateTackleById(SimpleDeserializer.deserializeShort(buffer), buffer, baseConfiguration));
        }
    }

    private static void deserialize(AmmunitionPacket packet, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        packet.setId(SimpleDeserializer.deserializeInt(buffer));
        packet.setCount(SimpleDeserializer.deserializeShort(buffer));
        packet.setAmmunition(getAmmunitionById(SimpleDeserializer.deserializeShort(buffer), buffer, baseConfiguration));
    }

    private static void deserialize(AmmunitionBag bag, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        bag.setId(SimpleDeserializer.deserializeInt(buffer));
        int size = SimpleDeserializer.deserializeShort(buffer);
        for (int i = 0; i < size; i++) {
            AmmunitionPacket packet = new AmmunitionPacket();
            bag.getPackets().add(packet);
            deserialize(bag.getPackets().get(i), buffer, baseConfiguration);
        }
    }

    public static void deserialize(Warrior warrior, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        BattleDeserializer.deserializeAlly(warrior, buffer, baseConfiguration);
        warrior.setStrength(SimpleDeserializer.deserializeShort(buffer));
        warrior.setSpeed(buffer.get());
        warrior.setMarksmanship(buffer.get());
        warrior.setCraftiness(buffer.get());
        warrior.setBirthDate(new Date(SimpleDeserializer.deserializeLong(buffer)));
        warrior.setVitality(buffer.get());
        warrior.setSkillScores(SimpleDeserializer.deserializeShorts(buffer));
        warrior.setSkills(new Skill[baseConfiguration.getWeaponCategories().size()]);
        WarriorHelper.adjustSkills(warrior, baseConfiguration);
        warrior.setActionScore(SimpleDeserializer.deserializeShort(buffer));
        warrior.setExperience(SimpleDeserializer.deserializeInt(buffer));
        Bag bag = new Bag();
        bag.setTackles(new ClientStateTackleCollection(new Vector()));
        warrior.setBag(bag);
        deserialize(warrior.getBag(), buffer, baseConfiguration);
        warrior.setAmmunitionBag(ClientHelper.createAmmunitionBag(baseConfiguration));
        deserialize(warrior.getAmmunitionBag(), buffer, baseConfiguration);
    }

    public static Account deserializeAnother(MicroByteBuffer buffer){
        Account account = new Account();
        account.setId(SimpleDeserializer.deserializeInt(buffer));
        account.setName(SimpleDeserializer.deserializeString(buffer));
        account.setFrameId(SimpleDeserializer.deserializeInt(buffer));
        return account;
    }

    public static Account deserialize(MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        Account account = deserializeAnother(buffer);
        account.setBreadwinner(SimpleDeserializer.deserializeInt(buffer));
        account.setCoach(SimpleDeserializer.deserializeInt(buffer));
        account.setEconomist(SimpleDeserializer.deserializeInt(buffer));
        account.setExperience(SimpleDeserializer.deserializeInt(buffer));
        account.setFortunate(SimpleDeserializer.deserializeInt(buffer));
        account.setSpecialist(SimpleDeserializer.deserializeInt(buffer));
        account.setMoney(SimpleDeserializer.deserializeInt(buffer));
        account.setRegolith(SimpleDeserializer.deserializeInt(buffer));
        byte length = buffer.get();
        ClientWarriorCollection warriors = new ClientWarriorCollection(new Vector());
        for (int i = 0; i < length; i++) {
            warriors.add(new Warrior());
            deserialize(warriors.get(i), buffer, baseConfiguration);
        }
        account.setWarriors(warriors);
        account.setBase(deserializeBase(buffer, baseConfiguration));
        return account;
    }

    public static void deserializeStoreHouse(StoreHouse storeHouse, MicroByteBuffer buffer, BaseConfiguration baseConfiguration){
        storeHouse.setLevel(buffer.get());
        deserialize(storeHouse.getBag(), buffer, baseConfiguration);
        deserialize(storeHouse.getAmmunitionBag(), buffer, baseConfiguration);
    }

    private static Base deserializeBase(MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        Base base = new Base();
        base.setId(SimpleDeserializer.deserializeInt(buffer));
        ClearingShop clearingShop = new ClearingShop();
        base.setClearingShop(clearingShop);
        clearingShop.setLevel(buffer.get());
        Hospital hospital = new Hospital();
        base.setHospital(hospital);
        hospital.setLevel(buffer.get());
        RestHouse restHouse = new RestHouse();
        base.setRestHouse(restHouse);
        restHouse.setLevel(buffer.get());
        ShootingRange shootingRange = new ShootingRange();
        base.setShootingRange(shootingRange);
        shootingRange.setLevel(buffer.get());
        StoreHouse storeHouse = ClientHelper.createStoreHouse(baseConfiguration);
        base.setStoreHouse(storeHouse);
        deserializeStoreHouse(storeHouse, buffer, baseConfiguration);
        TrainingCenter trainingCenter = new TrainingCenter();
        base.setTrainingCenter(trainingCenter);
        trainingCenter.setLevel(buffer.get());
        WorkShop workShop = new WorkShop();
        base.setWorkShop(workShop);
        workShop.setLevel(buffer.get());
        return base;
    }

}
