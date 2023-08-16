package by.matveev.lenovostart;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;


import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBSampleHelper;
import by.matveev.lenovostart.lib.Filealmat;
import by.matveev.lenovostart.lib.MyPremission;
import by.matveev.lenovostart.lib.Setting;
import by.matveev.lenovostart.lib.WIFIService;

//import androidx.databinding.DataBindingUtil;
//import androidx.databinding.ViewDataBinding;
//import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Setting setting;
    //    final String FILENAME_CSV = "999.csv";
    final String DIR_SD = "Documents";
    String[] nextLine;
    //  ProgressTextView progressTextViewMain;
    String PACKAGE_NAME;

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
    Button btnConnect;

    TextView txtLog;
    TextView textview;
    Filealmat filealmat;

    //  ProgressBar progressBar;

    //  private ViewDataBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private WiFiMonitor mWiFiMonitor;           //Объект WiFiMonitor, поиск сети, вывод доступных точек
    private final static String ANDROID_PACKAGE = "application/vnd.android.package-archive";


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbars = findViewById(R.id.toolbar);
        setSupportActionBar(toolbars);


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

        btnEditor = (Button) findViewById(R.id.btnTwoFieldNum);
        btnEditor.setOnClickListener(this);

        btnLoadAll = (Button) findViewById(R.id.btnLoadAll);
        btnLoadAll.setOnClickListener(this);

        btnOneField = (Button) findViewById(R.id.btnOneField);
        btnOneField.setOnClickListener(this);

