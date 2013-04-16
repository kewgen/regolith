package com.geargames.regolith.awt.components;

import com.geargames.awt.components.PContentPanel;
import com.geargames.common.packer.PObject;

/**
 * User: abarakov
 * Date: 29.03.13
 */
public abstract class PRootContentPanel extends PContentPanel {

    public PRootContentPanel(PObject prototype) {
        super(prototype);
    }

    /**
     * Обработчик события отображения окна.
     */
    public abstract void onShow();

    /**
     * Обработчик события закрытия окна.
     */
    public abstract void onHide();

//    /**
//     * Обработчик события принудительного закрытия окна. Возникает в случае щелчка вне Touch-области модального окна и
//     * если ModalAutoClose == true.
//     */
//    public abstract void onCancel();

}