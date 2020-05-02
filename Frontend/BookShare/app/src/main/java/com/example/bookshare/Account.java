package com.example.bookshare;

import android.graphics.Bitmap;


public class Account {

    public static Account Instance;

    String Username;
    String FirstName;
    String LastName;
    String Email;
    String Token;
    Bitmap Avatar;

    public static Account getInstance(String username, String firstName, String lastName, String email, String token){
        if(Instance == null){
            Instance = new Account(username,firstName,lastName,email,token);
        }
        else {
            Instance.Username = username;
            Instance.Username = firstName;
            Instance.Username = lastName;
            Instance.Username = email;
            Instance.Username = token;
        }
        return Instance;
    }

    public static Account getInstance(Bitmap avatar){
        if(Instance == null){
            Instance = new Account(avatar);
        }
        else {
            Instance.Avatar = avatar;
        }
        return Instance;
    }

    private Account(String username, String firstName, String lastName, String email, String token) {
        Username = username;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Token = token;
    }

    private Account(Bitmap avatar) {
        Avatar = avatar;
    }
}
