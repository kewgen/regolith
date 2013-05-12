package com.geargames.regolith.units.map.unit;

import com.geargames.common.Graphics;
import com.geargames.common.env.Environment;
import com.geargames.common.packer.PUnit;
import com.geargames.common.packer.PUnitScript;
import com.geargames.regolith.helpers.ClientGUIHelper;
import com.geargames.regolith.units.map.AbstractClientWarriorElement;

/**
 * User: abarakov
 * Date: 01.05.13
 */
// HumanGraphicComponent
public class UnitScriptGraphicComponent extends AbstractUnitScriptGraphicComponent {
    private PUnitScript script; // пакерная анимация
    private byte index;         // номер шага анимации
    private byte limit;         // количество шагов анимации
    private short cyclesCount;  // количество пройденных полных циклов анимации

    public UnitScriptGraphicComponent() {
        script = null;
        index = 0;
        limit = 0;
        cyclesCount = 0;
    }

    @Override
    public void start(AbstractClientWarriorElement warrior, byte action) {
        script = Environment.getRender().getUnitScript(
//                warrior.getFrameId()
                +ClientGUIHelper.convertPackerScriptsDirection(warrior.getDirection())
                        + action
                        + warrior.getWeapon().getWeaponType().getCategory().getPackerId()
        );
        index = 0;
        limit = (byte) script.getIndexes().size();
        cyclesCount = 0;
    }

    @Override
    public void stop() {

    }

    /**
     * Получить количество пройденных полных циклов анимации.
     *
     * @return
     */
    @Override
    public short getCyclesCount() {
        return cyclesCount;
    }

    /**
     * Вернуть текущий PUnit для отображения анимации из PUnitScript.
     *
     * @return
     */
    @Override
    public PUnit getCurrent() {
        return (PUnit) script.getIndex(index).getPrototype();
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        getCurrent().draw(graphics, x, y);
    }

    /**
     * Перейти к следующему кадру анимации из PUnitScript.
     */
    @Override
    public void onTick() {
        index++;
        if (index == limit) {
            index = 0;
            cyclesCount++;
        }
    }

}
