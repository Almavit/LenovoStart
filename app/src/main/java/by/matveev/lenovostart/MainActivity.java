package by.matveev.lenovostart;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.support.v4.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;
import by.matveev.lenovostart.lib.DBSampleHelper;
import by.matveev.lenovostart.lib.Filealmat;
import by.matveev.lenovostart.lib.MyPremission;
import by.matveev.lenovostart.lib.Setting;
import by.matveev.lenovostart.lib.SettingsManager;
import by.matveev.lenovostart.lib.WIFIService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    //AlertDialog alert;
    String alertTitle;
    String alertMessage;
    //Integer numPositionSpinner;
    DBHelper dbHelper;
    //Boolean Premis = true;
    Setting setting;
    SQLiteDatabase db;
    MyPremission almPremission;
    //    final String FILENAME_CSV = "999.csv";
    final String DIR_SD = "Documents";
    String[] nextLine;
    //  ProgressTextView progressTextViewMain;
    String PACKAGE_NAME;
    ListView groups;
    Button btnFourField;
    Button btnTwoField;
    Button btnTwoFieldQuan;
    Button btnOneField;
    Button btnEditor;
    Button btnStartElectron;
    Button btnSetting;
    Button btnClearPrice;

    //ProgressBar progressBar2;
    Button btnQR;
    Button btnLoadPrice;
    Button btnQrBarcode;
    Button btnRefreshMag;

    TextView txtLog;
    TextView textview;
    Filealmat filealmat;
    WIFIService wifis = null;
    Spinner spinNumMag;
    ArrayAdapter<CharSequence> adapter;


    //  private ViewDataBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 123;
    ////////II
    private static final int REQUEST_CODE_STORAGE = 1001;
    private static final int REQUEST_CODE_MANAGE_ALL_FILES = 1002;
    ////////II
    private WiFiMonitor mWiFiMonitor;           //Объект WiFiMonitor, поиск сети, вывод доступных точек
    private final static String ANDROID_PACKAGE = "application/vnd.android.package-archive";
    private int requestCode;
    private int resultCode;
    @Nullable
    private Intent data;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbars = findViewById(R.id.toolbar);
        setSupportActionBar(toolbars);

       // progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        checkStoragePermission(); // Вызов при старте II

        spinNumMag = (Spinner) findViewById(R.id.spinNumMag);
        spinNumMag.setOnItemSelectedListener(this);

        btnQrBarcode = (Button) findViewById(R.id.btnQrBarcode);
        btnQrBarcode.setOnClickListener(this);

        btnRefreshMag = (Button) findViewById(R.id.btnRefreshMag);
        btnRefreshMag.setOnClickListener(this);

        btnQR = (Button) findViewById(R.id.btnQR);
        btnQR.setOnClickListener(this);

        btnFourField = (Button) findViewById(R.id.btnFourField);
        btnFourField.setOnClickListener(this);

        btnTwoField = (Button) findViewById(R.id.btnTwoField);
        btnTwoField.setOnClickListener(this);

        btnTwoFieldQuan = (Button) findViewById(R.id.btnTwoFieldQuan);
        btnTwoFieldQuan.setOnClickListener(this);

        btnEditor = (Button) findViewById(R.id.btnTwoFieldNum);
        btnEditor.setOnClickListener(this);

        btnLoadPrice = (Button) findViewById(R.id.btnLoadPrice);
        btnLoadPrice.setOnClickListener(this);

        btnOneField = (Button) findViewById(R.id.btnOneField);
        btnOneField.setOnClickListener(this);

        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);

        btnClearPrice = (Button) findViewById(R.id.btnClearPrice);
        btnClearPrice.setOnClickListener(this);

        btnStartElectron = (Button) findViewById(R.id.btnStartElectron);
        btnStartElectron.setOnClickListener(this);

        txtLog = (TextView) findViewById(R.id.txtLog);
        textview = (TextView) findViewById(R.id.textView);


