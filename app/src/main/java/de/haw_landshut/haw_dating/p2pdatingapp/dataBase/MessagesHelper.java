package de.haw_landshut.haw_dating.p2pdatingapp.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Georg on 19.06.2016.
 */
public class MessagesHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public MessagesHelper(Context context) {
        super(context, TableData.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
