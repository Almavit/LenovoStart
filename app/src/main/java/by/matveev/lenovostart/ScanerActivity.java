package by.matveev.lenovostart;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBSampleHelper;
import by.matveev.lenovostart.lib.EAN13CodeBuilder;
import by.matveev.lenovostart.lib.FTPModel;
import by.matveev.lenovostart.lib.Filealmat;
import by.matveev.lenovostart.lib.Setting;


public class ScanerActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtnBarcode;
    EditText txtsQRcode;
    EditText txtdPrice;
    EditText txtdQuantity;
    EditText txtnNumber;
    TextView txtvBarcode;
    TextView txtvQRcode;
    TextView txtvPrice;
    TextView txtvQuantity;
    TextView txtvNumber;
    TextView txtLogScaner;

    TextView txtPriceRoz;
    TextView txtPriceOtp;

    Button btnAddPosition;
    //Button btnUploadDelete;
    Button btnDeleteFile;
    Button btnSaveToServer;
    Button btnEditDatTxt;

    private static final int MY_REQUEST_CODE = 123;

    private WifiManager wifiManager;

    Setting setting = new Setting();

    InputMethodManager imm;// для вывода клавиатуры
    // переменные для диалогового окна Выгрузить - Удалить
    AlertDialog.Builder ad;
    Context context;
    // идентификатор диалогового окна AlertDialog с кнопками
    private final int IDD_THREE_BUTTONS = 0;


    final String LOG_TAG = "myLogs";
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
        try {
            setting.loadSetting(this);  //загрузка настроек программы
        } catch (IOException e) {
            e.printStackTrace();
        }
        context = ScanerActivity.this;

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        txtnBarcode = (EditText) findViewById(R.id.txtnBarcode);
        txtvBarcode = (TextView) findViewById(R.id.txtvBarcode);

        txtvQRcode = (TextView) findViewById(R.id.txtvQRcode);
        txtsQRcode = (EditText) findViewById(R.id.txtsQRcode);

        txtvPrice = (TextView) findViewById(R.id.txtvPrice);
        txtdPrice = (EditText) findViewById(R.id.txtdPrice);
        txtvQuantity = (TextView) findViewById(R.id.txtvQuantity);
        txtdQuantity = (EditText) findViewById(R.id.txtdQuantity);
        txtvNumber = (TextView) findViewById(R.id.txtvNumber);
        txtnNumber = (EditText) findViewById(R.id.txtnNumber);

        txtLogScaner = (TextView) findViewById(R.id.txtLogScaner);

