package com.example.sibregionreport;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("show_tables.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 978, 622);
        stage.setTitle("Таблица отчетности СибирьРегионДорСтрой");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}