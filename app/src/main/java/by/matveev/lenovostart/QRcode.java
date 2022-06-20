package by.matveev.lenovostart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import by.matveev.lenovostart.lib.DBRepository;

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

            final DBRepository repositorys = new DBRepository(getApplicationContext());
            String dfdf;
            if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)){
//                ArrayAdapter<String> qradapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, repositorys.getDataDat());
                //qradapter.setDropDownViewResource(R.layout.simple_list_item_dat);
                //dbListView.setAdapter(qradapter);
                dfdf = repositorys.getDataQR().get(2);
                txtQRBarcode.setText(dfdf);
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

            }
         });
    }

    @Override
    public void onClick(View v) {

    }



}