package com.example.esmfamil.Controllers;

import com.example.esmfamil.HelloApplication;
import com.example.esmfamil.Models.Game;
import com.example.esmfamil.Models.GameFields;
import com.example.esmfamil.Models.GameSingleton;
import com.example.esmfamil.Models.SocketDevice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChooseLetterController implements Initializable {
    @FXML
    private Label letterChoiceText;
    @FXML
    private ChoiceBox<String> letterChoiceBox;
    @FXML
    private Button letterChoiceButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameSingleton gameSingleton = GameSingleton.getInstance();
        try {
            SocketDevice client = SocketDevice.getInstance();

            client.getSocket().on("goToGameView", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("Go To GameView fired");
                    gameSingleton.getGame().setCurrentChar(objects[0].toString());
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/GameView.fxml"));
                    Stage stage = gameSingleton.getStage();


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Parent root;
                                root = fxmlLoader.load();
                                Scene newScene = new Scene(root);
                                stage.setScene(newScene);
                                stage.show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                }
            });


            client.getSocket().emit("choosePlayer_" + gameSingleton.getGame().getId(), "test", (Ack) args -> {
                JSONObject response = new JSONObject(args[0].toString());
                if (response.getBoolean("areYouChosen")) {
                    Type StringListType = new TypeToken<ArrayList<String>>() {
                    }.getType();
                    ArrayList<String> notChosenCharacters = new Gson().fromJson(response.getString("notChosenCharacters"), StringListType);
                    letterChoiceBox.getItems().addAll(notChosenCharacters);
                } else {
                    letterChoiceText.setText("بازیکن " + response.getString("chosenPlayerUserName") + " در حال انتخاب حرف است.");
                    letterChoiceBox.hide();
                    letterChoiceButton.setText("صبرکنید...");
                    letterChoiceButton.setDisable(true);
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public void letterSubmit(ActionEvent actionEvent) {
        if (validateForm()) {
            GameSingleton gameSingleton = GameSingleton.getInstance();
            try {
                SocketDevice client = SocketDevice.getInstance();
                System.out.println("letterSubmit_" + gameSingleton.getGame().getId());
                client.getSocket().emit("letterSubmit_" + gameSingleton.getGame().getId(), letterChoiceBox.getSelectionModel().getSelectedItem(), (Ack) args -> {

                });
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean validateForm() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("خطا");

        if (letterChoiceBox.getSelectionModel().getSelectedItem() == null) {
            alert.setContentText("حرف مورد نظر خودتان را انتخاب کنید.");
            alert.showAndWait();
            return false;
        }

        return true;
    }
}
