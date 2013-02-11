package com.geargames.regolith.managers;

import com.geargames.regolith.MainServerHelper;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Mikhail_Kutuzov
 *         created: 25.05.12  23:04
 */
public class ServerCommonManager {
    private MainServerConfiguration serverConfiguration;


    public void setServerConfiguration(MainServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    public boolean checkForName(String login) {
        Session session = serverConfiguration.getSessionFactory().openSession();
        Criteria account = session.createCriteria(Account.class);
        account.add(Restrictions.eq("name", login));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("id"));
        account.setProjection(projectionList);
        boolean result = account.list().size() == 0;
        session.close();
        return  result;
    }

    public boolean create(Login login) {
        Account account = MainServerHelper.createDefaultAccount(
                serverConfiguration.getRegolithConfiguration().getBaseConfiguration(),
                login.getName(), login.getPassword());
        Session session = serverConfiguration.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(account.getBase());
        session.save(account);
        tx.commit();
        session.close();
        return true;
    }


    public Account login(Login login) {
        //serverConfiguration.getServerContext().ge

        Session session = serverConfiguration.getSessionFactory().openSession();
        Criteria account = session.createCriteria(Account.class);
        account.add(Restrictions.eq("name", login.getName()));
        account.add(Restrictions.eq("password", login.getPassword()));

        List<Account> accounts = (List<Account>)account.list();
        session.close();
        if(accounts.size()!=1){
            return null;
        }
        return accounts.get(0);
    }

    public void logout(Login login) {

    }
}
