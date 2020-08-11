package com.hame.forum.controller.utils;

import android.content.Context;
import android.util.AttributeSet;

public class MySpinner extends androidx.appcompat.widget.AppCompatSpinner {
    OnItemSelectedListener onItemSelectedListener;

    public MySpinner(Context context) {
        super(context);
    }

    public MySpinner(Context context, int mode) {
        super(context, mode);
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @Override
    public void setSelection(int position, boolean animate) {
        super.setSelection(position, animate);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }
}
