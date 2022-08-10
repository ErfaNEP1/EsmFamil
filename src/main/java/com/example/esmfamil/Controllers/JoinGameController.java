package com.example.esmfamil.Controllers;

import com.example.esmfamil.HelloApplication;
import com.example.esmfamil.Models.Game;
import com.example.esmfamil.Models.GameFields;
import com.example.esmfamil.Models.GameSingleton;
import com.example.esmfamil.Models.SocketDevice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.socket.client.Ack;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JoinGameController implements Initializable {
    @FXML
    TableView<Game> listGames;
    @FXML
    TableColumn<Game, String> nameFieldTable;
    @FXML
    TableColumn<Game, String> modeFieldTable;
    @FXML
    TableColumn<Game, Integer> roundFieldTable;
    @FXML
    TableColumn<Game, Integer> timeFieldTable;
    @FXML
    TextField userNameField;
    @FXML
    Button getToMainMenu, joinGame;


    public void joinGame(Event event) {
        if (validateForm()) {
            try {
                SocketDevice client = SocketDevice.getInstance();

                JSONObject playerInfo = new JSONObject();
                playerInfo.put("user_name", this.userNameField.getText());
                playerInfo.put("game_name", this.listGames.getSelectionModel().getSelectedItem().getName());
                playerInfo.put("game_id", this.listGames.getSelectionModel().getSelectedItem().getId());

                client.getSocket().emit("joinGame", playerInfo.toString(), (Ack) args -> {
                    JSONObject response = new JSONObject(args[0].toString());

                    System.out.println(response.getString("status"));
                    client.setSession_id(response.getString("session_id"));

                    if (response.getString("status").equals("ok")) {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/GameLobby.fxml"));

                        Stage stage = (Stage) userNameField.getScene().getWindow();
                        stage.setUserData(playerInfo.getInt("game_id"));

                        stage.setUserData(response.getInt("game_id"));
                        GameLobbyController gameLobbyController = new GameLobbyController();
                        gameLobbyController.setStage(stage);
                        fxmlLoader.setController(gameLobbyController);


                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Parent root = fxmlLoader.load();
                                    Scene newScene = new Scene(root);
                                    stage.setScene(newScene);
                                    stage.show();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        Game newGame = new Gson().fromJson(response.getString("game_model"), Game.class);
                        GameSingleton gameSingleton = GameSingleton.getInstance();
                        gameSingleton.setGame(newGame);
                        gameSingleton.setRole("PLAYER");
                        gameSingleton.setSession_id(response.getString("session_id"));
                    }
                });


            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void getToMainMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
        Stage stage = (Stage) listGames.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 800, 600));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            SocketDevice client = SocketDevice.getInstance();
            client.open();

            nameFieldTable.setCellValueFactory(new PropertyValueFactory<>("name"));
            modeFieldTable.setCellValueFactory(new PropertyValueFactory<>("gameMode"));
            roundFieldTable.setCellValueFactory(new PropertyValueFactory<>("gameRoundCount"));
            timeFieldTable.setCellValueFactory(new PropertyValueFactory<>("gameTimeSeconds"));


            client.getSocket().emit("getLiveGames", "test", (Ack) args -> {
                Type gameListType = new TypeToken<ArrayList<Game>>() {
                }.getType();
                ArrayList<Game> liveGames = new Gson().fromJson(args[0].toString(), gameListType);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listGames.setItems(FXCollections.observableArrayList(liveGames));
                        listGames.getColumns().clear();
                        listGames.getColumns().addAll(nameFieldTable, modeFieldTable, roundFieldTable, timeFieldTable);
                    }
                });

            });


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateForm() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("خطا");
        if (this.userNameField.getText().isEmpty()) {
            alert.setContentText("نام کاربری خود را وارد کنید.");
            alert.showAndWait();
            return false;
        }

        if (this.listGames.getSelectionModel().getSelectedItem() == null) {
            alert.setContentText("بازی مورد نظر خود را انتخاب کنید");
            alert.showAndWait();
            return false;
        }

        if (!userNameField.getText().matches("^[a-zA-Z\\d]*$")) {
            alert.setContentText("فقط حروف انگلیسی به همراه اعداد وارد کنید.");
            alert.showAndWait();
            return false;
        }

        return true;
    }

}
