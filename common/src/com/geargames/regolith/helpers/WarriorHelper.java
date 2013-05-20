package com.geargames.regolith.helpers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.*;
import com.geargames.regolith.units.map.MoveOneStepListener;
import com.geargames.regolith.units.tackle.*;

/**
 * Users: mkutuzov, abarakov
 * Date: 04.03.12
 */
public class WarriorHelper {
    public static final long HOUR = 60 * 60 * 1000;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;

    /**
     * Вернуть радиус обзора бойца.
     *
     * @param warrior
     * @return
     */
    public static int getObservingRadius(Warrior warrior) {
        return 5;
    }

    /**
     * Вернуть радиус наибольшей достижимости бойца warrior.
     *
     * @param warrior
     * @return
     */
    public static int getRoutableRadius(Warrior warrior, BattleConfiguration battleConfiguration) {
        return warrior.getActionScore() / battleConfiguration.getActionFees().getMove();
    }

    public static int getReachableRadius(Warrior warrior, BattleConfiguration battleConfiguration) {
        return warrior.getActionScore() / battleConfiguration.getActionFees().getMove();
    }

    /**
     * Переместить бойца warrior в клетку (cellX;cellY) на карте.
     * ANNOTATION этот код работает с экземплярами warrior не используя их идентификаторов //todo: что это значит?
     *
     * @param warrior
     * @param cellX
     * @param cellY
     */
    public static void putWarriorIntoMap(BattleCell[][] cells, Warrior warrior, int cellX, int cellY) { //todo: cellX, cellY -> short
        BattleCell previousCell = cells[warrior.getCellX()][warrior.getCellY()];
        previousCell.removeElement(warrior);

        BattleCell nextCell = cells[cellX][cellY];
        warrior.setCellX((short) cellX);
        warrior.setCellY((short) cellY);

        nextCell.addElement(warrior);
    }

    /**
     * Сделать шаг на соседнюю c warrior клетку. Длина этого шага, по каждой из осей, не может превышать 1 клетку.
     *
     * @param warrior
     * @param stepX
     * @param stepY
     * @return обнаруженных бойцов противника
     */
    public static WarriorCollection step(BattleCell[][] cells, Warrior warrior, int stepX, int stepY, BattleConfiguration battleConfiguration) {
        BattleMapHelper.clearViewAround(cells, warrior);
        BattleMapHelper.resetShortestCell(cells[warrior.getCellX()][warrior.getCellY()], warrior);
        putWarriorIntoMap(cells, warrior, warrior.getCellX() + stepX, warrior.getCellY() + stepY);
        warrior.setActionScore((short) (warrior.getActionScore() - battleConfiguration.getActionFees().getMove()));
        System.out.println("A warrior '" + warrior.getName() + "' observe on step (" + warrior.getCellX() + ":" + warrior.getCellY() + ")");
        return battleConfiguration.getObserver().observe(warrior);
    }

    /**
     * Возвращаем направление.
     *
     * @param cells
     * @param warrior
     * @return
     */
    public static Direction getStepDirection(BattleCell[][] cells, Warrior warrior) {
        int sizeMinusOne = cells.length - 1;
        int xx = warrior.getCellX();
        int yy = warrior.getCellY();
        Direction direction;
        if (xx < sizeMinusOne && BattleMapHelper.isShortestPathCell(cells[xx + 1][yy], warrior)) {
            direction = Direction.LEFT_RIGHT;
        } else if (xx - 1 >= 0 && BattleMapHelper.isShortestPathCell(cells[xx - 1][yy], warrior)) {
            direction = Direction.RIGHT_LEFT;
        } else if (yy < sizeMinusOne && BattleMapHelper.isShortestPathCell(cells[xx][yy + 1], warrior)) {
            direction = Direction.UP_DOWN;
        } else if (yy - 1 >= 0 && BattleMapHelper.isShortestPathCell(cells[xx][yy - 1], warrior)) {
            direction = Direction.DOWN_UP;
        } else if (xx < sizeMinusOne && yy < sizeMinusOne && BattleMapHelper.isShortestPathCell(cells[xx + 1][yy + 1], warrior)) {
            direction = Direction.UP_DOWN_RIGHT;
        } else if (xx < sizeMinusOne && yy - 1 >= 0 && BattleMapHelper.isShortestPathCell(cells[xx + 1][yy - 1], warrior)) {
            direction = Direction.DOWN_UP_RIGHT;
        } else if (xx - 1 >= 0 && yy - 1 >= 0 && BattleMapHelper.isShortestPathCell(cells[xx - 1][yy - 1], warrior)) {
            direction = Direction.DOWN_UP_LEFT;
        } else if (xx - 1 >= 0 && yy < sizeMinusOne && BattleMapHelper.isShortestPathCell(cells[xx - 1][yy + 1], warrior)) {
            direction = Direction.UP_DOWN_LEFT;
        } else {
            direction = Direction.NONE;
        }
        return direction;
    }

