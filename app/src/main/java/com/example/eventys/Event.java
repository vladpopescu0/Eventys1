package com.example.eventys;

import java.util.List;

public class Event {

    public String creator,date,time,nrParticipants,name1,description,icon,allParticipants;
    public double xlong,ylat;

    public Event(){

    }
    public Event(String name1, String description,String nrParticipants,String time, String date, double xlong, double ylat, String creator, String icon,String allParticipants){
        this.name1=name1;
        this.description=description;
        this.nrParticipants=nrParticipants;
        this.time=time;
        this.date=date;
        this.xlong=xlong;
        this.ylat=ylat;
        this.creator=creator;
        this.icon=icon;
        this.allParticipants=allParticipants;
    }

    public String getName1() {
        return name1;
    }

    public String getDescription() {
        return description;
    }

    public String getNrParticipants() {
        return nrParticipants;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public double getXlong() {
        return xlong;
    }

    public double getYlat() {
        return ylat;
    }

    public String getCreator(){ return creator;}

    public String getIcon(){return icon; }

    public String getAllParticipants(){return allParticipants;}
}
