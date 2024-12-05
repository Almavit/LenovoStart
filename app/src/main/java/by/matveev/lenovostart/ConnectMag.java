package by.matveev.lenovostart;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;
import by.matveev.lenovostart.lib.DBSampleHelper;
import by.matveev.lenovostart.lib.Filealmat;
import by.matveev.lenovostart.lib.Setting;
import by.matveev.lenovostart.lib.WIFIService;

public class ConnectMag extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Setting setting;
    Button btnLoadConnect;
    Button btnSaveConnect;
    Button btnUpdateConnect;
    Button btnCreatenewWIFI;

    Filealmat filealmat;

    EditText txtIPmask;
    EditText txtIpmag;
    EditText txtIpmodem;
    EditText txtIpscaner;
    TextView txtlogConnect;

    Spinner spinMag;
    WIFIService wifis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_mag);

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
        btnCreatenewWIFI = (Button) findViewById(R.id.btnCreatenewWIFI);

        spinMag = (Spinner) findViewById(R.id.spinMag);
        spinMag.setOnItemSelectedListener(this);

        try {
            wifis = new WIFIService(this);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

//        if (wifis.addAdapterWiFi(this, spinMag)) {
//            if (!wifis.ipNameSSID.equals("")){
//                wifis.SelectIPMask(this, wifis.ipMaskAddress,0,spinMag);
//                txtlogConnect.setText("ДАННЫЕ ОБНОВЛЕНЫ");
//                Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
//                txtlogConnect.setBackgroundColor(Color.GREEN);
//            } else {
//                txtlogConnect.setText("ДАННЫЕ НЕ ЗАГРУЖЕНЫ! НЕТ СВЯЗИ");
//                txtlogConnect.setBackgroundColor(Color.RED);
//            }
//        }
    }

    public void CreateNewWIFI(View v, String nameSSID, String pass) {
        try {
            WIFIService wifis = new WIFIService(this);
            //=================================
            if (!wifis.AddConnectWIFI(this, nameSSID, pass))//"\"dd-wrt\""
                Toast.makeText(getApplicationContext(), "WIFI СОЕДИНЕНИЕ ОТСУТСТВУЕТ",
                        Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SaveConnect(View v) {
        ArrayList<String> list = new ArrayList<String>();
        //String sIPAdressServer;
        //Setting setting = new Setting();
        String[] stringsetting = new String[6];
        stringsetting[0] =
                stringsetting[0] = txtIPmask.getText().toString();
        stringsetting[1] = txtIpmag.getText().toString();
        stringsetting[2] = txtIpmodem.getText().toString();
        stringsetting[3] = txtIpscaner.getText().toString();
        //sIPAdressServer = txtIPmask.getText().toString() + "." + txtIpmodem.getText().toString();
        if(wifis.CreateWIFI(this, txtlogConnect, spinMag) == 0){
            Toast.makeText(getApplicationContext(), "НАСТРОЙКИ СОХРАНЕНЫ",
                    Toast.LENGTH_SHORT).show();
        }
        if (!wifis.ipNameSSID.equals("null")){
            list = wifis.SelectIPMask(this, wifis.ipMaskAddress, 0,spinMag);
            if (0 != list.size()){

                if (list.get(1).toString() != null && !list.get(1).toString().isEmpty() && !list.get(1).toString().equals("null")){
                    txtIPmask.setText(list.get(1).toString());
                }else{
                    txtlogConnect.setText("НЕТ МАСКИ СЕТИ wifi.csv");
                    txtlogConnect.setBackgroundColor(Color.RED);
                    txtIPmask.setText("");
                }
                if (list.get(2).toString() != null && !list.get(2).toString().isEmpty() && !list.get(2).toString().equals("null")){
                    txtIpmag.setText(list.get(2).toString());
                }else{
                    txtlogConnect.setText("НЕТ IP магазина wifi.csv");
                    txtlogConnect.setBackgroundColor(Color.RED);
                    txtIpmag.setText("");
                }
                if (list.get(3).toString() != null && !list.get(3).toString().isEmpty() && !list.get(3).toString().equals("null")){
                    txtIpmodem.setText(list.get(3).toString());
                }else{
                    txtlogConnect.setText("НЕТ IP модема wifi.csv");
                    txtlogConnect.setBackgroundColor(Color.RED);
                    txtIpmodem.setText("");
                }
                if (list.get(4).toString() != null && !list.get(4).toString().isEmpty() && !list.get(4).toString().equals("null")){
                    txtIpscaner.setText(list.get(4).toString());
                }else{
                    txtIpscaner.setText("");
                    txtlogConnect.setText("НЕТ IP сканера wifi.csv");
                    txtlogConnect.setBackgroundColor(Color.RED);
                }
                if (list.get(5).toString() != null && !list.get(5).toString().isEmpty() && !list.get(5).toString().equals("null")){
                    //sNameWifi = list.get(5).toString();
                }else{
                    txtlogConnect.setText("НЕТ имени SSID wifi.csv");
                    txtlogConnect.setBackgroundColor(Color.RED);
                }
                txtlogConnect.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
                txtlogConnect.setBackgroundColor(Color.GREEN);
            } else {
                txtlogConnect.setText("НЕТ ДАННЫХ СЕТИ wifi.csv");
                txtlogConnect.setBackgroundColor(Color.RED);
            }
        }
           // setting.nextLine = stringsetting;
//        DBHelper dbHelper = new DBHelper(this);
//        dbHelper.sa
//        try {
//
//            setting.saveSetting(this, sIPAdressServer, setting.sUserFTP, setting.sPasswordFTP,
//                    setting.sPortFTP, setting.sPathFile, setting.sModeWorking);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    private static void setStaticIpConfiguration(WifiManager manager, WifiConfiguration config, InetAddress ipAddress, int prefixLength, InetAddress gateway, InetAddress[] dns) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException {
        // First set up IpAssignment to STATIC.
        Object ipAssignment = getEnumValue("android.net.IpConfiguration$IpAssignment", "STATIC");
        callMethod(config, "setIpAssignment", new String[]{"android.net.IpConfiguration$IpAssignment"}, new Object[]{ipAssignment});

        // Then set properties in StaticIpConfiguration.
        Object staticIpConfig = newInstance("android.net.StaticIpConfiguration");
        Object linkAddress = newInstance("android.net.LinkAddress", new Class<?>[]{InetAddress.class, int.class}, new Object[]{ipAddress, prefixLength});

        setField(staticIpConfig, "ipAddress", linkAddress);
        setField(staticIpConfig, "gateway", gateway);
        getField(staticIpConfig, "dnsServers", ArrayList.class).clear();
        for (int i = 0; i < dns.length; i++)
            getField(staticIpConfig, "dnsServers", ArrayList.class).add(dns[i]);

        callMethod(config, "setStaticIpConfiguration", new String[]{"android.net.StaticIpConfiguration"}, new Object[]{staticIpConfig});
        manager.updateNetwork(config);
        manager.saveConfiguration();
    }

    private static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        return newInstance(className, new Class<?>[0], new Object[0]);
    }

    private static Object newInstance(String className, Class<?>[] parameterClasses, Object[] parameterValues) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        Constructor<?> constructor = clz.getConstructor(parameterClasses);
        return constructor.newInstance(parameterValues);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object getEnumValue(String enumClassName, String enumValue) throws ClassNotFoundException {
        Class<Enum> enumClz = (Class<Enum>) Class.forName(enumClassName);
        return Enum.valueOf(enumClz, enumValue);
    }

    private static void setField(Object object, String fieldName, Object value) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.set(object, value);
    }

    private static <T> T getField(Object object, String fieldName, Class<T> type) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        return type.cast(field.get(object));
    }

    private static void callMethod(Object object, String methodName, String[] parameterTypes, Object[] parameterValues) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Class<?>[] parameterClasses = new Class<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
            parameterClasses[i] = Class.forName(parameterTypes[i]);

        Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);

        method.invoke(object, parameterValues);

    }

    public static void setIpAssignment(String assign, WifiConfiguration wifiConf)
            throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Object ipConfiguration = wifiConf.getClass().getMethod("getIpConfiguration").invoke(wifiConf);
            setEnumField(ipConfiguration, assign, "ipAssignment");
        } else {
            setEnumField(wifiConf, assign, "ipAssignment");
        }
    }

    private static void setEnumField(Object obj, String value, String name)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Field f = obj.getClass().getField(name);


        f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
    }

    ////////////////////////////////////////////////////////////
