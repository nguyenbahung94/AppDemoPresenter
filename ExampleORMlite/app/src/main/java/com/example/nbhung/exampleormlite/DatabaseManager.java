package com.example.nbhung.exampleormlite;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by nbhung on 7/21/2017.
 */

public class DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseHelper helper;

    private DatabaseManager(Context context) {
        helper = new DatabaseHelper(context);
    }

    public static void init(Context context) {
        if (null == instance) {
            instance = new DatabaseManager(context);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    public DatabaseHelper getHelper() {
        return helper;
    }

    public ArrayList<Cat> getAllCats() throws SQLException {
        ArrayList<Cat> cats = null;
        cats = (ArrayList<Cat>) getHelper().getCatDao().queryForAll();
        return cats;
    }

    public void addCat(Cat cat) throws SQLException {
        getHelper().getCatDao().create(cat);
    }

    public void refreshCat(Cat cat) throws SQLException {
        getHelper().getCatDao().refresh(cat);
    }

    public void updateCat(Cat wishlist) throws SQLException {
        getHelper().getCatDao().update(wishlist);
    }

    public void deleteCat(int catId) throws SQLException {
        DeleteBuilder<Cat, Integer> deleteBuilder = getHelper().getCatDao().deleteBuilder();
        deleteBuilder.where().eq("id", catId);
        deleteBuilder.delete();
    }

    public Kitten newKitten() throws SQLException {
        Kitten kitten = new Kitten();
        getHelper().getKittenDao().create(kitten);
        return kitten;
    }

    public Kitten newKittenAppend(Kitten kitten) {
        try {
            getHelper().getKittenDao().create(kitten);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kitten;
    }

    public void updateKitten(Kitten item) throws SQLException {
        getHelper().getKittenDao().update(item);
    }

    public ArrayList<Kitten> getAllKittens() throws SQLException {
        ArrayList<Kitten> kittenArrayList = null;
        kittenArrayList = (ArrayList<Kitten>) getHelper().getKittenDao().queryForAll();
        return kittenArrayList;
    }

    public void deletekit(int kittenId) throws SQLException {
        DeleteBuilder<Kitten, Integer> deleteBuilder = getHelper().getKittenDao().deleteBuilder();
        deleteBuilder.where().eq("id", kittenId);
        deleteBuilder.delete();
    }
}
