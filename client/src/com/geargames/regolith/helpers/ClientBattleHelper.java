package com.geargames.regolith.helpers;

import com.geargames.common.util.ArrayList;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.map.router.Router;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.Unit;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.map.Pair;

import java.util.Vector;

/**
 * Users: mkutuzov, abarakov
 * Date: 27.02.12
 */
public class ClientBattleHelper {

    /**
     * Проставить стоимости в ОД достижимости окружающих точек карты, для бойца warrior.
     * <p/>
     * Предворяется зачисткой старого кратчайшего пути и пометкой окружающих ячеек как UN_ROUTED.
     *
     * @param warrior
     * @param router
     */
    public static void route(Warrior warrior, Router router) {
        BattleMapHelper.resetShortestPath(warrior, warrior.getX(), warrior.getY());
        BattleMapHelper.prepare(warrior.getBattleGroup().getAlliance().getBattle().getMap());
        router.route(warrior);
    }

    /**
     * На карте, с проставленными из начальной точки(где стоит warrior) стоимостями достижимости, выбрать кратчайший путь
     * из точки x:y в точку где стоит warrior и отметить каждую ячейку этого пути как часть кратчайшего пути.
     *
     * @param warrior
     * @param x
     * @param y
     */
    public static void trace(Warrior warrior, int x, int y) {
        BattleMapHelper.resetShortestPath(warrior, warrior.getX(), warrior.getY());
        BattleMapHelper.makeShortestRoute(x, y, warrior);
    }

    /**
     * Настроить координаты бойца соответсвующего unit в пикселах на экране screen.
     *
     * @param screen
     * @param unit
     */
    public static void initMapXY(BattleScreen screen, BattleUnit unit) {
        Pair pair = ClientConfigurationFactory.getConfiguration().getCoordinateFinder().find(unit.getUnit().getWarrior().getY(), unit.getUnit().getWarrior().getX(), screen);
        unit.setMapX(pair.getX());
        unit.setMapY(pair.getY());
    }


    /**
     * Ускоренно перемещаем бойца противника(warrior) в расчётную точку.
     *
     * @param warrior
     * @param map
     */
    public static void moveEnemy(Warrior warrior, BattleMap map) {
        BattleCell[][] cells = map.getCells();
        Direction direction;
        BattleAlliance alliance = warrior.getBattleGroup().getAlliance();
        do {
            direction = WarriorHelper.getStepDirection(warrior, cells);
            BattleMapHelper.resetShortestCell(cells[warrior.getX()][warrior.getY()], alliance, warrior);
            WarriorHelper.putWarriorIntoMap(warrior, map, warrior.getX() + direction.getX(), warrior.getY() + direction.getY());
        }
        while (direction != Direction.NONE);
    }

    /**
     * Быстро переместить созные войска battleUnits в место назначения на экране screen.
     *
     * @param battleUnits
     * @param battle
     * @param battleConfiguration
     * @param screen
     */
    public static void immediateMoveAllies(ArrayList battleUnits, Battle battle, BattleConfiguration battleConfiguration, BattleScreen screen) {
        int size = battleUnits.size();
        for (int i = 0; i < size; i++) {
            BattleUnit battleUnit = ((BattleUnit) battleUnits.get(i));
            Warrior warrior = battleUnit.getUnit().getWarrior();
            if (warrior.isMoving()) {
                WarriorHelper.move(warrior, battle.getMap().getCells(), NullStepListener.instance, battleConfiguration);
                ClientBattleHelper.initMapXY(screen, battleUnit);
            }
        }
    }


    /**
     * Быстро переместить врага на его место назначения на экране screen.
     *
     * @param enemies
     * @param battle
     * @param screen
     */
    public static void immediateMoveEnemies(ArrayList enemies, Battle battle, BattleScreen screen) {
        int size = enemies.size();
        for (int i = 0; i < size; i++) {
            BattleUnit battleUnit = (BattleUnit) enemies.get(i);
            Warrior warrior = battleUnit.getUnit().getWarrior();
            if (warrior.isMoving()) {
                ClientBattleHelper.moveEnemy(warrior, battle.getMap());
                ClientBattleHelper.initMapXY(screen, battleUnit);
            }
        }
    }


