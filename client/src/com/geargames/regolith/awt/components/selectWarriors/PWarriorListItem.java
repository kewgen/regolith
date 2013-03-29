package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.components.PRadioButton;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.*;
import com.geargames.common.String;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: abarakov
 * Date: 29.03.13
 * Элемент-кнопка списка бойцов.
 */
public class PWarriorListItem extends PRadioButton { // PPrototypeElement
    private PFrame warriorAvatar;
    private Warrior warrior;

    private PSimpleLabel labelRank;
    private PSimpleLabel labelName;

    public PWarriorListItem(PObject prototype) {
        super(prototype);

        // Воинское звание бойца
        Index index = prototype.getIndexBySlot(11);
        labelRank = new PSimpleLabel((IndexObject) index);
        labelRank.setText(String.valueOfC("<RANK>"));
        labelRank.setFont(PFontCollection.getFont8());
        labelRank.setX(index.getX());
        labelRank.setY(index.getY());

        // Имя бойца
        index = prototype.getIndexBySlot(109);
        labelName = new PSimpleLabel((IndexObject) index);
        labelName.setText(String.valueOfC("<NAME>"));
        labelName.setFont(PFontCollection.getFont8());
        labelName.setX(index.getX());
        labelName.setY(index.getY());
    }

    public PFrame getWarriorAvatar() {
        return warriorAvatar;
    }

    public void setWarriorAvatar(PFrame frame) {
        this.warriorAvatar = frame;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;

        //todo: Использовать FrameId для получения изображения бойца
//        warrior.getFrameId();
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);

        labelRank.draw(graphics, x + labelRank.getX(), y + labelRank.getY());
        labelName.draw(graphics, x + labelName.getX(), y + labelName.getY());
    }

    @Override
    public void onClick() {
//        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//        panelManager.getSelectMapPanel().setSelectedMap(battleMap);
    }

    public boolean onEvent(int code, int param, int xTouch, int yTouch) {
        if (code == Event.EVENT_SYNTHETIC_CLICK) {
            onClick();
        }
        return false;
    }

}
