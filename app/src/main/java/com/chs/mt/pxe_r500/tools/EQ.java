package com.chs.mt.pxe_r500.tools;
import com.chs.mt.pxe_r500.R;

import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.DataStruct_EFFect;
import com.chs.mt.pxe_r500.datastruct.DataStruct_Output;
import com.chs.mt.pxe_r500.datastruct.Define;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


@SuppressLint("ClickableViewAccessibility") public class EQ extends View {
	private boolean DEBUG = true;
	private String TAG = "FUCK-EQ#";
    //画布相关
    private Paint P_FrameOutSide; //外框
    private Paint P_FrameInside;  //内框
    private Paint P_FrameMidLine; //中线
    private Paint P_FrameText; //中线
    private Paint P_MidText; //中线上的编号
    private Paint P_EQLine; //曲线
    private float marginLeft=30;   //框离左边距
    private float marginTop=10;    //框离顶边距
    private float marginRight=10;  //框离右边距
    private float marginBottom=10; //框离底边距
    //整个框的四个角
    private float Left=30;  
    private float Top=10;    
    private float Right=10;  
    private float Bottom=10; 
    //框的中心线坐标
    //private float MidX=10;  
    private float MidY=10;
    //框的长宽坐标
    private float FrameWidth=10;  
    private float FrameHeight=10;
    
    private RectF RectF = null;
    private Paint RectFPaint = null;
    
    private float frameOutsideWith=4;//外框线宽
    private float frameInsideWith=2; //内框线宽
    private float frameTextSize=2; //内框线宽
    private float EQTextSize=2; //EQ字体大小
    private float EQLineWith=2; //曲线宽
    //X轴的划分
    float x3Base;
	float x3Tap;
			
	float xTap1;
	float xTap2;
	float xTap3;
	float xTap4;
	float xTap5;
	float xTap6;
	float xTap7;
	float xTap8;
	float xTap9;

	//增益显示 
//	private String  TV_DB1="+12";//"+20"
//	private String  TV_DB2="+9" ;//""
//	private String  TV_DB3="+6" ;//"+10"
//	private String  TV_DB4="+3" ;//""				
//	private String  TV_DB5="0dB";//
//	private String  TV_DB6="-3" ;//""	
//	private String  TV_DB7="-6" ;//"-10"
//	private String  TV_DB8="-9" ;//""
//	private String  TV_DB9="-12";//"-20"	
	private String  TV_DB1="+20";//"+20"
	private String  TV_DB2="" ;//""
	private String  TV_DB3="+10" ;//"+10"
	private String  TV_DB4="" ;//""				
	private String  TV_DB5="0dB";//
	private String  TV_DB6="" ;//""	
	private String  TV_DB7="-10" ;//"-10"
	private String  TV_DB8="" ;//""
	private String  TV_DB9="-20";//"-20"
	//频率显示
	private String  TV_Freq00="";
	private String[] TV_Freq0  = new String[9];
	private String[] TV_Freq1  = new String[9];
	private String[] TV_Freq2  = new String[9];

    //颜色
    private int colorFrameOutside;
    private int colorFrameInside;
    private int colorMidLine;
    private int colorFrameText;
    private int colorMidLineText;
    @SuppressWarnings("unused")
	private int colorEQNum;
    private int colorEQLine;
	//窗口大小
	private int Wind_Height=0;
	private int Wind_Width=0;	
	@SuppressWarnings("unused")
	private Context mContext = null;
	public  DataStruct_Output EQ_Filter = new DataStruct_Output();  
	public  DataStruct_Output ReadEQ = new DataStruct_Output();
	private static int MAXEQ = 31;
	
	//画曲线相关
	private  static final double  FS      = 96000;
	private  static final double  pi      = 3.1415926;
//	private  static final int  Oct_6dB    = 0;
//	private  static final int  Oct_12dB   = 1;
//	private  static final int  Oct_18dB   = 2;
//	private  static final int  Oct_24dB   = 3;
//	private  static final int  Oct_30dB   = 4;
//	private  static final int  Oct_36dB   = 5;
//	private  static final int  Oct_42dB   = 6;
//	private  static final int  Oct_48dB   = 7;
//	private  static final int  HL_OFF     = 8;                   //   高低通类型等于7时 高低通 OFF
	private  static final int  Oct_6dB    = 0;
	private  static final int  Oct_12dB   = 1;
	private  static final int  Oct_18dB   = 2;
	private  static final int  Oct_24dB   = 3;
	private  static final int  Oct_30dB   = 4;
	private  static final int  Oct_36dB   = 5;
	private  static final int  Oct_42dB   = 6;
	private  static final int  Oct_48dB   = 7;
	private  static final int  HL_OFF     = 8;
	
	private  static final int  L_R        = 0;
	private  static final int  Bessel     = 1; 
	private  static final int  ButtWorth  = 2;
	
	//保存滤波器图形每点所对应的dB值
	double m_nPoint[] = new double[241];
	double m_nPointHP[] = new double[241];//高通各点增益
	double m_nPointLP[] = new double[241];//低通各点增益
	double m_nPointPEQ[][] = new double[MAXEQ][];//31点增益
	//保存31点EQ的频率在241点EQ的索引值
	//int EQPoint[] = new int[DataStruct.OUT_CH_EQ_MAX];
	int EQPointLR[][] = new int[MAXEQ][];
	
	//保存滤波器图形每点所对应的dB值的Y坐标
	double m_nPointY[] = new double[241];
	double m_nPointX[] = new double[241];
	
	double FreqNumPointX[] = new double[31];
	double FreqNumPointY[] = new double[31];


	//保存手滑过的坐标
	double drawPointY[] = new double[31];
	double drawPointX[] = new double[31];
	//private boolean bool_DrawUpdate=false;//false:根据数据更新曲线    true:根据手势更新曲线	
	//隐藏背景，只显示曲线
	private boolean bool_HideBg=false;
	private boolean bool_HideLR_Side=false;
	
	private int line_start=0;
	private int line_end=240;
	float stepH=0;
	float stepW=0;
	float stepY=0;
	float stepX=0;
	/////////////////////////    触摸事件      /////////////////////////////
	
	private boolean CanTouch = true;
	/*事件监听 */  
    private OnEQDrawChangeListener mOnEQDrawChangeListener=null; 
	
	
	///////////////////////////////////////////////////////
    public EQ(Context context) {      	
        super(context);  
        mContext = context;
        init(null, 0);  
    }  
  
    public EQ(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mContext = context;
        init(attrs, 0);  
    }  
  
    public EQ(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        mContext = context;
        init(attrs, defStyle);  
    }  
  
