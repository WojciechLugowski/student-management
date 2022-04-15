package com.example.studentmanagement.controllers;

import com.example.studentmanagement.Model.DataClass;
import com.example.studentmanagement.Model.Student.*;
import com.example.studentmanagement.Model.Student.Class;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.FileWriter;
import java.net.URL;
import java.util.*;

public class UserController implements Initializable {

    private DataClass dataClass = DataClass.getInstance();
    private User user;
    private Student student;
    @FXML
    private Button btnResign;

    @FXML
    private Button btnSign;

    @FXML
    private Button btnExportCsv;

    @FXML
    private ComboBox<String> comboClass;

    @FXML
    private Label labAvgMark;

    @FXML
    private Label labName;

    @FXML
    private Label labStudentCond;

    @FXML
    private ListView<Double> lvMarks;
    @FXML
    private TableColumn<Marks, String> colClass;

    @FXML
    private TableColumn<Marks, String> colMark;
    @FXML
    private TableView<Marks> tvMarks;
    @FXML
    private CheckBox checkLowScores;

    private ListProperty<Double> listProperty = new SimpleListProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializeCombos();
    }

    public void initializeCombos(){
        ObservableList<String> classObservableList = FXCollections.observableArrayList(dataClass.getClassesNames());
        comboClass.setItems(classObservableList);
    }
    public void initializeUser(User user){
        this.user=user;
        student = user.getStudent();
        labName.setText(student.getForename()+" "+student.getSurname());
    }
    public void comboClassChange(){
        if(btnExportCsv.isDisabled()){
            btnExportCsv.setDisable(false);
        }
        if(comboClass.getValue().equals(dataClass.defaultName)){
            showScene(null);
            return;
        }
        for (Class aClass : dataClass.getClasses()) {
            if(comboClass.getValue().equals(aClass.getClassName())){
                showScene(aClass);
                break;
            }
        }
    }
    public void showScene(Class aClass){
        if(aClass==null){
            checkLowScores.setVisible(true);
            btnResign.setDisable(true);
            btnSign.setDisable(true);
            showAvgMark(student.getStudentAvg());
            showAvgMarksTable();
        }else{
            checkLowScores.setVisible(false);
            StudentCondition studentCondition = student.getStudentCondition(aClass);
            if(studentCondition==null){
                btnSign.setDisable(false);
                btnResign.setDisable(true);
            } else if(studentCondition.equals(StudentCondition.RESIGNED)){
                btnSign.setDisable(true);
                btnResign.setDisable(true);
            }else {
                btnSign.setDisable(true);
                btnResign.setDisable(false);
            }
            if(student.getMarks(aClass)==null){
                showAvgMark(null);
            }else{
                showAvgMark(student.getMarks(aClass).getAvg());
            }
            showMarkList(aClass);
            showStudentCond(studentCondition);
        }
    }

    public void setCheckLowScores(){
        showAvgMarksTable();
    }
    public void showMarkList(Class aClass){
        tvMarks.setVisible(false);
        lvMarks.setVisible(true);
        lvMarks.getItems().clear();
        if(student.getMarks(aClass)!=null){
            List<Double> marksList = student.getMarks(aClass).getMarks();
            if(marksList!=null) {
                lvMarks.getItems().addAll(marksList);
            }
        }
    }
    public void showAvgMarksTable(){
        colClass.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Marks, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Marks, String> p) {
                return new SimpleStringProperty(p.getValue().getClassMember().getClassName());
            }
        });
        colMark.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Marks, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Marks, String> p) {
                if(p.getValue().getAvg()!=null)
                    return new SimpleStringProperty(p.getValue().getAvg().toString());
                return new SimpleStringProperty();
            }
        });
        List<Marks> marksBelowList;
        ObservableList<Marks> items;
        if(checkLowScores.isSelected()){
            marksBelowList = new ArrayList<>();
            for (Marks marks: student.getMarksList()) {
                if(marks.getAvg()<dataClass.passingMark){
                    marksBelowList.add(marks);
                }
            }
            items = FXCollections.observableArrayList(marksBelowList);
        } else{
            items = FXCollections.observableArrayList(student.getMarksList());
        }
        lvMarks.setVisible(false);
        tvMarks.setVisible(true);
        tvMarks.setItems(items);
    }

    public void showAvgMark(Double avgMark){
        if(avgMark!=null){
            labAvgMark.setVisible(true);
            labAvgMark.setText(avgMark.toString());
        }else{
            labAvgMark.setVisible(false);
        }
    }
    public void showStudentCond(StudentCondition studentCondition){
        if(studentCondition!=null)
            labStudentCond.setText(studentCondition.toString());
        else
            labStudentCond.setText("");
    }

    public void setBtnSignUp(){
        if(!comboClass.getValue().equals(dataClass.defaultName)){
            for (Class aClass : dataClass.getClasses()) {
                if(comboClass.getValue().equals(aClass.getClassName())){
                    if(student.getStudentCondition(aClass)==null){
                        aClass.addStudent(student, StudentCondition.AWAITING);
                        showStudentCond(student.getStudentCondition(aClass));
                    } else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Student jest już zapisany na ten przedmiot");
                        alert.show();
                        break;
                    }
                }
            }
        }
    }
    public void setBtnResign(){
        if(!comboClass.getValue().equals(dataClass.defaultName)) {
            for (Class aClass : dataClass.getClasses()) {
                if (comboClass.getValue().equals(aClass.getClassName())) {
                    student.changeStudentCondition(aClass, StudentCondition.RESIGNED);
                    showStudentCond(student.getStudentCondition(aClass));
                    break;
                }
            }
        }
    }
    public void exportTableCsv() {
        List<Student> studentList = null;
        String fileName = "userData.csv";
        FileWriter fileWriter;
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(student.getForename()+","+student.getSurname()+"\n");
        Alert alertInform = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        if(comboClass.getValue().equals(dataClass.defaultName)){
            if(!student.getMarksList().isEmpty()){
                stringBuilder.append("Przedmiot"+","+"Ocena"+"\n");
                for(Marks marks: student.getMarksList()) {
                    stringBuilder.append(marks.getClassMember().getClassName() + "," + marks.getAvg() + "\n");
                }
            }else {
                alertError.setContentText("Tabela jest pusta");
                alertError.show();
                return;
            }
        }else{
            Marks marks = null;
            for (Class aClass : dataClass.getClasses()) {
                if(comboClass.getValue().equals(aClass.getClassName())){
                    marks = student.getMarks(aClass);
                    break;
                }
            }
            if(marks!=null && !marks.getMarks().isEmpty()){
                stringBuilder.append(comboClass.getValue()+"\n");
                for (Double aDouble : marks.getMarks()) {
                    stringBuilder.append(aDouble+"\n");
                }
            }else {
                alertError.setContentText("Tabela jest pusta");
                alertError.show();
                return;
            }
        }
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write(stringBuilder.toString());
            alertInform.setContentText("Zapisano do pliku "+fileName);
            alertInform.show();
            fileWriter.close();
        }catch (Exception e){
            alertError.setContentText("Nie można utworzyć pliku");
            alertError.show();
            System.out.println(e);
        }
    }

}
