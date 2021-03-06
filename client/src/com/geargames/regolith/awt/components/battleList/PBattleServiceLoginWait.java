package com.geargames.regolith.awt.components.battleList;

import com.geargames.common.network.DataMessageListener;
import com.geargames.common.packer.PObject;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.*;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.common.PWaitingWindow;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.map.observer.StrictPerimeterObserver;
import com.geargames.regolith.map.router.RecursiveWaveRouter;
import com.geargames.regolith.serializers.answers.ClientBattleLoginAnswer;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

import java.util.Vector;

/**
 * User: m/v/kutuzov
 * Date: 16.04.13
 * Time: 10:07
 */
public class PBattleServiceLoginWait extends PWaitingWindow implements DataMessageListener {
    private short[] types;

    public PBattleServiceLoginWait(PObject prototype) {
        super(prototype);
        types = new short[]{Packets.BATTLE_SERVICE_LOGIN};
    }

    @Override
    public void onHide() {
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().unregister(this);
    }

    @Override
    public void onShow() {
        getMessageLabel().setText(LocalizedStrings.BATTLE_START_AWAITING);
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
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
        ClientBattleLoginAnswer battleLogin = (ClientBattleLoginAnswer) message;
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        if (battleLogin.isSuccess()) {
            ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
            BattleConfiguration battleConfiguration = configuration.getBattleConfiguration();

            ClientBattleContext battleContext = ClientConfigurationFactory.getConfiguration().getBattleContext();
            battleContext.initiate(configuration.getBattle());

            ClientWarriorCollection units = new ClientWarriorCollection();
            units.setWarriors(new Vector(battleContext.getGroupUnits().size() + battleContext.getAllyUnits().size()));
            units.addAll(battleContext.getGroupUnits());
            units.addAll(battleContext.getAllyUnits());

            battleConfiguration.setObserver(new StrictPerimeterObserver(units));
            battleConfiguration.setRouter(new RecursiveWaveRouter());

            //todo: Следует ожидать остальных игроков, т.е. ждать оповещения от сервера, что все игроки залогинились к битве, только после этого закрывать это окно и отображать окошко битвы

            //todo забрать из сообщения группы которые уже залогинились и сообщить о них
            BattleGroup[] loggedIn = battleLogin.getBattleGroups();
            String string = "";
            for (int i = 0; i < loggedIn.length; i++) {
                if (loggedIn[i] != null) {
                    string += loggedIn[i].getAccount().getName() + "\n";
                }
            }
            NotificationBox.info(string, this);
            panelManager.hideAll();
            panelManager.show(panelManager.getHeadlineWindow());
//            panelManager.show(panelManager.getLeft());
            panelManager.show(panelManager.getRight());
            panelManager.show(panelManager.getBattleScreen());
            panelManager.show(panelManager.getBattleMenuWindow());
        } else {
            NotificationBox.error(LocalizedStrings.COULD_NOT_LOGIN_TO_BATTLE, this);
        }
        panelManager.hide(panelManager.getLoginBattleServiceWait());
    }

}
