package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.Weapon;
import com.geargames.regolith.units.tackle.*;

/**
 * @author Mikhail_Kutuzov
 *         created: 28.03.12  20:25
 */
public class TackleSerializer {

    private static void serializeAbstractTackle(AbstractTackle tackle, MicroByteBuffer buffer){
        SerializeHelper.serializeEntityReference(tackle, buffer);
        SimpleSerializer.serialize(tackle.getFrameId(), buffer);
        SimpleSerializer.serialize(tackle.getName(), buffer);
        SimpleSerializer.serialize(tackle.getWeight(), buffer);
    }

    private static void serializeAmmunition(Ammunition ammunition, MicroByteBuffer buffer){
        serializeAbstractTackle(ammunition, buffer);
        SerializeHelper.serializeEntityReference(ammunition.getCategory(), buffer);
    }

    private static void serializeStateTackle(StateTackle stateTackle, MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(stateTackle, buffer);
        SimpleSerializer.serialize(stateTackle.getFirmness(), buffer);
        SimpleSerializer.serialize(stateTackle.getState(), buffer);
        SimpleSerializer.serialize(stateTackle.getUpgrade(), buffer);
    }

    public static void serializeAmmunitionCategory(AmmunitionCategory ammunitionCategory, MicroByteBuffer buffer){
        SerializeHelper.serializeEntityReference(ammunitionCategory, buffer);
        SimpleSerializer.serialize(ammunitionCategory.getName(), buffer);
        SimpleSerializer.serialize(ammunitionCategory.getQuality(), buffer);
    }

    public static void serializeWeaponCategory(WeaponCategory weaponCategory, MicroByteBuffer buffer){
        SerializeHelper.serializeEntityReference(weaponCategory, buffer);
        SimpleSerializer.serialize(weaponCategory.getName(), buffer);
        SerializeHelper.serializeEntityReferences(weaponCategory.getWeaponTypes(), buffer);
    }

    public static void serializeArmorType(ArmorType armorType, MicroByteBuffer buffer){
        serializeAbstractTackle(armorType, buffer);

        buffer.put(armorType.getArmor());
        SimpleSerializer.serialize(armorType.getBaseFirmness(), buffer);
        buffer.put(armorType.getBodyParticle());
        SimpleSerializer.serialize(armorType.getCraftinessBonus(), buffer);
        SimpleSerializer.serialize(armorType.getMarksmanshipBonus(), buffer);
        SimpleSerializer.serialize(armorType.getRegenerationBonus(), buffer);
        SimpleSerializer.serialize(armorType.getSpeedBonus(), buffer);
        SimpleSerializer.serialize(armorType.getStrengthBonus(), buffer);
        SimpleSerializer.serialize(armorType.getVisibilityBonus(), buffer);
    }

    public static void serializeWeaponType(WeaponType weaponType, MicroByteBuffer buffer){
        serializeAbstractTackle(weaponType, buffer);
        SerializeHelper.serializeEntityReference(weaponType.getCategory(), buffer);

        buffer.put(weaponType.getAccuracy());
        buffer.put(weaponType.getAccurateAction());
        buffer.put(weaponType.getQuickAction());
        buffer.put(weaponType.getAmmunitionPerShoot());
        SimpleSerializer.serialize(weaponType.getBaseFirmness(), buffer);
        SimpleSerializer.serialize(weaponType.getCapacity(), buffer);
        SimpleSerializer.serialize(weaponType.getCriticalDamage(), buffer);

        buffer.put(weaponType.getDistance().getMax());
        buffer.put(weaponType.getDistance().getMaxOptimal());
        buffer.put(weaponType.getDistance().getMinOptimal());
        buffer.put(weaponType.getDistance().getMin());

        SimpleSerializer.serialize(weaponType.getMaxDamage().getMaxDistance(), buffer);
        SimpleSerializer.serialize(weaponType.getMaxDamage().getOptDistance(), buffer);
        SimpleSerializer.serialize(weaponType.getMaxDamage().getMinDistance(), buffer);

        SimpleSerializer.serialize(weaponType.getMinDamage().getMaxDistance(), buffer);
        SimpleSerializer.serialize(weaponType.getMinDamage().getOptDistance(), buffer);
        SimpleSerializer.serialize(weaponType.getMinDamage().getMinDistance(), buffer);

        buffer.put(weaponType.getMinSkill().getAction());
        SerializeHelper.serializeEntityReferences(weaponType.getProjectiles(), buffer);
    }

    public static void serializeMedikit(Medikit medikit, MicroByteBuffer buffer){
        serializeAmmunition(medikit, buffer);
        buffer.put(medikit.getActionScores());
        SimpleSerializer.serialize(medikit.getValue(), buffer);
        SimpleSerializer.serialize(medikit.getMinSkill().getAction(), buffer);
    }

    public static void serializeProjectile(Projectile projectile, MicroByteBuffer buffer) {
        serializeAmmunition(projectile, buffer);
        SimpleSerializer.serialize((short)projectile.getWeaponTypes().size(), buffer);
    }

    public static void serializeArmor(Armor armor, MicroByteBuffer buffer) {
        serializeStateTackle(armor, buffer);
        SerializeHelper.serializeEntityReference(armor.getArmorType(), buffer);
    }

    public static void serializeWeapon(Weapon weapon, MicroByteBuffer buffer){
        serializeStateTackle(weapon, buffer);
        SerializeHelper.serializeEntityReference(weapon.getWeaponType().getCategory(), buffer);
        SerializeHelper.serializeEntityReference(weapon.getWeaponType(), buffer);
        if(weapon.getProjectile() != null){
            SerializeHelper.serializeEntityReference(weapon.getProjectile(), buffer);
        }else{
            SimpleSerializer.serialize(SerializeHelper.NULL_REFERENCE, buffer);
        }
        SimpleSerializer.serialize(weapon.getLoad(), buffer);
    }

}
