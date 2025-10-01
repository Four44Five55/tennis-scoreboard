package org.example.service;

import org.example.dao.MatchDAO;
import org.example.dao.PlayerDAO;
import org.example.exception.NotFoundException;
import org.example.model.Match;
import org.example.model.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MatchServiceImpl implements MatchService {
    private final MatchDAO matchDAO;
    private final PlayerDAO playerDAO;

    public MatchServiceImpl(MatchDAO matchDAO, PlayerDAO playerDAO) {
        this.matchDAO = matchDAO;
        this.playerDAO = playerDAO;
    }

    @Override
    public Match startNewMatch(String player1Name, String player2Name) {
        if (Objects.equals(player1Name, player2Name)) {
            throw new IllegalArgumentException("Игрок не может играть сам с собой.");
        }

        Player player1 = playerDAO.findByName(player1Name)
                .orElseThrow(() -> new NotFoundException("Игрок не найден: " + player1Name));
        Player player2 = playerDAO.findByName(player2Name)
                .orElseThrow(() -> new NotFoundException("Игрок не найден: " + player2Name));

        Match match = new Match();
        match.setPlayer1(player1);
        match.setPlayer2(player2);

        matchDAO.save(match);
        return match;
    }

    @Override
    public Match finishMatch(int matchId, String winnerName) {
        Match match = matchDAO.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Матч не найден: " + matchId));

        Player winner = playerDAO.findByName(winnerName)
                .orElseThrow(() -> new NotFoundException("Игрок-победитель не найден: " + winnerName));

        boolean isWinnerParticipant = Objects.equals(winner.getId(), match.getPlayer1().getId()) ||
                Objects.equals(winner.getId(), match.getPlayer2().getId());
        if (!isWinnerParticipant) {
            throw new IllegalArgumentException("Победитель должен быть одним из участников матча.");
        }

        match.setWinner(winner);
        matchDAO.save(match);
        return match;
    }

    @Override
    public List<Match> findMatchesByPlayerName(String playerName) {
        return matchDAO.findByPlayerName(playerName);
    }

    @Override
    public List<Match> getAllMatches() {
        return matchDAO.findAll();
    }

    @Override
    public Optional<Match> findById(int id) {
        return matchDAO.findById(id);
    }
}
