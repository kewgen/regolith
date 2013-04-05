package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;

/**
 * User: mikhail v. kutuzov, abarakov
 * Кнопка для входа в меню битв.
 */
public class PBattlesButton extends PTouchButton {

    public PBattlesButton(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
        try {
            ClientConfirmationAnswer answer = ClientConfigurationFactory.getConfiguration().getBaseManager().goBattleManager();
            if (answer.isConfirm()) {
                PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
                panelManager.getBattlesPanel().showPanel(null, panelManager.getMainMenu(), false);
            } else {
                Debug.error("the server has not confirmed a transmission into the [battle market] state");
            }
        } catch (Exception e) {
            Debug.error("could not retrieve synchronous answer for a message: [go to battle market]", e);
        }
    }

}