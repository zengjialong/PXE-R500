package com.chs.mt.pxe_r500.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.filemanger.common.FileUtil;
import com.chs.mt.pxe_r500.bean.JsonDataSt;
import com.chs.mt.pxe_r500.adapter.ListSEffItemAdapter.setSeffFileAdpterOnItemClick;
import com.chs.mt.pxe_r500.bean.DSP_DataInfo;
import com.chs.mt.pxe_r500.bean.DSP_SingleData;
import com.chs.mt.pxe_r500.bean.SEFF_File;
import com.chs.mt.pxe_r500.bean.SEff_ListDat;
import com.chs.mt.pxe_r500.db.CarBrands_Table;
import com.chs.mt.pxe_r500.db.CarTypes_Table;
import com.chs.mt.pxe_r500.db.DB_LoginSM_Table;
import com.chs.mt.pxe_r500.db.DB_SEffData_Table;
import com.chs.mt.pxe_r500.db.DB_SEffFile_Recently_Table;
import com.chs.mt.pxe_r500.db.DB_SEffFile_Table;
import com.chs.mt.pxe_r500.db.DataBaseCCMHelper;
import com.chs.mt.pxe_r500.db.DataBaseOpenHelper;
import com.chs.mt.pxe_r500.db.MacTypes_Table;
import com.chs.mt.pxe_r500.db.MacsAgentName_Table;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.operation.JsonRWUtil;
import com.chs.mt.pxe_r500.tools.MVP_ViewPage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.pxe_r500.adapter.ListSEffItemAdapter;
import com.chs.mt.pxe_r500.adapter.SEffViewHolder;
import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.util.ToastUtil;

@SuppressLint({ "HandlerLeak", "NewApi", "InflateParams" }) public class SEff_DownloadActivity extends Activity{
	private Toast mToast;
	private Context mContext;
	public static final int WHAT_IS_UPDATE_LISTVIEW=0x00;    // 刷新EQ通道名字
	//private Handler mHandler ;
	//private ListSEffItemAdapter SEffAdapter = null;
	//标题
	private final static int Title_NUM = 3;
	private LinearLayout LY_Back,LY_Search;
	private TextView TV_PageName;
	//private String filePathString;
	private EditText ET_Search;
	private ImageView btn_back;
	private Button[]   B_Title = new Button[Title_NUM];
	private View[]   View = new View[Title_NUM];
	//private boolean bool_ShowSearchBar=false;
	private String searchString ="" ;
	private String searchKeyword ="" ;
	//0:处于名称排序界面，1：处于音效作者排序界面，2：处于时间排序界面，3：模糊搜索界面
	//private int SearchBarUI=0;

	private List<SEff_ListDat> SEff_List = new ArrayList<SEff_ListDat>();
	private List<SEFF_File> seffFile_list = new ArrayList<SEFF_File>();
	private SEFF_File seff_file = new SEFF_File();
	//弹出连接选项菜单
	private PopupMenu PM_ConMenu;
	private Menu connectMenu;
	MenuInflater menuInflater;

	//排序及选择
	private LinearLayout LY_ListOrderSel;
	private boolean bool_OrderMode=true;//false降序，true:升序
	private boolean bool_SelMode=false;//false：非全选，true:全选
	private boolean bool_MultiselectMode=false;//false：非多选模式，true:多选模式
	private LinearLayout LY_ListOrder,LY_ListCheckAll,LY_ListMultiselect,LY_ListOpt,LY_SelBottomBar;
	private LinearLayout[]  LY_SelBottomBarItem = new LinearLayout[4];
	private Button B_Order,B_SelMode;//,B_SelAS;
	private TextView TV_ListSelected;
	//private int mposition;
	//private static DB_SEffFile_Table dbSEfFile_Table;
	//界面
	//PageNUM

	private final static int PAGE_NUM = 4;
	private int CUR_PAGE = 0;
	private static final int PAGE_Name     = 0;
	private static final int PAGE_Favorite = 1;
	private static final int PAGE_Love     = 3;
	private static final int PAGE_Recently = 2;

	//private int CUR_OPT = 0;
	private static final int OPT_Share     = 0;
	private static final int OPT_Favorite = 1;
	private static final int OPT_Love     = 2;
	private static final int OPT_Delete = 3;

	//用于多选用
	private boolean[]  boolFavorite = new boolean[4];
	private boolean[]  boolLove = new boolean[4];

	private MVP_ViewPage VP_CHS_Pager;//
	private View[] LY_VIEW = new View[3];
	private ListView[]  mListViewSF = new ListView[3];
	private ListSEffItemAdapter[]  SEffAdapterSF = new ListSEffItemAdapter[3];

	private List<SEff_ListDat> SEff_List_Temp = new ArrayList<SEff_ListDat>();

	private List<SEff_ListDat> SEff_List_Name = new ArrayList<SEff_ListDat>();
	private List<SEff_ListDat> SEff_List_Favorite = new ArrayList<SEff_ListDat>();
	private List<SEff_ListDat> SEff_List_Love = new ArrayList<SEff_ListDat>();
	private List<SEff_ListDat> SEff_List_Recently = new ArrayList<SEff_ListDat>();

	private List<SEff_ListDat> SEff_List_NameOld = new ArrayList<SEff_ListDat>();
	private List<SEff_ListDat> SEff_List_FavoriteOld = new ArrayList<SEff_ListDat>();
	private List<SEff_ListDat> SEff_List_LoveOld = new ArrayList<SEff_ListDat>();
//  	private List<SEff_ListDat> SEff_List_RecentlyOld = new ArrayList<SEff_ListDat>();

	//    private List<SEFF_File> seffFile_list_Name = new ArrayList<SEFF_File>();    
//    private List<SEFF_File> seffFile_list_Favorite = new ArrayList<SEFF_File>();
//    private List<SEFF_File> seffFile_list_Love = new ArrayList<SEFF_File>();
	private List<SEFF_File> seffFile_list_Recently = new ArrayList<SEFF_File>();
	private JsonRWUtil jsonRWOpt = null;

	private AlertDialogFragment alertDialogFragment=null;
	private LoadingDialogFragment mLoadingDialogFragment=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chs_layout_sefflistview_download);
		mContext=this;
		if((!DataStruct.db.isOpen()||(!DataStruct.db_CCM.isOpen()))){
			initDatabases();
		}
		jsonRWOpt = new JsonRWUtil();
		InitViewPager();
		initHeadBar();
		initDataListView();
		initSEffAdapterClickListen();
		DataStruct.LocalSEffFile_List.clear();