    @SuppressWarnings("unused")
	private void init(AttributeSet attrs, int defStyle) {  
        final TypedArray a = getContext().obtainStyledAttributes(attrs,  
                R.styleable.eq, defStyle, 0);  
  
        initAttributes(a); 
        //外框
		P_FrameOutSide = new Paint(Paint.ANTI_ALIAS_FLAG); 
		P_FrameOutSide.setColor(colorFrameOutside);  
		P_FrameOutSide.setStrokeWidth(frameOutsideWith);
		//内框
		P_FrameInside = new Paint(Paint.ANTI_ALIAS_FLAG); 
		P_FrameInside.setColor(colorFrameInside);  
		P_FrameInside.setStrokeWidth(frameInsideWith); 
		//框路线
		P_FrameMidLine = new Paint(Paint.ANTI_ALIAS_FLAG); 
		P_FrameMidLine.setColor(colorMidLine);  
		P_FrameMidLine.setStrokeWidth(frameInsideWith); 
		//增益和频率
		P_FrameText = new Paint(Paint.ANTI_ALIAS_FLAG); 
		P_FrameText.setColor(colorFrameText);  
		P_FrameText.setStrokeWidth(frameInsideWith); 
		//P_FrameText.setTextAlign(Align.CENTER);
		P_FrameText.setTextSize(frameTextSize);
		//增益和频率
		P_MidText = new Paint(Paint.ANTI_ALIAS_FLAG); 
		P_MidText.setColor(colorMidLineText);  
		P_MidText.setStrokeWidth(frameInsideWith); 
		//P_MidText.setTextAlign(Align.CENTER);
		P_MidText.setTextSize(EQTextSize);
		
		//EQ曲线
		P_EQLine = new Paint(Paint.ANTI_ALIAS_FLAG); 
		P_EQLine.setColor(colorEQLine);  
		P_EQLine.setStrokeWidth(EQLineWith); 
		
		TV_Freq0[0]="30";//30
		TV_Freq0[1]="";//40
		TV_Freq0[2]="";//50
		TV_Freq0[3]="";
		TV_Freq0[4]="";//70
		TV_Freq0[5]="";
		TV_Freq0[6]="";
		TV_Freq0[7]="100";//100
		TV_Freq0[8]="200";//200
		
		TV_Freq1[0]="";//300
		TV_Freq1[1]="";
		TV_Freq1[2]="500";//500
		TV_Freq1[3]="";
		TV_Freq1[4]="";//700
		TV_Freq1[5]="";
		TV_Freq1[6]="";
		TV_Freq1[7]="1k";//1k
		TV_Freq1[8]="2k";//2k

		TV_Freq2[0]="";//3k
		TV_Freq2[1]="";//4k
		TV_Freq2[2]="5k";//5k
		TV_Freq2[3]="";
		TV_Freq2[4]="";//7k
		TV_Freq2[5]="";
		TV_Freq2[6]="";
		TV_Freq2[7]="10k";//10k
		TV_Freq2[8]="20k";
		if(DataStruct.CurMacMode.EQ.EQ_Gain_MAX==400){
			TV_DB1="+20";//"+20"
			TV_DB2="" ;//""
			TV_DB3="+10" ;//"+10"
			TV_DB4="" ;//""				
			TV_DB5="0dB";//
			TV_DB6="" ;//""	
			TV_DB7="-10" ;//"-10"
			TV_DB8="" ;//""
			TV_DB9="-20";//"-20"
		}else if(DataStruct.CurMacMode.EQ.EQ_Gain_MAX==240){
			TV_DB1="+12";//"+20"
			TV_DB2="+9" ;//""
			TV_DB3="+6" ;//"+10"
			TV_DB4="+3" ;//""
			TV_DB5="0dB";//
			TV_DB6="-3" ;//""
			TV_DB7="-6" ;//"-10"
			TV_DB8="-9" ;//""
			TV_DB9="-12";//"-20"	
		}
		//31点增益
		for(int i=0;i<MAXEQ;i++){
			m_nPointPEQ[i]=new double[241];
			EQPointLR[i]=new int[2];
		}
	
        a.recycle();  
        
        RectF = new RectF((Left),Top,Right,Bottom);
        RectFPaint = new Paint();
        RectFPaint.setColor(colorFrameOutside);  
        RectFPaint.setStrokeWidth(frameOutsideWith); 
        RectFPaint.setStyle(Style.STROKE);
        
    }

	void initAttributes(TypedArray a){
	    frameOutsideWith = a.getDimension(R.styleable.eq_frameOutsideWith, 4);
	    frameInsideWith = a.getDimension(R.styleable.eq_frameInsideWith, 2);
	    frameTextSize = a.getDimension(R.styleable.eq_frameTextSize, 10);
	    EQTextSize = a.getDimension(R.styleable.eq_EQTextSize, 10);
	    EQLineWith = a.getDimension(R.styleable.eq_EQLineWith, 2);
	    marginLeft = a.getDimension(R.styleable.eq_marginLeft, 30);   //框离左边距
	    marginTop = a.getDimension(R.styleable.eq_marginTop, 10);    //框离顶边距
	    marginRight = a.getDimension(R.styleable.eq_marginRight, 10);  //框离右边距
	    marginBottom = a.getDimension(R.styleable.eq_marginBottom, 10); //框离底边距
	    //颜色
	    colorFrameOutside = a.getColor(R.styleable.eq_colorFrameOutside, Color.BLACK);
	    colorFrameInside = a.getColor(R.styleable.eq_colorFrameInside, Color.BLACK);
	    colorMidLine = a.getColor(R.styleable.eq_colorMidLine, Color.BLACK);
	    colorFrameText = a.getColor(R.styleable.eq_colorFrameText, Color.BLACK);
	    colorMidLineText = a.getColor(R.styleable.eq_colorMidLineText, Color.BLACK);
	    colorEQNum = a.getColor(R.styleable.eq_colorEQNum, Color.BLACK);
	    colorEQLine = a.getColor(R.styleable.eq_colorEQLine, Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(!bool_HideBg){
			DrawFrame(canvas);//画框
			DrawText(canvas); //标出字体 
		}
		float tl=(Wind_Height-marginTop-marginBottom)/8;
		canvas.drawLine(Left, Top+tl*4, Right, Top+tl*4, P_FrameMidLine);	
		DrawLine(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Wind_Height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);  
        Wind_Width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);  
        setMeasuredDimension(Wind_Width, Wind_Height);
        //if(DEBUG) System.out.println("EQ Wind_Width:"+Wind_Width);
        //if(DEBUG) System.out.println("EQ Wind_Height:"+Wind_Height);
        
        //整个框的四个角
        
  		Left=marginLeft;  
  	    Top=marginTop;    
  	    Right=Wind_Width-marginRight;  
  	    Bottom=Wind_Height-marginBottom; 
  	    
  	    //MidX=marginLeft+(Wind_Width-marginLeft-marginRight)/2;
  	    MidY=marginTop+(Wind_Height-marginTop-marginBottom)/2;
  	    
  	    FrameWidth=Wind_Width-marginLeft-marginRight;
  	    FrameHeight=Wind_Height-marginTop-marginBottom;
  	    //X轴的划分
  	    //分3大分，再分9小分
  	    x3Base=marginLeft;
  		x3Tap=(Wind_Width-marginLeft-marginRight)/3;
  				
  		xTap1=x3Tap*3/10*3/5;
  		xTap2=x3Tap*3/10*2/5;
  		xTap3=x3Tap*4/10*1/4;
  		xTap4=x3Tap*4/10*1/5;
  		xTap5=x3Tap*4/10*1/6;
  		xTap6=x3Tap*4/10*1/7;
  		xTap7=x3Tap*4/10*1/8;
  		xTap8=x3Tap*4/10*1/9;
  		xTap9=x3Tap*3/10;
  		
  		
  		//计算出各占坐标
  		stepH=FrameHeight / (DataStruct.CurMacMode.EQ.EQ_Gain_MAX/10);
  		stepW=Wind_Width / (float) (MAXEQ);
  		stepY=FrameHeight / (DataStruct.CurMacMode.EQ.EQ_Gain_MAX/10);
		stepX=FrameWidth / (line_end-line_start);
  			
  			
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
	}
	
