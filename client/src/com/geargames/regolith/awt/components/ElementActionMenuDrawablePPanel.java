package com.geargames.regolith.awt.components;

import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.BattleScreen;

/**
 * Предок всех панелек, которые отображаются поверх всех остальных панелек.
 * User: abarakov
 * Date: 08.04.13
 */
public class ElementActionMenuDrawablePPanel extends DefaultDrawablePPanel {
    private int cellX;
    private int cellY;

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellXY(int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
    }

    @Override
    public byte getLayer() {
        return TOP_LAYER;
    }

    @Override
    public void onShow() {
        realign();
        super.onShow();
    }

    /**
     * Обработчик события прокрутки игровой карты.
     */
    public void onScrollingMapChanged() {
        realign();
    }

    private void realign() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        BattleScreen battleScreen = panelManager.getBattleScreen();
        Pair pair = ClientConfigurationFactory.getConfiguration().getCoordinateFinder().find(cellX, cellY, battleScreen);
        setX(pair.getX() - battleScreen.getMapX());
        setY(pair.getY() - battleScreen.getMapY());
    }

}
