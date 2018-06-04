package com.xw.h2market;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.h2market.fragment.HomeFragment;
import com.xw.h2market.fragment.MyFragment;
import com.xw.h2market.fragment.SearchFragment;
import com.xw.h2market.util.FragmentSwitchTool;
import com.xw.h2market.util.MyImageLoader;

import cn.bmob.v3.Bmob;


public class MainActivity extends AppCompatActivity {
    private LinearLayout main_home, main_search, main_my;
    private ImageView main_iv_home, main_iv_search, main_iv_my;
    private TextView main_tv_home, main_tv_search, main_tv_my;
    private FragmentSwitchTool tool;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyImageLoader.init(this);
        Bmob.initialize(this, "026b4e93388c020e370c97d6e31b2d1f");
        initView();
        tool = new FragmentSwitchTool(getSupportFragmentManager(), R.id.main_Container);
        tool.setClickableViews(main_home, main_search, main_my);
        tool.addSelectedViews(new View[]{main_iv_home, main_tv_home})
                .addSelectedViews(new View[]{main_iv_search, main_tv_search})
                .addSelectedViews(new View[]{main_iv_my, main_tv_my});
        tool.setFragments(HomeFragment.class, SearchFragment.class, MyFragment.class);
        tool.changeTag(main_home);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //初始化控件
    private void initView() {
        main_home = findViewById(R.id.main_home);
        main_search = findViewById(R.id.main_search);
        main_my = findViewById(R.id.main_my);

        main_iv_home = findViewById(R.id.main_iv_home);
        main_iv_search = findViewById(R.id.main_iv_search);
        main_iv_my = findViewById(R.id.main_iv_my);

        main_tv_home = findViewById(R.id.main_tv_home);
        main_tv_search = findViewById(R.id.main_tv_search);
        main_tv_my = findViewById(R.id.main_tv_my);
    }
}
