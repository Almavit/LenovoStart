package by.matveev.lenovostart;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import by.matveev.lenovostart.lib.FTPModel;


public class ScanerActivity extends AppCompatActivity implements View.OnClickListener{

    EditText txtnBarcode;
    EditText txtdPrice;
    EditText txtdQuantity;
    EditText txtnNumber;
    TextView txtvBarcode;
    TextView txtvPrice;
    TextView txtvQuantity;
    TextView txtvNumber;
    TextView txtLogScaner;
    Button btnAddPosition;
    //Button btnUploadDelete;
    Button btnDeleteFile;
    Button btnSaveToServer;

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


    private static final int MY_REQUEST_CODE = 123;

    private WifiManager wifiManager;



    InputMethodManager imm;// для вывода клавиатуры
    // переменные для диалогового окна Выгрузить - Удалить
    AlertDialog.Builder ad;
    Context context;
    // идентификатор диалогового окна AlertDialog с кнопками
    private final int IDD_THREE_BUTTONS = 0;


    final String LOG_TAG = "myLogs";
    final String DIR_SD = "Documents";
    final String FILENAME_SD = "Dat1.txt";
    //final String FILENAME_SD_copy = "Dat_Copy.txt";


    private static final int PERMISSION_REQUEST_CODE = 123;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);





        setContentView(R.layout.activity_scaner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // переменные и функции для диалогового окна
        loadSetting();  //загрузка настроек программы
        context = ScanerActivity.this;
        String title = "Выбор есть всегда";
        String message = "Выбери пищу";
        String button1String = "Вкусная пища";
        String button2String = "Здоровая пища";

        ad = new AlertDialog.Builder(context);
        ad.setTitle(title);  // заголовок
        ad.setMessage(message); // сообщение
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Вы сделали правильный выбор",
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Возможно вы правы", Toast.LENGTH_LONG)
                        .show();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context, "Вы ничего не выбрали",
                        Toast.LENGTH_LONG).show();
            }
        });


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        txtnBarcode = (EditText) findViewById(R.id.txtnBarcode);
        txtvBarcode = (TextView) findViewById(R.id.txtvBarcode);
        txtvPrice = (TextView) findViewById(R.id.txtvPrice);
        txtdPrice = (EditText) findViewById(R.id.txtdPrice);
        txtvQuantity = (TextView) findViewById(R.id.txtvQuantity);
        txtdQuantity = (EditText) findViewById(R.id.txtdQuantity);
        txtvNumber = (TextView) findViewById(R.id.txtvNumber);
        txtnNumber = (EditText) findViewById(R.id.txtnNumber);

        txtLogScaner = (TextView) findViewById(R.id.txtLogScaner);
        btnAddPosition = (Button) findViewById(R.id.btnAddPosition);
 //       btnUploadDelete = (Button) findViewById(R.id.btnUploadDelete);

        btnDeleteFile = (Button) findViewById(R.id.btnDeleteFile);
        btnSaveToServer = (Button) findViewById(R.id.btnSaveToServer);
        btnSaveToServer.setOnClickListener(this);
        Intent intent = getIntent();

        //txtIpConnection.setVisibility(VisibleTxtNumber);
// barcode visible
/*        int VisibleTxtBarcode = intent.getIntExtra("VisibleTxtBarcode",  View.VISIBLE);
        txtvBarcode.setVisibility(VisibleTxtBarcode);
        int VisibleIntBarcode = intent.getIntExtra("VisibleIntBarcode",  View.VISIBLE);
        txtnBarcode.setVisibility(VisibleIntBarcode);*/
// number visible
        int VisibleTxtNumber = intent.getIntExtra("VisibleTxtNumber",  View.VISIBLE);
        txtvNumber.setVisibility(VisibleTxtNumber);
        int VisibleIntNumber = intent.getIntExtra("VisibleIntNumber",  View.VISIBLE);
        txtnNumber.setVisibility(VisibleIntNumber);
// pricevisible
        int VisibleTxtPrice = intent.getIntExtra("VisibleTxtPrice",  View.VISIBLE);
        txtvPrice.setVisibility(VisibleTxtPrice);
        int VisibleIntPrice = intent.getIntExtra("VisibleIntPrice",  View.VISIBLE);
        txtdPrice.setVisibility(VisibleIntPrice);