/////////////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////////////
        PACKAGE_NAME = getApplicationContext().getPackageName();

        try {
            wifis = new WIFIService(this);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        dbHelper = new DBHelper(this);
        filealmat = new Filealmat();
        setting = new Setting();
        almPremission = new MyPremission();
        if (almPremission.myPremission(this)) {

            db = new DBHelper(this).getReadableDatabase();
            new DBHelper(this).onCreate(db);
            db.close();

        } else {
            almPremission.premissionsactive = false;
        }

    }
//=============================

    static class ScanReceiver extends BroadcastReceiver {
        private String LOG_TAG = " ";

        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager wifi = (WifiManager) context.getSystemService(WIFI_SERVICE);//Извлекаем WifiManager для управления доступом Wi-Fi.
            List scanResultList = wifi.getScanResults();//Возвращает результаты последнего сканирования точки доступа.
            for (Object scanResult : scanResultList) {
                Log.d(LOG_TAG, scanResult.toString());
                Toast.makeText(context, "click" + scanResult.toString(), Toast.LENGTH_LONG).show();
            }
            context.unregisterReceiver(this);
        }
    }

    //=============================
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Integer numberPosition = position;
        wifis.SelectIPMask(this, "", numberPosition, spinNumMag);            //your code here for selection
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "onNothingSelected", Toast.LENGTH_LONG).show();
    }
    ///////II
    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 и выше
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_MANAGE_ALL_FILES);
            } else {
                Toast.makeText(this, "Доступ к файлам разрешён", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Android 7–10
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CODE_STORAGE);
            } else {
                Toast.makeText(this, "Разрешения уже получены", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ///////II
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                Toast.makeText(this, "Разрешения предоставлены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешения НЕ предоставлены", Toast.LENGTH_SHORT).show();
            }
        }
