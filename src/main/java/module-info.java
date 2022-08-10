module com.example.esmfamil {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires socket.io.client;
    requires com.google.gson;
    requires json;
    requires engine.io.client;

    opens com.example.esmfamil to javafx.fxml;
    exports com.example.esmfamil;
    exports com.example.esmfamil.Controllers;
    opens com.example.esmfamil.Controllers to javafx.fxml;
    opens com.example.esmfamil.CustomComponent to javafx.fxml;
    exports com.example.esmfamil.Models;
    opens com.example.esmfamil.Models to javafx.fxml, com.google.gson;
}