//    public void test(Context context, String networkSSID, String password) {
//        try {
//            WIFIService wifis = new WIFIService(this);
//            //=================================
//            if (!wifis.AddConnectWIFI(this, networkSSID, password))
//                Toast.makeText(getApplicationContext(), "WIFI СОЕДИНЕНИЕ ОТСУТСТВУЕТ",
//                        Toast.LENGTH_SHORT).show();
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            Thread.sleep(5000); // пауза на 1 секунду
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//
//        WifiConfiguration wifiConf = null;
//        //  WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo connectionInfo = manager.getConnectionInfo();
//        List<WifiConfiguration> configuredNetworks = manager.getConfiguredNetworks();
//
//        for (WifiConfiguration conf : configuredNetworks) {
//            if (conf.networkId == connectionInfo.getNetworkId()) {
//                wifiConf = conf;
//                break;
//            }
//        }
//        String fff = wifiConf.SSID;
//        try {
//                if (wifiConf != null)
//                {
//                    try
//                    {
//                        setStaticIpConfiguration(manager, wifiConf,
//                                InetAddress.getByName("10.250.1.130"), 24,
//                                InetAddress.getByName("10.250.1.3"),
//                                new InetAddress[] { InetAddress.getByName("8.8.8.8"), InetAddress.getByName("0.0.0.0") });
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            setIpAssignment("STATIC", wifiConf); //or "DHCP" for dynamic setting
//           // setIpAddress(InetAddress.getByName("192.168.0.100"), 24, wifiConf);
//           // setGateway(InetAddress.getByName("4.4.4.4"), wifiConf);
//          //  setDNS(InetAddress.getByName("4.4.4.4"), wifiConf);
//            manager.updateNetwork(wifiConf); //apply the setting
//            manager.saveConfiguration(); //Save it
//            setting = new Setting();
//            Integer iCountConnect = 0;
//            if (setting.loadSetting(this)) {
//
//                while (!setting.executeCommand(setting.sAdressServer)) {
//                    Thread.sleep(10000);
//                    iCountConnect++;
//                    if(iCountConnect > 10) {
//                        break;
//                    }
//                }
//            }
//            if(iCountConnect <= 10) {
//                txtlogConnect.setText("НОВЫЙ WIFI АДРЕС");
//                txtlogConnect.setBackgroundColor(Color.GREEN);
//            }else{
//                txtlogConnect.setText("НЕТ СВЯЗИ С СЕРВЕРОМ");
//                txtlogConnect.setBackgroundColor(Color.RED);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//    public void CreateWIFI(View v) {
//
////          test(this, "\"Office33\"", "WTy_74Ag");
//
//    }

    public void LoadSetting(View v) throws SocketException, UnknownHostException {
        ArrayList<String> list = new ArrayList<String>();
        WIFIService wifis = new WIFIService(this);
        DBHelper dbHelper = new DBHelper(this);
        String sqlStroka;
       // String tableName;
        filealmat = new Filealmat();
        //tableName = DBSampleHelper.DBConnectIP.TABLE_IP;
        sqlStroka = "select * from " + DBSampleHelper.DBConnectIP.TABLE_IP;
//        try {
            if (!filealmat.LoadFileCsv( filealmat.NameDirectory, "wifi.csv")) {
                txtlogConnect.setText("ФАЙЛ wifi.csv отсутствует или поврежден");
                txtlogConnect.setBackgroundColor(Color.RED);

            } else {
//            if (!filealmat.LoadCsvFileFtp(this, filealmat.NameDirectory, "wifi.csv")) {
//                txtlogConnect.setText("ФАЙЛ wifi.csv не загружен");
//                txtlogConnect.setBackgroundColor(Color.RED);
//            } else {
                if (!dbHelper.DBSaveData(this, DBSampleHelper.DBConnectIP.TABLE_IP, sqlStroka, filealmat.reader)) {
                    txtlogConnect.setText("ДАННЫЕ wifi.csv В БД НЕ СОХРАНЕНЫ");
                    txtlogConnect.setBackgroundColor(Color.RED);
                }
// добавить в текстовые поля параметры настроек
                if (wifis.addAdapterWiFi(this, spinMag)) {
                    if (!wifis.ipNameSSID.equals("null")){
                        list = wifis.SelectIPMask(this, wifis.ipMaskAddress, 0,spinMag);
                        if (0 != list.size()){

                            if (list.get(1).toString() != null && !list.get(1).toString().isEmpty() && !list.get(1).toString().equals("null")){
                                txtIPmask.setText(list.get(1).toString());
                            }else{
                                txtlogConnect.setText("НЕТ МАСКИ СЕТИ wifi.csv");
                                txtlogConnect.setBackgroundColor(Color.RED);
                                txtIPmask.setText("");
                            }
                            if (list.get(2).toString() != null && !list.get(2).toString().isEmpty() && !list.get(2).toString().equals("null")){
                                txtIpmag.setText(list.get(2).toString());
                            }else{
                                txtlogConnect.setText("НЕТ IP магазина wifi.csv");
                                txtlogConnect.setBackgroundColor(Color.RED);
                                txtIpmag.setText("");
                            }
                            if (list.get(3).toString() != null && !list.get(3).toString().isEmpty() && !list.get(3).toString().equals("null")){
                                txtIpmodem.setText(list.get(3).toString());
                            }else{
                                txtlogConnect.setText("НЕТ IP модема wifi.csv");
                                txtlogConnect.setBackgroundColor(Color.RED);
                                txtIpmodem.setText("");
                            }
                            if (list.get(4).toString() != null && !list.get(4).toString().isEmpty() && !list.get(4).toString().equals("null")){
                                txtIpscaner.setText(list.get(4).toString());
                            }else{
                                txtIpscaner.setText("");
                                txtlogConnect.setText("НЕТ IP сканера wifi.csv");
                                txtlogConnect.setBackgroundColor(Color.RED);
                            }
                            if (list.get(5).toString() != null && !list.get(5).toString().isEmpty() && !list.get(5).toString().equals("null")){
                                //sNameWifi = list.get(5).toString();
                            }else{
                                txtlogConnect.setText("НЕТ имени SSID wifi.csv");
                                txtlogConnect.setBackgroundColor(Color.RED);
                            }
                            txtIPmask.setText(list.get(1).toString());
                            txtIpmag.setText(list.get(2).toString());
                            txtIpmodem.setText(list.get(3).toString());
                            txtIpscaner.setText(list.get(4).toString());
                            txtIpmodem.setText(list.get(5).toString());

                            txtlogConnect.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                            Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
                            txtlogConnect.setBackgroundColor(Color.GREEN);
                        } else {
                            txtlogConnect.setText("НЕТ ДАННЫХ СЕТИ wifi.csv");
                            txtlogConnect.setBackgroundColor(Color.RED);
                        }
                    }
                }
            }

//        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void LoadAllSetting(View v) {

        filealmat = new Filealmat();
        txtlogConnect.setText("       ...       ");
        txtlogConnect.setBackgroundColor(Color.WHITE);
// запускаем длительную операцию

        Toast.makeText(this, "ЖДИТЕ! ", Toast.LENGTH_LONG);
        if (!filealmat.LoadFileCsv( filealmat.NameDirectory, "wifi.csv")) {
            txtlogConnect.setText("ФАЙЛ wifi.csv отсутствует или поврежден");
            txtlogConnect.setBackgroundColor(Color.RED);

        } else {

        }
        try {
            if (filealmat.LoadSaveCsvToDB(this, filealmat.NameDirectory, filealmat.NameFileCSV_IP,
                    "select * from " + DBSampleHelper.DBConnectIP.TABLE_IP, DBSampleHelper.DBConnectIP.TABLE_IP)) {
                WIFIService wifis = new WIFIService(this);
                if (wifis.addAdapterWiFi(this, spinMag)) {
                    if (!wifis.ipNameSSID.equals("null")){
                        ArrayList<String> list = new ArrayList<String>();
                        list = wifis.SelectIPMask(this, wifis.ipMaskAddress, 0,spinMag);

                        txtIPmask.setText(list.get(1).toString());
                        txtIpmag.setText(list.get(2).toString());
                        txtIpmodem.setText(list.get(3).toString());
                        txtIpscaner.setText(list.get(4).toString());
                        txtIpmodem.setText(list.get(5).toString());

                        txtlogConnect.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                        Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
                        txtlogConnect.setBackgroundColor(Color.GREEN);
                    } else {
                        txtlogConnect.setText("НЕТ СВЯЗИ");
                        txtlogConnect.setBackgroundColor(Color.RED);
                    }
                }

            } else {
                txtlogConnect.setText("НЕТ СВЯЗИ");
                txtlogConnect.setBackgroundColor(Color.RED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedPosition = spinMag.getSelectedItem().toString();
        Integer numberPosition = position;
        wifis.SelectIPMask(this,"", numberPosition,spinMag);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//
    }

    @Override
    public void onResume(){
        super.onResume();
        if (wifis.addAdapterWiFi(this, spinMag)) {
            if (!wifis.ipNameSSID.equals("")){
                wifis.SelectIPMask(this, wifis.ipMaskAddress,0,spinMag);
                txtlogConnect.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                Toast.makeText(this, "ДАННЫЕ ОБНОВЛЕНЫ", Toast.LENGTH_LONG);
                txtlogConnect.setBackgroundColor(Color.GREEN);
              //  LoadAllSetting(v);

            } else {
                txtlogConnect.setText("ДАННЫЕ НЕ ЗАГРУЖЕНЫ! НЕТ СВЯЗИ");
                txtlogConnect.setBackgroundColor(Color.RED);
            }
        }
    }

}