package com.i5i58.yunxin.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.i5i58.yunxin.CheckSumBuilder;

public class VideoCloudSignUtil {

	public static String createSign(SortedMap<Object, Object> parameters, String signKey){
		StringBuffer sb = new StringBuffer();
		Set<Map.Entry<Object, Object>> es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator<Map.Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)) {
				sb.append(k + "=" + v);
			}
			if (it.hasNext()){
				sb.append("&");
			}
		}
		sb.append(signKey);
		System.out.println("字符串拼接后是：" + sb.toString());
		String sign = CheckSumBuilder.getMD5(sb.toString()).toUpperCase();
		return sign;
	}
}
