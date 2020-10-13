package com.chs.mt.pxe_r500.db;

import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.bean.SEFF_Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DB_SEffData_Table {
	/**
	 * �����Ǵ���ҵ��ķ�������JAVA����ʵ����
	 * ����������Ĵ����ݿ⣬һ��Ҫ�ر����ݿ�
	 * */
	//private DataBaseOpenHelper dbOpenHelper;
	private SQLiteDatabase mDb;
	
	public DB_SEffData_Table(Context context,SQLiteDatabase mdatabase) {
		//dbOpenHelper = mdbOpenHelper;
		mDb = mdatabase;
	}
	/*
	 * �����µ�һ�����ݣ����뵽���ݱ���	
	 */
	/**
     * ����һ������
     * @param seffData
     */
	public void insert(SEFF_Data seffData) {
		ContentValues values = new ContentValues();
		values.put(SEFF_Data.T_CID, seffData.Get_cid());
		values.put(SEFF_Data.T_UID, seffData.Get_uid());
		values.put(SEFF_Data.T_HIGHDATA, seffData.Get_highData());
		values.put(SEFF_Data.T_VERSION, seffData.Get_version());
		values.put(SEFF_Data.T_TYPE, seffData.Get_type());
		values.put(SEFF_Data.T_CTIME, seffData.Get_ctime());
		values.put(SEFF_Data.T_BRAND, seffData.Get_brand());
		values.put(SEFF_Data.T_MACMODEL, seffData.Get_macModel());
		values.put(SEFF_Data.T_DETAILS, seffData.Get_details());
		values.put(SEFF_Data.T_EFFNAME, seffData.Get_effName());
		
		mDb.insert(DataBaseOpenHelper.TABLE_SEff_Data, null, values);
	}
	 /**
     * ɾ��һ������
     * @param key
     * @param date
     * 
     *  //ɾ������   
		String whereClause = "id=?";   
		//ɾ����������   
		String[] whereArgs = {String.valueOf(2)};   
		//ִ��ɾ��   
		db.delete("stu_table",whereClause,whereArgs); 
     */	
    public void delete(String key, String date) {
    	mDb.delete(DataBaseOpenHelper.TABLE_SEff_Data, key+"=?", new String[] {date});
    }
    
    /**
     * ����һ������
     *
     *  private void update(SQLiteDatabase db) {   
		//ʵ��������ֵ ContentValues values = new ContentValues();   
		//��values���������   
		values.put("snumber","101003");   
		//�޸�����   
		String whereClause = "id=?";   
		//�޸���Ӳ���   
		String[] whereArgs={String.valuesOf(1)};   
		//�޸�   
		db.update("usertable",values,whereClause,whereArgs);   
		}   
     */
    public void update(String key, String date,SEFF_Data seffData) {
    	ContentValues values = new ContentValues();
    	values.put(SEFF_Data.T_CID, seffData.Get_cid());
		values.put(SEFF_Data.T_UID, seffData.Get_uid());
		values.put(SEFF_Data.T_HIGHDATA, seffData.Get_highData());
		values.put(SEFF_Data.T_VERSION, seffData.Get_version());
		values.put(SEFF_Data.T_TYPE, seffData.Get_type());
		values.put(SEFF_Data.T_CTIME, seffData.Get_ctime());
		values.put(SEFF_Data.T_BRAND, seffData.Get_brand());
		values.put(SEFF_Data.T_MACMODEL, seffData.Get_macModel());
		values.put(SEFF_Data.T_DETAILS, seffData.Get_details());
		values.put(SEFF_Data.T_EFFNAME, seffData.Get_effName());
		mDb.update(DataBaseOpenHelper.TABLE_SEff_Data, values, key+"=?", new String[] {date});
    }
    
    /**
     * ����һ������
     * @param key
     * @param date
     * @return
     */
    public SEFF_Data find(String key ,String date) {	
		Cursor cursor = mDb.query(DataBaseOpenHelper.TABLE_SEff_Data, null, key+"=?", new String[] {date}, null, null, null);
		if(cursor != null) {
		    //cursor.moveToFirst();
		    if(cursor.moveToNext()){
				return new SEFF_Data(
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3),
						cursor.getString(4),
						cursor.getString(5),
						cursor.getString(6),
						cursor.getString(7),
						cursor.getString(8),
						cursor.getString(9),
						cursor.getString(10)
						);
			}
			return null;
		}		
		return null;
    }

	
    /**
	 * ��ȡ���м�¼
	 * 
	 * @param orderBy
	 * @param limit
	 * @return
	 */
	public List<SEFF_Data> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<SEFF_Data> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<SEFF_Data> getList(String sql, String[] selectionArgs) {
		ArrayList<SEFF_Data> list = new ArrayList<SEFF_Data>();
		SEFF_Data feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new SEFF_Data();
				index = c.getColumnIndex(SEFF_Data.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(SEFF_Data.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_UID);
				if (index != -1) {
					feed.Set_uid(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_HIGHDATA);
				if (index != -1) {
					feed.Set_highData(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_VERSION);
				if (index != -1) {
					feed.Set_version(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_TYPE);
				if (index != -1) {
					feed.Set_type(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_CTIME);
				if (index != -1) {
					feed.Set_ctime(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_BRAND);
				if (index != -1) {
					feed.Set_brand(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_MACMODEL);
				if (index != -1) {
					feed.Set_macModel(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_DETAILS);
				if (index != -1) {
					feed.Set_details(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_EFFNAME);
				if (index != -1) {
					feed.Set_effName(c.getString(index));

				}
				
				list.add(feed);

			} while (c.moveToNext());
		}

		if (c != null) {
			c.close();
			c = null;

		}

		return list;
	}

	/**
	 * ��ȡSEFF_Data�������Ϣ
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return
	 */

	public List<SEFF_Data> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<SEFF_Data> list = new ArrayList<SEFF_Data>();
		// HashSet<SEFF_Data> set=new HashSet<SEFF_Data>();
		SEFF_Data feed = null;
		if (columns == null) {

			columns = new String[] { 
				SEFF_Data.T_ID, 
				SEFF_Data.T_CID,
				SEFF_Data.T_UID, 
				SEFF_Data.T_HIGHDATA,
				SEFF_Data.T_VERSION, 
				SEFF_Data.T_TYPE,
				SEFF_Data.T_CTIME, 
				SEFF_Data.T_BRAND,
				SEFF_Data.T_MACMODEL, 
				SEFF_Data.T_DETAILS,
				SEFF_Data.T_EFFNAME, 
			};
		}
		Cursor c = mDb.query(DataBaseOpenHelper.TABLE_SEff_Data, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new SEFF_Data();
				index = c.getColumnIndex(SEFF_Data.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(SEFF_Data.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_UID);
				if (index != -1) {
					feed.Set_uid(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_HIGHDATA);
				if (index != -1) {
					feed.Set_highData(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_VERSION);
				if (index != -1) {
					feed.Set_version(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_TYPE);
				if (index != -1) {
					feed.Set_type(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_CTIME);
				if (index != -1) {
					feed.Set_ctime(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_BRAND);
				if (index != -1) {
					feed.Set_brand(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_MACMODEL);
				if (index != -1) {
					feed.Set_macModel(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_DETAILS);
				if (index != -1) {
					feed.Set_details(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_Data.T_EFFNAME);
				if (index != -1) {
					feed.Set_effName(c.getString(index));

				}
				
				list.add(feed);

			} while (c.moveToNext());
		}

		if (c != null) {
			c.close();
			c = null;

		}
		// for (SEFF_Data menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}
	
	
	
	
}
