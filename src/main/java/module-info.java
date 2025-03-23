module com.example.sibregionreport {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires static lombok;


    opens com.example.sibregionreport to javafx.fxml;
    exports com.example.sibregionreport.controllers;
    opens com.example.sibregionreport.controllers to javafx.fxml;
    exports com.example.sibregionreport;
    opens com.example.sibregionreport.CommonClasses to javafx.fxml;
}