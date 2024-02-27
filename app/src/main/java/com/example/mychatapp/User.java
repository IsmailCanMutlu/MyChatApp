package com.example.mychatapp;

public class User {
    private String username;
    private String email;
    private String id;

    // Firebase'in veritabanı için gereken boş yapıcı
    public User() {
        // Boş yapıcı
    }

    // Parametreli yapıcı
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    // ID için getter ve setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    // Kullanıcı adı için getter ve setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // E-posta için getter ve setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

