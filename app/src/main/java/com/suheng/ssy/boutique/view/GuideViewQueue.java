package com.suheng.ssy.boutique.view;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GuideViewQueue {

    private static GuideViewQueue sInstance = new GuideViewQueue();
    private List<GuideView> mGuideViewList = new ArrayList<>();

    private GuideViewQueue() {
    }

    public GuideViewQueue addBuilder(GuideView.Builder builder) {
        if (builder != null) {
            builder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GuideView guideView = mGuideViewList.get(0);
                    guideView.hide();
                    mGuideViewList.remove(guideView);
                    show();
                }
            });
            mGuideViewList.add(builder.create());
        }
        return sInstance;
    }

    public void show() {
        if (mGuideViewList.size() > 0) {
            mGuideViewList.get(0).show();
        }
    }

    public void onNext() {
        if (mGuideViewList.size() > 0) {
            mGuideViewList.get(0).getBuilder().getOnClickListener().onClick(mGuideViewList.get(0));
        }
    }

    public boolean hasNext() {
        return !mGuideViewList.isEmpty();
    }

    public static GuideViewQueue getInstance() {
        return sInstance;
    }
}
