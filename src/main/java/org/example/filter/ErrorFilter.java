package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.ApiException;
import org.example.util.JsonUtil;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/*")
public class ErrorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);

        } catch (ApiException e) {
            if (resp.isCommitted()) throw new ServletException(e);
            resp.resetBuffer();
            String msg = (e.getMessage() == null || e.getMessage().isBlank()) ? e.getCode() : e.getMessage();
            JsonUtil.sendErrorResponse(resp, e.getStatus(), msg);

        } catch (Throwable t) {
            if (resp.isCommitted()) {
                if (t instanceof ServletException se) throw se;
                if (t instanceof IOException ioe) throw ioe;
                throw new ServletException(t);
            }
            resp.resetBuffer();
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Произошла внутренняя ошибка приложения.");
        }
    }
}