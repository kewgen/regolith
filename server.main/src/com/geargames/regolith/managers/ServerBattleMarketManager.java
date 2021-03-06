package com.geargames.regolith.managers;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.ServerBattleHelper;
import com.geargames.regolith.helpers.BattleTypeHelper;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.*;
import com.geargames.regolith.units.Account;
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
    private Random random;

    public ServerBattleMarketManager() {
        configuration = MainServerConfigurationFactory.getConfiguration();
        random = new Random();
    }

    public Battle createBattle(BattleMap battleMap, int battleTypeId, Account author) throws RegolithException {
        BattleType battleType = BattleTypeHelper.findBattleTypeById(battleTypeId, battleMap.getPossibleBattleTypes());
        Battle battle = ServerBattleHelper.createBattle(battleMap.getName() + "_" + author.getName(), battleMap, battleType);
        battle.setAuthor(author);
        Session session = configuration.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(battle);
        tx.commit();
        Integer id = (Integer) session.getIdentifier(battle);
        battle = (Battle) session.load(Battle.class, id); //todo: Это точно нужно?
        session.close();
        BattleManagerContext battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        Set<Account> listeners = new HashSet<Account>();
        listeners.add(author);
        battleManagerContext.getBattleListeners().put(battle, listeners);
        battleManagerContext.getCreatedBattles().put(author, battle);
        battleManagerContext.getBattlesByAccount().put(author, battle);
        battleManagerContext.getBattlesById().put(id, battle);
        battleManagerContext.getCompleteGroups().put(battle, new HashSet<BattleGroup>(battleType.getAllianceSize() * battleType.getAllianceAmount()));
        return battle;
    }

    public boolean listenToBattle(Battle battle, Account participant) {
        BattleManagerContext battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        battleManagerContext.getBattleListeners().get(battle).add(participant);
        battleManagerContext.getBattlesByAccount().put(participant, battle);
        return true;
    }

    public Collection<Battle> battlesJoinTo() {
        return configuration.getServerContext().getBattleManagerContext().getCreatedBattles().values();
    }

    public List<BattleMap> browseBattleMaps() {
        Session session = configuration.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(BattleMap.class);
        List<BattleMap> list = criteria.list();
        return list;
    }

    //todo: сделано крайне неоптимально
    public List<BattleMap> browseBattleMaps(BattleType battleType) {
        LinkedList<BattleMap> maps = new LinkedList<BattleMap>();
        for (BattleMap map : browseBattleMaps()) {
            for (BattleType type : map.getPossibleBattleTypes()) {
                if (type.getId() == battleType.getId()) { //todo: сравнивать ссылки, а не id
                    maps.add(map);
                    break;
                }
            }
        }
        return maps;
    }

    public BattleMap getRandomBattleMap(BattleType type) {
        List<BattleMap> maps = browseBattleMaps(type);
        if (maps.size() > 0) {
            return maps.get(random.nextInt(maps.size()));
        } else {
            return null;
        }
    }

}
