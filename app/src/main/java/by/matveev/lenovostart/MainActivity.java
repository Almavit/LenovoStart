package by.matveev.lenovostart;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import com.opencsv.CSVReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.FTPModel;
import by.matveev.lenovostart.lib.Filealmat;
//import by.matveev.lenovostart.lib.MyPremission;
import by.matveev.lenovostart.lib.MyPremission;
import by.matveev.lenovostart.lib.ProgressTextView;
import by.matveev.lenovostart.lib.Setting;
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

    SQLiteDatabase database;
    SQLiteDatabase db;

    final String FILENAME_CSV = "999.csv";
    final String DIR_SD = "Documents";

    private static final String FILENAME = "./alphabet.utf8";
    private static final String ENCODING_WIN1251 = "windows-1251";
    private static final String ENCODING_UTF8 = "UTF-8";
    String[] nextLine;
    ProgressTextView progressTextViewMain;


    final String USER_NAME = "user_name";
    final String USER_PASSWORD = "user_passowrd";
    final String ADRESS_SERVER = "adress_server";
    final String PATH_FILE = "path_file";
    final String PORT_FTP = "21";
    final String MODE_WORKING = "1";

    String sAdressServer;
    String sUserFTP;
    String sPasswordFTP;
    String sPortFTP;
    String sPathFile;
    String sModeWorking;

    Button btnFourField;
    Button btnTwoField;
    Button btnTwoFieldQuan;
    Button btnOneField;
    Button btnEditor;
    Button btnStartElectron;
    Button btnSetting;
    Button btnEditDatTxt;
    Button btnQR;
    Button btnLoadAll;
    TextView txtLog;
    EditText txtIp;
    CheckBox chkWiFi;


   // MyPremission almPremission;
    Filealmat filealmat;


    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 //       MyFileToSD = new Filealmat();
        //MyFileToSD.NameFile = "Dat1.txt";
//        MyFileToSD.writeFileSD("Documents","Dat1.txt", null);
        filealmat = new Filealmat();
        MyPremission almPremission = new MyPremission();
//
        Boolean Premis = true;
//        do{
//            if (Premis){
                if (!almPremission.myPremission(this)) {
                    Premis = true;
                } else {
                    Premis = false;
                }
//            }
//
//        } while(!Premis);

//        Setting setting = new Setting();
//        setting.loadSetting(this);
//        String sAdressServer = setting.sAdressServer;

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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



//        btnSaveToServer = (Button) findViewById(R.id.btnSaveToServer);
//        btnSaveToServer.setOnClickListener(this);

        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);

        btnStartElectron = (Button) findViewById(R.id.btnStartElectron);
        btnStartElectron.setOnClickListener(this);

        btnEditDatTxt = (Button) findViewById(R.id.btnEditDatTxt);
        btnEditDatTxt.setOnClickListener(this);


        txtLog = (TextView) findViewById(R.id.txtLog);



       // if (!myPremission())  return;
    }

    //===========================   проверка разрешений приложения  ================================
