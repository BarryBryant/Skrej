package com.willowtreeapps.skrej.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by barrybryant on 11/17/16.
 */

public class RealmUser extends RealmObject {

    @PrimaryKey
    private String email;
    @Required
    private String name;

    public RealmUser() {
    }

    public RealmUser(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
