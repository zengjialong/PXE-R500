package com.chs.mt.pxe_r500.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chs.mt.pxe_r500.MusicBox.MMListInfo;

import java.util.ArrayList;
import java.util.List;


public class DB_MMusicList_Table {

	private SQLiteDatabase mDb;

	public DB_MMusicList_Table(Context context, SQLiteDatabase mdatabase) {
		mDb = mdatabase;
	}

	public void insert(MMListInfo mMMLinfo) {
		ContentValues values = new ContentValues();
		values.put("CurID", mMMLinfo.ID);
		values.put("Total", mMMLinfo.Total);
        values.put("FileName", mMMLinfo.FileName);
		values.put("Form", mMMLinfo.Form);
		values.put("FormSt", mMMLinfo.FormSt);
		values.put("CurFileID", mMMLinfo.CurFileID);
		values.put("ID3_Title", mMMLinfo.ID3_Title);
        values.put("ID3_Artist", mMMLinfo.ID3_Artist);
        values.put("ID3_Album", mMMLinfo.ID3_Album);
        values.put("ID3_Year", mMMLinfo.ID3_Year);
        values.put("ID3_Comment", mMMLinfo.ID3_Comment);
        values.put("ID3_Genre", mMMLinfo.ID3_Genre);
        values.put("USBIDST", mMMLinfo.USBIDST);
		mDb.insert(DataBaseMusicHelper.TABLE_MusicList, null, values);
	}

    public void delete(String key, String date) {
    	mDb.delete(DataBaseMusicHelper.TABLE_MusicList, key+"=?", new String[] {date});
    }
    
    public void update(String key, String date,MMListInfo mMMLinfo) {
    	ContentValues values = new ContentValues();
        values.put("CurID", mMMLinfo.ID);
        values.put("Total", mMMLinfo.Total);
        values.put("FileName", mMMLinfo.FileName);
        values.put("Form", mMMLinfo.Form);
        values.put("FormSt", mMMLinfo.FormSt);
        values.put("CurFileID", mMMLinfo.CurFileID);
        values.put("ID3_Title", mMMLinfo.ID3_Title);
        values.put("ID3_Artist", mMMLinfo.ID3_Artist);
        values.put("ID3_Album", mMMLinfo.ID3_Album);
        values.put("ID3_Year", mMMLinfo.ID3_Year);
        values.put("ID3_Comment", mMMLinfo.ID3_Comment);
        values.put("ID3_Genre", mMMLinfo.ID3_Genre);
        values.put("USBIDST", mMMLinfo.USBIDST);
		mDb.update(DataBaseMusicHelper.TABLE_MusicList, values, key+"=?", new String[] {date});
    }

