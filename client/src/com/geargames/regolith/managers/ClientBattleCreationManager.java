package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
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
 */
public class ClientBattleCreationManager {
    private ClientConfiguration configuration;
    private ClientStartBattleAnswer startBattleAnswer;
    private ClientCancelBattleAnswer cancelBattleAnswer;
    private ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer;
    private ClientCompleteGroupAnswer completeGroupAnswer;
    private ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer;

    public ClientBattleCreationManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        startBattleAnswer = new ClientStartBattleAnswer(configuration.getAccount(), configuration);
        cancelBattleAnswer = new ClientCancelBattleAnswer();
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
        completeGroupAnswer = new ClientCompleteGroupAnswer();
        evictAccountFromAllianceAnswer = new ClientEvictAccountFromAllianceAnswer();
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
     * Послать сообщение-уведомление о заполненности боевой группы бойцами.
     * @param group    заполняемая боевая группа
     * @param warriors массив бойцов, которые должны войти в боевую группу.
     * @return         сообщение-подтверждение класса ClientCompleteGroupAnswer
     */
    public ClientDeferredAnswer completeGroup(BattleGroup group, Warrior[] warriors) {
        completeGroupAnswer.setBattle(group.getAlliance().getBattle());
        return configuration.getNetwork().sendSynchronousMessage(
                new BattleGroupCompleteRequest(configuration, warriors, group), completeGroupAnswer);
    }

    /**
     * Послать сообщение-уведомление о роспуске боевой группы.
     * @param group распускаемая боевая группа
     * @return      сообщение-подтверждение класса ClientCompleteGroupAnswer
     */
    public ClientDeferredAnswer disbandGroup(BattleGroup group) {
        completeGroupAnswer.setBattle(group.getAlliance().getBattle());
        return configuration.getNetwork().sendSynchronousMessage(
                new BattleGroupDisbandRequest(configuration, group), completeGroupAnswer);
    }

    public void doNotListenToBattle(Battle battle) {
        configuration.getNetwork().sendMessage(new DoNotListenToBattleRequest(configuration, battle));
    }

}
