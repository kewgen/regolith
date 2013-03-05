package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Кнопка привязана к панели состояния бойца. Делает следующего бойца текущим в списке клиента.
 */
public class PNextWarriorButton extends PTouchButton {
    private PWarriorCharacteristics characteristics;

    public PNextWarriorButton(PObject prototype, PWarriorCharacteristics characteristics) {
        super(prototype);
        this.characteristics = characteristics;
        setVisible(true);
    }

    public void onClick() {
        characteristics.next();
        setVisible(characteristics.hasNext());
        characteristics.getPrevious().setVisible(characteristics.hasPrevious());
    }

}

