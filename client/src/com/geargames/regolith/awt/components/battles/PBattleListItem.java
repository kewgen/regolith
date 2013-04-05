package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mikhail v. kutuzov, abarakov
 * Панель для отображения текущих битв.
 */
public class PBattleListItem extends PContentPanel {
    private PBattlePanels playersPanel;
    private PLabel battleTypeLabel;
    private PLabel compositionLabel;
    private PLabel levelLabel;

    public PBattleListItem(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                playersPanel = new PBattlePanels((PObject) index.getPrototype(), this);
                break;
            case 2:
                compositionLabel = new PSimpleLabel(index);
//                compositionLabel.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(compositionLabel, index);
                break;
            case 3:
                levelLabel = new PSimpleLabel(index);
//                levelLabel.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(levelLabel, index);
                break;
            case 109:
                battleTypeLabel = new PSimpleLabel(index);
//                battleTypeLabel.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(battleTypeLabel, index);
                break;
        }
    }

    public Battle getBattle() {
        return playersPanel.getBattle();
    }

//    public void setBattle(Battle battle) {
//        if (getBattle() != battle) { //todo: id или ссылки на объекты?
//            playersPanel.setBattle(battle);
//        }
//    }

    public void updateBattle(Battle battle) {
        playersPanel.updateBattle(battle);

        battleTypeLabel.setText(getBattle().getName());
        compositionLabel.setText(getBattle().getBattleType().getName());
//        levelLabel.setText(getBattle().);
    }

//   /**
//     * Обновить морды игроков на панели битвы, по содержимому getBattle().
//     */
//    public void update() {
//        int allianceAmount = getBattle().getBattleType().getAllianceAmount();
//        int allianceSize = getBattle().getBattleType().getAllianceSize();
//        for (int i = 0; i < allianceAmount; i++) {
//            for (int j = 0; j < allianceSize; j++) {
//                playersPanel.resetButtonAccount(i, j);
//            }
//        }
//

//    }

//    /**
//     * Обновить конкретную морду на панели битв.
//     *
//     * @param allianceNumber номер боевой стороны морды
//     * @param groupNumber  нормер группы морды
//     */
//    public void resetButtonAccount(int allianceNumber, int groupNumber) {
//        playersPanel.resetButtonAccount(allianceNumber, groupNumber);
//    }

    public void resetButtonAccount(BattleGroup battleGroup) {
        playersPanel.resetButtonAccount(
                battleGroup.getAlliance().getNumber(),
                battleGroup.getAlliance().getAllies().indexById(battleGroup.getId()));
    }

}
