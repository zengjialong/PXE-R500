package com.chs.mt.pxe_r500.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chs.mt.pxe_r500.MusicBox.MFListInfo;

import java.util.ArrayList;
import java.util.List;


public class DB_MFileList_Table {

	private SQLiteDatabase mDb;

	public DB_MFileList_Table(Context context, SQLiteDatabase mdatabase) {
		mDb = mdatabase;
	}

	public void insert(MFListInfo mFLinfo) {
		ContentValues values = new ContentValues();
		values.put("CurDirID", mFLinfo.CurDirID);
		values.put("FileName", mFLinfo.FileName);
		values.put("TotalFiles", mFLinfo.TotalFiles);
		values.put("TotalSongs", mFLinfo.TotalSongs);
		values.put("SongsStartID", mFLinfo.SongsStartID);
		values.put("SongsEndID", mFLinfo.SongsEndID);
		values.put("USBIDST", mFLinfo.USBIDST);
		mDb.insert(DataBaseMusicHelper.TABLE_FileList, null, values);
	}

    public void delete(String key, String date) {
    	mDb.delete(DataBaseMusicHelper.TABLE_FileList, key+"=?", new String[] {date});
    }
    
    public void update(String key, String date,MFListInfo mFLinfo) {
    	ContentValues values = new ContentValues();
        values.put("CurDirID", mFLinfo.CurDirID);
        values.put("FileName", mFLinfo.FileName);
        values.put("TotalFiles", mFLinfo.TotalFiles);
        values.put("TotalSongs", mFLinfo.TotalSongs);
        values.put("SongsStartID", mFLinfo.SongsStartID);
        values.put("SongsEndID", mFLinfo.SongsEndID);
        values.put("USBIDST", mFLinfo.USBIDST);
		mDb.update(DataBaseMusicHelper.TABLE_FileList, values, key+"=?", new String[] {date});
    }