//        boolean allowed = true;
//
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//
//                for (int res : grantResults) {
//                    // if user granted all permissions.
//                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
//                }
//                break;
//            default:
//                // if user not granted permissions.
//                allowed = false;
//                break;
//        }
//        if (allowed) {
//            //user granted all permissions we can perform our task.
//            filealmat.makeFolder(this, "");
//
//
//        } else {
//            // we will give warning to user that they haven't granted permissions.
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    showNoStoragePermissionSnackbar();
//                }
//            }
//        }
    }

    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(this.findViewById(R.id.activity_scaner), "Storage permission isn't granted", Snackbar.LENGTH_LONG)
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

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            // Filealmat makefolder = new Filealmat();
//            filealmat.makeFolder(this, "");
//            return;
//        }
        ///////II
        if (requestCode == REQUEST_CODE_MANAGE_ALL_FILES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "MANAGE_EXTERNAL_STORAGE предоставлено", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "MANAGE_EXTERNAL_STORAGE НЕ предоставлено", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
//===================================================/////////////////////////////////////////////////////////////////
//===================================================
    public void onClick(View v) {
        Intent intent = new Intent(this, ScanerActivity.class);

        ContentValues contentValues = new ContentValues();
        intent.putExtra("VisibleTxtQuantity", View.VISIBLE);
        intent.putExtra("VisibleIntQuantity", View.VISIBLE);
        intent.putExtra("VisibleTxtPrice", View.VISIBLE);
        intent.putExtra("VisibleIntPrice", View.VISIBLE);
        intent.putExtra("VisibleTxtNumber", View.VISIBLE);
        intent.putExtra("VisibleIntNumber", View.VISIBLE);

        switch (v.getId()) {
            case R.id.btnClearPrice:
                AlertDialog.Builder builders = new AlertDialog.Builder(MainActivity.this);
                builders.setTitle("ОЧИСТИТЬ ПРАЙС")
                        .setMessage("ДЛЯ ПРОДОЛЖЕНИЯ НАЖМИТЕ ОК")
                        //.setIcon(R.mipmap.ic_launcher_vesta)
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DBHelper db = new DBHelper(MainActivity.this);
                                        if(!db.DeleteDB(MainActivity.this,"Price")){

                                        }
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                builders.show();
                break;
            case R.id.btnRefreshMag:
                   try {



                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("ИЗМЕНИТЬ МАГАЗИН")
                            .setMessage("ДЛЯ ПРОДОЛЖЕНИЯ НАЖМИТЕ ОК")
                            //.setIcon(R.mipmap.ic_launcher_vesta)
                            .setCancelable(false)
                            .setNegativeButton("ОК",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (wifis.CreateWIFI(MainActivity.this, txtLog, spinNumMag) == 0) {

                                            } else {

                                            }
                                            dialog.cancel();
                                        }
                                    })
                            .setNeutralButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });
                   // AlertDialog alert = builder.create();
                       builder.show();
                 } finally {
                }
                // Создаем новое подключение если отсутствует
                break;
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
             //   intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
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
                //intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
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
     //           intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
                intent.putExtra("VisibleIntPrice", View.INVISIBLE);
                intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                intent.putExtra("VisibleIntQuantity", View.INVISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.action_item5), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnQrBarcode:
                Toast.makeText(MainActivity.this, "Функция недоступна", Toast.LENGTH_SHORT).show();
                String[] commandLine = new String[]{"ifconfig"};
                Process process = null;
                try {
                    process = Runtime.getRuntime().exec(commandLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    line = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (line.matches("\\s*|\\t|\\r|\\n")) {
                    //  if (conf.isNotBlank()) {
                    //   ifconfigs.add(conf);
                    //   conf = new DxIfconfig();
                } else if (line.trim().matches("inet addr:(\\d{1,3}\\.){3}\\d{1,3}( ){2}" +
                        "(Bcast:(\\d{1,3}\\.){3}\\d{1,3}( ){2}){0,1}" +
                        "Mask:(\\d{1,3}\\.){3}\\d{1,3}")) {
                    String[] props = line.trim().split("( ){2}");
                    for (String prop : props) {
                        if (prop.length() == 0) {
                            continue;
                        }
                        String[] kv = prop.split(":");
                        if (kv[0].startsWith("inet addr")) {
                            //  conf.inetAddr = kv[1];
                        } else if (kv[0].startsWith("Bcast")) {
                            //  conf.bcast = kv[1];
                        } else if (kv[0].startsWith("Mask")) {
                            //  conf.mask = kv[1];
                        }
                    }
                }
                if (line == null) {
                    break;
                }
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
                Intent intentSetting = new Intent(this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.btnLoadPrice:
                try {
                  //  progressBar2.setVisibility(View.VISIBLE);
//                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("НАЧАТЬ ЗАГРУЗКУ")
                                .setMessage(" Нажмите ОК для продолжения работы.                              ОЖИДАЙТЕ ЗАВЕРШЕНИЯ ПРОЦЕССА")
                              //  .setIcon(R.mipmap.ic_launcher_vesta)
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                try{
                                                    //progressBar2.setVisibility(View.VISIBLE);
                                                    //////////////////////////////////  загрузка прайса
                                                    LoadPrice(MainActivity.this);
                                                    //////////////////////////////////
                                                }finally {
                                                    //progressBar2.setVisibility(View.INVISIBLE);
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                    builder.setTitle(alertTitle)
                                                            .setMessage(alertMessage)
                                                            //.setIcon(R.mipmap.ic_launcher_vesta)
                                                            .setCancelable(false)
                                                            .setNegativeButton("ОК",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                            dialog.cancel();
                                                                        }
                                                                    });
                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                }
                                                dialog.cancel();
                                            }
                                        })


                                .setNeutralButton("Отмена",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        //alert.cancel();

//                        txtLog.setText("       ...       ");
//                        txtLog.setBackgroundColor(Color.WHITE);
//                        // запускаем длительную операцию
//
//                        Toast.makeText(this, "ЖДИТЕ! ИДЕТ ЗАГРУЗКА ДАННЫХ", Toast.LENGTH_LONG);
//                        if (filealmat.LoadSaveCsvToDB(this, DIR_SD, "price.csv",
//                                "select * from " + DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE, DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE)) {
//                            txtLog.setText("ДАННЫЕ ОБНОВЛЕНЫ");
//                            alertTitle = "ДАННЫЕ ОБНОВЛЕНЫ";
//                            alertMessage = "Проверьте данные";
//                            Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
//                            txtLog.setBackgroundColor(Color.GREEN);
//
//                            //    textview.setBackgroundColor(Color.WHITE);
//                            //    btnLoadAll.getBackground().setColorFilter(Color.parseColor("Данные обновлены"), PorterDuff.Mode.DARKEN);
//                        } else {
//                            txtLog.setText("ДАННЫЕ НЕ СОХРАНЕНЫ!");
//                            txtLog.setBackgroundColor(Color.RED);
//                            alertTitle = "ДАННЫЕ НЕ СОХРАНЕНЫ!";
//                            alertMessage = "Возможно данные отсутствуют или произошла ошибка загрузки";
//                        }
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }




                } finally {

//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle(alertTitle)
//                            .setMessage(alertMessage)
//                            .setIcon(R.mipmap.ic_launcher_vesta)
//                            .setCancelable(false)
//                            .setNegativeButton("ОК",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//
//                                            dialog.cancel();
//                                        }
//                                    });
//                    AlertDialog alert = builder.create();
//                    alert.show();
                }
                break;
            default:
                break;
            //               progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

