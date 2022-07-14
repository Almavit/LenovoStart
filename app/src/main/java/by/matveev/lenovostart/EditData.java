package by.matveev.lenovostart;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;

public class EditData extends AppCompatActivity implements View.OnClickListener {


 //   InputStream inputStream;

    private static final int PERMISSION_REQUEST_CODE = 123;
    private SQLiteDatabase db;

    Button btnEditDat;
    Button btnSaveDat;
    Button btnDeleDat;
    Button btnDonloadDat;

    TextView txtStroka;

    DBHelper dbHelper;

    String[] nextLine;
    Integer iCountField;
    Integer iCountStrok;

    String datFileString;
    Cursor datBaseCursor;

    ListView dbListView;


    String sAdressServer;
    String sUserFTP;
    String sPasswordFTP;
    String sPortFTP;
    String sPathFile;
    String sModeWorking;

    final String USER_NAME = "user_name";
    final String USER_PASSWORD = "user_passowrd";
    final String ADRESS_SERVER = "adress_server";
    final String PATH_FILE = "path_file";
    final String PORT_FTP = "21";
    final String MODE_WORKING = "1";

//    TextView txtTov;


//    InputStream inputStream;
//    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//    String csvLine;

    SQLiteDatabase database;

    final String FILENAME_DAT_TXT = "Dat1.txt";
    final String DIR_SD = "Documents";
    public static  final String DAT_TABLE_DOCUMENT = "Dat";
    private static final String ENCODING_WIN1251 = "windows-1251";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_data);

     //   txtTov  = (TextView) findViewById(R.id.txtTov);


        txtStroka = (TextView) findViewById(R.id.txtStroka);

        btnEditDat = (Button) findViewById(R.id.btnEditDat);
        btnEditDat.setOnClickListener(this);

        btnSaveDat = (Button) findViewById(R.id.btnSaveDat);
        btnSaveDat.setOnClickListener(this);

        btnDeleDat = (Button) findViewById(R.id.btnDeleDat);
        btnDeleDat.setOnClickListener(this);

        btnDonloadDat = (Button) findViewById(R.id.btnDonloadDat);
        btnDonloadDat.setOnClickListener(this);


   //     dbGridEditDat = (GridView) findViewById(R.id.dbGridEditDat);

        dbListView = (ListView) findViewById(R.id.dbListView);


       // Intent intent = new Intent(this, electron_document.class);



        AdapterView.OnItemClickListener datitemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String fgsdfsdfs = parent.getItemAtPosition(position).toString();
                txtStroka.setText(fgsdfsdfs);
            }
        };

        dbListView.setOnItemClickListener(datitemListener);

        //LoaddbListView();

    }
