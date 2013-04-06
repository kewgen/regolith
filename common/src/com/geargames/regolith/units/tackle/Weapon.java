package com.geargames.regolith.units.tackle;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.units.ElementTypes;

/**
 * User: mkutuzov
 * Date: 08.02.12
 */
public class Weapon extends StateTackle {
    private short load;
    private Projectile projectile;
    private WeaponType weaponType;

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public int getFrameId() {
        return weaponType.getFrameId();
    }

    public String getName() {
        return weaponType.getName();
    }

    public short getWeight() {
        return (short) (weaponType.getWeight() + (getProjectile() != null ? getProjectile().getWeight() * getLoad() : 0));
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public short getLoad() {
        return load;
    }

    public void setLoad(short load) {
        this.load = load;
    }

    public void onShoot(BattleConfiguration battleConfiguration) {
        setState((short) (getState() - battleConfiguration.getWeaponSpoiling()));
        setLoad((short) (getLoad() - weaponType.getAmmunitionPerShoot()));
    }

    public int getType() {
        return TackleType.WEAPON;
    }

    @Override
    public short getElementType() {
        return ElementTypes.WEAPON;
    }

}
