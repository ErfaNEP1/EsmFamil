package com.example.esmfamil.CustomComponent;

import com.example.esmfamil.HelloApplication;
import com.example.esmfamil.Models.GameFields;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.beans.property.StringProperty;
import java.io.IOException;

public class GameFieldUI extends VBox {

    private GameFields field;
    @FXML
    Label fieldName;
    @FXML
    TextField fieldInput;

    public GameFieldUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(
                "views/GameFieldUi.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setTextFieldName(String value) {
        fieldNameProperty().set(value);
    }

    public String getTextInput(){
        return fieldInput.getText();
    }
    public void setTextInput(){
        fieldInputProperty().set("");
    }

    public StringProperty fieldNameProperty() {
        return fieldName.textProperty();
    }

    public StringProperty fieldInputProperty() {
        return fieldInput.textProperty();
    }

    public GameFields getField() {
        return field;
    }

    public void setField(GameFields field) {
        this.field = field;
    }

    public void setCustomDisable(Boolean b){
        fieldInput.setDisable(b);
    }
}
