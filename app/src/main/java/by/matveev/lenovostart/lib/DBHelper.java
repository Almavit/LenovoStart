package by.matveev.lenovostart.lib;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
//import static android.content.ContextWrapper.*;
//import static android.content.Context.*;

//import static android.content.Context.MODE_PRIVATE;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;


import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DIR_SD = "Documents";
    public static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/" + DIR_SD + "/" + "base/" + "DocumentDB.db";

    public String setTime = "";
    ContentValues ScontentValues = new ContentValues();
    private SQLiteDatabase dataBase;

    private final Context fContext;

    private SQLiteDatabase myDataBase;
    String DB_PATH = null;

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.fContext = context;
    }

    public ArrayList getSelectIPMask(Context contex, String sIPMask){
        int asd;
        ArrayList<String> list = new ArrayList<String>();
        DBHelper dbHelper = new DBHelper(contex);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DBRepository.WiFiFields nummag = DBRepository.WiFiFields.IP_KEY_NUMMAG;
        DBRepository.WiFiFields ipmask = DBRepository.WiFiFields.IP_KEY_MASK;
        DBRepository.WiFiFields ipserver = DBRepository.WiFiFields.IP_KEY_SERVER;
        DBRepository.WiFiFields ipmodem = DBRepository.WiFiFields.IP_KEY_MODEM;
        DBRepository.WiFiFields ipscaner = DBRepository.WiFiFields.IP_KEY_SCANER;
        DBRepository.WiFiFields ipwifi = DBRepository.WiFiFields.IP_KEY_WIFI;

        Cursor cursor = db.query(true, DBSampleHelper.DBConnectIP.TABLE_IP,
                new String[]{DBSampleHelper.DBConnectIP.IP_NUMMAG, DBSampleHelper.DBConnectIP.IP_MASK,
                        DBSampleHelper.DBConnectIP.IP_SERVER, DBSampleHelper.DBConnectIP.IP_MODEM,
                        DBSampleHelper.DBConnectIP.IP_SCANER, DBSampleHelper.DBConnectIP.IP_WIFI},
                DBSampleHelper.DBConnectIP.IP_MASK + " = ?",
                new String[]{sIPMask}, null, null, null, null);


        if ((cursor != null) && (cursor.getCount() > 0)) {

            cursor.moveToFirst();

            String sField = cursor.getString(nummag.getFieldCode());// + "   ;   " +
            list.add(sField);
            sField = cursor.getString(ipmask.getFieldCode());// + "   ;   " +
            list.add(sField);
            sField = cursor.getString(ipserver.getFieldCode());// + "   ;   " +
            list.add(sField);
            sField = cursor.getString(ipmodem.getFieldCode());// + "   ;   " +
            list.add(sField);
            sField = cursor.getString(ipscaner.getFieldCode());
            list.add(sField);
            sField = cursor.getString(ipwifi.getFieldCode());
            list.add(sField);
//            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList getSelectIPName(Context contex, String sIPName){
        ArrayList<String> list = new ArrayList<String>();
        DBHelper dbHelper = new DBHelper(contex);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DBRepository.WiFiFields nummag = DBRepository.WiFiFields.IP_KEY_NUMMAG;
        DBRepository.WiFiFields ipmask = DBRepository.WiFiFields.IP_KEY_MASK;
        DBRepository.WiFiFields ipserver = DBRepository.WiFiFields.IP_KEY_SERVER;
        DBRepository.WiFiFields ipmodem = DBRepository.WiFiFields.IP_KEY_MODEM;
        DBRepository.WiFiFields ipscaner = DBRepository.WiFiFields.IP_KEY_SCANER;
        DBRepository.WiFiFields ipwifi = DBRepository.WiFiFields.IP_KEY_WIFI;

        Cursor cursor = db.query(true, DBSampleHelper.DBConnectIP.TABLE_IP,
                new String[]{DBSampleHelper.DBConnectIP.IP_NUMMAG, DBSampleHelper.DBConnectIP.IP_MASK,
                        DBSampleHelper.DBConnectIP.IP_SERVER, DBSampleHelper.DBConnectIP.IP_MODEM,
                        DBSampleHelper.DBConnectIP.IP_SCANER, DBSampleHelper.DBConnectIP.IP_WIFI},
                DBSampleHelper.DBConnectIP.IP_NUMMAG + " = ?",
                new String[]{sIPName}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {

            cursor.moveToFirst();

            String sField = cursor.getString(nummag.getFieldCode());// + "   ;   " +

            list.add(sField);
            sField = cursor.getString(ipmask.getFieldCode());// + "   ;   " +
            list.add(sField);
            sField = cursor.getString(ipserver.getFieldCode());// + "   ;   " +
            list.add(sField);
            sField = cursor.getString(ipmodem.getFieldCode());// + "   ;   " +
            list.add(sField);
            sField = cursor.getString(ipscaner.getFieldCode());
            list.add(sField);
            sField = cursor.getString(ipwifi.getFieldCode());
            list.add(sField);
//            } while (cursor.moveToNext());
        }

        return list;
    }
    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() {
        // String myPath = DATABASE_NAME;
        SQLiteDatabase checkDB = null;
        try {


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

//            String SqlText = "create table if not exists " + TABLE_DOCUMENT + "(" +
////                KEY_ID + " integer primary key, " +
//                    KEY_QR_CODE + " text, " +
//                    KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
//                    KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
//                    KEY_PRICEOTP + " text, " + KEY_PRICE + " text, " +
//                    KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")";
//            checkDB.execSQL(SqlText);
//
//            checkDB.close();
            // checkDB = null;
//            checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);
//
//
//            String SqlTextDat = "create table if not exists " + TABLE_DOCUMENT_DAT + "(" + DAT_KEY_ID + " integer, " +
//                    DAT_KEY_BARCODE + " text, " + DAT_KEY_PRICE + " text, " + DAT_KEY_QUANTITY + " text, " +
//                    DAT_KEY_POSITION + " text)";
//            checkDB.execSQL(SqlTextDat);
//
//            checkDB.close();

            //checkDB.close();
            // checkDB = null;
//            checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);
//
//            String SqlTextIP = "create table if not exists " + TABLE_IP + "(" + IP_NUMMAG + " text, " +
//                    IP_MASK + " text, " + IP_SERVER + " text, " + IP_MODEM + " text, " + IP_SCANER + " text)";
//            checkDB.execSQL(SqlTextIP);
//
//            checkDB.close();

            //checkDB.close();
            // checkDB = null;
//            checkDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, null);
//
//            String SqlTextPrice = "create table if not exists " + TABLE_DOCUMENT_PRICE + "(" + PRICE_BARCODE + " text, " +
//                    PRICE_NAME_TOV + " text, " + PRICE_PRICEOTP + " text, " + PRICE_PRICE + " text, " + PRICE_DATA + " text)";
//            checkDB.execSQL(SqlTextPrice);


            Cursor cursor = null;
            try {
                String column = "";
                String name = "";
                cursor = checkDB.rawQuery("PRAGMA table_info(" + DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE + ")", null);//проверка состояния таблицы
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
//
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


       // db.execSQL(DBSampleHelper.DBConnectIP.DROP_TABLE);
        db.execSQL(DBSampleHelper.DBConnectIP.CREATE_TABLE);
        db.execSQL(DBSampleHelper.DBPrice.CREATE_TABLE);
        db.execSQL(DBSampleHelper.DBDat.CREATE_TABLE);
        db.execSQL(DBSampleHelper.DBQTable.CREATE_TABLE);
    }

    public boolean DBSaveData(Context contex, String TableName, String SqlStroka, CSVReader reader) {
        DBHelper dbHelper = new DBHelper(contex);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (TableName.equals(DBSampleHelper.DBConnectIP.TABLE_IP)) {
            if(!WhileTableWrite(contex, db, TableName, reader)){
                return false;
            }
        }
        if (TableName.equals(DBSampleHelper.DBQTable.TABLE_DOCUMENT)) {

        }
        if (TableName.equals(DBSampleHelper.DBDat.TABLE_DOCUMENT_DAT)) {

        }
        if (TableName.equals(DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE)) {

        }

        return true;
    }

    public boolean DeleteDB(Context context, String tableName){
        DBHelper dbHelper = new DBHelper(context);
        boolean status = false;
        SQLiteDatabase dd = dbHelper.getWritableDatabase();//getReadableDatabase
        //DBHelper dbHelper = new DBHelper(context);
        try {
            dd.close();
            dd = dbHelper.getWritableDatabase();
            Integer iCode = dd.delete(tableName, null, null);
            // iCode  количество удаленных записей
            if (iCode < 0){
                return status;
            }else{
                status = true;
            }

        }finally {
            dd.close();
            //dbdb = this.getWritableDatabase();
        }

        return status;
    }
    public boolean WhileTableWrite(Context contex, SQLiteDatabase db, String TableName, CSVReader reader) {
        String[] nextLine = null;
        Long iSSS;

       // DBHelper dbHelper = new DBHelper(contex);
     //   SQLiteDatabase dbdb = dbHelper.getWritableDatabase();
        //dbdb.execSQL("DROP TABLE IF EXISTS " + DropTableName);
        db.delete(TableName, null, null);
    //    dbdb.close();
     //   dbdb = dbHelper.getWritableDatabase();
//        ScontentValues = ValuesIPSetting(reader);
        // iSSS = db.insert(TableName, null, ScontentValues);
//        if (iSSS <= 0) {
//            db.close();
//            return false;
//        }
        while (true) {
            try {
                if (!((nextLine = reader.readNext()) != null))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }// считываем данные с CSV  файла
            ScontentValues = ContentValuesIPSetting(nextLine);
            iSSS = db.insert(TableName, null, ScontentValues);
            if (iSSS <= 0) {
                db.close();
                return false;
            }
        }

        return true;
    }

    public boolean SaveDataIP(Context contex, String TableName, String SqlStroka, CSVReader reader) throws IOException, InterruptedException {
        Integer iFor = 0;
        Long codeError;
        DBHelper dbHelper = new DBHelper(contex);
        String[] nextLine = null;
        SQLiteDatabase dbdb = dbHelper.getWritableDatabase();//getReadableDatabase
        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();
        dbdb.delete(TableName, null, null);
        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();

        while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
            ScontentValues = ContentValuesIPSetting(nextLine);
            codeError = dbdb.insert(TableName, null, ScontentValues);
            iFor++;
            if (codeError <= 0) {
                dbdb.close();
                return false;
            }
            //nextLine = null;
        }
        dbdb.close();
        return true;
    }

    public boolean SaveDataPrice(Context contex, String TableName, String SqlStroka, CSVReader reader) throws IOException, InterruptedException {
        DBHelper dbHelper = new DBHelper(contex);
        Integer iCo = 0;
      //  SQLiteDatabase checkDB = null;
        String[] nextLine = null;
        Long iCodeError;
        Integer iCount = 0;
        SQLiteDatabase dbdb = dbHelper.getWritableDatabase();//getReadableDatabase
//        dbdb.close();
        if(!DeleteDB(contex, TableName)){

            return false;
        }
//        dbdb = dbHelper.getWritableDatabase();
//        dbdb.delete(TableName, null, null);
//        dbdb.close();
//        dbdb = dbHelper.getWritableDatabase();
//        dbdb.close();
//        dbdb = dbHelper.getWritableDatabase();
///////////////////////////////////////////

        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(dbdb, TableName);
        // Get the numeric indexes for each of the columns that we're updating
        final int barcode = ih.getColumnIndex(DBSampleHelper.DBPrice.PRICE_BARCODE);
        final int nametov = ih.getColumnIndex(DBSampleHelper.DBPrice.PRICE_NAME_TOV);
        final int priceotp = ih.getColumnIndex(DBSampleHelper.DBPrice.PRICE_PRICEOTP);
        final int price = ih.getColumnIndex(DBSampleHelper.DBPrice.PRICE_PRICE);
        final int pricedata = ih.getColumnIndex(DBSampleHelper.DBPrice.PRICE_DATA);
        final long startTime = System.currentTimeMillis();
        try {
            dbdb.execSQL("PRAGMA synchronous=OFF");
            dbdb.setLockingEnabled(false);
            dbdb.beginTransaction();
            while (((nextLine = reader.readNext()) != null)) {
                // ... Create the data for this row (not shown) ...
                // Get the InsertHelper ready to insert a single row
                try{
                    if(!nextLine[0].equals("")) {
                        iCo++;
                        ih.prepareForInsert();
                        // Add the data for each column
                        ih.bind(barcode, nextLine[0]);
                        ih.bind(nametov, nextLine[1]);
                        ih.bind(priceotp, nextLine[2]);
                        ih.bind(price, nextLine[3]);
                        ih.bind(pricedata, nextLine[4]);
                        // Insert the row into the database.
                        ih.execute();
                    }
                }catch (Exception e){

                    return false;
                }
            }
            dbdb.setTransactionSuccessful();
        } finally {
            dbdb.endTransaction();
            dbdb.setLockingEnabled(true);
            dbdb.execSQL("PRAGMA synchronous=NORMAL");
            ih.close();
            dbdb.close();
            final long endtime = System.currentTimeMillis();
            long millis = endtime - startTime;  // obtained from StopWatch
            long minutes = (millis / 1000)  / 60;
            int seconds = (int)((millis / 1000) % 60);
            setTime = String.valueOf(minutes) + " минут " + String.valueOf(seconds) + " секунд";
        }
////////////////////////////////////////
       // dbdb.execSQL("PRAGMA synchronous=NORMAL");
//        dbdb.execSQL("PRAGMA synchronous=OFF");
//        dbdb.beginTransaction();
//        dbdb.setLockingEnabled(false);
//
//
//        try{
//            while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
//
//                if(!nextLine[0].equals("")) {
//                    try{
//                        ScontentValues = ContentValuesPriceCsv(nextLine);
//                        iCodeError = dbdb.insert(TableName, null, ScontentValues);
//                        iCo++;
//
//                        if (iCodeError <= 0) {
//                            dbdb.close();
//                            return false;
//                        }
//                    }catch (Exception e){
//                        return false;
//                    }
//                }
//            }
//
//        }finally {
//            final long endtime = System.currentTimeMillis();
//            long millis = endtime - startTime;  // obtained from StopWatch
//            long minutes = (millis / 1000)  / 60;
//            int seconds = (int)((millis / 1000) % 60);
//            dbdb.setLockingEnabled(true);
//            dbdb.endTransaction();
//            setTime = String.valueOf(minutes) + " минут " + String.valueOf(seconds) + " секунд";
//            //dbdb.execSQL("PRAGMA synchronous=NORMAL");
//            Integer iNumDB = iCo;
//            dbdb.close();
//
//         //   if (Globals.ENABLE_LOGGING) {
//        //    }
//
//        }

        return true;
    }

    public boolean SaveDataDat(Context contex, String TableName, String SqlStroka, CSVReader reader) throws IOException {
        DBHelper dbHelper = new DBHelper(contex);
        String[] nextLine = null;
        String[] sID = new String[1];
        SQLiteDatabase dbdb = dbHelper.getWritableDatabase();//getReadableDatabase

        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();
        dbdb.delete(TableName, null, null);
        dbdb.close();
        dbdb = dbHelper.getWritableDatabase();
        Integer iFor = 0;


        while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
            iFor++;
            String str = Integer.toString(iFor);

            sID[0] = str;

            nextLine = concatArray(sID, nextLine);

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


    public ContentValues ContentValuesQRCsv(String[] csvreader) {

        ContentValues ScontentValues = new ContentValues();

        ScontentValues.put(DBSampleHelper.DBQTable.KEY_QR_CODE, csvreader[0]);
        ScontentValues.put(DBSampleHelper.DBQTable.KEY_NUM_NAKL, csvreader[1]);
        ScontentValues.put(DBSampleHelper.DBQTable.KEY_DATE, csvreader[2]);
        ScontentValues.put(DBSampleHelper.DBQTable.KEY_NAME_POST, csvreader[3]);
        ScontentValues.put(DBSampleHelper.DBQTable.KEY_NUM_POZ, csvreader[4]);
        ScontentValues.put(DBSampleHelper.DBQTable.KEY_BARCODE, csvreader[5]);
        ScontentValues.put(DBSampleHelper.DBQTable.KEY_NAME_TOV, csvreader[6]);
        ScontentValues.put(DBSampleHelper.DBQTable.KEY_QUANTITY, csvreader[7]);
        ScontentValues.put(DBSampleHelper.DBQTable.KEY_STATUS, csvreader[8]);

        return ScontentValues;
    }

    public ContentValues ContentValuesDat(String[] csvreader) {

        ContentValues ScontentValues = new ContentValues();

        ScontentValues.put(DBSampleHelper.DBDat.DAT_KEY_ID, csvreader[0]);
        ScontentValues.put(DBSampleHelper.DBDat.DAT_KEY_BARCODE, csvreader[1]);
        ScontentValues.put(DBSampleHelper.DBDat.DAT_KEY_PRICE, csvreader[2]);
        ScontentValues.put(DBSampleHelper.DBDat.DAT_KEY_QUANTITY, csvreader[3]);
        ScontentValues.put(DBSampleHelper.DBDat.DAT_KEY_POSITION, csvreader[4]);

        return ScontentValues;
    }

    public ContentValues ContentValuesPriceCsv(String[] csvreader) {


        ContentValues ScontentValues = new ContentValues();

        ScontentValues.put(DBSampleHelper.DBPrice.PRICE_BARCODE, csvreader[0]);
        ScontentValues.put(DBSampleHelper.DBPrice.PRICE_NAME_TOV, csvreader[1]);
        ScontentValues.put(DBSampleHelper.DBPrice.PRICE_PRICEOTP, csvreader[2]);
        ScontentValues.put(DBSampleHelper.DBPrice.PRICE_PRICE, csvreader[3]);
        ScontentValues.put(DBSampleHelper.DBPrice.PRICE_DATA, csvreader[4]);

        return ScontentValues;
    }

    public ContentValues ContentValuesIPSetting(String[] settingIPreader) {

        ContentValues ScontentValues = new ContentValues();

        ScontentValues.put(DBSampleHelper.DBConnectIP.IP_NUMMAG, settingIPreader[0]);
        ScontentValues.put(DBSampleHelper.DBConnectIP.IP_MASK, settingIPreader[1]);
        ScontentValues.put(DBSampleHelper.DBConnectIP.IP_SERVER, settingIPreader[2]);
        ScontentValues.put(DBSampleHelper.DBConnectIP.IP_MODEM, settingIPreader[3]);
        ScontentValues.put(DBSampleHelper.DBConnectIP.IP_SCANER, settingIPreader[4]);
        ScontentValues.put(DBSampleHelper.DBConnectIP.IP_WIFI, settingIPreader[5]);

        return ScontentValues;
    }


//    public ContentValues ValuesIPSetting(CSVReader reader) {
//        String[] nextLine = null;
//        ContentValues ScontentValues = new ContentValues();
//
//        while (true) {
//            try {
//                if (!((nextLine = reader.readNext()) != null))
//                    break;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }// считываем данные с CSV  файла
//            ScontentValues.put(DBSampleHelper.DBConnectIP.IP_NUMMAG, nextLine[0]);
//            ScontentValues.put(DBSampleHelper.DBConnectIP.IP_MASK, nextLine[1]);
//            ScontentValues.put(DBSampleHelper.DBConnectIP.IP_SERVER, nextLine[2]);
//            ScontentValues.put(DBSampleHelper.DBConnectIP.IP_MODEM, nextLine[3]);
//            ScontentValues.put(DBSampleHelper.DBConnectIP.IP_SCANER, nextLine[4]);
//            ScontentValues.put(DBSampleHelper.DBConnectIP.IP_WIFI, nextLine[5]);
//
//        }
//        return ScontentValues;
//    }
}
