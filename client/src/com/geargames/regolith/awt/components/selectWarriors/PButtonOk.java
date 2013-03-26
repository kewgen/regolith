package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.components.PEntitledTouchButton;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.battleCreate.PBattleCreatePanel;
import com.geargames.regolith.managers.ClientBattleCreationManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: abarakov
 * Date: 26.03.13
 * Кнопка Ok для ...
 */
public class PButtonOk extends PEntitledTouchButton {

    public PButtonOk(PObject prototype) {
        super(prototype);
    }

    public void onClick() {
//        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//        panelManager.hide(panelManager.getBattleCreate());
//        panelManager.show(panelManager.getMainMenu());
    }

    /**
     *
     */
    public static void completeGroup() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        PBattleCreatePanel battleCreatePanel = (PBattleCreatePanel) panelManager.getBattleCreate().getElement();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();

//        Debug.debug("The client go to the battle market...");
//        ClientDeferredAnswer answer = battleCreationManager.completeGroup();
//        if (!waitForAnswer(answer)) {
//            Debug.critical("Waiting time answer has expired");
//        }
//        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
//        if (!confirm.isConfirm()) {
//            Debug.critical("The client could not go to the battle market");
//        }
    }

    public static boolean waitForAnswer(ClientDeferredAnswer answer) {
        try {
            return answer.retrieve(100);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
