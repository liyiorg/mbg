package com.github.liyiorg.mbg.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MBGFileUtil {

	private static String ROOT_PATH = System.getProperty("user.dir");
	
	public static File getFile(String filePath){
		return getFile("/src/main/java/", filePath);
	}
	
	public static File getFile(String projectDir,String filePath){
		return new File(ROOT_PATH + projectDir + filePath);
	}
	
	public static boolean createFile(File file,String contents){
		if(file.getParentFile().isDirectory()){
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
		}
		InputStream in = new ByteArrayInputStream(contents.getBytes()); 
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			while ((bytesRead = in.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, bytesRead);
			}
			fileOutputStream.flush();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
