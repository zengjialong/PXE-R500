package com.chs.mt.pxe_r500.main;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.adapter.ListSEffItemAdapter;
import com.chs.mt.pxe_r500.bean.JsonDataSt;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.filemanger.common.FileUtil;
import com.chs.mt.pxe_r500.bean.SEFF_File;
import com.chs.mt.pxe_r500.bean.SEff_ListDat;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.operation.JsonRWUtil;
import com.chs.mt.pxe_r500.tools.ExpandAnimation;
import com.chs.mt.pxe_r500.tools.MVP_ViewPage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.pxe_r500.adapter.SEffViewHolder;
import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.util.ToastUtil;

@SuppressLint({ "HandlerLeak", "NewApi" }) public class SEff_DownloadSearchActivity extends Activity{
	private Context mContext;	
	public static final int WHAT_IS_UPDATE_LISTVIEW=0x00;    // 刷新EQ通道名字
	private Handler mHandler ;
    //标题
    private final static int Title_NUM = 4;
  	private LinearLayout lLY_Back;
  	private TextView TV_PageName;
  	private EditText ET_Search;
  	private Button B_Search;

  	private String searchString ="" ;
  	private String searchKeyword ="" ;
    //0:处于名称排序界面，1：处于音效作者排序界面，2：处于时间排序界面，3：模糊搜索界面
  	//private int SearchBarUI=0;
  	
 //   private List<SEff_ListDat> SEff_List = new ArrayList<SEff_ListDat>();
    private List<SEFF_File> seffFile_list = new ArrayList<SEFF_File>();
    private SEFF_File seff_file = new SEFF_File();

    //界面
  	//PageNUM
  	private final static int PAGE_NUM = 1;
  	private MVP_ViewPage VP_CHS_Pager;//
  	private View[] LY_VIEW = new View[PAGE_NUM];
  	private ListView[]  mListViewSF = new ListView[PAGE_NUM];
  	private ListSEffItemAdapter[]  SEffAdapterSF = new ListSEffItemAdapter[PAGE_NUM];
  	private List<SEff_ListDat> SEff_List_Name = new ArrayList<SEff_ListDat>();

    private AlertDialogFragment alertDialogFragment=null;
    private LoadingDialogFragment mLoadingDialogFragment=null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chs_layout_sefflistview_download_search);
        mContext=this;
        InitViewPager();
        initHeadBar();
        initDataListView();
        initSEffAdapterClickListen();       
        mHandler = new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
     			super.handleMessage(msg);
     			if (msg.what == WHAT_IS_UPDATE_LISTVIEW) {

    			} 
    		}
        };
        
        
    }
    private void initDataListView(){
    	searchString = String.valueOf(ET_Search.getText());
		seffFile_list.clear();
		if(searchString.length()>0){
			seffFile_list = DataStruct.dbSEfFile_Table.findByKeyword("file_name", searchString);	
			//seffFile_list = DataStruct.dbSEfFile_Table.findBy(searchString);
			//seffFile_list = DataStruct.dbSEfFile_Table.OrderBy("file_name", false);
		}else{
			//seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
		}
        //if(seffFile_list.size()>0){
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
				seff_file.Get_list_sel(),
				seff_file.Get_list_is_open(),
				seff_file.Get_file_type()
				));
    	}
    	
    	SEffAdapterSF[0] = new ListSEffItemAdapter(mContext, SEff_List_Name);
        mListViewSF[0].setAdapter(SEffAdapterSF[0]);

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
    	lLY_Back = (LinearLayout) findViewById(R.id.id_llyout_net_back);
		TV_PageName = (TextView) findViewById(R.id.di_tv_net_pagedown_name);
		TV_PageName.setText(getResources().getString(R.string.Menu_Open));
		
		ET_Search = (EditText) findViewById(R.id.id_et_download_search_edit);
	  	B_Search = (Button) findViewById(R.id.id_b_search);	  	
        
		//初始UI
	  	searchKeyword = "file_name";
		//Back
		lLY_Back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		
