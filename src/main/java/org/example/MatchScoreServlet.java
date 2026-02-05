package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.MatchScoreDTO;
import org.example.exception.ValidationException;
import org.example.service.OngoingMatchesService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/api/match-score")
public class MatchScoreServlet extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String uuidParam = req.getParameter("uuid");
            if (uuidParam == null || uuidParam.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Требуется UUID матча.");
                return;
            }
            UUID matchId = UUID.fromString(uuidParam);
            Optional<MatchScoreDTO> matchDTO = ongoingMatchesService.getMatchScoreDTO(matchId);

            if (matchDTO.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Текущий матч с UUID " + matchId + " не найден или завершен.");
                return;
            }

            req.setAttribute("matchDTO", matchDTO.get());

            req.getRequestDispatcher("/match-score.jsp").forward(req, resp);

        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат UUID матча.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String uuidParam = req.getParameter("uuid");
            String playerParam = req.getParameter("player");

            if (uuidParam == null || uuidParam.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Требуется UUID матча.");
                return;
            }
            if (playerParam == null || playerParam.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Требуется номер игрока.");
                return;
            }

            UUID matchId = UUID.fromString(uuidParam);
            int playerNum = Integer.parseInt(playerParam);

            if (playerNum != 1 && playerNum != 2) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Номер игрока должен быть 1 или 2.");
                return;
            }
            Optional<org.example.entity.Match> finishedMatch = ongoingMatchesService.updateScore(matchId, playerNum);

            if (finishedMatch.isPresent()) {
                // Матч завершён - перенаправляем на список матчей
                resp.sendRedirect(req.getContextPath() + "/api/matches");
            } else {
                // Матч продолжается - остаёмся на странице счёта
                resp.sendRedirect(req.getContextPath() + "/api/match-score?uuid=" + uuidParam);
            }

        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат параметров: " + e.getMessage());
        }
    }
}