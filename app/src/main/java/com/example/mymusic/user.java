package com.example.mymusic;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class user extends RealmObject {
    @PrimaryKey
    private String ID;
    private String name;
    private String password;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getID() {
        return ID;
    }
}
