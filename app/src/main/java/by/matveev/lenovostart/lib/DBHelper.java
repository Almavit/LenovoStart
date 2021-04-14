package by.matveev.lenovostart.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static  final int DATABASE_VERSION = 1;
    public static  final String DATABASE_NAME = "DocumentDB";
    public static  final String TABLE_DOCUMENT = "Document";

    public static  final String KEY_ID = "_id";
    public static  final String KEY_NUM_NAKL = "numnakl";
    public static  final String KEY_DATE = "date";
    public static  final String KEY_NAME_POST = "namepost";
    public static  final String KEY_NUM_POZ = "numpoz";
    public static  final String KEY_BARCODE = "batcode";
    public static  final String KEY_NAME_TOV = "nametov";
    public static  final String KEY_PRICE = "price";
    public static  final String KEY_QUANTITY = "quantity";
    public static  final String KEY_TARA = "tara";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_DOCUMENT + "(" + KEY_ID + " integer primary key, " +
                KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
                KEY_NUM_POZ + " text, " + KEY_BARCODE + " real, " + KEY_NAME_TOV + " text, " +
                KEY_PRICE + " real, " + KEY_QUANTITY + " real, " + KEY_TARA + " integer" + ")"  );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_DOCUMENT);

        onCreate(db);
    }
}