	void DrawFrame(Canvas canvas) {
		//外框
		
		//canvas.drawRect(RectF, RectFPaint);
		canvas.drawLine(Left-frameInsideWith, Top, Right+frameInsideWith, Top, P_FrameOutSide);//上边
		canvas.drawLine(Left-frameInsideWith, Bottom, Right+frameInsideWith, Bottom, P_FrameOutSide);//下边
		canvas.drawLine(Left, Top, Left, Bottom, P_FrameOutSide);//左边
		canvas.drawLine(Right, Top, Right, Bottom, P_FrameOutSide);//右边
		//画横线
		/**/
		float tl=(Wind_Height-marginTop-marginBottom)/8;
		for(int i=1;i<=7;i++){
			if(i==4){
				canvas.drawLine(Left, Top+tl*i, Right, Top+tl*i, P_FrameMidLine);	
			}else{
				canvas.drawLine(Left, Top+tl*i, Right, Top+tl*i, P_FrameInside);
			}					
		}
		float x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,base;	
		//画竖线
		for(int i=0;i<=2;i++){
			base=x3Tap*i;
			x0=Left+base;
			x1=xTap1+x0;
			x2=xTap2+x1;
			x3=xTap3+x2;
			x4=xTap4+x3;
			x5=xTap5+x4;
			x6=xTap6+x5;
			x7=xTap7+x6;
			x8=xTap8+x7;
			x9=xTap9+x8;
			canvas.drawLine(x1, Top, x1, Bottom, P_FrameInside);
			canvas.drawLine(x2, Top, x2, Bottom, P_FrameInside);
			canvas.drawLine(x3, Top, x3, Bottom, P_FrameInside);
			canvas.drawLine(x4, Top, x4, Bottom, P_FrameInside);
			canvas.drawLine(x5, Top, x5, Bottom, P_FrameInside);
			canvas.drawLine(x6, Top, x6, Bottom, P_FrameInside);
			canvas.drawLine(x7, Top, x7, Bottom, P_FrameInside);
			canvas.drawLine(x8, Top, x8, Bottom, P_FrameInside);
			canvas.drawLine(x9, Top, x9, Bottom, P_FrameInside);
		}

	}
	
	void DrawText(Canvas canvas){
		float tl=(Wind_Height-marginTop-marginBottom)/8;
		int txml=8;
		canvas.drawText(TV_DB1, txml, marginTop+frameTextSize/2, P_FrameText);
		canvas.drawText(TV_DB2, txml, marginTop+frameTextSize/2+tl*1, P_FrameText);
		canvas.drawText(TV_DB3, txml, marginTop+frameTextSize/2+tl*2, P_FrameText);
		canvas.drawText(TV_DB4, txml, marginTop+frameTextSize/2+tl*3, P_FrameText);
		canvas.drawText(TV_DB5, txml, marginTop+frameTextSize/2+tl*4, P_FrameText);
		canvas.drawText(TV_DB6, txml, marginTop+frameTextSize/2+tl*5, P_FrameText);		
		canvas.drawText(TV_DB7, txml, marginTop+frameTextSize/2+tl*6, P_FrameText);
		canvas.drawText(TV_DB8, txml, marginTop+frameTextSize/2+tl*7, P_FrameText);
		canvas.drawText(TV_DB9, txml, marginTop+frameTextSize/2+tl*8, P_FrameText);
		
		tl=(Wind_Width-marginLeft-marginRight)/17;
		float txbb=Wind_Height-frameTextSize/2;
		float txbs=marginLeft-frameTextSize/2;
		canvas.drawText(TV_Freq00, txbs, txbb, P_FrameText);
		float x1,x2,x3,x4,x5,x6,x7,x8,x9;	
	
		for(int i=0;i<=2;i++){
			x3Base=x3Tap*i+marginLeft;
			x1=x3Base+xTap1;
			x2=xTap2+x1;
			x3=xTap3+x2;
			x4=xTap4+x3;
			x5=xTap5+x4;
			x6=xTap6+x5;
			x7=xTap7+x6;
			x8=xTap8+x7;
			x9=xTap9+x8;
			if(i==0){
				canvas.drawText(TV_Freq0[0], x1-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq0[1], x2-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq0[2], x3-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq0[3], x4-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq0[4], x5-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq0[5], x6-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq0[6], x7-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq0[7], x8-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq0[8], x9-frameTextSize, txbb, P_FrameText);				
			}else if(i==1){
				canvas.drawText(TV_Freq1[0], x1-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq1[1], x2-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq1[2], x3-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq1[3], x4-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq1[4], x5-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq1[5], x6-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq1[6], x7-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq1[7], x8-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq1[8], x9-frameTextSize/2, txbb, P_FrameText);		
			}else if(i==2){
				canvas.drawText(TV_Freq2[0], x1-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq2[1], x2-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq2[2], x3-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq2[3], x4-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq2[4], x5-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq2[5], x6-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq2[6], x7-frameTextSize/2, txbb, P_FrameText);
				canvas.drawText(TV_Freq2[7], x8-frameTextSize, txbb, P_FrameText);
				canvas.drawText(TV_Freq2[8], x9-frameTextSize*3/2, txbb, P_FrameText);		
			} 
		}
		
	}
	void DrawFreqNum(Canvas canvas){
		
		
		for(int i=0;i<MAXEQ;i++){
			int nFr	= GetFreqIndex(EQ_Filter.EQ[i].freq);	//查找索引值P_MidText
			FreqNumPointY[i]=MidY-m_nPoint[nFr]*stepY;
			FreqNumPointX[i]=nFr*stepX+marginLeft;
			if(FreqNumPointY[i]>(MidY+FrameHeight/2-EQTextSize/2)){
				FreqNumPointY[i]= MidY+FrameHeight/2-EQTextSize;
			}else if(FreqNumPointY[i]<(marginTop+EQTextSize/2)){
				FreqNumPointY[i]= marginTop+EQTextSize;
			}
			if(i>=9){
				FreqNumPointX[i]-=EQTextSize/2;
			}
			canvas.drawText(String.valueOf(i+1), 
					(int) (FreqNumPointX[i])-EQTextSize/4, 
					(int) (FreqNumPointY[i])+EQTextSize/4, 
					P_MidText);
		}
	}
	//画曲线
	void DrawLine(Canvas canvas){
		ComposePoint();
		//连线
		if(bool_HideBg){
			for(int i=line_start;i<line_end;i++){
				if((m_nPointY[i]<(Wind_Height-marginBottom))&&(m_nPointY[i]>marginTop)&&
					(m_nPointY[i+1]<(Wind_Height-marginBottom))&&(m_nPointY[i+1]>marginTop)){
					canvas.drawLine((float) m_nPointX[i-line_start], (float)m_nPointY[i], (float)m_nPointX[i-line_start+1], (float)m_nPointY[i+1], P_EQLine);
				}
			}
		}else if(!bool_HideBg){
			for(int i=line_start;i<line_end;i++){
				canvas.drawLine((float) m_nPointX[i-line_start], (float)m_nPointY[i], (float)m_nPointX[i-line_start+1], (float)m_nPointY[i+1], P_EQLine);
			}
			
		}
		
		
		//刷新上下边线
		if(!bool_HideBg){
			canvas.drawLine(Left-frameInsideWith, Top, Right+frameInsideWith, Top, P_FrameOutSide);//上边
			canvas.drawLine(Left-frameInsideWith, Bottom, Right+frameInsideWith, Bottom, P_FrameOutSide);//下边
			canvas.drawLine(Left, Top, Left, Bottom, P_FrameOutSide);//左边
			canvas.drawLine(Right, Top, Right, Bottom, P_FrameOutSide);//右边
		}
		
	}
	//隐藏曲线，只显示线
	public void SetHideBg(boolean hide){
		bool_HideBg = hide;
		//if(bool_HideLR_Side){
        	marginLeft=0;  
        	marginRight=0;    
        	marginTop=0; 
        	marginBottom=0;
        //}
	}
	//把各点组合成曲线
	void ComposePoint(){

		for(int i=0;i<241;i++){
			m_nPoint[i]=0;
			for(int j=0;j<MAXEQ;j++){
				m_nPoint[i]+=m_nPointPEQ[j][i];
			}		
			m_nPoint[i]+=m_nPointHP[i];
			m_nPoint[i]+=m_nPointLP[i];
		}
		
		
		//计算出各占坐标
		double step=(double)FrameHeight / (double)DataStruct.CurMacMode.EQ.EQ_Gain_MAX;//每个DP对应多少个高度的点数
		double stepx=FrameWidth/240.0;//(double)(line_end-line_start);
		for(int i=0;i<241;i++){
			m_nPointY[i]=MidY-m_nPoint[i]*10.0*step;
			m_nPointX[i]=i*stepx+marginLeft;
			if(!bool_HideBg){
				if(m_nPointY[i]>(Wind_Height-marginBottom)){
					m_nPointY[i]=(Wind_Height-marginBottom);
				}else if(m_nPointY[i]<(marginTop)){
					m_nPointY[i]=marginTop;
				}
			}			
		}
	}

