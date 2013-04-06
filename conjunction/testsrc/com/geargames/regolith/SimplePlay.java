package com.geargames.regolith;

import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: olga
 * Date: 28.06.12
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public class SimplePlay {
    private static SimpleService service;

    public static void main(String[] args) throws Exception {
        service = MainServiceManager.runMainService();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());
        Login login = new Login();
        login.setName("автор");
        login.setPassword("секрет");
        System.out.println("checking for a login name");

        ClientConfirmationAnswer confirm = commonManager.checkForName(login.getName());
        if (confirm.isConfirm()) {
            System.out.println("trying to createAmmunitionBag the login name");
            confirm = commonManager.create(login);
            if (!confirm.isConfirm()) {
                System.out.println("could not createAmmunitionBag an account");
                return;
            }
        }

        System.out.println("going to log in");

        ClientLoginAnswer loginAnswer = commonManager.login(login);
        if (loginAnswer.getError() != null) {
            System.err.println(loginAnswer.getError());
            return;
        }

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        if (clientConfiguration.getBaseWarriors() != null) {
            Warrior[] warriors = clientConfiguration.getBaseWarriors();
            byte amount = clientConfiguration.getBaseConfiguration().getInitWarriorsAmount();
            if (warriors.length < amount) {
                System.out.println("An amount of available warriors is not enough");
                return;
            }
            Warrior[] initWarriors = new Warrior[amount];
            for (int i = 0; i < amount; i++) {
                warriors[i].setName(warriors[i].getName() + "i");
                initWarriors[i] = warriors[i];
            }
            System.out.println("We are trying to hire warriors");
            ClientJoinBaseWarriorsAnswer clientJoinBaseWarriorsAnswer = baseWarriorMarketManager.hireWarrior(initWarriors);
            ClientWarriorCollection clientWarriorCollection = new ClientWarriorCollection(new Vector());
            if (clientJoinBaseWarriorsAnswer.isSuccess()) {
                for (Warrior warrior : clientJoinBaseWarriorsAnswer.getWarriors()) {
                    clientWarriorCollection.add(warrior);
                }
                account.setWarriors(clientWarriorCollection);
            } else {
                System.out.println("could not get a set base warriors");
                return;
            }
        }

        System.out.println("lets go to the battle market");

        confirm = baseManager.goBattleMarket();

        if (!confirm.isConfirm()) {
            System.err.println("could not go to the battle market");
            return;
        }

        System.out.println("browsing maps");

        BattleType battleType = BaseConfigurationHelper.findBattleTypeByArgs(2, 1, 1,
                ClientConfigurationFactory.getConfiguration().getBaseConfiguration());


        ClientBattleMapAnswer battleMap = battleMarketManager.browseRandomBattleMap(battleType);

        ClientListenToBattleAnswer listenToBattle = battleMarketManager.createBattle(battleMap.getBattleMap(), battleType);
        ;
        Battle battle = listenToBattle.getBattle();
        if (battle == null) {
            System.err.println("could not createAmmunitionBag his own battle.");
        }

        BattleGroup group;

        group = battleCreationManager.joinToAlliance(battle.getAlliances()[0]).getBattleGroup();
        if (group == null) {
            System.err.println("could not joint to his own battle.");
            return;
        }

        if (battle.getBattleType().getGroupSize() > account.getWarriors().size()) {
            System.out.println(account.getName() + " has no enough warriors for this battle.");
            return;
        }

        Warrior[] warriors = new Warrior[battle.getBattleType().getGroupSize()];
        for (int i = 0; i < warriors.length; i++) {
            warriors[i] = account.getWarriors().get(i);
        }


        ClientCompleteGroupAnswer complete = battleCreationManager.completeGroup(group, warriors);


        if (complete.getBattleGroup() == null) {
            System.err.println("could not put a group into his own battle.");
            return;
        }

        service.stopService();
    }

}