//        mHandler = new Handler() {
//    		@Override
//    		public void handleMessage(Message msg) {
//     			super.handleMessage(msg);
//     			if (msg.what == WHAT_IS_UPDATE_LISTVIEW) {
//
//    			} 
//    		}
//        };


	}
	private void initDatabases() {
		if (DataStruct.db != null && DataStruct.db.isOpen()) {
			DataStruct.db.close();
			Log.i("info", "db is lll");
		}
		// --------获取数据库对象----------//
		DataStruct.DataBaseHelper = new DataBaseOpenHelper(mContext);
		DataStruct.db=DataStruct.DataBaseHelper.getReadableDatabase();
		DataStruct.dbSEffData_Table = new DB_SEffData_Table(mContext, DataStruct.db);
		DataStruct.dbSEfFile_Table = new DB_SEffFile_Table(mContext, DataStruct.db);
		DataStruct.dbSEfFile_Recently_Table = new DB_SEffFile_Recently_Table(mContext, DataStruct.db);
		DataStruct.dbLoginSM_Table = new DB_LoginSM_Table(mContext, DataStruct.db);

		DataStruct.CCM_DataBaseHelper = new DataBaseCCMHelper(mContext);
		DataStruct.db_CCM=DataStruct.CCM_DataBaseHelper.getReadableDatabase();
		DataStruct.dbCarBrands_Table = new CarBrands_Table(mContext, DataStruct.db_CCM);
		DataStruct.dbCarTypes_Table = new CarTypes_Table(mContext, DataStruct.db_CCM);
		DataStruct.dbMacTypes_Table = new MacTypes_Table(mContext, DataStruct.db_CCM);
		DataStruct.dbMacsAgentName_Table = new MacsAgentName_Table(mContext, DataStruct.db_CCM);
	}
	/**
	 * 查询SD卡里文档
	 */
	private void UpdateLocalSEffList(){
		DataStruct.LocalSEffFile_List.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {  //
				DataStruct.LocalSEffFile_List.clear();
				//GetFiles(Environment.getExternalStorageDirectory().toString(), DataStruct.CHS_SEff_TYPE,true);
				queryFiles();
			}
		}).start();

	}
	private void queryFiles(){
		String name="";
		String dec="";

		DSP_DataInfo dataInfo=new DSP_DataInfo();
		DSP_SingleData mDSP_SData = new DSP_SingleData();
		String[] projection = new String[] { BaseColumns._ID,
				MediaColumns.DATA,
				MediaColumns.DATE_ADDED,
				MediaColumns.SIZE
		};
		Cursor cursor = getContentResolver().query(
				Uri.parse("content://media/external/chs_file"),
				projection,
				MediaColumns.DATA + " like ?",
				new String[]{"%"+Define.CHS_SEff_TYPE},
				null);
		System.out.println("BUG cursor:"+cursor);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int dataindex = cursor
						.getColumnIndex(MediaColumns.DATA);
				int sizeindex = cursor
						.getColumnIndex(MediaColumns.SIZE);
				int addedindex = cursor
						.getColumnIndex(MediaColumns.DATE_ADDED);
				do {
					String path = cursor.getString(dataindex);
					String size = cursor.getString(sizeindex);
					String added = cursor.getString(addedindex);

					dec =path.substring(path.length()-Define.CHS_SEff_TYPE_L, path.length());
					System.out.println("BUG queryFiles path:"+path);
					if(dec.equals(Define.CHS_SEff_TYPE)){
						int dot=path.lastIndexOf("/");
						name=path.substring(dot+1);
						name =name.substring(0, name.length()-Define.CHS_SEff_TYPE_L);
						SEFF_File seff_File =new SEFF_File();
						mDSP_SData = jsonRWOpt.LoadJsonLocal2DSP_DataInfo( mContext,path);
						System.out.println("BUG 详情时候的值为"+dataInfo.Get_data_eff_briefing());
						if(mDSP_SData!=null){
							if(mDSP_SData!=null){
								dataInfo = mDSP_SData.Get_data_info();
								seff_File.Set_file_id("id?");
								seff_File.Set_file_type(mDSP_SData.Get_fileType());
								seff_File.Set_file_name(name);
								seff_File.Set_file_path(path);
								seff_File.Set_file_favorite("0");
								seff_File.Set_file_love("0");
								seff_File.Set_file_size(size);
								seff_File.Set_file_time(added);
								seff_File.Set_file_msg("msg?");

								seff_File.Set_data_user_name(dataInfo.Get_data_user_name());
								seff_File.Set_data_machine_type(dataInfo.Get_data_machine_type());
								seff_File.Set_data_car_type(dataInfo.Get_data_car_type());
								seff_File.Set_data_car_brand(dataInfo.Get_data_car_brand());
								seff_File.Set_data_group_name(dataInfo.Get_data_group_name());
								seff_File.Set_data_upload_time(dataInfo.Get_data_upload_time());
								seff_File.Set_data_eff_briefing(dataInfo.Get_data_eff_briefing());

								DataStruct.LocalSEffFile_List.add(seff_File);
							}
						}
					}
				} while (cursor.moveToNext());
			}
		}
		cursor.close();

		//整机
		cursor = getContentResolver().query(
				Uri.parse("content://media/external/chs_file"),
				projection,
				MediaColumns.DATA + " like ?",
				new String[]{"%"+Define.CHS_SEff_MAC_TYPE},
				null);

		System.out.println("BUG cursor:"+cursor);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int idindex = cursor
						.getColumnIndex(BaseColumns._ID);
				int dataindex = cursor
						.getColumnIndex(MediaColumns.DATA);
				int sizeindex = cursor
						.getColumnIndex(MediaColumns.SIZE);
				int addedindex = cursor
						.getColumnIndex(MediaColumns.DATE_ADDED);
				do {
					cursor.getString(idindex);
					String path = cursor.getString(dataindex);
					String size = cursor.getString(sizeindex);
					String added = cursor.getString(addedindex);

					dec =path.substring(path.length()-Define.CHS_SEff_TYPE_L, path.length());
					System.out.println("BUG queryFiles path:"+path);
					if(dec.equals(Define.CHS_SEff_MAC_TYPE)){
						int dot=path.lastIndexOf("/");
						name=path.substring(dot+1);
						name =name.substring(0, name.length()-Define.CHS_SEff_TYPE_L);
						Log.e("test@",name);
						SEFF_File seff_File =new SEFF_File();
						mDSP_SData = jsonRWOpt.LoadJsonLocal2DSP_DataInfo( mContext,path);
						if(mDSP_SData!=null){
							if(mDSP_SData!=null){
								dataInfo = mDSP_SData.Get_data_info();
								seff_File.Set_file_id("id?");
								seff_File.Set_file_type(mDSP_SData.Get_fileType());
								seff_File.Set_file_name(name);
								seff_File.Set_file_path(path);
								seff_File.Set_file_favorite("0");
								seff_File.Set_file_love("0");
								seff_File.Set_file_size(size);
								seff_File.Set_file_time(added);
								seff_File.Set_file_msg("msg?");

								seff_File.Set_data_user_name(dataInfo.Get_data_user_name());
								seff_File.Set_data_machine_type(dataInfo.Get_data_machine_type());
								seff_File.Set_data_car_type(dataInfo.Get_data_car_type());
								seff_File.Set_data_car_brand(dataInfo.Get_data_car_brand());
								seff_File.Set_data_group_name(dataInfo.Get_data_group_name());
								seff_File.Set_data_upload_time(dataInfo.Get_data_upload_time());
								seff_File.Set_data_eff_briefing(dataInfo.Get_data_eff_briefing());

								DataStruct.LocalSEffFile_List.add(seff_File);
							}
						}
					}
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
	}
	/*
    private void GetFiles(String Path, String Extension,boolean IsIterative){ //搜索目录，扩展名，是否进入子文件夹	
        File[] files =new File(Path).listFiles();
        for (int i =0; i < files.length; i++){
            File f = files[i];
            if (f.isFile()){
                if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)){ //判断扩展名                  
                    String path = f.getPath(); 	  
                    String dec =path.substring(path.length()-DataStruct.CHS_SEff_TYPE_L, path.length());
//              	    System.out.println("SD isJson:"+isJson);
                       if(dec.equals(DataStruct.CHS_SEff_TYPE)){
                           int dot=path.lastIndexOf("/");
                           String name=path.substring(dot+1);   

                           name = name.substring(0, name.length()-DataStruct.CHS_SEff_TYPE_L);  
                        SEFF_File seff_File =new SEFF_File();

                        DSP_SingleData mDSP_SData=new DSP_SingleData();	
                        JsonRWUtil jsonRWSingleData=new JsonRWUtil();
                        mDSP_SData = jsonRWSingleData.LoadJsonLocal2DSP_DataInfo( mContext,path);
                        //System.out.println("DBTEST --GetFiles-----name:"+name+",mDSP_SData:"+mDSP_SData);
                        if(mDSP_SData!=null){
                            DSP_DataInfo dataInfo = mDSP_SData.Get_data_info();
                            seff_File.Set_file_id("id?");
                            seff_File.Set_file_type(mDSP_SData.Get_fileType());
                            seff_File.Set_file_name(name);
                            seff_File.Set_file_path(path);
                            seff_File.Set_file_favorite("0");
                            seff_File.Set_file_love("0");
                            seff_File.Set_file_size("size?");
                            seff_File.Set_file_time("time?");
                            seff_File.Set_file_msg("msg?");
                            
                            
                            seff_File.Set_data_user_name(dataInfo.Get_data_user_name());
                            seff_File.Set_data_machine_type(dataInfo.Get_data_machine_type());
                            seff_File.Set_data_car_type(dataInfo.Get_data_car_type());
                            seff_File.Set_data_car_brand(dataInfo.Get_data_car_brand());
                            seff_File.Set_data_group_name(dataInfo.Get_data_group_name());
                            seff_File.Set_data_upload_time(dataInfo.Get_data_upload_time());
                            seff_File.Set_data_eff_briefing(dataInfo.Get_data_eff_briefing());
                            
                            DataStruct.LocalSEffFile_List.add(seff_File);
                        }        
                        
                        
                       }
                    
                    //System.out.println("SD  -----"+f.getPath());
                }
                if (!IsIterative){
                    break;
                }	                
            }else if (f.isDirectory() && f.getPath().indexOf("/.") == -1){ //忽略点文件（隐藏文件/文件夹）
                GetFiles(f.getPath(), Extension, IsIterative);
            }
        }
    }
    */
	private void initDataListView(){
		//此为另外开的线程扫描SD卡的全部音效数据
		//从本机的数据库中读取		
		seffFile_list.clear();
		seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
		if(((DataStruct.LocalSEffFile_List.size() > 0)&&(!DataStruct.boolUpdateLocalFile)&&(seffFile_list.size()>0))||
				(DataStruct.LocalSEffFile_List.size() > seffFile_list.size())
		){
			//检查扫描结果，如有新的，测加到数据库表中
			for(int i=0;i<DataStruct.LocalSEffFile_List.size();i++){
				SEFF_File sfile = DataStruct.dbSEfFile_Table.find("file_name",
						DataStruct.LocalSEffFile_List.get(i).Get_file_name());
				if(sfile==null){
					DataStruct.dbSEfFile_Table.insert(DataStruct.LocalSEffFile_List.get(i));
				}
			}





			DataStruct.boolUpdateLocalFile=true;
//        	seffFile_list.clear();
//        	//复位清空数据表
//	        DataStruct.dbSEfFile_Table.ResetTable();
//	        //把相关数据的表复写到数据表中
//			for(int i=0;i < DataStruct.LocalSEffFile_List.size();i++){
//				DataStruct.dbSEfFile_Table.insert(new SEFF_File(
//					DataStruct.LocalSEffFile_List.get(i).Get_file_id(),//file_id
//					DataStruct.LocalSEffFile_List.get(i).Get_file_type(),//file_type
//					DataStruct.LocalSEffFile_List.get(i).Get_file_name(),//file_name
//					DataStruct.LocalSEffFile_List.get(i).Get_file_path(),//file_path
//					DataStruct.LocalSEffFile_List.get(i).Get_file_favorite(),//file_favorite
//					DataStruct.LocalSEffFile_List.get(i).Get_file_love(),//file_collect
//					DataStruct.LocalSEffFile_List.get(i).Get_file_size(),//file_size
//					DataStruct.LocalSEffFile_List.get(i).Get_file_time(),//file_time
//					DataStruct.LocalSEffFile_List.get(i).Get_file_msg(),//file_msg
//					
//					DataStruct.LocalSEffFile_List.get(i).Get_data_user_name(),//data_user_name
//					DataStruct.LocalSEffFile_List.get(i).Get_data_machine_type(),//data_machine_type
//					DataStruct.LocalSEffFile_List.get(i).Get_data_car_type(),//data_car_type
//					DataStruct.LocalSEffFile_List.get(i).Get_data_car_brand(),//data_car_brand
//					DataStruct.LocalSEffFile_List.get(i).Get_data_group_name(),//data_group_name
//					DataStruct.LocalSEffFile_List.get(i).Get_data_upload_time(),//data_upload_time
//					DataStruct.LocalSEffFile_List.get(i).Get_data_eff_briefing(),//data_eff_briefing
//					
//					"0",//list_sel
//    				"0"//list_is_open
//    			));
//			}
			Toast.makeText(SEff_DownloadActivity.this, R.string.UpdateLocalFile,
					Toast.LENGTH_SHORT).show();
		}
//        else{
//        	//检查现有的数据表中的数据，在本机中是否存在
//        	//从本机的数据库中读取		
//            seffFile_list.clear();
//            seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
////            System.out.println("del$:list.size:"+seffFile_list.size());
//            //检查在数据库中的数据在本机文件中是否存在否则删除记录
//            int j=0;
//            int cnt=0;
//            boolean sync=false;
//            if(seffFile_list.size() > 0){
//            	do{
//            		SEFF_File seff_file = seffFile_list.get(j); 
//            		if(!fileIsExists(seff_file.Get_file_path())){
//            			sync=true;
//            			seffFile_list.remove(j);
//            		}else{
//            			++j;
//            		}
//            		++cnt;
//            	}while(cnt < seffFile_list.size());
//            }
//            if(sync){//需要同步到数据库中
//            	//复位清空数据表，id 从头开始
//                DataStruct.dbSEfFile_Table.ResetTable();
//                //SEff_List.clear();
//                //把删除相关数据的表复写到数据表中
//                if(seffFile_list.size()>0){
//        			for(int i=0;i < seffFile_list.size();i++){
//        				DataStruct.dbSEfFile_Table.insert(new SEFF_File(
//        					seffFile_list.get(i).Get_file_id(),//file_id
//        					seffFile_list.get(i).Get_file_type(),//file_type
//        					seffFile_list.get(i).Get_file_name(),//file_name
//        					seffFile_list.get(i).Get_file_path(),//file_path
//        					seffFile_list.get(i).Get_file_favorite(),//file_favorite
//        					seffFile_list.get(i).Get_file_love(),//file_collect
//        					seffFile_list.get(i).Get_file_size(),//file_size
//        					seffFile_list.get(i).Get_file_time(),//file_time
//        					seffFile_list.get(i).Get_file_msg(),//file_msg
//        					
//        					seffFile_list.get(i).Get_data_user_name(),//data_user_name
//        					seffFile_list.get(i).Get_data_machine_type(),//data_machine_type
//        					seffFile_list.get(i).Get_data_car_type(),//data_car_type
//        					seffFile_list.get(i).Get_data_car_brand(),//data_car_brand
//        					seffFile_list.get(i).Get_data_group_name(),//data_group_name
//        					seffFile_list.get(i).Get_data_upload_time(),//data_upload_time
//        					seffFile_list.get(i).Get_data_eff_briefing(),//data_eff_briefing
//        					
//        					"0",//list_sel
//            				"0"//list_is_open
//        				));
//        			}
//                }
//            }            
//        }

		//把所有下拉清零
		seffFile_list.clear();
		seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
		for(int i=0;i<seffFile_list.size();i++){
			seffFile_list.get(i).Set_list_is_open("0");
			seffFile_list.get(i).Set_list_sel("0");
			DataStruct.dbSEfFile_Table.update("id",
					String.valueOf(seffFile_list.get(i).Get_id()), seffFile_list.get(i));
		}
		//最近列表
		seffFile_list.clear();
		seffFile_list = DataStruct.dbSEfFile_Recently_Table.getTableList();
		for(int i=0;i<seffFile_list.size();i++){
			seffFile_list.get(i).Set_list_is_open("0");
			seffFile_list.get(i).Set_list_sel("0");
			DataStruct.dbSEfFile_Recently_Table.update("id",
					String.valueOf(seffFile_list.get(i).Get_id()), seffFile_list.get(i));
		}


		for(int i=0;i<4;i++){
			initListData(i);
		}
	}
	private void initListData(int index){
		//用于多选列表用
		boolFavorite[index]=false;
		boolLove[index]=false;

		seffFile_list = DataStruct.dbSEfFile_Table.getTableList();

		if(index==PAGE_Name){
			SEff_List_Name.clear();
			for(int i=0;i<seffFile_list.size();i++){
				seff_file = seffFile_list.get(i);
				SEff_List_Name.add(new SEff_ListDat(
						seff_file.Get_id(),
						seff_file.Get_file_name(),
						seff_file.Get_file_path(),
						seff_file.Get_data_user_name(),
						seff_file.Get_data_upload_time(),
						seff_file.Get_file_favorite(),
						seff_file.Get_file_love(),
						"0",//seff_file.Get_list_sel(),
						seff_file.Get_list_is_open(),
						seff_file.Get_file_type()
				));
				//用于多选列表用
				if(seff_file.Get_file_favorite().equals("1")){
					boolFavorite[index]=true;
				}
				if(seff_file.Get_file_love().equals("1")){
					boolLove[index]=true;
				}
			}
			if(SEff_List_NameOld.size()>0){
				for(int i=0;i<SEff_List_Name.size();i++){
					for(int j=0;j<SEff_List_NameOld.size();j++){
						if(SEff_List_Name.get(i).getId()==SEff_List_NameOld.get(j).getId()){
							SEff_List_Name.get(i).setOpen(SEff_List_NameOld.get(j).isOpen());
						}
					}
				}
			}else{//第一次加载     	
				for(int i=0;i<SEff_List_Name.size();i++){
					SEff_List_Name.get(i).setOpen("0");
				}
			}

			SEff_List_NameOld.clear();
			for(int i=0;i<SEff_List_Name.size();i++){
				SEff_ListDat seff_ListDat=SEff_List_Name.get(i);
				SEff_List_NameOld.add(seff_ListDat);
			}

			SEffAdapterSF[0] = new ListSEffItemAdapter(this, SEff_List_Name);
			mListViewSF[0].setAdapter(SEffAdapterSF[0]);
		}else if(index==PAGE_Favorite){
			SEff_List_Favorite.clear();
			for(int i=0;i<seffFile_list.size();i++){
				if(seffFile_list.get(i).Get_file_favorite().equals("1")){
					seff_file = seffFile_list.get(i);
					SEff_List_Favorite.add(new SEff_ListDat(
							seff_file.Get_id(),
							seff_file.Get_file_name(),
							seff_file.Get_file_path(),
							seff_file.Get_data_user_name(),
							seff_file.Get_data_upload_time(),
							seff_file.Get_file_favorite(),
							seff_file.Get_file_love(),
							"0",//seff_file.Get_list_sel(),
							seff_file.Get_list_is_open(),
							seff_file.Get_file_type()
					));
					//用于多选列表用
					if(seff_file.Get_file_favorite().equals("1")){
						boolFavorite[index]=true;
					}
					if(seff_file.Get_file_love().equals("1")){
						boolLove[index]=true;
					}
				}
			}
			if(SEff_List_FavoriteOld.size()>0){
				for(int i=0;i<SEff_List_Favorite.size();i++){
					for(int j=0;j<SEff_List_FavoriteOld.size();j++){
						if(SEff_List_Favorite.get(i).getId()==SEff_List_FavoriteOld.get(j).getId()){
							SEff_List_Favorite.get(i).setOpen(SEff_List_FavoriteOld.get(j).isOpen());
						}
					}
				}
			}else{//第一次加载     	
				for(int i=0;i<SEff_List_Favorite.size();i++){
					SEff_List_Favorite.get(i).setOpen("0");
				}
			}

			SEff_List_FavoriteOld.clear();
			for(int i=0;i<SEff_List_Favorite.size();i++){
				SEff_ListDat seff_ListDat=SEff_List_Favorite.get(i);
				SEff_List_FavoriteOld.add(seff_ListDat);
			}

			SEffAdapterSF[1] = new ListSEffItemAdapter(this, SEff_List_Favorite);
			mListViewSF[1].setAdapter(SEffAdapterSF[1]);
		}else if(index==PAGE_Love){
			SEff_List_Love.clear();
			for(int i=0;i<seffFile_list.size();i++){
				if(seffFile_list.get(i).Get_file_love().equals("1")){
					seff_file = seffFile_list.get(i);
					SEff_List_Love.add(new SEff_ListDat(
							seff_file.Get_id(),
							seff_file.Get_file_name(),
							seff_file.Get_file_path(),
							seff_file.Get_data_user_name(),
							seff_file.Get_data_upload_time(),
							seff_file.Get_file_favorite(),
							seff_file.Get_file_love(),
							"0",//seff_file.Get_list_sel(),
							seff_file.Get_list_is_open(),
							seff_file.Get_file_type()
					));
					//用于多选列表用
					if(seff_file.Get_file_favorite().equals("1")){
						boolFavorite[index]=true;
					}
					if(seff_file.Get_file_love().equals("1")){
						boolLove[index]=true;
					}
				}
			}

			if(SEff_List_LoveOld.size()>0){
				for(int i=0;i<SEff_List_Love.size();i++){
					for(int j=0;j<SEff_List_LoveOld.size();j++){
						if(SEff_List_Love.get(i).getId()==SEff_List_LoveOld.get(j).getId()){
							SEff_List_Love.get(i).setOpen(SEff_List_LoveOld.get(j).isOpen());
						}
					}
				}
			}else{//第一次加载     	
				for(int i=0;i<SEff_List_Love.size();i++){
					SEff_List_Love.get(i).setOpen("0");
				}
			}

			SEff_List_LoveOld.clear();
			for(int i=0;i<SEff_List_Love.size();i++){
				SEff_ListDat seff_ListDat=SEff_List_Love.get(i);
				SEff_List_LoveOld.add(seff_ListDat);
			}



			SEffAdapterSF[2] = new ListSEffItemAdapter(this, SEff_List_Love);
			mListViewSF[2].setAdapter(SEffAdapterSF[2]);
		}else if(index==PAGE_Recently){
			SEff_List_Recently.clear();
			seffFile_list_Recently = DataStruct.dbSEfFile_Recently_Table.getTableList();

			int tal=seffFile_list_Recently.size();
			if(tal>0){
				for(int i=0;i<tal;i++){
					seff_file = seffFile_list_Recently.get(tal-i-1);
					SEff_List_Recently.add(new SEff_ListDat(
							seff_file.Get_id(),
							seff_file.Get_file_name(),
							seff_file.Get_file_path(),
							seff_file.Get_data_user_name(),
							seff_file.Get_data_upload_time(),
							seff_file.Get_file_favorite(),
							seff_file.Get_file_love(),
							"0",//seff_file.Get_list_sel(),
							seff_file.Get_list_is_open(),
							seff_file.Get_file_type()
					));
					//用于多选列表用
					if(seff_file.Get_file_favorite().equals("1")){
						boolFavorite[index]=true;
					}
					if(seff_file.Get_file_love().equals("1")){
						boolLove[index]=true;
					}
				}
			}
			SEffAdapterSF[2] = new ListSEffItemAdapter(this, SEff_List_Recently);
			mListViewSF[2].setAdapter(SEffAdapterSF[2]);
		}
	}
	public boolean fileIsExists(String path){
		try{
			File f=new File(path);
			if(!f.exists()){
				return false;
			}
		}catch (Exception e) {
			return false;
		}
		return true;
	}

	private void initHeadBar(){
		LY_Back = (LinearLayout) findViewById(R.id.id_llyout_net_back);
		btn_back = (ImageView) findViewById(R.id.id_b_back);
		TV_PageName = (TextView) findViewById(R.id.di_tv_net_pagedown_name);
		TV_PageName.setText(getResources().getString(R.string.net_downed));

		ET_Search = (EditText) findViewById(R.id.id_et_download_search_edit);
		LY_Search = (LinearLayout) findViewById(R.id.id_ly_search);
		//B_Menu = (Button) findViewById(R.id.id_b_menu);

		B_Title[0] = (Button) findViewById(R.id.id_b_list_0);
		B_Title[1] = (Button) findViewById(R.id.id_b_list_1);
		B_Title[2] = (Button) findViewById(R.id.id_b_list_3);
		//B_Title[3] = (Button) findViewById(R.id.id_b_list_3);

		View[0] = findViewById(R.id.id_v_sel0);
		View[1] = findViewById(R.id.id_v_sel1);
		View[2] = findViewById(R.id.id_v_sel3);
		//	View[3] = findViewById(R.id.id_v_sel3);
		//弹出菜单
		PM_ConMenu = new PopupMenu(this, findViewById(R.id.id_b_menu));
		connectMenu = PM_ConMenu.getMenu();
		// 通过XML文件添加菜单项  
		menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.chs_menu_seff_download, connectMenu);
		////排序及选择
		LY_ListOrder = (LinearLayout) findViewById(R.id.id_ly_in_order);
		LY_ListCheckAll = (LinearLayout) findViewById(R.id.id_ly_in_seff_list_check_all);
		LY_ListMultiselect = (LinearLayout) findViewById(R.id.id_ly_in_multiselect);
		LY_ListOpt = (LinearLayout) findViewById(R.id.id_ly_in_sellopt);
		LY_SelBottomBar = (LinearLayout) findViewById(R.id.id_ly_bottom_bar);
		LY_ListOrderSel = (LinearLayout) findViewById(R.id.id_ly_order_and_selall);


		LY_SelBottomBarItem[0] = (LinearLayout) findViewById(R.id.id_ly_seff_list_bottom_bar0);
		LY_SelBottomBarItem[1] = (LinearLayout) findViewById(R.id.id_ly_seff_list_bottom_bar1);
		LY_SelBottomBarItem[2] = (LinearLayout) findViewById(R.id.id_ly_seff_list_bottom_bar2);
		LY_SelBottomBarItem[3] = (LinearLayout) findViewById(R.id.id_ly_seff_list_bottom_bar3);
		for(int i=0;i<4;i++){
			LY_SelBottomBarItem[i].setTag(i);
		}

		B_Order = (Button) findViewById(R.id.id_b_seff_list_order);
		B_SelMode = (Button) findViewById(R.id.id_b_seff_list_sell_all);
		//B_SelAS = (Button) findViewById(R.id.id_b_seff_list_multiselect);
		TV_ListSelected = (TextView) findViewById(R.id.id_tv_seff_list_havesel);




		//初始UI
		searchKeyword = "file_name";
		for(int i=0;i<Title_NUM;i++){
			B_Title[i].setTextColor(getResources().getColor(R.color.dowloadtitle_color_normal));
			View[i].setBackgroundColor(getResources().getColor(R.color.dowloadtitle_vcolor_normal));
		}
		B_Title[0].setTextColor(getResources().getColor(R.color.dowloadtitle_color_press));
		View[0].setBackgroundColor(getResources().getColor(R.color.dowloadtitle_vcolor_press));

		//Back
		LY_Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(android.view.View v) {
				finish();
			}
		});

		for(int i=0;i<Title_NUM;i++){
			B_Title[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					for(int i=0;i<DataStruct.CurMacMode.Out.OUT_CH_MAX_USE;i++){
						if(view.getId()==B_Title[i].getId()){

							//TitleSel(i);
							VP_CHS_Pager.setCurrentItem(i,false);
							break;
						}
					}
				}
			});
		}

		LY_Search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				if(!bool_ShowSearchBar){
//					bool_ShowSearchBar=true;
//					TV_PageName.setVisibility(android.view.View.GONE);
//					ET_Search.setVisibility(android.view.View.VISIBLE);
//				}else {
//					bool_ShowSearchBar=false;
//					TV_PageName.setVisibility(android.view.View.VISIBLE);
//					ET_Search.setVisibility(android.view.View.GONE);
//				}
				Intent intent = new Intent();
				intent.setClass(mContext, SEff_DownloadSearchActivity.class);
				mContext.startActivity(intent);
			}
		});
		ET_Search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				searchString = String.valueOf(ET_Search.getText());
