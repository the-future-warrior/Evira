package com.mr_17.evira.model;

public class UsersListRecyclerViewModel
{
    String firstName;
    String lastName;
    String username;
    String emailAddress;
    String phoneNumber;
    String panNumber;
    String panURL;
    String aadharNumber;
    String aadharURL;
    String profilePicURL;
    String address;
    boolean isBlocked;
    boolean isAdmin;
    boolean isSuperAdmin;

    public UsersListRecyclerViewModel(String firstName, String lastName, String username, String emailAddress, String phoneNumber, String panNumber, String panURL, String aadharNumber, String aadharURL, String profilePicURL, String address, boolean isBlocked, boolean isAdmin, boolean isSuperAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.panNumber = panNumber;
        this.panURL = panURL;
        this.aadharNumber = aadharNumber;
        this.aadharURL = aadharURL;
        this.profilePicURL = profilePicURL;
        this.address = address;
        this.isAdmin = isAdmin;
        this.isBlocked = isBlocked;
        this.isSuperAdmin = isSuperAdmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public String getPanURL() {
        return panURL;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public String getAadharURL() {
        return aadharURL;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public String getAddress() {
        return address;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }
}

