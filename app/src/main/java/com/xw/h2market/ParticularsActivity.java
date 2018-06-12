package com.xw.h2market;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.xw.h2market.db.HistoryDbOperator;
import com.xw.h2market.pojo.Information;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ParticularsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_time, tv_title, tv_price, tv_chaffer, tv_content, tv_name, tv_phone, tv_qq, tv_copy;
    private RollPagerView mViewPager;
    private Information infor;
    public ArrayList<String> urls = new ArrayList<String>();
    public ImageView view, img_call;
    private Dialog dialog;
    private Context context;
    private String inforId;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if (what == 1) {
                dialog.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particulars);
        context = ParticularsActivity.this;
        initView();
        requestTitle();
        setTitle();
        setData();
        initToolbar();
    }

    /**
     * 将状态栏设置为透明
     */
    private void requestTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setTitle() {
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("商品详细");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    //获取数据
    private void setData() {
        BmobQuery<Information> query = new BmobQuery<>();
        query.getObject(inforId, new QueryListener<Information>() {
            @Override
            public void done(Information information, BmobException e) {
                if (e == null) {
                    infor = information;
                    tv_time.setText(information.getCreatedAt());
                    tv_title.setText(information.getTitle());
                    tv_price.setText("¥ " + information.getPrice());
                    if (information.getChaffer().equals("可小刀")) {
                        tv_chaffer.setSelected(true);
                    }
                    tv_content.setText(information.getContent());
                    tv_name.setText(information.getName());
                    tv_phone.setText(information.getPhone());
                    tv_qq.setText(information.getQq());
                    insertHistory(infor);
                    setImg();
                }
            }
        });
    }

    /**
     * 设置图片轮显
     */
    public void setImg() {
        if (infor.getImg1() != null) {
            urls.add(infor.getImg1().getUrl());
        }
        if (infor.getImg2() != null) {
            urls.add(infor.getImg2().getUrl());
        }
        if (infor.getImg3() != null) {
            urls.add(infor.getImg3().getUrl());
        }
        //设置播放时间间隔
        mViewPager.setPlayDelay(3500);
        mViewPager.setAnimationDurtion(500);
        mViewPager.setAdapter(new TestNormalAdapter());
        mViewPager.setHintView(new ColorPointHintView(context, Color.parseColor("#FF4081"), Color.WHITE));
    }

    private class TestNormalAdapter extends StaticPagerAdapter {

        @Override
        public View getView(ViewGroup container, final int position) {

            view = new ImageView(container.getContext());
            final String url1 = urls.get(position);
            Glide.with(context).load(url1)
                    .error(R.drawable.imageselector_photo)
                    .into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(context, R.style.image_AlertDialog_style);
                    dialog.setContentView(R.layout.dialog_layout);
                    final ImageView imageView = dialog.findViewById(R.id.dialog_img);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(url1);
                                HttpURLConnection conn = (HttpURLConnection) url
                                        .openConnection();
                                conn.setRequestMethod("GET");
                                conn.setConnectTimeout(5000);
                                int code = conn.getResponseCode();
                                if (code == 200) {
                                    InputStream in = conn.getInputStream();
                                    imageView.setImageBitmap(BitmapFactory.decodeStream(in));
                                    dialog.setCanceledOnTouchOutside(true);
                                    Window w = dialog.getWindow();
                                    WindowManager.LayoutParams lp = w.getAttributes();
                                    lp.x = 0;
                                    lp.y = 40;
                                    dialog.onWindowAttributesChanged(lp);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    Message msg = new Message();
                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                } else {
                                    Toast.makeText(ParticularsActivity.this, "请求失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

    }

    private void insertHistory(Information infor) {
        if (!HistoryDbOperator.getInstance(ParticularsActivity.this).queryData(infor)) {
            HistoryDbOperator.getInstance(ParticularsActivity.this).insertData(infor);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.particulars_img_call:
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv_phone.getText().toString()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("xx", e.getMessage());
                }
                break;

            case R.id.particulars_tv_copy:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // 得到剪贴板管理器
                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, tv_qq.getText().toString()));
                    Toast.makeText(ParticularsActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(tv_qq.getText().toString());
                    Toast.makeText(ParticularsActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }

    /**
     * 初始化顶部的Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.particulars_toolbar);
        setSupportActionBar(toolbar);
        // 设置点击返回键的事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParticularsActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        SharedPreferences sp = getSharedPreferences("login_user", Context.MODE_PRIVATE);
        String usernmae1 = sp.getString("username", null);
        if (username.equals(usernmae1)) {
            getMenuInflater().inflate(R.menu.menu_particulars, menu);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_delete) {
            Information infor = new Information();
            infor.setValue("sell_and_buy","已售");
            infor.update(inforId,new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(ParticularsActivity.this, "标记已售成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.e("xx", e.getMessage());
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);


    }

    private void initView() {
        tv_time = findViewById(R.id.particulars_tv_time);
        tv_title = findViewById(R.id.particulars_tv_title);
        tv_price = findViewById(R.id.particulars_tv_price);
        tv_chaffer = findViewById(R.id.particulars_tv_chaffer);
        tv_content = findViewById(R.id.particulars_tv_content);
        tv_name = findViewById(R.id.particulars_tv_name);
        tv_phone = findViewById(R.id.particulars_tv_phone);
        tv_qq = findViewById(R.id.particulars_tv_qq);
        tv_copy = findViewById(R.id.particulars_tv_copy);
        img_call = findViewById(R.id.particulars_img_call);
        mViewPager = findViewById(R.id.particulars_view_pager);
        img_call.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        Intent intent = getIntent();
        inforId = intent.getStringExtra("id");
    }
}
