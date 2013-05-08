package com.geargames.regolith.network;

import com.geargames.awt.components.PObjectElement;
import com.geargames.common.logging.Debug;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.managers.ClientBaseWarriorMarketManager;
import com.geargames.regolith.managers.ClientBattleCreationManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.managers.ClientCommonManager;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

import java.util.Vector;

/**
 * User: abarakov
 * Date: 10.04.13
 */
public class ClientRequestHelper {

    /**
     * Залогинить клиента.
     */
    public static boolean clientLogon(boolean createIfNotExist) {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();

        Debug.debug("Is checking for a login name");
        try {
            Login login = clientConfiguration.getLogin();
            ClientConfirmationAnswer confirm = commonManager.checkForName(login.getName());
            if (createIfNotExist) {
                if (confirm.isConfirm()) {
                    Debug.debug("Trying to create an account");

                    confirm = commonManager.create(login);
                    if (!confirm.isConfirm()) {
                        Debug.critical("Could not create the account"); //to do: на сервер не должно отсылаться никаких сообщений по случаю ошибки
                        return false;
                    }
                }
            } else {
                // Аккаунт создаваться не будет, он должен существовать
                if (!confirm.isConfirm()) {
                    Debug.critical("Cannot login to account"); //to do: на сервер не должно отсылаться никаких сообщений по случаю ошибки
                    return false;
                }
            }

            Debug.debug("Going to login");

            ClientLoginAnswer loginAnswer = commonManager.login(login);
            if (loginAnswer.getError() != null) {
                Debug.critical("Cannot login to account: " + loginAnswer.getError()); //to do: на сервер не должно отсылаться никаких сообщений по случаю ошибки
                return false;
            }
            Debug.debug("The connection is established successfully");
            Account account = loginAnswer.getAccount();
            Debug.debug("Account id = " + account.getId());
            account.setName(login.getName());
            account.setPassword(login.getPassword());

            Debug.debug("Configuring the client...");
            clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
            clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());
            clientConfiguration.setAccount(account);
            return true;
        } catch (Exception e) {
            Debug.critical("Exception during the logon", e); //to do: на сервер не должно отсылаться никаких сообщений по случаю ошибки
            return false;
        }
    }

    /**
     * Нанять для игрока доступных бойцов.
     */
    public static boolean hireWarrior() {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        try {
            Account account = clientConfiguration.getAccount();
            if (account.getWarriors().size() <= 0) {
                if (clientConfiguration.getBaseWarriors() != null) {
                    Debug.debug("The client is trying to hire warriors");

                    Warrior[] warriors = clientConfiguration.getBaseWarriors();
                    byte amount = clientConfiguration.getBaseConfiguration().getInitWarriorsAmount();
                    if (warriors.length < amount) {
                        Debug.critical("An amount of available warriors is not enough for the client");
                        return false;
                    }

                    Warrior[] initWarriors = new Warrior[amount];
                    for (int i = 0; i < amount; i++) {
                        Warrior warrior = warriors[i];
                        initWarriors[i] = warrior;
                        warrior.setName(warrior.getName() + "I");
                    }

                    ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

                    Debug.debug("We are trying to hire warriors");
                    ClientJoinBaseWarriorsAnswer clientJoinBaseWarriorsAnswer = baseWarriorMarketManager.hireWarrior(initWarriors);
                    if (!clientJoinBaseWarriorsAnswer.isSuccess()) {
                        Debug.critical("The client could not get a set of base warriors");
                        return false;
                    }

                    ClientWarriorCollection clientWarriorCollection = new ClientWarriorCollection(new Vector());
                    for (Warrior warrior : clientJoinBaseWarriorsAnswer.getWarriors()) {
                        clientWarriorCollection.add(warrior);
                    }
                    account.setWarriors(clientWarriorCollection);
                }
            }
            return true;
        } catch (Exception e) {
            Debug.critical("Exception during recruitment warriors", e); //to do: на сервер не должно отсылаться никаких сообщений по случаю ошибки
            return false;
        }
    }

    /**
     * Вспомогательная функция. Подписывает игрока на получение обновлений битвы.
     *
     * @param battle - битва, на которую нужно подписаться;
     * @return - true, если подписались на битву успешно.
     */
    public static boolean listenToBattle(Battle battle, PObjectElement element, String textError) {
        ClientBattleMarketManager battleMarketManager = ClientConfigurationFactory.getConfiguration().getBattleMarketManager();
        try {
            Debug.debug("Trying to connect to the battle for listening (battle id = " + battle.getId() + ")...");
            ClientListenToBattleAnswer listenToBattleAnswer = battleMarketManager.listenToBattle(battle);
            if (!listenToBattleAnswer.isSuccess()) {
                Debug.error("ListenToBattle: Request rejected (battle id = " + battle.getId() + ")");
                NotificationBox.error(textError, element);
                return false;
            }
            Debug.debug("The client listens to the battle (battle id = " + battle.getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("ListenToBattle: Send request and receive answer is failed (battle id = " + battle.getId() + ")", e);
            NotificationBox.error(textError, element);
            return false;
        }
    }

    /**
     * Вспомогательная функция. Отписывает игрока от получения обновлений битвы.
     *
     * @param battle - битва, от которой нужно отписаться;
     * @return - true, если отписались от битвы успешно.
     */
    public static boolean doNotListenToBattle(Battle battle, PObjectElement element, String textError) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying to unsubscribe from the battle (battle id = " + battle.getId() + ")...");
            ClientConfirmationAnswer confirmationAnswer = battleCreationManager.doNotListenToBattle(battle);
            if (!confirmationAnswer.isConfirm()) {
                Debug.error("DoNotListenToBattle: Request rejected (battle id = " + battle.getId() + ")");
                NotificationBox.error(textError, element);
                return false;
            }
            Debug.debug("The client unsubscribe from battle (battle id = " + battle.getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("DoNotListenToBattle: Send request and receive answer is failed (battle id = " + battle.getId() + ")", e);
            NotificationBox.error(textError, element);
            return false;
        }
    }

    /**
     * Вспомогательная функция. Занимает за игроком боевую группу.
     *
     * @param battleGroup - боевая группа, которую нужно занять;
     * @return - true, если боевая группа была занята игроком.
     */
    public static boolean joinToAlliance(BattleGroup battleGroup, PObjectElement element, String textError) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying join to an alliance (alliance id = " + battleGroup.getAlliance().getId() + "; number = " + battleGroup.getAlliance().getNumber() + ")...");
            ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = battleCreationManager.joinToAlliance(battleGroup.getAlliance());
            if (!joinToBattleAllianceAnswer.isSuccess()) {
                Debug.error("JoinToAlliance: Request rejected (alliance id = " + battleGroup.getAlliance().getId() + ")");
                NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION, element);
                return false;
            }
            //todo: battleGroup и joinToBattleAllianceAnswer.getBattleGroup() разные объекты, потому battleGroup может иметь устаревшее содержимое после десериализации ответа
