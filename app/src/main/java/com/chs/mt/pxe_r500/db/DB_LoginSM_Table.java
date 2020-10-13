package com.chs.mt.pxe_r500.db;

import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.bean.LoginSM;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DB_LoginSM_Table {
	/**
	 * �����Ǵ���ҵ��ķ�������JAVA����ʵ����
	 * ����������Ĵ����ݿ⣬һ��Ҫ�ر����ݿ�
	 * */
	//private DataBaseOpenHelper dbOpenHelper;
	private SQLiteDatabase mDb;
	
	public DB_LoginSM_Table(Context context,SQLiteDatabase mdatabase) {
		//dbOpenHelper = mdbOpenHelper;
		mDb = mdatabase;
	}
	/*
	 * �����µ�һ�����ݣ����뵽���ݱ���	
	 */
	/**
     * ����һ������
     * @param loginSM
     */
	public void insert(LoginSM loginSM) {
		ContentValues values = new ContentValues();
		values.put(LoginSM.T_NAME, loginSM.Get_name());
		values.put(LoginSM.T_PASSWORD, loginSM.Get_password());
		values.put(LoginSM.T_RE_PASSWORD, loginSM.Get_re_password());
		values.put(LoginSM.T_AUTO_LOGIN, loginSM.Get_auto_login());
		values.put(LoginSM.T_RECENTLY, loginSM.Get_recently());
		mDb.insert(DataBaseOpenHelper.TABLE_LoginSM, null, values);
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
    	mDb.delete(DataBaseOpenHelper.TABLE_LoginSM, key+"=?", new String[] {date});
    }
    //ɾ��������������
    public void deleteAll() {
    	String sql = "SELECT * FROM " + DataBaseOpenHelper.TABLE_LoginSM;

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
    public void update(String key, String date,LoginSM loginSM) {
    	ContentValues values = new ContentValues();
 
    	values.put(LoginSM.T_NAME, loginSM.Get_name());
		values.put(LoginSM.T_PASSWORD, loginSM.Get_password());
		values.put(LoginSM.T_RE_PASSWORD, loginSM.Get_re_password());
		values.put(LoginSM.T_AUTO_LOGIN, loginSM.Get_auto_login());
		values.put(LoginSM.T_RECENTLY, loginSM.Get_recently());
		
		mDb.update(DataBaseOpenHelper.TABLE_LoginSM, values, key+"=?", new String[] {date});
    }
    
    
    public void ResetTable() {
    	mDb.execSQL("delete from "+DataBaseOpenHelper.TABLE_LoginSM+";");
    	//mDb.execSQL("DELETEFROM sqlite_sequence WHERE name="+DataBaseOpenHelper.TABLE_LoginSM);
    	mDb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+DataBaseOpenHelper.TABLE_LoginSM+"'");
    }
    
    /**
     * ����һ������
     * @param key
     * @param date
     * @return
     */
    public LoginSM find(String key ,String date) {	
    	//OK
//    	String sql = "SELECT * FROM " + DataBaseOpenHelper.TABLE_LoginSM;
//	    Cursor cursor = mDb.rawQuery(sql, null); // ִ�в�ѯ���
//	    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {  
//	    	System.out.println("DBTEST-SSFSS-"+"cursor=" + cursor.getInt(0));
//	    	if(cursor.getInt(0) == date){
//	    		return new LoginSM(
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
    	

		Cursor cursor = mDb.query(DataBaseOpenHelper.TABLE_LoginSM, null, key+"=?", new String[] {date}, null, null, null);
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
				return new LoginSM(
					cursor.getInt(0),
					cursor.getString(1),
					cursor.getString(2),
					cursor.getString(3),
					cursor.getString(4),
					cursor.getString(5)
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
	public List<LoginSM> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<LoginSM> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<LoginSM> getList(String sql, String[] selectionArgs) {
		ArrayList<LoginSM> list = new ArrayList<LoginSM>();
		LoginSM feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new LoginSM();
				index = c.getColumnIndex(LoginSM.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(LoginSM.T_NAME);
				if (index != -1) {
					feed.Set_name(c.getString(index));

				}
				
				index = c.getColumnIndex(LoginSM.T_PASSWORD);
				if (index != -1) {
					feed.Set_password(c.getString(index));

				}
				
				index = c.getColumnIndex(LoginSM.T_RE_PASSWORD);
				if (index != -1) {
					feed.Set_re_password(c.getString(index));

				}
				
				index = c.getColumnIndex(LoginSM.T_AUTO_LOGIN);
				if (index != -1) {
					feed.Set_auto_login(c.getString(index));

				}
				index = c.getColumnIndex(LoginSM.T_RECENTLY);
				if (index != -1) {
					feed.Set_recently(c.getString(index));

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

	public List<LoginSM> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<LoginSM> list = new ArrayList<LoginSM>();
		// HashSet<LoginSM> set=new HashSet<LoginSM>();
		LoginSM feed = null;
		if (columns == null) {

			columns = new String[] { 
				LoginSM.T_ID, 
				LoginSM.T_NAME,
				LoginSM.T_PASSWORD, 
				LoginSM.T_RE_PASSWORD,
				LoginSM.T_AUTO_LOGIN,
				LoginSM.T_RECENTLY
			};
		}
		Cursor c = mDb.query(DataBaseOpenHelper.TABLE_LoginSM, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new LoginSM();
				index = c.getColumnIndex(LoginSM.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(LoginSM.T_NAME);
				if (index != -1) {
					feed.Set_name(c.getString(index));

				}
				
				index = c.getColumnIndex(LoginSM.T_PASSWORD);
				if (index != -1) {
					feed.Set_password(c.getString(index));

				}
				
				index = c.getColumnIndex(LoginSM.T_RE_PASSWORD);
				if (index != -1) {
					feed.Set_re_password(c.getString(index));

				}
				
				index = c.getColumnIndex(LoginSM.T_AUTO_LOGIN);
				if (index != -1) {
					feed.Set_auto_login(c.getString(index));

				}
				index = c.getColumnIndex(LoginSM.T_RECENTLY);
				if (index != -1) {
					feed.Set_recently(c.getString(index));

				}
		
				list.add(feed);

			} while (c.moveToNext());
		}

		if (c != null) {
			c.close();
			c = null;

		}
		// for (LoginSM menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}
	
	//  ģ����ѯ  
    public List<LoginSM>findBy(String keyword){  
        List<LoginSM> list = new ArrayList<LoginSM>();  
        String sql="select *from "+DataBaseOpenHelper.TABLE_LoginSM+
        		" where id like ? "
        		+"or name like ? "
        		+"or password like ? "
        		+"or re_password like ?"
        		+"or auto_login like ?"
        		+"or recently like ?"
        		;   
         
        String contexstr[]={
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
        	LoginSM loginSM = new LoginSM();
        	
        	loginSM.Set_id(result.getShort(0));
        	
        	loginSM.Set_name(result.getString(1));
        	loginSM.Set_password(result.getString(2));
        	loginSM.Set_re_password(result.getString(3));
        	loginSM.Set_auto_login(result.getString(4));
        	loginSM.Set_recently(result.getString(5));
	    	
	        list.add(loginSM);  
        }  
        return list;  
    }  
    //  �����ֲ�ѯ  
    public List<LoginSM>findByKeyword(String keyword, String Data){  
        List<LoginSM> list = new ArrayList<LoginSM>();  
        String sql="select *from "+DataBaseOpenHelper.TABLE_LoginSM+
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
        	LoginSM loginSM = new LoginSM();

        	loginSM.Set_id(result.getShort(0));

        	loginSM.Set_name(result.getString(1));
        	loginSM.Set_password(result.getString(2));
        	loginSM.Set_re_password(result.getString(3));
        	loginSM.Set_auto_login(result.getString(4));
        	loginSM.Set_recently(result.getString(5));
	    	
	        list.add(loginSM);  
        }  
        return list;  
    } 
	public List<LoginSM> getTableList(){
		List<LoginSM> list = new ArrayList<LoginSM>();
		
		String sql = "SELECT * FROM " + DataBaseOpenHelper.TABLE_LoginSM;
	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // ����ѭ���ķ�ʽ��������
//	    	for(int j=0;j<result.getCount();j++){
//	    		System.out.println("DBTEST--NUM:"+j + result.getShort(j));
//	    	}
	    	
	    	LoginSM loginSM = new LoginSM();
	    	loginSM.Set_id(result.getShort(0));

        	loginSM.Set_name(result.getString(1));
        	loginSM.Set_password(result.getString(2));
        	loginSM.Set_re_password(result.getString(3));
        	loginSM.Set_auto_login(result.getString(4));
        	loginSM.Set_recently(result.getString(5));
	    	
	        list.add(loginSM);

	        //printfSEFF_File(seFF_file);
	    }
//	    LoginSM seFF_file = new LoginSM();
//	    if(list.size()>0){
//        	for(int i=0;i<list.size();i++){        		
//        		seFF_file = list.get(i);
//        		System.out.println("DBTEST-FUCK-NUM:"+i + "-Name:" + seFF_file.Get_file_name());
//        	}
//        }
	    return list;
	}
	

}
