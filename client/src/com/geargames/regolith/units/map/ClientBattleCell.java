package com.geargames.regolith.units.map;

import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mkutuzov
 * Date: 19.02.12
 * Клиентская реализация клетки игрового поля.
  */
public class ClientBattleCell extends BattleCell {
    private short visible;
    private short path;
    private byte visited;

    public short getVisibility(BattleAlliance battleAlliance) {
        return visible;
    }

    public short getOptimalPath(BattleAlliance battleAlliance) {
        return path;
    }

    public void setVisibility(BattleAlliance battleAlliance, short visibility) {
        visible = visibility;
    }

    public void setOptimalPath(BattleAlliance battleAlliance, short path) {
        this.path = path;
    }

    public boolean isVisited(BattleAlliance battleAlliance) {
        return visited != 0;
    }

    public void setVisited(BattleAlliance battleAlliance, boolean visited) {
        if(visited){
            this.visited = (byte)1;
        }else{
            this.visited = (byte)0;
        }
    }
}
