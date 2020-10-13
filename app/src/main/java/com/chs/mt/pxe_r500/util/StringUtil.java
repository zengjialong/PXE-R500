package com.chs.mt.pxe_r500.util;

import java.util.List;

public class StringUtil {
	static public String join(List<String> list, String sep) {
		StringBuffer tmp = new StringBuffer();
		for(int i =0; i < list.size(); i++) {
			tmp.append(list.get(i));
			if(i<list.size()-1)
				tmp.append(sep);
		}
		return tmp.toString();
	}
}
