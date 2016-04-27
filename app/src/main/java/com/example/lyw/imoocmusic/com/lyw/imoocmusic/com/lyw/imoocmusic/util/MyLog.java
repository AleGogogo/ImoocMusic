package com.example.lyw.imoocmusic.com.lyw.imoocmusic.com.lyw.imoocmusic.util;

import android.util.Log;

/**
 * Created by LYW on 2016/4/21.
 */
public class MyLog {
    public static final boolean DEBUG = true;

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }
}
