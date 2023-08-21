package by.matveev.lenovostart.lib;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import by.matveev.lenovostart.MainActivity;

public class WIFIService extends Service {
    public String ipAdressScaner;
    public String ipMaskScaner;
    public String ipGatewayScaner;
    public String ipDNS1Scaner;
    public String ipDNS2Scaner;
   // public String ipServerScaner;
    public String ipDNS1;
    public String ipDNS2;
    IntentFilter intentFilter = new IntentFilter();                    //Создаем объект для отслеживания изменений в сети.
    private WiFiMonitor mWiFiMonitor;           //Объект WiFiMonitor, поиск сети, вывод доступных точек

    public WIFIService(Context context) throws SocketException, UnknownHostException {
        // This works both in tethering and when connected to an Access Point


        enableWifi(context);
        wifiIpAddress(context);

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
        String ipAdressScaner;
        ipAdressScaner = "asas";
    }

    @Override
    public void onStart(Intent intent, int startId) {//Service Started
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Сервис запущен", Toast.LENGTH_LONG).show();
        String ipAdressScaner;
        ipAdressScaner = "asas";
    }

    @Override
    public void onDestroy() {//Service Destroyed
        Toast.makeText(this, "Сервис выключен", Toast.LENGTH_LONG).show();

    }

    public boolean enableWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
       // if (!wifiManager.isWifiEnabled()) {
        while(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
//            Toast toast = Toast.makeText(context, "Wifi включен", Toast.LENGTH_SHORT);
//            toast.show();
            //return  true;
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


    protected Boolean wifiIpAddress(Context context) throws SocketException {
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
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();


        while (interfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback())
                continue; // Don't want to broadcast to the loopback interface

            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
            {
                InetAddress broadcast = interfaceAddress.getBroadcast();

                // Android seems smart enough to set to null broadcast to
                //  the external mobile network. It makes sense since Android
                //  silently drop UDP broadcasts involving external mobile network.
                if (broadcast == null)
                    continue;
                else{
               //     InetAddress ip = interfaceAddress.getAddress();
              //      ipAdressScaner = ip.toString().replaceAll("/", "");
                    short subnetmask = interfaceAddress.getNetworkPrefixLength(); //is another way to express subnet mask

                    ipMaskScaner = Short.toString(subnetmask);

                }

                // Use the broadcast
            }
        }


        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
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
            ipDNS1=Integer.reverseBytes(ipDNS1);
            ipDNS2=Integer.reverseBytes(ipDNS2);

        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
        byte[] ipByteGateway = BigInteger.valueOf(ipGateway).toByteArray();
  //      byte[] ipByteServer = BigInteger.valueOf(ipServer).toByteArray();
        byte[] ipByteNetmask = BigInteger.valueOf(ipNetmask).toByteArray();
        byte[] ipByteDNS1 = BigInteger.valueOf(ipDNS1).toByteArray();
        byte[] ipByteDNS2 = BigInteger.valueOf(ipDNS2).toByteArray();

        try {
            ipAdressScaner = InetAddress.getByAddress(ipByteArray).getHostAddress();
            ipGatewayScaner = InetAddress.getByAddress(ipByteGateway).getHostAddress();
            ipDNS1Scaner = InetAddress.getByAddress(ipByteDNS1).getHostAddress();
        //    ipDNS2Scaner = InetAddress.getByAddress(ipByteDNS2).getHostAddress();
     //       ipServerScaner = InetAddress.getByAddress(ipByteServer).getHostAddress();
    //        ipNetmaskString = InetAddress.getByAddress(ipByteNetmask).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
           // ipAddressString = null;
            return false;
        }


////////////////////////////////////
////////////////////////////////
        return true;
    }

////////////////////////==========================/////////////////////////////////


}