	public void  SetEQData(DataStruct_Output SyncEQ) {
		MAXEQ = 31;
		for(int j=0;j<MAXEQ;j++){ 
			EQ_Filter.EQ[j].freq  = SyncEQ.EQ[j].freq;
			EQ_Filter.EQ[j].level = SyncEQ.EQ[j].level;
			EQ_Filter.EQ[j].bw    = SyncEQ.EQ[j].bw;
			EQ_Filter.EQ[j].shf_db= SyncEQ.EQ[j].shf_db;
			EQ_Filter.EQ[j].type  = SyncEQ.EQ[j].type;

//			System.out.println("EQ j:"+j);
//			System.out.println("EQ freq:"+EQ_Filter.EQ[j].freq);
//			System.out.println("EQ level:"+EQ_Filter.EQ[j].level);
//			System.out.println("EQ bw:"+EQ_Filter.EQ[j].bw);
//			System.out.println("EQ shf_db:"+EQ_Filter.EQ[j].shf_db);
//			System.out.println("EQ type:"+EQ_Filter.EQ[j].type);
		}
		
		EQ_Filter.h_freq   = SyncEQ.h_freq;
		EQ_Filter.h_filter = SyncEQ.h_filter;
		EQ_Filter.h_level  = SyncEQ.h_level;
		EQ_Filter.l_freq   = SyncEQ.l_freq;
		EQ_Filter.l_filter = SyncEQ.l_filter;
		EQ_Filter.l_level  = SyncEQ.l_level;	
		
//  		System.out.println("EQ h_freq:"+EQ_Filter.h_freq);
//  		System.out.println("EQ h_filter:"+EQ_Filter.h_filter);
//  		System.out.println("EQ h_level:"+EQ_Filter.h_level);
//  		System.out.println("EQ l_freq:"+EQ_Filter.l_freq);
//  		System.out.println("EQ l_filter:"+EQ_Filter.l_filter);
//  		System.out.println("EQ l_level:"+EQ_Filter.l_level);
  		
  		UpdateEQFilter();
	}

	public void  SetEFFData(DataStruct_EFFect SyncEQ) {
		MAXEQ = 3;
		for(int j=0;j<MAXEQ;j++){
			EQ_Filter.EQ[j].freq  = SyncEQ.EQ[j].freq;
			EQ_Filter.EQ[j].level = SyncEQ.EQ[j].level;
			EQ_Filter.EQ[j].bw    = SyncEQ.EQ[j].bw;
			EQ_Filter.EQ[j].shf_db= SyncEQ.EQ[j].shf_db;
			EQ_Filter.EQ[j].type  = SyncEQ.EQ[j].type;

//			System.out.println("EQ j:"+j);
//			System.out.println("EQ freq:"+EQ_Filter.EQ[j].freq);
//			System.out.println("EQ level:"+EQ_Filter.EQ[j].level);
//			System.out.println("EQ bw:"+EQ_Filter.EQ[j].bw);
//			System.out.println("EQ shf_db:"+EQ_Filter.EQ[j].shf_db);
//			System.out.println("EQ type:"+EQ_Filter.EQ[j].type);

		}

		EQ_Filter.h_freq   = SyncEQ.h_freq;
		EQ_Filter.h_filter = SyncEQ.h_filter;
		EQ_Filter.h_level  = SyncEQ.h_level;
		EQ_Filter.l_freq   = SyncEQ.l_freq;
		EQ_Filter.l_filter = SyncEQ.l_filter;
		EQ_Filter.l_level  = SyncEQ.l_level;

//		System.out.println("EQ h_freq:"+EQ_Filter.h_freq);
//		System.out.println("EQ h_filter:"+EQ_Filter.h_filter);
//		System.out.println("EQ h_level:"+EQ_Filter.h_level);
//		System.out.println("EQ l_freq:"+EQ_Filter.l_freq);
//		System.out.println("EQ l_filter:"+EQ_Filter.l_filter);
//		System.out.println("EQ l_level:"+EQ_Filter.l_level);

		UpdateEQFilter();
	}

