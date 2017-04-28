package com.wang.imagepicker.utils;


import android.util.Log;


/**
 * Created by wang
 * on 2017/4/17
 */

public class L {

    private static boolean sDebug = false;

    private static String TAG = "ImagePicker";


    public static void init(boolean debug, String tag) {
        sDebug = debug;
        TAG = tag;
    }

    public static void init(boolean debug) {
        sDebug = debug;
    }


    public static void d(String tag, String msg) {
        if (sDebug) {
            Log.d(TAG + tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.d(TAG + tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (sDebug) {
            Log.e(TAG + tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.e(TAG + tag, msg, tr);
        }
    }


    public static void i(String tag, String msg) {
        if (sDebug) {
            Log.i(TAG + tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.i(TAG + tag, msg, tr);
        }
    }


    public static void v(String tag, String msg) {
        if (sDebug) {
            Log.v(TAG + tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.v(TAG + tag, msg, tr);
        }
    }


    public static void w(String tag, String msg) {
        if (sDebug) {
            Log.w(TAG + tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (sDebug) {
            Log.w(TAG + tag, msg, tr);
        }
    }

    public static void d(String msg) {
        if (sDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (sDebug) {
            Log.d(TAG, msg, tr);
        }
    }

    public static void e(String msg) {
        if (sDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (sDebug) {
            Log.e(TAG, msg, tr);
        }
    }


    public static void i(String msg) {
        if (sDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (sDebug) {
            Log.i(TAG, msg, tr);
        }
    }


    public static void v(String msg) {
        if (sDebug) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (sDebug) {
            Log.v(TAG, msg, tr);
        }
    }


    public static void w(String msg) {
        if (sDebug) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (sDebug) {
            Log.w(TAG, msg, tr);
        }
    }
}
