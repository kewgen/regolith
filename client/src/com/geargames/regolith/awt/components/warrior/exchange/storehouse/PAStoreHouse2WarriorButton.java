package com.geargames.regolith.awt.components.warrior.exchange.storehouse;

import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.common.String;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;
import com.geargames.regolith.awt.components.menues.StoreHouseVerticalTackles;
import com.geargames.regolith.awt.components.menues.WarriorVerticalTackles;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.base.StoreHouseHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.*;

/**
 * User: mikhail v. kutuzov
 * Взять вещи со склада пользователя и надеть вещи на бойца.
 */
public class PAStoreHouse2WarriorButton extends PExchangeButton {

    public PAStoreHouse2WarriorButton(PObject prototype) {
        super(prototype, String.valueOfC("ЗАРЯДИТЬ"));
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

                            PPanelSingletonFabric fabric = PPanelSingletonFabric.getInstance();
                            PWarriorPanel warriorPanel = ((PWarriorPanel)fabric.getWarrior().getElement());

                            warriorPanel.getWarriorTacklesElement().initiateMotionListener();
                            warriorPanel.getStoreTacklesElement().initiateMotionListener();

                            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
                            ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(configuration);
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
