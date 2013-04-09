package com.geargames.regolith.helpers;

import com.geargames.common.util.ArrayList;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.Unit;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.map.Pair;

/**
 * Users: mkutuzov, abarakov
 * Date: 27.02.12
 */
public class ClientBattleHelper {

    public static void observe(Warrior warrior, BattleConfiguration battleConfiguration) {
        battleConfiguration.getObserver().observe(warrior);
    }

    public static void route(Warrior warrior, BattleConfiguration battleConfiguration) {
        BattleMapHelper.resetShortestPath(warrior, warrior.getX(), warrior.getY());
        BattleMapHelper.prepare(warrior.getBattleGroup().getAlliance().getBattle().getMap());
        battleConfiguration.getRouter().route(warrior);
    }

    public static void trace(Warrior warrior, int x, int y) {
        BattleMapHelper.resetShortestPath(warrior, warrior.getX(), warrior.getY());
        BattleMapHelper.makeShortestRoute(x, y, warrior);
        //warrior.setDirection(WarriorHelper.getStepDirection(warrior, warrior.getBattleGroup().getAlliance().getBattle().getMap().getCells()));
    }

    /**
     * Запросить сервер: переместить основного игрока клиентского приложения из того места где он находится в точку x;y
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
    public static void initMapXY(BattleScreen screen, BattleUnit unit) {
        Pair pair = screen.getCoordinateFinder().find(unit.getUnit().getWarrior().getY(), unit.getUnit().getWarrior().getX(), screen);
        unit.setMapX(pair.getX());
        unit.setMapY(pair.getY());
    }


    /**
     * Вернуть солдатиков аккаунта account для BattleScreen из битвы battle.
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
                        unit.setWarrior(warriors.get(k));
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
                            unit.setWarrior(warriors.get(k));
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
     *  Вернуть солдатиков для отрисовки на BattleScreen -  противников по битве battle для account.
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
                    unit.setWarrior(warriors.get(k));
                    unit.init();
                    battleUnit.setUnit(unit);
                    battleUnits.add(battleUnit);
                }
            }
        }
        return battleUnits;
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

    public static BattleUnit getBattleUnitByWarrior(ArrayList battleUnits, Warrior warrior){
        for (int i = 0; i < battleUnits.size(); i++) {
            BattleUnit battleUnit = ((BattleUnit)battleUnits.get(i));
            if (warrior == battleUnit.getUnit().getWarrior()) {
                return battleUnit;
            }
        }
        return null;

    }

}