//        progressTextViewMain = (ProgressTextView) findViewById(R.id.progressTextViewMain);
//        progressTextViewMain.setValue(0); // устанавливаем нужное значение

        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);

        btnStartElectron = (Button) findViewById(R.id.btnStartElectron);
        btnStartElectron.setOnClickListener(this);

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);


        txtLog = (TextView) findViewById(R.id.txtLog);
        textview = (TextView) findViewById(R.id.textView);

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        binding.btnConnect.setOnClickLister(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent intentConnect = new Intent(MainActivity.this, ConnectMag.class);
//                startActivity(intentConnect);
//
//            }
//        });

        //  progressBar = (ProgressBar) findViewById(R.id.progressBar);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        filealmat = new Filealmat();
        setting = new Setting();
        try {
            if (setting.loadSetting(this)) {
                if (!setting.executeCommand(setting.sAdressServer)) {
                    txtLog.setText("НЕТ СВЯЗИ С СЕРВЕРОМ!");
                    txtLog.setBackgroundColor(Color.RED);
                    return;
                }
            } else {
                txtLog.setText("НАСТРОЙКИ НЕ ЗАГРУЖЕНЫ!");
                txtLog.setBackgroundColor(Color.RED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyPremission almPremission = new MyPremission();
        Boolean Premis = true;

        if (!almPremission.myPremission(this)) {
            Premis = true;
        } else {
            Premis = false;
        }
        //    DBHelper db = new DBHelper(this);
        SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
        new DBHelper(this).onCreate(db);

        db.close();
        WIFIService wifis = new WIFIService(this);

        wifis.enableWifi();
       // enableWifi();                                   //Проверяем включен ли WiFi, если нет то включаем
        //         bindToNetwork();                           //Для версии выше 5, для связки процесса с сетью  (без интернет доступа)
        IntentFilter intentFilter = new IntentFilter();                    //Создаем объект для отслеживания изменений в сети.
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);    //Произошло изменение сетевого подключения. IntentFilter должен содержать событие.

        registerReceiver(mWiFiMonitor, intentFilter);

        //      enableWifi();                                   //Проверяем включен ли WiFi, если нет то включаем
        // bindToNetwork();                           //Для версии выше 5, для связки процесса с сетью  (без интернет доступа)
//        IntentFilter intentFiltera = new IntentFilter();                    //Создаем объект для отслеживания изменений в сети.
//        intentFiltera.addAction(ConnectivityManager.CONNECTIVITY_ACTION);    //Произошло изменение сетевого подключения. IntentFilter должен содержать событие.

        registerReceiver(mWiFiMonitor, intentFilter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:

                for (int res : grantResults) {
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }
        if (allowed) {
            //user granted all permissions we can perform our task.

            filealmat.makeFolder(this, "");
//            DBHelper db = new DBHelper(this);
//
//            db.createDataBase();
//            db.close();


        } else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();

                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Filealmat makefolder = new Filealmat();
            filealmat.makeFolder(this, "");
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
            case R.id.btnConnect:
                Intent intentConnect = new Intent(this, ConnectMag.class);
                startActivity(intentConnect);

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

                /////////////////////////////////////////
                String[] commandLine = new String[]{"ifconfig"};
                Process process = null;

                try {
                    process = Runtime.getRuntime().exec(commandLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                //    DxIfconfig conf = new DxIfconfig();
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
//                    System.out.println(line.trim());

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


//                String action = intent.getAction();
//                //   Log.d(LOG_TAG, action);
//                ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
//                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//                //    Log.d(LOG_TAG, "isConnected: " + isConnected);
//                Toast.makeText(this, "isConnected: " + isConnected, Toast.LENGTH_LONG).show();
//                String SSID = activeNetwork.getExtraInfo();
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                    String dlldl = activeNetwork.getExtraInfo().getBytes(StandardCharsets.UTF_8).toString();
//                }
//
//                if (!isConnected)
//                    return;
//                boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
//              //  String ssidd = activeNetwork.getExtraInfo().toString();
//                // String ipwifi = activeNetwork.getDetailedState().toString();
//                //   Log.d(LOG_TAG, "isWiFi: " + isWiFi);
//                Toast.makeText(this, "isWiFi: " + isWiFi, Toast.LENGTH_LONG).show();
//                if (!isWiFi)
//                    return;
//                WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
//                // параметры WIFI сканера
//                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
//
//                String ipwifi = wifiIpAddress(this);
//
//                //  Log.d(LOG_TAG, connectionInfo.getSSID());
//                Toast.makeText(this, "Connected to Internet: " + connectionInfo.getSSID(), Toast.LENGTH_LONG).show();
//

                /////////////////////////////////////////

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
                try {
                    txtLog.setText("       ...       ");
                    txtLog.setBackgroundColor(Color.WHITE);
// запускаем длительную операцию

                    Toast.makeText(this, "ЖДИТЕ! ИДЕТ ЗАГРУЗКА ДАННЫХ", Toast.LENGTH_LONG);
                    if (filealmat.LoadSaveCsvToDB(this, DIR_SD, "price.csv",
                            "select * from " + DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE, DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE)) {
                        txtLog.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                        Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
                        txtLog.setBackgroundColor(Color.GREEN);

                        //    textview.setBackgroundColor(Color.WHITE);
                        //    btnLoadAll.getBackground().setColorFilter(Color.parseColor("Данные обновлены"), PorterDuff.Mode.DARKEN);
                    } else {
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
                //               progressBar.setVisibility(ProgressBar.INVISIBLE);
//===============================

//===========================================
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

    private void bindToNetwork() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder;
        // Log.d(TAG, "All OK 123 !!!!!!!!!!!!!!!");
        Toast.makeText(this, "All OK !!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new NetworkRequest.Builder();
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            connectivityManager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(Network network) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        connectivityManager.bindProcessToNetwork(network);
                        //Log.d(TAG, "All OK !!!!!!!!!!!!!!!");
                    } else {
                        ConnectivityManager.setProcessDefaultNetwork(network);
                    }
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    connectivityManager.unregisterNetworkCallback(this);
                }
            });
        }
    }


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
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();

            String ipwifi = wifiIpAddress(context);

            Log.d(LOG_TAG, connectionInfo.getSSID());
            Toast.makeText(context, "Connected to Internet: " + connectionInfo.getSSID(), Toast.LENGTH_LONG).show();

        }
    }


    protected String wifiIpAddress(Context context) {
        String ipAddressString;
        String ipGatewayString;
        String ipServerString;
        String ipNetmaskString;
        String s_dns1;
        String s_dns2;
        String s_gateway;
        String s_ipAddress;
        String s_leaseDuration;
        String s_netmask;
        String s_serverAddress;
        TextView info;
        DhcpInfo d;
        WifiManager wifii;

        wifii = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        d = wifii.getDhcpInfo();
        String sss = d.toString();
        s_dns1 = "DNS 1: " + String.valueOf(d.dns1);
        s_dns2 = "DNS 2: " + String.valueOf(d.dns2);
        s_gateway = "Default Gateway: " + String.valueOf(d.gateway);
        s_ipAddress = "IP Address: " + String.valueOf(d.ipAddress);
        s_leaseDuration = "Lease Time: " + String.valueOf(d.leaseDuration);
        s_netmask = "Subnet Mask: " + String.valueOf(d.netmask);
        s_serverAddress = "Server IP: " + String.valueOf(d.serverAddress);

        //////

        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        int ipGateway = d.gateway;
        int ipServer = d.serverAddress;
        int ipNetmask = d.netmask;
        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
            ipGateway = Integer.reverseBytes(ipGateway);
            ipServer = Integer.reverseBytes(ipServer);
            ipNetmask = Integer.reverseBytes(ipNetmask);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
        byte[] ipByteGateway = BigInteger.valueOf(ipGateway).toByteArray();
        // byte[] ipByteServer = BigInteger.valueOf(ipServer).toByteArray();
        byte[] ipByteNetmask = BigInteger.valueOf(ipNetmask).toByteArray();

        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
            ipGatewayString = InetAddress.getByAddress(ipByteGateway).getHostAddress();
            // ipServerString = InetAddress.getByAddress(ipByteServer).getHostAddress();
            //  ipNetmaskString = InetAddress.getByAddress(ipByteNetmask).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }


////////////////////////////////////
////////////////////////////////
        return ipAddressString;
    }

////////////////////////==========================/////////////////////////////////


    /////////////////////////////////
    public void Update(final Integer lastAppVersion, Context context) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Доступно обновление приложения rutracker free до версии " +
                                lastAppVersion + " - желаете обновиться? " +
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
        switch (id) {
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
        enableWifi();                                   //Проверяем включен ли WiFi, если нет то включаем
        // String wifiIpAddress;
        String ipAdressScaner = wifiIpAddress(this);
        try {
            if (setting.loadSetting(this)) {
                if (!setting.executeCommand(setting.sAdressServer)) {
                    txtLog.setText("НЕТ СВЯЗИ С СЕРВЕРОМ!");
                    txtLog.setBackgroundColor(Color.RED);
                    return;
                } else {
                    txtLog.setText("       ...       ");
                    txtLog.setBackgroundColor(Color.WHITE);
                }
            } else {
                txtLog.setText("НАСТРОЙКИ НЕ ЗАГРУЖЕНЫ!");
                txtLog.setBackgroundColor(Color.RED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
