package by.matveev.lenovostart;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.Filealmat;
import by.matveev.lenovostart.lib.MyPremission;
import by.matveev.lenovostart.lib.ProgressTextView;
import by.matveev.lenovostart.lib.Setting;
import by.matveev.lenovostart.lib.SettingsManager;
import by.matveev.lenovostart.lib.WIFIService;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    public static Network getNetwork(final Context context, final int transport) {
//        final ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        for (Network network : connManager.getAllNetworks()) {
//            NetworkCapabilities networkCapabilities = connManager.getNetworkCapabilities(network);
//            if (networkCapabilities != null &&
//                    networkCapabilities.hasTransport(transport) &&
//                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
//                return network;
//            }
//        }
//        return null;
//    }

//    private static final int PERMISSION_REQUEST_CODE = 123;
//
//    SQLiteDatabase database;
//    SQLiteDatabase db;
    Setting setting;

    private final static String ANDROID_PACKAGE = "application/vnd.android.package-archive";
    final String FILENAME_CSV = "999.csv";
    final String DIR_SD = "Documents";

//    private static final String FILENAME = "./alphabet.utf8";
//    private static final String ENCODING_WIN1251 = "windows-1251";
//    private static final String ENCODING_UTF8 = "UTF-8";
    String[] nextLine;
    ProgressTextView progressTextViewMain;
    String PACKAGE_NAME;

//    final String USER_NAME = "user_name";
//    final String USER_PASSWORD = "user_passowrd";
//    final String ADRESS_SERVER = "adress_server";
//    final String PATH_FILE = "path_file";
//    final String PORT_FTP = "21";
//    final String MODE_WORKING = "1";

//    String sAdressServer;
//    String sUserFTP;
//    String sPasswordFTP;
//    String sPortFTP;
//    String sPathFile;
//    String sModeWorking;

    Button btnFourField;
    Button btnTwoField;
    Button btnTwoFieldQuan;
    Button btnOneField;
    Button btnEditor;
    Button btnStartElectron;
    Button btnSetting;

    Button btnQR;
    Button btnLoadAll;
    Button btnQrBarcode;

    TextView txtLog;
//    EditText txtIp;
//    CheckBox chkWiFi;


   // MyPremission almPremission;
    Filealmat filealmat;


    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper db = new DBHelper(this);
        db.createDataBase();
        db.close();
        PACKAGE_NAME = getApplicationContext().getPackageName();

        filealmat = new Filealmat();
        setting = new Setting();
        MyPremission almPremission = new MyPremission();
//
        Boolean Premis = true;

                if (!almPremission.myPremission(this)) {
                    Premis = true;
                } else {
                    Premis = false;
                }
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnQrBarcode = (Button) findViewById(R.id.btnQrBarcode);
        btnQrBarcode.setOnClickListener(this);

        btnQR = (Button) findViewById(R.id.btnQR);
        btnQR.setOnClickListener(this);

        btnFourField = (Button) findViewById(R.id.btnFourField);
        btnFourField.setOnClickListener(this);

        btnTwoField = (Button) findViewById(R.id.btnTwoField);
        btnTwoField.setOnClickListener(this);

        btnTwoFieldQuan = (Button) findViewById(R.id.btnTwoFieldQuan);
        btnTwoFieldQuan.setOnClickListener(this);

        btnOneField = (Button) findViewById(R.id.btnOneField);
        btnOneField.setOnClickListener(this);

        btnEditor = (Button) findViewById(R.id.btnTwoFieldNum);
        btnEditor.setOnClickListener(this);

        btnLoadAll = (Button) findViewById(R.id.btnLoadAll);
        btnLoadAll.setOnClickListener(this);

        progressTextViewMain = (ProgressTextView) findViewById(R.id.progressTextViewMain);
        progressTextViewMain.setValue(0); // устанавливаем нужное значение

        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);

        btnStartElectron = (Button) findViewById(R.id.btnStartElectron);
        btnStartElectron.setOnClickListener(this);

        txtLog = (TextView) findViewById(R.id.txtLog);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }
        if (allowed){
            //user granted all permissions we can perform our task.

            filealmat.makeFolder(this,"");
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();

                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }
    }
    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(this.findViewById(R.id.activity_scaner), "Storage permission isn't granted" , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();
                        Toast.makeText(getApplicationContext(), "Open Permissions and grant the Storage permission",
                                Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
           // Filealmat makefolder = new Filealmat();
            filealmat.makeFolder(this,"");
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

     public void onClick(View v) {
        Intent intent = new Intent(this, ScanerActivity.class);
//         DBHelper dbHelper;// = new DBHelper(this);

         ContentValues contentValues = new ContentValues();
        intent.putExtra("VisibleTxtQuantity", View.VISIBLE);
        intent.putExtra("VisibleIntQuantity", View.VISIBLE);
        intent.putExtra("VisibleTxtPrice", View.VISIBLE);
        intent.putExtra("VisibleIntPrice", View.VISIBLE);
        intent.putExtra("VisibleTxtNumber", View.VISIBLE);
        intent.putExtra("VisibleIntNumber", View.VISIBLE);


        switch (v.getId()) {
            case R.id.btnFourField:
                Toast.makeText(MainActivity.this, getString(R.string.action_item1), Toast.LENGTH_SHORT).show();
                startActivity(intent);

                break;
            case R.id.btnTwoField:
                intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                intent.putExtra("VisibleIntQuantity", View.INVISIBLE);
                intent.putExtra("VisibleTxtNumber", View.INVISIBLE);
                intent.putExtra("VisibleIntNumber", View.INVISIBLE);

                Toast.makeText(MainActivity.this, getString(R.string.action_item2), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnOneField:
                intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
                intent.putExtra("VisibleIntPrice", View.INVISIBLE);
                intent.putExtra("VisibleTxtNumber", View.INVISIBLE);
                intent.putExtra("VisibleIntNumber", View.INVISIBLE);
                intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                intent.putExtra("VisibleIntQuantity", View.INVISIBLE);


                Toast.makeText(MainActivity.this, getString(R.string.action_item3), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;

            //btnTwoFieldQuan
            case R.id.btnTwoFieldQuan:
                intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
                intent.putExtra("VisibleIntPrice", View.INVISIBLE);
                intent.putExtra("VisibleTxtNumber", View.INVISIBLE);
                intent.putExtra("VisibleIntNumber", View.INVISIBLE);
                //intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                //intent.putExtra("VisibleIntQuantity", View.INVISIBLE);
                intent.putExtra("VisibleTxtNumber", View.INVISIBLE);
                intent.putExtra("VisibleIntNumber", View.INVISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.action_item2), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnTwoFieldNum:
                intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
                intent.putExtra("VisibleIntPrice", View.INVISIBLE);
                intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                intent.putExtra("VisibleIntQuantity", View.INVISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.action_item5), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnQrBarcode:
                Toast.makeText(MainActivity.this, "Функция недоступна", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStartElectron:
                //Intent intentStartElectronDocument = new Intent(this, StartElectronDocument.class);
                //startActivity(intentStartElectronDocument);
                Toast.makeText(MainActivity.this, "Функция недоступна", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnQR:
//                Intent intentQR = new Intent(this, QRcode.class);
//                startActivity(intentQR);

                Toast.makeText(MainActivity.this, "Функция недоступна", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnSetting:
//                Update(0,this);
//
                Intent intentSetting = new Intent(this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            default:
                break;
            case R.id.btnLoadAll:
                Setting setting = new Setting();
//              setting.loadSetting(this);
                //Filealmat filealmat = new Filealmat();

//                if (!setting.executeCommand(sAdressServer)) {
//                    txtLog.setBackgroundColor(Color.RED);
//                    break;
//                }
//===============================
                try {
                    txtLog.setText("       ...       ");
                    txtLog.setBackgroundColor(Color.WHITE);
                    Toast.makeText(this, "ЖДИТЕ! ИДЕТ ЗАГРУЗКА ДАННЫХ" , Toast.LENGTH_LONG);
                    if (filealmat.LoadSaveCsvToDB(this, DIR_SD,"price.csv",
                            "select * from " + DBHelper.TABLE_DOCUMENT_PRICE,DBHelper.TABLE_DOCUMENT_PRICE)){
                        txtLog.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                        Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ" , Toast.LENGTH_LONG);
                        txtLog.setBackgroundColor(Color.GREEN);
                    }else{
                        txtLog.setText("ДАННЫЕ НЕ СОХРАНЕНЫ!");
                        txtLog.setBackgroundColor(Color.RED);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//===============================

//===========================================
        }
    }
/////////////////////////////////
public void Update(final Integer lastAppVersion, Context context) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Доступно обновление приложения rutracker free до версии " +
                            lastAppVersion + " - желаете обновиться? " +
                            "Если вы согласны - вы будете перенаправлены к скачиванию APK файла,"
                            +" который затем нужно будет открыть.")
                    .setCancelable(true)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                if(!filealmat.LoadFileFtp(context, filealmat.NameDirectory,filealmat.NameFileAPK)) {
                    //no
                }else{
                    // создаём новое намерение
                    Intent intent = new Intent(Intent.ACTION_VIEW);
// устанавливаем флаг для того, чтобы дать внешнему приложению пользоваться нашим FileProvider
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    String apkUrl = Environment.getExternalStorageDirectory() + "/" + "Documents" + "/" + filealmat.NameFileAPK;
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


//                            File directory = getExternalFilesDir(null);
//                            File file = new File(directory, "app-debug.apk");
//                            Uri fileUri = Uri.fromFile(file);
//                            if (Build.VERSION.SDK_INT >= 24) {
//                                fileUri = FileProvider.getUriForFile(context, context.getPackageName() ,
//                                        file);
//                            }
//                            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
//                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//                            intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            startActivity(intent);
                            finish();
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//
//                            String apkUrl = Environment.getExternalStorageDirectory() + "/" + "Documents" + "/" + "app-debug.apk";
//                            File file = new File(apkUrl);
//                            //"https://github.com/chu888chu888/android-autoupdater/blob/master/sample/src/main/java/com/github/snowdream/android/apps/autoupdater/MainActivity.java";
//                            //
//                            //"https://github.com/jehy/rutracker-free/releases/download/" + lastAppVersion + "/app-release.apk";
//                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//                            //intent.setDataAndType(Uri.parse(apkUrl), "application/vnd.android.package-archive");
//
//                            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//                            //intent.setData(Uri.parse(apkUrl));
//
//
//                            //intent.setDataAndType(Uri.parse(FileUtil.getPublicDir(Environment.getExternalStorageDirectory() + "/" + "Documents" + "/").concat("/Vertretungsplan.apk")),
//                            //        "application/vnd.android.package-archive");
//                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SettingsManager.put(this, "LastIgnoredUpdateVersion", lastAppVersion.toString());
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    });


}
//////////////
    // Start the  service
    public void startNewService(View view) {

        startService(new Intent(this, WIFIService.class));
    }

    // Stop the  service
    public void stopNewService(View view) {

        stopService(new Intent(this, WIFIService.class));
    }
////////////////////////////////////

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
           return true;
        }*/
        switch (id){
            case R.id.action_item1:
                Toast.makeText(MainActivity.this, getString(R.string.action_item1), Toast.LENGTH_LONG).show();
            case R.id.action_item2:
                Toast.makeText(MainActivity.this, getString(R.string.action_item2), Toast.LENGTH_LONG).show();
            case R.id.action_item3:
                Toast.makeText(MainActivity.this, getString(R.string.action_item3), Toast.LENGTH_LONG).show();
            case R.id.action_item4:
                Toast.makeText(MainActivity.this, getString(R.string.action_item4), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(MainActivity.this, "onStart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(MainActivity.this, "onPause", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(MainActivity.this, "onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(MainActivity.this, "onDestroy", Toast.LENGTH_LONG).show();
    }


}