    /**
     * Переместить бойца warrior из того места где он находится в точку (x;y) по кратчайшему пути.
     *
     * @param cells
     * @param warrior
     * @param x
     * @param y
     * @return обнаруженных бойцов противника
     */
    public static WarriorCollection move(BattleCell[][] cells, Warrior warrior, int x, int y,
                                         MoveOneStepListener listener, BattleConfiguration battleConfiguration) {
        BattleMapHelper.makeShortestRoute(cells, x, y, warrior);
        return move(cells, warrior, listener, battleConfiguration);
    }

    /**
     * Переместить бойца warrior по отмеченному на карте кратчайшему пути.
     *
     * @param cells
     * @param warrior
     * @param listener
     * @param battleConfiguration
     * @return обнаруженных бойцов противника
     */
    public static WarriorCollection move(BattleCell[][] cells, Warrior warrior, MoveOneStepListener listener,
                                         BattleConfiguration battleConfiguration) {
        int steps = getReachableRadius(warrior, battleConfiguration);
        for (int i = 0; i < steps; i++) {
            Direction direction = getStepDirection(cells, warrior);
            if (direction != Direction.NONE) {
                listener.onStep(warrior, direction.getX(), direction.getY());
                WarriorCollection detectedUnits = step(cells, warrior, direction.getX(), direction.getY(), battleConfiguration);
                if (detectedUnits.size() != 0) {
                    BattleMapHelper.resetShortestPath(cells,warrior,battleConfiguration);
                    return detectedUnits;
                }
            } else {
                break;
            }
        }
        return null;
    }

    /**
     * Перезарядить (забить магазин до предела) оружие бойца warrior патронами projectile до отказа.
     * В случае отсутствия патронов projectile в сумке бойца вернуть false и отменить перезарядку.
     * Боец пробует прежде всего сложить патроны из магазина в сумку и если там нет места, перезарядка отменяется
     * и возвращается false.
     *
     * @param warrior
     * @param projectile
     * @return
     */
    public static boolean rechargeWeapon(Warrior warrior, Projectile projectile) {
        return rechargeWeapon(warrior.getWeapon(), projectile, warrior.getAmmunitionBag());
    }

    public static boolean rechargeWeapon(Weapon weapon, Projectile projectile, AmmunitionBag bag) {
        if (!WeaponHelper.mayPut(weapon, projectile)) {
            return false;
        }
        if (!AmmunitionBagHelper.contains(bag, projectile)) {
            return false;
        }
        int need;
        if (weapon.getProjectile().getId() == projectile.getId()) {
            need = weapon.getWeaponType().getCapacity() - weapon.getLoad();
            weapon.setLoad((short) (weapon.getLoad() + AmmunitionBagHelper.putOut(bag, projectile, need)));
            return true;
        } else {
            need = weapon.getWeaponType().getCapacity();
            int loaded = weapon.getLoad();
            AmmunitionBagHelper.putIn(bag, weapon.getProjectile(), (short) loaded);
            weapon.setLoad((short) AmmunitionBagHelper.putOut(bag, projectile, need));
            weapon.setProjectile(projectile);
            return true;
        }
    }

