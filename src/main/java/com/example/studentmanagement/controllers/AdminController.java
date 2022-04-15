package com.example.studentmanagement.controllers;

import com.example.studentmanagement.Model.DataClass;
import com.example.studentmanagement.Model.Student.Class;
import com.example.studentmanagement.Model.Student.Student;
import com.example.studentmanagement.Model.Student.StudentCondition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.FileWriter;
import java.net.URL;
import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;

public class AdminController implements Initializable {
    private DataClass dataClass = DataClass.getInstance();

    @FXML
    private Button btnAddMark;

    @FXML
    private Button btnChangeState;

    @FXML
    private Button btnExportCsv;

    @FXML
    private TableColumn<Student, String> colAvg;

    @FXML
    private TableColumn<Student, String> colBirthDate;

    @FXML
    private TableColumn<Student, String> colForname;

    @FXML
    private TableColumn<Student, String> colState;

    @FXML
    private TableColumn<Student, String> colSurname;

    @FXML
    private ComboBox<String> comboClass;

    @FXML
    private ComboBox<StudentCondition> comboState;

    @FXML
    private TableView<Student> tvStudent;

    @FXML
    private TextField tfMark;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializeCombos();
    }

    public void initializeCombos(){
        List<String> classNames = dataClass.getClassesNames();
        ObservableList<String> classObservableList = observableArrayList(dataClass.getClassesNames());
        comboClass.setItems(classObservableList);
        ObservableList<StudentCondition>  studentConditionObservableList = observableArrayList(Arrays.asList(StudentCondition.values()));
        comboState.setItems(studentConditionObservableList);
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
            }
        }
    }
    public void showScene(Class aClass){
        if(aClass==null){
            btnAddMark.setDisable(true);
            btnChangeState.setDisable(true);
            showStudentsTable(null);
        }else{
            btnAddMark.setDisable(false);
            btnChangeState.setDisable(false);
            showStudentsTable(aClass);
        }
    }

    public void setBtnAddMark(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Student student = tvStudent.getSelectionModel().getSelectedItem();
        String className = comboClass.getSelectionModel().getSelectedItem();
        if(student!=null && !className.equals("dowolny")){
            if(tfMark.getText()!=null && tfMark.getText().length()>0){
                double mark = Double.parseDouble(tfMark.getText());
                for (Class aClass : dataClass.getClasses()) {
                    if(aClass.getClassName().equals(className)){
                        aClass.addMark(student, mark);
                        tvStudent.refresh();
                        break;
                    }
                }
            } else{
                alert.setContentText("Nie wpisano oceny");
                alert.show();
            }
        } else{
            alert.setContentText("Nie wybrano studenta");
            alert.show();
        }
    }
    public void setBtnChangeState(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Student student = tvStudent.getSelectionModel().getSelectedItem();
        if(student!=null){
            StudentCondition studentCondition = comboState.getSelectionModel().getSelectedItem();
            if(studentCondition!=null){
                for (Class aClass : dataClass.getClasses()) {
                    if(comboClass.getValue().equals(aClass.getClassName())){
                        aClass.changeStudentCondition(student,studentCondition);
                        tvStudent.refresh();
                        break;
                    }
                }
            } else{
                alert.setContentText("Nie wybrano stanu");
                alert.show();
            }
        } else {
            alert.setContentText("Nie wybrano studenta");
            alert.show();
        }
    }
    public void showStudentsTable(Class aClass){
        ObservableList<Student> list;

        colForname.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getForename()));
        colSurname.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSurname()));
        colBirthDate.setCellValueFactory(p -> new SimpleStringProperty(Integer.toString(p.getValue().getDateOfBirth())));
        if(aClass==null){
            list = observableArrayList(dataClass.getStudents());
            colAvg.setCellValueFactory(p -> {
                if(!p.getValue().getMarksList().isEmpty())
                    return new SimpleStringProperty(p.getValue().getStudentAvg().toString());
                return new SimpleStringProperty();
            });
            colState.setVisible(false);
        }else{
            list = observableArrayList(aClass.getStudentList());
            colAvg.setCellValueFactory(p -> {
                if(p.getValue().getMarks(aClass)!=null)
                    return new SimpleStringProperty(p.getValue().getMarks(aClass).getAvg().toString());
                return new SimpleStringProperty();
            });
            colState.setVisible(true);
            colState.setCellValueFactory(p-> {
                if (p.getValue().getStudentCondition(aClass)!=null)
                    return new SimpleStringProperty(p.getValue().getStudentCondition(aClass).toString());
                return new SimpleStringProperty();
            });
        }
        tvStudent.setItems(list);

    }

   public void exportTableCsv() {
        List<Student> studentList = null;
        Alert alertInform = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        if(comboClass.getValue().equals(dataClass.defaultName)){
            studentList = dataClass.students;
        }
        for (Class aClass : dataClass.classes) {
            if(comboClass.getValue().equals(aClass.getClassName())){
                studentList = aClass.getStudentList();
                break;
            }
        }
        if(studentList!=null && !studentList.isEmpty()){
            try{
                String fileName = "adminTableData.csv";
                FileWriter fileWriter = new FileWriter(fileName);
                if(!comboClass.getValue().equals(dataClass.defaultName))
                    fileWriter.write(comboClass.getValue()+"\n");
                StringBuilder columnNameBuilder = new StringBuilder();
                columnNameBuilder.append("forename"+",");
                columnNameBuilder.append("surname"+",");
                columnNameBuilder.append("dateOfBirth"+",");
                columnNameBuilder.append("average\n");
                fileWriter.write(columnNameBuilder.toString());
                for (Student student : studentList) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(student.getForename()+',');
                    stringBuilder.append(student.getSurname()+',');
                    stringBuilder.append(student.getDateOfBirth()+",");
                    stringBuilder.append(student.getStudentAvg()+"\n");
                    fileWriter.write(stringBuilder.toString());
                }
                alertInform.setContentText("Zapisano do pliku "+fileName);
                alertInform.show();
                fileWriter.close();
            } catch (Exception e){
                alertError.setContentText("Nie można utworzyć pliku");
                alertError.show();
                System.out.println(e);
            }
        } else{
            alertError.setContentText("Tabela jest pusta");
            alertError.show();
        }
    }

}
