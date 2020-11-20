package com.example.eventys;

public class User {

    public String fName,email,userID;

    public User(){

    }

    public User(String fName,String email,String userID){
        this.fName=fName;
        this.email=email;
        this.userID=userID;
    }
    public String getfName(){
        return fName;
    }
    public String getEmail(){
        return email;
    }
    public String getUserID(){return  userID;}

}
