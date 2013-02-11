package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;

/**
 * User: mikhail v. kutuzov
 *
 */
public class PWarriorInformationConfirm extends PTouchButton {
    public PWarriorInformationConfirm(PObject prototype) {
        super(prototype);
    }

    public void action() {


        PPanelSingletonFabric fabric = PPanelSingletonFabric.getInstance();
        fabric.hideModal();
    }
}
