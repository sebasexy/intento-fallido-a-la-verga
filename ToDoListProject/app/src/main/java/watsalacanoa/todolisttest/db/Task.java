package watsalacanoa.todolisttest.db;

import android.provider.BaseColumns;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Task {

    public static final String TABLE_NOTE = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEMS = "items";
    public static final String COLUMN_SLASHED = "slashed";
    public static final String COLUMN_NOTE_TITLE = "noteTitle";

    ////////////////////////////////////////////
    public static final String TABLE_NOTIOLI = "notioli";
    public static final String NOTIOLI_COLUMN_ID = "_id";
    public static final String NOTIOLI_COLUMN_TITLE = "title";
    //public static final String NOTIOLI_COLUMN_CONTENT = "content";
    ////////////////////////////////////////////

    // CALENDIOLI INFO //////
    /////////////////////////
    public static final String TABLE_CALENDIOLI = "calendioli";
    public static final String CALENDIOLI_ID = "_id";
    public static final String CALENDIOLI_TITLE = "title";
    public static final String CALENDIOLI_EVENT = "event";

    private static final String CALENDIOLI_CREATE = "CREATE TABLE "
            + TABLE_CALENDIOLI
            + "("
            +CALENDIOLI_ID + "TEXT PRIMARY KEY NOT NULL, "
            + CALENDIOLI_TITLE + " TEXT NOT NULL, "
            + CALENDIOLI_EVENT + " TEXT NOT NULL"
            + ");";
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_NOTE
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SLASHED + " TEXT NOT NULL, "
            + COLUMN_NOTE_TITLE + " TEXT NOT NULL, "
            + COLUMN_ITEMS + " TEXT NOT NULL"
            + ");";
            ////////////////////////////////////////////
            ////////////////////////////////////////////
    private static final String NOTIOLI_CREATE = " CREATE TABLE "
                    + TABLE_NOTIOLI
                    + "("
                    + NOTIOLI_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NOTIOLI_COLUMN_TITLE + " TEXT NOT NULL"
                    //+ NOTIOLI_COLUMN_CONTENT + " TEXT NOT NULL"
                    + ");";

    public static void onCreate(SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
        db.execSQL(NOTIOLI_CREATE);
        db.execSQL(CALENDIOLI_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Task.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIOLI);
        onCreate(database);
        database.execSQL("DROP TABLE IF EXISTS"  + TABLE_CALENDIOLI);
    }
}
