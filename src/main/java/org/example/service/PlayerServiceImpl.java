package org.example.service;

import org.example.dao.PlayerDAO;
import org.example.dao.PlayerDAOImpl;
import org.example.model.Player;
import org.example.validation.PlayerValidator;

import java.util.List;
import java.util.Optional;

public class PlayerServiceImpl implements PlayerService {
    private final PlayerDAO playerDAO;

    public PlayerServiceImpl() {
        this.playerDAO = new PlayerDAOImpl();
    }

    @Override
    public void createOrUpdatePlayer(Player player) {
        PlayerValidator.validateCreate(player);
        playerDAO.save(player);
    }

    @Override
    public Optional<Player> getPlayerByName(String name) {
        String valid = PlayerValidator.validateNameQuery(name);
        return playerDAO.findByName(valid);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerDAO.findAll();
    }
}
