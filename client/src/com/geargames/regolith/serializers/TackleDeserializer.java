package com.geargames.regolith.serializers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.tackle.*;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 04.04.12
 */
public class TackleDeserializer {

    public static void deserializeMedikit(Medikit medikit, MicroByteBuffer buffer, BaseConfiguration baseConfiguration){
        deserializeAmmunition(medikit, buffer, baseConfiguration);
        medikit.setActionScores(buffer.get());
        medikit.setValue(SimpleDeserializer.deserializeShort(buffer));
        medikit.setMinSkill(deserializeSkill(buffer, baseConfiguration));
    }

    public static void deserializeTackle(Tackle tackle, MicroByteBuffer buffer) {
        tackle.setId(SimpleDeserializer.deserializeInt(buffer));
        tackle.setFrameId(SimpleDeserializer.deserializeInt(buffer));
        tackle.setName(SimpleDeserializer.deserializeString(buffer));
        tackle.setWeight(SimpleDeserializer.deserializeShort(buffer));
    }

    public static WeaponCategory deserializeWeaponCategory(MicroByteBuffer buffer, BaseConfiguration configuration) {
        return BaseConfigurationHelper.findWeaponCategoryById(SimpleDeserializer.deserializeInt(buffer), configuration);
    }

    private static Skill deserializeSkill(MicroByteBuffer buffer, BaseConfiguration configuration) {
        return BaseConfigurationHelper.findSkillByAction(buffer.get(), configuration);
    }

    private static ProjectileCollection deserializeWeaponsProjectiles(MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        short length = buffer.get();
        ClientProjectileCollection projectiles = new ClientProjectileCollection();
        projectiles.setProjectiles(new Vector(length));
        for (int i = 0; i < length; i++) {
            projectiles.add(BaseConfigurationHelper.findProjectileById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
        }
        return projectiles;
    }

    public static void deserialize(WeaponType weaponType, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        deserializeTackle(weaponType, buffer);
        WeaponCategory category = deserializeWeaponCategory(buffer, baseConfiguration);
        if (category != null) {
            weaponType.setCategory(category);
        } else {
            throw new IllegalArgumentException();
        }

        weaponType.setAccuracy(buffer.get());
        weaponType.setAccurateAction(buffer.get());
        weaponType.setAmmunitionPerShoot(buffer.get());

        weaponType.setBaseFirmness(SimpleDeserializer.deserializeShort(buffer));
        weaponType.setCapacity(SimpleDeserializer.deserializeShort(buffer));
        weaponType.setCriticalDamage(SimpleDeserializer.deserializeShort(buffer));
        WeaponDistances distances = new WeaponDistances();
        weaponType.setDistance(distances);
        distances.setMax(buffer.get());
        distances.setMaxOptimal(buffer.get());
        distances.setMinOptimal(buffer.get());
        distances.setMin(buffer.get());

        WeaponDamage damage = new WeaponDamage();
        weaponType.setMaxDamage(damage);
        damage.setMaxDistance(SimpleDeserializer.deserializeShort(buffer));
        damage.setOptDistance(SimpleDeserializer.deserializeShort(buffer));
        damage.setMinDistance(SimpleDeserializer.deserializeShort(buffer));

        damage = new WeaponDamage();
        weaponType.setMinDamage(damage);
        damage.setMaxDistance(SimpleDeserializer.deserializeShort(buffer));
        damage.setOptDistance(SimpleDeserializer.deserializeShort(buffer));
        damage.setMinDistance(SimpleDeserializer.deserializeShort(buffer));

        weaponType.setMinSkill(deserializeSkill(buffer, baseConfiguration));
        ProjectileCollection projectiles = deserializeWeaponsProjectiles(buffer, baseConfiguration);
        weaponType.setProjectiles(projectiles);
        int length = projectiles.size();
        for (int i = 0; i < length; i++) {
            WeaponTypeCollection weaponTypes = projectiles.get(i).getWeaponTypes();
            int wLength = weaponTypes.size();
            boolean found = false;
            for (int j = 0; j < wLength; j++) {
                if (weaponTypes.get(j) == weaponType) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                weaponTypes.add(weaponType);
            }
        }
    }

    public static void deserialize(ArmorType armorType, MicroByteBuffer buffer) {
        deserializeTackle(armorType, buffer);

        armorType.setArmor(buffer.get());
        armorType.setBaseFirmness(SimpleDeserializer.deserializeShort(buffer));
        armorType.setBodyParticle(buffer.get());
        armorType.setCraftinessBonus(SimpleDeserializer.deserializeShort(buffer));
        armorType.setMarksmanshipBonus(SimpleDeserializer.deserializeShort(buffer));
        armorType.setRegenerationBonus(SimpleDeserializer.deserializeShort(buffer));
        armorType.setSpeedBonus(SimpleDeserializer.deserializeShort(buffer));
        armorType.setStrengthBonus(SimpleDeserializer.deserializeShort(buffer));
        armorType.setVisibilityBonus(SimpleDeserializer.deserializeShort(buffer));
    }

    private static void deserializeAmmunition(Ammunition ammunition, MicroByteBuffer buffer, BaseConfiguration configuration) {
        deserializeTackle(ammunition, buffer);
        int id = SimpleDeserializer.deserializeInt(buffer);
        AmmunitionCategoryCollection collection = configuration.getAmmunitionCategories();
        int size = collection.size();
        for (int  i = 0; i < size; i++) {
            if (collection.get(i).getId() == id) {
                ammunition.setCategory(collection.get(i));
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void deSerializeProjectile(Projectile projectile, MicroByteBuffer buffer, BaseConfiguration configuration) {
        deserializeAmmunition(projectile, buffer, configuration);
        ClientWeaponTypeCollection dictionary = new ClientWeaponTypeCollection();
        dictionary.setWeaponTypes(new Vector(SimpleDeserializer.deserializeShort(buffer)));
        projectile.setWeaponTypes(dictionary);
    }

    private static void deSerializeStateTackle(StateTackle stateTackle, MicroByteBuffer buffer) {
        stateTackle.setId(SimpleDeserializer.deserializeInt(buffer));
        stateTackle.setFirmness(SimpleDeserializer.deserializeShort(buffer));
        stateTackle.setState(SimpleDeserializer.deserializeShort(buffer));
        stateTackle.setUpgrade(buffer.get());
    }

    public static Armor deSerialize(Armor armor, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        deSerializeStateTackle(armor, buffer);
        armor.setArmorType(BaseConfigurationHelper.findArmorTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
        return armor;
    }

    public static Weapon deSerialize(Weapon weapon, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        deSerializeStateTackle(weapon, buffer);
        int categoryId = SimpleDeserializer.deserializeInt(buffer);
        WeaponCategoryCollection categories = baseConfiguration.getWeaponCategories();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == categoryId) {
                weapon.setWeaponType(BaseConfigurationHelper.findWeaponTypeById(SimpleDeserializer.deserializeInt(buffer), categories.get(i)));
                break;
            }
        }
        int projectileId = SimpleDeserializer.deserializeInt(buffer);
        if (projectileId != SimpleSerializer.NULL_REFERENCE) {
            weapon.setProjectile(BaseConfigurationHelper.findProjectileById(projectileId, baseConfiguration));
        }
        weapon.setLoad(SimpleDeserializer.deserializeShort(buffer));
        return weapon;
    }
}
