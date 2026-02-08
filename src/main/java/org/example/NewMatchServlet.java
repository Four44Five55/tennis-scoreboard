package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.ValidationException;
import org.example.service.OngoingMatchesService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/api/new-match")
public class NewMatchServlet extends HttpServlet {
    private final OngoingMatchesService service = OngoingMatchesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/new-match.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String player1Name = request.getParameter("player1_name");
        String player2Name = request.getParameter("player2_name");

        try {
            UUID matchUuid = service.startMatchInPlay(player1Name, player2Name);
            response.sendRedirect(request.getContextPath() + "/api/match-score?uuid=" + matchUuid);

        } catch (ValidationException e) {
            String errorMessage = e.getFieldErrors().values().stream().findFirst().orElse("Ошибка валидации");
            request.setAttribute("error", errorMessage);
            request.setAttribute("player1_name", player1Name);
            request.setAttribute("player2_name", player2Name);
            request.getRequestDispatcher("/new-match.jsp").forward(request, response);
        }
    }
}

