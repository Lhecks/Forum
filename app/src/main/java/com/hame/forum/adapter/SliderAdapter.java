package com.hame.forum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hame.forum.R;

public class SliderAdapter extends PagerAdapter {
    public int[] sliderImage = {
            R.drawable.a350_airbus,
            R.drawable.ford_mustang,
            R.drawable.mercedes_benz,
            R.drawable.zz
    };
    public String[] sliderTextTitle = {
            "Welcome",
            "What do we do",
            "What you are going to do",
            "Now get Started"
    };
    public String[] sliderTextDescription = {
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo",
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo",
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo",
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo"
    };
    private Context myContext;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context myContext) {
        this.myContext = myContext;
    }

    @Override
    public int getCount() {
        return sliderTextTitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
//        return view == [RelativeLayout] object;
    }
}
