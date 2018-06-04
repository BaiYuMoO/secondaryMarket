package com.xw.h2market.pojo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/5/19.
 */

public class Information extends BmobObject {
    private String username;
    private String title;
    private String content;
    private String price;
    private String name;
    private String phone;
    private String qq;
    private String sell_and_buy;
    private String type;
    private String chaffer;
    private BmobFile img1;
    private BmobFile img2;
    private BmobFile img3;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSell_and_buy() {
        return sell_and_buy;
    }

    public void setSell_and_buy(String sell_and_buy) {
        this.sell_and_buy = sell_and_buy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChaffer() {
        return chaffer;
    }

    public void setChaffer(String chaffer) {
        this.chaffer = chaffer;
    }

    public BmobFile getImg1() {
        return img1;
    }

    public void setImg1(BmobFile img1) {
        this.img1 = img1;
    }

    public BmobFile getImg2() {
        return img2;
    }

    public void setImg2(BmobFile img2) {
        this.img2 = img2;
    }

    public BmobFile getImg3() {
        return img3;
    }

    public void setImg3(BmobFile img3) {
        this.img3 = img3;
    }
}
