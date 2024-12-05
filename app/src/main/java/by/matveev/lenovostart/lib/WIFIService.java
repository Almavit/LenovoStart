package by.matveev.lenovostart.lib;

import static android.system.OsConstants.AF_INET;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


import by.matveev.lenovostart.MainActivity;
import by.matveev.lenovostart.R;

public class WIFIService extends Service {
    public String ipAdressScaner = "";
    public String ipMaskScaner = "";
    public String ipGatewayScaner = "";
    public String ipDNS1Scaner = "";
    public String ipDNS2Scaner = "";
    public String ipMaskAddress = "";
    public String ipNameSSID = "";
    public String alertTitleWIFI = "";
    public String alertMessageWIFI = "";
    // public String ipServerScaner;
    public String ipDNS1 = "";
    public String ipDNS2 = "";
    IntentFilter intentFilter = new IntentFilter(); //Создаем объект для отслеживания изменений в сети.
    private WiFiMonitor mWiFiMonitor;               //Объект WiFiMonitor, поиск сети, вывод доступных точек

    public WIFIService(Context context) throws SocketException, UnknownHostException {
        // This works both in tethering and when connected to an Access Point
        if (enableWifi(context)) {
            wifiIpAddress(context);
        }
        //bindToNetwork();

        // intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);    //Произошло изменение сетевого подключения. IntentFilter должен содержать событие.

        // registerReceiver(mWiFiMonitor, intentFilter);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.//Not yet implemented
        throw new UnsupportedOperationException("Еще не реализовано");
    }


    @Override
    public void onCreate() {//The new Service was Created
        Toast.makeText(this, "Сервис создан", Toast.LENGTH_LONG).show();
//        String ipAdressScaner;
//        ipAdressScaner = "asas";
    }

    @Override
    public void onStart(Intent intent, int startId) {//Service Started
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Сервис запущен", Toast.LENGTH_LONG).show();
//        String ipAdressScaner;
//        ipAdressScaner = "asas";
    }

    @Override
    public void onDestroy() {//Service Destroyed
        Toast.makeText(this, "Сервис выключен", Toast.LENGTH_LONG).show();

    }

    public boolean enableWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        // if (!wifiManager.isWifiEnabled()) {
        while (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
//            Toast toast = Toast.makeText(context, "Wifi включен", Toast.LENGTH_SHORT);
//            toast.show();
            //return  true;
        }
        return true;
    }
