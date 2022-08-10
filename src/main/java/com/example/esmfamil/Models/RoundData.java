package com.example.esmfamil.Models;

public class RoundData {
    private String data;
    private GameFields gameField;

    public RoundData(String data, GameFields gameField) {
        this.data = data;
        this.gameField = gameField;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public GameFields getGameField() {
        return gameField;
    }

    public void setGameField(GameFields gameField) {
        this.gameField = gameField;
    }
}
