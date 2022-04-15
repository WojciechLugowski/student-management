package com.example.studentmanagement.Model.Student;


import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "classes")
public class Class implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer classId;

    @Column(name = "name")
    private String className;
    @Column(name="max_students")
    private int maxStudent;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "classes_students",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> studentList = new ArrayList<>();

    public Class() {
    }

    public Class(String className, int maxStudent) {
        this.className = className;
        this.maxStudent = maxStudent;
    }
    public boolean isEnrolled(Student s){
        if(s.getStudentId() == null){
            if(studentList.contains(s))
                return true;
        } else{
            for (Student student : studentList) {
                if(Objects.equals(student.getStudentId(), s.getStudentId())){
                    return true;
                }
            }
        }
        return false;
    }
    public void addStudent(Student s, StudentCondition studentCondition){
        if(!isEnrolled(s)){
            studentList.add(s);
            s.enrollClass(this, studentCondition);
        }else{
            System.out.println("Student is already in group");
        }
    }

    @PreRemove
    public void removeStudentsFromGroup(){
        for (Student student : studentList) {
            student.removeClass(this);
        }
        studentList.removeAll(studentList);
    }

    public void addMark(Student s, double mark) {
        if(isEnrolled(s)){
            s.addMark(this, mark);
        } else{
            System.out.println("Student is not in this group");
        }
    }
    public StudentCondition getStudentCondition(Student s){
        return s.getStudentCondition(this);
    }
    public void changeStudentCondition(Student s, StudentCondition studentCondition){
        if(isEnrolled(s)){
            s.changeStudentCondition(this, studentCondition);
        } else{
            System.out.println("Student is not in this group");
        }
    }
    public Double getClassFillPercentage(){
        double studentCount = studentList.size();
        return (studentCount/maxStudent)*100;
    }

    public int getStudentIndex(Student s){
        return studentList.indexOf(s);
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getMaxStudent() {
        return maxStudent;
    }

    public void setMaxStudent(int maxStudent) {
        this.maxStudent = maxStudent;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}

