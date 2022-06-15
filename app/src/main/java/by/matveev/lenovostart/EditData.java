package by.matveev.lenovostart;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
//
//    TextView txtTov;


//    InputStream inputStream;
//    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//    String csvLine;

    SQLiteDatabase database;

    final String FILENAME_DAT_TXT = "Dat1.txt";
    final String DIR_SD = "Documents";
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

        LoaddbListView();

    }
///////////////////////////

    @Override
    public void onClick(View v) {
        final DBRepository repositorys = new DBRepository(getApplicationContext());

        switch (v.getId()) {
            case R.id.btnDonloadDat:
                ArrayAdapter<String> datadapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, repositorys.getDataDat());
                datadapter.setDropDownViewResource(R.layout.simple_list_item_dat);
                dbListView.setAdapter(datadapter);
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


//                SQLiteDatabase checkDB = null;
//                try {
//                    checkDB = SQLiteDatabase.openDatabase(DBHelper.DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
//                } catch (SQLiteException e) {
//                    //файл базы данных отсутствует
//                }
//                if (checkDB != null) {
//                    //ничего не делаем – файл базы данных уже есть
//                } else {
//                    checkDB = SQLiteDatabase.openOrCreateDatabase(DBHelper.DATABASE_NAME, null, null);
//                }
//                String SqlText = "delete * from " + DBHelper.TABLE_DOCUMENT_DAT + " " + DBHelper.DAT_KEY_ID + "=" + sssssss;
//                checkDB.execSQL(SqlText );
//
//                checkDB.close();

//

                int iDataStatus = database.delete(dbHelper.TABLE_DOCUMENT_DAT," datid="+sssssss,null);
                datBaseCursor = database.query(dbHelper.TABLE_DOCUMENT_DAT, null, null, null, null, null, null);
                iCountField = datBaseCursor.getCount();//количество полей

                database.close();

                txtStroka.setText("");
                // сохранить в файл Dat1.txt
             //   dbListView.clear();

//                LoaddbListView();
//                datadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, repositorys.getDataDat());
//                datadapter.setDropDownViewResource(R.layout.simple_list_item_dat);
//                dbListView.setAdapter(datadapter);
                break;
        }
        //
    }
    //
    public void LoaddbListView(){


        dbHelper = new DBHelper(this);

        try {

            dbHelper.createDataBase();
            try {
                dbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }

            File csvfile = new File(Environment.getExternalStorageDirectory()+ "/" +
                    DIR_SD + "/" + FILENAME_DAT_TXT);
            ContentValues datContentValues = new ContentValues();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            /* "If you want to package the .csv file with the application and have it install on the internal storage when the app installs, create an assets folder in your project src/main folder (e.g., c:\myapp\app\src\main\assets\), and put the .csv file in there, then reference it like this in your activity:" (from the cited answer) */
            /* "Если вы хотите упаковать файл .csv вместе с приложением и установить его во внутреннее хранилище при установке приложения, создайте папку assets в вашей папке project src/main (например, c:\myapp\app\src\main\assets \), и поместите туда файл .csv, а затем ссылайтесь на него следующим образом в вашей деятельности:" (из процитированного ответа) */
            datFileString = this.getApplicationInfo().dataDir + File.pathSeparatorChar  +
                    FILENAME_DAT_TXT;
            File csvfiles = new File(datFileString);

            CSVReader reader = new CSVReader(
                    new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                    ';', '\'', 0);
            String sStrok = reader.toString();
            // считываем данные с БД
            database = dbHelper.getWritableDatabase();
            datBaseCursor = database.rawQuery("select * from " + dbHelper.TABLE_DOCUMENT_DAT,null);
            // определяем, какие столбцы из курсора будут выводиться в ListView
            iCountStrok = datBaseCursor.getCount(); //количество строк
            iCountStrok = 0;
            while ((nextLine = reader.readNext()) != null) {
                iCountStrok++;
            }
            reader = new CSVReader(
                    new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                    ';', '\'', 0);
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
            database.close();
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
            // txtLogMessege.setText("");
        }

    }

