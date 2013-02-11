package com.geargames.regolith.units;

import com.geargames.regolith.units.dictionaries.AccountCollection;

/**
 * User: mkutuzov
 * Date: 19.02.12
 */
public class Clan extends Entity {
    private String name;
    private AccountCollection accounts;
    private Account leader;
    private Account assistant;
    private int money;
    private int regolith;

    public Account getLeader() {
        return leader;
    }

    public void setLeader(Account leader) {
        this.leader = leader;
    }

    public Account getAssistant() {
        return assistant;
    }

    public void setAssistant(Account assistant) {
        this.assistant = assistant;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getRegolith() {
        return regolith;
    }

    public void setRegolith(int regolith) {
        this.regolith = regolith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountCollection getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountCollection accounts) {
        this.accounts = accounts;
    }
}
