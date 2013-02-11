package com.geargames.regolith;

import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Rank;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.battle.Border;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.tackle.*;

/**
 * User: mkutuzov
 * Date: 19.04.12
 *
 * Класс помошник, предназначен для общего(для клиента и сервера) поиска сущностей внутри текущей конфигурации.
 */
public class BaseConfigurationHelper {
    public static BattleType findBattleTypeById(int id, BaseConfiguration configuration) {
        if (id == SimpleSerializer.NULL_REFERENCE) {
            return null;
        } else {
            BattleTypeCollection battleTypes = configuration.getBattleTypes();
            int length = battleTypes.size();
            for (int i = 0; i < length; i++) {
                if (id == battleTypes.get(i).getId()) {
                    return battleTypes.get(i);
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static Medikit findMedikitById(int id, BaseConfiguration configuration) {
        if (id == SimpleSerializer.NULL_REFERENCE) {
            return null;
        } else {
            MedikitCollection medikits = configuration.getMedikits();
            int length = medikits.size();
            for (int i = 0; i < length; i++) {
                if (id == medikits.get(i).getId()) {
                    return medikits.get(i);
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static WeaponCategory findWeaponCategoryById(int id, BaseConfiguration configuration) {
        if (id == SimpleSerializer.NULL_REFERENCE) {
            return null;
        } else {
            WeaponCategoryCollection categories = configuration.getWeaponCategories();
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId() == id) {
                    return categories.get(i);
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static Rank findRankById(int id, BaseConfiguration configuration) {
        if (id == SimpleSerializer.NULL_REFERENCE) {
            return null;
        } else {
            RankCollection ranks = configuration.getRanks();
            for (int i = 0; i < ranks.size(); i++) {
                if (id == ranks.get(i).getId()) {
                    return ranks.get(i);
                }
            }
            throw new IllegalArgumentException("There is no rank with an identifier = " + id + ". Ranks are " + ranks);
        }
    }

    public static Skill findSkillByAction(byte action, BaseConfiguration configuration) {
        SkillCollection skills = configuration.getSkills();
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i).getAction() == action) {
                return skills.get(i);
            }
        }
        throw new IllegalArgumentException();
    }

    public static ArmorType findArmorTypeById(int id, BaseConfiguration configuration) {
        if (id == SimpleSerializer.NULL_REFERENCE) {
            return null;
        } else {
            ArmorTypeCollection armorTypes = configuration.getArmorTypes();
            int length = armorTypes.size();
            for (int i = 0; i < length; i++) {
                if (id == armorTypes.get(i).getId()) {
                    return armorTypes.get(i);
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static WeaponType findWeaponTypeById(int id, WeaponCategory weaponCategory) {
        if (id == SimpleSerializer.NULL_REFERENCE) {
            return null;
        } else {
            WeaponTypeCollection weaponTypes = weaponCategory.getWeaponTypes();
            for (int i = 0; i < weaponTypes.size(); i++) {
                if (id == weaponTypes.get(i).getId()) {
                    return weaponTypes.get(i);
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static Projectile findProjectileById(int id, BaseConfiguration configuration) {
        if (id == SimpleSerializer.NULL_REFERENCE) {
            return null;
        } else {
            ProjectileCollection projectiles = configuration.getProjectiles();
            for (int i = 0; i < projectiles.size(); i++) {
                if (projectiles.get(i).getId() == id) {
                    return projectiles.get(i);
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static Border findBorderById(int id, BaseConfiguration configuration) {
        if (id == SimpleSerializer.NULL_REFERENCE) {
            return null;
        } else {
            BorderCollection borders = configuration.getBorders();
            for (int i = 0; i < borders.size(); i++) {
                if (borders.get(i).getId() == id) {
                    return borders.get(i);
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
