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
 * Date: 06.12.12
 * Time: 9:59
 * Кнопка: запрос на перемещение клади со склада в сумку.
 */
public class PTStoreHouse2BagButton extends PExchangeButton {

    public PTStoreHouse2BagButton(PObject prototype) {
        super(prototype);
        setText("В СУМКУ");
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        BaseConfiguration baseConfiguration = configuration.getBaseConfiguration();
        Account account = ClientConfigurationFactory.getConfiguration().getAccount();
        StoreHouse storeHouse = account.getBase().getStoreHouse();
        short number = (short)getPanel().getNumber();
        StateTackle tackle = TackleTransitionHelper.moveStateTackleStoreHouse2Bag(storeHouse, number,warrior,baseConfiguration);
        if(tackle != null){
            PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel)fabric.getWarrior().getElement());

            warriorPanel.getStoreTacklesElement().initiateMotionListener();
            warriorPanel.getBagTacklesElement().initiateMotionListener();

            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
            ClientMoveTackleByNumberRequest move = new ClientMoveTackleByNumberRequest(configuration);
            move.setNumber(number);
            move.setAmount((short)1);
            move.setRealAmount((short)1);
            move.setWarrior(warrior);
            move.setType(Packets.TAKE_TACKLE_FROM_STORE_HOUSE_PUT_INTO_BAG);
            move.setTackle(tackle);
            BatchMessageManager.getInstance().deferredSend(move, answer);
        }
    }
}
