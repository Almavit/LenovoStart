package by.matveev.lenovostart.lib;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import static android.content.ContextWrapper.*;
//import static android.content.Context.*;

//import static android.content.Context.MODE_PRIVATE;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;


import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static  final int DATABASE_VERSION = 3;
    public static  final String DIR_SD = "Documents";
    public static  final String DATABASE_NAME = Environment.getExternalStorageDirectory()+ "/" + DIR_SD + "/" + "base/"  + "DocumentDB.db";
    public static  final String TABLE_DOCUMENT = "Document";
    public static  final String TABLE_DOCUMENT_DAT = "Dat";
//    public static  final String KEY_ID = "_id";
    public static  final String KEY_Q_COD = "qcod";
    public static  final String KEY_NUM_NAKL = "numnakl";
    public static  final String KEY_DATE = "date";
    public static  final String KEY_NAME_POST = "namepost";
    public static  final String KEY_NUM_POZ = "numpoz";
    public static  final String KEY_BARCODE = "barcode";
    public static  final String KEY_NAME_TOV = "nametov";
    public static  final String KEY_PRICE = "price";
    public static  final String KEY_QUANTITY = "quantity";
    public static  final String KEY_STATUS = "status";

    public static  final String DAT_KEY_ID = "datid";
    public static  final String DAT_KEY_BARCODE = "datbarcode";
    public static  final String DAT_KEY_PRICE = "datprice";
    public static  final String DAT_KEY_QUANTITY = "datquantity";
    public static  final String DAT_KEY_POSITION = "datposition";



    private SQLiteDatabase dataBase;

    private final Context fContext;

    private SQLiteDatabase myDataBase;
    String DB_PATH = null;

    public DBHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.fContext = context;
    }



    public void selectNumNakl(SQLiteDatabase db, String NumNakl){
        boolean dbExist = checkDataBase();
        if (dbExist) {
            if (tableExists()){

            }

            //ничего не делаем – файл базы данных уже есть
        } else {
            this.getReadableDatabase();

            db.execSQL("select * from " + TABLE_DOCUMENT + "where  " + KEY_NUM_NAKL + " = " + NumNakl);
//                KEY_ID + " integer primary key, " +
//                KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
//                KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
//                //KEY_PRICE + " text, " +
//                KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")"  );
        }
    }

    public void selectGroup(SQLiteDatabase db){
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //ничего не делаем – файл базы данных уже есть
        } else {
            this.getReadableDatabase();

        db.execSQL("select " + KEY_NUM_NAKL + "count(*)" + " from " + TABLE_DOCUMENT + " group by " + KEY_NUM_NAKL);
//                KEY_ID + " integer primary key, " +
//                KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
//                KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
//                //KEY_PRICE + " text, " +
//                KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")"  );
        }

    }

    private boolean tableExists()
    {
        dataBase = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);

        if (TABLE_DOCUMENT == null || dataBase == null || !dataBase.isOpen())
        {
            return false;
        }
        Cursor cursor = dataBase.rawQuery(
                "SELECT COUNT(*) FROM  sqlite_master   WHERE  type='table' AND name = " + TABLE_DOCUMENT,
                new String[] {"table", TABLE_DOCUMENT}
        );
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();

        return count > 0;
    }

   /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     * */
    public void createDataBase() {
       // String myPath = DATABASE_NAME;
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //файл базы данных отсутствует
        }
        if (checkDB != null) {
            //ничего не делаем – файл базы данных уже есть
        } else {
            checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);
        }
            String SqlText = "create table if not exists " + TABLE_DOCUMENT + "(" +
//                KEY_ID + " integer primary key, " +
                    KEY_Q_COD + " text, " +
                    KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
                    KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
                    KEY_PRICE + " text, " +
                    KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")";
            checkDB.execSQL(SqlText );

        checkDB.close();
       // checkDB = null;
        checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);

        String SqlTextDat = "create table if not exists " + TABLE_DOCUMENT_DAT + "(" + DAT_KEY_ID + " integer, " +
                DAT_KEY_BARCODE + " text, " + DAT_KEY_POSITION + " text, " + DAT_KEY_PRICE + " text, " +
                DAT_KEY_QUANTITY + " text)";
        checkDB.execSQL(SqlTextDat);

            checkDB.close();

    }

    private void CreateBase() {
        SQLiteDatabase checkDB = null;
        try {
            // String myPath = DATABASE_NAME;
            //checkDB = openOrCreateDatabase(DATABASE_NAME , MODE_PRIVATE, null);
            checkDB.execSQL("create table " + TABLE_DOCUMENT + "(" +
//                KEY_ID + " integer primary key, " +
                KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
                KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
                KEY_PRICE + " text, " +
                KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")"  );

        } catch (SQLiteException ioe) {
            //файл базы данных отсутствует
        }
        if (checkDB != null) {
            checkDB.close();
        }
    }

//    public void openDataBase() throws SQLException {
//
//        // Open the database
//        String myPath = DB_PATH + DATABASE_NAME;
//        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
//                SQLiteDatabase.OPEN_READWRITE);
//
//
//    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            //файл базы данных отсутствует
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        InputStream input = fContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_NAME;
        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        input.close();
    }

    public void openDataBase() throws SQLException {
        String path = DATABASE_NAME;
        dataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (dataBase != null)
            dataBase.close();
        super.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_DOCUMENT);
//
//        onCreate(db);
    }




    // return cursor
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        return myDataBase.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy);

    }

    public Cursor rawQuery(String query) {
        // TODO Auto-generated method stub
        return myDataBase.rawQuery(query, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


/*        db.execSQL("create table " + TABLE_DOCUMENT + "(" +
//                KEY_ID + " integer primary key, " +
                KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
                KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
                KEY_PRICE + " text, " +
                KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")"  );*/
    }


}
