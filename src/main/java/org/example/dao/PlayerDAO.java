package org.example.dao;

import org.example.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDAO {
    void save(Player player);

    Optional<Player> findById(int id);

    Optional<Player> findByName(String name);

    List<Player> findAll();
}
