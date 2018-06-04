package com.xw.h2market.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.xw.h2market.pojo.Information;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/2.
 */

public class HistoryDbOperator {
    /**
     * 单例对象
     */
    private static HistoryDbOperator operator;
    // 数据库打开帮助类
    private HistorySQLiteOpenHelper helper;
    // 同步代码块的钥匙
    private static Object key = new Object();
    // 数据封装对象
    private Gson gson = new Gson();

    /**
     * 私有化构造函数
     *
     * @param context
     */
    private HistoryDbOperator(Context context) {
        helper = new HistorySQLiteOpenHelper(context);
    }

    /**
     * 获取单例对象
     *
     * @param context
     * @return
     */
    public static HistoryDbOperator getInstance(Context context) {
        if (operator == null) {
            synchronized (key) {
                if (operator == null) {
                    operator = new HistoryDbOperator(context);
                }
            }
        }
        return operator;
    }

    /**
     * 查询所有的历史记录
     *
     * @return
     */
    public List<Information> getAllInformation() {
        List<Information> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(HistorySQLiteOpenHelper.TABLE_HISTORY, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(getInformation(cursor));
        }
        cursor.close();
        db.close();
        return list;
    }


    /**
     * 查询是否存在重复数据
     *
     * @param
     * @return
     */
    public boolean queryData(Information infor) {
        String information = gson.toJson(infor);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(HistorySQLiteOpenHelper.TABLE_HISTORY, null, HistorySQLiteOpenHelper.COLUMN_HISTORY_INFORMATION + "='" + information + "'", null, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 插入一个商品对象到数据库
     *
     * @param
     */
    public void insertData(Information infor) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String information = gson.toJson(infor);
        values.put(HistorySQLiteOpenHelper.COLUMN_HISTORY_INFORMATION, information);
        db.insert(HistorySQLiteOpenHelper.TABLE_HISTORY, null, values);
    }

    /**
     * 删除所有浏览记录
     */
    public void deleteData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db = helper.getWritableDatabase();
        db.execSQL("delete from " + HistorySQLiteOpenHelper.TABLE_HISTORY);
        db.close();
    }

    private Information getInformation(Cursor cursor) {
        Information infor = gson.fromJson(cursor.getString(cursor.getColumnIndex(HistorySQLiteOpenHelper.COLUMN_HISTORY_INFORMATION)), Information.class);
        return infor;
    }
}
