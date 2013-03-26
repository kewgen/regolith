package com.geargames.regolith.awt.components.battleCreate;

import com.geargames.awt.components.PEntitledTouchButton;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.managers.ClientBaseManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.ClientBrowseBattleMapsAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.ExitZone;

/**
 * User: abarakov
 * Date: 04.03.13
 * Кнопка Ok для создания битвы.
 */
public class PButtonOk extends PEntitledTouchButton {

    public PButtonOk(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
        createBattle("test", "test"); //todo: Задать правильные значения
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(panelManager.getBattleCreate());
        panelManager.show(panelManager.getMainMenu());
    }

    /**
     * Создать битву с именем name.
     * @param name
     * @param mapName имя карты
     * @return
     */
    public static Battle createBattle(String name, String mapName) {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        PBattleCreatePanel battleCreatePanel = (PBattleCreatePanel) panelManager.getBattleCreate().getElement();

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


//        Battle battle = new Battle();
//        battle.setName(name);
//        battle.setAuthor(clientConfiguration.getAccount());
//
//        byte allianceAmount = battleCreatePanel.getAllianceAmount();
//
//        BattleAlliance[] allianceList = new BattleAlliance[allianceAmount];
//        for (byte i = 0; i < allianceList.length; i++) {
//            BattleAlliance alliance = new BattleAlliance();
//            ExitZone exitZone = new ExitZone();
//            //todo: заполнить exitZone
//            alliance.setExit(exitZone);
//            alliance.setNumber(i);
//            alliance.setAllies(new ClientBattleGroupCollection()); //todo: заполнить ClientBattleGroupCollection
//            alliance.setBattle(battle);
//            allianceList[i] = alliance;
//        }
//        battle.setAlliances(allianceList);
//
//        byte allianceSize = battleCreatePanel.getAllianceSize();
//        byte groupSize = battleCreatePanel.getGroupSize();
//
//        BattleMap battleMap = ClientBattleHelper.createBattleMap(20); //todo: где взять поле битвы?
//        battle.setMap(battleMap);
//        BattleType[] battleTypeList = new BattleType[1];
//        BattleType battleType = new BattleType();
//        battleType.setAllianceAmount(allianceAmount);
//        battleType.setAllianceSize(allianceSize);
//        battleType.setGroupSize(groupSize);
//        battleTypeList[0] = battleType;
//        battleMap.setPossibleBattleTypes(battleTypeList);
//
//        battle.setBattleType(battleType); //todo: верно?

        return battle;
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
