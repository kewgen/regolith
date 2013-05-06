package com.geargames.regolith.service;

import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.helpers.FightHelper;
import com.geargames.regolith.helpers.ServerBattleHelper;
import com.geargames.regolith.managers.CommonBattleManager;
import com.geargames.regolith.map.observer.Observer;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.answers.FinishBattleMessage;
import com.geargames.regolith.serializers.answers.ServerChangeActiveAllianceMessage;
import com.geargames.regolith.serializers.answers.ServerInitiallyObservedEnemies;
import com.geargames.regolith.service.clientstates.ClientState;
import com.geargames.regolith.service.state.ClientAtBattle;
import com.geargames.regolith.service.state.ClientCheckSumAwaiting;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import com.geargames.regolith.units.map.HumanElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class BattleSchedulerService {
    public static ThreadLocal<MicroByteBuffer> MICRO_BYTE_BUFFER = new ThreadLocal<MicroByteBuffer>() {
        @Override
        protected MicroByteBuffer initialValue() {
            return new MicroByteBuffer(new byte[1024 * 4]);
        }
    };

    private Logger logger = LoggerFactory.getLogger(BattleSchedulerService.class);
    private ScheduledExecutorService scheduler;
    private int beginDelay;
    private ClientWriter writer;

    public BattleSchedulerService() {
    }

    public void start() {
        BattleServiceConfiguration battleServiceConfiguration = BattleServiceConfigurationFactory.getConfiguration();
        scheduler = Executors.newScheduledThreadPool(1);
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

    public void add(final List<MessageToClient> messages) {
        logger.debug("add rough messages to writer size {} ", messages.size());
        scheduler.execute(new Runnable() {
            @Override
            public void run() {
                if (Thread.currentThread().isInterrupted()) {
                    logger.debug("Could not add an immediate message(the service has been interrupted) ");
                    return;
                }
                for (MessageToClient message : messages) {
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
                            logger.debug("Could not run a battle cycle (the service has been interrupted)" + serverBattle.getBattle().getName());
                            return;
                        }
                        RegolithConfiguration regolithConfiguration = BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration();
                        int index = serverBattle.getActive();
                        if (index != ServerBattle.NONE) {
                            for (BattleClient client : serverBattle.getAlliances().get(index)) {
                                client.lock();
                                client.setState(new ClientCheckSumAwaiting());
                                client.release();
                            }
                        } else {
                            logger.debug("Every client is trying to observe its neighbours");
                            Battle battle = serverBattle.getBattle();
                            BattleAlliance[] alliances = battle.getAlliances();
                            Observer observer = regolithConfiguration.getBattleConfiguration().getObserver();
                            logger.debug("alliances' amount: {}.", alliances.length);
                            for (BattleAlliance alliance : alliances) {
                                logger.debug("an alliance number {} is observing.", alliance.getNumber());

                                Set<HumanElement> enemies = ServerBattleHelper.allianceObservedBattle(alliance, observer, serverBattle.getHumanElements());

                                if (enemies.size() > 0) {
                                    List<SocketChannel> recipients = BattleServiceRequestUtils.getRecipients(serverBattle.getAlliances().get(alliance.getNumber()));
                                    writer.addMessageToClient(new BattleMessageToClient(recipients, new ServerInitiallyObservedEnemies(MICRO_BYTE_BUFFER.get(), enemies).serialize()));
                                }
                            }
                        }
                        index = (index + 1) % serverBattle.getAlliances().size();
                        for (BattleClient client : serverBattle.getAlliances().get(index)) {
                            client.lock();
                            ClientState state = client.getState();
                            if (state instanceof ClientCheckSumAwaiting) {
                                //todo клиент со своего последнего хода не подтвердил свои данные
                                CommonBattleManager.closeBattle(serverBattle);
                            } else {
                                logger.debug("a battle client {} has been activated", client.getAccount().getName());
                                FightHelper.resetAllianceScores(serverBattle.getBattle().getAlliances()[index], regolithConfiguration.getBaseConfiguration());
                                client.setState(new ClientAtBattle(serverBattle));
                            }
                            client.release();
                        }
                        serverBattle.setActive(index);
                        logger.debug("an alliance number {} has been activated", index);

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
                            logger.debug("sent [finish a battle]");
                            writer.addMessageToClient(new BattleMessageToClient(
                                    BattleServiceRequestUtils.getRecipients(serverBattle.getClients()),
                                    new FinishBattleMessage(new MicroByteBuffer(new byte[20]), winner).serialize()));
                        } else {
                            logger.debug("sent [change active alliance] {}", index);
                            writer.addMessageToClient(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(serverBattle.getClients()),
                                    new ServerChangeActiveAllianceMessage(new MicroByteBuffer(new byte[20]),
                                            serverBattle.getBattle().getAlliances()[serverBattle.getActive()]).serialize()));
                        }
                    }
                }, beginDelay, serverBattle.getBattle().getBattleType().getTurnTime(), TimeUnit.SECONDS));
    }

    public void remove(final ServerBattle serverBattle) {
        serverBattle.getFuture().cancel(true);
    }

}
