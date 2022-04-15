package com.example.studentmanagement.Model.Student;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="marks")
public class Marks implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer marksId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="class_id", nullable=true)
    private Class classMember;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="markstable_marks")
    @JoinColumn(name="marks")
    private List<Double> marks = new ArrayList<Double>();


    public Marks() {
    }

    public Marks(Class classMember) {
        this.classMember = classMember;
    }

    public void setMark(double mark){
        if(mark>=3 && mark<=5)
            marks.add(mark);
    }

    public List<Double> getMarks() {
        return marks;
    }
    public Double getAvg(){
        if(marks.isEmpty()){
            return null;
        }
        double sum=0;
        for (Double mark : marks) {
            sum+=mark;
        }
        return sum/marks.size();
    }
    public void removeMark(){
        classMember=null;
        marks.removeAll(marks);
    }

    public Class getClassMember() {
        return classMember;
    }

    public void setClassMember(Class classMember) {
        this.classMember = classMember;
    }

    public Integer getMarksId() {
        return marksId;
    }

    public void setMarksId(Integer marksId) {
        this.marksId = marksId;
    }
}