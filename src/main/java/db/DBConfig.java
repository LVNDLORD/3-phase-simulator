package db;

import jakarta.persistence.*;

/**
 * Configuration class for managing the EntityManager and connecting to the MariaDB database.
 */
public class DBConfig {
    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    /**
     * Gets an instance of the EntityManager, creating it if necessary.
     *
     * @return The EntityManager instance.
     */
    public synchronized static EntityManager getInstance() {
        if (em==null) {
            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("SimulationMariaDbUnit");
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}