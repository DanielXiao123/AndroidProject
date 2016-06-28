package com.xsh.android.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by DanielXiao on 2016/6/18.
 */
public class AppManager {

    private Stack<Activity> activityStack = new Stack<Activity>();

    private Activity curActivity;

    private static AppManager appManager = null;

    private AppManager(){

    }

    public static AppManager getInstance(){
        if(appManager == null){
            appManager = new AppManager();
        }
        return appManager;
    }

    public void addActivity(Activity activity){
        activityStack.add(activity);
    }

    public void removeCurrent(){
        Activity lastElment = activityStack.lastElement();
        lastElment.finish();
        activityStack.remove(lastElment);
}

    public void removeAll(){
        for(int i=activityStack.size()-1; i>=0; i++){
            Activity activity = activityStack.get(i);
            activity.finish();
            activityStack.remove(activity);
        }
    }

    public void removeActivty(Activity activity){
        for(int i=activityStack.size()-1; i>=0; i--){
            Activity activity1 = activityStack.get(i);
            if(activity1.getClass().equals(activity.getClass())){
                activity1.finish();
                activityStack.remove(activity1);
            }
        }
    }

    public Activity getCurrentObj(){
        return activityStack.lastElement();
    }

    public int getSize(){
        return activityStack.size();
    }
}
