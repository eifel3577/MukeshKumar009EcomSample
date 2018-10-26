package com.example.mukeshkumar009ecomsample.startup;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.mukeshkumar009ecomsample.R;

/**точка входа в приложение.Имплементирует Animation.AnimationListener.Это слушатель,который
 *  получает уведомления от анимации */
public class SplashActivity extends Activity implements Animation.AnimationListener {

    /**Обьект Animation.Animation это абстрактный класс для анимации, которая будет применяться к View*/
    Animation animFadeIn;
    /**макет */
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /**Настройка скрытия статус бара */
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
        }

        /**загрузка анимации.Загружает файл анимации animation_fade_in из ресурсов*/
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_fade_in);
        /**установка слушателя анимации */
        animFadeIn.setAnimationListener(this);
        /**установка макета */
        linearLayout = (LinearLayout) findViewById(R.id.layout_linear);
        /**старт анимации */
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animFadeIn);

    }

    /**если пользователь не дожидается конца анимации и нажимает кнопку Назад,то приложение закрывается */
    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        //under Implementation
    }

    /**когда анимация заканчивается,пользователь переходит на WelcomeActivity */
    public void onAnimationEnd(Animation animation) {
            // Start Main Screen
            Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(i);
            this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        //under Implementation
    }

}