//               battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
            Debug.debug("Client joined to the alliance (alliance id = " + battleGroup.getAlliance().getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("JoinToAlliance: Send request and receive answer is failed (alliance id = " + battleGroup.getAlliance().getId() + ")", e);
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION, element);
            return false;
        }
    }

    public static boolean disbandBattleGroup(BattleGroup battleGroup, PObjectElement element, String textError) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying disband the battle group (battle group id = " + battleGroup.getId() + ")...");
            ClientCompleteGroupAnswer completeGroupAnswer = battleCreationManager.disbandGroup(battleGroup);
            if (!completeGroupAnswer.isSuccess()) {
                Debug.error("DisbandGroup: Request rejected (battle group id = " + battleGroup.getId() + ")");
                NotificationBox.error(textError, element);
                return false;
            }
            Debug.debug("The client disbanded the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("DisbandGroup: Send request and receive answer is failed (battle group id = " + battleGroup.getId() + ")", e);
            NotificationBox.error(textError, element);
            return false;
        }
    }

    public static boolean completeBattleGroup(BattleGroup battleGroup, Warrior[] initWarriors, PObjectElement element, String textError) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying to complete the battle group (battle group id = " + battleGroup.getId() + ")...");
            ClientCompleteGroupAnswer completeGroupAnswer = battleCreationManager.completeGroup(battleGroup, initWarriors);
            if (!completeGroupAnswer.isSuccess()) {
                Debug.critical("CompleteGroup: Request rejected (battle group id = " + battleGroup.getId() + ")");
                NotificationBox.error(textError, element);
                return false;
            }
            Debug.debug("Client completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("CompleteGroup: Send request and receive answer is failed (battle group id = " + battleGroup.getId() + ")", e);
            NotificationBox.error(textError, element);
            return false;
        }
    }

    /**
     * Вспомогательная функция. Исключает игрока из боевой группы.
     *
     * @param battleGroup - боевая группа, которую нужно освободить;
     * @return - true, если освобождение прошло успешно.
     */
    public static boolean evictAccountFromBattleGroup(BattleGroup battleGroup, PObjectElement element, String textError) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying to get out of the battle (" +
                    "battle id = " + battleGroup.getAlliance().getBattle().getId() + "; battle group id = " + battleGroup.getId() + ")...");
            ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = battleCreationManager.evictAccount(
                    battleGroup.getAlliance(), battleGroup.getAccount());
            if (!evictAccountFromAllianceAnswer.isSuccess()) {
                Debug.error("EvictAccount: Request rejected (battle group id = " + battleGroup.getId() + ")");
                NotificationBox.error(textError, element);
                return false;
            }
            Debug.debug("The client evicted from the alliance (" +
                    "battle group id = " + battleGroup.getId() +
                    "; account id = " + evictAccountFromAllianceAnswer.getAccount().getId() +
                    "; alliance id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("EvictAccount: Send request and receive answer is failed (battle group id = " + battleGroup.getId() + ")", e);
            NotificationBox.error(textError, element);
            return false;
        }
    }

    /**
     * Вспомогательная функция. Начинает битву.
     *
     * @return - true, если битва началась успешно.
     */
    public static ClientStartBattleAnswer startBattle(PObjectElement element, String textError) {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleCreationManager battleCreationManager = configuration.getBattleCreationManager();
        try {
            Debug.debug("Trying to start the battle (battle id = " + configuration.getBattle().getId() + ")...");
            ClientStartBattleAnswer startBattleAnswer = battleCreationManager.startBattle();
            if (!startBattleAnswer.isSuccess()) {
                Debug.error("StartBattle: Request rejected (battle id = " + configuration.getBattle().getId() + ")");
                NotificationBox.error(textError, element);
                return startBattleAnswer;
            }
            Debug.debug("The client begun the battle (battle id = " + startBattleAnswer.getBattle().getId() +
                    "; address: " + startBattleAnswer.getHost() + ":" + startBattleAnswer.getPort() + ")");
            return startBattleAnswer;
        } catch (Exception e) {
            Debug.error("StartBattle: Send request and receive answer is failed (battle id = " + configuration.getBattle().getId() + ")", e);
            NotificationBox.error(textError, element);
            return null;
        }
    }

    /**
     * Вспомогательная функция. Отменяет битву.
     *
     * @param battle - битва, которую нужно отменить;
     * @return - true, если отмена битвы прошла успешно.
     */
    public static boolean cancelBattle(Battle battle, PObjectElement element, String textError) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("Trying to cancelation the battle (battle id = " + battle.getId() + ")...");
            ClientCancelBattleAnswer cancelBattleAnswer = battleCreationManager.cancelBattle();
            if (!cancelBattleAnswer.isSuccess()) {
                Debug.error("CancelBattle: Request rejected (battle id = " + battle.getId() + ")");
                NotificationBox.error(textError, element);
                return false;
            }
            Debug.debug("The client canceled battle (battle id = " + battle.getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("CancelBattle: Send request and receive answer is failed (battle id = " + battle.getId() + ")", e);
            NotificationBox.error(textError, element);
            return false;
        }
    }

}
