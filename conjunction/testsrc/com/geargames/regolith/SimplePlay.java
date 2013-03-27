package com.geargames.regolith;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.BattleMap;

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

    private static void retrieving(ClientDeferredAnswer answer, int count) throws Exception {
        int i = 0;
        while (answer.getAnswer() == null) {
            Thread.sleep(100);
            if (i++ == count) {
                throw new RegolithException("server is not answering");
            }
        }
        answer.getAnswer().deSerialize();
    }

    public static void main(String[] args) throws Exception {
        service = MainServiceManager.runMainService();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());
        Login login = new Login();
        login.setName("автор");
        login.setPassword("секрет");
        System.out.println("checking for a login name");
        ClientDeferredAnswer answer = commonManager.checkForName(login.getName());
        retrieving(answer, 1000);

        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        if (confirm.isConfirm()) {
            System.out.println("trying to createAmmunitionBag the login name");
            answer = commonManager.create(login);
            retrieving(answer, 1000);
            confirm = (ClientConfirmationAnswer) answer.getAnswer();
            if (!confirm.isConfirm()) {
                System.out.println("could not createAmmunitionBag an account");
                return;
            }
        }

        System.out.println("going to log in");

        answer = commonManager.login(login);
        retrieving(answer, 1000);

        ClientLoginAnswer loginAnswer = (ClientLoginAnswer) answer.getAnswer();
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
            answer = baseWarriorMarketManager.hireWarrior(initWarriors);
            retrieving(answer, 1000);
            ClientJoinBaseWarriorsAnswer clientJoinBaseWarriorsAnswer = (ClientJoinBaseWarriorsAnswer) answer.getAnswer();
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

        answer = baseManager.goBattleManager();
        retrieving(answer, 1000);

        confirm = (ClientConfirmationAnswer) answer.getAnswer();

        if (!confirm.isConfirm()) {
            System.err.println("could not go to the battle market");
            return;
        }

        System.out.println("browsing maps");

        answer = battleMarketManager.browseBattleMaps();
        retrieving(answer, 1000);

        ClientBrowseBattleMapsAnswer browseMaps = (ClientBrowseBattleMapsAnswer) answer.getAnswer();

        BattleMap[] maps = browseMaps.getBattleMaps();
        if (maps.length > 0) {
            answer = battleMarketManager.createBattle(maps[0], 0);
            retrieving(answer, 1000);
            ClientListenToBattleAnswer listenToBattle = (ClientListenToBattleAnswer) answer.getAnswer();
            Battle battle = listenToBattle.getBattle();
            if (battle == null) {
                System.err.println("could not createAmmunitionBag his own battle.");
            }

            BattleGroup group;
            answer = battleCreationManager.joinToAlliance(battle.getAlliances()[0]);
            retrieving(answer, 1000);

            group = ((ClientJoinToBattleAllianceAnswer) answer.getAnswer()).getBattleGroup();
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

            answer = battleCreationManager.completeGroup(group, warriors);
            retrieving(answer, 1000);

            confirm = (ClientConfirmationAnswer) answer.getAnswer();


            if (!confirm.isConfirm()) {
                System.err.println("could not put a group into his own battle.");
                return;
            }
        } else {
            System.out.print("there are no maps");
        }

        service.stopService();
    }

}
