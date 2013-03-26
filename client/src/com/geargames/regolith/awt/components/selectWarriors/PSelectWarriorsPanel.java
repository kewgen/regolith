package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.battleCreate.PButtonOk;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: abarakov
 * Date: 26.03.13
 * Панель просмотра доступных карт и выбора одной из них для битвы.
 */
// PBrowseMapsPanel
public class PSelectWarriorsPanel extends PContentPanel {

    private int allianceAmount;
    private int allianceSize;
    private int groupSize;
    private BattleMap battleMap;
    private DrawablePPanel currentPanel;

    public PSelectWarriorsPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
//                IndexObject view = (IndexObject) prototype.getIndexBySlot(53);
//
//                Account account = ClientConfigurationFactory.getConfiguration().getAccount();
//                PObject object = (PObject) view.getPrototype();
                break;
            case 1: {
                // Заголовок списка
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.BROWSE_MAPS_LIST_TITLE);
                labelTitle.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
            case 5: {
                // Заголовок списка
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setFont(PFontCollection.getFontLabel());
                addPassiveChild(labelTitle, index);
                break;
            }

            // ----- Общие компоненты ----------------------------------------------------------------------------------

            case 13:
                // Кнопка Ok
                PButtonOk buttonOk = new PButtonOk((PObject) index.getPrototype());
                buttonOk.setText(LocalizedStrings.BUTTON_OK);
                buttonOk.setFont(PFontCollection.getFontButtonCaption());
                addActiveChild(buttonOk, index);
                break;
            case 19:
                // Заголовок окна
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.BROWSE_MAPS_PANEL_TITLE);
                labelTitle.setFont(PFontCollection.getFontFormTitle());
                addPassiveChild(labelTitle, index);
                break;
        }
    }

    public void showPanel(int allianceAmount, int allianceSize, int groupSize, boolean isRandomMap, DrawablePPanel callerPanel) {
        this.allianceAmount = allianceAmount;
        this.allianceSize   = allianceSize;
        this.groupSize      = groupSize;
        if (isRandomMap) {
            battleMap = null;
            currentPanel = callerPanel;
            createBattle();
        } else {
            PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
            currentPanel = panelManager.getSelectMap();
            panelManager.hide(callerPanel);
            panelManager.show(currentPanel);
        }
    }

    public void createBattle() {
        //todo: может вывести панельку, в которой сообщить пользователю о текущем состоянии обмена сообщениями между клиентов и сервером
        // Ожидаем ответа от игроков/сервера. Пожалуйста подождите.



        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.hide(currentPanel);
        panelManager.show(panelManager.getSelectWarriors());
    }

}
