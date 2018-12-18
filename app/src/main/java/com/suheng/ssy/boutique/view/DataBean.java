package com.suheng.ssy.boutique.view;

public class DataBean implements IDataBean {
    String textName;

    public DataBean(String textName) {
        this.textName = textName;
    }

    @Override
    public String getTextName() {
        return textName;
    }
}
