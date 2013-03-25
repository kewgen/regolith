package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PVerticalScrollView;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.Network;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientBrowseBattlesAnswer;
import com.geargames.regolith.serializers.answers.ClientEvictAccountFromAllianceAnswer;
import com.geargames.regolith.serializers.answers.ClientJoinToBattleAllianceAnswer;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Список текущих боёв.
 */
public class PBattlesList extends PVerticalScrollView implements TimerListener {
    private ClientBrowseBattlesAnswer browseBattlesAnswer;

    private PBattleListItemVector items;
    private Network network;

    public PBattlesList(PObject listPrototype) {
        super(listPrototype);
        IndexObject index = (IndexObject) listPrototype.getIndexBySlot(0);
        items = new PBattleListItemVector((PObject) index.getPrototype(), getShownItemsAmount());
        network = ClientConfigurationFactory.getConfiguration().getNetwork();
        TimerManager.setPeriodicTimer(2000, this);
        browseBattlesAnswer = new ClientBrowseBattlesAnswer();
    }

    public void onTimer(int timerId) {
        try {
            //network.getAsynchronousAnswer(browseBattlesAnswer, Packets.BROWSE_CREATED_BATTLES);
        } catch (Exception e) {
            Debug.error("Could not serialize list of battles",e);
        }
    }

    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    public Vector getItems() {
        return items;
    }

}
