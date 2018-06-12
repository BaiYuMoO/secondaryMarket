package com.xw.h2market.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xw.h2market.util.GlideLoader;
import com.xw.h2market.LoginActivity;
import com.xw.h2market.MyAboutActivity;
import com.xw.h2market.MyGoodsActivity;
import com.xw.h2market.MyHistoryActivity;
import com.xw.h2market.MySettingsActivity;
import com.xw.h2market.R;
import com.xw.h2market.pojo.User;
import com.xw.h2market.util.CircleImageView;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;


/**
 * Created by Administrator on 2018/4/28.
 */

public class MyFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button btn_my_login, btn_layout;
    private TextView tv_hint;
    private CircleImageView img_buddha;
    private LinearLayout my_linear_goods, my_linear_history, my_linear_about, my_linear_settings;
    private SharedPreferences sp;
    private String username = null;
    public static final int REQUEST_CODE = 1002;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private ArrayList<String> path = new ArrayList<>();//存放图片地址

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myfragment_layout, null);
        initView();
        getBuddha();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_img_buddha:
                if (sp.getString("id", null) == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    initPermissions();
                }
                break;
            case R.id.btn_my_login:
                if (sp.getString("username", null) == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.my_linear_goods:
                Intent intent = new Intent(getActivity(), MyGoodsActivity.class);
                startActivity(intent);
                break;
            case R.id.my_linear_history:
                Intent intent1 = new Intent(getActivity(), MyHistoryActivity.class);
                startActivity(intent1);
                break;
            case R.id.my_linear_about:
                Intent intent2 = new Intent(getActivity(), MyAboutActivity.class);
                startActivity(intent2);
                break;
            case R.id.my_linear_settings:
                Intent intent3 = new Intent(getActivity(), MySettingsActivity.class);
                startActivity(intent3);
                break;
            case R.id.my_btn_logout:
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                btn_my_login.setText("点击登录");
                btn_layout.setVisibility(View.INVISIBLE);
                img_buddha.setImageResource(R.drawable.ic_buddha);
                Toast.makeText(getActivity(), "注销成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //开启选择图片视图
    private void openImg() {
        path.clear();
        ImageConfig imageConfig = new ImageConfig.Builder(// GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.blue))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.blue))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启单选   （默认为多选）  (单选 为 singleSelect)
                .singleSelect()
                // 已选择的图片路径
                .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .build();
        ImageSelector.open(MyFragment.this, imageConfig);   // 开启图片选择器
    }

    public void initPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("xw", "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("xw", "拒绝过了");
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(getActivity(), "请在 设置-应用管理 中开启此应用的存储授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("xw", "进行授权");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Log.i("xw", "不需要授权 ");
            openImg();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("xw", "同意授权");
                openImg();
            } else {
                Log.i("xw", "拒绝授权");
            }
        }
    }

    private void getBuddha() {
        String id = sp.getString("id", null);
        if (id == null) {
            img_buddha.setImageResource(R.drawable.ic_buddha);
        } else {
            BmobQuery<User> query = new BmobQuery<>();
            query.getObject(id, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        if (user.getBuddha().getUrl() != null) {
                            Glide.with(getActivity()).load(user.getBuddha().getUrl()).into(img_buddha);
                        } else {
                            img_buddha.setImageResource(R.drawable.ic_buddha);
                        }
                    } else if (e.getErrorCode() == 9015) {
                        img_buddha.setImageResource(R.drawable.ic_buddha);
                    } else {
                        Toast.makeText(getActivity(), "获取头像失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            path.clear();
            path.addAll(pathList);
            //上传头像图片
            String[] filePaths = new String[path.size()];
            for (int p = 0; p < path.size(); p++) {
                filePaths[p] = path.get(p).toString();
            }
            BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    User user = new User();
                    user.setValue("buddha", list.get(0));
                    user.update(sp.getString("id", null), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                tv_hint.setText("");
                                img_buddha.setClickable(true);
                                getBuddha();
                            } else {
                                Toast.makeText(getActivity(), "上传头像失败，请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    img_buddha.setClickable(false);
                    tv_hint.setText("正在上传头像" + curPercent + "%");
                }

                @Override
                public void onError(int i, String s) {
                    tv_hint.setText("");
                    img_buddha.setClickable(true);
                }
            });
        }
    }

    @Override
    public void onResume() {
        getBuddha();
        if (sp.getString("username", null) != null) {
            btn_my_login.setText(sp.getString("username", null));
            btn_layout.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void initView() {
        img_buddha = view.findViewById(R.id.my_img_buddha);
        btn_my_login = view.findViewById(R.id.btn_my_login);
        tv_hint = view.findViewById(R.id.my_tv_hint);
        my_linear_goods = view.findViewById(R.id.my_linear_goods);
        my_linear_history = view.findViewById(R.id.my_linear_history);
        my_linear_about = view.findViewById(R.id.my_linear_about);
        my_linear_settings = view.findViewById(R.id.my_linear_settings);
        btn_layout = view.findViewById(R.id.my_btn_logout);
        btn_my_login.setOnClickListener(this);
        img_buddha.setOnClickListener(this);
        my_linear_goods.setOnClickListener(this);
        my_linear_history.setOnClickListener(this);
        my_linear_about.setOnClickListener(this);
        my_linear_settings.setOnClickListener(this);
        btn_layout.setOnClickListener(this);
        btn_layout.setVisibility(View.INVISIBLE);
        sp = getActivity().getSharedPreferences("login_user", Context.MODE_PRIVATE);
        username = sp.getString("username", null);
        if (username != null) {
            btn_my_login.setText(username);
            btn_layout.setVisibility(View.VISIBLE);
        }
    }
}
