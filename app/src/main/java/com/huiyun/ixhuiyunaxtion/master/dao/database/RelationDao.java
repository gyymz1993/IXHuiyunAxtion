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
 * 输入设备 & 输出设备的关联表(_Relation)的DAO
 *
 */
public class RelationDao {
	// TAG
	private static final String TAG = "RelationDao";
	
	// 表名
	public static final String TABLE_RELATION = "_relation";
	
	// 列名
	public static final String COLUMN_INPUT_ADDR  = "input_addr";
	public static final String COLUMN_INPUT_NUM   = "input_obj";
	public static final String COLUMN_INPUT_TYPE  = "input_type";
	public static final String COLUMN_OUTPUT_ADDR = "output_addr";
	public static final String COLUMN_OUTPUT_NUM  = "output_obj";
	public static final String COLUMN_OUTPUT_TYPE = "output_type";
	public static final String COLUMN_SCENE_NAME  = "scene_name";

	
	 /**
	 *  Function:如果数据表不存在，创建一个数据表
	 *  @param db
	 */
	public static void createTable(SQLiteDatabase db){
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_RELATION +"(" +
				COLUMN_INPUT_TYPE  + " INTEGER NOT NULL," +
				COLUMN_INPUT_ADDR  + " INTEGER NOT NULL," +
				COLUMN_INPUT_NUM   + " INTEGER NOT NULL," +
				COLUMN_OUTPUT_TYPE + " INTEGER NOT NULL," +
				COLUMN_OUTPUT_ADDR + " INTEGER NOT NULL," +
				COLUMN_OUTPUT_NUM  + " INTEGER NOT NULL," +
				COLUMN_SCENE_NAME  + " VARCHAR(30),"      +
				"CONSTRAINT unique_input PRIMARY KEY ("   + 
				COLUMN_INPUT_ADDR + "," + COLUMN_INPUT_NUM + "))";
		db.execSQL(sql);
	}
	
	
	 /**
	 *  Function:更新数据表
	 *  注:当前的操作是直接把旧数据表删除后新建数据表
	 *  @param db
	 *  @param oldVersion
	 *  @param newVersion
	 */
	public static void updateTable(final SQLiteDatabase db, int oldVersion, int newVersion){
		try {
			String sql = "DROP TABLE " + TABLE_RELATION;
			db.execSQL(sql);
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
		}
		
		createTable(db);
	}
	
	private MySQLiteHelper helper;
	
	public RelationDao(Context context){
		this.helper = new MySQLiteHelper(context);
	}
	
