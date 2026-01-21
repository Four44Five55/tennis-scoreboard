package org.example.service;

import org.example.dao.PlayerDAO;
import org.example.dao.PlayerDAOImpl;
import org.example.entity.Player;
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
        Player savedPlayer = playerDAO.save(player);
        player.setId(savedPlayer.getId());
    }

    @Override
    public Optional<Player> getPlayerByName(String name) {
        String valid = PlayerValidator.validateNameQuery(name);
        return playerDAO.findByName(valid);
    }

    @Override
    public Optional<Player> getPlayerById(int id) {
        return playerDAO.findById(id);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerDAO.findAll();
    }
}
