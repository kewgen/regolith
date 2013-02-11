package com.geargames.regolith.awt.components.warrior.exchange.warrior;

import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.common.String;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;
import com.geargames.regolith.awt.components.menues.BagVerticalTackles;
import com.geargames.regolith.awt.components.menues.WarriorVerticalTackles;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Кнопка снимающая вещи с война в сумку.
 */
public class PTWarrior2BagButton extends PExchangeButton {

    public PTWarrior2BagButton(PObject prototype) {
        super(prototype, String.valueOfC("В СУМКУ"));
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        StateTackle tackle = (StateTackle) getPanel().getTackle();
        TackleTransitionHelper.moveStateTackleWarrior2Bag(warrior, tackle);

        PPanelSingletonFabric fabric = PPanelSingletonFabric.getInstance();
        PWarriorPanel warriorPanel = ((PWarriorPanel)fabric.getWarrior().getElement());
        warriorPanel.getWarriorTacklesElement().initiateMotionListener();
        warriorPanel.getBagTacklesElement().initiateMotionListener();

        ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
        ClientMoveTackle move = new ClientMoveTackle(ClientConfigurationFactory.getConfiguration());
        move.setTackle(tackle);
        move.setWarrior(warrior);
        move.setType(Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG);
        BatchMessageManager.getInstance().deferredSend(move, answer);
    }
}
