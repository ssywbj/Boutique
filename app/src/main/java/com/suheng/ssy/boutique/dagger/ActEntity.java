package com.suheng.ssy.boutique.dagger;

public class ActEntity {

    String name;

    public ActEntity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ActEntity{" + "name='" + name + '\'' + '}';
    }
}
