package db;

import jakarta.persistence.EntityManager;

public class Dao {
    public void persist(Result result) {
        EntityManager em = DBConfig.getInstance();
        em.getTransaction().begin();
        em.persist(result);
        em.getTransaction().commit();
    }
}
