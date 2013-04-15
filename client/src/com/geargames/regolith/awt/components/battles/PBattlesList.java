package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

import java.util.Vector;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Список текущих боёв.
 */
public class PBattlesList extends PVerticalScrollView {
    private PBattleListItemVector items;

    public PBattlesList(PObject listPrototype) {
        super(listPrototype);
        IndexObject index = (IndexObject) listPrototype.getIndexBySlot(0);
        items = new PBattleListItemVector((PObject) index.getPrototype(), getShownItemsAmount());
    }

    @Override
    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    @Override
    public Vector getItems() {
        return items;
    }

    public void updateList() {
        items.update();
    }

    public void updateButtonAccount(BattleGroup battleGroup, boolean isReady) {
        Battle listenedBattle = ClientConfigurationFactory.getConfiguration().getBattle();
        if (listenedBattle != null && battleGroup.getAlliance().getBattle().getId() == listenedBattle.getId()) {
            ((PBattleListItem) items.elementAt(0)).resetButtonAccount(battleGroup, isReady);
        }
    }

}
