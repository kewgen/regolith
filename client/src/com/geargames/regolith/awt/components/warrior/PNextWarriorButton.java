package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Кнопка привязана к панели состояния бойца. Делает следующего бойца в списке клиента текущим.
 */
public class PNextWarriorButton extends PTouchButton {
    private PWarriorCharacteristics characteristics;
    private boolean visible;

    public PNextWarriorButton(PObject prototype, PWarriorCharacteristics characteristics) {
        super(prototype);
        this.characteristics = characteristics;
        setVisible(true);
    }

    public void action() {
        characteristics.next();
        setVisible(characteristics.hasNext());
        characteristics.getPrevious().setVisible(characteristics.hasPrevious());
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}

