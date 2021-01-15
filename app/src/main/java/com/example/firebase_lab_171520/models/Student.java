package com.example.firebase_lab_171520.models;

public class Student {
    private String userId;
    private String index;
    private String name;
    private String surname;
    private String phone;

    public Student() {
    }

    private String address;

    public Student(String userId, String index, String name, String surname, String phone, String address) {
        this.userId = userId;
        this.index = index;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
