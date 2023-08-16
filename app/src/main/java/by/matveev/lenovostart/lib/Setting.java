package by.matveev.lenovostart.lib;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.opencsv.CSVReader;

import java.io.IOException;


public class Setting {

    SharedPreferences sPref;
    Filealmat filealmat;

    public String sAdressServer;
    public String sUserFTP;
    public String sPasswordFTP;
    public String sPortFTP;
    public String sPathFile;
    public String sModeWorking;
    public String FileNameSetting = "setting.csv";
    public String FileNameDat = "Dat1.txt";

    final String USER_NAME = "user_name";
    final String USER_PASSWORD = "user_passowrd";
    final String ADRESS_SERVER = "adress_server";
    final String PATH_FILE = "path_file";
    final String PORT_FTP = "21";
    final String MODE_WORKING = "1";
    public String[] nextLine = null;

    public boolean loadSetting(Context context) throws IOException {

        filealmat = new Filealmat();
        CSVReader reader;
        sPref = context.getSharedPreferences(FileNameSetting, MODE_PRIVATE);


        if (filealmat.LoadCsv((Activity) context, filealmat.NameDirectory,"setting.csv")){
        //    while ((nextLine = filealmat.reader.readNext()) != null) {// считываем данные с CSV  файла
                nextLine = filealmat.reader.readNext();
                if(nextLine==null){

                    return false;
                }
                sAdressServer = nextLine[0];
                sUserFTP = nextLine[1];
                sPasswordFTP = nextLine[2];
                sPortFTP = nextLine[3];
                sPathFile = nextLine[4];
                sModeWorking = nextLine[5];
            if (sAdressServer.equals(""))
                return false;

        }else{
            sAdressServer = sPref.getString(ADRESS_SERVER, "");
            sUserFTP = sPref.getString(USER_NAME, "");
            sPasswordFTP = sPref.getString(USER_PASSWORD, "");
            sPortFTP = sPref.getString(PORT_FTP, "");
            sPathFile = sPref.getString(PATH_FILE, "");
            sModeWorking = sPref.getString(MODE_WORKING, "");

            if (sAdressServer.equals("")){

              //return false;
            }else{
                nextLine = new String[6];
                nextLine[0] = sAdressServer;
                nextLine[1] = sUserFTP;
                nextLine[2] = sPasswordFTP;
                nextLine[3] = sPortFTP;
                nextLine[4] = sPathFile;
                nextLine[5] = sModeWorking;
            }
            if(!saveSetting(context)){
                return false;
            }
        }


        return true;
    }
    public boolean saveSetting(Context context) throws IOException {
       // String[] nextLine = null;
        //String text = "";
        filealmat = new Filealmat();
        StringBuilder sbText  = new StringBuilder();

        sPref = context.getSharedPreferences("setting", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        if (nextLine!= null) {
            ed.putString(ADRESS_SERVER, nextLine[0].toString());
            ed.putString(USER_NAME, nextLine[1].toString());
            ed.putString(USER_PASSWORD, nextLine[2].toString());
            ed.putString(PORT_FTP, nextLine[3].toString());
            ed.putString(PATH_FILE, nextLine[4].toString());
            ed.putString(MODE_WORKING, nextLine[5].toString());
            ed.commit();
        }else{
            ed.putString(ADRESS_SERVER, "10.250.1.16");
            ed.putString(USER_NAME, "FTPsession");
            ed.putString(USER_PASSWORD, "12345");
            ed.putString(PORT_FTP, "21");
            ed.putString(PATH_FILE, "Documents");
            ed.putString(MODE_WORKING, "1");
            ed.commit();
            return false;
        }
        for (int iFor = 0; iFor < nextLine.length; iFor++)
        {
            if(iFor != nextLine.length-1){
                sbText.append(nextLine[iFor] + ";");
            }else{
                sbText.append(nextLine[iFor] );
            }

        }

        if(0 != filealmat.writeFileSD(context,filealmat.NameDirectory,"setting.csv",sbText)){
            return false;
        }
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
            if(mExitValue == 0){
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