    public void ResetTable() {
        mDb.execSQL("delete from "+DataBaseMusicHelper.TABLE_MusicList+";");
        //mDb.execSQL("DELETEFROM sqlite_sequence WHERE name="+DataBaseMusicHelper.TABLE_MusicList);
        mDb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+DataBaseMusicHelper.TABLE_MusicList+"'");
    }


    public MMListInfo find(String key ,String date) {
		Cursor cursor = mDb.query(DataBaseMusicHelper.TABLE_MusicList,
                null,
                key+"=?",
                new String[] {date},
                null,
                null,
                null);
		if(cursor != null) {
		    //cursor.moveToFirst();
		    if(cursor.moveToNext()){
                for(int j=0;j<cursor.getCount();j++){
                    if(j==0){
                        System.out.println("BUG DBTEST--NUM:"+j + cursor.getShort(j));
                    }else{
                        System.out.println("BUG DBTEST--NUM:"+j + cursor.getString(j));
                    }
                }
				return new MMListInfo(
						cursor.getInt(0),
						cursor.getInt(1),
						cursor.getInt(2),
						cursor.getString(3),
						cursor.getInt(4),
						cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13)
						);
			}
			return null;
		}		
		return null;
    }

	
	public List<MMListInfo> getAll(String orderBy, String limit) {
		return getList(null, null, null, null, null, orderBy, limit);
	}

	public List<MMListInfo> getAllByColomn(String[] columns, String orderBy,
			String limit) {
		return getList(columns, null, null, null, null, orderBy, limit);
	}
	
	public ArrayList<MMListInfo> getList(String sql, String[] selectionArgs) {
		ArrayList<MMListInfo> list = new ArrayList<MMListInfo>();
		MMListInfo feed = null;

		Cursor c = mDb.rawQuery(sql, selectionArgs);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

				feed = new MMListInfo();
				index = c.getColumnIndex("CurID");
				if (index != -1) {
					feed.CurID=(c.getInt(index));

				}

                index = c.getColumnIndex("Total");
                if (index != -1) {
                    feed.Total=(c.getInt(index));

                }

                index = c.getColumnIndex("FileName");
                if (index != -1) {
                    feed.FileName=(c.getString(index));

                }

                index = c.getColumnIndex("Form");
                if (index != -1) {
                    feed.Form=(c.getInt(index));

                }

                index = c.getColumnIndex("FormSt");
                if (index != -1) {
                    feed.FormSt=(c.getString(index));

                }

                index = c.getColumnIndex("CurFileID");
                if (index != -1) {
                    feed.CurFileID=(c.getInt(index));

                }


                index = c.getColumnIndex("ID3_Title");
                if (index != -1) {
                    feed.ID3_Title=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Artist");
                if (index != -1) {
                    feed.ID3_Artist=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Album");
                if (index != -1) {
                    feed.ID3_Album=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Year");
                if (index != -1) {
                    feed.ID3_Year=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Comment");
                if (index != -1) {
                    feed.ID3_Comment=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Genre");
                if (index != -1) {
                    feed.ID3_Genre=(c.getString(index));

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


	public List<MMListInfo> getList(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<MMListInfo> list = new ArrayList<MMListInfo>();
		// HashSet<MMListInfo> set=new HashSet<MMListInfo>();
		MMListInfo feed = null;
		if (columns == null) {

			columns = new String[] { 
				    "CurID",
                    "Total",
                    "FileName",
                    "Form",
                    "FormSt",
                    "CurFileID",

                    "ID3_Title",
                    "ID3_Artist",
                    "ID3_Album",
                    "ID3_Year",
                    "ID3_Comment",
                    "ID3_Genre",
                    "USBIDST",
			};
		}
		Cursor c = mDb.query(DataBaseMusicHelper.TABLE_MusicList, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		int index = -1;
		boolean hadata = c.moveToFirst();
		if (hadata) {
			do {

                feed = new MMListInfo();
                index = c.getColumnIndex("CurID");
                if (index != -1) {
                    feed.CurID=(c.getInt(index));

                }

                index = c.getColumnIndex("Total");
                if (index != -1) {
                    feed.Total=(c.getInt(index));

                }

                index = c.getColumnIndex("FileName");
                if (index != -1) {
                    feed.FileName=(c.getString(index));

                }

                index = c.getColumnIndex("Form");
                if (index != -1) {
                    feed.Form=(c.getInt(index));

                }

                index = c.getColumnIndex("FormSt");
                if (index != -1) {
                    feed.FormSt=(c.getString(index));

                }

                index = c.getColumnIndex("CurFileID");
                if (index != -1) {
                    feed.CurFileID=(c.getInt(index));

                }


                index = c.getColumnIndex("ID3_Title");
                if (index != -1) {
                    feed.ID3_Title=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Artist");
                if (index != -1) {
                    feed.ID3_Artist=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Album");
                if (index != -1) {
                    feed.ID3_Album=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Year");
                if (index != -1) {
                    feed.ID3_Year=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Comment");
                if (index != -1) {
                    feed.ID3_Comment=(c.getString(index));

                }

                index = c.getColumnIndex("ID3_Genre");
                if (index != -1) {
                    feed.ID3_Genre=(c.getString(index));

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
		// for (MMListInfo menuEntity : set) {
		// list.add(menuEntity);
		// }
		return list;
	}



    public List<MMListInfo>findBy(String keyword){
        List<MMListInfo> list = new ArrayList<MMListInfo>();
        String sql="select *from "+DataBaseMusicHelper.TABLE_MusicList+
                " where id like ? "
                +"or CurID like ? "
                +"or Total like ? "
                +"or FileName like ? "
                +"or Form like ?"
                +"or FormSt like ?"
                +"or CurFileID like ?"

                +"or ID3_Title like ? "
                +"or ID3_Artist like ? "
                +"or ID3_Album like ? "
                +"or ID3_Year like ?"
                +"or ID3_Comment like ?"
                +"or ID3_Genre like ?"
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
                "%"+keyword+"%",
                "%"+keyword+"%",
                "%"+keyword+"%",
                "%"+keyword+"%",
                "%"+keyword+"%",

                "%"+keyword+"%"
        };
        Cursor result=mDb.rawQuery(sql,contexstr);

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            MMListInfo mMMLinfo = new MMListInfo();
            mMMLinfo.ID = result.getInt(1);
            mMMLinfo.Total = result.getInt(2);
            mMMLinfo.FileName = result.getString(3);
            mMMLinfo.Form = result.getInt(4);
            mMMLinfo.FormSt = result.getString(5);
            mMMLinfo.CurFileID = result.getInt(6);

            mMMLinfo.ID3_Title = result.getString(7);
            mMMLinfo.ID3_Artist = result.getString(8);
            mMMLinfo.ID3_Album = result.getString(9);
            mMMLinfo.ID3_Year = result.getString(10);
            mMMLinfo.ID3_Comment = result.getString(11);
            mMMLinfo.ID3_Genre = result.getString(12);
            mMMLinfo.USBIDST = result.getString(13);

            list.add(mMMLinfo);
        }
        return list;
    }

    public List<MMListInfo>findByKeyword(String keyword, String Data){
        List<MMListInfo> list = new ArrayList<MMListInfo>();
        String sql="select *from "+DataBaseMusicHelper.TABLE_MusicList+
                " where "+keyword+" like ? "
                ;

        String contexstr[]={
                "%"+Data+"%"
        };
        Cursor result=mDb.rawQuery(sql,contexstr);

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            MMListInfo mMMLinfo = new MMListInfo();
            mMMLinfo.ID = result.getInt(1);
            mMMLinfo.Total = result.getInt(2);
            mMMLinfo.FileName = result.getString(3);
            mMMLinfo.Form = result.getInt(4);
            mMMLinfo.FormSt = result.getString(5);
            mMMLinfo.CurFileID = result.getInt(6);

            mMMLinfo.ID3_Title = result.getString(7);
            mMMLinfo.ID3_Artist = result.getString(8);
            mMMLinfo.ID3_Album = result.getString(9);
            mMMLinfo.ID3_Year = result.getString(10);
            mMMLinfo.ID3_Comment = result.getString(11);
            mMMLinfo.ID3_Genre = result.getString(12);
            mMMLinfo.USBIDST = result.getString(13);

            list.add(mMMLinfo);
        }
        return list;
    }

    public List<MMListInfo>OrderBy(String keyword, boolean INC_SUB){
        List<MMListInfo> list = new ArrayList<MMListInfo>();
        String sql="select *from "+DataBaseMusicHelper.TABLE_MusicList+
                " ORDER BY "+keyword+" DESC " ;
        if(INC_SUB){
            sql="select *from "+DataBaseMusicHelper.TABLE_MusicList+
                    " ORDER BY "+keyword+" ASC " ;
        }
        Cursor result=mDb.rawQuery(sql,null);

        //-----------------------
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            MMListInfo mMMLinfo = new MMListInfo();
            mMMLinfo.ID = result.getInt(1);
            mMMLinfo.Total = result.getInt(2);
            mMMLinfo.FileName = result.getString(3);
            mMMLinfo.Form = result.getInt(4);
            mMMLinfo.FormSt = result.getString(5);
            mMMLinfo.CurFileID = result.getInt(6);

            mMMLinfo.ID3_Title = result.getString(7);
            mMMLinfo.ID3_Artist = result.getString(8);
            mMMLinfo.ID3_Album = result.getString(9);
            mMMLinfo.ID3_Year = result.getString(10);
            mMMLinfo.ID3_Comment = result.getString(11);
            mMMLinfo.ID3_Genre = result.getString(12);
            mMMLinfo.USBIDST = result.getString(13);

            list.add(mMMLinfo);
        }
        return list;
    }
    public List<MMListInfo>OrderByKeyWord(String keyWordBase, String BaseWord, String keyword, boolean INC_SUB){
        List<MMListInfo> list = new ArrayList<MMListInfo>();
        String sql="select *from "+DataBaseMusicHelper.TABLE_MusicList+
                " where "+keyWordBase+" =  "+BaseWord+
                " ORDER BY "+keyword+" DESC " ;


        if(INC_SUB){
            sql="select *from "+DataBaseMusicHelper.TABLE_MusicList+
                    " where "+keyWordBase+" =  "+BaseWord+
                    " ORDER BY "+keyword+" ASC " ;
        }
        Cursor result=mDb.rawQuery(sql,null);

        //-----------------------
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            MMListInfo mMMLinfo = new MMListInfo();
            mMMLinfo.ID = result.getInt(1);
            mMMLinfo.Total = result.getInt(2);
            mMMLinfo.FileName = result.getString(3);
            mMMLinfo.Form = result.getInt(4);
            mMMLinfo.FormSt = result.getString(5);
            mMMLinfo.CurFileID = result.getInt(6);

            mMMLinfo.ID3_Title = result.getString(7);
            mMMLinfo.ID3_Artist = result.getString(8);
            mMMLinfo.ID3_Album = result.getString(9);
            mMMLinfo.ID3_Year = result.getString(10);
            mMMLinfo.ID3_Comment = result.getString(11);
            mMMLinfo.ID3_Genre = result.getString(12);
            mMMLinfo.USBIDST = result.getString(13);

            list.add(mMMLinfo);
        }
        return list;
    }
    public List<MMListInfo> getTableList(){
        List<MMListInfo> list = new ArrayList<MMListInfo>();

        String sql = "SELECT * FROM " + DataBaseMusicHelper.TABLE_MusicList;
        Cursor result = mDb.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
//	    	for(int j=0;j<result.getCount();j++){
//	    		System.out.println("DBTEST--NUM:"+j + result.getShort(j));
//	    	}

            MMListInfo mMMLinfo = new MMListInfo();
            mMMLinfo.ID = result.getInt(1);
            mMMLinfo.Total = result.getInt(2);
            mMMLinfo.FileName = result.getString(3);
            mMMLinfo.Form = result.getInt(4);
            mMMLinfo.FormSt = result.getString(5);
            mMMLinfo.CurFileID = result.getInt(6);

            mMMLinfo.ID3_Title = result.getString(7);
            mMMLinfo.ID3_Artist = result.getString(8);
            mMMLinfo.ID3_Album = result.getString(9);
            mMMLinfo.ID3_Year = result.getString(10);
            mMMLinfo.ID3_Comment = result.getString(11);
            mMMLinfo.ID3_Genre = result.getString(12);
            mMMLinfo.USBIDST = result.getString(13);
            list.add(mMMLinfo);

            //printfSEFF_File(mMMLinfo);
        }
//	    MMListInfo mMMLinfo = new MMListInfo();
//	    if(list.size()>0){
//        	for(int i=0;i<list.size();i++){
//        		mMMLinfo = list.get(i);
//        		System.out.println("DBTEST-FUCK-NUM:"+i + "-Name:" + mMMLinfo.Get_file_name());
//        	}
//        }
        return list;
    }



}
