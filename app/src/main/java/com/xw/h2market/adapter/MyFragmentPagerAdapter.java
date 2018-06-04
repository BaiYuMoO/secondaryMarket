package com.xw.h2market.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmetnmanager;  //管理器
    private List<Fragment> listfragment; //fragment集合

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> listfragment) {
        super(fm);
        this.fragmetnmanager = fm;
        this.listfragment = listfragment;
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        return listfragment.get(position); //fragment的指针
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listfragment.size();
    }

}
