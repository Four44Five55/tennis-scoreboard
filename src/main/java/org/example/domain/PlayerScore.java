package org.example.domain;

import org.example.entity.Player;

public class PlayerScore {
    private final Player player;

    private int point;
    private int game;
    private int set;

    public PlayerScore(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player не может быть null");
        }
        this.player = player;
        this.point = 0;
        this.game = 0;
        this.set = 0;
    }


    public int getId() {
        return player.getId();
    }

    public String getName() {
        return player.getName();
    }

    public int getPoints() {
        return point;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint() {
        this.point++;
    }

    /**
     * Возвращает отображение очков в теннисном формате.
     * 0 → "0", 1 → "15", 2 → "30", 3 → "40"
     * При 4+ очках возвращает "Ad" (advantage)
     */
    public String getDisplayPoints() {
        return switch (point) {
            case 0 -> "0";
            case 1 -> "15";
            case 2 -> "30";
            case 3 -> "40";
            default -> "Ad"; // 4+ очков
        };
    }

    public void resetPoints() {
        this.point = 0;
    }

    public int getGames() {
        return game;
    }

    public void addGame() {
        this.game++;
    }

    public void resetGames() {
        this.game = 0;
    }
    public int getSet() {
        return set;
    }
    public void addSet() {
        this.set++;
    }
    public void resetSet() {
        this.set = 0;
    }
}
