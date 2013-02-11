package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Кнопка привязана к панели состояния бойца. Делает предыдущего бойца в списке клиента текущим.
 */
public class PPreviousWarriorButton extends PTouchButton {
    private PWarriorCharacteristics characteristics;
    private boolean visible;

    public PPreviousWarriorButton(PObject prototype, PWarriorCharacteristics characteristics) {
        super(prototype);
        this.characteristics = characteristics;
    }

    public void action() {
        characteristics.previous();
        setVisible(characteristics.hasPrevious());
        characteristics.getNext().setVisible(characteristics.hasNext());
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
