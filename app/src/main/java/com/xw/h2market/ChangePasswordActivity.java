package com.xw.h2market;

import android.content.Context;
import android.content.SharedPreferences;
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
import cn.bmob.v3.listener.UpdateListener;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_pwd, edt_new_pwd, edt_affirm_pwd;
    private Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        initToolbar();
    }

    @Override
    public void onClick(View view) {
        final SharedPreferences sp = getSharedPreferences("login_user", Context.MODE_PRIVATE);
        String username = sp.getString("username", null);
        String password = sp.getString("password", null);
        String pwd = edt_pwd.getText().toString().trim();
        final String newPwd = edt_new_pwd.getText().toString().trim();
        String affirmPwd = edt_affirm_pwd.getText().toString().trim();
        if (pwd.isEmpty()) {
            Toast.makeText(this, "原密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (!pwd.equals(password)) {
            Toast.makeText(this, "原密码不正确", Toast.LENGTH_SHORT).show();
            return;
        } else if (newPwd.isEmpty()) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (affirmPwd.isEmpty()) {
            Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (!newPwd.equals(affirmPwd)) {
            Toast.makeText(this, "确认密码与新密码不匹配", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String bql = "select * from User where username = ?";
            new BmobQuery<User>().doSQLQuery(bql, new SQLQueryListener<User>() {
                @Override
                public void done(BmobQueryResult<User> result, BmobException e) {
                    if (e == null) {
                        List<User> list = result.getResults();
                        if (list.size() > 0) {
                            User u = list.get(0);
                            u.setPassword(newPwd);
                            u.update(u.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("password",newPwd);
                                        editor.commit();
                                        Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Log.e("xxx",e.getMessage());
                                    }
                                }
                            });
                        }
                    }else{
                        Log.e("xx",e.getMessage());
                    }
                }
            },username);
        }
    }

    /**
     * 初始化顶部的Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.change_pwd_toolbar);
        setSupportActionBar(toolbar);
        // 设置点击返回键的事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordActivity.this.finish();
            }
        });
    }

    private void init() {
        edt_pwd = findViewById(R.id.change_pwd);
        edt_new_pwd = findViewById(R.id.change_new_pwd);
        edt_affirm_pwd = findViewById(R.id.change_affirm_pwd);
        btn_change = findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);
    }
}
