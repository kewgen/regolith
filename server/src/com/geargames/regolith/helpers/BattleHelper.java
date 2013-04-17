package com.geargames.regolith.helpers;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.ExitZone;

import java.io.*;
import java.util.*;

/**
 * User: mkutuzov
 * Date: 07.03.12
 */
public class BattleHelper {
    public static void putAllianceOnMap(BattleAlliance alliance, ExitZone exit) {
        BattleGroupCollection groups = alliance.getAllies();
        int xAmount = (exit.getxRadius() << 1) + 1;
        int yAmount = (exit.getyRadius() << 1) + 1;
        int yBegin = exit.getY() - exit.getyRadius();
        int xBegin = exit.getX() - exit.getxRadius();
        int cellsPerWarrior = (xAmount * yAmount) / (groups.size() * groups.get(0).getWarriors().size());
        if (cellsPerWarrior < 1) {
            throw new IllegalArgumentException("There is no enough free space for warriors to be spread.");
        }
        int n = 0;
        BattleMap battleMap = groups.get(0).getAlliance().getBattle().getMap();
        for (int j = 0; j < groups.size(); j++) {
            BattleGroup group = groups.get(j);
            for (int i = 0; i < group.getWarriors().size(); i++) {
                Warrior warrior = group.getWarriors().get(i);
                int y = yBegin + n / xAmount;
                int x = xBegin + (n % xAmount > 1 ? (n % xAmount - 1) : 0);
                WarriorHelper.putWarriorIntoMap(warrior, battleMap, x, y);
                n += cellsPerWarrior;
            }
        }
    }

    /**
     * Расположить отряды союзников на карте случайным образом и установить точку выхода отряда.
     */
    public static void spreadAlliancesOnTheMap(Battle battle) {
        BattleAlliance[] alliances = battle.getAlliances();
        for (int i = 0; i < alliances.length; i++) {
            putAllianceOnMap(alliances[i], battle.getMap().getExits()[i]);
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
                    cell.getPaths().put(alliance, (short) 0);
                    cell.getVisibilities().put(alliance, (short) 0);
                    cell.getVisited().put(alliance, false);
                }
            }
        }
    }

    /**
     * Проверка на то что битва заполнена бойцами.
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

    public static Warrior getWarriorInAccountById(Account account, int warriorId) throws RegolithException{
        ServerWarriorCollection warriors = (ServerWarriorCollection)account.getWarriors();

        for(Warrior warrior : warriors.getWarriors()){
            if(warrior.getId() == warriorId){
                return warrior;
            }
        }
        throw new RegolithException();
    }

}
