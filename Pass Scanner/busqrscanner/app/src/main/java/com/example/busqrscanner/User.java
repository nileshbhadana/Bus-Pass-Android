package com.example.busqrscanner;

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
