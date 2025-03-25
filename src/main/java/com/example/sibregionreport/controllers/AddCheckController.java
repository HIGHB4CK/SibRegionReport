package com.example.sibregionreport.controllers;

import com.example.sibregionreport.CommonClasses.TextFormatters;
import com.example.sibregionreport.services.DAOLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddCheckController {

    @FXML
    private TextField indicateCustomer;
    @FXML
    private TextField indicateFrom;
    @FXML
    private TextField indicateTo;
    @FXML
    private TextField indicateDieselSpend;
    @FXML
    private TextField indicateKm;
    @FXML
    private TextField indicateStart;
    @FXML
    private TextField indicateEnd;
    @FXML
    private TextField indicateTrip_num;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> indicateState_Num;
    @FXML
    private ChoiceBox<String> indicateDriver;
    @FXML
    private TextField indicateHours;
    @FXML
    private TextField indicateMaterial;
    @FXML
    private TextField indicateTons;

    @FXML
    public void initialize() {
        indicateDieselSpend.setTextFormatter(TextFormatters.createShortTextFormatter());
        indicateKm.setTextFormatter(TextFormatters.createIntegerTextFormatter());
        indicateTrip_num.setTextFormatter(TextFormatters.createShortTextFormatter());
        indicateTons.setTextFormatter(TextFormatters.createShortTextFormatter());
        indicateHours.setTextFormatter(TextFormatters.createShortTextFormatter());
    }

    @FXML
    public void saveData() throws SQLException {
        DAOLoader daoLoader = new DAOLoader();
        daoLoader.init();

        String customer;
        String from;
        String destination;
        Short fuel;
        Integer mileage;
        Short trip_num;
        String material;
        Short tons;
        Integer hours_cnt;
        LocalTime shift_time_start;
        LocalTime shift_time_ending;

        try {
            LocalDate date = datePicker.getValue();
            String state_num = indicateState_Num.getValue();
            String driver = indicateDriver.getValue();
            customer = indicateCustomer.getText();
            from = indicateFrom.getText();
            destination = indicateTo.getText();
            fuel = Short.valueOf(indicateDieselSpend.getText());
            mileage = Integer.parseInt(indicateKm.getText());
            trip_num = Short.valueOf(indicateTrip_num.getText());
            material = indicateMaterial.getText();
            tons = Short.valueOf(indicateTons.getText());
            hours_cnt = Integer.parseInt(indicateHours.getText());

            try {
                shift_time_start = LocalTime.parse(indicateStart.getText());
            } catch (DateTimeParseException e) {
                showAlert("Ошибка ввода", "Неверный формат времени начала смены. Используйте H:mm");
                return;
            }

            try {
                shift_time_ending = LocalTime.parse(indicateEnd.getText());
            } catch (DateTimeParseException e) {
                showAlert("Ошибка ввода", "Неверный формат времени окончания смены. Используйте H:mm");
                return;
            }

            daoLoader.saveData(date, state_num, driver, customer, from, destination, fuel, mileage, trip_num, material, tons, hours_cnt, shift_time_start, shift_time_ending);
            daoLoader.close();
        } catch(Exception e) {
            showAlert("Ошибка ввода", "Не все поля заполнены");
            return;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
