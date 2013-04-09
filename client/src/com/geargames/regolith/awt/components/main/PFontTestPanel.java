package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.*;
import com.geargames.common.Graphics;
import com.geargames.common.env.Environment;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.Graph;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRootContentPanel;

/**
 * User: abarakov
 * Date: 09.04.13
 */
public class PFontTestPanel extends PRootContentPanel {
    private PEntitledRadioButton button8;
    private PEntitledRadioButton button10;
    private PEntitledRadioButton button12;
    private PEntitledRadioButton button14;
    private PEntitledToggleButton buttonCodeOrChar;

    public PFontTestPanel(PObject prototype) {
        super(prototype);

        PRadioGroup group = new PRadioGroup(3);

        button8 = new PEntitledRadioButton(Environment.getRender().getObject(Graph.OBJ_BUT));
        button8.setText("8");
        addActiveChild(button8, 160, -130);
        group.addButton(button8);

        button10 = new PEntitledRadioButton(Environment.getRender().getObject(Graph.OBJ_BUT));
        button10.setText("10");
        addActiveChild(button10, 160, -80);
        group.addButton(button10);

        button12 = new PEntitledRadioButton(Environment.getRender().getObject(Graph.OBJ_BUT));
        button12.setText("12");
        addActiveChild(button12, 160, -30);
        button12.setChecked(true);
        group.addButton(button12);

        button14 = new PEntitledRadioButton(Environment.getRender().getObject(Graph.OBJ_BUT));
        button14.setText("14");
        addActiveChild(button14, 160, 20);
        group.addButton(button14);

        buttonCodeOrChar = new PEntitledToggleButton(Environment.getRender().getObject(Graph.OBJ_BUT));
        buttonCodeOrChar.setText("CODE / SYMBOL");
        addActiveChild(buttonCodeOrChar, 160, 70);

        PButtonOk button = new PButtonOk(Environment.getRender().getObject(Graph.OBJ_BUT));
        button.setText("ЗАКРЫТЬ");
        addActiveChild(button, 160, 120);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject parentPrototype) {
//        switch (index.getSlot()) {
//            case 17:
//                PButtonOk button = new PButtonOk((PObject)index.getPrototype());
//                button.setText("ЗАКРЫТЬ");
//                addActiveChild(button, index);
//                break;
//            case 22:
//                // Заголовок окна
//                PSimpleLabel labelTitle = new PSimpleLabel(index);
//                labelTitle.setText("ТЕСТ ШРИФТОВ");
//                labelTitle.setFont(PFontCollection.getFontFormTitle());
//                addPassiveChild(labelTitle, index);
//                break;
//        }
    }

    @Override
    protected void createDefaultElementByIndex(IndexObject index, PObject parentPrototype) {
//        switch (parentPrototype.getIndexes().indexOf(index)) {
//            case 1:
//            case 3:
//                break;
//            default:
//                super.createDefaultElementByIndex(index, parentPrototype);
//                break;
//        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    private String[] chars = new String[] {
            " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIGKLMNOPQRSTUVWXYZ[\\]^_`abcdefghigklmnopqrstuvwxyz{|}~",
            "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя"
    };

    @Override
    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);
        x -= 380;
        y -= 160;
        PFont newFont;
        if (button8.getChecked()) {
            newFont = PFontCollection.getFont8();
        } else
        if (button10.getChecked()) {
            newFont = PFontCollection.getFont10();
        } else
        if (button12.getChecked()) {
            newFont = PFontCollection.getFont12();
        } else
        if (button14.getChecked()) {
            newFont = PFontCollection.getFont14();
        } else {
            return;
        }
        PFont oldFont = graphics.getFont();
        graphics.setFont(newFont);
        int yCode = 0;
        for (int i = 0; i < chars.length; i++) {
            int xCode = 0;
            String charSet = chars[i];
            for (int j = 0; j < charSet.length(); j++) {
                String s;
                if (buttonCodeOrChar.getChecked()) {
                    s = String.valueOf(charSet.charAt(j));
                } else {
                    s = String.valueOf((int)charSet.charAt(j));
                }
                graphics.drawString(s, x + xCode * 32, y + yCode * 20, 0);

                if (++xCode == 16) {
                    xCode = 0;
                    yCode++;
                }
            }
        }
        graphics.setFont(oldFont);
    }

    public class PButtonOk extends PEntitledTouchButton {

        public PButtonOk(PObject prototype) {
            super(prototype);
        }

        @Override
        public void onClick() {
            super.onClick();
        }

    }

}
