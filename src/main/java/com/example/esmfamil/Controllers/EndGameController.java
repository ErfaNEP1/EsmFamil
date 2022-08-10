package com.example.esmfamil.Controllers;

import com.example.esmfamil.HelloApplication;
import com.example.esmfamil.Models.GameSingleton;
import com.example.esmfamil.Models.Player;
import com.example.esmfamil.Models.SocketDevice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.socket.client.Ack;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {
    @FXML
    private Button backToMainMenu;
    @FXML
    private TableView<Player> playersTable;

    @FXML
    private TableColumn<Player,String> usernameCol;
    @FXML
    private TableColumn<Player,Integer> scoreCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameSingleton gameSingleton = GameSingleton.getInstance();

        try {
            SocketDevice socketDevice = SocketDevice.getInstance();
            socketDevice.getSocket().emit("getScores_" + gameSingleton.getGame().getId(), "", (Ack) args ->{
                Type RoundDataType = new TypeToken<ArrayList<Player>>() {
                }.getType();
                ArrayList<Player> playerData = new Gson().fromJson(args[0].toString(), RoundDataType);

                usernameCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
                scoreCol.setCellValueFactory(new PropertyValueFactory<>("roundScore"));

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        playersTable.setItems(FXCollections.observableArrayList(playerData));
                        playersTable.getColumns().clear();
                        playersTable.getColumns().add(usernameCol);
                        playersTable.getColumns().add(scoreCol);
                    }
                });
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public void backtomainmenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
        Stage stage = (Stage) playersTable.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 800, 600));
    }
}
