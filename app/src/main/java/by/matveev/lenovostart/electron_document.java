package by.matveev.lenovostart;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;
import by.matveev.lenovostart.lib.FTPModel;

public class electron_document extends AppCompatActivity  implements View.OnClickListener {

    public enum Fields {
        //        ID(0),
//        ANIMALNAME(1);
//    public static  final String KEY_ID = "_id";
        KEY_NUM_NAKL(0), // = "numnakl";
        KEY_DATE(1),     // = "date";
        KEY_NAME_POST(2),// = "namepost";
        KEY_NUM_POZ(3),  // = "numpoz";
        KEY_BARCODE(4),  // = "barcode";
        KEY_NAME_TOV(5), // = "nametov";
        //    KEY_PRICE(0), // = "price";
        KEY_QUANTITY(6), // = "quantity";
        KEY_STATUS(7);   // = "status";
        Fields(int i) {
            this.fieldCode = i;
        }
        public int getFieldCode()
        {
            return fieldCode;
        }
        private int fieldCode;
    }

    private static final int PERMISSION_REQUEST_CODE = 123;
    final String USER_NAME = "user_name";
    final String USER_PASSWORD = "user_passowrd";
    final String ADRESS_SERVER = "adress_server";
    final String PATH_FILE = "path_file";
    final String PORT_FTP = "21";
    final String MODE_WORKING = "1";

    final String LOG_TAG = "myLogs";
    final String DIR_SD = "Documents";
    final String FILENAME_SD = "Dat1.txt";

    String sAdressServer;
    String sUserFTP;
    String sPasswordFTP;
    String sPortFTP;
    String sPathFile;
    String sModeWorking;


    String barcodeFind;
    String barcodeCursor;

    String numNak;
    String dataNakl;
    String numPoz;
    String statNakl;
    String postNakl;


    SQLiteDatabase db;
    Context cont;

    Cursor cursor;
    Integer iPositionCursor;
    Button btnExit;
    Button btnBackward;
    Button btnForward;
    Button btnFind;

    Button btnClearElectron;
    Button btnAddElectron;
    Button btnSaveToServerElectron;

    TextView txtNumNakladn;
    EditText txtDataNakladn;
    EditText txtPostNakladn;
    EditText txtPozNakladn;
    TextView txtBarcodeNakladn;
    TextView txtTovNakladn;
    EditText txtQuanNakladn;
    EditText txtStatusNakladn;
    EditText txtBarcodeFind;
    TextView txtvQuanNakladn;
    LinearLayout lnlyLinear;
    DBHelper dbHelper;

    public static  final String KEY_NUM_NAKL = "numnakl";
    public static  final String KEY_DATE = "date";
    public static  final String KEY_NAME_POST = "namepost";
    public static  final String KEY_NUM_POZ = "numpoz";
    public static  final String KEY_BARCODE = "barcode";
    public static  final String KEY_NAME_TOV = "nametov";
    //    public static  final String KEY_PRICE = "price";
    public static  final String KEY_QUANTITY = "quantity";
    public static  final String KEY_STATUS = "status";

    public static  final String TABLE_DOCUMENT = "Document";


