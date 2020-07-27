package com.hame.forum;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hame.forum.controller.utils.SessionManager;

import java.util.Objects;

public class Welcome extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearDots;
    private int[] layouts;
    private Button skipButton, nextButton;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addMyBottomPoints(position);

            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                nextButton.setText(R.string.got_it);
                skipButton.setVisibility(View.GONE);
            } else {
                // still pages are left
                nextButton.setText(R.string.next);
                skipButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        Objects.requireNonNull(getSupportActionBar()).hide();
        getSupportActionBar().hide();
        sessionManager = new SessionManager(getApplicationContext());
        if (!sessionManager.isFirstTimeLaunch()) {
            launchMains();
            finish();
        }
        if (Build.VERSION.SDK_INT >= 18) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        viewPager = findViewById(R.id.view_pager_intro);
        linearDots = findViewById(R.id.layout_dots);
        skipButton = findViewById(R.id.button_skip_view_pager);
        nextButton = findViewById(R.id.button_next_view_pager);

        layouts = new int[]{
                R.layout.intro_slider,
                R.layout.intro_first_slide,
                R.layout.intro_second_slide,
                R.layout.intro_third_slide
        };

//        adding bottom dots
        addMyBottomPoints(0);

//         making notification bar transparent
        changeStatusBarColor();

        Welcome.pagerAdapters pagerAdapters = new pagerAdapters();
        viewPager.setAdapter(pagerAdapters);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMain();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    launchMain();
                }
            }
        });
    }

    private void addMyBottomPoints(int currentPage) {
        TextView[] myPoints = new TextView[layouts.length];

        linearDots.removeAllViews();
        for (int i = 0; i < myPoints.length; i++) {
            myPoints[i] = new TextView(this);
            myPoints[i].setText(Html.fromHtml("&#8226;"));
            myPoints[i].setTextSize(35);
            myPoints[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            linearDots.addView(myPoints[i]);
        }

        if (myPoints.length > 0)
            myPoints[currentPage].setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchMain() {
        sessionManager.setLogin(false);
//        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Welcome.this, Registered.class));
        finish();
    }

    private void launchMains() {
        sessionManager.setLogin(false);
//        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Welcome.this, MainActivity.class));
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //    View pager adapter class
    public class pagerAdapters extends PagerAdapter {
        private Context context;

        pagerAdapters() {
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object objects) {
            View view = (View) objects;
            container.removeView(view);
        }
    }

}
