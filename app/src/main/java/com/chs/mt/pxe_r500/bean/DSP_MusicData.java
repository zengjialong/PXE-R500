package com.chs.mt.pxe_r500.bean;

import com.chs.mt.pxe_r500.datastruct.Define;

public class DSP_MusicData {

	public int[][] music = new int[Define.MAX_CH][Define.IN_LEN];
	///////////////////////////////////////////////////////////////////////
	public void SetMusicData(int index, int[] initData){
		if(index >= Define.MAX_CH ){
			index = Define.MAX_CH-1;
		}
		for(int i=0;i<Define.IN_LEN;i++){
			this.music[index][i]=initData[i];
		}
	}
	public int[] GetMusicData(int index){
		if(index >= Define.MAX_CH ){
			index = Define.MAX_CH-1;
		}
		return music[index];
	}

	///////////////////////////////////////////////////////////////////////
	public DSP_MusicData(int[][] initData) {
		super();
		for(int i=0;i<Define.MAX_CH;i++){
			music[i]= new int[Define.IN_LEN];
		}
		
		this.music=initData;
	}
	public DSP_MusicData() {
		super();
		for(int i=0;i<Define.MAX_CH;i++){
			music[i]= new int[Define.IN_LEN];
		}
		for(int i=0;i<Define.MAX_CH;i++){
			for(int j=0;j<Define.IN_LEN;j++){
				this.music[i][j]=0;
			}
		}
		// TODO Auto-generated constructor stub
	}

 
}