//				if(searchString.length()>0){
				seffFile_list.clear();
				if(searchString.length()>0){
					seffFile_list = DataStruct.dbSEfFile_Table.findByKeyword(searchKeyword, searchString);
				}else{
					seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
				}
				//if(seffFile_list.size()>0){
				SEff_List.clear();
				for(int i=0;i<seffFile_list.size();i++){
					seff_file = seffFile_list.get(i);
					SEff_List.add(new SEff_ListDat(
							seff_file.Get_id(),
							seff_file.Get_file_name(),
							seff_file.Get_file_path(),
							seff_file.Get_data_user_name(),
							seff_file.Get_data_upload_time(),
							seff_file.Get_file_favorite(),
							seff_file.Get_file_love(),
							seff_file.Get_list_sel(),
							seff_file.Get_list_is_open(),
							seff_file.Get_file_type()
					));
				}

				SEffAdapterSF[CUR_PAGE] = new ListSEffItemAdapter(mContext, SEff_List);
				mListViewSF[CUR_PAGE].setAdapter(SEffAdapterSF[CUR_PAGE]);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		////排序及选择bool_OrderMode
		//排序
		LY_ListOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO
				if(bool_OrderMode){//false升序，true:降序
					bool_OrderMode=false;
					B_Order.setBackground(getResources().getDrawable(R.drawable.chs_order_down));
				}else{
					bool_OrderMode=true;
					B_Order.setBackground(getResources().getDrawable(R.drawable.chs_order_up));
				}
				seffFile_list.clear();

				switch (CUR_PAGE) {
					case PAGE_Name:
						seffFile_list = DataStruct.dbSEfFile_Table.OrderBy("file_name", bool_OrderMode);
						//System.out.println("BUG--OrderBy :"+seffFile_list.size());
						SEff_List_Name.clear();
						for(int i=0;i<seffFile_list.size();i++){
							seff_file = seffFile_list.get(i);
							SEff_List_Name.add(new SEff_ListDat(
									seff_file.Get_id(),
									seff_file.Get_file_name(),
									seff_file.Get_file_path(),
									seff_file.Get_data_user_name(),
									seff_file.Get_data_upload_time(),
									seff_file.Get_file_favorite(),
									seff_file.Get_file_love(),
									"0",//seff_file.Get_list_sel(),
									"0",//seff_file.Get_list_is_open(),
									seff_file.Get_file_type()
							));
						}
						//SEffAdapterSF[0].notifyDataSetChanged();
						SEffAdapterSF[0] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Name);
						mListViewSF[0].setAdapter(SEffAdapterSF[0]);
						initSEffAdapterClickListen();
						break;
					case PAGE_Favorite:
						seffFile_list = DataStruct.dbSEfFile_Table.OrderByKeyWord("file_favorite", "1","data_user_name", bool_OrderMode);
						SEff_List_Favorite.clear();
						for(int i=0;i<seffFile_list.size();i++){
							seff_file = seffFile_list.get(i);
							SEff_List_Favorite.add(new SEff_ListDat(
									seff_file.Get_id(),
									seff_file.Get_file_name(),
									seff_file.Get_file_path(),
									seff_file.Get_data_user_name(),
									seff_file.Get_data_upload_time(),
									seff_file.Get_file_favorite(),
									seff_file.Get_file_love(),
									"0",//seff_file.Get_list_sel(),
									"0",//seff_file.Get_list_is_open()
									seff_file.Get_file_type()
							));
						}
						//SEffAdapterSF[1].notifyDataSetChanged();
						SEffAdapterSF[1] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Favorite);
						mListViewSF[1].setAdapter(SEffAdapterSF[1]);
						initSEffAdapterClickListen();
						break;
					case PAGE_Love:
						seffFile_list = DataStruct.dbSEfFile_Table.OrderByKeyWord("file_love", "1","data_user_name", bool_OrderMode);
						SEff_List_Love.clear();
						for(int i=0;i<seffFile_list.size();i++){
							seff_file = seffFile_list.get(i);
							SEff_List_Love.add(new SEff_ListDat(
									seff_file.Get_id(),
									seff_file.Get_file_name(),
									seff_file.Get_file_path(),
									seff_file.Get_data_user_name(),
									seff_file.Get_data_upload_time(),
									seff_file.Get_file_favorite(),
									seff_file.Get_file_love(),
									"0",//seff_file.Get_list_sel(),
									"0",//seff_file.Get_list_is_open()
									seff_file.Get_file_type()
							));
						}
						//SEffAdapterSF[2].notifyDataSetChanged();
						SEffAdapterSF[2] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Love);
						mListViewSF[2].setAdapter(SEffAdapterSF[2]);
						initSEffAdapterClickListen();
						break;
					case PAGE_Recently:
						seffFile_list.clear();
						seffFile_list = DataStruct.dbSEfFile_Recently_Table.OrderBy("data_user_name", bool_OrderMode);
						SEff_List_Recently.clear();
						for(int i=0;i<seffFile_list.size();i++){
							seff_file = seffFile_list.get(i);
							SEff_List_Recently.add(new SEff_ListDat(
									seff_file.Get_id(),
									seff_file.Get_file_name(),
									seff_file.Get_file_path(),
									seff_file.Get_data_user_name(),
									seff_file.Get_data_upload_time(),
									seff_file.Get_file_favorite(),
									seff_file.Get_file_love(),
									"0",//seff_file.Get_list_sel(),
									"0",//seff_file.Get_list_is_open()
									seff_file.Get_file_type()
							));
						}
						//SEffAdapterSF[3].notifyDataSetChanged();
						SEffAdapterSF[2] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Recently);
						mListViewSF[2].setAdapter(SEffAdapterSF[2]);
						initSEffAdapterClickListen();
						break;
					default:
						break;
				}
			}
		});
		//全选，非全选
		LY_ListCheckAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//
				if(!bool_SelMode){//false：非全选，true:全选
					bool_SelMode=true;
					B_SelMode.setBackground(getResources().getDrawable(R.drawable.chs_seff_sel_press));

					switch (CUR_PAGE) {
						case PAGE_Name:
							for(int i=0;i<SEff_List_Name.size();i++){
								SEff_List_Name.get(i).setSel(SEff_ListDat.LIST_DROP_SELED);
							}
							SEffAdapterSF[0].notifyDataSetChanged();
							break;
						case PAGE_Favorite:
							for(int i=0;i<SEff_List_Favorite.size();i++){
								SEff_List_Favorite.get(i).setSel(SEff_ListDat.LIST_DROP_SELED);
							}
							SEffAdapterSF[1].notifyDataSetChanged();
							break;
						case PAGE_Love:
							for(int i=0;i<SEff_List_Love.size();i++){
								SEff_List_Love.get(i).setSel(SEff_ListDat.LIST_DROP_SELED);
							}
							SEffAdapterSF[2].notifyDataSetChanged();
							break;
						case PAGE_Recently:
							for(int i=0;i<SEff_List_Recently.size();i++){
								SEff_List_Recently.get(i).setSel(SEff_ListDat.LIST_DROP_SELED);
							}
							SEffAdapterSF[2].notifyDataSetChanged();
							break;



						default:
							break;
					}

				}else{
					bool_SelMode=false;
					B_SelMode.setBackground(getResources().getDrawable(R.drawable.chs_seff_sel_normal));

					switch (CUR_PAGE) {
						case PAGE_Name:
							for(int i=0;i<SEff_List_Name.size();i++){
								SEff_List_Name.get(i).setSel(SEff_ListDat.LIST_DROP_WSEL);
							}
							SEffAdapterSF[0].notifyDataSetChanged();
							break;
						case PAGE_Favorite:
							for(int i=0;i<SEff_List_Favorite.size();i++){
								SEff_List_Favorite.get(i).setSel(SEff_ListDat.LIST_DROP_WSEL);
							}
							SEffAdapterSF[1].notifyDataSetChanged();
							break;
						case PAGE_Love:
							for(int i=0;i<SEff_List_Love.size();i++){
								SEff_List_Love.get(i).setSel(SEff_ListDat.LIST_DROP_WSEL);
							}
							SEffAdapterSF[2].notifyDataSetChanged();
							break;
						case PAGE_Recently:
							for(int i=0;i<SEff_List_Recently.size();i++){
								SEff_List_Recently.get(i).setSel(SEff_ListDat.LIST_DROP_WSEL);
							}
							SEffAdapterSF[2].notifyDataSetChanged();
							break;



						default:
							break;
					}
				}
				FlashItemSel();
			}
		});
		//多选模式
		LY_ListMultiselect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//
				//false：非多选模式，true:多选模式
				bool_MultiselectMode=true;
				LY_ListOrder.setVisibility(android.view.View.GONE);
				LY_ListMultiselect.setVisibility(android.view.View.GONE);
				LY_ListCheckAll.setVisibility(android.view.View.VISIBLE);
				TV_ListSelected.setVisibility(android.view.View.VISIBLE);
				LY_ListOpt.setVisibility(android.view.View.VISIBLE);
				LY_SelBottomBar.setVisibility(android.view.View.VISIBLE);
				B_SelMode.setBackground(getResources().getDrawable(R.drawable.chs_seff_sel_normal));


				switch (CUR_PAGE) {
					case PAGE_Name:
						for(int i=0;i<SEff_List_Name.size();i++){
							SEff_List_Name.get(i).setSel(SEff_ListDat.LIST_DROP_WSEL);
						}
						SEffAdapterSF[0].notifyDataSetChanged();
						break;

					case PAGE_Favorite:
						for(int i=0;i<SEff_List_Favorite.size();i++){
							SEff_List_Favorite.get(i).setSel(SEff_ListDat.LIST_DROP_WSEL);
						}
						SEffAdapterSF[1].notifyDataSetChanged();
						break;

					case PAGE_Love:
						for(int i=0;i<SEff_List_Love.size();i++){
							SEff_List_Love.get(i).setSel(SEff_ListDat.LIST_DROP_WSEL);
						}
						SEffAdapterSF[2].notifyDataSetChanged();
						break;
					case PAGE_Recently:
						for(int i=0;i<SEff_List_Recently.size();i++){
							SEff_List_Recently.get(i).setSel(SEff_ListDat.LIST_DROP_WSEL);
						}
						SEffAdapterSF[2].notifyDataSetChanged();
						break;

					default:
						break;
				}

			}
		});
		LY_ListOpt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//
				//false：非多选模式，true:多选模式
				bool_MultiselectMode=false;
				LY_ListMultiselect.setVisibility(android.view.View.GONE);
				LY_ListOrder.setVisibility(android.view.View.VISIBLE);
				LY_ListCheckAll.setVisibility(android.view.View.GONE);
				TV_ListSelected.setVisibility(android.view.View.GONE);
				LY_ListOpt.setVisibility(android.view.View.GONE);
				LY_SelBottomBar.setVisibility(android.view.View.GONE);

				switch (CUR_PAGE) {
					case PAGE_Name:
						for(int i=0;i<SEff_List_Name.size();i++){
							SEff_List_Name.get(i).setSel(SEff_ListDat.LIST_HEAD_HIDE);
						}
						SEffAdapterSF[0].notifyDataSetChanged();
						break;
					case PAGE_Favorite:
						for(int i=0;i<SEff_List_Favorite.size();i++){
							SEff_List_Favorite.get(i).setSel(SEff_ListDat.LIST_HEAD_HIDE);
						}
						SEffAdapterSF[1].notifyDataSetChanged();
						break;
					case PAGE_Love:
						for(int i=0;i<SEff_List_Love.size();i++){
							SEff_List_Love.get(i).setSel(SEff_ListDat.LIST_HEAD_HIDE);
						}
						SEffAdapterSF[2].notifyDataSetChanged();
						break;
					case PAGE_Recently:
						for(int i=0;i<SEff_List_Recently.size();i++){
							SEff_List_Recently.get(i).setSel(SEff_ListDat.LIST_HEAD_HIDE);
						}
						SEffAdapterSF[2].notifyDataSetChanged();
						break;


					default:
						break;
				}
			}
		});

		for(int i=0;i<4;i++){
			LY_SelBottomBarItem[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					int cindex=(Integer) arg0.getTag();
					boolean hasSel=false;
					SEff_List_Temp.clear();
					switch (CUR_PAGE) {
						case PAGE_Name:
							if(cindex==OPT_Share){

							}else if(cindex==OPT_Favorite){
								boolFavorite[0]=!boolFavorite[0];
								for(int i=0;i<SEff_List_Name.size();i++){
									if(SEff_List_Name.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										seff_file = DataStruct.dbSEfFile_Table.find("id", String.valueOf(SEff_List_Name.get(i).getId()));

										if(boolFavorite[0]){
											SEff_List_Name.get(i).setFravorite("1");
											seff_file.Set_file_favorite("1");
										}else{
											SEff_List_Name.get(i).setFravorite("0");
											seff_file.Set_file_favorite("0");
										}

										DataStruct.dbSEfFile_Table.update("id", String.valueOf(SEff_List_Name.get(i).getId()), seff_file);
									}
								}
								SEffAdapterSF[0].notifyDataSetChanged();
							}else if(cindex==OPT_Love){
								boolLove[0]=!boolLove[0];
								for(int i=0;i<SEff_List_Name.size();i++){
									if(SEff_List_Name.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										seff_file = DataStruct.dbSEfFile_Table.find("id", String.valueOf(SEff_List_Name.get(i).getId()));

										if(boolLove[0]){
											SEff_List_Name.get(i).setLove("1");
											seff_file.Set_file_love("1");
										}else{
											SEff_List_Name.get(i).setLove("0");
											seff_file.Set_file_love("0");
										}

										DataStruct.dbSEfFile_Table.update("id", String.valueOf(SEff_List_Name.get(i).getId()), seff_file);
									}
								}
								SEffAdapterSF[0].notifyDataSetChanged();
							}else if(cindex==OPT_Delete){
								hasSel=false;
								for(int i=0;i<SEff_List_Name.size();i++){
									if(SEff_List_Name.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										hasSel=true;
										break;
									}
								}

								if(hasSel){
									System.out.println("BUG  hasSel-："+hasSel);
									ShowDeleteSeffDialog(0,seff_file,0);
								}
							}
							FlashItemSel();
							break;
						case PAGE_Favorite:

							if(cindex==OPT_Share){

							}else if(cindex==OPT_Favorite){
								boolFavorite[1]=!boolFavorite[1];
								for(int i=0;i<SEff_List_Favorite.size();i++){
									if(SEff_List_Favorite.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										seff_file = DataStruct.dbSEfFile_Table.find("id", String.valueOf(SEff_List_Favorite.get(i).getId()));

										if(boolFavorite[1]){
											SEff_List_Favorite.get(i).setFravorite("1");
											seff_file.Set_file_favorite("1");
										}else{
											SEff_List_Favorite.get(i).setFravorite("0");
											seff_file.Set_file_favorite("0");
										}

										DataStruct.dbSEfFile_Table.update("id", String.valueOf(SEff_List_Favorite.get(i).getId()), seff_file);
									}
								}
								SEffAdapterSF[1].notifyDataSetChanged();
							}else if(cindex==OPT_Love){
								boolLove[1]=!boolLove[1];
								for(int i=0;i<SEff_List_Favorite.size();i++){
									if(SEff_List_Favorite.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										seff_file = DataStruct.dbSEfFile_Table.find("id", String.valueOf(SEff_List_Favorite.get(i).getId()));

										if(boolLove[1]){
											SEff_List_Favorite.get(i).setLove("1");
											seff_file.Set_file_love("1");
										}else{
											SEff_List_Favorite.get(i).setLove("0");
											seff_file.Set_file_love("0");
										}

										DataStruct.dbSEfFile_Table.update("id", String.valueOf(SEff_List_Favorite.get(i).getId()), seff_file);
									}
								}
								SEffAdapterSF[1].notifyDataSetChanged();
							}else if(cindex==OPT_Delete){
								hasSel=false;
								for(int i=0;i<SEff_List_Favorite.size();i++){
									if(SEff_List_Favorite.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										hasSel=true;
										break;
									}
								}

								if(hasSel){
									ShowDeleteSeffDialog(0,seff_file,0);
								}
							}
							FlashItemSel();
							break;
						case PAGE_Love:

							if(cindex==OPT_Share){

							}else if(cindex==OPT_Favorite){
								boolFavorite[2]=!boolFavorite[2];
								for(int i=0;i<SEff_List_Love.size();i++){
									if(SEff_List_Love.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										seff_file = DataStruct.dbSEfFile_Table.find("id", String.valueOf(SEff_List_Love.get(i).getId()));

										if(boolFavorite[2]){
											SEff_List_Love.get(i).setFravorite("1");
											seff_file.Set_file_favorite("1");
										}else{
											SEff_List_Love.get(i).setFravorite("0");
											seff_file.Set_file_favorite("0");
										}

										DataStruct.dbSEfFile_Table.update("id", String.valueOf(SEff_List_Love.get(i).getId()), seff_file);
									}
								}
								SEffAdapterSF[2].notifyDataSetChanged();
							}else if(cindex==OPT_Love){
								boolLove[2]=!boolLove[2];
								for(int i=0;i<SEff_List_Love.size();i++){
									if(SEff_List_Love.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										seff_file = DataStruct.dbSEfFile_Table.find("id", String.valueOf(SEff_List_Love.get(i).getId()));

										if(boolLove[2]){
											SEff_List_Love.get(i).setLove("1");
											seff_file.Set_file_love("1");
										}else{
											SEff_List_Love.get(i).setLove("0");
											seff_file.Set_file_love("0");
										}

										DataStruct.dbSEfFile_Table.update("id", String.valueOf(SEff_List_Love.get(i).getId()), seff_file);
									}
								}
								SEffAdapterSF[2].notifyDataSetChanged();
							}else if(cindex==OPT_Delete){
								hasSel=false;
								for(int i=0;i<SEff_List_Love.size();i++){
									if(SEff_List_Love.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										hasSel=true;
										break;
									}
								}

								if(hasSel){
									ShowDeleteSeffDialog(0,seff_file,0);
								}
							}
							FlashItemSel();
							break;
						case PAGE_Recently:

							if(cindex==OPT_Share){

							}else if(cindex==OPT_Favorite){
								boolFavorite[3]=!boolFavorite[3];
								for(int i=0;i<SEff_List_Recently.size();i++){
									if(SEff_List_Recently.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", String.valueOf(SEff_List_Recently.get(i).getId()));

										if(boolFavorite[3]){
											SEff_List_Recently.get(i).setFravorite("1");
											seff_file.Set_file_favorite("1");
										}else{
											SEff_List_Recently.get(i).setFravorite("0");
											seff_file.Set_file_favorite("0");
										}

										DataStruct.dbSEfFile_Recently_Table.update("id", String.valueOf(SEff_List_Recently.get(i).getId()), seff_file);
									}
								}
								SEffAdapterSF[2].notifyDataSetChanged();
							}else if(cindex==OPT_Love){
								boolLove[3]=!boolLove[3];
								for(int i=0;i<SEff_List_Recently.size();i++){
									if(SEff_List_Recently.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", String.valueOf(SEff_List_Recently.get(i).getId()));

										if(boolLove[3]){
											SEff_List_Recently.get(i).setLove("1");
											seff_file.Set_file_love("1");
										}else{
											SEff_List_Recently.get(i).setLove("0");
											seff_file.Set_file_love("0");
										}

										DataStruct.dbSEfFile_Recently_Table.update("id", String.valueOf(SEff_List_Recently.get(i).getId()), seff_file);
									}
								}
								SEffAdapterSF[2].notifyDataSetChanged();
							}else if(cindex==OPT_Delete){
								hasSel=false;
								for(int i=0;i<SEff_List_Recently.size();i++){
									if(SEff_List_Recently.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
										hasSel=true;
										break;
									}
								}

								if(hasSel){
									ShowDeleteSeffDialog(0,seff_file,0);
								}
							}
							FlashItemSel();
							break;
						default:
							break;
					}

				}
			});
		}
	}
	//弹出菜单
	public void PM_ConMenu(View v) {
		PM_ConMenu.show();
	}
	//头栏目选择
	private void TitleSel(int sel){
		switch (sel) {
			case 0: searchKeyword = "file_name";
				CUR_PAGE=PAGE_Name;
				LY_ListOrderSel.setVisibility(android.view.View.VISIBLE);
				break;//data_user_name//data_upload_time
			case 1: searchKeyword = "file_favorite";
				CUR_PAGE=PAGE_Favorite;
				LY_ListOrderSel.setVisibility(android.view.View.VISIBLE);
				break;
			case 2: searchKeyword = "file_love";
				CUR_PAGE=PAGE_Love;
				LY_ListOrderSel.setVisibility(android.view.View.VISIBLE);
				break;
			case 3:
				CUR_PAGE=PAGE_Recently;
				LY_ListOrderSel.setVisibility(android.view.View.VISIBLE);
				break;
			default: break;
		}

		for(int i=0;i<Title_NUM;i++){
			B_Title[i].setTextColor(getResources().getColor(R.color.dowloadtitle_color_normal));
			View[i].setBackgroundColor(getResources().getColor(R.color.dowloadtitle_vcolor_normal));
		}
		B_Title[sel].setTextColor(getResources().getColor(R.color.dowloadtitle_color_press));
		View[sel].setBackgroundColor(getResources().getColor(R.color.dowloadtitle_vcolor_press));
		//更新数据
		initListData(sel);
		SEffAdapterSF[sel].notifyDataSetChanged();
		initSEffAdapterClickListen();
		//恢复排序多选项目
		bool_MultiselectMode=false;
		LY_ListMultiselect.setVisibility(android.view.View.GONE);
		LY_ListOrder.setVisibility(android.view.View.VISIBLE);
		LY_ListCheckAll.setVisibility(android.view.View.GONE);
		TV_ListSelected.setVisibility(android.view.View.GONE);
		LY_ListOpt.setVisibility(android.view.View.GONE);
		LY_SelBottomBar.setVisibility(android.view.View.GONE);
	}

	/**
	 * 旋转切换图标
	 * @param v
	 * @param item
	 */
