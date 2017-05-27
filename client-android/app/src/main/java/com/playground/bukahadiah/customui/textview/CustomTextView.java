package com.playground.bukahadiah.customui.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.playground.bukahadiah.R;


public class CustomTextView extends AppCompatTextView {
	public CustomTextView(Context context) {
		super(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()){
			setCustomAttrs(context, attrs);
		}
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (!isInEditMode()){
			setCustomAttrs(context, attrs);
		}
	}
	
	private void setCustomAttrs(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);
		final int N = typedArray.getIndexCount();

		for (int i = 0; i < N; ++i) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.CustomFont_font:
				String myFont = typedArray.getString(attr);
				Font.applyFont(myFont, this, context);
				break;
			}
		}

		typedArray.recycle();
	}
}
