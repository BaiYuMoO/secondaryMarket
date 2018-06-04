package com.xw.h2market;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class MySettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linear_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        init();
        initToolbar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_settings_change_pwd:
                SharedPreferences sp = getSharedPreferences("login_user", Context.MODE_PRIVATE);
                String username = sp.getString("username", null);
                if (username == null) {
                    Intent intent = new Intent(MySettingsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                } else {
                    Intent intent = new Intent(this, ChangePasswordActivity.class);
                    startActivity(intent);
                    break;
                }
        }
    }

    /**
     * 初始化顶部的Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.mySettings_toolbar);
        setSupportActionBar(toolbar);
        // 设置点击返回键的事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySettingsActivity.this.finish();
            }
        });
    }

    private void init() {
        linear_change = findViewById(R.id.my_settings_change_pwd);
        linear_change.setOnClickListener(this);
    }
}
