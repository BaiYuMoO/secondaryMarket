package com.xw.h2market.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/6/2.
 */

public class HistorySQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库history.db中记录浏览历史的表名：history
     */
    public static final String TABLE_HISTORY = "history";

    /**
     * history表中的用户名列
     */
    public static final String COLUMN_HISTORY_INFORMATION = "information";


    /**
     * 创建history表的sql命令
     */
    private String sql_create_table_history = "create table " + TABLE_HISTORY +
            "(_id integer primary key autoincrement," + COLUMN_HISTORY_INFORMATION + ")";

    public HistorySQLiteOpenHelper(Context context) {
        super(context, "history.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_create_table_history);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
