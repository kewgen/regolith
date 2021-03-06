package com.geargames.regolith.helpers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.map.router.Router;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleScreen;
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
     * Предворяется зачисткой старого кратчайшего пути и пометкой окружающих ячеек, как UN_ROUTED.
     *
     * @param warrior
     * @param router
     */
    public static void route(BattleCell[][] cells, Warrior warrior, Router router, BattleConfiguration battleConfiguration) {
        BattleMapHelper.resetShortestPath(cells, warrior, battleConfiguration);
        BattleMapHelper.prepare(cells);
        router.route(warrior, battleConfiguration);
    }

    /**
     * На карте, с проставленными из начальной точки (где стоит warrior) стоимостями достижимости, выбрать кратчайший путь
     * из точки (x;y) в точку, где стоит warrior и отметить каждую ячейку этого пути, как часть кратчайшего пути.
     *
     * @param cells
     * @param warrior
     * @param x
     * @param y
     */
    public static void trace(BattleCell[][] cells, Warrior warrior, int x, int y, BattleConfiguration battleConfiguration) {
        BattleMapHelper.resetShortestPath(cells, warrior, battleConfiguration);
        BattleMapHelper.makeShortestRoute(cells, x, y, warrior);
    }

    /**
     * Настроить координаты бойца соответствующего warrior в пикселах на экране screen.
     *
     * @param screen
     * @param warrior
     */
    public static void initMapXY(BattleScreen screen, ClientWarriorElement warrior) {
        Pair pair = ClientConfigurationFactory.getConfiguration().getCoordinateFinder().find(warrior.getCellX(), warrior.getCellY(), screen);
        warrior.setMapX((short) pair.getX());
        warrior.setMapY((short) pair.getY());
    }

