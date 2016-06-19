package de.haw_landshut.haw_dating.p2pdatingapp.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Georg on 19.06.2016.
 */
public class MessagesHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_DATABASE_QUERY =
            "CREATE TABLE " + TableData.Messages.TABLE_NAME + TableData.OPENING_BRACKET
                    + TableData.Messages.COLUMN_MESSAGE_UUID + TableData.Messages.COLUMN_MESSAGE_UUID_TYPE + "PRIMARY KEY" + TableData.COLON
                    + TableData.Messages.COLUMN_MESSAGE + TableData.Messages.COLUMN_MESSAGE_TYPE + TableData.COLON
                    + TableData.Messages.COLUMN_DECRYPTED + TableData.Messages.COLUMN_DECRYPTED_TYPE + TableData.COLON
                    + TableData.Messages.COLUMN_OWN + TableData.Messages.COLUMN_OWN_TYPE + TableData.COLON
                    + TableData.Messages.COLUMN_SECRET_UUID + TableData.Messages.COLUMN_SECRET_UUID_TYPE
                    + TableData.CLOSING_BRACKET + TableData.SEMI_COLON;
    private static final String TAG = "DatabaseOperation";

    public MessagesHelper(Context context) {
        super(context, TableData.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void storeMessage(
            final String messageUUID,
            final String serializedWifiMessage,
            final boolean decrypted,
            final boolean own,
            final String secret) {
        if (own && !decrypted) {
            throw new IllegalArgumentException("own = true && decrypted == false");
        }
        if (decrypted && secret == null) {
            throw new IllegalArgumentException("decrypted = true && secret == null");
        }
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Messages.COLUMN_MESSAGE_UUID, messageUUID);
        contentValues.put(TableData.Messages.COLUMN_MESSAGE, serializedWifiMessage);
        contentValues.put(TableData.Messages.COLUMN_DECRYPTED, boolToInt(decrypted));
        contentValues.put(TableData.Messages.COLUMN_OWN, boolToInt(own));
        contentValues.put(TableData.Messages.COLUMN_SECRET_UUID, secret);
        final long error = db.insert(TableData.Messages.TABLE_NAME, null, contentValues);
        if (error == -1) {
            Log.d(TAG, "storeMessage error: " + serializedWifiMessage);
        }
    }

    public Set<UUID> getStoredUUIDS() {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(TableData.Messages.TABLE_NAME, new String[]{TableData.Messages.COLUMN_MESSAGE_UUID}, null, null, null, null, null);
        final int rows = cursor.getCount();
        if (rows != 0) {
            final Set<UUID> resultSet = new LinkedHashSet<>(rows);
            do {
                resultSet.add(UUID.fromString(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        return new LinkedHashSet<>();
    }

    private static int boolToInt(final boolean bool) {
        return bool ? 1 : 0;
    }

    private static boolean intToBool(final int n) {
        return !(n == 0);
    }
}
