package com.example.studentmanagement;

import com.example.studentmanagement.Model.DataClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;

public class StudentManagementApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DataClass dataClass = DataClass.getInstance();

        dataClass.initializeDefaultData();
        dataClass.saveDataToDB();
        try{
            //dataClass.initializeDataFromDB();
            FXMLLoader fxmlLoader = new FXMLLoader(StudentManagementApp.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Dziekanat");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Blad polaczenia z baza danych");
            alert.show();
        }

    }
    public static void main(String[] args) {
        launch();
    }
}