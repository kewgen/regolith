package com.geargames.regolith.units.map;

import com.geargames.regolith.units.battle.BattleAlliance;

import java.util.Map;

/**
 * User: mkutuzov
 * Date: 22.03.12
 */
public class ServerBattleCell extends BattleCell {
    private Map<BattleAlliance, Short> visibilities;
    private Map<BattleAlliance, Short> paths;
    private Map<BattleAlliance, Boolean> visited;

    public boolean isVisited(BattleAlliance battleAlliance) {
        return visited.get(battleAlliance);
    }

    public void setVisited(BattleAlliance battleAlliance, boolean visited) {
        this.visited.put(battleAlliance, visited);
    }

    public short getVisibility(BattleAlliance battleAlliance) {
        return visibilities.get(battleAlliance);
    }

    public short getOptimalPath(BattleAlliance battleAlliance) {
        return paths.get(battleAlliance);
    }

    public void setVisibility(BattleAlliance battleAlliance, short visibility) {
        visibilities.put(battleAlliance, visibility);
    }

    public void setOptimalPath(BattleAlliance battleAlliance, short path) {
        paths.put(battleAlliance, path);
    }

    public Map<BattleAlliance, Short> getVisibilities() {
        return visibilities;
    }

    public void setVisibilities(Map<BattleAlliance, Short> visibilities) {
        this.visibilities = visibilities;
    }

    public Map<BattleAlliance, Short> getPaths() {
        return paths;
    }

    public void setPaths(Map<BattleAlliance, Short> paths) {
        this.paths = paths;
    }

    public Map<BattleAlliance, Boolean> getVisited() {
        return visited;
    }

    public void setVisited(Map<BattleAlliance, Boolean> visited) {
        this.visited = visited;
    }
}