    public void ResetTable() {
        mDb.execSQL("delete from "+DataBaseMusicHelper.TABLE_FileList+";");
        //mDb.execSQL("DELETEFROM sqlite_sequence WHERE name="+DataBaseMusicHelper.TABLE_FileList);
        mDb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+DataBaseMusicHelper.TABLE_FileList+"'");
    }


    public MFListInfo find(String key ,String date) {
		Cursor cursor = mDb.query(DataBaseMusicHelper.TABLE_FileList, null, key+"=?", new String[] {date}, null, null, null);
		if(cursor != null) {
		    //cursor.moveToFirst();
		    if(cursor.moveToNext()){
				return new MFListInfo(
						cursor.getInt(0),
						cursor.getInt(1),
						cursor.getString(2),
						cursor.getInt(3),
						cursor.getInt(4),
						cursor.getInt(5),
                        cursor.getInt(6),
                        cursor.getString(7)
						);
			}
			return null;
		}		
		return null;
    }

	
	public List<MFListInfo> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<MFListInfo> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<MFListInfo> getList(String sql, String[] selectionArgs) {
		ArrayList<MFListInfo> list = new ArrayList<MFListInfo>();
		MFListInfo feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new MFListInfo();
				index = c.getColumnIndex("CurDirID");
				if (index != -1) {
					feed.CurDirID=(c.getInt(index));

				}

                index = c.getColumnIndex("FileName");
                if (index != -1) {
                    feed.FileName=(c.getString(index));

                }

                index = c.getColumnIndex("TotalFiles");
                if (index != -1) {
                    feed.TotalFiles=(c.getInt(index));

                }

                index = c.getColumnIndex("TotalSongs");
                if (index != -1) {
                    feed.TotalSongs=(c.getInt(index));

                }

                index = c.getColumnIndex("SongsStartID");
                if (index != -1) {
                    feed.SongsStartID=(c.getInt(index));

                }

                index = c.getColumnIndex("SongsEndID");
                if (index != -1) {
                    feed.SongsEndID=(c.getInt(index));

                }

                index = c.getColumnIndex("USBIDST");
                if (index != -1) {
                    feed.USBIDST=(c.getString(index));

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


	public List<MFListInfo> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<MFListInfo> list = new ArrayList<MFListInfo>();
		// HashSet<MFListInfo> set=new HashSet<MFListInfo>();
		MFListInfo feed = null;
		if (columns == null) {

			columns = new String[] { 
				    "CurDirID",
                    "FileName",
                    "TotalFiles",
                    "TotalSongs",
                    "SongsStartID",
                    "SongsEndID",
                    "USBIDST",

			};
		}
		Cursor c = mDb.query(DataBaseMusicHelper.TABLE_FileList, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

                feed = new MFListInfo();
                index = c.getColumnIndex("CurDirID");
                if (index != -1) {
                    feed.CurDirID=(c.getInt(index));

                }

                index = c.getColumnIndex("FileName");
                if (index != -1) {
                    feed.FileName=(c.getString(index));

                }

                index = c.getColumnIndex("TotalFiles");
                if (index != -1) {
                    feed.TotalFiles=(c.getInt(index));

                }

                index = c.getColumnIndex("TotalSongs");
                if (index != -1) {
                    feed.TotalSongs=(c.getInt(index));

                }

                index = c.getColumnIndex("SongsStartID");
                if (index != -1) {
                    feed.SongsStartID=(c.getInt(index));

                }

                index = c.getColumnIndex("SongsEndID");
                if (index != -1) {
                    feed.SongsEndID=(c.getInt(index));

                }
                index = c.getColumnIndex("USBIDST");
                if (index != -1) {
                    feed.USBIDST=(c.getString(index));

                }
				list.add(feed);

			} while (c.moveToNext());
		}

		if (c != null) {
			c.close();
			c = null;

		}
		// for (MFListInfo menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}



    public List<MFListInfo>findBy(String keyword){
        List<MFListInfo> list = new ArrayList<MFListInfo>();
        String sql="select *from "+DataBaseMusicHelper.TABLE_FileList+
                " where id like ? "
                +"or CurDirID like ? "
                +"or FileName like ? "
                +"or TotalFiles like ? "
                +"or TotalSongs like ?"
                +"or SongsStartID like ?"
                +"or SongsEndID like ?"
                +"or USBIDST like ?"
                ;

        String contexstr[]={
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

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            MFListInfo mFLinfo = new MFListInfo();
            mFLinfo.CurDirID = result.getInt(1);
            mFLinfo.FileName = result.getString(2);
            mFLinfo.TotalFiles = result.getInt(3);
            mFLinfo.TotalSongs = result.getInt(4);
            mFLinfo.SongsStartID = result.getInt(5);
            mFLinfo.SongsEndID = result.getInt(6);
            mFLinfo.USBIDST = result.getString(7);

            list.add(mFLinfo);
        }
        return list;
    }

    public List<MFListInfo>findByKeyword(String keyword, String Data){
        List<MFListInfo> list = new ArrayList<MFListInfo>();
        String sql="select *from "+DataBaseMusicHelper.TABLE_FileList+
                " where "+keyword+" like ? "
                ;

        String contexstr[]={
                "%"+Data+"%"
        };
        Cursor result=mDb.rawQuery(sql,contexstr);

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            MFListInfo mFLinfo = new MFListInfo();
            mFLinfo.CurDirID = result.getInt(1);
            mFLinfo.FileName = result.getString(2);
            mFLinfo.TotalFiles = result.getInt(3);
            mFLinfo.TotalSongs = result.getInt(4);
            mFLinfo.SongsStartID = result.getInt(5);
            mFLinfo.SongsEndID = result.getInt(6);
            mFLinfo.USBIDST = result.getString(7);

            list.add(mFLinfo);
        }
        return list;
    }

    public List<MFListInfo>OrderBy(String keyword, boolean INC_SUB){
        List<MFListInfo> list = new ArrayList<MFListInfo>();
        String sql="select *from "+DataBaseMusicHelper.TABLE_FileList+
                " ORDER BY "+keyword+" DESC " ;
        if(INC_SUB){
            sql="select *from "+DataBaseMusicHelper.TABLE_FileList+
                    " ORDER BY "+keyword+" ASC " ;
        }
        Cursor result=mDb.rawQuery(sql,null);

        //-----------------------
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            MFListInfo mFLinfo = new MFListInfo();
            mFLinfo.CurDirID = result.getInt(1);
            mFLinfo.FileName = result.getString(2);
            mFLinfo.TotalFiles = result.getInt(3);
            mFLinfo.TotalSongs = result.getInt(4);
            mFLinfo.SongsStartID = result.getInt(5);
            mFLinfo.SongsEndID = result.getInt(6);
            mFLinfo.USBIDST = result.getString(7);

            list.add(mFLinfo);
        }
        return list;
    }
    public List<MFListInfo>OrderByKeyWord(String keyWordBase, String BaseWord, String keyword, boolean INC_SUB){
        List<MFListInfo> list = new ArrayList<MFListInfo>();
        String sql="select *from "+DataBaseMusicHelper.TABLE_FileList+
                " where "+keyWordBase+" =  "+BaseWord+
                " ORDER BY "+keyword+" DESC " ;


        if(INC_SUB){
            sql="select *from "+DataBaseMusicHelper.TABLE_FileList+
                    " where "+keyWordBase+" =  "+BaseWord+
                    " ORDER BY "+keyword+" ASC " ;
        }
        Cursor result=mDb.rawQuery(sql,null);

        //-----------------------
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            MFListInfo mFLinfo = new MFListInfo();
            mFLinfo.CurDirID = result.getInt(1);
            mFLinfo.FileName = result.getString(2);
            mFLinfo.TotalFiles = result.getInt(3);
            mFLinfo.TotalSongs = result.getInt(4);
            mFLinfo.SongsStartID = result.getInt(5);
            mFLinfo.SongsEndID = result.getInt(6);
            mFLinfo.USBIDST = result.getString(7);

            list.add(mFLinfo);
        }
        return list;
    }
    public List<MFListInfo> getTableList(){
        List<MFListInfo> list = new ArrayList<MFListInfo>();

        String sql = "SELECT * FROM " + DataBaseMusicHelper.TABLE_FileList;
        Cursor result = mDb.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
//	    	for(int j=0;j<result.getCount();j++){
//	    		System.out.println("DBTEST--NUM:"+j + result.getShort(j));
//	    	}

            MFListInfo mFLinfo = new MFListInfo();
            mFLinfo.CurDirID = result.getInt(1);
            mFLinfo.FileName = result.getString(2);
            mFLinfo.TotalFiles = result.getInt(3);
            mFLinfo.TotalSongs = result.getInt(4);
            mFLinfo.SongsStartID = result.getInt(5);
            mFLinfo.SongsEndID = result.getInt(6);
            mFLinfo.USBIDST = result.getString(7);
            list.add(mFLinfo);

            //printfSEFF_File(mFLinfo);
        }
//	    MFListInfo mFLinfo = new MFListInfo();
//	    if(list.size()>0){
//        	for(int i=0;i<list.size();i++){
//        		mFLinfo = list.get(i);
//        		System.out.println("DBTEST-FUCK-NUM:"+i + "-Name:" + mFLinfo.Get_file_name());
//        	}
//        }
        return list;
    }


}