//        txtPriceOtp = (TextView) findViewById(R.id.txtPriceOtp);
//        txtPriceRoz = (TextView) findViewById(R.id.txtPriceRoz);

        btnAddPosition = (Button) findViewById(R.id.btnAddPosition);
        //       btnUploadDelete = (Button) findViewById(R.id.btnUploadDelete);

        btnDeleteFile = (Button) findViewById(R.id.btnDeleteFile);
        btnSaveToServer = (Button) findViewById(R.id.btnSaveToServer);
        btnSaveToServer.setOnClickListener(this);

        btnEditDatTxt = (Button) findViewById(R.id.btnEditDatTxt);
        btnEditDatTxt.setOnClickListener(this);


        hideKeyboard(txtnBarcode);

        Intent intent = getIntent();


        int visibletxtnBarcode = intent.getIntExtra("visibletxtnBarcode", View.VISIBLE);
        txtnBarcode.setVisibility(visibletxtnBarcode);
        int visibletxtvBarcode = intent.getIntExtra("visibletxtvBarcode", View.VISIBLE);
        txtvBarcode.setVisibility(visibletxtvBarcode);


        //  QRCode
        int VisibleTxtQRCode = intent.getIntExtra("VisibleTxtQRCodey", View.VISIBLE);
        txtvQRcode.setVisibility(VisibleTxtQRCode);
        int VisibleStrQRCode = intent.getIntExtra("VisibleStrQRCode", View.VISIBLE);
        txtsQRcode.setVisibility(VisibleStrQRCode);
        if (txtsQRcode.getVisibility() == View.INVISIBLE) {

            // 1. Просим EditText стать активным для ввода
            txtnBarcode.requestFocus();

            // 2. Выполняем действия чуть позже, после того как система реально даст фокус.
            txtnBarcode.post(() -> {
                // 3. Выделяем весь текст внутри EditText
                txtnBarcode.selectAll();

                // 4. Получаем доступ к системе ввода (клавиатура, IME)
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    // 5. Открываем клавиатуру и показываем её для нашего EditText
                    imm.showSoftInput(txtnBarcode, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }

        //txtIpConnection.setVisibility(VisibleTxtNumber);
// barcode visible
/*        int VisibleTxtBarcode = intent.getIntExtra("VisibleTxtBarcode",  View.VISIBLE);
        txtvBarcode.setVisibility(VisibleTxtBarcode);
        int VisibleIntBarcode = intent.getIntExtra("VisibleIntBarcode",  View.VISIBLE);
        txtnBarcode.setVisibility(VisibleIntBarcode);*/
// number visible
        int VisibleTxtNumber = intent.getIntExtra("VisibleTxtNumber", View.VISIBLE);
        txtvNumber.setVisibility(VisibleTxtNumber);
        int VisibleIntNumber = intent.getIntExtra("VisibleIntNumber", View.VISIBLE);
        txtnNumber.setVisibility(VisibleIntNumber);
// pricevisible
        int VisibleTxtPrice = intent.getIntExtra("VisibleTxtPrice", View.VISIBLE);
        txtvPrice.setVisibility(VisibleTxtPrice);
        int VisibleIntPrice = intent.getIntExtra("VisibleIntPrice", View.VISIBLE);
        txtdPrice.setVisibility(VisibleIntPrice);
//        int VisibleIntPriceRoz = intent.getIntExtra("VisibleIntPriceRoz",  View.VISIBLE);
//        txtdPrice.setVisibility(VisibleIntPriceRoz);
//        int VisibleIntPriceOtp = intent.getIntExtra("VisibleIntPriceOtp",  View.VISIBLE);
//        txtdPrice.setVisibility(VisibleIntPriceOtp);
// quantity visible
        int VisibleTxtQuantity = intent.getIntExtra("VisibleTxtQuantity", View.VISIBLE);
        txtvQuantity.setVisibility(VisibleTxtQuantity);
        int VisibleIntQuantity = intent.getIntExtra("VisibleIntQuantity", View.VISIBLE);
        txtdQuantity.setVisibility(VisibleIntQuantity);

// проверка наличия разрешения на чтение и редактирование файла
        try {
            writeFileSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // filealmat.writeFileSD(this,setting.sPathFile,setting.FileNameDat,);
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
        txtsQRcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Integer asdddd = event.getAction();
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String sQcode = txtsQRcode.getText().toString();
                    if(sQcode.length() == 0) return
                            false;

                    //////////
                    String barcode = extractEanFromQcode(sQcode);// определение и выделение штрихкода из qcode
                    try {
                        FocusView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 1. Просим EditText стать активным для ввода
                    txtsQRcode.requestFocus();

                    // 2. Выполняем действия чуть позже, после того как система реально даст фокус.
                    txtsQRcode.post(() -> {
                        // 3. Выделяем весь текст внутри EditText
                        txtsQRcode.selectAll();

                        // 4. Получаем доступ к системе ввода (клавиатура, IME)
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                        if (imm != null) {
                            // 5. Открываем клавиатуру и показываем её для нашего EditText
                            imm.showSoftInput(txtsQRcode, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
                    //////////  Неправильно. ( нужны знания как строится структура строки q кода. и на основе этого извлекать штрихкод. Штрихкод бывает EAN13 EAN8
//                    sQcode = txtsQRcode.getText().toString().replaceAll("\n", "");// очистить от символа \n
//                    sQcode = txtsQRcode.getText().toString().replaceAll("\u001D", "");// очистить от символа \u001D
//                    if (txtnBarcode.getVisibility() == View.VISIBLE) {
//                        txtnBarcode.setText(barcode);
//                        txtnBarcode.setFocusable(true);
//                        txtnBarcode.selectAll();
//                        //showKeyboard(txtnBarcode);
//                    }

                }

                return false;
            }
            }

        );


        txtnBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //ставить код для выборки из главного перечня
                    //txtQR.setText("");
                    //txtQR.setBackgroundColor(Color.WHITE);
                    String sqlStroka = txtnBarcode.getText().toString();
                    String sBarcode = txtnBarcode.getText().toString();
                    if(sBarcode.length() == 0) return
                            false;

                    sBarcode = txtnBarcode.getText().toString().replaceAll("\n", "");// очистить от символа \n
                    sBarcode = txtnBarcode.getText().toString().replaceAll("\u001D", "");// очистить от символа \u001D

                    if (sBarcode.length() < 8 )return
                        false;
                    String ddd = sBarcode.substring(0, 2);
                    if (ddd.equals("22")) {
                        ddd = sBarcode.substring(0,7) + "00000";
                        EAN13CodeBuilder bb = new EAN13CodeBuilder(ddd);
                        sBarcode = bb.getCode().toString();
                    }
                    txtnBarcode.setText(sBarcode);
                    if(sBarcode.length() < 13){
                        Integer lengthSqlStroka = sBarcode.length();
                        for (int x = 0; x <= (12 - lengthSqlStroka); x++){
                            sBarcode = "0" + sBarcode;
                        }
                    }

                    //setTitle(sqlStroka);
                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DBHelper.DATABASE_NAME, null, null);
                    //String[] columnsName = new String[]{KEY_PRICE, KEY_BARCODE,KEY_NAME_TOV,KEY_QUANTITY};
                   // sqlStroka = txtnBarcode.getText().toString().replaceAll("\n", "");// очистить от символа \n
                   // sqlStroka = txtnBarcode.getText().toString().replaceAll("\u001D", "");// очистить от символа \u001D
                    sqlStroka = "select * from " + DBSampleHelper.DBPrice.TABLE_DOCUMENT_PRICE + " where " + DBSampleHelper.DBPrice.PRICE_BARCODE + " = '" + sBarcode + "'";// создать строку SQL запроса
                    Cursor cursor = db.rawQuery(sqlStroka, null);


                    // DBHelper
                    Integer iCountursor = cursor.getCount();
                    db.close();
                    if ((cursor != null) && (cursor.getCount() > 0)) {
                        cursor.moveToFirst();
                        String sTextView = "";
                        String sPriceOtp = "";
                        String sNameTovar = "";
                        String sDateTovar = "";
                        String sPrice = "";
                        //sDateTovar = cursor.getString(0);
                        sNameTovar = cursor.getString(1);
                        sPriceOtp = cursor.getString(2);//PricePall.setText(cursor.getString(8));
                        sPrice = cursor.getString(3);
                        sDateTovar = cursor.getString(4);//PricePall.setText(cursor.getString(8));
                        //         sTextView = cursor.getString(5);
                        //setTitle(sNameTovar);
//                    txtPriceRoz.setText(sPrice);
//                    txtPriceOtp.setText(sPriceOtp);

                        sTextView = sDateTovar + "  ||  " + sNameTovar;//"ЦЕНЫ:   " + sPriceOtp + "   |   " + cursor.getString(3) + "   |        "   + sDateTovar; //+ "ДАТА ""\n"
                        sTextView = sTextView.substring(0,42);
                        setTitle(sBarcode);// + "  |  " + sNameTovar);
                        txtvBarcode.setText(sTextView);
                        txtvBarcode.setBackgroundColor(Color.GREEN);
                        //float sizeText = txtvPrice.getTextSize();
                        if (txtvPrice.getVisibility() == View.INVISIBLE) {

                            txtvPrice.setVisibility(View.VISIBLE);
                        }
                        txtvPrice.setTextSize(70);
                        txtvPrice.setGravity(Gravity.CENTER);
                        txtvPrice.setBackgroundColor(Color.GREEN);
                        txtvPrice.setText(cursor.getString(3) + " руб.");

//            музыка
//                    try {
//                        Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notify);
//                        r.play();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    } else {
                        txtvBarcode.setText("Штрих-код");
                        txtvBarcode.setBackgroundColor(Color.WHITE);
                        setTitle("Сканирование");
                        txtLogScaner.setText("...");
                        txtvPrice.setBackgroundColor(Color.WHITE);
                        txtvPrice.setText("Цена");
                        txtvPrice.setGravity(Gravity.LEFT);
                        txtvPrice.setTextSize(22);
                    }
                    db.close();
                    if (txtdPrice.getVisibility() == View.VISIBLE) {
                        txtdPrice.setFocusable(true);
                        txtdPrice.selectAll();
                        showKeyboard(txtdPrice);
                    } else {
                        if (txtnNumber.getVisibility() == View.VISIBLE) {
                            txtnNumber.setFocusable(true);
                            txtnNumber.selectAll();
                            showKeyboard(txtnNumber);
                        } else {
                            if (txtdQuantity.getVisibility() == View.VISIBLE) {
                                txtdQuantity.setFocusable(true);
                                txtdQuantity.selectAll();
                                showKeyboard(txtdQuantity);
                            } else {
                                try {
                                    FocusView();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (txtdQuantity.getVisibility() == View.VISIBLE) {
                        //
                    } else {
                        try {
                            FocusView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (txtnNumber.getVisibility() == View.VISIBLE) {
                        //
                    } else {
                        try {
                            FocusView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (btnAddPosition.getVisibility() == View.VISIBLE) {
                        //Toast.makeText(getApplicationContext(), "txtnNumber.setOnKeyListener", Toast.LENGTH_LONG).show();
                        try {
                            FocusView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                if (hasFocus) {
                    // Toast.makeText(getApplicationContext(), "txtnBarcode on focus", Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });
        btnAddPosition.setOnKeyListener(new View.OnKeyListener() {


            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                try {
                    FocusView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                if (hasFocus) {
                    //Toast.makeText(getApplicationContext(), "txtdPrice on focus", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "txtdPrice lost focus", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnAddPosition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtnBarcode.requestFocus();
                    //Toast.makeText(getApplicationContext(), "btnAddPosition on focus", Toast.LENGTH_LONG).show();
                } else {
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

    public void hideKeyboard(EditText editText) {
        InputMethodManager imms = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imms.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        //imm.toggleSoftInput(0, 0);
    }

    void FocusView() throws IOException {
        if (txtnBarcode.length() > 0 && txtnBarcode.getVisibility() == View.VISIBLE){
            writeFileSD();
        } else {
            if (txtsQRcode.length() > 0 && txtsQRcode.getVisibility() == View.VISIBLE){
                writeFileSD();
            } else {
                ToastMessageCenter("Отсутствуют данные по Q-коду. В файл не записано.");
                return;
            }
            ToastMessageCenter("Отсутствуют данные по штрихкоду. В файл не записано.");
            return;
        }

        if (txtsQRcode.length() > 0 && txtsQRcode.getVisibility() == View.VISIBLE)
            txtsQRcode.getText().clear();
        if (txtnBarcode.length() > 0 && txtnBarcode.getVisibility() == View.VISIBLE)
            txtnBarcode.getText().clear();

        if (txtnNumber.length() > 0 && txtnNumber.getVisibility() == View.VISIBLE)
            txtnNumber.getText().clear();
        if (txtdPrice.length() > 0 && txtdPrice.getVisibility() == View.VISIBLE)
            txtdPrice.getText().clear();
        if (txtdQuantity.length() > 0 && txtdQuantity.getVisibility() == View.VISIBLE)
            txtdQuantity.getText().clear();
        if (txtvPrice.length() > 0 && txtvPrice.getVisibility() == View.VISIBLE){

        }

        txtnBarcode.selectAll();
    }

    void writeFileSD() throws IOException {// запись на SD диск  // подготавливаем переменные

        String txtQR = "";
//        String txtBarcode = txtnBarcode.getText().toString();
        String txtBarcode = "";
        String txtNumber = "";
        String txtQuantity = "";
        String txtPrice = "";
        StringBuilder addText = new StringBuilder();


        setting.loadSetting(this);
        if (txtnBarcode.length() > 0 && txtnBarcode.getVisibility() == View.VISIBLE)
            txtBarcode = txtnBarcode.getText().toString();
        if (txtsQRcode.length() > 0 && txtsQRcode.getVisibility() == View.VISIBLE)
            txtNumber = txtsQRcode.getText().toString();
        if (txtnNumber.length() > 0 && txtnNumber.getVisibility() == View.VISIBLE)
            txtNumber = txtnNumber.getText().toString();
        if (txtdQuantity.length() > 0 && txtdQuantity.getVisibility() == View.VISIBLE)
            txtQuantity = txtdQuantity.getText().toString();
        if (txtdPrice.length() > 0 && txtdPrice.getVisibility() == View.VISIBLE)
            txtPrice = txtdPrice.getText().toString();
        String text = txtBarcode + ";" + txtPrice + ";" + txtQuantity + ";" + txtNumber + ";" + txtQR + ";";
        if (text.length() <= 5) {
            text = "";
        }

        addText.insert(0, text);
        Filealmat filealmat = new Filealmat();
        if (filealmat.writeFileSD(this, setting.sPathFile, setting.FileNameDat, addText) != 0) {
            return;
        } else {
            btnAddPosition.setText("Добавить позицию (" + filealmat.NumberOfRecords + ")");
            if (!setting.sModeWorking.equals("1")) {
                btnSaveToServer.setEnabled(false);
            } else {
                btnSaveToServer.setEnabled(true);
            }
            btnDeleteFile.setEnabled(true);

        }
    }

//================================================================

    private void copyFileUsingStream(File source, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            if (!myPremission()) return;
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

    public void DeleteFilee(View v) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        try {
            setting.loadSetting(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + setting.sPathFile);
        // создаем каталог
        sdPath.mkdirs();

        File[] elems = sdPath.listFiles();

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
        MediaScannerConnection.scanFile(ScanerActivity.this, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
        // формируем объект File, который содержит путь к файлу
        final File sdFile = new File(sdPath, setting.FileNameSetting);
//        final File sdFile_copy = new File(sdPath, FILENAME_SD_copy);

        if (sdFile.exists())
            //sdFile.renameTo(sdFile_copy);
            sdFile.delete();
        //if(sdFile_copy.exists())
        //sdFile.renameTo(sdFile_copy);
        //sdFile_copy.delete();

        if (sdFile.exists())
            ToastMessageCenter("Файл " + setting.FileNameSetting + " не удален!");
        else
            ToastMessageCenter("Файл " + setting.FileNameSetting + " удален!");
        //if(sdFile_copy.exists())
        //    ToastMessageCenter("Файл sdFile_copy не удален!");
        //else
        //    ToastMessageCenter("Файл sdFile_copy удален!");
        btnAddPosition.setText("Добавить позицию ");
        // btnUploadDelete.setEnabled(false);
        if (!setting.sModeWorking.equals("1")) {
            btnSaveToServer.setEnabled(false);
        } else {
            btnSaveToServer.setEnabled(false);
        }
        btnDeleteFile.setEnabled(false);
        txtLogScaner.setText("...");
        setTitle("Сканирование");
        txtvBarcode.setText("Штрих-код");
        txtvBarcode.setBackgroundColor(Color.WHITE);
        txtvPrice.setBackgroundColor(Color.WHITE);
        txtvPrice.setText("");
        txtvPrice.setTextSize(22);
        txtvPrice.setGravity(Gravity.LEFT);

    }
//    public void SavDelFileOnClick(View v) {
//
//        showDialog(IDD_THREE_BUTTONS);
//    }

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
        sdPath = new File(sdPath.getAbsolutePath() + "/" + setting.sPathFile);
        // создаем каталог
        sdPath.mkdirs();

        File[] elems = sdPath.listFiles();

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
        MediaScannerConnection.scanFile(ScanerActivity.this, paths, null, null);//заставляем повторно сканировать пути - после этого они должны отобразится на компьютере
        // формируем объект File, который содержит путь к файлу
        final File sdFile = new File(sdPath, setting.FileNameDat);
//        final File sdFile_copy = new File(sdPath, FILENAME_SD_copy);
        try {
            setting.loadSetting(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (id) {

            case IDD_THREE_BUTTONS:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Выберите действие")
                        .setCancelable(false)
                        .setPositiveButton("Удалить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        if (sdFile.exists())
                                            sdFile.delete();
                                        if (sdFile.exists())
                                            ToastMessageCenter("Файл " + setting.FileNameDat + " не удален!");
                                        else
                                            ToastMessageCenter("Файл " + setting.FileNameDat + " удален!");
                                        btnAddPosition.setText("Добавить позицию ");
                                        if (!setting.sModeWorking.equals("1")) {
                                            btnSaveToServer.setEnabled(false);
                                        } else {
                                            btnSaveToServer.setEnabled(false);
                                        }
                                        btnDeleteFile.setEnabled(false);
                                        txtLogScaner.setText("...");
                                        setTitle("Сканирование");
                                        txtvBarcode.setText("Штрих-код");
                                        txtvBarcode.setBackgroundColor(Color.WHITE);
                                        txtvPrice.setBackgroundColor(Color.WHITE);
                                        txtvPrice.setText("");
                                        txtvPrice.setTextSize(22);
                                        txtvPrice.setGravity(Gravity.LEFT);
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder.create();
            default:
                return null;
        }
    }

    //===========================   проверка разрешений приложения  ================================
    private boolean myPremission() {
        if (hasPermissions()) {
            // our app has permissions.
            makeFolder();
        } else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale();
        }
        return true;
    }

    private void makeFolder() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "fandroid");

        if (!file.exists()) {
            Boolean ff = file.mkdir();
            if (ff) {
                Toast.makeText(this, "Folder created successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_LONG).show();
            }

        } else {
            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
        }
    }

    private boolean hasPermissions() {
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permissions) {
            /*
             * с помощью метода checkCallingOrSelfPermission в цикле проверяет
             * предоставленные приложению разрешения и сравнивает их с тем, которое нам необходимо.
             * При отсутствии разрешения метод будет возвращать false, а при наличии разрешения — true.
             */
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }

        return true;
    }

    private void requestPerms() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:

                for (int res : grantResults) {
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed) {
            //user granted all permissions we can perform our task.
            makeFolder();
        } else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();

                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }

    }

    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(this.findViewById(R.id.activity_scaner), "Storage permission isn't granted", Snackbar.LENGTH_LONG)
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
            requestPerms();
        } else {
            requestPerms();
        }
    }

    //========================  конец проверки разрешений  ==============================//
    public void ToastMessageCenter(String s) {
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public void ok(View v) throws IOException {
        if (txtnBarcode.length() > 0) {
//            txtnBarcode.event
            writeFileSD();
        } else {
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
        try {
            writeFileSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        showKeyboard(txtnBarcode);

        txtnBarcode.setFocusable(true);
        txtnBarcode.selectAll();
        txtnBarcode.setCursorVisible(true);
        hideKeyboard(txtnBarcode);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtnBarcode.getWindowToken(), 0);
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
    //включить wifi
    public void enableWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Integer iState = wifiManager.getWifiState();
            wifiManager.setWifiEnabled(true);
            iState = wifiManager.getWifiState();
            Toast toast = Toast.makeText(getApplicationContext(), "Wifi включен", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public void onClick(View v) {
       // enableWifi();
        try {
            if (setting.loadSetting(this)){
                if(!setting.executeCommand(setting.sAdressServer)) {
                    enableWifi();
                    if(!setting.executeCommand(setting.sAdressServer)) {
                        txtLogScaner.setText("НЕТ СВЯЗИ С СЕРВЕРОМ!");
                        txtLogScaner.setBackgroundColor(Color.RED);
                        return;
                    }
                }else{
                    txtLogScaner.setText("       ...       ");
                    txtLogScaner.setBackgroundColor(Color.WHITE);
                }
            }else {
                txtLogScaner.setText("НАСТРОЙКИ НЕ ЗАГРУЖЕНЫ!");
                txtLogScaner.setBackgroundColor(Color.RED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (v.getId()) {
            case R.id.btnEditDatTxt:
                Intent intentEditDatTxt = new Intent(this, EditData.class);
                startActivity(intentEditDatTxt);

                break;
            case R.id.btnSaveToServer:
                txtLogScaner.setText("...");
                setTitle("Сканирование");
                txtvBarcode.setText("Штрих-код");
                txtvBarcode.setBackgroundColor(Color.WHITE);
                txtvPrice.setBackgroundColor(Color.WHITE);
                txtvPrice.setText("");
                txtvPrice.setTextSize(22);
                txtvPrice.setGravity(Gravity.LEFT);

                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    txtLogScaner.setText("SD-карта не доступна: " + Environment.getExternalStorageState());
                    // ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
                    return;
                }
                 if (setting.executeCommand(setting.sAdressServer)) {
                    try {
                        setting.loadSetting(this);//11
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        FTPModel mymodel = new FTPModel(this);
                        boolean co = mymodel.connect(setting.sAdressServer, setting.sUserFTP, setting.sPasswordFTP, Integer.parseInt(setting.sPortFTP));
                        if (co) {
                            txtLogScaner.setText("ДАННЫЕ СОХРАНЕНЫ");
                            setTitle("ДАННЫЕ СОХРАНЕНЫ");
                        } else {
                            txtLogScaner.setText("FTP СВЯЗЬ ОТСУТСТВУЕТ!");
                            setTitle("FTP СВЯЗЬ ОТСУТСТВУЕТ!");
                        }
                        // saveUrl(Environment.getExternalStorageDirectory() + "/Documents/Dat1.txt", "10.250.1.15/asd");
                    } catch (Exception e) {
                        txtLogScaner.setText("НЕТ СВЯЗИ С СЕРВЕРОМ");
                    }
                }else{
                     txtLogScaner.setText("НЕТ СВЯЗИ С СЕРВЕРОМ");
                 }
        }
    }
    ///////////////////////////////


    /**
     * Извлекает EAN (13 или 8) из строки qcode (поддерживает ASCII GS \u001D и варианты с/без скобок).
     * Возвращает строку EAN (8 или 13 символов) или null, если не найдено.
     */
    public static String extractEanFromQcode(String qcode) {
        if (qcode == null) return null;

        // Нормализуем: убираем скобки, оставляем разделитель \u001D (если он есть)
        String s = qcode.replace("(", "").replace(")", "");

        // 1) Ищем AI 01 + 14 цифр (GTIN-14)
        Pattern p01 = Pattern.compile("01(\\d{14})");
        Matcher m01 = p01.matcher(s);
        if (m01.find()) {
            String gtin14 = m01.group(1);
            String ean = gtin14ToEan(gtin14);
            if (ean != null) return ean;
        }

        // 2) Если AI 01 не найден — как резерв: ищем standalone 13- или 8-значные числа,
        //    но убедимся, что они не часть более длинной цифровой строки
        Matcher m13 = Pattern.compile("(?<!\\d)(\\d{13})(?!\\d)").matcher(s);
        if (m13.find()) return m13.group(1);

        Matcher m8 = Pattern.compile("(?<!\\d)(\\d{8})(?!\\d)").matcher(s);
        if (m8.find()) return m8.group(1);

        return null;
    }

    /** Конвертирует GTIN-14 в EAN-13 или EAN-8 при наличии ведущих нулей. */
    private static String gtin14ToEan(String gtin14) {
        if (gtin14 == null || gtin14.length() != 14) return null;
        // убираем ведущие нули
        String trimmed = gtin14.replaceFirst("^0+", "");
        if (trimmed.length() == 13 || trimmed.length() == 8) {
            // валидируем контрольную цифру — если не прошёл, всё равно возвращаем, но можно отклонить
            if (isValidEan(trimmed)) return trimmed;
            // если контрольная цифра не прошла, всё равно возвращаем (по необходимости можно вернуть null)
            return trimmed;
        }
        // как запасной вариант: вернуть правые 13 цифр (обычная практика для GTIN->EAN13)
        return gtin14.substring(1);
    }

    /** Проверка контрольной цифры EAN-8 / EAN-13 */
    public static boolean isValidEan(String ean) {
        if (ean == null) return false;
        int len = ean.length();
        if (!(len == 8 || len == 13)) return false;
        int sum = 0;
        for (int i = 0; i < len - 1; i++) {
            int d = ean.charAt(i) - '0';
            int weight = (i % 2 == 0) ? 1 : 3; // слева: 1,3,1,3...
            sum += d * weight;
        }
        int check = (10 - (sum % 10)) % 10;
        int last = ean.charAt(len - 1) - '0';
        return check == last;
    }

}
