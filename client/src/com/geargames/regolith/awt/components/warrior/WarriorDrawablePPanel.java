package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.DrawablePPanel;
import com.geargames.common.logging.Debug;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;

/**
 * User: mikhail v. kutuzov
 * Date: 25.12.12
 * Time: 23:43
 */
public class WarriorDrawablePPanel extends DrawablePPanel {

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
        BatchMessageManager manager = BatchMessageManager.getInstance();
        if (manager.size() > 0) {
            try {
                ArrayList answers = manager.commitMessages().getAnswers();
                for (int i = 0; i < answers.size(); i++) {
                    if (!((ClientConfirmationAnswer) answers.get(i)).isConfirm()) {
                        Debug.error("A FATAL ERROR: bad server operation");
                        System.exit(1);
                    }
                }
            } catch (Exception e) {
                 //todo:  Написать корректный обработчик события
            }
        }
    }

    @Override
    public byte getLayer() {
        return MIDDLE_LAYER;
    }

}
