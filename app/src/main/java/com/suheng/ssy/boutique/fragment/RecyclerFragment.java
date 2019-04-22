package com.suheng.ssy.boutique.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.suheng.ssy.boutique.BR;
import com.suheng.ssy.boutique.BoutiqueApp;
import com.suheng.ssy.boutique.FragmentRecyclerActivity;
import com.suheng.ssy.boutique.R;
import com.suheng.ssy.boutique.dagger.ActEntity;
import com.suheng.ssy.boutique.dagger.DaggerActivityComponent;
import com.suheng.ssy.boutique.databinding.FragmentRecyclerBinding;
import com.suheng.ssy.boutique.view.RecyclerBindingAdapter;
import com.suheng.ssy.boutique.view.RecyclerBindingHolder;
import com.suheng.ssy.boutique.view.RecyclerDivider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.shape.RectLightShape;

/**
 * Created by wbj on 2018/12/11.
 */
@Route(path = "/app/activity/fragment_recycler")//也可以通过路由的方式启动Fragment
public class RecyclerFragment extends BasicFragment {

    @Inject
    ActEntity mActEntity;
    private FragmentRecyclerBinding mViewBinding;
    private List<ItemModel> mItemModels = new ArrayList<>();
    private MyAdapter mMyAdapter;
    private ItemModel mCurrentItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(mTag, this + ", onCreate");
    }

    @Nullable
    @Override//在Fragment中使用Databinding
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(mTag, this + ", onCreateView");
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerActivityComponent.builder().appComponent(((BoutiqueApp) getActivity()
                .getApplication()).getAppComponent()).build().getActSubComponent().inject(this);
        Log.d(mTag, this + ", onActivityCreated, " + mActEntity.toString());

        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));//设置布局管理器
        mViewBinding.recyclerView.addItemDecoration(new RecyclerDivider(getContext(), LinearLayoutManager.VERTICAL));
        mViewBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<String> stringList = Arrays.asList(getResources().getStringArray(R.array.main_item));

        for (String str : stringList) {
            mItemModels.add(new ItemModel(str, (new Random().nextInt(5) % 3 == 0)
                    ? ItemModel.VIEW_TYPE_RED : ItemModel.VIEW_TYPE_BLACK, this));
        }
        mMyAdapter = new MyAdapter(mItemModels);
        mViewBinding.recyclerView.setAdapter(mMyAdapter);//设置adapter

        NewbieGuide.with(this)
                .setLabel(System.nanoTime() + "")
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(getActivity().findViewById(R.id.button_two))
                        .setLayoutRes(R.layout.guideview))
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(mTag, this + ", onResume");
        this.showNextTipViewOnCreated();
        /*getActivity().findViewById(R.id.button_one).post(new Runnable() {
            @Override
            public void run() {
                GuideViewQueue.getInstance()
                        .addBuilder(new GuideView.Builder(getActivity())
                                .setTargetView(R.id.button_one)
                                .setHintView(View.inflate(getActivity(), R.layout.guideview, null))
                                .setHintViewDirection(GuideView.Direction.BOTTOM)
                                .setForm(GuideView.Form.RECTANGLE))
                        .show();
            }
        });*/
    }

    private HighLight mHightLight;

    /**
     * 当界面布局完成显示next模式提示布局
     * 显示方法必须在onLayouted中调用
     * 适用于Activity及Fragment中使用
     * 可以直接在onCreated方法中调用
     *
     * @author isanwenyu@163.com
     */
    public void showNextTipViewOnCreated() {
        mHightLight = new HighLight(getActivity())//
                 /*.anchor(getActivity().findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor*/
                .autoRemove(false)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        mHightLight.addHighLight(R.id.button_one, R.layout.guideview, new OnLeftPosCallback(45), new RectLightShape())
                                /*.addHighLight(R.id.btn_light,R.layout.info_gravity_left_down,new OnRightPosCallback(5),new CircleLightShape())
                                .addHighLight(R.id.btn_bottomLight,R.layout.info_gravity_left_down,new OnTopPosCallback(),new CircleLightShape())*/;
                        //然后显示高亮布局
                        mHightLight.show();
                    }
                })
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {
                        Toast.makeText(getActivity(), "clicked and show next tip view by yourself", Toast.LENGTH_SHORT).show();
                        mHightLight.next();
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(mTag, this + ", onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(mTag, this + ", onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(mTag, this + ", onDetach");
    }

    public void clickItem(ItemModel itemModel) {
        Log.d(mTag, "item: " + itemModel.title.get());
        if (itemModel.getItemType() == ItemModel.VIEW_TYPE_BLACK) {
            if (mItemModels.contains(itemModel)) {
                mItemModels.remove(itemModel);
                mMyAdapter.notifyDataSetChanged();
            }
        } else if (itemModel.getItemType() == ItemModel.VIEW_TYPE_RED) {
            mCurrentItem = itemModel;
            if (getActivity() instanceof FragmentRecyclerActivity) {//Fragment调用Activity权限管理的方法之一
                if (mItemModels.contains(mCurrentItem)) {
                    if (mItemModels.indexOf(mCurrentItem) == 2) {
                        ((FragmentRecyclerActivity) getActivity()).queryCallPhonePermission("18819059959");
                    } else {
                        ((FragmentRecyclerActivity) getActivity()).queryExternalStoragePermission(0x10);
                    }
                }
            }
        }
    }

    public void saveInfoInFile() {
        if (mCurrentItem != null) {
            Log.d(mTag, "saveInfoInFile: " + mCurrentItem.title.get());
        }
    }

    private class MyAdapter extends RecyclerBindingAdapter<ItemModel> {

        public MyAdapter(List<ItemModel> itemModelList) {
            super(itemModelList);
        }

        @Override
        public int getItemViewType(int position) {
            return getDataList().get(position).getItemType();
        }

        @NonNull
        @Override//在RecyclerView的adapter中使用DataBinding
        public RecyclerBindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewDataBinding dataBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                    (viewType == ItemModel.VIEW_TYPE_BLACK ? R.layout.fragment_recycler_adt : R.layout.fragment_recycler_adt2), parent, false);
            return (viewType == ItemModel.VIEW_TYPE_BLACK ? new BlackHolder(dataBinding.getRoot()) : new RedHolder(dataBinding.getRoot()));//实例化ViewHolder
        }

        @Override
        public int getVariableId(@NonNull RecyclerBindingHolder bindingHolder) {
            return BR.itemModel;
        }

        class BlackHolder extends RecyclerBindingHolder {

            public BlackHolder(View view) {
                super(view);
            }
        }

        class RedHolder extends RecyclerBindingHolder {

            public RedHolder(View view) {
                super(view);
            }
        }
    }

    public static class ItemModel {
        public static final int VIEW_TYPE_BLACK = 0;
        public static final int VIEW_TYPE_RED = 1;
        private ObservableField<String> title = new ObservableField<>();
        private WeakReference<RecyclerFragment> mWeakReference;
        private int mItemType;

        public ItemModel(String title, int itemType, RecyclerFragment recyclerFragment) {
            this.title.set(title);
            mItemType = itemType;
            mWeakReference = new WeakReference<>(recyclerFragment);
        }

        public void onClickItem(ItemModel itemModel) {
            mWeakReference.get().clickItem(itemModel);
        }

        public ObservableField<String> getTitle() {
            return title;
        }

        public int getItemType() {
            return mItemType;
        }
    }

}