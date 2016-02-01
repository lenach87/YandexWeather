package com.example.myapplication2.app.POJO;

import java.io.Serializable;
import java.util.ArrayList;

public class Country implements Serializable {

    private String name;
    private ArrayList<City> cities;

    public Country(String name) {
        this.name=name;
    }
    public Country(String name, ArrayList<City> cities) {

        this.name = name;
        this.cities=cities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    @Override
    public String toString () {
        return getName();
    }
}
