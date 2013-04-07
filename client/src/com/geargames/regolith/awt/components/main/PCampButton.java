package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.BattleTypeCollection;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.BattleMap;

import java.util.Vector;

/**
 * User: m.v.kutuzov
 * Date: 07.04.13
 */
public class PCampButton extends PTouchButton {

    public PCampButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void onClick() {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        Account account = clientConfiguration.getAccount();
        BattleTypeCollection types = ClientConfigurationFactory.getConfiguration().getBaseConfiguration().getBattleTypes();
        BattleType type = null;
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).getName().equals("1x1")) {
                type = types.get(i);
            }
        }
        if (type != null) {
            Battle battle = new Battle();

            Account enemyAccount = new Account();
            enemyAccount.setId(-1);

            battle.setAlliances(new BattleAlliance[2]);
            for(int i = 0; i < battle.getAlliances().length; i++){
                BattleAlliance alliance = new BattleAlliance();
                ClientBattleGroupCollection battleGroups = new ClientBattleGroupCollection();
                alliance.setAllies(battleGroups);
                BattleGroup group = new BattleGroup();
                if(i == 0){
                    group.setAccount(account);
                }else{
                    group.setAccount(enemyAccount);
                }
                battleGroups.add(group);
                group.setWarriors(new ClientWarriorCollection(new Vector()));
                alliance.setBattle(battle);
                alliance.setNumber((byte) i);
                battle.getAlliances()[i] = alliance;
            }
            battle.setBattleType(type);
            BattleMap battleMap = ClientBattleHelper.createBattleMap(20);
            battle.setMap(battleMap);
            BattleType[] tmp = new BattleType[1];
            tmp[0] = type;
            battleMap.setPossibleBattleTypes(tmp);

            Border border = new ClientBorder();
            border.setAbleToLookThrough(false);
            border.setFrameId(1216);

            battleMap.getCells()[1][2].setElement(border);
            battleMap.getCells()[2][2].setElement(border);
            battleMap.getCells()[3][2].setElement(border);
            battleMap.getCells()[3][3].setElement(border);
            battleMap.getCells()[3][4].setElement(border);
            battleMap.getCells()[3][5].setElement(border);

            Warrior mine = account.getWarriors().get(0);

            Warrior another = account.getWarriors().get(1);

            WarriorHelper.addWarriorToGroup(battle.getAlliances()[0].getAllies().get(0), mine);
            WarriorHelper.addWarriorToGroup(battle.getAlliances()[1].getAllies().get(0), another);

            PRegolithPanelManager manager = PRegolithPanelManager.getInstance();
            manager.hideAll();
            manager.getBattleScreen().setBattle(battle);
            manager.changeScreen(manager.getBattleScreen());
        }
    }
}
