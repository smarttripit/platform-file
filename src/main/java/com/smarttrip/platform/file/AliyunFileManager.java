/**
 * Date:2015年9月29日上午10:50:34
 * Copyright (c) 2015, songjiesdnu@163.com All Rights Reserved.
 */
package com.smarttrip.platform.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.smarttrip.platform.util.UUIDUtils;

/**
 * Function: OSS文件上传和下载. <br/>
 * date: 2015年9月29日 上午10:50:34 <br/>
 *
 * @author songjiesdnu@163.com
 */
public class AliyunFileManager {
	private Logger logger = LoggerFactory.getLogger(AliyunFileManager.class);
	
	private String accessKeyId;
	private String accessKeySecret;
	private String endpoint;
	private String bucketName;
	
	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	private OSSClient getOSSClient(){
		return new OSSClient(endpoint,accessKeyId, accessKeySecret);
	}
	
	/**
	 * 
	 * @author songjiesdnu@163.com
	 * @param filename：文件名称
	 * @param content：文件的内容
	 * @return 文件在OSS中的唯一标识，即fileId
	 */
	public String upload(String filename, byte[] content){
		logger.debug("upload file to OSS--start");
		if(content == null  ||  content.length == 0){
			throw new IllegalArgumentException("文件内容不能为空");
		}
		OSSClient client = this.getOSSClient();
		String key = UUIDUtils.getUUID();
		InputStream is = new ByteArrayInputStream(content);
		
		ObjectMetadata meta = new ObjectMetadata();
	    meta.setContentLength(content.length);//必须设置ContentLength
	    Map<String, String> userMetadata = new HashMap<String, String>();
	    if(filename == null  ||  filename.equals("")){
	    	filename = key;
	    }
	    logger.debug("filename:" + filename);
	    userMetadata.put("filename", filename);
	    meta.setUserMetadata(userMetadata);
	    
	    client.putObject(bucketName, key, is, meta);
	    logger.debug("upload file to OSS--end");
	    return key;
	}
	
	/**
	 * 从OSS上现在文件
	 * @author songjiesdnu@163.com
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public FileInfo download(String fileId) throws IOException{
		logger.debug("download file from OSS--start");
		if(fileId == null  ||  fileId.equals("")){
			throw new IllegalArgumentException("fileId不能为null或者空字符串");
		}
		OSSClient client = this.getOSSClient();
		OSSObject ossObject = client.getObject(bucketName, fileId);
		FileInfo fileInfo = new FileInfo();
		InputStream is = ossObject.getObjectContent();
		byte[] content = IOUtils.readStreamAsByteArray(is);
		checkContent(content);
		ObjectMetadata objectMetadata = ossObject.getObjectMetadata();
		String fileName = objectMetadata.getUserMetadata().get("filename");
		fileInfo.setFileContent(content);
		fileInfo.setFileName(fileName);
		
		logger.debug("download file from OSS--end");
		return fileInfo;
	}
	
	private void checkContent(byte[] content){
		boolean exist = true;
		if(content == null){
			exist = false;
		}else{
			if(content.length == 3){
				if(new String(content).equals("404")){
					exist = false;
				}
			}
		}
		if(!exist){
			throw new IllegalArgumentException("文件不存在");
		}
	}
}