package com.huiyun.ixhuiyunaxtion.master.dao.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * configure表的DAO
 *
 */
public class ConfigureDao {
	// 表名
	public static final String TABLE_CONFIGURE = "_configure";
	
	// 列名
	public static final String COLUMN_KEY     = "config_key";
	public static final String COLUMN_VAL_STR = "config_val_str";
	public static final String COLUMN_VAL_INT = "config_val_int";
	
	// 关键词
	public static final String ROW_LOCAL_ADDR = "local_addr";
	public static final String ROW_PAN_KEY    = "pan_key";
	
	public static final String STR_NULL = "";
	public static final int    INT_NULL =  0;
	
	public static void createTable(SQLiteDatabase db){
		// 创建表
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CONFIGURE +"(" +
				COLUMN_KEY + " VARCHAR(16) PRIMARY KEY," +
				COLUMN_VAL_STR + " VARCHAR(32)," +
				COLUMN_VAL_INT + " INTEGER)";
		db.execSQL(sql);
		
		// 插入属性
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_KEY, ROW_LOCAL_ADDR);
		cv.put(COLUMN_VAL_STR, STR_NULL);
		cv.put(COLUMN_VAL_INT, 1);
		db.insert(TABLE_CONFIGURE, null, cv);
		
		cv = new ContentValues();
		cv.put(COLUMN_KEY, ROW_PAN_KEY);
		cv.put(COLUMN_VAL_STR, STR_NULL);
		cv.put(COLUMN_VAL_INT, 1);
		db.insert(TABLE_CONFIGURE, null, cv);
	}
	
	//------------------------------------------------------------------------
	private MySQLiteHelper helper;
	
	public ConfigureDao(Context context){
		this.helper = new MySQLiteHelper(context);
	}
	
	public ConfigureBean getConfigureInfo(){
		SQLiteDatabase db = helper.getReadableDatabase();
		ConfigureBean bean = new ConfigureBean();
		String sql;
		Cursor c;
		
		// LOCAL ADDR
		sql = "SELECT * FROM " + TABLE_CONFIGURE + 
			" WHERE " + COLUMN_KEY + "=?";
		c = db.rawQuery(sql, new String[]{ROW_LOCAL_ADDR});
		if(c.moveToNext()){
			bean.setLocalAddr(c.getInt(c.getColumnIndex(COLUMN_VAL_INT)));
		}
		
		// PAN KEY
		sql = "SELECT * FROM " + TABLE_CONFIGURE + 
			" WHERE " + COLUMN_KEY + "=?";
		c = db.rawQuery(sql, new String[]{ROW_PAN_KEY});
		if(c.moveToNext()){
			bean.setPanKey(c.getInt(c.getColumnIndex(COLUMN_VAL_INT)));
		}
		
		c.close();
		return bean;
	}
	
	public void updateConfigureVal(String key, String valStr, int valInt){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new  ContentValues();
		cv.put(COLUMN_KEY, key);
		cv.put(COLUMN_VAL_STR, valStr);
		cv.put(COLUMN_VAL_INT, valInt);
		db.update(TABLE_CONFIGURE, cv, COLUMN_KEY + "=?", new String[]{key});
	}
	
	public void close(){
		helper.close();
	}
	
	public void showLogInfo(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CONFIGURE, null);
		int i = 0;
		while(c.moveToNext()){
			i++;
			String name = c.getString(c.getColumnIndex(COLUMN_KEY));
			String val_str = c.getString(c.getColumnIndex(COLUMN_VAL_STR));
			int val_int = c.getInt(c.getColumnIndex(COLUMN_VAL_INT));
			String raw = COLUMN_KEY + ":" + name + "," + 
							COLUMN_VAL_INT + ":" + val_int + "," +
							COLUMN_VAL_STR + ":" + val_str;
			Log.i("第" + i + "行:", raw);
		}
	}
}
