package com.example.sibregionreport;

import com.example.sibregionreport.controllers.ShowTablesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("show_tables.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 622);
        stage.setTitle("Таблица отчетности СибирьРегионДорСтрой");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            launch();
        } catch(Exception e) {
            System.out.println("Ошибка загрузки");
            e.printStackTrace();
        }
    }
}