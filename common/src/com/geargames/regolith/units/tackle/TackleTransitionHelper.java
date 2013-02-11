package com.geargames.regolith.units.tackle;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.units.*;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.base.StoreHouseHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.dictionaries.AmmunitionPacketCollection;
import com.geargames.regolith.units.dictionaries.ProjectileCollection;

/**
 * User: mikhail v. kutuzov
 * Date: 26.12.12
 * Time: 16:19
 */
public class TackleTransitionHelper {

    /**
     * Переместить расходник из сумки бойца warrior на склад storeHouse
     *
     * @param warrior    боец владелец сумки
     * @param number     номер кармана в котором лежит расходник
     * @param amount     количество расходника для перемещения
     * @param storeHouse склад
     * @return количество расходника положенного на склад.
     */
    public static int moveAmmunitionBag2StoreHouse(Warrior warrior, int number, int amount, StoreHouse storeHouse) {
        AmmunitionBag bag = warrior.getAmmunitionBag();
        AmmunitionPacket packet = bag.getPackets().get(number);
        if (packet != null) {
            amount = AmmunitionBagHelper.putOut(bag, packet.getAmmunition(), amount);
            if (amount != 0) {
                int put = StoreHouseHelper.add(storeHouse, packet.getAmmunition(), amount);
                if (put != amount) {
                    AmmunitionBagHelper.putIn(bag, packet.getAmmunition(), amount - put);
                }
                return put;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * Положить расходник из кармана number сумки бойца warrior в оружие.
     *
     * @param warrior
     * @param number
     * @return количество положенных патронов(может быть равно 0, если патроны не предназначены для этого оружия)
     */
    public static int moveAmmunitionBag2Warrior(Warrior warrior, int number, int amount) {
        Weapon weapon = warrior.getWeapon();
        AmmunitionBag bag = warrior.getAmmunitionBag();
        AmmunitionPacketCollection packets = warrior.getAmmunitionBag().getPackets();
        if (packets.size() > number) {
            AmmunitionPacket packet = packets.get(number);
            switch (packet.getAmmunition().getType()) {
                case TackleType.PROJECTILE:
                    if (weapon != null) {
                        Projectile projectile = (Projectile) packet.getAmmunition();
                        if (projectile.getId() != weapon.getProjectile().getId()) {
                            int room = weapon.getWeaponType().getCapacity() - weapon.getLoad();
                            amount = amount > room ? room : amount;
                            amount = AmmunitionBagHelper.putOut(bag, packet.getAmmunition(), amount);
                            weapon.setLoad((short) (weapon.getLoad() + amount));
                            return amount;
                        } else {
                            if (WeaponHelper.mayPut(warrior.getWeapon(), projectile)) {
                                AmmunitionBagHelper.putIn(bag, weapon.getProjectile(), weapon.getLoad());
                                int room = weapon.getWeaponType().getCapacity();
                                amount = amount > room ? room : amount;
                                amount = AmmunitionBagHelper.putOut(warrior.getAmmunitionBag(), packet.getAmmunition(), amount);
                                weapon.setLoad((short) amount);
                                weapon.setProjectile(projectile);
                                return amount;
                            }
                        }
                    }
                    break;
                case TackleType.MEDIKIT:
                    break;
            }
        }
        return 0;
    }

    /**
     * Надеть кладь с индексом number из сумки бойца warrior.
     *
     * @param warrior боец
     * @param number  номер клади
     * @return надетый предмет
     */
    public static StateTackle moveStateTackleBag2Warrior(Warrior warrior, int number) {
        return WarriorHelper.takeOnFromBag(warrior, number);
    }

    /**
     * Переместить кладь из сумки боца warrior на склад
     *
     * @param warrior боец
     * @param number  позиция в сумке
     * @param house   склад
     * @return предмет если операция прошла успешно
     */
    public static StateTackle moveStateTackleBag2StoreHouse(Warrior warrior, int number, StoreHouse house) {
        Bag bag = warrior.getBag();
        StateTackle tackle = bag.getTackles().get(number);
        if (StoreHouseHelper.add(house, tackle)) {
            BagHelper.putOut(bag, number);
            return tackle;
        }
        return null;
    }

    /**
     * Переместить расходники со склада storeHouse в сумку бойца warrior
     *
     * @param storeHouse        склад
     * @param number            позиция на складе
     * @param amount            количество расходников
     * @param warrior
     * @param baseConfiguration
     * @return  перемещённое количество
     */
    public static int moveAmmunitionStoreHouse2Bag(StoreHouse storeHouse, int number, int amount, Warrior warrior, BaseConfiguration baseConfiguration) {
        AmmunitionPacket packet = StoreHouseHelper.getAmmunition(storeHouse, number);
        amount = packet.getCount() > amount ? amount : packet.getCount();

        int put = WarriorHelper.putInToBag(warrior, packet.getAmmunition(), amount, baseConfiguration);
        if (put != 0) {
            StoreHouseHelper.removeAmmunition(storeHouse, number, put);
        }
        return put;
    }

    /**
     * Переместить расходник под номером number на складе storeHouse в сумку бойца warrior
     *
     * @param storeHouse
     * @param number
     * @param amount  количество перемещаемого расходника
     * @param warrior
     * @param baseConfiguration
     * @return  перемещённое количество
     */
    public static int moveAmmunitionStoreHouse2Warrior(StoreHouse storeHouse, int number, int amount, Warrior warrior, BaseConfiguration baseConfiguration) {
        AmmunitionPacket packet = StoreHouseHelper.getAmmunition(storeHouse, number);
        amount = packet.getCount() > amount ? amount : packet.getCount();
        Ammunition ammunition = packet.getAmmunition();
        switch (ammunition.getType()) {
            case TackleType.PROJECTILE:
                if (warrior.getWeapon() != null) {
                    Weapon weapon = warrior.getWeapon();
                    Projectile weaponProjectile = weapon.getProjectile();
                    Projectile projectile = (Projectile) ammunition;
                    if (WeaponHelper.mayPut(weapon, projectile)) {
                        if (weaponProjectile != null && weaponProjectile.getId() != projectile.getId()) {
                            AmmunitionBagHelper.putIn(warrior.getAmmunitionBag(), weaponProjectile, weapon.getLoad());
                            weapon.setProjectile(null);
                            weapon.setLoad((short) 0);
                        }
                        short rest = (short) WarriorHelper.addProjectileIntoWeapon(warrior, projectile, amount, baseConfiguration);
                        StoreHouseHelper.removeAmmunition(storeHouse, number, amount - rest);
                        return amount - rest;
                    }
                }
                break;
        }
        return 0;
    }

    /**
     * Переместить предмет со склада storeHouse под номером number в сумку бойца warrior
     *
     * @param storeHouse
     * @param number
     * @param warrior
     * @param baseConfiguration
     * @return предмет если операция прошла успешно
     */
    public static StateTackle moveStateTackleStoreHouse2Bag(StoreHouse storeHouse, int number, Warrior warrior, BaseConfiguration baseConfiguration) {
        StateTackle tackle = StoreHouseHelper.getStateTackle(storeHouse, number);
        if (tackle != null) {
            if (WarriorHelper.putInToBag(warrior, tackle, baseConfiguration)) {
                StoreHouseHelper.removeStateTackle(storeHouse, number);
                return tackle;
            }
        }
        return null;
    }

    /**
     * Переместить кладь с номером number со склада и надеть на бойца warrior
     *
     * @param storeHouse
     * @param number
     * @param warrior
     * @param baseConfiguration
     * @return предмет если операция прошла успешно
     */
    public static StateTackle moveStateTackleStoreHouse2Warrior(StoreHouse storeHouse, int number, Warrior warrior, BaseConfiguration baseConfiguration) {
        StateTackle tackle = StoreHouseHelper.getStateTackle(storeHouse, number);
        if (tackle != null) {
            if (WarriorHelper.takeOn(warrior, tackle, baseConfiguration)) {
                StoreHouseHelper.removeStateTackle(storeHouse, number);
                return tackle;
            }
        }
        return null;
    }

    /**
     * Найти по id клади кладь надетую на бойца.
     * @param warrior
     * @param tackleId
     * @return
     */
    public static StateTackle findTackleOnWarrior(Warrior warrior, int tackleId){
        if(warrior.getHeadArmor().getId() == tackleId){
            return warrior.getHeadArmor();
        } else if(warrior.getTorsoArmor().getId() == tackleId) {
            return warrior.getTorsoArmor();
        } else if (warrior.getLegsArmor().getId() == tackleId) {
            return warrior.getLegsArmor();
        } else if (warrior.getWeapon().getId() == tackleId) {
            return warrior.getWeapon();
        }
        return null;
    }

    /**
     * Снять кладь tackle с бойца warrior в его сумку.
     *
     * @param warrior
     * @param tackle
     */
    public static void moveStateTackleWarrior2Bag(Warrior warrior, StateTackle tackle) {
        Bag bag = warrior.getBag();
        switch (tackle.getType()) {
            case TackleType.ARMOR:
                switch (((Armor) tackle).getArmorType().getBodyParticle()) {
                    case BodyParticles.HEAD:
                        warrior.setHeadArmor(null);
                        break;
                    case BodyParticles.TORSO:
                        warrior.setTorsoArmor(null);
                        break;
                    case BodyParticles.LEGS:
                        warrior.setLegsArmor(null);
                        break;
                }
                break;
            case TackleType.WEAPON:
                warrior.setWeapon(null);
                break;
            default:
                return;
        }
        BagHelper.putIntoBag(bag, tackle);
    }

    /**
     * Снять кладь tackle с бойца warrior и пололжить ее на склад storeHouse
     * @param warrior
     * @param tackle
     * @param storeHouse
     * @return true если операция прошла успешно
     */
    public static boolean moveStateTackleWarrior2StoreHouse(Warrior warrior, StateTackle tackle, StoreHouse storeHouse){
        switch (tackle.getType()) {
            case TackleType.ARMOR:
                Armor armor = (Armor) tackle;
                if (StoreHouseHelper.add(storeHouse, armor)) {
                    switch (armor.getArmorType().getBodyParticle()) {
                        case BodyParticles.HEAD:
                            warrior.setHeadArmor(null);
                            return true;
                        case BodyParticles.TORSO:
                            warrior.setTorsoArmor(null);
                            return true;
                        case BodyParticles.LEGS:
                            warrior.setLegsArmor(null);
                            return true;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
            case TackleType.WEAPON:
                if (StoreHouseHelper.add(storeHouse, tackle)) {
                    warrior.setWeapon(null);
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }
}