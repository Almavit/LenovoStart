package by.matveev.lenovostart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;
import by.matveev.lenovostart.lib.DBSampleHelper;
import by.matveev.lenovostart.lib.Filealmat;
import by.matveev.lenovostart.lib.Setting;

public class ConnectMag extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnLoadConnect;
    Button btnSaveConnect;
    Button btnUpdateConnect;

    Filealmat filealmat;

    EditText txtIPmask;
    EditText txtIpmag;
    EditText txtIpmodem;
    EditText txtIpscaner;
    TextView txtlogConnect;

    Spinner spinMag;

//    String[] data = {"61", "62", "63", "63 филиал", "64", "65", "66", "69", "71", "72", "73", "73 филиал", "74", "76", "77", "79", "80", "81", "81 филиал", "82", "83",
//            "84", "85", "86", "87", "88", "120", "121", "122", "123", "124", "125", "125 филиал", "127", "128", "129", "129 филиал", "130", "131",
//            "132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "144", "145", "146", "147", "148", "149",
//            "150", "151", "152", "153", "154", "155", "156", "157", "158", "Гомель 1", "Гомель 2", "Гродно 1",
//            "Светлогорск 1", "Светлогорск 2", "Светлогорск 3", "Светлогорск 4", "Светлогорск 5", "Новополоцк 1", "Новополоцк 2", "Новополоцк 3",
//            "Новополоцк 4", "Новополоцк 5", "Новополоцк 6", "Новополоцк 7", "Полоцк 1"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_mag);

        //        public static final String IP_NUMMAG = "nummag";
//        public static final String IP_MASK = "ipmask";
//        public static final String IP_SERVER = "ipmag";
//        public static final String IP_MODEM = "ipmodem";
//        public static final String IP_SCANER = "ipscaner";

        txtlogConnect = (TextView) findViewById(R.id.txtlogConnect);


        txtIPmask = (EditText) findViewById(R.id.txtIPmask);
        //      txtIPmask.setOnClickListener(this);
        txtIpmag = (EditText) findViewById(R.id.txtIpmag);
        //      txtIpmag.setOnClickListener(this);
        txtIpmodem = (EditText) findViewById(R.id.txtIpmodem);
        //     txtIpmodem.setOnClickListener(this);
        txtIpscaner = (EditText) findViewById(R.id.txtIpscaner);
        //       txtIpscaner.setOnClickListener(this);

        btnLoadConnect = (Button) findViewById(R.id.btnLoadConnect);
        //      btnLoadConnect.setOnClickListener(this);

        btnSaveConnect = (Button) findViewById(R.id.btnSaveConnect);
        //       btnSaveConnect.setOnClickListener(this);

        btnUpdateConnect = (Button) findViewById(R.id.btnUpdateConnect);
        //       btnUpdateConnect.setOnClickListener(this);

        spinMag = (Spinner) findViewById(R.id.spinMag);
        spinMag.setOnItemSelectedListener(this);
//        spinMag.setOnItemClickListener itemSelectedListerner = new spinMag.getOnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0){
//
//            }
//
//        };
////        spinMag.setOnItemSelectedListener(new AdapterView.OnItemClickListener() {
//
//
//
////        }
    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.btnUpdateConnect:
//
//                break;
//            case R.id.btnSaveConnect:
//                try {
//
//                    String[] stringsetting = new String[6];
//                    stringsetting[0] =
//                            stringsetting[0] = txtIPmask.getText().toString();
//                    stringsetting[1] = txtIpmag.getText().toString();
//                    stringsetting[2] = txtIpmodem.getText().toString();
//                    stringsetting[3] = txtIpscaner.getText().toString();
//                    Setting setting = new Setting();
//                    setting.nextLine = stringsetting;
//                    if (!setting.saveSetting(this)) {
//                        Toast.makeText(getApplicationContext(), "НАСТРОЙКИ НЕ СОХРАНЕНЫ",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    } else {
//                        Toast.makeText(getApplicationContext(), "НАСТРОЙКИ СОХРАНЕНЫ",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.btnLoadConnect:
//                filealmat = new Filealmat();
//                txtlogConnect.setText("       ...       ");
//                txtlogConnect.setBackgroundColor(Color.WHITE);
//// запускаем длительную операцию
//
//                Toast.makeText(this, "ЖДИТЕ! ИДЕТ ЗАГРУЗКА ДАННЫХ", Toast.LENGTH_LONG);
//
//
//                try {
//                    if (filealmat.LoadSaveCsvToDB(this, filealmat.NameDirectory, filealmat.NameFileCSV_IP,
//                            "select * from " + DBSampleHelper.DBConnectIP.TABLE_IP, DBSampleHelper.DBConnectIP.TABLE_IP)) {
//
//                            if(AdapterWiFi()) {
//
//                                txtlogConnect.setText("ДАННЫЕ ОБНОВЛЕНЫ");
//                                Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
//                                txtlogConnect.setBackgroundColor(Color.GREEN);
//                            }
//                        //    textview.setBackgroundColor(Color.WHITE);
//                        //    btnLoadAll.getBackground().setColorFilter(Color.parseColor("Данные обновлены"), PorterDuff.Mode.DARKEN);
//                    } else {
//                        txtlogConnect.setText("ДАННЫЕ НЕ ЗАГРУЖЕНЫ!");
//                        txtlogConnect.setBackgroundColor(Color.RED);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//        }
//    }
//


    public void SaveConnect(View v) {
        try {

            String[] stringsetting = new String[6];
            stringsetting[0] =
                    stringsetting[0] = txtIPmask.getText().toString();
            stringsetting[1] = txtIpmag.getText().toString();
            stringsetting[2] = txtIpmodem.getText().toString();
            stringsetting[3] = txtIpscaner.getText().toString();
            Setting setting = new Setting();
            setting.nextLine = stringsetting;
            if (!setting.saveSetting(this)) {
                Toast.makeText(getApplicationContext(), "НАСТРОЙКИ НЕ СОХРАНЕНЫ",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "НАСТРОЙКИ СОХРАНЕНЫ",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void LoadAllConnect(View v) {

        filealmat = new Filealmat();
        txtlogConnect.setText("       ...       ");
        txtlogConnect.setBackgroundColor(Color.WHITE);
// запускаем длительную операцию

        Toast.makeText(this, "ЖДИТЕ! ИДЕТ ЗАГРУЗКА ДАННЫХ", Toast.LENGTH_LONG);


        try {
            if (filealmat.LoadSaveCsvToDB(this, filealmat.NameDirectory, filealmat.NameFileCSV_IP,
                    "select * from " + DBSampleHelper.DBConnectIP.TABLE_IP, DBSampleHelper.DBConnectIP.TABLE_IP)) {

                if (AdapterWiFi()) {

                    txtlogConnect.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                    Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
                    txtlogConnect.setBackgroundColor(Color.GREEN);
                }
                //    textview.setBackgroundColor(Color.WHITE);
                //    btnLoadAll.getBackground().setColorFilter(Color.parseColor("Данные обновлены"), PorterDuff.Mode.DARKEN);
            } else {
                txtlogConnect.setText("ДАННЫЕ НЕ ЗАГРУЖЕНЫ!");
                txtlogConnect.setBackgroundColor(Color.RED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // return false;
    }

    ;

    public boolean AdapterWiFi() {
        final DBRepository repositorys = new DBRepository(getApplicationContext());
        ArrayAdapter<String> datadapterdat;
        ArrayAdapter<String> datadapter;

        datadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, repositorys.getDataWifi());
        datadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinMag.setAdapter(datadapter);

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = spinMag.getSelectedItem().toString();
        Integer posss = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}