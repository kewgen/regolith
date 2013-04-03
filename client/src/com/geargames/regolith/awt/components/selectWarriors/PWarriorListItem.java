package com.geargames.regolith.awt.components.selectWarriors;

import com.geargames.awt.components.PSimpleLabel;
import com.geargames.awt.components.PToggleButton;
import com.geargames.common.*;
import com.geargames.common.packer.*;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: abarakov
 * Date: 29.03.13
 * Элемент-кнопка списка бойцов.
 */
public class PWarriorListItem extends PToggleButton { // PPrototypeElement
    private PSprite warriorAvatar;
    private Warrior warrior;

    private PSimpleLabel labelRank;
    private PSimpleLabel labelName;

    public PWarriorListItem(PObject prototype) {
        super(prototype);

        // Воинское звание бойца
        Index index = prototype.getIndexBySlot(11);
        labelRank = new PSimpleLabel((IndexObject) index);
        labelRank.setText("<RANK>");
        labelRank.setFont(PFontCollection.getFont8());
        labelRank.setX(index.getX());
        labelRank.setY(index.getY());

        // Имя бойца
        index = prototype.getIndexBySlot(109);
        labelName = new PSimpleLabel((IndexObject) index);
        labelName.setText("<NAME>");
        labelName.setFont(PFontCollection.getFont8());
        labelName.setX(index.getX());
        labelName.setY(index.getY());
    }

    public PSprite getWarriorAvatar() {
        return warriorAvatar;
    }

    public void setWarriorAvatar(PSprite avatar) {
        this.warriorAvatar = avatar;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;

        //todo: Использовать FrameId для получения изображения бойца
//        warrior.getFrameId();

        labelName.setText(warrior.getName());
        labelRank.setText(warrior.getRank().getName());
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
            setChecked(!getChecked());
            onClick();
        }
        return false;
    }

}
