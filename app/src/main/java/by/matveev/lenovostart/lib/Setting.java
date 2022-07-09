package by.matveev.lenovostart.lib;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;


public class Setting {

    public String sAdressServer;
    public String sUserFTP;
    public String sPasswordFTP;
    public String sPortFTP;
    public String sPathFile;
    public String sModeWorking;

    final String USER_NAME = "user_name";
    final String USER_PASSWORD = "user_passowrd";
    final String ADRESS_SERVER = "adress_server";
    final String PATH_FILE = "path_file";
    final String PORT_FTP = "21";
    final String MODE_WORKING = "1";

    public boolean loadSetting(Context context) {

        SharedPreferences sPref = context.getSharedPreferences("setting", Context.MODE_PRIVATE);

        sAdressServer = sPref.getString(ADRESS_SERVER, "");
        sUserFTP = sPref.getString(USER_NAME, "");
        sPasswordFTP = sPref.getString(USER_PASSWORD, "");
        sPortFTP = sPref.getString(PORT_FTP, "");
        sPathFile = sPref.getString(PATH_FILE, "");
        sModeWorking = sPref.getString(MODE_WORKING, "");

        return true;
    }




    ///////////////////////////////
    public boolean executeCommand(String ip){
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 "+ ip);
            int mExitValue = mIpAddrProcess.waitFor();
            //txtLogMessege.setText(" mExitValue " + mExitValue);
            if(mExitValue==0){
                //txtLogMessege.setText("ЕСТЬ СВЯЗЬ");
                return true;
            }else{
                //txtLogMessege.setText("НЕТ СВЯЗИ");
                return false;
            }
        }
        catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
            //txtLogMessege.setText(" Ошибка:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
            //txtLogMessege.setText(" Ошибка:" + e);
        } return false;
    }



}
