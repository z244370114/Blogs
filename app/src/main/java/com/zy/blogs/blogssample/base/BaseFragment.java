package com.zy.blogs.blogssample.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    public Activity mActivity;
    private View mRootView;
    private LayoutInflater inflater;
    private Toast mToast;
    public static final String TYPE = "type";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            this.inflater = inflater;
            mRootView = inflater.inflate(setLayoutResouceId(), container, false);
            ButterKnife.bind(this, mRootView);
            setUpContentView();
//            initAppView(mRootView, savedInstanceState);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

//    protected abstract void initAppView(View mRootView, Bundle savedInstanceState);

    protected abstract int setLayoutResouceId();

    protected abstract void setUpContentView();

    protected abstract void setUpData();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
    }


    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(mActivity, text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    /**
     * 切换Activity
     *
     * @param c 需要切换到的Activity
     */
    public void GoActivity(Class<?> c) {
        Intent intent = new Intent(mActivity, c);
        this.startActivity(intent);
    }

    /**
     * 切换Activity
     *
     * @param c    需要切换到的Activity
     * @param type 参数
     */
    public void GoActivity(Class<?> c, String type) {
        Intent intent = new Intent(mActivity, c);
        intent.putExtra(TYPE, type);
        this.startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        onUnsubscribe();
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }
}
