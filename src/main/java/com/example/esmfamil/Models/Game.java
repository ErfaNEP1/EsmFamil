package com.example.esmfamil.Models;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String name;
    private int id;

    private int gameTimeSeconds;
    private int gameRoundCount;

    private String stage;

    private GameMode gameMode;
    private List<GameFields> gameFields = new ArrayList<>();

    private final List<Player> players = new ArrayList<>();

    private final List<String> characters = new ArrayList<>();

    private String currentChar;

    public Game() {

    }

    public Game(String name, int id, List<GameFields> gameFields) {
        this.name = name;
        this.id = id;
        this.gameFields = gameFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<GameFields> getGameFields() {
        return gameFields;
    }

    public void setGameFields(List<GameFields> gameFields) {
        this.gameFields = gameFields;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public int getGameTimeSeconds() {
        return gameTimeSeconds;
    }

    public void setGameTimeSeconds(int gameTimeSeconds) {
        this.gameTimeSeconds = gameTimeSeconds;
    }

    public int getGameRoundCount() {
        return gameRoundCount;
    }

    public void setGameRoundCount(int gameRoundCount) {
        this.gameRoundCount = gameRoundCount;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public String getCurrentChar() {
        return currentChar;
    }

    public void setCurrentChar(String currentChar) {
        this.currentChar = currentChar;
    }
}