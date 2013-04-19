package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.*;
import com.geargames.regolith.units.Underload;
import com.geargames.regolith.units.Rank;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.SubordinationDamage;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.battle.Barrier;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.tackle.*;

/**
 * @author Mikhail_Kutuzov
 *         created: 01.04.12  15:29
 */
public class ConfigurationSerializer {

    private static void serialize(Barrier barrier, MicroByteBuffer buffer, ServerWeaponCategoryCollection weaponCategories) {
        SerializeHelper.serializeEntityReference(barrier, buffer);
        SimpleSerializer.serialize(barrier.getFrameId(), buffer);
        byte simpleTrio = 0;
        if (barrier.isAbleToLookThrough()) {
            simpleTrio |= 1;
        }
        if (barrier.isAbleToWalkThrough()) {
            simpleTrio |= 2;
        }
        if (barrier.isHalfLong()) {
            simpleTrio |= 4;
        }
        SimpleSerializer.serialize(simpleTrio, buffer);

        byte shootThrough = 0;
        for (WeaponCategory weaponCategory : weaponCategories.getCategories()) {
            boolean able = barrier.isAbleToShootThrough(weaponCategory);
            if (able) {
                shootThrough |= 1 << weaponCategory.getId();
            }
        }
        buffer.put(shootThrough);
    }

    private static void serialize(WeaponCategory category, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(category, buffer);
        SimpleSerializer.serialize(category.getName(), buffer);
        SimpleSerializer.serialize((short) category.getWeaponTypes().size(), buffer);
        for (WeaponType weaponType : ((ServerWeaponTypeCollection)category.getWeaponTypes()).getWeaponTypes()) {
            TackleSerializer.serializeWeaponType(weaponType, buffer);
        }
    }

    private static void serialize(Rank rank, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(rank, buffer);
        SimpleSerializer.serialize(rank.getName(), buffer);
        SimpleSerializer.serialize(rank.getExperience(), buffer);
    }

    private static void serialize(Skill skill, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(skill.getAction(), buffer);
        SimpleSerializer.serialize(skill.getExperience(), buffer);
    }

    private static void serialize(AmmunitionCategory ammunitionCategory, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(ammunitionCategory, buffer);
        SimpleSerializer.serialize(ammunitionCategory.getName(), buffer);
        SimpleSerializer.serialize(ammunitionCategory.getQuality(), buffer);
    }

    private static void serialize(BattleType battleType, MicroByteBuffer buffer){
        SerializeHelper.serializeEntityReference(battleType, buffer);
        SimpleSerializer.serialize(battleType.getName(), buffer);
        SimpleSerializer.serialize(battleType.getScores(), buffer);
        SimpleSerializer.serialize(battleType.getAllianceAmount(), buffer);
        SimpleSerializer.serialize(battleType.getAllianceSize(), buffer);
        SimpleSerializer.serialize(battleType.getGroupSize(), buffer);
    }

