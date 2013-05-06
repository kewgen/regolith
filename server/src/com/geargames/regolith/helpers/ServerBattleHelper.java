package com.geargames.regolith.helpers;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.ExitZone;

import java.io.*;
import java.util.*;

import com.geargames.regolith.map.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: mkutuzov
 * Date: 07.03.12
 */
public class ServerBattleHelper {
    private static Logger logger = LoggerFactory.getLogger(BattleHelper.class);

    public static void putAllianceOnMap(BattleCell[][] cells, ServerHumanElementCollection units, BattleAlliance alliance, ExitZone exit) throws RegolithException {
        BattleGroupCollection groups = alliance.getAllies();
        int xAmount = (exit.getxRadius() * 2) + 1;
        int yAmount = (exit.getyRadius() * 2) + 1;
        int xBegin = exit.getX() - exit.getxRadius();
        int yBegin = exit.getY() - exit.getyRadius();
        if (xBegin < 0) {
            xBegin = 0;
        }
        if (yBegin < 0) {
            yBegin = 0;
        }
        int mapWidth = cells[0].length;
        int mapHeight = cells.length;
        if (xBegin + xAmount > mapWidth) {
            xAmount = mapWidth - xBegin;
        }
        if (yBegin + yAmount > mapHeight) {
            yAmount = mapHeight - yBegin;
        }
        int cellsPerWarrior = (xAmount * yAmount) / (groups.size() * groups.get(0).getWarriors().size());
        if (cellsPerWarrior < 1) {
            throw new IllegalArgumentException("There is no enough free space for warriors to be spread.");
        }
        int n = 0;
        for (int j = 0; j < groups.size(); j++) {
            BattleGroup group = groups.get(j);
            for (int i = 0; i < group.getWarriors().size(); i++) {
                Warrior warrior = group.getWarriors().get(i);
                int x = xBegin + (n % xAmount > 1 ? (n % xAmount - 1) : 0);
                int y = yBegin + n / yAmount;
                HumanElement unit = BattleMapHelper.getHumanElementByHuman(units, warrior);
                WarriorHelper.putWarriorIntoMap(cells, unit, x, y);
                unit.setDirection(Direction.UP_DOWN); //todo: Класс ExitZone должен иметь поле direction, которое здесь и нужно использовать
                n += cellsPerWarrior;
            }
        }
    }

    /**
     * Расположить отряды союзников на карте случайным образом и установить точку выхода отряда.
     */
    public static void spreadAlliancesOnTheMap(Battle battle, ServerHumanElementCollection units) throws RegolithException {
        BattleAlliance[] alliances = battle.getAlliances();
        for (int i = 0; i < alliances.length; i++) {
            putAllianceOnMap(battle.getMap().getCells(), units, alliances[i], battle.getMap().getExits()[i]);
            alliances[i].setExit(battle.getMap().getExits()[i]);
        }
    }

    public static BattleGroup createBattleGroup(Account account) {
        BattleGroup group = new BattleGroup();
        ServerWarriorCollection warriors = new ServerWarriorCollection();
        warriors.setWarriors(new LinkedList<Warrior>());
        group.setWarriors(warriors);
        group.setAccount(account);
        return group;
    }

    /**
     * Создать битву с именем name.
     *
     * @param name
     * @param battleMap  карта
     * @param battleType тип битвы (выбирается из карты)
     * @return
     */
    public static Battle createBattle(String name, BattleMap battleMap, BattleType battleType) throws RegolithException {
        int exitsAmount = battleMap.getExits().length;

        int allianceAmount = battleType.getAllianceAmount();
        int allianceSize = battleType.getAllianceSize();
        int groupSize = battleType.getGroupSize();

        if (exitsAmount < allianceAmount) {
            throw new RegolithException();
        }
        for (ExitZone exitZone : battleMap.getExits()) {
            if ((exitZone.getxRadius() * 2 + 1) * (exitZone.getyRadius() * 2 + 1) < allianceSize * groupSize) {
                throw new RegolithException();
            }
        }

        Battle battle = new Battle();
        battle.setName(name);
        battle.setBattleType(battleType);
        battle.setAlliances(new BattleAlliance[allianceAmount]);
        for (int i = 0; i < allianceAmount; i++) {
            BattleAlliance alliance = new BattleAlliance();
            alliance.setAllies(new ServerBattleGroupCollection());
            alliance.setBattle(battle);
            alliance.setNumber((byte) i);
            for (int j = 0; j < allianceSize; j++) {
                BattleGroup group = new BattleGroup();
                group.setWarriors(new ServerWarriorCollection(new LinkedList<Warrior>()));
                WarriorHelper.addGroupIntoAlliance(alliance, group);
            }
            battle.getAlliances()[i] = alliance;
        }
        battle.setMap(battleMap);
        return battle;
    }

