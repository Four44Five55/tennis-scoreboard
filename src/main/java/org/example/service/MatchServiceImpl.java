package org.example.service;

import org.example.dao.MatchDAO;
import org.example.dao.PlayerDAO;
import org.example.dto.MatchResponseDTO;
import org.example.dto.PaginatedResponseDTO;
import org.example.dto.PlayerResponseDTO;
import org.example.exception.NotFoundException;
import org.example.model.Match;
import org.example.model.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public PaginatedResponseDTO<MatchResponseDTO> getPaginatedMatches(int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        if (pageSize > 100) pageSize = 100;

        List<Match> matchesFromDB = matchDAO.findPaginated(page, pageSize);
        long totalMatches = matchDAO.countAll();

        List<MatchResponseDTO> matchDTOs = matchesFromDB.stream()
                .map(this::mapToMatchDTO)
                .collect(Collectors.toList());

        int totalPages = 0;
        if (totalMatches > 0) {
            totalPages = (int) Math.ceil((double) totalMatches / pageSize);
        }

        PaginatedResponseDTO<MatchResponseDTO> response = new PaginatedResponseDTO<>();
        response.setContent(matchDTOs);
        response.setCurrentPage(page);
        response.setPageSize(pageSize);
        response.setTotalItems(totalMatches);
        response.setTotalPages(totalPages);

        return response;
    }

    @Override
    public PaginatedResponseDTO<MatchResponseDTO> getPaginatedMatchesByPlayerName(String playerName, int page, int pageSize) {
        // Валидация
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        if (pageSize > 100) pageSize = 100;

        List<Match> matchesFromDB = matchDAO.findPaginatedByPlayerName(playerName, page, pageSize);
        long totalMatches = matchDAO.countByPlayerName(playerName);

        List<MatchResponseDTO> matchDTOs = matchesFromDB.stream()
                .map(this::mapToMatchDTO)
                .collect(Collectors.toList());

        int totalPages = 0;
        if (totalMatches > 0) {
            totalPages = (int) Math.ceil((double) totalMatches / pageSize);
        }

        PaginatedResponseDTO<MatchResponseDTO> response = new PaginatedResponseDTO<>();
        response.setContent(matchDTOs);
        response.setCurrentPage(page);
        response.setPageSize(pageSize);
        response.setTotalItems(totalMatches);
        response.setTotalPages(totalPages);

        return response;
    }

    @Override
    public long countMatches() {
        return matchDAO.countAll();
    }

    private MatchResponseDTO mapToMatchDTO(Match match) {
        if (match == null) return null;
        MatchResponseDTO dto = new MatchResponseDTO();
        dto.setId(match.getId());
        dto.setPlayer1(mapToPlayerDTO(match.getPlayer1()));
        dto.setPlayer2(mapToPlayerDTO(match.getPlayer2()));
        if (match.getWinner() != null) {
            dto.setWinner(mapToPlayerDTO(match.getWinner()));
        }
        return dto;
    }

    private PlayerResponseDTO mapToPlayerDTO(Player player) {
        if (player == null) return null;
        PlayerResponseDTO dto = new PlayerResponseDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        return dto;
    }
}
