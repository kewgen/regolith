package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

/**
 * Боевая группа представляет собой бойцов одного клиентского приложения участвующего в битве.
 * User: mkutuzov
 * Date: 20.02.12
 */
public class BattleGroup extends Entity {
    private Account account;
    private BattleAlliance alliance;
    private WarriorCollection warriors;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BattleAlliance getAlliance() {
        return alliance;
    }

    public void setAlliance(BattleAlliance alliance) {
        this.alliance = alliance;
    }

    public WarriorCollection getWarriors() {
        return warriors;
    }

    public void setWarriors(WarriorCollection warriors) {
        this.warriors = warriors;
    }
}
