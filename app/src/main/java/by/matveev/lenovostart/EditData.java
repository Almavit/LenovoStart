package by.matveev.lenovostart;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import by.matveev.lenovostart.lib.DBHelper;
import by.matveev.lenovostart.lib.DBRepository;

public class EditData extends AppCompatActivity implements View.OnClickListener {

    Button btnEditDat;
    Button btnSaveDat;
    Button btnDeleDat;
    Button btnDonloadDat;

    TextView txtStroka;

    DBHelper dbHelper;

    String[] nextLine;
    Integer iCountField;
    Integer iCountStrok;

    String datFileString;
    Cursor datBaseCursor;

    ListView dbListView;
//
//    TextView txtTov;


//    InputStream inputStream;
//    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//    String csvLine;

    SQLiteDatabase database;

    final String FILENAME_DAT_TXT = "Dat1.txt";
    final String DIR_SD = "Documents";
    private static final String ENCODING_WIN1251 = "windows-1251";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_data);

     //   txtTov  = (TextView) findViewById(R.id.txtTov);


        txtStroka = (TextView) findViewById(R.id.txtStroka);

        btnEditDat = (Button) findViewById(R.id.btnEditDat);
        btnEditDat.setOnClickListener(this);

        btnSaveDat = (Button) findViewById(R.id.btnSaveDat);
        btnSaveDat.setOnClickListener(this);

        btnDeleDat = (Button) findViewById(R.id.btnDeleDat);
        btnDeleDat.setOnClickListener(this);

        btnDonloadDat = (Button) findViewById(R.id.btnDonloadDat);
        btnDonloadDat.setOnClickListener(this);


   //     dbGridEditDat = (GridView) findViewById(R.id.dbGridEditDat);

        dbListView = (ListView) findViewById(R.id.dbListView);


       // Intent intent = new Intent(this, electron_document.class);



        AdapterView.OnItemClickListener datitemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String fgsdfsdfs = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(),"Вы выбрали "
//                                + parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
//                parent.setSelection(position);
//                List resultList = new ArrayList();
//                while (true){
//                    try {
//                        if (!((csvLine = reader.readLine()) != null)) break;
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                    String[] row = csvLine.split(";");
//                    resultList.add(row);
//                }
               // txtID_.setText(resultList[0].);
            //Intent intent = new Intent(this, electron_document.class);

