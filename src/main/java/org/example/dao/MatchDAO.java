package org.example.dao;

import org.example.model.Match;

import java.util.List;
import java.util.Optional;

public interface MatchDAO {
    void save(Match match);

    Optional<Match> findById(int id);

    List<Match> findAll();

    List<Match> findByPlayerName(String name);
}
