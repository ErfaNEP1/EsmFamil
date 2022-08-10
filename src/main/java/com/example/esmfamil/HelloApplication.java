package com.example.esmfamil;

import com.example.esmfamil.Models.SocketDevice;
import io.socket.client.IO;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.Socket;
import javafx.stage.WindowEvent;

public class HelloApplication extends Application {

    private static final int DEFAULT_PORT = 9999;
    private static final String IP_ADDRESS = "http://localhost:";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Esm-Famil");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    SocketDevice.getInstance().close();
                    System.exit(0);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        stage.show();




    }

    public static void main(String[] args) {
        launch();
    }
}