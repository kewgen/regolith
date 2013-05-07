package com.geargames.regolith;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.helpers.*;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.*;
import com.geargames.regolith.units.base.*;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.tackle.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * User: mkutuzov
 * Date: 10.04.12
 */
public class SerializerTest {

    @Test
    public void simple() {
        MicroByteBuffer buffer = new MicroByteBuffer(new byte[100]);
        buffer.setLimit(100);
        buffer.setPosition(0);
        buffer.mark();
        SimpleSerializer.serialize("1234567890", buffer);
        SimpleSerializer.serialize(Short.MAX_VALUE, buffer);
        SimpleSerializer.serialize(Integer.MAX_VALUE, buffer);
        SimpleSerializer.serialize(Long.MAX_VALUE, buffer);
        SimpleSerializer.serialize(1.1f, buffer);
        SimpleSerializer.serialize(1.5, buffer);
        buffer.reset();
        Assert.assertEquals("1234567890", SimpleDeserializer.deserializeString(buffer));
        Assert.assertEquals(Short.MAX_VALUE, SimpleDeserializer.deserializeShort(buffer));
        Assert.assertEquals(Integer.MAX_VALUE, SimpleDeserializer.deserializeInt(buffer));
        Assert.assertEquals(Long.MAX_VALUE, SimpleDeserializer.deserializeLong(buffer));
        Assert.assertEquals(1.1f, SimpleDeserializer.deserializeFloat(buffer), 1.0 / SimpleSerializer.DECIMAL_SCALE);
        Assert.assertEquals(1.5, SimpleDeserializer.deserializeDouble(buffer), 1.0 / SimpleSerializer.DECIMAL_SCALE);
    }

    private BattleConfiguration battleConfiguration;

    @Before
    public void preConfiguration() {
        battleConfiguration = new BattleConfiguration();
        battleConfiguration.setAbilityMax((short) 1000);
        battleConfiguration.setAccurateShootFix(1.3);
        ActionFees actionFees = new ActionFees();
        actionFees.setChangeWeapon((byte) 1);
        actionFees.setMove((byte) 2);
        actionFees.setPickupTackle((byte) 3);
        actionFees.setRechargeWeapon((byte) 4);
        actionFees.setShutOrOpen((byte) 5);
        actionFees.setSitOrStand((byte) 3);

        battleConfiguration.setActionFees(actionFees);
        battleConfiguration.setActiveTime(60);
        battleConfiguration.setArmorSpoiling(4);
        battleConfiguration.setCriticalBarrierToVictimDistance(10);
        battleConfiguration.setKillExperienceMultiplier(2);
        Underload[] underloads = new Underload[2];

        Underload underload = new Underload();
        underload.setAction((byte) 10);
        underload.setMax((byte) 1);
        underload.setMin((byte) 1);
        underloads[0] = underload;

        underload = new Underload();
        underload.setAction((byte) 2);
        underload.setMin((byte) 11);
        underload.setMax((byte) 20);
        underloads[1] = underload;

        battleConfiguration.setUnderloads(underloads);
        battleConfiguration.setMinimalMapSize(20);
        battleConfiguration.setQuickShootFix(1.2);
        battleConfiguration.setSitHunterShootFix(1.3);
        battleConfiguration.setStandHunterShootFix(1.5);
        battleConfiguration.setSitVictimShootFix(0.8);
        battleConfiguration.setStandVictimShootFix(1);
        battleConfiguration.setWeaponSpoiling(3);
        battleConfiguration.setWalkSpeed(5);
        SubordinationDamage[] subordinationDamages = new SubordinationDamage[2];
        SubordinationDamage subordinationDamage = new SubordinationDamage();
        subordinationDamage.setDamage((byte) 3);
        subordinationDamage.setMaxRankDifference((byte) 2);
        subordinationDamage.setMinRankDifference((byte) 0);
        subordinationDamages[0] = subordinationDamage;

        subordinationDamage = new SubordinationDamage();
        subordinationDamage.setDamage((byte) 2);
        subordinationDamage.setMaxRankDifference((byte) 6);
        subordinationDamage.setMinRankDifference((byte) 3);
        subordinationDamages[1] = subordinationDamage;

        battleConfiguration.setSubordinationDamage(subordinationDamages);
    }

