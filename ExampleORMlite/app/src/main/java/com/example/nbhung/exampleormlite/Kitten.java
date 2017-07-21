package com.example.nbhung.exampleormlite;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by nbhung on 7/21/2017.
 */

public class Kitten {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Cat cat;

    public Kitten() {
    }

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

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }
}
