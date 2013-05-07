package com.geargames.regolith.awt.components.warrior.exchange.storehouse;

import com.geargames.common.packer.PObject;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumberRequest;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.*;

/**
 * User: mikhail v. kutuzov
 * Надеть предмет со склада.
 */
public class PTStoreHouse2WarriorButton extends PExchangeButton {

    public PTStoreHouse2WarriorButton(PObject prototype) {
        super(prototype);
        setText("НАДЕТЬ");
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        BaseConfiguration baseConfiguration = configuration.getBaseConfiguration();
        Account account = ClientConfigurationFactory.getConfiguration().getAccount();
        StoreHouse storeHouse = account.getBase().getStoreHouse();
        short number = (short) getPanel().getNumber();
        StateTackle tackle = TackleTransitionHelper.moveStateTackleStoreHouse2Warrior(storeHouse, number, warrior, baseConfiguration);
        if (tackle != null) {
            PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel) fabric.getWarrior().getElement());

            warriorPanel.getWarriorTacklesElement().initiateMotionListener();
            warriorPanel.getStoreTacklesElement().initiateMotionListener();

            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
            ClientMoveTackleByNumberRequest move = new ClientMoveTackleByNumberRequest(configuration);
            move.setNumber(number);
            move.setWarrior(warrior);
            move.setAmount((short) (1));
            move.setRealAmount((short) 1);
            move.setType(Packets.TAKE_TACKLE_FROM_STORE_HOUSE_PUT_ON_WARRIOR);
            BatchMessageManager.getInstance().deferredSend(move, answer);
        }
    }
}
