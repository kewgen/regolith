package com.geargames.regolith.awt.components.warrior.exchange.bag;

import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.common.String;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;
import com.geargames.regolith.awt.components.menues.BagVerticalTackles;
import com.geargames.regolith.awt.components.menues.StoreHouseVerticalTackles;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
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
        super(prototype, String.valueOfC("НА СКЛАД"));
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
            PPanelSingletonFabric fabric = PPanelSingletonFabric.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel) fabric.getWarrior().getElement());

            warriorPanel.getBagTacklesElement().initiateMotionListener();
            warriorPanel.getStoreTacklesElement().initiateMotionListener();

            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();

            ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(configuration);
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
