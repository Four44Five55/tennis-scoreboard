package org.example.dto;

/**
 * DTO для отображения текущего счета матча на странице.
 * Содержит только те данные, которые нужны для отображения в JSP.
 */
public class MatchScoreDTO {
    private final PlayerScoreDTO player1;
    private final PlayerScoreDTO player2;

    public MatchScoreDTO(PlayerScoreDTO player1, PlayerScoreDTO player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public PlayerScoreDTO getPlayer1() {
        return player1;
    }

    public PlayerScoreDTO getPlayer2() {
        return player2;
    }
}
