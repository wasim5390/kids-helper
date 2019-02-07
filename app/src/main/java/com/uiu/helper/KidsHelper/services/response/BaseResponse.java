package com.uiu.helper.KidsHelper.services.response;

import java.util.ArrayList;

public abstract class BaseResponse<T> {
    protected ArrayList<T> list = new ArrayList<T>();

    protected String key;
    protected String value;

    public abstract void set(String input);

    public ArrayList<T> getList() {
        return list;
    }

    public String getString() {
        if (this.value == null)
            return "";

        return this.value;
    }
}
