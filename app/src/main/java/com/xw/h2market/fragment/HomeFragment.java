package com.xw.h2market.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xw.h2market.R;
import com.xw.h2market.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager viewPager;
    private LinearLayout home_all, home_study, home_living_goods, home_other , ll_Current;//ll_Current用于记录当前layout
    private TextView home_tv_all, home_tv_study, home_tv_living_goods, home_tv_other;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefragment_layout, null);
        initView();
        initData();
        return view;
    }


    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }

    private void changeTab(int id) {
        ll_Current.setSelected(false);
        switch (id) {
            case R.id.home_all:
                viewPager.setCurrentItem(0);
            case 0:
                home_all.setSelected(true);
                ll_Current = home_all;
                break;
            case R.id.home_study:
                viewPager.setCurrentItem(1);
            case 1:
                home_study.setSelected(true);
                ll_Current = home_study;
                break;
            case R.id.home_living_goods:
                viewPager.setCurrentItem(2);
            case 2:
                home_living_goods.setSelected(true);
                ll_Current = home_living_goods;
                break;
            case R.id.home_other:
                viewPager.setCurrentItem(3);
            case 3:
                home_other.setSelected(true);
                ll_Current = home_other;
                break;
        }
    }

    //初始化数据
    private void initData() {
        Fragment homeAllFragment = new HomeAllFragment();
        Fragment homeStudyFragment = new HomeStudyFragment();
        Fragment homeLivingGoodsFragment = new HomeLivingGoodsFragment();
        Fragment homeOtherFragment = new HomeOtherFragment();

        fragments.add(homeAllFragment);
        fragments.add(homeStudyFragment);
        fragments.add(homeLivingGoodsFragment);
        fragments.add(homeOtherFragment);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    //初始化控件
    private void initView() {
        viewPager = view.findViewById(R.id.viewPager);
        home_all = view.findViewById(R.id.home_all);
        home_study = view.findViewById(R.id.home_study);
        home_living_goods = view.findViewById(R.id.home_living_goods);
        home_other = view.findViewById(R.id.home_other);

        home_tv_all = view.findViewById(R.id.home_tv_all);
        home_tv_study = view.findViewById(R.id.home_tv_study);
        home_tv_living_goods = view.findViewById(R.id.home_tv_living_goods);
        home_tv_other = view.findViewById(R.id.home_tv_other);

        home_all.setOnClickListener(this);
        home_study.setOnClickListener(this);
        home_living_goods.setOnClickListener(this);
        home_other.setOnClickListener(this);
        home_all.setSelected(true);
        ll_Current = home_all;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
