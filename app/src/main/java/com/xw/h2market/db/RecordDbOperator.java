package com.xw.h2market.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xw.h2market.pojo.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/26.
 */

public class RecordDbOperator {
    /**
     * 单例对象
     */
    private static RecordDbOperator operator;
    // 数据库打开帮助类
    private RecordSQLiteOpenHelper helper;
    // 同步代码块的钥匙
    private static Object key = new Object();

    /**
     * 私有化构造函数
     *
     * @param context
     */
    private RecordDbOperator(Context context) {
        helper = new RecordSQLiteOpenHelper(context);
    }

    /**
     * 获取单例对象
     *
     * @param context
     * @return
     */
    public static RecordDbOperator getInstance(Context context) {
        if (operator == null) {
            synchronized (key) {
                if (operator == null) {
                    operator = new RecordDbOperator(context);
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
    public List<Record> getAllRecord() {
        List<Record> rc = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(RecordSQLiteOpenHelper.TABLE_RECORDS, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            rc.add(getRecord(cursor));
        }
        cursor.close();
        db.close();
        return rc;
    }


    /**
     * 查询是否存在重复数据
     *
     * @param
     * @return
     */
    public boolean queryData(Record r) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(RecordSQLiteOpenHelper.TABLE_RECORDS, null, RecordSQLiteOpenHelper.COLUMN_RECORD_HISTORY+"='" + r.getName()+"'", null, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 插入一条历史记录到数据库
     *
     * @param
     */
    public void insertData(Record r) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordSQLiteOpenHelper.COLUMN_RECORD_HISTORY, r.getName());
        db.insert(RecordSQLiteOpenHelper.TABLE_RECORDS, null, values);
    }

    /**
     * 删除所有历史记录
     */
    public void deleteData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db = helper.getWritableDatabase();
        db.execSQL("delete from " + RecordSQLiteOpenHelper.TABLE_RECORDS);
        db.close();
    }

    private Record getRecord(Cursor cursor) {
        Record r = new Record();
        r.setId(cursor.getInt(cursor.getColumnIndex("_id")));
        r.setName(cursor.getString(cursor.getColumnIndex(RecordSQLiteOpenHelper.COLUMN_RECORD_HISTORY)));
        return r;
    }
}
