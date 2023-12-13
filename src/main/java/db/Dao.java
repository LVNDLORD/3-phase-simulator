package db;

import jakarta.persistence.EntityManager;

/**
 * Data Access Object (DAO) class for interacting with the database using Hibernate's EntityManager.
 */
public class Dao {
    /**
     * Persists a Result object to the database.
     *
     * @param result The Result object to be persisted.
     */
    public void persist(Result result) {
        EntityManager em = DBConfig.getInstance();
        em.getTransaction().begin();
        em.persist(result);
        em.getTransaction().commit();
    }
}
