package com.chs.mt.pxe_r500.db;

import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.bean.SEFF_File;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DB_SEffFile_Table {
	/**
	 * �����Ǵ���ҵ��ķ�������JAVA����ʵ����
	 * ����������Ĵ����ݿ⣬һ��Ҫ�ر����ݿ�
	 * */
	//private DataBaseOpenHelper dbOpenHelper;
	private SQLiteDatabase mDb;
	
	public DB_SEffFile_Table(Context context,SQLiteDatabase mdatabase) {
		//dbOpenHelper = mdbOpenHelper;
		mDb = mdatabase;
	}
	/*
	 * �����µ�һ�����ݣ����뵽���ݱ���	
	 */
	/**
     * ����һ������
     * @param sefffile
     */
	public void insert(SEFF_File sefffile) {
		ContentValues values = new ContentValues();
		values.put(SEFF_File.F_ID, sefffile.Get_file_id());
		values.put(SEFF_File.F_TYPE, sefffile.Get_file_type());
		values.put(SEFF_File.F_NAME, sefffile.Get_file_name());
		values.put(SEFF_File.F_PATH, sefffile.Get_file_path());
		values.put(SEFF_File.F_FAVORITE, sefffile.Get_file_favorite());
		values.put(SEFF_File.F_LOVE, sefffile.Get_file_love());
		values.put(SEFF_File.F_SIZE, sefffile.Get_file_size());
		values.put(SEFF_File.F_TIME, sefffile.Get_file_time());
		values.put(SEFF_File.F_MSG, sefffile.Get_file_msg());
		
		values.put(SEFF_File.D_USER_NAME, sefffile.Get_data_user_name());
		values.put(SEFF_File.D_MAC_TYPE, sefffile.Get_data_machine_type());
		values.put(SEFF_File.D_CAR_TYPE, sefffile.Get_data_car_type());
		values.put(SEFF_File.D_CAR_BRAND, sefffile.Get_data_car_brand());
		values.put(SEFF_File.D_GROUP_NAME, sefffile.Get_data_group_name());
		values.put(SEFF_File.D_UPLOAD_TIME, sefffile.Get_data_upload_time());
		values.put(SEFF_File.D_BRIEFING, sefffile.Get_data_eff_briefing());
		
		values.put(SEFF_File.L_SEL, sefffile.Get_list_sel());
		values.put(SEFF_File.L_IS_OPEN, sefffile.Get_list_is_open());
		
		mDb.insert(DataBaseOpenHelper.TABLE_SEff_FilE, null, values);
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
    	mDb.delete(DataBaseOpenHelper.TABLE_SEff_FilE, key+"=?", new String[] {date});
    }
    //ɾ��������������
    public void deleteAll() {
    	String sql = "SELECT * FROM " + DataBaseOpenHelper.TABLE_SEff_FilE;

	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
	    	delete("id", String.valueOf(result.getInt(0)));
	    }
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
    public void update(String key, String date,SEFF_File sefffile) {
    	ContentValues values = new ContentValues();
    	values.put(SEFF_File.F_ID, sefffile.Get_file_id());
    	values.put(SEFF_File.F_TYPE, sefffile.Get_file_type());
		values.put(SEFF_File.F_NAME, sefffile.Get_file_name());
		values.put(SEFF_File.F_PATH, sefffile.Get_file_path());
		values.put(SEFF_File.F_FAVORITE, sefffile.Get_file_favorite());
		values.put(SEFF_File.F_LOVE, sefffile.Get_file_love());
		values.put(SEFF_File.F_SIZE, sefffile.Get_file_size());
		values.put(SEFF_File.F_TIME, sefffile.Get_file_time());
		values.put(SEFF_File.F_MSG, sefffile.Get_file_msg());
		

		values.put(SEFF_File.D_USER_NAME, sefffile.Get_data_user_name());
		values.put(SEFF_File.D_MAC_TYPE, sefffile.Get_data_machine_type());
		values.put(SEFF_File.D_CAR_TYPE, sefffile.Get_data_car_type());
		values.put(SEFF_File.D_CAR_BRAND, sefffile.Get_data_car_brand());
		values.put(SEFF_File.D_GROUP_NAME, sefffile.Get_data_group_name());
		values.put(SEFF_File.D_UPLOAD_TIME, sefffile.Get_data_upload_time());
		values.put(SEFF_File.D_BRIEFING, sefffile.Get_data_eff_briefing());
		

		values.put(SEFF_File.L_SEL, sefffile.Get_list_sel());
		values.put(SEFF_File.L_IS_OPEN, sefffile.Get_list_is_open());
		
		mDb.update(DataBaseOpenHelper.TABLE_SEff_FilE, values, key+"=?", new String[] {date});
    }
    
    
    public void ResetTable() {
    	mDb.execSQL("delete from "+DataBaseOpenHelper.TABLE_SEff_FilE+";");
    	//mDb.execSQL("DELETEFROM sqlite_sequence WHERE name="+DataBaseOpenHelper.TABLE_SEff_FilE);
    	mDb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+DataBaseOpenHelper.TABLE_SEff_FilE+"'");
    }
    
    /**
     * ����һ������
     * @param key
     * @param date
     * @return
     */
    public SEFF_File find(String key ,String date) {	
    	//OK
//    	String sql = "SELECT * FROM " + DataBaseOpenHelper.TABLE_SEff_FilE;
//	    Cursor cursor = mDb.rawQuery(sql, null); // ִ�в�ѯ���
//	    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {  
//	    	System.out.println("DBTEST-SSFSS-"+"cursor=" + cursor.getInt(0));
//	    	if(cursor.getInt(0) == date){
//	    		return new SEFF_File(
//	    				cursor.getInt(0),
//	    				cursor.getString(1),
//						cursor.getString(2),
//						cursor.getString(3),
//						cursor.getString(4),
//						cursor.getString(5),
//						cursor.getString(6),
//						cursor.getString(7)
//						);
//	    	}
//	    }
    	

		Cursor cursor = mDb.query(DataBaseOpenHelper.TABLE_SEff_FilE, null, key+"=?", new String[] {date}, null, null, null);
		if(cursor != null) {
		    //cursor.moveToFirst();
		    if(cursor.moveToNext()){
		    	for(int j=0;j<cursor.getCount();j++){
		    		if(j==0){
		    			System.out.println("DBTEST--NUM:"+j + cursor.getShort(j));
		    		}else{
		    			System.out.println("DBTEST--NUM:"+j + cursor.getString(j));
		    		}		    		
		    	}
				return new SEFF_File(
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
						cursor.getString(10),
						cursor.getString(11),
						cursor.getString(12),
						cursor.getString(13),
						cursor.getString(14),
						cursor.getString(15),
						cursor.getString(16),
						cursor.getString(17),
						cursor.getString(18)
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
	public List<SEFF_File> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<SEFF_File> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<SEFF_File> getList(String sql, String[] selectionArgs) {
		ArrayList<SEFF_File> list = new ArrayList<SEFF_File>();
		SEFF_File feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new SEFF_File();
				feed = new SEFF_File();
				index = c.getColumnIndex(SEFF_File.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(SEFF_File.F_ID);
				if (index != -1) {
					feed.Set_file_id(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_TYPE);
				if (index != -1) {
					feed.Set_file_name(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_NAME);
				if (index != -1) {
					feed.Set_file_name(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_PATH);
				if (index != -1) {
					feed.Set_file_path(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_FAVORITE);
				if (index != -1) {
					feed.Set_file_favorite(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.F_LOVE);
				if (index != -1) {
					feed.Set_file_love(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.F_SIZE);
				if (index != -1) {
					feed.Set_file_size(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_TIME);
				if (index != -1) {
					feed.Set_file_time(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_MSG);
				if (index != -1) {
					feed.Set_file_msg(c.getString(index));

				}
				/////////
				index = c.getColumnIndex(SEFF_File.D_USER_NAME);
				if (index != -1) {
					feed.Set_data_user_name(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_MAC_TYPE);
				if (index != -1) {
					feed.Set_data_machine_type(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_CAR_TYPE);
				if (index != -1) {
					feed.Set_data_car_type(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_CAR_BRAND);
				if (index != -1) {
					feed.Set_data_car_brand(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_GROUP_NAME);
				if (index != -1) {
					feed.Set_data_group_name(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_UPLOAD_TIME);
				if (index != -1) {
					feed.Set_data_upload_time(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_BRIEFING);
				if (index != -1) {
					feed.Set_data_eff_briefing(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.L_SEL);
				if (index != -1) {
					feed.Set_list_sel(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.L_IS_OPEN);
				if (index != -1) {
					feed.Set_list_is_open(c.getString(index));

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
	 * ��ȡSEFF_File�������Ϣ
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

	public List<SEFF_File> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<SEFF_File> list = new ArrayList<SEFF_File>();
		// HashSet<SEFF_File> set=new HashSet<SEFF_File>();
		SEFF_File feed = null;
		if (columns == null) {

			columns = new String[] { 
				SEFF_File.T_ID, 
				SEFF_File.F_ID,
				SEFF_File.F_TYPE, 
				SEFF_File.F_NAME, 
				SEFF_File.F_PATH,
				SEFF_File.F_FAVORITE, 
				SEFF_File.F_LOVE, 
				SEFF_File.F_SIZE,
				SEFF_File.F_TIME, 
				SEFF_File.F_MSG,
				
				SEFF_File.D_USER_NAME, 
				SEFF_File.D_MAC_TYPE,
				SEFF_File.D_CAR_TYPE, 
				SEFF_File.D_CAR_BRAND,
				SEFF_File.D_GROUP_NAME, 
				SEFF_File.D_UPLOAD_TIME,
				SEFF_File.D_BRIEFING,
				
				SEFF_File.L_SEL,
				SEFF_File.L_IS_OPEN
			};
		}
		Cursor c = mDb.query(DataBaseOpenHelper.TABLE_SEff_FilE, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new SEFF_File();
				index = c.getColumnIndex(SEFF_File.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(SEFF_File.F_ID);
				if (index != -1) {
					feed.Set_file_id(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_TYPE);
				if (index != -1) {
					feed.Set_file_name(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_NAME);
				if (index != -1) {
					feed.Set_file_name(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_PATH);
				if (index != -1) {
					feed.Set_file_path(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_FAVORITE);
				if (index != -1) {
					feed.Set_file_favorite(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.F_LOVE);
				if (index != -1) {
					feed.Set_file_love(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.F_SIZE);
				if (index != -1) {
					feed.Set_file_size(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_TIME);
				if (index != -1) {
					feed.Set_file_time(c.getString(index));

				}

				index = c.getColumnIndex(SEFF_File.F_MSG);
				if (index != -1) {
					feed.Set_file_msg(c.getString(index));

				}
				/////////
				index = c.getColumnIndex(SEFF_File.D_USER_NAME);
				if (index != -1) {
					feed.Set_data_user_name(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_MAC_TYPE);
				if (index != -1) {
					feed.Set_data_machine_type(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_CAR_TYPE);
				if (index != -1) {
					feed.Set_data_car_type(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_CAR_BRAND);
				if (index != -1) {
					feed.Set_data_car_brand(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_GROUP_NAME);
				if (index != -1) {
					feed.Set_data_group_name(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_UPLOAD_TIME);
				if (index != -1) {
					feed.Set_data_upload_time(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.D_BRIEFING);
				if (index != -1) {
					feed.Set_data_eff_briefing(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.L_SEL);
				if (index != -1) {
					feed.Set_list_sel(c.getString(index));

				}
				index = c.getColumnIndex(SEFF_File.L_IS_OPEN);
				if (index != -1) {
					feed.Set_list_is_open(c.getString(index));

				}
		
				list.add(feed);

			} while (c.moveToNext());
		}

		if (c != null) {
			c.close();
			c = null;

		}
		// for (SEFF_File menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}
	
	//  ģ����ѯ  
    public List<SEFF_File>findBy(String keyword){  
        List<SEFF_File> list = new ArrayList<SEFF_File>();  
        String sql="select *from "+DataBaseOpenHelper.TABLE_SEff_FilE+
        		" where id like ? "
        		+"or file_id like ? "
        		+"or file_type like ? "
        		+"or file_name like ? "
        		+"or file_path like ?"
        		+"or file_favorite like ?"
        		+"or file_love like ?"
        		+"or file_size like ?"
        		+"or file_time like ?"
        		+"or file_msg like ?"
        		+"or data_user_name like ?"
        		+"or data_machine_type like ?"
        		+"or data_car_type like ?"
        		+"or data_car_brand like ?"
        		+"or data_group_name like ?"
        		+"or data_upload_time like ?"
        		+"or data_eff_briefing like ?"
        		+"or list_sel like ?"
        		+"or list_is_open like ?"
        		;   
         
        String contexstr[]={
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%",
        		"%"+keyword+"%"
        		};  
        Cursor result=mDb.rawQuery(sql,contexstr);  
           
        //-----sqlite�Դ���ѯ------  
//      String columns[]={"id","name","birthday"};//����lie������  
//      Cursor result=this.db.query(TABLE, columns, null, null, null, null, null);  
        //-----------------------  
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
        	SEFF_File seFF_file = new SEFF_File();
	    	seFF_file.Set_id(result.getShort(0));
	    	seFF_file.Set_file_id(result.getString(1));
	    	seFF_file.Set_file_type(result.getString(2));
	    	seFF_file.Set_file_name(result.getString(3));
	    	seFF_file.Set_file_path(result.getString(4));
	    	seFF_file.Set_file_favorite(result.getString(5));
	    	seFF_file.Set_file_love(result.getString(6));
	    	seFF_file.Set_file_size(result.getString(7));
	    	seFF_file.Set_file_time(result.getString(8));
	    	seFF_file.Set_file_msg(result.getString(9));
	    	
	    	seFF_file.Set_data_user_name(result.getString(10));
	    	seFF_file.Set_data_machine_type(result.getString(11));
	    	seFF_file.Set_data_car_type(result.getString(12));
	    	seFF_file.Set_data_car_brand(result.getString(13));
	    	seFF_file.Set_data_group_name(result.getString(14));
	    	seFF_file.Set_data_upload_time(result.getString(15));
	    	seFF_file.Set_data_eff_briefing(result.getString(16));
	    	
	    	seFF_file.Set_list_sel(result.getString(17));
	    	seFF_file.Set_list_is_open(result.getString(18));
	    	
	        list.add(seFF_file);  
        }  
        return list;  
    }  
    //  �����ֲ�ѯ  
    public List<SEFF_File>findByKeyword(String keyword, String Data){  
        List<SEFF_File> list = new ArrayList<SEFF_File>();  
        String sql="select *from "+DataBaseOpenHelper.TABLE_SEff_FilE+
        		" where "+keyword+" like ? "
        		;   
         
        String contexstr[]={
        		//"%"+keyword+"%",
        		"%"+Data+"%"
        		};  
        Cursor result=mDb.rawQuery(sql,contexstr);  
           
        //-----sqlite�Դ���ѯ------  
//      String columns[]={"id","name","birthday"};//����lie������  
//      Cursor result=this.db.query(TABLE, columns, null, null, null, null, null);  
        //-----------------------  
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
        	SEFF_File seFF_file = new SEFF_File();
	    	seFF_file.Set_id(result.getShort(0));
	    	seFF_file.Set_file_id(result.getString(1));
	    	seFF_file.Set_file_type(result.getString(2));
	    	seFF_file.Set_file_name(result.getString(3));
	    	seFF_file.Set_file_path(result.getString(4));
	    	seFF_file.Set_file_favorite(result.getString(5));
	    	seFF_file.Set_file_love(result.getString(6));
	    	seFF_file.Set_file_size(result.getString(7));
	    	seFF_file.Set_file_time(result.getString(8));
	    	seFF_file.Set_file_msg(result.getString(9));
	    	
	    	seFF_file.Set_data_user_name(result.getString(10));
	    	seFF_file.Set_data_machine_type(result.getString(11));
	    	seFF_file.Set_data_car_type(result.getString(12));
	    	seFF_file.Set_data_car_brand(result.getString(13));
	    	seFF_file.Set_data_group_name(result.getString(14));
	    	seFF_file.Set_data_upload_time(result.getString(15));
	    	seFF_file.Set_data_eff_briefing(result.getString(16));
	    	
	    	seFF_file.Set_list_sel(result.getString(17));
	    	seFF_file.Set_list_is_open(result.getString(18));
	    	
	        list.add(seFF_file);  
        }  
        return list;  
    } 
    //  �����ֲ�ѯ  
    /**
     * 
     * @param keyword �������
     * @param INC_SUB ����ʽ-false:����true������
     * @return
     * ���������� name �� salary ����sqlite> SELECT * FROM COMPANY ORDER BY NAME, SALARY ASC;
     */
    public List<SEFF_File>OrderBy(String keyword, boolean INC_SUB){  
        List<SEFF_File> list = new ArrayList<SEFF_File>();  
        String sql="select *from "+DataBaseOpenHelper.TABLE_SEff_FilE+
        		" ORDER BY "+keyword+" DESC " ;   
        if(INC_SUB){
        	sql="select *from "+DataBaseOpenHelper.TABLE_SEff_FilE+
            		" ORDER BY "+keyword+" ASC " ;   
        }        
        Cursor result=mDb.rawQuery(sql,null);  
           
        //-----------------------  
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
        	SEFF_File seFF_file = new SEFF_File();
	    	seFF_file.Set_id(result.getShort(0));
	    	seFF_file.Set_file_id(result.getString(1));
	    	seFF_file.Set_file_type(result.getString(2));
	    	seFF_file.Set_file_name(result.getString(3));
	    	seFF_file.Set_file_path(result.getString(4));
	    	seFF_file.Set_file_favorite(result.getString(5));
	    	seFF_file.Set_file_love(result.getString(6));
	    	seFF_file.Set_file_size(result.getString(7));
	    	seFF_file.Set_file_time(result.getString(8));
	    	seFF_file.Set_file_msg(result.getString(9));
	    	
	    	seFF_file.Set_data_user_name(result.getString(10));
	    	seFF_file.Set_data_machine_type(result.getString(11));
	    	seFF_file.Set_data_car_type(result.getString(12));
	    	seFF_file.Set_data_car_brand(result.getString(13));
	    	seFF_file.Set_data_group_name(result.getString(14));
	    	seFF_file.Set_data_upload_time(result.getString(15));
	    	seFF_file.Set_data_eff_briefing(result.getString(16));
	    	
	    	seFF_file.Set_list_sel(result.getString(17));
	    	seFF_file.Set_list_is_open(result.getString(18));
	    	
	        list.add(seFF_file);  
        }  
        return list;  
    } 
    public List<SEFF_File>OrderByKeyWord(String keyWordBase, String BaseWord, String keyword, boolean INC_SUB){  
        List<SEFF_File> list = new ArrayList<SEFF_File>();  
        String sql="select *from "+DataBaseOpenHelper.TABLE_SEff_FilE+
        		" where "+keyWordBase+" =  "+BaseWord+       		
        		" ORDER BY "+keyword+" DESC " ;   
        
        
        if(INC_SUB){
        	sql="select *from "+DataBaseOpenHelper.TABLE_SEff_FilE+
        			" where "+keyWordBase+" =  "+BaseWord+    
            		" ORDER BY "+keyword+" ASC " ;   
        }        
        Cursor result=mDb.rawQuery(sql,null);  
           
        //-----------------------  
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
        	SEFF_File seFF_file = new SEFF_File();
	    	seFF_file.Set_id(result.getShort(0));
	    	seFF_file.Set_file_id(result.getString(1));
	    	seFF_file.Set_file_type(result.getString(2));
	    	seFF_file.Set_file_name(result.getString(3));
	    	seFF_file.Set_file_path(result.getString(4));
	    	seFF_file.Set_file_favorite(result.getString(5));
	    	seFF_file.Set_file_love(result.getString(6));
	    	seFF_file.Set_file_size(result.getString(7));
	    	seFF_file.Set_file_time(result.getString(8));
	    	seFF_file.Set_file_msg(result.getString(9));
	    	
	    	seFF_file.Set_data_user_name(result.getString(10));
	    	seFF_file.Set_data_machine_type(result.getString(11));
	    	seFF_file.Set_data_car_type(result.getString(12));
	    	seFF_file.Set_data_car_brand(result.getString(13));
	    	seFF_file.Set_data_group_name(result.getString(14));
	    	seFF_file.Set_data_upload_time(result.getString(15));
	    	seFF_file.Set_data_eff_briefing(result.getString(16));
	    	
	    	seFF_file.Set_list_sel(result.getString(17));
	    	seFF_file.Set_list_is_open(result.getString(18));
	    	
	        list.add(seFF_file);  
        }  
        return list;  
    } 
	public List<SEFF_File> getTableList(){
		List<SEFF_File> list = new ArrayList<SEFF_File>();
		
		String sql = "SELECT * FROM " + DataBaseOpenHelper.TABLE_SEff_FilE;
	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // ����ѭ���ķ�ʽ��������
//	    	for(int j=0;j<result.getCount();j++){
//	    		System.out.println("DBTEST--NUM:"+j + result.getShort(j));
//	    	}
	    	
	    	SEFF_File seFF_file = new SEFF_File();
	    	seFF_file.Set_id(result.getShort(0));
	    	seFF_file.Set_file_id(result.getString(1));
	    	seFF_file.Set_file_type(result.getString(2));
	    	seFF_file.Set_file_name(result.getString(3));
	    	seFF_file.Set_file_path(result.getString(4));
	    	seFF_file.Set_file_favorite(result.getString(5));
	    	seFF_file.Set_file_love(result.getString(6));
	    	seFF_file.Set_file_size(result.getString(7));
	    	seFF_file.Set_file_time(result.getString(8));
	    	seFF_file.Set_file_msg(result.getString(9));
	    	
	    	seFF_file.Set_data_user_name(result.getString(10));
	    	seFF_file.Set_data_machine_type(result.getString(11));
	    	seFF_file.Set_data_car_type(result.getString(12));
	    	seFF_file.Set_data_car_brand(result.getString(13));
	    	seFF_file.Set_data_group_name(result.getString(14));
	    	seFF_file.Set_data_upload_time(result.getString(15));
	    	seFF_file.Set_data_eff_briefing(result.getString(16));
	    	
	    	seFF_file.Set_list_sel(result.getString(17));
	    	seFF_file.Set_list_is_open(result.getString(18));
	    	
	    	
	    	
	        list.add(seFF_file);

	        //printfSEFF_File(seFF_file);
	    }
//	    SEFF_File seFF_file = new SEFF_File();
//	    if(list.size()>0){
//        	for(int i=0;i<list.size();i++){        		
//        		seFF_file = list.get(i);
//        		System.out.println("DBTEST-FUCK-NUM:"+i + "-Name:" + seFF_file.Get_file_name());
//        	}
//        }
	    return list;
	}
	
	private void  printfSEFF_File(SEFF_File sefffile) {
		System.out.println("DBTEST--"+"Get_id=" + sefffile.Get_id());
		System.out.println("DBTEST--"+"Get_file_id=" + sefffile.Get_file_id());
		System.out.println("DBTEST--"+"Get_file_type=" + sefffile.Get_file_type());
		System.out.println("DBTEST--"+"Get_file_name=" + sefffile.Get_file_name());
		System.out.println("DBTEST--"+"Get_file_path=" + sefffile.Get_file_path());
		System.out.println("DBTEST--"+"Get_file_favorite=" + sefffile.Get_file_favorite());
		System.out.println("DBTEST--"+"file_love=" + sefffile.Get_file_love());
		System.out.println("DBTEST--"+"Get_file_size=" + sefffile.Get_file_size());
		System.out.println("DBTEST--"+"Get_file_time=" + sefffile.Get_file_time());
		System.out.println("DBTEST--"+"Get_file_msg=" + sefffile.Get_file_msg());
		
		System.out.println("DBTEST--"+"Get_data_user_name=" + sefffile.Get_data_user_name());
		System.out.println("DBTEST--"+"Get_data_machine_type=" + sefffile.Get_data_machine_type());
		System.out.println("DBTEST--"+"Get_data_car_type=" + sefffile.Get_data_car_type());
		System.out.println("DBTEST--"+"Get_data_car_brand=" + sefffile.Get_data_car_brand());
		System.out.println("DBTEST--"+"Get_data_group_name=" + sefffile.Get_data_group_name());
		System.out.println("DBTEST--"+"Get_data_upload_time=" + sefffile.Get_data_upload_time());
		System.out.println("DBTEST--"+"Get_data_eff_briefing=" + sefffile.Get_data_eff_briefing());
		
		System.out.println("DBTEST--"+"Get_list_sel=" + sefffile.Get_list_sel());
		System.out.println("DBTEST--"+"Get_list_is_open=" + sefffile.Get_list_is_open());
	}
}
