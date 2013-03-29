package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.CenteredElasticInertMotionListener;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.DefaultPContentPanel;
import com.geargames.regolith.localization.LocalizedStrings;

/**
 * User: abarakov
 * Date: 26.03.13
 * Панель просмотра доступных для найма бойцов и выбора одного или нескольких из них для битвы.
 */
public class PSelectWarriorsPanel extends DefaultPContentPanel {

    private PWarriorList warriorList;

    public PSelectWarriorsPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 1: {
                // Заголовок списка
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.SELECT_WARRIORS_LIST_TITLE);
                labelTitle.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
            case 2: {
                // Список бойцов
                warriorList = new PWarriorList((PObject) index.getPrototype());
                addActiveChild(warriorList, index);

                CenteredElasticInertMotionListener motionListener = new CenteredElasticInertMotionListener();
                motionListener.setInstinctPosition(false);
                warriorList.setMotionListener(
                        ScrollHelper.adjustHorizontalCenteredMenuMotionListener(
                                motionListener,
                                warriorList.getDrawRegion(),
                                warriorList.getItemsAmount(),
                                warriorList.getItemSize(),
                                warriorList.getPrototype().getDrawRegion().getMinX()
                        )
                );
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
            case 109: {
                // Заголовок окна
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.SELECT_WARRIORS_PANEL_TITLE);
                labelTitle.setFont(PFontCollection.getFontFormTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

}
