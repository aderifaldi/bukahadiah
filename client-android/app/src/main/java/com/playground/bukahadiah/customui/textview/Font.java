package com.playground.bukahadiah.customui.textview;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;


import com.playground.bukahadiah.R;

import java.util.Hashtable;

public class Font {

	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface get(Context c, String name) {
		synchronized (cache) {
			return get(c.getAssets(), name);
		}
	}

	public static Typeface get(AssetManager asm, String name) {
		synchronized (cache) {
			if (!cache.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(
						asm,
						String.format("%s", name)
				);
				cache.put(name, t);
			}
			return cache.get(name);
		}
	}

	public static void applyFont(String myStyle, TextView tv, Context context){
		Typeface tf = null;
		if (myStyle.equals(context.getString(R.string.bold))){
			tf = get(context.getAssets(), FontPath.BOLD);
		}else if (myStyle.equals(context.getString(R.string.regular))){
			tf = get(context.getAssets(), FontPath.REGULAR);
		}else {
			tf = get(context.getAssets(), FontPath.LIGHT);
		}

		tv.setTypeface(tf);
	}

}
