package org.example.validation;

import org.example.domain.MatchInPlay;
import org.example.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Теннисный матч: подсчёт очков")
class MatchScoreTest {

    private MatchInPlay matchInPlay;

    @BeforeEach
    void setUp() {
        Player player1 = new Player("Федерер");
        Player player2 = new Player("Надаль");
        matchInPlay = new MatchInPlay(player1, player2);
    }
    @Test
    @DisplayName("Счет 40 40")
    void Score40to40NotGameOver() {
        matchInPlay.addPoint(1);
        matchInPlay.addPoint(1);
        matchInPlay.addPoint(1);
        matchInPlay.addPoint(2);
        matchInPlay.addPoint(2);
        matchInPlay.addPoint(2);
        matchInPlay.addPoint(1);
        matchInPlay.addPoint(2);

        assertEquals(0, matchInPlay.getPlayer1().getGames());
        assertEquals(0, matchInPlay.getPlayer2().getGames());
    }
    @Test
    @DisplayName("Новый матч начинается со счета  0 0")
    void newMatch_StartWithZeroPoints() {
        assertEquals("0", matchInPlay.getPlayer1DisplayPoints());
        assertEquals("0", matchInPlay.getPlayer2DisplayPoints());
    }
}
