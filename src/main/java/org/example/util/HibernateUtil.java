package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private HibernateUtil() {
    }

    private static final EntityManagerFactory managerFactory = buildEntityManagerFactory();

    private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

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
        EntityManager manager = threadLocal.get();
        if (manager == null || !manager.isOpen()) {
            manager = managerFactory.createEntityManager();
            threadLocal.set(manager);
        }
        return manager;
    }

    public static void closeEntityManager() {
        EntityManager manager = threadLocal.get();
        if (manager != null && manager.isOpen()) {
            manager.close();
        }
        threadLocal.remove();
    }

    /**
     * Метод закрытия ресурсов работы с БД, по завершению работы веб-приложения.
     */
    public static void shutdown() {
        if (managerFactory != null && managerFactory.isOpen()) {
            managerFactory.close();
        }
    }


}
