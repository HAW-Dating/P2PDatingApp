package de.haw_landshut.haw_dating.p2pdatingapp.dataBase;

import android.app.Application;

/**
 * Created by Georg on 19.06.2016.
 */
public class DataBaseApplication extends Application {
    private static DataBaseApplication instance;
    private MessagesHelper dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        getDataBase().drop();
    }

    public static DataBaseApplication getInstance() {
        return instance;
    }

    public synchronized MessagesHelper getDataBase() {
        if (dataBase == null) {
            dataBase = new MessagesHelper(this);
        }
        return dataBase;
    }
}
