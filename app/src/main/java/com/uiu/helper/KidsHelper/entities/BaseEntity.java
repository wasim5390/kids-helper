package com.uiu.helper.KidsHelper.entities;


import java.util.ArrayList;

public abstract class BaseEntity<T> {

    final String DATE_TIME_FORMAT = "yyyy-mm-ddThh:mm:ssZ";

    public abstract void set(String jsonString);
    public ArrayList<T> list = new ArrayList<>();
}