////////////////////////==========================/////////////////////////////////
    private void LoadPrice(Context context){
        txtLog.setText("       ...       ");
        txtLog.setBackgroundColor(Color.WHITE);
        // запускаем длительную операцию

        Toast.makeText(context, "ЖДИТЕ! ИДЕТ ЗАГРУЗКА ДАННЫХ", Toast.LENGTH_LONG);
        try {
            if (filealmat.LoadSaveCsvToDB(context, DIR_SD, "price.csv",
                    "select * from " + DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE, DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE)) {
                txtLog.setText("ПРАЙС ОБНОВЛЕН");
                alertTitle = "ПРАЙС ОБНОВЛЕН " + dbHelper.setTime;
                alertMessage = "Проверьте данные";
                Toast.makeText(this, "ПРАЙС ОБНОВЛЕН за " + dbHelper.setTime, Toast.LENGTH_LONG);
                txtLog.setBackgroundColor(Color.GREEN);

                //    textview.setBackgroundColor(Color.WHITE);
                //    btnLoadAll.getBackground().setColorFilter(Color.parseColor("Данные обновлены"), PorterDuff.Mode.DARKEN);
            } else {
                txtLog.setText("ПРАЙС НЕ ОБНОВЛЕН!");
                txtLog.setBackgroundColor(Color.RED);
                alertTitle = "ПРАЙС НЕ ОБНОВЛЕН!";
                alertMessage = "Возможно данные отсутствуют или произошла ошибка загрузки";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

////////////////////////==========================/////////////////////////////////

    //включить wifi
    public void enableWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            Toast toast = Toast.makeText(getApplicationContext(), "Wifi включен", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

//    private void bindToNetwork() {
//        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkRequest.Builder builder;
//        // Log.d(TAG, "All OK 123 !!!!!!!!!!!!!!!");
//        Toast.makeText(this, "All OK !!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new NetworkRequest.Builder();
//            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
//            connectivityManager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
//
//                @Override
//                public void onAvailable(Network network) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        connectivityManager.bindProcessToNetwork(network);
//                        //Log.d(TAG, "All OK !!!!!!!!!!!!!!!");
//                    } else {
//                        ConnectivityManager.setProcessDefaultNetwork(network);
//                    }
//                    try {
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    connectivityManager.unregisterNetworkCallback(this);
//                }
//            });
//        }
//    }


    class WiFiMonitor extends BroadcastReceiver {
        private String LOG_TAG = "myWiFiMonitor";

        //   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(LOG_TAG, action);
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            Log.d(LOG_TAG, "isConnected: " + isConnected);
            Toast.makeText(context, "isConnected: " + isConnected, Toast.LENGTH_LONG).show();
            String ddd = activeNetwork.getExtraInfo();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String dlldl = activeNetwork.getExtraInfo().getBytes(StandardCharsets.UTF_8).toString();
            }

            if (!isConnected)
                return;
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            String ssidd = activeNetwork.getExtraInfo().toString();
            // String ipwifi = activeNetwork.getDetailedState().toString();
            Log.d(LOG_TAG, "isWiFi: " + isWiFi);
            Toast.makeText(context, "isWiFi: " + isWiFi, Toast.LENGTH_LONG).show();
            if (!isWiFi)
                return;
            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            // параметры WIFI сканера
            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();

            try {
                wifis.wifiIpAddress(context);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            Log.d(LOG_TAG, connectionInfo.getSSID());
            Toast.makeText(context, "Connected to Internet: " + connectionInfo.getSSID(), Toast.LENGTH_LONG).show();

        }
    }

//
//    protected String wifiIpAddress(Context context) {
//        String ipAddressString;
//        String ipGatewayString;
//        String ipServerString;
//        String ipNetmaskString;
//        String s_dns1;
//        String s_dns2;
//        String s_gateway;
//        String s_ipAddress;
//        String s_leaseDuration;
//        String s_netmask;
//        String s_serverAddress;
//        TextView info;
//        DhcpInfo d;
//        WifiManager wifii;
//
//        wifii = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//
//        d = wifii.getDhcpInfo();
//        String sss = d.toString();
//        s_dns1 = "DNS 1: " + String.valueOf(d.dns1);
//        s_dns2 = "DNS 2: " + String.valueOf(d.dns2);
//        s_gateway = "Default Gateway: " + String.valueOf(d.gateway);
//        s_ipAddress = "IP Address: " + String.valueOf(d.ipAddress);
//        s_leaseDuration = "Lease Time: " + String.valueOf(d.leaseDuration);
//        s_netmask = "Subnet Mask: " + String.valueOf(d.netmask);
//        s_serverAddress = "Server IP: " + String.valueOf(d.serverAddress);
//
//        //////
//
//        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
//        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
//        int ipGateway = d.gateway;
//        int ipServer = d.serverAddress;
//        int ipNetmask = d.netmask;
//        // Convert little-endian to big-endianif needed
//        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
//            ipAddress = Integer.reverseBytes(ipAddress);
//            ipGateway = Integer.reverseBytes(ipGateway);
//            ipServer = Integer.reverseBytes(ipServer);
//            ipNetmask = Integer.reverseBytes(ipNetmask);
//        }
//
//        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
//        byte[] ipByteGateway = BigInteger.valueOf(ipGateway).toByteArray();
//        // byte[] ipByteServer = BigInteger.valueOf(ipServer).toByteArray();
//        byte[] ipByteNetmask = BigInteger.valueOf(ipNetmask).toByteArray();
//
//        try {
//            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
//            ipGatewayString = InetAddress.getByAddress(ipByteGateway).getHostAddress();
//            // ipServerString = InetAddress.getByAddress(ipByteServer).getHostAddress();
//            //  ipNetmaskString = InetAddress.getByAddress(ipByteNetmask).getHostAddress();
//        } catch (UnknownHostException ex) {
//            Log.e("WIFIIP", "Unable to get host address.");
//            ipAddressString = null;
//        }
//
//
//////////////////////////////////////
//////////////////////////////////
//        return ipAddressString;
//    }
//
////////////////////////==========================/////////////////////////////////


    /////////////////////////////////
    public void Update(final Integer lastAppVersion, Context context) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Доступно обновление приложения" +
                                lastAppVersion + " - Желаете обновиться? " +
                                "Если вы согласны - вы будете перенаправлены к скачиванию APK файла,"
                                + " который затем нужно будет открыть.")
                        .setCancelable(true)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (!filealmat.LoadFileFtp(context, filealmat.NameDirectory, filealmat.NameFileAPK)) {
                                    //no
                                } else {
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
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //SettingsManager.put(this, "LastIgnoredUpdateVersion", lastAppVersion.toString());
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
//        this.requestCode = requestCode;
//        this.resultCode = resultCode;
//        this.data = data;
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
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        /*if (id == R.id.action_settings) {
//           return true;
//        }*/
//        switch (id) {
//            case R.id.action_item1:
//                Toast.makeText(MainActivity.this, getString(R.string.action_item1), Toast.LENGTH_LONG).show();
//            case R.id.action_item2:
//                Toast.makeText(MainActivity.this, getString(R.string.action_item2), Toast.LENGTH_LONG).show();
//            case R.id.action_item3:
//                Toast.makeText(MainActivity.this, getString(R.string.action_item3), Toast.LENGTH_LONG).show();
//            case R.id.action_item4:
//                Toast.makeText(MainActivity.this, getString(R.string.action_item4), Toast.LENGTH_LONG).show();
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (almPremission.premissionsactive) {
            try {
                if (wifis.wifiIpAddress(this)) {
                    if (!wifis.ipNameSSID.equals("null")) {
                        enableWifi();                                   //Проверяем включен ли WiFi, если нет то включаем
                        // String wifiIpAddress;
                        try {
                            wifis.wifiIpAddress(this);
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (!wifis.ipAdressScaner.equals("")) {
                                if (setting.loadSetting(this)) {
                                    if (!setting.executeCommand(setting.sAdressServer)) {// проверка связи с сервером. PING
                                        txtLog.setText("НЕТ СВЯЗИ С СЕРВЕРОМ!");
                                        txtLog.setBackgroundColor(Color.RED);
                                        return;
                                    } else {// связь  поддерживается
                                        txtLog.setText("       ...       ");
                                        txtLog.setBackgroundColor(Color.WHITE);
                                    }
                                } else {
                                    txtLog.setText("НАСТРОЙКИ НЕ ЗАГРУЖЕНЫ!");
                                    txtLog.setBackgroundColor(Color.RED);
                                }
                            } else {
                                txtLog.setText("НЕТ СВЯЗИ С СЕРВЕРОМ!");
                                txtLog.setBackgroundColor(Color.RED);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        txtLog.setText("НЕТ СВЯЗИ С СЕРВЕРОМ! Отсутствует WIFI имя");
                        txtLog.setBackgroundColor(Color.RED);
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (wifis == null) {
                try {
                    wifis = new WIFIService(this);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

            if (almPremission.PremissionGPS(this)) {
                //получаем разрешение на местополжение
                almPremission.premissionsactive = true;
            } else {
                //отказываемся от разрешения на местоположение
                almPremission.premissionsactive = false;
            }
            if (almPremission.premissionsactive) {
                try {
                    if(db == null) {
                        db = new DBHelper(this).getReadableDatabase();
                    }
                   // new DBHelper(this).onCreate(db);
                    db.close();
                    txtLog.setText("   ...   ");
                    txtLog.setBackgroundColor(Color.WHITE);

                    if (wifis.addAdapterWiFi(this, spinNumMag)) {
                        if (wifis.wifiIpAddress(this)) {

                            //получаем список магазинов и подразделений в список
                            //WIFIService wifis = null;

                            if (!wifis.ipNameSSID.equals("null")) {
                                //если имя SSID не пустое и WIFI активен получаем данные и выделяем в списке магазин или подразделение
                                wifis.SelectIPMask(this, wifis.ipMaskAddress, 0, spinNumMag);
                                //txtLog.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                                Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
                                //txtlogConnect.setBackgroundColor(Color.GREEN);
                            } else {
                                //txtlogConnect.setText("ДАННЫЕ НЕ ЗАГРУЖЕНЫ! НЕТ СВЯЗИ");
                                //txtlogConnect.setBackgroundColor(Color.RED);
                            }

                            try {
                                if (!wifis.ipNameSSID.equals("null")) {
                                    if ((!wifis.ipMaskAddress.equals("")) || (wifis.ipMaskAddress != null)) {// &(wifis.ipNameSSID == null)

                                        ArrayList<String> list = new ArrayList<String>();
                                        list.addAll(dbHelper.getSelectIPMask(this, wifis.ipMaskAddress));
                                        if (list.size() > 0) {

                                            String sIPAdressServer = list.get(1).toString() + "." + list.get(2).toString();

                                            try {
                                                // получаем настройки setting.csv
                                                if (setting.loadSetting(this)) {

                                                    if (!sIPAdressServer.equals(setting.sAdressServer)) {
                                                        if ((sIPAdressServer == null) || (setting.sAdressServer == null) || ((sIPAdressServer == "") || (setting.sAdressServer == ""))) {
                                                            // пустые строки
                                                        } else {
                                                            //                                            if () {
                                                            //// пустые строки
                                                            //                                            } else {
                                                            try {
                                                                if (!setting.saveSetting(this, sIPAdressServer, setting.sUserFTP, setting.sPasswordFTP,
                                                                        setting.sPortFTP, setting.sPathFile, setting.sModeWorking)) {
                                                                    Toast.makeText(getApplicationContext(), "НАСТРОЙКИ НЕ СОХРАНЕНЫ",
                                                                            Toast.LENGTH_SHORT).show();

                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "НАСТРОЙКИ СОХРАНЕНЫ",
                                                                            Toast.LENGTH_SHORT).show();

                                                                }
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            //                                            }
                                                        }
                                                    }
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                if (setting.loadSetting(this)) {
                                                    if (!setting.executeCommand(setting.sAdressServer)) {
                                                        txtLog.setText("НЕТ СВЯЗИ С СЕРВЕРОМ!");
                                                        txtLog.setBackgroundColor(Color.RED);
                                                        // return;
                                                    }
                                                } else {
                                                    txtLog.setText("НАСТРОЙКИ НЕ ЗАГРУЖЕНЫ!");
                                                    txtLog.setBackgroundColor(Color.RED);
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            txtLog.setText("НЕТ СВЯЗИ С СЕРВЕРОМ!");
                                            txtLog.setBackgroundColor(Color.RED);
                                        }
                                        IntentFilter intentFilter = new IntentFilter();                    //Создаем объект для отслеживания изменений в сети.
                                        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);    //Произошло изменение сетевого подключения. IntentFilter должен содержать событие.
                                        registerReceiver(mWiFiMonitor, intentFilter);
                                        registerReceiver(mWiFiMonitor, intentFilter);
                                        //            final WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                                        //            ScanReceiver scanReceiver = new ScanReceiver();
                                        //            registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                                        //            wifi.startScan();         } });
                                        //    requestPermissions(new String[{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
                                    }
                                } else {
                                    txtLog.setText("НЕТ СВЯЗИ С СЕРВЕРОМ! Отсутствует WIFI имя");
                                    txtLog.setBackgroundColor(Color.RED);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            txtLog.setText("НЕТ НАСТРОЕК! БД IPTable пуста!");
                            txtLog.setBackgroundColor(Color.RED);
                        }
                    }else{
                        txtLog.setText("НЕТ НАСТРОЕК! БД IPTable пуста! Загрузить с файла!");
                        txtLog.setBackgroundColor(Color.RED);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else {
                txtLog.setText("НЕТ РАЗРЕШЕНИЯ ");
                txtLog.setBackgroundColor(Color.RED);

            }
        } finally {
            // alert.cancel();
        }
        // Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_LONG).show();
        //1
    }

    @Override
    protected void onPause() {
        super.onPause();
     //    Toast.makeText(MainActivity.this, "onPause", Toast.LENGTH_LONG).show();
        //2
    }

    @Override
    protected void onStop() {
        super.onStop();
     //     Toast.makeText(MainActivity.this, "onStop", Toast.LENGTH_LONG).show();
        //3
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  Toast.makeText(MainActivity.this, "onDestroy", Toast.LENGTH_LONG).show();
    }


}