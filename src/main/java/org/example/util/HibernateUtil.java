package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.exception.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public final class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static EntityManagerFactory managerFactory;
    private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    private HibernateUtil() {
    }

    public static synchronized void initialize() {
        if (managerFactory == null || !managerFactory.isOpen()) {
            try {
                Map<String, String> properties = new HashMap<>();

                String dbUrl = System.getenv("DB_URL");
                if (dbUrl != null) {
                    properties.put("jakarta.persistence.jdbc.url", dbUrl);
                }

                String dbUser = System.getenv("DB_USER");
                if (dbUser != null) {
                    properties.put("jakarta.persistence.jdbc.user", dbUser);
                }

                String dbPassword = System.getenv("DB_PASSWORD");
                if (dbPassword != null) {
                    properties.put("jakarta.persistence.jdbc.password", dbPassword);
                }
                managerFactory = Persistence.createEntityManagerFactory("tennis", properties);
                logger.info("Инициализация EntityManagerFactory.");
            } catch (Exception e) {
                logger.error("Ошибка инициализации EntityManagerFactory.", e);
                throw new InitializationException("Не удалось инициализировать EntityManagerFactory.", e);
            }
        }
    }

    public static EntityManager getEntityManager() {
        initialize();
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
            logger.debug("EntityManager закрыт для текущего потока.");
        }
        threadLocal.remove();
    }

    public static void shutdown() {
        if (managerFactory != null && managerFactory.isOpen()) {
            managerFactory.close();
            logger.info("Завершение закрытия EntityManagerFactory.");
        }
    }


}
