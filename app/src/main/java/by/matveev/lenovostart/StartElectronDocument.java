package by.matveev.lenovostart;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;
import by.matveev.lenovostart.lib.FTPModel;
import by.matveev.lenovostart.lib.ProgressTextView;

public class StartElectronDocument extends AppCompatActivity implements View.OnClickListener {

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
    String[] nextLine;
    String[] countries = null;
    GridView dbGridBase;


    DBHelper dbHelper;
    EditText txtNumDocument;
    Button btnLoad;
    Button btnUnLoad;
    Button btnClearBase;
    Button btnAllNumNakl;
    Context context;
    Integer iNumCol;
    EditText txtLogMessege;
    ProgressTextView progressTextView;

    SQLiteDatabase database;



    final String FILENAME_CSV = "999.csv";
    final String DIR_SD = "Documents";

    private static final String FILENAME = "./alphabet.utf8";
    private static final String ENCODING_WIN1251 = "windows-1251";
    private static final String ENCODING_UTF8 = "UTF-8";


    String sqlStroka;

    public StartElectronDocument() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_electron_document);



        progressTextView = (ProgressTextView) findViewById(R.id.progressTextView);
        progressTextView.setValue(0); // устанавливаем нужное значение

        dbGridBase = (GridView) findViewById(R.id.dbGridBase);

        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);



        txtLogMessege = (EditText) findViewById(R.id.txtLogMessege);
        txtLogMessege.setOnClickListener(this);
       // btnUnLoad = (Button) findViewById(R.id.btnUnLoad);
      //  btnUnLoad.setOnClickListener(this);

        btnClearBase = (Button) findViewById(R.id.btnClearBase);
        btnClearBase.setOnClickListener(this);

        btnAllNumNakl = (Button) findViewById(R.id.btnAllNumNakl);
        btnAllNumNakl.setOnClickListener(this);


        dbHelper = new DBHelper(this);


    //    final DBRepository repository = new DBRepository(getApplicationContext());

        Intent intent = new Intent(this, electron_document.class);

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(),"Вы выбрали накладную № "
                                + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(this, electron_document.class);

                intent.putExtra("VisibletxtNumNakladn", parent.getItemAtPosition(position).toString());
                startActivity(intent);

 //               ssssss(parent.getItemAtPosition(position).toString());
                //startActivity(intent);

            }
        };

        dbGridBase.setOnItemClickListener(itemListener);
        //    dbGridBase.setOnClickListener(this);
    }

    public void ssssss(String sdf){

//    String nameTov;
//    String namePostav;
//    String numPoz;
//    String Barcodes;
//    String DataNakl;
//    String numNakldn;
//    String quntityTov;
//    String statusTov;


//    String s1;
//    String s2;
        final DBRepository repositorys = new DBRepository(getApplicationContext());
        Intent intent = new Intent(this, electron_document.class);

        intent.putExtra("VisibletxtNumNakladn", sdf);
        startActivity(intent);
        SQLiteDatabase database;
    //= dbHelper.getWritableDatabase();
  //  ContentValues contentValues = new ContentValues();

        dbHelper.createDataBase();
        try {
            dbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        ArrayAdapter<String> adapter;
        ArrayList<String> arrayBase;
        arrayBase = repositorys.getDataNakld(sdf);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, repositorys.getDataNakld(sdf));
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        dbGridBase.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        dbHelper = new DBHelper(this);
        //SQLiteDatabase database;
        //= dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Integer iCountStrok;
        Integer iCountField;
//        String sValueCSVNumNakldn = null;
//        String sValueFieldNumNakldn = null;
//        String sValueFieldBarcode = null;
//        String sValueFieldDate = null;
//        String sValueFieldNumPoz = null;
//        String sValueFieldName = null;
//        String sValueFieldQuantity = null;
//        String sValueFieldStatus = null;
//        String sValueFieldPost = null;
//        String sStrok = null;
        String[] countries = null;
       // dbHelper helper = new dbHelper(this);
        dbHelper.createDataBase();
        try {
            dbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
//обработка кнопок
        switch (v.getId()){
            case R.id.dbGridBase:
                ArrayAdapter<String> adapters = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);

                dbGridBase.setAdapter(adapters);
                break;
            case R.id.btnAllNumNakl:
                final DBRepository repository = new DBRepository(this.getApplicationContext());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, repository.getDataAllNakld());
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

                dbGridBase.setAdapter(adapter);
                break;
            case R.id.btnClearBase:
                try{
//                    sqlStroka = "select * from " + DBHelper.TABLE_DOCUMENT + " where " + DBHelper.KEY_BARCODE +
//                            " = '" + nextLine[5].toString() + "' AND " + DBHelper.KEY_NUM_NAKL +
//                            " = '" + nextLine[1].toString() + "' AND " +
//                            DBHelper.KEY_DATE + " = '" + nextLine[2].toString() + "'";
//                    basecursor = database.rawQuery(sqlStroka,null);
//                    basecursor.moveToFirst();// установка курсора в начало
//
                    database = dbHelper.getWritableDatabase();
                    //Cursor basecursor = database.query(DBHelper.TABLE_DOCUMENT, null, null, null, null, null, null);
                    //basecursor = database.rawQuery("select * from " + DBHelper.TABLE_DOCUMENT,null);
                    //iCountField = basecursor.getCount();//количество полей

                    database.delete(DBHelper.TABLE_DOCUMENT,null,null);
                    txtLogMessege.setBackgroundColor(Color.WHITE);
                    txtLogMessege.setText(" ТАБЛИЦА ПУСТАЯ ");
                } catch (SQLException sqle) {
                    throw sqle;
                }
                break;
            case R.id.btnLoad:
                //загружаем настройки программы
                txtLogMessege.setBackgroundColor(Color.WHITE);
                txtLogMessege.setText("...");
                loadSetting();
                if (!executeCommand(sAdressServer)){
                    txtLogMessege.setBackgroundColor(Color.RED);
                    break;
                }

                //подключаемся к FTP серверу
                FTPModel mymodel = new FTPModel();
                File sdPath = Environment.getExternalStorageDirectory();
                // добавляем свой каталог устройства к пути
                sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD + "/" + FILENAME_CSV);
                // загрузка csv файла с FTP сервера
                boolean ko = mymodel.downloadAndSaveFile(sAdressServer,Integer.parseInt(sPortFTP),
                        sUserFTP,sPasswordFTP,  FILENAME_CSV, sdPath);
                if(ko){
                    // Toast.makeText(context, "Файл данных загружен", Toast.LENGTH_LONG).show();
                    txtLogMessege.setText("ДАННЫЕ ЗАГРУЖЕНЫ");
                    txtLogMessege.setBackgroundColor(Color.GREEN);
                }else{
                    //Toast.makeText(context, "Файл данных не загружен", Toast.LENGTH_LONG).show();
                    txtLogMessege.setText("ДАННЫЕ НЕ СОХРАНЕНЫ!");
                    txtLogMessege.setBackgroundColor(Color.RED);
                    break;
                }