// quantity visible
        int VisibleTxtQuantity = intent.getIntExtra("VisibleTxtQuantity",  View.VISIBLE);
        txtvQuantity.setVisibility(VisibleTxtQuantity);
        int VisibleIntQuantity = intent.getIntExtra("VisibleIntQuantity",  View.VISIBLE);
        txtdQuantity.setVisibility(VisibleIntQuantity);

// проверка наличия разрешения на чтение и редактирование файла
        readFileSD();

// события barcode addTextChangedListener
        txtnBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ToastMessageCenter("txtnBarcode - beforeTextChanged");
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // ToastMessageCenter("txtnBarcode - onTextChanged");
            }
            @Override
            public void afterTextChanged(Editable editable) {
               // ToastMessageCenter("txtnBarcode - afterTextChanged");
            }
        });

        txtnBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    //
                if (txtdPrice.getVisibility() == View.VISIBLE){
                    txtdPrice.setFocusable(true);
                    txtdPrice.selectAll();
                    showKeyboard(txtdPrice);
                }else{
                    if (txtnNumber.getVisibility() == View.VISIBLE){
                        txtnNumber.setFocusable(true);
                        txtnNumber.selectAll();
                        showKeyboard(txtnNumber);
                    }else{
                        if (txtdQuantity.getVisibility() == View.VISIBLE){
                            txtdQuantity.setFocusable(true);
                            txtdQuantity.selectAll();
                            showKeyboard(txtdQuantity);
                        }else{
                            FocusView();
                        }
                    }
                }
            }
            return false;
            }
        });

        txtdPrice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&  (keyCode == KeyEvent.KEYCODE_ENTER)){
                    if (txtdQuantity.getVisibility() == View.VISIBLE) {
                        //
                    }else{
                        FocusView();
                        txtdPrice.setNextFocusDownId(txtnBarcode.getId());
                        txtnBarcode.setFocusable(true);
                        txtnBarcode.selectAll();
                    }
                }
                return false;
            }
        });
        txtdQuantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&  (keyCode == KeyEvent.KEYCODE_ENTER)){
                    if (txtnNumber.getVisibility() == View.VISIBLE){
                        //
                    }else {
                        FocusView();
                        txtdQuantity.setNextFocusDownId(txtnBarcode.getId());
                        txtnBarcode.setFocusable(true);
                        txtnBarcode.selectAll();
                    }
                }
                return false;
            }
        });
        txtnNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&  (keyCode == KeyEvent.KEYCODE_ENTER)){
                    if (btnAddPosition.getVisibility() == View.VISIBLE)  {
                        //Toast.makeText(getApplicationContext(), "txtnNumber.setOnKeyListener", Toast.LENGTH_LONG).show();
                        FocusView();
                        txtnNumber.setNextFocusDownId(txtnBarcode.getId());
                        txtnBarcode.setFocusable(true);
                        txtnBarcode.selectAll();

                    }
                }
                return false;
            }
        });
        txtnBarcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                   // Toast.makeText(getApplicationContext(), "txtnBarcode on focus", Toast.LENGTH_LONG).show();
                }else {

                }
            }
        });
        btnAddPosition.setOnKeyListener(new View.OnKeyListener() {


            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                       FocusView();
                        txtnBarcode.requestFocus();
                        txtnBarcode.setFocusable(true);
                        txtnBarcode.selectAll();
                return false;
            }
        });
