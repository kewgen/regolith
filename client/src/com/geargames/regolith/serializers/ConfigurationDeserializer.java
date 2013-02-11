package com.geargames.regolith.serializers;

import com.geargames.regolith.*;
import com.geargames.regolith.units.Underload;
import com.geargames.regolith.units.Rank;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.SubordinationDamage;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.battle.ClientBorder;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.tackle.*;

import java.util.Vector;

/**
 * @author Mikhail_Kutuzov
 *         created: 09.04.12  10:07
 */
public class ConfigurationDeserializer {
    private static RankCollection deserializeRanks(MicroByteBuffer buffer) {
        int length = buffer.get();
        ClientRankCollection ranks = new ClientRankCollection();
        ranks.setRanks(new Vector(length));
        for (int i = 0; i < length; i++) {
            Rank rank = new Rank();
            ranks.add(rank);
            rank.setId(SimpleDeserializer.deserializeInt(buffer));
            rank.setName(SimpleDeserializer.deserializeString(buffer));
            rank.setExperience(SimpleDeserializer.deserializeShort(buffer));
        }
        return ranks;
    }

    private static SkillCollection deserializeSkills(MicroByteBuffer buffer) {
        int length = buffer.get();
        ClientSkillCollection skills = new ClientSkillCollection();
        skills.setSkills(new Vector(length));
        for (int i = 0; i < length; i++) {
            skills.add(new Skill());
            skills.get(i).setAction(buffer.get());
            skills.get(i).setExperience(SimpleDeserializer.deserializeShort(buffer));
        }
        return skills;
    }

    private static ArmorTypeCollection deserializeArmorTypes(MicroByteBuffer buffer) {
        short length = SimpleDeserializer.deserializeShort(buffer);
        ClientArmorTypeCollection armorTypes = new ClientArmorTypeCollection();
        armorTypes.setArmorTypes(new Vector(length));
        for (int i = 0; i < length; i++) {
            armorTypes.add(new ArmorType());
            TackleDeserializer.deserialize(armorTypes.get(i), buffer);
        }
        return armorTypes;
    }

    private static ProjectileCollection deserializeProjectiles(MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        short length = SimpleDeserializer.deserializeShort(buffer);
        ClientProjectileCollection projectiles = new ClientProjectileCollection();
        projectiles.setProjectiles(new Vector(length));
        for (int i = 0; i < length; i++) {
            projectiles.add(new Projectile());
            TackleDeserializer.deSerializeProjectile(projectiles.get(i), buffer, baseConfiguration);
        }
        return projectiles;
    }

    private static void deserializeWeaponCategories(MicroByteBuffer buffer, BaseConfiguration configuration) {
        byte length = buffer.get();
        ClientWeaponCategoryCollection categories = new ClientWeaponCategoryCollection();
        configuration.setWeaponCategories(categories);
        categories.setWeaponCategories(new Vector(length));
        for (int i = 0; i < length; i++) {
            int id = SimpleDeserializer.deserializeInt(buffer);
            WeaponCategory weaponCategory = new WeaponCategory();
            categories.add(weaponCategory);
            weaponCategory.setId(id);
            weaponCategory.setName(SimpleDeserializer.deserializeString(buffer));
            short wLength = SimpleDeserializer.deserializeShort(buffer);
            ClientWeaponTypeCollection weaponTypes = new ClientWeaponTypeCollection();
            weaponTypes.setWeaponTypes(new Vector(wLength));
            for (int j = 0; j < wLength; j++) {
                weaponTypes.add(new WeaponType());
                TackleDeserializer.deserialize(weaponTypes.get(j), buffer, configuration);
            }
            weaponCategory.setWeaponTypes(weaponTypes);
        }
    }

