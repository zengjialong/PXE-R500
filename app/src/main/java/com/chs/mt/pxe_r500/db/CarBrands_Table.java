package com.chs.mt.pxe_r500.db;

import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.bean.CarBrands;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class CarBrands_Table {
	/**
	 * �����Ǵ���ҵ��ķ�������JAVA����ʵ����
	 * ����������Ĵ����ݿ⣬һ��Ҫ�ر����ݿ�
	 * */
	//private DataBaseCCMHelper dbOpenHelper;
	private SQLiteDatabase mDb;
	
	public CarBrands_Table(Context context,SQLiteDatabase mdatabase) {
		//dbOpenHelper = mdbOpenHelper;
		mDb = mdatabase;
	}
	/*
	 * �����µ�һ�����ݣ����뵽���ݱ���	
	 */
	/**
     * ����һ������
     * @param car_brands
     */
	public void insert(CarBrands car_brands) {
		ContentValues values = new ContentValues();
		
		values.put(CarBrands.T_CID, car_brands.Get_cid());
		values.put(CarBrands.T_NAME, car_brands.Get_name());
		values.put(CarBrands.T_IMG_PATH, car_brands.Get_img_path());
		
		mDb.insert(DataBaseCCMHelper.TABLE_CarBrands, null, values);
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
    	mDb.delete(DataBaseCCMHelper.TABLE_CarBrands, key+"=?", new String[] {date});
    }
    //ɾ��������������
    public void deleteAll() {
    	String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_CarBrands;

	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {  
	    	delete("id", String.valueOf(result.getInt(0)));
	    }
    }
    /**
     * ����һ������
     *
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
    public void update(String key, String date,CarBrands car_brands) {
    	ContentValues values = new ContentValues();
 
		values.put(CarBrands.T_CID, car_brands.Get_cid());
		values.put(CarBrands.T_NAME, car_brands.Get_name());
		values.put(CarBrands.T_IMG_PATH, car_brands.Get_img_path());

		mDb.update(DataBaseCCMHelper.TABLE_CarBrands, values, key+"=?", new String[] {date});
    }
    
    
    public void ResetTable() {
    	mDb.execSQL("delete from "+ DataBaseCCMHelper.TABLE_CarBrands+";");
    	//mDb.execSQL("DELETEFROM sqlite_sequence WHERE name="+DataBaseCCMHelper.TABLE_CarBrands);
    	mDb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+ DataBaseCCMHelper.TABLE_CarBrands+"'");
    }
    
    /**
     * ����һ������
     * @param key
     * @param date
     * @return
     */
    public CarBrands find(String key ,String date) {	
    	//OK
//    	String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_CarBrands;
//	    Cursor cursor = mDb.rawQuery(sql, null); // ִ�в�ѯ���
//	    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {  
//	    	System.out.println("DBTEST-SSFSS-"+"cursor=" + cursor.getInt(0));
//	    	if(cursor.getInt(0) == date){
//	    		return new CarBrands(
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
    	

		Cursor cursor = mDb.query(DataBaseCCMHelper.TABLE_CarBrands, null, key+"=?", new String[] {date}, null, null, null);
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
				return new CarBrands(
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3)
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
	public List<CarBrands> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<CarBrands> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<CarBrands> getList(String sql, String[] selectionArgs) {
		ArrayList<CarBrands> list = new ArrayList<CarBrands>();
		CarBrands feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new CarBrands();
				index = c.getColumnIndex(CarBrands.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(CarBrands.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}
				
				index = c.getColumnIndex(CarBrands.T_NAME);
				if (index != -1) {
					feed.Set_name(c.getString(index));

				}
				
				index = c.getColumnIndex(CarBrands.T_IMG_PATH);
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

	public List<CarBrands> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<CarBrands> list = new ArrayList<CarBrands>();
		// HashSet<CarBrands> set=new HashSet<CarBrands>();
		CarBrands feed = null;
		if (columns == null) {

			columns = new String[] { 
				CarBrands.T_ID, 
				CarBrands.T_CID,
				CarBrands.T_NAME,
				CarBrands.T_IMG_PATH
			};
		}
		Cursor c = mDb.query(DataBaseCCMHelper.TABLE_CarBrands, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new CarBrands();
				index = c.getColumnIndex(CarBrands.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(CarBrands.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}

				index = c.getColumnIndex(CarBrands.T_NAME);
				if (index != -1) {
					feed.Set_name(c.getString(index));

				}
				
				index = c.getColumnIndex(CarBrands.T_IMG_PATH);
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
		// for (CarBrands menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}
	
	//  ģ����ѯ  
    public List<CarBrands>findBy(String keyword){  
        List<CarBrands> list = new ArrayList<CarBrands>();  
        String sql="select *from "+ DataBaseCCMHelper.TABLE_CarBrands+
        		" where id like ? "
        		+"or cid like ? "
        		+"or name like ?"
        		+"or img_path like ?"
        		;   

        String contexstr[]={
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
        	CarBrands car_brands = new CarBrands();
        	
        	car_brands.Set_id(result.getShort(0));
        	
        	car_brands.Set_cid(result.getString(1));
        	car_brands.Set_name(result.getString(2));
        	car_brands.Set_img_path(result.getString(3));
	    	
	        list.add(car_brands);  
        }  
        return list;  
    }  
    //  �����ֲ�ѯ  
    public List<CarBrands>findByKeyword(String keyword, String Data){  
        List<CarBrands> list = new ArrayList<CarBrands>();  
        String sql="select *from "+ DataBaseCCMHelper.TABLE_CarBrands+
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
        	CarBrands car_brands = new CarBrands();

        	car_brands.Set_id(result.getShort(0));
        	
        	car_brands.Set_cid(result.getString(1));
        	car_brands.Set_name(result.getString(2));
        	car_brands.Set_img_path(result.getString(3));
	    	
	        list.add(car_brands);  
        }  
        return list;  
    } 
	public List<CarBrands> getTableList(){
		List<CarBrands> list = new ArrayList<CarBrands>();
		
		String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_CarBrands;
	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // ����ѭ���ķ�ʽ��������
//	    	for(int j=0;j<result.getCount();j++){
//	    		System.out.println("DBTEST--NUM:"+j + result.getShort(j));
//	    	}
	    	
	    	CarBrands car_brands = new CarBrands();
	    	car_brands.Set_id(result.getShort(0));
        	
        	car_brands.Set_cid(result.getString(1));
        	car_brands.Set_name(result.getString(2));
        	car_brands.Set_img_path(result.getString(3));
        	
	        list.add(car_brands);

	        //printfSEFF_File(seFF_file);
	    }
//	    CarBrands seFF_file = new CarBrands();
//	    if(list.size()>0){
//        	for(int i=0;i<list.size();i++){        		
//        		seFF_file = list.get(i);
//        		System.out.println("DBTEST-FUCK-NUM:"+i + "-Name:" + seFF_file.Get_file_name());
//        	}
//        }
	    return list;
	}
	

}
