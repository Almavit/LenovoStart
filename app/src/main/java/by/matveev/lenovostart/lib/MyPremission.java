package by.matveev.lenovostart.lib;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;

public class MyPremission {

    private static final int PERMISSION_REQUEST_CODE = 123;


    public boolean myPremission(Activity activity){
        int CodError;
        if (hasPermissions(activity)){
            // our app has permissions.
            Filealmat makefolder = new Filealmat();
            makefolder.makeFolder(activity,"");
        }
        else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale(activity);
        }

        if (-1 != ExternalStorageState(activity)){
            return false;
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

//    private void makeFolder(Activity activity){
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fandroid");
//
//        if (!file.exists()){
//            Boolean ff = file.mkdir();
//            if (ff){
//                Toast.makeText(activity, "Folder created successfully", Toast.LENGTH_LONG).show();
//            }
//            else {
//                Toast.makeText(activity, "Failed to create folder", Toast.LENGTH_LONG).show();
//            }
//
//        }
//        else {
//            // Toast.makeText(this, "Folder already exist", Toast.LENGTH_LONG).show();//Папка уже существует
//        }
//    }
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
            requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);//вывод сообщения
        }
    }

    public int ExternalStorageState(Activity activity){
        //        // проверяем доступность SD
 //       while (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                //Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
                Toast.makeText(activity, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
                return -2;
            }
//        }
        return 0;
    }


    /////////////////////////////////////

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        boolean allowed = true;
//
//        switch (requestCode){
//            case PERMISSION_REQUEST_CODE:
//
//                for (int res : grantResults){
//                    // if user granted all permissions.
//                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
//                }
//                break;
//            default:
//                // if user not granted permissions.
//                allowed = false;
//                break;
//        }
//        if (allowed){
//            //user granted all permissions we can perform our task.
//
//           /////// filealmat.makeFolder(this);
//        }
//        else {
//            // we will give warning to user that they haven't granted permissions.
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
////                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
////
////                } else {
////                    showNoStoragePermissionSnackbar();
////                }
////            }
//        }
//    }
}
