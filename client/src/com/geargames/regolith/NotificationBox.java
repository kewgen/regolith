package com.geargames.regolith;

import com.geargames.awt.TextHint;
import com.geargames.awt.components.PObjectElement;
import com.geargames.regolith.awt.components.PRegolithPanelManager;

/**
 * User: abarakov
 * Date: 01.04.13
 */
public class NotificationBox {

    private static void message(String text) {
        TextHint.show(text, Port.getW() / 2, Port.getH() / 2);
    }

    private static void message(String text, PObjectElement element/*, int x, int y*/) {
        TextHint.show(text,
                PRegolithPanelManager.getInstance().getEventX()/* - x*/,
                PRegolithPanelManager.getInstance().getEventY()/* - y + element.getDrawRegion().getHeight()*/
        );
    }

    public static void info(String text, PObjectElement element/*, int x, int y*/) {
        message(text, element/*, x, y*/);
    }

    public static void warning(String text, PObjectElement element/*, int x, int y*/) {
        message(text, element/*, x, y*/);
    }

    public static void error(String text, PObjectElement element/*, int x, int y*/) {
        message(text, element/*, x, y*/);
    }

    public static void error(String text){
        message(text);
    }

}