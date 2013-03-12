package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

/**
 * Боевая группа представляет собой список всех бойцов одного из игроков участвующего в битве.
 * User: mkutuzov
 * Date: 20.02.12
 */
public class BattleGroup extends Entity {
    private Account account;
    private BattleAlliance alliance;
    private WarriorCollection warriors;

    /**
     * Вернет ссылку на игрока, на стороне которого, сражается данный боец.
     * @return
     */
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Вернет ссылку на команду (союз игроков), в союзе с которой, сражается данная группа бойцов.
     * @return
     */
    public BattleAlliance getAlliance() {
        return alliance;
    }

    public void setAlliance(BattleAlliance alliance) {
        this.alliance = alliance;
    }

    /**
     * Вернет список бойцов, которые входят в данную боевую группу.
     * @return
     */
    public WarriorCollection getWarriors() {
        return warriors;
    }

    public void setWarriors(WarriorCollection warriors) {
        this.warriors = warriors;
    }

}