//    private void rotateSwitcherIcon(View v, SEff_ListDat item){
//        float degree = item.isOpen().equals(SEff_ListDat.LIST_DROP_DOWN_OPEN) ? 180 : 0;
//         ViewPropertyAnimator.animate(v)
//        .setDuration(100)
//        .setInterpolator(new AccelerateDecelerateInterpolator())
//        .rotation(degree)
//        .start();
//    }
///////////////////////////////////////////////  界面  ////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 初始化ViewPager
	 */
	@SuppressLint("InflateParams") public void InitViewPager() {
		VP_CHS_Pager = (MVP_ViewPage) findViewById(R.id.vPager);
		//VP_CHS_Pager.setNoScroll(true);
		LayoutInflater LV_CHS_LI = LayoutInflater.from(this);
		final ArrayList<View> LV_CHS_list = new ArrayList<View>();
		LY_VIEW[0] = LV_CHS_LI.inflate(R.layout.chs_sefflistview_all, null);
		LY_VIEW[1] = LV_CHS_LI.inflate(R.layout.chs_sefflistview_favorite, null);
		//LY_VIEW[2] = LV_CHS_LI.inflate(R.layout.chs_sefflistview_collect, null);
		LY_VIEW[2] = LV_CHS_LI.inflate(R.layout.chs_sefflistview_recently, null);

		LV_CHS_list.add(LY_VIEW[0]);
		LV_CHS_list.add(LY_VIEW[1]);
		LV_CHS_list.add(LY_VIEW[2]);
		//LV_CHS_list.add(LY_VIEW[3]);

		CHS_PageAdapter myPageAdapter = new CHS_PageAdapter(LV_CHS_list);
		VP_CHS_Pager.setAdapter(myPageAdapter);
		VP_CHS_Pager.setOnPageChangeListener(new MyOnPageChangeListener());
		//VP_CHS_Pager.setOffscreenPageLimit(5);
		mListViewSF[0] = (ListView) LY_VIEW[0].findViewById(R.id.seff_download_listview_all);
		mListViewSF[1] = (ListView) LY_VIEW[1].findViewById(R.id.seff_download_listview_favorite);
		//  mListViewSF[2] = (ListView) LY_VIEW[2].findViewById(R.id.seff_download_listview_collect);
		mListViewSF[2] = (ListView) LY_VIEW[2].findViewById(R.id.seff_download_listview_recently);

	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			TitleSel(arg0);
			CUR_PAGE = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			//解锁onTouchEvent(MotionEvent event)可以传到父控制
			VP_CHS_Pager.setNoScrollOnIntercept(false);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void insertRecentlyTable(SEFF_File seff_file) {
		seff_file.Set_list_is_open("0");
		seff_file.Set_list_sel("0");

		SEFF_File sfile = DataStruct.dbSEfFile_Recently_Table.find("file_name",seff_file.Get_file_name());
		if(sfile==null){//列表无则增加到列表 
			DataStruct.dbSEfFile_Recently_Table.insert(seff_file);
		}else{//列表有则提到最上层
			seffFile_list.clear();
			seffFile_list = DataStruct.dbSEfFile_Recently_Table.getTableList();

			for(int i=0;i<seffFile_list.size();i++){
				//System.out.println("BUG Get_file_name:"+seffFile_list.get(i).Get_file_name());
				if(seffFile_list.get(i).Get_file_name().equals(seff_file.Get_file_name())){
					seffFile_list.remove(i);
				}
			}

			DataStruct.dbSEfFile_Recently_Table.ResetTable();
			//把删除相关数据的表复写到数据表中
			if(seffFile_list.size()>0){
				for(int i=0;i < seffFile_list.size();i++){
					DataStruct.dbSEfFile_Recently_Table.insert(seffFile_list.get(i));
				}
			}
			DataStruct.dbSEfFile_Recently_Table.insert(seff_file);
		}
		if(CUR_PAGE==PAGE_Recently){
			initListData(CUR_PAGE);
		}
	}
	private void ShowDeleteSeffDialog(final int SM,final SEFF_File seff_file,final int postion){
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.DialogDel);
		//builder.setIcon(R.drawable.chs_dialog_warning);
		builder.setMessage(R.string.DialogDelMsg);

		builder.setPositiveButton(R.string.Sure, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				if(SM==0){
					DeleteSeff();
				}else if(SM==1){
					DeleteSingleSeff(seff_file,postion);
				}
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				//这里添加点击确定后的逻辑  
			}
		});
		builder.create().show();
	}
	//TODO
	private void DeleteSeff() {
		int index=0;
		if(CUR_PAGE==PAGE_Name){
			do{
				if(SEff_List_Name.get(index).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
					File dir = new File(SEff_List_Name.get(index).getfilePath());
					if (dir.isFile()) {
						dir.delete();
					} else {
						FileUtil.deleteDir(dir);
					}

					DataStruct.dbSEfFile_Table.delete("id", String.valueOf(SEff_List_Name.get(index).getId()));
					SEff_List_Name.remove(index);
				}else{
					++index;
				}
			}while(SEff_List_Name.size()>index);

			seffFile_list.clear();
			seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
			int toal=seffFile_list.size();
			//System.out.println("BUG seffFile_list toal="+toal);
			//清除列表再重新组合
			DataStruct.dbSEfFile_Table.ResetTable();
			if(toal>0){
				for(int i=0;i < toal;i++){
					DataStruct.dbSEfFile_Table.insert(new SEFF_File(
							seffFile_list.get(i).Get_file_id(),//file_id
							seffFile_list.get(i).Get_file_type(),//file_type
							seffFile_list.get(i).Get_file_name(),//file_name
							seffFile_list.get(i).Get_file_path(),//file_path
							seffFile_list.get(i).Get_file_favorite(),//file_favorite
							seffFile_list.get(i).Get_file_love(),//file_collect
							seffFile_list.get(i).Get_file_size(),//file_size
							seffFile_list.get(i).Get_file_time(),//file_time
							seffFile_list.get(i).Get_file_msg(),//file_msg

							seffFile_list.get(i).Get_data_user_name(),//data_user_name
							seffFile_list.get(i).Get_data_machine_type(),//data_machine_type
							seffFile_list.get(i).Get_data_car_type(),//data_car_type
							seffFile_list.get(i).Get_data_car_brand(),//data_car_brand
							seffFile_list.get(i).Get_data_group_name(),//data_group_name
							seffFile_list.get(i).Get_data_upload_time(),//data_upload_time
							seffFile_list.get(i).Get_data_eff_briefing(),//data_eff_briefing

							SEff_ListDat.LIST_DROP_WSEL,//list_sel
							SEff_ListDat.LIST_DROP_DOWN_CLOSE//list_is_open
					));
				}
				SEff_List_Name.clear();
				seffFile_list.clear();
				seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
				toal=seffFile_list.size();
				//System.out.println("BUG seffFile_list in.size="+seffFile_list.size());
				for(int i=0;i<toal;i++){
					seff_file = seffFile_list.get(i);
					SEff_List_Name.add(new SEff_ListDat(
							seff_file.Get_id(),
							seff_file.Get_file_name(),
							seff_file.Get_file_path(),
							seff_file.Get_data_user_name(),
							seff_file.Get_data_upload_time(),
							seff_file.Get_file_favorite(),
							seff_file.Get_file_love(),
							SEff_ListDat.LIST_DROP_WSEL,//list_sel
							SEff_ListDat.LIST_DROP_DOWN_CLOSE,//list_is_open
							seff_file.Get_file_type()
					));
				}
			}
			//System.out.println("BUG SEff_List_Name.size="+SEff_List_Name.size());
			SEffAdapterSF[0] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Name);
			mListViewSF[0].setAdapter(SEffAdapterSF[0]);
		}else if(CUR_PAGE==PAGE_Favorite){
			do{
				if(SEff_List_Favorite.get(index).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
					File dir = new File(SEff_List_Favorite.get(index).getfilePath());
					if (dir.isFile()) {
						dir.delete();
					} else {
						FileUtil.deleteDir(dir);
					}

					DataStruct.dbSEfFile_Table.delete("id", String.valueOf(SEff_List_Favorite.get(index).getId()));
					SEff_List_Favorite.remove(index);
				}else{
					++index;
				}
			}while(SEff_List_Favorite.size()>index);

			seffFile_list.clear();
			seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
			int toal=seffFile_list.size();
			//System.out.println("BUG seffFile_list toal="+toal);
			//清除列表再重新组合
			DataStruct.dbSEfFile_Table.ResetTable();
			if(toal>0){
				for(int i=0;i < toal;i++){
					DataStruct.dbSEfFile_Table.insert(new SEFF_File(
							seffFile_list.get(i).Get_file_id(),//file_id
							seffFile_list.get(i).Get_file_type(),//file_type
							seffFile_list.get(i).Get_file_name(),//file_name
							seffFile_list.get(i).Get_file_path(),//file_path
							seffFile_list.get(i).Get_file_favorite(),//file_favorite
							seffFile_list.get(i).Get_file_love(),//file_collect
							seffFile_list.get(i).Get_file_size(),//file_size
							seffFile_list.get(i).Get_file_time(),//file_time
							seffFile_list.get(i).Get_file_msg(),//file_msg

							seffFile_list.get(i).Get_data_user_name(),//data_user_name
							seffFile_list.get(i).Get_data_machine_type(),//data_machine_type
							seffFile_list.get(i).Get_data_car_type(),//data_car_type
							seffFile_list.get(i).Get_data_car_brand(),//data_car_brand
							seffFile_list.get(i).Get_data_group_name(),//data_group_name
							seffFile_list.get(i).Get_data_upload_time(),//data_upload_time
							seffFile_list.get(i).Get_data_eff_briefing(),//data_eff_briefing

							SEff_ListDat.LIST_DROP_WSEL,//list_sel
							SEff_ListDat.LIST_DROP_DOWN_CLOSE//list_is_open
					));
				}
				SEff_List_Favorite.clear();
				seffFile_list.clear();
				seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
				toal=seffFile_list.size();
				//System.out.println("BUG seffFile_list in.size="+seffFile_list.size());
				for(int i=0;i<toal;i++){
					seff_file = seffFile_list.get(i);
					SEff_List_Favorite.add(new SEff_ListDat(
							seff_file.Get_id(),
							seff_file.Get_file_name(),
							seff_file.Get_file_path(),
							seff_file.Get_data_user_name(),
							seff_file.Get_data_upload_time(),
							seff_file.Get_file_favorite(),
							seff_file.Get_file_love(),
							SEff_ListDat.LIST_DROP_WSEL,//list_sel
							SEff_ListDat.LIST_DROP_DOWN_CLOSE,//list_is_open
							seff_file.Get_file_type()
					));
				}
			}
			//System.out.println("BUG SEff_List_Favorite.size="+SEff_List_Favorite.size());
			SEffAdapterSF[1] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Favorite);
			mListViewSF[1].setAdapter(SEffAdapterSF[1]);
		}else if(CUR_PAGE==PAGE_Love){
			do{
				if(SEff_List_Love.get(index).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
					File dir = new File(SEff_List_Love.get(index).getfilePath());
					if (dir.isFile()) {
						dir.delete();
					} else {
						FileUtil.deleteDir(dir);
					}

					DataStruct.dbSEfFile_Table.delete("id", String.valueOf(SEff_List_Love.get(index).getId()));
					SEff_List_Love.remove(index);
				}else{
					++index;
				}
			}while(SEff_List_Love.size()>index);

			seffFile_list.clear();
			seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
			int toal=seffFile_list.size();
			// System.out.println("BUG seffFile_list toal="+toal);
			//清除列表再重新组合
			DataStruct.dbSEfFile_Table.ResetTable();
			if(toal>0){
				for(int i=0;i < toal;i++){
					DataStruct.dbSEfFile_Table.insert(new SEFF_File(
							seffFile_list.get(i).Get_file_id(),//file_id
							seffFile_list.get(i).Get_file_type(),//file_type
							seffFile_list.get(i).Get_file_name(),//file_name
							seffFile_list.get(i).Get_file_path(),//file_path
							seffFile_list.get(i).Get_file_favorite(),//file_favorite
							seffFile_list.get(i).Get_file_love(),//file_collect
							seffFile_list.get(i).Get_file_size(),//file_size
							seffFile_list.get(i).Get_file_time(),//file_time
							seffFile_list.get(i).Get_file_msg(),//file_msg

							seffFile_list.get(i).Get_data_user_name(),//data_user_name
							seffFile_list.get(i).Get_data_machine_type(),//data_machine_type
							seffFile_list.get(i).Get_data_car_type(),//data_car_type
							seffFile_list.get(i).Get_data_car_brand(),//data_car_brand
							seffFile_list.get(i).Get_data_group_name(),//data_group_name
							seffFile_list.get(i).Get_data_upload_time(),//data_upload_time
							seffFile_list.get(i).Get_data_eff_briefing(),//data_eff_briefing

							SEff_ListDat.LIST_DROP_WSEL,//list_sel
							SEff_ListDat.LIST_DROP_DOWN_CLOSE//list_is_open
					));
				}
				SEff_List_Love.clear();
				seffFile_list.clear();
				seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
				toal=seffFile_list.size();
				//System.out.println("BUG seffFile_list in.size="+seffFile_list.size());
				for(int i=0;i<toal;i++){
					seff_file = seffFile_list.get(i);
					SEff_List_Love.add(new SEff_ListDat(
							seff_file.Get_id(),
							seff_file.Get_file_name(),
							seff_file.Get_file_path(),
							seff_file.Get_data_user_name(),
							seff_file.Get_data_upload_time(),
							seff_file.Get_file_favorite(),
							seff_file.Get_file_love(),
							SEff_ListDat.LIST_DROP_WSEL,//list_sel
							SEff_ListDat.LIST_DROP_DOWN_CLOSE,//list_is_open
							seff_file.Get_file_type()
					));
				}
			}
			//System.out.println("BUG SEff_List_Name.size="+SEff_List_Love.size());
			SEffAdapterSF[2] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Love);
			mListViewSF[2].setAdapter(SEffAdapterSF[2]);
		}else if(CUR_PAGE==PAGE_Recently){
			do{
				if(SEff_List_Recently.get(index).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
					File dir = new File(SEff_List_Recently.get(index).getfilePath());
					if (dir.isFile()) {
						dir.delete();
					} else {
						FileUtil.deleteDir(dir);
					}

					DataStruct.dbSEfFile_Recently_Table.delete("id", String.valueOf(SEff_List_Recently.get(index).getId()));
					SEff_List_Recently.remove(index);
				}else{
					++index;
				}
			}while(SEff_List_Recently.size()>index);

			seffFile_list.clear();
			seffFile_list = DataStruct.dbSEfFile_Recently_Table.getTableList();
			int toal=seffFile_list.size();
			//System.out.println("BUG seffFile_list toal="+toal);
			//清除列表再重新组合
			DataStruct.dbSEfFile_Recently_Table.ResetTable();
			if(toal>0){
				for(int i=0;i < toal;i++){
					DataStruct.dbSEfFile_Recently_Table.insert(new SEFF_File(
							seffFile_list.get(i).Get_file_id(),//file_id
							seffFile_list.get(i).Get_file_type(),//file_type
							seffFile_list.get(i).Get_file_name(),//file_name
							seffFile_list.get(i).Get_file_path(),//file_path
							seffFile_list.get(i).Get_file_favorite(),//file_favorite
							seffFile_list.get(i).Get_file_love(),//file_collect
							seffFile_list.get(i).Get_file_size(),//file_size
							seffFile_list.get(i).Get_file_time(),//file_time
							seffFile_list.get(i).Get_file_msg(),//file_msg

							seffFile_list.get(i).Get_data_user_name(),//data_user_name
							seffFile_list.get(i).Get_data_machine_type(),//data_machine_type
							seffFile_list.get(i).Get_data_car_type(),//data_car_type
							seffFile_list.get(i).Get_data_car_brand(),//data_car_brand
							seffFile_list.get(i).Get_data_group_name(),//data_group_name
							seffFile_list.get(i).Get_data_upload_time(),//data_upload_time
							seffFile_list.get(i).Get_data_eff_briefing(),//data_eff_briefing

							SEff_ListDat.LIST_DROP_WSEL,//list_sel
							SEff_ListDat.LIST_DROP_DOWN_CLOSE//list_is_open
					));
				}
				SEff_List_Recently.clear();
				seffFile_list.clear();
				seffFile_list = DataStruct.dbSEfFile_Recently_Table.getTableList();
				toal=seffFile_list.size();
				//System.out.println("BUG seffFile_list in.size="+seffFile_list.size());
				for(int i=0;i<toal;i++){
					seff_file = seffFile_list.get(i);
					SEff_List_Recently.add(new SEff_ListDat(
							seff_file.Get_id(),
							seff_file.Get_file_name(),
							seff_file.Get_file_path(),
							seff_file.Get_data_user_name(),
							seff_file.Get_data_upload_time(),
							seff_file.Get_file_favorite(),
							seff_file.Get_file_love(),
							SEff_ListDat.LIST_DROP_WSEL,//list_sel
							SEff_ListDat.LIST_DROP_DOWN_CLOSE,//list_is_open
							seff_file.Get_file_type()
					));
				}
			}
			//System.out.println("BUG SEff_List_Name.size="+SEff_List_Recently.size());
			SEffAdapterSF[2] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Recently);
			mListViewSF[2].setAdapter(SEffAdapterSF[2]);
		}
		initSEffAdapterClickListen();
		TV_ListSelected.setText(getResources().getString(R.string.seffitem_haveSel)
				+":"+String.valueOf(0));
	}
	private void DeleteSingleSeff(SEFF_File seff_file, int postion) {
		if(CUR_PAGE==PAGE_Name){
			File dir = new File(seff_file.Get_file_path());
			if (dir.isFile()) {
				dir.delete();
			} else {
				FileUtil.deleteDir(dir);
			}
			if (dir == null || !dir.exists()) {
				Toast.makeText(SEff_DownloadActivity.this, R.string.delSuccess,
						Toast.LENGTH_SHORT).show();
				DataStruct.dbSEfFile_Table.delete("id", String.valueOf(SEff_List_Name.get(postion).getId()));
				SEff_List_Name.remove(postion);

				//SEffAdapterSF[0].notifyDataSetChanged();
				SEffAdapterSF[0] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Name);
				mListViewSF[0].setAdapter(SEffAdapterSF[0]);
				initSEffAdapterClickListen();
			} else {
				Toast.makeText(SEff_DownloadActivity.this, R.string.delFailed,
						Toast.LENGTH_SHORT).show();
			}

		}else if(CUR_PAGE==PAGE_Favorite){
			File dir = new File(seff_file.Get_file_path());
			if (dir.isFile()) {
				dir.delete();
			} else {
				FileUtil.deleteDir(dir);
			}
			if (dir == null || !dir.exists()) {
				Toast.makeText(SEff_DownloadActivity.this, R.string.delSuccess,
						Toast.LENGTH_SHORT).show();
				DataStruct.dbSEfFile_Table.delete("id", String.valueOf(SEff_List_Favorite.get(postion).getId()));
				SEff_List_Favorite.remove(postion);

				//SEffAdapterSF[1].notifyDataSetChanged();
				SEffAdapterSF[1] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Favorite);
				mListViewSF[1].setAdapter(SEffAdapterSF[1]);
				initSEffAdapterClickListen();
			} else {
				Toast.makeText(SEff_DownloadActivity.this, R.string.delFailed,
						Toast.LENGTH_SHORT).show();
			}
		}else if(CUR_PAGE==PAGE_Love){
			File dir = new File(seff_file.Get_file_path());
			if (dir.isFile()) {
				dir.delete();
			} else {
				FileUtil.deleteDir(dir);
			}
			if (dir == null || !dir.exists()) {
				Toast.makeText(SEff_DownloadActivity.this, R.string.delSuccess,
						Toast.LENGTH_SHORT).show();
				DataStruct.dbSEfFile_Table.delete("id", String.valueOf(SEff_List_Love.get(postion).getId()));
				SEff_List_Love.remove(postion);

				//SEffAdapterSF[2].notifyDataSetChanged();
				SEffAdapterSF[2] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Love);
				mListViewSF[2].setAdapter(SEffAdapterSF[2]);
				initSEffAdapterClickListen();
			} else {
				Toast.makeText(SEff_DownloadActivity.this, R.string.delFailed,
						Toast.LENGTH_SHORT).show();
			}
		}else if(CUR_PAGE==PAGE_Recently){
			File dir = new File(seff_file.Get_file_path());
			if (dir.isFile()) {
				dir.delete();
			} else {
				FileUtil.deleteDir(dir);
			}
			if (dir == null || !dir.exists()) {
				Toast.makeText(SEff_DownloadActivity.this, R.string.delSuccess,
						Toast.LENGTH_SHORT).show();
				DataStruct.dbSEfFile_Table.delete("id", String.valueOf(SEff_List_Recently.get(postion).getId()));
				SEff_List_Recently.remove(postion);

				//SEffAdapterSF[3].notifyDataSetChanged();
				SEffAdapterSF[2] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Recently);
				mListViewSF[2].setAdapter(SEffAdapterSF[2]);
				initSEffAdapterClickListen();
			} else {
				Toast.makeText(SEff_DownloadActivity.this, R.string.delFailed,
						Toast.LENGTH_SHORT).show();
			}
		}

		TV_ListSelected.setText(getResources().getString(R.string.seffitem_haveSel)
				+":"+String.valueOf(0));
	}


	private void ShowDelDialog(final  SEFF_File seff_file,int index,final int postion){



		Bundle bundle = new Bundle();

		bundle.putInt(AlertDialogFragment.ST_DataOPT, index);
		bundle.putString(AlertDialogFragment.ST_SetTitle, getResources().getString(R.string.dialog_title_Prompt));
		if(index==2){//删除
			bundle.putString(AlertDialogFragment.ST_SetMessage,getResources().getString(R.string.DialogDelMsg));
		}else{
			try {
				if(seff_file.Get_data_eff_briefing()!=null){
					System.out.println("BUG  =-=-=-="+seff_file.Get_data_eff_briefing());
					bundle.putString(AlertDialogFragment.ST_SetMessage,String.valueOf(getResources().getString(R.string.SSM_Detial)+":"+seff_file.Get_data_eff_briefing()));
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}


		if(alertDialogFragment == null){
			alertDialogFragment= new AlertDialogFragment();
		}
		if (!alertDialogFragment.isAdded()) {
			alertDialogFragment.setArguments(bundle);
			alertDialogFragment.show(getFragmentManager(), "alertDialogFragment");
		}

		alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

			@Override
			public void onClickDialogListener(int type, boolean boolClick) {
				System.out.println("BUG 当前的额只为"+type+boolClick);
				if (boolClick) {
					if(type==2) {
						DeleteSingleSeff(seff_file, postion);
					}
				}else{


				}

			}
		});
	}


	private void ShowUseSeffDialog(final SEFF_File seff_file){

		String st = "";
		if(seff_file.Get_file_type().equals(JsonDataSt.DSP_Single)){
			st = getString(R.string.opt_User);
		}else{
			st = getString(R.string.opt_UserMac);
		}

		Bundle bundle = new Bundle();
		bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
		bundle.putString(AlertDialogFragment.ST_SetMessage, st);
		bundle.putString(AlertDialogFragment.ST_SetTitle, getResources().getString(R.string.dialog_title_Prompt));
		if(alertDialogFragment == null){
			alertDialogFragment= new AlertDialogFragment();
		}
		if (!alertDialogFragment.isAdded()) {
			alertDialogFragment.setArguments(bundle);
			alertDialogFragment.show(getFragmentManager(), "alertDialogFragment");
		}

		alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

			@Override
			public void onClickDialogListener(int type, boolean boolClick) {
				if(boolClick){


					final String filePath=seff_file.Get_file_path();

					int fileType=DataStruct.jsonRWOpt.getSEFFFileType(mContext, filePath);
					////ToastMsg("GetFileType="+String.valueOf(fileType));
					if(fileType == 1){
						DataOptUtil.UpdateForJsonSingleData(filePath,mContext);
					}else if(fileType == 2){
						DataOptUtil.loadMacEffJsonData(filePath,mContext);
					}else{
						ToastUtil.showShortToast(mContext,getResources().getString(R.string.LoadSEff_Fail));
					}
					insertRecentlyTable(seff_file);

					if(!DataStruct.U0SynDataSucessFlg){
						ToastUtil.showShortToast(mContext,getResources().getString(R.string.off_line_mode));
						return;
					}
					showLoadingDialog();
				}
			}
		});


	}

	private void showLoadingDialog(){
		if(mLoadingDialogFragment == null){
			mLoadingDialogFragment = new LoadingDialogFragment();
		}
		if (!mLoadingDialogFragment.isAdded()) {
			mLoadingDialogFragment.show(getFragmentManager(), "mLoadingDialogFragment");
		}


	}

	private void FlashItemSel(){
		int sel=0;

		if(CUR_PAGE==PAGE_Name){
			if(SEff_List_Name.size()>0){
				for(int i=0;i<SEff_List_Name.size();i++){
					if(SEff_List_Name.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
						++sel;
					}
				}
			}

		}else if(CUR_PAGE==PAGE_Favorite){
			if(SEff_List_Favorite.size()>0){
				for(int i=0;i<SEff_List_Favorite.size();i++){
					if(SEff_List_Favorite.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
						++sel;
					}
				}
			}
		}else if(CUR_PAGE==PAGE_Love){
			if(SEff_List_Love.size()>0){
				for(int i=0;i<SEff_List_Love.size();i++){
					if(SEff_List_Love.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
						++sel;
					}
				}
			}
		}else if(CUR_PAGE==PAGE_Recently){
			if(SEff_List_Recently.size()>0){
				for(int i=0;i<SEff_List_Recently.size();i++){
					if(SEff_List_Recently.get(i).getSel().equals(SEff_ListDat.LIST_DROP_SELED)){
						++sel;
					}
				}
			}
		}

		TV_ListSelected.setText(getResources().getString(R.string.seffitem_haveSel)
				+":"+String.valueOf(sel));
	}
	private void initSEffAdapterClickListen(){
		SEffAdapterSF[0].setOnSeffFileAdpterOnItemClick(new setSeffFileAdpterOnItemClick() {

			@Override
			public void onAdpterClick(int which, int postion,View v) {
				final BaseAdapter adapter = (BaseAdapter) mListViewSF[0].getAdapter();
				SEff_ListDat item = (SEff_ListDat) adapter.getItem(postion);
				String db_id=String.valueOf(item.getId());

				ViewGroup itemViewCh = (ViewGroup) v.getParent().getParent().getParent();
				SEffViewHolder seffViewHolderCh = (SEffViewHolder) itemViewCh.getTag();
				SEFF_File seff_file = null;
				// TODO Auto-generated method stub
				System.out.println("BUG Click db_id:"+db_id);
				if(which == R.id.seff_id_item_sel){
					System.out.println("BUG ClickItems前项选择");

					if(item.sel.equals(SEff_ListDat.LIST_DROP_WSEL)){
						item.sel=SEff_ListDat.LIST_DROP_SELED;
					}else if(item.sel.equals(SEff_ListDat.LIST_DROP_SELED)){
						item.sel=SEff_ListDat.LIST_DROP_WSEL;
					}

					SEff_List_Name.get(postion).setSel(item.sel);
					SEffAdapterSF[0].notifyDataSetChanged();
					FlashItemSel();
					//DB
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_list_sel(item.getSel());
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_item_seff){
					if(bool_MultiselectMode){
						if(item.sel.equals(SEff_ListDat.LIST_DROP_WSEL)){
							item.sel=SEff_ListDat.LIST_DROP_SELED;
						}else if(item.sel.equals(SEff_ListDat.LIST_DROP_SELED)){
							item.sel=SEff_ListDat.LIST_DROP_WSEL;
						}

						SEff_List_Name.get(postion).setSel(item.sel);
						SEffAdapterSF[0].notifyDataSetChanged();
						FlashItemSel();
						//DB
						seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
						seff_file.Set_list_sel(item.getSel());
						DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
					}else{
						System.out.println("BUG ClickItems layout");
						seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
						ShowUseSeffDialog(seff_file);
					}
				}else if(which == R.id.id_iv_favorite){
					System.out.println("BUG ClickItems收藏");

					item.fravorite="0";
					seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love);
					seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_iv_love){
					System.out.println("BUG ClickItems喜欢");

					item.love="0";
					seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love);
					seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.seff_id_switcher_btn){
					System.out.println("BUG Click下拉B");

					//	boolean showa=false;
					if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_OPEN)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						//	showa=false;
					}else if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_CLOSE)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_OPEN);
						//	showa=true;
					}

					SEff_List_NameOld.get(postion).setOpen(item.isOpen());

