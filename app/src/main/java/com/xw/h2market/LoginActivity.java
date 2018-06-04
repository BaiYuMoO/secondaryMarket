package com.xw.h2market;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.h2market.pojo.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edt_name;
    private EditText edt_password;
    private TextView tv_forget_password, tv_register;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar();
        initView();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_tv_forget_password:
                Toast.makeText(LoginActivity.this, "暂不支持", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_tv_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                final String username = edt_name.getText().toString().trim();
                final String password = edt_password.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    String bql = "select * from User where username = ? and password = ?";
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.setSQL(bql);
                    query.setPreparedParams(new Object[]{username, password});
                    query.doSQLQuery(new SQLQueryListener<User>() {
                        @Override
                        public void done(BmobQueryResult<User> bmobQueryResult, BmobException e) {
                            if (e == null) {
                                List<User> list = bmobQueryResult.getResults();
                                if (list != null && list.size() > 0) {
                                    User user = list.get(0);
                                    String id = user.getObjectId();
                                    SharedPreferences sp = getSharedPreferences("login_user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("id",id);
                                    editor.putString("username",username);
                                    editor.putString("password",password);
                                    editor.commit();
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败请重试！", Toast.LENGTH_SHORT).show();
                                Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                            }
                        }
                    });
                }
                break;
        }
    }

    /**
     * 初始化顶部的Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        // 设置点击返回键的事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });
    }

    private void initView() {
        edt_name = findViewById(R.id.login_edt_name);
        edt_password = findViewById(R.id.login_edt_password);
        tv_forget_password = findViewById(R.id.login_tv_forget_password);
        tv_register = findViewById(R.id.login_tv_register);
        btn_login = findViewById(R.id.btn_login);

        tv_forget_password.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }
}