//                intent.putExtra("VisibletxtNumNakladn", parent.getItemAtPosition(position).toString());
//                startActivity(intent);

                //               ssssss(parent.getItemAtPosition(position).toString());
                //startActivity(intent);
                txtStroka.setText(fgsdfsdfs);

            }
        };

        dbListView.setOnItemClickListener(datitemListener);




        dbHelper = new DBHelper(this);

        try {
// OPTION 1: if the file is in the sd
            dbHelper.createDataBase();
            try {
                dbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }

            File csvfile = new File(Environment.getExternalStorageDirectory()+ "/" +
                    DIR_SD + "/" + FILENAME_DAT_TXT);
// END OF OPTION 1


            dbHelper = new DBHelper(this);

            ContentValues datContentValues = new ContentValues();

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //Environment.getExternalStorageDirectory()
// OPTION 2: pack the file with the app
            /* "If you want to package the .csv file with the application and have it install on the internal storage when the app installs, create an assets folder in your project src/main folder (e.g., c:\myapp\app\src\main\assets\), and put the .csv file in there, then reference it like this in your activity:" (from the cited answer) */
            /* "Если вы хотите упаковать файл .csv вместе с приложением и установить его во внутреннее хранилище при установке приложения, создайте папку assets в вашей папке project src/main (например, c:\myapp\app\src\main\assets \), и поместите туда файл .csv, а затем ссылайтесь на него следующим образом в вашей деятельности:" (из процитированного ответа) */
            datFileString = this.getApplicationInfo().dataDir + File.pathSeparatorChar  +
                    FILENAME_DAT_TXT;
            File csvfiles = new File(datFileString);
// END OF OPTION 2
            CSVReader reader = new CSVReader(
                    new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                    ';', '\'', 0);
            String sStrok = reader.toString();
            // считываем данные с БД
            database = dbHelper.getWritableDatabase();
            datBaseCursor = database.rawQuery("select * from " + dbHelper.TABLE_DOCUMENT_DAT,null);
            // определяем, какие столбцы из курсора будут выводиться в ListView
            iCountStrok = datBaseCursor.getCount(); //количество строк
            iCountStrok = 0;
            while ((nextLine = reader.readNext()) != null) {
                iCountStrok++;
            }
            reader = new CSVReader(
                    new InputStreamReader(new FileInputStream(csvfile.getAbsolutePath()), ENCODING_WIN1251),
                    ';', '\'', 0);
            //progressTextView.setMaxValue(iCountStrok);
            iCountField = datBaseCursor.getColumnCount();//количество полей
            datBaseCursor.moveToFirst();// установка курсора в начало
            iCountStrok = 0;
            //чистим БД сканированного
            datFileString = "delete  from " + DBHelper.TABLE_DOCUMENT_DAT ;
            int iDelete = database.delete("Dat",null,null);
//            datBaseCursor = database.rawQuery(datFileString,null);
//            iCountField = datBaseCursor.getCount();//количество полей
//            database.close();
//            dbHelper.close();
//            database = dbHelper.getWritableDatabase();
//            database = SQLiteDatabase.openOrCreateDatabase(DBHelper.DATABASE_NAME, null, null);

            datBaseCursor = database.query(dbHelper.TABLE_DOCUMENT_DAT, null, null, null, null, null, null);
            iCountField = datBaseCursor.getCount();//количество полей
            datBaseCursor.moveToFirst();// установка курсора в начало
            while ((nextLine = reader.readNext()) != null) {// считываем данные с Dat1.txt  файла
                iCountStrok++;
                datFileString = nextLine[0].toString();
//
//                datFileString = "select * from " + DBHelper.TABLE_DOCUMENT_DAT +
//                        " where " + DBHelper.DAT_KEY_BARCODE + " = '" + nextLine[0].toString()  + "'";
//                datBaseCursor = database.rawQuery(datFileString,null);


                datContentValues.put(DBHelper.DAT_KEY_ID, Integer.toString(iCountStrok));
                datContentValues.put(DBHelper.DAT_KEY_BARCODE, nextLine[0]);
                datContentValues.put(DBHelper.DAT_KEY_POSITION, nextLine[1]);
                datContentValues.put(DBHelper.DAT_KEY_QUANTITY, nextLine[2]);
                datContentValues.put(DBHelper.DAT_KEY_PRICE, nextLine[3]);
                database.insert(DBHelper.TABLE_DOCUMENT_DAT, null, datContentValues);

            }//end while
            database.close();

        } catch (Exception e) {
            database.close();
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
               // txtLogMessege.setText("");
        }


//        Intent datintent = new Intent(this, EditData.class);
//
//        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Toast.makeText(getApplicationContext(),"Вы выбрали  " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
//                //Intent datintent = new Intent(this, EditData.class);
//
//               // datintent.putExtra("VisibletxtNumNakladn", parent.getItemAtPosition(position).toString());
//              //  startActivity(datintent);
//
//                //               ssssss(parent.getItemAtPosition(position).toString());
//                //startActivity(intent);
//
//            }
//        };
//
//        dbGridEditDat.setOnItemClickListener(itemListener);

//        try{
//
//            ArrayAdapter<String>  adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_dat, repositorys.getDataDat());
//            adapter.setDropDownViewResource(R.layout.simple_list_item_dat);
//            dbGridEditDat.setAdapter(adapter);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
//        }

    }
///////////////////////////

    @Override
    public void onClick(View v) {
        final DBRepository repositorys = new DBRepository(getApplicationContext());
        switch (v.getId()) {
            case R.id.btnDonloadDat:

                ArrayAdapter<String> datadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, repositorys.getDataDat());

//                String sksksks0 = datadapter.getItem(0);
//                String sksksks1 = datadapter.getItem(1);
//                String sksksks2 = datadapter.getItem(2);
//                String sksksks3 = datadapter.getItem(3);
//                String sksksks4 = datadapter.getItem(4);
//                String sksksks5 = datadapter.getItem(5);
//                String sksksks6 = datadapter.getItem(6);
//                String sksksks7 = datadapter.getItem(7);
//                Integer iSSSsss = datadapter.getCount();
//                SimpleAdapter adapter = new SimpleAdapter(this,
//                        repositorys.getDataDat(),
//                        R.layout.simple_list_item_dat,
//                        new String[]{"ID", "BARCODE", "NUMBER", "QUANTITY", "PRICE"},
//                        new int[]{R.id.text_view_id, R.id.text_view_barcode,
//                                R.id.text_view_number, R.id.text_view_quantity,
//                                R.id.text_view_price});

              datadapter.setDropDownViewResource(R.layout.simple_list_item_dat);
                //dbGridEditDat.setAdapter(datadapter);
                dbListView.setAdapter(datadapter);

                //dbTableRow.setAdapter(datadapter);

//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
//                }
                break;

        }
        //
    }

}