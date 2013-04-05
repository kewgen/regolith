package com.geargames.regolith.managers;

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
     * Послать сообщение-уведомление об исключении пользователя account, входящего в военный союз alliance, из
     * создаваемой битвы. Сообщение может посылать, как автор битвы, так и сам пользователь, чтобы выйти из битвы.
     * @param alliance
     * @param account
     */
    public ClientEvictAccountFromAllianceAnswer evictAccount(BattleAlliance alliance, Account account) throws Exception {
        //todo: Передавать не alliance + account, а только battleGroup
//        evictAccountFromAllianceAnswer.setBattle(alliance.getBattle());
        configuration.getNetwork().sendSynchronousMessage(
                new EvictAccountRequest(configuration, account, alliance), evictAccountFromAllianceAnswer, 100);
        return evictAccountFromAllianceAnswer;
    }

    /**
     * Послать сообщение-запрос о начале битвы.
     * @param author ссылка на аккаунт пользователя - инициатора начала битвы.
     */
    public ClientStartBattleAnswer startBattle(Account author) throws Exception{
        configuration.getNetwork().sendSynchronousMessage(new StartBattleRequest(configuration), startBattleAnswer, 100);
        return startBattleAnswer;
    }

    /**
     * Послать сообщение-запрос об отмене пользователем битвы.
     */
    public ClientCancelBattleAnswer cancelBattle() throws Exception {
        configuration.getNetwork().sendSynchronousMessage(new ClientCancelBattleRequest(configuration), cancelBattleAnswer, 100);
        return cancelBattleAnswer;
    }

    /**
     * Послать сообщение-запрос о попытке присоединиться к создаваемой битве.
     */
    public ClientJoinToBattleAllianceAnswer joinToAlliance(BattleAlliance alliance) throws Exception {
//        joinToBattleAllianceAnswer.setBattle(alliance.getBattle());
        configuration.getNetwork().sendSynchronousMessage(new ClientJoinToBattleAllianceRequest(configuration, alliance), joinToBattleAllianceAnswer, 100);
        return joinToBattleAllianceAnswer;
    }

    /**
     * Послать сообщение-уведомление о заполненности боевой группы бойцами.
     * @param group    заполняемая боевая группа
     * @param warriors массив бойцов, которые должны войти в боевую группу.
     * @return         сообщение-подтверждение класса ClientCompleteGroupAnswer
     */
    public ClientCompleteGroupAnswer completeGroup(BattleGroup group, Warrior[] warriors) throws Exception{
//        completeGroupAnswer.setBattle(group.getAlliance().getBattle());
		configuration.getNetwork().sendSynchronousMessage(new BattleGroupCompleteRequest(configuration, warriors, group),completeGroupAnswer, 100);
        return completeGroupAnswer;
    }

    /**
     * Послать сообщение-уведомление о роспуске боевой группы.
     * @param group распускаемая боевая группа
     * @return      сообщение-подтверждение класса ClientCompleteGroupAnswer
     */
    public ClientCompleteGroupAnswer disbandGroup(BattleGroup group) throws Exception {
//        completeGroupAnswer.setBattle(group.getAlliance().getBattle());
        configuration.getNetwork().sendSynchronousMessage(new BattleGroupDisbandRequest(configuration, group), completeGroupAnswer, 100);
        return completeGroupAnswer;
    }

    public void doNotListenToBattle(Battle battle) {
        configuration.getNetwork().sendMessage(new DoNotListenToBattleRequest(configuration, battle));
    }

}
