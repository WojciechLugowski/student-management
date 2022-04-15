module com.example.okienkajavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires mysql.connector.java;
    requires java.sql;
    requires java.persistence;
    requires java.naming;

    opens com.example.studentmanagement to javafx.fxml, org.hibernate.orm.core;
    opens com.example.studentmanagement.Model to javafx.fxml, org.hibernate.orm.core;
    opens com.example.studentmanagement.Model.Student to javafx.fxml, org.hibernate.orm.core;


    exports com.example.studentmanagement.Model.Student;
    exports com.example.studentmanagement.Model;
    exports com.example.studentmanagement;
    exports com.example.studentmanagement.controllers;
    opens com.example.studentmanagement.controllers to javafx.fxml, org.hibernate.orm.core;


}