//		            int defaultHeight = getResources().getDimensionPixelSize(R.dimen.seff_item_height2);
//		            itemView.startAnimation(new ExpandAnimation(seffViewHolder.get_targetPanel(), showa,defaultHeight));
//		            rotateSwitcherIcon(v, item);

					for(int i=0;i<SEff_List_Name.size();i++){
						if(i != postion ){
							SEff_List_Name.get(i).setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						}
					}
					SEffAdapterSF[0].notifyDataSetChanged();

//		            SEffAdapterSF[0] = new ListSEffItemAdapter(SEff_DownloadActivity.this, SEff_List_Name);
//			    	mListViewSF[0].setAdapter(SEffAdapterSF[0]);
//			    	initSEffAdapterClickListen();
					mListViewSF[0].setSelectionFromTop(postion, 0);

					//DB
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_list_is_open(item.isOpen());
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_1){
					System.out.println("BUG Click使用");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					ShowUseSeffDialog(seff_file);
				}else if(which == R.id.id_ly_seff_list_2){
					System.out.println("BUG Click分享1");


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					if(seff_file!=null){
						JsonRWUtil jsonRWSingleData=new JsonRWUtil();
						jsonRWSingleData.ShareLocalSoundEFFData(
								SEff_DownloadActivity.this,seff_file.Get_file_path());
					}
				}else if(which == R.id.id_ly_seff_list_3){
					System.out.println("BUG Click收藏");

					if(item.getFravorite().equals("0")){
						item.fravorite="1";
						seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love_press);
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_FavoriteBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getFravorite().equals("1")){
						item.fravorite="0";
						seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love);
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					}


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_4){
					System.out.println("BUG Click喜欢:"+item.love);

					if(item.getLove().equals("0")){
						item.love="1";
						seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love_press);
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_LoveBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getLove().equals("1")){
						item.love="0";
						seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love);
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);

					}


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_5){
					System.out.println("BUG Click删除");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					//System.out.println("DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_name());
					//

					ShowDelDialog(seff_file,2,postion);

					//ShowDeleteSeffDialog(1,seff_file,postion);
				}else if(which == R.id.id_ly_seff_list_6){
					System.out.println("BUG Click 详情");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);

					ShowDelDialog(seff_file,1,postion);
