package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;

/**
 * User: mikhail v. kutuzov
 * Date: 08.10.12
 * Time: 21:50
 * Тренировочная битва.
 */
public class TrainingBattle extends ServerBattleType {

    @Override
    public boolean haveToStart(Battle battle) {
        for (BattleAlliance alliance : battle.getAlliances()) {
            if (alliance == null) {
                return false;
            } else {
                ServerBattleGroupCollection groups = (ServerBattleGroupCollection) alliance.getAllies();
                BattleType battleType = battle.getBattleType();
                int size = groups.size();
                if (size != battleType.getAllianceSize()) {
                    return false;
                }
                int groupSize = battleType.getGroupSize();
                for (BattleGroup group : groups.getBattleGroups()) {
                    if (group.getWarriors().size() != groupSize) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean haveToFinish(Battle battle) {
        int deadAlliance = 0;
        for (BattleAlliance alliance : battle.getAlliances()) {
            ServerBattleGroupCollection groups = (ServerBattleGroupCollection) alliance.getAllies();
            int deadGroup = 0;
            for (BattleGroup group : groups.getBattleGroups()) {
                ServerWarriorCollection warriors = (ServerWarriorCollection) group.getWarriors();
                int deadMan = 0;
                for (Warrior warrior : warriors.getWarriors()) {
                    if (warrior.getHealth() <= 0) {
                        deadMan++;
                    }
                }
                if (warriors.size() == deadMan) {
                    deadGroup++;
                }
            }
            if (groups.size() == deadGroup) {
                deadAlliance++;
            }
        }

        return battle.getAlliances().length - deadAlliance <= 1;
    }

    @Override
    public BattleAlliance getWinner(Battle battle) {
        for (BattleAlliance alliance : battle.getAlliances()) {
            ServerBattleGroupCollection groups = (ServerBattleGroupCollection) alliance.getAllies();
            int deadGroup = 0;
            for (BattleGroup group : groups.getBattleGroups()) {
                ServerWarriorCollection warriors = (ServerWarriorCollection) group.getWarriors();
                int deadMan = 0;
                for (Warrior warrior : warriors.getWarriors()) {
                    if (warrior.getHealth() <= 0) {
                        deadMan++;
                    }
                }
                if (warriors.size() == deadMan) {
                    deadGroup++;
                }
            }
            if (groups.size() != deadGroup) {
                return alliance;
            }
        }
        return null;
    }
}
