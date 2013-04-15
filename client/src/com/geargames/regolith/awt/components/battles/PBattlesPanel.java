package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.ElasticInertMotionListener;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.DataMessageListener;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.Packets;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.network.ClientRequestHelper;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.serializers.requests.LoginToBattleServiceRequest;
import com.geargames.regolith.serializers.requests.StartBattleRequest;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Панель, на которой расположен список всех созданных, в настоящий момент, битв + кнопка "Создать битву", для создания
 * своей битвы.
 */
public class PBattlesPanel extends PRootContentPanel implements DataMessageListener {
    private PBattlesList battleList;
    private PBattleCreateButton battleCreateButton;
    private short[] types;

    public PBattlesPanel(PObject prototype) {
        super(prototype);
        types = new short[] {
                Packets.BROWSE_CREATED_BATTLES,
                Packets.JOIN_TO_BATTLE_ALLIANCE,
                Packets.EVICT_ACCOUNT_FROM_ALLIANCE,
                Packets.GROUP_COMPLETE,
                Packets.GROUP_DISBAND,
                Packets.CANCEL_BATTLE,
                Packets.START_BATTLE
        };
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                battleList = new PBattlesList((PObject) index.getPrototype());
                ElasticInertMotionListener motionListener = new ElasticInertMotionListener();
                battleList.setMotionListener(motionListener);
                ScrollHelper.adjustVerticalInertMotionListener(motionListener, battleList);
                addActiveChild(battleList, index);
                break;
            case 1:
                battleCreateButton = new PBattleCreateButton((PObject) index.getPrototype());
                addActiveChild(battleCreateButton, index);
                break;
        }
    }

    @Override
    public int getInterval() {
        return 1000;
    }

    @Override
    public short[] getTypes() {
        return types;
    }

    @Override
    public void onReceive(ClientDeSerializedMessage message, short type) {
        try {
//            Debug.debug("PBattlesList.onReceive(): type = " + type);
            switch (type) {
                case Packets.BROWSE_CREATED_BATTLES: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): BROWSE_CREATED_BATTLES");
                    battleList.updateList();
                    break;
                }
                case Packets.GROUP_COMPLETE: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): GROUP_COMPLETE");
                    ClientCompleteGroupAnswer completeGroupAnswer = (ClientCompleteGroupAnswer) message;
                    doUpdateButtonAccount(completeGroupAnswer.getBattleGroup(), true);
                    break;
                }
                case Packets.GROUP_DISBAND: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): GROUP_DISBAND");
                    ClientCompleteGroupAnswer completeGroupAnswer = (ClientCompleteGroupAnswer) message;
                    doUpdateButtonAccount(completeGroupAnswer.getBattleGroup(), false);
                    break;
                }
                case Packets.JOIN_TO_BATTLE_ALLIANCE: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): JOIN_TO_BATTLE_ALLIANCE");
                    ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) message;
                    doUpdateButtonAccount(joinToBattleAllianceAnswer.getBattleGroup(), false);
                    break;
                }
                //todo: Если меня выкинули из битвы, нужно почистить переменную items.listenedBattle. Учесть, что я сам себя мог выкинуть из битвы, потому сам уже почистил и присвоил новые значения этим переменным.
                case Packets.EVICT_ACCOUNT_FROM_ALLIANCE: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): EVICT_ACCOUNT_FROM_ALLIANCE");
                    ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) message;
//                    if (ClientConfigurationFactory.getConfiguration().getAccount().getId() == evictAccountFromAllianceAnswer.getAccount().getId()) {
//                        // Меня выкинули из битвы
//                    }

//                    BattleGroup battleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
//                            ClientConfigurationFactory.getConfiguration().getBattle(),
//                            evictAccountFromAllianceAnswer.getAccount().getId());
                    if (evictAccountFromAllianceAnswer.getBattleGroup() != null) {
                        doUpdateButtonAccount(evictAccountFromAllianceAnswer.getBattleGroup(), false);
                    } else {
                        Debug.error("PPBattlesList.onReceive(type = " + type + "): battleGroup == null");
                    }

