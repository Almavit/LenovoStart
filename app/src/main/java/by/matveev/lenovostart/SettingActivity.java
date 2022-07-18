package by.matveev.lenovostart;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import by.matveev.lenovostart.lib.CSVFile;
import by.matveev.lenovostart.lib.Filealmat;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtAdressServer;
    EditText txtUserFTP;
    EditText txtPasswordFTP;
    EditText txtPathFile;
    EditText txtPortFTP;
    EditText txtModeWorking;
    Button btnSaveSetting;
    Button btnLoadSetting;

    SharedPreferences sPref;

    final String USER_NAME = "user_name";
    final String USER_PASSWORD = "user_passowrd";
    final String ADRESS_SERVER = "adress_server";
    final String PATH_FILE = "path_file";
    final String PORT_FTP = "21";
    final String MODE_WORKING = "1";

    String sAdressServer;
    String sUserFTP;
    String sPasswordFTP;
    String sPortFTP;
    String sPathFile;
    String sModeWorking;
    Filealmat filealmat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        txtModeWorking = (EditText) findViewById(R.id.txtModeWorking);
        txtModeWorking.setOnClickListener(this);

        txtAdressServer = (EditText) findViewById(R.id.txtAdressServer);
        txtAdressServer.setOnClickListener(this);

        txtUserFTP = (EditText) findViewById(R.id.txtUserFTP);
        txtUserFTP.setOnClickListener(this);

        txtPasswordFTP = (EditText) findViewById(R.id.txtPasswordFTP);
        txtPasswordFTP.setOnClickListener(this);

        txtPathFile = (EditText) findViewById(R.id.txtPathFile);
        txtPathFile.setOnClickListener(this);

        txtPortFTP = (EditText) findViewById(R.id.txtPortFTP);
        txtPortFTP.setOnClickListener(this);


        btnSaveSetting = (Button) findViewById(R.id.btnSaveSetting);
        btnSaveSetting.setOnClickListener(this);

        btnLoadSetting = (Button) findViewById(R.id.btnLoadSetting);
        btnLoadSetting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSaveSetting:
                saveSetting();
                break;
            case R.id.btnLoadSetting:
                try {
                    loadSetting();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }
    public void saveSetting(){

        String text = "";
        filealmat = new Filealmat();
        StringBuilder sbText  = new StringBuilder();

        sPref = getSharedPreferences("setting", MODE_PRIVATE);
        Editor ed = sPref.edit();

        ed.putString(ADRESS_SERVER, txtAdressServer.getText().toString());
        ed.putString(USER_NAME, txtUserFTP.getText().toString());
        ed.putString(USER_PASSWORD, txtPasswordFTP.getText().toString());
        ed.putString(PATH_FILE, txtPathFile.getText().toString());
        ed.putString(PORT_FTP, txtPortFTP.getText().toString());
        ed.putString(MODE_WORKING, txtModeWorking.getText().toString());
        ed.commit();
        text = txtAdressServer.getText().toString() + ";" +
                txtUserFTP.getText().toString() + ";" +
                txtPasswordFTP.getText().toString() + ";" +
                txtPortFTP.getText().toString() + ";" +
                txtPathFile.getText().toString() + ";" +
                txtModeWorking.getText().toString();
        sbText.append(text);
        if(0 == filealmat.writeFileSD(this,this,filealmat.NameDirectory,"setting.csv",sbText)){

        }
    }
    public void loadSetting() throws IOException {

        String[] nextLine = null;
        filealmat = new Filealmat();

        sPref = getSharedPreferences("setting", MODE_PRIVATE);


        if (filealmat.LoadCsv(this, filealmat.NameDirectory,"setting.csv")){
            while ((nextLine = filealmat.reader.readNext()) != null) {// считываем данные с CSV  файла
                sAdressServer = nextLine[0];
                sUserFTP = nextLine[1];
                sPasswordFTP = nextLine[2];
                sPortFTP = nextLine[3];
                sPathFile = nextLine[4];
                sModeWorking = nextLine[5];
            }
        }else{
            sAdressServer = sPref.getString(ADRESS_SERVER, "");
            sUserFTP = sPref.getString(USER_NAME, "");
            sPasswordFTP = sPref.getString(USER_PASSWORD, "");
            sPortFTP = sPref.getString(PORT_FTP, "");
            sPathFile = sPref.getString(PATH_FILE, "");
            sModeWorking = sPref.getString(MODE_WORKING, "");
        }
            if (!txtAdressServer.equals("")) {
                txtAdressServer.setText(sAdressServer);
                txtUserFTP.setText(sUserFTP);
                txtPasswordFTP.setText(sPasswordFTP);
                txtPathFile.setText(sPathFile);
                txtPortFTP.setText(sPortFTP);
                txtModeWorking.setText(sModeWorking);
            }



    }
}
