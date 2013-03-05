package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Кнопка привязана к панели состояния бойца. Делает предыдущего бойца текущим в списке клиента.
 */
public class PPreviousWarriorButton extends PTouchButton {
    private PWarriorCharacteristics characteristics;

    public PPreviousWarriorButton(PObject prototype, PWarriorCharacteristics characteristics) {
        super(prototype);
        this.characteristics = characteristics;
    }

    public void onClick() {
        characteristics.previous();
        setVisible(characteristics.hasPrevious());
        characteristics.getNext().setVisible(characteristics.hasNext());
    }

}
