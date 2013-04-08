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
import com.geargames.regolith.managers.ClientBattleCreationManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.*;
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
                battleList = new PBattlesList((PObject)index.getPrototype());
                ElasticInertMotionListener motionListener = new ElasticInertMotionListener();
                battleList.setMotionListener(motionListener);
                ScrollHelper.adjustVerticalInertMotionListener(motionListener, battleList);
                addActiveChild(battleList, index);
                break;
            case 1:
                battleCreateButton = new PBattleCreateButton((PObject)index.getPrototype());
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
                    battleList.updateButtonAccount(completeGroupAnswer.getBattleGroup());
                    break;
                }
                case Packets.GROUP_DISBAND: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): GROUP_DISBAND");
                    ClientCompleteGroupAnswer completeGroupAnswer = (ClientCompleteGroupAnswer) message;
                    battleList.updateButtonAccount(completeGroupAnswer.getBattleGroup());
                    break;
                }
                case Packets.JOIN_TO_BATTLE_ALLIANCE: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): JOIN_TO_BATTLE_ALLIANCE");
                    ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) message;
                    battleList.updateButtonAccount(joinToBattleAllianceAnswer.getBattleGroup());
                    break;
                }
                //todo: Если меня выкинули из битвы, нужно почистить переменную items.listenedBattle. Учесть, что я сам себя мог выкинуть из битвы, потому сам уже почистил и присвоил новые значения этим переменным.
                case Packets.EVICT_ACCOUNT_FROM_ALLIANCE: {
                    Debug.debug("PBattlesList.onReceive(type = " + type + "): EVICT_ACCOUNT_FROM_ALLIANCE");
                    ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) message;
                    BattleGroup battleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
                            ClientConfigurationFactory.getConfiguration().getBattle(),
                            evictAccountFromAllianceAnswer.getAccount().getId());
                    if (battleGroup != null) {
                        battleList.updateButtonAccount(battleGroup);
                    }
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
     * @param listenedBattle - битва созданная данным клиентом, или null если только начали слушать все битвы
     * @param callerPanel    - панелька, из которой перешли на данную панельку
     */
    // activatePanel
    public void showPanel(Battle listenedBattle, DrawablePPanel callerPanel, boolean isModalCallerPanel) {
        Debug.debug("Dialog 'Battles'");
        ClientConfigurationFactory.getConfiguration().setBattle(listenedBattle);
        battleList.updateList();
        ScrollHelper.adjustVerticalInertMotionListener((ElasticInertMotionListener)battleList.getMotionListener(), battleList);

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        if (isModalCallerPanel) {
            panelManager.hideModal();
        } else {
            panelManager.hide(callerPanel);
        }
        panelManager.show(panelManager.getBattlesWindow());
    }

    /**
     * Обработчик нажатия на кнопку "Создать бой".
     */
    public void onBattleCreateButtonClick() {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleCreationManager battleCreationManager = configuration.getBattleCreationManager();
        Battle listenedBattle = configuration.getBattle();
        if (listenedBattle == null) {
            // Нет битв, которую я создал или в которую вступил => можно смело создавать битву
            // Особой реакции на этот случай не требуется
        } else
        if (listenedBattle.getAuthor().getId() == configuration.getAccount().getId()) { //todo: id или ссылка?
            // Битва была создана мною => нужно заканселить ее
            // Этой ситуации вообще происходить не должно
            Debug.error("There was an attempt to create a battle, when there are already created battle");
            boolean res = cancelBattle(listenedBattle);
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
                boolean res = evictAccountFromBattleGroup(myBattleGroup) &&
                        doNotListenToBattle(listenedBattle);
                ClientConfigurationFactory.getConfiguration().setBattle(null);
                battleList.updateList();
                if (!res) {
                    return;
                }
            } else {
                // Я просто подписался на обновления битвы => нужно отписаться от обновлений
                //todo: Вероятно, этот случай никогда не наступит
                boolean res = doNotListenToBattle(listenedBattle);
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
    public void onPlayerButtonClick(BattleGroup battleGroup) {
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

        if (battleGroup.getAccount() == null) {
            // Я щелкнул по иконке незанятой боевой группы
            if (listenedBattle == null) {
                // У меня нет своей битвы и я не вхожу ни в одну другую битву => могу смело выбирать бойцов и вступать в чужую битву
                // Особой реакции на этот случай не требуется
            } else
            if (listenedBattle.getAuthor().getId() == configuration.getAccount().getId()) { //todo: сравнивать сущности по id или по ссылке?
                // У меня есть своя битва
                if (battleGroup.getAlliance().getBattle().getId() == listenedBattle.getId()) {
                    // Я собираюсь вступить в боевую группу своей битвы => сначало нужно освободить занятую боевую группу перед вступлением в другую
                    BattleGroup myBattleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
                            listenedBattle, configuration.getAccount().getId());
                    if (myBattleGroup != null) {
                        // Я вхожу в одну из других боевых груп => выхожу из нее и вхожу в другую
                        //todo: должно быть иное действие по освобождение занимаемой боевой группы, при этом я не должен выходить из битвы
                        if (!evictAccountFromBattleGroup(myBattleGroup)) {
                            return;
                        }
//                        disbandGroup(myBattleGroup);
                    }
                } else {
                    // Я щелкнул по иконке боевой группы чужой битвы, при этом у меня есть своя созданная битва => нужно заканселить свою битву
                    boolean res = cancelBattle(listenedBattle);
                    battleCreateButton.setVisible(true);
                    ClientConfigurationFactory.getConfiguration().setBattle(null);
                    battleList.updateList();
                    if (!res) {
                        return;
                    }
                }
            } else {
                // У меня нет своей битвы, но я занимаю боевую группу чужой битвы
                BattleGroup myBattleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
                        listenedBattle, configuration.getAccount().getId());
                if (myBattleGroup == null) {
                    Debug.error("PBattlesPanel.onPlayerButtonClick(): myBattleGroup == null");
                }

                if (battleGroup.getAlliance().getBattle().getId() == listenedBattle.getId()) {
                    // Я собираюсь вступить в другую боевую группу чужой битвы, одну из боевых групп которой, я уже занимаю
                    // Я вхожу в одну из других боевых груп => выхожу из нее и вхожу в другую
                    //todo: должно быть иное действие по освобождению занимаемой боевой группы, при этом я не должен выходить из битвы
                    if (!evictAccountFromBattleGroup(myBattleGroup)) {
                        return;
                    }
//                    disbandGroup(myBattleGroup);
                } else {
                    // Я собираюсь вступить в боевую группу другой битвы
                    boolean res = evictAccountFromBattleGroup(myBattleGroup) &&
                            doNotListenToBattle(listenedBattle);
                    ClientConfigurationFactory.getConfiguration().setBattle(null);
                    battleList.updateList();
                    if (!res) {
                        return;
                    }
                }
            }
        } else
        if (battleGroup.getAccount().getId() == configuration.getAccount().getId()) { //todo: id или ссылка?
            // Боевая группа занята мною => посмотрим каких бойцов я выбрал и, при необходимости, перевыберем их
            // Особой реакции на этот случай не требуется
        } else {
            // Боевая группа кем-то занята, но не мною => посмотрим информацию об игроке
            //todo: реализовать
//            PRegolithPanelManager.getInstance().getWarriorInfoPanel().showPanel(
//                    battleGroup.getAccount(),
//                    /*kickAllowed=*/battleGroup.getAlliance().getBattle().getAuthor().getId() == configuration.getAccount().getId());
            NotificationBox.error("ДОЛЖНО ПОЯВИТЬСЯ ОКНО С ИНФОРМАЦИЕЙ ОБ ИГРОКЕ. ДАННОЕ ОКНО В РАЗРАБОТКЕ", this);
            return;
        }
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getSelectWarriorsPanel().showPanel(battleGroup);
    }

    /**
     * Вспомогательная функция. Исключает игрока из боевой группы.
     * @param battleGroup - боевая группа, которую нужно освободить;
     * @return            - true, если освобождение прошло успешно.
     */
    private boolean evictAccountFromBattleGroup(BattleGroup battleGroup) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying to get out of the battle (" +
                    "battle id = " + battleGroup.getAlliance().getBattle().getId() + "; battle group id = " + battleGroup.getId() + ")...");
            ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = battleCreationManager.evictAccount(
                    battleGroup.getAlliance(), battleGroup.getAccount());
            if (!evictAccountFromAllianceAnswer.isSuccess()) {
                Debug.error("EvictAccount: Request rejected (battle group id = " + battleGroup.getId() + ")");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION, this);
                return false;
            }
            Debug.debug("The client evicted from the alliance (" +
                    "battle group id = " + battleGroup.getId() +
                    "; account id = " + evictAccountFromAllianceAnswer.getAccount().getId() +
                    "; alliance id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("EvictAccount: Send request and receive answer is failed (battle group id = " + battleGroup.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION, this);
            return false;
        }
    }

    /**
     * Вспомогательная функция. Отписывает игрока от получения обновлений битвы.
     * @param battle - битва, от которой нужно отписаться;
     * @return       - true, если отписались от битвы успешно.
     */
    private boolean doNotListenToBattle(Battle battle) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying to unsubscribe from the battle (battle id = " + battle.getId() + ")...");
            ClientConfirmationAnswer confirmationAnswer = battleCreationManager.doNotListenToBattle(battle);
            if (!confirmationAnswer.isConfirm()) {
                Debug.error("DoNotListenToBattle: Request rejected (battle id = " + battle.getId() + ")");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION, this);
                return false;
            }
            Debug.debug("The client unsubscribe from battle (battle id = " + battle.getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("DoNotListenToBattle: Send request and receive answer is failed (battle id = " + battle.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION, this);
            return false;
        }
    }

    /**
     * Вспомогательная функция. Отменяет битву.
     * @param battle - битва, которую нужно отменить;
     * @return       - true, если отмена битвы прошла успешно.
     */
    private boolean cancelBattle(Battle battle) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("Trying to cancelation the battle (battle id = " + battle.getId() + ")...");
            ClientCancelBattleAnswer cancelBattleAnswer = battleCreationManager.cancelBattle();
            if (!cancelBattleAnswer.isSuccess()) {
                Debug.error("CancelBattle: Request rejected (battle id = " + battle.getId() + ")");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_CANCEL_BATTLE_EXCEPTION, this);
                return false;
            }
            Debug.debug("The client canceled battle (battle id = " + battle.getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("CancelBattle: Send request and receive answer is failed (battle id = " + battle.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_CANCEL_BATTLE_EXCEPTION, this);
            return false;
        }
    }

    private boolean disbandGroup(BattleGroup battleGroup) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying disband the battle group (battle group id = " + battleGroup.getId() + ")...");
            ClientCompleteGroupAnswer completeGroupAnswer = battleCreationManager.disbandGroup(battleGroup);
            if (!completeGroupAnswer.isSuccess()) {
                Debug.error("DisbandGroup: Request rejected (battle group id = " + battleGroup.getId() + ")");
                NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_DISBAND_GROUP_EXCEPTION, this);
                return false;
            }
            Debug.debug("The client disbanded the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("DisbandGroup: Send request and receive answer is failed (battle group id = " + battleGroup.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_DISBAND_GROUP_EXCEPTION, this);
            return false;
        }
    }

}
