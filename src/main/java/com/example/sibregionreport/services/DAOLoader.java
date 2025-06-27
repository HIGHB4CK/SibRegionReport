package com.example.sibregionreport.services;

import com.example.sibregionreport.controllers.ShowTablesController;
import com.example.sibregionreport.models.*;
import com.example.sibregionreport.models.Driver;
import com.example.sibregionreport.models.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DAOLoader {

    private static final String DB_URL = "jdbc:postgresql://192.168.154.197:5432/SibRegionTalons";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Guseynov2005";

    private static Connection connection;

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void init() throws SQLException {
        connection = connect();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void saveData(LocalDate date, String state_num, String driver, String customer, String from, String destination, float fuel, int milleage, int trip_num, String material, float tons, float hours_cnt, LocalTime shift_time_start, LocalTime shift_time_ending) {
        String sql = "INSERT INTO alluser (\"Date\", \"State_num\", \"Driver\", \"Customer\", \"From\", \"Destination\", \"Fuel\", \"Mileage\", \"Trip_num\", \"Material\", \"Tons\", \"hours_cnt\", \"shift_time_start\", \"shift_time_ending\") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(date)); // Assuming datePicker.getValue() returns LocalDate
            preparedStatement.setString(2, state_num);
            preparedStatement.setString(3, driver);
            preparedStatement.setString(4, customer);
            preparedStatement.setString(5, from);
            preparedStatement.setString(6, destination);
            preparedStatement.setFloat(7, fuel);
            preparedStatement.setInt(8, milleage);
            preparedStatement.setFloat(9, trip_num);
            preparedStatement.setString(10, material);
            preparedStatement.setFloat(11, tons);
            preparedStatement.setFloat(12, hours_cnt);
            preparedStatement.setTime(13, Time.valueOf(shift_time_start));
            preparedStatement.setTime(14, Time.valueOf(shift_time_ending));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка сохранения данных.");
            alert.showAndWait();
        }
    }

    public static List<Driver> getDriverList(DatePicker datePicker1, DatePicker datePicker2, ChoiceBox<String> driverSelector, ChoiceBox<String> materialSelector, ChoiceBox<String> objectSelector) {
        List<Driver> driverList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("Select \"id\", \"Date\", \"Driver\", \"Mileage\", \"Trip_num\", \"hours_cnt\", \"Fuel\" FROM alluser WHERE 1=1");

        if (datePicker1.getValue() != null) {
            sql.append(" AND \"Date\" >= ?");
        }

        if (datePicker2.getValue() != null) {
            sql.append(" AND \"Date\" <= ?");
        }

        if (driverSelector.getValue() != null) {
            sql.append(" AND \"Driver\" = ?");
        }

        if (materialSelector.getValue() != null) {
            sql.append(" AND \"Material\" = ?");
        }

        if (objectSelector.getValue() != null) {
            sql.append(" AND \"Destination\" = ?");
        }

        sql.append(" ORDER BY \"Date\" ASC");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(String.valueOf(sql));
             ) {

            int paramIndex = 1;
            if (datePicker1.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker1.getValue()));
            }

            if (datePicker2.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker2.getValue()));
            }

            if (driverSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, driverSelector.getValue());
            }

            if (materialSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, materialSelector.getValue());
            }

            if (objectSelector.getValue() != null) {
                preparedStatement.setString(paramIndex, objectSelector.getValue());
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String name = resultSet.getString("Driver");
                int mills = resultSet.getInt("Mileage");
                short trip_num = resultSet.getShort("Trip_num");
                short hours_cnt = resultSet.getShort("hours_cnt");
                short fuel = resultSet.getShort("Fuel");
                Driver driver = new Driver(id, date, name, mills, trip_num, hours_cnt, fuel);
                driverList.add(driver);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка получения списка водителей.");
            alert.showAndWait();
        }
        return driverList;
    }

    public static List<Diesel> getDieselList(DatePicker datePicker1, DatePicker datePicker2, ChoiceBox<String> driverSelector, ChoiceBox<String> materialSelector, ChoiceBox<String> objectSelector) {
        List<Diesel> dieselList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("Select \"id\", \"Date\", \"State_num\", \"Driver\", \"Mileage\", \"Fuel\" FROM alluser WHERE 1=1");

        if (datePicker1.getValue() != null) {
            sql.append(" AND \"Date\" >= ?");
        }

        if (datePicker2.getValue() != null) {
            sql.append(" AND \"Date\" <= ?");
        }

        if (driverSelector.getValue() != null) {
            sql.append(" AND \"Driver\" = ?");
        }

        if (materialSelector.getValue() != null) {
            sql.append(" AND \"Material\" = ?");
        }

        if (objectSelector.getValue() != null) {
            sql.append(" AND \"Destination\" = ?");
        }

        sql.append(" ORDER BY \"Date\" ASC");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(String.valueOf(sql));
             ) {

            int paramIndex = 1;

            if (datePicker1.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker1.getValue()));
            }

            if (datePicker2.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker2.getValue()));
            }

            if (driverSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, driverSelector.getValue());
            }

            if (materialSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, materialSelector.getValue());
            }

            if (objectSelector.getValue() != null) {
                preparedStatement.setString(paramIndex, objectSelector.getValue());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String state_num = resultSet.getString("State_num");
                String driver_name = resultSet.getString("Driver");
                int mills = resultSet.getInt("Mileage");
                short fuel = resultSet.getShort("Fuel");
                Diesel diesel = new Diesel(id, date, state_num, driver_name, mills, fuel);
                dieselList.add(diesel);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка получения информации о расходах солярки.");
            alert.showAndWait();
        }
        return dieselList;
    }

    public static List<Material> getMaterialList(DatePicker datePicker1, DatePicker datePicker2, ChoiceBox<String> driverSelector, ChoiceBox<String> materialSelector, ChoiceBox<String> objectSelector)  {
        List<Material> materialList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("Select \"id\", \"Date\", \"Material\", \"Destination\", \"Trip_num\", \"Tons\" FROM alluser WHERE 1=1");

        if (datePicker1.getValue() != null) {
            sql.append(" AND \"Date\" >= ?");
        }

        if (datePicker2.getValue() != null) {
            sql.append(" AND \"Date\" <= ?");
        }

        if (driverSelector.getValue() != null) {
            sql.append(" AND \"Driver\" = ?");
        }

        if (materialSelector.getValue() != null) {
            sql.append(" AND \"Material\" = ?");
        }

        if (objectSelector.getValue() != null) {
            sql.append(" AND \"Destination\" = ?");
        }

        sql.append(" ORDER BY \"Date\" ASC");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(String.valueOf(sql));
             ) {

            int paramIndex = 1;

            if (datePicker1.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker1.getValue()));
            }

            if (datePicker2.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker2.getValue()));
            }

            if (driverSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, driverSelector.getValue());
            }

            if (materialSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, materialSelector.getValue());
            }

            if (objectSelector.getValue() != null) {
                preparedStatement.setString(paramIndex, objectSelector.getValue());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String material_name = resultSet.getString("Material");
                String object_name = resultSet.getString("Destination");
                short trip_num = resultSet.getShort("Trip_num");
                short tons = resultSet.getShort("Tons");
                Material material = new Material(id, date, material_name, object_name, trip_num, tons);
                materialList.add(material);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка получения списка материалов.");
            alert.showAndWait();
        }
        return materialList;
    }

    public static List<Objects> getObjectsList(DatePicker datePicker1, DatePicker datePicker2, ChoiceBox<String> driverSelector, ChoiceBox<String> materialSelector, ChoiceBox<String> objectSelector) {
        List<Objects> objectList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("Select \"id\", \"Date\", \"Material\", \"Destination\", \"Tons\" FROM alluser WHERE 1=1");

        if (datePicker1.getValue() != null) {
            sql.append(" AND \"Date\" >= ?");
        }

        if (datePicker2.getValue() != null) {
            sql.append(" AND \"Date\" <= ?");
        }

        if (driverSelector.getValue() != null) {
            sql.append(" AND \"Driver\" = ?");
        }

        if (materialSelector.getValue() != null) {
            sql.append(" AND \"Material\" = ?");
        }

        if (objectSelector.getValue() != null) {
            sql.append(" AND \"Destination\" = ?");
        }

        sql.append(" ORDER BY \"Date\" ASC");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(String.valueOf(sql));
             ) {

            int paramIndex = 1;

            if (datePicker1.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker1.getValue()));
            }

            if (datePicker2.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker2.getValue()));
            }

            if (driverSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, driverSelector.getValue());
            }

            if (materialSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, materialSelector.getValue());
            }

            if (objectSelector.getValue() != null) {
                preparedStatement.setString(paramIndex, objectSelector.getValue());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                int id = resultSet.getInt("id");
                String material_name = resultSet.getString("Material");
                String object_name = resultSet.getString("Destination");

                short tons = resultSet.getShort("Tons");
                Objects object = new Objects(id, date, object_name, material_name, tons);
                objectList.add(object);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка получения списка объектов.");
            alert.showAndWait();
        }

        return objectList;
    }

    public static List<Columns> getAllColumns(DatePicker datePicker1, DatePicker datePicker2, ChoiceBox<String> driverSelector, ChoiceBox<String> materialSelector, ChoiceBox<String> objectSelector) {
        ObservableList<Columns> columns = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("Select * FROM alluser WHERE 1=1");

        if (datePicker1.getValue() != null) {
            sql.append(" AND \"Date\" >= ?");
        }

        if (datePicker2.getValue() != null) {
            sql.append(" AND \"Date\" <= ?");
        }

        if (driverSelector.getValue() != null) {
            sql.append(" AND \"Driver\" = ?");
        }

        if (materialSelector.getValue() != null) {
            sql.append(" AND \"Material\" = ?");
        }

        if (objectSelector.getValue() != null) {
            sql.append(" AND \"Destination\" = ?");
        }

        sql.append(" ORDER BY \"Date\" ASC");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(String.valueOf(sql));
        ) {

            int paramIndex = 1;

            if (datePicker1.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker1.getValue()));
            }

            if (datePicker2.getValue() != null) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(datePicker2.getValue()));
            }

            if (driverSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, driverSelector.getValue());
            }

            if (materialSelector.getValue() != null) {
                preparedStatement.setString(paramIndex++, materialSelector.getValue());
            }

            if (objectSelector.getValue() != null) {
                preparedStatement.setString(paramIndex, objectSelector.getValue());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String state_num = resultSet.getString("State_num");
                String driver = resultSet.getString("Driver");
                String customer = resultSet.getString("Customer");
                String from = resultSet.getString("From");
                String destination = resultSet.getString("Destination");
                LocalTime shift_time_start = resultSet.getTime("shift_time_start").toLocalTime();
                LocalTime shift_time_ending = resultSet.getTime("shift_time_ending").toLocalTime();
                Short fuel = resultSet.getShort("Fuel");
                int mileage = resultSet.getInt("Mileage");
                float hours_num = resultSet.getFloat("hours_cnt");
                Short trip_num = resultSet.getShort("Trip_num");
                String material_name = resultSet.getString("Material");
                Short tons = resultSet.getShort("Tons");
                Columns column = new Columns(id, date, state_num, driver, customer, from,
                        destination, shift_time_start, shift_time_ending, fuel, mileage,
                        hours_num, trip_num, material_name, tons);
                columns.add(column);
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка получения списка всех данных.");
            alert.showAndWait();
        }
        return columns;
    }

    public static ObservableList<String> getObjectsDB() {
        ObservableList<String> objectsList = FXCollections.observableArrayList();
        String sql = "Select DISTINCT \"Destination\" FROM alluser";
        try (Connection connection2 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection2.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String object_name = resultSet.getString("Destination");
                objectsList.add(object_name);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка заполнения списка объектов для фильтра.");
            alert.showAndWait();
        }
        return objectsList;
    }

    public static ObservableList<String> getDriversDB() {
        ObservableList<String> driversList = FXCollections.observableArrayList();
        String sql = "Select DISTINCT \"Driver\" FROM alluser";
        try (Connection connection3 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection3.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String driver_name = resultSet.getString("Driver");
                driversList.add(driver_name);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка заполнения списка водителей для фильтра.");
            alert.showAndWait();
        }
        return driversList;
    }

    public static ObservableList<String> getMaterialsDB() {
        ObservableList<String> materialsList = FXCollections.observableArrayList();
        String sql = "Select DISTINCT \"Material\" FROM alluser";
        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection1.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
            String material_name = resultSet.getString("Material");
            materialsList.add(material_name);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка заполнения списка материалов для фильтра.");
            alert.showAndWait();
        }
        return materialsList;
    }

    public static void deleteFromDB(int id) {
        String sql = "DELETE FROM alluser WHERE id = ?";
        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection1.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка удаления строки.");
            alert.showAndWait();
        }
    }
}
