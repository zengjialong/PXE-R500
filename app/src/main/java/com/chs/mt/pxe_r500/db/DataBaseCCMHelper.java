package com.chs.mt.pxe_r500.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseCCMHelper extends SQLiteOpenHelper {
	private static DataBaseCCMHelper mInstance = null;
	/** ���ݿ����� **/
	private static final String DATABASE_NAME  = "CCM_DataBase.db";
	
	public static final String TABLE_CarBrands = "t_carbrands";
	public static final String TABLE_CarTypes  = "t_cartypes";
	public static final String TABLE_MacTypes  = "t_cmactypes";
	public static final String TABLE_MacsAgentName  = "t_macs_agent_name";
	/** ���ݿ�汾�� **/
	private static final int DATABASE_VERSION = 2;
	/**���ݿ�SQL��� ���һ����**/
 
    private static final String CarBrands_TABLE_CREATE = "CREATE TABLE t_carbrands (id integer primary key autoincrement,"
			+"cid TEXT,"
			+"name TEXT,"
			+"img_path TEXT"
			+");";
    private static final String CarTypes_TABLE_CREATE = "CREATE TABLE t_cartypes (id integer primary key autoincrement,"
			+"cid TEXT,"
			+"brand_id TEXT,"
			+"name TEXT,"
			+"img_path TEXT"
			+");";
    private static final String MacTypes_TABLE_CREATE = "CREATE TABLE t_cmactypes (id integer primary key autoincrement,"
			+"cid TEXT,"
			+"name TEXT"
			+");";
    private static final String MacsAgentName_TABLE_CREATE = "CREATE TABLE t_macs_agent_name (id integer primary key autoincrement,"
			+"cid TEXT,"
			+"mid TEXT,"
			+"AgentID TEXT,"
			+"cname TEXT"
			+");";
    /**ɾ��һ�ű��SQL���**/

	// ���췽��������
	public DataBaseCCMHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	/**����ģʽ**/
    static synchronized DataBaseCCMHelper getInstance(Context context) {
		if (mInstance == null) {
		    mInstance = new DataBaseCCMHelper(context);
		}
		return mInstance;
    }
    /**����������ӱ�**/
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CarBrands_TABLE_CREATE);
		db.execSQL(CarTypes_TABLE_CREATE);
		db.execSQL(MacTypes_TABLE_CREATE);
		db.execSQL(MacsAgentName_TABLE_CREATE);
	}
	
	// ���·���
	/**�����õ���ǰ���ݿ�İ汾��Ϣ ��֮ǰ���ݿ�İ汾��Ϣ   �����������ݿ�**/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("EROP TABLE IF EXISTS t_carbrands");
//		db.execSQL("EROP TABLE IF EXISTS t_cartypes");
//		db.execSQL("EROP TABLE IF EXISTS t_cmactypes");
		onCreate(db);
	}
	
	/**
     * ɾ�����ݿ�
     * @param context
     * @return
     */
    public boolean deleteDatabase(Context context) {
    	return context.deleteDatabase(DATABASE_NAME);
    }
    
    
    
	
	
	
	
	
	
	
}
