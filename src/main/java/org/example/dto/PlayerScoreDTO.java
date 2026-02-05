package org.example.dto;

/**
 * DTO для отображения очков игрока на странице матча.
 * Содержит только те данные, которые нужны для отображения.
 */
public class PlayerScoreDTO {
    private final String name;
    private final int sets;
    private final int games;
    private final String displayPoints;

    public PlayerScoreDTO(String name, int sets, int games, String displayPoints) {
        this.name = name;
        this.sets = sets;
        this.games = games;
        this.displayPoints = displayPoints;
    }

    public String getName() {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public int getGames() {
        return games;
    }

    public String getDisplayPoints() {
        return displayPoints;
    }
}
