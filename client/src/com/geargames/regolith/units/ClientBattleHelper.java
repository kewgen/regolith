package com.geargames.regolith.units;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.BattleMapHelper;
import com.geargames.regolith.map.Pair;

/**
 * User: mkutuzov
 * Date: 27.02.12
 */
public class ClientBattleHelper {
    private static BattleConfiguration battleConfiguration;


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
     *Запросить сервер: переместить основного игрока клиентского приложения из того места где он находится в точку x;y
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
    public static void initMapXY(BattleScreen screen, Unit unit){
        Pair pair = screen.getCoordinateFinder().find(unit.getWarrior().getY(), unit.getWarrior().getX(), screen);
        unit.setMapX(pair.getX());
        unit.setMapY(pair.getY());
    }

    /**
     * Загрузить объект карта битвы по её имени name.
     * @param name
     * @return игровая карта
     */
    public static BattleMap getBattleMapByName(String name){
        return createBattleMap(20);
    }

    /**
     * Проверочный метод, только для разработки.
     * Создать битву с имененм name.
     * @param name
     * @param mapName имя карты
     * @return
     */
    public static Battle createBattle(String name, String mapName) {
        Battle battle = new Battle();
        battle.setName(name);

        battle.setAlliances(new BattleAlliance[2]);
        for(int i = 0; i < battle.getAlliances().length; i++){
             BattleAlliance alliance = new BattleAlliance();
             alliance.setAllies(new ClientBattleGroupCollection());
             alliance.setBattle(battle);
             battle.getAlliances()[i] = alliance;
        }
        BattleMap battleMap = getBattleMapByName(mapName);
        battle.setMap(battleMap);
        BattleType[] types = new BattleType[1];
        BattleType type = new BattleType();
        type.setAllianceAmount(2);
        type.setGroupSize(1);
        type.setAllianceSize(1);
        types[0] = type;
        battleMap.setPossibleBattleTypes(types);

        return battle;
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