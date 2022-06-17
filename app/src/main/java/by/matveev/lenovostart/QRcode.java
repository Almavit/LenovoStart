package by.matveev.lenovostart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
    }

    @Override
    public void onClick(View v) {

    }
}