    public static void prepareBattle(Battle battle) {
        BattleCell[][] cells = battle.getMap().getCells();
        int allianceAmount = battle.getBattleType().getAllianceAmount();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                ServerBattleCell cell = (ServerBattleCell) cells[i][j];
                cell.setPaths(new HashMap<BattleAlliance, Short>());
                cell.setVisibilities(new HashMap<BattleAlliance, Short>());
                cell.setVisited(new HashMap<BattleAlliance, Boolean>());
                for (int k = 0; k < allianceAmount; k++) {
                    BattleAlliance alliance = battle.getAlliances()[k];
                    cell.setOptimalPath(alliance, (short) 0);
                    cell.setVisibility(alliance, (short) 0);
                    cell.setVisited(alliance, false);
                }
            }
        }
    }

    /**
     * Боевой союз осмотрелся в поисках жертвы.
     * @param alliance
     * @param observer
     * @return
     */
    public static Set<Ally> allianceObservedBattle(BattleAlliance alliance, Observer observer) {
        ServerBattleGroupCollection groups = (ServerBattleGroupCollection) alliance.getAllies();
        Set<Ally> enemies = new HashSet<Ally>();
        logger.debug("groups amount: {} ", groups.getBattleGroups().size());
        for (BattleGroup group : groups.getBattleGroups()) {
            ServerWarriorCollection warriors = (ServerWarriorCollection) group.getWarriors();
            for (Warrior warrior : warriors.getWarriors()) {
                logger.debug("a warrior named {} is observing a territory ", warrior.getName());
                enemies.addAll(((ServerAllyCollection) observer.observe(warrior)).getAllies());
            }
        }
        return enemies;
    }


    /**
     * Проверка на то, что битва заполнена бойцами.
     *
     * @param battle
     * @return
     */
    public static boolean isBattleComplete(Battle battle) {
        for (BattleAlliance alliance : battle.getAlliances()) {
            if (alliance == null) {
                return false;
            } else {
                BattleGroupCollection battleGroupCollection = alliance.getAllies();
                int size = battleGroupCollection.size();
                if (size != battle.getBattleType().getAllianceSize()) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (battleGroupCollection.get(i).getWarriors().size() != battle.getBattleType().getGroupSize()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Создать игровую карту размера size*size.
     *
     * @param size
     * @return
     */
    public static BattleMap createBattleMap(int size) {
        BattleMap battleMap = new BattleMap();
        battleMap.setCells(createBattleCells(size));
        return battleMap;
    }

    public static BattleCell[][] createBattleCells(int size) {
        ServerBattleCell[][] cells = new ServerBattleCell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new ServerBattleCell();
            }
        }
        return cells;
    }

    public static byte[] serializeBattleCells(BattleCell[][] cells) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new ObjectOutputStream(output).writeObject(cells);
        return output.toByteArray();
    }

    public static BattleCell[][] deserializeBattleCells(byte[] content) throws IOException, ClassNotFoundException {
        ByteArrayInputStream input = new ByteArrayInputStream(content);
        return (BattleCell[][]) (new ObjectInputStream(input).readObject());
    }

    public static BattleAlliance findAllianceById(Battle battle, int allianceId) {
        for (BattleAlliance battleAlliance : battle.getAlliances()) {
            if (battleAlliance.getId() == allianceId) {
                return battleAlliance;
            }
        }
        return null;
    }

    public static BattleGroup findBattleGroupByAccountId(Battle battle, int accountId) {
        for (BattleAlliance alliance : battle.getAlliances()) {
            ServerBattleGroupCollection groups = (ServerBattleGroupCollection) alliance.getAllies();
            for (BattleGroup battleGroup : groups.getBattleGroups()) {
                Account account = battleGroup.getAccount();
                if (account != null && account.getId() == accountId) {
                    return battleGroup;
                }
            }
        }
        return null;
    }

    public static BattleGroup findBattleGroupById(BattleAlliance alliance, int groupId) {
        ServerBattleGroupCollection groups = (ServerBattleGroupCollection) alliance.getAllies();
        for (BattleGroup battleGroup : groups.getBattleGroups()) {
            if (battleGroup.getId() == groupId) {
                return battleGroup;
            }
        }
        return null;
    }

    public static Warrior getWarriorInAccountById(Account account, int warriorId) throws RegolithException {
        ServerWarriorCollection warriors = (ServerWarriorCollection) account.getWarriors();

        for (Warrior warrior : warriors.getWarriors()) {
            if (warrior.getId() == warriorId) {
                return warrior;
            }
        }
        throw new RegolithException();
    }

    /**
     * Вернуть солдатиков располагающихся на карте для битвы battle.
     *
     * @param battle
     * @return
     */
    public static ServerHumanElementCollection getBattleUnits(Battle battle) {
        ServerHumanElementCollection units = new ServerHumanElementCollection();
        BattleType battleType = battle.getBattleType();
        units.setElements(new ArrayList<HumanElement>(battleType.getAllianceAmount() * battleType.getAllianceSize() * battleType.getGroupSize()));
        BattleAlliance[] alliances = battle.getAlliances();
        for (int i = 0; i < alliances.length; i++) {
            BattleGroupCollection groups = alliances[i].getAllies();
            for (int j = 0; j < groups.size(); j++) {
                BattleGroup group = groups.get(j);
                WarriorCollection warriors = group.getWarriors();
                int groupSize = battle.getBattleType().getGroupSize();
                for (int k = 0; k < groupSize; k++) {
                    HumanElement unit = new ServerHumanElement();
                    unit.setHuman(warriors.get(k));
                    unit.setDirection(Direction.UP_DOWN);
                    units.add(unit);
                }
            }
        }
        return units;
    }

}
