package com.dudaizhong.news.modules.zhihu.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dudaizhong.news.R;
import com.dudaizhong.news.base.BaseFragment;
import com.dudaizhong.news.base.utils.DensityUtil;
import com.dudaizhong.news.modules.zhihu.adapter.SectionAdapter;
import com.dudaizhong.news.modules.zhihu.domain.SectionList;
import com.dudaizhong.news.modules.zhihu.presenter.SectionPresenter;
import com.dudaizhong.news.modules.zhihu.presenter.contract.SectionContract;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dudaizhong on 2016/9/18.
 */

public class SectionFragment extends BaseFragment<SectionPresenter> implements SectionContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recycler_zhihu_section)
    RecyclerView recyclerZhihuSection;
    @Bind(R.id.swipe_zhihu_section)
    SwipeRefreshLayout swipeZhihuSection;
    @Bind(R.id.error)
    LinearLayout mError;

    private ArrayList<SectionList.DataBean> datas;
    private SectionAdapter adapter;

    @Override
    protected SectionPresenter createPresenter() {
        return new SectionPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhihu_hot;
    }

    @Override
    protected void initEventAndData() {
        recyclerZhihuSection.setLayoutManager(new GridLayoutManager(getContext(), 2));
        datas = new ArrayList<>();
        adapter = new SectionAdapter(getContext(), datas);
        recyclerZhihuSection.setAdapter(adapter);
        swipeZhihuSection.setOnRefreshListener(this);
        showLoading();
        getPresenter().getContent(getContext());
    }

    @Override
    public void showLoading() {
        swipeZhihuSection.setProgressViewOffset(false, 0, DensityUtil.dip2px(getContext(), 24));
        swipeZhihuSection.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (null != swipeZhihuSection && swipeZhihuSection.isRefreshing())
            swipeZhihuSection.setRefreshing(false);
        recyclerZhihuSection.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
    }

    @Override
    public void showContent(SectionList sectionList) {
        datas.clear();
        datas.addAll(sectionList.getData());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        recyclerZhihuSection.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        getPresenter().getContent(getContext());
    }

    @OnClick(R.id.error)
    public void onClick() {
        showLoading();
        getPresenter().getContent(getContext());
    }
}
