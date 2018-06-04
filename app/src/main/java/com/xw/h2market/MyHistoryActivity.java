package com.xw.h2market;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xw.h2market.adapter.SearchAdapter;
import com.xw.h2market.db.HistoryDbOperator;
import com.xw.h2market.pojo.Information;

import java.util.List;

public class MyHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView my_history_recycle;
    private TextView tv_clear;
    private List<Information> informationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        init();
        initToolbar();
        setSearchAdapter();
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyHistoryActivity.this);
        builder.setMessage("确认删除全部浏览记录？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HistoryDbOperator.getInstance(MyHistoryActivity.this).deleteData();
                        setSearchAdapter();
                    }
                })
                .setNegativeButton("取消", null);
        builder.show();
    }

    private void setSearchAdapter() {
        informationList = HistoryDbOperator.getInstance(MyHistoryActivity.this).getAllInformation();
        LinearLayoutManager linearManager = new LinearLayoutManager(MyHistoryActivity.this);
        my_history_recycle.setLayoutManager(linearManager);
        SearchAdapter adapter = new SearchAdapter(MyHistoryActivity.this, informationList);
        my_history_recycle.setAdapter(adapter);
    }


    /**
     * 初始化顶部的Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.myHistory_toolbar);
        setSupportActionBar(toolbar);
        // 设置点击返回键的事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyHistoryActivity.this.finish();
            }
        });
    }

    private void init() {
        my_history_recycle = findViewById(R.id.my_history_recycle);
        tv_clear = findViewById(R.id.my_history_tv_clear);
        tv_clear.setOnClickListener(this);
    }
}
