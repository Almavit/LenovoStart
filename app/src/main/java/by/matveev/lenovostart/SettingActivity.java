package by.matveev.lenovostart;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import by.matveev.lenovostart.lib.CSVFile;
import by.matveev.lenovostart.lib.Filealmat;
import by.matveev.lenovostart.lib.Setting;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtAdressServer;
    EditText txtUserFTP;
    EditText txtPasswordFTP;
    EditText txtPathFile;
    EditText txtPortFTP;
    EditText txtModeWorking;
    Button btnSaveSetting;
    Button btnLoadSetting;


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
        Setting setting = new Setting();
        switch (v.getId()){
            case R.id.btnSaveSetting:
                try {

                    String[] stringsetting = new String[6];
                    stringsetting[0] =
                            stringsetting[0] = txtAdressServer.getText().toString();
                            stringsetting[1] = txtUserFTP.getText().toString();
                            stringsetting[2] = txtPasswordFTP.getText().toString();
                            stringsetting[3] = txtPortFTP.getText().toString();
                            stringsetting[4] = txtPathFile.getText().toString();
                            stringsetting[5] = txtModeWorking.getText().toString();
                            setting.nextLine = stringsetting;
                    if(!setting.saveSetting(this)){

                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnLoadSetting:
                try {
                    if(!setting.loadSetting(this)){
                        Toast.makeText(this, "ИЗМЕНИТЬ И СОХРАНИТЬ НОВЫЕ НАСТРОЙКИ", Toast.LENGTH_LONG).show();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
