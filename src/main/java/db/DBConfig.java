package db;

import jakarta.persistence.*;

public class DBConfig {
    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

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