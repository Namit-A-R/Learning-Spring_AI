package com.namitar.springai.model;

import java.util.List;
import java.util.Objects;

public class CountryCities {
    private String country;
    private List<String> cities;

    // Constructor
    public CountryCities(String country, List<String> cities) {
        this.country = country;
        this.cities = cities;
    }

    // Getters
    public String getCountry() {
        return country;
    }

    public List<String> getCities() {
        return cities;
    }

    // Setters (optional, if you want mutability)
    public void setCountry(String country) {
        this.country = country;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountryCities that)) return false;
        return Objects.equals(country, that.country) &&
                Objects.equals(cities, that.cities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, cities);
    }

    // toString
    @Override
    public String toString() {
        return "CountryCities{" +
                "country='" + country + '\'' +
                ", cities=" + cities +
                '}';
    }
}
