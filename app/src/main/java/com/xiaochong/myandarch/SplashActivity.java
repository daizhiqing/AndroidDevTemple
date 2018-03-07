package com.xiaochong.myandarch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import site.gemus.openingstartanimation.LineDrawStrategy;
import site.gemus.openingstartanimation.NormalDrawStrategy;
import site.gemus.openingstartanimation.OpeningStartAnimation;
import site.gemus.openingstartanimation.RedYellowBlueDrawStrategy;
import site.gemus.openingstartanimation.RotationDrawStrategy;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
                .setDrawStategy(new NormalDrawStrategy())
                .setAppStatement("daizhiqing")
                .setAnimationFinishTime(6000)
                .create();
        openingStartAnimation.show(this);

//        OpeningStartAnimation openingStartAnimation1 = new OpeningStartAnimation.Builder(this)
//                .setDrawStategy(new RedYellowBlueDrawStrategy())
//                .create();
//        openingStartAnimation1.show(this);
//
//        OpeningStartAnimation openingStartAnimation2 = new OpeningStartAnimation.Builder(this)
//                .setDrawStategy(new LineDrawStrategy())
//                .create();
//        openingStartAnimation2.show(this);
//
//        OpeningStartAnimation openingStartAnimation3 = new OpeningStartAnimation.Builder(this)
//                .setDrawStategy(new RotationDrawStrategy())
//                .create();
//        openingStartAnimation3.show(this);
    }
}
