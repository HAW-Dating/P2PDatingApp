package de.haw_landshut.haw_dating.p2pdatingapp.dataBase;

import android.provider.BaseColumns;

/**
 * Created by Georg on 19.06.2016.
 */
public class TableData {
    public static final String DATABASE_NAME = "P2P_DATING_DATABASE";
    public static final String SPACE = " ";
    public static final String SEMI_COLON = SPACE + ";" + SPACE;
    public static final String COLON = SPACE + "," + SPACE;
    public static final String OPENING_BRACKET = SPACE + "(" + SPACE;
    public static final String CLOSING_BRACKET = SPACE + ")" + SPACE;

    private TableData() {
    }

    public static class Messages implements BaseColumns {
        public static final String TABLE_NAME = "RECEIVED_MESSAGES";
        public static final String COLUMN_UUID = "UUID";
        public static final String COLUMN_UUID_TYPE = SPACE + "TEXT" + SPACE;
        public static final String COLUMN_MESSAGE = "MESSAGE";
        public static final String COLUMN_MESSAGE_TYPE = SPACE + "TEXT" + SPACE;
        public static final String COLUMN_DECRYPTED = "DECRYPTED";
        public static final String COLUMN_DECRYPTED_TYPE = SPACE + "INTEGER" + SPACE;
        public static final String COLUMN_OWN = "OWN";
        public static final String COLUMN_OWN_TYPE = SPACE + "INTEGER" + SPACE;
    }
}
