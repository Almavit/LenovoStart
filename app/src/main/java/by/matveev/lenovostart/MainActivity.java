package by.matveev.lenovostart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 123;

    Button btnFourField;
    Button btnTwoField;
    Button btnTwoFieldQuan;
    Button btnOneField;
    Button btnEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnFourField = (Button) findViewById(R.id.btnFourField);
        btnFourField.setOnClickListener(this);
        btnTwoField = (Button) findViewById(R.id.btnTwoField);
        btnTwoField.setOnClickListener(this);
        btnTwoFieldQuan = (Button) findViewById(R.id.btnTwoFieldQuan);
        btnTwoFieldQuan.setOnClickListener(this);

        btnOneField = (Button) findViewById(R.id.btnOneField);
        btnOneField.setOnClickListener(this);
        btnEditor = (Button) findViewById(R.id.btnTwoFieldNum);
        btnEditor.setOnClickListener(this);

    }


     public void onClick(View v) {
        Intent intent = new Intent(this, by.matfeev.lenovostart.ScanerActivity.class);
        //View.INVISIBLE = 4
        //View.VISIBLE = 0
        //View.GONE = 8

/*        intent.putExtra("VisibleTxtBarcode", View.VISIBLE);
        intent.putExtra("VisibleIntBarcode", View.VISIBLE);*/
        intent.putExtra("VisibleTxtQuantity", View.VISIBLE);
        intent.putExtra("VisibleIntQuantity", View.VISIBLE);
        intent.putExtra("VisibleTxtPrice", View.VISIBLE);
        intent.putExtra("VisibleIntPrice", View.VISIBLE);
        intent.putExtra("VisibleTxtNumber", View.VISIBLE);
        intent.putExtra("VisibleIntNumber", View.VISIBLE);


        switch (v.getId()){
            case R.id.btnFourField:
                Toast.makeText(MainActivity.this, getString(R.string.action_item1), Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnTwoField:
                intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                intent.putExtra("VisibleIntQuantity", View.INVISIBLE);
                intent.putExtra("VisibleTxtNumber", View.INVISIBLE);
                intent.putExtra("VisibleIntNumber", View.INVISIBLE);

                Toast.makeText(MainActivity.this, getString(R.string.action_item2), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnOneField:
                intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
                intent.putExtra("VisibleIntPrice", View.INVISIBLE);
                intent.putExtra("VisibleTxtNumber", View.INVISIBLE);
                intent.putExtra("VisibleIntNumber", View.INVISIBLE);
                intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                intent.putExtra("VisibleIntQuantity", View.INVISIBLE);


                Toast.makeText(MainActivity.this, getString(R.string.action_item3), Toast.LENGTH_SHORT).show();
                break;

                //btnTwoFieldQuan
            case R.id.btnTwoFieldQuan:
                intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
                intent.putExtra("VisibleIntPrice", View.INVISIBLE);
                intent.putExtra("VisibleTxtNumber", View.INVISIBLE);
                intent.putExtra("VisibleIntNumber", View.INVISIBLE);
                //intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                //intent.putExtra("VisibleIntQuantity", View.INVISIBLE);
                intent.putExtra("VisibleTxtNumber", View.INVISIBLE);
                intent.putExtra("VisibleIntNumber", View.INVISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.action_item2), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTwoFieldNum:
                intent.putExtra("VisibleTxtPrice", View.INVISIBLE);
                intent.putExtra("VisibleIntPrice", View.INVISIBLE);
                intent.putExtra("VisibleTxtQuantity", View.INVISIBLE);
                intent.putExtra("VisibleIntQuantity", View.INVISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.action_item5), Toast.LENGTH_SHORT).show();

                break;//return;
            default:
                break;

         }
         startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
           return true;
        }*/
        switch (id){
            case R.id.action_item1:
                Toast.makeText(MainActivity.this, getString(R.string.action_item1), Toast.LENGTH_LONG).show();
            case R.id.action_item2:
                Toast.makeText(MainActivity.this, getString(R.string.action_item2), Toast.LENGTH_LONG).show();
            case R.id.action_item3:
                Toast.makeText(MainActivity.this, getString(R.string.action_item3), Toast.LENGTH_LONG).show();
            case R.id.action_item4:
                Toast.makeText(MainActivity.this, getString(R.string.action_item4), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(MainActivity.this, "onStart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(MainActivity.this, "onPause", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(MainActivity.this, "onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(MainActivity.this, "onDestroy", Toast.LENGTH_LONG).show();
    }


}