    @Test
    public void circleBattleConfiguration() {
        MicroByteBuffer buffer = new MicroByteBuffer(new byte[500]);
        buffer.setLimit(500);
        buffer.setPosition(0);
        buffer.mark();
        ConfigurationSerializer.serialize(battleConfiguration, buffer);
        buffer.setLimit(buffer.getPosition() + 1);
        buffer.reset();

        BattleConfiguration deserialized = ConfigurationDeserializer.deserializeBattleConfiguration(buffer);

        System.out.println(battleConfiguration.getSitHunterShootFix() + " " + deserialized.getSitHunterShootFix());
        System.out.println(battleConfiguration.getSitVictimShootFix() + " " + deserialized.getSitVictimShootFix());
        System.out.println(battleConfiguration.getQuickShootFix() + " " + deserialized.getQuickShootFix());

        Assert.assertEquals(battleConfiguration.getAbilityMax(), deserialized.getAbilityMax());
        Assert.assertEquals(battleConfiguration.getWalkSpeed(), deserialized.getWalkSpeed());
    }

    BaseConfiguration baseConfiguration;

    @Before
    public void before() {
        baseConfiguration = new BaseConfiguration();
        ServerProjectileCollection projectiles = new ServerProjectileCollection();
        projectiles.setProjectiles(new ArrayList<Projectile>(2));
        Projectile projectile1 = new Projectile();
        projectile1.setId(1);
        projectile1.setName("1");
        AmmunitionCategory category1 = new AmmunitionCategory();
        category1.setId(1);
        category1.setName("Хорошие");
        category1.setQuality(1.2);
        projectile1.setCategory(category1);
        projectile1.setFrameId(10);
        projectile1.setWeight((short) 10);
        projectiles.add(projectile1);

        AmmunitionCategory category2 = new AmmunitionCategory();
        category2.setId(2);
        category2.setName("Наилепшие");
        category2.setQuality(1.5);

        Projectile projectile2 = new Projectile();
        projectile2.setId(2);
        projectile2.setName("2");
        projectile2.setCategory(category2);
        projectile2.setFrameId(12);
        projectile2.setWeight((short) 20);
        projectiles.add(projectile2);

        AmmunitionCategory[] ammunitionCategories = new AmmunitionCategory[2];
        ammunitionCategories[0] = category1;
        ammunitionCategories[1] = category2;
        ServerAmmunitionCategoryCollection ammunitionCategoryDictionary = new ServerAmmunitionCategoryCollection();
        ammunitionCategoryDictionary.setCategories(Arrays.asList(ammunitionCategories));
        baseConfiguration.setAmmunitionCategories(ammunitionCategoryDictionary);
        baseConfiguration.setProjectiles(projectiles);

        ArmorType[] armorTypes = new ArmorType[2];
        ArmorType armorType = new ArmorType();
        armorType.setId(1);
        armorType.setName("Броня");
        armorType.setFrameId(14);
        armorType.setWeight((short) 5000);
        armorType.setArmor((byte) 100);
        armorType.setBaseFirmness((short) 120);
        armorType.setBodyParticle(BodyParticles.LEGS);
        armorType.setCraftinessBonus((short) 1);
        armorType.setMarksmanshipBonus((short) 0);
        armorType.setRegenerationBonus((short) 1);
        armorType.setSpeedBonus((short) -1);
        armorType.setStrengthBonus((short) 0);
        armorType.setVisibilityBonus((short) 0);
        armorTypes[0] = armorType;

        armorType = new ArmorType();
        armorType.setId(2);
        armorType.setName("Шлем");
        armorType.setFrameId(16);
        armorType.setWeight((short) 1000);
        armorType.setArmor((byte) 100);
        armorType.setBaseFirmness((short) 120);
        armorType.setBodyParticle(BodyParticles.HEAD);
        armorType.setCraftinessBonus((short) 1);
        armorType.setMarksmanshipBonus((short) 0);
        armorType.setRegenerationBonus((short) 1);
        armorType.setSpeedBonus((short) -1);
        armorType.setStrengthBonus((short) 0);
        armorType.setVisibilityBonus((short) 0);
        armorTypes[1] = armorType;

        ServerArmorTypeCollection armorTypeDictionary = new ServerArmorTypeCollection();
        armorTypeDictionary.setArmorTypes(Arrays.asList(armorTypes));
        baseConfiguration.setArmorTypes(armorTypeDictionary);

        Skill[] skills = new Skill[2];
        Skill skill = new Skill();
        skill.setAction((byte) 0);
        skill.setExperience((short) 0);
        skills[0] = skill;
        skill = new Skill();
        skill.setAction((byte) 5);
        skill.setExperience((short) 50);
        skills[1] = skill;

        ServerSkillCollection skillDictionary = new ServerSkillCollection();
        skillDictionary.setSkills(Arrays.asList(skills));
        baseConfiguration.setSkills(skillDictionary);

        WeaponCategory[] weaponCategories = new WeaponCategory[1];
        WeaponCategory weaponCategory = new WeaponCategory();
        weaponCategory.setId(0);
        weaponCategory.setName("Прикольное");

        ServerWeaponTypeCollection weaponTypes = new ServerWeaponTypeCollection();
        weaponTypes.setWeaponTypes(new ArrayList<WeaponType>(2));

        WeaponType weaponType = new WeaponType();
        weaponType.setId(1);
        weaponType.setAccuracy((byte) 10);
        weaponType.setAccurateAction((byte) 2);
        weaponType.setAmmunitionPerShoot((byte) 1);
        weaponType.setBaseFirmness((short) 110);
        weaponType.setCapacity((short) 20);
        weaponType.setCategory(weaponCategory);
        weaponType.setCriticalDamage((short) 10);
        WeaponDistances weaponDistances = new WeaponDistances();
        weaponDistances.setMax((byte) 100);
        weaponDistances.setMaxOptimal((byte) 90);
        weaponDistances.setMinOptimal((byte) 15);
        weaponDistances.setMin((byte) 5);
        weaponType.setDistance(weaponDistances);
        WeaponDamage max = new WeaponDamage();
        max.setMaxDistance((short) 2);
        max.setMinDistance((short) 2);
        max.setOptDistance((short) 10);
        weaponType.setMaxDamage(max);
        WeaponDamage min = new WeaponDamage();
        min.setMaxDistance((short) 1);
        min.setMinDistance((short) 1);
        min.setOptDistance((short) 5);
        weaponType.setMinDamage(min);
        weaponType.setMinSkill(skill);
        weaponType.setProjectiles(projectiles);
        weaponType.setQuickAction((byte) 1);
        weaponType.setFrameId(17);
        weaponType.setWeight((short) 2000);
        weaponType.setName("ружьё");

        weaponTypes.add(weaponType);

        weaponType = new WeaponType();
        weaponType.setId(1);
        weaponType.setAccuracy((byte) 10);
        weaponType.setAccurateAction((byte) 2);
        weaponType.setAmmunitionPerShoot((byte) 1);
        weaponType.setBaseFirmness((short) 110);
        weaponType.setCapacity((short) 20);
        weaponType.setCategory(weaponCategory);
        weaponType.setCriticalDamage((short) 10);
        weaponDistances = new WeaponDistances();
        weaponDistances.setMax((byte) 100);
        weaponDistances.setMaxOptimal((byte) 90);
        weaponDistances.setMinOptimal((byte) 15);
        weaponDistances.setMin((byte) 5);
        weaponType.setDistance(weaponDistances);
        max = new WeaponDamage();
        max.setMaxDistance((short) 2);
        max.setMinDistance((short) 2);
        max.setOptDistance((short) 10);
        weaponType.setMaxDamage(max);
        min = new WeaponDamage();
        min.setMaxDistance((short) 1);
        min.setMinDistance((short) 1);
        min.setOptDistance((short) 5);
        weaponType.setMinDamage(min);
        weaponType.setMinSkill(skill);
        weaponType.setProjectiles(projectiles);
        weaponType.setQuickAction((byte) 1);
        weaponType.setFrameId(17);
        weaponType.setWeight((short) 2000);
        weaponType.setName("автомат");

        weaponTypes.add(weaponType);


        projectile1.setWeaponTypes(weaponTypes);
        projectile2.setWeaponTypes(weaponTypes);
        weaponCategory.setWeaponTypes(weaponTypes);
        weaponCategories[0] = weaponCategory;

        ServerWeaponCategoryCollection weaponCategoryDictionary = new ServerWeaponCategoryCollection();
        weaponCategoryDictionary.setCategories(Arrays.asList(weaponCategories));
        baseConfiguration.setWeaponCategories(weaponCategoryDictionary);
        baseConfiguration.setMaxWorkShopLevel((byte) 10);
        baseConfiguration.setMaxWorkShopProbability((byte) 15);
        baseConfiguration.setMinWorkShopProbability((byte) 5);
        baseConfiguration.setPocketsAmount((byte) 100);

        Rank[] ranks = new Rank[2];
        Rank rank = new Rank();
        rank.setId(1);
        rank.setName("1");
        rank.setExperience((short) 100);
        ranks[0] = rank;

        rank = new Rank();
        rank.setId(2);
        rank.setName("2");
        rank.setExperience((short) 200);
        ranks[1] = rank;
        ServerRankCollection rankDictionary = new ServerRankCollection();
        rankDictionary.setRanks(Arrays.asList(ranks));
        baseConfiguration.setRanks(rankDictionary);
        baseConfiguration.setRevision((short) 1);

        Medikit[] medikits = new Medikit[2];
        Medikit medikit = new Medikit();
        medikit.setId(0);
        medikit.setActionScores((byte) 2);
        medikit.setMinSkill(skills[0]);
        medikit.setValue((short) 200);
        medikit.setCategory(category1);
        medikit.setName("Медичка");
        medikit.setWeight((short) 100);
        medikit.setFrameId(10);
        medikits[0] = medikit;
        medikit = new Medikit();
        medikit.setId(1);
        medikit.setActionScores((byte) 2);
        medikit.setMinSkill(skills[1]);
        medikit.setValue((short) 500);
        medikit.setCategory(category1);
        medikit.setName("Медичка");
        medikit.setWeight((short) 100);
        medikit.setFrameId(11);
        medikits[1] = medikit;

        Barrier[] barriers = new Barrier[2];
        ServerBarrier barrier = new ServerBarrier();
        barrier.setFrameId(11);
        barrier.setId(0);
        barrier.setAbleToLookThrough(false);
        barrier.setAbleToWalkThrough(false);
        Map<WeaponCategory, Boolean> shoot = new HashMap<WeaponCategory, Boolean>();
        barrier.setShootThrough(shoot);
        shoot.put(weaponCategory, false);
        barriers[0] = barrier;
        barrier = new ServerBarrier();
        barrier.setFrameId(12);
        barrier.setId(1);
        barrier.setAbleToLookThrough(false);
        barrier.setAbleToWalkThrough(true);
        barrier.setShootThrough(shoot);
        barriers[1] = barrier;
        ServerMedikitCollection medikitDictionary = new ServerMedikitCollection();
        medikitDictionary.setMedikits(Arrays.asList(medikits));
        baseConfiguration.setMedikits(medikitDictionary);
        ServerBarrierCollection barrierDictionary = new ServerBarrierCollection();
        barrierDictionary.setBarriers(Arrays.asList(barriers));
        baseConfiguration.setBarriers(barrierDictionary);
        beforeAccount();
        ServerBattleTypeCollection battleTypes = new ServerBattleTypeCollection();
        battleTypes.setBattleTypes(new LinkedList<BattleType>());

        BattleType battleType = new TrainingBattle();
        battleType.setId(0);
        battleType.setName("1:1");
        battleType.setScores((byte) 5);
        battleType.setAllianceAmount((short) 2);
        battleType.setAllianceSize((short) 1);
        battleType.setGroupSize((short) 1);
        battleTypes.add(battleType);

        battleType = new TrainingBattle();
        battleType.setId(1);
        battleType.setName("1:1:1");
        battleType.setScores((byte) 10);
        battleType.setAllianceAmount((short) 3);
        battleType.setAllianceSize((short) 1);
        battleType.setGroupSize((short) 1);
        battleTypes.add(battleType);

        battleType = new TrainingBattle();
        battleType.setId(2);
        battleType.setName("1+1:1+1");
        battleType.setScores((byte) 15);
        battleType.setAllianceAmount((short) 2);
        battleType.setAllianceSize((short) 2);
        battleType.setGroupSize((short) 1);
        battleTypes.add(battleType);

        baseConfiguration.setBattleTypes(battleTypes);
    }

