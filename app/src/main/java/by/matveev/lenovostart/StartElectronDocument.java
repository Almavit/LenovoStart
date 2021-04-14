package by.matveev.lenovostart;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.FTPModel;

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



    DBHelper dbHelper;
    EditText txtNumDocument;
    Button btnLoad;
    Button btnUnLoad;
    Button btnClear;

    Context context;

    final String FILENAME_CSV = "999.csv";
    final String DIR_SD = "Documents";

    public StartElectronDocument() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_electron_document);

       // SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contenvalue = new ContentValues();

        txtNumDocument = (EditText) findViewById(R.id.txtNumDocument);
        txtNumDocument.setOnClickListener(this);

        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);

        btnUnLoad = (Button) findViewById(R.id.btnUnLoad);
        btnUnLoad.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        dbHelper = new DBHelper(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtNumDocument:
                //

                break;
            case R.id.btnLoad:
                //

                loadSetting();
                FTPModel mymymodel = new FTPModel();

                File sdPath = Environment.getExternalStorageDirectory();
                // добавляем свой каталог к пути
                sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD + "/999.csv");

                String fs = "999.csv";
                boolean ko = mymymodel.downloadAndSaveFile(sAdressServer,Integer.parseInt(sPortFTP),sUserFTP,sPasswordFTP,  fs, sdPath);
                if(ko){
                    Toast.makeText(context, "Файл данных загружен", Toast.LENGTH_LONG).show();
                    //txtLogScaner.setText("ДАННЫЕ СОХРАНЕНЫ");
                }else{
                    Toast.makeText(context, "Файл данных не загружен", Toast.LENGTH_LONG).show();
                   // txtLogScaner.setText("ДАННЫЕ НЕ СОХРАНЕНЫ!");
                }


                break;
            case R.id.btnUnLoad:
                //
                break;
            case R.id.btnClear:
                //
                break;
        }

    }

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
//    public void export(View view) {
//        //generate data
//        StringBuilder data = new StringBuilder();
//        data.append("Time,Distance");
//        for (int i = 0; i < 5; i++) {
//            data.append("\n" + String.valueOf(i) + "," + String.valueOf(i * i));
//        }
//
//        try {
//            //saving the file into device
//            FileOutputStream out = openFileOutput(FILENAME_CSV, Context.MODE_PRIVATE);
//            out.write((data.toString()).getBytes());
//            out.close();
//
//            //exporting
//            Context context = getApplicationContext();
//            File sdPath = Environment.getExternalStorageDirectory();
//            // добавляем свой каталог к пути
//            sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
//
//            File filelocation = new File(sdPath, FILENAME_CSV);
//
//            //File filelocation = new File(getFilesDir(), FILENAME_CSV);
//            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
//            Intent fileIntent = new Intent(Intent.ACTION_SEND);
//            fileIntent.setType("text/csv");
//            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "999");
//            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
//            startActivity(Intent.createChooser(fileIntent, "Send mail"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
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
}
