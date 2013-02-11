package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Account;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 03.05.12
 */
public class ClientAccountCollection extends AccountCollection {
    private Vector accounts;

    public Vector getAccounts() {
        return accounts;
    }

    public void setAccounts(Vector accounts) {
        this.accounts = accounts;
    }

    public Account get(int index) {
        return (Account)accounts.elementAt(index);
    }

    public void add(Account account) {
        accounts.addElement(account);
    }

    public void insert(Account account, int index) {
        accounts.insertElementAt(account, index);
    }

    public void set(Account account, int index) {
        accounts.setElementAt(account, index);
    }

    public void remove(int index) {
        accounts.remove(index);
    }

    public int size() {
        return accounts.size();
    }
}
