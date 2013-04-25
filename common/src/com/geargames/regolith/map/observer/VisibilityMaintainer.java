package com.geargames.regolith.map.observer;

import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.dictionaries.AllyCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.battle.Ally;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public class VisibilityMaintainer extends BattleCellMaintainer {
    private AllyCollection allies;

    public VisibilityMaintainer(AllyCollection allies) {
        this.allies = allies;
    }

    /**
     * Если ячейку видно , то проверяем: нет ли там преграды для взгляда?
     * если есть возвращаем true иначе - false.
     * Затем помечаем ячейку видимой.
     * Если ячейку не видно - помечаем не видимой и возвращаем true
     *
     * @param warrior
     * @param hidden
     * @return
     */
    public boolean maintain(BattleCell[][] cells, Ally warrior, boolean hidden, int x, int y) {
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
                if (!was && cell.getElement() instanceof Ally) {
                    allies.add((Ally) cell.getElement());
                }
            }
        }
        return hidden;
    }

    public AllyCollection getAllies() {
        return allies;
    }
}
