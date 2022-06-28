package by.matveev.lenovostart.lib;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;



public class DBRepository {
    private SQLiteDatabase db;
    //private SQLiteDatabase Datdb;
    private Context cont;

    Integer iCountFields;

    String sssssss;

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

    public static  final String TABLE_DOCUMENT = "Document";

    public static  final String DAT_KEY_ID = "id";
    public static  final String DAT_KEY_BARCODE = "barcode";
    public static  final String DAT_KEY_PRICE = "price";
    public static  final String DAT_KEY_QUANTITY = "quantity";
    public static  final String DAT_KEY_NUMBER = "number";

    public static  final String DAT_TABLE_DOCUMENT = "Dat";

   // @SuppressLint("WrongConstant")
    public DBRepository(Context context) {
        //Подключение к базе данных
       db = SQLiteDatabase.openOrCreateDatabase(DBHelper.DATABASE_NAME, null, null);
//       Datdb = SQLiteDatabase.openOrCreateDatabase(DBHelper.DATABASE_NAME, null, null);
 /*        db.execSQL("create table " + TABLE_DOCUMENT + "(" +
//                KEY_ID + " integer primary key, " +
                KEY_NUM_NAKL + " text, " + KEY_DATE + " text, " + KEY_NAME_POST + " text, " +
                KEY_NUM_POZ + " text, " + KEY_BARCODE + " text, " + KEY_NAME_TOV + " text, " +
                KEY_PRICE + " text, " +
                KEY_QUANTITY + " text, " + KEY_STATUS + " text" + ")"  );*/
    //    db = new DBHelper(context).getWritableDatabase();
   //     cont = context;
       // return db;
    }
    //Создадим перечисление с полями таблицы Dat.
    public enum DatFields {

        DAT_KEY_ID(0),          // = "id";
        DAT_KEY_BARCODE(1),     // = "barcode";
        DAT_KEY_NUMBER(2),// = "number";
        DAT_KEY_QUANTITY(3),  // = "quantity";
        DAT_KEY_PRICE(4);  // = "price";

        DatFields(int i) {
            this.datfieldCode = i;
        }
        public int getFieldCode()
        {
            return datfieldCode;
        }
        private int datfieldCode;
    }
    //Создадим перечисление с полями таблицы Document.
    public enum Fields {
        KEY_QR_CODE(0), // = "";
        KEY_NUM_NAKL(1), // = "numnakl";
        KEY_DATE(2),     // = "date";
        KEY_NAME_POST(3),// = "namepost";
        KEY_NUM_POZ(4),  // = "numpoz";
        KEY_BARCODE(5),  // = "barcode";
        KEY_NAME_TOV(6), // = "nametov";
        //    KEY_PRICE(0), // = "price";
        KEY_QUANTITY(7), // = "quantity";
        KEY_STATUS(8);   // = "status";
        Fields(int i) {
            this.fieldCode = i;
        }
        public int getFieldCode()
        {
            return fieldCode;
        }
        private int fieldCode;
    }
    ////======================


    public ArrayList<String>  getDataQR(){
        String asdf;
        String[] columnsName = new String[]{KEY_QR_CODE,KEY_NUM_NAKL,KEY_DATE, KEY_NAME_POST,
                KEY_NUM_POZ,KEY_BARCODE,KEY_NAME_TOV,KEY_QUANTITY,KEY_STATUS};
        ArrayList<String> list = new ArrayList<String>();
        //columnsName = new String[]{fieldsColumns};
        Fields field = Fields.KEY_QR_CODE;

        //1

        Cursor cursor = db.query("Document",columnsName , null,null, KEY_BARCODE, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            iCountFields = cursor.getColumnCount();
            do {
                iCountFields = cursor.getPosition();

                asdf = cursor.getString(2);

                list.add(cursor.getString(field.getFieldCode()));

            } while (cursor.moveToNext());
        }
        return list;
    }



