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
import com.geargames.regolith.units.dictionaries.ClientHumanElementCollection;

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

            ClientHumanElementCollection groupUnits = panelManager.getBattleScreen().getGroupUnits();
            ClientHumanElementCollection allyUnits = panelManager.getBattleScreen().getAllyUnits();

            ClientHumanElementCollection units = new ClientHumanElementCollection();
            units.setElements(new Vector(groupUnits.size() + allyUnits.size()));
            units.addAll(groupUnits);
            units.addAll(allyUnits);

            battleConfiguration.setObserver(new StrictPerimeterObserver(units));
            battleConfiguration.setRouter(new RecursiveWaveRouter());

            //todo забрать из сообщения группы которые уже залогинились и сообщить о них
            BattleGroup[] loggedIn = battleLogin.getBattleGroups();
            String string = null ;
            for(int i = 0; i < loggedIn.length; i++){
                if(loggedIn[i] != null){
                    string += loggedIn[i].getAccount().getName();
                }
            }
            NotificationBox.info(string, this);
            panelManager.hideAll();
            panelManager.getBattleScreen().setBattle(ClientConfigurationFactory.getConfiguration().getBattle());
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
