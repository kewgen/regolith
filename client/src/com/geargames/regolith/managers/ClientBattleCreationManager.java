package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
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
    private ClientStartBattleAnswer startBattleAnswer;
    private ClientCancelBattleAnswer cancelBattleAnswer;
    private ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer;
    private ClientGroupReadyStateAnswer groupReadyStateAnswer;
    private ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer;
    private ClientConfirmationAnswer confirmationAnswer;

    public ClientBattleCreationManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        startBattleAnswer = new ClientStartBattleAnswer(configuration.getAccount(), configuration);
        cancelBattleAnswer = new ClientCancelBattleAnswer();
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
        groupReadyStateAnswer = new ClientGroupReadyStateAnswer();
        evictAccountFromAllianceAnswer = new ClientEvictAccountFromAllianceAnswer();
        confirmationAnswer = new ClientConfirmationAnswer();
    }

    /**
     * Послать сообщение-уведомление об исключении пользователя account, входящего в военный союз alliance, из
     * создаваемой битвы. Сообщение может посылать, как автор битвы, так и сам пользователь, чтобы выйти из битвы.
     * @param alliance
     * @param account
     */
    public ClientDeferredAnswer evictAccount(BattleAlliance alliance, Account account) {
        evictAccountFromAllianceAnswer.setBattle(alliance.getBattle());
        return configuration.getNetwork().sendSynchronousMessage(
                new EvictAccountRequest(configuration, account, alliance), evictAccountFromAllianceAnswer);
    }

    /**
     * Послать сообщение-запрос о начале битвы.
     * @param author ссылка на аккаунт пользователя - инициатора начала битвы.
     */
    public ClientDeferredAnswer startBattle(Account author) {
        return configuration.getNetwork().sendSynchronousMessage(new StartBattleRequest(configuration), startBattleAnswer);
    }

    /**
     * Послать сообщение-запрос об отмене пользователем битвы.
     */
    public ClientDeferredAnswer cancelBattle() {
        return configuration.getNetwork().sendSynchronousMessage(new ClientCancelBattleRequest(configuration), cancelBattleAnswer);
    }

    /**
     * Послать сообщение-запрос о попытке присоединиться к создаваемой битве.
     */
    public ClientDeferredAnswer joinToAlliance(BattleAlliance alliance) {
        joinToBattleAllianceAnswer.setBattle(alliance.getBattle());
        return configuration.getNetwork().sendSynchronousMessage(new ClientJoinToBattleAllianceRequest(configuration, alliance), joinToBattleAllianceAnswer);
    }

    public ClientDeferredAnswer completeGroup(BattleGroup group, Warrior[] warriors) {
        return configuration.getNetwork().sendSynchronousMessage(new BattleGroupCompleteRequest(configuration, warriors, group),confirmationAnswer);
    }

    public void doNotListenToBattle(Battle battle) {
        configuration.getNetwork().sendMessage(new DoNotListenToBattleRequest(configuration, battle));
    }

    public ClientDeferredAnswer isReady(BattleGroup group) {
        groupReadyStateAnswer.setBattle(group.getAlliance().getBattle());
        return configuration.getNetwork().sendSynchronousMessage(
                new GroupReadyStateRequest(configuration, Packets.GROUP_IS_READY, group), groupReadyStateAnswer);
    }

    public ClientDeferredAnswer isNotReady(BattleGroup group) {
        groupReadyStateAnswer.setBattle(group.getAlliance().getBattle());
        return configuration.getNetwork().sendSynchronousMessage(
                new GroupReadyStateRequest(configuration, Packets.GROUP_IS_NOT_READY, group), groupReadyStateAnswer);
    }

}
