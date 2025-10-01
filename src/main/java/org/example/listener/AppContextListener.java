package org.example.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.dao.MatchDAO;
import org.example.dao.MatchDAOImpl;
import org.example.dao.PlayerDAO;
import org.example.dao.PlayerDAOImpl;
import org.example.exception.InitializationException;
import org.example.service.MatchService;
import org.example.service.MatchServiceImpl;
import org.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            URL u = Thread.currentThread().getContextClassLoader().getResource("META-INF/persistence.xml");
            System.out.println("persistence.xml URL = " + u);
            HibernateUtil.initialize();
            PlayerDAO playerDAO = new PlayerDAOImpl();
            MatchDAO matchDAO = new MatchDAOImpl();
            MatchService matchService = new MatchServiceImpl(matchDAO, playerDAO);

            ServletContext servletContext = servletContextEvent.getServletContext();
            servletContext.setAttribute("matchService", matchService);

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
