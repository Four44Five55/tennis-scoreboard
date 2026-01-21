package org.example.service;

import org.example.entity.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {
    void createOrUpdatePlayer(Player player);
    Optional<Player> getPlayerByName(String name);
    Optional<Player> getPlayerById(int id);
    List<Player> getAllPlayers();
}
