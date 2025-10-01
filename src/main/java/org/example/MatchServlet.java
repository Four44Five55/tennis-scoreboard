package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.MatchService;
import org.example.service.PlayerService;
import org.example.service.PlayerServiceImpl;

import java.io.IOException;

@WebServlet("/api/matches")
public class MatchServlet extends HttpServlet {
    private final PlayerService playerService = new PlayerServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(response.getWriter(), matchService.getAllMatches());

    }
}
