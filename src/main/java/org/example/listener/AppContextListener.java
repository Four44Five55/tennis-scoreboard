package org.example.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.exception.InitializationException;
import org.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            HibernateUtil.initialize();
            logger.info("Приложение запущено: JPA инициализирован");
        } catch (InitializationException e) {
            logger.error("Приложение не запущено: при инициализации JPA произошла ошибка ", e);
            throw new RuntimeException("Критическая ошибка: приложение не может быть запущено без JPA", e);
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        HibernateUtil.shutdown();
        logger.info("Приложение остановлено: JPA shutdown завершено.");
    }
}
