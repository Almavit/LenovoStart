package by.matveev.lenovostart.lib;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Filealmat {
    public String PatshFile;
    public String NameFile;
    public String NameDirectory = "Documents";
    final String LOG_TAG = "PatshDIR_SD";
    public Activity activity;
    private static final String ENCODING_WIN1251 = "windows-1251";



    public int  writeFileSD(String PatshDIR_SD, String FileName,StringBuilder addText){
        int returnerror = 0;
        String textAdd = "";

        //loadSetting(); //добавить класс
        // проверяем доступность SD
        MyPremission almPremission = new MyPremission();
        if (!almPremission.myPremission(activity))  {
            returnerror = -1;
        }else{

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
        MediaScannerConnection.scanFile(activity, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
//        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FileName);



        try {
            // открываем поток для записи если файла нет
            //ToastMessageCenter("Запись");
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            textAdd = addText.toString();//list.toString();//text;//Add + text ;
//            //bw.write(textAdd);
//
            bw.append(textAdd);
            // закрываем поток
            bw.close();
           // ++NumberOfRecords;
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
            /////ToastMessageCenter("Ошибка: Файл невозможно открыть.");
            returnerror = -2;
        }


        return returnerror;
    }

    public boolean makeFolder(Activity activity,String namedirectoryDocuments){
        String sDirectory = "";
        if (namedirectoryDocuments == ""){
            sDirectory = NameDirectory;
        }else{
            sDirectory = namedirectoryDocuments;
        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),sDirectory);

        if (!file.exists()){
            Boolean ff = file.mkdir();
            if (ff){
                Toast.makeText(activity, "Folder created successfully", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(activity, "Failed to create folder", Toast.LENGTH_LONG).show();
            }

        }
        else {
            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
        }
        return true;
    }



    public boolean LoadSaveCsvToDB(Context context, String DirName, String FileNameCSV, String SqlStroka, String TableName) throws IOException, InterruptedException {
        boolean returnstatus = true;
        ContentValues ScontentValues = new ContentValues();
        Setting setting = new Setting();

        if (!setting.loadSetting(context)){
            return false;
        }
        if (!setting.executeCommand(setting.sAdressServer)) {
           // txtLog.setBackgroundColor(Color.RED);
            return false;
        }
        //SQLiteDatabase db;
        //подключаемся к FTP серверу
        FTPModel mymodel = new FTPModel();
        // получает корневой каталог
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог устройства к пути куда загружаем файл с сервера
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DirName + "/" + FileNameCSV);
        // загрузка csv файла с FTP сервера
        boolean ko = mymodel.downloadAndSaveFile(setting.sAdressServer, Integer.parseInt(setting.sPortFTP),
                setting.sUserFTP, setting.sPasswordFTP, FileNameCSV, sdPath);
        if (ko) {

        } else {
            return false;// не загрузилось
        }
//////////////////////////////////
        File csvfile = new File(Environment.getExternalStorageDirectory() + "/" +
                DirName + "/" + FileNameCSV);

        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                ';', '\n', 0);

// END OF OPTION 1
        DBHelper dbHelper = new DBHelper(context);
        if (!dbHelper.SaveDataPrice(context,TableName,SqlStroka,reader)){
            return false;
        }
//
//        db = dbHelper.getWritableDatabase();//getReadableDatabase
//        db.delete(TableName,null,null);
//        db.close();
//        db = dbHelper.getWritableDatabase();
//        Cursor basecursor = db.rawQuery(SqlStroka, null);//"select * from " + DBHelper.TABLE_DOCUMENT
//        Integer iCount = basecursor.getCount();
//        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
//                ';', '\'', 0);
//        //sStrok = reader.toString();
//        // считываем данные с БД
//        //db = dbHelper.getWritableDatabase();
//        // db = dbHelper.getWritableDatabase();
//
//        while ((nextLine = reader.readNext()) != null) {// считываем данные с CSV  файла
//            ScontentValues = ScontentValues(nextLine);
////            ScontentValues.put(DBHelper.KEY_QR_CODE, nextLine[0]);
////            ScontentValues.put(DBHelper.KEY_NUM_NAKL, nextLine[1]);
////            ScontentValues.put(DBHelper.KEY_DATE, nextLine[2]);
////            ScontentValues.put(DBHelper.KEY_NAME_POST, nextLine[3]);
////            ScontentValues.put(DBHelper.KEY_NUM_POZ, nextLine[4]);
////            ScontentValues.put(DBHelper.KEY_BARCODE, nextLine[5]);
////            ScontentValues.put(DBHelper.KEY_NAME_TOV, nextLine[6]);
////            ScontentValues.put(DBHelper.KEY_QUANTITY, nextLine[7]);
////            ScontentValues.put(DBHelper.KEY_STATUS, nextLine[8]);
//            db.insert(DBHelper.TABLE_DOCUMENT, null, ScontentValues);
//        }
//        db.close();

        return returnstatus;
    }
}
