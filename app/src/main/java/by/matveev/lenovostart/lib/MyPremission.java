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
    public Boolean premissionsactive = true;

    public boolean PremissionGPS(Context context){
        if(hasPremmisionGPS(context)){
            // our app has permissions.

        }
        else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationaleGPS(context);
            premissionsactive = false;
            return false;
        }

        return true;
    }

    private boolean hasPremmisionGPS(Context context) {
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
        for (String perms : permissions){
            //с помощью метода checkCallingOrSelfPermission в цикле проверяет
            //предоставленные приложению разрешения и сравнивает их с тем, которое нам необходимо.
            //При отсутствии разрешения метод будет возвращать false, а при наличии разрешения — true.
            res = checkCallingOrSelfPermission(context, perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    public boolean myPremission(Context context){
        if (hasPermissions(context)){
            // our app has permissions.
            Filealmat makefolder = new Filealmat();
            makefolder.makeFolder(context,"");
        }
        else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale(context);
            premissionsactive = false;
            return false;
        }
        if (0 != ExternalStorageState(context)){
            premissionsactive = false;
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
    private void requestPermissionWithRationale(Context context) {


        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPerms((Activity) context);
        } else {
            requestPerms((Activity) context);
        }
    }
    private void requestPerms(Activity activity){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);//вывод сообщения разрешения на доступ к файлам медиа и папкам

        }
    }
    private void requestPermissionWithRationaleGPS(Context context) {


        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermsGPS((Activity) context);
        } else {
            requestPermsGPS((Activity) context);
        }
    }
    private void requestPermsGPS(Activity activity){
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);//вывод сообщения разрешения на доступ к файлам медиа и папкам

        }
    }

    public int ExternalStorageState( Context context){
//        handleExternalStorageState(mExternalStorageAvailable,
//                mExternalStorageWriteable);
        //        // проверяем доступность SD
 //       while (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {


//        У нас есть два состояния памяти. Одно - внутренняя карта памяти, а другое - внешняя SD-карта.
//        Это зависит от производителей устройств, как они создали путь к карте памяти.
//        Итак, если вы проверяете доступность SD-карты, то оно может возвращать true в обоих случаях
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                //Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
                //Toast.makeText( "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
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
