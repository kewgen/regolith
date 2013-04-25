package com.geargames.regolith.awt.components.battleList;

import com.geargames.common.network.DataMessageListener;
import com.geargames.common.packer.PObject;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.common.PWaitingWindow;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.serializers.answers.ClientBattleLoginAnswer;

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
            //todo забрать из сообщения группы которые уже залогинились и сообщить о них
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
