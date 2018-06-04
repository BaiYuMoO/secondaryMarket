package com.xw.h2market.pojo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/5/17.
 */

public class User extends BmobObject {
    private String username;
    private String password;
    private BmobFile buddha;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BmobFile getBuddha() {
        return buddha;
    }

    public void setBuddha(BmobFile buddha) {
        this.buddha = buddha;
    }
}
