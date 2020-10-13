package com.chs.mt.pxe_r500.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {
	private ZipFile zipFile;
	private String outputDir;
	public void unzipArchive(ZipFile zipFile, String outputDir) {
		this.zipFile = zipFile;
		this.outputDir = outputDir;
    try {
    	Enumeration<?extends ZipEntry> entries = zipFile.entries();
    	while(entries.hasMoreElements()){
    		ZipEntry entry = entries.nextElement();
    		unzipEntry(entry);
    	}
    } catch (Exception e) {
      LogUtil.log(e);
    }
	}

	private void unzipEntry(ZipEntry entry) throws IOException {
		if (entry.isDirectory()) {
			File tmp = new File(outputDir, entry.getName());
			if(!tmp.exists())
				tmp.mkdir();
      return;
		}
		File outputFile = new File(outputDir, entry.getName());
		if (!outputFile.getParentFile().exists()) {
      outputFile.getParentFile().mkdir();
		}
		BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry)); 
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile, false));
		try { 
			int b = -1;
			while((b = is.read())!=-1)   {   
				os.write(b); 
      }   
		} finally {
      os.close();
      is.close();
		}	
	}
}
