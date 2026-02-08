package org.example.service;

import org.example.dao.MatchDAO;
import org.example.dao.PlayerDAO;
import org.example.dto.MatchResponseDTO;
import org.example.dto.PaginatedResponseDTO;
import org.example.dto.PlayerResponseDTO;
import org.example.entity.Match;
import org.example.entity.Player;
import org.example.exception.NotFoundException;
import org.example.exception.ValidationException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MatchServiceImpl implements MatchService {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    private final MatchDAO matchDAO;
    private final PlayerDAO playerDAO;

    public MatchServiceImpl(MatchDAO matchDAO, PlayerDAO playerDAO) {
        this.matchDAO = matchDAO;
        this.playerDAO = playerDAO;
    }

    /**
     * Нормализует параметры пагинации.
     */
    private int normalizePageSize(int pageSize) {
        if (pageSize < 1) return DEFAULT_PAGE_SIZE;
        if (pageSize > MAX_PAGE_SIZE) return MAX_PAGE_SIZE;
        return pageSize;
    }

    /**
     * Нормализует номер страницы.
     */
    private int normalizePage(int page) {
        return page < 1 ? 1 : page;
    }

    @Override
    public Match saveMatch(Match match) {
        return matchDAO.save(match);
    }

    @Override
    public Match startNewMatch(String player1Name, String player2Name) {
        if (Objects.equals(player1Name, player2Name)) {
            throw new ValidationException("Игрок не может играть сам с собой.");
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
            throw new ValidationException("Победитель должен быть одним из участников матча.");
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
        int normalizedPage = normalizePage(page);
        int normalizedPageSize = normalizePageSize(pageSize);

        List<Match> matchesFromDB = matchDAO.findPaginated(normalizedPage, normalizedPageSize);
        long totalMatches = matchDAO.countAll();

        return buildPaginatedResponse(
                matchesFromDB,
                totalMatches,
                normalizedPage,
                normalizedPageSize
        );
    }

    @Override
    public PaginatedResponseDTO<MatchResponseDTO> getPaginatedMatchesByPlayerName(String playerName, int page, int pageSize) {
        int normalizedPage = normalizePage(page);
        int normalizedPageSize = normalizePageSize(pageSize);

        List<Match> matchesFromDB = matchDAO.findPaginatedByPlayerName(playerName, normalizedPage, normalizedPageSize);
        long totalMatches = matchDAO.countByPlayerName(playerName);

        return buildPaginatedResponse(
                matchesFromDB,
                totalMatches,
                normalizedPage,
                normalizedPageSize
        );
    }

    /**
     * Собирает ответ пагинации из данных БД.
     */
    private PaginatedResponseDTO<MatchResponseDTO> buildPaginatedResponse(
            List<Match> matches,
            long totalItems,
            int currentPage,
            int pageSize
    ) {
        List<MatchResponseDTO> matchDTOs = matches.stream()
                .map(this::mapToMatchDTO)
                .collect(Collectors.toList());

        int totalPages = (totalItems > 0)
                ? (int) Math.ceil((double) totalItems / pageSize)
                : 0;

        PaginatedResponseDTO<MatchResponseDTO> response = new PaginatedResponseDTO<>();
        response.setContent(matchDTOs);
        response.setCurrentPage(currentPage);
        response.setPageSize(pageSize);
        response.setTotalItems(totalItems);
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
