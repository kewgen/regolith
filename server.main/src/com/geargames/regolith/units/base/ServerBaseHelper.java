package com.geargames.regolith.units.base;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.helpers.ServerHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerStateTackleCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.Weapon;

import java.util.*;

/**
 * User: mkutuzov
 * Date: 26.02.12
 */
public class ServerBaseHelper {
    private static Timer upgrader = new Timer(false);
    private static Map<WorkShop, Random> UPGRADE_RANDOM_GENERATORS = new HashMap<WorkShop, Random>();

    public static StoreHouse createStoreHouse(BaseConfiguration baseConfiguration){
        StoreHouse house = new StoreHouse();
        Bag bag = new Bag();
        bag.setWeight((short)0);
        bag.setTackles(new ServerStateTackleCollection(new LinkedList<StateTackle>()));
        AmmunitionBag ammunitionBag = ServerHelper.createAmmunitionBag(baseConfiguration);
        ammunitionBag.setWeight(0);
        house.setBag(bag);
        house.setAmmunitionBag(ammunitionBag);

        return house;
    }

    /**
     * Починить кладь состояние которой ухудшилась.
     *
     * @param workShop
     * @param tackle
     */
    public static void fix(WorkShop workShop, StateTackle tackle, RegolithConfiguration regolithConfiguration) {
        int amount = regolithConfiguration.getBaseConfiguration().getMaxWorkShopLevel() + 1;
        tackle.setFirmness((short) (tackle.getFirmness() - ((tackle.getFirmness() - tackle.getState()) * (amount - workShop.getLevel())) / 100));
        tackle.setState(tackle.getFirmness());
    }

    /**
     * Время которое мастерская workShop потратит на улучшение предмета.
     *
     * @param workShop
     * @return
     */
    public static int getUpgradeTime(WorkShop workShop) {
        return workShop.getLevel() * 1000 * 10;
    }

    /**
     * Может ли мастерская workShop проводить улучшение предмета workShop.
     *
     * @param workShop
     * @param tackle
     * @return
     */
    public static boolean isAbleToUpgrade(WorkShop workShop, StateTackle tackle) {
        return workShop.getLevel() > tackle.getUpgrade();
    }

    private static Random getRandom(WorkShop workShop) {
        if (!UPGRADE_RANDOM_GENERATORS.containsKey(workShop)) {
            UPGRADE_RANDOM_GENERATORS.put(workShop, new Random());
        }
        return UPGRADE_RANDOM_GENERATORS.get(workShop);
    }

    /**
     * Вернуть следующий уровень предмета.
     * В зависимости от уровня мастерской предмет может быть испорчен до начального уровня.
     *
     * @param workShop
     * @param level
     * @return
     */
    public static byte getNextLevel(WorkShop workShop, byte level) {
        int random = getRandom(workShop).nextInt(100);
        BaseConfiguration configuration = MainServerConfigurationFactory.getConfiguration().getRegolithConfiguration().getBaseConfiguration();
        double k = (double) (configuration.getMaxWorkShopLevel() - configuration.getMinWorkShopProbability()) / (double) configuration.getMaxWorkShopLevel();
        byte probability = (byte) (-k * workShop.getLevel() + configuration.getMaxWorkShopProbability());

        if (random < probability) {
            level = 1;
        } else {
            level++;
        }
        return level;
    }

    /**
     * Улучшить предмет до следующего уровня.
     * Улучшение может "сорваться" и мы получим в итоге оружие первого уровня.
     *
     * @param workShop
     * @param tackle
     */
    private static void upgrade(final WorkShop workShop, final StateTackle tackle) {
        if (!isAbleToUpgrade(workShop, tackle)) {
            return;
        }
        byte level = getNextLevel(workShop, tackle.getUpgrade());
        tackle.setUpgrade(level);
        short firmness = 0;
        if (tackle instanceof Armor) {
            firmness = ((Armor) tackle).getArmorType().getBaseFirmness();
        } else if (tackle instanceof Weapon) {
            firmness = ((Weapon) tackle).getWeaponType().getBaseFirmness();
        }
        for (int i = 0; i < level; i++) {
            firmness += 1.15 * firmness;
        }
        tackle.setFirmness(firmness);
        tackle.setState(firmness);
    }

    /**
     * Улучшаем предмет tackle в мастерской workShop за деньги amount.
     * За деньги улучшение занимает некоторое время(в зависимости от уровня мастерской).
     *
     * @param workShop
     * @param tackle
     * @param amount
     */
    public static void upgradeForMoney(final WorkShop workShop, final StateTackle tackle, final int amount) {
        upgrader.schedule(new TimerTask() {
            public void run() {
                upgrade(workShop, tackle);
            }
        }, getUpgradeTime(workShop));
    }

    /**
     * Улучшаем предмет tackle в мастерской workShop за реголит amount.
     *
     * @param workShop
     * @param tackle
     * @param amount
     */
    public static void upgradeForRegolith(WorkShop workShop, StateTackle tackle, int amount) {
        upgrade(workShop, tackle);
    }

    /**
     * Подлечить бойцов в больице.
     * @param hospital
     * @param configuration
     */
    public static void remedyAccount(Hospital hospital, BaseConfiguration configuration) {
        int remedy = hospital.getLevel();
        for (Warrior warrior : ((ServerWarriorCollection)hospital.getWarriors()).getWarriors()) {
            short max = WarriorHelper.getMaxHealth(warrior, configuration);
            if (warrior.getHealth() < max) {
                warrior.setHealth(warrior.getHealth() + remedy);
                if (max > warrior.getHealth()) {
                    warrior.setHealth(max);
                }
            }
        }
    }

}