	public DataStruct_Output  GetEQData() {
		for(int j=0;j<MAXEQ;j++){ 
			ReadEQ.EQ[j].freq  = EQ_Filter.EQ[j].freq;
			ReadEQ.EQ[j].level = EQ_Filter.EQ[j].level;
			ReadEQ.EQ[j].bw    = EQ_Filter.EQ[j].bw;
			ReadEQ.EQ[j].shf_db= EQ_Filter.EQ[j].shf_db;
			ReadEQ.EQ[j].type  = EQ_Filter.EQ[j].type;
		}
		
		ReadEQ.h_freq   = EQ_Filter.h_freq;
		ReadEQ.h_filter = EQ_Filter.h_filter;
		ReadEQ.h_level  = EQ_Filter.h_level;
		ReadEQ.l_freq   = EQ_Filter.l_freq;
		ReadEQ.l_filter = EQ_Filter.l_filter;
		ReadEQ.l_level  = EQ_Filter.l_level;		
		
		return ReadEQ;
	}
	/*
	 * 高通滤波器更新
	 * 
	 */
	private void UpdateEQFilter(){		
		UpDataHPF();
		UpDataLPF();
		for(int i=0;i<MAXEQ;i++){
			UpDataPEQ(i);
		}			
		invalidate();
	}
		
	
	/*
	 * 高通滤波器更新
	 * 
	 */
	private void UpDataHPF()
	{
		int	i;
		double HPF_FsPi,f,HPF_Wf;
		double w0;
		double HPF_fre;
		int	   HPF_type,HPF_oct;
		double HPF_Q[] = new double[4];
		
		double temp_a0[] = new double[4];
		double temp_a1[] = new double[4];
		double temp_a2[] = new double[4];
		
		double temp_b0[] = new double[4];
		double temp_b1[] = new double[4];
		double temp_b2[] = new double[4];
		
		double w0_cValue,w0_sValue,w_h,alph_h,alphah;
		double a1,a2,b1,b2,b3,A,B,C,D,EE,tmp;
		tmp	=	0.0;
		HPF_fre	   = EQ_Filter.h_freq;
		HPF_type   = EQ_Filter.h_filter;
		int m_nOct = EQ_Filter.h_level;
		boolean m_nDataType = false;
		if(m_nDataType){
			if(m_nOct	==1){
				HPF_oct	=	m_nOct+1;
			}else{
				HPF_oct	=	m_nOct;
			}
		}else{
			HPF_oct	=	m_nOct;
			if(HPF_type	== Bessel){
				/*if(HPF_oct ==	Oct_36dB){
					HPF_oct	=	Oct_42dB;
				}else{
					if(HPF_oct ==	Oct_42dB){
						HPF_oct	=	Oct_36dB;
					}
				}*/
				switch(HPF_oct){
					case Oct_30dB:
						HPF_oct	=	Oct_42dB;
						break;
					case Oct_36dB:
						HPF_oct	=	Oct_48dB;
						break;
					case Oct_42dB:
						HPF_oct	=	Oct_30dB;
						break;
					case Oct_48dB:
						HPF_oct	=	Oct_36dB;
						break;
				}
			}
		}	
		HPF_FsPi = 0.00006544985;
		switch(HPF_oct){
			//case Oct_6dB:
			case Oct_12dB:
				if(HPF_type	== L_R){
					HPF_Q[0] = 1.0;
					HPF_Q[1] = 0.0;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
				}
				if(HPF_type	== ButtWorth){
					HPF_Q[0] = 0.70422535;
					HPF_Q[1] = 0.0;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
				}
				if(HPF_type	== Bessel){
					HPF_Q[0] = 0.70422535;
					HPF_Q[1] = 0.0;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
					HPF_fre	=	HPF_fre/1.2683112435;			
				}
				break;
			case Oct_18dB:
				if(HPF_type	== L_R){
					HPF_Q[0] = 1.0;
					HPF_Q[1] = 0.70422535211;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
				}
				if(HPF_type	== ButtWorth){
					HPF_Q[0] = 1.0;
					HPF_Q[1] = 0.5;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
				}
				if(HPF_type	== Bessel){
					HPF_Q[0] = 0.7560;
					HPF_Q[1] = 0.72463768;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
					HPF_fre	=	HPF_fre/1.2854981442;			
				}		
				break;
			case Oct_24dB:
				if(HPF_type	== L_R){
					HPF_Q[0] = 0.70422535211;
					HPF_Q[1] = 0.70422535211;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
				}
				if(HPF_type	== ButtWorth){
					HPF_Q[0] = 0.92592593;
					HPF_Q[1] = 0.38167939;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
				}
				if(HPF_type	== Bessel){
					HPF_Q[0] = 0.96153846;
					HPF_Q[1] = 0.61728395;
					HPF_Q[2] = 0.0;
					HPF_Q[3] = 0.0;
					HPF_fre	=	HPF_fre/1.5006420029;					
				}
				break;
			case Oct_30dB:
				if(	HPF_type==L_R){
					HPF_Q[0]=1.0;
					HPF_Q[1]=0.62;
					HPF_Q[2]=0.62;
					HPF_Q[3]=0.0;
				}
				if(	HPF_type==ButtWorth){
					HPF_Q[0]=1.0;
					HPF_Q[1]=0.8064616129;
					HPF_Q[2]=0.3086417953;
					HPF_Q[3]=0.0;
				}
				if(	HPF_type==Bessel){
					HPF_Q[0]=0.6656;
					HPF_Q[1]=0.8928571428;
					HPF_Q[2]=0.54347826;
					HPF_Q[3]=0.0;
					HPF_fre	=	HPF_fre	/	1.3854981442;
				}
				break;
			case Oct_36dB:
				if(	HPF_type==L_R){
					HPF_Q[0]=1.0;
					HPF_Q[1]=0.5;
					HPF_Q[2]=0.5;
					HPF_Q[3]=0.0;
				}
				if(	HPF_type==ButtWorth){
					HPF_Q[0]=0.96153846;
					HPF_Q[1]=0.70422535211;
					HPF_Q[2]=0.2590673575;
					HPF_Q[3]=0.0;
				}
				if(	HPF_type==Bessel){
					HPF_Q[0]=0.980392156;
					HPF_Q[1]=0.819672131;
					HPF_Q[2]=0.490196;
					HPF_Q[3]=0.0;
					HPF_fre	=	HPF_fre	/	1.5554981442;
				}
				break;
			case Oct_42dB:
				if(	HPF_type==L_R){
					HPF_Q[0]=1.0;
					HPF_Q[1]=0.4531343150;
					HPF_Q[2]=0.92592592593;
					HPF_Q[3]=0.4531343150;
				}
				if(	HPF_type==ButtWorth){
					HPF_Q[0]=1.0;
					HPF_Q[1]=0.90909090909;
					HPF_Q[2]=0.625;
					HPF_Q[3]=0.2222222222;
				}
				if(	HPF_type==Bessel){
					HPF_Q[0]=0.5937;
					HPF_Q[1]=0.943396226415;
					HPF_Q[2]=0.757575757576;
					HPF_Q[3]=0.4424778761;
					HPF_fre	=	HPF_fre	/	1.5554981442;
				}
				break;
			case Oct_48dB:
				if(HPF_type	== L_R){
					HPF_Q[0] = 0.92592592593;
					HPF_Q[1] = 0.3731343150;
					HPF_Q[2] = 0.92592592593;
					HPF_Q[3] = 0.3731343150;
				}
				if(HPF_type	== ButtWorth){
					HPF_Q[0] = 0.98039126;
					HPF_Q[1] = 0.83333333;
					HPF_Q[2] = 0.55555556;
					HPF_Q[3] = 0.1953125;
				}
				if(HPF_type	== Bessel){
					HPF_Q[0] = 0.98039216;
					HPF_Q[1] = 0.89285714;
					HPF_Q[2] = 0.70422535;
					HPF_Q[3] = 0.40650406;
					HPF_fre	=	HPF_fre/1.6791201410;
					
				}
				break;
		}
		w0 = HPF_FsPi	*	HPF_fre;
		w0_cValue	=	Math.cos(w0);
		w0_sValue	=	Math.sin(w0);
		w_h	=	w0*0.5;
		alph_h = Math.tan(w_h);
		for(i=0;i<4;i++){
			alphah = w0_sValue * HPF_Q[i];
			if(HPF_Q[i]	== 0){
				temp_a0[i] = 1;
				temp_a1[i] = 0;
				temp_a2[i] = 0;
				temp_b0[i] = 1;
				temp_b1[i] = 0;
				temp_b2[i] = 0;		 
			}else{
				temp_a0[i] = 1+alphah;
				if(temp_a0[i]	== 0){
					temp_a0[i] = 0.0000000001;
				}
				temp_a1[i] = -2.0*w0_cValue;
				temp_a2[i] = 1-alphah;
				temp_b0[i] = (1+w0_cValue)*0.5;
				temp_b1[i] = -2.0*temp_b0[i];
				temp_b2[i] = temp_b0[i];				
			}
		}
//		if(HPF_oct ==	Oct_18dB|| HPF_oct ==	Oct_30dB ||	HPF_oct	== Oct_42dB){
//			temp_a0[0] = HPF_Q[0]*alph_h+1;
//			if(temp_a0[0]	== 0)	temp_a0[0] =0.0000000001;
//			temp_a1[0] = HPF_Q[0]*alph_h-1;
//			temp_a2[0] = 0.0;
//			temp_b0[0] = 1.0;
//			temp_b1[0] = -1.0;
//			temp_b2[0] = 0.0;
//		}
		if(HPF_oct == Oct_18dB|| HPF_oct == Oct_30dB || HPF_oct == Oct_42dB || HPF_oct == Oct_6dB)
		  {
			  temp_a0[0] = HPF_Q[0]*alph_h+1;
			  if(temp_a0[0] == 0) temp_a0[0] =0.0000000001;
			  temp_a1[0] = HPF_Q[0]*alph_h-1;
			  temp_a2[0] = 0.0;
			  temp_b0[0] = 1.0;
			  temp_b1[0] = -1.0;
			  temp_b2[0] = 0.0;
		  }

		for(int	j=0;j< 241;j++){
			f	=	Define.FREQ241[j];
			HPF_Wf = 2*pi*f/FS;
			for(i=0;i<4;i++){
				a1 = temp_a1[i]/temp_a0[i];
				a2 = temp_a2[i]/temp_a0[i];
				
				b1 = temp_b0[i]/temp_a0[i];
				b2 = temp_b1[i]/temp_a0[i];
				b3 = temp_b2[i]/temp_a0[i];
				A	=	Math.cos(HPF_Wf)+a1+a2*Math.cos(HPF_Wf);
				B	=	Math.sin(HPF_Wf)-a2*Math.sin(HPF_Wf);
				C	=	b1*Math.cos(HPF_Wf)+b2+b3*Math.cos(HPF_Wf);
				D	=	b1*Math.sin(HPF_Wf)-b3*Math.sin(HPF_Wf);
				EE  = Math.sqrt((C*C+D*D)/(A*A+B*B));
				tmp	=	tmp	+20*Math.log10(EE);
			}
			if(EQ_Filter.h_freq!=20){
				m_nPointHP[j]	=	tmp;
			}			
			tmp	=	0;
		}
		if((EQ_Filter.h_freq==20)||(EQ_Filter.h_level==HL_OFF)){
			for(int	j=0;j< 241;j++){
				m_nPointHP[j]	=	0;
			}			
		}

	}
	/*
	 * 低通滤波器更新
	 * 
	 */
	private void UpDataLPF()
	{
		int	i;
		double LPF_FsPi,f;
		double w0;
		double LPF_fre;
		int	LPF_type,LPF_oct;
		double alphal1,alphal2;
		double Bessel_LPF_Qa[] = new double[4];
		double Bessel_LPF_Qb[] = new double[4];
		double temp_bi,temp_ai;
		double temp_a0[] = new double[4];
		double temp_a1[] = new double[4];
		double temp_a2[] = new double[4];
		double temp_b0[] = new double[4];
		double temp_b1[] = new double[4];
		double temp_b2[] = new double[4];
		double LPF_Q[] = new double[4];
		double a1,a2,b1,b2,b3;
		double A,B,C,D,EE,tmp;
		double w0_cValue,w0_sValue,w_l,alph_l,alphal;
		double LPF_Wf;
		tmp	=	0;
		LPF_fre	 =	EQ_Filter.l_freq;
		LPF_type =  EQ_Filter.l_filter;
		int m_nOct =  EQ_Filter.l_level;
		boolean m_nDataType=false;
		if(m_nDataType){
			if(m_nOct	== 1){
				 LPF_oct = m_nOct+1;	
			}else{
				 LPF_oct = m_nOct;	
			}
		}else{
			LPF_oct	=	m_nOct;	
		}
		LPF_FsPi = 0.00006544985;	
		if(	LPF_type ==	Bessel ){
			/*	
			if(LPF_oct ==	Oct_36dB){
				LPF_oct = Oct_42dB;
			}else{
				if(LPF_oct ==	Oct_42dB){
								 LPF_oct = Oct_36dB;
				}
			}*/
			switch(LPF_oct){
				case Oct_30dB:
					LPF_oct	=	Oct_48dB;
					break;
				case Oct_36dB:
					LPF_oct	=	Oct_42dB;
					break;
				case Oct_42dB:
					LPF_oct	=	Oct_30dB;
					break;
				case Oct_48dB:
					LPF_oct	=	Oct_36dB;
					break;
			}
			switch(LPF_oct){
				//case Oct_6dB:
				case Oct_12dB:
					LPF_fre	=	LPF_fre/1.3616541287;
					Bessel_LPF_Qa[0] = 1.0;
					Bessel_LPF_Qb[0] = 0.3333333333;
					
					Bessel_LPF_Qa[1] = 0.0;
					Bessel_LPF_Qb[1] = 0.0;
					
					Bessel_LPF_Qa[2] = 0.0;
					Bessel_LPF_Qb[2] = 0.0;
					
					Bessel_LPF_Qa[3] = 0.0;
					Bessel_LPF_Qb[3] = 0.0;
					break;
				case Oct_18dB:
					LPF_fre	=	LPF_fre/1.75567236868;
					Bessel_LPF_Qa[0] = 0.5693712514;
					Bessel_LPF_Qb[0] = 0.154812441;
					
					Bessel_LPF_Qa[1] = 0.430628846;
					Bessel_LPF_Qb[1] = 0.0;
					
					Bessel_LPF_Qa[2] = 0.0;
					Bessel_LPF_Qb[2] = 0.0;
					
					Bessel_LPF_Qa[3] = 0.0;
					Bessel_LPF_Qb[3] = 0.0;
					break;
				case Oct_24dB:
					LPF_fre	=	LPF_fre/2.1139176749;
					Bessel_LPF_Qa[0] = 0.369;
					Bessel_LPF_Qb[0] = 0.087858766;
					
					Bessel_LPF_Qa[1] = 0.6278294896;
					Bessel_LPF_Qb[1] = 0.109408;
					
					Bessel_LPF_Qa[2] = 0.0;
					Bessel_LPF_Qb[2] = 0.0;
					
					Bessel_LPF_Qa[3] = 0.0;
					Bessel_LPF_Qb[3] = 0.0;
					break;
				case Oct_30dB:
					LPF_fre=LPF_fre	/	1.0;//2.1139176749;									
					Bessel_LPF_Qa[0] = 0.6656;//0.369;
					Bessel_LPF_Qb[0] = 0.0;//0.087858766;
					
					Bessel_LPF_Qa[1] = 1.1402;//0.6278294896;
					Bessel_LPF_Qb[1] = 0.4128;//0.109408;
					
					Bessel_LPF_Qa[2] = 0.6216;
					Bessel_LPF_Qb[2] = 0.3245;
					
					Bessel_LPF_Qa[3] = 0.0;
					Bessel_LPF_Qb[3] = 0.0;
					break;
				case Oct_36dB:
					LPF_fre=LPF_fre	/	1.0;;									
					Bessel_LPF_Qa[0] = 1.2217;//0.369;
					Bessel_LPF_Qb[0] = 0.3887;//0.087858766;
					
					Bessel_LPF_Qa[1] = 0.9686;//0.6278294896;
					Bessel_LPF_Qb[1] = 0.3505;//0.109408;
					
					Bessel_LPF_Qa[2] = 0.5131;
					Bessel_LPF_Qb[2] = 0.2756;
					
					Bessel_LPF_Qa[3] = 0.0;
					Bessel_LPF_Qb[3] = 0.0;
					break;
				case Oct_42dB:
					LPF_fre=LPF_fre	/	1.0;//3.17961723751;								
					Bessel_LPF_Qa[0] = 0.5937;//0.117235677;
					Bessel_LPF_Qb[0] = 0.0;//0.02064747;
					
					Bessel_LPF_Qa[1] = 1.0944;//0.226516664;
					Bessel_LPF_Qb[1] = 0.3395;//0.0259273886;
					
					Bessel_LPF_Qa[2] = 0.8304;//0.3067559;
					Bessel_LPF_Qb[2] = 0.3011;//0.0294683265;
					
					Bessel_LPF_Qa[3] = 0.4332;//0.3494916166;
					Bessel_LPF_Qb[3] = 0.2381;//0.031272257;
					break;
				case Oct_48dB:
					LPF_fre	=	LPF_fre/3.17961723751;
					Bessel_LPF_Qa[0] = 0.117235677;
					Bessel_LPF_Qb[0] = 0.02064747;
					
					Bessel_LPF_Qa[1] = 0.226516664;
					Bessel_LPF_Qb[1] = 0.0259273886;
					
					Bessel_LPF_Qa[2] = 0.3067559;
					Bessel_LPF_Qb[2] = 0.0294683265;
					
					Bessel_LPF_Qa[3] = 0.3494916166;
					Bessel_LPF_Qb[3] = 0.031272257;
					break;
			}
			w0 = LPF_fre * LPF_FsPi;
			alphal1	=	Math.tan(w0*0.5);
			alphal2	=	alphal1	*alphal1;
			for( i = 0;i<4;i++){
				temp_ai	=	Bessel_LPF_Qa[i];
				temp_bi	=	Bessel_LPF_Qb[i];
				if(temp_ai ==	0){
					temp_a0[i] = 1;
					temp_a1[i] = 0;
					temp_a2[i] = 0;
					temp_b0[i] = 1;
					temp_b1[i] = 0;
					temp_b2[i] = 0;
				}else{
					temp_a0[i] = alphal2+temp_ai*alphal1+temp_bi;
					temp_a1[i] = 2*alphal2 - 2*temp_bi;
					temp_a2[i] = alphal2-temp_ai*alphal1+temp_bi;
					temp_b0[i] = alphal2;
					temp_b1[i] = 2*alphal2;
					temp_b2[i] = alphal2;
				}			
			}
		}else{
			switch(LPF_oct){
			//case Oct_6dB:
			case Oct_12dB:
				if(LPF_type	== L_R){
					LPF_Q[0] = 1.0;
					LPF_Q[1] = 0.0;
					LPF_Q[2] = 0.0;
					LPF_Q[3] = 0.0;
				}
				if(LPF_type	== ButtWorth){
					LPF_Q[0] = 0.70422535;
					LPF_Q[1] = 0.0;
					LPF_Q[2] = 0.0;
					LPF_Q[3] = 0.0;
				}
				break;
			case Oct_18dB:
				if(LPF_type	== L_R){
					LPF_Q[0] = 1.0;
					LPF_Q[1] = 0.70422535211;
					LPF_Q[2] = 0.0;
					LPF_Q[3] = 0.0;
				}
				if(LPF_type	== ButtWorth){
					LPF_Q[0] = 1.0;
					LPF_Q[1] = 0.5;
					LPF_Q[2] = 0.0;
					LPF_Q[3] = 0.0;
				}
				break;
				
			case Oct_24dB:
				if(LPF_type	== L_R){
					LPF_Q[0] = 0.70422535211;
					LPF_Q[1] = 0.70422535211;
					LPF_Q[2] = 0.0;
					LPF_Q[3] = 0.0;
				}
				if(LPF_type	== ButtWorth){
					LPF_Q[0] = 0.92592593;
					LPF_Q[1] = 0.38167939;
					LPF_Q[2] = 0.0;
					LPF_Q[3] = 0.0;
				}
				break;

			case Oct_30dB:
				if(	LPF_type==L_R){
					LPF_Q[0]=1.0;
					LPF_Q[1]=0.62;
					LPF_Q[2]=0.62;
					LPF_Q[3]=0.0;
				}
				if(	LPF_type==ButtWorth){
					LPF_Q[0]=1.0;
					LPF_Q[1]=0.8064616129;
					LPF_Q[2]=0.3086417953;
					LPF_Q[3]=0.0;
				}
				break;
			case Oct_36dB:
				if(	LPF_type==L_R){
					LPF_Q[0]=1.0;
					LPF_Q[1]=0.5;
					LPF_Q[2]=0.5;
					LPF_Q[3]=0.0;
				}
				if(	LPF_type==ButtWorth){
					LPF_Q[0]=0.96153846;
					LPF_Q[1]=0.70422535211;
					LPF_Q[2]=0.2590673575;
					LPF_Q[3]=0.0;
				}
				break;
			case Oct_42dB:
				if(	LPF_type==L_R){
					LPF_Q[0]=1.0;
					LPF_Q[1]=0.4531343150;
					LPF_Q[2]=0.92592592593;
					LPF_Q[3]=0.4531343150;
				}
				if(	LPF_type==ButtWorth){
					LPF_Q[0]=1.0;
					LPF_Q[1]=0.90909090909;
					LPF_Q[2]=0.625;
					LPF_Q[3]=0.2222222222;
				}
				break;
			case Oct_48dB:
				if(LPF_type	==	L_R){
					LPF_Q[0] = 0.92592592593;
					LPF_Q[1] = 0.3731343150;
					LPF_Q[2] = 0.92592592593;
					LPF_Q[3] = 0.3731343150;
				}
				if(LPF_type	== ButtWorth){
					LPF_Q[0] = 0.98039216;
					LPF_Q[1] = 0.83333333;
					LPF_Q[2] = 0.55555556;
					LPF_Q[3] = 0.1953125;
				}
				break;
			}
			w0	=	LPF_fre	*LPF_FsPi;
			w0_cValue	=	Math.cos(w0);
			w0_sValue	=	Math.sin(w0);
			w_l	=	w0*0.5;
			alph_l = Math.tan(w_l);
			for(i=0;i<4;i++){
				alphal = w0_sValue * LPF_Q[i];
				if(LPF_Q[i]	== 0){
					temp_a0[i] = 1;
					temp_a1[i] = 0;
					temp_a2[i] = 0;
					temp_b0[i] = 1;
					temp_b1[i] = 0;
					temp_b2[i] = 0;
				}else{
					temp_a0[i] = 1+alphal;
					if(temp_a0[i]	== 0){
						temp_a0[i] = 0.0000000001;
					}					
					temp_a1[i] =-2.0*w0_cValue;
					temp_a2[i] = 1-alphal;
					temp_b0[i] = (1-w0_cValue)*0.5;
					temp_b1[i] = 2.0*temp_b0[i];
					temp_b2[i] = temp_b0[i];					
				}				
			}
//			if(LPF_oct ==	Oct_18dB|| LPF_oct ==	Oct_30dB ||	LPF_oct	== Oct_42dB){
//				temp_a0[0] = alph_l+LPF_Q[0];
//				if(temp_a0[0]	== 0){
//					temp_a0[0] = 0.0000000001;					
//				}
//				temp_a1[0] = alph_l-LPF_Q[0];
//				temp_a2[0] = 0.0;
//				temp_b0[0] = alph_l;
//				temp_b1[0] = alph_l;
//				temp_b2[0] = 0.0;
//			}
			if(LPF_oct == Oct_18dB|| LPF_oct == Oct_30dB || LPF_oct == Oct_42dB|| LPF_oct == Oct_6dB )
			{
				temp_a0[0] = alph_l+LPF_Q[0];
				if(temp_a0[0] == 0)
				{
					temp_a0[0] = 0.0000000001;
					
				}
				temp_a1[0] = alph_l-LPF_Q[0];
				temp_a2[0] = 0.0;
				temp_b0[0] = alph_l;
				temp_b1[0] = alph_l;
				temp_b2[0] = 0.0;
			}
		}
		for(int	j=0;j<241;j++)
		{
			f	=	Define.FREQ241[j];
			LPF_Wf = 2*pi*f/FS;
			for(i=0;i<4;i++)
			{
				a1 = temp_a1[i]	/	temp_a0[i];
				a2 = temp_a2[i]	/	temp_a0[i];
				
				b1 = temp_b0[i]	/	temp_a0[i];
				b2 = temp_b1[i]	/	temp_a0[i];
				b3 = temp_b2[i]	/	temp_a0[i];
				A	=	 Math.cos(LPF_Wf) +a1+a2*Math.cos(LPF_Wf);
				B	=	 Math.sin(LPF_Wf) -a2*Math.sin(LPF_Wf);
				C	=	 b1*Math.cos(LPF_Wf)+b2+b3*Math.cos(LPF_Wf);
				D	=	 b1*Math.sin(LPF_Wf)-b3*Math.sin(LPF_Wf);
				EE = Math.sqrt((C*C+D*D)/(A*A+B*B));
				tmp	=	tmp+20*Math.log10(EE);
			}
			if(EQ_Filter.l_freq!=20000){
				m_nPointLP[j]	=	tmp;
			}
			tmp	=	0;	
			
		}	
		
		if((EQ_Filter.l_freq==20000)||(EQ_Filter.l_level==HL_OFF)){
			for(int	j=0;j< 241;j++){
				m_nPointLP[j]	=	0;
			}			
		}
	}
	private double GetBW(int BW) {
		return (BW*0.01+0.05);
	}
	/**
	 * 更新EQ的图形
	 */
	private void UpDataPEQ(int eqIndex)
	{		
		//mtest
		//EQ_Filter.EQ[eqIndex].bw=0;//295
		//EQ_Filter.EQ[4].level=450;
	
		double f;
		double PEQ_FsPi,PEQ_Fc,PEQ_dB,PEQ_bw;
		double w0,temp,temp_A,temp_sin,alpha,temp_a0,temp_a1,temp_a2,temp_b0,temp_b1,temp_b2;
		double a1,a2,b1,b2,b3;
		double A,B,C,D,EE,tmp;
		double PEQ_Wf;
		int	nFreq;
		
		if(EQ_Filter.EQ[eqIndex].bw>Define.EQ_BW_MAX){
			EQ_Filter.EQ[eqIndex].bw=Define.EQ_BW_MAX;
		}
		
		nFreq	=	GetFreqIndex(EQ_Filter.EQ[eqIndex].freq);	//查找索引值

		//PEQ_Fc  = EQ_Filter.EQ[eqIndex].freq;
//		PEQ_Fc = DataStruct.FREQ241[nFreq];
//		PEQ_dB = (EQ_Filter.EQ[eqIndex].level-DataStruct.EQ_LEVEL_ZERO)/10;
		//PEQ_bw = DataStruct.EQ_BW[DataStruct.EQ_BW_MAX-EQ_Filter.EQ[eqIndex].bw];
		
//		PEQ_bw = 0.200 + (EQ_Filter.EQ[eqIndex].bw)/100.0;
		//PEQ_bw = (double) (DataStruct.EQ_BW[DataStruct.EQ_BW_MAX-EQ_Filter.EQ[eqIndex].bw])*0.2;
		//System.out.println("EQ EQ_Filter.EQ[eqIndex].bw:"+EQ_Filter.EQ[eqIndex].bw);
		
		PEQ_Fc = Define.FREQ241[nFreq];
		PEQ_dB = (EQ_Filter.EQ[eqIndex].level-DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO)/10.0;
		PEQ_bw = GetBW(EQ_Filter.EQ[eqIndex].bw);
		
		
		//System.out.println("EQ nFreq@"+eqIndex+":"+nFreq);
		//System.out.println("EQ PEQ_dB:"+PEQ_dB);
		//System.out.println("EQ PEQ_bw:"+PEQ_bw);
		try {


			for (int i = 0; i < 241; i++) {
				f = Define.FREQ241[i];
				PEQ_Wf = 2 * pi * f / FS;
				//PEQ_FsPi = 0.00013089969;
				PEQ_FsPi = 0.00006544985;
				w0 = PEQ_Fc * PEQ_FsPi;
				temp = PEQ_dB / 40.0;
				temp_A = Math.pow(10.0, temp);
				temp_sin = Math.sin(w0);
				alpha = temp_sin * Math.sinh(0.69314718 * 0.5 * PEQ_bw * w0 / temp_sin);
				temp_a0 = 1 + alpha / temp_A;
				temp_a1 = (-2) * Math.cos(w0);
				temp_a2 = 1 - alpha / temp_A;
				temp_b0 = 1 + alpha * temp_A;
				temp_b1 = (-2) * Math.cos(w0);
				temp_b2 = 1 - alpha * temp_A;
				temp_a0 = 1.0 / temp_a0;
				a1 = temp_a1 * temp_a0;      //a1
				a2 = temp_a2 * temp_a0;     //a2
				b1 = temp_b0 * temp_a0;  //b0
				b2 = temp_b1 * temp_a0;    //b1
				b3 = temp_b2 * temp_a0;//b2
				A = Math.cos(PEQ_Wf) + a1 + a2 * Math.cos(PEQ_Wf);
				B = Math.sin(PEQ_Wf) - a2 * Math.sin(PEQ_Wf);
				C = b1 * Math.cos(PEQ_Wf) + b2 + b3 * Math.cos(PEQ_Wf);
				D = b1 * Math.sin(PEQ_Wf) - b3 * Math.sin(PEQ_Wf);
				EE = Math.sqrt((C * C + D * D) / (A * A + B * B));
				tmp = 20 * Math.log10(EE);
				m_nPointPEQ[eqIndex][i] = tmp;//把文档之值转换为坐标值
			}
		}catch (ArrayIndexOutOfBoundsException e){
			System.out.println("BUG Error  超出");
		}
	}

