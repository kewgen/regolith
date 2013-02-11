package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mikhail v. kutuzov
 * Панель для отображения текущих битв.
 */
public class PBattleListItem extends PContentPanel {
    private PBattleButtons battleButtons;
    private PLabel battleTypeLabel;
    private PLabel composition;
    private PLabel level;

    public PBattleListItem(PObject prototype) {
        super(prototype);

        ArrayList indexes = prototype.getIndexes();
        for (int i = 0; i < indexes.size(); i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            if (index.isSlot()) {
                switch (index.getSlot()) {
                    case 0:
                        battleButtons = new PBattleButtons((PObject) index.getPrototype(), this);
                        break;
                    case 1:
                        battleTypeLabel = new PSimpleLabel(index);
                        addPassiveChild(battleTypeLabel, index);
                        break;
                    case 2:
                        composition = new PSimpleLabel(index);
                        addPassiveChild(composition, index);
                        break;
                    case 3:
                        level = new PSimpleLabel(index);
                        addPassiveChild(level, index);
                        break;
                }
            }
        }
    }

    public void setBattle(Battle battle) {
        if (battleButtons.getBattle() != battle) {
            battleButtons.setBattle(battle);
        }
    }

    public Battle getBattle() {
        return battleButtons.getBattle();
    }

    public void resetButton(int allianceNumber, int groupNumber) {
        battleButtons.resetButton(allianceNumber, groupNumber);
    }
}
