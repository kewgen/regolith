package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.DrawablePPanel;
import com.geargames.common.logging.Debug;
import com.geargames.common.String;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;

/**
 * User: mikhail v. kutuzov
 * Date: 25.12.12
 * Time: 23:43
 */
public class WarriorDrawablePPanel extends DrawablePPanel {
    public void onHide() {
        BatchMessageManager manager = BatchMessageManager.getInstance();
        if (manager.size() > 0) {
            manager.commitMessages();
            manager.retrieve(10000);
            ArrayList answers = manager.getAnswer().getAnswers();
            for (int i = 0; i < answers.size(); i++) {
                if (!((ClientConfirmationAnswer) answers.get(i)).isConfirm()) {
                    Debug.error(String.valueOfC("A FATAL ERROR: bad server operation"));
                    System.exit(1);
                }
            }
        }
    }

    public void onShow() {
    }
}
