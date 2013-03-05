package com.geargames.regolith.awt.components.warrior.exchange;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PValueComponent;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.AbstractTackle;

/**
 * User: mikhail.kutuzov
 * Базовая панель для перемещения вещей.
 */
public abstract class PExchangePanel extends PContentPanel {
    private int number;
    private boolean initiated;

    private AbstractTackle tackle;
    private int amount;

    protected PExchangePanel(PObject prototype) {
        super(prototype);
    }

    protected abstract void addButton1(PObject prototype, Index index);

    protected abstract void addButton2(PObject prototype, Index index);

    protected abstract void initiate();

    public abstract PValueComponent getAmountBox();

    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            initiate();
            initiated = true;
        }
        super.draw(graphics, x, y);
    }

    public boolean onEvent(int code, int param, int xTouch, int yTouch) {
        boolean result = false;
        if (code >= Event.EVENT_KEY_PRESSED && code <= Event.EVENT_KEY_DOWN) {
            if (getTouchRegion().isWithIn(xTouch, yTouch)) {
                result = super.onEvent(code, param, xTouch, yTouch);
            } else {
                if (code == Event.EVENT_TOUCH_RELEASED) {
                    PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
                    if (fabric.isModal()) {
                        fabric.hideModal();
                    }
                }
            }
        } else {
            result = super.onEvent(code, param, xTouch, yTouch);
        }
        return result;
    }

    /**
     * Номер элемента меню.
     *
     * @return
     */
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        initiated = false;
    }

    /**
     * Текущий боец.
     *
     * @return
     */
    public Warrior getWarrior() {
        return ((PWarriorPanel) PRegolithPanelManager.getInstance().getWarrior().getElement()).getCharacteristics().getWarrior();
    }

    /**
     * Вернуть вещь которая предполагается к перемещению.
     *
     * @return
     */
    public AbstractTackle getTackle() {
        return tackle;
    }

    /**
     * Количество вещей.
     *
     * @return
     */
    public int getAmount() {
        return amount;
    }

    public void setTackle(AbstractTackle tackle, int amount){
        this.tackle = tackle;
        this.amount = amount;
        initiated = false;
    }

}
