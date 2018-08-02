package com.autohush.www.dah;

/**
 * Created by PKBEST on 17-03-2016.
 */
public class ListItems {
    String Location;
    String Profile;

    ListItems(String s, String p)
    {
        Location = s;
        Profile = p;
    }

    public String getLocation()
    {
        return Location;
    }

    public String getProfile(){ return Profile; }
}
