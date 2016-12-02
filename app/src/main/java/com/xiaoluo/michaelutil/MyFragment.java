package com.xiaoluo.michaelutil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoluo.michaelutil.activity.CircleImageViewActivity;
import com.xiaoluo.michaelutil.activity.GifviewActivity;
import com.xiaoluo.michaelutil.activity.RoundProgressBarActivity;
import com.xiaoluo.michaelutil.activity.SwitcherViewActivity;
import com.xiaoluo.michaelutil.adapter.MyRecyclerViewAdapter;
import com.xiaoluo.michaelutil.adapter.MyStaggeredViewAdapter;
import com.xiaoluo.michaelutil.utils.SnackbarUtil;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MyRecyclerViewAdapter.OnItemClickListener, MyStaggeredViewAdapter.OnItemClickListener {

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private MyStaggeredViewAdapter mStaggeredAdapter;
    private static final int VERTICAL_LIST = 0;
    private static final int HORIZONTAL_LIST = 1;
    private static final int VERTICAL_GRID = 2;
    private static final int HORIZONTAL_GRID = 3;
    private static final int STAGGERED_GRID = 4;
    private static final int SPAN_COUNT = 2;
    private int flag = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(com.xiaoluo.michaelutil.R.layout.frag_main, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(com.xiaoluo.michaelutil.R.id.id_swiperefreshlayout);
        mRecyclerView = (RecyclerView) mView.findViewById(com.xiaoluo.michaelutil.R.id.id_recyclerview);
        flag = (int) getArguments().get("flag");
        configRecyclerView();
        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(com.xiaoluo.michaelutil.R.color.main_blue_light, com.xiaoluo.michaelutil.R.color.main_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void configRecyclerView() {
        switch (flag) {
            case VERTICAL_LIST://垂直列表
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_LIST://水平列表
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                break;
            case VERTICAL_GRID://标准9宫格列表
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_GRID://水平九宫格列表
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.HORIZONTAL, false);
                break;
            case STAGGERED_GRID://瀑布流列表
                mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
                break;
        }
        if (flag != STAGGERED_GRID) {//非瀑布流列表
            mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity());
            mRecyclerViewAdapter.setOnItemClickListener(this);
            if (flag == VERTICAL_LIST) {
                mRecyclerViewAdapter.setmType(0);
            } else {
                mRecyclerViewAdapter.setmType(1);
            }
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        } else {//gridView 显示
            mStaggeredAdapter = new MyStaggeredViewAdapter(getActivity());
            mStaggeredAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mStaggeredAdapter);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onRefresh() {
        // 刷新时模拟数据的变化
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
//                int temp = (int) (Math.random() * 10);
//                if (flag != STAGGERED_GRID) {//标准列表
//                    mRecyclerViewAdapter.mDatas.add(0, "new" + temp);
//                    mRecyclerViewAdapter.notifyDataSetChanged();
//                } else {//瀑布流列表
//                    mStaggeredAdapter.mDatas.add(0, "new" + temp);
//                    mStaggeredAdapter.mHeights.add(0, (int) (Math.random() * 300) + 200);
//                    mStaggeredAdapter.notifyDataSetChanged();
//                }
            }
        }, 1000);
    }

    @Override
    public void onItemClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView, getString(com.xiaoluo.michaelutil.R.string.item_clicked), 0);
        Intent intent = new Intent();
        switch (position) {
            case 0://半圆形进度
                intent.setClass(getActivity(), RoundProgressBarActivity.class);
                break;
            case 1://圆形ImageView
                intent.setClass(getActivity(), CircleImageViewActivity.class);
                break;
            case 2://垂直滚动TextView
                intent.setClass(getActivity(), SwitcherViewActivity.class);
                break;
            case 3://gif图片展示
                intent.setClass(getActivity(), GifviewActivity.class);
                break;
        }
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView, getString(com.xiaoluo.michaelutil.R.string.item_longclicked), 0);
    }
}
