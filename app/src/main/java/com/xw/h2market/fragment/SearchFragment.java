package com.xw.h2market.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.h2market.R;
import com.xw.h2market.adapter.HistoryAdapter;
import com.xw.h2market.adapter.SearchAdapter;
import com.xw.h2market.db.RecordDbOperator;
import com.xw.h2market.pojo.Information;
import com.xw.h2market.pojo.Record;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;


/**
 * Created by Administrator on 2018/4/28.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText search_edt;
    private Button btn_search;
    private RecyclerView history_recycle, search_recycle;
    private TextView search_tv_clear;
    private List<Information> informationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searchfragment_layout, null);
        initView();
        setHistoryAdapter();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                String content = search_edt.getText().toString().trim();
                Record r = new Record();
                r.setName(content);
                if (content.isEmpty()) {
                } else {
                    if (!RecordDbOperator.getInstance(getActivity()).queryData(r)) {
                        RecordDbOperator.getInstance(getActivity()).insertData(r);
                        setHistoryAdapter();
                    }
                    searchResult();
                }
                break;
            case R.id.search_tv_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认删除全部历史记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RecordDbOperator.getInstance(getActivity()).deleteData();
                                setHistoryAdapter();
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
        }
    }

    private void setHistoryAdapter() {
        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        history_recycle.setLayoutManager(staggered);
        HistoryAdapter adapter = new HistoryAdapter(RecordDbOperator.getInstance(getActivity()).getAllRecord());
        history_recycle.setAdapter(adapter);
        adapter.setClickCallback(new HistoryAdapter.ClickCallback() {
            @Override
            public void click(View v, Record r) {
                search_edt.setText(r.getName());
                searchResult();
            }
        });
    }

    private void searchResult() {
        final String content = search_edt.getText().toString().trim();
        BmobQuery<Information> query = new BmobQuery<>();
        String bql = "select * from Information where order by createdAt desc";
        query.setSQL(bql);
        query.doSQLQuery(new SQLQueryListener<Information>() {
            @Override
            public void done(BmobQueryResult<Information> result, BmobException e) {
                if (e == null) {
                    informationList = getContains(result.getResults(), content);
                    if (informationList.size() > 0) {
                        LinearLayoutManager linearManager = new LinearLayoutManager(getActivity());
                        search_recycle.setLayoutManager(linearManager);
                        SearchAdapter adapter = new SearchAdapter(getActivity(), informationList);
                        search_recycle.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "没有找到相关数据哟", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请检查网络设置，稍后再重试", Toast.LENGTH_SHORT).show();
                    Log.i("xw", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 筛选符合查询关键字的条目
     *
     * @param list 所有的数据
     * @param key  关键字
     * @return 符合关键字的数据
     */
    private List<Information> getContains(List<Information> list, String key) {
        List<Information> l = new ArrayList<>();
        for (Information i : list) {
            if (i.getTitle().contains(key)) {
                l.add(i);
            }
        }
        if(l.size()==0){
            Toast.makeText(getActivity(), "没有找到相关数据哟", Toast.LENGTH_SHORT).show();
        }
        return l;

    }

    private void initView() {
        search_edt = view.findViewById(R.id.search_edt);
        history_recycle = view.findViewById(R.id.history_recycle);
        search_recycle = view.findViewById(R.id.search_recycle);
        search_tv_clear = view.findViewById(R.id.search_tv_clear);
        btn_search = view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        search_tv_clear.setOnClickListener(this);
    }
}
