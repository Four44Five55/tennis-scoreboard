package org.example.filter;

import jakarta.persistence.EntityManager;
import org.example.util.HibernateUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/*")
public class TransactionalFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        EntityManager manager = null;
        try {
            manager = HibernateUtil.getEntityManager();
            manager.getTransaction().begin();

            chain.doFilter(request, response);

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().commit();
            }
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            throw new ServletException(e);
        } finally {
            HibernateUtil.closeEntityManager();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