    @Test
    public void circleBaseConfiguration() {
        MicroByteBuffer buffer = new MicroByteBuffer(new byte[1000]);
        buffer.setLimit(1000);
        buffer.setPosition(0);
        buffer.mark();
        ConfigurationSerializer.serialize(baseConfiguration, buffer);
        buffer.setLimit(buffer.getPosition() + 1);
        buffer.reset();
        BaseConfiguration deserialized = ConfigurationDeserializer.deserializeBaseConfiguration(buffer);
        Assert.assertEquals(baseConfiguration.getPocketsAmount(), deserialized.getPocketsAmount());
        int i = 0;
        for (WeaponCategory weaponCategory1 : ((ServerWeaponCategoryCollection) baseConfiguration.getWeaponCategories()).getCategories()) {
            WeaponCategory currentCategory = deserialized.getWeaponCategories().get(i);
            Assert.assertEquals(weaponCategory1.getId(), currentCategory.getId());
            Assert.assertEquals(weaponCategory1.getName(), currentCategory.getName());
            int j = 0;
            for (WeaponType weaponType1 : ((ServerWeaponTypeCollection) weaponCategory1.getWeaponTypes()).getWeaponTypes()) {
                WeaponType currentWeaponType = currentCategory.getWeaponTypes().get(j);
                Assert.assertEquals(weaponType1.getName(), currentWeaponType.getName());
                Assert.assertEquals(weaponType1.getCriticalDamage(), currentWeaponType.getCriticalDamage());
                Assert.assertEquals(weaponType1.getCapacity(), currentWeaponType.getCapacity());

                ProjectileCollection projectiles1 = weaponType1.getProjectiles();
                ProjectileCollection projectiles2 = currentWeaponType.getProjectiles();
                for (int k = 0; k < projectiles1.size(); k++) {
                    Assert.assertEquals(projectiles1.get(k).getId(), projectiles2.get(k).getId());
                }
                j++;
            }
            i++;
        }
    }

