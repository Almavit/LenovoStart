package by.matveev.lenovostart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QRcode extends AppCompatActivity implements View.OnClickListener{

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
                txtQRBarcode.setText("1234567890123");
            }
                return false;
            }
        });
        txtQR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
            public void onFocusChange(View v, boolean hasFocus){

            }
         });
    }

    @Override
    public void onClick(View v) {

    }



}