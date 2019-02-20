package com.example.wbj.basic;

import com.google.gson.Gson;

public class BasicBean {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
