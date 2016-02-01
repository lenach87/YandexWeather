package com.example.myapplication2.app.POJO;


import java.io.Serializable;

public class City implements Serializable, Comparable <City> {
    private String name;
    private Long id;
    private String country;

    public City (String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public City () {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString () {
        return getName();
    }

    @Override
    public int compareTo (City another) {
        return getName().compareTo(another.getName());
    }
}
