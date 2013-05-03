package com.geargames.regolith.map.observer;

import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.dictionaries.HumanElementCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.CellElementTypes;
import com.geargames.regolith.units.map.HumanElement;

/**
 * Users: mkutuzov, abarakov
 * Date: 15.03.12
 */
public class VisibilityMaintainer extends BattleCellMaintainer {
    private HumanElementCollection allies;

    public VisibilityMaintainer(HumanElementCollection allies) {
        this.allies = allies;
    }

    /**
     * Если ячейку видно, то проверяем: нет ли там преграды для взгляда?
     * если есть возвращаем true, иначе - false.
     * Затем помечаем ячейку видимой.
     * Если ячейку не видно - помечаем не видимой и возвращаем true
     *
     * @param unit
     * @param hidden
     * @return
     */
    @Override
    public boolean maintain(BattleCell[][] cells, HumanElement unit, boolean hidden, int x, int y) {
        if (!hidden) {
            BattleCell cell = cells[x][y];
            boolean was = BattleMapHelper.isVisible(cell, unit.getHuman().getBattleGroup().getAlliance());
            SecurityOperationManager security = unit.getHuman().getBattleGroup().getAccount().getSecurity();
            if (security != null) {
                security.adjustObserve(x + y);
            }
            BattleMapHelper.show(cell, unit.getHuman());
            if (cell.getElement() != null) {
                hidden = !cell.getElement().isAbleToLookThrough();
                if (!was && cell.getElement().getElementType() == CellElementTypes.HUMAN) {
                    HumanElement humanElement = (HumanElement) cell.getElement();
                    if (humanElement.getHuman().getMembershipType() == Human.ALLY) {
                        allies.add(humanElement);
                    }
                }
            }
        }
        return hidden;
    }

    public HumanElementCollection getAllies() {
        return allies;
    }

}
