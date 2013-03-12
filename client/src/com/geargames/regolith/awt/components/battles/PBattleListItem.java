package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.battle.Battle;

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
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
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

    public void setBattle(Battle battle) {
        if (battleButtons.getBattle() != battle) {
            battleButtons.setBattle(battle);
        }
    }

    public Battle getBattle() {
        return battleButtons.getBattle();
    }

    /**
     * Обновить морды игроков на панели битвы, по содержимому getBattle().
     */
    public void update() {
        int allianceAmount = getBattle().getBattleType().getAllianceAmount();
        int groupSize = getBattle().getBattleType().getAllianceSize();
        for (int i = 0; i < allianceAmount; i++) {
            for (int j = 0; j < groupSize; j++) {
                battleButtons.resetButtonAccount(i, j);
            }
        }
    }

    /**
     * Обновить конкретную морду на панели битв.
     *
     * @param allianceNumber номер боевой стороны морды
     * @param groupNumber  нормер группы морды
     */
    public void update(int allianceNumber, int groupNumber) {
        battleButtons.resetButtonAccount(allianceNumber, groupNumber);
    }
}