    public static void serialize(BaseConfiguration configuration, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(configuration.getRevision(), buffer);

        ServerBattleTypeCollection battleTypes = (ServerBattleTypeCollection)configuration.getBattleTypes();

        SimpleSerializer.serialize((byte) battleTypes.size(), buffer);
        for(BattleType battleType : battleTypes.getBattleTypes()){
            serialize(battleType, buffer);
        }

        ServerRankCollection ranks = (ServerRankCollection)configuration.getRanks();
        SimpleSerializer.serialize((byte) ranks.size(), buffer);
        for (Rank rank : ranks.getRanks()) {
            serialize(rank, buffer);
        }
        ServerSkillCollection skills = (ServerSkillCollection)configuration.getSkills();
        SimpleSerializer.serialize((byte) skills.size(), buffer);
        for (Skill skill : skills.getSkills()) {
            serialize(skill, buffer);
        }
        ServerAmmunitionCategoryCollection ammunitionCategories = (ServerAmmunitionCategoryCollection)configuration.getAmmunitionCategories();
        SimpleSerializer.serialize((byte) ammunitionCategories.size(), buffer);
        for (AmmunitionCategory ammunitionCategory : ammunitionCategories.getCategories()) {
            serialize(ammunitionCategory, buffer);
        }
        buffer.put(configuration.getMaxWorkShopLevel());
        buffer.put(configuration.getMaxWorkShopProbability());
        buffer.put(configuration.getMinWorkShopProbability());
        buffer.put(configuration.getPocketsAmount());
        buffer.put(configuration.getVitalityStep());
        buffer.put(configuration.getStrengthStep());
        buffer.put(configuration.getSpeedStep());
        buffer.put(configuration.getMarksmanshipStep());
        buffer.put(configuration.getCraftinessStep());
        SimpleSerializer.serialize(configuration.getBaseActionScore(), buffer);
        SimpleSerializer.serialize(configuration.getBaseHealth(), buffer);
        SimpleSerializer.serialize(configuration.getBaseStrength(), buffer);
        SimpleSerializer.serialize(configuration.getBaseCraftiness(), buffer);
        SimpleSerializer.serialize(configuration.getBaseMarksmanship(), buffer);
        SimpleSerializer.serialize(configuration.getInitWarriorsAmount(), buffer);

        ServerArmorTypeCollection armorTypes = (ServerArmorTypeCollection)configuration.getArmorTypes();
        SimpleSerializer.serialize((short) armorTypes.size(), buffer);
        for (ArmorType armorType : armorTypes.getArmorTypes()) {
            TackleSerializer.serializeArmorType(armorType, buffer);
        }
        ServerProjectileCollection projectiles = (ServerProjectileCollection)configuration.getProjectiles();
        SimpleSerializer.serialize((short) projectiles.size(), buffer);
        for (Projectile projectile : projectiles.getProjectiles()) {
            TackleSerializer.serializeProjectile(projectile, buffer);
        }
        ServerWeaponCategoryCollection weaponCategories = (ServerWeaponCategoryCollection)configuration.getWeaponCategories();
        SimpleSerializer.serialize((byte) weaponCategories.size(), buffer);
        for (WeaponCategory category : weaponCategories.getCategories()) {
            serialize(category, buffer);
        }
        ServerMedikitCollection medikits = (ServerMedikitCollection)configuration.getMedikits();
        SimpleSerializer.serialize((byte) medikits.size(), buffer);
        for (Medikit medikit : medikits.getMedikits()) {
            TackleSerializer.serializeMedikit(medikit, buffer);
        }
        ServerBarrierCollection barriers = (ServerBarrierCollection)configuration.getBarriers();
        SimpleSerializer.serialize(barriers.size(), buffer);
        for (Barrier barrier : barriers.getBarriers()) {
            serialize(barrier, buffer, weaponCategories);
        }

        SimpleSerializer.serialize(configuration.getMaxDamage(), buffer);
        SimpleSerializer.serialize(configuration.getMaxDistance(), buffer);
        SimpleSerializer.serialize(configuration.getMaxWeight(), buffer);
    }

    private static void serialize(ActionFees fees, MicroByteBuffer buffer) {
        buffer.put(fees.getChangeWeapon());
        buffer.put(fees.getMove());
        buffer.put(fees.getPickupTackle());
        buffer.put(fees.getRechargeWeapon());
        buffer.put(fees.getShutOrOpen());
        buffer.put(fees.getSitOrStand());
    }

    public static void serialize(BattleConfiguration configuration, MicroByteBuffer buffer) {
        SimpleSerializer.serialize(configuration.getAbilityMax(), buffer);
        serialize(configuration.getActionFees(), buffer);
        SimpleSerializer.serialize(configuration.getActiveTime(), buffer);
        SimpleSerializer.serialize(configuration.getArmorSpoiling(), buffer);
        SimpleSerializer.serialize(configuration.getWeaponSpoiling(), buffer);
        SimpleSerializer.serialize(configuration.getCriticalBarrierToVictimDistance(), buffer);
        SimpleSerializer.serialize(configuration.getKillExperienceMultiplier(), buffer);
        SimpleSerializer.serialize(configuration.getMinimalMapSize(), buffer);
        SimpleSerializer.serialize(configuration.getWalkSpeed(), buffer);

        SimpleSerializer.serialize((byte) (configuration.getSubordinationDamage().length), buffer);
        for (SubordinationDamage subordinationDamage : configuration.getSubordinationDamage()) {
            SimpleSerializer.serialize(subordinationDamage.getDamage(), buffer);
            buffer.put(subordinationDamage.getMaxRankDifference());
            buffer.put(subordinationDamage.getMinRankDifference());
        }

        SimpleSerializer.serialize((byte) (configuration.getUnderloads().length), buffer);
        for (Underload underload : configuration.getUnderloads()) {
            serialize(underload, buffer);
        }
    }

    private static void serialize(Underload underload, MicroByteBuffer buffer) {
        buffer.put(underload.getAction());
        buffer.put(underload.getMax());
        buffer.put(underload.getMin());
    }

}
