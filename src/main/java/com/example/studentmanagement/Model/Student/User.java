package com.example.studentmanagement.Model.Student;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int userId;
    private String username;
    private String password;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "student_id")
    private Student student;
    @Column(name="user_type")
    private UserType userType;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.userType = UserType.ADMIN;
    }

    public User(String username, String password, Student student) {
        this.username = username;
        this.password = password;
        this.student = student;
        this.userType = UserType.USER;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