    Fields numNakld = Fields.KEY_NUM_NAKL;
    Fields datNakld = Fields.KEY_DATE;
    Fields postavNakld = Fields.KEY_NAME_POST;
    Fields numPozNakld = Fields.KEY_NUM_POZ;
    Fields barcodeNakld = Fields.KEY_BARCODE;
    Fields nameTovNakld = Fields.KEY_NAME_TOV;
    Fields quntityNakld = Fields.KEY_QUANTITY;
    Fields statusNakld = Fields.KEY_STATUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electron_document);


        dbHelper = new DBHelper(this);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] countries = null;
        // dbHelper helper = new dbHelper(this);
        dbHelper.createDataBase();
        try {
            dbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        db = new DBHelper(this).getWritableDatabase();


        String[] columns = null;

        txtvQuanNakladn = (TextView) findViewById(R.id.txtvQuanNakladn);
        btnBackward = (Button) findViewById(R.id.btnBack);
        btnBackward.setOnClickListener(this);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
        btnForward = (Button) findViewById(R.id.btnForward);
        btnForward.setOnClickListener(this);

        btnFind = (Button) findViewById(R.id.btnFind);
        btnFind.setOnClickListener(this);

        btnClearElectron = (Button) findViewById(R.id.btnClearElectron);
        btnClearElectron.setOnClickListener(this);
        btnAddElectron = (Button) findViewById(R.id.btnAddElectron);
        btnAddElectron.setOnClickListener(this);
        btnSaveToServerElectron = (Button) findViewById(R.id.btnSaveToServerElectron);
        btnSaveToServerElectron.setOnClickListener(this);

        lnlyLinear = (LinearLayout) findViewById(R.id.lnlyLinear);

        txtNumNakladn = (TextView) findViewById(R.id.txtNumNakladn);
        txtNumNakladn.setOnClickListener(this);
      //  txtDataNakladn = (EditText) findViewById(R.id.txtDataNakladn);
   //     txtDataNakladn.setOnClickListener(this);
  //      txtPostNakladn = (EditText) findViewById(R.id.txtPostNakladn);
  //      txtPostNakladn.setOnClickListener(this);
  //      txtPozNakladn = (EditText) findViewById(R.id.txtPozNakladn);
  //      txtPozNakladn.setOnClickListener(this);
        txtBarcodeNakladn = (TextView) findViewById(R.id.txtBarcodeNakladn);
        txtBarcodeNakladn.setOnClickListener(this);
        txtTovNakladn = (TextView) findViewById(R.id.txtTovNakladn);
        txtTovNakladn.setOnClickListener(this);
        txtQuanNakladn = (EditText) findViewById(R.id.txtQuanNakladn);
        txtQuanNakladn.setOnClickListener(this);
   //     txtStatusNakladn = (EditText) findViewById(R.id.txtStatusNakladn);
  //      txtStatusNakladn.setOnClickListener(this);

        txtBarcodeFind = (EditText) findViewById(R.id.txtBarcodeFind);
        txtBarcodeFind.setOnClickListener(this);

        Intent intent = getIntent();
        int VisibletxtNumNakladn = intent.getIntExtra("VisibletxtNumNakladn",  View.VISIBLE);
        txtNumNakladn.setVisibility(VisibletxtNumNakladn);
        String ddd;
        ddd = getIntent().getStringExtra("VisibletxtNumNakladn");
        txtNumNakladn.setText(ddd);

        numNak = txtNumNakladn.getText().toString();

        columns = new String[] {KEY_NUM_NAKL + ", " + KEY_DATE + ", " + KEY_NAME_POST + ", " + KEY_NUM_POZ + ", " + KEY_BARCODE + ", " + KEY_NAME_TOV + ", " + KEY_QUANTITY + ", " + KEY_STATUS};
        cursor = db.query("Document",columns, KEY_NUM_NAKL + " = " + numNak,
                null, null, null, KEY_NUM_POZ);
        cursor.moveToFirst();

        btnBackward.setEnabled(true);
        btnForward.setEnabled(true);
        if (txtQuanNakladn.getText().toString().length() > 0) {
            switch (cursor.getString(statusNakld.getFieldCode())) {
                case "0": {
                    txtTovNakladn.setBackgroundColor(Color.GREEN);
                    txtTovNakladn.refreshDrawableState();
                    break;
                }
                case "1": {
                    txtTovNakladn.setBackgroundColor(Color.RED);
                    txtTovNakladn.refreshDrawableState();
                    break;
                }
            }
        }
        txtQuanNakladn.setBackgroundColor(Color.WHITE);

        readFileSD();
    }
    void FindTovarToBarcode(String BarCodeFind){
        if (BarCodeFind.length() == 0) {
            ToastMessageCenter("Отсутствует штрихкод!");
            return;
        }
        cursor.moveToFirst();
        while(!txtBarcodeNakladn.getText().toString().equals(BarCodeFind)){
            if(txtBarcodeNakladn.getText().toString().equals(BarCodeFind))
                break;
            if(!cursor.isAfterLast()) {
                cursor.moveToNext();
                txtBarcodeNakladn.setText(cursor.getString(barcodeNakld.getFieldCode()));
                txtTovNakladn.setText(cursor.getString(nameTovNakld.getFieldCode()));
                txtQuanNakladn.setText(cursor.getString(quntityNakld.getFieldCode()));
                barcodeCursor = txtBarcodeNakladn.getText().toString();
                statNakl = cursor.getString(statusNakld.getFieldCode());
                iPositionCursor = cursor.getPosition();
            }else
                cursor.close();
        }
        if (iPositionCursor > 0)
            btnBackward.setEnabled(true);
        if (iPositionCursor == 0)
            btnBackward.setEnabled(false);
        if (iPositionCursor != (cursor.getCount() - 1))
            btnForward.setEnabled(true);
        if (!cursor.isAfterLast())
            btnForward.setEnabled(false);
        barcodeCursor = txtBarcodeNakladn.getText().toString();
     //   statNakl = cursor.getString(statusNakld.getFieldCode());
        //txtQuanNakladn.setFocusable(true);
        txtQuanNakladn.setSelectAllOnFocus(true);
        txtQuanNakladn.requestFocus();
        switch (cursor.getString(statusNakld.getFieldCode())){//(statNakl){
            case "0":
                txtTovNakladn.setBackgroundColor(Color.GREEN);
                txtTovNakladn.refreshDrawableState();
                break;
            case "1":
                txtTovNakladn.setBackgroundColor(Color.RED);
                txtTovNakladn.refreshDrawableState();
                break;
        }

    }
    @Override
    public void onClick(View v) {
        txtQuanNakladn.setBackgroundColor(Color.WHITE);
        switch (v.getId()) {
            case R.id.lnlyLinear:

                break;
            case R.id.btnSaveToServerElectron:
                if(!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    //txtLogScaner.setText("SD-карта не доступна: " + Environment.getExternalStorageState());
                    ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
                    return;
                }

                loadSetting();
                if (executeCommand(sAdressServer)){
                    try{
                        FTPModel mymodel = new FTPModel();

                        boolean co = mymodel.connect(sAdressServer,sUserFTP,sPasswordFTP,Integer.parseInt(sPortFTP));
                        if(co){
                            ToastMessageCenter("ДАННЫЕ СОХРАНЕНЫ ");
                            //txtLogScaner.setText("ДАННЫЕ СОХРАНЕНЫ");
                        }else{
                            ToastMessageCenter("ДАННЫЕ НЕ СОХРАНЕНЫ! ");
                            //txtLogScaner.setText("ДАННЫЕ НЕ СОХРАНЕНЫ!");
                        }
                        // saveUrl(Environment.getExternalStorageDirectory() + "/Documents/Dat1.txt", "10.250.1.15/asd");
                    }
                    catch(Exception e){
                        ToastMessageCenter("НЕТ СВЯЗИ С СЕРВЕРОМ" + e);
                        //txtLogScaner.setText("НЕТ СВЯЗИ С СЕРВЕРОМ");
                    }
                }

                break;
            case R.id.txtQuanNakladn:
                if(!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                //txtLogScaner.setText("SD-карта не доступна: " + Environment.getExternalStorageState());
                 ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
                return;
            }
                writeFileSD();
                break;
            case R.id.btnFind:
                FindTovarToBarcode(txtBarcodeFind.getText().toString());
                break;
            case R.id.txtBarcodeFind:
                FindTovarToBarcode(txtBarcodeFind.getText().toString());
                break;
            case R.id.btnBack:
                if (cursor.getPosition() > 0)
                    cursor.moveToPrevious();
                txtBarcodeNakladn.setText(cursor.getString(barcodeNakld.getFieldCode()));
                txtTovNakladn.setText(cursor.getString(nameTovNakld.getFieldCode()));
                txtQuanNakladn.setText(cursor.getString(quntityNakld.getFieldCode()));
                iPositionCursor = cursor.getPosition();
                if (cursor.getPosition() > 0)
                    btnBackward.setEnabled(true);
                if (cursor.getPosition() == 0)
                    btnBackward.setEnabled(false);
                if (cursor.getPosition() != (cursor.getCount() - 1))
                    btnForward.setEnabled(true);
                switch (cursor.getString(statusNakld.getFieldCode())) {
                    case "0":
                        txtTovNakladn.setBackgroundColor(Color.GREEN);
                        txtTovNakladn.refreshDrawableState();
                        break;
                    case "1":
                        txtTovNakladn.setBackgroundColor(Color.RED);
                        txtTovNakladn.refreshDrawableState();
                        break;
                }
                break;
            case R.id.btnForward:

                if (cursor.getPosition() == (cursor.getCount() - 1)) {
                    btnForward.setEnabled(false);
                    break;
                }
                cursor.moveToNext();
                txtBarcodeNakladn.setText(cursor.getString(barcodeNakld.getFieldCode()));
                txtTovNakladn.setText(cursor.getString(nameTovNakld.getFieldCode()));
                txtQuanNakladn.setText(cursor.getString(quntityNakld.getFieldCode()));
                iPositionCursor = cursor.getPosition();
                if (cursor.getPosition() > 0)
                    btnBackward.setEnabled(true);
                if (cursor.getPosition() == 0)
                    btnBackward.setEnabled(false);
                if (cursor.getPosition() == (cursor.getCount() - 1))
                    btnForward.setEnabled(false);
                switch (cursor.getString(statusNakld.getFieldCode())) {
                    case "0":{
                        txtTovNakladn.setBackgroundColor(Color.GREEN);
                        txtTovNakladn.refreshDrawableState();
                        break;
                    }
                    case "1":{
                        txtTovNakladn.setBackgroundColor(Color.RED);
                        txtTovNakladn.refreshDrawableState();
                        break;
                    }
                }
                break;
            case R.id.btnExit:
                finish();
                System.exit(0);
                break;
        }
    }
    ///////////////////////////////
    private boolean executeCommand(String ip){
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 "+ ip);
            int mExitValue = mIpAddrProcess.waitFor();
            ToastMessageCenter(" mExitValue " + mExitValue);
            //txtLogScaner.setText(" mExitValue " + mExitValue);
            if(mExitValue==0){
                ToastMessageCenter("ЕСТЬ СВЯЗЬ " + mExitValue);
               // txtLogScaner.setText("ЕСТЬ СВЯЗЬ");
                return true;
            }else{
                ToastMessageCenter("НЕТ СВЯЗИ " + mExitValue);
               // txtLogScaner.setText("НЕТ СВЯЗИ");
                return false;
            }
        }
        catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
            ToastMessageCenter(" Ошибка:" + ignore);
            //txtLogScaner.setText(" Ошибка:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
            ToastMessageCenter("Ошибка:"  + e);
        //    txtLogScaner.setText(" Ошибка:" + e);
        } return false;
    }