    /**
     * Выложить патроны из оружия в сумку
     *
     * @param warrior
     * @param amount  количество патронов
     * @return удалось выложить - true(патроны убираются из оружия и кладутся в сумку), не удалось - false(количество патронов в оружии не меняется).
     */
    public static boolean unloadWeapon(Warrior warrior, byte amount) {
        Weapon weapon = warrior.getWeapon();
        if (weapon.getLoad() < amount) {
            return false;
        } else {
            AmmunitionBagHelper.putIn(warrior.getAmmunitionBag(), weapon.getProjectile(), amount);
            weapon.setLoad((short) (weapon.getLoad() - amount));
            if (weapon.getLoad() == 0) {
                weapon.setProjectile(null);
            }
            return true;
        }
    }

    /**
     * Использовать аптечку.
     *
     * @param warrior
     * @param medikit
     * @param regolithConfiguration
     */
    public static boolean useMedikit(Warrior warrior, Medikit medikit, RegolithConfiguration regolithConfiguration) {
        short max = getMaxHealth(warrior, regolithConfiguration.getBaseConfiguration());

        if (1 == AmmunitionBagHelper.putOut(warrior.getAmmunitionBag(), medikit, 1)) {
            int value = warrior.getHealth() + medikit.getValue();
            if (value <= max) {
                warrior.setHealth(value);
            } else {
                warrior.setHealth(max);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Добавить группу бойцов в военный союз.
     *
     * @param alliance
     * @param group
     */
    public static boolean addGroupIntoAlliance(BattleAlliance alliance, BattleGroup group) {
        if (alliance.getBattle().getBattleType().getAllianceSize() > alliance.getAllies().size()) {
            alliance.getAllies().add(group);
            group.setAlliance(alliance);
            numerateWarriors(alliance, group);
            return true;
        }
        return false;
    }

    /**
     * Раздать войнам группы group номера по их индексу в группе и военном союзе alliance.
     *
     * @param alliance
     * @param group
     */
    public static void numerateWarriors(BattleAlliance alliance, BattleGroup group) {
        WarriorCollection warriors = group.getWarriors();

        BattleGroupCollection groups = alliance.getAllies();
        int groupNumber = -1;
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i) == group) {
                groupNumber = i;
            }
        }
        if (groupNumber != -1) {
            if (warriors != null) {
                for (byte j = 0; j < warriors.size(); j++) {
                    warriors.get(j).setNumber(getNumberByIndex(groupNumber + j));
                }
            }
        }
    }

    /**
     * Вернуть номер бойца по его индексу в группе.
     *
     * @return
     */
    public static short getNumberByIndex(int index) {
        return (short) (1 << index);
    }

    /**
     * Вернуть возраст бойца.
     *
     * @param warrior
     * @return
     */
    public static long getAge(Warrior warrior) {
        return (System.currentTimeMillis() - warrior.getBirthDate().getTime()) / WEEK;
    }

    /**
     * Вернуть звание бойца, согласно уровню его очков опыта.
     *
     * @param warrior
     * @param baseConfiguration
     * @return
     */
    public static Rank getRank(Warrior warrior, BaseConfiguration baseConfiguration) {
        RankCollection baseRanks = baseConfiguration.getRanks();
        Rank result = null;
        for (int i = 0; i < baseRanks.size(); i++) {
            Rank rank = baseRanks.get(i);
            if (rank.getExperience() > warrior.getExperience()) {
                break;
            }
            result = rank;
        }
        return result;
    }

    /**
     * Метод поиска умения бойца warrior использовать данную категорию оружия получился жестко завязанным
     * на предположения:
     * 1. категории оружия грузятся в конфигурацию последовательно с id = 0 и далее + 1
     * 2. точно в таком же порядке грузится массив очков опыта по категориям
     *
     * @param warrior
     * @param category
     * @return
     */
    public static short getSkillScore(Warrior warrior, WeaponCategory category) {
        return warrior.getSkillScores()[category.getId() - 1];
    }

    /**
     * Возвращаем уровень навыка владения оружием бойца warrior.
     *
     * @param warrior
     * @param category
     * @return
     */
    public static Skill getSkill(Warrior warrior, WeaponCategory category) {
        return warrior.getSkills()[category.getId() - 1];
    }

    /**
     * Вернуть количество
     *
     * @param configuration
     * @return
     */
    public static int getAmountOfSkills(BaseConfiguration configuration) {
        return configuration.getSkills().size();
    }

