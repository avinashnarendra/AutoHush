package com.autohush.www.dah;

public class TimeList {

    String Time;
    String Duration;
    String Profile;

    TimeList(String Time,String Duration,String Profile)
    {
        this.Time = Time;
        this.Duration = Duration;
        this.Profile = Profile;
    }

    public String getTime(){
        return Time;
    }

    public String getDuration() {
        return Duration;
    }

    public String getProfile() { return Profile; }
}
