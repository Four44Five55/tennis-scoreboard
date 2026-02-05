package org.example.domain;

import org.example.entity.Player;

public class MatchInPlay {
    private PlayerScore player1;
    private PlayerScore player2;
    private boolean isTieBreak;
    private boolean isFinished;
    private int idWinner;

    public MatchInPlay(Player player1, Player player2) {
        this.player1 = new PlayerScore(player1);
        this.player2 = new PlayerScore(player2);
    }

    public PlayerScore getPlayer1() {
        return player1;
    }

    public PlayerScore getPlayer2() {
        return player2;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public int getIdWinner() {
        return idWinner;
    }

    /**
     * Возвращает отображение очков для игрока 1 в теннисном формате.
     * Учитывает состояние "ровно" (deuce), "больше" (advantage).
     */
    public String getPlayer1DisplayPoints() {
        return getDisplayPoints(player1, player2);
    }

    /**
     * Возвращает отображение очков для игрока 2 в теннисном формате.
     * Учитывает состояние "ровно" (deuce), "больше" (advantage).
     */
    public String getPlayer2DisplayPoints() {
        return getDisplayPoints(player2, player1);
    }

    private String getDisplayPoints(PlayerScore player, PlayerScore opponent) {
        int p1Points = player.getPoints();
        int p2Points = opponent.getPoints();

        if (isTieBreak) {
            return String.valueOf(player.getPoints());
        }

        // Оба игрока имеют >= 3 очка (40+)
        if (p1Points >= 3 && p2Points >= 3) {
            if (p1Points == p2Points) {
                return "40"; // "ровно"
            } else if (p1Points > p2Points) {
                return "Ad"; // "больше"
            }
        }

        // Обычное отображение
        return player.getDisplayPoints();
    }


    public void addPoint(int playerNum) {
        if (isFinished) {
            return;
        }

        PlayerScore scorer = playerNum == 1 ? player1 : player2;
        PlayerScore opponent = playerNum == 1 ? player2 : player1;

        scorer.addPoint();

        // Проверка выигранного гейма
        if (isGameWon(scorer, opponent)) {
            scorer.addGame();
            scorer.resetPoints();
            opponent.resetPoints();

            // Проверка выигранного сета
            if (isSetWon(scorer, opponent)) {
                scorer.addSet();
                scorer.resetGames();
                opponent.resetGames();

                // Проверка выигранного матча
                if (scorer.getSet() >= 2) {
                    isFinished = true;
                    idWinner = scorer.getId();
                }
            }
        }
    }

    private boolean isGameWon(PlayerScore scorer, PlayerScore opponent) {
        if (isTieBreak) {
            return scorer.getPoints() >= 7 && (scorer.getPoints() - opponent.getPoints()) >= 2;
        }
        // Обычный гейм: 4 очка с отрывом >= 2
        return scorer.getPoints() >= 4 && (scorer.getPoints() - opponent.getPoints()) >= 2;
    }

    private boolean isSetWon(PlayerScore scorer, PlayerScore opponent) {
        // Тай-брейк при 6-6
        if (scorer.getGames() == 6 && opponent.getGames() == 6) {
            isTieBreak = true;
            player1.resetPoints();
            player2.resetPoints();
            return false;
        }

        boolean setWon = false;

        // Сет выигрывается при 6 геймах с отрывом >= 2
        // Или при 7 геймах (если был 6-5 и игра до 7)
        // Или при 7 геймах в тай-брейке (7-6)
        if (scorer.getGames() >= 6 && (scorer.getGames() - opponent.getGames()) >= 2) {
            setWon = true;
        } else if (scorer.getGames() == 7) {
            setWon = true;
        }

        // Сбрасываем флаг тай-брейка при завершении сета
        if (setWon) {
            isTieBreak = false;
        }

        return setWon;
    }
}
