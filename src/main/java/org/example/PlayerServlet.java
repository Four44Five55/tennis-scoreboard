package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.Player;
import org.example.service.PlayerService;
import org.example.service.PlayerServiceImpl;

import java.io.IOException;

@WebServlet("/api/players")
public class PlayerServlet extends HttpServlet {
    private final PlayerService playerService = new PlayerServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(response.getWriter(), playerService.getAllPlayers());

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        Player newPlayer = new Player();
        newPlayer.setName(name);
        playerService.createOrUpdatePlayer(newPlayer);

    }
}
