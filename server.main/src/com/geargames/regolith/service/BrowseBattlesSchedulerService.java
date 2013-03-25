package com.geargames.regolith.service;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.serializers.answers.ServerBrowseCreatedBattlesAnswer;
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

    private Queue<Battle> battles;
    private Queue<Changer> requests;

    private Set<SocketChannel> newListeners;
    private Set<SocketChannel> oldListeners;

    private interface Changer {
        void change();
    }

    private class AddChanger implements Changer {
        private Client client;
        public AddChanger(Client client){
            this.client = client;
        }

        @Override
        public void change() {
            newListeners.add(client.getChannel());
        }
    }

    private class DeleteChanger implements Changer {
        private Client client;

        public DeleteChanger(Client client){
            this.client = client;
        }

        @Override
        public void change() {
            newListeners.remove(client.getChannel());
            oldListeners.remove(client.getChannel());
        }
    }


    /**
     * Добавить клиента к списку слушателей активных битв.
     * @param client
     */
    public void addListener(Client client) {
        requests.add(new AddChanger(client));
    }

    /**
     * Удалить клиента из списка слушателей активных битв.
     * @param client
     */
    public void removeListener(Client client) {
        requests.add(new DeleteChanger(client));
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
        battles.add(battle);
    }

    public BrowseBattlesSchedulerService() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        writer = configuration.getWriter();
        battleMarketManager = configuration.getBattleMarketManager();
        buffer = new MicroByteBuffer(new byte[configuration.getMessageBufferSize()]);
        oldListeners = new HashSet<SocketChannel>();
        newListeners = new HashSet<SocketChannel>();
        requests = new LinkedList<Changer>();
        battles = new LinkedList<Battle>();
    }

    public void start() {
        executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int size = requests.size();
                for (int i = 0; i < size; i++) {
                    requests.poll().change();
                }

                writer.addMessageToClient(new MainMessageToClient(newListeners,
                        new ServerBrowseCreatedBattlesAnswer(buffer, battleMarketManager.battlesJoinTo()).serialize()));

                Set<Battle> browsed = new HashSet<Battle>();
                size = browsed.size();
                for (int i = 0; i < size; i++) {
                    browsed.add(battles.poll());
                }

                writer.addMessageToClient(new MainMessageToClient(oldListeners,
                        new ServerBrowseCreatedBattlesAnswer(buffer, browsed.toArray(new Battle[]{})).serialize()));
                oldListeners.addAll(newListeners);
                newListeners.clear();
            }
        },MainServerConfigurationFactory.getConfiguration().getBrowseBattlesTimeInterval(),
          MainServerConfigurationFactory.getConfiguration().getBrowseBattlesTimeInterval(),
          TimeUnit.SECONDS);
    }

}
