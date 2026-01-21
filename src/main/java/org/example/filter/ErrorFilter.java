package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.ApiException;
import org.example.exception.ValidationException;
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
            // простое сообщения об ошибке.(проверка аргументов на null, на отрицательные значения и т.д.)
        } catch (IllegalArgumentException e) {
            if (resp.isCommitted()) throw new ServletException(e);
            resp.resetBuffer();
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            //нужно валидировать внешние данные, вернуть детальный отчет по нескольким полям
        } catch (ValidationException ve) { // Твое исключение обрабатывается отдельно
            if (resp.isCommitted()) throw new ServletException(ve);
            resp.resetBuffer();
            JsonUtil.sendValidationErrorResponse(resp, ve.getStatus(), ve.getFieldErrors());
        } catch (ApiException e) {
            if (resp.isCommitted()) throw new ServletException(e);
            resp.resetBuffer();
            if (e instanceof org.example.exception.ValidationException ve) {
                JsonUtil.sendValidationErrorResponse(resp, e.getStatus(), ve.getFieldErrors());
            } else {
                String msg = (e.getMessage() == null || e.getMessage().isBlank()) ? e.getCode() : e.getMessage();
                JsonUtil.sendErrorResponse(resp, e.getStatus(), msg);
            }
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