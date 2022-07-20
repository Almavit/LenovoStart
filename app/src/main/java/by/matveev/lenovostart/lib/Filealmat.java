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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Filealmat {
    public String PatshFile;
    public String NameFile;
    public String NameDirectory = "Documents";
    final String LOG_TAG = "PatshDIR_SD";
    //public Activity activity;
    private static final String ENCODING_WIN1251 = "windows-1251";
    public CSVReader reader;
    public Integer NumberOfRecords = 0;

public boolean DeleteFile(String PatshDIR_SD, String FileName){
    int returnerror = 0;
    String textAdd = "";
    String line = "";
    // получаем путь к SD
    File sdPath = Environment.getExternalStorageDirectory();
    // добавляем свой каталог к пути
    sdPath = new File(sdPath.getAbsolutePath() + "/" + PatshDIR_SD);
    // создаем каталог
    sdPath.mkdirs();
//    File[] elems = sdPath.listFiles();
//
//    String[] paths = new String[1 + (elems == null? 0 : elems.length)];
//    int i = 0;
//    paths[i] = sdPath.getAbsolutePath();//добавляем в список повторно сканируемых путей саму папку - что бы она отобразилась если была создана после подключения к компьютеру
//    i++;
//    if (elems != null) {
//        for (File elem : elems) {
//            paths[i] = elem.getAbsolutePath();//добавляем в список повторно сканируемых путей содержимое папки (у меня не было вложенных папок)
//            i++;
//        }
//    }
 //   MediaScannerConnection.scanFile(contex, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
//        // формируем объект File, который содержит путь к файлу
    File sdFile = new File(sdPath, FileName);
// Проверка наличия файла
    if (sdFile.exists()) {
        //Файл в наличии
        sdFile.delete();
    }
    return true;
}
    public int  writeFileSD(Context contex, String PatshDIR_SD, String FileName,StringBuilder addText) throws IOException {
        int returnerror = 0;
        String textAdd = "";
        String line = "";
    //    Setting setting = new Setting();
   //     setting.loadSetting(contex);

        //loadSetting(); //добавить класс
        // проверяем доступность SD
        MyPremission almPremission = new MyPremission();
        if (!almPremission.myPremission(contex))  {
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
        MediaScannerConnection.scanFile(contex, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
//        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FileName);


// Проверка наличия файла
        if (sdFile.exists()){
            //Файл в наличии
            //String df = sdFile.getName();


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
            } catch (IOException e) {
                e.printStackTrace();
                //ToastMessageCenter("Ошибка: Файл не открывается для чтения.");
                return -1;
            }
        }

        Setting setting = new Setting();
        if (sdFile.getName().equals(setting.FileNameSetting)){
            // открываем поток для записи настроек
            //ToastMessageCenter("Запись");
                BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
                textAdd = addText.toString();
                bw.write(textAdd);
                bw.close();
                return 0;
        }
        try {


            // открываем поток для записи если файла нет
            //ToastMessageCenter("Запись");
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            textAdd = textAdd + addText.toString();//list.toString();//text;//Add + text ;


            bw.append(textAdd);
            // закрываем поток

            bw.close();
            ++NumberOfRecords;
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            /////ToastMessageCenter("Ошибка: Файл невозможно открыть.");
            returnerror = -2;
        }


        return returnerror;
    }

    public boolean makeFolder(Context context,String namedirectoryDocuments){
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
                Toast.makeText(context, "Folder created successfully", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, "Failed to create folder", Toast.LENGTH_LONG).show();
            }

        }
        else {
            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
        }
        return true;
    }

    public boolean LoadCsv(Context context, String DirName, String FileNameCSV){

        String textAdd = "";
        String line = "";
        // проверяем доступность SD
        MyPremission almPremission = new MyPremission();
        if (!almPremission.myPremission(context))  {
            return false;
        }else{

        }
//        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
//        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DirName);
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
        MediaScannerConnection.scanFile(context, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
//        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FileNameCSV);
// Проверка наличия файла
        if (sdFile.exists()) {
            //Файл в наличии
            try {
                File csvfile = new File(Environment.getExternalStorageDirectory() + "/" +
                        DirName + "/" + FileNameCSV);

                reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                        ';', '\n', 0);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }
//  получить данные из csv файла по сети
    public boolean LoadCsvFileFtp(Context context, String DirName, String FileNameCSV) throws IOException {
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

        reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                ';', '\n', 0);
        //String[] nextLine = reader.readNext();
        csvfile.exists();
        return true;
    }

//сохранение данные из файла csv в БД SQLite
    public boolean LoadSaveCsvToDB(Context context, String DirName, String FileNameCSV, String SqlStroka, String TableName) throws IOException, InterruptedException {

        boolean returnstatus = true;
        ContentValues ScontentValues = new ContentValues();
        Setting setting = new Setting();

        if (!LoadCsvFileFtp(context, DirName, FileNameCSV)){
            return false;
        }else{
            DBHelper dbHelper = new DBHelper(context);
            if (!dbHelper.SaveDataPrice(context,TableName,SqlStroka,reader)){
                return false;
            }
            dbHelper.close();
            reader.close();
            ScontentValues.clear();
        }



        return returnstatus;
    }
}
