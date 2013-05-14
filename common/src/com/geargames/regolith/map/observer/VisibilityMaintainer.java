package com.geargames.regolith.map.observer;

import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.CellElementTypes;

/**
 * Users: mkutuzov, abarakov
 * Date: 15.03.12
 */
public class VisibilityMaintainer extends BattleCellMaintainer {
    private WarriorCollection enemies;

    public VisibilityMaintainer(WarriorCollection enemies) {
        this.enemies = enemies;
    }

    /**
     * Если ячейку видно, то проверяем: нет ли там преграды для взгляда?
     * если есть возвращаем true, иначе - false.
     * Затем помечаем ячейку видимой бойцом warrior если она не была им видима ранее и она стала видимой.
     * Если ячейку стало не видно - помечаем невидимой и возвращаем true.
     *
     * @param warrior
     * @param hidden
     * @param x       координата по первому измерению массива
     * @param y       координата по второму измерению массива
     * @return
     */
    @Override
    public boolean maintain(BattleCell[][] cells, Warrior warrior, boolean hidden, int x, int y) {
        if (!hidden) {
            BattleCell cell = cells[x][y];
            boolean was = BattleMapHelper.isVisible(cell, warrior.getBattleGroup().getAlliance());
            SecurityOperationManager security = warrior.getBattleGroup().getAccount().getSecurity();
            if (security != null) {
                security.adjustObserve(x + y);
            }
            BattleMapHelper.show(cell, warrior);
            if (cell.getElement() != null) {
                hidden = !cell.getElement().isAbleToLookThrough();
                if (!was && cell.getElement().getElementType() == CellElementTypes.HUMAN) {
                    Warrior warriorElement = (Warrior) cell.getElement();
                    if (!WarriorHelper.isAlly(warrior, warriorElement)) {
                        enemies.add(warriorElement);
                    }
                }
            }
        }
        return hidden;
    }

    public WarriorCollection getEnemies() {
        return enemies;
    }

}
