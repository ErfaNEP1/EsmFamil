package com.example.esmfamil.Controllers;

import com.example.esmfamil.HelloApplication;
import com.example.esmfamil.Models.*;
import com.google.gson.Gson;
import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GameLobbyController implements Initializable {
    private int gameId;

    public TableView<Player> listPlayers;
    public Button startGame;
    public Button getToMainMenu;
    public ListView<String> listDetails;
    public TableColumn<Player, String> roleCol;
    public TableColumn<Player, String> playerCol;
    private Stage myStage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            SocketDevice client = SocketDevice.getInstance();

            client.getSocket().on("updatePlayerList", objects -> {
                System.out.println(objects[0].toString());

                Game updatedData = new Gson().fromJson(objects[0].toString(), Game.class);

                System.out.println(updatedData.getPlayers().toString());

                playerCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
                roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

                Platform.runLater(() -> {
                    listPlayers.setItems(FXCollections.observableList(updatedData.getPlayers()));
                    listPlayers.getColumns().clear();
                    listPlayers.getColumns().add(playerCol);
                    listPlayers.getColumns().add(roleCol);
                });
            });

            client.getSocket().on("removeGame", objects -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
                Stage stage = (Stage) listPlayers.getScene().getWindow();
                try {
                    stage.setScene(new Scene(fxmlLoader.load(), 800, 600));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                GameSingleton gameSingleton = GameSingleton.getInstance();
                gameSingleton.setGame(null);
                gameSingleton.setRole(null);

                client.close();
            });

            client.getSocket().on("goToChooseCharacterView", objects -> {

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/ChooseLetter.fxml"));
                Stage stage = (Stage) listPlayers.getScene().getWindow();
                Platform.runLater(() -> {
                    try {
                        stage.setScene(new Scene(fxmlLoader.load(), 600, 400));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            });

            client.getSocket().emit("gameInfo", myStage.getUserData().toString(), (Ack) args -> {
                Game game = new Gson().fromJson(args[0].toString(), Game.class);
                for (GameFields gameField : game.getGameFields()) {
                    listDetails.getItems().add(gameField.getName());
                }
                listDetails.getItems().add("نوع بازی : " + game.getGameMode().getName());


                playerCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
                roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

                Platform.runLater(() -> {
                    listPlayers.setItems(FXCollections.observableList(game.getPlayers()));
                    listPlayers.getColumns().clear();
                    listPlayers.getColumns().add(playerCol);
                    listPlayers.getColumns().add(roleCol);
                });


                if (args[1].equals("PLAYER")) {
                    Platform.runLater(() -> {
                        startGame.setDisable(true);
                        startGame.setText("صبر کنید...");
                    });

                } else {
                    getToMainMenu.setDisable(true);
                    getToMainMenu.setText("صبر کنید...");
                }

                GameSingleton gameSingleton = GameSingleton.getInstance();
                gameSingleton.setGame(game);
                gameSingleton.setRole(args[1].toString());
                gameSingleton.setStage(myStage);
                gameId = game.getId();

            });


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


    }

    public void startGame(ActionEvent actionEvent) throws IOException, URISyntaxException {
        Socket socket = SocketDevice.getInstance().getSocket();
        socket.emit("startGame", gameId);
    }


    public void getToMainMenu(ActionEvent actionEvent) throws URISyntaxException, IOException {
        SocketDevice client = SocketDevice.getInstance();
        client.close();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
        Stage stage = (Stage) listDetails.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 800, 600));
    }

    public void setStage(Stage stage) {
        this.myStage = stage;
    }
}
