package com.example.chatapp;

public class User {
    String profilepic,mail,username,pass,userID,lastmessage,status;

    public  User(){}

    public User(String id, String namee, String emaill, String password, String profilepic, String status){
        this.userID = id;
        this.username = namee;
        this.mail = emaill;
        this.pass = password;
        this.profilepic = profilepic;
        this.status = status;
    }


    public String getProfilepic () {
        return profilepic;
    }


    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
