package by.matveev.lenovostart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;

public class QRcode extends AppCompatActivity implements View.OnClickListener{


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


    EditText txtQR;
    TextView txtQRBarcode;
    TextView txtQRNameTov;
    TextView txtQRNamePost;
    TextView txtQRPricePall;
    EditText txtQRPrice;
    TextView txtQRDate;
    TextView txtQRNumNakl;
    TextView txtQRNumPosition;

    Button btnQRSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        txtQR = (EditText) findViewById(R.id.txtQR);
        txtQRBarcode = (TextView) findViewById(R.id.txtQRBarcode);
        txtQRNameTov = (TextView) findViewById(R.id.txtQRNameTov);

        txtQR.setOnKeyListener(new View.OnKeyListener() {
        @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

            if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)){

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DBHelper.DATABASE_NAME, null, null);
                //final DBRepository repositorys = new DBRepository(getApplicationContext());
                String[] columnsName = new String[]{KEY_QR_CODE,KEY_NUM_NAKL,KEY_DATE, KEY_NAME_POST,
                        KEY_NUM_POZ,KEY_BARCODE,KEY_NAME_TOV,KEY_QUANTITY,KEY_STATUS};
                String sqlStroka = txtQR.getText().toString().replaceAll("\u001D","");
// символ \u001D

                sqlStroka = "select * from " + DBHelper.TABLE_DOCUMENT + " where " + DBHelper.KEY_QR_CODE + " = '" + sqlStroka + "'";


                //sqlStroka = " where " + DBHelper.KEY_QR_CODE + " = " + txtQR.getText().toString();
                Cursor cursor = db.rawQuery(sqlStroka,null);
                String dfdf = "0000000000000000000";
                Integer iCountFields = cursor.getCount();
                if ((cursor != null) && (cursor.getCount() > 0)) {
                    cursor.moveToFirst();

//                    do {
                    //iCountFields = cursor.getPosition();
                    dfdf = cursor.getString(0);
                    dfdf = cursor.getString(1);
                    dfdf = cursor.getString(2);
                    dfdf = cursor.getString(3);
                    dfdf = cursor.getString(4);
                    dfdf = cursor.getString(5);
                    dfdf = cursor.getString(6);
                    dfdf = cursor.getString(7);
                    dfdf = cursor.getString(8);
                    dfdf = cursor.getString(9);
                    txtQRNamePost.setText(cursor.getString(3));
                    txtQRBarcode.setText(cursor.getString(5));
                    txtQRNameTov.setText(cursor.getString(6));
                    txtQRPricePall.setText(cursor.getString(8));
                    //txtQRPrice.setText(cursor.getString(5));
//                    dfdf = cursor.getString(1);



                        //dfdf = cursor.getString(dfdf.getFieldCode()));

//                    } while (cursor.moveToNext());
                }
//                dfdf = repositorys.getDataQR().get(1);
//                dfdf = repositorys.getDataQR().get(2);
//                dfdf = repositorys.getDataQR().get(3);
              //  txtQRBarcode.setText(dfdf);
//                while ((repositorys = repositorys.getDataQR().) != null) {
//                    iCountStrok++;
//                }
            }
                return false;
            }
        });
        txtQR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
            public void onFocusChange(View v, boolean hasFocus){
                        String dfdf="23";
                        txtQRBarcode.setText(dfdf);

            }
         });
    }

    @Override
    public void onClick(View v) {
        String dfdf="1111111111";
        txtQRBarcode.setText(dfdf);
    }



}