package by.matveev.lenovostart.lib;

import android.content.ContentValues;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
//import static android.content.ContextWrapper.*;
//import static android.content.Context.*;

//import static android.content.Context.MODE_PRIVATE;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;


import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import com.opencsv.CSVReader;

public class DBHelper extends SQLiteOpenHelper {

    public static  final int DATABASE_VERSION = 3;
    public static  final String DIR_SD = "Documents";
    public static  final String DATABASE_NAME = Environment.getExternalStorageDirectory()+ "/" + DIR_SD + "/" + "base/"  + "DocumentDB.db";
    public static  final String TABLE_DOCUMENT = "Document";
    public static  final String TABLE_DOCUMENT_DAT = "Dat";
    public static  final String TABLE_DOCUMENT_PRICE = "Price";
//    public static  final String KEY_ID = "_id";
    public static  final String KEY_QR_CODE = "qrcode";
    public static  final String KEY_NUM_NAKL = "numnakl";
    public static  final String KEY_DATE = "date";
    public static  final String KEY_NAME_POST = "namepost";
    public static  final String KEY_NUM_POZ = "numpoz";
    public static  final String KEY_BARCODE = "barcode";
    public static  final String KEY_NAME_TOV = "nametov";
    public static  final String KEY_PRICE = "price";
    public static  final String KEY_PRICEOTP = "priceotp";
    public static  final String KEY_QUANTITY = "quantity";
    public static  final String KEY_STATUS = "status";

    public static  final String DAT_KEY_ID = "datid";
    public static  final String DAT_KEY_BARCODE = "datbarcode";
    public static  final String DAT_KEY_PRICE = "datprice";
    public static  final String DAT_KEY_QUANTITY = "datquantity";
    public static  final String DAT_KEY_POSITION = "datposition";

    //public static  final String PRICE_NUM_NAKL = "numnakl";
    //public static  final String KEY_DATE = "date";
    //public static  final String KEY_NAME_POST = "namepost";
    //public static  final String KEY_NUM_POZ = "numpoz";
    public static  final String PRICE_BARCODE = "barcode";
    public static  final String PRICE_NAME_TOV = "nametov";
    public static  final String PRICE_PRICEOTP = "priceotp";
    public static  final String PRICE_PRICE = "price";
    public static  final String PRICE_DATA = "data";
    //public static  final String KEY_QUANTITY = "quantity";
    //public static  final String KEY_STATUS = "status";

    ContentValues ScontentValues = new ContentValues();
    private SQLiteDatabase dataBase;

    private final Context fContext;

    private SQLiteDatabase myDataBase;
    String DB_PATH = null;

    public DBHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.fContext = context;
    }

   /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     * */
    public void createDataBase() {
       // String myPath = DATABASE_NAME;
        SQLiteDatabase checkDB = null;
        try{


        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
            // checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);
        } catch (SQLiteException e) {
            //файл базы данных отсутствует
        }
        if (checkDB != null) {
            //ничего не делаем – файл базы данных уже есть
        } else {
            checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);
        }
            checkDB.close();
            // checkDB = null;
            checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);

            String SqlText = "create table if not exists " + TABLE_DOCUMENT + "(" +
