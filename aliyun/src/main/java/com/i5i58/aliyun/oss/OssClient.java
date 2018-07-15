package com.i5i58.aliyun.oss;

import java.net.URL;
import java.util.Date;

import com.aliyun.oss.OSSClient;

public class OssClient {
	String accessKeyId = "78xKgz5yZPGMjdtv";
	String accessKeySecret = "PzsRMoiU4XsLFVURtNmi3jaxVeFoLT";
	// 以杭州为例
	String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
	String bucketName = "i5i58test";
	String key = "Gifts/1 (1).png";

	OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

	public String generatePresignedUrl() {
		Date expiration = new Date(new Date().getTime() + 3600 * 1000);

		// 生成URL
		URL url = client.generatePresignedUrl(bucketName, key, expiration);
		return url.toString();
	}
}
