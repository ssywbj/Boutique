package ying.jie.boutique.menu_function;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ying.jie.boutique.R;
import ying.jie.util.VoicePlayer;

/**
 * 表情面板：一开始隐藏于界面和底部，然后以动画的形式出现、隐藏
 */
public class ExpressionPanel extends LinearLayout implements AdapterView.OnItemClickListener {
    public static final String TAG = ExpressionPanel.class.getSimpleName();
    private static final int TRANSLATION_DURATION_TIME = 300;
    private ViewPager mViewPager;
    private ImageView mImageSelected;
    private List<ExpressionPanelUnit> mPanelUnitList = new ArrayList<>();
    private int mTranslationDistance;
    private boolean mIsVisible;
    private OnPanelItemClickListener mOnPanelItemClickListener;

    public ExpressionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        setPadding(1, 0, 1, 0);
        setBackgroundResource(R.color.loading_dialog_bg);

        mTranslationDistance = getContext().getResources().getDimensionPixelSize(R.dimen.expression_panel_height_total);

        ExpressionPanelUnit panelUnitOne = new ExpressionPanelUnit(getContext(), R.drawable.expression_item_01, ExpressionPanelUnit.UNIT_ITEMS);
        panelUnitOne.mGridView.setOnItemClickListener(this);
        mPanelUnitList.add(panelUnitOne);
        ExpressionPanelUnit panelUnitTwo = new ExpressionPanelUnit(getContext(), R.drawable.expression_item_19, 10);
        panelUnitTwo.mGridView.setOnItemClickListener(this);
        mPanelUnitList.add(panelUnitTwo);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.expression_panel_view, this);
        view.setOnTouchListener(new OnTouchListener() {//防止点击事件被穿透
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        this.initViewPager();
        this.setSelectedItem(0);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.pager_expression_view);
        if (mPanelUnitList.size() > 1) {
            mViewPager.setOffscreenPageLimit(mPanelUnitList.size() - 1);//除默认加载1个页面外，再提前加载n个页面
        }
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mPanelUnitList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public ExpressionPanelUnit instantiateItem(ViewGroup container, int position) {
                ExpressionPanelUnit expressionPanelUnit = mPanelUnitList.get(position);
                container.addView(expressionPanelUnit);
                return expressionPanelUnit;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((ExpressionPanelUnit) object);
            }
        });
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    private void setSelectedItem(int index) {
        if (index < 0 || index > mPanelUnitList.size() - 1) {
            return;
        }

        mViewPager.setCurrentItem(index);
        if (mImageSelected != null) {
            mImageSelected.setImageResource(R.drawable.expression_unit_normal);
        }

        switch (index) {
            case 0:
                mImageSelected = (ImageView) findViewById(R.id.image_point_one);
                break;
            case 1:
                mImageSelected = (ImageView) findViewById(R.id.image_point_two);
                break;
            default:
                break;
        }
        mImageSelected.setImageResource(R.drawable.expression_unit_selected);
    }

    public void showByAnimation() {
        ObjectAnimator translationShow = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, mTranslationDistance);
        translationShow.setDuration(TRANSLATION_DURATION_TIME);
        translationShow.setInterpolator(new AccelerateDecelerateInterpolator());
        translationShow.start();

        mIsVisible = true;
    }

    public void hideByAnimation() {
        ObjectAnimator translationHide = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, -mTranslationDistance);
        translationHide.setDuration(TRANSLATION_DURATION_TIME);
        translationHide.setInterpolator(new AccelerateDecelerateInterpolator());
        translationHide.start();

        mIsVisible = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPanelUnitList.clear();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ExpressionPanel.this.setSelectedItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position += ExpressionPanelUnit.UNIT_ITEMS * mViewPager.getCurrentItem();
        this.setItemClickVoice(position);

        ImageView imageExpression = (ImageView) view.findViewById(R.id.expression_unit_item);
        if (mOnPanelItemClickListener != null) {
            mOnPanelItemClickListener.onPanelItemClick(imageExpression.getDrawable(), position);
        }
    }

    private void setItemClickVoice(int pst) {
        switch (pst) {
            case 0://voice 6
            case 8:
            case 12:
            case 16:
            case 23:
            case 25:
                VoicePlayer.playVoice("expression_voice_06.mp3");
                break;
            case 1://voice 2
            case 9:
            case 10:
            case 17:
            case 19:
            case 22:
                VoicePlayer.playVoice("expression_voice_02.mp3");
                break;
            case 2://voice 4
            case 15:
                VoicePlayer.playVoice("expression_voice_04.mp3");
                break;
            case 3://voice 1
            case 5:
            case 11:
            case 18:
            case 27:
            case 21:
                VoicePlayer.playVoice("expression_voice_01.mp3");
                break;
            case 4://voice 3
            case 7:
            case 20:
            case 24:
                VoicePlayer.playVoice("expression_voice_03.mp3");
                break;
            case 6://voice 5
            case 13:
            case 14:
            case 26:
                VoicePlayer.playVoice("expression_voice_05.mp3");
                break;
            default:
                break;
        }
    }

    public void setOnPanelItemClickListener(OnPanelItemClickListener onPanelItemClickListener) {
        mOnPanelItemClickListener = onPanelItemClickListener;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public interface OnPanelItemClickListener {
        void onPanelItemClick(Drawable drawable, int pst);
    }

}
