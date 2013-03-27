package com.geargames.regolith.helpers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.units.*;
import com.geargames.regolith.units.base.*;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.tackle.StateTackle;

import java.io.*;
import java.util.*;

/**
 * User: mkutuzov
 * Date: 11.06.12
 */
public class MainServerHelper {
    public static final int ZERO_ID = 0;

    /**
     * Создать пользователя по умолчанию.
     * @param baseConfiguration
     * @param name
     * @param password
     * @return
     */
    public static Account createDefaultAccount(BaseConfiguration baseConfiguration, String name, String password) {
        Account account = new Account();
        account.setSecurity(new SecurityOperationManager());
        ServerWarriorCollection accountWarriors = new ServerWarriorCollection();
        accountWarriors.setWarriors(new LinkedList<Warrior>());
        account.setWarriors(accountWarriors);

        account.setName(name);
        account.setPassword(password);

        Base base = new Base();
        base.setAccount(account);
        account.setBase(base);

        base.setClearingShop(new ClearingShop());
        base.setHospital(new Hospital());
        base.setRestHouse(new RestHouse());
        base.setShootingRange(new ShootingRange());
        base.setStoreHouse(ServerBaseHelper.createStoreHouse(baseConfiguration));
        Bag bag = new Bag();
        ServerStateTackleCollection serverStateTackleCollection = new ServerStateTackleCollection(new LinkedList<StateTackle>());
        bag.setTackles(serverStateTackleCollection);
        base.getStoreHouse().setBag(bag);
        AmmunitionBag ammunitionBag = ServerHelper.createAmmunitionBag(baseConfiguration);
        base.getStoreHouse().setAmmunitionBag(ammunitionBag);
        base.setTrainingCenter(new TrainingCenter());
        base.setWorkShop(new WorkShop());
        account.setMoney(baseConfiguration.getMoney());
        return account;
    }

    /**
     * Копировать бойца используя serialization.
     *
     * @param from
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Warrior copyThroughSerialization(Warrior from) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream(2048);
        new ObjectOutputStream(outBuffer).writeObject(from);
        return (Warrior)(new ObjectInputStream(new ByteArrayInputStream(outBuffer.toByteArray())).readObject());
    }

}