//    private boolean myPremission(){
//        if (hasPermissions()){
//            // our app has permissions.
//          //  Filealmat makefolder = new Filealmat();
//            filealmat.makeFolder(this);
//        }
//        else {
//            //our app doesn't have permissions, So i m requesting permissions.
//            requestPermissionWithRationale();
//        }
//        return true;
//    }
//    private void makeFolder(){
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fandroid");
//
//        if (!file.exists()){
//            Boolean ff = file.mkdir();
//            if (ff){
//                Toast.makeText(this, "Folder created successfully", Toast.LENGTH_LONG).show();
//            }
//            else {
//                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_LONG).show();
//            }
//
//        }
//        else {
//            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
//        }
//    }
//    private boolean hasPermissions(){
//        int res = 0;
//        //string array of permissions,
//        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//        for (String perms : permissions){
//            /*
//             * с помощью метода checkCallingOrSelfPermission в цикле проверяет
//             * предоставленные приложению разрешения и сравнивает их с тем, которое нам необходимо.
//             * При отсутствии разрешения метод будет возвращать false, а при наличии разрешения — true.
//             */
//            res = checkCallingOrSelfPermission(perms);
//            if (!(res == PackageManager.PERMISSION_GRANTED)){
//                return false;
//            }
//        }
//
//        return true;
//    }
//    private void requestPerms(){
//        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
//        }
//    }
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
//    public void requestPermissionWithRationale() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //final String message = "Storage permission is needed to show files count";
//            //Snackbar.make(this.findViewById(R.id.activity_scaner), message, Snackbar.LENGTH_LONG)
//            //        .setAction("GRANT", new View.OnClickListener() {
//            //   @Override
//            //    public void onClick(View v) {
//            requestPerms();
//            //    }
//            //     })
//            //     .show();
//
//        } else {
//            requestPerms();
//        }
//    }
    //========================  конец проверки разрешений  ==============================//

     public void onClick(View v) {
        Intent intent = new Intent(this, ScanerActivity.class);
         DBHelper dbHelper;// = new DBHelper(this);
         //SQLiteDatabase database;
         //= dbHelper.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
        intent.putExtra("VisibleTxtQuantity", View.VISIBLE);
        intent.putExtra("VisibleIntQuantity", View.VISIBLE);
        intent.putExtra("VisibleTxtPrice", View.VISIBLE);
        intent.putExtra("VisibleIntPrice", View.VISIBLE);
        intent.putExtra("VisibleTxtNumber", View.VISIBLE);
        intent.putExtra("VisibleIntNumber", View.VISIBLE);


        switch (v.getId()) {
            case R.id.btnQR:
                Intent intentQR = new Intent(this, QRcode.class);
                startActivity(intentQR);
                break;
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
            case R.id.btnLoadAll:
                loadSetting();
                if (!executeCommand(sAdressServer)) {
                    txtLog.setBackgroundColor(Color.RED);
                    break;
                }
                try {
                    if (LoadSaveCsvToDB(DIR_SD,"price.csv","select * from " + DBHelper.TABLE_DOCUMENT_PRICE,DBHelper.TABLE_DOCUMENT_PRICE)){
                        txtLog.setText("ДАННЫЕ ОБНОВЛЕНЫ");
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
                }

                //подключаемся к FTP серверу
                FTPModel mymodel = new FTPModel();
                // получает корневой каталог
                File sdPath = Environment.getExternalStorageDirectory();
                // добавляем свой каталог устройства к пути куда загружаем файл с сервера
                sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD + "/" + FILENAME_CSV);
                // загрузка csv файла с FTP сервера
                boolean ko = mymodel.downloadAndSaveFile(sAdressServer, Integer.parseInt(sPortFTP),
                        sUserFTP, sPasswordFTP, FILENAME_CSV, sdPath);
                if (ko) {
                    // Toast.makeText(context, "Файл данных загружен", Toast.LENGTH_LONG).show();
                    txtLog.setText("ДАННЫЕ ЗАГРУЖЕНЫ");
                    txtLog.setBackgroundColor(Color.GREEN);
                } else {
                    //Toast.makeText(context, "Файл данных не загружен", Toast.LENGTH_LONG).show();
                    txtLog.setText("ДАННЫЕ НЕ СОХРАНЕНЫ!");
                    txtLog.setBackgroundColor(Color.RED);
                    break;
                }
////////////////////////////   распаковка CSV    ////////////////////////////////////
                try {
// OPTION 1: if the file is in the sd

                    File csvfile = new File(Environment.getExternalStorageDirectory() + "/" +
                            DIR_SD + "/" + FILENAME_CSV);
// END OF OPTION 1
                    dbHelper = new DBHelper(this);
                    //SQLiteDatabase db = dbHelper.getReadableDatabase();
                    //Environment.getExternalStorageDirectory()
// OPTION 2: pack the file with the app
                    /* "If you want to package the .csv file with the application and have it install on the internal storage when the app installs, create an assets folder in your project src/main folder (e.g., c:\myapp\app\src\main\assets\), and put the .csv file in there, then reference it like this in your activity:" (from the cited answer) */
                    /* "Если вы хотите упаковать файл .csv вместе с приложением и установить его во внутреннее хранилище при установке приложения, создайте папку assets в вашей папке project src/main (например, c:\myapp\app\src\main\assets \), и поместите туда файл .csv, а затем ссылайтесь на него следующим образом в вашей деятельности:" (из процитированного ответа) */
                    // String csvfileStrin  = this.getApplicationInfo().dataDir + File.pathSeparatorChar  +
                    //FILENAME_CSV;
                    //    File csvfiles = csvfile;//new File(csvfileString);
// END OF OPTION 2
                    CSVReader reader = new CSVReader(
                            new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                            ';', '\'', 0);
                    //sStrok = reader.toString();
                    // считываем данные с БД
                    database = dbHelper.getWritableDatabase();
                    Cursor basecursor = database.rawQuery("select * from " + DBHelper.TABLE_DOCUMENT, null);
                    // определяем, какие столбцы из курсора будут выводиться в ListView
                    Integer iCountStrok = basecursor.getCount();//количество строк
                    iCountStrok = 0;

                    while ((nextLine = reader.readNext()) != null) {
                        iCountStrok++;

                    }


                    reader = new CSVReader(
                            new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                            ';', '\'', 0);
                    progressTextViewMain.setMaxValue(iCountStrok);
                    Integer iCountField = basecursor.getColumnCount();//количество полей
                    basecursor.moveToFirst();// установка курсора в начало
                    iCountStrok = 1;
                    while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
                        progressTextViewMain.setValue(iCountStrok);
                        iCountStrok++;
                        String sqlStroka = nextLine[0].toString(); //QR code
                        sqlStroka = nextLine[1].toString(); // № invoice
                        sqlStroka = nextLine[2].toString(); // invoice date
                        sqlStroka = nextLine[3].toString();//provider
                        sqlStroka = nextLine[4].toString();// # items in the invoice
                        sqlStroka = nextLine[5].toString();// barcode
                        sqlStroka = nextLine[6].toString();//name  product
                        sqlStroka = nextLine[7].toString();// price
                        sqlStroka = nextLine[8].toString();// status

                        sqlStroka = "select * from " + DBHelper.TABLE_DOCUMENT + " where " + DBHelper.KEY_BARCODE +
                                " = '" + nextLine[5].toString() + "' AND " + DBHelper.KEY_NUM_NAKL +
                                " = '" + nextLine[1].toString() + "' AND " +
                                DBHelper.KEY_DATE + " = '" + nextLine[2].toString() + "'";
                        basecursor = database.rawQuery(sqlStroka, null);
                        basecursor.moveToFirst();// установка курсора в начало
                        iCountField = basecursor.getCount();//количество полей
                        //  csvfileString = basecursor.getString(4);
//                        if(basecursor.isAfterLast()){
//                         }else{
//                        }
                        if (iCountField != 0) {

//                            sValueFieldNumNakldn = basecursor.getString(basecursor.getColumnIndex(DBHelper.KEY_QR_CODE));
//                            sValueFieldBarcode   = basecursor.getString(basecursor.getColumnIndex(DBHelper.KEY_BARCODE));
//                            sValueFieldDate      = basecursor.getString(basecursor.getColumnIndex(DBHelper.KEY_DATE));
                            contentValues.put(DBHelper.KEY_QR_CODE, nextLine[0]);
                            contentValues.put(DBHelper.KEY_NUM_NAKL, nextLine[1]);
                            contentValues.put(DBHelper.KEY_DATE, nextLine[2]);
                            contentValues.put(DBHelper.KEY_NAME_POST, nextLine[3]);
                            contentValues.put(DBHelper.KEY_NUM_POZ, nextLine[4]);
                            contentValues.put(DBHelper.KEY_BARCODE, nextLine[5]);
                            contentValues.put(DBHelper.KEY_NAME_TOV, nextLine[6]);
                            contentValues.put(DBHelper.KEY_QUANTITY, nextLine[7]);
                            contentValues.put(DBHelper.KEY_STATUS, nextLine[8]);
                            database.update(DBHelper.TABLE_DOCUMENT, contentValues, null, null);
                        } else {
                            contentValues.put(DBHelper.KEY_QR_CODE, nextLine[0]);
                            contentValues.put(DBHelper.KEY_NUM_NAKL, nextLine[1]);
                            contentValues.put(DBHelper.KEY_DATE, nextLine[2]);
                            contentValues.put(DBHelper.KEY_NAME_POST, nextLine[3]);
                            contentValues.put(DBHelper.KEY_NUM_POZ, nextLine[4]);
                            contentValues.put(DBHelper.KEY_BARCODE, nextLine[5]);
                            contentValues.put(DBHelper.KEY_NAME_TOV, nextLine[6]);
                            contentValues.put(DBHelper.KEY_QUANTITY, nextLine[7]);
                            contentValues.put(DBHelper.KEY_STATUS, nextLine[8]);
                            database.insert(DBHelper.TABLE_DOCUMENT, null, contentValues);
                        }

                    }
                    database.close();
                    txtLog.setText("ДАННЫЕ ОБНОВЛЕНЫ");
                    txtLog.setBackgroundColor(Color.GREEN);
                    break;


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public boolean LoadSaveCsvToDB(String DirName, String FileNameCSV, String SqlStroka, String TableName) throws IOException {
        boolean returnstatus = true;
        ContentValues ScontentValues = new ContentValues();
        //SQLiteDatabase db;
        //подключаемся к FTP серверу
        FTPModel mymodel = new FTPModel();
        // получает корневой каталог
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог устройства к пути куда загружаем файл с сервера
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DirName + "/" + FileNameCSV);
        // загрузка csv файла с FTP сервера
        boolean ko = mymodel.downloadAndSaveFile(sAdressServer, Integer.parseInt(sPortFTP),
                sUserFTP, sPasswordFTP, FILENAME_CSV, sdPath);
        if (ko) {

        } else {
            return false;// не загрузилось
        }
//////////////////////////////////
        File csvfile = new File(Environment.getExternalStorageDirectory() + "/" +
                DirName + "/" + FileNameCSV);
// END OF OPTION 1
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();//getReadableDatabase
        db.delete(TableName,null,null);
        db.close();
        db = dbHelper.getWritableDatabase();
        Cursor basecursor = db.rawQuery(SqlStroka, null);//"select * from " + DBHelper.TABLE_DOCUMENT
        Integer iCount = basecursor.getCount();
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                ';', '\'', 0);
        //sStrok = reader.toString();
        // считываем данные с БД
        //db = dbHelper.getWritableDatabase();
       // db = dbHelper.getWritableDatabase();

        while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
            ScontentValues = ScontentValues(nextLine);
//            ScontentValues.put(DBHelper.KEY_QR_CODE, nextLine[0]);
//            ScontentValues.put(DBHelper.KEY_NUM_NAKL, nextLine[1]);
//            ScontentValues.put(DBHelper.KEY_DATE, nextLine[2]);
//            ScontentValues.put(DBHelper.KEY_NAME_POST, nextLine[3]);
//            ScontentValues.put(DBHelper.KEY_NUM_POZ, nextLine[4]);
//            ScontentValues.put(DBHelper.KEY_BARCODE, nextLine[5]);
//            ScontentValues.put(DBHelper.KEY_NAME_TOV, nextLine[6]);
//            ScontentValues.put(DBHelper.KEY_QUANTITY, nextLine[7]);
//            ScontentValues.put(DBHelper.KEY_STATUS, nextLine[8]);
            db.insert(DBHelper.TABLE_DOCUMENT, null, ScontentValues);
        }
        db.close();

        return returnstatus;
    }
    public ContentValues ScontentValues(String[] csvreader){

        ContentValues ScontentValues = new ContentValues();
        ScontentValues.put(DBHelper.KEY_QR_CODE, nextLine[0]);
        ScontentValues.put(DBHelper.KEY_NUM_NAKL, nextLine[1]);
        ScontentValues.put(DBHelper.KEY_DATE, nextLine[2]);
        ScontentValues.put(DBHelper.KEY_NAME_POST, nextLine[3]);
        ScontentValues.put(DBHelper.KEY_NUM_POZ, nextLine[4]);
        ScontentValues.put(DBHelper.KEY_BARCODE, nextLine[5]);
        ScontentValues.put(DBHelper.KEY_NAME_TOV, nextLine[6]);
        ScontentValues.put(DBHelper.KEY_QUANTITY, nextLine[7]);
        ScontentValues.put(DBHelper.KEY_STATUS, nextLine[8]);
        return ScontentValues;
    }

    public void loadSetting() {

        SharedPreferences sPref;

        sPref = getSharedPreferences("setting", MODE_PRIVATE);

        sAdressServer = sPref.getString(ADRESS_SERVER, "");
        sUserFTP = sPref.getString(USER_NAME, "");
        sPasswordFTP = sPref.getString(USER_PASSWORD, "");
        sPortFTP = sPref.getString(PORT_FTP, "");
        sPathFile = sPref.getString(PATH_FILE, "");
        sModeWorking = sPref.getString(MODE_WORKING, "");
    }
    ///////////////////////////////
    private boolean executeCommand(String ip){
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 "+ ip);
            int mExitValue = mIpAddrProcess.waitFor();
            txtLog.setText(" mExitValue " + mExitValue);
            if(mExitValue==0){
                txtLog.setText("ЕСТЬ СВЯЗЬ");
                return true;
            }else{
                txtLog.setText("НЕТ СВЯЗИ");
                return false;
            }
        }
        catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
            txtLog.setText(" Ошибка:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
            txtLog.setText(" Ошибка:" + e);
        } return false;
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
