package com.xw.h2market;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.xw.h2market.adapter.SearchAdapter;
import com.xw.h2market.pojo.Information;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class MyGoodsActivity extends AppCompatActivity {

    private RecyclerView my_goods_recycle;
    private List<Information> informationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        init();
        initToolbar();
        getData();
    }

    private void getData() {
        SharedPreferences sp = this.getSharedPreferences("login_user", Context.MODE_PRIVATE);
        String username = sp.getString("username", null);
        if (username == null) {
            Toast.makeText(MyGoodsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobQuery<Information> query = new BmobQuery<>();
        String bql = "select * from Information where username = ? order by createdAt desc";
        query.setSQL(bql);
        query.setPreparedParams(new Object[]{username});
        query.doSQLQuery(new SQLQueryListener<Information>() {
            @Override
            public void done(BmobQueryResult<Information> result, BmobException e) {
                if (e == null) {
                    informationList = result.getResults();
                    if (informationList.size() > 0) {
                        LinearLayoutManager linearManager = new LinearLayoutManager(MyGoodsActivity.this);
                        my_goods_recycle.setLayoutManager(linearManager);
                        SearchAdapter adapter = new SearchAdapter(MyGoodsActivity.this, informationList);
                        my_goods_recycle.setAdapter(adapter);
                    } else {
                        Toast.makeText(MyGoodsActivity.this, "您未发布过商品", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyGoodsActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 初始化顶部的Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.myGoods_toolbar);
        setSupportActionBar(toolbar);
        // 设置点击返回键的事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyGoodsActivity.this.finish();
            }
        });
    }

    private void init() {
        my_goods_recycle = findViewById(R.id.my_goods_recycle);
    }
}