    /**
     * Проставляем бойцу уровень навыка владения оружием в зависимости от очков опыта, выделенных наёмником на навык.
     *
     * @param warrior
     * @param baseConfiguration
     */
    public static void adjustSkills(Warrior warrior, BaseConfiguration baseConfiguration) {
        SkillCollection baseLevels = baseConfiguration.getSkills();
        WeaponCategoryCollection weaponCategories = baseConfiguration.getWeaponCategories();
        for (int i = 0; i < weaponCategories.size(); i++) {
            short skillLevel = getSkillScore(warrior, weaponCategories.get(i));
            warrior.getSkills()[i] = getSkillByExperience(skillLevel, baseConfiguration);
        }
    }

    /**
     * Вернуть навык владения оружием по количеству очков опыта.
     *
     * @param experience
     * @param baseConfiguration
     * @return
     */
    public static Skill getSkillByExperience(short experience, BaseConfiguration baseConfiguration) {
        SkillCollection baseLevels = baseConfiguration.getSkills();
        Skill skill = null;
        for (int j = 1; j < baseLevels.size(); j++) {
            if (baseLevels.get(j).getExperience() > experience) {
                skill = baseLevels.get(j - 1);
                break;
            }
        }
        if (skill == null) {
            skill = baseLevels.get(baseLevels.size() - 1);
        }
        return skill;
    }

