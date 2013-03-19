package com.geargames.regolith.managers;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.BattleMap;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

/**
 * User: mkutuzov
 * Date: 11.05.12
 */
public class ServerBattleMarketManager {
    private MainServerConfiguration configuration;

    public ServerBattleMarketManager() {
        configuration = MainServerConfigurationFactory.getConfiguration();
    }

    public Battle createBattle(BattleMap battleMap, int battleTypeIndex, Account author) throws RegolithException{
        BattleType type = battleMap.getPossibleBattleTypes()[battleTypeIndex];
        Battle battle = BattleHelper.createBattle(battleMap.getName() + author.getName(), battleMap, battleTypeIndex);
        battle.setAuthor(author);
        Session session = configuration.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(battle);
        tx.commit();
        Integer id = (Integer) session.getIdentifier(battle);
        battle = (Battle) session.load(Battle.class, id);
        session.close();
        BattleManagerContext battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        Set<Account> listeners = new HashSet<Account>();
        listeners.add(author);
        battleManagerContext.getBattleListeners().put(battle, listeners);
        battleManagerContext.getCreatedBattles().put(author, battle);
        battleManagerContext.getBattlesById().put(id, battle);
        battleManagerContext.getCompleteGroups().put(battle, new HashSet<BattleGroup>(type.getAllianceSize() * type.getAllianceAmount()));
        return battle;
    }

    public boolean listenToBattle(Battle battle, Account participant) {
        configuration.getServerContext().getBattleManagerContext().getBattleListeners().get(battle).add(participant);
        return true;
    }

    public Battle[] battlesJoinTo() {
        Collection<Battle> collection = configuration.getServerContext().getBattleManagerContext().getCreatedBattles().values();
        return collection.toArray(new Battle[collection.size()]);
    }

    public BattleMap[] browseBattleMaps() {
        Session session = configuration.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(BattleMap.class);
        List<BattleMap> list = criteria.list();
        return list.toArray(new BattleMap[list.size()]);
    }

    public boolean goToBase() {
        return true;
    }
}
