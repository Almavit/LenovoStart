package by.matveev.lenovostart.lib;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;

public class MyPremission {

    private static final int PERMISSION_REQUEST_CODE = 123;


    public boolean myPremission(Activity activity){
        if (hasPermissions(activity)){
            // our app has permissions.
            makeFolder(activity);
        }
        else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale(activity);
        }
        return true;
    }
    private boolean hasPermissions(Context context){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permissions){
            /*
             * с помощью метода checkCallingOrSelfPermission в цикле проверяет
             * предоставленные приложению разрешения и сравнивает их с тем, которое нам необходимо.
             * При отсутствии разрешения метод будет возвращать false, а при наличии разрешения — true.
             */
            res = checkCallingOrSelfPermission(context, perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }

        return true;
    }

//    private int checkCallingOrSelfPermission(String perms) {
//        return 0;
//    }


    private void makeFolder(Activity activity){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fandroid");

        if (!file.exists()){
            Boolean ff = file.mkdir();
            if (ff){
                Toast.makeText(activity, "Folder created successfully", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(activity, "Failed to create folder", Toast.LENGTH_LONG).show();
            }

        }
        else {
            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
        }
    }
    private void requestPermissionWithRationale(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPerms(activity);
        } else {
            requestPerms(activity);
        }
    }
    private void requestPerms(Activity activity){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
        }
    }

//    private void requestPermissions(String[] permissions, int permissionRequestCode) {
//    }


}
