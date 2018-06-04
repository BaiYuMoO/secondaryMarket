package com.xw.h2market.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xw.h2market.R;

public class MyImageLoader {

    public static ImageLoader imgLoader;

    public static void init(Context context) {
        if (imgLoader != null) {
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnLoading(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .displayer(new RoundedBitmapDisplayer(0)) // 设置成圆角图片
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPoolSize(3)
                // 最大开启线程数
                .defaultDisplayImageOptions(options).diskCacheFileCount(30)
                .memoryCacheSize(30 * 1024 * 1024).build(); // 最大缓存空间，超过自动清空

        imgLoader = ImageLoader.getInstance(); // 创建ImageLoader对象，采用单例模式
        imgLoader.init(config); // 最后用上面的配置信息初始化对象
    }
}