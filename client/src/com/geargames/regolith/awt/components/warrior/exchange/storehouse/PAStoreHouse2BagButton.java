package com.geargames.regolith.awt.components.warrior.exchange.storehouse;

import com.geargames.common.String;
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
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.base.StoreHouseHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 *
 */
public class PAStoreHouse2BagButton extends PExchangeButton {

    public PAStoreHouse2BagButton(PObject prototype) {
        super(prototype, String.valueOfC("В СУМКУ"));
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        BaseConfiguration baseConfiguration = configuration.getBaseConfiguration();
        Account account = ClientConfigurationFactory.getConfiguration().getAccount();
        StoreHouse storeHouse = account.getBase().getStoreHouse();
        int number = getPanel().getNumber();
        int amount = getPanel().getAmountBox().getValue();
        Ammunition ammunition = StoreHouseHelper.getAmmunition(storeHouse, number).getAmmunition();

        int realAmount = TackleTransitionHelper.moveAmmunitionStoreHouse2Bag(storeHouse, number, amount, warrior, baseConfiguration);
        if (realAmount != 0) {
            PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel)fabric.getWarrior().getElement());

            warriorPanel.getStoreTacklesElement().initiateMotionListener();
            warriorPanel.getBagTacklesElement().initiateMotionListener();

            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
            ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(configuration);
            move.setNumber((short)number);
            move.setAmount((short)amount);
            move.setRealAmount((short) realAmount);
            move.setWarrior(warrior);
            move.setType(Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_INTO_BAG);
            move.setTackle(ammunition);
            BatchMessageManager.getInstance().deferredSend(move, answer);
        }
    }
}
