package com.chs.mt.pxe_r500.db;

import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.bean.MacsAgentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MacsAgentName_Table {
	/**
	 * �����Ǵ���ҵ��ķ�������JAVA����ʵ����
	 * ����������Ĵ����ݿ⣬һ��Ҫ�ر����ݿ�
	 * */
	//private DataBaseCCMHelper dbOpenHelper;
	private SQLiteDatabase mDb;
	
	public MacsAgentName_Table(Context context,SQLiteDatabase mdatabase) {
		//dbOpenHelper = mdbOpenHelper;
		mDb = mdatabase;
	}
	/*
	 * �����µ�һ�����ݣ����뵽���ݱ���	
	 */
	/**
     * ����һ������
     * @param macsAgentName
     */
	public void insert(MacsAgentName macsAgentName) {
		ContentValues values = new ContentValues();
		
		values.put(MacsAgentName.T_CID, macsAgentName.Get_cid());
		values.put(MacsAgentName.T_MID, macsAgentName.Get_mid());
		values.put(MacsAgentName.T_AgentID, macsAgentName.Get_AgentID());
		values.put(MacsAgentName.T_CNAME, macsAgentName.Get_cname());
		
		mDb.insert(DataBaseCCMHelper.TABLE_MacsAgentName, null, values);
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
    	mDb.delete(DataBaseCCMHelper.TABLE_MacsAgentName, key+"=?", new String[] {date});
    }
    //ɾ��������������
    public void deleteAll() {
    	String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_MacsAgentName;

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
    public void update(String key, String date,MacsAgentName macsAgentName) {
    	ContentValues values = new ContentValues();
 
    	values.put(MacsAgentName.T_CID, macsAgentName.Get_cid());
		values.put(MacsAgentName.T_MID, macsAgentName.Get_mid());
		values.put(MacsAgentName.T_AgentID, macsAgentName.Get_AgentID());
		values.put(MacsAgentName.T_CNAME, macsAgentName.Get_cname());

		mDb.update(DataBaseCCMHelper.TABLE_MacsAgentName, values, key+"=?", new String[] {date});
    }
    
    
    public void ResetTable() {
    	mDb.execSQL("delete from "+DataBaseCCMHelper.TABLE_MacsAgentName+";");
    	//mDb.execSQL("DELETEFROM sqlite_sequence WHERE name="+DataBaseCCMHelper.TABLE_MacsAgentName);
    	mDb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+DataBaseCCMHelper.TABLE_MacsAgentName+"'");
    }
    
    /**
     * ����һ������
     * @param key
     * @param date
     * @return
     */
    public MacsAgentName find(String key ,String date) {	
    	//OK
//    	String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_MacsAgentName;
//	    Cursor cursor = mDb.rawQuery(sql, null); // ִ�в�ѯ���
//	    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {  
//	    	System.out.println("DBTEST-SSFSS-"+"cursor=" + cursor.getInt(0));
//	    	if(cursor.getInt(0) == date){
//	    		return new MacsAgentName(
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
    	

		Cursor cursor = mDb.query(DataBaseCCMHelper.TABLE_MacsAgentName, null, key+"=?", new String[] {date}, null, null, null);
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
				return new MacsAgentName(
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
	public List<MacsAgentName> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<MacsAgentName> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<MacsAgentName> getList(String sql, String[] selectionArgs) {
		ArrayList<MacsAgentName> list = new ArrayList<MacsAgentName>();
		MacsAgentName feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new MacsAgentName();
				index = c.getColumnIndex(MacsAgentName.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(MacsAgentName.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}
				
				index = c.getColumnIndex(MacsAgentName.T_MID);
				if (index != -1) {
					feed.Set_mid(c.getString(index));

				}
				index = c.getColumnIndex(MacsAgentName.T_AgentID);
				if (index != -1) {
					feed.Set_AgentID(c.getString(index));

				}
				index = c.getColumnIndex(MacsAgentName.T_CNAME);
				if (index != -1) {
					feed.Set_cname(c.getString(index));

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

	public List<MacsAgentName> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<MacsAgentName> list = new ArrayList<MacsAgentName>();
		// HashSet<MacsAgentName> set=new HashSet<MacsAgentName>();
		MacsAgentName feed = null;
		if (columns == null) {

			columns = new String[] { 
				MacsAgentName.T_ID, 
				MacsAgentName.T_CID,
				MacsAgentName.T_MID,
				MacsAgentName.T_AgentID,
				MacsAgentName.T_CNAME
			};
		}
		Cursor c = mDb.query(DataBaseCCMHelper.TABLE_MacsAgentName, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new MacsAgentName();
				index = c.getColumnIndex(MacsAgentName.T_ID);
				if (index != -1) {
					feed.Set_id(c.getInt(index));

				}
				
				index = c.getColumnIndex(MacsAgentName.T_CID);
				if (index != -1) {
					feed.Set_cid(c.getString(index));

				}
				
				index = c.getColumnIndex(MacsAgentName.T_MID);
				if (index != -1) {
					feed.Set_mid(c.getString(index));

				}
				index = c.getColumnIndex(MacsAgentName.T_AgentID);
				if (index != -1) {
					feed.Set_AgentID(c.getString(index));

				}
				index = c.getColumnIndex(MacsAgentName.T_CNAME);
				if (index != -1) {
					feed.Set_cname(c.getString(index));

				}
		
				list.add(feed);

			} while (c.moveToNext());
		}

		if (c != null) {
			c.close();
			c = null;

		}
		// for (MacsAgentName menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}
//  �����ֲ�ѯ  
    public List<MacsAgentName>findByBaseForKeyword(String BaseKey, String BaseData, String findKey, String findData){  
        List<MacsAgentName> list = new ArrayList<MacsAgentName>();  
        String sql="select *from "+DataBaseCCMHelper.TABLE_MacsAgentName+
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
        	MacsAgentName macsAgentName = new MacsAgentName();

        	macsAgentName.Set_id(result.getShort(0));
        	
        	macsAgentName.Set_cid(result.getString(1));
        	macsAgentName.Set_mid(result.getString(2));
        	macsAgentName.Set_AgentID(result.getString(3));
        	macsAgentName.Set_cname(result.getString(4));
	    	
	        list.add(macsAgentName);  
        }  
        return list;  
    } 
	//  ģ����ѯ  
    public List<MacsAgentName>findBy(String keyword){  
        List<MacsAgentName> list = new ArrayList<MacsAgentName>();  
        String sql="select *from "+DataBaseCCMHelper.TABLE_MacsAgentName+
        		" where id like ? "
        		+"or cid like ? "
        		+"or mid like ?"
        		+"or AgentID like ?"
        		+"or cname like ?"
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
        	MacsAgentName macsAgentName = new MacsAgentName();
        	
        	macsAgentName.Set_id(result.getShort(0));
        	
        	macsAgentName.Set_cid(result.getString(1));
        	macsAgentName.Set_mid(result.getString(2));
        	macsAgentName.Set_AgentID(result.getString(3));
        	macsAgentName.Set_cname(result.getString(4));
	    	
	        list.add(macsAgentName);  
        }  
        return list;  
    }  
    //  �����ֲ�ѯ  
    public List<MacsAgentName>findByKeyword(String keyword, String Data){  
        List<MacsAgentName> list = new ArrayList<MacsAgentName>();  
        String sql="select *from "+DataBaseCCMHelper.TABLE_MacsAgentName+
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
        	MacsAgentName macsAgentName = new MacsAgentName();

        	macsAgentName.Set_id(result.getShort(0));
        	
        	macsAgentName.Set_cid(result.getString(1));
        	macsAgentName.Set_mid(result.getString(2));
        	macsAgentName.Set_AgentID(result.getString(3));
        	macsAgentName.Set_cname(result.getString(4));
	    	
	        list.add(macsAgentName);  
        }  
        return list;  
    } 
	public List<MacsAgentName> getTableList(){
		List<MacsAgentName> list = new ArrayList<MacsAgentName>();
		
		String sql = "SELECT * FROM " + DataBaseCCMHelper.TABLE_MacsAgentName;
	    Cursor result = mDb.rawQuery(sql, null); // ִ�в�ѯ���
	    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // ����ѭ���ķ�ʽ��������
//	    	for(int j=0;j<result.getCount();j++){
//	    		System.out.println("DBTEST--NUM:"+j + result.getShort(j));
//	    	}
	    	
	    	MacsAgentName macsAgentName = new MacsAgentName();
	    	macsAgentName.Set_id(result.getShort(0));
        	
	    	macsAgentName.Set_cid(result.getString(1));
        	macsAgentName.Set_mid(result.getString(2));
        	macsAgentName.Set_AgentID(result.getString(3));
        	macsAgentName.Set_cname(result.getString(4));
	    	
	        list.add(macsAgentName);

	        //printfSEFF_File(seFF_file);
	    }
//	    MacsAgentName seFF_file = new MacsAgentName();
//	    if(list.size()>0){
//        	for(int i=0;i<list.size();i++){        		
//        		seFF_file = list.get(i);
//        		System.out.println("DBTEST-FUCK-NUM:"+i + "-Name:" + seFF_file.Get_file_name());
//        	}
//        }
	    return list;
	}
	

}
