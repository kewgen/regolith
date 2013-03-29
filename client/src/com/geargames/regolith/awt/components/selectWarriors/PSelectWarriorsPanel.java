package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.localization.LocalizedStrings;

/**
 * User: abarakov
 * Date: 26.03.13
 * Панель просмотра доступных карт и выбора одной из них для битвы.
 */
// PBrowseMapsPanel
public class PSelectWarriorsPanel extends PContentPanel {

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
                labelTitle.setText(LocalizedStrings.SELECT_WARRIORS_LIST_TITLE);
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

            case 3:
                // Кнопка Ok
                PButtonOk buttonOk = new PButtonOk((PObject) index.getPrototype());
                buttonOk.setText(LocalizedStrings.BUTTON_OK);
                buttonOk.setFont(PFontCollection.getFontButtonCaption());
                addActiveChild(buttonOk, index);
                break;
//            case 19:
//                // Заголовок окна
//                PSimpleLabel labelTitle = new PSimpleLabel(index);
//                labelTitle.setText(LocalizedStrings.SELECT_WARRIORS_PANEL_TITLE);
//                labelTitle.setFont(PFontCollection.getFontFormTitle());
//                addPassiveChild(labelTitle, index);
//                break;
        }
    }

}
