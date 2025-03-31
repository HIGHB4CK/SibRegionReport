package com.example.sibregionreport.services;

import com.example.sibregionreport.models.Diesel;
import com.example.sibregionreport.models.Driver;
import com.example.sibregionreport.models.Material;
import com.example.sibregionreport.models.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DAOLoader {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sibregion";
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

    public void saveData(LocalDate date, String state_num, String driver, String customer, String from, String destination, Short fuel, int milleage, short trip_num, String material, short tons, int hours_cnt, LocalTime shift_time_start, LocalTime shift_time_ending) {
        String sql = "INSERT INTO alluser (\"Date\", \"State_num\", \"Driver\", \"Customer\", \"From\", \"Destination\", \"Fuel\", \"Mileage\", \"Trip_num\", \"Material\", \"Tons\", \"hours_cnt\", \"shift_time_start\", \"shift_time_ending\") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(date)); // Assuming datePicker.getValue() returns LocalDate
            preparedStatement.setString(2, state_num);
            preparedStatement.setString(3, driver);
            preparedStatement.setString(4, customer);
            preparedStatement.setString(5, from);
            preparedStatement.setString(6, destination);
            preparedStatement.setShort(7, fuel);
            preparedStatement.setInt(8, milleage);
            preparedStatement.setShort(9, trip_num);
            preparedStatement.setString(10, material);
            preparedStatement.setShort(11, tons);
            preparedStatement.setInt(12, hours_cnt);
            preparedStatement.setTime(13, Time.valueOf(shift_time_start));
            preparedStatement.setTime(14, Time.valueOf(shift_time_ending));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Driver> getDriverList() {
        List<Driver> driverList = new ArrayList<>();
        String sql = "Select \"Date\", \"Driver\", \"Mileage\", \"Trip_num\", \"hours_cnt\", \"Fuel\" FROM alluser ORDER BY \"Date\" ASC";

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String name = resultSet.getString("Driver");
                int mills = resultSet.getInt("Mileage");
                short trip_num = resultSet.getShort("Trip_num");
                short hours_cnt = resultSet.getShort("hours_cnt");
                short fuel = resultSet.getShort("Fuel");
                Driver driver = new Driver(date, name, mills, trip_num, hours_cnt, fuel);
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

    public static List<Diesel> getDieselList() {
        List<Diesel> dieselList = new ArrayList<>();
        String sql = "Select \"Date\", \"State_num\", \"Driver\", \"Mileage\", \"Fuel\" FROM alluser ORDER BY \"Date\" ";

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String state_num = resultSet.getString("State_num");
                String driver_name = resultSet.getString("Driver");
                int mills = resultSet.getInt("Mileage");
                short fuel = resultSet.getShort("Fuel");
                Diesel diesel = new Diesel(date, state_num, driver_name, mills, fuel);
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

    public static List<Material> getMaterialList()  {
        List<Material> materialList = new ArrayList<>();
        String sql = "Select \"Date\", \"Material\", \"Destination\", \"Trip_num\", \"Tons\" FROM alluser ORDER BY \"Date\" ASC";

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String material_name = resultSet.getString("Material");
                String object_name = resultSet.getString("Destination");
                short trip_num = resultSet.getShort("Trip_num");
                short tons = resultSet.getShort("Tons");
                Material material = new Material(date, material_name, object_name, trip_num, tons);
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

    public static List<Objects> getObjectsList() {
        List<Objects> objectList = new ArrayList<>();
        String sql = "Select \"Date\", \"Material\", \"Destination\", \"Tons\" FROM alluser ORDER BY \"Date\" ASC";

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String material_name = resultSet.getString("Material");
                String object_name = resultSet.getString("Destination");

                short tons = resultSet.getShort("Tons");
                Objects object = new Objects(date, object_name, material_name, tons);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return materialsList;
    }
}
