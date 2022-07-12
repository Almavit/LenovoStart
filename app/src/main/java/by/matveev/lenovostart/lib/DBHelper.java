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

    public void QRselect(SQLiteDatabase db, String NumNakl){

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
        try{


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
                    KEY_QR_CODE + " text, " +
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

            checkDB.close();
            // checkDB = null;
            checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);

            String SqlTextPrice = "create table if not exists " + TABLE_DOCUMENT_PRICE + "(" + PRICE_BARCODE + " text, " +
                    PRICE_NAME_TOV + " text, " + PRICE_PRICE + " text, " + PRICE_DATA +  " text)";
            checkDB.execSQL(SqlTextPrice);

            checkDB.close();

        } catch (SQLiteException e) {
            if (checkDB != null) {
                checkDB.close();
            }
        }
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
//        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
//                ';', '\'', 0);
        //sStrok = reader.toString();
        // считываем данные с БД
        //db = dbHelper.getWritableDatabase();
        // db = dbHelper.getWritableDatabase();
        Integer ISSSSS = 0;
        List sdsdsdsdsd = reader.readAll();
        String dddd = sdsdsdsdsd.toString();
        while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
            ISSSSS++;
//            nextLine[0] = nextLine[0].replaceAll("\n","");
//            nextLine[1] = nextLine[1].replaceAll("\n","");
//            nextLine[2] = nextLine[2].replaceAll("\n","");
//            nextLine[3] = nextLine[3].replaceAll("\n","");
//            if (nextLine[1].length() > 71){
//                String S1 = nextLine[0];
//                String S2 = nextLine[1];
//                String S3 = nextLine[2];
//                String S4 = nextLine[3];
//            }

            //Thread.sleep(5000);
            ScontentValues = ContentValuesPriceCsv(nextLine);
            dbdb.insert(TableName, null, ScontentValues);
             //nextLine = null;
        }
        dbdb.close();
            return true;
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
    public ContentValues ContentValuesPriceCsv(String[] csvreader){

        ContentValues ScontentValues = new ContentValues();
        String S1 = csvreader[0];
        String S2 = csvreader[1];
        String S3 = csvreader[2];
        String S4 = csvreader[3];

        ScontentValues.put(DBHelper.PRICE_BARCODE, csvreader[0]);
        ScontentValues.put(DBHelper.PRICE_NAME_TOV, csvreader[1]);
        ScontentValues.put(DBHelper.PRICE_PRICE, csvreader[2]);
        ScontentValues.put(DBHelper.PRICE_DATA, csvreader[3]);


        return ScontentValues;
    }
}
