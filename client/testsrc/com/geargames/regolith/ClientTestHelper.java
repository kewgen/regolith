package com.geargames.regolith;

import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.map.BattleMap;

import com.geargames.regolith.managers.ClientBaseWarriorMarketManager;
import com.geargames.regolith.managers.ClientCommonManager;
import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientJoinBaseWarriorsAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import junit.framework.Assert;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov, abarakov
 * Date: 16.02.13
 *
 * Модуль только для разработки!
 */
public class ClientTestHelper {

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        try {
            return answer.retrieve(100);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Залогинить клиента.
     */
    public static ClientLoginAnswer clientLogon(String accountName, String accountPassword, boolean createIfNotExist) {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();

        Login login = new Login();
        login.setName(accountName);
        login.setPassword(accountPassword);

        System.out.println("Is checking for a login name");

        ClientDeferredAnswer answer = commonManager.checkForName(accountName);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        if (createIfNotExist) {
            if (confirm.isConfirm()) {
                System.out.println("Trying to create an account");

                answer = commonManager.create(login);
                Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

                confirm = (ClientConfirmationAnswer) answer.getAnswer();
                Assert.assertTrue("Could not create the account", confirm.isConfirm());
            }
        } else {
            // Аккаунт создаваться не будет, он должен существовать
            Assert.assertFalse("Cannot login to account", confirm.isConfirm());
        }

        System.out.println("Going to login");

        answer = commonManager.login(login);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientLoginAnswer loginAnswer = (ClientLoginAnswer) answer.getAnswer();
        Assert.assertNull("Cannot login to account: " + loginAnswer.getError(), loginAnswer.getError());

        return loginAnswer;
    }

    public static BattleAlliance getFreeAlliance(Battle battle) {
        BattleAlliance alliance = null;
        for (BattleAlliance battleAlliance : battle.getAlliances()) {
            for (int i = 0; i < battleAlliance.getAllies().size(); i++) {
                if (battleAlliance.getAllies().get(i).getAccount() == null) {
                    alliance = battleAlliance;
                    break;
                }
            }
            if (alliance != null) {
                return alliance;
            }
        }
        return null;
    }

    /**
     * Нанять для игрока доступных бойцов.
     */
    public static void hireWarriorForClient(Account account) {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();

        if (account.getWarriors().size() <= 0) {
            if (clientConfiguration.getBaseWarriors() != null) {
                System.out.println("The client is trying to hire warriors");

                Warrior[] warriors = clientConfiguration.getBaseWarriors();
                byte amount = clientConfiguration.getBaseConfiguration().getInitWarriorsAmount();
                Assert.assertTrue("An amount of available warriors is not enough for the client", warriors.length >= amount);

                Warrior[] initWarriors = new Warrior[amount];
                for (int i = 0; i < amount; i++) {
                    Warrior warrior = warriors[i];
                    initWarriors[i] = warrior;
                    warrior.setName(warrior.getName() + "i");
                }

                ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

                ClientDeferredAnswer answer = baseWarriorMarketManager.hireWarrior(initWarriors);
                Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
                ClientJoinBaseWarriorsAnswer clientJoinBaseWarriorsAnswer = (ClientJoinBaseWarriorsAnswer) answer.getAnswer();
                Assert.assertTrue("The client could not get a set of base warriors", clientJoinBaseWarriorsAnswer.isSuccess());

                ClientWarriorCollection clientWarriorCollection = new ClientWarriorCollection(new Vector());
                for (Warrior warrior : clientJoinBaseWarriorsAnswer.getWarriors()) {
                    clientWarriorCollection.add(warrior);
                }
                account.setWarriors(clientWarriorCollection);
            }
        }
    }

    /**
     * Создать битву с именем name.
     * @param name
     * @param mapName имя карты
     * @return
     */
    public static Battle createBattle(String name, String mapName) {
        Battle battle = new Battle();
        battle.setName(name);

        battle.setAlliances(new BattleAlliance[2]);
        for(int i = 0; i < battle.getAlliances().length; i++){
            BattleAlliance alliance = new BattleAlliance();
            alliance.setAllies(new ClientBattleGroupCollection());
            alliance.setBattle(battle);
            battle.getAlliances()[i] = alliance;
        }
        BattleMap battleMap = ClientBattleHelper.createBattleMap(20);
        battle.setMap(battleMap);
        BattleType[] types = new BattleType[1];
        BattleType type = new BattleType();
        type.setAllianceAmount(2);
        type.setGroupSize(1);
        type.setAllianceSize(1);
        types[0] = type;
        battleMap.setPossibleBattleTypes(types);

        return battle;
    }

    public static void checkAsyncMessages() {
//        Manager.pause(1000); // задержка, чтобы пришли сообщения, которые могли прийти
        Assert.assertTrue("The client has unread messages",
                ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousMessagesSize() == 0);
    }


}
