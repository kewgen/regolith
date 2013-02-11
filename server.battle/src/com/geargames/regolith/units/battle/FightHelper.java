package com.geargames.regolith.units.battle;

import com.geargames.regolith.*;
import com.geargames.regolith.map.observer.ShootBarriersFinder;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.map.observer.LineViewCaster;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.SubordinationDamage;
import com.geargames.regolith.units.map.BattleMapHelper;
import com.geargames.regolith.units.tackle.*;

import java.util.*;

/**
 * @author Mikhail_Kutuzov
 *         created: 16.03.12  14:28
 */
public class FightHelper {
    private static Map<Warrior, Random> shootProbability = new HashMap<Warrior, Random>();
    private static Random random = new Random();

    private static int getRealDamage(Warrior victim, int damage) {
        return victim.getHealth() - damage > 0 ? damage : victim.getHealth();
    }

    /**
     *
     * @param victor
     * @param vanquished
     * @param battleConfiguration
     * @return Возвращает -1 в плохом случае =)
     */
    private static int getNormalDamageExperience(Warrior victor, Warrior vanquished, BattleConfiguration battleConfiguration) {
        int difference = vanquished.getRank().getId() - victor.getRank().getId();
        SubordinationDamage[] damages = battleConfiguration.getSubordinationDamage();
        for (int i = 0; i < damages.length; i++) {
            SubordinationDamage subDamage = damages[i];
            if (difference < subDamage.getMaxRankDifference() && difference >=  subDamage.getMinRankDifference()) {
                return subDamage.getDamage();
            }
        }
        return -1;
    }

    private static int getVictimDamage(Warrior hunter, Warrior victim) {
        Hashtable victimDamages = hunter.getVictimsDamages();
        if (victimDamages.contains(victim)) {
            return (Integer) victimDamages.get(victim);
        } else {
            victimDamages.put(victim, 0);
            return 0;
        }
    }

    /**
     * Боец hunter наносит повреждение в размере здоровья damage бойцу victim,
     * попутно, получая шанс по окончаниии битвы отхватить за это очки опыта.
     *
     * @param hunter
     * @param victim
     * @param damage
     */
    public static void hurt(Warrior hunter, Warrior victim, int damage) {
        BattleConfiguration battleConfiguration = BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration().getBattleConfiguration();
        int realDamage = getRealDamage(victim, damage);
        int killExperienceMultiplier = battleConfiguration.getKillExperienceMultiplier();
        victim.setHealth(victim.getHealth() - realDamage);
        int victimDamage = getVictimDamage(hunter, victim);
        if (victim.getHealth() <= 0) {
            hunter.getVictimsDamages().put(victim, killExperienceMultiplier * (victimDamage + realDamage));
        } else {
            hunter.getVictimsDamages().put(victim, victimDamage + realDamage);
        }
    }