///////////////////////////
//01048100620052972123duZSYF+93E>Xy
//0104810319017400212nuzH1NG+93p<x>
    @Override
    public void onClick(View v) {
        final DBRepository repositorys = new DBRepository(getApplicationContext());
        ArrayAdapter<String> datadapterdat;
        ArrayAdapter<String> datadapter;
        switch (v.getId()) {
            case R.id.btnDonloadDat:
                LoaddbListView();
                datadapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, repositorys.getDataDat());
                datadapter.setDropDownViewResource(R.layout.simple_list_item_dat);
                dbListView.setAdapter(datadapter);
                break;
            case R.id.btnSaveDat:
                datadapterdat = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, repositorys.getDataDat());
                datadapterdat.setDropDownViewResource(R.layout.simple_list_item_dat);
                dbListView.setAdapter(datadapterdat);
                //writeFileSD(DIR_SD,);
                break;
            case R.id.btnDeleDat:
                List resultList = new ArrayList();
                String csvLine = null;
                String sssssss = txtStroka.getText().toString();
                //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
           //
                if (!((csvLine = sssssss) != null))
                    break;
                   // sssssss = csvLine.split(";");
                String[] row = csvLine.split(";");
                resultList.add(row);
                sssssss = row[0].toString().replaceAll("\\s","");

                dbHelper = new DBHelper(this);

                try {
                        dbHelper.createDataBase();
                        try {
                            dbHelper.openDataBase();
                        } catch (SQLException sqle) {
                            throw sqle;
                        }
                } catch (Exception e) {
                        database.close();
                        e.printStackTrace();
                        Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
                        // txtLogMessege.setText("");
                }
                database = dbHelper.getWritableDatabase();
                int iDataStatus = database.delete(dbHelper.TABLE_DOCUMENT_DAT," datid="+sssssss,null);
                datBaseCursor = database.query(dbHelper.TABLE_DOCUMENT_DAT, null, null, null, null, null, null);
                iCountField = datBaseCursor.getCount();//количество полей

                database.close();

                txtStroka.setText("");

                // обновляем таблицу
                datadapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, repositorys.getDataDat());
                datadapter.setDropDownViewResource(R.layout.simple_list_item_dat);
                dbListView.setAdapter(datadapter);
                // обновляем таблицу end

                // Запись в файл Dat1.txt
                try {
                    writeFileSD(DIR_SD, FILENAME_DAT_TXT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Запись в файл Dat1.txt end

                break;
        }
        //
    }
    //
    public void LoaddbListView(){
        final String LOG_TAG = "LoaddbListView";
        ContentValues datContentValues = new ContentValues();
        dbHelper = new DBHelper(this);

//        if (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
//            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
//            return;
//        }

        try {

            File csvfile = new File(Environment.getExternalStorageDirectory()+ "/" + DIR_SD + "/" + FILENAME_DAT_TXT);

            datFileString = this.getApplicationInfo().dataDir + File.pathSeparatorChar  + FILENAME_DAT_TXT;
            File csvfiles = new File(datFileString);
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),';', '\n', 0);
            String sStrok = reader.toString();
            iCountStrok = 0;
            while ((nextLine = reader.readNext()) != null) {
                iCountStrok++;
            }

            reader = new CSVReader(
                    new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                    ';', '\n', 0);


            dbHelper.createDataBase();
            try {
                dbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            /* "If you want to package the .csv file with the application and have it install on the internal storage when the app installs, create an assets folder in your project src/main folder (e.g., c:\myapp\app\src\main\assets\), and put the .csv file in there, then reference it like this in your activity:" (from the cited answer) */
            /* "Если вы хотите упаковать файл .csv вместе с приложением и установить его во внутреннее хранилище при установке приложения, создайте папку assets в вашей папке project src/main (например, c:\myapp\app\src\main\assets \), и поместите туда файл .csv, а затем ссылайтесь на него следующим образом в вашей деятельности:" (из процитированного ответа) */

            // считываем данные с БД
            database = dbHelper.getWritableDatabase();
            datBaseCursor = database.rawQuery("select * from " + dbHelper.TABLE_DOCUMENT_DAT,null);
            // определяем, какие столбцы из курсора будут выводиться в ListView
            iCountStrok = datBaseCursor.getCount(); //количество строк
            //progressTextView.setMaxValue(iCountStrok);
            iCountField = datBaseCursor.getColumnCount();//количество полей
            datBaseCursor.moveToFirst();// установка курсора в начало
            iCountStrok = 0;
            //чистим БД сканированного
            datFileString = "delete  from " + DBHelper.TABLE_DOCUMENT_DAT ;
            int iDelete = database.delete("Dat",null,null);

            datBaseCursor = database.query(dbHelper.TABLE_DOCUMENT_DAT, null, null, null, null, null, null);
            iCountField = datBaseCursor.getCount();//количество полей
            datBaseCursor.moveToFirst();// установка курсора в начало
            while ((nextLine = reader.readNext()) != null) {// считываем данные с Dat1.txt  файла
                iCountStrok++;
                datFileString = nextLine[0].toString();


                datContentValues.put(DBHelper.DAT_KEY_ID, Integer.toString(iCountStrok));
                datContentValues.put(DBHelper.DAT_KEY_BARCODE, nextLine[0]);
                datContentValues.put(DBHelper.DAT_KEY_POSITION, nextLine[1]);
                datContentValues.put(DBHelper.DAT_KEY_QUANTITY, nextLine[2]);
                datContentValues.put(DBHelper.DAT_KEY_PRICE, nextLine[3]);
                database.insert(DBHelper.TABLE_DOCUMENT_DAT, null, datContentValues);

            }//end while
            database.close();

        } catch (Exception e) {
            if(null != database) {
                database.close();
            }
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
            // txtLogMessege.setText("");
        }

    }
    public void ToastMessageCenter(String s){
        Toast toast = Toast.makeText(this, s , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER , 0, 0);
        toast.show();
    }
    void writeFileSD(String PatshDIR_SD, String FileName) throws IOException {// запись на SD диск  // подготавливаем переменные
////
        final String LOG_TAG = "PatshDIR_SD";
//        String txtBarcode = txtnBarcode.getText().toString();
//        String txtNumber = "";
//        String txtQuantity = "";
//        String txtPrice = "";
        String textAdd = "";
        String line = "";
        Integer NumberOfRecords = 0;


//==================================================================================================
        Integer id;
        String barcode;
        String number;
        String quantity;
        String price;
        Integer iFields;
        StringBuilder sbText  = new StringBuilder();
        String text = "";
        String[] datcolumnsName = null;

       // datcolumnsName = new String[]{DAT_KEY_ID,DAT_KEY_BARCODE, DAT_KEY_NUMBER, DAT_KEY_QUANTITY, DAT_KEY_PRICE};//,
        DBRepository.DatFields fieldid = DBRepository.DatFields.DAT_KEY_ID;
        DBRepository.DatFields fieldbarcose = DBRepository.DatFields.DAT_KEY_BARCODE;
        DBRepository.DatFields fieldnumber = DBRepository.DatFields.DAT_KEY_NUMBER;
        DBRepository.DatFields fieldquantity = DBRepository.DatFields.DAT_KEY_QUANTITY;
        DBRepository.DatFields fieldprice = DBRepository.DatFields.DAT_KEY_PRICE;

        ArrayList<String> list = new ArrayList<String>();
        db = SQLiteDatabase.openOrCreateDatabase(DBHelper.DATABASE_NAME, null, null);

        Cursor cursor = db.query(DAT_TABLE_DOCUMENT,null , null,null, null, null, null);
        int iF = cursor.getCount();
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            Integer iCountFields = cursor.getColumnCount();
            do {
                iCountFields = cursor.getPosition();
                text = //cursor.getString(fieldid.getFieldCode()) + "   ;   " +
                        cursor.getString(fieldbarcose.getFieldCode()) + ";" +
                        cursor.getString(fieldnumber.getFieldCode()) + ";" +
                        cursor.getString(fieldquantity.getFieldCode()) + ";" +
                        cursor.getString(fieldprice.getFieldCode()) + "\r\n";
                sbText.append(text);
            } while (cursor.moveToNext());
        }
