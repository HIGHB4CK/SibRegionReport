package com.example.sibregionreport.services;

import com.example.sibregionreport.models.Driver;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DAOLoader {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sibregion"; // Замените на свой URL
    private static final String DB_USER = "postgres"; // Замените на свое имя пользователя
    private static final String DB_PASSWORD = "Guseynov2005"; // Замените на свой пароль

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
        String sql = "INSERT INTO alluser (\"Date\", \"State_num\", \"Driver\", \"Customer\", \"From\", \"Destination\", \"Fuel\", \"Milleage\", \"Trip_num\", \"Material\", \"Tons\", \"hours_cnt\", \"shift_time_start\", \"shift_time_ending\") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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

    public List<Driver> getDrivers() {
        List<Driver> driverList = new ArrayList<>();
        String sql = "Select \"Date\", \"Driver\", \"Mileage\", \"Trip_num\", \"hours_cnt\", \"Fuel\" FROM alluser";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            LocalDate date = resultSet.getDate("Date").toLocalDate();
            String name = resultSet.getString("Driver");
            int mills = resultSet.getInt("Milleage");
            short trip_num = resultSet.getShort("Trip_num");
            short hours_cnt = resultSet.getShort("hours_cnt");
            short fuel = resultSet.getShort("Fuel");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return driverList;
    }
}
