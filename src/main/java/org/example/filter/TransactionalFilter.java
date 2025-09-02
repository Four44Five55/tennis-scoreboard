package org.example.filter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/api/*")
public class TransactionalFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        EntityManager manager = null;
        EntityTransaction transaction = null;
        try {
            manager = HibernateUtil.getEntityManager();
            transaction = manager.getTransaction();

            transaction.begin();

            chain.doFilter(request, response);

            if (transaction.isActive()) {
                transaction.commit();
            }

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            String uri = ((HttpServletRequest) request).getRequestURI();
            logger.error("Транзакция не удалась для URL: {}", uri, e);
            throw new ServletException("Ошибка транзакции.", e);
        } finally {
            HibernateUtil.closeEntityManager();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
