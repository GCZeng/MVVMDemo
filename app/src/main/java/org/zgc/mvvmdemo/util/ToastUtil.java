package org.zgc.mvvmdemo.util;

import android.widget.Toast;

import org.zgc.mvvmdemo.app.APP;



/**
 * Toast Util
 * Created by Nick on 2017/2/1
 */
public class ToastUtil {
    private ToastUtil() {
    }

    public static void showShort( CharSequence message) {
        Toast.makeText(APP.sInstance, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort( int message) {
        Toast.makeText(APP.sInstance, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong( CharSequence message) {
        Toast.makeText(APP.sInstance, message, Toast.LENGTH_LONG).show();
    }

    public static void showLong( int message) {
        Toast.makeText(APP.sInstance, message, Toast.LENGTH_LONG).show();
    }

    public static void show(CharSequence message, int duration) {
        Toast.makeText(APP.sInstance, message, duration).show();
    }

    public static void show( int message, int duration) {
        Toast.makeText(APP.sInstance, message, duration).show();
    }


}
