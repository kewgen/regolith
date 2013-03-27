package com.geargames.regolith.helpers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.units.dictionaries.ProjectileCollection;
import com.geargames.regolith.units.tackle.Projectile;
import com.geargames.regolith.units.tackle.Weapon;
import com.geargames.regolith.units.tackle.WeaponDistances;
import com.geargames.regolith.units.tackle.WeaponType;

/**
 * User: mkutuzov
 * Date: 06.02.12
 */
public class WeaponHelper {

    /**
     * Вернуть наибольшее возможное повреждение для данного типа оружия weaponType на рас-
     * стоянии distance.
     *
     * @param weaponType
     * @param distance
     * @return
     */
    public static int getWeaponTypeMaxDamage(WeaponType weaponType, double distance) {
        WeaponDistances wDistance = weaponType.getDistance();
        if (distance > wDistance.getMaxOptimal()) {
            if (distance <= wDistance.getMax()) {
                int dis1 = wDistance.getMaxOptimal();
                int dis2 = wDistance.getMax();
                int dem1 = weaponType.getMaxDamage().getOptDistance();
                int dem2 = weaponType.getMaxDamage().getMaxDistance();
                double k = (double) (dem1 - dem2) / (double) (dis1 - dis2);
                return dem1 - (int) (k * (dis1 + distance));
            } else {
                return 0;
            }
        } else if (distance < wDistance.getMinOptimal()) {
            if (distance >= wDistance.getMin()) {
                int dis1 = wDistance.getMinOptimal();
                int dis2 = wDistance.getMin();
                int dem1 = weaponType.getMaxDamage().getOptDistance();
                int dem2 = weaponType.getMaxDamage().getMinDistance();
                double k = (double) (dem1 - dem2) / (double) (dis1 - dis2);
                return dem1 - (int) (k * (dis1 + distance));
            } else {
                return 0;
            }
        } else {
            return weaponType.getMaxDamage().getOptDistance();
        }
    }

    /**
     * Вернуть наименьшее возможное повреждение для данного типа оружия weaponType на рас-
     * стоянии distance.
     *
     * @param weaponType
     * @param distance
     * @return
     */
    public static int getWeaponTypeMinDamage(WeaponType weaponType, double distance) {
        WeaponDistances wDistance = weaponType.getDistance();
        if (distance > wDistance.getMaxOptimal()) {
            if (distance <= wDistance.getMax()) {
                int dis1 = wDistance.getMaxOptimal();
                int dis2 = wDistance.getMax();
                int dem1 = weaponType.getMinDamage().getOptDistance();
                int dem2 = weaponType.getMinDamage().getMaxDistance();
                double k = (double) (dem1 - dem2) / (double) (dis1 - dis2);
                return dem1 - (int) (k * (dis1 + distance));
            } else {
                return 0;
            }
        } else if (distance < wDistance.getMinOptimal()) {
            if (distance >= wDistance.getMin()) {
                int dis1 = wDistance.getMinOptimal();
                int dis2 = wDistance.getMin();
                int dem1 = weaponType.getMinDamage().getOptDistance();
                int dem2 = weaponType.getMinDamage().getMinDistance();
                double k = (double) (dem1 - dem2) / (double) (dis1 - dis2);
                return dem1 - (int) (k * (dis1 + distance));
            } else {
                return 0;
            }
        } else {
            return weaponType.getMinDamage().getOptDistance();
        }
    }

    /**
     * Вернуть прибавку к меткости оружия за счёт уровня upgrade.
     *
     * @param weapon
     * @return
     */
    public static int getWeaponAccuracyBonus(Weapon weapon, BaseConfiguration baseConfiguration) {
        int max = baseConfiguration.getMaxWorkShopLevel();
        return (weapon.getUpgrade() / 2) + (weapon.getUpgrade() == max ? 1 : 0);
    }

    /**
     * Вернуть точность попадения оружием weapon.
     *
     * @param weapon
     * @return
     */
    public static int getWeaponAccuracy(Weapon weapon, BaseConfiguration baseConfiguration) {
        return weapon.getWeaponType().getAccuracy() + getWeaponAccuracyBonus(weapon, baseConfiguration);
    }

    /**
     * Вернуть прибавку к урону наносимому оружием, за счёт уровня upgrade оружия.
     *
     * @param weapon
     * @return
     */
    public static int getWeaponDamageBonus(Weapon weapon, BaseConfiguration baseConfiguration) {
        int max = baseConfiguration.getMaxWorkShopLevel();
        return weapon.getUpgrade() + (weapon.getUpgrade() == max ? 2 : 0);
    }

    /**
     * Вернуть наибольший урон наносимый оружием weapon на расстоянии distance.
     *
     * @param weapon
     * @param distance
     * @param baseConfiguration
     * @return
     */
    public static int getMaxWeaponDamage(Weapon weapon, double distance, BaseConfiguration baseConfiguration) {
        return getWeaponTypeMaxDamage(weapon.getWeaponType(), distance) + getWeaponDamageBonus(weapon, baseConfiguration);
    }

    /**
     * Вернуть наименьший урон наносимый оружием weapon на расстоянии distance.
     *
     * @param weapon
     * @param distance
     * @param baseConfiguration
     * @return
     */
    public static int getMinWeaponDamage(Weapon weapon, double distance, BaseConfiguration baseConfiguration) {
        return getWeaponTypeMinDamage(weapon.getWeaponType(), distance) + getWeaponDamageBonus(weapon, baseConfiguration);
    }


    /**
     * Посчитать степень влияния состояния оружия на урон наносимый выстрелом.
     * предполагается:
     * влияние оружия на урон падает с ростом износа(ухудшением состояния)
     * state = 0 -> оружие уменьшает урон на 50%, state = прочность -> оружие уменьшает урон на 0%
     *
     * @param weapon
     * @return
     */
    public static double getWeaponStateInfluence(Weapon weapon) {
        return ((double) (50 + weapon.getState() * 50 / weapon.getFirmness())) / 100.0;
    }

    /**
     * Можно ли выстрилить зарядом projectile из оружия weapon?
     *
     * @param weapon
     * @param projectile
     * @return
     */
    public static boolean mayPut(Weapon weapon, Projectile projectile) {
        ProjectileCollection projectiles = weapon.getWeaponType().getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).getId() == projectile.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Может ли оружие произвести выстрел.
     *
     * @param weapon
     * @return
     */
    public static boolean mayShoot(Weapon weapon) {
        return weapon.getProjectile() != null && weapon.getState() > 0 && weapon.getLoad() - weapon.getWeaponType().getAmmunitionPerShoot() >= 0;
    }

    /**
     * Сложить патроны в оружие и вернуть количество оставшихся на руках патронов.
     *
     * @param weapon
     * @param projectile
     * @param amount
     * @return
     */
    public static int putProjectile(Weapon weapon, Projectile projectile, int amount) {
        Projectile weaponProjectile = weapon.getProjectile();
        if (weaponProjectile == null || weaponProjectile.getId() == projectile.getId()) {
            int rest = 0;
            short capacity = weapon.getWeaponType().getCapacity();
            int room = capacity - weapon.getLoad();
            rest = amount - room;
            weapon.setProjectile(projectile);
            if (rest < 0) {
                weapon.setLoad((short) (capacity + rest));
                return 0;
            } else {
                weapon.setLoad(capacity);
                return rest;
            }
        }
        return amount;
    }
}
