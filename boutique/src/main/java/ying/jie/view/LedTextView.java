package ying.jie.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Wbj on 2016/2/1.
 */
public class LedTextView extends TextView {

    public LedTextView(Context context) {
        super(context);

        this.setFontType(context);
    }

    public LedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setFontType(context);
    }

    /**
     * 设置字体类型
     */
    private void setFontType(Context context) {
        AssetManager assetManager = context.getAssets();
        Typeface font = Typeface.createFromAsset(assetManager, "digital-7.ttf");
        setTypeface(font);
    }


}
