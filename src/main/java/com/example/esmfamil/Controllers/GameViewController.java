package com.example.esmfamil.Controllers;

import com.example.esmfamil.CustomComponent.GameFieldUI;
import com.example.esmfamil.HelloApplication;
import com.example.esmfamil.Models.*;
import com.google.gson.Gson;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {
    @FXML
    private Label stoppedPlayer;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label currentLetterText;
    @FXML
    private Button stopButton;
    private Game game;
    @FXML
    private GridPane fieldsBox;

    public GameViewController() throws URISyntaxException {
        SocketDevice socketDevice = SocketDevice.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        socketDevice.getSocket().on("giveRoundData", args -> {
            JSONObject jsonObject = new JSONObject(args[0].toString());
            System.out.println(jsonObject);
            ArrayList<RoundData> roundData = new ArrayList<>();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stoppedPlayer.setText("بازیکن " + jsonObject.getString("stopper") + " استُپ کرد.");
                    stopButton.setDisable(true);
                }
            });
            for (Node node : fieldsBox.getChildren()) {
                if (node instanceof GameFieldUI gameFieldUI) {
                    gameFieldUI.setCustomDisable(true);
                    roundData.add(new RoundData(gameFieldUI.getTextInput(), gameFieldUI.getField()));
                }
            }

            Gson gson = new Gson();

            if (args.length > 1 && args[1] instanceof Ack) {
                JSONObject object = new JSONObject();
                object.put("roundData", gson.toJson(roundData));
                object.put("session_id", gameSingleton.getSession_id());
                for (RoundData round : roundData) {
                    if (round.getData().length() > 0 && round.getData().charAt(0) == gameSingleton.getGame().getCurrentChar().charAt(0)) {
                        ((Ack) args[1]).call(object.toString());
                        break;
                    }
                }


                if (gameSingleton.getRole().equals("CREATOR")) {
                    try {
                        Thread.sleep(500);
                        socketDevice.getSocket().emit("startOtherRound_" + gameSingleton.getGame().getId());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GameSingleton gameSingleton = GameSingleton.getInstance();


        game = gameSingleton.getGame();

        currentLetterText.setText("حرف این دور : " + game.getCurrentChar());
        int row = 0;
        int col = 0;

        fieldsBox.getChildren().clear();

        for (GameFields gameField : game.getGameFields()) {
            GameFieldUI gameFieldUI = new GameFieldUI();
            gameFieldUI.setTextFieldName(gameField.getName());
            gameFieldUI.setField(gameField);
            gameFieldUI.setTextInput();
            System.out.println(gameFieldUI.getTextInput());
            fieldsBox.add(gameFieldUI, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        try {
            SocketDevice socketDevice = SocketDevice.getInstance();

            socketDevice.getSocket().on("startAnotherRound", args -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/ChooseLetter.fxml"));
                Stage stage = gameSingleton.getStage();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stage.setScene(new Scene(fxmlLoader.load(), 600, 400));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            });
            socketDevice.getSocket().on("endGame", args -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/EndGame.fxml"));
                Stage stage = gameSingleton.getStage();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stage.setScene(new Scene(fxmlLoader.load(), 600, 400));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopButtonAction(ActionEvent actionEvent) throws URISyntaxException {
        stopButton.setDisable(true);
        SocketDevice socketDevice = SocketDevice.getInstance();
        System.out.println("Stop Button");
        if (validateForm()) {
            socketDevice.getSocket().emit("gameStop_" + game.getId());
        }
    }


    private boolean validateForm() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("خطا");
        for (Node node : fieldsBox.getChildren()) {
            if (node instanceof GameFieldUI gameFieldUI) {
                if (gameFieldUI.getTextInput().isEmpty()) {
                    alert.setContentText("برای استُپ باید همه ستون هارا پر کنید.");
                    alert.showAndWait();
                    return false;
                }

                if (gameFieldUI.getTextInput().charAt(0) != game.getCurrentChar().charAt(0)) {
                    alert.setContentText("کلمه های وارد شده باید با حرف مشخص شده شروع شوند.");
                    alert.showAndWait();
                    return false;
                }

            }
        }
        return true;
    }
}
