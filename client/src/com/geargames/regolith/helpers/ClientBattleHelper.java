package com.geargames.regolith.helpers;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.BattleUnit;
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

    public static void observe(Warrior warrior, BattleConfiguration battleConfiguration){
        battleConfiguration.getObserver().observe(warrior);
    }

    public static void route(Warrior warrior, BattleConfiguration battleConfiguration){
        BattleMapHelper.resetShortestPath(warrior, warrior.getX(), warrior.getY());
        BattleMapHelper.prepare(warrior.getBattleGroup().getAlliance().getBattle().getMap());
        battleConfiguration.getRouter().route(warrior);
    }

    public static void trace(Warrior warrior, int x, int y){
        BattleMapHelper.resetShortestPath(warrior, warrior.getX(), warrior.getY());
        BattleMapHelper.makeShortestRoute(x, y, warrior);
        warrior.setDirection(WarriorHelper.getStepDirection(warrior, warrior.getBattleGroup().getAlliance().getBattle().getMap().getCells()));
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
     * @param screen
     * @param unit
     */
    public static void initMapXY(BattleScreen screen, BattleUnit unit){
        Pair pair = screen.getCoordinateFinder().find(unit.getWarrior().getY(), unit.getWarrior().getX(), screen);
        unit.setMapX(pair.getX());
        unit.setMapY(pair.getY());
    }


    /**
     * Создать игровую карту размера size*size.
     * @param size
     * @return
     */
    public static BattleMap createBattleMap(int size){
        BattleMap battleMap = new BattleMap();
        BattleCell[][] cells = new ClientBattleCell[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                cells[i][j] = new ClientBattleCell();
            }
        }
        battleMap.setCells(cells);
        return battleMap;
    }

    public static BattleAlliance findAllianceById(Battle battle, int allianceId) throws RegolithException {
        BattleAlliance[] alliances = battle.getAlliances();
        int length = alliances.length;

        for (int i = 0 ; i < length; i++) {
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
        ClientWarriorCollection warriors = (ClientWarriorCollection)account.getWarriors();
        int size = warriors.size();
        for (int i = 0; i < size; i++) {
            if (warriors.get(i).getId() == warriorId) {
                return warriors.get(i);
            }
        }
        throw new RegolithException();
    }

    public static Warrior findWarrior(Battle battle, int warriorId)throws RegolithException{
        BattleAlliance[] alliances = battle.getAlliances();

        int allianceAmount = battle.getBattleType().getAllianceAmount();
        int allianceSize = battle.getBattleType().getAllianceSize();
        int groupSize = battle.getBattleType().getGroupSize();

        for(int i = 0; i < allianceAmount; i++){
            BattleGroupCollection groups = alliances[i].getAllies();
            for(int j = 0; j < allianceSize; j++){
                WarriorCollection warriors = groups.get(j).getWarriors();
                for(int k = 0; k < groupSize; k++){
                    if(warriorId == warriors.get(k).getId()){
                        return warriors.get(k);
                    }
                }
            }
        }
        throw new RegolithException();
    }

}