    /**
     * Является ли боец warrior созником аккаунта account.
     *
     * @param warrior
     * @param account
     * @return
     */
    public static boolean isAlly(Warrior warrior, Account account) {
        BattleGroupCollection allies = warrior.getBattleGroup().getAlliance().getAllies();
        for (int i = 0; i < allies.size(); i++) {
            if (allies.get(i).getWarriors().get(0).getBattleGroup().getAccount().getId() == account.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Наибольший возможный уровень здоровья бойца warrior.
     *
     * @param warrior
     * @param configuration
     * @return
     */
    public static short getMaxHealth(Warrior warrior, BaseConfiguration configuration) {
        return (short) (configuration.getBaseHealth() + warrior.getVitality() * configuration.getVitalityStep());
    }

    /**
     * Количество очков действия бойца warrior на начало хода.
     *
     * @param warrior
     * @param configuration
     * @return
     */
    public static byte getMaxActionScores(Warrior warrior, BaseConfiguration configuration) {
        return (byte) (configuration.getBaseActionScore() + warrior.getSpeed() * configuration.getSpeedStep());
    }

    /**
     * Грузоподъемность бойца warrior.
     *
     * @param warrior
     * @param configuration
     * @return
     */
    public static short getStrength(Warrior warrior, BaseConfiguration configuration) {
        return (short) (configuration.getBaseStrength() + warrior.getStrength() * configuration.getStrengthStep());
    }

    /**
     * Ловкость бойца warrior(в процентах).
     *
     * @param warrior
     * @param configuration
     * @return
     */
    public static byte getCraftinessPercent(Warrior warrior, BaseConfiguration configuration) {
        return (byte) (configuration.getBaseCraftiness() + warrior.getCraftiness() * configuration.getCraftinessStep());
    }

    /**
     * Меткость бойца warrior (в процентах).
     *
     * @param warrior
     * @param configuration
     * @return
     */
    public static byte getMarksmanshipPercent(Warrior warrior, BaseConfiguration configuration) {
        return (byte) (configuration.getBaseMarksmanship() + warrior.getMarksmanship() * configuration.getMarksmanshipStep());
    }

    public static void addWarriorToGroup(BattleGroup group, Warrior warrior) {
        group.getWarriors().add(warrior);
        warrior.setBattleGroup(group);
    }

    /**
     * Принадлежит ли группа group пользователю account.
     *
     * @param group
     * @param account
     * @return
     */
    public static boolean isMine(BattleGroup group, Account account) {
        return group.getAccount().getId() == account.getId();
    }

    /**
     * Является ли боевой союз alliance союзником пользователя account.
     *
     * @param alliance
     * @param account
     * @return
     */
    public static boolean isAlly(BattleAlliance alliance, Account account) {
        for (int i = 0; i < alliance.getAllies().size(); i++) {
            BattleGroup group = alliance.getAllies().get(i);
            if (isMine(group, account)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Являются ли бойцы warrior1 и warrior2 союзниками.
     *
     * @param warrior1
     * @param warrior2
     * @return
     */
    public static boolean isAlly(Warrior warrior1, Warrior warrior2) {
        return warrior1.getBattleGroup().getAlliance().getId() == warrior2.getBattleGroup().getAlliance().getId();
    }

    /**
     * Положить в сумку броню или оружие.
     *
     * @param warrior
     * @param tackle
     * @param configuration
     * @return true если смогли положить
     */
    public static boolean putInToBag(Warrior warrior, StateTackle tackle, BaseConfiguration configuration) {
        short total = getTotalLoad(warrior);
        short strength = WarriorHelper.getStrength(warrior, configuration);
        if (strength < total + tackle.getWeight()) {
            return false;
        }
        BagHelper.putIntoBag(warrior.getBag(), tackle);
        return true;
    }

    /**
     * Вынуть из сумки броню или оружие.
     *
     * @param warrior
     * @param number
     * @return
     */
    public static StateTackle putOutOfBag(Warrior warrior, int number) {
        Bag bag = warrior.getBag();
        if (number < bag.getTackles().size()) {
            return BagHelper.putOut(bag, number);
        } else {
            return null;
        }
    }

    /**
     * Добавить аммуницию в сумку.
     * Здесь накладываем ограничения на грузоподъёмность бойца.
     *
     * @param warrior
     * @param ammunition
     * @param amount
     * @param configuration
     * @return количество добавленной аммуниции
     */
    public static short putInToBag(Warrior warrior, Ammunition ammunition, int amount, BaseConfiguration configuration) {
        AmmunitionBag bag = warrior.getAmmunitionBag();
        int current = getTotalLoad(warrior);
        int incoming = ammunition.getWeight() * amount;
        int ability = getStrength(warrior, configuration);
        if (ability < incoming + current) {
            amount = (ability - current) / ammunition.getWeight();
        }
        if (amount > 0) {
            AmmunitionBagHelper.putIn(bag, ammunition, amount);
        }
        return (short) amount;
    }


    /**
     * Вынуть из сумки бойца warrior содержимое и надеть на него.
     * <p/>
     * Действия внутри бойца не вызывают проверку на загрузку.
     *
     * @param warrior
     * @param number
     * @return
     */
    public static StateTackle takeOnFromBag(Warrior warrior, int number) {
        Bag bag = warrior.getBag();
        StateTackle tackle = bag.getTackles().get(number);
        int weight = 0;
        switch (tackle.getType()) {
            case TackleType.ARMOR:
                Armor armor = (Armor) tackle;
                switch (armor.getArmorType().getBodyParticle()) {
                    case BodyParticles.HEAD:
                        Armor head = warrior.getHeadArmor();
                        if (head != null) {
                            weight = head.getWeight() - armor.getWeight();
                            bag.getTackles().set(head, number);
                        } else {
                            weight = -armor.getWeight();
                            bag.getTackles().remove(number);
                        }
                        warrior.setHeadArmor(armor);
                        break;
                    case BodyParticles.TORSO:
                        Armor torso = warrior.getTorsoArmor();
                        if (torso != null) {
                            weight = torso.getWeight() - armor.getWeight();
                            bag.getTackles().set(torso, number);
                        } else {
                            weight = -armor.getWeight();
                            bag.getTackles().remove(number);
                        }
                        warrior.setTorsoArmor(armor);
                        break;
                    case BodyParticles.LEGS:
                        Armor legs = warrior.getTorsoArmor();
                        if (legs != null) {
                            weight = legs.getWeight() - armor.getWeight();
                            bag.getTackles().set(legs, number);
                        } else {
                            weight = -armor.getWeight();
                            bag.getTackles().remove(number);
                        }
                        warrior.setLegsArmor(armor);
                        break;
                    default:
                        return null;
                }
                break;
            case TackleType.WEAPON:
                Weapon weapon = (Weapon) tackle;
                Weapon hand = warrior.getWeapon();
                if (hand != null) {
                    weight = hand.getWeight() - weapon.getWeight();
                    bag.getTackles().set(hand, number);
                } else {
                    weight = -weapon.getWeight();
                    bag.getTackles().remove(number);
                }
                warrior.setWeapon(weapon);
                break;
            default:
                return null;
        }
        bag.setWeight((short) (bag.getWeight() + weight));
        return tackle;
    }

    /**
     * Взять из внешней среды и надеть на воина броню или оружие.
     * <p/>
     * Действие выцзывает проверку на загрузку.
     *
     * @param warrior
     * @param stateTackle
     * @return
     */
    public static boolean takeOn(Warrior warrior, StateTackle stateTackle, BaseConfiguration baseConfiguration) {
        int summary = getTotalLoad(warrior);
        short strength = getStrength(warrior, baseConfiguration);
        if (strength >= stateTackle.getWeight() + summary) {
            switch (stateTackle.getType()) {
                case TackleType.ARMOR:
                    Armor armor = (Armor) stateTackle;
                    switch (armor.getArmorType().getBodyParticle()) {
                        case BodyParticles.HEAD:
                            if (warrior.getHeadArmor() != null) {
                                BagHelper.putIntoBag(warrior.getBag(), warrior.getHeadArmor());
                            }
                            warrior.setHeadArmor(armor);
                            break;
                        case BodyParticles.TORSO:
                            if (warrior.getTorsoArmor() != null) {
                                BagHelper.putIntoBag(warrior.getBag(), warrior.getTorsoArmor());
                            }
                            warrior.setTorsoArmor(armor);
                            break;
                        case BodyParticles.LEGS:
                            if (warrior.getLegsArmor() != null) {
                                BagHelper.putIntoBag(warrior.getBag(), warrior.getLegsArmor());
                            }
                            warrior.setLegsArmor(armor);
                            break;
                        default:
                            return false;
                    }
                    break;
                case TackleType.WEAPON:
                    if (warrior.getWeapon() != null) {
                        BagHelper.putIntoBag(warrior.getBag(), warrior.getWeapon());
                    }
                    warrior.setWeapon((Weapon) stateTackle);
                    break;
                default:
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Снять с воина в сумку броню или оружие.
     * <p/>
     * Действие не вызывает проверку на загрузку.
     *
     * @param warrior
     * @param tackle
     */
    public static void takeOffIntoBag(Warrior warrior, StateTackle tackle) {
        switch (tackle.getType()) {
            case TackleType.ARMOR:
                Armor armor = (Armor) tackle;
                switch (armor.getArmorType().getBodyParticle()) {
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
                Weapon weapon = (Weapon) tackle;
                warrior.setWeapon(null);
                break;
            default:
                return;
        }
        BagHelper.putIntoBag(warrior.getBag(), tackle);
    }

    /**
     * Подсчитаем загрузку бойца warrior надетым оружием и бронёй.
     *
     * @param warrior
     * @return вес
     */
    private static short calculateLoad(Warrior warrior) {
        Weapon weapon = warrior.getWeapon();
        Armor head = warrior.getHeadArmor();
        Armor torso = warrior.getTorsoArmor();
        Armor legs = warrior.getLegsArmor();
        return ((short) ((weapon != null ? weapon.getWeight() : 0)
                + (head != null ? head.getWeight() : 0)
                + (torso != null ? torso.getWeight() : 0)
                + (legs != null ? legs.getWeight() : 0)));
    }


    /**
     * Посчитать суммарную загрузку бойца.
     *
     * @param warrior
     * @return
     */
    public static short getTotalLoad(Warrior warrior) {
        return (short) (calculateLoad(warrior) + warrior.getBag().getWeight() + warrior.getAmmunitionBag().getWeight());
    }

    /**
     * Сложить патроны из окружающей среды в оружие.
     * <p/>
     * Действие вызывает проверку на загрузку бойца
     *
     * @param warrior
     * @param projectile
     * @param amount
     * @return количество оставшихся на руках патронов
     */
    public static int addProjectileIntoWeapon(Warrior warrior, Projectile projectile, int amount, BaseConfiguration baseConfiguration) {
        Weapon weapon = warrior.getWeapon();
        weapon.setProjectile(projectile);
        int may = (getStrength(warrior, baseConfiguration) - getTotalLoad(warrior)) / projectile.getWeight();
        int room = weapon.getWeaponType().getCapacity() - weapon.getLoad();
        may = may < room ? may : room;
        int rest = amount - may;
        if (rest >= 0) {
            weapon.setLoad((short) (weapon.getLoad() + may));
            return rest;
        } else {
            weapon.setLoad((short) (weapon.getLoad() + amount));
            return 0;
        }
    }

    /**
     * Проверить, мертвый ли боец. Вернет true, если боец мертвый.
     *
     * @param warrior
     * @return
     */
    public static boolean isDead(Warrior warrior) {
        return warrior.getHealth() <= 0;
    }


    /**
     * Есть ли очки действия чтоб поднять.
     *
     * @param warrior
     * @param configuration
     * @return
     */
    public static boolean mayPeekOrPut(Warrior warrior, BattleConfiguration configuration) {
        return warrior.getActionScore() >= configuration.getActionFees().getPickupTackle();
    }

    /**
     * Растратить очки действия на поднять\положить предмет.
     *
     * @param warrior
     * @param configuration
     */
    public static void payForPickOrPut(Warrior warrior, BattleConfiguration configuration) {
        warrior.setActionScore((short) (warrior.getActionScore() - configuration.getActionFees().getPickupTackle()));
    }

    /**
     * Хватит ли очков на прицельную стрельбу?
     *
     * @param warrior
     * @return
     */
    public static boolean isAbleToShootAccurately(Warrior warrior) {
        return warrior.getActionScore() - warrior.getWeapon().getWeaponType().getAccurateAction() >= 0;
    }

    /**
     * Хватит очков действия на быструю стрельбу?
     *
     * @param warrior
     * @return
     */
    public static boolean isAbleToShootQuickly(Warrior warrior) {
        return warrior.getActionScore() - warrior.getWeapon().getWeaponType().getQuickAction() >= 0;
    }

    /**
     * Вернет true, если боец может сесть.
     */
    public static boolean maySit(Warrior warrior, BattleConfiguration battleConfiguration) {
        return !warrior.isSitting() && //warrior.getLogic().isIdle() &&
                warrior.getActionScore() >= battleConfiguration.getActionFees().getSitOrStand();
    }

    /**
     * Вернет true, если боец может встать.
     */
    public static boolean mayStand(Warrior warrior, BattleConfiguration battleConfiguration) {
        return warrior.isSitting() && //warrior.getLogic().isIdle() &&
                warrior.getActionScore() >= battleConfiguration.getActionFees().getSitOrStand();
    }

    /**
     * Вернет true, если боец может "наспех" выстрелить.
     */
    public static boolean mayHastilyShot(Warrior warrior) {
        return /*warrior.getLogic().isIdle() && */isAbleToShootQuickly(warrior);
    }

    /**
     * Вернет true, если боец может прицельно выстрелить.
     */
    public static boolean mayAccurateShot(Warrior warrior) {
        return /*warrior.getLogic().isIdle() && */isAbleToShootAccurately(warrior);
    }

    /**
     * Приказать бойцу встать.
     */
    public static void stand(Warrior warrior, BattleConfiguration battleConfiguration) {
//        warrior.getLogic().stand();
        warrior.setActionScore((short) (warrior.getActionScore() - battleConfiguration.getActionFees().getSitOrStand()));
    }

    /**
     * Приказать бойцу сесть.
     */
    public static void sit(Warrior warrior, BattleConfiguration battleConfiguration) {
//        warrior.getLogic().sit();
        warrior.setActionScore((short) (warrior.getActionScore() - battleConfiguration.getActionFees().getSitOrStand()));
    }

    /**
     * Приказать бойцу сделать выстрел "наспех".
     */
    public static void doHastilyShot(Warrior warrior) {
//        warrior.getLogic().hastilyShot();
        warrior.setActionScore((short) (warrior.getActionScore() - warrior.getWeapon().getWeaponType().getQuickAction()));
    }

    /**
     * Приказать бойцу сделать прицельный выстрел.
     */
    public static void doAccurateShot(Warrior warrior) {
//        warrior.getLogic().accurateShot();
        warrior.setActionScore((short) (warrior.getActionScore() - warrior.getWeapon().getWeaponType().getAccurateAction()));
    }

}
