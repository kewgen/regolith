package com.geargames.regolith.awt.components.warrior.exchange.storehouse;

import com.geargames.common.packer.PObject;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.helpers.StoreHouseHelper;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumberRequest;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.*;

/**
 * User: mikhail v. kutuzov
 * Взять вещи со склада пользователя и надеть вещи на бойца.
 */
public class PAStoreHouse2WarriorButton extends PExchangeButton {

    public PAStoreHouse2WarriorButton(PObject prototype) {
        super(prototype);
        setText("ЗАРЯДИТЬ");
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        BaseConfiguration baseConfiguration = configuration.getBaseConfiguration();
        Account account = ClientConfigurationFactory.getConfiguration().getAccount();
        StoreHouse storeHouse = account.getBase().getStoreHouse();
        int number = getPanel().getNumber();
        int amount = getPanel().getAmountBox().getValue();
        AmmunitionPacket packet = StoreHouseHelper.getAmmunition(storeHouse, number);
        if (packet != null) {
            int realAmount = TackleTransitionHelper.moveAmmunitionStoreHouse2Warrior(storeHouse, number, amount, warrior, baseConfiguration);

            if (realAmount != 0) {
                Ammunition ammunition = packet.getAmmunition();
                switch (ammunition.getType()) {
                        case TackleType.PROJECTILE:

                            PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
                            PWarriorPanel warriorPanel = ((PWarriorPanel)fabric.getWarrior().getElement());

                            warriorPanel.getWarriorTacklesElement().initiateMotionListener();
                            warriorPanel.getStoreTacklesElement().initiateMotionListener();

                            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
                            ClientMoveTackleByNumberRequest move = new ClientMoveTackleByNumberRequest(configuration);
                            move.setNumber((short) number);
                            move.setWarrior(warrior);
                            move.setAmount((short) amount);
                            move.setRealAmount((short) (realAmount));
                            move.setType(Packets.TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_ONTO_WARRIOR);
                            move.setTackle(ammunition);
                            BatchMessageManager.getInstance().deferredSend(move, answer);
                            break;
                    case TackleType.MEDIKIT:
                        break;
                }
            }
        }
    }
}
