package com.example.bookshare;

public class Account {

    String Username;
    String Password;
    String FirstName;
    String LastName;
    String Email;
    String AvatarAddress;
    int ID;

    public Account(String username, String password, String firstName, String lastName, String email, String avatarAddress, int ID) {
        Username = username;
        Password = password;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        AvatarAddress = avatarAddress;
        this.ID = ID;
    }
}