//		B_Search.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				
//				Intent intent = new Intent();
//				intent.setClass(mContext, SEff_DownloadSearchActivity.class);
//				mContext.startActivity(intent);
//			}
//		});
		ET_Search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				searchString = String.valueOf(ET_Search.getText());
				seffFile_list.clear();
				if(searchString.length()>0){
					seffFile_list = DataStruct.dbSEfFile_Table.findByKeyword("file_name", searchString);	
					//seffFile_list = DataStruct.dbSEfFile_Table.findByKeyword(searchKeyword, searchString);	
					//seffFile_list = DataStruct.dbSEfFile_Table.findBy(searchString);
				}else{
					//seffFile_list = DataStruct.dbSEfFile_Table.getTableList();
				}
		        //if(seffFile_list.size()>0){
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
        				seff_file.Get_list_sel(),
        				seff_file.Get_list_is_open(),
        				seff_file.Get_file_type()
        			));
	        	}	        	
	        	SEffAdapterSF[0] = new ListSEffItemAdapter(mContext, SEff_List_Name);
	            mListViewSF[0].setAdapter(SEffAdapterSF[0]);
	            initSEffAdapterClickListen();				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
    }

    /**
     * 旋转切换图标
     * @param v
     * @param item
     */
    private void rotateSwitcherIcon(View v, SEff_ListDat item){
        float degree = item.isOpen().equals(SEff_ListDat.LIST_DROP_DOWN_OPEN) ? 180 : 0;
//         ViewPropertyAnimator.animate(v)
//        .setDuration(100)
//        .setInterpolator(new AccelerateDecelerateInterpolator())
//        .rotation(degree)
//        .start();
    }
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

         
        LV_CHS_list.add(LY_VIEW[0]);   


        CHS_PageAdapter myPageAdapter = new CHS_PageAdapter(LV_CHS_list);
        VP_CHS_Pager.setAdapter(myPageAdapter);        
        VP_CHS_Pager.setOnPageChangeListener(new MyOnPageChangeListener());
        //VP_CHS_Pager.setOffscreenPageLimit(5);
        mListViewSF[0] = (ListView) LY_VIEW[0].findViewById(R.id.seff_download_listview_all);

  
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
		public void onPageSelected(int arg0) {

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
            	if(seffFile_list.get(i).Get_file_name().equals(seff_file.Get_file_name())){
            		seffFile_list.remove(i);
            	}
            }
            
            DataStruct.dbSEfFile_Recently_Table.ResetTable();
            //把删除相关数据的表复写到数据表中
			if(seffFile_list.size()>0){				
				for(int i=0;i < seffFile_list.size();i++){
					DataStruct.dbSEfFile_Recently_Table.insert(seff_file);
				}
			}
    	}
	}
    private void initSEffAdapterClickListen(){
    	SEffAdapterSF[0].setOnSeffFileAdpterOnItemClick(new ListSEffItemAdapter.setSeffFileAdpterOnItemClick() {
			
			@Override
			public void onAdpterClick(int which, int postion,View v) {
				final BaseAdapter adapter = (BaseAdapter) mListViewSF[0].getAdapter();
	            SEff_ListDat item = (SEff_ListDat) adapter.getItem(postion);
	            String db_id=String.valueOf(item.getId());
	            ViewGroup itemView = (ViewGroup) v.getParent().getParent();
	            SEffViewHolder seffViewHolder = (SEffViewHolder) itemView.getTag();
	            
	            ViewGroup itemViewCh = (ViewGroup) v.getParent().getParent().getParent();
				SEffViewHolder seffViewHolderCh = (SEffViewHolder) itemViewCh.getTag();
	            SEFF_File seff_file = null;
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

					//DB
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_list_sel(item.getSel());
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_item_seff){
					System.out.println("BUG ClickItems layout");
                    seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
                    ShowUseSeffDialog(seff_file);
				}else if(which == R.id.id_iv_favorite){
					System.out.println("BUG ClickItems收藏");

					item.fravorite="0";
					seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_iv_love){
					System.out.println("BUG ClickItems喜欢");

					item.love="0";
					seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.seff_id_switcher_btn){
					System.out.println("BUG Click下拉B");

					boolean showa=false;
					if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_OPEN)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						showa=false;
					}else if(item.isOpen.equals(SEff_ListDat.LIST_DROP_DOWN_CLOSE)){
						item.setOpen(SEff_ListDat.LIST_DROP_DOWN_OPEN);
						showa=true;
					}

					int defaultHeight = getResources().getDimensionPixelSize(R.dimen.seff_item_height2);
					itemView.startAnimation(new ExpandAnimation(seffViewHolder.get_targetPanel(), showa,defaultHeight));
					rotateSwitcherIcon(v, item);

					for(int i=0;i<SEff_List_Name.size();i++){
						if(i != postion ){
							SEff_List_Name.get(i).setOpen(SEff_ListDat.LIST_DROP_DOWN_CLOSE);
						}
					}
					SEffAdapterSF[0].notifyDataSetChanged();

					//DB
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_list_is_open(item.isOpen());
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_1){
					System.out.println("BUG Click使用");

                    seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
                    ShowUseSeffDialog(seff_file);
				}else if(which == R.id.id_ly_seff_list_2){
					System.out.println("BUG Click分享");


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					if(seff_file!=null){
						//System.out.println("DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_path());
						JsonRWUtil jsonRWSingleData=new JsonRWUtil();
						jsonRWSingleData.ShareLocalSoundEFFData(
								SEff_DownloadSearchActivity.this,seff_file.Get_file_path());
					}
				}else if(which == R.id.id_ly_seff_list_3){
					System.out.println("BUG Click收藏");

					if(item.getFravorite().equals("0")){
						item.fravorite="1";
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_FavoriteBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getFravorite().equals("1")){
						item.fravorite="0";
						seffViewHolderCh.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
					}


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_favorite(item.fravorite);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_4){
					System.out.println("BUG Click喜欢:"+item.love);

					if(item.getLove().equals("0")){
						item.love="1";
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.VISIBLE);
						seffViewHolderCh.get_B_LoveBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
					}else if(item.getLove().equals("1")){
						item.love="0";
						seffViewHolderCh.get_B_LoveBar().setVisibility(android.view.View.GONE);

					}


					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					seff_file.Set_file_love(item.love);
					DataStruct.dbSEfFile_Table.update("id", db_id, seff_file);
				}else if(which == R.id.id_ly_seff_list_5){
					System.out.println("BUG Click删除");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);
					//System.out.println("DBTEST-SSFSS-"+"Get_file_path=" + seff_file.Get_file_name());
					File dir = new File(seff_file.Get_file_path());
					if (dir.isFile()) {
						dir.delete();
					} else {
						FileUtil.deleteDir(dir);
					}
					if (dir == null || !dir.exists()) {
						Toast.makeText(SEff_DownloadSearchActivity.this, R.string.delSuccess,
								Toast.LENGTH_SHORT).show();
						DataStruct.dbSEfFile_Table.delete("id", String.valueOf(SEff_List_Name.get(postion).getId()));
						SEff_List_Name.remove(postion);

						//SEffAdapterSF[0].notifyDataSetChanged();
						SEffAdapterSF[0] = new ListSEffItemAdapter(SEff_DownloadSearchActivity.this, SEff_List_Name);
						mListViewSF[0].setAdapter(SEffAdapterSF[0]);
					} else {
						Toast.makeText(SEff_DownloadSearchActivity.this, R.string.delFailed,
								Toast.LENGTH_SHORT).show();
					}
				}else if(which == R.id.id_ly_seff_list_6){
					System.out.println("BUG Click 详情");
					seff_file = DataStruct.dbSEfFile_Table.find("id", db_id);

					AlertDialog.Builder builder = new AlertDialog.Builder(SEff_DownloadSearchActivity.this);
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
                    if(!DataStruct.U0SynDataSucessFlg){
                        ToastUtil.showShortToast(mContext,getResources().getString(R.string.off_line_mode));
                        return;
                    }

                    final String filePath=seff_file.Get_file_path();

                    int fileType=DataStruct.jsonRWOpt.getSEFFFileType(mContext, filePath);
                    ////ToastMsg("GetFileType="+String.valueOf(fileType));
					//MacCfg.click_Mac=1;


                    if(fileType == 1){
                        DataOptUtil.UpdateForJsonSingleData(filePath,mContext);
                    }else if(fileType == 2){
                        DataOptUtil.loadMacEffJsonData(filePath,mContext);
                    }else{
                        ToastUtil.showShortToast(mContext,getResources().getString(R.string.LoadSEff_Fail));
                    }
                    insertRecentlyTable(seff_file);
                    showLoadingDialog();
                }else{
					//MacCfg.click_Mac=0;
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
}
