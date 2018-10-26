package com.example.mukeshkumar009ecomsample.startup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mukeshkumar009ecomsample.R;
import com.example.mukeshkumar009ecomsample.utility.PrefManager;

/**активити,куда переходит пользователь после стартовой анимации */
public class WelcomeActivity extends AppCompatActivity {

    /**ViewPager для слайдов */
    private ViewPager viewPager;
    /**ViewPagerAdapter для обработки пролистывания слайдов */
    private MyViewPagerAdapter myViewPagerAdapter;
    /**макет */
    private LinearLayout dotsLayout;
    /**массив TextView для заглавий слайдов */
    private TextView[] dots;
    /**массив макетов */
    private int[] layouts;
    /**кнопки пропустить и далее   */
    private Button btnSkip, btnNext;
    /** PrefManager для доступа к преференсам.Нужно для отслеживания,первый раз ли загружается приложение*/
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        /**с помощью PrefManager проверка первый ли раз загружается приложение.Если это не первая загрузка,то
         * стартует MainActivity*/
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Notification bar делается прозрачным,данный метод для ОС старше 21
        /**запрос видимости статус бара */
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        /**инициализация вьюшек */
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        /** макеты слайдов приветствия*/
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        /** добавление нижних точек*/
        addBottomDots(0);

        /**Notification bar делается прозрачным,данный метод для ОС младше 21*/
        changeStatusBarColor();

        /**установка адаптера и передача его ViewPager-у */
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        /**подключение к ViewPager-у слушателя */
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        /**если пользователь нажимает на пропустить то сразу переходит на MainActivity */
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
        /** если пользователь нажимает на далее*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**проверка это последняя страница? */
                // if last page home screen will be launched
                int current = getItem(+1);
                /**если не последняя переход на следующую */
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                }
                /**если последняя,то переход на MainActivity */
                else {
                    launchHomeScreen();
                }
            }
        });
    }

    /**настройка точек внизу */
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    /**возвращает позицию текущего отображаемого слайда */
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    /**перенаправляет на MainActivity */
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    /** слушатель событий ViewPager-а*/
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        /**пользователь пролистнул страницу */
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Делание Notification Bar прозрачным для версий ОС младше 21
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Адаптер для ViewPager-а
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
