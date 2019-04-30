package com.example.stephen.rentalfinder;

public class Connection {
    private String ipAddress;
    private String urlAddApartment;
    private String urlSignUpUser;
    private String urlViewApartments;
    private String urlLogin;

    Connection() {
        this.ipAddress = "http://192.168.43.105/projects/rental-finder/";
        this.urlAddApartment = ipAddress + "addapartment.php";
        this.urlSignUpUser = ipAddress + "signup.php";
        this.urlViewApartments = ipAddress + "viewapartments.php?page=";
        this.urlLogin = ipAddress + "login.php";
    }

    public String getUrlAddApartment() {
        return urlAddApartment;
    }

    public String getUrlSignUpUser() {
        return urlSignUpUser;
    }

    public String getUrlViewApartments() {
        return urlViewApartments;
    }

    public String getUrlLogin() {
        return urlLogin;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
