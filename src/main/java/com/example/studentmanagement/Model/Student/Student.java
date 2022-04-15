package com.example.studentmanagement.Model.Student;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer studentId;

    private String forename;
    private String surname;
    @Column(name="date_of_birth")
    private int dateOfBirth;

    @ManyToMany(mappedBy = "studentList")
    private List<Class> classList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "marks_id")
    private List<Marks> marksList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @CollectionTable(name = "student_conditions")
    @JoinColumn(name = "student_conditions")
    private List<StudentConditionMap> studentConditions = new ArrayList<>();

    public Student() {
    }

    public Student(String forename, String surname, int dateOfBirth) {
        this.forename = forename;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }
    public void enrollClass(Class aClass, StudentCondition studentCondition){
        if(!isEnrolled(aClass)){
            classList.add(aClass);
            marksList.add(new Marks(aClass));
            studentConditions.add(new StudentConditionMap(aClass, studentCondition));
        }
    }
    public boolean isEnrolled(Class aClass){
        if(aClass.getClassId() == null){
            if(classList.contains(aClass))
                return true;
        } else{
            for (Class aClass1 : classList) {
                if(aClass1.getClassId() == aClass.getClassId())
                    return true;
            }
        }
        return false;
    }
    public void removeClass(Class aClass){
        Integer index = getClassIndex(aClass);
        if(index>=0){
            Marks mark = getMarks(aClass);
            if(mark!=null) {
                //mark.removeMark();
                marksList.remove(mark);
            }
            classList.remove(index);
        }
    }

    public Double getStudentAvg(){
        List<Double> avgMarkList = getStudentAvgMarks();
        if(!avgMarkList.isEmpty()){
            double sum =0;
            for (Double aDouble : avgMarkList) {
                sum+=aDouble;
            }
            return sum/avgMarkList.size();
        }
        return null;
    }
    public List<Double> getStudentAvgMarks(){
        List<Double> avgMarks = new ArrayList<>();
        for (Marks marks : marksList) {
            Double avgMark = marks.getAvg();
            if(avgMark!=null){
                avgMarks.add(avgMark);
            }
        }
        return avgMarks;
    }
    public Marks getMarks(Class aClass){
        for (Marks marks : marksList) {
            if(aClass.getClassId()!=null){
                if(marks.getClassMember().getClassId() == aClass.getClassId())
                    return marks;
            } else if(marks.getClassMember().equals(aClass)){
                return marks;
            }
        }
        return null;
    }
    public List<Marks> getMarksList(){
        return marksList;
    }
    public void addMark(Class aClass, double mark){
        Marks marks = getMarks(aClass);
        if(marks!=null){
            marks.setMark(mark);
        } else{
            throw new IllegalArgumentException("Student is not enrolled in that class");
        }
    }
    public void changeStudentCondition(Class aClass, StudentCondition studentCondition){
        for (StudentConditionMap condition : studentConditions) {
            if(condition.getStudentClass().equals(aClass)){
                condition.setStudentCondition(studentCondition);
                break;
            }
        }
    }
    public StudentCondition getStudentCondition(Class aClass){
        for (StudentConditionMap condition : studentConditions) {
            if(condition.getStudentClass().equals(aClass)){
                return condition.getStudentCondition();
            }
        }
        return null;
    }
    public int getClassIndex(Class aClass){
        if(classList.contains(aClass)){
            return classList.indexOf(aClass);
        }
        if(aClass.getClassId()!=null){
            for (int i =0; i<classList.size(); i++) {
                if(classList.get(i).getClassId() == aClass.getClassId())
                    return i;
            }
        }
        return -1;
    }
    public int getClassIndexMarks(Class aClass){
        if(marksList.contains(aClass)){
            return marksList.indexOf(aClass);
        }
        if(aClass.getClassId()!=null){
            for (int i =0; i<marksList.size(); i++) {
                if(marksList.get(i).getClassMember().getClassId() == aClass.getClassId())
                    return i;
            }
        }
        return -1;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(int dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

}