package com.example.stephen.rentalfinder;

public class ViewApartmentDataHandler {
    private String apartmentName, rent, contact,location,imageUrl,description;

    public ViewApartmentDataHandler() {
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String name) {
        this.apartmentName = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