	 /**
	 *  Function:添加关联记录
	 *  @param bean 要添加的记录
	 */
	public void add(RelationBean bean){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_INPUT_TYPE, bean.getInputType());
		cv.put(COLUMN_INPUT_ADDR, bean.getInputAddr());
		cv.put(COLUMN_INPUT_NUM, bean.getInputObj());
		cv.put(COLUMN_OUTPUT_TYPE, bean.getOutputType());
		cv.put(COLUMN_OUTPUT_ADDR, bean.getOutputAddr());
		cv.put(COLUMN_OUTPUT_NUM, bean.getOutputObj());
		cv.put(COLUMN_SCENE_NAME, bean.getSceneName());
		db.insert(TABLE_RELATION, null, cv);
	}
	
	 /**
	 *  Function:根据设备的地址和编号删除指定关联记录
	 *  @param addr 设备的地址
	 *  @param num 设备的编号
	 *  @param isInput true表示根据输入设备，false表示根据输出设备
	 */
	public void delete(int addr, int num, boolean isInput){
		SQLiteDatabase db = helper.getWritableDatabase();
		if(isInput){
			// 根据输入设备
			String whereClause = COLUMN_INPUT_ADDR + " = " + addr + " AND " +
					COLUMN_INPUT_NUM + " = " + num;
			db.delete(TABLE_RELATION, whereClause, null);
		} else {
			// 根据输出设备
			String whereClause = COLUMN_OUTPUT_ADDR + " = " + addr + " AND " +
					COLUMN_OUTPUT_NUM + " = " + num;
			db.delete(TABLE_RELATION, whereClause, null);
		}
	}
	
	 /**
	 *  Function:根据输入设备的地址和编号，找出所有对应关联记录
	 *  @param inputAddr
	 *  @param inputNum
	 *  @return
	 */
	public List<RelationBean> findAllByInput(int inputAddr, int inputNum){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<RelationBean> list = new ArrayList<RelationBean>();
		
		String sql = "SELECT * FROM " + TABLE_RELATION + " WHERE " +
					COLUMN_INPUT_ADDR + " = " + inputAddr + " AND " +
					COLUMN_INPUT_NUM + " = " + inputNum;
		Cursor c = db.rawQuery(sql, null);
		
		while(c.moveToNext()){
			RelationBean bean = new RelationBean();
			bean.setInputType(c.getInt(c.getColumnIndex(COLUMN_INPUT_TYPE)));
			bean.setInputAddr(c.getInt(c.getColumnIndex(COLUMN_INPUT_ADDR)));
			bean.setInputObj(c.getInt(c.getColumnIndex(COLUMN_INPUT_NUM)));
			bean.setOutputType(c.getInt(c.getColumnIndex(COLUMN_OUTPUT_TYPE)));
			bean.setOutputAddr(c.getInt(c.getColumnIndex(COLUMN_OUTPUT_ADDR)));
			bean.setOutputObj(c.getInt(c.getColumnIndex(COLUMN_OUTPUT_NUM)));
			bean.setSceneName(c.getString(c.getColumnIndex(COLUMN_SCENE_NAME)));
			list.add(bean);
		}
		
		return list;
	}
	
	 /**
	 *  Function:根据输出设备的地址和编号，找出所有对应关联记录
	 *  @param outputAddr
	 *  @param outputNum
	 *  @return
	 */
	public List<RelationBean> findAllByOutput(int outputAddr, int outputNum){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<RelationBean> list = new ArrayList<RelationBean>();
		
		String sql = "SELECT * FROM " + TABLE_RELATION + " WHERE " +
					COLUMN_OUTPUT_ADDR + " = " + outputAddr + " AND " +
					COLUMN_OUTPUT_NUM + " = " + outputNum;
		Cursor c = db.rawQuery(sql, null);
		
		while(c.moveToNext()){
			RelationBean bean = new RelationBean();
			bean.setInputType(c.getInt(c.getColumnIndex(COLUMN_INPUT_TYPE)));
			bean.setInputAddr(c.getInt(c.getColumnIndex(COLUMN_INPUT_ADDR)));
			bean.setInputObj(c.getInt(c.getColumnIndex(COLUMN_INPUT_NUM)));
			list.add(bean);
		}
		
		return list;
	}
	
	 /**
	 *  Function:获得所有对应关联记录
	 *  @return
	 */
	public List<RelationBean> findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<RelationBean> list = new ArrayList<RelationBean>();
		
		String sql = "SELECT * FROM " + TABLE_RELATION;
		Cursor c = db.rawQuery(sql, null);
		
		while(c.moveToNext()){
			RelationBean bean = new RelationBean();
			bean.setInputType(c.getInt(c.getColumnIndex(COLUMN_INPUT_TYPE)));
			bean.setInputAddr(c.getInt(c.getColumnIndex(COLUMN_INPUT_ADDR)));
			bean.setInputObj(c.getInt(c.getColumnIndex(COLUMN_INPUT_NUM)));
			bean.setOutputType(c.getInt(c.getColumnIndex(COLUMN_OUTPUT_TYPE)));
			bean.setOutputAddr(c.getInt(c.getColumnIndex(COLUMN_OUTPUT_ADDR)));
			bean.setOutputObj(c.getInt(c.getColumnIndex(COLUMN_OUTPUT_NUM)));
			bean.setSceneName(c.getString(c.getColumnIndex(COLUMN_SCENE_NAME)));
			list.add(bean);
		}
		
		return list;
	}
	
	
	 /**
	 *  Function:关闭
	 */
	public void close(){
		helper.close();
	}
}
