package org.example.service;

import org.example.dao.PlayerDAO;
import org.example.dao.PlayerDaoImpl;
import org.example.model.Player;

import java.util.List;
import java.util.Optional;

public class PlayerServiceImpl implements PlayerService {
    private final PlayerDAO playerDAO;

    public PlayerServiceImpl() {
        this.playerDAO = new PlayerDaoImpl();
    }

    @Override
    public void createOrUpdatePlayer(Player player) {
        if (player.getName() == null || player.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя игрока не может быть пустым.");
        }
        player.setName(player.getName().trim());
        playerDAO.save(player);
    }

    @Override
    public Optional<Player> getPlayerByName(String name) {
        return playerDAO.findByName(name);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerDAO.findAll();
    }
}
