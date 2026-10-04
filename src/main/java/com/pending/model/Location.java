package com.pending.model;

public class Location {
    private String state;
    private String city;
    private String county;
    private String street;
    private String zipcode;

    public Location(String state, String city, String county, String street, String zipcode) {
        this.state = state;
        this.city = city;
        this.county = county;
        this.street = street;
        this.zipcode = zipcode;
    }

    public boolean isInState(String state) {
        return state.equals(this.state);
    }

    public boolean isInCounty(String county) {
        return county.equals(this.county);
    }

    public boolean isInCity(String city) {
        return city.equals(this.city);
    }

    public boolean isInZip(String zipcode) {
        return zipcode.equals(this.zipcode);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
