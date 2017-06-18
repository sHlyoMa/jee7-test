package com.enay.jee.test.model;

import javax.persistence.*;

@Entity
@NamedQueries(
        @NamedQuery(name = "User.findAll", query = "select u from User u")
)
public class User {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
