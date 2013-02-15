package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.awt.components.PTouchButton;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.app.Render;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

/**
 * User: mikhail v. kutuzov
 * Морда бойца, для горизонтального меню - отбор в боевую группу.
 */
public class PHorizontalAccountWarriorItem extends PTouchButton {
    private Render render;
    private Warrior warrior;
    private ClientWarriorCollection selected;

    private PLabel rank;
    private PLabel name;

    public PHorizontalAccountWarriorItem(PObject prototype, ClientWarriorCollection selected) {
        super(prototype);
        this.selected = selected;
        IndexObject index = (IndexObject)prototype.getIndexBySlot(2);
        rank = new PSimpleLabel(index);
        rank.setX(index.getX());
        rank.setY(index.getY());

        index = (IndexObject)prototype.getIndexBySlot(2);
        name = new PSimpleLabel(index);
        name.setX(index.getX());
        name.setY(index.getY());
    }

    //todo: сделана система подгрузки картинок
    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);
        name.draw(graphics, x + name.getX(), y + name.getY());
        rank.draw(graphics, x + rank.getX(), y + rank.getY());

        if (selected.contains(warrior)) {

        }
    }

    public void action() {
        PRegolithPanelManager fabric = PRegolithPanelManager.getInstance();
        fabric.showModal(fabric.getWarriorInfo());
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    public Render getRender() {
        return render;
    }

    public void setRender(Render render) {
        this.render = render;
    }
}
