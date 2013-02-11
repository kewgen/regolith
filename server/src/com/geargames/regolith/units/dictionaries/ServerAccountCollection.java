package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Account;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 03.05.12
 */
public class ServerAccountCollection extends AccountCollection {
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account get(int index) {
        return accounts.get(index);
    }

    public void add(Account account) {
        accounts.add(account);
    }

    public void insert(Account account, int index) {
        accounts.add(index, account);
    }

    public void set(Account account, int index) {
        accounts.set(index, account);
    }

    public void remove(int index) {
        accounts.remove(index);
    }

    public int size() {
        return accounts.size();
    }
}
