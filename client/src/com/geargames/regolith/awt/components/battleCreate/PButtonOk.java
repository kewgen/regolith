package com.geargames.regolith.awt.components.battleCreate;

import com.geargames.awt.components.PEntitledTouchButton;
import com.geargames.common.String;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.common.network.ClientDeferredAnswer;
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

    public PButtonOk(PObject prototype, String text) {
        super(prototype, text);
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
    public static Battle createBattle(java.lang.String name, java.lang.String mapName) {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        PBattleCreatePanel battleCreatePanel = (PBattleCreatePanel) panelManager.getBattleCreate().getElement();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();

        Battle battle = new Battle();
        battle.setName(name);
        battle.setAuthor(clientConfiguration.getAccount());

        byte allianceAmount = battleCreatePanel.getAllianceAmount();

        BattleAlliance[] allianceList = new BattleAlliance[allianceAmount];
        for (byte i = 0; i < allianceList.length; i++) {
            BattleAlliance alliance = new BattleAlliance();
            ExitZone exitZone = new ExitZone();
            //todo: заполнить exitZone
            alliance.setExit(exitZone);
            alliance.setNumber(i);
            alliance.setAllies(new ClientBattleGroupCollection()); //todo: заполнить ClientBattleGroupCollection
            alliance.setBattle(battle);
            allianceList[i] = alliance;
        }
        battle.setAlliances(allianceList);

        byte allianceSize = battleCreatePanel.getAllianceSize();
        byte groupSize = battleCreatePanel.getGroupSize();

        BattleMap battleMap = ClientBattleHelper.createBattleMap(20); //todo: где взять поле битвы?
        battle.setMap(battleMap);
        BattleType[] battleTypeList = new BattleType[1];
        BattleType battleType = new BattleType();
        battleType.setAllianceAmount(allianceAmount);
        battleType.setAllianceSize(allianceSize);
        battleType.setGroupSize(groupSize);
        battleTypeList[0] = battleType;
        battleMap.setPossibleBattleTypes(battleTypeList);

        battle.setBattleType(battleType); //todo: верно?




        ClientDeferredAnswer answer = clientConfiguration.getBattleCreationManager().startBattle(clientConfiguration.getAccount());

//        if (answer.retrieve(100)) { // ждем ответа в течении 10 сек
//            answer.getAnswer().deSerialize();
//            ClientStartBattleAnswer
//        }

        return battle;
    }

}
