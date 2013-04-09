package com.geargames.regolith.awt.components.main;

import com.geargames.common.logging.Debug;
import com.geargames.common.network.DataMessageListener;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.awt.components.common.PShowingModalLabel;

/**
 * Панелька, на которой отображаются текущее количество игровых ресурсов доступных игроку.
 * Users: mikhail v. kutuzov, abarakov
 * Date: 11.01.13
 */
public class PMoneyRegolithPanel extends PRootContentPanel implements DataMessageListener {
    private PShowingModalLabel moneyLabel;
    private PShowingModalLabel regolithLabel;
    private short[] types;

    public PMoneyRegolithPanel(PObject prototype) {
        super(prototype);
        types = new short[] {
//                Packets.MONEY_UPDATE
        };
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                createDefaultElementByIndex(index, prototype);
                break;
            case 1:
                moneyLabel = new PShowingModalLabel((PObject)index.getPrototype());
                moneyLabel.setText("0");
//                moneyLabel.setPanel();
                addActiveChild(moneyLabel, index);
                break;
            case 2:
                regolithLabel = new PShowingModalLabel((PObject)index.getPrototype());
                regolithLabel.setText("0");
//                regolithLabel.setPanel();
                addActiveChild(regolithLabel, index);
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
//                case Packets.MONEY_UPDATE:
//                    Debug.debug("PBattlesList.onReceive(type = " + type + "): MONEY_UPDATE");
//                    ClientMoneyUpdate moneyUpdate = (ClientMoneyUpdate) message;
//                    moneyLabel.setText(moneyUpdate.getMoney());
//                    regolithLabel.setText(moneyUpdate.getRegolith());
//                    break;
                default:
                    Debug.error("There is a message of type = " + type);
            }
        } catch (Exception e) {
            Debug.error("Exception while processing a message of type = " + type, e);
        }
    }

    @Override
    public void onShow() {
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().register(this);
    }

    @Override
    public void onHide() {
        ClientConfigurationFactory.getConfiguration().getMessageDispatcher().unregister(this);
    }

}
