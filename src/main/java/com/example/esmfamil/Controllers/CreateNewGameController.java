package com.example.esmfamil.Controllers;

import com.example.esmfamil.HelloApplication;
import com.example.esmfamil.Models.*;
import com.google.gson.Gson;
import io.socket.client.Ack;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateNewGameController implements Initializable {
    @FXML
    private TextField gameRoundCount;
    @FXML
    private TextField gameTimeField;
    @FXML
    ListView<GameFields> listFields;
    @FXML
    ComboBox<GameMode> comboBoxGameMode;

    @FXML
    Button getToMainMenu, createGame;
    @FXML
    TextField userNameField;

    @FXML
    protected void getToMainMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
        Stage stage = (Stage) listFields.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 800, 600));
    }

    @FXML
    protected void createGame() {
        if (validateForm()) {
            try {
                SocketDevice client = SocketDevice.getInstance();
                client.open();
                Gson gson = new Gson();

                Game game = new Game();
                game.setName(this.userNameField.getText());
                game.setGameFields(listFields.getSelectionModel().getSelectedItems());
                game.setGameMode(comboBoxGameMode.getSelectionModel().getSelectedItem());
                game.setGameRoundCount(Integer.parseInt(gameRoundCount.getText()));
                game.setGameTimeSeconds(Integer.parseInt(gameTimeField.getText()));
                System.out.println(gson.toJson(game));


                client.getSocket().emit("createGame", gson.toJson(game), (Ack) args -> {
                    JSONObject response = new JSONObject(args[0].toString());
                    System.out.println(response.getString("status"));
                    client.setSession_id(response.getString("session_id"));
                    if (response.getString("status").equals("ok")) {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/GameLobby.fxml"));
                        Stage stage = (Stage) listFields.getScene().getWindow();
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

                        Game newGame = new Gson().fromJson(response.getString("game_model"),Game.class);
                        GameSingleton gameSingleton = GameSingleton.getInstance();
                        gameSingleton.setGame(newGame);
                        gameSingleton.setRole("CREATOR");
                        gameSingleton.setSession_id(response.getString("session_id"));

                    }
                });

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listFields.setItems(FXCollections.observableArrayList(GameFields.values()));
        listFields.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        comboBoxGameMode.setItems(FXCollections.observableArrayList(GameMode.values()));

    }

    private boolean validateForm() {

        System.out.println(this.gameRoundCount.getText());
        System.out.println(this.gameTimeField.getText());

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("خطا");
        if (this.userNameField.getText().isEmpty()) {
            alert.setContentText("نام کاربری خود را وارد کنید.");
            alert.showAndWait();
            return false;
        }

        if(this.gameRoundCount.getText().isEmpty()){
            alert.setContentText("تعداد راند های بازی را وارد کنید.");
            alert.showAndWait();
            return false;
        }

        if (this.listFields.getSelectionModel().getSelectedItems().size() < 5) {
            alert.setContentText("تعداد ستون های بازی باید بیشتر از 5 عدد باشد!");
            alert.showAndWait();
            return false;
        }

        if (this.comboBoxGameMode.getSelectionModel().getSelectedItem() == null) {
            alert.setContentText("نحوه اتمام بازی را مشخص کنید.");
            alert.showAndWait();
            return false;
        }

        if (!userNameField.getText().matches("^[a-zA-Z\\d]*$")) {
            alert.setContentText("فقط حروف انگلیسی به همراه اعداد وارد کنید.");
            alert.showAndWait();
            return false;
        }

        if (!parseInteger(gameRoundCount.getText())){
            alert.setContentText("تعداد راند های بازی باید عددی باشد.");
            alert.showAndWait();
            return false;
        }

        if (!parseInteger(gameTimeField.getText())){
            alert.setContentText("زمان هر راند باید عددی باشد.");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private boolean parseInteger(String s) {
        try {
            int number = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
