package com.chs.mt.pxe_r500.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.bean.CarTypes;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class CarTypes_Table {
	/**
	 * �����Ǵ���ҵ��ķ�������JAVA����ʵ����
	 * ����������Ĵ����ݿ⣬һ��Ҫ�ر����ݿ�
	 * */
	//private DataBaseCCMHelper dbOpenHelper;
	private SQLiteDatabase mDb;
	
	public CarTypes_Table(Context context,SQLiteDatabase mdatabase) {
		//dbOpenHelper = mdbOpenHelper;
		mDb = mdatabase;
	}
	/*
	 * �����µ�һ�����ݣ����뵽���ݱ���	
	 */
	/**
     * ����һ������
     * @param car_types
     */
	public void insert(CarTypes car_types) {
		ContentValues values = new ContentValues();
		values.put(CarTypes.T_CID, car_types.Get_cid());
		values.put(CarTypes.T_BRAND_ID, car_types.Get_brand_id());
		values.put(CarTypes.T_NAME, car_types.Get_name());
		values.put(CarTypes.T_IMG_PATH, car_types.Get_img_path());
		mDb.insert(DataBaseCCMHelper.TABLE_CarTypes, null, values);
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
    	mDb.delete(DataBaseCCMHelper.TABLE_CarTypes, key+"=?", new String[] {date});
    }
    //ɾ��������������
    public void deleteAll() {
    	String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_CarTypes;

	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
	    	delete("id", String.valueOf(result.getInt(0)));
	    }
    }
    /**
     * ����һ������
     * @param key
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
    public void update(String key, String date,CarTypes car_types) {
    	ContentValues values = new ContentValues();
 
		values.put(CarTypes.T_CID, car_types.Get_cid());
		values.put(CarTypes.T_BRAND_ID, car_types.Get_brand_id());
		values.put(CarTypes.T_NAME, car_types.Get_name());
		values.put(CarTypes.T_IMG_PATH, car_types.Get_img_path());

		mDb.update(DataBaseCCMHelper.TABLE_CarTypes, values, key+"=?", new String[] {date});
    }
    
    
    public void ResetTable() {
    	mDb.execSQL("delete from "+DataBaseCCMHelper.TABLE_CarTypes+";");
    	//mDb.execSQL("DELETEFROM sqlite_sequence WHERE name="+DataBaseCCMHelper.TABLE_CarTypes);
    	mDb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+DataBaseCCMHelper.TABLE_CarTypes+"'");
    }
    
    /**
     * ����һ������
     * @param key
     * @param date
     * @return
     */
    public CarTypes find(String key ,String date) {	
    	//OK
//    	String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_CarTypes;
//	    Cursor cursor = mDb.rawQuery(sql, null); // ִ�в�ѯ���
//	    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {  
//	    	System.out.println("DBTEST-SSFSS-"+"cursor=" + cursor.getInt(0));
//	    	if(cursor.getInt(0) == date){
//	    		return new CarTypes(
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
    	

		Cursor cursor = mDb.query(DataBaseCCMHelper.TABLE_CarTypes, null, key+"=?", new String[] {date}, null, null, null);
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
				return new CarTypes(
					cursor.getInt(0),
					cursor.getString(1),
					cursor.getString(2),
					cursor.getString(3),
					cursor.getString(4)
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
	public List<CarTypes> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<CarTypes> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<CarTypes> getList(String sql, String[] selectionArgs) {
		ArrayList<CarTypes> list = new ArrayList<CarTypes>();
		CarTypes feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new CarTypes();
				index = c.getColumnIndex(CarTypes.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(CarTypes.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}
				
				index = c.getColumnIndex(CarTypes.T_BRAND_ID);
				if (index != -1) {
					feed.Set_brand_id(c.getString(index));

				}
				
				index = c.getColumnIndex(CarTypes.T_NAME);
				if (index != -1) {
					feed.Set_name(c.getString(index));

				}
				
				index = c.getColumnIndex(CarTypes.T_IMG_PATH);
				if (index != -1) {
					feed.Set_img_path(c.getString(index));

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

	public List<CarTypes> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<CarTypes> list = new ArrayList<CarTypes>();
		// HashSet<CarTypes> set=new HashSet<CarTypes>();
		CarTypes feed = null;
		if (columns == null) {

			columns = new String[] { 
				CarTypes.T_ID, 
				CarTypes.T_CID,
				CarTypes.T_BRAND_ID, 
				CarTypes.T_NAME,
				CarTypes.T_IMG_PATH
			};
		}
		Cursor c = mDb.query(DataBaseCCMHelper.TABLE_CarTypes, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new CarTypes();
				index = c.getColumnIndex(CarTypes.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(CarTypes.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}
				
				index = c.getColumnIndex(CarTypes.T_BRAND_ID);
				if (index != -1) {
					feed.Set_brand_id(c.getString(index));

				}
				
				index = c.getColumnIndex(CarTypes.T_NAME);
				if (index != -1) {
					feed.Set_name(c.getString(index));

				}
				
				index = c.getColumnIndex(CarTypes.T_IMG_PATH);
				if (index != -1) {
					feed.Set_img_path(c.getString(index));

				}
		
				list.add(feed);

			} while (c.moveToNext());
		}

		if (c != null) {
			c.close();
			c = null;

		}
		// for (CarTypes menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}
	
	//  ģ����ѯ  
    public List<CarTypes>findBy(String keyword){  
        List<CarTypes> list = new ArrayList<CarTypes>();  
        String sql="select *from "+DataBaseCCMHelper.TABLE_CarTypes+
        		" where id like ? "
        		+"or cid like ? "
        		+"or brand_id like ? "
        		+"or name like ?"
        		+"or img_path like ?"
        		;   
         
        String contexstr[]={
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
        	CarTypes car_types = new CarTypes();
        	
        	car_types.Set_id(result.getShort(0));
        	
        	car_types.Set_cid(result.getString(1));
        	car_types.Set_brand_id(result.getString(2));
        	car_types.Set_name(result.getString(3));
        	car_types.Set_img_path(result.getString(4));
	    	
	        list.add(car_types);  
        }  
        return list;  
    }  
    //  �����ֲ�ѯ  
    public List<CarTypes>findByKeyword(String keyword, String Data){  
        List<CarTypes> list = new ArrayList<CarTypes>();  
        String sql="select *from "+DataBaseCCMHelper.TABLE_CarTypes+
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
        	CarTypes car_types = new CarTypes();

        	car_types.Set_id(result.getShort(0));
        	
        	car_types.Set_cid(result.getString(1));
        	car_types.Set_brand_id(result.getString(2));
        	car_types.Set_name(result.getString(3));
        	car_types.Set_img_path(result.getString(4));
	    	
	        list.add(car_types);  
        }  
        return list;  
    } 
    //  �����ֲ�ѯ  
    public List<CarTypes>findByBaseForKeyword(String BaseKey, String BaseData, String findKey, String findData){  
        List<CarTypes> list = new ArrayList<CarTypes>();  
        String sql="select *from "+DataBaseCCMHelper.TABLE_CarTypes+
        		" where "+BaseKey+" = ? "+
        		" and "+findKey+" like ? "
        		;   
         
        String contexstr[]={
        		BaseData,
        		"%"+findData+"%"
        		};  
        
        Cursor result=mDb.rawQuery(sql,contexstr);  
           

        //-----------------------  
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
        	CarTypes car_types = new CarTypes();

        	car_types.Set_id(result.getShort(0));
        	
        	car_types.Set_cid(result.getString(1));
        	car_types.Set_brand_id(result.getString(2));
        	car_types.Set_name(result.getString(3));
        	car_types.Set_img_path(result.getString(4));
	    	
	        list.add(car_types);  
        }  
        return list;  
    } 
	public List<CarTypes> getTableList(){
		List<CarTypes> list = new ArrayList<CarTypes>();
		
		String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_CarTypes;
	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // ����ѭ���ķ�ʽ��������
//	    	for(int j=0;j<result.getCount();j++){
//	    		System.out.println("DBTEST--NUM:"+j + result.getShort(j));
//	    	}
	    	
	    	CarTypes car_types = new CarTypes();
	    	car_types.Set_id(result.getShort(0));
        	
        	car_types.Set_cid(result.getString(1));
        	car_types.Set_brand_id(result.getString(2));
        	car_types.Set_name(result.getString(3));
        	car_types.Set_img_path(result.getString(4));
	    	
	        list.add(car_types);

	        //printfSEFF_File(seFF_file);
	    }
//	    CarTypes seFF_file = new CarTypes();
//	    if(list.size()>0){
//        	for(int i=0;i<list.size();i++){        		
//        		seFF_file = list.get(i);
//        		System.out.println("DBTEST-FUCK-NUM:"+i + "-Name:" + seFF_file.Get_file_name());
//        	}
//        }
	    return list;
	}
	

}