//////////////

    void writeFileSD() {// запись на SD диск  // подготавливаем переменные
        if(txtBarcodeFind.getText().toString().length() == 0) {
            return;
        }
        String txtBarcode = txtBarcodeFind.getText().toString();

        String txtNumber = "";
        String txtQuantity = "";
        String txtPrice = "";
        String textAdd = "";
        String line = "";
        Integer NumberOfRecords = 0;
        loadSetting();
        if (cursor.getString(numPozNakld.getFieldCode()).length() > 0)// && txtnNumber.getVisibility() == View.VISIBLE)
            txtNumber = cursor.getString(numPozNakld.getFieldCode()); //txtnNumber.getText().toString();
        if (txtQuanNakladn.length() > 0 && txtQuanNakladn.getVisibility() == View.VISIBLE)
            txtQuantity = txtQuanNakladn.getText().toString();
//        if (cursor.getString(pric.getFieldCode()).length() > 0)//txtPrice.length() > 0 && txtPrice.getVisibility() == View.VISIBLE)
        txtPrice = "";//txtdPrice.getText().toString();
        String text =  txtBarcode + ";" + txtPrice + ";" + txtQuantity + ";" + txtNumber + ";";
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();

        File[] elems = sdPath.listFiles();

        String[] paths = new String[1 + (elems == null? 0 : elems.length)];
        int i = 0;
        paths[i] = sdPath.getAbsolutePath();//добавляем в список повторно сканируемых путей саму папку - что бы она отобразилась если была создана после подключения к компьютеру
        i++;
        if (elems != null) {
            for (File elem : elems) {
                paths[i] = elem.getAbsolutePath();//добавляем в список повторно сканируемых путей содержимое папки (у меня не было вложенных папок)
                i++;
            }
        }
        MediaScannerConnection.scanFile(electron_document.this, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        //File sdFile_copy = new File(sdPath, FILENAME_SD_copy);

        // Проверка наличия файла
        if (sdFile.exists()){
            //ToastMessageCenter("Файл в наличии.");
            // проверка разрешений
            if (!myPremission())  return;
            String lineSeparator = System.getProperty("line.separator");
            try {
                // открываем поток для чтения
                BufferedReader br = new BufferedReader(new FileReader(sdFile));
                // пишем данные
                //ToastMessageCenter("Файл открыт для чтения.");

                StringBuilder builder = new StringBuilder();
                NumberOfRecords = 0;
                while ((line = br.readLine()) != null) {
                    builder.append(line + "\r\n");
                    ++NumberOfRecords;
                }
                textAdd = builder.toString();
                // закрываем поток
                br.close();
                Log.d(LOG_TAG, "Файл  на SD: " + sdFile.getAbsolutePath());
                btnAddElectron.setText("Добавить позицию (" + NumberOfRecords + ")");
                //        btnUploadDelete.setEnabled(true);
                if (!sModeWorking.equals("1")) {
                    btnSaveToServerElectron.setEnabled(false);
                }else{
                    btnSaveToServerElectron.setEnabled(true);
                }
                btnClearElectron.setEnabled(true);

                //ToastMessageCenter( "Чтение");
                //if(sdFile.exists())
                //sdFile.renameTo(sdFile); // переименовать файл
                //copyFileUsingStream(sdFile, sdFile_copy);// копировать файл

            } catch (IOException e) {
                e.printStackTrace();
                ToastMessageCenter("Ошибка: Файл не открывается для чтения.");
                return;
            }
        }//else{ ToastMessageCenter("Файл отсутствует."); }
        try {
            // открываем поток для записи если файла нет
            //ToastMessageCenter("Запись");
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            textAdd = textAdd + text ;
            //bw.write(textAdd);

            bw.append(textAdd);
            // закрываем поток
            bw.close();
            ++NumberOfRecords;
            //if(sdFile.exists())
            //sdFile.renameTo(sdFile); // переименовать файл
            //copyFileUsingStream(sdFile, sdFile);// копировать файл
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            //ToastMessageCenter("Данные сохранены на SD.+");
            btnAddElectron.setText("Добавить позицию (" + NumberOfRecords + ")");
            //     btnUploadDelete.setEnabled(true);
            if (!sModeWorking.equals("1")) {
                //1
                btnSaveToServerElectron.setEnabled(false);
            }else{
                btnSaveToServerElectron.setEnabled(true);
            }
            btnClearElectron.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
            ToastMessageCenter("Ошибка: Файл невозможно открыть.");
            return;
        }
    }
    public void ToastMessageCenter(String s){
        Toast toast = Toast.makeText(this, s , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER , 0, 0);
        toast.show();
    }

    public void loadSetting(){

        SharedPreferences sPref;

        sPref = getSharedPreferences("setting", MODE_PRIVATE);

        sAdressServer = sPref.getString(ADRESS_SERVER, "");
        sUserFTP = sPref.getString(USER_NAME, "");
        sPasswordFTP = sPref.getString(USER_PASSWORD, "");
        sPortFTP = sPref.getString(PORT_FTP, "");
        sPathFile = sPref.getString(PATH_FILE, "");
        sModeWorking = sPref.getString(MODE_WORKING, "");
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
            makeFolder();
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
            makeFolder();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //final String message = "Storage permission is needed to show files count";
            //Snackbar.make(this.findViewById(R.id.activity_scaner), message, Snackbar.LENGTH_LONG)
            //        .setAction("GRANT", new View.OnClickListener() {
            //   @Override
            //    public void onClick(View v) {
            requestPerms();
            //    }
            //     })
            //     .show();

        } else {
            requestPerms();
        }
    }
    //========================  конец проверки разрешений  ==============================//

    //===========================   проверка разрешений приложения  ================================
    private boolean myPremission(){
        if (hasPermissions()){
            // our app has permissions.
            makeFolder();
        }
        else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale();
        }
        return true;
    }
    private void makeFolder(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fandroid");

        if (!file.exists()){
            Boolean ff = file.mkdir();
            if (ff){
                Toast.makeText(this, "Folder created successfully", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_LONG).show();
            }

        }
        else {
            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
        }
    }
    private boolean hasPermissions(){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permissions){
            /*
             * с помощью метода checkCallingOrSelfPermission в цикле проверяет
             * предоставленные приложению разрешения и сравнивает их с тем, которое нам необходимо.
             * При отсутствии разрешения метод будет возвращать false, а при наличии разрешения — true.
             */
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }

        return true;
    }
    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }
    private int readFileSD() {

        String line = "";
        Integer NumberOfRecords;
        loadSetting();
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
            return 0;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        //sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        //File sdFile_copy = new File(sdPath, FILENAME_SD_copy);

        // Проверка наличия файла
        if (sdFile.exists()) {
            //ToastMessageCenter("Файл в наличии.");
            // проверка разрешений
            if (!myPremission())
                return 0;
            try {
                // открываем поток для чтения
                BufferedReader br = new BufferedReader(new FileReader(sdFile));
                // пишем данные
                //ToastMessageCenter("Файл открыт для чтения.");

                StringBuilder builder = new StringBuilder();
                NumberOfRecords = 0;
                while ((line = br.readLine()) != null) {
                    builder.append(line + "\n");
                    ++NumberOfRecords;
                }
                // закрываем поток
                br.close();
                Log.d(LOG_TAG, "Файл  на SD: " + sdFile.getAbsolutePath());
                btnAddElectron.setText("Добавить позицию (" + NumberOfRecords + ")");
                //if(sdFile.exists())
                // sdFile.renameTo(sdFile_copy); // переименовать файл
                //     copyFileUsingStream(sdFile, sdFile_copy);// копировать файл

                return NumberOfRecords;
            } catch (IOException e) {
                e.printStackTrace();

                if (!myPremission())
                    ToastMessageCenter("Ошибка: Файл не открывается для чтения.");
                else
                    return 0;//

                return 0;
            }
        }else{
            //     btnUploadDelete.setEnabled(false);
            btnSaveToServerElectron.setEnabled(false);
            btnClearElectron.setEnabled(false);
            if (!myPremission())
                return 0;
        }
        return 0;
    }

}




