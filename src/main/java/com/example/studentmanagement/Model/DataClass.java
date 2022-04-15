package com.example.studentmanagement.Model;

import com.example.studentmanagement.Model.Student.Class;
import com.example.studentmanagement.Model.Student.Student;
import com.example.studentmanagement.Model.Student.StudentCondition;
import com.example.studentmanagement.Model.Student.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class DataClass implements Serializable {
    private static transient DataClass instance;
    public List<Class> classes = new ArrayList<>();
    public List<Student> students = new ArrayList<>();
    public List<User> users = new ArrayList<>();
    public String defaultName = "dowolny";
    public final Double passingMark = 3.0;
    private DataClass(){
    }

    public static DataClass getInstance(){
        if(instance==null){
            instance = new DataClass();
        }
        return instance;
    }
    public static void deserializeData(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        if(instance == null){
            instance = (DataClass) inputStream.readObject();
        }
    }

    public List<Student> getStudents() {
        Session session = HibernateUtil.getSession();
        for (Student student : students) {
            session.refresh(student);
        }
        return students;
    }

    public List<User> getUsers() {
        Session session = HibernateUtil.getSession();
        for (User user : users) {
            session.refresh(user);
        }
        return users;
    }

    public List<Class> getClasses() {
        Session session = HibernateUtil.getSession();
        for (Class aClass : classes) {
            session.refresh(aClass);
        }
        return classes;
    }
    public List<String> getClassesNames(){
        getClasses();
        List<String> classNames = new ArrayList<>();
        classNames.add(defaultName);
        for (Class aClass : classes) {
            classNames.add(aClass.getClassName());
        }
        return classNames;
    }


    public void saveDataToDB(){
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
           for (User user : users) {
                session.saveOrUpdate(user);
            }
            for (Class aClass : classes) {
                session.saveOrUpdate(aClass);
            }
            for (Student student : students) {
                session.saveOrUpdate(student);
            }
            transaction.commit();
        }catch(Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public void initializeDataFromDB(){
        Session session = HibernateUtil.getSession();
        Transaction transaction=null;
        try{
            classes = session.createQuery("from Class", Class.class).list();
            students = session.createQuery("from Student", Student.class).list();
            users = session.createQuery("from User", User.class).list();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public void initializeDefaultData(){
        initializeTables();
        initializeUsers();
    }


    private void initializeTables() {
        Class class1 = new Class("Programowanie obiektowe", 15);
        Class class2 = new Class("Metody optymalizacji", 10);
        Class class3 = new Class("Progranowanie równoległe", 5);

        Student student1 = new Student("Bogusław", "Łęcina", 1980);
        Student student2 = new Student("Domino", "Jachaś", 1988);
        Student student3 = new Student("Tytus", "Bomba", 2007);
        Student student4 = new Student("JJ", "Torpeda",1999);
        Student student5 = new Student("Bartosz", "Walaszek", 1977);
        class1.addStudent(student3, StudentCondition.ACTIVE);
        class1.addStudent(student4, StudentCondition.ACTIVE);
        class1.addStudent(student1, StudentCondition.ACTIVE);

        class1.addMark(student1, 5); class1.addMark(student1, 4.5); class1.addMark(student1, 3);
        class1.addMark(student3, 2); class1.addMark(student3, 4.5); class1.addMark(student3, 4);
        class1.addMark(student4, 4); class1.addMark(student4, 4.5); class1.addMark(student4, 3.5);


        class2.addStudent(student1, StudentCondition.ABSENT);
        class2.addStudent(student2, StudentCondition.SICK);
        class2.addMark(student1, 2); class2.addMark(student1, 3); class2.addMark(student1, 3.5);
        class2.addMark(student2, 4); class2.addMark(student2, 3.5); class2.addMark(student2, 3);

        class3.addStudent(student5, StudentCondition.SUSPENDED);
        class3.addMark(student5, 2); class3.addMark(student5, 2); class3.addMark(student5, 3);


        students.add(student1); students.add(student2); students.add(student3); students.add(student4); students.add(student5);
        classes.add(class1); classes.add(class2); classes.add(class3);

    }
    private void initializeUsers(){
        User user = new User("user", "user", students.get(0));
        User admin = new User("admin", "admin");
        users = new ArrayList<User>();
        users.add(user);
        users.add(admin);
    }
}
