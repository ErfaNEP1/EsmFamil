package com.example.esmfamil.Controllers;

import com.example.esmfamil.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    Button activeGames, createNewGame;
    @FXML
    protected void activeGamesHandle() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/JoinGameView.fxml"));

        Stage stage = (Stage) createNewGame.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 800, 300);
        stage.setTitle("Esm-Famil");
        stage.setScene(scene);
    }

    @FXML
    protected void createNewGameHandle() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/CreateNewGame.fxml"));

        Stage stage = (Stage) createNewGame.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 800, 300);
        stage.setTitle("Esm-Famil");
        stage.setScene(scene);
    }
}
