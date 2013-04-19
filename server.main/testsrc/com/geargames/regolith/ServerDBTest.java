package com.geargames.regolith;

import com.geargames.regolith.helpers.*;
import com.geargames.regolith.units.*;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.ExitZone;
import com.geargames.regolith.units.tackle.*;
import junit.framework.Assert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Users: mkutuzov, abarakov
 * Date: 05.06.12
 */
public class ServerDBTest {
    private static SessionFactory sessionFactory;

    @BeforeClass
    public static void before() {
        Skill[] skills = new Skill[20];
        skills[0] = new Skill();
        skills[0].setAction((byte) 1);
        skills[0].setExperience((short) 5);
        skills[1] = new Skill();
        skills[1].setAction((byte) 2);
        skills[1].setExperience((short) 10);
        skills[2] = new Skill();
        skills[2].setAction((byte) 3);
        skills[2].setExperience((short) 40);
        skills[3] = new Skill();
        skills[3].setAction((byte) 4);
        skills[3].setExperience((short) 80);
        skills[4] = new Skill();
        skills[4].setAction((byte) 5);
        skills[4].setExperience((short) 140);
        skills[5] = new Skill();
        skills[5].setAction((byte) 6);
        skills[5].setExperience((short) 240);
        skills[6] = new Skill();
        skills[6].setAction((byte) 7);
        skills[6].setExperience((short) 400);
        skills[7] = new Skill();
        skills[7].setAction((byte) 8);
        skills[7].setExperience((short) 640);
        skills[8] = new Skill();
        skills[8].setAction((byte) 9);
        skills[8].setExperience((short) 1040);
        skills[9] = new Skill();
        skills[9].setAction((byte) 10);
        skills[9].setExperience((short) 1680);
        skills[10] = new Skill();
        skills[10].setAction((byte) 11);
        skills[10].setExperience((short) 2720);
        skills[11] = new Skill();
        skills[11].setAction((byte) 12);
        skills[11].setExperience((short) 4400);
        skills[12] = new Skill();
        skills[12].setAction((byte) 13);
        skills[12].setExperience((short) 7000);
        skills[13] = new Skill();
        skills[13].setAction((byte) 14);
        skills[13].setExperience((short) 11600);
        skills[14] = new Skill();
        skills[14].setAction((byte) 15);
        skills[14].setExperience((short) 18600);
        skills[15] = new Skill();
        skills[15].setAction((byte) 16);
        skills[15].setExperience((short) 30000);
        skills[16] = new Skill();
        skills[16].setAction((byte) 17);
        skills[16].setExperience((short) 50000);
        skills[17] = new Skill();
        skills[17].setAction((byte) 18);
        skills[17].setExperience((short) 80000);
        skills[18] = new Skill();
        skills[18].setAction((byte) 20);
        skills[18].setExperience((short) 130000);
        skills[19] = new Skill();
        skills[19].setAction((byte) 25);
        skills[19].setExperience((short) 200000);

        ServerRankCollection ranks = new ServerRankCollection();
        ranks.setRanks(new LinkedList<Rank>());

        Rank rank = new Rank();
        rank.setName("РЕКРУТ");
        ranks.add(rank);
        rank = new Rank();
        rank.setName("СТАЖЕР");
        rank.setExperience((short) 50);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("РЯДОВОЙ");
        rank.setExperience((short) 200);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("ЕФРЕЙТОР");
        rank.setExperience((short) 400);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("МЛАДШИЙ СЕРЖАНТ");
        rank.setExperience((short) 700);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("СЕРЖАНТ");
        rank.setExperience((short) 1200);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("СТАРШИЙ СЕРЖАНТ");
        rank.setExperience((short) 2000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("СТАРШИНА");
        rank.setExperience((short) 3200);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("МЛАДШИЙ ЛЕЙТЕНАНТ");
        rank.setExperience((short) 5200);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("ЛЕЙТЕНАНТ");
        rank.setExperience((short) 8400);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("СТАРШИЙ ЛЕЙТЕНАНТ");
        rank.setExperience((short) 13600);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("КАПИТАН");
        rank.setExperience((short) 22000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("МАЙОР");
        rank.setExperience((short) 35000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("ПОДПОЛКОВНИК");
        rank.setExperience((short) 58000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("ПОЛКОВНИК");
        rank.setExperience((short) 93000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("ГЕНЕРАЛ-МАЙОР");
        rank.setExperience((short) 150000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("ГЕНЕРАЛ-ЛЕЙТЕНАНТ");
        rank.setExperience((short) 250000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("ГЕНЕРАЛ-ПОЛКОВНИК");
        rank.setExperience((short) 400000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("ГЕНЕРАЛ-АРМИИ");
        rank.setExperience((short) 650000);
        ranks.add(rank);
        rank = new Rank();
        rank.setName("МАРШАЛ");
        rank.setExperience((short) 1000000);
        ranks.add(rank);

        BaseConfiguration baseConfiguration;
        baseConfiguration = new BaseConfiguration();
        baseConfiguration.setPocketsAmount((byte) 10);
        baseConfiguration.setRanks(ranks);
        baseConfiguration.setBaseActionScore((byte) 20);
        baseConfiguration.setMaxDamage((byte) 10);
        baseConfiguration.setMaxDistance((byte) 15);
        baseConfiguration.setMaxWeight((byte) 100);

        ServerSkillCollection skillCollection = new ServerSkillCollection();
        skillCollection.setSkills(new LinkedList<Skill>());
        for (Skill s : skills) {
            skillCollection.add(s);
        }
        baseConfiguration.setSkills(skillCollection);
        baseConfiguration.setMaxWorkShopLevel((byte) 10);


        ServerWeaponCategoryCollection weaponCategories;

        weaponCategories = new ServerWeaponCategoryCollection();
        weaponCategories.setCategories(new LinkedList<WeaponCategory>());

        WeaponCategory weaponCategory = new WeaponCategory();
        weaponCategory.setPackerId(0);
        weaponCategory.setName("ХОЛОДНОЕ");
        ServerWeaponTypeCollection blades = new ServerWeaponTypeCollection();
        blades.setWeaponTypes(new LinkedList<WeaponType>());
        weaponCategory.setWeaponTypes(blades);

        weaponCategories.add(weaponCategory);

        WeaponType weaponType = new WeaponType();
        weaponType.setName("НОЖ");
        ServerProjectileCollection bladeProjectiles = new ServerProjectileCollection();
        bladeProjectiles.setProjectiles(new LinkedList<Projectile>());
        weaponType.setProjectiles(bladeProjectiles);
        blades.add(weaponType);
        weaponType.setCategory(weaponCategory);
        weaponCategory.getWeaponTypes().add(weaponType);

        weaponType.setAccuracy((byte) 100);
        weaponType.setAccurateAction((byte) 1);
        weaponType.setAmmunitionPerShoot((byte) 0);
        weaponType.setBaseFirmness((byte) 50);
        weaponType.setFrameId(75);
        weaponType.setCapacity((short) 1);
        weaponType.setWeight((short) 1000);
        WeaponDistances distances = new WeaponDistances();
        distances.setMax((byte) 1);
        distances.setMin((byte) 0);
        distances.setMaxOptimal((byte) 1);
        distances.setMinOptimal((byte) 0);
        weaponType.setDistance(distances);
        WeaponDamage max = new WeaponDamage();
        WeaponDamage min = new WeaponDamage();
        weaponType.setMaxDamage(max);
        weaponType.setMinDamage(min);
        max.setMaxDistance((short) 10);
        max.setMinDistance((short) 10);
        max.setOptDistance((short) 10);
        min.setMaxDistance((short) 5);
        min.setMinDistance((short) 5);
        min.setOptDistance((short) 5);
        weaponType.setCriticalDamage((short) 10);
        weaponType.setMinSkill(skills[0]);


        weaponCategory = new WeaponCategory();
        weaponCategory.setPackerId(0);
        weaponCategories.add(weaponCategory);
        weaponCategory.setName("МЕТАТЕЛЬНЫЕ");
        ServerWeaponTypeCollection thrown = new ServerWeaponTypeCollection();
        thrown.setWeaponTypes(new LinkedList<WeaponType>());

        weaponCategory = new WeaponCategory();
        weaponCategory.setPackerId(0);
        weaponCategories.add(weaponCategory);
        weaponCategory.setName("ЛЕГКОЕ");
        ServerWeaponTypeCollection light = new ServerWeaponTypeCollection();
        light.setWeaponTypes(new LinkedList<WeaponType>());

        weaponCategory = new WeaponCategory();
        weaponCategory.setPackerId(0);
        weaponCategories.add(weaponCategory);
        weaponCategory.setName("ТЯЖЕЛОЕ");
        ServerWeaponTypeCollection heavy = new ServerWeaponTypeCollection();
        heavy.setWeaponTypes(new LinkedList<WeaponType>());


        weaponCategory = new WeaponCategory();
        weaponCategory.setPackerId(0);
        weaponCategories.add(weaponCategory);
        weaponCategory.setName("ДАЛЬНОБОЙНОЕ");

        ServerWeaponTypeCollection firearms = new ServerWeaponTypeCollection();
        firearms.setWeaponTypes(new LinkedList<WeaponType>());

        weaponCategory.setWeaponTypes(firearms);
        weaponType = new WeaponType();
        weaponType.setName("ВИНТОВКА");
        weaponType.setCategory(weaponCategory);
        weaponType.setFrameId(149);
        firearms.add(weaponType);
        weaponType.setAccuracy((byte) 60);
        weaponType.setAccurateAction((byte) 2);
        weaponType.setQuickAction((byte) 1);
        weaponType.setAmmunitionPerShoot((byte) 1);
        weaponType.setBaseFirmness((byte) 50);
        weaponType.setCapacity((short) 1);

        distances = new WeaponDistances();

        distances.setMax((byte) 100);
        distances.setMin((byte) 10);
        distances.setMaxOptimal((byte) 90);
        distances.setMinOptimal((byte) 20);

        weaponType.setDistance(distances);
        weaponType.setMinSkill(skills[0]);
        weaponType.setCriticalDamage((short) 10);
        max = new WeaponDamage();
        min = new WeaponDamage();
        max.setMaxDistance((short) 10);
        max.setMinDistance((short) 10);
        max.setOptDistance((short) 50);
        min.setMaxDistance((short) 5);
        min.setMinDistance((short) 5);
        min.setOptDistance((short) 25);
        weaponType.setMaxDamage(max);
        weaponType.setMinDamage(min);
        weaponType.setWeight((short) 2000);

        ServerProjectileCollection projectiles = new ServerProjectileCollection();
        projectiles.setProjectiles(new LinkedList<Projectile>());

        ServerWeaponTypeCollection projectileWeaponTypes = new ServerWeaponTypeCollection();
        projectileWeaponTypes.setWeaponTypes(new LinkedList<WeaponType>());
        projectileWeaponTypes.add(firearms.get(0));
        Projectile projectile = new Projectile();
        projectile.setName("ПУЛЯ");
        projectile.setWeaponTypes(projectileWeaponTypes);
        projectile.setWeight((short) 10);
        projectile.setFrameId(145);
        projectiles.add(projectile);

        projectile = new Projectile();
        projectile.setName("ТЯЖЕЛАЯ ПУЛЯ");
        ServerWeaponTypeCollection projectileWeaponTypes1 = new ServerWeaponTypeCollection();
        projectileWeaponTypes1.setWeaponTypes(new LinkedList<WeaponType>());
        projectileWeaponTypes1.getWeaponTypes().addAll(projectileWeaponTypes.getWeaponTypes());

        projectile.setWeaponTypes(projectileWeaponTypes1);
        projectile.setWeight((short) 20);
        projectile.setFrameId(145);
        projectiles.add(projectile);
        ServerProjectileCollection firearmProjectiles = new ServerProjectileCollection();
        firearmProjectiles.setProjectiles(new LinkedList<Projectile>());
        firearmProjectiles.getProjectiles().addAll(projectiles.getProjectiles());
        firearms.get(0).setProjectiles(firearmProjectiles);

        projectile = new Projectile();
        projectile.setName("NULL");
        ServerWeaponTypeCollection projectileWeaponTypes2 = new ServerWeaponTypeCollection();
        projectileWeaponTypes2.setWeaponTypes(new LinkedList<WeaponType>());
        projectileWeaponTypes2.add(blades.get(0));
        projectile.setWeaponTypes(projectileWeaponTypes2);
        projectile.setWeight((short) 0);
        projectile.setFrameId(145);
        projectiles.add(projectile);
        blades.get(0).getProjectiles().add(projectile);

        ServerAmmunitionCategoryCollection ammunitionCategoryCollection = new ServerAmmunitionCategoryCollection();
        ammunitionCategoryCollection.setCategories(new LinkedList<AmmunitionCategory>());

        AmmunitionCategory ammunitionCategory = new AmmunitionCategory();
        ammunitionCategory.setName("ПРОСРОЧЕННЫЕ");
        ammunitionCategory.setQuality(0.8);
        ammunitionCategoryCollection.add(ammunitionCategory);
        projectiles.get(0).setCategory(ammunitionCategory);
        projectiles.get(1).setCategory(ammunitionCategory);

        ammunitionCategory = new AmmunitionCategory();
        ammunitionCategory.setName("СТАНДАРТНЫЕ");
        ammunitionCategory.setQuality(1.0);
        ammunitionCategoryCollection.add(ammunitionCategory);
        projectiles.get(2).setCategory(ammunitionCategory);
        baseConfiguration.setWeaponCategories(weaponCategories);

        ammunitionCategory = new AmmunitionCategory();
        ammunitionCategory.setName("УСИЛЕННЫЕ");
        ammunitionCategory.setQuality(1.05);
        ammunitionCategoryCollection.add(ammunitionCategory);
        ammunitionCategory = new AmmunitionCategory();
        ammunitionCategory.setName("СПЕЦИАЛЬНЫЕ");
        ammunitionCategory.setQuality(1.1);
        ammunitionCategoryCollection.add(ammunitionCategory);
        ammunitionCategory = new AmmunitionCategory();
        ammunitionCategory.setName("ПРЕМИУМ");
        ammunitionCategory.setQuality(1.2);
        ammunitionCategoryCollection.add(ammunitionCategory);

        baseConfiguration.setAmmunitionCategories(ammunitionCategoryCollection);

        weaponCategory = new WeaponCategory();
        weaponCategory.setPackerId(0);
        weaponCategories.add(weaponCategory);
        weaponCategory.setName("АВТОМАТИЧЕСКОЕ");
        ServerWeaponTypeCollection automatic = new ServerWeaponTypeCollection();
        automatic.setWeaponTypes(new LinkedList<WeaponType>());

        ServerArmorTypeCollection armorTypes = new ServerArmorTypeCollection();
        armorTypes.setArmorTypes(new LinkedList<ArmorType>());

        ArmorType armorType = new ArmorType();
        armorType.setName("БРОНЯ ЛEГКАЯ");
        armorType.setBodyParticle((byte) 1);
        armorType.setArmor((byte) 90);
        armorType.setWeight((short) 10);
        armorType.setFrameId(2);
        armorType.setBaseFirmness((short) 100);
        armorTypes.add(armorType);

        armorType = new ArmorType();
        armorType.setName("БРОНЯ СРЕДНЯЯ");
        armorType.setBodyParticle((byte) 1);
        armorType.setArmor((byte) 100);
        armorType.setWeight((short) 20);
        armorType.setFrameId(3);
        armorType.setBaseFirmness((short) 100);
        armorTypes.add(armorType);

        armorType = new ArmorType();
        armorType.setName("БРОНЯ ТЯЖEЛАЯ");
        armorType.setBodyParticle((byte) 1);
        armorType.setArmor((byte) 110);
        armorType.setWeight((short) 25);
        armorType.setFrameId(4);
        armorType.setBaseFirmness((short) 100);
        armorTypes.add(armorType);
        baseConfiguration.setArmorTypes(armorTypes);

        baseConfiguration.setProjectiles(projectiles);
        baseConfiguration.setRevision((short) 1);
        baseConfiguration.setMaxWorkShopLevel((byte) 10);
        baseConfiguration.setVitalityStep((byte) 2);
        baseConfiguration.setBaseHealth((short) 50);
        baseConfiguration.setBaseStrength((short) 20000);

        ServerBattleTypeCollection battleTypes = new ServerBattleTypeCollection();
        battleTypes.setBattleTypes(new LinkedList<BattleType>());

        BattleType battleType1x1 = new TrainingBattle();
        battleType1x1.setName("1x1");
        battleType1x1.setScores((byte) 5);
        battleType1x1.setAllianceAmount((short)2);
        battleType1x1.setAllianceSize((short)1);
        battleType1x1.setGroupSize((short)1);
        battleTypes.add(battleType1x1);

        BattleType battleType1x1x1 = new TrainingBattle();
        battleType1x1x1.setName("1x1x1");
        battleType1x1x1.setScores((byte) 10);
        battleType1x1x1.setAllianceAmount((short)3);
        battleType1x1x1.setAllianceSize((short)1);
        battleType1x1x1.setGroupSize((short)1);
        battleTypes.add(battleType1x1x1);

        BattleType battleType1x1x1x1 = new TrainingBattle();
        battleType1x1x1x1.setName("1x1x1x1");
        battleType1x1x1x1.setScores((byte) 10);
        battleType1x1x1x1.setAllianceAmount((short)4);
        battleType1x1x1x1.setAllianceSize((short)1);
        battleType1x1x1x1.setGroupSize((short)1);
        battleTypes.add(battleType1x1x1x1);

        BattleType battleType2x2 = new TrainingBattle();
        battleType2x2.setName("2x2");
        battleType2x2.setScores((byte) 15);
        battleType2x2.setAllianceAmount((short)2);
        battleType2x2.setAllianceSize((short)2);
        battleType2x2.setGroupSize((short)1);
        battleTypes.add(battleType2x2);

        BattleType battleType1x1_2 = new TrainingBattle();
        battleType1x1_2.setName("1x1");
        battleType1x1_2.setScores((byte) 5);
        battleType1x1_2.setAllianceAmount((short)2);
        battleType1x1_2.setAllianceSize((short)1);
        battleType1x1_2.setGroupSize((short)2);
        battleTypes.add(battleType1x1_2);

        baseConfiguration.setBattleTypes(battleTypes);

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration = configuration.configure("hibernate.cfg.xml");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        sessionFactory = configuration.buildSessionFactory();

        BattleConfiguration battleConfiguration = new BattleConfiguration();

        SubordinationDamage[] damages = new SubordinationDamage[7];
        battleConfiguration.setSubordinationDamage(damages);

        damages[0] = new SubordinationDamage();
        damages[0].setDamage((short) 40);
        damages[0].setMaxRankDifference((byte) -4);
        damages[0].setMinRankDifference((byte) -20);
        damages[1] = new SubordinationDamage();
        damages[1].setDamage((short) 20);
        damages[1].setMaxRankDifference((byte) -3);
        damages[1].setMinRankDifference((byte) -4);
        damages[2] = new SubordinationDamage();
        damages[2].setDamage((short) 15);
        damages[2].setMaxRankDifference((byte) -2);
        damages[2].setMinRankDifference((byte) -3);
        damages[3] = new SubordinationDamage();
        damages[3].setDamage((short) 10);
        damages[3].setMaxRankDifference((byte) 3);
        damages[3].setMinRankDifference((byte) -2);
        damages[4] = new SubordinationDamage();
        damages[4].setDamage((short) 9);
        damages[4].setMaxRankDifference((byte) 4);
        damages[4].setMinRankDifference((byte) 3);
        damages[5] = new SubordinationDamage();
        damages[5].setDamage((short) 8);
        damages[5].setMaxRankDifference((byte) 5);
        damages[5].setMinRankDifference((byte) 4);
        damages[6] = new SubordinationDamage();
        damages[6].setDamage((short) 7);
        damages[6].setMaxRankDifference((byte) 21);
        damages[6].setMinRankDifference((byte) 5);

        Underload[] underloads = new Underload[4];
        battleConfiguration.setUnderloads(underloads);

        underloads[0] = new Underload();
        underloads[0].setAction((byte) 2);
        underloads[0].setMax((byte) -10);
        underloads[0].setMin((byte) -7);

        underloads[1] = new Underload();
        underloads[1].setAction((byte) 4);
        underloads[1].setMax((byte) -3);
        underloads[1].setMin((byte) -6);

        underloads[2] = new Underload();
        underloads[2].setAction((byte) 8);
        underloads[2].setMax((byte) -1);
        underloads[2].setMin((byte) -2);

        underloads[3] = new Underload();
        underloads[3].setAction((byte) 16);
        underloads[3].setMax((byte) 0);
        underloads[3].setMin((byte) 0);

        battleConfiguration.setRevision((short) 1);
        battleConfiguration.setAbilityMax(skills[skills.length - 1].getAction());

        ActionFees fees = new ActionFees();
        fees.setChangeWeapon((byte)1);
        fees.setMove((byte)1);
        fees.setPickupTackle((byte)1);
        fees.setRechargeWeapon((byte)1);
        fees.setShutOrOpen((byte)1);
        fees.setSitOrStand((byte)1);

        battleConfiguration.setActionFees(fees);

        Warrior[] baseWarriors = new Warrior[2];

        List<StateTackle> tackles = new LinkedList<StateTackle>();

        Bag bag1 = new Bag();
        bag1.setTackles(new ServerStateTackleCollection(new LinkedList<StateTackle>()));
        AmmunitionBag ammunitionBag1 = ServerHelper.createAmmunitionBag(baseConfiguration);
        Warrior warrior1 = WarriorHelper.createWarrior(baseConfiguration, bag1, ammunitionBag1, "ВОИН1", 12);
        warrior1.setRank(baseConfiguration.getRanks().get(0));
        warrior1.setFrameId(49);
        warrior1.setVitality((byte) 50);
        warrior1.setHealth(20);
        warrior1.setStrength((short) 10000);

        Armor armor1 = new Armor();
        tackles.add(armor1);
        ArmorType armorType1 = baseConfiguration.getArmorTypes().get(0);
        armor1.setArmorType(armorType1);

        armor1.setState(armorType1.getBaseFirmness());
        armor1.setFirmness(armorType1.getBaseFirmness());

        warrior1.setTorsoArmor(armor1);

        Weapon rifle1 = new Weapon();
        tackles.add(rifle1);
        rifle1.setProjectile(baseConfiguration.getProjectiles().get(0));
        WeaponType weaponType1 = firearms.get(0);
        rifle1.setWeaponType(weaponType1);
        rifle1.setFirmness(weaponType1.getBaseFirmness());
        rifle1.setState(weaponType1.getBaseFirmness());
        rifle1.setLoad((short) 2);
        warrior1.setWeapon(rifle1);

        short[] skillScores = warrior1.getSkillScores();

        for (int i = 0; i < skillScores.length; i++) {
            skillScores[i] = (short) (i * 2);
        }

        WarriorHelper.putInToBag(warrior1, baseConfiguration.getProjectiles().get(0), 300, baseConfiguration);

        rifle1 = new Weapon();
        tackles.add(rifle1);
        rifle1.setProjectile(baseConfiguration.getProjectiles().get(0));
        weaponType1 = firearms.get(0);
        rifle1.setWeaponType(weaponType1);
        rifle1.setFirmness(weaponType1.getBaseFirmness());
        rifle1.setState(weaponType1.getBaseFirmness());
        rifle1.setLoad((short) 1);
        WarriorHelper.putInToBag(warrior1, rifle1, baseConfiguration);

        rifle1 = new Weapon();
        tackles.add(rifle1);
        rifle1.setProjectile(baseConfiguration.getProjectiles().get(0));
        weaponType1 = firearms.get(0);
        rifle1.setWeaponType(weaponType1);
        rifle1.setFirmness(weaponType1.getBaseFirmness());
        rifle1.setState(weaponType1.getBaseFirmness());
        rifle1.setLoad((short) 0);
        WarriorHelper.putInToBag(warrior1, rifle1, baseConfiguration);

        rifle1 = new Weapon();
        tackles.add(rifle1);
        rifle1.setProjectile(baseConfiguration.getProjectiles().get(0));
        weaponType1 = firearms.get(0);
        rifle1.setWeaponType(weaponType1);
        rifle1.setFirmness(weaponType1.getBaseFirmness());
        rifle1.setState((short) (weaponType1.getBaseFirmness() / 2));
        rifle1.setLoad((short) 1);
        WarriorHelper.putInToBag(warrior1, rifle1, baseConfiguration);

        baseWarriors[0] = warrior1;

        Bag bag2 = new Bag();
        bag2.setTackles(new ServerStateTackleCollection(new LinkedList<StateTackle>()));
        AmmunitionBag ammunitionBag2 = ServerHelper.createAmmunitionBag(baseConfiguration);
        Warrior warrior2 = WarriorHelper.createWarrior(baseConfiguration, bag2, ammunitionBag2, "ВОИН2", 12);
        warrior2.setRank(baseConfiguration.getRanks().get(1));
        warrior2.setFrameId(50);
        warrior2.setVitality((byte) 50);
        warrior2.setHealth(30);
        warrior2.setStrength((short) 15000);

        Armor armor2 = new Armor();
        tackles.add(armor2);
        armor2.setArmorType(baseConfiguration.getArmorTypes().get(0));

        armor2.setState((short) 100);
        armor2.setFirmness((short) 100);

        warrior2.setTorsoArmor(armor2);

        Weapon rifle2 = new Weapon();
        tackles.add(rifle2);
        rifle2.setProjectile(baseConfiguration.getProjectiles().get(0));
        rifle2.setWeaponType(firearms.getWeaponTypes().get(0));

        warrior2.setWeapon(rifle2);

        skillScores = warrior2.getSkillScores();

        for (int i = 0; i < skillScores.length; i++) {
            skillScores[i] = (short) (i * 3);
        }

        Weapon machineGun = new Weapon();
        tackles.add(machineGun);
        machineGun.setProjectile(baseConfiguration.getProjectiles().get(0));
        machineGun.setWeaponType(firearms.get(0));
        machineGun.setFirmness(firearms.get(0).getBaseFirmness());
        machineGun.setState((short) (firearms.get(0).getBaseFirmness() / 2));
        machineGun.setLoad((short) 112);

        WarriorHelper.putInToBag(warrior2, machineGun, baseConfiguration);
        WarriorHelper.putInToBag(warrior2, baseConfiguration.getProjectiles().get(0), 100, baseConfiguration);
        WarriorHelper.putInToBag(warrior2, baseConfiguration.getProjectiles().get(1), 200, baseConfiguration);
        baseWarriors[1] = warrior2;

        baseConfiguration.setInitWarriorsAmount((byte) 2);

        ServerBorder border = new ServerBorder();
        border.setShootThrough(new HashMap<WeaponCategory, Boolean>());
        border.setAbleToLookThrough(true);
        for (int i = 0; i < weaponCategories.size(); i++) {
            border.setAbleToShootThrough(baseConfiguration.getWeaponCategories().get(i), i % 2 == 0);
        }

        border.setAbleToWalkThrough(false);
        border.setHalfLong(true);

        ServerBorderCollection borders = new ServerBorderCollection();
        borders.setBorders(new LinkedList<Border>());
        borders.add(border);
        baseConfiguration.setBorders(borders);

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        for (int i = 0; i < ammunitionCategoryCollection.size(); i++) {
            session.save(ammunitionCategoryCollection.get(i));
        }

        session.save(battleConfiguration);
        session.save(baseConfiguration);

        for (AbstractTackle tackle : tackles) {
            session.save(tackle);
        }

        BaseWarriorsMarket market = new BaseWarriorsMarket();
        market.setRevision(1);
        market.setWarriors(new HashSet<Warrior>());
        for (Warrior warrior : baseWarriors) {
            session.save(warrior.getBag());
            session.save(warrior.getAmmunitionBag());
            market.getWarriors().add(warrior);
        }
        session.save(market);

        tx.commit();
        session.close();

        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        sessionFactory = configuration.buildSessionFactory();
    }

    @Test
    public void warriorAccountTest() {
        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        session.close();

        Account account = MainServerHelper.createDefaultAccount(baseConfiguration, "ПОЛЬЗОВАТЕЛЬ", "СЕКРЕТ");

        Bag bag = new Bag();
        bag.setTackles(new ServerStateTackleCollection(new LinkedList<StateTackle>()));

        AmmunitionBag ammunitionBag = ServerHelper.createAmmunitionBag(baseConfiguration);
        Warrior warrior = WarriorHelper.createWarrior(baseConfiguration, bag, ammunitionBag, "ВОИН", 12);
        warrior.setRank(baseConfiguration.getRanks().get(0));
        warrior.setFrameId(49);
        warrior.setStrength((short) 10000);

        Armor armor = new Armor();
        armor.setArmorType(baseConfiguration.getArmorTypes().get(0));

        armor.setState((short) 100);
        armor.setFirmness((short) 100);

        Assert.assertTrue(WarriorHelper.takeOn(warrior, armor, baseConfiguration));

        Weapon rifle = new Weapon();
        WeaponCategory weaponCategory = null;
        for (int i = 0; !(weaponCategory = baseConfiguration.getWeaponCategories().get(i)).getName().equals("ДАЛЬНОБОЙНОЕ"); i++);

        rifle.setWeaponType(weaponCategory.getWeaponTypes().get(0));
        for (int i = 0; i < baseConfiguration.getProjectiles().size(); i++) {
            Projectile projectile = baseConfiguration.getProjectiles().get(i);
            if (WeaponHelper.mayPut(rifle, projectile)) {
                Assert.assertTrue(WeaponHelper.putProjectile(rifle, projectile, 10) < 10);
                break;
            }
        }
        Assert.assertTrue(WarriorHelper.takeOn(warrior, rifle, baseConfiguration));
        short[] skillScores = warrior.getSkillScores();

        for (int i = 0; i < skillScores.length; i++) {
            skillScores[i] = (short) (i * 2);
        }

        WarriorHelper.putInToBag(warrior, baseConfiguration.getProjectiles().get(0), 100, baseConfiguration);
        account.getWarriors().add(warrior);

        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(armor);
        session.save(rifle);
        session.save(warrior.getBag());
        session.save(warrior.getAmmunitionBag());
        session.save(warrior);
        session.save(account.getBase());
        session.save(account);

        tx.commit();
        session.close();

        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        warrior = (Warrior) session.load(Warrior.class, warrior.getId());
        Weapon weapon = warrior.getWeapon();

        tx.commit();
        session.close();

        Assert.assertEquals(weapon.getName(), rifle.getName());
        Assert.assertEquals(weapon.getWeaponType().getCategory().getName(), rifle.getWeaponType().getCategory().getName());
        Assert.assertEquals(weapon.getProjectile().getName(), rifle.getProjectile().getName());
        Assert.assertEquals(weapon.getProjectile().getCategory().getName(), rifle.getProjectile().getCategory().getName());
    }


    @Test
    public void testCopiesWarrior() throws ClassNotFoundException, IOException {
        Session session = sessionFactory.openSession();
        BaseWarriorsMarket market = (BaseWarriorsMarket) session.createCriteria(BaseWarriorsMarket.class).list().get(0);
        Warrior warrior = market.getWarriors().toArray(new Warrior[2])[0];
        session.close();

        Warrior newWarrior = MainServerHelper.copyThroughSerialization(warrior);

        session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        save(newWarrior.getTorsoArmor(), session);
        save(newWarrior.getWeapon(), session);
        save(newWarrior.getHeadArmor(), session);
        save(newWarrior.getLegsArmor(), session);
        save(newWarrior.getBag(), session);
        save(newWarrior.getAmmunitionBag(), session);
        session.save(newWarrior);
        transaction.commit();
        session.close();


        Assert.assertEquals(warrior.getAmmunitionBag().getPackets().size(), newWarrior.getAmmunitionBag().getPackets().size());
        if (newWarrior.getAmmunitionBag().getPackets().size() > 0) {
            Assert.assertEquals(warrior.getAmmunitionBag().getPackets().get(0).getAmmunition().getId(), newWarrior.getAmmunitionBag().getPackets().get(0).getAmmunition().getId());
            Assert.assertEquals(warrior.getAmmunitionBag().getPackets().get(0).getCount(), newWarrior.getAmmunitionBag().getPackets().get(0).getCount());
        }
    }

    private void save(Object object, Session session) {
        if (object != null) {
            session.save(object);
        }
    }

    @Test
    public void battleTest() throws Exception {
        BattleMap map1 = BattleHelper.createBattleMap(10);
        map1.setName("КАРТА1");
        ExitZone[] exits = new ExitZone[2];
        ExitZone exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[0] = exit;
        exit = new ExitZone();
        exit.setX((short) 7);
        exit.setY((short) 7);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[1] = exit;

        map1.setExits(exits);

        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        session.close();

        BattleType[] types = new BattleType[1];
        map1.setPossibleBattleTypes(types);
        types[0] = baseConfiguration.getBattleTypes().get(0);

        Battle battle = BattleHelper.createBattle("qqq", map1, types[0]);
        BattleHelper.prepareBattle(battle);

        Border border = baseConfiguration.getBorders().get(0);
        map1.getCells()[3][3].setElement(border);

        Box box = new Box();
        ServerMagazineCollection serverMagazineCollection = new ServerMagazineCollection();
        serverMagazineCollection.setMagazines(new LinkedList<Magazine>());
        box.setMagazines(serverMagazineCollection);
        ServerStateTackleCollection serverStateTackleCollection = new ServerStateTackleCollection();
        serverStateTackleCollection.setTackles(new LinkedList<StateTackle>());
        box.setTackles(serverStateTackleCollection);
        ServerMedikitCollection serverMedikitCollection = new ServerMedikitCollection();
        serverMedikitCollection.setMedikits(new LinkedList<Medikit>());
        box.setMedikits(serverMedikitCollection);

        Weapon rifle = new Weapon();
        rifle.setLoad((short) 10);
        Projectile projectile = baseConfiguration.getProjectiles().get(0);
        rifle.setProjectile(projectile);
        rifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));

        box.getTackles().add(rifle);
        map1.getCells()[1][1].setElement(box);
        map1.setContent(BattleHelper.serializeBattleCells(map1.getCells()));

        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(map1);
        session.save(battle);
        Serializable battle_id = session.getIdentifier(battle);
        tx.commit();
        session.close();

        session = sessionFactory.openSession();
        Battle newBattle = (Battle) session.load(Battle.class, battle_id);
        newBattle.getMap().getContent();
        session.close();

        newBattle.getMap().setCells(BattleHelper.deserializeBattleCells(newBattle.getMap().getContent()));
        Assert.assertTrue(battle.getMap().getCells()[1][1].getElement() instanceof Box);
        Assert.assertTrue(battle.getMap().getCells()[3][3].getElement() instanceof Border);

        Box newBox = (Box) battle.getMap().getCells()[1][1].getElement();

        Assert.assertEquals(newBox.getTackles().get(0).getName(), rifle.getName());
    }

    @Test
    public void addMap2() throws Exception {
        BattleMap map2 = BattleHelper.createBattleMap(10);
        map2.setName("КАРТА2");
        ExitZone[] exits = new ExitZone[3];
        ExitZone exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 4);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[0] = exit;
        exit = new ExitZone();
        exit.setX((short) 9);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[1] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[2] = exit;

        map2.setExits(exits);

        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        session.close();

        BattleType[] types = new BattleType[2];
        map2.setPossibleBattleTypes(types);
        types[0] = baseConfiguration.getBattleTypes().get(0); // 1x1
        types[1] = baseConfiguration.getBattleTypes().get(1); // 1x1x1

//        Battle battle = BattleHelper.createBattle("qqq", map2, types[0]);
//        BattleHelper.prepareBattle(battle);

        Border border = baseConfiguration.getBorders().get(0);
        map2.getCells()[3][3].setElement(border);

        Box box = new Box();
        ServerMagazineCollection serverMagazineCollection = new ServerMagazineCollection();
        serverMagazineCollection.setMagazines(new LinkedList<Magazine>());
        box.setMagazines(serverMagazineCollection);
        ServerStateTackleCollection serverStateTackleCollection = new ServerStateTackleCollection();
        serverStateTackleCollection.setTackles(new LinkedList<StateTackle>());
        box.setTackles(serverStateTackleCollection);
        ServerMedikitCollection serverMedikitCollection = new ServerMedikitCollection();
        serverMedikitCollection.setMedikits(new LinkedList<Medikit>());
        box.setMedikits(serverMedikitCollection);

        Weapon rifle = new Weapon();
        rifle.setLoad((short) 10);
        Projectile projectile = baseConfiguration.getProjectiles().get(0);
        rifle.setProjectile(projectile);
        rifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));

        box.getTackles().add(rifle);
        map2.getCells()[1][1].setElement(box);
        map2.setContent(BattleHelper.serializeBattleCells(map2.getCells()));

        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(map2);
//        session.save(battle);
//        Serializable battle_id = session.getIdentifier(battle);
        tx.commit();
        session.close();

//        session = sessionFactory.openSession();
//        Battle newBattle = (Battle) session.load(Battle.class, battle_id);
//        newBattle.getMap().getContent();
//        session.close();
//
//        newBattle.getMap().setCells(BattleHelper.deserializeBattleCells(newBattle.getMap().getContent()));
//        Assert.assertTrue(battle.getMap().getCells()[1][1].getElement() instanceof Box);
//        Assert.assertTrue(battle.getMap().getCells()[3][3].getElement() instanceof Border);
//
//        Box newBox = (Box) battle.getMap().getCells()[1][1].getElement();
//
//        Assert.assertEquals(newBox.getTackles().get(0).getName(), rifle.getName());
    }

    @Test
    public void addMap3() throws Exception {
        BattleMap map3 = BattleHelper.createBattleMap(10);
        map3.setName("КАРТА3");
        ExitZone[] exits = new ExitZone[4];
        ExitZone exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[0] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[1] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[2] = exit;
        exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[3] = exit;

        map3.setExits(exits);

        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        session.close();

        BattleType[] types = new BattleType[2];
        map3.setPossibleBattleTypes(types);
        types[0] = baseConfiguration.getBattleTypes().get(0); // 1x1
        types[1] = baseConfiguration.getBattleTypes().get(3); // 2x2

        Border border = baseConfiguration.getBorders().get(0);
        map3.getCells()[3][3].setElement(border);

        Box box = new Box();
        ServerMagazineCollection serverMagazineCollection = new ServerMagazineCollection();
        serverMagazineCollection.setMagazines(new LinkedList<Magazine>());
        box.setMagazines(serverMagazineCollection);
        ServerStateTackleCollection serverStateTackleCollection = new ServerStateTackleCollection();
        serverStateTackleCollection.setTackles(new LinkedList<StateTackle>());
        box.setTackles(serverStateTackleCollection);
        ServerMedikitCollection serverMedikitCollection = new ServerMedikitCollection();
        serverMedikitCollection.setMedikits(new LinkedList<Medikit>());
        box.setMedikits(serverMedikitCollection);

        Weapon rifle = new Weapon();
        rifle.setLoad((short) 10);
        Projectile projectile = baseConfiguration.getProjectiles().get(0);
        rifle.setProjectile(projectile);
        rifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));

        box.getTackles().add(rifle);
        map3.getCells()[1][1].setElement(box);
        map3.setContent(BattleHelper.serializeBattleCells(map3.getCells()));

        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(map3);
        tx.commit();
        session.close();
    }

    @Test
    public void addMap4() throws Exception {
        BattleMap map4 = BattleHelper.createBattleMap(10);
        map4.setName("КАРТА4");
        ExitZone[] exits = new ExitZone[4];
        ExitZone exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[0] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[1] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[2] = exit;
        exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[3] = exit;

        map4.setExits(exits);

        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        session.close();

        BattleType[] types = new BattleType[3];
        map4.setPossibleBattleTypes(types);
        types[0] = baseConfiguration.getBattleTypes().get(0); // 1x1
        types[1] = baseConfiguration.getBattleTypes().get(2); // 1x1x1x1
        types[2] = baseConfiguration.getBattleTypes().get(3); // 2x2

        Border border = baseConfiguration.getBorders().get(0);
        map4.getCells()[3][3].setElement(border);

        Box box = new Box();
        ServerMagazineCollection serverMagazineCollection = new ServerMagazineCollection();
        serverMagazineCollection.setMagazines(new LinkedList<Magazine>());
        box.setMagazines(serverMagazineCollection);
        ServerStateTackleCollection serverStateTackleCollection = new ServerStateTackleCollection();
        serverStateTackleCollection.setTackles(new LinkedList<StateTackle>());
        box.setTackles(serverStateTackleCollection);
        ServerMedikitCollection serverMedikitCollection = new ServerMedikitCollection();
        serverMedikitCollection.setMedikits(new LinkedList<Medikit>());
        box.setMedikits(serverMedikitCollection);

        Weapon rifle = new Weapon();
        rifle.setLoad((short) 10);
        Projectile projectile = baseConfiguration.getProjectiles().get(0);
        rifle.setProjectile(projectile);
        rifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));

