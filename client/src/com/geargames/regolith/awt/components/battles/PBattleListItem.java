package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PContentPanel;
import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Панель для отображения битвы, ее боевых групп и игроков, в них вступивших.
 */
public class PBattleListItem extends PContentPanel {
    private PBattlePanels panelPlayers;
    private PLabel labelBattleType;
    private PLabel labelComposition;
    private PLabel labelLevel;
    private PStartBattleButton buttonStartBattle;

    public PBattleListItem(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                panelPlayers = new PBattlePanels((PObject) index.getPrototype(), this);
                break;
            case 2:
                labelComposition = new PSimpleLabel(index);
//                labelComposition.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(labelComposition, index);
                break;
            case 3:
                labelLevel = new PSimpleLabel(index);
//                labelLevel.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(labelLevel, index);
                break;
            case 4:
                buttonStartBattle = new PStartBattleButton((PObject)index.getPrototype());
                addActiveChild(buttonStartBattle, index);
                break;
            case 109:
                labelBattleType = new PSimpleLabel(index);
//                labelBattleType.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(labelBattleType, index);
                break;
        }
    }

    public Battle getBattle() {
        return panelPlayers.getBattle();
    }

    public void updateBattle(Battle battle) {
        panelPlayers.updateBattle(battle);

        labelBattleType.setText(getBattle().getName());
        labelComposition.setText(getBattle().getBattleType().getName());
//        labelLevel.setText(getBattle().);

        buttonStartBattle.setVisible(panelPlayers.getIsReadyBattle());
    }

//    /**
//     * Обновить морды игроков на панели битвы, по содержимому getBattle().
//     */
//    public void update() {
//        int allianceAmount = getBattle().getBattleType().getAllianceAmount();
//        int allianceSize = getBattle().getBattleType().getAllianceSize();
//        for (int i = 0; i < allianceAmount; i++) {
//            for (int j = 0; j < allianceSize; j++) {
//                panelPlayers.resetButtonAccount(i, j);
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
//        panelPlayers.resetButtonAccount(allianceNumber, groupNumber);
//    }

    public void resetButtonAccount(BattleGroup battleGroup, boolean isReady) {
        panelPlayers.resetButtonAccount(
                battleGroup.getAlliance().getNumber(),
                battleGroup.getAlliance().getAllies().indexById(battleGroup.getId()),
                isReady);
    }

}
