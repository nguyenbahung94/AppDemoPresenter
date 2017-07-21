package com.example.nbhung.exampleormlite;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbhung on 7/21/2017.
 */

public class Cat {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;
    @ForeignCollectionField
    private ForeignCollection<Kitten> kittens;

    public Cat() {
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

    public List<Kitten> getKittens() {
        ArrayList<Kitten> itemlist=new ArrayList<>();
        for (Kitten item:kittens){
            itemlist.add(item);
        }
        return itemlist;
    }

    public void setKittens(ForeignCollection<Kitten> kittens) {
        this.kittens = kittens;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                '}';
    }
}
