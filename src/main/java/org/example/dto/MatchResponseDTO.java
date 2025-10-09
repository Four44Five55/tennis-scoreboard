package org.example.dto;

public class MatchResponseDTO {
    private int id;
    private PlayerResponseDTO player1;
    private PlayerResponseDTO player2;
    private PlayerResponseDTO winner;

    public MatchResponseDTO() {
    }

    public MatchResponseDTO(PlayerResponseDTO winner, PlayerResponseDTO player2, PlayerResponseDTO player1, int id) {
        this.winner = winner;
        this.player2 = player2;
        this.player1 = player1;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerResponseDTO getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerResponseDTO player1) {
        this.player1 = player1;
    }

    public PlayerResponseDTO getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerResponseDTO player2) {
        this.player2 = player2;
    }

    public PlayerResponseDTO getWinner() {
        return winner;
    }

    public void setWinner(PlayerResponseDTO winner) {
        this.winner = winner;
    }
}
