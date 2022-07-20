package by.matveev.lenovostart;

import android.Manifest;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;
import by.matveev.lenovostart.lib.Filealmat;

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
        switch (v.getId()) {
            case R.id.btnDonloadDat:
                if (LoaddbListView()) {
                    if(!AdapterEdit()){
                        Toast.makeText(getApplicationContext(), "ОШИБКА ОБНОВЛЕНИЯ АДАПТЕРА", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                break;
            case R.id.btnSaveDat:
                DataBDEditDelete(0);
                SaveToFileEdit();
                if(!AdapterEdit()){
                    Toast.makeText(getApplicationContext(), "ОШИБКА ОБНОВЛЕНИЯ АДАПТЕРА", Toast.LENGTH_LONG).show();
                    break;
                }
                break;
            case R.id.btnDeleDat:
                LoaddbListView();
                DataBDEditDelete(-1);
                // обновляем таблицу
                if(!AdapterEdit()){
                    Toast.makeText(getApplicationContext(), "btnAddPosition on focus", Toast.LENGTH_LONG).show();
                    break;
                }
                SaveToFileEdit();
                break;
        }
    }
    //
    public boolean SaveToFileEdit(){

        Filealmat filealmat = new Filealmat();
        StringBuilder stringBuilder = new StringBuilder();
        database = dbHelper.getWritableDatabase();

        DBRepository.DatFields fieldbarcose = DBRepository.DatFields.DAT_KEY_BARCODE;
        DBRepository.DatFields fieldnumber = DBRepository.DatFields.DAT_KEY_NUMBER;
        DBRepository.DatFields fieldquantity = DBRepository.DatFields.DAT_KEY_QUANTITY;
        DBRepository.DatFields fieldprice = DBRepository.DatFields.DAT_KEY_PRICE;
        Cursor cursor = database.query(DAT_TABLE_DOCUMENT,null , null,null, null, null, null);
        int iF = cursor.getCount();
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            Integer iCountFields = cursor.getColumnCount();
            do {
                iCountFields = cursor.getPosition();
                String text = //cursor.getString(fieldid.getFieldCode()) + "   ;   " +
                        cursor.getString(fieldbarcose.getFieldCode()) + ";" +
                                cursor.getString(fieldprice.getFieldCode()) + ";" +
                                cursor.getString(fieldquantity.getFieldCode()) + ";" +
                                cursor.getString(fieldnumber.getFieldCode()) + "\n" ;
                stringBuilder.append(text);
            } while (cursor.moveToNext());
        }
        database.close();
        // Запись в файл Dat1.txt
        try {
            filealmat.DeleteFile(DIR_SD, FILENAME_DAT_TXT);
            filealmat.writeFileSD(this,DIR_SD, FILENAME_DAT_TXT,stringBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean  DataBDEditDelete(int status){
        List resultList = new ArrayList();
        ContentValues cv = new ContentValues();
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        String csvLine = null;
        String sID = txtStroka.getText().toString();
        String sStroks ;
        if (!((csvLine = sID) != null))
            return false;
        String[] row = csvLine.split(";");
        resultList.add(row);
        sID = row[0].toString().replaceAll("\\s","");

        if (status < 0) {//отрицательный - дэлете
            int iDataStatus = database.delete(dbHelper.TABLE_DOCUMENT_DAT, " datid=" + sID, null);
        }
        if (status == 0) {//0 - редактировать
            // String[] row = csvLine.split(";");
            sStroks = row[0].toString().replaceAll("\\s","");
            sStroks = sStroks + row[1].toString().replaceAll("\\s","");
            sStroks = sStroks + row[2].toString().replaceAll("\\s","");
            sStroks = sStroks + row[3].toString().replaceAll("\\s","");
            sStroks = sStroks + row[4].toString().replaceAll("\\s","");

            cv.put(dbHelper.DAT_KEY_ID, row[0].toString().replaceAll("\\s",""));
            cv.put(dbHelper.DAT_KEY_BARCODE, row[1].toString().replaceAll("\\s",""));
            cv.put(dbHelper.DAT_KEY_PRICE, row[2].toString().replaceAll("\\s",""));
            cv.put(dbHelper.DAT_KEY_QUANTITY, row[3].toString().replaceAll("\\s",""));
            cv.put(dbHelper.DAT_KEY_POSITION, row[4].toString().replaceAll("\\s",""));


            int iDataStatus = database.update(dbHelper.TABLE_DOCUMENT_DAT,cv , " datid = ?" ,  new String[] {sID});
        }
        datBaseCursor = database.query(dbHelper.TABLE_DOCUMENT_DAT, null, null, null, null, null, null);
        iCountField = datBaseCursor.getCount();//количество полей

        database.close();

        txtStroka.setText("");

        return true;
    }
    public boolean AdapterEdit(){
        final DBRepository repositorys = new DBRepository(getApplicationContext());
        ArrayAdapter<String> datadapterdat;
        ArrayAdapter<String> datadapter;

        datadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, repositorys.getDataDat());
        datadapter.setDropDownViewResource(R.layout.simple_list_item_dat);
        dbListView.setAdapter(datadapter);

        return true;
    }
    public boolean LoaddbListView(){
        final String LOG_TAG = "LoaddbListView";
        ContentValues datContentValues = new ContentValues();
        dbHelper = new DBHelper(this);
        Filealmat filealmat = new Filealmat();
        try {

            if (!filealmat.LoadCsvFileFtp(this,DIR_SD, FILENAME_DAT_TXT)){
                Toast.makeText(this, "ДАННЫЕ НАСТРОЕК ОТСУТСТВУЮТ!!!", Toast.LENGTH_SHORT).show();
                return false;
            }
            dbHelper.SaveDataDat(this, dbHelper.TABLE_DOCUMENT_DAT, "", filealmat.reader);

        } catch (Exception e) {
            if(null != dbHelper) {
                dbHelper.close();
            }

            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found 1", Toast.LENGTH_SHORT).show();
            return false;
        }
            if(null != dbHelper) {
                dbHelper.close();
            }
            return true;
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
        id = dbListView.getCount();

                SparseBooleanArray chosen = (dbListView).getCheckedItemPositions();
        for (int i = 0; i < chosen.size(); i++) {
            // если пользователь выбрал пункт списка,
            // то выводим его в TextView.
            if (chosen.valueAt(i)) {
               // selection.append(foods[chosen.keyAt(i)] + " ");
                text = text +  chosen.keyAt(i) + ";";
            }
        }
        //DBRepository.DatFields fieldid = DBRepository.DatFields.DAT_KEY_ID;
        DBRepository.DatFields fieldbarcose = DBRepository.DatFields.DAT_KEY_BARCODE;
        DBRepository.DatFields fieldnumber = DBRepository.DatFields.DAT_KEY_NUMBER;
        DBRepository.DatFields fieldquantity = DBRepository.DatFields.DAT_KEY_QUANTITY;
        DBRepository.DatFields fieldprice = DBRepository.DatFields.DAT_KEY_PRICE;

        //ArrayList<String> list = new ArrayList<String>();
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
                        cursor.getString(fieldprice.getFieldCode()) + ";" +
                        cursor.getString(fieldquantity.getFieldCode()) + ";" +
                        cursor.getString(fieldnumber.getFieldCode()) + "\r\n";
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