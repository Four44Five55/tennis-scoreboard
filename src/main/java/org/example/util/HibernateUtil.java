package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private HibernateUtil() {
    }

    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    /**
     * Создает фабрику
     */
    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory("tennis");
        } catch (Exception e) {
            System.err.println("Initial EntityManagerFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Создает контекст для работы с БД
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Метод закрытия ресурсов работы с БД, по завершению работы веб-приложения.
     */
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }


}
