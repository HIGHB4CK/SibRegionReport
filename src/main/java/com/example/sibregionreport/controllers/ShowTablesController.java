package com.example.sibregionreport.controllers;

import com.example.sibregionreport.models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.util.List;

public class ShowTablesController {

    Stage newAddScene;

    List<Driver> driverList;
    List<Diesel> dieselList;
    List<Material> materialList;
    List<Objects> objectsList;

    @FXML
    private TableView tableView;

    @FXML
    public void addCheck() {
        if (newAddScene == null) {
            try {
                // Используем getClass().getResource() для надежного поиска файла
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sibregionreport/add_check.fxml")); // Замените путь, если нужно
                Scene scene = new Scene(loader.load(), 610, 400);
                newAddScene = new Stage();
                newAddScene.setTitle("Отображение таблиц");
                newAddScene.setScene(scene);
                newAddScene.setOnCloseRequest(e -> newAddScene = null);
                newAddScene.show();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Не удалось отобразить окно добавления талона" + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            newAddScene.toFront();
        }
    }

    public void showDieselSpend() {

    }

    public void showDriversReport() {

    }

    public void showMaterialsSpend() {

    }

    public void showObjectsReport() {

    }
}
