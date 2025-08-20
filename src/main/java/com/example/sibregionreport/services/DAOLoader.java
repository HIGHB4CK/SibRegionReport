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
import javafx.scene.control.Label;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class DAOLoader {

    private static final String DB_URL = "jdbc:postgresql://192.168.154.197:5432/SibRegionTalons";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Guseynov2005";

    private static Connection connection;

    @FXML
    Label info = new Label();

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void init() throws SQLException {
        connection = connect();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void saveData(LocalDate date, String state_num, String driver, String customer, String from,
                         String destination, Float fuel, Integer milleage, Short trip_num, String material,
                         Float tons, Float hours_cnt, LocalTime shift_time_start, LocalTime shift_time_ending) {
        String sql = "INSERT INTO alldata (\"Date\", \"State_num\", \"Driver\", \"Customer\", \"From\", " +
                "\"Destination\", \"Fuel\", \"Mileage\", \"Trip_num\", \"Material\", \"Tons\", \"hours_cnt\", " +
                "\"shift_time_start\", \"shift_time_ending\") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            java.sql.Time start_time = null;
            if (shift_time_start != null) {
                start_time = Time.valueOf(shift_time_start);
            }

            java.sql.Time ending_time = null;
            if (shift_time_ending != null) {
                ending_time = Time.valueOf(shift_time_ending);
            }

            if (date != null) {
                // Assuming datePicker.getValue() returns LocalDate
                preparedStatement.setDate(1, java.sql.Date.valueOf(date));
            }

            preparedStatement.setString(2, state_num);
            preparedStatement.setString(3, driver);
            preparedStatement.setString(4, customer);
            preparedStatement.setString(5, from);
            preparedStatement.setString(6, destination);
            preparedStatement.setObject(7, fuel, Types.FLOAT);
            preparedStatement.setObject(8, milleage, Types.INTEGER);
            preparedStatement.setObject(9, trip_num, Types.SMALLINT);
            preparedStatement.setString(10, material);
            preparedStatement.setObject(11, tons, Types.FLOAT);
            preparedStatement.setObject(12, hours_cnt, Types.FLOAT);
            preparedStatement.setTime(13, start_time);
            preparedStatement.setTime(14, ending_time);

            preparedStatement.executeUpdate();

            info.setText("Успешное сохранение");

            showMessageDialog(null, "Талон успешно сохранен в базе данных.");

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка сохранения данных.");
            alert.showAndWait();
        }
    }

    private static String getSQL(StringBuilder sql, DatePicker datePicker1, DatePicker datePicker2, ChoiceBox<String> driverSelector, ChoiceBox<String> materialSelector, ChoiceBox<String> objectSelector) {
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
        return String.valueOf(sql);
    }

    private static void setPreparedStatement(PreparedStatement preparedStatement, DatePicker datePicker1, DatePicker datePicker2, ChoiceBox<String> driverSelector, ChoiceBox<String> materialSelector, ChoiceBox<String> objectSelector) throws SQLException {
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
    };


    public static List<Driver> getDriverList(DatePicker datePicker1, DatePicker datePicker2, ChoiceBox<String> driverSelector, ChoiceBox<String> materialSelector, ChoiceBox<String> objectSelector) {
        List<Driver> driverList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("Select \"id\", \"Date\", \"Driver\", \"Mileage\", \"Trip_num\", \"hours_cnt\", \"Fuel\" FROM alldata WHERE 1=1");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(getSQL(sql, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector));
             ) {

            setPreparedStatement(preparedStatement, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector);

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
        StringBuilder sql = new StringBuilder("Select \"id\", \"Date\", \"State_num\", \"Driver\", \"Mileage\", \"Fuel\" FROM alldata WHERE 1=1");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(getSQL(sql, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector));
             ) {

            setPreparedStatement(preparedStatement, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector);

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
        StringBuilder sql = new StringBuilder("Select \"id\", \"Date\", \"Material\", \"Destination\", \"Trip_num\", \"Tons\" FROM alldata WHERE 1=1");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(getSQL(sql, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector));
             ) {

            setPreparedStatement(preparedStatement, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector);

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
        StringBuilder sql = new StringBuilder("Select \"id\", \"Date\", \"Material\", \"Destination\", \"Tons\" FROM alldata WHERE 1=1");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(getSQL(sql, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector));
             ) {

            setPreparedStatement(preparedStatement, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector);

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
        StringBuilder sql = new StringBuilder("Select * FROM alldata WHERE 1=1");

        try (Connection connection1 = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection1.prepareStatement(getSQL(sql, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector));
        ) {

            setPreparedStatement(preparedStatement, datePicker1, datePicker2, driverSelector, materialSelector, objectSelector);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                LocalTime start_time = null;
                LocalTime ending_time = null;

                int id = resultSet.getInt("id");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String state_num = resultSet.getString("State_num");
                String driver = resultSet.getString("Driver");
                String customer = resultSet.getString("Customer");
                String from = resultSet.getString("From");
                String destination = resultSet.getString("Destination");
                Time shift_time_start = resultSet.getTime("shift_time_start");
                if (shift_time_start != null) {
                    start_time = shift_time_start.toLocalTime();
                }
                Time shift_time_ending = resultSet.getTime("shift_time_ending");
                if (shift_time_ending != null) {
                    ending_time = shift_time_ending.toLocalTime();
                }
                Short fuel = resultSet.getShort("Fuel");
                int mileage = resultSet.getInt("Mileage");
                float hours_num = resultSet.getFloat("hours_cnt");
                Short trip_num = resultSet.getShort("Trip_num");
                String material_name = resultSet.getString("Material");
                Short tons = resultSet.getShort("Tons");
                Columns column = new Columns(id, date, state_num, driver, customer, from,
                        destination, start_time, ending_time, fuel, mileage,
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
        String sql = "Select DISTINCT \"Destination\" FROM alldata";
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
        String sql = "Select DISTINCT \"Driver\" FROM alldata";
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
        String sql = "Select DISTINCT \"Material\" FROM alldata";
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
        String sql = "DELETE FROM alldata WHERE id = ?";
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

    public static void updateField(int id, String columnName, String newValue) throws SQLException {
        String sql = "UPDATE alldata SET \"" + columnName + "\" = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Определяем тип данных и преобразуем значение
            if (columnName.equals("id") || columnName.equals("Mileage") ||
                    columnName.equals("Trip_num") || columnName.equals("hours_cnt") ||
                    columnName.equals("Fuel") || columnName.equals("Tons")) {

                // Числовые поля
                try {
                    if (columnName.equals("Fuel") || columnName.equals("Tons") ||
                            columnName.equals("hours_cnt")) {
                        pstmt.setFloat(1, Float.parseFloat(newValue));
                    } else {
                        pstmt.setInt(1, Integer.parseInt(newValue));
                    }
                } catch (NumberFormatException e) {
                    throw new SQLException("Некорректное числовое значение для поля " + columnName);
                }

            } else if (columnName.equals("Date")) {
                // Дата
                try {
                    pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.parse(newValue)));
                } catch (Exception e) {
                    throw new SQLException("Некорректный формат даты");
                }

            } else if (columnName.equals("shift_time_start") || columnName.equals("shift_time_ending")) {
                // Время
                try {
                    pstmt.setTime(1, java.sql.Time.valueOf(LocalTime.parse(newValue)));
                } catch (Exception e) {
                    throw new SQLException("Некорректный формат времени");
                }

            } else {
                // Текстовые поля
                pstmt.setString(1, newValue);
            }

            pstmt.setInt(2, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Запись с id=" + id + " не найдена");
            }
        }
    }

    public static void updateDriverField(int id, String fieldName, String value) throws SQLException {
        updateField(id, mapFieldNameToColumn(fieldName), value);
    }

    public static void updateDieselField(int id, String fieldName, String value) throws SQLException {
        updateField(id, mapFieldNameToColumn(fieldName), value);
    }

    public static void updateMaterialField(int id, String fieldName, String value) throws SQLException {
        updateField(id, mapFieldNameToColumn(fieldName), value);
    }

    public static void updateObjectField(int id, String fieldName, String value) throws SQLException {
        updateField(id, mapFieldNameToColumn(fieldName), value);
    }

    public static void updateAllColumnsField(int id, String fieldName, String value) throws SQLException {
        updateField(id, mapFieldNameToColumn(fieldName), value);
    }

    // Вспомогательный метод для преобразования имен полей в названия колонок БД
    private static String mapFieldNameToColumn(String fieldName) {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("id", "id");
        fieldMap.put("Дата", "Date");
        fieldMap.put("Имя", "Driver");
        fieldMap.put("Пробег", "Mileage");
        fieldMap.put("Количество рейсов", "Trip_num");
        fieldMap.put("Количество часов", "hours_cnt");
        fieldMap.put("Израсходовано топлива", "Fuel");
        fieldMap.put("Гос номер", "State_num");
        fieldMap.put("Материал", "Material");
        fieldMap.put("Объект", "Destination");
        fieldMap.put("Тонаж", "Tons");
        fieldMap.put("Тон", "Tons");
        fieldMap.put("Водитель", "Driver");
        fieldMap.put("Закачик", "Customer");
        fieldMap.put("Время начала", "shift_time_start");
        fieldMap.put("Время конца", "shift_time_ending");
        fieldMap.put("Откуда", "From");
        fieldMap.put("Куда", "Destination");
        fieldMap.put("Расход топлива", "Fuel");
        fieldMap.put("Километраж", "Mileage");
        fieldMap.put("Количество часов", "hours_cnt");
        fieldMap.put("Количество рейсов", "Trip_num");
        fieldMap.put("Материал", "Material");

        return fieldMap.getOrDefault(fieldName, fieldName);
    }
}