        box.getTackles().add(rifle);
        map4.getCells()[1][1].setElement(box);
        map4.setContent(BattleHelper.serializeBattleCells(map4.getCells()));

        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(map4);
        tx.commit();
        session.close();
    }

    @Test
    public void addMap5() throws Exception {
        BattleMap map5 = BattleHelper.createBattleMap(10);
        map5.setName("КАРТА5");
        ExitZone[] exits = new ExitZone[4];
        ExitZone exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[0] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[1] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[2] = exit;
        exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[3] = exit;

        map5.setExits(exits);

        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        session.close();

        BattleType[] types = new BattleType[4];
        map5.setPossibleBattleTypes(types);
        types[0] = baseConfiguration.getBattleTypes().get(0); // 1x1
        types[1] = baseConfiguration.getBattleTypes().get(1); // 1x1x1
        types[2] = baseConfiguration.getBattleTypes().get(2); // 1x1x1x1
        types[3] = baseConfiguration.getBattleTypes().get(3); // 2x2

        Border border = baseConfiguration.getBorders().get(0);
        map5.getCells()[3][3].setElement(border);

        Box box = new Box();
        ServerMagazineCollection serverMagazineCollection = new ServerMagazineCollection();
        serverMagazineCollection.setMagazines(new LinkedList<Magazine>());
        box.setMagazines(serverMagazineCollection);
        ServerStateTackleCollection serverStateTackleCollection = new ServerStateTackleCollection();
        serverStateTackleCollection.setTackles(new LinkedList<StateTackle>());
        box.setTackles(serverStateTackleCollection);
        ServerMedikitCollection serverMedikitCollection = new ServerMedikitCollection();
        serverMedikitCollection.setMedikits(new LinkedList<Medikit>());
        box.setMedikits(serverMedikitCollection);

        Weapon rifle = new Weapon();
        rifle.setLoad((short) 10);
        Projectile projectile = baseConfiguration.getProjectiles().get(0);
        rifle.setProjectile(projectile);
        rifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));

        box.getTackles().add(rifle);
        map5.getCells()[1][1].setElement(box);
        map5.setContent(BattleHelper.serializeBattleCells(map5.getCells()));

        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(map5);
        tx.commit();
        session.close();
    }

    @Test
    public void addMap6() throws Exception {
        BattleMap map5 = BattleHelper.createBattleMap(10);
        map5.setName("КАРТА6");
        ExitZone[] exits = new ExitZone[4];
        ExitZone exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[0] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[1] = exit;
        exit = new ExitZone();
        exit.setX((short) 8);
        exit.setY((short) 2);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[2] = exit;
        exit = new ExitZone();
        exit.setX((short) 2);
        exit.setY((short) 8);
        exit.setxRadius((byte) 2);
        exit.setyRadius((byte) 2);
        exits[3] = exit;

        map5.setExits(exits);

        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        session.close();

        BattleType[] types = new BattleType[2];
        map5.setPossibleBattleTypes(types);
        types[0] = baseConfiguration.getBattleTypes().get(0); // 1x1*1
        types[1] = baseConfiguration.getBattleTypes().get(4); // 1x1*2

        Border border = baseConfiguration.getBorders().get(0);
        map5.getCells()[3][3].setElement(border);

        Box box = new Box();
        ServerMagazineCollection serverMagazineCollection = new ServerMagazineCollection();
        serverMagazineCollection.setMagazines(new LinkedList<Magazine>());
        box.setMagazines(serverMagazineCollection);
        ServerStateTackleCollection serverStateTackleCollection = new ServerStateTackleCollection();
        serverStateTackleCollection.setTackles(new LinkedList<StateTackle>());
        box.setTackles(serverStateTackleCollection);
        ServerMedikitCollection serverMedikitCollection = new ServerMedikitCollection();
        serverMedikitCollection.setMedikits(new LinkedList<Medikit>());
        box.setMedikits(serverMedikitCollection);

        Weapon rifle = new Weapon();
        rifle.setLoad((short) 10);
        Projectile projectile = baseConfiguration.getProjectiles().get(0);
        rifle.setProjectile(projectile);
        rifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));

        box.getTackles().add(rifle);
        map5.getCells()[1][1].setElement(box);
        map5.setContent(BattleHelper.serializeBattleCells(map5.getCells()));

        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(map5);
        tx.commit();
        session.close();
    }

}