//                KEY_ID + " integer primary key, " +
                    KEY_QR_CODE + " text, " +
                    KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
                    KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
                    KEY_PRICEOTP + " text, " + KEY_PRICE + " text, " +
                    KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")";
            checkDB.execSQL(SqlText );

        checkDB.close();
       // checkDB = null;
        checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);



        String SqlTextDat = "create table if not exists " + TABLE_DOCUMENT_DAT + "(" + DAT_KEY_ID + " integer, " +
                DAT_KEY_BARCODE + " text, " + DAT_KEY_PRICE + " text, " + DAT_KEY_QUANTITY + " text, " +
                 DAT_KEY_POSITION+ " text)";
        checkDB.execSQL(SqlTextDat);

            checkDB.close();

            //checkDB.close();
            // checkDB = null;
            checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);

            String SqlTextPrice = "create table if not exists " + TABLE_DOCUMENT_PRICE + "(" + PRICE_BARCODE + " text, " +
                    PRICE_NAME_TOV + " text, " + PRICE_PRICEOTP + " text, "  + PRICE_PRICE + " text, " + PRICE_DATA +  " text)";
            checkDB.execSQL(SqlTextPrice);






            Cursor cursor = null;
            try {
                String column = "";
                String name = "";
                cursor = checkDB.rawQuery("PRAGMA table_info("+ TABLE_DOCUMENT_PRICE +")", null);
                if (cursor != null) {
                    Integer iCountursor = cursor.getCount();
                    while (cursor.moveToNext()) {
                        name = cursor.getString(1);
                        if (column.equalsIgnoreCase(name)) {
                            //isExists = true;
                            break;
                        }
                    }
                }

            } finally {
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
            }

            checkDB.close();


        } catch (SQLiteException e) {
            if (checkDB != null) {
                checkDB.close();
            }
        }
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

    @Override
    public void onCreate(SQLiteDatabase db) {

      //  createDataBase();
/*        db.execSQL("create table " + TABLE_DOCUMENT + "(" +
//                KEY_ID + " integer primary key, " +
                KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
                KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
                KEY_PRICE + " text, " +
                KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")"  );*/
    }

    public boolean SaveDataPrice(Context contex,String TableName, String SqlStroka,CSVReader reader) throws IOException, InterruptedException {
        DBHelper dbHelper = new DBHelper(contex);
        String[] nextLine = null;
        SQLiteDatabase dbdb = dbHelper.getWritableDatabase();//getReadableDatabase


        Cursor basecursor = dbdb.rawQuery(SqlStroka, null);//"select * from " + DBHelper.TABLE_DOCUMENT
        Integer iCount = basecursor.getCount();
        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();
        dbdb.delete(TableName,null,null);
        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();
        basecursor = dbdb.rawQuery(SqlStroka, null);//"select * from " + DBHelper.TABLE_DOCUMENT
        iCount = basecursor.getCount();
        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();
        Integer ISSSSS = 0;

//        List sdsdsdsdsd = reader.readAll();
//        String dddd = sdsdsdsdsd.toString();
        while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
            ISSSSS++;
            ScontentValues = ContentValuesPriceCsv(nextLine);
            dbdb.insert(TableName, null, ScontentValues);
             //nextLine = null;
        }
        dbdb.close();
            return true;
    }

    public boolean SaveDataDat(Context contex,String TableName, String SqlStroka,CSVReader reader) throws IOException {
        DBHelper dbHelper = new DBHelper(contex);
        String[] nextLine =  null;
        String[] sID = new String[1];
        SQLiteDatabase dbdb = dbHelper.getWritableDatabase();//getReadableDatabase

        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();
        dbdb.delete(TableName,null,null);
        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();
        Integer ISSSSS = 0;


        while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
            ISSSSS++;
            String str = Integer.toString(ISSSSS);

            sID[0] = str;

            nextLine = concatArray(sID,nextLine);

            ScontentValues = ContentValuesDat(nextLine);
            dbdb.insert(TableName, null, ScontentValues);
            //nextLine = null;
        }
        dbdb.close();
        return true;
    }
    // метод для склеивания двух строковых массивов
    private String[] concatArray(String[] a, String[] b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        String[] r = new String[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public ContentValues ContentValuesQRCsv(String[] csvreader){

        ContentValues ScontentValues = new ContentValues();

        ScontentValues.put(DBHelper.KEY_QR_CODE, csvreader[0]);
        ScontentValues.put(DBHelper.KEY_NUM_NAKL, csvreader[1]);
        ScontentValues.put(DBHelper.KEY_DATE, csvreader[2]);
        ScontentValues.put(DBHelper.KEY_NAME_POST, csvreader[3]);
        ScontentValues.put(DBHelper.KEY_NUM_POZ, csvreader[4]);
        ScontentValues.put(DBHelper.KEY_BARCODE, csvreader[5]);
        ScontentValues.put(DBHelper.KEY_NAME_TOV, csvreader[6]);
        ScontentValues.put(DBHelper.KEY_QUANTITY, csvreader[7]);
        ScontentValues.put(DBHelper.KEY_STATUS, csvreader[8]);

        return ScontentValues;
    }
    public ContentValues ContentValuesDat(String[] csvreader){

        ContentValues ScontentValues = new ContentValues();

        ScontentValues.put(DBHelper.DAT_KEY_ID, csvreader[0]);
        ScontentValues.put(DBHelper.DAT_KEY_BARCODE, csvreader[1]);
        ScontentValues.put(DBHelper.DAT_KEY_PRICE, csvreader[2]);
        ScontentValues.put(DBHelper.DAT_KEY_QUANTITY, csvreader[3]);
        ScontentValues.put(DBHelper.DAT_KEY_POSITION, csvreader[4]);

        return ScontentValues;
    }
    public ContentValues ContentValuesPriceCsv(String[] csvreader){


            ContentValues ScontentValues = new ContentValues();

            ScontentValues.put(DBHelper.PRICE_BARCODE, csvreader[0]);
            ScontentValues.put(DBHelper.PRICE_NAME_TOV, csvreader[1]);
            ScontentValues.put(DBHelper.PRICE_PRICEOTP, csvreader[2]);
            ScontentValues.put(DBHelper.PRICE_PRICE, csvreader[3]);
            ScontentValues.put(DBHelper.PRICE_DATA, csvreader[4]);

        return ScontentValues;
    }
}
