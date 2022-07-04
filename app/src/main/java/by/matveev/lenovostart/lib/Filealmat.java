package by.matveev.lenovostart.lib;

import android.app.Activity;
import android.media.MediaScannerConnection;
import android.os.Environment;

import java.io.File;

public class Filealmat {
    public String PatshFile;
    public String NameFile;
    public String NameDirectory;
    final String LOG_TAG = "PatshDIR_SD";
    public Activity activity;



    public int  writeFileSD(String PatshDIR_SD, String FileName,StringBuilder addText){
        int returnerror = 0;


        //loadSetting(); добавить класс
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






        return returnerror;
    }
}