//					AlertDialog.Builder builder = new AlertDialog.Builder(SEff_DownloadActivity.this);
//					builder.setTitle(R.string.Details);
//					builder.setMessage(seff_file.Get_data_eff_briefing());
//
//					builder.setPositiveButton(R.string.Sure, new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int whichButton) {
//
//						}
//					});
//					builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int whichButton) {
//							//这里添加点击确定后的逻辑
//						}
//					});
//					builder.create().show();
				}

			}
		});


		//1
		SEffAdapterSF[1].setOnSeffFileAdpterOnItemClick(new setSeffFileAdpterOnItemClick() {

			@Override
			public void onAdpterClick(int which, int postion,View v) {
				final BaseAdapter adapter = (BaseAdapter) mListViewSF[1].getAdapter();
				SEff_ListDat item = (SEff_ListDat) adapter.getItem(postion);
				String db_id=String.valueOf(item.getId());
//	            ViewGroup itemView = (ViewGroup) v.getParent().getParent();
//	            SEffViewHolder seffViewHolder = (SEffViewHolder) itemView.getTag();

				ViewGroup itemViewCh = (ViewGroup) v.getParent().getParent().getParent();
				SEffViewHolder seffViewHolderCh = (SEffViewHolder) itemViewCh.getTag();
				SEFF_File seff_file;
				// TODO Auto-generated method stub
				System.out.println("BUG Click db_id:"+db_id);
				if(which == R.id.seff_id_item_sel){
					System.out.println("BUG ClickItems前项选择");

					if(item.sel.equals(SEff_ListDat.LIST_DROP_WSEL)){
						item.sel=SEff_ListDat.LIST_DROP_SELED;
					}else if(item.sel.equals(SEff_ListDat.LIST_DROP_SELED)){
						item.sel=SEff_ListDat.LIST_DROP_WSEL;
					}
					SEff_List_Favorite.get(postion).setSel(item.sel);
					SEffAdapterSF[1].notifyDataSetChanged();
					FlashItemSel();
					//DB
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_list_sel(item.getSel());
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_item_seff){
					if(bool_MultiselectMode){
						if(item.sel.equals(SEff_ListDat.LIST_DROP_WSEL)){
							item.sel=SEff_ListDat.LIST_DROP_SELED;
						}else if(item.sel.equals(SEff_ListDat.LIST_DROP_SELED)){
							item.sel=SEff_ListDat.LIST_DROP_WSEL;
						}
						SEff_List_Favorite.get(postion).setSel(item.sel);
						SEffAdapterSF[1].notifyDataSetChanged();
						FlashItemSel();
						//DB
						seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
						seff_file.Set_list_sel(item.getSel());
						DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
					}else{
						System.out.println("BUG ClickItems layout");
						seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
						ShowUseSeffDialog(seff_file);
					}
				}else if(which == R.id.id_iv_favorite){
					System.out.println("BUG ClickItems收藏");

					item.fravorite="0";
					seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love);
					seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_iv_love){
					System.out.println("BUG ClickItems喜欢");

					item.love="0";
					seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love);
					seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.seff_id_switcher_btn){
					System.out.println("BUG Click下拉B");


					//boolean showa=false;
					if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_OPEN)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						//	showa=false;
					}else if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_CLOSE)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_OPEN);
						//	showa=true;
					}

					SEff_List_FavoriteOld.get(postion).setOpen(item.isOpen());

