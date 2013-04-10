package com.geargames.regolith.awt.components.common;

import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PObjectElement;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PObject;

/**
 * Кликабельный лейбл.
 * User: abarakov
 * Date: 08.04.13
 */
public abstract class PTouchLabel extends PObjectElement {
    private PLabel label;
    private boolean isPushed;

    public PTouchLabel(PObject prototype) {
        super(prototype);
        IndexObject index = (IndexObject) prototype.getIndexBySlot(109);
        label = new PSimpleLabel(index);
        label.setX(index.getX());
        label.setY(index.getY());
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        label.draw(graphics, x + label.getX(), y + label.getY());
    }

    public String getText() {
        return label.getText();
    }

    public void setText(String text) {
        label.setText(text);
    }

    public PFont getFont() {
        return label.getFont();
    }

    public void setFont(PFont font) {
        label.setFont(font);
    }

    @Override
    public boolean onEvent(int code, int param, int x, int y) {
        switch (code) {
            case Event.EVENT_TOUCH_PRESSED:
                isPushed = true;
                return true;
            case Event.EVENT_TOUCH_RELEASED:
                boolean oldPushed = isPushed;
                isPushed = false;
                if (oldPushed && getTouchRegion().isWithIn(x, y)) {
                    onClick();
                }
                break;
        }
        return false;
    }

    /**
     * Обработчик события возникающего при клике по поверхности лейбла.
     */
    public abstract void onClick();

}