    private Account account;

    public void beforeAccount() {
        account = new Account();
        account.setName("пупс");
        account.setId(1);
        Base base = new Base();
        account.setBase(base);
        base.setId(1);
        base.setAccount(account);
        ClearingShop clearingShop = new ClearingShop();
        clearingShop.setLevel((byte) 2);
        base.setClearingShop(clearingShop);
        Hospital hospital = new Hospital();
        hospital.setLevel((byte) 2);
        base.setHospital(hospital);
        RestHouse restHouse = new RestHouse();
        restHouse.setLevel((byte) 2);
        base.setRestHouse(restHouse);
        ShootingRange shootingRange = new ShootingRange();
        shootingRange.setLevel((byte) 2);
        base.setShootingRange(shootingRange);
        StoreHouse storeHouse = ServerBaseHelper.createStoreHouse(baseConfiguration);
        storeHouse.setLevel((byte) 2);
        base.setStoreHouse(storeHouse);
        TrainingCenter trainingCenter = new TrainingCenter();
        trainingCenter.setLevel((byte) 2);
        base.setTrainingCenter(trainingCenter);
        WorkShop workShop = new WorkShop();
        workShop.setLevel((byte) 2);
        base.setWorkShop(workShop);

        BattleGroup group = new BattleGroup();
        group.setAccount(account);
        ServerWarriorCollection warriorCollection = new ServerWarriorCollection();
        warriorCollection.setWarriors(new LinkedList<Warrior>());
        group.setWarriors(warriorCollection);

        ServerWarriorCollection warriors = new ServerWarriorCollection();
        warriors.setWarriors(new LinkedList<Warrior>());

        Bag bag = new Bag();
        ServerStateTackleCollection tackleCollection = new ServerStateTackleCollection(new LinkedList<StateTackle>());
        bag.setTackles(tackleCollection);
        bag.setId(0);

        AmmunitionBag ammunitionBag = ServerHelper.createAmmunitionBag(baseConfiguration);

        Warrior warrior = WarriorHelper.createWarrior(baseConfiguration, bag, ammunitionBag, "воин", 2);
        WarriorHelper.addWarriorToGroup(group, warrior);
        warriors.add(warrior);
        warrior.setId(0);


        Weapon rifle = new Weapon();
        Projectile projectile = baseConfiguration.getProjectiles().get(0);
        rifle.setProjectile(projectile);
        rifle.setLoad((short) 10);
        rifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));
        rifle.setFirmness((short) 130);
        rifle.setState((short) 130);
        rifle.setUpgrade((byte) 2);
        rifle.setId(2);
        BagHelper.putIntoBag(bag, rifle);
        AmmunitionBagHelper.putIn(warrior.getAmmunitionBag(), projectile, 100);
        Weapon autoRifle = new Weapon();
        autoRifle.setProjectile(projectile);
        autoRifle.setLoad((short) 20);
        autoRifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(1));
        autoRifle.setFirmness((short) 100);
        autoRifle.setState((short) 100);
        autoRifle.setUpgrade((byte) 1);
        autoRifle.setId(3);
        warrior.setRank(baseConfiguration.getRanks().get(0));
        warrior.setWeapon(autoRifle);
        warrior.setActionScore((short) 20);
        warrior.setCraftiness((byte) 3);
        warrior.setExperience(400);
        warrior.setMarksmanship((byte) 3);

        int length = warrior.getSkillScores().length;
        for (int i = 0; i < length; i++) {
            warrior.getSkillScores()[i] = (short) (i * 100);
        }
        WarriorHelper.adjustSkills(warrior, baseConfiguration);
        warrior.setStrength((short) 5000);
        warrior.setSpeed((byte) 30);
        warrior.setVitality((byte) 5);

        Armor armor = new Armor();
        ArmorType armorType = baseConfiguration.getArmorTypes().get(0);
        armor.setArmorType(armorType);
        armor.setId(11);
        armor.setFirmness((short) 130);
        armor.setState((short) 100);
        armor.setUpgrade((byte) 2);

        warrior.setTorsoArmor(armor);

        account.setWarriors(warriors);
        Clan clan = new Clan();
        account.setClan(clan);
        clan.setName("супер");
        clan.setId(1);
        ServerAccountCollection accounts = new ServerAccountCollection();
        accounts.setAccounts(new LinkedList<Account>());
        accounts.add(account);
        clan.setAccounts(accounts);
    }

    @Test
    public void account() {
        MicroByteBuffer buffer = new MicroByteBuffer(new byte[1000]);
        buffer.setLimit(1000);
        buffer.setPosition(0);
        buffer.mark();
        AccountSerializer.serialize(account, buffer);
        buffer.reset();
        Account newAccount = AccountDeserializer.deserialize(buffer, baseConfiguration);
        Warrior newWarrior = newAccount.getWarriors().get(0);
        Warrior warrior = account.getWarriors().get(0);
        Assert.assertEquals(warrior.getWeapon().getId(), newWarrior.getWeapon().getId());
        Assert.assertEquals(warrior.getWeapon().getProjectile().getName(), newWarrior.getWeapon().getProjectile().getName());
        Assert.assertEquals(warrior.getBag().getTackles().size(), newWarrior.getBag().getTackles().size());
        Assert.assertEquals(((Weapon) (warrior.getBag().getTackles().get(0))).getLoad(), ((Weapon) (newWarrior.getBag().getTackles().get(0))).getLoad());
        Assert.assertEquals(null, newWarrior.getHeadArmor());
        Assert.assertEquals(warrior.getTorsoArmor().getArmorType().getArmor(), newWarrior.getTorsoArmor().getArmorType().getArmor());
        Assert.assertEquals(warrior.getTorsoArmor().getState(), newWarrior.getTorsoArmor().getState());
        Assert.assertEquals(warrior.getTorsoArmor().getFirmness(), newWarrior.getTorsoArmor().getFirmness());
    }

    @Test
    public void battle() throws RegolithException {
        Battle battle = new Battle();
        battle.setId(0);
        battle.setBattleType(BaseConfigurationHelper.findBattleTypeById(0, baseConfiguration));
        battle.setName("битва");
        BattleAlliance[] alliances = new BattleAlliance[2];
        BattleAlliance alliance = new BattleAlliance();
        alliances[0] = alliance;
        alliance.setId(0);
        alliance.setNumber((byte) 0);
        alliance.setBattle(battle);
        ServerBattleGroupCollection battleGroups = new ServerBattleGroupCollection();


        WarriorCollection warriors = new ServerWarriorCollection();
        ((ServerWarriorCollection) warriors).setWarriors(new LinkedList<Warrior>());

        Bag bag = new Bag();
        ServerStateTackleCollection tackleCollection = new ServerStateTackleCollection(new LinkedList<StateTackle>());
        bag.setTackles(tackleCollection);
        bag.setId(1);
        AmmunitionBag ammunitionBag = ServerHelper.createAmmunitionBag(baseConfiguration);
        Warrior warrior = WarriorHelper.createWarrior(baseConfiguration, bag, ammunitionBag, "воин", 2);
        warriors.add(warrior);
        warrior.setId(1);
        warrior.setBag(bag);

        Weapon rifle = new Weapon();
        Projectile projectile = baseConfiguration.getProjectiles().get(0);
        rifle.setProjectile(projectile);
        rifle.setLoad((short) 8);
        rifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));
        rifle.setFirmness((short) 110);
        rifle.setState((short) 100);
        rifle.setUpgrade((byte) 1);
        rifle.setId(62);
        BagHelper.putIntoBag(bag, rifle);
        AmmunitionBagHelper.putIn(warrior.getAmmunitionBag(), projectile, 200);
        Weapon autoRifle = new Weapon();
        autoRifle.setProjectile(projectile);
        autoRifle.setLoad((short) 10);
        autoRifle.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(1));
        autoRifle.setFirmness((short) 100);
        autoRifle.setState((short) 90);
        autoRifle.setUpgrade((byte) 1);
        autoRifle.setId(63);
        warrior.setRank(baseConfiguration.getRanks().get(0));
        warrior.setWeapon(autoRifle);
        Account enemy = new Account();
        enemy.setName("вражина");
        enemy.setId(2);
        enemy.setWarriors(warriors);

        BattleGroup battleGroup = new BattleGroup();
        battleGroups.add(battleGroup);
        battleGroup.setAlliance(alliance);
        battleGroup.setId(0);
        WarriorHelper.addWarriorToGroup(battleGroup, warrior);
        battleGroup.setAccount(enemy);

        warrior.setActionScore((short) 20);
        warrior.setCraftiness((byte) 15);
        warrior.setExperience(400);
        warrior.setMarksmanship((byte) 15);

        int length = warrior.getSkillScores().length;
        for (int i = 0; i < length; i++) {
            warrior.getSkillScores()[i] = (short) (i * 100);
        }
        WarriorHelper.adjustSkills(warrior, baseConfiguration);
        warrior.setStrength((short) 5000);
        warrior.setSpeed((byte) 30);
        warrior.setVitality((byte) 5);

        Armor armor = new Armor();
        ArmorType armorType = baseConfiguration.getArmorTypes().get(0);
        armor.setArmorType(armorType);
        armor.setId(11);
        armor.setFirmness((short) 130);
        armor.setState((short) 100);
        armor.setUpgrade((byte) 2);

        warrior.setHeadArmor(armor);
        warrior.setBattleGroup(battleGroup);

        warriors.set(warrior, 0);

        battleGroup.setWarriors(warriors);
        alliance.setAllies(battleGroups);

        alliance = new BattleAlliance();
        alliances[1] = alliance;
        alliance.setId(2);
        alliance.setNumber((byte) 2);
        alliance.setBattle(battle);
        battleGroups = new ServerBattleGroupCollection();
        battleGroup = new BattleGroup();
        battleGroup.setAlliance(alliance);
        battleGroups.add(battleGroup);
        warriors = account.getWarriors();
        for (int i = 0; i < warriors.size(); i++) {
            warriors.get(i).setBattleGroup(battleGroup);
        }
        battleGroup.setWarriors(warriors);
        alliance.setAllies(battleGroups);
        battle.setAlliances(alliances);

        BattleMap battleMap = ServerBattleHelper.createBattleMap(40);
        battleMap.setId(0);

        ExitZone exitZone = new ExitZone();
        exitZone.setY((short) 2);
        exitZone.setX((short) 2);
        exitZone.setxRadius((byte) 2);
        exitZone.setyRadius((byte) 2);
        ExitZone[] exitZones = new ExitZone[2];
        exitZones[0] = exitZone;
        exitZone = new ExitZone();
        exitZone.setY((short) 38);
        exitZone.setX((short) 38);
        exitZone.setxRadius((byte) 2);
        exitZone.setyRadius((byte) 2);
        exitZones[1] = exitZone;

        battleMap.setExits(exitZones);
        battleMap.setName("Кровавые болота");
        BattleType[] types = new BattleType[1];
        types[0] = baseConfiguration.getBattleTypes().get(0);
        battleMap.setPossibleBattleTypes(types);

        battle.setMap(battleMap);
        ServerBattleHelper.spreadAlliancesOnTheMap(battle);

        BattleCell[][] cells = battleMap.getCells();

        Weapon weapon = new Weapon();
        weapon.setId(7);
        weapon.setUpgrade((byte) 3);
        weapon.setLoad((short) 1);
        weapon.setWeaponType(baseConfiguration.getWeaponCategories().get(0).getWeaponTypes().get(0));
        weapon.setProjectile(baseConfiguration.getProjectiles().get(1));
        weapon.setFirmness((short) 150);
        weapon.setState((short) 150);

        cells[10][10].addElement(weapon);

        MicroByteBuffer buffer = new MicroByteBuffer(new byte[2000]);
        buffer.setLimit(2000);
        buffer.setPosition(0);
        buffer.mark();
        BattleSerializer.serialize(battle, account, buffer);
        buffer.reset();
        Battle newBattle = BattleDeserializer.deserializeBattle(buffer, baseConfiguration, account);

        Assert.assertEquals(((Weapon) (battle.getMap().getCells()[10][10].getElement())).getLoad(), ((Weapon) (newBattle.getMap().getCells()[10][10].getElement())).getLoad());
        Assert.assertEquals(((Weapon) (battle.getMap().getCells()[10][10].getElement())).getWeaponType().getName(), ((Weapon) (newBattle.getMap().getCells()[10][10].getElement())).getWeaponType().getName());

        Assert.assertEquals(battle.getAlliances()[0].getNumber(), newBattle.getAlliances()[0].getNumber());
        warrior = battle.getAlliances()[0].getAllies().get(0).getWarriors().get(0);
        Warrior newWarrior = newBattle.getAlliances()[0].getAllies().get(0).getWarriors().get(0);
        Assert.assertEquals(battle.getAlliances()[0].getNumber(), newBattle.getAlliances()[0].getNumber());
        Assert.assertEquals(warrior.getHeadArmor().getId(), newWarrior.getHeadArmor().getId());
        Assert.assertEquals(warrior.getHeadArmor().getArmorType().getArmor(), newWarrior.getHeadArmor().getArmorType().getArmor());
        //набор умений врага не известен на клиенте
        Assert.assertNull(newWarrior.getSkills());
        Assert.assertEquals(warrior.getWeapon().getFrameId(), newWarrior.getWeapon().getFrameId());
        Assert.assertEquals(warrior.getWeapon().getProjectile().getName(), newWarrior.getWeapon().getProjectile().getName());
    }

    @Test
    public void poorBattle() throws RegolithException {
        Battle battle = new Battle();
        battle.setId(0);
        battle.setName("пустая битва");

        MicroByteBuffer buffer = new MicroByteBuffer(new byte[2000]);
        BattleSerializer.serialize(battle, account, buffer);
        buffer.setPosition(0);
        Battle newBattle = BattleDeserializer.deserializeBattle(buffer, baseConfiguration, account);
        Assert.assertEquals(newBattle.getName(), battle.getName());
    }
}
