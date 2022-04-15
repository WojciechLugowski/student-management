package com.example.studentmanagement.Model.Student;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class StudentConditionMap implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="class_id", nullable=true)
    private Class studentClass;
    private StudentCondition studentCondition;

    public StudentConditionMap() {
    }

    public StudentConditionMap(Class studentClass, StudentCondition studentCondition) {
        this.studentClass = studentClass;
        this.studentCondition = studentCondition;
    }

    public Class getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(Class studentClass) {
        this.studentClass = studentClass;
    }

    public StudentCondition getStudentCondition() {
        return studentCondition;
    }

    public void setStudentCondition(StudentCondition studentCondition) {
        this.studentCondition = studentCondition;
    }
}
