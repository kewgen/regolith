package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 27.06.12
 * Time: 14:42
 */
public class ClientBattleCreationManager {
    private ClientConfiguration configuration;
    private ClientStartBattleAnswer clientStartBattleAnswer;
    private ClientCancelBattleAnswer clientCancelBattleAnswer;
    private ClientJoinToBattleAllianceAnswer clientJoinToBattleAllianceAnswer;
    private ClientGroupReadyStateAnswer clientGroupReadyStateAnswer;
    private ClientEvictAccountFromAllianceAnswer clientEvictAccountFromAllianceAnswer;

    public ClientBattleCreationManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        clientStartBattleAnswer  = new ClientStartBattleAnswer(configuration.getAccount(), configuration.getBaseConfiguration());
        clientCancelBattleAnswer = new ClientCancelBattleAnswer();
        clientJoinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
        clientGroupReadyStateAnswer = new ClientGroupReadyStateAnswer();
        clientEvictAccountFromAllianceAnswer = new ClientEvictAccountFromAllianceAnswer();
    }

    /**
     * Послать сообщение-уведомление об исключении пользователя account, входящего в военный союз alliance, из
     * создаваемой битвы. Сообщение может посылать, как автор битвы, так и сам пользователь, чтобы выйти из битвы.
     * @param alliance
     * @param account
     */
    public ClientDeferredAnswer evictAccount(BattleAlliance alliance, Account account) {
        clientEvictAccountFromAllianceAnswer.setBattle(alliance.getBattle());
        return configuration.getNetwork().sendSynchronousMessage(
                new EvictAccountRequest(configuration, account, alliance), clientEvictAccountFromAllianceAnswer);
    }

    /**
     * Послать сообщение-запрос о начале битвы.
     * @param author ссылка на аккаунт пользователя - инициатора начала битвы.
     */
    public ClientDeferredAnswer startBattle(Account author) {
        return configuration.getNetwork().sendSynchronousMessage(new StartBattleRequest(configuration), clientStartBattleAnswer);
    }

    /**
     * Послать сообщение-запрос об отмене пользователем битвы.
     */
    public ClientDeferredAnswer cancelBattle() {
        return configuration.getNetwork().sendSynchronousMessage(new ClientCancelBattleRequest(configuration), clientCancelBattleAnswer);
    }

    /**
     * Послать сообщение-запрос о попытке присоединиться к создаваемой битве.
     */
    public ClientDeferredAnswer joinToAlliance(BattleAlliance alliance) {
        clientJoinToBattleAllianceAnswer.setBattle(alliance.getBattle());
        return configuration.getNetwork().sendSynchronousMessage(new ClientJoinToBattleAllianceRequest(configuration, alliance), clientJoinToBattleAllianceAnswer);
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
        clientGroupReadyStateAnswer.setBattle(group.getAlliance().getBattle());
        return configuration.getNetwork().sendSynchronousMessage(
                new GroupReadyStateRequest(configuration, Packets.GROUP_IS_READY, group), clientGroupReadyStateAnswer);
    }

    public ClientDeferredAnswer isNotReady(BattleGroup group) {
        clientGroupReadyStateAnswer.setBattle(group.getAlliance().getBattle());
        return configuration.getNetwork().sendSynchronousMessage(
                new GroupReadyStateRequest(configuration, Packets.GROUP_IS_NOT_READY, group), clientGroupReadyStateAnswer);
    }

}
