package com.example.nbhung.exampleormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbhung on 7/21/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "cat.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<Cat, Integer> catDao = null;
    private Dao<Kitten, Integer> kittenDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Cat.class);
            TableUtils.createTable(connectionSource, Kitten.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        List<String> allsq1 = new ArrayList<>();
        for (String str1 : allsq1) {
            database.execSQL(str1);
        }
    }

    public Dao<Cat, Integer> getCatDao() throws SQLException {
        if (catDao == null) {
            catDao = getDao(Cat.class);
        }
        return catDao;
    }

    public Dao<Kitten, Integer> getKittenDao() throws SQLException {
        if (kittenDao == null) {
            kittenDao = getDao(Kitten.class);
        }
        return kittenDao;
    }
}
