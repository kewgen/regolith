package com.geargames.regolith.awt.components.warrior.exchange.bag;

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
import com.geargames.regolith.units.tackle.*;

/**
 * User: mikhail v. kutuzov
 * Сложить аммуницию в оружие бойца.
 */
public class PABag2WarriorButton extends PExchangeButton {
    public PABag2WarriorButton(PObject prototype) {
        super(prototype, com.geargames.common.String.valueOfC("ЗАРЯДИТЬ"));
    }

    public void doAction() {
        Warrior warrior = getPanel().getWarrior();
        int number = getPanel().getNumber() - warrior.getBag().getTackles().size();
        int amount = getPanel().getAmountBox().getValue();
        AbstractTackle tackle = warrior.getAmmunitionBag().getPackets().get(number).getAmmunition();
        int realAmount = TackleTransitionHelper.moveAmmunitionBag2Warrior(warrior, number, amount);

        if (realAmount != 0) {
            PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
            PWarriorPanel warriorPanel = ((PWarriorPanel) fabric.getWarrior().getElement());

            warriorPanel.getBagTacklesElement().initiateMotionListener();
            warriorPanel.getWarriorTacklesElement().initiateMotionListener();

            ClientConfirmationAnswer answer = new ClientConfirmationAnswer();
            ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(ClientConfigurationFactory.getConfiguration());
            move.setNumber((short) number);
            move.setWarrior(warrior);
            move.setAmount((short) amount);
            move.setRealAmount((short) realAmount);
            move.setTackle(tackle);
            move.setType(Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_BODY);

            BatchMessageManager.getInstance().deferredSend(move, answer);
        }
    }
}
