package com.example.nbhung.myapplicationgreendao;

import android.app.Application;

import com.example.nbhung.myapplicationgreendao.db.DaoMaster;
import com.example.nbhung.myapplicationgreendao.db.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by nbhung on 7/18/2017.
 */

public class AppController extends Application {
    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "student-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
