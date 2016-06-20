package de.haw_landshut.haw_dating.p2pdatingapp.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import de.haw_landshut.haw_dating.p2pdatingapp.MatchesActivity;
import de.haw_landshut.haw_dating.p2pdatingapp.data.WifiMessage;
import de.haw_landshut.haw_dating.p2pdatingapp.match.Match;

/**
 * Created by Georg on 19.06.2016.
 */
public class MessagesHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_DATABASE_QUERY =
            "CREATE TABLE " + TableData.Messages.TABLE_NAME + TableData.OPENING_BRACKET
                    + TableData.Messages.COLUMN_MESSAGE_UUID + TableData.Messages.COLUMN_MESSAGE_UUID_TYPE + "PRIMARY KEY" + TableData.COLON
                    + TableData.Messages.COLUMN_MESSAGE + TableData.Messages.COLUMN_MESSAGE_TYPE + TableData.COLON
                    + TableData.Messages.COLUMN_DATE + TableData.Messages.COLUMN_DATE_TYPE + TableData.COLON
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
            final long date,
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
        if (own) {
            db.delete(TableData.Messages.TABLE_NAME, TableData.Messages.COLUMN_OWN + " = 1", null);
        }

        final ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Messages.COLUMN_MESSAGE_UUID, messageUUID);
        contentValues.put(TableData.Messages.COLUMN_MESSAGE, serializedWifiMessage);
        contentValues.put(TableData.Messages.COLUMN_DATE, date);
        contentValues.put(TableData.Messages.COLUMN_DECRYPTED, boolToInt(decrypted));
        contentValues.put(TableData.Messages.COLUMN_OWN, boolToInt(own));
        if (decrypted) {
            contentValues.put(TableData.Messages.COLUMN_SECRET_UUID, secret);
        } else {
            contentValues.putNull(TableData.Messages.COLUMN_SECRET_UUID);
        }
        final long error = db.insert(TableData.Messages.TABLE_NAME, null, contentValues);
        if (error == -1) {
            Log.d(TAG, "storeMessage error: " + serializedWifiMessage);
        }
    }

    public List<Match> getMatches(final Context context) {
        final List<Match> resultList = new ArrayList<>();
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(
                TableData.Messages.TABLE_NAME,
                new String[]{
                        TableData.Messages.COLUMN_MESSAGE,
                        TableData.Messages.COLUMN_SECRET_UUID,
                        TableData.Messages.COLUMN_DATE,
                        TableData.Messages.COLUMN_OWN},
                TableData.Messages.COLUMN_DECRYPTED + " = 1",
                null,
                null,
                null,
                null);
        final int rows = cursor.getCount();
        if (rows > 0) {
            while (cursor.moveToNext()) {
                final WifiMessage wifiMessage = WifiMessage.deserialize(cursor.getString(0));
                final String secret = cursor.getString(1);
                final long date = cursor.getLong(2);
                final boolean own = intToBool(cursor.getInt(3));

                resultList.add(makeMatch(wifiMessage, secret, date, own, context));
            }
        }
        return resultList;
    }

    public String getOwnSerializedSearchProfile() {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(
                TableData.Messages.TABLE_NAME,
                new String[]{
                        TableData.Messages.COLUMN_MESSAGE},
                TableData.Messages.COLUMN_OWN + " = 1",
                null,
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return null;
    }

    private Match makeMatch(
            final WifiMessage wifiMessage,
            final String secret,
            final long date,
            final boolean own,
            final Context context) {
        return new Match(
                wifiMessage.getUuid().toString(),
                DateUtils.formatDateTime(context, date, 0),
                secret,
                own);
    }

    public Set<UUID> getStoredUUIDS() {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(
                TableData.Messages.TABLE_NAME,
                new String[]{TableData.Messages.COLUMN_MESSAGE_UUID},
                null, null, null, null, null);

        final int rows = cursor.getCount();
        if (rows != 0) {
            final Set<UUID> resultSet = new LinkedHashSet<>(rows);
            while (cursor.moveToNext()) {
                resultSet.add(UUID.fromString(cursor.getString(0)));
            }
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
