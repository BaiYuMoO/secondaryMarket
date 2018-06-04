package com.xw.h2market;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xw.h2market.pojo.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edt_name;
    private EditText edt_password;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolbar();
        initView();
    }

    @Override
    public void onClick(View view) {
        final String username = edt_name.getText().toString().trim();
        final String password = edt_password.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            String bql = "select * from User where username = ?";
            BmobQuery<User> query = new BmobQuery<User>();
            query.setSQL(bql);
            query.setPreparedParams(new Object[]{username});
            query.doSQLQuery(new SQLQueryListener<User>() {
                @Override
                public void done(BmobQueryResult<User> bmobQueryResult, BmobException e) {
                    if (e == null) {
                        List<User> list = bmobQueryResult.getResults();
                        if (list != null && list.size() > 0) {
                            Toast.makeText(RegisterActivity.this, "该用户已存在！", Toast.LENGTH_SHORT).show();
                        } else {
                            User user = new User();
                            user.setUsername(username);
                            user.setPassword(password);
                            user.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败，请重试！", Toast.LENGTH_SHORT).show();
                        Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                    }
                }
            });

        }

    }
    /**
     * 初始化顶部的Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        // 设置点击返回键的事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });
    }

    private void initView() {
        edt_name = findViewById(R.id.register_edt_name);
        edt_password = findViewById(R.id.register_edt_password);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }
}
