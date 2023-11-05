package model;

import javax.persistence.Id;

public class Ingredient {
    @Id
    private int id;
    private String name;
    private String typ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    @Override
    public String toString() {
        return "Ingredient ID: " + id + ", Name: " + name + ", Type: " + typ;
    }
}