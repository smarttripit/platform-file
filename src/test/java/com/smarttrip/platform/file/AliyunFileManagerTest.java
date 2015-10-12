/**
 * Date:2015年9月29日上午11:04:56
 * Copyright (c) 2015, songjiesdnu@163.com All Rights Reserved.
 */
package com.smarttrip.platform.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function: 测试阿里云的文件上传和下载. <br/>
 * date: 2015年9月29日 上午11:04:56 <br/>
 *
 * @author songjiesdnu@163.com
 */
public class AliyunFileManagerTest {
	private Logger logger = LoggerFactory.getLogger(AliyunFileManagerTest.class);

	private static AliyunFileManager afm;
	
	@Before
	public void init(){
		afm = new AliyunFileManager();
		afm.setAccessKeyId("nJnORGJzdcGxfATe");
		afm.setAccessKeySecret("QCf1uKEi5TFvAgiliOnkwjkNRhsoxC");
		afm.setEndpoint("http://oss-cn-beijing.aliyuncs.com");
		afm.setBucketName("bucket-songjie");
	}
		
	@Test(expected = IllegalArgumentException.class)
	public void testUpload_null() throws IOException{
		afm.upload(null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpload_empty() throws IOException{
		afm.upload("", new byte[]{});
	}
	
	@Test
	public void testUpload() throws IOException{
//		String filename = "22.jpg";
//		File file = new File("C:/22.jpg");
//		String filename = "11.zip";
//		File file = new File("C:/11.zip");
//		String filename = "88.zip";
//		File file = new File("C:/88.zip");
//		String filename = "Desktop.zip";
//		File file = new File("C:/Desktop.zip");
//		String filename = "25.zip";
//		File file = new File("C:/25.zip");
		String filename = "144.zip";
		File file = new File("C:/144.zip");
		InputStream is = new FileInputStream(file);
		byte[] content = IOUtils.toByteArray(is);
		afm.upload(filename, content);
		is.close();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDownload_null() throws IOException{
		String fileId = null;
		FileInfo fileInfo = afm.download(fileId);
		String fileName= fileInfo.getFileName();
		logger.debug("文件名:" + fileName);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDownload_emptyStr() throws IOException{
		String fileId = "";
		FileInfo fileInfo = afm.download(fileId);
		String fileName= fileInfo.getFileName();
		logger.debug("文件名:" + fileName);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDownload_nonexist() throws IOException{
		String fileId = "124565789tfh";
		FileInfo fileInfo = afm.download(fileId);
		String fileName= fileInfo.getFileName();
		logger.debug("文件名:" + fileName);
	}
	
	@Test
	public void testDownload() throws IOException{
		String fileId = "87cf579df73746509d46497d614f77e8";
		FileInfo fileInfo = afm.download(fileId);
		String fileName= fileInfo.getFileName();
		logger.debug("文件名:" + fileName);
	}
}