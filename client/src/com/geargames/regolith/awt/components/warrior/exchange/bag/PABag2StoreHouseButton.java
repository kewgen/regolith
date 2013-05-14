package com.geargames.regolith.awt.components.warrior.exchange.bag;

import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumberRequest;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.AbstractTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Переместить из сумки на склад пользователя аммуницию бойца.
 */
public class PABag2StoreHouseButton extends PExchangeButton {
    public PABag2StoreHouseButton(PObject prototype) {
        super(prototype);
        setText("НА СКЛАД");
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        int number = getPanel().getNumber() - warrior.getBag().getTackles().size();
        AbstractTackle tackle = warrior.getAmmunitionBag().getPackets().get(number).getAmmunition();
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        StoreHouse storeHouse = configuration.getAccount().getBase().getStoreHouse();
        int amount = getPanel().getAmountBox().getValue();
        int realAmount = TackleTransitionHelper.moveAmmunitionBag2StoreHouse(warrior,number,amount,storeHouse);
        if (realAmount != 0) {
            PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel) fabric.getWarrior().getElement());

            warriorPanel.getBagTacklesElement().initiateMotionListener();
            warriorPanel.getStoreTacklesElement().initiateMotionListener();

            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();

            ClientMoveTackleByNumberRequest move = new ClientMoveTackleByNumberRequest(configuration);
            move.setNumber((short)number);
            move.setWarrior(warrior);
            move.setAmount((short) amount);
            move.setRealAmount((short)realAmount);
            move.setTackle(tackle);
            move.setType(Packets.TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE);

            BatchMessageManager.getInstance().deferredSend(move, answer);
        }
    }
}
