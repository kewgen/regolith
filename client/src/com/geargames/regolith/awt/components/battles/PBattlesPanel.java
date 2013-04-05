package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.ElasticInertMotionListener;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.managers.ClientBattleCreationManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.ClientCancelBattleAnswer;
import com.geargames.regolith.serializers.answers.ClientCompleteGroupAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientEvictAccountFromAllianceAnswer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Панель, на которой расположен список всех созданных, в настоящий момент, битв + кнопка "Создать битву", для создания
 * своей битвы.
 */
public class PBattlesPanel extends PRootContentPanel {
    private PBattlesList battleList;
    private PBattleCreateButton battleCreateButton;

    public PBattlesPanel(PObject prototype) {
        super(prototype);
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
    public void onShow() {
        Debug.debug("Dialog 'Battles': onShow");
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarket = configuration.getBattleMarketManager();
        try {
            ClientConfirmationAnswer confirmationAnswer = battleMarket.listenToCreatedBattles();
            if (confirmationAnswer.isConfirm()) {
                ObjectManager.getInstance().getBattleCollection().getBattles().clear();
                battleList.registerMessageListener();
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
            battleList.unregisterMessageListener();
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
        battleList.setListenedBattle(listenedBattle);
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
        Battle listenedBattle = battleList.getListenedBattle();
        if (listenedBattle == null) {
            // Нет битв, которую я создал или в которую вступил => можно смело создавать битву
            // Особой реакции на этот случай не требуется
        } else
        if (listenedBattle.getAuthor().getId() == configuration.getAccount().getId()) { //todo: id или ссылка?
            // Битва была создана мною => нужно заканселить ее
            // Этой ситуации вообще происходить недолжно
            Debug.error("There was an attempt to create a battle, when there are already created battle");
            cancelBattle(listenedBattle);
            battleList.setListenedBattle(null);
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
                evictAccountFromBattleGroup(myBattleGroup);
                battleList.setListenedBattle(null);
            } else {
                // Я просто подписался на обновления битвы => нужно отписаться от обновлений
                //todo: Вероятно, этот случай никогда не наступит
                Debug.debug("The client is trying to unsubscribe from the battle (battle id = " + listenedBattle.getId() + ")...");
                battleCreationManager.doNotListenToBattle(listenedBattle);
                //todo: Обработать ответ от сервера
//                if (!doNotListenToBattleAnswer.isSuccess()) {
//                }
                Debug.debug("The client unsubscribe from battle (battle id = " + listenedBattle.getId() + ")");
                battleList.setListenedBattle(null);
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
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        Battle listenedBattle = battleList.getListenedBattle();
        if (listenedBattle != null && listenedBattle.getAuthor().getId() == configuration.getAccount().getId() &&
                listenedBattle.getId() != battleGroup.getAlliance().getBattle().getId()) { //todo: id или ссылка?
            // Я щелкнул по иконке боевой группы чужой битвы, при этом у меня есть своя созданная битва => нужно заканселить свою битву
            if (cancelBattle(listenedBattle)) {
                battleCreateButton.setVisible(true);
            }
            battleList.setListenedBattle(null);
        } else {
            Account accountInBattleGroup = battleGroup.getAccount();
            if (accountInBattleGroup == null) {
                // Боевая группа никем не занята => займем ее
                BattleGroup myBattleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(
                        battleGroup.getAlliance().getBattle(), configuration.getAccount().getId());
                if (myBattleGroup != null) {
                    // Я вхожу в одну из других боевых груп => выхожу из нее и вхожу в другую
                    evictAccountFromBattleGroup(myBattleGroup);
//                    disbandGroup(myBattleGroup);
                }
            } else
            if (accountInBattleGroup.getId() == configuration.getAccount().getId()) { //todo: id или ссылка?
                // Боевая группа занята мною => посмотрим каких бойцов я выбрал и, при необходимости, перевыберем их
                // Особой реакции на этот случай не требуется
            } else {
                // Боевая группа кем-то занята, но не мною => посмотрим информацию об игроке
                //todo: реализовать
                NotificationBox.error("ДОЛЖНО ПОЯВИТЬСЯ ОКНО С ИНФОРМАЦИЕЙ ОБ ИГРОКЕ. ДАННОЕ ОКНО В РАЗРАБОТКЕ", this);
                return;
            }
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