//		            int defaultHeight = getResources().getDimensionPixelSize(R.dimen.seff_item_height2);
//		            itemView.startAnimation(new ExpandAnimation(seffViewHolder.get_targetPanel(), showa,defaultHeight));
//		            rotateSwitcherIcon(v, item);

					for(int i=0;i<SEff_List_Favorite.size();i++){
						if(i != postion ){
							SEff_List_Favorite.get(i).setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						}
					}
					SEffAdapterSF[1].notifyDataSetChanged();
					mListViewSF[1].setSelectionFromTop(postion, 0);
					//DB
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_list_is_open(item.isOpen());
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_1){
					System.out.println("BUG Click使用");

					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					ShowUseSeffDialog(seff_file);
				}else if(which == R.id.id_ly_seff_list_2){
					System.out.println("BUG Click分享6");


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					if(seff_file!=null){
						System.out.println("BUG DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_path());
						JsonRWUtil jsonRWSingleData=new JsonRWUtil();
						jsonRWSingleData.ShareLocalSoundEFFData(
								SEff_DownloadActivity.this,seff_file.Get_file_path());
					}
				}else if(which == R.id.id_ly_seff_list_3){
					System.out.println("BUG Click收藏");

					if(item.getFravorite().equals("0")){
						item.fravorite="1";
						seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love_press);
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_FavoriteBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getFravorite().equals("1")){
						item.fravorite="0";
						seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love);
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					}


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_4){
					System.out.println("BUG Click喜欢:"+item.love);

					if(item.getLove().equals("0")){
						item.love="1";
						seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love_press);
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_LoveBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getLove().equals("1")){
						item.love="0";
						seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love);
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);

					}


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_5){
					System.out.println("BUG Click删除");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					//System.out.println("DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_name());
					//ShowDeleteSeffDialog(1,seff_file,postion);
					ShowDelDialog(seff_file,2,postion);
				}else if(which == R.id.id_ly_seff_list_6){
					System.out.println("BUG Click 详情");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					ShowDelDialog(seff_file,1,postion);
//					AlertDialog.Builder builder = new AlertDialog.Builder(SEff_DownloadActivity.this);
//					builder.setTitle(R.string.Details);
//					builder.setMessage(seff_file.Get_data_eff_briefing());
//
//					builder.setPositiveButton(R.string.Sure, new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int whichButton) {
//
//						}
//					});
//					builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int whichButton) {
//							//这里添加点击确定后的逻辑
//						}
//					});
//					builder.create().show();
				}
			}
		});

		//2
		SEffAdapterSF[2].setOnSeffFileAdpterOnItemClick(new setSeffFileAdpterOnItemClick() {

			@Override
			public void onAdpterClick(int which, int postion,View v) {
				final BaseAdapter adapter = (BaseAdapter) mListViewSF[2].getAdapter();
				SEff_ListDat item = (SEff_ListDat) adapter.getItem(postion);
				String db_id=String.valueOf(item.getId());
//	            ViewGroup itemView = (ViewGroup) v.getParent().getParent();
//	            SEffViewHolder seffViewHolder = (SEffViewHolder) itemView.getTag();

				ViewGroup itemViewCh = (ViewGroup) v.getParent().getParent().getParent();
				SEffViewHolder seffViewHolderCh = (SEffViewHolder) itemViewCh.getTag();
				SEFF_File seff_file;
				// TODO Auto-generated method stub
				System.out.println("BUG Click db_id:"+db_id);
				if(which == R.id.seff_id_item_sel){
					System.out.println("BUG ClickItems前项选择");

					if(item.sel.equals(SEff_ListDat.LIST_DROP_WSEL)){
						item.sel=SEff_ListDat.LIST_DROP_SELED;
					}else if(item.sel.equals(SEff_ListDat.LIST_DROP_SELED)){
						item.sel=SEff_ListDat.LIST_DROP_WSEL;
					}
					SEff_List_Love.get(postion).setSel(item.sel);
					SEffAdapterSF[2].notifyDataSetChanged();
					FlashItemSel();
					//DB
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_list_sel(item.getSel());
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_item_seff){
					if(bool_MultiselectMode){
						if(item.sel.equals(SEff_ListDat.LIST_DROP_WSEL)){
							item.sel=SEff_ListDat.LIST_DROP_SELED;
						}else if(item.sel.equals(SEff_ListDat.LIST_DROP_SELED)){
							item.sel=SEff_ListDat.LIST_DROP_WSEL;
						}
						SEff_List_Love.get(postion).setSel(item.sel);
						SEffAdapterSF[2].notifyDataSetChanged();
						FlashItemSel();
						//DB
						seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
						seff_file.Set_list_sel(item.getSel());
						DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
					}else{
						System.out.println("BUG ClickItems layout");
						seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
						ShowUseSeffDialog(seff_file);
					}
				}else if(which == R.id.id_iv_favorite){
					System.out.println("BUG ClickItems收藏");

					item.fravorite="0";
					seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_favorite);
					seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_iv_love){
					System.out.println("BUG ClickItems喜欢");

					item.love="0";
					seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love);
					seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.seff_id_switcher_btn){
					System.out.println("BUG Click下拉B");

					//boolean showa=false;
					if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_OPEN)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						//	showa=false;
					}else if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_CLOSE)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_OPEN);
						//	showa=true;
					}


					SEff_List_LoveOld.get(postion).setOpen(item.isOpen());


