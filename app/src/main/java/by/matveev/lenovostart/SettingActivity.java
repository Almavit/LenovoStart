package by.matveev.lenovostart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import by.matveev.lenovostart.lib.Filealmat;
import by.matveev.lenovostart.lib.Setting;
import by.matveev.lenovostart.lib.SettingsManager;


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
    private final static String ANDROID_PACKAGE = "application/vnd.android.package-archive";


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

                String apkurl = "";
                //apkurl = Environment.getExternalStorageDirectory() + "/" + filealmat.NameDirectory + "/" + filealmat.NameFileAPK;
                //Update(apkurl);
                Toast.makeText(getApplicationContext(), "ЖДИТЕ! ИДЕТ ОБНОВЛЕНИЕ!",
                        Toast.LENGTH_LONG).show();
                Update(0,this);
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
                        Toast.makeText(getApplicationContext(), "НАСТРОЙКИ НЕ СОХРАНЕНЫ",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        Toast.makeText(getApplicationContext(), "НАСТРОЙКИ СОХРАНЕНЫ",
                                Toast.LENGTH_SHORT).show();
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
                    }else{
                        Toast.makeText(getApplicationContext(), "НАСТРОЙКИ ЗАГРУЖЕНЫ",
                                Toast.LENGTH_SHORT).show();
                        txtAdressServer.setText(setting.sAdressServer);
                        txtUserFTP.setText(setting.sUserFTP);
                        txtPasswordFTP.setText(setting.sPasswordFTP);
                        txtPortFTP.setText(setting.sPortFTP);
                        txtPathFile.setText(setting.sPathFile);
                        txtModeWorking.setText(setting.sModeWorking);

                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    //===================  обновление программы старт ===========================
    public void Update(final Integer lastAppVersion, Context context) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Доступно обновление приложения - желаете обновиться? " +
                                "Если вы согласны - вы будете перенаправлены к скачиванию APK файла,"
                                +" который затем нужно будет открыть.")
                        .setCancelable(true)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String sd = filealmat.NameDirectory;
                                if(!filealmat.LoadFileFtp(context, filealmat.NameDirectory,filealmat.NameFileAPK)) {
                                    //no
                                }else{
                                    // создаём новое намерение
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
// устанавливаем флаг для того, чтобы дать внешнему приложению пользоваться нашим FileProvider
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    String apkUrl = Environment.getExternalStorageDirectory() + "/" + filealmat.NameDirectory + "/" + filealmat.NameFileAPK;
                                    File file = new File(apkUrl);
// генерируем URI, я определил полномочие как ID приложения в манифесте, последний параметр это файл, который я хочу открыть
                                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);

// я открываю PDF-файл, поэтому я даю ему действительный тип MIME
                                    intent.setDataAndType(uri, ANDROID_PACKAGE);//"application/pdf"

// подтвердите, что устройство может открыть этот файл!
                                    PackageManager pm = context.getPackageManager();
                                    if (intent.resolveActivity(pm) != null) {
                                        startActivity(intent);
                                    }

                                }
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //SettingsManager.put(this, "LastIgnoredUpdateVersion", lastAppVersion.toString());
                                dialog.cancel();
                                finish();
                                dialog.dismiss();
                            }


                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }
//////////////


}
