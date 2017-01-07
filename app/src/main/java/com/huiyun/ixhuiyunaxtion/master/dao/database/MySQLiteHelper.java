package com.huiyun.ixhuiyunaxtion.master.dao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 继承自SQLiteOpenHelper，本身会被DAO调用
 * 用于Axtion.db上的数据表
 *
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
	
	// 数据库名
	private static String DATABASE_NAME = "Axtion.db";
	
	// 数据库版本号
	private static int VERSION = 2;

	public MySQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, VERSION);
	}

	// 当数据库未被创建时，调用该函数
	@Override
	public void onCreate(SQLiteDatabase db) {
		ConfigureDao.createTable(db);
		RelationDao.createTable(db);
		SceneDao.createTable(db);
	}

	// 当数据库版本号大于当前的版本号时，调用该函数更新
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		RelationDao.updateTable(db, oldVersion, newVersion);
		SceneDao.updateTable(db, oldVersion, newVersion);
	}
}
