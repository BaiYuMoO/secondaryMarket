package com.xw.h2market;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.h2market.adapter.ImageAdapter;
import com.xw.h2market.pojo.Information;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class IssueActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioGroup sell_and_buy;
    private RadioButton sell, buy;
    private EditText edt_title, edt_price, edt_name, edt_phone, edt_qq, edt_content;
    private TextView tv_sell, tv_buy, tv_titleNumber, tv_contentNumber, tv_add_image, tv_jd;
    private Spinner spn;
    private Switch switch_Chaffer;
    private RecyclerView recycler;
    private Button btn_issue;
    private ArrayList<String> path = new ArrayList<>();//存放图片地址
    private ProgressBar progressBar;
    private ImageAdapter adapter;
    private SharedPreferences sp;
    public static final int REQUEST_CODE = 1000;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String sb, type, chaffer = "不可小刀", username;//sb用于保存出售还是求购,type用于保存什么类型,chaffer用于保存是否可小刀(默认不可小刀),username用于保存是哪个用户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        initToolbar();
        initView();
    }

    /**
     * 初始化顶部的Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.issue_toolbar);
        setSupportActionBar(toolbar);
        // 设置点击返回键的事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IssueActivity.this.finish();
            }
        });
    }

    //监听RadioGroup
    private void getRadioButtonText() {
        if (buy.getId() == sell_and_buy.getCheckedRadioButtonId()) {
            sb = tv_buy.getText().toString();
        } else if (sell.getId() == sell_and_buy.getCheckedRadioButtonId()) {
            sb = tv_sell.getText().toString();
        }
        sell_and_buy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == buy.getId()) {
                    sb = tv_buy.getText().toString();
                } else if (i == sell.getId()) {
                    sb = tv_sell.getText().toString();
                }
            }
        });

    }

    //监听类型（Spinner）
    private void spinnerSelectedListenner() {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //监听是否可小刀（switch）
    private void getChaffer() {
        switch_Chaffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    chaffer = "可小刀";
                } else {
                    chaffer = "不可小刀";
                }
            }
        });
    }

    //监听标题输入框中的文字
    private void TextChanged() {
        edt_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String title = edt_title.getText().toString();
                tv_titleNumber.setText(title.length() + "/20");
            }
        });

        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = edt_content.getText().toString();
                tv_contentNumber.setText(content.length() + "/100");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issue_tv_add_image:
                initPermissions();
                break;
            case R.id.btn_issue:
                saveInformation();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            path.clear();
            path.addAll(pathList);
            adapter.notifyDataSetChanged();
        }
    }

    //开启选择图片视图
    private void openImg() {
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
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                .mutiSelect()
//                        .crop()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(3)
                // 已选择的图片路径
                .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();
        ImageSelector.open(IssueActivity.this, imageConfig);   // 开启图片选择器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(gridLayoutManager);
        adapter = new ImageAdapter(this, path);
        recycler.setAdapter(adapter);
    }

    //上传物品信息到bmob数据库
    private void saveInformation() {
        btn_issue.setEnabled(false);
        btn_issue.setText("发布中...");
        sp = this.getSharedPreferences("login_user", Context.MODE_PRIVATE);
        username = sp.getString("username", null);
        String title = edt_title.getText().toString();
        String content = edt_content.getText().toString();
        String price = edt_price.getText().toString();
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String qq = edt_qq.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            btn_issue.setEnabled(true);
            btn_issue.setText("发布");
        } else if (price.isEmpty()) {
            Toast.makeText(this, "请输入价格", Toast.LENGTH_SHORT).show();
            btn_issue.setEnabled(true);
            btn_issue.setText("发布");
        } else if (name.isEmpty()) {
            Toast.makeText(this, "请输入联系人姓名", Toast.LENGTH_SHORT).show();
            btn_issue.setEnabled(true);
            btn_issue.setText("发布");
        } else if (phone.isEmpty() && qq.isEmpty()) {
            Toast.makeText(this, "请至少填入一个联系方式", Toast.LENGTH_SHORT).show();
            btn_issue.setEnabled(true);
            btn_issue.setText("发布");
        } else if (content.isEmpty()) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            btn_issue.setEnabled(true);
            btn_issue.setText("发布");
        } else if (path.size() == 0) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            btn_issue.setEnabled(true);
            btn_issue.setText("发布");
        } else {
            final Information information = new Information();
            information.setUsername(username);
            information.setTitle(title);
            information.setContent(content);
            information.setPrice(price);
            information.setName(name);
            information.setPhone(phone);
            information.setQq(qq);
            information.setSell_and_buy(sb);
            information.setType(type);
            information.setChaffer(chaffer);
            information.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                    } else {
                        Toast.makeText(IssueActivity.this, "发布失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        Log.i("xw", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });


            //上传物品图片
            String[] filePaths = new String[path.size()];
            for (int p = 0; p < path.size(); p++) {
                filePaths[p] = path.get(p).toString();
            }
            BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                int position = 0; //file指标
                int p = 1;

                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                    //2、urls-上传文件的完整url地址
                    information.setValue("img" + p, list.get(position));
                    information.update(information.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                tv_jd.setText("上传完成");
                                finish();
                            } else {
                                Toast.makeText(IssueActivity.this, "发布失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                Log.i("xw", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                    position++;
                    p++;
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    tv_jd.setText("正在上传第" + curIndex + "张  " + curPercent + "%");
                    progressBar.setProgress(curPercent);
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    Toast.makeText(IssueActivity.this,"上传失败" + errormsg,Toast.LENGTH_SHORT).show();
                    btn_issue.setEnabled(true);
                    btn_issue.setText("发布");
                }
            });
        }
    }

    public void initPermissions() {
        if (ContextCompat.checkSelfPermission(IssueActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("xw", "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(IssueActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("xw", "拒绝过了");
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(IssueActivity.this, "请在 设置-应用管理 中开启此应用的存储授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("xw", "进行授权");
                ActivityCompat.requestPermissions(IssueActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
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

    private void initView() {
        sell_and_buy = findViewById(R.id.sell_and_buy);
        sell = findViewById(R.id.issue_rb_sell);
        buy = findViewById(R.id.issue_rb_buy);
        tv_sell = findViewById(R.id.issue_tv_sell);
        tv_buy = findViewById(R.id.issue_tv_buy);
        tv_add_image = findViewById(R.id.issue_tv_add_image);
        tv_titleNumber = findViewById(R.id.issue_tv_titleNumber);
        tv_contentNumber = findViewById(R.id.issue_tv_contentNumber);
        tv_jd = findViewById(R.id.issue_tv_jd);
        edt_title = findViewById(R.id.issue_edt_title);
        edt_price = findViewById(R.id.issue_edt_price);
        edt_name = findViewById(R.id.issue_edt_name);
        edt_phone = findViewById(R.id.issue_edt_phone);
        edt_qq = findViewById(R.id.issue_edt_qq);
        edt_content = findViewById(R.id.issue_edt_content);
        progressBar = findViewById(R.id.issue_progress_progressBar);
        spn = findViewById(R.id.issue_spinner);
        switch_Chaffer = findViewById(R.id.issue_switch_chaffer);
        recycler = findViewById(R.id.issue_recycler);
        btn_issue = findViewById(R.id.btn_issue);
        tv_add_image.setOnClickListener(this);
        btn_issue.setOnClickListener(this);
        getRadioButtonText();
        TextChanged();
        spinnerSelectedListenner();
        getChaffer();
    }
}
