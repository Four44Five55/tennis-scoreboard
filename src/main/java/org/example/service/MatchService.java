package org.example.service;

import org.example.dto.MatchResponseDTO;
import org.example.dto.PaginatedResponseDTO;
import org.example.model.Match;

import java.util.List;
import java.util.Optional;

public interface MatchService {

    Match startNewMatch(String player1Name, String player2Name);

    Match finishMatch(int matchId, String winnerName);

    List<Match> findMatchesByPlayerName(String playerName);

    List<Match> getAllMatches();

    Optional<Match> findById(int id);

    PaginatedResponseDTO<MatchResponseDTO> getPaginatedMatches(int page, int pageSize);
    PaginatedResponseDTO<MatchResponseDTO> getPaginatedMatchesByPlayerName(String playerName, int page, int pageSize);

    long countMatches();
}
