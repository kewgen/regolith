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
import com.geargames.regolith.awt.components.menues.BagVerticalTackles;
import com.geargames.regolith.awt.components.menues.StoreHouseVerticalTackles;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
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
        super(prototype, String.valueOfC("В СУМКУ"));
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
            PPanelSingletonFabric fabric = PPanelSingletonFabric.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel)fabric.getWarrior().getElement());

            warriorPanel.getStoreTacklesElement().initiateMotionListener();
            warriorPanel.getBagTacklesElement().initiateMotionListener();

            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
            ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(configuration);
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
