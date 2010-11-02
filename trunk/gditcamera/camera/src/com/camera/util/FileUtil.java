package com.camera.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

	public static InputStream file2Stream(String filePath){
		try{
			return new FileInputStream(new File(filePath));
			
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
}