//====================================================================================================


        loadSetting();

//        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
//        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
//        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + PatshDIR_SD);
//        // создаем каталог
        sdPath.mkdirs();
//
        File[] elems = sdPath.listFiles();
//
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
        MediaScannerConnection.scanFile(this, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
//        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FileName);
//        //File sdFile_copy = new File(sdPath, FILENAME_SD_copy);
//
//        // Проверка наличия файла
//?        if (sdFile.exists()){
//            //ToastMessageCenter("Файл в наличии.");
//            // проверка разрешений
           if (!myPremission())  return;
//?            String lineSeparator = System.getProperty("line.separator");
//?            try {
//                // открываем поток для чтения
//?                BufferedReader br = new BufferedReader(new FileReader(sdFile));
//                // пишем данные
//                //ToastMessageCenter("Файл открыт для чтения.");
//
//?                StringBuilder builder = new StringBuilder();
// ?               NumberOfRecords = 0;
//?                while ((line = br.readLine()) != null) {
//?                    builder.append(line + "\r\n");
//?                    ++NumberOfRecords;
// ?               }
              ///  textAdd = builder.toString();
//                // закрываем поток
//                br.close();
// ?               Log.d(LOG_TAG, "Файл  на SD: " + sdFile.getAbsolutePath());
// ?           } catch (IOException e) {
// ?               e.printStackTrace();
// /               ToastMessageCenter("Ошибка: Файл не открывается для чтения.");
// ?               return;
// ?           }
//?       }//else{ ToastMessageCenter("Файл отсутствует."); }
        try {
            // открываем поток для записи если файла нет
            //ToastMessageCenter("Запись");
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            textAdd = sbText.toString();//list.toString();//text;//Add + text ;
//            //bw.write(textAdd);
//
            bw.append(textAdd);
            // закрываем поток
            bw.close();
            ++NumberOfRecords;
            //if(sdFile.exists())
            //sdFile.renameTo(sdFile); // переименовать файл
            //copyFileUsingStream(sdFile, sdFile);// копировать файл
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            //ToastMessageCenter("Данные сохранены на SD.+");
          //  btnAddPosition.setText("Добавить позицию (" + NumberOfRecords + ")");
            //     btnUploadDelete.setEnabled(true);
//            if (!sModeWorking.equals("1")) {
//                //1
//                btnSaveToServer.setEnabled(false);
//            }else{
//                btnSaveToServer.setEnabled(true);
//            }
//            btnDeleteFile.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
            ToastMessageCenter("Ошибка: Файл невозможно открыть.");
            return;
        }
    }

//
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
    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
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

}