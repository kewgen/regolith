package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientJoinBattleAnswer;
import com.geargames.regolith.serializers.answers.ClientStartBattleAnswer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Date: 27.06.12
 * Time: 14:42
 */
public class ClientBattleCreationManager {
    private ClientConfiguration configuration;
    private ClientStartBattleAnswer clientStartBattleAnswer;
    private ClientJoinBattleAnswer clientJoinBattleAnswer;

    public ClientBattleCreationManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        clientStartBattleAnswer = new ClientStartBattleAnswer(configuration.getAccount(), configuration.getBaseConfiguration());
        clientJoinBattleAnswer = new ClientJoinBattleAnswer();
    }

    /**
     * ??? Послать сообщение-уведомление об исключении пользователя account входящего в военный союз alliance из
     * создаваемой битвы. Сообщение может посылать, как автор битвы, так и сам пользователь, чтобы выйти из битвы.
     * @param alliance
     * @param account
     */
    public void evictAccount(BattleAlliance alliance, Account account) {
        configuration.getNetwork().sendMessage(new EvictAccountRequest(configuration, account, alliance));
    }

    /**
     * Послать сообщение-запрос о начале битвы.
     * @param author ссылка на аккаунт пользователя - инициатора начала битвы.
     */
    public ClientDeferredAnswer startBattle(Account author) {
        return configuration.getNetwork().sendSynchronousMessage(new StartBattleRequest(configuration), clientStartBattleAnswer);
    }

    /**
     * ??? Послать сообщение-уведомление о том, что пользователь отменил начало битвы.
     */
    public void cancelBattle() {
        configuration.getNetwork().sendMessage(new CancelBattleRequest(configuration));
    }

    /**
     * Послать сообщение-запрос о попытке присоединиться к создаваемой битве.
     */
<<<<<<< HEAD
    public ClientDeferredAnswer joinToAlliance(BattleAlliance alliance) {
        return configuration.getNetwork().sendSynchronousMessage(new JoinToAllianceRequest(configuration, alliance), clientJoinBattleAnswer);
=======
    public ClientDeferredAnswer joinToAlliance(BattleAlliance alliance, Account participant) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.JOIN_TO_BATTLE_ALLIANCE);
        ClientJoinBattleAnswer clientJoinBattleAnswer = new ClientJoinBattleAnswer(alliance.getBattle());
        messageLock.setMessage(clientJoinBattleAnswer);
        configuration.getNetwork().sendMessage(new JoinToAllianceRequest(configuration, alliance));

        return new ClientDeferredAnswer(clientJoinBattleAnswer);
>>>>>>> 3253015de3033fa47ec52497a0f41d7773cb488b
    }

    public ClientDeferredAnswer completeGroup(BattleGroup group, Warrior[] warriors) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.GROUP_COMPLETE);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new BattleGroupCompleteRequest(configuration, warriors, group));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }

    public void doNotListenToBattle(Battle battle) {
        configuration.getNetwork().sendMessage(new DoNotListenToBattleRequest(configuration, battle));
    }

    public ClientDeferredAnswer isReady(BattleGroup group) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.GROUP_IS_READY);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new GroupReadyStateRequest(configuration,Packets.GROUP_IS_READY, group));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }

    public ClientDeferredAnswer isNotReady(BattleGroup group) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.GROUP_IS_NOT_READY);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new GroupReadyStateRequest(configuration, Packets.GROUP_IS_NOT_READY, group));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }
}
