package aliyun;

import com.i5i58.aliyun.oss.OssClient;

public class Test {

	public static void main(String[] args)
	{
		OssClient client = new OssClient();
		System.out.println(client.generatePresignedUrl());
	}
}
