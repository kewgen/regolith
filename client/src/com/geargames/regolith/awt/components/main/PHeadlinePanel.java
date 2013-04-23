package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.env.Environment;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.util.NullRegion;
import com.geargames.common.util.Region;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * Панель "Заголовок" висит посередине верхней стороны экрана.
 */
public class PHeadlinePanel extends PRootContentPanel implements TimerListener {
    private PLabel label;
    private int timerId;
    private BattleAlliance alliance;
    private long expirationTime; // время в миллисекундах, когда хд завершится

    public PHeadlinePanel(PObject prototype) {
        super(prototype);
        timerId = TimerManager.NULL_TIMER;
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                createDefaultElementByIndex(index, prototype);
                break;
            case 109:
                label = new PSimpleLabel(index);
//                label.setFont(PFontCollection.getFontTitle());
                addPassiveChild(label, index);
                break;
        }
    }

    @Override
    public Region getDrawRegion() {
        return NullRegion.instance;
    }

    @Override
    public Region getTouchRegion() {
        return NullRegion.instance;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {
        TimerManager.killTimer(timerId);
    }

    public void changeLabel() {
        long remainingTime = (Environment.currentTimeMillis() - expirationTime) / 1000;
        if (remainingTime < 0 ) {
            remainingTime = 0;
        }
        label.setText("ХОД АЛЬЯНСА #" + alliance.getNumber() + " (ID=" + alliance.getId() + ") ВРЕМЯ = " + remainingTime);
    }

    public void onTimer(int timerId) {
        changeLabel();
    }

    public void setActiveAlliance(BattleAlliance alliance) {
        this.expirationTime = Environment.currentTimeMillis() + alliance.getBattle().getBattleType().getTurnTime() * 1000;
        this.alliance = alliance;
        changeLabel();
        if (timerId == TimerManager.NULL_TIMER) {
            timerId = TimerManager.setPeriodicTimer(1000, this);
        } else {
            TimerManager.setPeriodicTimer(timerId, 1000, this);
        }
    }

}
