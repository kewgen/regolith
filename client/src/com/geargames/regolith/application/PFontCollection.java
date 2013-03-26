package com.geargames.regolith.application;

import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFontComposite;
import com.geargames.common.packer.PFontManager;

/**
 * User: abarakov
 * Date: 04.03.13
 */
public class PFontCollection {

    private static PFontCollection instance;

    private PFont font8;
    private PFont font10;
    private PFont font12; // == baseFont
    private PFont font14;

    private PFontCollection(PFontManager fontManager, PFont baseFont) {
        this.font12 = baseFont;

        this.font8  = fontManager.createReSizedFont((PFontComposite) baseFont, 8);
        this.font10 = fontManager.createReSizedFont((PFontComposite) baseFont, 10);
        this.font14 = fontManager.createReSizedFont((PFontComposite) baseFont, 14);
    }

    public static void initiate(PFontManager fontManager, PFont baseFont) {
        instance = new PFontCollection(fontManager, baseFont);
    }

//    public static PFont getBaseFont() {
//        return instance.baseFont;
//    }

    private static void checkInstance() {
        if (instance == null) {
            throw new ExceptionInInitializerError("PFontCollection: instance == null");
        }
    }

    public static PFont getFont8() {
        checkInstance();
        return instance.font8;
    }

    public static PFont getFont10() {
        checkInstance();
        return instance.font10;
    }

    public static PFont getFont12() {
        checkInstance();
        return instance.font12;
    }

    public static PFont getFont14() {
        checkInstance();
        return instance.font14;
    }

    public static PFont getFontButtonCaption() {
        checkInstance();
        return instance.font12;
    }

    public static PFont getFontButtonCaptionSmall() {
        checkInstance();
        return instance.font12;
    }

    public static PFont getFontButtonCaptionLarge() {
        checkInstance();
        return instance.font12;
    }

    public static PFont getFontFormTitle() {
        checkInstance();
        return instance.font14;
    }

    public static PFont getFontLabel() {
        checkInstance();
        return instance.font12;
    }

    public static PFont getFontLabelSmall() {
        checkInstance();
        return instance.font12;
    }

    public static PFont getFontLabelLarge() {
        checkInstance();
        return instance.font12;
    }

    public static PFont getFontListTitle() {
        checkInstance();
        return instance.font12;
    }

    public static PFont getFontHint() {
        checkInstance();
        return instance.font12;
    }

}