package com.i5i58.Videocloud163;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.CheckSumBuilder;
import com.i5i58.yunxin.GetParams;
import com.i5i58.yunxin.PostParams;
import com.i5i58.yunxin.Utils.CreateChannelResult;
import com.i5i58.yunxin.Utils.UpdateAddressResult;
import com.i5i58.yunxin.Utils.UpdateChannelResult;
import com.i5i58.yunxin.Utils.YXResultSet;

/**
 * @ClassName: Videocloud163
 * @author Lee
 * @date 2016年8月19日 下午1:18:34
 * 
 */
public class Videocloud163 {

	public static String appKey = "9816e5b316f24e69bac9934e627b314f";
	public static String appSecret = "e4ed7774cdd24892833a023cf19b28fd";
	public static int Tag = 0;

	public static String httpPost(String url, PostParams... postParams) throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);

			String nonce = "12345";
			String curTime = String.valueOf((new Date()).getTime() / 1000L);
			String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);// 参考
			// System.out.println(curTime);
			// System.out.println(checkSum);
			StringBuffer par = new StringBuffer("");
			String s = "";

			// 设置请求的header
			httpPost.addHeader("AppKey", appKey);
			httpPost.addHeader("Nonce", nonce);
			httpPost.addHeader("CurTime", curTime);
			httpPost.addHeader("CheckSum", checkSum);
			httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

			// 设置请求的参数
			for (PostParams params : postParams) {
				if (!"".equals(params.getValue()) && !"".equals(params.getName())) {
					par.append("\"" + params.getName() + "\":\"" + params.getValue() + "\",");
				}
			}
			if (null != par) {
				s = par.toString().substring(0, par.length() - 1);
			}

			System.out.println(s);
			StringEntity params = new StringEntity("{" + s + "}", Consts.UTF_8);
			httpPost.setEntity(params);
			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				// 打印执行结果
				String retString = EntityUtils.toString(response.getEntity(), "utf-8");
				System.out.println(retString);
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
				return retString;
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}

	public static String httpPostForArray(String url, PostParams... postParams) throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);

			String nonce = "12345";
			String curTime = String.valueOf((new Date()).getTime() / 1000L);
			String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);// 参考

			StringBuffer par = new StringBuffer("");
			String s = "";

			// 设置请求的header
			httpPost.addHeader("AppKey", appKey);
			httpPost.addHeader("Nonce", nonce);
			httpPost.addHeader("CurTime", curTime);
			httpPost.addHeader("CheckSum", checkSum);
			httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

			// 设置请求的参数
			for (PostParams params : postParams) {
				par.append("'" + params.getName() + "':['" + params.getValue() + "'],");
			}
			if (null != par) {
				s = par.toString().substring(0, par.length() - 1);
			}

			StringEntity params = new StringEntity("{" + s + "}", Consts.UTF_8);
			httpPost.setEntity(params);

			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				// 打印执行结果
				String retString = EntityUtils.toString(response.getEntity(), "utf-8");
				System.out.println(retString);
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
				return retString;
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}

	private static String getParameters(Map<String, Object> map) {
		StringBuffer buffer = new StringBuffer();
		if (map == null || map.isEmpty()) {
			return "";
		} else {
			for (String key : map.keySet()) {
				String value = String.valueOf(map.get(key));
				if (buffer.length() < 1) {
					buffer.append(key).append("=").append(value);
				} else {
					buffer.append("&").append(key).append("=").append(value);
				}
			}
		}
		return buffer.toString();
	}

	private static String getResult(HttpURLConnection conn) {
		StringBuffer buffer = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			buffer = new StringBuffer();
			String line = null;
			while (null != (line = reader.readLine())) {
				buffer.append(line);
			}
			reader.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer == null ? "" : buffer.toString();
	}

	public static String doGet(GetParams getParams) {
		String result = "";
		try {
			URL urlGet;
			if (1 == Tag) {
				urlGet = new URL(getParams.getUrl());
			} else {
				urlGet = new URL(getParams.getUrl() + "?" + getParameters(getParams.getMap()));
			}
			HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("x-nos-token", getParams.getToken());
			conn.setDoOutput(true);// 设置可以使用输出流写数据

			result = getResult(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	public static String doPost(GetParams getParams) {
		String result = "";
		try {
			URL urlPost = new URL(getParams.getUrl());
			HttpURLConnection conn = (HttpURLConnection) urlPost.openConnection();
			conn.setRequestMethod("POST");// 设置请求方式
			conn.setDoInput(true);// 设置可以使用输入流读取数�?
			conn.setDoOutput(true);// 设置可以使用输出流写数据
			conn.setUseCaches(false);// 不使用缓�?
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.setRequestProperty("Charset", "utf-8");
			String data = getParameters(getParams.getMap());
			conn.setRequestProperty("Connection", "Keep-Alive");
			// �?
			OutputStream out = conn.getOutputStream();
			out.write(data.getBytes());
			out.flush();// 刷新
			out.close();
			result = getResult(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	// 文件上传
	public static String doPostForUuload(GetParams getParams) {
		String result = "";
		try {
			String nonce = "12345";
			String curTime = String.valueOf((new Date()).getTime() / 1000L);
			URL urlPost;
			if ("false".equals(getParams.getComplete())) {
				urlPost = new URL(getParams.getUrl() + "?" + "offset=" + getParams.getOffset() + "&complete="
						+ getParams.getComplete() + "&version=" + getParams.getVersion() + "&context="
						+ getParams.getContext());
			} else {
				urlPost = new URL(getParams.getUrl() + "?" + "offset=" + getParams.getOffset() + "&complete="
						+ getParams.getComplete() + "&version=" + getParams.getVersion());
			}
			HttpURLConnection conn = (HttpURLConnection) urlPost.openConnection();
			conn.setRequestMethod("POST");// 设置请求方式
			conn.setDoInput(true);// 设置可以使用输入流读取数
			conn.setDoOutput(true);// 设置可以使用输出流写数据
			conn.setUseCaches(false);// 不使用缓
			conn.setRequestProperty("Content-Type", getParams.getType());
			conn.setRequestProperty("CheckSum", CheckSumBuilder.getCheckSum(appSecret, nonce, curTime));
			conn.setRequestProperty("AppKey", "9816e5b316f24e69bac9934e627b314f");
			conn.setRequestProperty("Nonce", nonce);
			conn.setRequestProperty("x-nos-token", getParams.getToken());
			conn.setRequestProperty("CurTime", "1471671897");
			conn.setRequestProperty("Content-Length", getParams.getLength());
			String uri = getParams.getUri();

			File file = new File(uri);
			FileInputStream in = new FileInputStream(uri);
			long len = file.length();
			byte buffer[] = new byte[(int) len];
			in.read(buffer);
			in.close();
			OutputStream out = conn.getOutputStream();
			out.write(buffer);
			out.flush();// 刷新
			out.close();

			result = getResult(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	// ======================================================================================================直播====================================
	/**
	 * 
	 * createChannel 创建频道
	 * 
	 * @author Lee
	 * @param cname
	 *            频道名字（最大长度64个字符）
	 * @param type
	 *            频道类型（0:rtmp；1:hls；2:http）
	 * @return
	 * @throws IOException
	 */
	public static CreateChannelResult createChannel(String cname, String type) throws IOException {
		String ret = httpPost("https://vcloud.163.com/app/channel/create", new PostParams("name", cname),
				new PostParams("type", type));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, CreateChannelResult.class);
	}

	/**
	 * 
	 * updateChannel 修改频道
	 * 
	 * @author Lee
	 * @param cname
	 *            频道名字（最大长度64个字符）
	 * @param cid
	 *            频道ID，32位字符串
	 * @param type
	 *            频道类型（0:rtmp；1:hls；2:http）
	 * @throws IOException
	 */
	public static UpdateChannelResult updateChannel(String cname, String cid, String type) throws IOException {
		String ret = httpPost("https://vcloud.163.com/app/channel/update", new PostParams("name", cname),
				new PostParams("cid", cid), new PostParams("type", type));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, UpdateChannelResult.class);
	}

	/**
	 * 
	 * deleteChannel 删除频道
	 * 
	 * @author Lee
	 * @param cid
	 *            频道ID，32位字符串
	 * @throws IOException
	 */
	public static String deleteChannel(String cid) throws IOException {
		return httpPost("https://vcloud.163.com/app/channel/delete", new PostParams("cid", cid));
	}

	/**
	 * 
	 * channelstatsChannel 获取频道状态
	 * 
	 * @author Lee
	 * @param cid
	 *            频道ID，32位字符串
	 * @return
	 * @throws IOException
	 * 
	 */
	public static YXResultSet channelstatsChannel(String cid) throws IOException {
		String ret = httpPost("https://vcloud.163.com/app/channelstats", new PostParams("cid", cid));
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * getChannellist 获取频道列表
	 * 
	 * @author Lee
	 * @param records
	 *            单页记录数，默认值为10
	 * @param page
	 *            要取第几页，默认值为1
	 * @param ofield
	 *            排序的域，支持的排序域为：ctime（默认）
	 * @param sort
	 *            升序还是降序，1升序，0降序，默认为desc
	 * @return
	 * @throws IOException
	 * @return String
	 */
	public static String getChannellist(String records, String page, String ofield, String sort) throws IOException {
		return httpPost("https://vcloud.163.com/app/channellist", new PostParams("records", records),
				new PostParams("pnum", page), new PostParams("ofield", ofield), new PostParams("sort", sort));
	}

	/**
	 * 
	 * updateAddress 重新获取推流地址
	 * 
	 * @author Lee
	 * @param cid
	 *            频道ID，32位字符串
	 * @return
	 * @throws IOException
	 */
	public static UpdateAddressResult updateAddress(String cid) throws IOException {
		String ret = httpPost("https://vcloud.163.com/app/address", new PostParams("cid", cid));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, UpdateAddressResult.class);
	}

	/**
	 * 
	 * setAlwaysRecord 设置频道为录制状态
	 * 
	 * @author Lee
	 * @param cid
	 *            频道ID，32位字符串
	 * @param needRecord
	 *            状态：1-开启录制； 0-关闭录制 @param format 格式：1-flv； 0-mp4
	 * @param time
	 *            时间（录制切片时长(分钟)，默认120分钟）
	 * @param filename
	 *            录制后文件名，格式为filename_YYYYMMDD-HHmmssYYYYMMDD-HHmmss,
	 *            文件名录制起始时间（年月日时分秒)-录制结束时间（年月日时分秒)
	 * @return
	 * @throws IOException
	 */
	public static String setAlwaysRecord(String cid, String needRecord, String format, String time, String filename)
			throws IOException {
		return httpPost("https://vcloud.163.com/app/channel/setAlwaysRecord", new PostParams("cid", cid),
				new PostParams("needRecord", needRecord), new PostParams("format", format),
				new PostParams("duration", time), new PostParams("filename", filename));
	}

	/**
	 * 
	 * pauseChannel 暂停频道
	 * 
	 * @author Lee
	 * @param cid
	 *            频道ID，32位字符串
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet pauseChannel(String cid) throws IOException {
		String ret = httpPost("https://vcloud.163.com/app/channel/pause", new PostParams("cid", cid));
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * pauseChannelList 批量暂停频道
	 * 
	 * @author Lee
	 * 
	 * @param cids
	 *            频道ID列表，jsonArray
	 * @return
	 * @throws IOException
	 * @return String
	 */
	public static String pauseChannelList(String cids) throws IOException {
		return httpPost("https://vcloud.163.com/app/channellist/pause", new PostParams("cidList", cids));
	}

	/**
	 * 
	 * resumeChannel 恢复频道
	 * 
	 * @author Lee
	 * @param cid
	 *            频道ID，32位字符串
	 * @return
	 * @throws IOException
	 */

	public static YXResultSet resumeChannel(String cid) throws IOException {
		String ret = httpPost("https://vcloud.163.com/app/channel/resume", new PostParams("cid", cid));
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * pauseChannelList 批量恢复频道
	 * 
	 * @author Lee
	 * @param cids频道ID列表，jsonArray
	 * @return
	 * @throws IOException
	 * @return String
	 */
	public static String resumeChannelList(String cids) throws IOException {
		return httpPost("https://vcloud.163.com/app/channe  llist/resume", new PostParams("cidList", cids));
	}

	/**
	 * 
	 * getVideoList 获取录制视频文件列表
	 * 
	 * @author Lee
	 * @param cid
	 *            频道ID，32位字符串
	 * @return
	 * @throws IOException
	 */
	public static String getVideoList(String cid) throws IOException {
		return httpPost("https://vcloud.163.com/app/videolist", new PostParams("cid", cid));
	}

	// ======================================================================================================点播====================================
	// 视频分类管理==========================================================================================================================
	/**
	 * 
	 * createFileClassification 创建视频分类 @author Lee
	 * 
	 * @param name
	 *            视频分类的名称
	 * @param msg
	 *            视频分类的描述信息
	 * @return
	 * @throws IOException
	 */
	public static String createFileClassification(String name, String msg) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/type/create", new PostParams("typeName", name),
				new PostParams("description", msg));
	}

	/**
	 * 
	 * getFileClassification 获取视频分类 @author Lee
	 * 
	 * @param tid
	 *            视频分类的ID
	 * @return
	 * @throws IOException
	 */
	public static String getFileClassification(String tid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/type/get", new PostParams("typeId", tid));
	}

	/**
	 * 
	 * getFileClassificationList 获取视频分类列表 @author Lee
	 * 
	 * @param page
	 *            获取视频分类列表分页后的索引
	 * @param size
	 *            获取视频分类列表一页的记录数（若为-1，表示不用分页）
	 * @return
	 * @throws IOException
	 */
	public static String getFileClassificationList(String page, String size) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/type/list", new PostParams("currentPage", page),
				new PostParams("pageSize", size));
	}

	/**
	 * 
	 * updateFileClassification 修改视频分类 @author Lee
	 * 
	 * @param tid
	 *            视频分类的ID
	 * @param tname
	 *            视频分类的名称
	 * @param tmsg
	 *            视频分类的描述信息
	 * @return
	 * @throws IOException
	 */
	public static String updateFileClassification(String tid, String tname, String tmsg) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/type/update", new PostParams("typeId", tid),
				new PostParams("typeName", tname), new PostParams("description", tmsg));
	}

	/**
	 * 
	 * deleteFileClassification 删除视频分类 @author Lee
	 * 
	 * @param tid
	 *            视频分类ID
	 * @return
	 * @throws IOException
	 */
	public static String deleteFileClassification(String tid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/type/typeDelete", new PostParams("typeId", tid));
	}

	/**
	 * 
	 * setFileClassification 设置视频的分类 @author Lee
	 * 
	 * @param vid
	 *            视频ID @param tid 视频分类的ID
	 * @return
	 * @throws IOException
	 * @return String
	 */
	public static String setFileClassification(String vid, String tid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/type/set", new PostParams("vid", vid),
				new PostParams("typeId", tid));
	}

	// 视频转码模板管理==========================================================================================================================
	/**
	 * 
	 * createVideoTranscodingTemplate 创建视频转码模板 @author Lee
	 * 
	 * @param pname
	 *            视频转码模板的名称
	 * @param sdMp4
	 *            标清Mp4格式（1表示选择，0表示不选择）
	 * @param hdMp4
	 *            高清Mp4格式（1表示选择，0表示不选择）
	 * @param shdMp4
	 *            超清Mp4格式（1表示选择，0表示不选择）
	 * @param sdFlv
	 *            标清Flv格式（1表示选择，0表示不选择）
	 * @param hdFlv
	 *            高清Flv格式（1表示选择，0表示不选择）
	 * @param shdFlv
	 *            超清Flv格式（1表示选择，0表示不选择）
	 * @param sdHls
	 *            标清Hls格式（1表示选择，0表示不选择）
	 * @param hdHls
	 *            高清Hls格式（1表示选择，0表示不选择）
	 * @param shdHls
	 *            超清Hls格式（1表示选择，0表示不选择）
	 * @return
	 * @throws IOException
	 * @return String
	 */
	public static String createVideoTranscodingTemplate(String pname, String sdMp4, String hdMp4, String shdMp4,
			String sdFlv, String hdFlv, String shdFlv, String sdHls, String hdHls, String shdHls) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/preset/create", new PostParams("presetName", pname),
				new PostParams("sdMp4", sdMp4), new PostParams("hdMp4", hdMp4), new PostParams("shdMp4", shdMp4),
				new PostParams("sdFlv", sdFlv), new PostParams("hdFlv", hdFlv), new PostParams("shdFlv", shdFlv),
				new PostParams("sdHls", sdHls), new PostParams("hdHls", hdHls), new PostParams("shdHls", shdHls));
	}

	/**
	 * 
	 * getVideoTranscodingTemplate 获取视频转码模板
	 * 
	 * @author Lee
	 * @param pid
	 * @return
	 * @throws IOException
	 */
	public static String getVideoTranscodingTemplate(String pid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/preset/get", new PostParams("presetId", pid));
	}

	/**
	 * 
	 * getVideoTranscodingTemplateList 获取视频转码模板列表
	 * 
	 * @author Lee
	 * @param page
	 *            获取视频转码模板列表分页后的索引
	 * @param size
	 *            获取视频转码模板列表一页的记录数（若为-1，表示不用分页）
	 * @throws IOException
	 * @return String
	 */
	public static String updateVideoTranscodingTemplate(String page, String size) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/preset/update", new PostParams("currentPage", page),
				new PostParams("pageSize", size));
	}

	/**
	 * 
	 * updateVideoTranscodingTemplate 修改视频转码模板
	 * 
	 * @author Lee
	 * 
	 * @param pid
	 *            视频转码模板的Id
	 * @param pname
	 *            视频转码模板的名称
	 * @param pname
	 *            视频转码模板的名称
	 * @param sdMp4
	 *            标清Mp4格式（1表示选择，0表示不选择）
	 * @param hdMp4
	 *            高清Mp4格式（1表示选择，0表示不选择）
	 * @param shdMp4
	 *            超清Mp4格式（1表示选择，0表示不选择）
	 * @param sdFlv
	 *            标清Flv格式（1表示选择，0表示不选择）
	 * @param hdFlv
	 *            高清Flv格式（1表示选择，0表示不选择）
	 * @param shdFlv
	 *            超清Flv格式（1表示选择，0表示不选择）
	 * @param sdHls
	 *            标清Hls格式（1表示选择，0表示不选择）
	 * @param hdHls
	 *            高清Hls格式（1表示选择，0表示不选择）
	 * @param shdHls
	 *            超清Hls格式（1表示选择，0表示不选择）
	 * @throws IOException
	 * @return String
	 */
	public static String updateVideoTranscodingTemplate(String pid, String pname, String sdMp4, String hdMp4,
			String shdMp4, String sdFlv, String hdFlv, String shdFlv, String sdHls, String hdHls, String shdHls)
			throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/preset/update", new PostParams("presetId", pid),
				new PostParams("presetName", pname), new PostParams("sdMp4", sdMp4), new PostParams("hdMp4", hdMp4),
				new PostParams("shdMp4", shdMp4), new PostParams("sdFlv", sdFlv), new PostParams("hdFlv", hdFlv),
				new PostParams("shdFlv", shdFlv), new PostParams("sdHls", sdHls), new PostParams("hdHls", hdHls),
				new PostParams("shdHls", shdHls));
	}

	/**
	 * 
	 * deleteVideoTranscodingTemplate 删除视频转码模板 @author Lee
	 * 
	 * @param pid
	 *            视频转码模板的Id
	 * @return
	 * @throws IOException
	 */
	public static String deleteVideoTranscodingTemplate(String pid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/preset/presetDelete", new PostParams("presetId", pid));
	}

	// 视频水印管理==========================================================================================================================
	/**
	 * 
	 * createVideoWatermarkTemplate 创建视频水印模板 @author Lee
	 * 
	 * @param wname
	 *            视频水印的名称
	 * @param wid
	 *            视频水印图片的ID (?)
	 * @param wmsg
	 *            视频水印的描述信息
	 * @param wxy
	 *            视频水印左上角的坐标，其值为相对值，比如：8%_6%表示在视频左上角的偏右8%视频宽度、偏下6%视频高度的位置。如果不选填，默认设置为5%_5%
	 * @param whw
	 *            视频水印的长宽，其值为相对值，比如：10%x15%表示长为视频宽度的10%，宽为视频宽度的15%。如果不选填，默认设置为不缩放或拉伸
	 * @return
	 * @throws IOException
	 */
	public static String createVideoWatermarkTemplate(String wname, String wid, String wmsg, String wxy, String whw)
			throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/watermark/create", new PostParams("watermarkName", wname),
				new PostParams("imageId", wid), new PostParams("description", wmsg), new PostParams("coordinate", wxy),
				new PostParams("scale", whw));
	}

	/**
	 * 
	 * getVideoWatermarkTemplate 获取视频水印模板 @author Lee
	 * 
	 * @param wid
	 *            视频水印模板Id
	 * @return
	 * @throws IOException
	 */
	public static String getVideoWatermarkTemplate(String wid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/watermark/get", new PostParams("watermarkId", wid));
	}

	/**
	 * 
	 * getVideoWatermarkTemplateList 获取视频水印模板列表
	 * 
	 * @author Lee
	 * @param page
	 *            获取视频水印模板列表分页后的索引
	 * @param size
	 *            获取视频水印模板列表一页的记录数（若为-1，表示不用分页）
	 * @throws IOException
	 * @return String
	 */
	public static String getVideoWatermarkTemplateList(String page, String size) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/watermark/list", new PostParams("currentPage", page),
				new PostParams("pageSize", size));
	}

	/**
	 * 
	 * updateVideoWatermarkTemplate 改视频水印模板 @author Lee
	 * 
	 * @param wid
	 *            视频水印模板Id
	 * @param wname
	 *            视频水印的名称
	 * @param wmsg
	 *            视频水印的描述信息
	 * @param coordinate
	 *            视频水印左上角的坐标，其值为相对值，比如：8%_6%表示在视频左上角的偏右8%视频宽度、偏下6%视频高度的位置。如果不选填，默认设置为5%_5%
	 * @param scale
	 *            视频水印的长宽，其值为相对值，比如：10%x15%表示长为视频宽度的10%，宽为视频宽度的15%。如果不选填，默认设置为不缩放或拉伸
	 * @return
	 * @throws IOException
	 */
	public static String updateVideoWatermarkTemplate(String wid, String wname, String wmsg, String coordinate,
			String scale) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/watermark/update", new PostParams("watermarkId", wid),
				new PostParams("watermarkName", wname), new PostParams("description", wmsg),
				new PostParams("coordinate", coordinate), new PostParams("scale", scale));
	}

	/**
	 * 
	 * deleteVideoWatermarkTemplate 删除视频水印模板
	 * 
	 * @author Lee
	 * @param wid
	 *            视频水印模板ID
	 * @return
	 * @throws IOException
	 */
	public static String deleteVideoWatermarkTemplate(String wid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/watermark/delete", new PostParams("watermarkId", wid));
	}

	// 视频文件管理==========================================================================================================================

	/**
	 * 
	 * getVideoMsg 获取视频文件信息
	 * 
	 * @author Lee
	 * @param vid
	 *            视频ID
	 * @return
	 * @throws IOException
	 */
	public static String getVideoMsg(String vid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/video/get", new PostParams("vid", vid));
	}

	/**
	 * 
	 * getVideoList 获取视频文件信息列表
	 * 
	 * @author Lee
	 * @param page
	 *            获取视频列表分页后的索引
	 * @param size
	 *            获取视频列表一页的记录数（若为-1，表示不用分页）
	 * @param status
	 *            根据视频状态过滤选择（0表示获取所有状态视频，10表示初始，20表示失败，30表示处理中，40表示成功，50表示屏蔽）
	 * @param type
	 *            根据视频分类过滤选择（0表示获取所有分类视频）
	 * @throws IOException
	 * @return String
	 */
	public static String getVideoList(String page, String size, String status, String type) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/video/list", new PostParams("currentPage", page),
				new PostParams("pageSize", size), new PostParams("status", status), new PostParams("type", type));
	}

	/**
	 * 
	 * editVideoMsg 视频文件信息编辑
	 * 
	 * @author Lee
	 * @param vid
	 *            视频ID
	 * @param vname
	 *            视频的名称
	 * @param vtype
	 *            视频分类ID
	 * @param vmsg
	 *            视频的描述信息
	 * @return
	 * @throws IOException
	 */
	public static String editVideoMsg(String vid, String vname, String vtype, String vmsg) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/video/edit", new PostParams("vid", vid),
				new PostParams("videoName", vname), new PostParams("typeId", vtype),
				new PostParams("description", vmsg));
	}

	/**
	 * 
	 * delete_singleVideo 删除单个转码输出视频
	 * 
	 * @author Lee
	 * 
	 * @param vid
	 *            视频ID
	 * @param vtype
	 *            视频转码格式（1表示标清mp4，2表示高清mp4，3表示超清mp4，4表示标清flv，5表示高清flv，6表示超清flv，7表示标清hls，8表示高清hls，9表示超清hls）
	 * @return
	 * @throws IOException
	 */
	public static String delete_singleVideo(String vid, String vtype) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/video/delete_single", new PostParams("vid", vid),
				new PostParams("style", vtype));
	}

	/**
	 * 
	 * videoDelete 删除视频文件
	 * 
	 * @author Lee
	 * @param vid
	 *            视频ID
	 * @return
	 * @throws IOException
	 */
	public static String videoDelete(String vid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/video/videoDelete", new PostParams("vid", vid));
	}

	/**
	 * 
	 * videoDisable 视频屏蔽
	 * 
	 * @author Lee
	 * @param vid
	 *            视频ID
	 * @return
	 * @throws IOException
	 */
	public static String videoDisable(String vid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/video/videoDisable", new PostParams("vid", vid));
	}

	/**
	 * 
	 * videoRecover 视频恢复
	 * 
	 * @author Lee
	 * @param vid
	 *            视频ID
	 * @return
	 * @throws IOException
	 */
	public static String videoRecover(String vid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/video/videoRecover", new PostParams("vid", vid));
	}

	// 文件上传==========================================================================================================================
	/**
	 * 
	 * initUpload 文件上传初始化
	 * 
	 * @author Lee
	 * @param ofname
	 *            上传文件的原始名称（包含后缀名）
	 * @param ufname
	 *            用户命名的上传文件名称
	 * @param tid
	 *            视频所属的类别ID（不填写为默认分类
	 * @param pid
	 *            视频所需转码模板ID（不填写为默认模板，默认模板不进行转码）
	 * @param url
	 *            转码成功后回调客户端的URL地址（需标准http格式）（不能死空字符串）
	 * @param vmsg
	 *            上传视频的描述信息
	 * @param wid
	 *            视频水印ID（不填写为不添加水印，如果选择，请务必在水印管理中提前完成水印图片的上传和参数的配置；且必需设置prestId字段，且presetId字段不为默认模板）
	 * @return
	 * @throws IOException
	 */
	public static String initUpload(String ofname, String ufname, String tid, String pid, String url, String vmsg,
			String wid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/upload/init", new PostParams("originFileName", ofname),
				new PostParams("userFileName", ufname), new PostParams("typeId", tid), new PostParams("presetId", pid),
				new PostParams("callbackUrl", url), new PostParams("description", vmsg),
				new PostParams("watermarkId", wid));
	}

	/**
	 * 
	 * getUploadUrl 获取上传加速节点地址
	 * 
	 * @author Lee
	 * @param bucket
	 *            存储上传文件的桶名，可在视频上传初始化接口的返回参数bucket获取
	 * @return
	 * @throws IOException
	 */
	public static String getUploadUrl(String bucket) throws IOException {
		GetParams getParams = new GetParams();
		getParams.setUrl("http://wanproxy.127.net/lbs");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", 1.0);
		map.put("bucketname", bucket);
		getParams.setMap(map);
		return doGet(getParams);
	}

	/**
	 * 
	 * uploadFiles 文件数据上传
	 * 
	 * @author Lee
	 * @param bucket
	 *            存储对象的桶名
	 * @param oname
	 *            生成的唯一对象名
	 * @param token
	 *            请求头参数，上传token
	 * @param clenth
	 *            请求头参数，当前片的内容长度，单位：字节（Byte）。Content-Length合法值是[0~4M]，
	 * @param ctype
	 *            请求头参数，标准http头。表示请求内容的类型，比如：image/jpeg。 仅第一次上传生效，续传不生效
	 * @param md5
	 *            请求头参数，文件内容md5值
	 * @param offset
	 *            存储对象的桶名
	 * @param complete
	 *            是否为最后一块数据。合法值：true/false
	 * @param version
	 *            http api版本号。这里是固定值1.0
	 * @param context
	 *            上传上下文。本字段是只能被上传服务器解读使用的不透明字段，上传端不应修改其内容。用户不带此参数表示是第一次上传，
	 * @return
	 * @throws IOException
	 */
	public static String uploadFiles(String bucket, String oname, String token, String clenth, String ctype, String md5,
			String offset, String complete, String version, String context, String uri) throws IOException {

		GetParams getParams = new GetParams();
		getParams.setUrl("http://223.252.216.48/" + bucket + "/" + oname);
		getParams.setToken(token);
		getParams.setLength(clenth);
		getParams.setType(ctype);
		getParams.setMD5(md5);
		getParams.setUri(uri);
		getParams.setOffset(offset);
		getParams.setComplete(complete);
		getParams.setVersion(version);
		getParams.setContext(context);
		return doPostForUuload(getParams);
	}

	/**
	 * 
	 * breakpointUuload 断点续传查询
	 * 
	 * @author Lee
	 * @param token
	 * @param bucket
	 * @param oname
	 * @param context
	 * @param version
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static String breakpointUuload(String token, String bucket, String oname, String context, String version)
			throws IOException {
		GetParams getParams = new GetParams();
		getParams.setUrl("http://223.252.216.48/" + bucket + "/" + oname + "?uploadContext" + "&context=" + context
				+ "&version=" + version);
		Tag = 1;
		getParams.setToken(token);
		return doGet(getParams);
	}

	/**
	 * 
	 * overUpload 上传完成根据对象名查询视频或水印图片主ID
	 * 
	 * @author Lee
	 * 
	 * @param oname
	 *            上传文件的对象名列表
	 * @return
	 * @throws IOException
	 */
	public static String overUpload(String oname) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/snapshot/create", new PostParams("objectNames", oname));
	}

	// 视频截图==========================================================================================================================
	/**
	 * 
	 * videoSnapshot 获取视频截图地址
	 * 
	 * @author Lee
	 * @param vid
	 *            视频ID
	 * @param size
	 *            截图尺寸，包含以下几种值：1表示640X360，2表示400X300，3表示320X180，4表示200X150
	 * @param offset
	 *            截图偏移，包含以下几种值：0表示视频第一帧，1表示时间轴10%位置，2表示时间轴20%位置，3表示时间轴30%位置，
	 *            4表示时间轴40%位置，5表示时间轴50%位置，6表示时间轴60%位置，
	 *            7表示时间轴70%位置，8表示时间轴80%位置，9表示时间轴90%位置
	 * @return
	 * @throws IOException
	 */
	public static String videoSnapshotCreate(String vid, String size, String offset) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/snapshot/create", new PostParams("vid", vid),
				new PostParams("size", size), new PostParams("offset", offset));
	}

	/**
	 * 
	 * videoSnapshotSet 设置视频封面
	 * 
	 * @author Lee
	 * @param vid
	 *            视频ID
	 * @param type
	 *            封面设置方法：1表示使用截图URL，2表示使用本地上传图片
	 * @param url
	 *            type值为1，则代表截图URL；type值为2，则代表本地图片路径
	 * @param data
	 *            type值为2时，需填写，代表本地图片数据的base64编码字符串数据
	 * @return
	 * @throws IOException
	 */
	public static String videoSnapshotSet(String vid, String type, String url, String data) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/snapshot/set", new PostParams("vid", vid),
				new PostParams("type", type), new PostParams("path", url), new PostParams("data", data));
	}

	// 视频转码==========================================================================================================================
	/**
	 * 
	 * resetMultiVideo 对视频文件转码
	 * 
	 * @author Lee
	 * @param vids
	 *            多个视频ID组成的列表(List)
	 * @param pid
	 *            转码模板ID
	 * @param wid
	 *            视频水印ID（不填写为不添加水印，且若填写，presetId值不能为默认模板）
	 * @return
	 * @throws IOException
	 */
	public static String resetMultiVideo(String vids, String pid, String wid) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/transcode/resetmulti", new PostParams("vids", vids),
				new PostParams("presetId", pid), new PostParams("watermarkId", wid));
	}

	/**
	 * 
	 * transcodeVideos 多视频无模板转码
	 * 
	 * @author Lee
	 * @param vids
	 *            多个视频ID组成的列表
	 * @param sdMp4
	 *            标清Mp4格式（1表示选择，0表示不选择）
	 * @param hdMp4
	 *            高清Mp4格式（1表示选择，0表示不选择）
	 * @param shdMp4
	 *            超清Mp4格式（1表示选择，0表示不选择）
	 * @param sdFlv
	 *            标清Flv格式（1表示选择，0表示不选择）
	 * @param hdFlv
	 *            高清Flv格式（1表示选择，0表示不选择）
	 * @param shdFlv
	 *            超清Flv格式（1表示选择，0表示不选择）
	 * @param sdHls
	 *            标清Hls格式（1表示选择，0表示不选择）
	 * @param hdHls
	 *            高清Hls格式（1表示选择，0表示不选择）
	 * @param shdHls
	 *            超清Hls格式（1表示选择，0表示不选择）
	 * @return
	 * @throws IOException
	 */
	public static String transcodeVideos(String vids, String sdMp4, String hdMp4, String shdMp4, String sdFlv,
			String hdFlv, String shdFlv, String sdHls, String hdHls, String shdHls) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/transcode", new PostParams("vids", vids),
				new PostParams("sdMp4", sdMp4), new PostParams("hdMp4", hdMp4), new PostParams("shdMp4", shdMp4),
				new PostParams("sdFlv", sdFlv), new PostParams("hdFlv", hdFlv), new PostParams("shdFlv", shdFlv),
				new PostParams("sdHls", sdHls), new PostParams("hdHls", hdHls), new PostParams("shdHls", shdHls));
	}

	/**
	 * 
	 * setCallback 设置回调地址接口
	 * 
	 * @author Lee
	 * @param url
	 *            转码成功后回调客户端的URL地址
	 * @return
	 * @throws IOException
	 */
	public static String setCallback(String url) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/transcode/setcallback", new PostParams("callbackUrl", url));
	}

	// 数据查询==========================================================================================================================
	/**
	 * 
	 * flowStatistics 流量统计查询
	 * 
	 * @author Lee
	 * @param str
	 *            需要查询的开始时间戳（单位：毫秒）
	 * @param end
	 *            需要查询的结束时间戳（单位：毫秒）
	 * @param page
	 *            获取流量数据列表分页后的索引
	 * @param size
	 *            获取流量数据列表一页的记录数（若为-1，表示不用分页）
	 * @return
	 * @throws IOException
	 */
	public static String flowStatistics(String str, String end, String page, String size) throws IOException {
		return httpPost("https://vcloud.163.com/app/vod/stats/flow", new PostParams("startDate", str),
				new PostParams("endDate", end), new PostParams("currentPage", page), new PostParams("pageSize", size));
	}

	/**
	 * 
	 * bandStatistics 带宽统计查询
	 * 
	 * @author Lee @param str 需要查询的开始时间戳（单位：毫秒）
	 * @param end
	 *            需要查询的结束时间戳（单位：毫秒）
	 * @param page
	 *            获取流量数据列表分页后的索引
	 * @param size
	 *            获取流量数据列表一页的记录数（若为-1，表示不用分页）
	 * @return
	 * @throws IOException
	 */
	public static String bandStatistics(String str, String end, String page, String size) throws IOException {
		return httpPostForArray("https://vcloud.163.com/app/vod/stats/band", new PostParams("startDate", str),
				new PostParams("endDate", end), new PostParams("currentPage", page), new PostParams("pageSize", size));
	}

	/**
	 * 
	 * storageStatistics 带宽统计查询
	 * 
	 * @author Lee
	 * @param str
	 *            需要查询的开始时间戳（单位：毫秒）
	 * @param end
	 *            需要查询的结束时间戳（单位：毫秒）
	 * @param page
	 *            获取流量数据列表分页后的索引
	 * @param size
	 *            获取流量数据列表一页的记录数（若为-1，表示不用分页）
	 * @return
	 * @throws IOException
	 */
	public static String storageStatistics(String str, String end, String page, String size) throws IOException {
		return httpPostForArray("https://vcloud.163.com/app/vod/stats/storage", new PostParams("startDate", str),
				new PostParams("endDate", end), new PostParams("currentPage", page), new PostParams("pageSize", size));
	}
	
	/**
	 * 设置直播频道回调
	 * 
	 * @author songfl
	 * @throws IOException 
	 * */
	public static YXResultSet setChStatusCallback(String chStatusClk) throws IOException{
		String ret = httpPost("https://vcloud.163.com/app/chstatus/setcallback", new PostParams("chStatusClk", chStatusClk));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}
	/**
	 * 设置回调的加签秘钥
	 * @author songfl
	 * @param signKey
	 * @return
	 * @throws IOException
	 * */
	public static YXResultSet setSignKey(String signKey) throws IOException {
		String ret = httpPost("https://vcloud.163.com/app/callback/setSignKey", new PostParams("signKey", signKey));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}
}