    public ArrayList<String> getDataAllNakld()
    {
       // String asdf;
        String naAAA;
        String[] columnsName = null;
        columnsName = new String[]{KEY_QR_CODE,KEY_NUM_NAKL};//, "count(*)"
        //columnsName = "numnakl";
        Fields field = Fields.KEY_NUM_NAKL;
        ArrayList<String> list = new ArrayList<String>();


        Cursor cursor = db.query("Document",columnsName , null,null, KEY_NUM_NAKL, null, null);
        iCountFields = cursor.getColumnCount();

        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            iCountFields = cursor.getCount();
            do {
                //iCountFields = cursor.getPosition();
                iCountFields = field.getFieldCode();
                sssssss = cursor.getString(1);

                list.add(cursor.getString(field.getFieldCode()));

            } while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<String> getDataDat()
    {

        Integer id;
        String barcode;
        String number;
        String quantity;
        String price;
        Integer iFields;

        String[] datcolumnsName = null;

        datcolumnsName = new String[]{DAT_KEY_ID,DAT_KEY_BARCODE, DAT_KEY_NUMBER, DAT_KEY_QUANTITY, DAT_KEY_PRICE};//,
        DatFields fieldid = DatFields.DAT_KEY_ID;
        DatFields fieldbarcose = DatFields.DAT_KEY_BARCODE;
        DatFields fieldnumber = DatFields.DAT_KEY_NUMBER;
        DatFields fieldquantity = DatFields.DAT_KEY_QUANTITY;
        DatFields fieldprice = DatFields.DAT_KEY_PRICE;

        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = db.query(DAT_TABLE_DOCUMENT,null , null,null, null, null, null);
        int iF = cursor.getCount();
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            iCountFields = cursor.getColumnCount();
            do {
                iCountFields = cursor.getPosition();
                String sField = cursor.getString(fieldid.getFieldCode()) + "   ;   " +
                        cursor.getString(fieldbarcose.getFieldCode()) + "   ;   " +
                        cursor.getString(fieldnumber.getFieldCode()) + "   ;   " +
                        cursor.getString(fieldquantity.getFieldCode()) + "   ;   " +
                        cursor.getString(fieldprice.getFieldCode());
                 list.add(sField);
            } while (cursor.moveToNext());
        }
        return list;
    }
//=========================================================|||||||
    public ArrayList<String> getDataNakld(String numNakl)
    {
        Integer iFields;
        String asdf;
        String naAAA;
        String nameTov;
        String namePostav;
        String numPoz;
        String Barcodes;
        String DataNakl;
        String numNakldn;
        String quntityTov;
        String statusTov;
        String[] columnsName = null;
        String[] columns = null;
        columnsName = new String[]{KEY_NUM_NAKL};//, "count(*)"
        // columnsName = "numnakl";
        Fields numNakld = Fields.KEY_NUM_NAKL;
        Fields datNakld = Fields.KEY_DATE;
        Fields postavNakld = Fields.KEY_NAME_POST;
        Fields numPozNakld = Fields.KEY_NUM_POZ;
        Fields barcodeNakld = Fields.KEY_BARCODE;
        Fields nameTovNakld = Fields.KEY_NAME_TOV;
        Fields quntityNakld = Fields.KEY_QUANTITY;
        Fields statusNakld = Fields.KEY_STATUS;


        columns = new String[] {KEY_NUM_NAKL + ", " + KEY_DATE + ", " + KEY_NAME_POST + ", " + KEY_NUM_POZ + ", " + KEY_BARCODE + ", " + KEY_NAME_TOV + ", " + KEY_QUANTITY + ", " + KEY_STATUS};

        ArrayList<String> list = new ArrayList<String>();

        // cursor help http://developer.alexanderklimov.ru/android/sqlite/cursor.php
        Cursor cursor = db.query("Document",null, KEY_NUM_NAKL + " = " +
                numNakl, null, null, null, KEY_NUM_POZ);


        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            iCountFields = cursor.getColumnCount();
            iCountFields = cursor.getCount();
            iFields = Fields.values().length;

            iCountFields = cursor.getPosition();

                    list.add(cursor.getString(numNakld.getFieldCode()));
                    list.add(cursor.getString(datNakld.getFieldCode()));
        }
        return list;
    }




}