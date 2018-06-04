package com.xw.h2market;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Animation extends AppCompatActivity {
    private TextView tv_top;
    private TextView tv_buttom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_animation);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        tv_top = (TextView) findViewById(R.id.AnimationTest_top);
        tv_buttom = (TextView) findViewById(R.id.AnimationTest_buttom);
        AlphaAnimationT();
        ScaleAnimationT();
        TimerT();
    }

    public void AlphaAnimationT() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        animationSet.addAnimation(alphaAnimation);
        tv_top.startAnimation(animationSet);
        tv_buttom.startAnimation(animationSet);
    }

    public void ScaleAnimationT() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1.0f, 0, 1.0f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.4f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.4f);
        scaleAnimation.setDuration(2000);
        animationSet.addAnimation(scaleAnimation);
        tv_top.startAnimation(animationSet);
        tv_buttom.startAnimation(animationSet);
    }

    public void TimerT() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Animation.this, StartActivity.class);
                startActivity(intent);
                Animation.this.finish();
            }
        };
        timer.schedule(task, 3000);
    }
}