    private static AmmunitionCategoryCollection deserializeAmmunitionCategories(MicroByteBuffer buffer) {
        byte length = buffer.get();
        ClientAmmunitionCategoryCollection ammunitionCategories = new ClientAmmunitionCategoryCollection();
        ammunitionCategories.setCategories(new Vector(length));
        for (int i = 0; i < length; i++) {
            AmmunitionCategory ammunitionCategory = new AmmunitionCategory();
            ammunitionCategory.setId(SimpleDeserializer.deserializeInt(buffer));
            ammunitionCategory.setName(SimpleDeserializer.deserializeString(buffer));
            ammunitionCategory.setQuality(SimpleDeserializer.deserializeDouble(buffer));
            ammunitionCategories.add(ammunitionCategory);
        }
        return ammunitionCategories;
    }

    private static BattleType deserializeBattleType(MicroByteBuffer buffer){
        BattleType battleType = new BattleType();
        battleType.setId(SimpleDeserializer.deserializeInt(buffer));
        battleType.setName(SimpleDeserializer.deserializeString(buffer));
        battleType.setScores(buffer.get());
        battleType.setAllianceAmount(SimpleDeserializer.deserializeInt(buffer));
        battleType.setAllianceSize(SimpleDeserializer.deserializeInt(buffer));
        battleType.setGroupSize(SimpleDeserializer.deserializeInt(buffer));
        return battleType;
    }

    private static BattleTypeCollection deserializeBattleTypes(MicroByteBuffer buffer){
        ClientBattleTypeCollection battleTypes = new ClientBattleTypeCollection();
        battleTypes.setBattleTypes(new Vector());

        int size = buffer.get();
        for(int i = 0; i < size; i++){
            battleTypes.add(deserializeBattleType(buffer));
        }
        return battleTypes;
    }


    public static BaseConfiguration deserializeBaseConfiguration(MicroByteBuffer buffer) {
        BaseConfiguration configuration = new BaseConfiguration();
        configuration.setRevision(SimpleDeserializer.deserializeShort(buffer));
        configuration.setBattleTypes(deserializeBattleTypes(buffer));
        configuration.setRanks(deserializeRanks(buffer));
        configuration.setSkills(deserializeSkills(buffer));
        configuration.setAmmunitionCategories(deserializeAmmunitionCategories(buffer));
        configuration.setMaxWorkShopLevel(buffer.get());
        configuration.setMaxWorkShopProbability(buffer.get());
        configuration.setMinWorkShopProbability(buffer.get());
        configuration.setPocketsAmount(buffer.get());
        configuration.setVitalityStep(buffer.get());
        configuration.setStrengthStep(buffer.get());
        configuration.setSpeedStep(buffer.get());
        configuration.setMarksmanshipStep(buffer.get());
        configuration.setCraftinessStep(buffer.get());
        configuration.setBaseActionScore(buffer.get());
        configuration.setBaseHealth(SimpleDeserializer.deserializeShort(buffer));
        configuration.setBaseStrength(SimpleDeserializer.deserializeShort(buffer));
        configuration.setBaseCraftiness(buffer.get());
        configuration.setBaseMarksmanship(buffer.get());
        configuration.setInitWarriorsAmount(buffer.get());
        configuration.setArmorTypes(deserializeArmorTypes(buffer));
        configuration.setProjectiles(deserializeProjectiles(buffer, configuration));
        deserializeWeaponCategories(buffer, configuration);
        configuration.setMedikits(deserializeMedikits(buffer, configuration));
        configuration.setBorders(deserializeBorders(buffer));

        configuration.setMaxDamage(buffer.get());
        configuration.setMaxDistance(buffer.get());
        configuration.setMaxWeight(buffer.get());

        return configuration;
    }