	int GetFreqIndex(double Freq){
	 	double LNum,RNum,Num;
		for(int i=0;i<241;i++){
			Num = Define.FREQ241[i];
			if(Num >=Freq){
				if(i>0){
					LNum = Freq - Define.FREQ241[i-1];
					RNum = Num - Freq;
					if(LNum < RNum){
						return (i-1);
					}else{
						return i;
					}
				}
				return i;
			}else{
				if(i == 240){
					return i;
				}
			}	
		}		
		return -1;
	}
	
	public void SetStartEnd(int start, int end) {		
		line_start=start;
		line_end=end;
	} 
	
	public void HideLeftRightSide(boolean hide) {		
		bool_HideLR_Side=hide;
	} 
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////       触摸事件            //////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//保存滤波器图形每点所对应的dB值的Y坐标
	//double m_nPointY[] = new double[241];
	//double m_nPointX[] = new double[241];
	//保存手滑过的坐标
	private void dealPoint(float x, float y){
		for(int i=0;i<MAXEQ;i++){
			if((i*stepW < x )&&(i+1)*stepW>=x){
				EQ_Filter.EQ[i].level = (int)(10*(FrameHeight - y)/stepH)+DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
				//if(DEBUG) System.out.println(TAG+"EQ EQ_Filter.EQ["+i+"].level:"+EQ_Filter.EQ[i].level);
			}
		}
	}
	private boolean isDown(float x, float y){
		if((x >= marginLeft) && ((Wind_Width - marginRight) >= x) &&
		   (y >= marginTop) && ((Wind_Height - marginBottom) >= y)){
			return true;	
		}		
		return false;		
	}
//	@Override
//    public boolean onTouchEvent(MotionEvent event) {
//    	if(!CanTouch){
//    		return true;
//    	}
//        float eventX = event.getX();
//        float eventY = event.getY();
//       
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            	//if(DEBUG) System.out.println(TAG+"MotionEvent.ACTION_DOWN");	
//            	if(isDown(eventX,eventY)){ 
//                	for(int i=0;i<DataStruct.MAXEQ;i++){//清零
//                		drawPointX[i] = 0;
//                		drawPointY[i] = 0;
//                	}
//                }
//            	if (mOnEQDrawChangeListener != null){  
//                	mOnEQDrawChangeListener.onProgressChanged(this,  this.EQ_Filter, true);  
//                }
//                break ;
//                
//            case MotionEvent.ACTION_MOVE:
//            	//if(DEBUG) System.out.println(TAG+"MotionEvent.ACTION_MOVE");	
//            	if(isDown(eventX,eventY)){ 
//                	//if(DEBUG) System.out.println(TAG+"EQ eventX:"+eventX+",eventY:"+eventY);
//                	dealPoint(eventX,eventY);
//                }
//            	if (mOnEQDrawChangeListener != null){  
//                	mOnEQDrawChangeListener.onProgressChanged(this,  this.EQ_Filter, true);  
//                }
//                break ;
//                
//            case MotionEvent.ACTION_UP:
//            	
//            	UpdateEQFilter();
//            	if (mOnEQDrawChangeListener != null){  
//                	mOnEQDrawChangeListener.onProgressChanged(this,  this.EQ_Filter, false);  
//                }
//	            //invalidate();
//                break ;
//            default: 
//
//	            //invalidate();
//            	break;
//        }
//        return true;
//    }
	
	public void setEQDrawChangeListener(OnEQDrawChangeListener l) {  
    	mOnEQDrawChangeListener = l;  
    }  
  
    public interface OnEQDrawChangeListener {  
  
        public abstract void onProgressChanged(EQ eq,  
        		DataStruct_Output EQ_Filter, boolean fromUser);  
  
    }  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
