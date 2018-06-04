package com.xw.h2market.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.xw.h2market.IssueActivity;
import com.xw.h2market.LoginActivity;
import com.xw.h2market.R;
import com.xw.h2market.adapter.InformationAdapter;
import com.xw.h2market.pojo.Information;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Administrator on 2018/4/28.
 */

public class HomeStudyFragment extends Fragment implements View.OnClickListener {
    private List<Information> informationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton home_study_fab;
    private View view;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //RecyclerView自定义Adapter
    private InformationAdapter adapter;

    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;
    private static final int STATE_REFRESH = 0;// 下拉刷新

    private static final int STATE_MORE = 1;// 加载更多
    private int limit = 6; // 每页的数据是10条
    private int curPage = 0; // 当前页的编号，从0开始
    private String lastTime = null; //记录最后一条item的时间

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_study_fragment_layout, null);
        initView();
        pullRefresh();
        return view;
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sp = getActivity().getSharedPreferences("login_user", Context.MODE_PRIVATE);
        String username = sp.getString("username", null);
        if (username == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), IssueActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        queryData(0, STATE_REFRESH);
        super.onResume();
    }

    private void pullRefresh() {
        //下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        //进入Activity就进行自动下拉刷新
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
        //下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 下拉刷新(从第一页开始装载数据)
                queryData(0, STATE_REFRESH);
            }
        });

        //上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                // 上拉加载更多(加载下一页数据)
                queryData(curPage, STATE_MORE);
            }
        });
    }

    /**
     * 分页获取数据
     *
     * @param page       页码
     * @param actionType ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(int page, final int actionType) {
        BmobQuery<Information> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        // 按类型是学习相关查询
        query.addWhereEqualTo("type","学习相关");
        // 如果是加载更多
        if (actionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
                Log.e("xw", date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip((page-1) * limit);
        } else {
            // 下拉刷新
            page = 0;
            query.setSkip(page);
        }

        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(new FindListener<Information>() {
            @Override
            public void done(List<Information> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        if (actionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把informationList清空，重新添加
                            curPage = 0;
                            informationList.clear();
                            // 获取最后时间
                            lastTime = list.get(list.size() - 1).getCreatedAt();

                        }
                        // 将本次查询的数据添加到informationList中
                        for (Information information : list) {
                            informationList.add(information);
                        }
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;
                        mAdapter.notifyDataSetChanged();
                        mPtrFrame.setLoadMoreEnable(true);
                    } else if (actionType == STATE_MORE) {
                        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                    } else if (actionType == STATE_REFRESH) {
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                    }
                    mPtrFrame.refreshComplete();
                    mPtrFrame.loadMoreComplete(true);
                } else {
                    Toast.makeText(getActivity(), "查询失败请检查网络", Toast.LENGTH_SHORT).show();
                    mPtrFrame.refreshComplete();
                }
            }
        });
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.study_recycler_view);
        mPtrFrame = view.findViewById(R.id.home_study_view_frame);
        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggered);
        adapter = new InformationAdapter(getActivity(), informationList);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        home_study_fab = view.findViewById(R.id.home_study_fab);
        home_study_fab.setOnClickListener(this);
    }
}
