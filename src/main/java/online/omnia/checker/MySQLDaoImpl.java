package online.omnia.checker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 26.09.2017.
 */
public class MySQLDaoImpl {
    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    private static MySQLDaoImpl instance;

    static {
        configuration = new Configuration()
                .addAnnotatedClass(AccountsEntity.class)
                .addAnnotatedClass(PostBackEntity.class)
                .addAnnotatedClass(LeadsIncorrectEntity.class)
                .addAnnotatedClass(AdvertsEntity.class)
                .configure("/hibernate.cfg.xml");
        Map<String, String> properties = Utils.iniFileReader();
        configuration.setProperty("hibernate.connection.password", properties.get("password"));
        configuration.setProperty("hibernate.connection.username", properties.get("username"));
        String jdbcURL = (properties.get("url")
                .startsWith("jdbc:mysql://") ? properties.get("url") : "jdbc:mysql://" + properties.get("url"));
        String url = (!jdbcURL.endsWith("/") ? jdbcURL + "/" : jdbcURL) + properties.get("dbname");
        configuration.setProperty("hibernate.connection.url", url);

        while (true) {
            try {
                sessionFactory = configuration.buildSessionFactory();
                break;
            } catch (PersistenceException e) {

                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public LeadsIncorrectEntity getLeadFromDb(java.util.Date leadDate, int advertId, int leadSum) {
        Session session = sessionFactory.openSession();
        LeadsIncorrectEntity leadsIncorrectEntity = null;
        try {
            leadsIncorrectEntity = session.createQuery("from LeadsIncorrectEntity where lead_date=:leadDate and advert_id=:advertId and lead_sum=:leadSum", LeadsIncorrectEntity.class)
                    .setParameter("leadDate", leadDate)
                    .setParameter("advertId", advertId)
                    .setParameter("leadSum", leadSum)
                    .getSingleResult();
        } catch (NoResultException e) {
            leadsIncorrectEntity = null;
        }
        session.close();
        return leadsIncorrectEntity;
    }
    public void updateLeadsIncorrect(LeadsIncorrectEntity leadsIncorrectEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(leadsIncorrectEntity);
        session.getTransaction().commit();
        session.close();
    }
    public void updatePostback(PostBackEntity postBackEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(postBackEntity);
        session.getTransaction().commit();
        session.close();
    }
    public void addLeadsIncorrect(LeadsIncorrectEntity leadsIncorrectEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(leadsIncorrectEntity);
        session.getTransaction().commit();
        session.close();
    }
    public List<PostBackEntity> getPostbacks(String clickId, java.util.Date date) {
        Session session = sessionFactory.openSession();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted = simpleDateFormat.format(date);
        List<PostBackEntity> postBackEntities = session.createQuery("from PostBackEntity where clickid=:clickid and t6 like:t6", PostBackEntity.class)
                .setParameter("clickid", clickId)
                .setParameter("t6", formatted.substring(0, formatted.length() - 2) + "%")
                .getResultList();
        session.close();
        return postBackEntities;
    }
    public List<AdvertsEntity> getAdverts(String shortname) {
        Session session = sessionFactory.openSession();
        List<AdvertsEntity> advertsEntities;
        while (true) {
            try {
                advertsEntities = session.createQuery("from AdvertsEntity acc where advshortname=:shortname", AdvertsEntity.class)
                        .setParameter("shortname", shortname)
                        .getResultList();
                break;
            } catch (PersistenceException e) {
                e.printStackTrace();
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return advertsEntities;
    }
    public List<AccountsEntity> getAccountsEntities(String type) {
        Session session = sessionFactory.openSession();
        List<AccountsEntity> accountsEntities;
        while (true) {
            try {
                accountsEntities = session.createQuery("from AccountsEntity acc where acc.type=:accType", AccountsEntity.class)
                        .setParameter("accType", type)
                        .getResultList();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return accountsEntities;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static MySQLDaoImpl getInstance() {
        if (instance == null) instance = new MySQLDaoImpl();
        return instance;
    }
}
