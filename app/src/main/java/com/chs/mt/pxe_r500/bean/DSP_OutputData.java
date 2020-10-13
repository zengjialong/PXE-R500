package com.chs.mt.pxe_r500.bean;

import com.chs.mt.pxe_r500.datastruct.Define;

public class DSP_OutputData {

	public int[][] output = new int[Define.MAX_CH][Define.OUT_LEN];
	///////////////////////////////////////////////////////////////////////
	public void SetOutputData(int index,int initData[]){
		if(index >= Define.MAX_CH ){
			index = Define.MAX_CH-1;
		}
		
		for(int i=0;i<Define.OUT_LEN;i++){
			this.output[index][i]=initData[i];
		}
	}
	public int[] GetOutputData(int index){
		if(index >= Define.MAX_CH ){
			index = Define.MAX_CH-1;
		}
		
		return output[index];
	}

	///////////////////////////////////////////////////////////////////////
	public DSP_OutputData(int index,int initData[]) {
		super();
		if(index >= Define.MAX_CH ){
			index = Define.MAX_CH-1;
		}
		for(int i=0;i<Define.MAX_CH;i++){
			output[i]= new int[Define.OUT_LEN];			
		}
		for(int j=0;j<Define.MAX_CH;j++){
			for(int i=0;i<Define.OUT_LEN;i++){
				this.output[j][i]=initData[i];
			}
		}
	}
	public DSP_OutputData() {
		super();
		for(int i=0;i<Define.MAX_CH;i++){
			output[i]= new int[Define.OUT_LEN];			
		}
		for(int j=0;j<Define.MAX_CH;j++){
			for(int i=0;i<Define.OUT_LEN;i++){
				this.output[j][i]=0;
			}
		}
	}
	public DSP_OutputData(int[][] output ) {
		super();
		for(int i=0;i<Define.MAX_CH;i++){
			output[i]= new int[Define.OUT_LEN];			
		}
		this.output=output;
	}

}