//		            int defaultHeight = getResources().getDimensionPixelSize(R.dimen.seff_item_height2);
//		            itemView.startAnimation(new ExpandAnimation(seffViewHolder.get_targetPanel(), showa,defaultHeight));
//		            rotateSwitcherIcon(v, item);

					for(int i=0;i<SEff_List_Love.size();i++){
						if(i != postion ){
							SEff_List_Love.get(i).setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						}
					}
					SEffAdapterSF[2].notifyDataSetChanged();
					mListViewSF[2].setSelectionFromTop(postion, 0);
					//DB
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_list_is_open(item.isOpen());
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_1){
					System.out.println("BUG Click使用");

					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					ShowUseSeffDialog(seff_file);
				}else if(which == R.id.id_ly_seff_list_2){
					System.out.println("BUG Click分享5");


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					if(seff_file!=null){
						System.out.println("BUG DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_path());
						JsonRWUtil jsonRWSingleData=new JsonRWUtil();
						jsonRWSingleData.ShareLocalSoundEFFData(
								SEff_DownloadActivity.this,seff_file.Get_file_path());
					}
				}else if(which == R.id.id_ly_seff_list_3){
					System.out.println("BUG Click收藏");

					if(item.getFravorite().equals("0")){
						item.fravorite="1";
						seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love_press);
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_FavoriteBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getFravorite().equals("1")){
						item.fravorite="0";
						seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_favorite);
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					}


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_4){
					System.out.println("BUG Click喜欢:"+item.love);

					if(item.getLove().equals("0")){
						item.love="1";
						seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love_press);
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_LoveBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getLove().equals("1")){
						item.love="0";
						seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love);
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);

					}


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_5){
					System.out.println("BUG Click删除");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					//System.out.println("DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_name());

					ShowDelDialog(seff_file,2,postion);
					//ShowDeleteSeffDialog(1,seff_file,postion);
				}else if(which == R.id.id_ly_seff_list_6){
					System.out.println("BUG Click 详情");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);

					AlertDialog.Builder builder = new AlertDialog.Builder(SEff_DownloadActivity.this);
					builder.setTitle(R.string.SSM_Detial);
					builder.setMessage(seff_file.Get_data_eff_briefing());

					builder.setPositiveButton(R.string.Sure, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {

						}
					});
					builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							//这里添加点击确定后的逻辑
						}
					});
					builder.create().show();
				}

			}
		});




		//3
		SEffAdapterSF[2].setOnSeffFileAdpterOnItemClick(new setSeffFileAdpterOnItemClick() {

			@Override
			public void onAdpterClick(int which, int postion,View v) {
				final BaseAdapter adapter = (BaseAdapter) mListViewSF[2].getAdapter();
				SEff_ListDat item = (SEff_ListDat) adapter.getItem(postion);
				String db_id=String.valueOf(item.getId());
				//ViewGroup itemView = (ViewGroup) v.getParent().getParent();
				//SEffViewHolder seffViewHolder = (SEffViewHolder) itemView.getTag();

				ViewGroup itemViewCh = (ViewGroup) v.getParent().getParent().getParent();
				SEffViewHolder seffViewHolderCh = (SEffViewHolder) itemViewCh.getTag();
				SEFF_File seff_file;
				// TODO Auto-generated method stub
				System.out.println("BUG Click db_id:"+db_id);
				if(which == R.id.seff_id_item_sel){
					System.out.println("BUG ClickItems前项选择");

					if(item.sel.equals(SEff_ListDat.LIST_DROP_WSEL)){
						item.sel=SEff_ListDat.LIST_DROP_SELED;
					}else if(item.sel.equals(SEff_ListDat.LIST_DROP_SELED)){
						item.sel=SEff_ListDat.LIST_DROP_WSEL;
					}

					SEff_List_Recently.get(postion).setSel(item.sel);
					SEffAdapterSF[2].notifyDataSetChanged();
					FlashItemSel();
					//DB
					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					seff_file.Set_list_sel(item.getSel());
					DataStruct.dbSEfFile_Recently_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_item_seff){
					if(bool_MultiselectMode){
						if(item.sel.equals(SEff_ListDat.LIST_DROP_WSEL)){
							item.sel=SEff_ListDat.LIST_DROP_SELED;
						}else if(item.sel.equals(SEff_ListDat.LIST_DROP_SELED)){
							item.sel=SEff_ListDat.LIST_DROP_WSEL;
						}

						SEff_List_Recently.get(postion).setSel(item.sel);
						SEffAdapterSF[2].notifyDataSetChanged();
						FlashItemSel();
						//DB
						seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
						seff_file.Set_list_sel(item.getSel());
						DataStruct.dbSEfFile_Recently_Table.update("id", db_id, seff_file);
					}else{
						System.out.println("BUG ClickItems layout");
						seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
						ShowUseSeffDialog(seff_file);
					}
				}else if(which == R.id.id_iv_favorite){
					System.out.println("BUG ClickItems收藏");

					item.fravorite="0";
					seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love);
					seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Recently_Table.update("id", db_id, seff_file);

					//同时增加到前三个列表中
					SEFF_File seff_filef = DataStruct.dbSEfFile_Table.find("file_name", seff_file.Get_file_name());
					if(seff_filef!=null){
						seff_file.Set_file_favorite(item.fravorite);
						DataStruct.dbSEfFile_Table.update("file_name", item.fravorite, seff_filef);
					}else{
						if(fileIsExists(seff_file.Get_file_path())){
							DataStruct.dbSEfFile_Table.insert(seff_file);
						}
					}
				}else if(which == R.id.id_iv_love){
					System.out.println("BUG ClickItems喜欢");

					item.love="0";
					seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love);
					seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Recently_Table.update("id", db_id, seff_file);

					//同时增加到前三个列表中
					SEFF_File seff_filel = DataStruct.dbSEfFile_Table.find("file_name", seff_file.Get_file_name());
					if(seff_filel!=null){
						seff_file.Set_file_favorite(item.love);
						DataStruct.dbSEfFile_Table.update("file_love", item.love, seff_filel);
					}else{
						if(fileIsExists(seff_file.Get_file_path())){
							DataStruct.dbSEfFile_Table.insert(seff_file);
						}
					}
				}else if(which == R.id.seff_id_switcher_btn){
					System.out.println("BUG Click下拉B");

					//boolean showa=false;
					if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_OPEN)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						//	showa=false;
					}else if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_CLOSE)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_OPEN);
						//	showa=true;
					}

//		            int defaultHeight = getResources().getDimensionPixelSize(R.dimen.seff_item_height2);
//		            itemView.startAnimation(new ExpandAnimation(seffViewHolder.get_targetPanel(), showa ,defaultHeight));
//		            rotateSwitcherIcon(v, item);

					for(int i=0;i<SEff_List_Recently.size();i++){
						if(i != postion ){
							SEff_List_Recently.get(i).setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						}
					}
					SEffAdapterSF[2].notifyDataSetChanged();
					mListViewSF[2].setSelectionFromTop(postion, 0);
					//DB
					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					seff_file.Set_list_is_open(item.isOpen());
					DataStruct.dbSEfFile_Recently_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_1){
					System.out.println("BUG Click使用");

					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					ShowUseSeffDialog(seff_file);
				}else if(which == R.id.id_ly_seff_list_2){
					System.out.println("BUG Click分享7");


					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					if(seff_file!=null){
						//System.out.println("DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_path());
						JsonRWUtil jsonRWSingleData=new JsonRWUtil();
						jsonRWSingleData.ShareLocalSoundEFFData(
								SEff_DownloadActivity.this,seff_file.Get_file_path());
					}
				}else if(which == R.id.id_ly_seff_list_3){
					System.out.println("BUG Click收藏");

					if(item.getFravorite().equals("0")){
						item.fravorite="1";
						seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love_press);
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_FavoriteBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getFravorite().equals("1")){
						item.fravorite="0";
						seffViewHolderCh.get_B_Favorite().setBackgroundResource(R.drawable.chs_seff_love);
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					}


					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);

					//同时增加到前三个列表中
					SEFF_File seff_fileff = DataStruct.dbSEfFile_Table.find("file_name", seff_file.Get_file_name());
					if(seff_fileff!=null){
						seff_file.Set_file_favorite(item.fravorite);
						DataStruct.dbSEfFile_Table.update("file_name", item.fravorite, seff_fileff);
					}else{
						if(fileIsExists(seff_file.Get_file_path())){
							DataStruct.dbSEfFile_Table.insert(seff_file);
						}
					}
				}else if(which == R.id.id_ly_seff_list_4){
					System.out.println("BUG Click喜欢:"+item.love);

					if(item.getLove().equals("0")){
						item.love="1";
						seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love_press);
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_LoveBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getLove().equals("1")){
						item.love="0";
						seffViewHolderCh.get_B_Love().setBackgroundResource(R.drawable.chs_seff_love);
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);

					}


					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);

					//同时增加到前三个列表中
					SEFF_File seff_filell = DataStruct.dbSEfFile_Table.find("file_name", seff_file.Get_file_name());
					if(seff_filell!=null){
						seff_file.Set_file_favorite(item.love);
						DataStruct.dbSEfFile_Table.update("file_love", item.love, seff_filell);
					}else{
						if(fileIsExists(seff_file.Get_file_path())){
							DataStruct.dbSEfFile_Table.insert(seff_file);
						}
					}
				}else if(which == R.id.id_ly_seff_list_5){
					System.out.println("BUG Click删除");
					seff_file = DataStruct.dbSEfFile_Recently_Table.find("id", db_id);
					//System.out.println("DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_name());
					ShowDelDialog(seff_file,2,postion);
					//ShowDeleteSeffDialog(1,seff_file,postion);
				}else if(which == R.id.id_ly_seff_list_6){

					System.out.println("BUG Click 详情");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					ShowDelDialog(seff_file,1,postion);

				}
			}



		});









	}

}
