package by.matveev.lenovostart.lib;


// класс для хранения информации о том, что пользователь решил проигнорировать этот релиз:

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsManager {
    static String get(Context mContext, String key) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        String data = settings.getString(key, null);
        if (data == null)
            Log.d("SettingsManager", "No settings " + key + " is stored! ");
        else
            Log.d("SettingsManager", "Got settings " + key + " equal to " + data);
        return data;
    }

    @SuppressLint("CommitPrefEdits")
    public static void put(DialogInterface.OnClickListener mContext, String key, String value) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences((Context) mContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        Log.d("SettingsManager", "Saved setting " + key + " equal to " + value);
        editor.commit();
    }
}
