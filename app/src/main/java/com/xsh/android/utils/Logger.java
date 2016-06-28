package com.xsh.android.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/6/29.
 */
public class Logger {

    public static boolean isShowLog = true;

    public static void i(Object object, String info){
        if(!isShowLog){
            return;
        }
        if(object instanceof String){
            Log.i((String) object, info);
        }else if(object instanceof Class){
            Log.i(((Class)object).getSimpleName(), info);
        }else {
            Log.i(object.getClass().getSimpleName(), info);
        }
    }
}
