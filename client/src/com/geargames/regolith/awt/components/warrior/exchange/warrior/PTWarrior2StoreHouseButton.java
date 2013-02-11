package com.geargames.regolith.awt.components.warrior.exchange.warrior;

import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;
import com.geargames.regolith.awt.components.menues.StoreHouseVerticalTackles;
import com.geargames.regolith.awt.components.menues.WarriorVerticalTackles;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackle;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Кнопка перемещающая вещи надетые на война на склад.
 */
public class PTWarrior2StoreHouseButton extends PExchangeButton {
    public PTWarrior2StoreHouseButton(PObject prototype) {
        super(prototype, com.geargames.common.String.valueOfC("НА СКЛАД"));
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        StateTackle tackle = (StateTackle) getPanel().getTackle();
        StoreHouse house = ClientConfigurationFactory.getConfiguration().getAccount().getBase().getStoreHouse();
        if(TackleTransitionHelper.moveStateTackleWarrior2StoreHouse(warrior, tackle, house)){

            PPanelSingletonFabric fabric = PPanelSingletonFabric.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel)fabric.getWarrior().getElement());
            warriorPanel.getWarriorTacklesElement().initiateMotionListener();
            warriorPanel.getStoreTacklesElement().initiateMotionListener();

            ClientMoveTackle move = new ClientMoveTackle(ClientConfigurationFactory.getConfiguration());
            move.setTackle(tackle);
            move.setWarrior(warrior);
            move.setType(Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_STORE_HOUSE);
            BatchMessageManager.getInstance().deferredSend(move, new ClientConfirmationAnswer());
        }
    }
}
