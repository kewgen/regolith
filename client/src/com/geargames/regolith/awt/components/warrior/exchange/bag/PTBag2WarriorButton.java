package com.geargames.regolith.awt.components.warrior.exchange.bag;

import com.geargames.common.String;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.PExchangeButton;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;

/**
 * User: mikhail v. kutuzov
 * Надеть оружие или броню из сумки текущего бойца на него.
 */
public class PTBag2WarriorButton extends PExchangeButton {
    public PTBag2WarriorButton(PObject prototype) {
        super(prototype, String.valueOfC("НАДЕТЬ"));
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        int number = getPanel().getNumber();

        StateTackle tackle = TackleTransitionHelper.moveStateTackleBag2Warrior(warrior, number);

        PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
        PWarriorPanel warriorPanel = ((PWarriorPanel) fabric.getWarrior().getElement());

        warriorPanel.getBagTacklesElement().initiateMotionListener();
        warriorPanel.getWarriorTacklesElement().initiateMotionListener();

        ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
        ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(ClientConfigurationFactory.getConfiguration());
        move.setNumber((short)number);
        move.setWarrior(warrior);
        move.setAmount((short) 1);
        move.setRealAmount((short) 1);
        move.setTackle(tackle);
        move.setType(Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_BODY);

        BatchMessageManager.getInstance().deferredSend(move, answer);
    }
}
