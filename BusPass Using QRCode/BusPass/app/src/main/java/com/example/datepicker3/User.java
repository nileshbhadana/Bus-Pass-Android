package com.example.datepicker3;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class User {

    public String name;
    public String email;
    public String password;
    public String fileurl;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email, String password,String fileurl) {
        this.name = name;
        this.email= email;
        this.password=password;
        this.fileurl=fileurl;
    }
}
