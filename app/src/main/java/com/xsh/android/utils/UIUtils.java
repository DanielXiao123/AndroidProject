package com.xsh.android.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.xsh.android.MyApplicaiton;
import com.xsh.android.common.AppManager;

/**
 * Created by Administrator on 2016/6/18.
 */
     public class UIUtils {

     public static Context getContext(){
     return MyApplicaiton.context;
     }

     public static Handler getHandler(){
     return MyApplicaiton.handler;
     }

     public static boolean isInMainThread(){
     int tid = android.os.Process.myTid();
     if(tid == MyApplicaiton.mainThreadId){
     return true;
     }
     return false;
     }

     /**
     * 保證Runnable方法執行在主線程
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable){
        if(isInMainThread()){
            runnable.run();
        }else{
            getHandler().post(runnable);
        }
    }

    public static View getXmlView(int layoutId){
        return View.inflate(getContext(),layoutId, null);
    }

    public static int getColor(int colorId){
        return getContext().getResources().getColor(colorId);
    }

    public static String[] getStringArr(int arrId){
        return getContext().getResources().getStringArray(arrId);
    }

    public static void showToast(String content, boolean isLong){
        Toast.makeText(getContext(), content, isLong == true? Toast.LENGTH_LONG: Toast.LENGTH_SHORT).show();
    }

    public static int dp2px(int dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static int px2dp(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px/density + 0.5);
    }

    public static void gotoActivity(Class clazz,Bundle bundle){
        Intent intent = new Intent(getContext(), clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        AppManager.getInstance().getCurrentObj().startActivity(intent);
    }

}
