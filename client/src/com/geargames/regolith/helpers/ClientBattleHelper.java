package com.geargames.regolith.helpers;

import com.geargames.common.util.ArrayList;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.RegolithException;
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

    public static void observe(HumanElement unit, BattleConfiguration battleConfiguration) {
        battleConfiguration.getObserver().observe(unit);
    }

    public static void route(BattleCell[][] cells, HumanElement unit, BattleConfiguration battleConfiguration) {
        BattleMapHelper.resetShortestPath(cells, unit, unit.getCellX(), unit.getCellY(), battleConfiguration);
        BattleMapHelper.prepare(cells);
        battleConfiguration.getRouter().route(unit, battleConfiguration);
    }

    public static void trace(BattleCell[][] cells, HumanElement unit, int x, int y, BattleConfiguration battleConfiguration) {
        BattleMapHelper.resetShortestPath(cells, unit, unit.getCellX(), unit.getCellY(), battleConfiguration);
        BattleMapHelper.makeShortestRoute(cells, x, y, unit);
    }

    /**
     * Запросить сервер: переместить активного бойца клиентского приложения из того места где он находится в точку (x;y)
     * по кратчайшему пути.
     *
     * @param x
     * @param y
     */
    public static void move(BattleScreen screen, int x, int y) {

    }

    /**
     * Настроить координаты бойца соответсвующего unit в пикселах на экране screen.
     *
     * @param screen
     * @param unit
     */
    public static void initMapXY(BattleScreen screen, HumanElement unit) {
        Pair pair = ClientConfigurationFactory.getConfiguration().getCoordinateFinder().find(unit.getCellY(), unit.getCellX(), screen);
        unit.setMapX((short) pair.getX());
        unit.setMapY((short) pair.getY());
    }

//    /**
//     * Ускоренно перемещаем бойца противника unit в расчётную точку.
//     *
//     * @param cells
//     * @param unit
//     */
//    public static void immediateMoveEnemy(BattleCell[][] cells, HumanElement unit) {
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
//            HumanElement unit = units.get(i);
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
//            HumanElement unit = units.get(i);
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
    public static ClientHumanElementCollection getBattleUnits(Battle battle, Account account) {
        BattleAlliance[] alliances = battle.getAlliances();
        int id = account.getId();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            for (int j = 0; j < groups.size(); j++) {
                BattleGroup group = groups.get(j);
                if (group.getAccount().getId() == id) {
                    int groupSize = battle.getBattleType().getGroupSize();
                    ClientHumanElementCollection units = new ClientHumanElementCollection();
                    units.setElements(new Vector(groupSize));
                    WarriorCollection warriors = group.getWarriors();
                    for (int k = 0; k < groupSize; k++) {
                        HumanElement unit = new ClientHumanElement();
                        unit.setHuman(warriors.get(k));
                        unit.setDirection(Direction.UP_DOWN);
                        units.add(unit);
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
    public static ClientHumanElementCollection getAllyBattleUnits(Battle battle, Account account) {
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
                ClientHumanElementCollection units = new ClientHumanElementCollection();
                units.setElements(new Vector(groupSize * (battle.getBattleType().getAllianceSize() - 1)));
                for (int j = 0; j < groups.size(); j++) {
                    BattleGroup group = groups.get(j);
                    if (group.getAccount().getId() != id) {
                        WarriorCollection warriors = group.getWarriors();
                        for (int k = 0; k < groupSize; k++) {
                            HumanElement unit = new ClientHumanElement();
                            unit.setHuman(warriors.get(k));
                            unit.setDirection(Direction.UP_DOWN);
                            units.add(unit);
                        }
                    }
                }
                return units;
            }
        }
        ClientHumanElementCollection units = new ClientHumanElementCollection();
        units.setElements(new Vector(0));
        return units;
    }

    /**
     * Вернуть солдатиков для отрисовки на BattleScreen - противников по битве battle для account.
     *
     * @param battle
     * @param account
     * @return
     */
    public static ClientHumanElementCollection getEnemyBattleUnits(Battle battle, Account account) {
        BattleAlliance[] alliances = battle.getAlliances();
        int id = account.getId();
        boolean found = false;

        int groupSize = battle.getBattleType().getGroupSize();
        ClientHumanElementCollection units = new ClientHumanElementCollection();
        units.setElements(new Vector(groupSize * (battle.getBattleType().getAllianceSize()) * (battle.getBattleType().getAllianceAmount() - 1)));

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
                    HumanElement unit = new ClientHumanElement();
                    unit.setHuman(warriors.get(k));
                    unit.setDirection(Direction.UP_DOWN);
                    units.add(unit);
                }
            }
        }
        return units;
    }

//    public static void position(BattleUnit battleUnit, BattleScreen screen) {
//
//    }

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
        throw new RegolithException("Warrior was not found (id = " + warriorId + ")");
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
     * @param units
     * @param baseConfiguration
     */
    public static void resetActionScores(ClientHumanElementCollection units, BaseConfiguration baseConfiguration) {
        int size = units.size();
        for (int i = 0; i < size; i++) {
            Warrior warrior = (Warrior) units.get(i).getHuman();
            warrior.setActionScore(WarriorHelper.getMaxActionScores(warrior, baseConfiguration));
        }
    }

}