    private static void deserializeBorder(ClientBorder border, MicroByteBuffer buffer) {
        border.setId(SimpleDeserializer.deserializeInt(buffer));
        border.setFrameId(SimpleDeserializer.deserializeInt(buffer));
        byte simpleTrio = buffer.get();
        if ((simpleTrio & 1) != 0) {
            border.setAbleToLookThrough(true);
        } else {
            border.setAbleToLookThrough(false);
        }
        if ((simpleTrio & 2) != 0) {
            border.setAbleToWalkThrough(true);
        } else {
            border.setAbleToWalkThrough(false);
        }
        if ((simpleTrio & 4) != 0) {
            border.setHalfLong(true);
        } else {
            border.setHalfLong(false);
        }
        border.setShootThrough(buffer.get());
    }

    private static BorderCollection deserializeBorders(MicroByteBuffer buffer) {
        int length = SimpleDeserializer.deserializeInt(buffer);
        ClientBorderCollection borders = new ClientBorderCollection();
        borders.setBorders(new Vector(length));
        for (int i = 0; i < length; i++) {
            ClientBorder border = new ClientBorder();
            borders.add(border);
            deserializeBorder(border, buffer);
        }

        return borders;
    }

    private static ClientMedikitCollection deserializeMedikits(MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        int length = buffer.get();
        ClientMedikitCollection medikits = new ClientMedikitCollection();
        medikits.setMedikits(new Vector(length));
        for (int i = 0; i < length; i++) {
            medikits.add(new Medikit());
            TackleDeserializer.deserializeMedikit(medikits.get(i), buffer, baseConfiguration);
        }
        return medikits;
    }

    private static ActionFees deserializeActionFees(MicroByteBuffer buffer) {
        ActionFees actionFees = new ActionFees();
        actionFees.setChangeWeapon(buffer.get());
        actionFees.setMove(buffer.get());
        actionFees.setPickupTackle(buffer.get());
        actionFees.setRechargeWeapon(buffer.get());
        actionFees.setShutOrOpen(buffer.get());
        actionFees.setSitOrStand(buffer.get());
        return actionFees;
    }

    private static SubordinationDamage[] deserializeSubordinationDamage(MicroByteBuffer buffer) {
        byte length = buffer.get();
        SubordinationDamage[] subordinationDamages = new SubordinationDamage[length];
        for (int i = 0; i < length; i++) {
            subordinationDamages[i] = new SubordinationDamage();
            subordinationDamages[i].setDamage(SimpleDeserializer.deserializeShort(buffer));
            subordinationDamages[i].setMaxRankDifference(buffer.get());
            subordinationDamages[i].setMinRankDifference(buffer.get());
        }
        return subordinationDamages;
    }

    private static Underload[] deserializeLoads(MicroByteBuffer buffer) {
        byte length = buffer.get();
        Underload[] underloads = new Underload[length];
        for (int i = 0; i < length; i++) {
            Underload underload = new Underload();
            underload.setAction(buffer.get());
            underload.setMax(buffer.get());
            underload.setMin(buffer.get());
            underloads[i] = underload;
        }
        return underloads;
    }

    public static BattleConfiguration deserializeBattleConfiguration(MicroByteBuffer buffer) {
        BattleConfiguration configuration = new BattleConfiguration();
        configuration.setAbilityMax(SimpleDeserializer.deserializeShort(buffer));
        configuration.setActionFees(deserializeActionFees(buffer));
        configuration.setActiveTime(SimpleDeserializer.deserializeInt(buffer));
        configuration.setArmorSpoiling(SimpleDeserializer.deserializeInt(buffer));
        configuration.setWeaponSpoiling(SimpleDeserializer.deserializeInt(buffer));
        configuration.setCriticalBarrierToVictimDistance(SimpleDeserializer.deserializeInt(buffer));
        configuration.setKillExperienceMultiplier(SimpleDeserializer.deserializeInt(buffer));
        configuration.setMinimalMapSize(SimpleDeserializer.deserializeInt(buffer));
        configuration.setWalkSpeed(SimpleDeserializer.deserializeInt(buffer));
        configuration.setSubordinationDamage(deserializeSubordinationDamage(buffer));
        configuration.setUnderloads(deserializeLoads(buffer));
        return configuration;
    }
}