    /**
     * Вернуть солдатиков аккаунта account для BattleScreen из битвы battle.
     *
     * @param battle
     * @param account
     * @return
     */
    public static ArrayList getBattleUnits(Battle battle, Account account) {
        BattleAlliance[] alliances = battle.getAlliances();
        int id = account.getId();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            for (int j = 0; j < groups.size(); j++) {
                BattleGroup group = groups.get(j);
                if (group.getAccount().getId() == id) {
                    int groupSize = battle.getBattleType().getGroupSize();
                    ArrayList battleUnits = new ArrayList(groupSize);
                    WarriorCollection warriors = group.getWarriors();
                    for (int k = 0; k < groupSize; k++) {
                        BattleUnit battleUnit = new BattleUnit();
                        Unit unit = new Unit();
                        Warrior warrior = warriors.get(k);
                        warrior.setDirection(Direction.UP_DOWN);
                        unit.setWarrior(warrior);
                        unit.init();
                        battleUnit.setUnit(unit);
                        battleUnits.add(battleUnit);
                    }
                    return battleUnits;
                }
            }
        }
        return null;
    }

    /**
     * Вернуть солдатиков для  отрисовки на BattleScreen для всех союзников account по битве battle.
     *
     * @param battle
     * @param account
     * @return
     */
    public static ArrayList getAllyBattleUnits(Battle battle, Account account) {
        BattleAlliance[] alliances = battle.getAlliances();
        int groupSize = battle.getBattleType().getGroupSize();
        ArrayList battleUnits = new ArrayList(groupSize * (battle.getBattleType().getAllianceSize() - 1));
        int id = account.getId();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            boolean found = false;
            for (int j = 0; j < groups.size(); j++) {
                if (groups.get(j).getAccount().getId() == id) {
                    found = true;
                    break;
                }
            }
            if (found) {
                for (int j = 0; j < groups.size(); j++) {
                    BattleGroup group = groups.get(j);
                    if (group.getAccount().getId() != id) {
                        WarriorCollection warriors = group.getWarriors();
                        for (int k = 0; k < groupSize; k++) {
                            BattleUnit battleUnit = new BattleUnit();
                            Unit unit = new Unit();
                            Warrior warrior = warriors.get(k);
                            warrior.setDirection(Direction.UP_DOWN);
                            unit.setWarrior(warrior);
                            unit.init();
                            battleUnit.setUnit(unit);
                            battleUnits.add(battleUnit);
                        }
                    }
                }
                return battleUnits;
            }

        }
        return battleUnits;
    }

    /**
     * Вернуть солдатиков для отрисовки на BattleScreen -  противников по битве battle для account.
     *
     * @param battle
     * @param account
     * @return
     */
    public static ArrayList getEnemyBattleUnits(Battle battle, Account account) {
        BattleAlliance[] alliances = battle.getAlliances();
        int id = account.getId();
        boolean found = false;

        int groupSize = battle.getBattleType().getGroupSize();
        ArrayList battleUnits = new ArrayList(groupSize * (battle.getBattleType().getAllianceSize()) * (battle.getBattleType().getAllianceAmount() - 1));

        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            if (!found) {
                for (int j = 0; j < groups.size(); j++) {
                    if (groups.get(j).getAccount().getId() == id) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    continue;
                }
            }
            for (int j = 0; j < groups.size(); j++) {
                BattleGroup group = groups.get(j);
                WarriorCollection warriors = group.getWarriors();
                for (int k = 0; k < groupSize; k++) {
                    BattleUnit battleUnit = new BattleUnit();
                    Unit unit = new Unit();
                    Warrior warrior = warriors.get(k);
                    warrior.setDirection(Direction.UP_DOWN);
                    unit.setWarrior(warrior);
                    unit.init();
                    battleUnit.setUnit(unit);
                    battleUnits.add(battleUnit);
                }
            }
        }
        return battleUnits;
    }

    public static void position(BattleUnit battleUnit, BattleScreen screen) {

    }


    /**
     * Создать игровую карту размера size*size.
     *
     * @param size
     * @return
     */
    public static BattleMap createBattleMap(int size) {
        BattleMap battleMap = new BattleMap();
        BattleCell[][] cells = new ClientBattleCell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new ClientBattleCell();
            }
        }
        battleMap.setCells(cells);
        return battleMap;
    }

    public static BattleAlliance findAllianceById(Battle battle, int allianceId) throws RegolithException {
        BattleAlliance[] alliances = battle.getAlliances();
        int length = alliances.length;

        for (int i = 0; i < length; i++) {
            if (alliances[i].getId() == allianceId) {
                return alliances[i];
            }
        }
        throw new RegolithException();
    }

    public static AllyCollection getAllies(Battle battle, Account account) throws Exception {
        BattleAlliance alliance = findBattleAlliance(battle, account);
        BattleGroupCollection groups = alliance.getAllies();
        ClientAllyCollection allies = new ClientAllyCollection();
        allies.setAllies(new Vector());
        for (int i = 0; i < groups.size(); i++) {
            BattleGroup group = groups.get(i);
            WarriorCollection warriors = group.getWarriors();
            for (int j = 0; j < warriors.size(); j++) {
                allies.add(warriors.get(j));
            }
        }
        return allies;
    }

    public static BattleGroup findBattleGroupById(Battle battle, int battleGroupId) throws RegolithException {
        BattleAlliance[] alliances = battle.getAlliances();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            for (int j = 0; j < groups.size(); j++) {
                BattleGroup battleGroup = groups.get(j);
                if (battleGroup.getId() == battleGroupId) {
                    return battleGroup;
                }
            }
        }
        throw new RegolithException();
    }

    public static BattleAlliance findBattleAlliance(Battle battle, Account account) throws Exception {
        BattleGroup group = tryFindBattleGroupByAccountId(battle, account.getId());
        if (group != null) {
            return group.getAlliance();
        } else {
            throw new Exception();
        }
    }

    public static BattleGroup tryFindBattleGroupByAccountId(Battle battle, int accountId) {
        BattleAlliance[] alliances = battle.getAlliances();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection battleGroups = alliances[i].getAllies();
            for (int j = 0; j < battleGroups.size(); j++) {
                BattleGroup battleGroup = battleGroups.get(j);
                Account account = battleGroup.getAccount();
                if (account != null && account.getId() == accountId) {
                    return battleGroup;
                }
            }
        }
        return null;
    }

    public static Account findAccountById(Battle battle, int accountId) throws RegolithException {
        BattleAlliance[] alliances = battle.getAlliances();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection battleGroups = alliances[i].getAllies();
            for (int j = 0; j < battleGroups.size(); j++) {
                Account account = battleGroups.get(j).getAccount();
                if (account != null && account.getId() == accountId) {
                    return account;
                }
            }
        }
        throw new RegolithException();
    }

    public static Warrior findWarrior(Account account, int warriorId) throws RegolithException {
        ClientWarriorCollection warriors = (ClientWarriorCollection) account.getWarriors();
        int size = warriors.size();
        for (int i = 0; i < size; i++) {
            if (warriors.get(i).getId() == warriorId) {
                return warriors.get(i);
            }
        }
        throw new RegolithException();
    }

    public static Warrior findWarrior(Battle battle, int warriorId) throws RegolithException {
        BattleAlliance[] alliances = battle.getAlliances();

        int allianceAmount = battle.getBattleType().getAllianceAmount();
        int allianceSize = battle.getBattleType().getAllianceSize();
        int groupSize = battle.getBattleType().getGroupSize();

        for (int i = 0; i < allianceAmount; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            for (int j = 0; j < allianceSize; j++) {
                WarriorCollection warriors = groups.get(j).getWarriors();
                for (int k = 0; k < groupSize; k++) {
                    if (warriorId == warriors.get(k).getId()) {
                        return warriors.get(k);
                    }
                }
            }
        }
        throw new RegolithException();
    }

    public static BattleUnit findBattleUnitByWarrior(ArrayList battleUnits, Warrior warrior) {
        for (int i = 0; i < battleUnits.size(); i++) {
            BattleUnit battleUnit = ((BattleUnit) battleUnits.get(i));
            if (warrior == battleUnit.getUnit().getWarrior()) {
                return battleUnit;
            }
        }
        return null;
    }

    public static BattleUnit findBattleUnitByWarriorId(ArrayList units, int id) throws Exception {
        for (int i = 0; i < units.size(); i++) {
            BattleUnit unit = (BattleUnit) units.get(i);
            if (unit.getUnit().getWarrior().getId() == id) {
                return unit;
            }
        }
        throw new Exception("Battle unit for a warrior id " + id + " was not found.");
    }

    /**
     * Слить аккаунты боевых групп из битвы from в into.
     *
     * @param into
     * @param from
     * @throws Exception если битвы не совместимы по типу
     */
    public static void mergeBattlesAccounts(Battle into, Battle from) throws Exception {
        if (into.getBattleType() != from.getBattleType()) {
            throw new Exception();
        }
        BattleAlliance[] intoAlliances = into.getAlliances();
        BattleAlliance[] fromAlliances = from.getAlliances();
        int length = intoAlliances.length;
        if (length != fromAlliances.length) {
            throw new Exception();
        }
        for (int i = 0; i < length; i++) {
            BattleGroupCollection intoGroups = intoAlliances[i].getAllies();
            BattleGroupCollection fromGroups = fromAlliances[i].getAllies();
            for (int j = 0; j < intoGroups.size(); j++) {
                intoGroups.get(j).setAccount(fromGroups.get(j).getAccount());
            }
        }
    }

    /**
     * Заполнить очки действия на начало хода.
     *
     * @param battleUnits
     * @param baseConfiguration
     */
    public static void resetActionScores(ArrayList battleUnits, BaseConfiguration baseConfiguration) {
        int size = battleUnits.size();
        for (int i = 0; i < size; i++) {
            Warrior warrior = ((BattleUnit) battleUnits.get(i)).getUnit().getWarrior();
            warrior.setActionScore(WarriorHelper.getMaxActionScores(warrior, baseConfiguration));
        }
    }
}
