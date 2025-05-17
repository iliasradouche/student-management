package com.student.management.models;

public class Student {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String dob;
    private String address;
    private String parentName;
    private String parentPhone;
    private String profileImagePath;

    // Full constructor with profile image path
    public Student(int id, String name, String email, String phone, String dob,
                   String address, String parentName, String parentPhone, String profileImagePath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.profileImagePath = profileImagePath;
    }

    // Getter and Setter methods
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
