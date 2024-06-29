package com.longthph30891.chatapp.models;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    public String userId;
    public String name;
    public String email;
    public String password;
    public String image;
    public String token;
    public String id;

    public User(String userId, String name, String email, String password, String image) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getImage() {
        return image;
    }

    public User setImage(String image) {
        this.image = image;
        return this;
    }
    public HashMap<String, Object> convertHashMap(){
        HashMap<String,Object> user = new HashMap<>();
        user.put("userId",userId);
        user.put("name",name);
        user.put("email",email);
        user.put("password",password);
        user.put("image",image);
        return user;
    }
}
