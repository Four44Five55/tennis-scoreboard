package org.example.service;

import org.example.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {
    void createOrUpdatePlayer(Player player);
    Optional<Player> getPlayerByName(String name);
    List<Player> getAllPlayers();
}
