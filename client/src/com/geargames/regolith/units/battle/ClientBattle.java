package com.geargames.regolith.units.battle;

/**
 * User: mikhail v. kutuzov
 * Date: 21.02.13
 * Time: 11:38
 */
public class ClientBattle extends Battle {
    private boolean updated;

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
