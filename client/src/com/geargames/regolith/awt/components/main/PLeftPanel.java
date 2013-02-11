package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Панелька висит в левом верзнем углу, на ней примостилась кнопка перехода "назад".
 */
public class PLeftPanel extends PContentPanel {
    private PBackButton button;

    public PLeftPanel(PObject prototype) {
        super(prototype);
        IndexObject index = (IndexObject)prototype.getIndexBySlot(1);
        button = new PBackButton((PObject)index.getPrototype());
        addActiveChild(button, index);
    }

}
