package com.example.studentmanagement.controllers;

import com.example.studentmanagement.StudentManagementApp;
import com.example.studentmanagement.Model.DataClass;
import com.example.studentmanagement.Model.Student.User;
import com.example.studentmanagement.Model.Student.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private DataClass dataClass = DataClass.getInstance();

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfLogin;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void setBtnLogin(ActionEvent event){
        boolean trueCredentials = false;
        if(tfLogin.getText()!=null && pfPassword.getText()!=null && tfLogin.getText().length()>0 &&pfPassword.getText().length()>0){
            for (User user : dataClass.getUsers()){
                if(user.getUsername().equals(tfLogin.getText()) && user.getPassword().equals(pfPassword.getText())){
                    try {
                        trueCredentials = true;
                        startNewScene(user, event);
                    } catch (Exception e){
                        System.err.println(e);
                    }
                }
            }
        }
        if(!trueCredentials){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Podano nieprawidłowe dane logowania");
            alert.show();
        }

    }
    private void startNewScene(User user, ActionEvent event) throws IOException {
        Parent root;
        FXMLLoader fxmlLoader;
        if(user.getUserType() == UserType.ADMIN){
            fxmlLoader = new FXMLLoader(StudentManagementApp.class.getResource("admin-view.fxml"));
            root = fxmlLoader.load();
        } else{
            fxmlLoader = new FXMLLoader(StudentManagementApp.class.getResource("user-view.fxml"));
            root = fxmlLoader.load();
            UserController userController = fxmlLoader.getController();
            userController.initializeUser(user);
        }
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
