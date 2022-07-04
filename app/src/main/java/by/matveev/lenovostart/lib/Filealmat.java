package by.matveev.lenovostart.lib;

import android.app.Activity;

public class Filealmat {
    public String PatshFile;
    public String NameFile;
    public String NameDirectory;
    final String LOG_TAG = "PatshDIR_SD";
    public Activity activity;



    public int  writeFileSD(String PatshDIR_SD, String FileName,StringBuilder addText){
        int returnerror = 0;
        MyPremission almPremission = new MyPremission();
        if (!almPremission.myPremission(activity))  {
            returnerror = -1;
        }else{

        }


        return returnerror;
    }
}
