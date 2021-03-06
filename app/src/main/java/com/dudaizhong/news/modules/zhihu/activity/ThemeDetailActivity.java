package com.dudaizhong.news.modules.zhihu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.dudaizhong.news.R;
import com.dudaizhong.news.base.BaseActivity;
import com.dudaizhong.news.base.utils.DensityUtil;
import com.dudaizhong.news.modules.zhihu.adapter.ThemeDetailAdapter;
import com.dudaizhong.news.modules.zhihu.domain.ThemeDetail;
import com.dudaizhong.news.modules.zhihu.presenter.ThemeDetailPresenter;
import com.dudaizhong.news.modules.zhihu.presenter.contract.ThemeDetailContract;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Markable on 2016/11/19.
 * TODO 这里的传值需要注意
 */

public class ThemeDetailActivity extends BaseActivity<ThemeDetailPresenter> implements ThemeDetailContract.View {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.error)
    LinearLayout mError;

    private int id;
    private String title;
    private ThemeDetailAdapter adapter;
    private ArrayList<ThemeDetail.StoriesBean> datas;
    private ThemeDetail themeDetail;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zhihu_theme_detail;
    }

    public static Intent getThemeDetailIntent(Context context, int id, String title) {
        Intent intent = new Intent(context, ThemeDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        return intent;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        Intent getId = getIntent();
        id = getId.getIntExtra("id", 0);
        title = getId.getStringExtra("title");
        initView();
        showLoading();
        getPresenter().getContent(this,id);
    }

    private void initView() {

        setToolBar(mToolbar, title);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        datas = new ArrayList<>();
        themeDetail = new ThemeDetail();
        adapter = new ThemeDetailAdapter(this, datas, themeDetail);
        mRecyclerView.setAdapter(adapter);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().getContent(ThemeDetailActivity.this,id);
            }
        });
    }


    @Override
    public void showLoading() {
        mSwipeRefresh.setProgressViewOffset(false, 0, DensityUtil.dip2px(this, 24));
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (null != mSwipeRefresh) {
            mSwipeRefresh.setRefreshing(false);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
    }

    @Override
    public void showContent(ThemeDetail themeDetail) {
        datas.clear();
        adapter.addData(themeDetail);
    }

    @Override
    public void showError() {
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.error)
    public void onClick() {
        showLoading();
        getPresenter().getContent(this,id);
    }
}
