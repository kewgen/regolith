package com.geargames.regolith.awt.components.selectMaps;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.components.*;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.battleCreate.PBattleCreatePanel;
import com.geargames.regolith.awt.components.battleCreate.PButtonOk;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.managers.ClientBaseManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.ClientBattleMapAnswer;
import com.geargames.regolith.serializers.answers.ClientBattleMapListAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: abarakov
 * Date: 26.03.13
 * Панель просмотра доступных карт и выбора одной из них для битвы.
 */
// PBrowseMapsPanel
public class PSelectMapPanel extends PContentPanel {

    private BattleType battleType;
    private BattleMap[] battleMaps;
    private BattleMap selectedMap;
    private DrawablePPanel currentPanel;

    public PSelectMapPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
//                IndexObject view = (IndexObject) prototype.getIndexBySlot(53);
//
//                Account account = ClientConfigurationFactory.getConfiguration().getAccount();
//                PObject object = (PObject) view.getPrototype();
                break;
            case 1: {
                // Заголовок списка
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.BROWSE_MAPS_LIST_TITLE);
                labelTitle.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
            case 5: {
                // Заголовок списка
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setFont(PFontCollection.getFontLabel());
                addPassiveChild(labelTitle, index);
                break;
            }

            // ----- Общие компоненты ----------------------------------------------------------------------------------

            case 13:
                // Кнопка Ok
                PButtonOk buttonOk = new PButtonOk((PObject) index.getPrototype());
                buttonOk.setText(LocalizedStrings.BUTTON_OK);
                buttonOk.setFont(PFontCollection.getFontButtonCaption());
                addActiveChild(buttonOk, index);
                break;
            case 19:
                // Заголовок окна
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.BROWSE_MAPS_PANEL_TITLE);
                labelTitle.setFont(PFontCollection.getFontFormTitle());
                addPassiveChild(labelTitle, index);
                break;
        }
    }

    public void showPanel(int allianceAmount, int allianceSize, int groupSize, boolean isRandomMap, DrawablePPanel callerPanel) {
        Debug.debug("Dialog 'Select map'");

        Debug.debug("Find battle type...");
        try {
            battleType = BaseConfigurationHelper.findBattleTypeByArgs(allianceAmount, allianceSize, groupSize,
                    ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
        } catch (Exception e) {
            Debug.critical("", e); //todo: Текст ошибки
            //todo: Сообщить пользователю об ошибке
            //todo: Перенести пользователя в одно из диалоговых окон, от куда он может продолжить игру
        }

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();

        Debug.debug("The client go to the battle market...");
        ClientDeferredAnswer answer = baseManager.goBattleManager();
        if (!waitForAnswer(answer)) {
            Debug.critical("Waiting time answer has expired");
        }
        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        if (!confirm.isConfirm()) {
            Debug.critical("The client could not go to the battle market");
        }

        if (isRandomMap) {
            Debug.debug("Browsing a random map...");
            answer = battleMarketManager.browseRandomBattleMap(battleType);
            if (!waitForAnswer(answer)) {
                Debug.critical("Waiting time answer has expired");
            }
            ClientBattleMapAnswer battleMapAnswer = (ClientBattleMapAnswer) answer.getAnswer();
            selectedMap = battleMapAnswer.getBattleMap();
            currentPanel = callerPanel;
            createBattle();
        } else {
            Debug.debug("Browsing maps...");
            answer = battleMarketManager.browseBattleMaps(battleType);
            if (!waitForAnswer(answer)) {
                Debug.critical("Waiting time answer has expired");
            }
            ClientBattleMapListAnswer battleMapList = (ClientBattleMapListAnswer) answer.getAnswer();
            battleMaps = battleMapList.getBattleMaps();
            if (battleMaps.length == 0) {
                Debug.critical("There are no maps to use");
            }
            //todo: selectedMap = ?

            PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
            currentPanel = panelManager.getSelectMap();
            panelManager.hide(callerPanel);
            panelManager.show(currentPanel);
        }
    }

    public void createBattle() {
        //todo: может вывести панельку, в которой сообщить пользователю о текущем состоянии обмена сообщениями между клиентов и сервером
        // Ожидаем ответа от игроков/сервера. Пожалуйста подождите.


        if (selectedMap == null) {
            Debug.critical("PSelectMapPanel.createBattle(): selectedMap == null");
            return;
        }
        if (currentPanel == null) {
            Debug.critical("PSelectMapPanel.createBattle(): currentPanel == null");
            return;
        }

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        com.geargames.regolith.awt.components.battleCreate.PBattleCreatePanel battleCreatePanel = (PBattleCreatePanel) panelManager.getBattleCreate().getElement();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();

        Debug.debug("Creating a battle (map id = " + selectedMap.getId() + ")...");

        ClientDeferredAnswer answer = battleMarketManager.createBattle(selectedMap, battleType);
        if (!waitForAnswer(answer)) {
            Debug.critical("Waiting time answer has expired");
        }
        ClientListenToBattleAnswer listenToBattleAnswer = (ClientListenToBattleAnswer) answer.getAnswer();
        Battle battle = listenToBattleAnswer.getBattle();
        Debug.debug("The battle was created (battle id = " + battle.getId() + ")");



//        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//        panelManager.hide(currentPanel);
//        panelManager.show(panelManager.getSelectWarriors());
    }

    public static boolean waitForAnswer(ClientDeferredAnswer answer) {
        try {
            return answer.retrieve(100);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
