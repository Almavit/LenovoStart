package by.matveev.lenovostart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import by.matveev.lenovostart.lib.DBSampleHelper;

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
  //  TextView txtQRPrice;
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
        txtQRBarcode = (TextView) findViewById(R.id.txtQRBarcode);
        txtQRNameTov = (TextView) findViewById(R.id.txtQRNameTov);

        txtQRNamePost = (TextView) findViewById(R.id.txtQRNamePost);
        txtQRPricePall = (TextView) findViewById(R.id.txtQRPricePall);

        //   txtQRPrice = (TextView) findViewById(R.id.txtQRPrice);
        txtQRDate = (TextView) findViewById(R.id.txtQRDate);
        txtQRNumNakl = (TextView) findViewById(R.id.txtQRNumNakl);
      //  txtQRNumPosition = (TextView) findViewById(R.id.txtQRNumPosition);


        txtQR.setOnKeyListener(new View.OnKeyListener() {
        @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

            if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)){
                //txtQR.setText("");
                txtQR.setBackgroundColor(Color.WHITE);
                String sqlStroka = txtQR.getText().toString();
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DBHelper.DATABASE_NAME, null, null);
//                String[] columnsName = new String[]{KEY_QR_CODE,KEY_NUM_NAKL,KEY_DATE, KEY_NAME_POST,KEY_NUM_POZ,
//                                                    KEY_BARCODE,KEY_NAME_TOV,KEY_QUANTITY,KEY_STATUS};
                sqlStroka = txtQR.getText().toString().replaceAll("\n","");// очистить от символа \n
                txtQR.setText(sqlStroka);
                sqlStroka = txtQR.getText().toString().replaceAll("\u001D","");// очистить от символа \u001D
                sqlStroka = "select * from " + DBSampleHelper.DBQTable.TABLE_DOCUMENT + " where " + DBSampleHelper.DBQTable.KEY_QR_CODE + " = '" + sqlStroka + "'";// создать строку SQL запроса
                Cursor cursor = db.rawQuery(sqlStroka,null);
                if ((cursor != null) && (cursor.getCount() > 0)) {
                    cursor.moveToFirst();
                    //txtQR.setText(cursor.getString(0));
                    txtQRNumNakl.setText(cursor.getString(1));
                    txtQRDate.setText(cursor.getString(2));
                    txtQRNamePost.setText(cursor.getString(3));
                    txtQRBarcode.setText(cursor.getString(5));
                    txtQRNameTov.setText(cursor.getString(6));
                    txtQRPricePall.setText(cursor.getString(8));
                    //txtQRPrice.setText("0.00");
                    //txtQR.setSelection(1, 33);
                    txtQR.getText().clear();
                    db.close();
                }else{
                    sqlStroka = txtQR.getText().toString().replaceAll("\u001D","");// очистить от символа \u001D
                    txtQR.setBackgroundColor(Color.RED);
                    txtQR.getText().clear();;
                    txtQRNumNakl.setText("");
                    txtQRDate.setText("");
                    txtQRNamePost.setText("");
                    txtQRBarcode.setText("");
                    txtQRNameTov.setText("");
                    txtQRPricePall.setText("");
                    //txtQR.selectAll();
                }
            }
                return false;
            }
        });
        txtQR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
            public void onFocusChange(View v, boolean hasFocus){
                      //  String dfdf="23";
                      //  txtQRBarcode.setText(dfdf);
                       // txtQR.selectAll();

            }
         });
    }

    @Override
    public void onClick(View v) {
     //   String dfdf="1111111111";
     //   txtQRBarcode.setText(dfdf);
        txtQR.selectAll();
    }



}