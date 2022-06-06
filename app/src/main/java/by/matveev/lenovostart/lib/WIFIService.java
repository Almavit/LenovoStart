package by.matveev.lenovostart.lib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class WIFIService extends Service {
    public WIFIService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.//Not yet implemented
        throw new UnsupportedOperationException("Еще не реализовано");
    }


    @Override
    public void onCreate() {//The new Service was Created
        Toast.makeText(this, "Сервис создан", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart(Intent intent, int startId) {//Service Started
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Сервис запущен", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDestroy() {//Service Destroyed
        Toast.makeText(this, "Сервис выключен", Toast.LENGTH_LONG).show();

    }

}