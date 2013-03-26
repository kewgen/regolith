package com.geargames.regolith.awt.components.selectMaps;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.components.*;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.battleCreate.PBattleCreatePanel;
import com.geargames.regolith.awt.components.battleCreate.PButtonOk;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.managers.ClientBaseManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.ClientBrowseBattleMapsAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.dictionaries.BattleTypeCollection;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: abarakov
 * Date: 26.03.13
 * Панель просмотра доступных карт и выбора одной из них для битвы.
 */
// PBrowseMapsPanel
public class PSelectMapPanel extends PContentPanel {

    private BattleMap battleMap;
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
        BattleType battleType;
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

        Debug.debug("Browsing maps...");
        answer = battleMarketManager.browseBattleMaps();
        if (!waitForAnswer(answer)) {
            Debug.critical("Waiting time answer has expired");
        }
        ClientBrowseBattleMapsAnswer browseMaps = (ClientBrowseBattleMapsAnswer) answer.getAnswer();
        BattleMap[] maps = browseMaps.getBattleMaps();
        if (maps.length == 0) {
            Debug.critical("There are no maps to use");
        }
        BattleMap battleMap = maps[0]; //todo: выбирать случайную, подходящую

        Debug.debug("Creating a battle (map id = " + battleMap.getId() + ")...");

        if (isRandomMap) {
            battleMap = null;
            currentPanel = callerPanel;
            createBattle();
        } else {
            PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
            currentPanel = panelManager.getSelectMap();
            panelManager.hide(callerPanel);
            panelManager.show(currentPanel);
        }
    }

    public void createBattle() {
        //todo: может вывести панельку, в которой сообщить пользователю о текущем состоянии обмена сообщениями между клиентов и сервером
        // Ожидаем ответа от игроков/сервера. Пожалуйста подождите.



        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        com.geargames.regolith.awt.components.battleCreate.PBattleCreatePanel battleCreatePanel = (PBattleCreatePanel) panelManager.getBattleCreate().getElement();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();

        /*
//        if (battleCreatePanel.getIsRandomMap()) {
            Debug.debug("Browsing maps...");
            answer = battleMarketManager.browseBattleMaps();
            if (!waitForAnswer(answer)) {
                Debug.critical("Waiting time answer has expired");
            }
            ClientBrowseBattleMapsAnswer browseMaps = (ClientBrowseBattleMapsAnswer) answer.getAnswer();
            BattleMap[] maps = browseMaps.getBattleMaps();
            if (maps.length == 0) {
                Debug.critical("There are no maps to use");
            }
            BattleMap battleMap = maps[0]; //todo: выбирать случайную, подходящую
//        }

        Debug.debug("Creating a battle (map id = " + battleMap.getId() + ")...");
        answer = battleMarketManager.createBattle(battleMap, 0);
        if (!waitForAnswer(answer)) {
            Debug.critical("Waiting time answer has expired");
        }
        ClientListenToBattleAnswer listenToBattleAnswer = (ClientListenToBattleAnswer) answer.getAnswer();
        Battle battle = listenToBattleAnswer.getBattle();
        Debug.debug("The battle was created (battle id = " + battle.getId() + ")");
        */



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
