package com.xsh.android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/6/29.
 * 弹性的ScollView
 * 这个是从金融项目中学来的
 */
public class MyScrollView extends ScrollView{

    private View innerView;

    private Rect normal = new Rect();

    private boolean animationFinish = true;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        int count = getChildCount();
        if(count >0){
            innerView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(innerView == null){
            return super.onTouchEvent(ev);
        }else{
            commonTouchEvent(ev);
        }

        return onTouchEvent(ev);
    }

    float y;

    float preY = 0;

    private void commonTouchEvent(MotionEvent ev) {
        if(!animationFinish){
            return;
        }

        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                y = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float preY = y == 0 ? ev.getY() :y;
                float nowY = (int) getY();
                float deltaY = nowY - preY;
                y = nowY;

                if(isNeedMove()){
                    if(normal.isEmpty()){
                        normal.set(innerView.getLeft(), innerView.getTop(), innerView.getRight(), innerView.getBottom());
                    }
                    innerView.layout(innerView.getLeft(), innerView.getTop() + (int)deltaY/2, innerView.getRight(), innerView.getBottom() + (int)deltaY/2);
                }
                break;

            case MotionEvent.ACTION_UP:

                if(!normal.isEmpty()){
                    animation();
                }
                break;
        }
    }

    private void animation() {
        TranslateAnimation animation = new TranslateAnimation(0,0, 0,normal.top - innerView.getTop());

        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationFinish = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                innerView.clearAnimation();
                innerView.layout(normal.left, normal.top, normal.right, normal.bottom);
                normal.setEmpty();
                animationFinish =true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        innerView.startAnimation(animation);

    }

    private boolean isNeedMove() {
        int offset = innerView.getMeasuredWidth() - getHeight();
        int scrollY = getScrollY();
        if(scrollY == 0 || offset == scrollY){
            return true;
        }
        return false;
    }
}
