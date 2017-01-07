package com.huiyun.ixhuiyunaxtion.master.dao.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 场景表的DAO(废弃)
 *
 */
public class SceneDao {
	// TAG
	private static final String TAG = "RelationDao";
		
	// 表名
	public static final String TABLE_SCENE = "_scene";
	
	// 列名
	public static final String COLUMN_SCENE  = "scene";
	public static final String COLUMN_TYPE   = "type";
	public static final String COLUMN_ADDR  = "addr";
	public static final String COLUMN_OBJ = "obj";
	public static final String COLUMN_VAL  = "val";
	public static final String COLUMN_RESERVED    = "reserved"; // 预留列，目前暂时没用，以后再更新数据表的时候有可能用得上

	
	public static void createTable(SQLiteDatabase db){
		// 创建表
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SCENE +"(" +
				COLUMN_SCENE + " INTEGER NOT NULL," +
				COLUMN_TYPE + " INTEGER NOT NULL," +
				COLUMN_ADDR + " INTEGER NOT NULL," +
				COLUMN_OBJ + " INTEGER NOT NULL," +
				COLUMN_VAL + " INTEGER NOT NULL," +
				COLUMN_RESERVED + " INTEGER," +
				"CONSTRAINT input_to_output PRIMARY KEY (" + 
				COLUMN_SCENE + "," + COLUMN_ADDR + "," +
				COLUMN_OBJ + "," + COLUMN_VAL + "))";
		db.execSQL(sql);
	}
	
	public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion){
		// 删掉再新建一个的方式显然不太好，毕竟数据需要保存下来的，这里需要重写
		try {
			String sql = "DROP TABLE " + TABLE_SCENE;
			db.execSQL(sql);
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
		}
		createTable(db);
	}
	
	//------------------------------------------------------------------------
	private MySQLiteHelper helper;
	
	public SceneDao(Context context){
		this.helper = new MySQLiteHelper(context);
	}
	
	// 添加
	public void add(SceneBean bean){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_SCENE, bean.getScene());
		cv.put(COLUMN_TYPE, bean.getType());
		cv.put(COLUMN_ADDR, bean.getAddr());
		cv.put(COLUMN_OBJ, bean.getObj());
		cv.put(COLUMN_VAL, bean.getVal());
		db.insert(TABLE_SCENE, null, cv);
	}
	
	// 删除
	public void delete(SceneBean bean){
		SQLiteDatabase db = helper.getWritableDatabase();
		String whereClause = COLUMN_SCENE + " = " + bean.getScene() + " AND " +
				COLUMN_TYPE + " = " + bean.getType() + " AND " +
				COLUMN_ADDR + " = " + bean.getAddr() + " AND " +
				COLUMN_OBJ + " = " + bean.getObj() + " AND " +
				COLUMN_VAL + " = " + bean.getVal();
		db.delete(TABLE_SCENE, whereClause, null);
	}
	
	// 删除指定场景的所有记录
	public void delete(int scene){
		SQLiteDatabase db = helper.getWritableDatabase();
		String whereClause = COLUMN_SCENE + " = " + scene;
		db.delete(TABLE_SCENE, whereClause, null);
	}
	
	// 获取指定场景的所有记录
	public List<SceneBean> findAllRecordByScene(int scene){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<SceneBean> list = new ArrayList<SceneBean>();
		
		String sql = "SELECT * FROM " + TABLE_SCENE + " WHERE " +
				COLUMN_SCENE + " = " + scene;
		Cursor c = db.rawQuery(sql, null);
		
		while(c.moveToNext()){
			SceneBean bean = new SceneBean();
			bean.setScene(c.getInt(c.getColumnIndex(COLUMN_SCENE)));
			bean.setType(c.getInt(c.getColumnIndex(COLUMN_TYPE)));
			bean.setAddr(c.getInt(c.getColumnIndex(COLUMN_ADDR)));
			bean.setObj(c.getInt(c.getColumnIndex(COLUMN_OBJ)));
			bean.setVal(c.getInt(c.getColumnIndex(COLUMN_VAL)));
			list.add(bean);
		}
		
		return list;
	}
	
	// 获取所有场景号
	public List<Integer> findAllScene(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Integer> list = new ArrayList<Integer>();
		
		String sql = "SELECT DISTINCT " + COLUMN_SCENE + " FROM " + TABLE_SCENE + 
				" ORDER BY " + COLUMN_SCENE;
		Cursor c = db.rawQuery(sql, null);
		while(c.moveToNext()){
			int scene = c.getInt(c.getColumnIndex(COLUMN_SCENE));
			list.add(scene);
		}
		
		return list;
	}
	
	public void close(){
		helper.close();
	}
}
