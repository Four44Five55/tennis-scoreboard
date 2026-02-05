package org.example.service;

import org.example.dao.MatchDAOImpl;
import org.example.dao.PlayerDAOImpl;
import org.example.domain.MatchInPlay;
import org.example.dto.MatchScoreDTO;
import org.example.dto.PlayerScoreDTO;
import org.example.entity.Match;
import org.example.entity.Player;
import org.example.exception.ValidationException;
import org.example.validation.PlayerValidator;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private final Map<UUID, MatchInPlay> ongoingMatchesInPlay = new ConcurrentHashMap<>();

    private final PlayerService playerService = new PlayerServiceImpl();
    private final MatchService matchService = new MatchServiceImpl(new MatchDAOImpl(), new PlayerDAOImpl());

    private OngoingMatchesService() {
    }

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }

    public UUID startMatchInPlay(String player1Name, String player2Name) {
        // Валидация имён (пустота, длина, недопустимая лексика)
        String validatedPlayer1 = PlayerValidator.validateNameQuery(player1Name);
        String validatedPlayer2 = PlayerValidator.validateNameQuery(player2Name);

        if (validatedPlayer1.equalsIgnoreCase(validatedPlayer2)) {
            throw new ValidationException("Игрок не может играть сам с собой.");
        }

        Player player1 = getOrCreatePlayer(validatedPlayer1);
        Player player2 = getOrCreatePlayer(validatedPlayer2);

        UUID matchInPlayId = UUID.randomUUID();
        MatchInPlay newGame = new MatchInPlay(player1, player2);

        ongoingMatchesInPlay.put(matchInPlayId, newGame);

        return matchInPlayId;
    }

    private Player getOrCreatePlayer(String playerName) {
        return playerService.getPlayerByName(playerName).orElseGet(() -> {
            Player newPlayer = new Player(playerName);
            playerService.createOrUpdatePlayer(newPlayer);
            return newPlayer;
        });
    }

    public Optional<MatchInPlay> getMatchInPlay(UUID matchInPlayId) {
        return Optional.ofNullable(ongoingMatchesInPlay.get(matchInPlayId));
    }

    /**
     * Возвращает DTO с данными о матче для отображения в JSP.
     * Скрывает внутреннюю структуру domain-сущности MatchInPlay.
     */
    public Optional<MatchScoreDTO> getMatchScoreDTO(UUID matchInPlayId) {
        return getMatchInPlay(matchInPlayId)
                .map(this::toMatchScoreDTO);
    }

    private MatchScoreDTO toMatchScoreDTO(MatchInPlay match) {
        PlayerScoreDTO player1DTO = new PlayerScoreDTO(
                match.getPlayer1().getName(),
                match.getPlayer1().getSet(),
                match.getPlayer1().getGames(),
                match.getPlayer1DisplayPoints()
        );

        PlayerScoreDTO player2DTO = new PlayerScoreDTO(
                match.getPlayer2().getName(),
                match.getPlayer2().getSet(),
                match.getPlayer2().getGames(),
                match.getPlayer2DisplayPoints()
        );

        return new MatchScoreDTO(player1DTO, player2DTO);
    }

    public void removeMatchInPlay(UUID matchInPlayId) {
        ongoingMatchesInPlay.remove(matchInPlayId);
    }

    public Optional<Match> updateScore(UUID matchId, int playerNum) {
        Optional<MatchInPlay> matchOptional = getMatchInPlay(matchId);

        if (matchOptional.isEmpty()) {
            return Optional.empty();
        }

        MatchInPlay matchInPlay = matchOptional.get();
        matchInPlay.addPoint(playerNum);

        if (matchInPlay.isFinished()) {
            Match finishedMatch = saveFinishedMatch(matchInPlay);
            removeMatchInPlay(matchId);
            return Optional.of(finishedMatch);
        }

        return Optional.empty();
    }

    private Match saveFinishedMatch(MatchInPlay matchInPlay) {
        Player player1 = playerService.getPlayerById(matchInPlay.getPlayer1().getId())
                .orElseThrow(() -> new IllegalStateException("Игрок 1 не найден"));
        Player player2 = playerService.getPlayerById(matchInPlay.getPlayer2().getId())
                .orElseThrow(() -> new IllegalStateException("Игрок 2 не найден"));
        Player winner = playerService.getPlayerById(matchInPlay.getIdWinner())
                .orElseThrow(() -> new IllegalStateException("Победитель не найден"));

        Match match = new Match();
        match.setPlayer1(player1);
        match.setPlayer2(player2);
        match.setWinner(winner);

        return matchService.saveMatch(match);
    }
}
