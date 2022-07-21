package by.matveev.lenovostart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    Button btnUpdate;
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

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        filealmat = new Filealmat();

    }

    @Override
    public void onClick(View v) {
        Setting setting = new Setting();
        switch (v.getId()){
            case R.id.btnUpdate:
                Update("");
                break;
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
    //===================  обновление программы старт ===========================

    public boolean Update(String apkurl){
        try {
            if(apkurl.length()!=0) {
                URL url = new URL(apkurl);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String PATH = Environment.getExternalStorageDirectory() + filealmat.NameDirectory;
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, filealmat.NameFileAPK);
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();//till here, it works fine - .apk is download to my sdcard in download file

                Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(PATH + filealmat.NameFileAPK))
                        .setType("application/android.com.app");
                startActivity(promptInstall);//installation is not working
            }else{
                if(!filealmat.LoadFileFtp(this, filealmat.NameDirectory,filealmat.NameFileAPK)) {
                    return false;
                }
                String PATH = Environment.getExternalStorageDirectory() + "/" + filealmat.NameDirectory + "/" + filealmat.NameFileAPK;
                Runtime.getRuntime().exec(new String[] {"su", "-c", "pm install -r " + PATH});

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Uri uri = Uri.fromFile(new File(PATH));
//                intent.setDataAndType(uri, "application/vnd.android.package-archive");
//                startActivity(intent);


//                Intent promptInstall = new Intent(Intent.ACTION_VIEW)
//                        .setData(Uri.parse(PATH + filealmat.NameFileAPK))
//                        .setType("application/android.com.app");
//                startActivity(promptInstall);//installation is not working
            }

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ОШИБКА ОБНОВЛЕНИЯ !", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
