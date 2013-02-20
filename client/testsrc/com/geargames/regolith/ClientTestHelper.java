package com.geargames.regolith;

import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mikhail v. kutuzov
 * Date: 16.02.13
 * Time: 12:25
 */
public class ClientTestHelper {
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
        BattleMap battleMap = ClientBattleHelper.createBattleMap(20);
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

}
