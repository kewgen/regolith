package com.geargames.regolith.units.battle;

import com.geargames.regolith.service.BattleClient;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * User: mkutuzov
 * Date: 20.07.12
 */
public class ServerBattle {
    private int active;
    private Battle battle;
    private Collection<BattleGroup> groups;
    private Collection<BattleGroup> readyGroups;
    private List<Set<BattleClient>> alliances;
    private Set<BattleClient> clients;
    private ScheduledFuture future;

    public ServerBattle(Battle battle) {
        this.battle = battle;
    }

    public Collection<BattleGroup> getGroups() {
        return groups;
    }

    public void setGroups(Collection<BattleGroup> groups) {
        this.groups = groups;
    }

    public Collection<BattleGroup> getReadyGroups() {
        return readyGroups;
    }

    public void setReadyGroups(Collection<BattleGroup> readyGroups) {
        this.readyGroups = readyGroups;
    }

    public ScheduledFuture getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture future) {
        this.future = future;
    }

    public Battle getBattle() {
        return battle;
    }

    public Set<BattleClient> getClients() {
        return clients;
    }

    public void setClients(Set<BattleClient> clients) {
        this.clients = clients;
    }

    public List<Set<BattleClient>> getAlliances() {
        return alliances;
    }

    public void setAlliances(List<Set<BattleClient>> alliances) {
        this.alliances = alliances;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
