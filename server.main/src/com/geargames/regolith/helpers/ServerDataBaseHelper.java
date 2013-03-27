package com.geargames.regolith.helpers;

import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleMap;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 22.06.12
 */
public class ServerDataBaseHelper {
    private static SessionFactory sessionFactory;

    static {
        sessionFactory = MainServerConfigurationFactory.getConfiguration().getSessionFactory();
    }

    /**
     * Загрузить объект карта битвы по её имени name.
     *
     * @param name
     * @return игровая карта
     */
    public static BattleMap getBattleMapByName(String name) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(BattleMap.class);
        criteria.add(Restrictions.eq("name", name));
        BattleMap map = (BattleMap) criteria.uniqueResult();
        session.close();
        return map;
    }

    /**
     * Загрузить объект карта битвы по её id.
     *
     * @param id
     * @return игровая карта
     */
    public static BattleMap getBattleMapById(int id) {
        Session session = sessionFactory.openSession();
        BattleMap map = (BattleMap) session.get(BattleMap.class, id);
        session.close();
        return map;
    }

    public static Warrior getWarriorById(int id) {
        Session session = sessionFactory.openSession();
        Warrior warrior = (Warrior) session.get(Warrior.class, id);
        session.close();
        return warrior;
    }

    public static Warrior[] getWarriorsByIds(int[] ids) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Warrior.class);
        Disjunction disjunction = Restrictions.disjunction();
        for (int id : ids) {
            disjunction.add(Restrictions.eq("id", id));
        }
        criteria.add(disjunction);
        List<Warrior> warriors = criteria.list();
        session.close();
        return warriors.toArray(new Warrior[warriors.size()]);
    }

    public static BattleType getBattleTypeById(int id){
        Session session = sessionFactory.openSession();
        BattleType type = (BattleType)session.get(BattleType.class, id);
        session.close();
        return type;
    }
}
