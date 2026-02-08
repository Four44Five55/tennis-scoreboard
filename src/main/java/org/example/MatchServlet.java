package org.example;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.MatchResponseDTO;
import org.example.dto.PaginatedResponseDTO;
import org.example.service.MatchService;
import org.example.util.JsonUtil;

import java.io.IOException;

@WebServlet("/api/matches")
public class MatchServlet extends HttpServlet {
    private MatchService matchService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Object service = config.getServletContext().getAttribute("matchService");
        if (service instanceof MatchService) {
            this.matchService = (MatchService) service;
        } else {
            throw new ServletException("Не удалось инициализировать MatchService в сервлете");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        String filterByPlayerName = request.getParameter("filter_by_player_name");
        int page;
        try {
            page = (pageParam == null) ? 1 : Integer.parseInt(pageParam);
            if (page < 1) page = 1;
        } catch (NumberFormatException e) {
            page = 1;
        }
        int pageSize = (pageSizeParam == null) ? 10 : Integer.parseInt(pageSizeParam);

        PaginatedResponseDTO<MatchResponseDTO> paginatedResponse;

        if (filterByPlayerName != null && !filterByPlayerName.isBlank()) {
            paginatedResponse = matchService.getPaginatedMatchesByPlayerName(filterByPlayerName, page, pageSize);
        } else {
            paginatedResponse = matchService.getPaginatedMatches(page, pageSize);
        }

        // Если запрос expects JSON (AJAX) - возвращаем JSON
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_OK, paginatedResponse);
        } else {
            // Иначе - показываем HTML страницу
            request.getRequestDispatcher("/matches.jsp").forward(request, response);
        }
    }
}