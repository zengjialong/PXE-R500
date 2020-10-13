package com.chs.mt.pxe_r500.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseMusicHelper extends SQLiteOpenHelper {
	private static DataBaseMusicHelper mInstance = null;

	private static final String DATABASE_NAME  = "CHS_MusicDB.db";
	public static final String TABLE_FileList = "t_FileList";
	public static final String TABLE_MusicList = "t_MusicList";


	private static final int DATABASE_VERSION = 2;

    private static final String FileList_TABLE_CREATE = "CREATE TABLE t_FileList (id integer primary key autoincrement,"
			+"CurDirID TEXT,"
			+"FileName TEXT,"
			+"TotalFiles TEXT,"
			+"TotalSongs TEXT,"
			+"SongsStartID TEXT,"
			+"SongsEndID TEXT,"
			+"USBIDST TEXT"
			+");";
    private static final String MusicList_TABLE_CREATE = "CREATE TABLE t_MusicList (id integer primary key autoincrement,"
			+"CurID TEXT,"
			+"Total TEXT,"
			+"FileName TEXT,"
			+"Form TEXT,"
			+"FormSt TEXT,"
			+"CurFileID TEXT,"
			+"ID3_Title TEXT,"
			+"ID3_Artist TEXT,"
			+"ID3_Album TEXT,"
			+"ID3_Year TEXT,"
			+"ID3_Comment TEXT,"
			+"ID3_Genre TEXT,"
            +"USBIDST TEXT"
			+");";


    private static final String FileList_TABLE_DELETE = "DROP TABLE t_FileList";
    private static final String MusicList_TABLE_DELETE = "DROP TABLE t_MusicList";

	public DataBaseMusicHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    static synchronized DataBaseMusicHelper getInstance(Context context) {
		if (mInstance == null) {
		    mInstance = new DataBaseMusicHelper(context);
		}
		return mInstance;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FileList_TABLE_CREATE);
		db.execSQL(MusicList_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("EROP TABLE IF EXISTS t_seffData");
//		db.execSQL("EROP TABLE IF EXISTS t_seffFile");
//		db.execSQL("EROP TABLE IF EXISTS t_seffFile_recently");
//		
//		db.execSQL("EROP TABLE IF EXISTS t_login_sm");
		onCreate(db);
	}
	

    public boolean deleteDatabase(Context context) {
    	return context.deleteDatabase(DATABASE_NAME);
    }
    
    
    
	
	
	
	
	
	
	
}
