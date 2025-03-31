package com.example.sibregionreport.controllers;

import com.example.sibregionreport.models.*;
import com.example.sibregionreport.services.DAOLoader;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import javax.swing.*;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ShowTablesController {

    Stage newAddScene;

    @FXML
    private ChoiceBox<String> objectPicker;

    @FXML
    private ChoiceBox<String> driverPicker;

    @FXML
    private ChoiceBox<String> materialPicker;

    @FXML
    private TableView<TableRecord> tableView;

    @FXML
    public void initialize() {
        // Заполняем ChoiceBox данными, полученными из базы данных, в отдельном потоке
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            ObservableList<String> objects = DAOLoader.getObjectsDB();
            ObservableList<String> drivers = DAOLoader.getDriversDB();
            ObservableList<String> materials = DAOLoader.getMaterialsDB();

            Platform.runLater(() -> {
                objectPicker.setItems(objects);
                driverPicker.setItems(drivers);
                materialPicker.setItems(materials);
                executor.shutdown();
            });

            // Добавляем null значения в списки
            objects.add(null);
            drivers.add(null);
            materials.add(null);

        });
    }

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

    @FXML
    public void showDriversReport() {
        List<Driver> driverList = DAOLoader.getDriverList();
        String selectedName = driverPicker.getValue();
        ObservableList<TableRecord> tableRecords = driverList.stream().filter(driver -> selectedName == null || selectedName.equals(driver.getName()))
                .map( driver -> {
            TableRecord record = new TableRecord();
            record.addField("Дата", driver.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
            record.addField("Имя", driver.getName());
            record.addField("Пробег", String.valueOf(driver.getMileage()));
            record.addField("Количество рейсов", String.valueOf(driver.getTrip_num()));
            record.addField("Количество часов", String.valueOf(driver.getHours_cnt()));
            record.addField("Израсходовано топлива", String.valueOf(driver.getFuelSpend()));

            return record;
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        showTableWithRecords(tableRecords);
    }

    @FXML
    public void showDieselSpend() {
        List<Diesel> dieselList = DAOLoader.getDieselList();
        String selectedName = driverPicker.getValue();
        ObservableList<TableRecord>tableRecords = dieselList.stream()
                .filter(diesel -> selectedName == null || selectedName.equals(diesel.getDriver_name()))
                .map(diesel ->{
                    TableRecord record = new TableRecord();
                    record.addField("Дата",diesel.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
                    record.addField("Гос номер", diesel.getState_num());
                    record.addField("Имя", diesel.getDriver_name());
                    record.addField("Пробег", String.valueOf(diesel.getMileage()));
                    record.addField("Израсходовано топлива", String.valueOf(diesel.getFuel_size()));

                    return record;
                }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        showTableWithRecords(tableRecords);
    }

    @FXML
    public void showMaterialsSpend() {
        List<Material> materialList = DAOLoader.getMaterialList();
        String selectedMaterial = materialPicker.getValue();
        ObservableList<TableRecord> tableRecords = materialList.stream()
                .filter(material -> selectedMaterial == null || selectedMaterial.equals(material.getMaterial_name()))
                .map(material ->{
                    TableRecord record = new TableRecord();
                    record.addField("Дата",material.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
                    record.addField("Материал", material.getMaterial_name());
                    record.addField("Объект", material.getObject_name());
                    record.addField("Количество рейсов", String.valueOf(material.getTrip_num()));
                    record.addField("Тонаж", String.valueOf(material.getTons()));

                    return record;
                }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        showTableWithRecords(tableRecords);
    }

    @FXML
    public void showObjectsReport() {
        List<Objects> objectsList = DAOLoader.getObjectsList();
        String selectedObject = objectPicker.getValue();
        ObservableList<TableRecord> tableRecords = objectsList.stream()
                .filter(object -> selectedObject == null || selectedObject.equals(object.getObject_name()))
                .map(object -> {
                    TableRecord record = new TableRecord();
                    record.addField("Дата",object.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
                    record.addField("Объект", object.getObject_name());
                    record.addField("Материал", object.getMaterial_name());
                    record.addField("Тонаж", String.valueOf(object.getTons()));

                    return record;
                }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        showTableWithRecords(tableRecords);
    }

    // Этот класс используется для хранения данных для строк таблицы
    private static class TableRecord {
        private final Map<String, String> data;

        public TableRecord() {
            this.data = new LinkedHashMap<>();
        }

        public TableRecord(String key, String value) {
            this.data = new LinkedHashMap<>();
            data.put(key, value);
        }

        public void addField(String key, String value) {
            data.put(key, value);
        }

        public String getField(String key) {
            return data.getOrDefault(key, "");
        }

        public Map<String, String> getAllFields() {
            return data;
        }
    }

    private void showTableWithRecords(ObservableList<TableRecord> tableRecords) {
        tableView.getColumns().clear();
        // Динамическое создание колонок на основе первой записи
        if (!tableRecords.isEmpty()) {
            for (String key : tableRecords.get(0).getAllFields().keySet()) {
                TableColumn<TableRecord, String> column = new TableColumn<>(key);
                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getField(key)));
                tableView.getColumns().add(column);
            }
            // Устанавливаем данные в таблицу
            tableView.setItems(tableRecords);
        }
    }
}