    /**
     * Посчитать очки опыта бойца, начисленные ему, за повреждения нанесённые врагам.
     *
     * @param warrior
     * @return очки опыта
     */
    public static int countExperience(Warrior warrior) {
        BattleConfiguration battleConfiguration = BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration().getBattleConfiguration();
        Set<Map.Entry> damages = warrior.getVictimsDamages().entrySet();
        int experience = 0;
        for (Map.Entry damage : damages) {
            experience += (((Integer) damage.getValue()) / getNormalDamageExperience(warrior, (Warrior) damage.getKey(),battleConfiguration));
        }
        return experience;
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
     * Боец hunter стреляет в бойца victim. Вероятностная функция изменяет внутренние состояния объектов.
     *
     * @param hunter
     * @param victim
     * @param accurately = true - боец стреляет прицельно, иначе - false
     * @return true - если цель поражена, иначе - false
     */
    public static boolean shoot(Warrior hunter, Warrior victim, boolean accurately) {
        RegolithConfiguration regolithConfiguration = BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration();
        BattleConfiguration battleConfiguration = regolithConfiguration.getBattleConfiguration();
        BaseConfiguration baseConfiguration = regolithConfiguration.getBaseConfiguration();
        if ((accurately && !isAbleToShootAccurately(hunter)) || (!accurately && !isAbleToShootQuickly(hunter))) {
            return false;
        }
        Weapon weapon = hunter.getWeapon();
        boolean goal = false;
        if (WeaponHelper.mayShoot(weapon)) {
            weapon.onShoot(battleConfiguration);
            if (weapon.getState() > 0 || random.nextInt(100) >= 30) {
                BattleConfiguration configuration = battleConfiguration;
                BattleMap map = hunter.getBattleGroup().getAlliance().getBattle().getMap();
                WeaponType weaponType = hunter.getWeapon().getWeaponType();
                double distance = getDistance(hunter, victim);
                double distanceI = getDistanceShootProbabilityFix(weaponType, getBaseShootProbability(hunter, baseConfiguration), distance);
                Pair barrierCoordinates = findBarrier(hunter, victim, map);
                ShootProbability barriersProbability = getShootFixedByBarriers(hunter, victim, map, barrierCoordinates, battleConfiguration);
                double barriersI = barriersProbability.probability;
                double hunterI = hunter.isHalfLong() ? configuration.getSitHunterShootFix() : configuration.getStandHunterShootFix();
                double victimI = victim.isHalfLong() ? configuration.getSitVictimShootFix() : configuration.getStandVictimShootFix();
                double accuratenessI = accurately ? 1 : 0.7;

                int probabilityOfGoodShoot = (int) (distanceI * barriersI * hunterI * victimI * accuratenessI * 100);

                if (!shootProbability.containsKey(hunter)) {
                    shootProbability.put(hunter, new Random());
                }
                if (probabilityOfGoodShoot >= shootProbability.get(hunter).nextInt(100)) {
                    Armor torso = victim.getTorsoArmor();
                    Armor legs = victim.getLegsArmor();
                    Armor head = victim.getHeadArmor();
                    short torsoArmor = 0;
                    if (torso != null) {
                        torsoArmor = torso.getArmorType().getArmor();
                    }
                    short legsArmor = 0;
                    if (legs != null) {
                        legsArmor = legs.getArmorType().getArmor();
                    }
                    short headArmor = 0;
                    if (head != null) {
                        headArmor = head.getArmorType().getArmor();
                    }

                    int skillProbability = 100 * (hunter.getMarksmanship() / 100) * (hunter.getMarksmanship() / 100);
                    Armor armor;
                    if (barriersProbability.inLegs) {
                        if (skillProbability >= shootProbability.get(hunter).nextInt(100)) {
                            if (torsoArmor <= headArmor && torsoArmor <= legsArmor) {
                                armor = torso;
                            } else if (headArmor <= torsoArmor && headArmor <= legsArmor) {
                                armor = head;
                            } else {
                                armor = legs;
                            }
                        } else {
                            //TODO сдесь тело человека разделено на 3 равных части(надо переделать)
                            int part = random.nextInt(100);
                            if (part > 67) {
                                armor = head;
                            } else if (part > 33) {
                                armor = torso;
                            } else {
                                armor = legs;
                            }
                        }
                    } else {
                        if (skillProbability >= shootProbability.get(hunter).nextInt(100)) {
                            if (torsoArmor <= headArmor) {
                                armor = torso;
                            } else {
                                armor = head;
                            }
                        } else {
                            int part = random.nextInt(100);
                            if (part > 50) {
                                armor = head;
                            } else {
                                armor = torso;
                            }
                        }
                    }
                    int damage = FightHelper.getWeaponDamage(hunter, distance, regolithConfiguration);
                    int damageWI = (int) (damage * WeaponHelper.getWeaponStateInfluence(weapon));
                    int damageAI;
                    if (armor != null) {
                        damageAI = ArmorHelper.getArmorDamageInfluence(armor, damageWI, regolithConfiguration.getBaseConfiguration());
                        armor.onHit(damageWI, regolithConfiguration.getBattleConfiguration());
                    } else {
                        damageAI = damageWI;
                    }
                    hurt(hunter, victim, damageAI);
                    goal = true;
                }
                if (accurately) {
                    hunter.setActionScore((short) (hunter.getActionScore() - weaponType.getAccurateAction()));
                } else {
                    hunter.setActionScore((short) (hunter.getActionScore() - weaponType.getQuickAction()));
                }
            } else {
                short selfDamage = (short) ((weapon.getWeaponType().getMaxDamage().getOptDistance() * random.nextInt(50)) / 100);
                Armor armor;
                if (random.nextInt(100) > 50) {
                    armor = hunter.getHeadArmor();
                } else {
                    armor = hunter.getTorsoArmor();
                }
                int damage;
                if (armor != null) {
                    damage = ArmorHelper.getArmorDamageInfluence(armor, selfDamage, regolithConfiguration.getBaseConfiguration());
                    armor.onHit(selfDamage, regolithConfiguration.getBattleConfiguration());
                } else {
                    damage = selfDamage;
                }
                int health = hunter.getHealth() - damage;
                hunter.setHealth(health);
            }
        }
        return goal;
    }

    /**
     * Вернуть базовый урон, наносимый текущим типом оружия в руках бойца warrior, по цели на расстоянии distance.
     *
     * @param warrior
     * @param distance
     * @return
     */
    public static int getWeaponDamage(Warrior warrior, double distance, RegolithConfiguration regolithConfiguration ) {
        Weapon weapon = warrior.getWeapon();
        Projectile projectile = weapon.getProjectile();
        Skill skill = WarriorHelper.getSkill(warrior, weapon.getWeaponType().getCategory());

        int abilityMax = regolithConfiguration.getBattleConfiguration().getAbilityMax();
        int maxDamage = WeaponHelper.getMaxWeaponDamage(weapon, distance,regolithConfiguration.getBaseConfiguration());
        int minDamage = WeaponHelper.getMinWeaponDamage(weapon, distance, regolithConfiguration.getBaseConfiguration());
        int damageDifference = maxDamage - minDamage;

        int dmgMin = (int) ((minDamage + damageDifference * skill.getAction() / (abilityMax + 5)) * projectile.getCategory().getQuality());
        int dmgMax = (int) (maxDamage * projectile.getCategory().getQuality());

        return Util.getRandom(dmgMin, dmgMax);
    }


    /**
     * Вернуть базовую вероятность попадения бойца в цель в процентах.
     *
     * @param hunter
     * @return
     */
    public static double getBaseShootProbability(Warrior hunter, BaseConfiguration baseConfiguration) {
        Weapon weapon = hunter.getWeapon();
        WeaponType type = weapon.getWeaponType();
        Skill skill = WarriorHelper.getSkill(hunter, type.getCategory());
        int tmp = (hunter.getMarksmanship() + WeaponHelper.getWeaponAccuracy(weapon, baseConfiguration ) + (skill.getAction() / 2));
        return tmp > 100 ? 1.0 : tmp / 100.0;
    }

    /**
     * Вернуть поправку к вероятности попадания в зависимости от расстояния.
     *
     * @param weaponType
     * @param baseProbability
     * @param distance
     * @return
     */
    public static double getDistanceShootProbabilityFix(WeaponType weaponType, double baseProbability, double distance) {
        WeaponDistances distances = weaponType.getDistance();
        if (distance > distances.getMaxOptimal() && distance <= distances.getMax()) {
            return baseProbability - (0.1) * Math.pow(distance, 1.5);
        } else if (distance <= distances.getMaxOptimal() && distance >= distances.getMinOptimal()) {
            return baseProbability;
        } else if (distance < distances.getMinOptimal() && distance >= distances.getMin()) {
            return baseProbability + (1 - baseProbability) / Math.pow(1.5, distance - 1);
        } else {
            return 0;
        }
    }

    /**
     * Вернуть расстояние между двумя точками карты.
     *
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @return
     */
    public static double getDistance(int x0, int y0, int x1, int y1) {
        return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
    }

    /**
     * Вернуть расстояние между двумя бойцами.
     *
     * @param warrior0
     * @param warrior1
     * @return
     */
    public static double getDistance(Warrior warrior0, Warrior warrior1) {
        return Math.sqrt((warrior0.getX() - warrior1.getX()) * (warrior0.getX() - warrior1.getX()) + (warrior0.getY() - warrior1.getY()) * (warrior0.getY() - warrior1.getY()));
    }

    public static Pair findBarrier(Warrior hunter, Warrior victim, BattleMap map) {
        int x0 = hunter.getX();
        int y0 = hunter.getY();

        int x1 = victim.getX();
        int y1 = victim.getY();


        ShootBarriersFinder finder = new ShootBarriersFinder();

        if (Math.abs(y1 - y0) <= Math.abs(x1 - x0)) {
            if (x1 > x0) {
                LineViewCaster.instance.castViewRight(x0, y0, x1, y1, map, hunter, finder);
            } else if (x1 < x0) {
                LineViewCaster.instance.castViewLeft(x0, y0, x1, y1, map, hunter, finder);
            }
        } else {
            if (y1 > y0) {
                LineViewCaster.instance.castViewDown(x0, y0, x1, y1, map, hunter, finder);
            } else if (y0 < y1) {
                LineViewCaster.instance.castViewUp(x0, y0, x1, y1, map, hunter, finder);
            }
        }
        return finder.getCoordinates();
    }

    /**
     * Вернуть коэфициент влияния преград между бойцами: стрелком hunter и целью victim.
     *
     * @param hunter
     * @param victim
     * @param map
     * @param barriersCoordinates самая близкая(из наиболее высоких ) к victim преграда лежащая на линии выстрела
     * @return
     */
    public static ShootProbability getShootFixedByBarriers(Warrior hunter, Warrior victim, BattleMap map, Pair barriersCoordinates, BattleConfiguration battleConfiguration) {

        int x1 = victim.getX();
        int y1 = victim.getY();

        BattleCell[][] cells = map.getCells();
        ShootProbability probability = new ShootProbability();
        probability.inLegs = true;
        if (BattleMapHelper.isAimed(cells[x1][y1], hunter)) {
            Element element = cells[barriersCoordinates.getX()][barriersCoordinates.getY()].getElement();
            if (element.isHalfLong()) {
                double distance = getDistance(hunter, victim);
                int criticalBarrierPercent = battleConfiguration.getCriticalBarrierToVictimDistance();
                if (distance <= hunter.getWeapon().getWeaponType().getDistance().getMax()) {
                    double barrierDistance = getDistance(victim.getX(), victim.getY(), barriersCoordinates.getX(), barriersCoordinates.getY());
                    if (!victim.isHalfLong() && !hunter.isHalfLong()) {
                        if (barrierDistance * 100 / distance > criticalBarrierPercent) {
                            probability.probability = 1;
                            return probability;
                        } else {
                            probability.probability = 0.5 + 0.5 * barrierDistance * 100.0 / criticalBarrierPercent;
                            return probability;
                        }
                    } else if (victim.isHalfLong() && !hunter.isHalfLong()) {
                        if (barrierDistance * 100 / distance > criticalBarrierPercent) {
                            probability.probability = 1;
                            return probability;
                        } else {
                            probability.probability = 0.2 + 0.8 * barrierDistance * 100.0 / criticalBarrierPercent;
                            if (probability.probability < 0.5) {
                                probability.inLegs = false;
                            }
                            return probability;
                        }
                    } else if (!victim.isHalfLong() && hunter.isHalfLong()) {
                        if (Math.abs(hunter.getX() - barriersCoordinates.getX()) == 1 && Math.abs(hunter.getY() - barriersCoordinates.getY()) == 1) {
                            probability.probability = 1;
                            return probability;
                        } else {
                            probability.probability = 0.5;
                            probability.inLegs = false;
                            return probability;
                        }
                    } else {
                        if (Math.abs(hunter.getX() - barriersCoordinates.getX()) == 1 && Math.abs(hunter.getY() - barriersCoordinates.getY()) == 1) {
                            probability.probability = 1;
                            return probability;
                        } else {
                            probability.probability = 0.2;
                            probability.inLegs = false;
                            return probability;
                        }
                    }
                }
            } else {
                probability.probability = 0;
                return probability;
            }
        }
        return probability;
    }

    private static class ShootProbability {
        public double probability;
        public boolean inLegs;
    }
}
