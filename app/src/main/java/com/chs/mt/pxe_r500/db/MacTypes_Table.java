package com.chs.mt.pxe_r500.db;

import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.bean.MacTypes;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MacTypes_Table {
	/**
	 * �����Ǵ���ҵ��ķ�������JAVA����ʵ����
	 * ����������Ĵ����ݿ⣬һ��Ҫ�ر����ݿ�
	 * */
	//private DataBaseCCMHelper dbOpenHelper;
	private SQLiteDatabase mDb;
	
	public MacTypes_Table(Context context,SQLiteDatabase mdatabase) {
		//dbOpenHelper = mdbOpenHelper;
		mDb = mdatabase;
	}
	/*
	 * �����µ�һ�����ݣ����뵽���ݱ���	
	 */
	/**
     * ����һ������
     * @param mac_types
     */
	public void insert(MacTypes mac_types) {
		ContentValues values = new ContentValues();
		values.put(MacTypes.T_CID, mac_types.Get_cid());
		values.put(MacTypes.T_NAME, mac_types.Get_name());
		mDb.insert(DataBaseCCMHelper.TABLE_MacTypes, null, values);
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
    	mDb.delete(DataBaseCCMHelper.TABLE_MacTypes, key+"=?", new String[] {date});
    }
    //ɾ��������������
    public void deleteAll() {
    	String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_MacTypes;

	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
	    	delete("id", String.valueOf(result.getInt(0)));
	    }
    }
    /**
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
    public void update(String key, String date,MacTypes mac_types) {
    	ContentValues values = new ContentValues();
 
		values.put(MacTypes.T_CID, mac_types.Get_cid());
		values.put(MacTypes.T_NAME, mac_types.Get_name());

		mDb.update(DataBaseCCMHelper.TABLE_MacTypes, values, key+"=?", new String[] {date});
    }
    
    
    public void ResetTable() {
    	mDb.execSQL("delete from "+DataBaseCCMHelper.TABLE_MacTypes+";");
    	//mDb.execSQL("DELETEFROM sqlite_sequence WHERE name="+DataBaseCCMHelper.TABLE_MacTypes);
    	mDb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+DataBaseCCMHelper.TABLE_MacTypes+"'");
    }
    
    /**
     * ����һ������
     * @param key
     * @param date
     * @return
     */
    public MacTypes find(String key ,String date) {	
    	//OK
//    	String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_MacTypes;
//	    Cursor cursor = mDb.rawQuery(sql, null); // ִ�в�ѯ���
//	    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {  
//	    	System.out.println("DBTEST-SSFSS-"+"cursor=" + cursor.getInt(0));
//	    	if(cursor.getInt(0) == date){
//	    		return new MacTypes(
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
    	

		Cursor cursor = mDb.query(DataBaseCCMHelper.TABLE_MacTypes, null, key+"=?", new String[] {date}, null, null, null);
		if(cursor != null) {
		    //cursor.moveToFirst();
		    if(cursor.moveToNext()){
		    	for(int j=0;j<cursor.getCount();j++){
		    		if(j==0){
		    			//System.out.println("DBTEST--NUM:"+j + cursor.getShort(j));
		    		}else{
		    			//System.out.println("DBTEST--NUM:"+j + cursor.getString(j));
		    		}		    		
		    	}
				return new MacTypes(
					cursor.getInt(0),
					cursor.getString(1),
					cursor.getString(2)
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
	public List<MacTypes> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<MacTypes> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<MacTypes> getList(String sql, String[] selectionArgs) {
		ArrayList<MacTypes> list = new ArrayList<MacTypes>();
		MacTypes feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new MacTypes();
				index = c.getColumnIndex(MacTypes.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(MacTypes.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}
				
				index = c.getColumnIndex(MacTypes.T_NAME);
				if (index != -1) {
					feed.Set_name(c.getString(index));

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

	public List<MacTypes> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<MacTypes> list = new ArrayList<MacTypes>();
		// HashSet<MacTypes> set=new HashSet<MacTypes>();
		MacTypes feed = null;
		if (columns == null) {

			columns = new String[] { 
				MacTypes.T_ID, 
				MacTypes.T_CID,
				MacTypes.T_NAME
			};
		}
		Cursor c = mDb.query(DataBaseCCMHelper.TABLE_MacTypes, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new MacTypes();
				index = c.getColumnIndex(MacTypes.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(MacTypes.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}

				index = c.getColumnIndex(MacTypes.T_NAME);
				if (index != -1) {
					feed.Set_name(c.getString(index));

				}
		
				list.add(feed);

			} while (c.moveToNext());
		}

		if (c != null) {
			c.close();
			c = null;

		}
		// for (MacTypes menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}
	
	//  ģ����ѯ  
    public List<MacTypes>findBy(String keyword){  
        List<MacTypes> list = new ArrayList<MacTypes>();  
        String sql="select *from "+DataBaseCCMHelper.TABLE_MacTypes+
        		" where id like ? "
        		+"or cid like ? "
        		+"or name like ?"
        		;   
         
        String contexstr[]={
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
        	MacTypes mac_types = new MacTypes();
        	
        	mac_types.Set_id(result.getShort(0));       	
        	mac_types.Set_cid(result.getString(1));
        	mac_types.Set_name(result.getString(2));

        	list.add(mac_types);  
        }  
        return list;  
    }  
    //  �����ֲ�ѯ  
    public List<MacTypes>findByKeyword(String keyword, String Data){  
        List<MacTypes> list = new ArrayList<MacTypes>();  
        String sql="select *from "+DataBaseCCMHelper.TABLE_MacTypes+
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
        	MacTypes mac_types = new MacTypes();

        	mac_types.Set_id(result.getShort(0));        	
        	mac_types.Set_cid(result.getString(1));
        	mac_types.Set_name(result.getString(2));
	    	
	        list.add(mac_types);  
        }  
        return list;  
    } 
	public List<MacTypes> getTableList(){
		List<MacTypes> list = new ArrayList<MacTypes>();
		
		String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_MacTypes;
	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // ����ѭ���ķ�ʽ��������
//	    	for(int j=0;j<result.getCount();j++){
//	    		System.out.println("DBTEST--NUM:"+j + result.getShort(j));
//	    	}
	    	
	    	MacTypes mac_types = new MacTypes();
	    	
	    	mac_types.Set_id(result.getShort(0));        	
        	mac_types.Set_cid(result.getString(1));
        	mac_types.Set_name(result.getString(2));

        	list.add(mac_types);

	        //printfSEFF_File(seFF_file);
	    }
//	    MacTypes seFF_file = new MacTypes();
//	    if(list.size()>0){
//        	for(int i=0;i<list.size();i++){        		
//        		seFF_file = list.get(i);
//        		System.out.println("DBTEST-FUCK-NUM:"+i + "-Name:" + seFF_file.Get_file_name());
//        	}
//        }
	    return list;
	}
	

}
