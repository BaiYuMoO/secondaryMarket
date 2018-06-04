package com.xw.h2market;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.xw.h2market.adapter.MyFragmentPagerAdapter;
import com.xw.h2market.fragment.Fragmentimage01;
import com.xw.h2market.fragment.Fragmentimage02;
import com.xw.h2market.fragment.Fragmentimage03;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Fragment> fragmentlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        viewPager = (ViewPager) findViewById(R.id.id_viewpager);
        SharedPreferences sp = getSharedPreferences("insert", MODE_PRIVATE);
        int i = sp.getInt("key", 0);
        if (i == 1) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            fragmentlist = new ArrayList<Fragment>();
            Fragmentimage01 f1 = new Fragmentimage01();
            Fragmentimage02 f2 = new Fragmentimage02();
            Fragmentimage03 f3 = new Fragmentimage03();
            fragmentlist.add(f1);
            fragmentlist.add(f2);
            fragmentlist.add(f3);


            viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentlist));
        }
    }

    private SharedPreferences getPreferences(String string) {
        // TODO Auto-generated method stub
        return null;
    }
}