// события price setOnFocusChangeListener
        txtdPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //Toast.makeText(getApplicationContext(), "txtdPrice on focus", Toast.LENGTH_LONG).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "txtdPrice lost focus", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnAddPosition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    txtnBarcode.requestFocus();
                    //Toast.makeText(getApplicationContext(), "btnAddPosition on focus", Toast.LENGTH_LONG).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "btnAddPosition lost focus", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void showKeyboard(TextView name) {
       // imm.toggleSoftInput(0, 0);
        /**
         * показываем программную клавиатуру
         */

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(name, 0);
            }

    }

    public void hideKeyboard() {
        imm.toggleSoftInput(0, 0);
    }

    void FocusView(){
        if (txtnBarcode.length() > 0) {
            writeFileSD();
        }else{
            ToastMessageCenter("Отсутствуют данные по штрихкоду. В файл не записано.");
            return;
        }
        if (txtnBarcode.length() > 0)
            txtnBarcode.getText().clear();
        if (txtnNumber.length() > 0 && txtnNumber.getVisibility() == View.VISIBLE)
            txtnNumber.getText().clear();
        if (txtdPrice.length() > 0 && txtdPrice.getVisibility() == View.VISIBLE)
            txtdPrice.getText().clear();
        if (txtdQuantity.length() > 0 && txtdQuantity.getVisibility() == View.VISIBLE)
            txtdQuantity.getText().clear();
        txtnBarcode.selectAll();
    }

    void writeFileSD() {// запись на SD диск  // подготавливаем переменные

        String txtBarcode = txtnBarcode.getText().toString();
        String txtNumber = "";
        String txtQuantity = "";
        String txtPrice = "";
        String textAdd = "";
        String line = "";
        Integer NumberOfRecords = 0;
        loadSetting();
        if (txtnNumber.length() > 0 && txtnNumber.getVisibility() == View.VISIBLE)
           txtNumber = txtnNumber.getText().toString();
        if (txtdQuantity.length() > 0 && txtdQuantity.getVisibility() == View.VISIBLE)
           txtQuantity = txtdQuantity.getText().toString();
        if (txtdPrice.length() > 0 && txtdPrice.getVisibility() == View.VISIBLE)
           txtPrice = txtdPrice.getText().toString();
        String text =  txtBarcode + ";" + txtPrice + ";" + txtQuantity + ";" + txtNumber + ";";
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();

        File[] elems = sdPath.listFiles();

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
        MediaScannerConnection.scanFile(ScanerActivity.this, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        //File sdFile_copy = new File(sdPath, FILENAME_SD_copy);

        // Проверка наличия файла
        if (sdFile.exists()){
            //ToastMessageCenter("Файл в наличии.");
            // проверка разрешений
            if (!myPremission())  return;
            String lineSeparator = System.getProperty("line.separator");
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
                btnAddPosition.setText("Добавить позицию (" + NumberOfRecords + ")");
        //        btnUploadDelete.setEnabled(true);
                if (!sModeWorking.equals("1")) {
                    btnSaveToServer.setEnabled(false);
                }else{
                    btnSaveToServer.setEnabled(true);
                }
                btnDeleteFile.setEnabled(true);

                //ToastMessageCenter( "Чтение");
                //if(sdFile.exists())
                  //sdFile.renameTo(sdFile); // переименовать файл
                    //copyFileUsingStream(sdFile, sdFile_copy);// копировать файл

            } catch (IOException e) {
                e.printStackTrace();
                ToastMessageCenter("Ошибка: Файл не открывается для чтения.");
                return;
            }
        }//else{ ToastMessageCenter("Файл отсутствует."); }
        try {
            // открываем поток для записи если файла нет
            //ToastMessageCenter("Запись");
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            textAdd = textAdd + text ;
            //bw.write(textAdd);

            bw.append(textAdd);
            // закрываем поток
            bw.close();
                   ++NumberOfRecords;
            //if(sdFile.exists())
                //sdFile.renameTo(sdFile); // переименовать файл
               //copyFileUsingStream(sdFile, sdFile);// копировать файл
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            //ToastMessageCenter("Данные сохранены на SD.+");
            btnAddPosition.setText("Добавить позицию (" + NumberOfRecords + ")");
       //     btnUploadDelete.setEnabled(true);
            if (!sModeWorking.equals("1")) {
                //1
                btnSaveToServer.setEnabled(false);
            }else{
                btnSaveToServer.setEnabled(true);
            }
            btnDeleteFile.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
            ToastMessageCenter("Ошибка: Файл невозможно открыть.");
            return;
        }
    }

    private void copyFileUsingStream(File source, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            if (!myPremission())  return;
            String lineSeparator = System.getProperty("line.separator");

            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //================================ копирование  ================================
    public void DeleteFile(View v) {

        showDialog(IDD_THREE_BUTTONS);
    }

    public void DeleteFilee(View v){
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        loadSetting();


        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();

        File[] elems = sdPath.listFiles();

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
        MediaScannerConnection.scanFile(ScanerActivity.this, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
        // формируем объект File, который содержит путь к файлу
        final File sdFile = new File(sdPath, FILENAME_SD);
//        final File sdFile_copy = new File(sdPath, FILENAME_SD_copy);

        if(sdFile.exists())
            //sdFile.renameTo(sdFile_copy);
            sdFile.delete();
        //if(sdFile_copy.exists())
        //sdFile.renameTo(sdFile_copy);
        //sdFile_copy.delete();

        if(sdFile.exists())
            ToastMessageCenter("Файл " + FILENAME_SD + " не удален!");
        else
            ToastMessageCenter("Файл " + FILENAME_SD + " удален!");
        //if(sdFile_copy.exists())
        //    ToastMessageCenter("Файл sdFile_copy не удален!");
        //else
        //    ToastMessageCenter("Файл sdFile_copy удален!");
        btnAddPosition.setText("Добавить позицию ");
       // btnUploadDelete.setEnabled(false);
        if (!sModeWorking.equals("1")){
            btnSaveToServer.setEnabled(false);
        }else{
            btnSaveToServer.setEnabled(false);
        }
        btnDeleteFile.setEnabled(false);
        txtLogScaner.setText("...");
    }
    public void SavDelFileOnClick(View v) {
        //============   одна кнопка
/*        AlertDialog.Builder builder = new AlertDialog.Builder(ScanerActivity.this);
        builder.setTitle("Важное сообщение!")
                .setMessage("Покормите кота!")
                .setCancelable(true)
                .setNegativeButton("ОК, иду на кухню",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();*/
        //ad.show();
        showDialog(IDD_THREE_BUTTONS);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
            return null;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();

        File[] elems = sdPath.listFiles();

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
        MediaScannerConnection.scanFile(ScanerActivity.this, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
        // формируем объект File, который содержит путь к файлу
        final File sdFile = new File(sdPath, FILENAME_SD);
//        final File sdFile_copy = new File(sdPath, FILENAME_SD_copy);
        loadSetting();
        switch (id) {
            case IDD_THREE_BUTTONS:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Выберите действие")
                        .setCancelable(false)
                        .setPositiveButton("Удалить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        if(sdFile.exists())
                                            //sdFile.renameTo(sdFile_copy);
                                            sdFile.delete();
                                        //if(sdFile_copy.exists())
                                            //sdFile.renameTo(sdFile_copy);
                                            //sdFile_copy.delete();

                                        if(sdFile.exists())
                                            ToastMessageCenter("Файл sdFile не удален!");
                                        else
                                            ToastMessageCenter("Файл sdFile удален!");
                                        //if(sdFile_copy.exists())
                                        //    ToastMessageCenter("Файл sdFile_copy не удален!");
                                        //else
                                        //    ToastMessageCenter("Файл sdFile_copy удален!");
                                        btnAddPosition.setText("Добавить позицию ");
           //                             btnUploadDelete.setEnabled(false);
                                        if (!sModeWorking.equals("1")){
                                            btnSaveToServer.setEnabled(false);
                                        }else{
                                            btnSaveToServer.setEnabled(false);
                                        }
                                        btnDeleteFile.setEnabled(false);
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                })
//                        .setNegativeButton("Выгрузить",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,
//                                                        int id) {
//                                        //if(sdFile_copy.exists())
//                                        //    copyFileUsingStream(sdFile_copy, sdFile);
//                                        //else
//                                        //    ToastMessageCenter("Данные не собраны!");
//                                        dialog.cancel();
//                                    }
//                                })
//
                                ;

                return builder.create();
            default:
                return null;
        }
    }
   private int readFileSD() {

       String line = "";
       Integer NumberOfRecords;
       loadSetting();
       // проверяем доступность SD
       if (!Environment.getExternalStorageState().equals(
               Environment.MEDIA_MOUNTED)) {
           Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
           ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
           return 0;
       }
       // получаем путь к SD
       File sdPath = Environment.getExternalStorageDirectory();
       // добавляем свой каталог к пути
       sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
       // создаем каталог
       //sdPath.mkdirs();
       // формируем объект File, который содержит путь к файлу
       File sdFile = new File(sdPath, FILENAME_SD);
       //File sdFile_copy = new File(sdPath, FILENAME_SD_copy);

       // Проверка наличия файла
       if (sdFile.exists()) {
           //ToastMessageCenter("Файл в наличии.");
           // проверка разрешений записи на карту памяти
           if (!myPremission())
               return 0;
           try {
               // открываем поток для чтения
               BufferedReader br = new BufferedReader(new FileReader(sdFile));
               // пишем данные
               //ToastMessageCenter("Файл открыт для чтения.");

               StringBuilder builder = new StringBuilder();
               NumberOfRecords = 0;
               while ((line = br.readLine()) != null) {
                   builder.append(line + "\n");
                   ++NumberOfRecords;
               }
               // закрываем поток
               br.close();
               Log.d(LOG_TAG, "Файл  на SD: " + sdFile.getAbsolutePath());
               btnAddPosition.setText("Добавить позицию (" + NumberOfRecords + ")");
               //if(sdFile.exists())
               // sdFile.renameTo(sdFile_copy); // переименовать файл
               //     copyFileUsingStream(sdFile, sdFile_copy);// копировать файл

               return NumberOfRecords;
           } catch (IOException e) {
               e.printStackTrace();

               if (!myPremission())
                   ToastMessageCenter("Ошибка: Файл не открывается для чтения.");
               else
                    return 0;//

               return 0;
           }
       }else{
      //     btnUploadDelete.setEnabled(false);
           btnSaveToServer.setEnabled(false);
           btnDeleteFile.setEnabled(false);
           if (!myPremission())
               return 0;
       }
       return 0;
   }
//=================  чтение - запись настроек  =========================
public void saveSetting(){
//    sPref = getSharedPreferences("setting", MODE_PRIVATE);
//    SharedPreferences.Editor ed = sPref.edit();
//
//    ed.putString(ADRESS_SERVER, txtAdressServer.getText().toString());
//    ed.putString(USER_NAME, txtUserFTP.getText().toString());
//    ed.putString(USER_PASSWORD, txtPasswordFTP.getText().toString());
//    ed.putString(PATH_FILE, txtPathFile.getText().toString());
//    ed.putString(PORT_FTP, txtPortFTP.getText().toString());
//    ed.commit();

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
//=================  чтение - запись настроек  =========================
    //===========================   проверка разрешений приложения  ================================
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
    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            makeFolder();
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();

                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }

    }
    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(this.findViewById(R.id.activity_scaner), "Storage permission isn't granted" , Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                openApplicationSettings();
                Toast.makeText(getApplicationContext(), "Open Permissions and grant the Storage permission",
                                Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            makeFolder();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    //========================  конец проверки разрешений  ==============================//
    public void ToastMessageCenter(String s){
        Toast toast = Toast.makeText(this, s , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER , 0, 0);
        toast.show();
    }


    public void ok(View v){
        if (txtnBarcode.length() > 0) {
            writeFileSD();
        }else{
            ToastMessageCenter("Отсутствуют данные по штрихкоду. В файл не записано.");
            return;
        }

        if (txtnBarcode.length() > 0)
            txtnBarcode.getText().clear();

        if (txtnNumber.length() > 0 && txtnNumber.getVisibility() == View.VISIBLE)
            txtnNumber.getText().clear();

        if (txtdPrice.length() > 0 && txtdPrice.getVisibility() == View.VISIBLE)
            txtdPrice.getText().clear();

        if (txtdQuantity.length() > 0 && txtdQuantity.getVisibility() == View.VISIBLE)
            txtdQuantity.getText().clear();
        txtnBarcode.setFocusable(true);
        txtnBarcode.selectAll();
        txtnBarcode.setCursorVisible(true);
        btnAddPosition.requestFocus();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

      showKeyboard(txtnBarcode);
        txtnBarcode.setFocusable(true);
        txtnBarcode.selectAll();
        txtnBarcode.setCursorVisible(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSaveToServer:
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    txtLogScaner.setText("SD-карта не доступна: " + Environment.getExternalStorageState());
                    // ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
                    return;
                }
                loadSetting();
                if (executeCommand(sAdressServer)){
                    try{
                        FTPModel mymodel = new FTPModel();


                        boolean co = mymodel.connect(sAdressServer,sUserFTP,sPasswordFTP,Integer.parseInt(sPortFTP));
                        if(co){
                            txtLogScaner.setText("ДАННЫЕ СОХРАНЕНЫ");
                        }else{
                            txtLogScaner.setText("ДАННЫЕ НЕ СОХРАНЕНЫ!");
                        }
                        // saveUrl(Environment.getExternalStorageDirectory() + "/Documents/Dat1.txt", "10.250.1.15/asd");
                    }
                    catch(Exception e){
                        txtLogScaner.setText("НЕТ СВЯЗИ С СЕРВЕРОМ");
                    }
                }
        }
    }
    ///////////////////////////////
    private boolean executeCommand(String ip){
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 "+ ip);
            int mExitValue = mIpAddrProcess.waitFor();
            txtLogScaner.setText(" mExitValue " + mExitValue);
            if(mExitValue==0){
                txtLogScaner.setText("ЕСТЬ СВЯЗЬ");
                return true;
            }else{
                txtLogScaner.setText("НЕТ СВЯЗИ");
                return false;
            }
        }
        catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
            txtLogScaner.setText(" Ошибка:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
            txtLogScaner.setText(" Ошибка:" + e);
        } return false;
    }
//////////////

    
}
