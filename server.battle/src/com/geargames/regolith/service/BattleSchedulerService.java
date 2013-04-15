package com.geargames.regolith.service;

import com.geargames.regolith.managers.CommonBattleManager;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.answers.FinishBattleMessage;
import com.geargames.regolith.serializers.answers.ServerChangeActiveAllianceMessage;
import com.geargames.regolith.service.clientstates.ClientState;
import com.geargames.regolith.service.state.ClientAtBattle;
import com.geargames.regolith.service.state.ClientCheckSumAwaiting;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

public class BattleSchedulerService {
    private Logger logger = LoggerFactory.getLogger(BattleSchedulerService.class);
    private ScheduledExecutorService scheduler;
    private int actionTime;
    private int beginDelay;
    private ClientWriter writer;

    public BattleSchedulerService() {
    }

    public void start() {
        BattleServiceConfiguration battleServiceConfiguration = BattleServiceConfigurationFactory.getConfiguration();
        scheduler = Executors.newScheduledThreadPool(1);
        actionTime = battleServiceConfiguration.getActionTime();
        beginDelay = battleServiceConfiguration.getBeginDelay();
        writer = battleServiceConfiguration.getWriter();
    }

    public void stop() {
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("could not start scheduler", e);
        }
    }

    public void add(final List<MessageToClient> messages){
        scheduler.execute(new Runnable() {
            @Override
            public void run() {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                for(MessageToClient message : messages){
                    writer.addMessageToClient(message);
                }
            }
        });
    }

    public void add(final ServerBattle serverBattle) {
        serverBattle.setFuture(scheduler.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        if (Thread.currentThread().isInterrupted()) {
                            return;
                        }
                        int index = serverBattle.getActive();
                        for (BattleClient client : serverBattle.getAlliances().get(index)) {
                            client.lock();
                            client.setState(new ClientCheckSumAwaiting());
                            client.release();
                        }
                        index = (index + 1) % serverBattle.getAlliances().size();
                        for (BattleClient client : serverBattle.getAlliances().get(index)) {
                            client.lock();
                            ClientState state = client.getState();
                            if (state instanceof ClientCheckSumAwaiting) {
                                //todo клиент со своего последнего хода не подтвердил свои данные
                                CommonBattleManager.closeBattle(serverBattle);
                            } else {
                                client.setState(new ClientAtBattle(serverBattle));
                            }
                            client.release();
                        }
                        serverBattle.setActive(index);

                        Battle battle = serverBattle.getBattle();
                        ServerBattleType type = (ServerBattleType) (battle.getBattleType());
                        BattleAlliance winner = type.getWinner(battle);
                        if (type.haveToFinish(battle)) {
                            for (BattleGroup group : ((ServerBattleGroupCollection) winner.getAllies()).getBattleGroups()) {
                                for (Warrior warrior : ((ServerWarriorCollection) group.getWarriors()).getWarriors()) {
                                    warrior.setExperience(FightHelper.countExperience(warrior) * type.getScores());
                                    warrior.getVictimsDamages().clear();
                                }
                            }
                            writer.addMessageToClient(new BattleMessageToClient(
                                 BattleServiceRequestUtils.getRecipients(serverBattle.getClients()),
                                 new FinishBattleMessage(new MicroByteBuffer(new byte[20]), winner).serialize()));
                        } else {
                            writer.addMessageToClient(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(serverBattle.getClients()),
                                    new ServerChangeActiveAllianceMessage(new MicroByteBuffer(new byte[20]),
                                            serverBattle.getBattle().getAlliances()[serverBattle.getActive()]).serialize()));
                        }
                    }
                }, beginDelay, actionTime, TimeUnit.SECONDS));
    }

    public void remove(final ServerBattle serverBattle) {
        serverBattle.getFuture().cancel(true);
    }

}
