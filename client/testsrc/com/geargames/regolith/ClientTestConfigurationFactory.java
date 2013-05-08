package com.geargames.regolith;

import com.geargames.regolith.map.observer.StrictPerimeterObserver;
import com.geargames.regolith.map.router.RecursiveWaveRouter;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Date: 22.08.12
 * Time: 16:34
 */
public class ClientTestConfigurationFactory {
    public static RegolithConfiguration configuration;

    public static RegolithConfiguration getDefaultConfiguration() {
        if (configuration == null) {
            synchronized (ClientTestConfigurationFactory.class) {
                if (configuration == null) {
                    configuration = new RegolithConfiguration();
                    BattleConfiguration battleConfiguration = new BattleConfiguration();
                    configuration.setBattleConfiguration(battleConfiguration);
                    ClientWarriorCollection collection = new ClientWarriorCollection();
                    collection.setWarriors(new Vector());
                    battleConfiguration.setObserver(new StrictPerimeterObserver(collection));
                    battleConfiguration.setRouter(new RecursiveWaveRouter());
                    battleConfiguration.setActiveTime(100);
                    battleConfiguration.setWalkSpeed(12);
                    battleConfiguration.setWeaponSpoiling(10);
                    battleConfiguration.setArmorSpoiling(4);
                    BaseConfiguration baseConfiguration = new BaseConfiguration();
                    baseConfiguration.setMaxWorkShopLevel((byte) 9);
                    baseConfiguration.setMaxWorkShopProbability((byte) 15);
                    baseConfiguration.setMinWorkShopProbability((byte) 5);
                    baseConfiguration.setPocketsAmount((byte) 10);
                    configuration.setBaseConfiguration(baseConfiguration);
                    battleConfiguration.setAccurateShootFix(1.2);
                    battleConfiguration.setQuickShootFix(0.7);
                    battleConfiguration.setSitHunterShootFix(1.2);
                    battleConfiguration.setStandHunterShootFix(1);
                    battleConfiguration.setSitVictimShootFix(0.8);
                    battleConfiguration.setStandVictimShootFix(1);
                    battleConfiguration.setCriticalBarrierToVictimDistance(66);
                    battleConfiguration.setKillExperienceMultiplier(2);
                    battleConfiguration.setAbilityMax((short) 25);
                }
            }
        }
        return configuration;
    }

}
