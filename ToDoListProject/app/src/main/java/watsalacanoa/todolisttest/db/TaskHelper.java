package watsalacanoa.todolisttest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "thedb.db";
    public static final int DB_VERSION = 1;
    public TaskHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Task.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Task.onUpgrade(db, oldVersion, newVersion);
    }
}