//новая процедура создания подключения
    public Integer CreateWIFI(Context context,TextView textView, Spinner spinner){
        Integer codeError = 0;
        //Integer countConnectWIFI;
        Integer numberPosition;
        String networkSSID;
        ArrayList<String> list = new ArrayList<String>();
        Setting setting = new Setting();

//        DBHelper db = new DBHelper(context);
//        if(!db.DeleteDB("Price")){
//            codeError = 0;
//        }
        if (ScanListWifi(context) == 0){// получить список активных видимых wifi подключений
            alertTitleWIFI = "НАСТРОЙКИ НЕ СОХРАНЕНЫ";
            alertMessageWIFI = "НЕТ СВЯЗИ!               ВКЛЮЧИТЬ GPS";
            codeError = -1;
        }else{
            numberPosition = spinner.getSelectedItemPosition();
        }
        list = NameSSIDcsv(context, spinner);// список настроек с DB SqlLite IPTable
        if (list == null){
            //      alertTitleWIFI = "НАСТРОЙКИ НЕ СОХРАНЕНЫ";
            //alertMessageWIFI = "НЕТ ИМЕНИ SSID в IPtable!        ДОБАВИТЬ!";
            codeError = -2;
        }


        if(codeError == 0){
            // Создаем wifi подключение
            networkSSID = list.get(5).toString();
            if (!AddConnectWIFI(context, networkSSID, "WTy_74Ag")) {
                codeError = -3;
            }else {
                String IPAdress = list.get(1).toString() + "." + list.get(2).toString();
                if(!SaveSettingCSV(context,IPAdress)){// сохраняем полученные настройки в файл настроек setting.csv
                    codeError = -4;
                }else{
                    alertTitleWIFI = "НАСТРОЙКИ СОХРАНЕНЫ";
                    alertMessageWIFI = "Обновление сетевых настроек подразделения произведены успешно!";
                    // проверяем соотвествие активного имени SSID
                    try {
                        if (wifiIpAddress(context)){
                            if (ipNameSSID.equals(list.get(5).toString())) {//Ищем соотвествие имени WIFI  с магазином
                                // есть соотвествие
                                // alertTitleWIFI = "       ...       ";
                                // txtLog.setBackgroundColor(Color.WHITE);
                                //enableWifi();// Включаем WIFI
                            }else{
                                alertTitleWIFI = "НАСТРОЙКИ СОХРАНЕНЫ";
                                alertMessageWIFI = "НЕТ wifi активного " + list.get(5).toString() + " подключения!";
                                codeError = -5;
                            }
                        }else{
                            codeError = -6;
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }

                }

                /////////////////////////////////////////////////////////////////////////////////////////

                /////////////////////////////////////////////////////////////////////////////////////////

            }
        }
        textView.setText(alertTitleWIFI);
        if(codeError != 0){
            textView.setBackgroundColor(Color.RED);
        }else{
            textView.setBackgroundColor(Color.GREEN);
        }


        return codeError;
    }
    // получить список активных видимых wifi подключений
    private Integer ScanListWifi(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);

        List<ScanResult> scanResultList = wifi.getScanResults();//сканируем список активных WIFI
        // получаем список активных wifi подключений
        return CountConnectWIFI(scanResultList);
    }
    // количество видимых wifi подключений
    private Integer CountConnectWIFI(List<ScanResult> scanResultList) {

        return scanResultList.size();
    }
    // список настроек с DB SqlLite IPTable
    private ArrayList<String> NameSSIDcsv(Context context, Spinner spinner){
        ArrayList<String> list = new ArrayList<String>();
        list = SelectIPMask(context, "", spinner.getSelectedItemPosition(), spinner);
        if ((list.get(5) == null)&&(list.get(5).equals(""))) {
            // null пустое поле имени SSID wifi
            alertTitleWIFI = "НАСТРОЙКИ НЕ СОХРАНЕНЫ";
            alertMessageWIFI = "НЕТ ИМЕНИ SSID в IPtable!        ДОБАВИТЬ!";
            return null;
        }else{

        }
        return list;
    }
    // сохраняем полученные настройки в файл настроек setting.csv
    private boolean SaveSettingCSV(Context context, String ipAdress){
        Setting setting = new Setting();
        try {
            if(!setting.loadSetting(context)){
                alertTitleWIFI = "НАСТРОЙКИ НЕ ЗАГРУЖЕНЫ";
                alertMessageWIFI = "Загрузка настроек setting.csv не произошло!";
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!setting.saveSetting(context, ipAdress, setting.sUserFTP, setting.sPasswordFTP,
                    setting.sPortFTP, setting.sPathFile, setting.sModeWorking)) {             //(!setting.saveSetting(this)){
                alertTitleWIFI = "НАСТРОЙКИ НЕ СОХРАНЕНЫ";
                alertMessageWIFI = "Обновление сетевых настроек подразделения не произошло!";
                return false;

            } else {
                alertTitleWIFI = "НАСТРОЙКИ СОХРАНЕНЫ";
                alertMessageWIFI = "Обновление сетевых настроек подразделения произведены успешно!";
                //break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

//    private boolean CheckConnect(){
//
//        if (ipNameSSID.equals(list.get(5).toString())) {//Ищем соотвествие имени WIFI  с магазином
//            // есть соотвествие
//            txtLog.setText("       ...       ");
//            txtLog.setBackgroundColor(Color.WHITE);
//            //enableWifi();// Включаем WIFI
//        }
//        return true;
//    }
//=====================================================================================================================
    public boolean CreateWIFIConnect(Context context, String networkSSID, String password, String IPaddress, String ipMask, String ipDNS) throws IOException, InterruptedException {
        Setting setting = new Setting();
        Integer iCountConnect = 0;
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = manager.getConnectionInfo();
        List<WifiConfiguration> configuredNetworks;
        WifiConfiguration wifiConf = null;

     //   List<ScanResult> scanResultList = ScanListWifi();//сканируем список активных WIFI



        if (!AddConnectWIFI(context, networkSSID, password)) {
              //  Toast.makeText(getApplicationContext(), "WIFI СОЕДИНЕНИЕ ОТСУТСТВУЕТ",
        }

        //  WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        configuredNetworks = manager.getConfiguredNetworks();
        for (WifiConfiguration conf : configuredNetworks) {
            if (conf.networkId == connectionInfo.getNetworkId()) {
                wifiConf = conf;
                break;
            }
        }
        if(!IPaddress.equals("")){
            if (wifiConf != null)
            {
                try
                {
                    setStaticIpConfiguration(manager, wifiConf,
                            InetAddress.getByName(IPaddress), 24,//"10.250.1.130"
                            InetAddress.getByName(ipMask),//"10.250.1.3"
                            new InetAddress[] { InetAddress.getByName(ipDNS), InetAddress.getByName("0.0.0.0") });//"8.8.8.8"
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            try {
                setIpAssignment("STATIC", wifiConf); //or "DHCP" for dynamic setting
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            // setIpAddress(InetAddress.getByName("192.168.0.100"), 24, wifiConf);
            // setGateway(InetAddress.getByName("4.4.4.4"), wifiConf);
            //  setDNS(InetAddress.getByName("4.4.4.4"), wifiConf);
            manager.updateNetwork(wifiConf); //apply the setting
            manager.saveConfiguration(); //Save it
        }
        return  true;
    }



    // Создаем wifi подключение
    public boolean AddConnectWIFI(Context context, String nameSSID, String pass){

        //==================================================================

        WifiManager wifis = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + nameSSID + "\"";//"\"dd-wrt\"";
        wc.preSharedKey  = "\"" + pass + "\"";// "\"WTy_74Ag\"";
        wc.hiddenSSID = true;

        wc.status = WifiConfiguration.Status.ENABLED;
//        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

        int res = wifis.addNetwork(wc);
        Log.d("WifiPreference", "add Network returned " + res );
        boolean b = wifis.enableNetwork(res, true);
        Log.d("WifiPreference", "enableNetwork returned " + b );
//        WifiConfiguration wifiConfig = new WifiConfiguration();
//        wifiConfig.SSID = String.format("\"%s\"","dd" );
//        wifiConfig.preSharedKey = String.format("\"%s\"", "ll");
//        wifiConfig.allowedProtocols
//
//        WifiManager wifiManagers = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
////remember id
//        int netId = wifiManagers.addNetwork(wifiConfig);
//        wifiManagers.disconnect();
//        wifiManagers.enableNetwork(netId, true);
//        wifiManagers.reconnect();

        try {
            Thread.sleep(7000); // пауза на 1 секунду
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void bindToNetwork() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder;
        // Log.d(TAG, "All OK 123 !!!!!!!!!!!!!!!");
        // Toast.makeText(this, "All OK !!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
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

            //  String ipwifi = wifiIpAddress(context);

            Log.d(LOG_TAG, connectionInfo.getSSID());
            Toast.makeText(context, "Connected to Internet: " + connectionInfo.getSSID(), Toast.LENGTH_LONG).show();

        }
    }

    public Boolean  addAdapterWiFi(Context context, Spinner spinner) {
        final DBRepository repositorys = new DBRepository(context);

     //   ArrayAdapter<String> datadapterdat;
        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter<String> datadapter;
        list = repositorys.getDataWifi();
        datadapter = new ArrayAdapter<String>(context, R.layout.spinner_text, list);
       // datadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
      //  datadapter = ArrayAdapter.createFromResource(context, datada, R.layout.spinner_text);

        spinner.setAdapter(datadapter);
        spinner.setSelection(0);
        if(list.size() != 0){
            return  true;
        }else {
            return false;
        }
    }

    public ArrayList<String> SelectIPMask(Context context, String sIPMask, Integer iPosition, Spinner spinner) {
        //final DBRepository repositorys = new DBRepository(getApplicationContext());
        ArrayList<String> list = new ArrayList<String>();
        DBHelper dbHelper = new DBHelper(this);
        if(iPosition == 0) {
            list.addAll(dbHelper.getSelectIPMask(context, sIPMask));
            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            if(list.size() > 0){
                iPosition = adapter.getPosition(list.get(0).toString());
                spinner.setSelection(iPosition);
            }else{
                spinner.setSelection(-1);
            }

        }else{
            spinner.setSelection(iPosition);
            list.addAll(dbHelper.getSelectIPName(context, spinner.getSelectedItem().toString()));
            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
// настройка сети

        }
        return list;
    }

    ////////////////////////////

    public Boolean wifiIpAddress(Context context) throws SocketException {
        Boolean returnStatus;
        DhcpInfo d;
        WifiManager wifii;
        ConnectivityManager connManager = null;
        wifii = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        d = wifii.getDhcpInfo();

//        String sss = d.toString();
//        s_dns1 = "DNS 1: " + String.valueOf(d.dns1);
//        s_dns2 = "DNS 2: " + String.valueOf(d.dns2);
//        s_gateway = "Default Gateway: " + String.valueOf(d.gateway);
//        s_ipAddress = "IP Address: " + String.valueOf(d.ipAddress);
//        s_leaseDuration = "Lease Time: " + String.valueOf(d.leaseDuration);
//        s_netmask = "Subnet Mask: " + String.valueOf(d.netmask);
//        s_serverAddress = "Server IP: " + String.valueOf(d.serverAddress);

        //////
//        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();


//        while (interfaces.hasMoreElements()) {
//            NetworkInterface networkInterface = interfaces.nextElement();
//
//            if (networkInterface.isLoopback())
//                continue; // Don't want to broadcast to the loopback interface
//
//            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
//                InetAddress broadcast = interfaceAddress.getBroadcast();
//
//                // Android seems smart enough to set to null broadcast to
//                //  the external mobile network. It makes sense since Android
//                //  silently drop UDP broadcasts involving external mobile network.
//                if (broadcast == null)
//                    continue;
//                else {
//                    //     InetAddress ip = interfaceAddress.getAddress();
//                    //      ipAdressScaner = ip.toString().replaceAll("/", "");
//                    short subnetmask = interfaceAddress.getNetworkPrefixLength(); //is another way to express subnet mask
//
//                    ipMaskScaner = Short.toString(subnetmask);
//
//                }
//
//                // Use the broadcast
//            }
//        }

      //  WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);

        try{
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }catch (Exception ex){
            returnStatus = false;
        }
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        networkInfo.getActiveNetworkInfo();
//        returnStatus = networkInfo.isConnected();
        if (networkInfo.isConnected()) {
            //  WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifii.getConnectionInfo();
            wifiInfo.getSSID();
            String ssid = networkInfo.getExtraInfo();
//            ssid = wifiInfo.getSSID();
//            ssid.replaceAll("^\"|\"$", "");
            ipNameSSID = ssid.replaceAll("^\"|\"$", "");


            int ipAddress = wifii.getConnectionInfo().getIpAddress();
            ipAddress = d.ipAddress;
            int ipGateway = d.gateway;
            int ipServer = d.serverAddress;
            int ipNetmask = d.netmask;
            int ipDNS1 = d.dns1;
            int ipDNS2 = d.dns2;
            // Convert little-endian to big-endianif needed
            if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                ipAddress = Integer.reverseBytes(ipAddress);
                ipGateway = Integer.reverseBytes(ipGateway);
                ipServer = Integer.reverseBytes(ipServer);
                ipNetmask = Integer.reverseBytes(ipNetmask);
                ipDNS1 = Integer.reverseBytes(ipDNS1);
                ipDNS2 = Integer.reverseBytes(ipDNS2);

            }

            byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
            byte[] ipByteGateway = BigInteger.valueOf(ipGateway).toByteArray();
            //      byte[] ipByteServer = BigInteger.valueOf(ipServer).toByteArray();
            byte[] ipByteNetmask = BigInteger.valueOf(ipNetmask).toByteArray();
            byte[] ipByteDNS1 = BigInteger.valueOf(ipDNS1).toByteArray();
            byte[] ipByteDNS2 = BigInteger.valueOf(ipDNS2).toByteArray();

            try {
                ipAdressScaner = InetAddress.getByAddress(ipByteArray).getHostAddress();
                long longAddr = ipAddress;
                ipMaskAddress = IpMaskAdresThree(longAddr);

//                  ipGatewayScaner = InetAddress.getByAddress(ipByteGateway).getHostAddress();
//                  ipDNS1Scaner = InetAddress.getByAddress(ipByteDNS1).getHostAddress();
                //  ipDNS2Scaner = InetAddress.getByAddress(ipByteDNS2).getHostAddress();
                //  ipServerScaner = InetAddress.getByAddress(ipByteServer).getHostAddress();
                //  ipNetmaskString = InetAddress.getByAddress(ipByteNetmask).getHostAddress();
                return true;
            } catch (UnknownHostException ex) {
                Log.e("WIFIIP", "Unable to get host address.");
                // ipAddressString = null;
                returnStatus = false;
            }
            returnStatus = true;
        }else{
            returnStatus = false;
        }
///////////////////////////////////
////////////////////////////////
        //ipNameSSID = "null";
        return returnStatus;
    }

    ////////////////////////==========================/////////////////////////////////
    public String IpMaskAdresThree(long addr) {
        String ipMaskAdress = "";
        if (addr != 0) {
            ipMaskAddress = Long.toString((addr >> 24) & 0xFF) + "." + Long.toString((addr >> 16) & 0xFF) + "." + Long.toString((addr >> 8) & 0xFF);
            ipMaskAddress = ipMaskAddress + "";
        }
        return ipMaskAddress;
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
}