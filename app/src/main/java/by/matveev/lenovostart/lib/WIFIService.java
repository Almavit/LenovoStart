package by.matveev.lenovostart.lib;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class WIFIService extends Service {
    String ipAdressScaner;


    public WIFIService(Context context) {


        ipAdressScaner = "asas";
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
        String ipAdressScaner;
        ipAdressScaner = "asas";
    }

    public boolean enableWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            Toast toast = Toast.makeText(getApplicationContext(), "Wifi включен", Toast.LENGTH_SHORT);
            toast.show();
            return  true;
        }
        return true;
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
            ipNetmaskString = InetAddress.getByAddress(ipByteNetmask).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }


////////////////////////////////////
////////////////////////////////
        return ipAddressString;
    }

////////////////////////==========================/////////////////////////////////


}