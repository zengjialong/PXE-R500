package com.chs.mt.pxe_r500.tools;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;

public class M_Button extends Button{
	private GradientDrawable gradientDrawable;
	private String backColors = "";
	private int backColori = 0;//
	private String backColorSelecteds = "";//����
	private int backColorSelectedi = 0;//�
	private int backGroundImage = 0;//
	private int backGroundImageSeleted = 0;//
	private String textColors = "";//
	private int textColori = 0;//
	private String textColorSeleteds = "";//���
	private int textColorSeletedi = 0;//��
	private float radius = 8;//
	private int shape = 0;//
	private Boolean fillet = false;
	private int Status=0;    
	private int Group =0xff; //�������
	private String GroupName="";//
	public M_Button(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public M_Button(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public M_Button(Context context) {
		this(context, null);
	}
	
	private void init() {
		Status= 0;    
		Group = 0xff; //
		GroupName="";//
	}
  	public void setStatus(int s_status) {
		Status = s_status;
	}
	public int getStatus() {
		return Status;
	}
 	public void setGroup(int s_Group) {
		Group = s_Group;
	}	
	public int getGroup() {
		return Group;
	}
	public void setGroupName(String s_GroupName) {
		GroupName = s_GroupName;
	}	
	public String getGroupName() {
		return GroupName;
	}
}