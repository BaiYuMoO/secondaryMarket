package com.xw.h2market.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/5/26.
 */

public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库temp.db中记录搜索历史的表名：records
     */
    public static final String TABLE_RECORDS = "records";

    /**
     * records表中的历史记录列
     */
    public static final String COLUMN_RECORD_HISTORY = "history";

    /**
     * 创建records表的sql命令
     */
    private String sql_create_table_records = "create table " + TABLE_RECORDS + "(_id integer primary key autoincrement," + COLUMN_RECORD_HISTORY + ")";

    public RecordSQLiteOpenHelper(Context context) {
        super(context, "temp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_create_table_records);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