//                    ClientConfigurationFactory.getConfiguration().setBattle(null);
//                    battleList.updateList();
                    break;
                }
                case Packets.CANCEL_BATTLE:
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): CANCEL_BATTLE");
                    //todo: Дореализовать обработчик события и тщательно его протестировать
//                    ClientCancelBattleAnswer cancelBattleAnswer = (ClientCancelBattleAnswer) message;
//                    if (cancelBattleAnswer.getBattleId() == ClientConfigurationFactory.getConfiguration().getBattle()) {
                    {
                        ClientConfigurationFactory.getConfiguration().setBattle(null);
                        battleCreateButton.setVisible(true);
                        battleList.updateList();
                    }
                    break;
                case Packets.START_BATTLE:
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): START_BATTLE");
                    //todo: реализовать
                    onStartBattleReceive(message);
                    break;
                default:
                    Debug.error("There is a message of type = " + type);
            }
        } catch (Exception e) {
            Debug.error("Exception while processing a message of type = " + type, e);
        }
    }

    public void registerMessageListener() {
        Debug.debug("PBattlesList.registerMessageListener");
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
    }

    public void unregisterMessageListener() {
        Debug.debug("PBattlesList.unregisterMessageListener");
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().unregister(this);
    }

    public void doUpdateButtonAccount(BattleGroup battleGroup, boolean isReady) {
        battleList.updateButtonAccount(battleGroup, isReady);
    }

    @Override
    public void onShow() {
        Debug.debug("Dialog 'Battles': onShow");
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarket = configuration.getBattleMarketManager();
        try {
            ClientConfirmationAnswer confirmationAnswer = battleMarket.listenToCreatedBattles();
            if (confirmationAnswer.isConfirm()) {
                ObjectManager.getInstance().getBattleCollection().getBattles().clear();
                registerMessageListener();
            } else {
                Debug.error("ListenToCreatedBattles: Request rejected");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_LISTEN_TO_CREATED_BATTLES_EXCEPTION, this);
            }
        } catch (Exception e) {
            Debug.error("ListenToCreatedBattles: Send request and receive answer is failed", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_LISTEN_TO_CREATED_BATTLES_EXCEPTION, this);
        }
    }

    @Override
    public void onHide() {
        Debug.debug("Dialog 'Battles': onHide");
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarket = configuration.getBattleMarketManager();
        try {
            ClientConfirmationAnswer confirmationAnswer = battleMarket.doNotListenToCreatedBattles();
            if (!confirmationAnswer.isConfirm()) {
                Debug.error("DoNotListenToCreatedBattles: Request rejected");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_CREATED_BATTLES_EXCEPTION, this);
            }
            unregisterMessageListener();
        } catch (Exception e) {
            Debug.error("DoNotListenToCreatedBattles: Send request and receive answer is failed", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_CREATED_BATTLES_EXCEPTION, this);
        }
    }

    /**
     * Отобразить панельку.
     *
     * @param listenedBattle - битва созданная данным клиентом, или null если только начали слушать все битвы
     * @param callerPanel    - панелька, из которой перешли на данную панельку
     */
    public void showPanel(Battle listenedBattle, BattleGroup completeBattleGroup, DrawablePPanel callerPanel) {
        Debug.debug("Dialog 'Battles'");
        ClientConfigurationFactory.getConfiguration().setBattle(listenedBattle);
        if (completeBattleGroup != null) {
            doUpdateButtonAccount(completeBattleGroup, true);
        }
        battleList.updateList();
        ScrollHelper.adjustVerticalInertMotionListener((ElasticInertMotionListener) battleList.getMotionListener(), battleList);

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(callerPanel);
        panelManager.show(panelManager.getBattlesWindow());
    }

    public void onStartBattleReceive(ClientDeSerializedMessage message){
        ClientStartBattleAnswer answer = (ClientStartBattleAnswer)message;
        if(answer.isSuccess()){
            ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
            configuration.setBattle(answer.getBattle());
            LoginToBattleServiceRequest request = new LoginToBattleServiceRequest();
            request.setBattle(answer.getBattle());
            configuration.getNetwork().sendMessage(new LoginToBattleServiceRequest());


        }else{
            NotificationBox.error(LocalizedStrings.COULD_NOT_START_BATTLE, this);
        }
    }

    /**
     * Обработчик нажатия на кнопку "Создать бой".
     */
    public void onBattleCreateButtonClick() {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        Battle listenedBattle = configuration.getBattle();
        if (listenedBattle == null) {
            // Нет битв, которую я создал или в которую вступил => можно смело создавать битву
            // Особой реакции на этот случай не требуется
        } else
        if (listenedBattle.getAuthor().getId() == configuration.getAccount().getId()) { //todo: id или ссылка?
            // Битва была создана мною => нужно заканселить ее
            // Этой ситуации вообще происходить не должно, но, на всякий случай, обработаем ее
            Debug.error("There was an attempt to create a battle, when there are already created battle");

            boolean res = ClientRequestHelper.cancelBattle(listenedBattle, this, LocalizedStrings.BATTLES_MSG_CANCEL_BATTLE_EXCEPTION);
            ClientConfigurationFactory.getConfiguration().setBattle(null);
            battleList.updateList();
            if (!res) {
                return;
            }
        } else {
            BattleGroup myBattleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(listenedBattle, configuration.getAccount().getId());
            if (myBattleGroup != null) {
                // Я вступил в одну из боевых групп => нужно выйти из битвы
                if (myBattleGroup.getAlliance().getBattle().getId() != listenedBattle.getId()) {
                    Debug.error("PBattlesPanel.onBattleCreateButtonClick: myBattleGroup.getAlliance().getBattle().getId() != listenedBattle.getId()");
                }
                if (myBattleGroup.getAccount().getId() != configuration.getAccount().getId()) {
                    Debug.error("PBattlesPanel.onBattleCreateButtonClick: myBattleGroup.getAccount().getId() != configuration.getAccount().getId()");
                }
                boolean res = ClientRequestHelper.evictAccountFromBattleGroup(myBattleGroup, this, LocalizedStrings.BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION) &&
                        ClientRequestHelper.doNotListenToBattle(listenedBattle, this, LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION);
                ClientConfigurationFactory.getConfiguration().setBattle(null);
                battleList.updateList();
                if (!res) {
                    return;
                }
            } else {
                // Я просто подписался на обновления битвы => нужно отписаться от обновлений
                // Эта ситуация возможна, если я вошел в боевую группу одной из битв, а потом автор этой битвы меня выкинул из боевой группы
                boolean res = ClientRequestHelper.doNotListenToBattle(listenedBattle, this, LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION);
                ClientConfigurationFactory.getConfiguration().setBattle(null);
                battleList.updateList();
                if (!res) {
                    return;
                }
            }
        }
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattleCreatePanel().showPanel(panelManager.getBattlesWindow());
        battleCreateButton.setVisible(false);
    }

    /**
     * Обработчик нажатия на иконку боевой группы, чтобы вступить в нее или посмотреть кто в нее вступил.
     */
    public void onPlayerButtonClick(BattleGroup targetBattleGroup) {
        /* Рассмотрим различные ситуации и варианты реакции на них:
        I.  Боевая группа свободна
            Важно: Не забываем, что мы можем уже состоять в одной из боевых групп той же битвы, в этом случае, нужно
            покинуть боевую группу, перед вступлением в новую.
            1. у меня есть своя битва
               а. я автор битвы       => выберем бойцов
               б. я не автор битвы    => отменим свою битву, вступим в чужую битву и выберем бойцов
            2. у меня нет своей битвы => выберем бойцов
        II. Боевая группа кем-то занята
            1. занята мною            => перевыберем бойцов
            2. занята кем-то другим
               а. я автор битвы       => посмотрим информацию о игроке, кнопка kick активна
               б. я не автор битвы    => посмотрим информацию о игроке
        */
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        Battle listenedBattle = configuration.getBattle();

        if (targetBattleGroup.getAccount() == null) {
            // Я щелкнул по иконке незанятой боевой группы
            if (listenedBattle == null) {
                // У меня нет своей битвы и я не вхожу ни в одну другую битву => могу смело выбирать бойцов и вступать в чужую битву
                // Особой реакции на этот случай не требуется
            } else
            if (listenedBattle.getAuthor().getId() == configuration.getAccount().getId()) { //todo: сравнивать сущности по id или по ссылке?
                // У меня есть своя битва
                if (targetBattleGroup.getAlliance().getBattle().getId() == listenedBattle.getId()) {
                    // Я собираюсь вступить в боевую группу своей битвы => сначало нужно освободить занятую боевую группу перед вступлением в другую
                    BattleGroup myBattleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
                            listenedBattle, configuration.getAccount().getId());
                    if (myBattleGroup != null) {
                        // Я вхожу в одну из других боевых груп => выхожу из нее и вхожу в другую
                        //todo: должно быть иное действие по освобождению занимаемой боевой группы, при этом я не должен выходить из битвы
                        if (!ClientRequestHelper.evictAccountFromBattleGroup(myBattleGroup, this, LocalizedStrings.BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION)) {
                            return;
                        }
//                        disbandBattleGroup(myBattleGroup);
                    }
                } else {
                    // Я щелкнул по иконке боевой группы чужой битвы, при этом у меня есть своя созданная битва => нужно заканселить свою битву
                    boolean res = ClientRequestHelper.cancelBattle(listenedBattle, this, LocalizedStrings.BATTLES_MSG_CANCEL_BATTLE_EXCEPTION);
                    battleCreateButton.setVisible(true);
                    ClientConfigurationFactory.getConfiguration().setBattle(null);
                    battleList.updateList();
                    if (!res) {
                        return;
                    }
                }
            } else {
                // У меня нет своей битвы, но я подписан на чужую битву
                BattleGroup myBattleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
                        listenedBattle, configuration.getAccount().getId());
                if (targetBattleGroup.getAlliance().getBattle().getId() == listenedBattle.getId()) {
                    if (myBattleGroup != null) {
                        // Я собираюсь вступить в другую боевую группу чужой битвы, одну из боевых групп которой, я уже занимаю
                        // Я вхожу в одну из других боевых груп => выхожу из нее и вхожу в другую
                        //todo: должно быть иное действие по освобождению занимаемой боевой группы, при этом я не должен выходить из битвы
                        if (!ClientRequestHelper.evictAccountFromBattleGroup(myBattleGroup, this, LocalizedStrings.BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION)) {
                            return;
                        }
//                        disbandBattleGroup(myBattleGroup);
                    } else {
                        // Я просто наблюдаю за битвой, не являясь ни ее создателем, ни входя ни в одну из ее боевых групп
                        // Эта ситуация возможна, если я вошел в боевую группу битвы, а потом автор этой битвы меня выкинул из боевой группы
                        // Особой реакции на этот случай не требуется
                    }
                } else {
                    // Я собираюсь вступить в боевую группу другой битвы
                    boolean res;
                    if (myBattleGroup != null) {
                        // Я вхожу в одну из боевых груп => выхожу из нее
                        res = ClientRequestHelper.evictAccountFromBattleGroup(myBattleGroup, this, LocalizedStrings.BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION);
                    } else {
                        // Я просто наблюдатель битвы, подписан на ее обновления
                        res = true;
                    }
                    res = res && ClientRequestHelper.doNotListenToBattle(listenedBattle, this, LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION);
                    ClientConfigurationFactory.getConfiguration().setBattle(null);
                    battleList.updateList();
                    if (!res) {
                        return;
                    }
                }
            }
        } else
        if (targetBattleGroup.getAccount().getId() == configuration.getAccount().getId()) { //todo: id или ссылка?
            // Боевая группа занята мною => посмотрим каких бойцов я выбрал и, при необходимости, перевыберем их
            // Особой реакции на этот случай не требуется
        } else {
            // Боевая группа кем-то занята, но не мною => посмотрим информацию об игроке
            PRegolithPanelManager.getInstance().getPlayerInfoPanel().showPanel(
                    targetBattleGroup, targetBattleGroup.getAlliance().getBattle().getAuthor().getId() == configuration.getAccount().getId());
            return;
        }
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getSelectWarriorsPanel().showPanel(targetBattleGroup);
    }

    public void onStartBattleButtonClick() {
        ClientRequestHelper.startBattle(this, LocalizedStrings.BATTLES_MSG_START_BATTLE_EXCEPTION);
    }

}
