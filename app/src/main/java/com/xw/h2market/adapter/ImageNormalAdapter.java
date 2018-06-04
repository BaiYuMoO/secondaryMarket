package com.xw.h2market.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.xw.h2market.R;

/**
 * Created by Administrator on 2018/5/24.
 */

public class ImageNormalAdapter extends StaticPagerAdapter {
    /*private ImageNormalAdapter(String... url){
        for ()
    }*/
    int[] imgs = new int[]{
            R.drawable.guide_image1,
            R.drawable.guide_image2,
            R.drawable.guide_image3,
    };

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setImageResource(imgs[position]);
        return view;
    }


    @Override
    public int getCount() {
        return imgs.length;
    }
}