package com.dudaizhong.news.modules.gank.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dudaizhong.news.R;
import com.dudaizhong.news.app.Constants;
import com.dudaizhong.news.base.BaseFragment;
import com.dudaizhong.news.base.utils.DensityUtil;
import com.dudaizhong.news.common.widget.LoadMoreRecyclerView;
import com.dudaizhong.news.modules.gank.adapter.AIAdapter;
import com.dudaizhong.news.modules.gank.domain.AIList;
import com.dudaizhong.news.modules.gank.presenter.AIPresenter;
import com.dudaizhong.news.modules.gank.presenter.contract.AIContract;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Markable on 2016/11/22.
 */

public class AIFragment extends BaseFragment<AIPresenter> implements AIContract.View {

    @Bind(R.id.swipe_zhihu_section)
    SwipeRefreshLayout mSwipeZhihuSection;
    @Bind(R.id.recycler_zhihu_section)
    LoadMoreRecyclerView mRecyclerZhihuSection;
    @Bind(R.id.error)
    LinearLayout mError;

    private ArrayList<AIList> datas = new ArrayList<>();
    private AIAdapter adapter;
    private String type;
    private int currentPage = 1;
    private static final int NUM = 10;

    @Override
    protected AIPresenter createPresenter() {
        return new AIPresenter(getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    public static AIFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        AIFragment aiFragment = new AIFragment();
        aiFragment.setArguments(bundle);
        return aiFragment;
    }


    @Override
    protected void initEventAndData() {

        Bundle bundle = getArguments();
        if (null != bundle) {
            type = bundle.getString(Constants.TYPE);
        }

        mRecyclerZhihuSection.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerZhihuSection.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getPresenter().getContent(type, NUM, ++currentPage);
            }
        });
        mSwipeZhihuSection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().getContent(type, NUM, 1);
            }
        });

        adapter = new AIAdapter(getContext(), datas);
        mRecyclerZhihuSection.setAdapter(adapter);
        showLoading();
        getPresenter().getContent(type, NUM, currentPage);
    }

    @Override
    public void showLoading() {
        mSwipeZhihuSection.setProgressViewOffset(false, 0, DensityUtil.dip2px(getContext(), 24));
        mSwipeZhihuSection.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (null != mSwipeZhihuSection) {
            mSwipeZhihuSection.setRefreshing(false);
        }
        mRecyclerZhihuSection.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
    }

    @Override
    public void showContent(ArrayList<AIList> aiList, int page) {
        currentPage = page;
        if (currentPage == 1 && null != datas) {
            datas.clear();
        }
        datas.addAll(aiList);
        mRecyclerZhihuSection.notifyDataChange(currentPage, aiList.size() * 10);
    }

    @Override
    public void showError() {
        mRecyclerZhihuSection.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.error)
    public void onClick() {
        showLoading();
        getPresenter().getContent(type, NUM, 1);
    }
}
