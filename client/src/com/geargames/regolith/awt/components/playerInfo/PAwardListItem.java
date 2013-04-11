package com.geargames.regolith.awt.components.playerInfo;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PObject;
import com.geargames.common.packer.PSprite;

/**
 * User: abarakov
 * Date: 10.04.13
 * Элемент-кнопка списка наград игрока.
 */
public class PAwardListItem extends PTouchButton {
    private PSprite awardIcon;
//    private Award award;

    public PAwardListItem(PObject prototype) {
        super(prototype);
    }

//    public PSprite getAwardIcon() {
//        return awardIcon;
//    }
//
//    public void setAwardIcon(PSprite icon) {
//        this.awardIcon = icon;
//    }
//
//    public Award getAward() {
//        return award;
//    }
//
//    public void setAward(Award award) {
//        this.award = award;
//
//        //todo: Использовать FrameId для получения иконки награды
////        award.getFrameId();
//    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);
    }

//    @Override
//    public void onClick() {
////        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
////        panelManager.getAwardInfoPanel().showPanel(award);
//    }

    public boolean onEvent(int code, int param, int xTouch, int yTouch) {
        if (code == Event.EVENT_SYNTHETIC_CLICK) {
            onClick();
        }
        return false;
    }

}
