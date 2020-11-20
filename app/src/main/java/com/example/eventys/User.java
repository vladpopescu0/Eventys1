package com.example.eventys;

public class User {

    public String fName,email,userID,myEvents,events;

    public User(){

    }

    public User(String fName,String email,String userID,String myEvents,String events){
        this.fName=fName;
        this.email=email;
        this.userID=userID;
        this.myEvents=myEvents;
        this.events=events;
    }
    public String getfName(){
        return fName;
    }
    public String getEmail(){
        return email;
    }
    public String getUserID(){return  userID;}
    public String getMyEvents(){return myEvents;}
    public String getEvents(){return events;}
}