////////////////////////////   распаковка CSV    ////////////////////////////////////
                try {
// OPTION 1: if the file is in the sd

                    File csvfile = new File(Environment.getExternalStorageDirectory()+ "/" +
                            DIR_SD + "/" + FILENAME_CSV);
// END OF OPTION 1
                    dbHelper = new DBHelper(this);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
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
                    Cursor basecursor = database.rawQuery("select * from " + DBHelper.TABLE_DOCUMENT,null);
                    // определяем, какие столбцы из курсора будут выводиться в ListView
                    iCountStrok = basecursor.getCount();//количество строк
                    iCountStrok = 0;

                    while ((nextLine = reader.readNext()) != null) {
                        iCountStrok++;

                    }


                    reader = new CSVReader(
                            new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                            ';', '\'', 0);
                    progressTextView.setMaxValue(iCountStrok);
                    iCountField = basecursor.getColumnCount();//количество полей
                    basecursor.moveToFirst();// установка курсора в начало
                    iCountStrok = 1;
                    while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
                        progressTextView.setValue(iCountStrok);
                        iCountStrok++;
                        sqlStroka = nextLine[0].toString(); //QR code
                        sqlStroka = nextLine[1].toString(); // № invoice
                        sqlStroka = nextLine[2].toString(); // invoice date
                        sqlStroka = nextLine[3].toString();//provider
                        sqlStroka = nextLine[4].toString();// # items in the invoice
                        sqlStroka = nextLine[5].toString();// ифксщву
                        sqlStroka = nextLine[6].toString();//name  product
                        sqlStroka = nextLine[7].toString();// price
                        sqlStroka = nextLine[8].toString();// status

                         sqlStroka = "select * from " + DBHelper.TABLE_DOCUMENT + " where " + DBHelper.KEY_BARCODE +
                                 " = '" + nextLine[5].toString() + "' AND " + DBHelper.KEY_NUM_NAKL +
                                 " = '" + nextLine[1].toString() + "' AND " +
                                DBHelper.KEY_DATE + " = '" + nextLine[2].toString() + "'";
                        basecursor = database.rawQuery(sqlStroka,null);
                        basecursor.moveToFirst();// установка курсора в начало
                        iCountField = basecursor.getCount();//количество полей
                      //  csvfileString = basecursor.getString(4);
//                        if(basecursor.isAfterLast()){
//                         }else{
//                        }
                        if (iCountField != 0){

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
                        }else{
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
//                        if (sValueFieldNumNakldn.equals(nextLine[0].toString()) &&
//                                sValueFieldBarcode.equals(nextLine[4].toString()) &&
//                                sValueFieldDate.equals(nextLine[1].toString())) {

//                            //
//                            String strasd = DBHelper.KEY_NUM_NAKL + " = " + "'" + sValueCSVNumNakldn + "'" + " AND " +
//                                    DBHelper.KEY_DATE + " = " + "'" + sValueFieldDate + "'" + " AND " +
//                                    DBHelper.KEY_BARCODE + " = " + "'" + sValueFieldBarcode + "'";
//                            db.delete(DBHelper.TABLE_DOCUMENT, strasd, null);
//                            //db.execSQL("delete from " + DBHelper.TABLE_DOCUMENT + " where " + strasd);
//
//                        }else{
//
//
//                            break;
//                            //basecursor.moveToLast();
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
                    txtLogMessege.setText("");
            }
////////////////////////////  конец распаковки CSV   /////////////////////////
        }
    }
/////////////////////////////

    public class ParseCSVLineByLine
    {

    }
    ////////////////////////////////////////////
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    public void export(View view){
        //generate data
        StringBuilder data = new StringBuilder();
        data.append("Time,Distance");
        for(int i = 0; i<5; i++){
            data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
        }

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }


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
            txtLogMessege.setText(" mExitValue " + mExitValue);
            if(mExitValue==0){
                txtLogMessege.setText("ЕСТЬ СВЯЗЬ");
                return true;
            }else{
                txtLogMessege.setText("НЕТ СВЯЗИ");
                return false;
            }
        }
        catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
            txtLogMessege.setText(" Ошибка:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
            txtLogMessege.setText(" Ошибка:" + e);
        } return false;
    }
//////////////

}
