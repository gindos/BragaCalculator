package gindos.bragacalculator;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;

/**
 * Created by gindos on 13.11.16.
 */

public class classdbReceptsBraga extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bragacalculator.db";
    private static final int SCHEMA = 1;

    public static final String TABLE_NAME_RECEPTS = "receptsbraga";
    // названия столбцов таблицы receptsbraga
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE_TIME = "date_time";

    public classdbReceptsBraga(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME_RECEPTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DATE_TIME + " INTEGER);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
/*
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
*/
    }

/*
    public String getName(SQLiteDatabase db, long id) {
        userCursor = db.rawQuery("select * from " + this.TABLE_NAME_RECEPTS + " where " +
                this.COLUMN_ID + "=?", id);
        userCursor.moveToFirst();
        nameBox.setText(userCursor.getString(1));
        yearBox.setText(String.valueOf(userCursor.getInt(2)));
        userCursor.close();
    }
*/
}
