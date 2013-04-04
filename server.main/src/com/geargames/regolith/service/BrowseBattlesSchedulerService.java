package com.geargames.regolith.service;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.serializers.answers.ServerBrowseBattlesAnswer;
import com.geargames.regolith.units.battle.Battle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: mkutuzov
 * Date: 23.03.13
 * Класс написан из соображений, что внешнее изменение состояния [addListener, removeListener, addBattle] будет
 * происходить из одного внешнего потока. На текущий момент это поток обработки клиентских запросов.
 */
public class BrowseBattlesSchedulerService {
    private static Logger logger = LoggerFactory.getLogger(BrowseBattlesSchedulerService.class);
    private ScheduledExecutorService executor;
    private ClientWriter writer;
    private ServerBattleMarketManager battleMarketManager;
    private MicroByteBuffer buffer;

    private Set<Battle> newBattles;
    private Set<Battle> deletedBattles;
    private Set<Battle> updatedBattles;

    private Set<SocketChannel> newListeners;
    private Set<SocketChannel> oldListeners;

    private class AddBattle implements Runnable {
        private Battle battle;

        private AddBattle(Battle battle) {
            this.battle = battle;
        }

        @Override
        public void run() {
            newBattles.add(battle);
        }
    }

    private class DeleteBattle implements Runnable {
        private Battle battle;

        private DeleteBattle(Battle battle) {
            this.battle = battle;
        }

        @Override
        public void run() {
            deletedBattles.add(battle);
        }
    }

    private class UpdateBattle implements Runnable {
        private Battle battle;

        private UpdateBattle(Battle battle) {
            this.battle = battle;
        }

        @Override
        public void run() {
            updatedBattles.add(battle);
        }
    }


    private class AddChanger implements Runnable {
        private Client client;

        public AddChanger(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            logger.debug("do listen to created battles " + client.getAccount().getName());
            newListeners.add(client.getChannel());
        }
    }

    private class DeleteChanger implements Runnable {
        private Client client;

        public DeleteChanger(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            logger.debug("do not listen to created battles " + client.getAccount().getName());
            newListeners.remove(client.getChannel());
            oldListeners.remove(client.getChannel());
        }
    }


    /**
     * Добавить клиента к списку слушателей активных битв.
     *
     * @param client
     */
    public void addListener(Client client) {
        executor.execute(new AddChanger(client));
    }

    /**
     * Удалить клиента из списка слушателей активных битв.
     *
     * @param client
     */
    public void removeListener(Client client) {
        executor.execute(new DeleteChanger(client));
    }

    /**
     * Добавить битву в которой произошли следующие изменения:
     * 0.битва создана
     * 1.игрок покинул боевой союз
     * 2.игрок подсоединился к боевому союзу
     *
     * @param battle
     */
    public void addBattle(Battle battle) {
        executor.execute(new AddBattle(battle));
    }

    public void deleteBattle(Battle battle) {
        executor.execute(new DeleteBattle(battle));
    }

    public void updateBattle(Battle battle) {
        executor.execute(new UpdateBattle(battle));
    }

    public BrowseBattlesSchedulerService() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        writer = configuration.getWriter();
        battleMarketManager = configuration.getBattleMarketManager();
        buffer = new MicroByteBuffer(new byte[configuration.getMessageBufferSize()]);
        oldListeners = new HashSet<SocketChannel>();
        newListeners = new HashSet<SocketChannel>();
        deletedBattles = new HashSet<Battle>();
        updatedBattles = new HashSet<Battle>();
        newBattles = new HashSet<Battle>();
    }

    public void start() {
        executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (newListeners.size() != 0) {
                    Collection<Battle> battlesList = battleMarketManager.battlesJoinTo();
                    writer.addMessageToClient(new MainMessageToClient(new LinkedList<SocketChannel>(newListeners),
                            ServerBrowseBattlesAnswer.answerNew(buffer, battlesList).serialize()));
                    logger.debug("an amount of new listeners =" + newListeners.size() + " an amount of battles =" + battlesList.size());
                }

                if (oldListeners.size() != 0) {
                    if (newBattles.size() > 0 || updatedBattles.size() > 0 || deletedBattles.size() > 0) {
                        writer.addMessageToClient(new MainMessageToClient(oldListeners,
                                ServerBrowseBattlesAnswer.answerOld(buffer, newBattles, updatedBattles, deletedBattles).serialize()));
                        logger.debug("an amount of old listeners =" + oldListeners.size() + " an amount of new battles =" + newBattles.size());
                    }
                }
                oldListeners.addAll(newListeners);
                newListeners.clear();
                newBattles.clear();
                updatedBattles.clear();
                deletedBattles.clear();
            }
        }, MainServerConfigurationFactory.getConfiguration().getBrowseBattlesTimeInterval(),
                MainServerConfigurationFactory.getConfiguration().getBrowseBattlesTimeInterval(),
                TimeUnit.SECONDS);
    }

}
