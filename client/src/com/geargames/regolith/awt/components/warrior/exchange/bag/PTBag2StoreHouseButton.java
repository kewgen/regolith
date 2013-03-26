package com.geargames.regolith.awt.components.warrior.exchange.bag;

import com.geargames.common.String;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Переместить оружие или броню из сумки на склад пользователя.
 */
public class PTBag2StoreHouseButton extends PExchangeButton {
    public PTBag2StoreHouseButton(PObject prototype) {
        super(prototype);
        setText(String.valueOfC("НА СКЛАД"));
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        short number = (short) getPanel().getNumber();
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        StoreHouse house = configuration.getAccount().getBase().getStoreHouse();
        StateTackle tackle = warrior.getBag().getTackles().get(number);

        if (TackleTransitionHelper.moveStateTackleBag2StoreHouse(warrior, number, house) == tackle) {
            PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel) fabric.getWarrior().getElement());

            warriorPanel.getBagTacklesElement().initiateMotionListener();
            warriorPanel.getStoreTacklesElement().initiateMotionListener();

            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
            ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(configuration);
            move.setNumber(number);
            move.setWarrior(warrior);
            move.setAmount((short) 1);
            move.setRealAmount((short) 1);
            move.setTackle(tackle);
            move.setType(Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_STORE_HOUSE);

            BatchMessageManager.getInstance().deferredSend(move, answer);
        }
    }
}
