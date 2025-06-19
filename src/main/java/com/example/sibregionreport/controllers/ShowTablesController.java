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
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import javax.swing.*;

import java.awt.event.ActionEvent;
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
    private DatePicker datePicker1;

    @FXML
    private DatePicker datePicker2;

    Runnable lastMethod;

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
        setuoDeleteOnKeyPress();
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Не удалось отобразить окно добавления талона.");
                alert.showAndWait();
            }
        }
        else {
            newAddScene.toFront();
        }
    }

    @FXML
    public void showDriversReport() {
        List<Driver> driverList = DAOLoader.getDriverList(datePicker1, datePicker2, driverPicker, materialPicker, objectPicker);
        ObservableList<TableRecord> tableRecords = FXCollections.observableArrayList();
        for (Driver driver : driverList) {
            TableRecord record = new TableRecord();
            record.addField("id", String.valueOf(driver.getId()));
            record.addField("Дата", driver.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
            record.addField("Имя", driver.getName());
            record.addField("Пробег", String.valueOf(driver.getMileage()));
            record.addField("Количество рейсов", String.valueOf(driver.getTrip_num()));
            record.addField("Количество часов", String.valueOf(driver.getHours_cnt()));
            record.addField("Израсходовано топлива", String.valueOf(driver.getFuelSpend()));

            tableRecords.add(record);
        }
        showTableWithRecords(tableRecords);
        lastMethod = this::showDriversReport;
    }

    @FXML
    public void showDieselSpend() {
        List<Diesel> dieselList = DAOLoader.getDieselList(datePicker1, datePicker2, driverPicker, materialPicker, objectPicker);
        ObservableList<TableRecord>tableRecords = FXCollections.observableArrayList();
        for (Diesel diesel : dieselList) {
            TableRecord record = new TableRecord();
            record.addField("id", String.valueOf(diesel.getId()));
            record.addField("Дата", diesel.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
            record.addField("Гос номер", diesel.getState_num());
            record.addField("Имя", diesel.getDriver_name());
            record.addField("Пробег", String.valueOf(diesel.getMileage()));
            record.addField("Израсходовано топлива", String.valueOf(diesel.getFuel_size()));

            tableRecords.add(record);
        }
        showTableWithRecords(tableRecords);
        lastMethod = this::showDieselSpend;
    }

    @FXML
    public void showMaterialsSpend() {
        List<Material> materialList = DAOLoader.getMaterialList(datePicker1, datePicker2, driverPicker, materialPicker, objectPicker);
        ObservableList<TableRecord> tableRecords = FXCollections.observableArrayList();
        for (Material material : materialList) {
            TableRecord record = new TableRecord();
            record.addField("id", String.valueOf(material.getId()));
            record.addField("Дата", material.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
            record.addField("Материал", material.getMaterial_name());
            record.addField("Объект", material.getObject_name());
            record.addField("Количество рейсов", String.valueOf(material.getTrip_num()));
            record.addField("Тонаж", String.valueOf(material.getTons()));

            tableRecords.add(record);
        }
        showTableWithRecords(tableRecords);
        lastMethod = this::showMaterialsSpend;
    }

    @FXML
    public void showObjectsReport() {
        List<Objects> objectsList = DAOLoader.getObjectsList(datePicker1, datePicker2, driverPicker, materialPicker, objectPicker);
        ObservableList<TableRecord> tableRecords = FXCollections.observableArrayList();
        for (Objects object : objectsList) {
            TableRecord record = new TableRecord();
            record.addField("id", String.valueOf(object.getId()));
            record.addField("Дата", object.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
            record.addField("Объект", object.getObject_name());
            record.addField("Материал", object.getMaterial_name());
            record.addField("Тонаж", String.valueOf(object.getTons()));

            tableRecords.add(record);
        }
        showTableWithRecords(tableRecords);
        lastMethod = this::showObjectsReport;
    }

    @FXML
    public void showAll() {
        List<Columns> columnsList = DAOLoader.getAllColumns(datePicker1, datePicker2, driverPicker, materialPicker, objectPicker);
        ObservableList<TableRecord> tableRecords = FXCollections.observableArrayList();
        for (Columns column : columnsList) {
            TableRecord record = new TableRecord();
            record.addField("id", String.valueOf(column.getId()));
            record.addField("Дата", column.getDate().format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
            record.addField("Гос номер", column.getState_num());
            record.addField("Водитель", column.getDriver());
            record.addField("Закачик", column.getCustomer());
            record.addField("Время начала", String.valueOf(column.getShift_time_start()));
            record.addField("Время конца", String.valueOf(column.getShift_time_ending()));
            record.addField("Откуда", column.getFrom());
            record.addField("Куда", column.getDestination());
            record.addField("Расход топлива", String.valueOf(column.getFuel()));
            record.addField("Километраж", String.valueOf(column.getMileage()));
            record.addField("Количество часов", String.valueOf(column.getHours_num()));
            record.addField("Количество рейсов", String.valueOf(column.getTrip_num()));
            record.addField("Материал", column.getMaterial_name());
            record.addField("Тон", String.valueOf(column.getTons()));

            tableRecords.add(record);
        }
        showTableWithRecords(tableRecords);
        lastMethod = this::showAll;
    }

    @FXML
    public void update() {
        initialize();
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

    private void setuoDeleteOnKeyPress() {
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                TableRecord record = tableView.getSelectionModel().getSelectedItem();
                if (record != null) {
                    DAOLoader.deleteFromDB(Integer.parseInt(record.getField("id")));
                    lastMethod.run();
                }
            }
        });
    }
}
