package com.geargames.regolith.awt.components.selectMaps;

import com.geargames.awt.components.PRadioButton;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: abarakov
 * Date: 28.03.13
 * Элемент-кнопка списка игровых карт.
 */
public class PBattleMapListItem extends PRadioButton { // PPrototypeElement
//    private PFrame battleMapPreview;
    private BattleMap battleMap;

    public PBattleMapListItem(PObject prototype) {
        super(prototype);
    }

//    public PFrame getMapPreview() {
//        return battleMapPreview;
//    }
//
//    public void setMapPreview(PFrame mapPreview) {
//        this.battleMapPreview = mapPreview;
//    }

    public BattleMap getMap() {
        return battleMap;
    }

    public void setMap(BattleMap map) {
        this.battleMap = map;
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);
//        battleMapPreview.draw(graphics, x, y);
    }

    @Override
    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getSelectMapPanel().setSelectedMap(battleMap);
    }

    public boolean onEvent(int code, int param, int xTouch, int yTouch) {
        if (code == Event.EVENT_SYNTHETIC_CLICK) {
            setChecked(true);
            onClick();
        }
        return false;
    }

}
