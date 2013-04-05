package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PObjectElement;
import com.geargames.awt.components.PPrototypeElement;
import com.geargames.common.env.Environment;
import com.geargames.common.util.Region;
import com.geargames.common.Graphics;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Индикатор состояния на основе фрейма.
 */
public class PFrameProgressIndicator extends PObjectElement {
    private int indicator;
    private int cardinality;
    private int value;
    private PPrototypeElement background;

    public PFrameProgressIndicator(PObject prototype) {
        super(prototype);

        //подложка
        Index index0 = prototype.getIndex(0);
        background = new PPrototypeElement();
        background.setPrototype(index0.getPrototype());
        Region region = new Region();
        background.setRegion(region);
        region.setMinX(index0.getX());
        region.setMinY(index0.getY());

        //фрейм-индикатор
        index0 = prototype.getIndex(1);
        indicator = index0.getPrototype().getPID();

        //количество фреймов жизни
        this.cardinality = prototype.getIndex(3).getX();
    }

    public void draw(Graphics graphics, int x, int y) {
        background.draw(graphics, x, y);
        Environment.getRender().getFrame(indicator + value).draw(graphics, x, y);
    }

    public int getCardinality() {
        return cardinality;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value > cardinality) {
            value = cardinality;
        }
        this.value = value;
    }

}
