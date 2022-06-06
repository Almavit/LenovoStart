package by.matveev.lenovostart;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import by.matveev.lenovostart.lib.FTPModel;
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

    Button btnFourField;
    Button btnTwoField;
    Button btnTwoFieldQuan;
    Button btnOneField;
    Button btnEditor;
    Button btnStartElectron;
    Button btnSetting;
    Button btnEditDatTxt;
    TextView txtLog;
    EditText txtIp;
    CheckBox chkWiFi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

//        btnSaveToServer = (Button) findViewById(R.id.btnSaveToServer);
//        btnSaveToServer.setOnClickListener(this);

        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);

        btnStartElectron = (Button) findViewById(R.id.btnStartElectron);
        btnStartElectron.setOnClickListener(this);

        btnEditDatTxt = (Button) findViewById(R.id.btnEditDatTxt);
        btnEditDatTxt.setOnClickListener(this);


        txtLog = (TextView) findViewById(R.id.txtLog);
    }


     public void onClick(View v) {
        Intent intent = new Intent(this, ScanerActivity.class);
        //View.INVISIBLE = 4
        //View.VISIBLE = 0
        //View.GONE = 8

/*        intent.putExtra("VisibleTxtBarcode", View.VISIBLE);
        intent.putExtra("VisibleIntBarcode", View.VISIBLE);*/
        intent.putExtra("VisibleTxtQuantity", View.VISIBLE);
        intent.putExtra("VisibleIntQuantity", View.VISIBLE);
        intent.putExtra("VisibleTxtPrice", View.VISIBLE);
        intent.putExtra("VisibleIntPrice", View.VISIBLE);
        intent.putExtra("VisibleTxtNumber", View.VISIBLE);
        intent.putExtra("VisibleIntNumber", View.VISIBLE);


        switch (v.getId()){
            case R.id.btnEditDatTxt:
                Intent intentEditDatTxt = new Intent(this, EditData.class);
                startActivity(intentEditDatTxt);

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
            case R.id.btnStartElectron:
                Intent intentStartElectronDocument = new Intent(this, StartElectronDocument.class);
                startActivity(intentStartElectronDocument);
        
                break;

            case R.id.btnSetting:

                Intent intentSetting = new Intent(this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            default:
                break;

         }
//         startActivity(intent);
    }
    // Start the  service
    public void startNewService(View view) {

        startService(new Intent(this, WIFIService.class));
    }

    // Stop the  service
    public void stopNewService(View view) {

        stopService(new Intent(this, WIFIService.class));
    }
////////////////////////////////////
//ftp

//    public  void ftpConn(String hostAddress, String log, String password) throws FileNotFoundException {
//        FTPClient fClient = new FTPClient();
//        // Environment.getExternalStorageDirectory().toString()
//        //txtLog.setText(Environment.getExternalStorageDirectory() + "/Documents/Dat1.txt");
//        if (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//           // Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
//           // ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
//            return;
//        }
//        FileInputStream fInput = new FileInputStream( Environment.getExternalStorageDirectory() + "/Documents/Dat1.txt");
//        String fs = "Yes.txt";
//        try {
//            fClient.connect("10.250.1.15",21);
//
//            fClient.enterLocalPassiveMode();
//            fClient.login(log, password);
//            fClient.storeFile(fs, fInput);
//            fClient.logout();
//            fClient.disconnect();
//            txtLog.setText("Yes ftp");
//        } catch (IOException ex) {
//            System.err.println(ex);
//            txtLog.setText("No ftp");
//        }
//    }
///////////////////////////
    // сохранить с сайта
//private void onDownloadComplete(boolean success) {
//    // файл скачался, можно как-то реагировать
//    Log.i("***", "************** " + success);
//}
//
//    private class LoadFile extends Thread {
//        private final String src;
//        private final File dest;
//
//        LoadFile(String src, File dest) {
//            this.src = src;
//            this.dest = dest;
//        }
//
//        @Override
//        public void run() {
//            try {
//
//                URL u = new URL(src);
//                FileUtils.copyURLToFile(u, dest,1000,1000);
//                //onDownloadComplete(true);
//                txtLog.setText("Yes save" + dest.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//                //onDownloadComplete(false);
//               // txtLog.setText("No save" + dest.toString());
//            }
//        }
//    }

///////////////////////////////
//private boolean executeCommand(String ip){
//        System.out.println("executeCommand");
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 "+ ip);
//            int mExitValue = mIpAddrProcess.waitFor();
//            txtLog.setText(" mExitValue "+ mExitValue);
//            if(mExitValue==0){
//                txtLog.setText("YES+++");
//                return true;
//            }else{
//                txtLog.setText("No---");
//                return false;
//            }
//        }
//        catch (InterruptedException ignore) {
//            ignore.printStackTrace();
//            System.out.println(" Exception:"+ignore);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println(" Exception:"+e);
//        } return false;
//    }
//////////////

///////////////////////////////  end ping

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

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
