package com.chs.mt.pxe_r500.bean;

import com.chs.mt.pxe_r500.datastruct.Define;

public class DSP_EFFData {

	public int[][] eff = new int[Define.MAX_EFF][Define.EFF_LEN];
	///////////////////////////////////////////////////////////////////////
	public void SetEFFData(int index, int[] initData){
		if(index >= Define.MAX_EFF ){
			index = Define.MAX_EFF-1;
		}
		for(int i=0;i<Define.EFF_LEN;i++){
			this.eff[index][i]=initData[i];
		}
	}
	public int[] GetEFFData(int index){
		if(index >= Define.MAX_EFF ){
			index = Define.MAX_EFF-1;
		}
		return eff[index];
	}

	///////////////////////////////////////////////////////////////////////
	public DSP_EFFData(int[][] initData) {
		super();
		for(int i=0;i<Define.MAX_EFF;i++){
			eff[i]= new int[Define.EFF_LEN];
		}

		this.eff=initData;
	}
	public DSP_EFFData() {
		super();
		for(int i=0;i<Define.MAX_EFF;i++){
			eff[i]= new int[Define.EFF_LEN];
		}
		for(int i=0;i<Define.MAX_EFF;i++){
			for(int j=0;j<Define.EFF_LEN;j++){
				this.eff[i][j]=0;
			}
		}
		// TODO Auto-generated constructor stub
	}

 
}
