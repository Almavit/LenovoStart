package by.matveev.lenovostart.lib;

import android.content.ContentValues;
import android.content.Context;
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
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class Filealmat {
    public String PatshFile;
    public String NameFileCSV;
    public String NameFileCSV_IP = "wifi.csv";
    public String NameDirectory = "Documents";
    public String NameFileAPK = "app-debug.apk";
    final String LOG_TAG = "PatshDIR_SD";
    //public Activity activity;
    private static final String ENCODING_WIN1251 = "windows-1251";
    private static final String ENCODING_DOS = "Cp866";
    public CSVReader reader;
    public Integer NumberOfRecords = 0;


    //    @Override
//    public void onCreate() {
//
//    }
    public boolean DeleteFile(String PatshDIR_SD, String FileName) {
        int returnerror = 0;
        String textAdd = "";
        String line = "";
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + PatshDIR_SD);
//        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FileName);
// Проверка наличия файла
        if (sdFile.exists()) {
            //Файл в наличии
            sdFile.delete();
        }
        if (sdFile.exists()) {
            //Файл в наличии
            sdFile.delete();
        }
        return true;
    }

    public int writeFileSD(Context contex, String PatshDIR_SD, String FileName, StringBuilder addText) throws IOException {
        int returnerror = 0;
        String textAdd = "";
        String line = "";
        // проверяем доступность SD
        MyPremission almPremission = new MyPremission();
        if (!almPremission.myPremission(contex)) {
            returnerror = -1;
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
        String[] paths = new String[1 + (elems == null ? 0 : elems.length)];
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
        if (sdFile.exists()) {
            //Файл в наличии
            try {
                // открываем поток для чтения
                BufferedReader br = new BufferedReader(new FileReader(sdFile));
                // пишем данные
                //ToastMessageCenter("Файл открыт для чтения.");

                StringBuilder builder = new StringBuilder();
                NumberOfRecords = 0;
                //      String sdsdsdsd = br.toString();
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
        if (sdFile.getName().equals(setting.FileNameSetting)) {
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
            if (addText.length() > 0) {
                textAdd = textAdd + addText.toString();//list.toString();//text;//Add + text ;
                ++NumberOfRecords;
            }
            bw.append(textAdd);
            // закрываем поток

            bw.close();

            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            /////ToastMessageCenter("Ошибка: Файл невозможно открыть.");
            returnerror = -2;
        }


        return returnerror;
    }

    public boolean makeFolder(Context context, String namedirectoryDocuments) {
        String sDirectory = "";
        if (namedirectoryDocuments == "") {
            sDirectory = NameDirectory;
        } else {
            sDirectory = namedirectoryDocuments;
        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + sDirectory + "/");
        if (!file.exists()) {
            Boolean ff = file.mkdir();
            if (ff) {
                Toast.makeText(context, "Папка " + sDirectory + " успешно создана", Toast.LENGTH_LONG).show();//Папка успешно создана
            } else {
                Toast.makeText(context, "Не удалось создать папку " + sDirectory, Toast.LENGTH_LONG).show();
            }

        } else {
            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
        }


        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + sDirectory + "/base/");

        if (!file.exists()) {
            Boolean ff = file.mkdir();
            if (ff) {
                Toast.makeText(context, "Папка " + sDirectory + " успешно создана", Toast.LENGTH_LONG).show();//Папка успешно создана
            } else {
                Toast.makeText(context, "Не удалось создать папку " + sDirectory, Toast.LENGTH_LONG).show();
            }

        } else {
            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
        }
        return true;
    }

    public boolean LoadCsv(Context context, String DirName, String FileNameCSV) {

        String textAdd = "";
        String line = "";
        // проверяем доступность SD
        MyPremission almPremission = new MyPremission();
        if (!almPremission.myPremission(context)) {
            return false;
        } else {

        }
//        // формируем объект File, который содержит путь к файлу
        File csvfile = new File(Environment.getExternalStorageDirectory() + "/" +
                DirName + "/" + FileNameCSV);

// Проверка наличия файла
        if (csvfile.exists()) {
            //Файл в наличии
            try {
                reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                        ';', '\n', 0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean ConvertDBFtoCSVFile(Context context, String DirName, String FileNameDBF, String FileNameCSV) {

        String FilePath = Environment.getExternalStorageDirectory().toString() + "/" + DirName + "/" + FileNameDBF;
        if (LoadFileFtp(context, DirName, FileNameDBF)) {
        }
        try {
            RandomAccessFile file = new RandomAccessFile(FilePath, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(FilePath, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            file.seek(32);
            //чтение
            byte[] bytes = new byte[524];
            file.read(bytes);
            //читаем строку, начиная с текущего положения курсора и до конца строки
            String text = file.readLine();
//            String texta = text.getBytes("Windows-1251").toString();
//            String texts = (new String(bytes,"US-ASCII"));
            file.close();
            Integer iFor;
            // открываем поток для записи если файла нет
            if (text.length() > 0) {
                String[] word = text.split("");
                String barcode = text.substring(20, 33);
                String nametov = text.substring(33, 113);
                for (String words : word) {
                    text = word[21];
                    text = words.substring(20, 13);
                }
            }
            String FileCSVPath = Environment.getExternalStorageDirectory().toString() + "/" + DirName + "/" + FileNameCSV;
            BufferedWriter bw = new BufferedWriter(new FileWriter(FileCSVPath));
            // пишем данные
            if (text.length() > 0) {
                text = text.toString();
                // ++NumberOfRecords;
            }
            bw.append(text);
            // закрываем поток

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //  получить данные из csv файла по сети
    public boolean LoadCsvFileFtp(Context context, String DirName, String FileNameCSV) throws IOException {
        if (!LoadFileFtp(context, DirName, FileNameCSV)) {

            return false;

        }
//////////////////////////////////
        File csvfile = new File(Environment.getExternalStorageDirectory() + "/" + DirName + "/" + FileNameCSV);
        if (csvfile.exists()) {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                    ';', '\n', 0);
            csvfile.exists();
        }
        return true;
    }


    public boolean LoadFileFtp(Context context, String DirName, String FileName) {
        Setting setting = new Setting();
        try {
            if (!setting.loadSetting(context)) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DirName + "/" + FileName);
        // загрузка csv файла с FTP сервера
        boolean ko = mymodel.downloadAndSaveFile(setting.sAdressServer, Integer.parseInt(setting.sPortFTP),
                setting.sUserFTP, setting.sPasswordFTP, FileName, sdPath);
        if (ko) {

        } else {
            return false;// не загрузилось
        }
        return true;
    }


    //сохранение данные из файла csv в БД SQLite
    public boolean LoadSaveCsvToDB(Context context, String DirName, String FileNameCSV, String SqlStroka, String TableName) throws IOException, InterruptedException {

        boolean returnstatus = true;
        ContentValues ScontentValues = new ContentValues();
        Setting setting = new Setting();

        if (!LoadCsvFileFtp(context, DirName, FileNameCSV)) {
            return false;
        } else {
            DBHelper dbHelper = new DBHelper(context);

            if (FileNameCSV.equals("price.csv")) {
                if (!dbHelper.SaveDataPrice(context, TableName, SqlStroka, reader)) {
                    return false;
                }
            }
            if (FileNameCSV.equals("wifi.csv")) {
//                if (!dbHelper.SaveDataIP(context, TableName, SqlStroka, reader)) {
//                    return false;
//                }}
                if (!dbHelper.DBSaveData(context, TableName, SqlStroka, reader)) {
                    return false;
                }
            }
            dbHelper.close();
            reader.close();
            ScontentValues.clear();
        }


        return returnstatus;
    }
//    @Override
//    public void onCreate() {
//
//    }
}
