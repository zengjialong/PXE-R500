package com.chs.mt.pxe_r500.bean;

public class DSP_SystemData {

	public int[] system = new int[32];

	public void Set_system(int[] initData) {
		for (int i = 0; i < 32; i++) {
			this.system[i] = initData[i];
		}
	}

	public int[] Get_system() {
		return system;
	}

	public DSP_SystemData( int[] system_all_data) {
		super();
		this.system = system_all_data;
	}
	public DSP_SystemData() {
		super();
		for (int i = 0; i < 32; i++) {
			this.system[i] = 0;
		}

	}
}
