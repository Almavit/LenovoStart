package by.matveev.lenovostart.lib;

import android.os.Environment;
import android.util.Log;

public class Filealmat {
    public String PatshFile;
    public String NameFile;
    public String NameDirectory;
    final String LOG_TAG = "PatshDIR_SD";

    public int  writeFileSD(){

    // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            //ToastMessageCenter("SD-карта не доступна: " + Environment.getExternalStorageState());
            return -1;
        }
        return 0;
    }
}
