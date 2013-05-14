package com.geargames.regolith.awt.components;

import com.geargames.awt.Anchors;
import com.geargames.common.util.Region;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.BattleScreen;

/**
 * Окошко для панелек с кнопками действий над выделенным динамическим элементом карты. Окошко располагается поверх всего
 * и перемещается вместе со скроллингом карты, чтобы всегда быть рядом с выделенным элементом.
 * User: abarakov
 * Date: 13.05.13
 */
//todo: Переименовать класс
public class ElementActionMenuDrawablePPanel extends DefaultDrawablePPanel {
    private int cellX;
    private int cellY;

    public ElementActionMenuDrawablePPanel() {
        setAnchor(Anchors.CENTER_ANCHOR);
        cellX = -1;
        cellY = -1;
    }

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
        Region region = getElement().getDrawRegion();
        setX(pair.getX() - battleScreen.getMapX() + region.getMinX());
        setY(pair.getY() - battleScreen.getMapY() + region.getMinY());
    }

}