//    /**
//     * Ускоренно перемещаем бойца противника unit в расчётную точку.
//     *
//     * @param cells
//     * @param unit
//     */
//    public static void immediateMoveEnemy(BattleCell[][] cells, WarriorElement unit) {
//        Direction direction;
//        do {
//            direction = WarriorHelper.getStepDirection(cells, unit);
//            BattleMapHelper.resetShortestCell(cells[unit.getCellX()][unit.getCellY()], unit.getHuman());
//            WarriorHelper.putWarriorIntoMap(cells, unit, unit.getCellX() + direction.getX(), unit.getCellY() + direction.getY());
//        } while (direction != Direction.NONE);
//    }
//
//    /**
//     * Быстро переместить созные войска battleUnits в место назначения на экране screen.
//     *
//     * @param units
//     * @param battle
//     * @param battleConfiguration
//     * @param screen
//     */
//    public static void immediateMoveAllies(ClientHumanElementCollection units, Battle battle,
//                                           BattleConfiguration battleConfiguration, BattleScreen screen) {
//        int size = units.size();
//        for (int i = 0; i < size; i++) {
//            WarriorElement unit = units.get(i);
//            if (unit.isMoving()) {
//                WarriorHelper.move(battle.getMap().getCells(), unit, NullStepListener.instance, battleConfiguration);
//                ClientBattleHelper.initMapXY(screen, unit);
//            }
//        }
//    }
//
//
//    /**
//     * Быстро переместить вражеских бойцов на их место назначения на экране screen.
//     *
//     * @param units
//     * @param battle
//     * @param screen
//     */
//    public static void immediateMoveEnemies(ClientHumanElementCollection units, Battle battle, BattleScreen screen) {
//        int size = units.size();
//        for (int i = 0; i < size; i++) {
//            WarriorElement unit = units.get(i);
//            if (unit.isMoving()) {
//                ClientBattleHelper.immediateMoveEnemy(battle.getMap().getCells(), unit);
//                ClientBattleHelper.initMapXY(screen, unit);
//            }
//        }
//    }


    /**
     * Вернуть солдатиков аккаунта account для BattleScreen из битвы battle.
     *
     * @param battle
     * @param account
     * @return
     */
    public static ClientWarriorCollection getGroupBattleUnits(Battle battle, Account account) {
        BattleAlliance[] alliances = battle.getAlliances();
        int id = account.getId();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            for (int j = 0; j < groups.size(); j++) {
                BattleGroup group = groups.get(j);
                if (group.getAccount().getId() == id) {
                    int groupSize = battle.getBattleType().getGroupSize();
                    ClientWarriorCollection units = new ClientWarriorCollection();
                    units.setWarriors(new Vector(groupSize));
                    WarriorCollection warriors = group.getWarriors();
                    for (int k = 0; k < groupSize; k++) {
                        units.add(warriors.get(k));
                    }
                    return units;
                }
            }
        }
        return null;
    }

    /**
     * Вернуть солдатиков для отрисовки на BattleScreen для всех союзников account по битве battle.
     *
     * @param battle
     * @param account
     * @return
     */
    public static ClientWarriorCollection getAllyBattleUnits(Battle battle, Account account) {
        BattleAlliance[] alliances = battle.getAlliances();
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
                int groupSize = battle.getBattleType().getGroupSize();
                ClientWarriorCollection units = new ClientWarriorCollection();
                units.setWarriors(new Vector(groupSize * (battle.getBattleType().getAllianceSize() - 1)));
                for (int j = 0; j < groups.size(); j++) {
                    BattleGroup group = groups.get(j);
                    if (group.getAccount().getId() != id) {
                        WarriorCollection warriors = group.getWarriors();
                        for (int k = 0; k < groupSize; k++) {
                            units.add(warriors.get(k));
                        }
                    }
                }
                return units;
            }
        }
        ClientWarriorCollection units = new ClientWarriorCollection();
        units.setWarriors(new Vector(0));
        return units;
    }

    /**
     * Вернуть солдатиков для отрисовки на BattleScreen - противников по битве battle для account.
     *
     * @param battle
     * @param account
     * @return
     */
    public static ClientWarriorCollection getEnemyBattleUnits(Battle battle, Account account) {
        BattleAlliance[] alliances = battle.getAlliances();
        int id = account.getId();
        boolean found = false;

        int groupSize = battle.getBattleType().getGroupSize();
        ClientWarriorCollection units = new ClientWarriorCollection();
        units.setWarriors(new Vector(groupSize * (battle.getBattleType().getAllianceSize()) * (battle.getBattleType().getAllianceAmount() - 1)));

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
                    units.add(warriors.get(k));
                }
            }
        }
        return units;
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

    /*
    public static HumanElementCollection getAllies(Battle battle, Account account) throws Exception {
        BattleAlliance alliance = findBattleAlliance(battle, account);
        BattleGroupCollection groups = alliance.getAllies();
        ClientHumanElementCollection allies = new ClientHumanElementCollection();
        allies.setElements(new Vector());
        for (int i = 0; i < groups.size(); i++) {
            BattleGroup group = groups.get(i);
            WarriorCollection warriors = group.getWarriors();
            for (int j = 0; j < warriors.size(); j++) {
                allies.add(warriors.get(j));
            }
        }
        return allies;
    }
    */

    public static BattleGroup findBattleGroupById(Battle battle, int id) throws RegolithException {
        BattleAlliance[] alliances = battle.getAlliances();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            for (int j = 0; j < groups.size(); j++) {
                BattleGroup battleGroup = groups.get(j);
                if (battleGroup.getId() == id) {
                    return battleGroup;
                }
            }
        }
        throw new RegolithException();
    }

    public static BattleGroup findBattleGroupInAllianceById(BattleAlliance alliance, int id) throws RegolithException {
        BattleGroupCollection groups = alliance.getAllies();
        for (int j = 0; j < groups.size(); j++) {
            BattleGroup battleGroup = groups.get(j);
            if (battleGroup.getId() == id) {
                return battleGroup;
            }
        }
        throw new RegolithException();
    }


    public static BattleAlliance findBattleAlliance(Battle battle, Account account) throws RegolithException {
        BattleGroup group = tryFindBattleGroupByAccountId(battle, account.getId());
        if (group != null) {
            return group.getAlliance();
        }
        throw new RegolithException();
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
        WarriorCollection warriors = account.getWarriors();
        int size = warriors.size();
        for (int i = 0; i < size; i++) {
            Warrior warrior = warriors.get(i);
            if (warrior.getId() == warriorId) {
                return warrior;
            }
        }
        throw new RegolithException();
    }

    public static Warrior findWarrior(Battle battle, int warriorId) throws RegolithException {
        BattleAlliance[] alliances = battle.getAlliances();

        int allianceAmount = battle.getBattleType().getAllianceAmount();
        int allianceSize = battle.getBattleType().getAllianceSize();

        for (int i = 0; i < allianceAmount; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            for (int j = 0; j < allianceSize; j++) {
                Warrior warrior = findWarriorInBattleGroup(groups.get(j), warriorId);
                if (warrior != null) {
                    return warrior;
                }
            }
        }
        throw new RegolithException("Warrior was not found (id = " + warriorId + ")");
    }

    public static Warrior findWarriorInBattleGroup(BattleGroup group, int warriorId) {
        WarriorCollection warriors = group.getWarriors();
        int size = warriors.size();
        for (int i = 0; i < size; i++) {
            Warrior warrior = warriors.get(i);
            if (warrior.getId() == warriorId) {
                return warrior;
            }
        }
        return null;
    }

    public static Warrior getWarriorById(ClientWarriorCollection collection, int warriorId) throws RegolithException {
        int size = collection.size();
        for (int i = 0; i < size; i++) {
            Warrior warrior = collection.get(i);
            if (warrior.getId() == warriorId) {
                return warrior;
            }
        }
        throw new RegolithException("A warrior with an id " + warriorId + " was not found.");
    }

    /**
     * Слить аккаунты боевых групп из битвы from в into.
     *
     * @param into
     * @param from
     * @throws RegolithException если битвы не совместимы по типу
     */
    public static void mergeBattlesAccounts(Battle into, Battle from) throws RegolithException {
        if (into.getBattleType() != from.getBattleType()) {
            throw new RegolithException();
        }
        BattleAlliance[] intoAlliances = into.getAlliances();
        BattleAlliance[] fromAlliances = from.getAlliances();
        int length = intoAlliances.length;
        if (length != fromAlliances.length) {
            throw new RegolithException();
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
     * @param units
     * @param baseConfiguration
     */
    public static void resetActionScores(ClientWarriorCollection units, BaseConfiguration baseConfiguration) {
        int size = units.size();
        for (int i = 0; i < size; i++) {
            Warrior warrior = units.get(i);
            warrior.setActionScore(WarriorHelper.getMaxActionScores(warrior, baseConfiguration));
        }
    }


    /**
     * Пометить ячейки из множества cells лежащие на прямой линии между 2-х точек как кратчайщий путь для warrior.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param cells
     * @param warrior
     */
    public static void makeEnemyFakePath(int x1, int y1, int x2, int y2, BattleCell[][] cells, Warrior warrior) {
        if (x1 <= x2) {
            markLineAsShortestPathByWarrior(x1, y1, x2, y2, cells, warrior);
        } else {
            markLineAsShortestPathByWarrior(x2, y2, x1, y1, cells, warrior);
        }
    }


    private static void markLineAsShortestPathByWarrior(int x1, int y1, int x2, int y2, BattleCell[][] cells, Warrior warrior) {
        double k = (double) (y2 - y1) / (double) (x2 - x1);
        double b = y1 - k * x1;
        int length = x2 - x1;
        for (int i = 1; i < length - 1; i++) {
            int x = x1 + i;
            BattleMapHelper.setShortestPathCell(cells[x][(int) (k * x + b)], warrior);
        }
    }

}
