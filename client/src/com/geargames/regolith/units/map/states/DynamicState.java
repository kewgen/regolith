package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class DynamicState extends UnitState {
    private byte current;
    private byte limit;
    private byte base;

    @Override
    public void init(Render render, byte base) {
        current = 0;
        this.base = base;
        limit = (byte)render.getUnitScript(base).getIndexes().size();
    }

    @Override
    public PUnit getPUnit(BattleUnit unit, Render render) {
        Warrior warrior = unit.getWarrior();
        int direction = warrior.getDirection().getNumber();
        direction = direction < 7 ? direction + 1 : 0;
        String category = warrior.getWeapon().getWeaponType().getCategory().getName();
        int weaponOffset;
        if (category.equals("ДАЛЬНОБОЙНОЕ")) {
            weaponOffset = 0;
        } else {
            return null;
        }

        return (PUnit) (render.getUnitScript(weaponOffset + direction).getIndex(current).getPrototype());
    }

    @Override
    public void onTime() {
        if (current < limit) {
            current++;
        }
    }
}
