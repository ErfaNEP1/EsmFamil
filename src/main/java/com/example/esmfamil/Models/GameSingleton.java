package com.example.esmfamil.Models;

import javafx.stage.Stage;

public class GameSingleton {

    private Game game;
    private String role;
    private String session_id;

    private Stage stage;


    private GameSingleton(){

    }
    public static GameSingleton getInstance() {
        if (ref == null)
            ref = new GameSingleton();
        return ref;
    }

    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }

    public Game getGame() {
        return game;
    }

    private static GameSingleton ref;

    public void setGame(Game game) {
        this.game = game;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