//    void writeFileSD(String PatshDIR_SD, String StrokaWrite) {// запись на SD диск  // подготавливаем переменные
//
//        String txtBarcode = txtnBarcode.getText().toString();
//        String txtNumber = "";
//        String txtQuantity = "";
//        String txtPrice = "";
//        String textAdd = "";
//        String line = "";
//        Integer NumberOfRecords = 0;
//        loadSetting();
//        if (txtnNumber.length() > 0 && txtnNumber.getVisibility() == View.VISIBLE)
//            txtNumber = txtnNumber.getText().toString();
//        if (txtdQuantity.length() > 0 && txtdQuantity.getVisibility() == View.VISIBLE)
//            txtQuantity = txtdQuantity.getText().toString();
//        if (txtdPrice.length() > 0 && txtdPrice.getVisibility() == View.VISIBLE)
//            txtPrice = txtdPrice.getText().toString();
//        String text =  txtBarcode + ";" + txtPrice + ";" + txtQuantity + ";" + txtNumber + ";";
//        // проверяем доступность SD
//        if (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
//            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
//            return;
//        }
//        // получаем путь к SD
//        File sdPath = Environment.getExternalStorageDirectory();
//        // добавляем свой каталог к пути
//        sdPath = new File(sdPath.getAbsolutePath() + "/" + PatshDIR_SD);
//        // создаем каталог
//        sdPath.mkdirs();
//
//        File[] elems = sdPath.listFiles();
//
//        String[] paths = new String[1 + (elems == null? 0 : elems.length)];
//        int i = 0;
//        paths[i] = sdPath.getAbsolutePath();//добавляем в список повторно сканируемых путей саму папку - что бы она отобразилась если была создана после подключения к компьютеру
//        i++;
//        if (elems != null) {
//            for (File elem : elems) {
//                paths[i] = elem.getAbsolutePath();//добавляем в список повторно сканируемых путей содержимое папки (у меня не было вложенных папок)
//                i++;
//            }
//        }
//        MediaScannerConnection.scanFile(ScanerActivity.this, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
//        // формируем объект File, который содержит путь к файлу
//        File sdFile = new File(sdPath, FILENAME_SD);
//        //File sdFile_copy = new File(sdPath, FILENAME_SD_copy);
//
//        // Проверка наличия файла
//        if (sdFile.exists()){
//            //ToastMessageCenter("Файл в наличии.");
//            // проверка разрешений
//            if (!myPremission())  return;
//            String lineSeparator = System.getProperty("line.separator");
//            try {
//                // открываем поток для чтения
//                BufferedReader br = new BufferedReader(new FileReader(sdFile));
//                // пишем данные
//                //ToastMessageCenter("Файл открыт для чтения.");
//
//                StringBuilder builder = new StringBuilder();
//                NumberOfRecords = 0;
//                while ((line = br.readLine()) != null) {
//                    builder.append(line + "\r\n");
//                    ++NumberOfRecords;
//                }
//                textAdd = builder.toString();
//                // закрываем поток
//                br.close();
//                Log.d(LOG_TAG, "Файл  на SD: " + sdFile.getAbsolutePath());
//                btnAddPosition.setText("Добавить позицию (" + NumberOfRecords + ")");
//                //        btnUploadDelete.setEnabled(true);
//                if (!sModeWorking.equals("1")) {
//                    btnSaveToServer.setEnabled(false);
//                }else{
//                    btnSaveToServer.setEnabled(true);
//                }
//                btnDeleteFile.setEnabled(true);
//
//                //ToastMessageCenter( "Чтение");
//                //if(sdFile.exists())
//                //sdFile.renameTo(sdFile); // переименовать файл
//                //copyFileUsingStream(sdFile, sdFile_copy);// копировать файл
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                ToastMessageCenter("Ошибка: Файл не открывается для чтения.");
//                return;
//            }
//        }//else{ ToastMessageCenter("Файл отсутствует."); }
//        try {
//            // открываем поток для записи если файла нет
//            //ToastMessageCenter("Запись");
//            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
//            // пишем данные
//            textAdd = textAdd + text ;
//            //bw.write(textAdd);
//
//            bw.append(textAdd);
//            // закрываем поток
//            bw.close();
//            ++NumberOfRecords;
//            //if(sdFile.exists())
//            //sdFile.renameTo(sdFile); // переименовать файл
//            //copyFileUsingStream(sdFile, sdFile);// копировать файл
//            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
//            //ToastMessageCenter("Данные сохранены на SD.+");
//            btnAddPosition.setText("Добавить позицию (" + NumberOfRecords + ")");
//            //     btnUploadDelete.setEnabled(true);
//            if (!sModeWorking.equals("1")) {
//                //1
//                btnSaveToServer.setEnabled(false);
//            }else{
//                btnSaveToServer.setEnabled(true);
//            }
//            btnDeleteFile.setEnabled(true);
//        } catch (IOException e) {
//            e.printStackTrace();
//            ToastMessageCenter("Ошибка: Файл невозможно открыть.");
//            return;
//        }
//    }

//


}