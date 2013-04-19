package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Account;

/**
 * User: mkutuzov
 * Date: 03.05.12
 */
public abstract class AccountCollection extends EntityCollection {
    public abstract Account get(int index);
    public abstract void add(Account account);
    public abstract void insert(Account account, int index);
    public abstract void set(Account account, int index);
}
