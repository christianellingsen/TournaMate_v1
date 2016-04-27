package com.dtu.tournamate_v1;

/**
 * Created by chris on 27-04-2016.
 */
public class User {

    private String u_ID, firstName, lastName, userName, imageURL, eMail;

    public User() {
        this.u_ID = "";
        this.firstName = "";
        this.lastName = "";
        this.userName = "";
        this.imageURL = "";
        this.eMail ="";
    }

    public String getU_ID() {
        return u_ID;
    }

    public void setU_ID(String u_ID) {
        this.u_ID = u_ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
