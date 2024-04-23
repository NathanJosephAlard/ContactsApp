package com.example.contactsapp;

public class Contact {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String birthday;

    // Default constructor
    public Contact() {
    }

    // Constructor without id (useful for creating new contacts)
    public Contact(String name, String email, String phone, String birthday) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    // Constructor with id (useful when fetching data from database)
    public Contact(int id, String name, String email, String phone, String birthday) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    // Getters and Setters
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
