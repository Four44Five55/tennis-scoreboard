package org.example.service;

import org.example.dao.PlayerDAO;
import org.example.dao.impl.PlayerDaoImpl;
import org.example.model.Player;

public class PlayerService {
    private final PlayerDAO player = new PlayerDaoImpl();

    public Player addPlayer(String name) {

    }
}
