package jie.example.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LedTextView extends TextView {

	public LedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		AssetManager assets = context.getAssets();
		final Typeface font = Typeface.createFromAsset(assets, "digital-7.ttf");
		setTypeface(font);
	}

}
