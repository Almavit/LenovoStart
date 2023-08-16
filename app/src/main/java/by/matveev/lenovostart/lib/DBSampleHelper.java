package by.matveev.lenovostart.lib;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DBSampleHelper {

    public static class DBConnectIP implements BaseColumns {



        public static final String TABLE_IP = "IPtable";

        public static final String IP_NUMMAG = "nummag";
        public static final String IP_MASK = "ipmask";
        public static final String IP_SERVER = "ipserver";
        public static final String IP_MODEM = "ipmodem";
        public static final String IP_SCANER = "ipscaner";
        public static final String IP_WIFI = "ipwifi";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_IP + " (" + IP_NUMMAG + " TEXT, " +
                IP_MASK + " TEXT, " + IP_SERVER + " TEXT, " + IP_MODEM + " TEXT, " + IP_SCANER + " TEXT," + IP_WIFI + " TEXT" +")";
        public static final String DROP_TABLE = "drop table " + TABLE_IP;
    }

    public static class DBPrice implements BaseColumns {

        public static final String TABLE_DOCUMENT_PRICE = "Price";

        public static final String PRICE_BARCODE = "barcode";
        public static final String PRICE_NAME_TOV = "nametov";
        public static final String PRICE_PRICEOTP = "priceotp";
        public static final String PRICE_PRICE = "price";
        public static final String PRICE_DATA = "data";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_DOCUMENT_PRICE + "(" + PRICE_BARCODE + " TEXT, " +
                PRICE_NAME_TOV + " TEXT, " + PRICE_PRICEOTP + " TEXT, " + PRICE_PRICE + " TEXT, " + PRICE_DATA + " TEXT" + ")";

        public static final String DROP_TABLE = "drop table if exists" + TABLE_DOCUMENT_PRICE;

    }


    public static class DBDat implements BaseColumns {

        public static final String TABLE_DOCUMENT_DAT = "Dat";

        public static final String DAT_KEY_ID = "datid";
        public static final String DAT_KEY_BARCODE = "datbarcode";
        public static final String DAT_KEY_PRICE = "datprice";
        public static final String DAT_KEY_QUANTITY = "datquantity";
        public static final String DAT_KEY_POSITION = "datposition";


        public static final String CREATE_TABLE = "create table if not exists " + TABLE_DOCUMENT_DAT + "(" + DAT_KEY_ID + " INTEGER, " +
                DAT_KEY_BARCODE + " TEXT, " + DAT_KEY_PRICE + " TEXT, " + DAT_KEY_QUANTITY + " TEXT, " +
                DAT_KEY_POSITION + " TEXT" + ")";
        public static final String DROP_TABLE = "drop table if exists" + TABLE_DOCUMENT_DAT;


    }

    public static final class DBQTable implements BaseColumns {

        public static final String TABLE_DOCUMENT = "Document";

        public static final String KEY_QR_CODE = "qrcode";
        public static final String KEY_NUM_NAKL = "numnakl";
        public static final String KEY_DATE = "date";
        public static final String KEY_NAME_POST = "namepost";
        public static final String KEY_NUM_POZ = "numpoz";
        public static final String KEY_BARCODE = "barcode";
        public static final String KEY_NAME_TOV = "nametov";
        public static final String KEY_PRICE = "price";
        public static final String KEY_PRICEOTP = "priceotp";
        public static final String KEY_QUANTITY = "quantity";
        public static final String KEY_STATUS = "status";


        public static final String CREATE_TABLE = "create table if not exists " + TABLE_DOCUMENT + "(" +
                KEY_QR_CODE + " TEXT, " +
                KEY_NUM_NAKL + " TEXT, " + KEY_DATE + " TEXT, " + KEY_NAME_POST + " TEXT, " +
                KEY_NUM_POZ + " TEXT, " + KEY_BARCODE + " TEXT, " + KEY_NAME_TOV + " TEXT, " +
                KEY_PRICEOTP + " TEXT, " + KEY_PRICE + " TEXT, " +
                KEY_QUANTITY + " TEXT, " + KEY_STATUS + " TEXT" + ")";

        public static final String DROP_TABLE = "drop table if exists" + TABLE_DOCUMENT;

    }
}
