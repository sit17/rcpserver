package com.i5i58;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class jsontest {

	public static void main(String[] args) {
		// String str =
		// "{\"code\":200,\"info\":{\"token\":\"9ff1579a495b436e8484591dc8af1727\",\"accid\":\"1a6ba71705fa4ddf89ba893d06e83565\",\"name\":\"10151\"}}";
		// try {
		// RegisterAccountResult rt = new JsonUtils().toObject(str,
		// RegisterAccountResult.class);
		// RegisterAccountInfo rai = rt.getInfo();
		// System.out.println(rai.getAccid());
		// System.out.println(rai.getName());
		// System.out.println(rai.getToken());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// System.out.println(UUID.randomUUID().toString().hashCode());

		StringBuilder sb = new StringBuilder();
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = null;
		try {
			document = saxBuilder.build(new StringReader(
					"<xml><appid><![CDATA[wx880fa74e302e49c6]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1427958602]]></mch_id><nonce_str><![CDATA[acc5efeb4d344408805d9063378fcee2]]></nonce_str><openid><![CDATA[oRGZJwnUw3srA9ae7aiBuhVxSrL0]]></openid><out_trade_no><![CDATA[20170308135838148895271872024760]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[7CEE4EE5B96197E1FB68DF0FB68C123D]]></sign><time_end><![CDATA[20170308135854]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[APP]]></trade_type><transaction_id><![CDATA[4004152001201703082650138993]]></transaction_id></xml>"));
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = e.getValue();
			sb.append(k);
			sb.append(":");
			sb.append(v);
			sb.append("\r\n");
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				// v = XMLUtil.getChildrenText(children);
			}
			// resultMap.put(k, v);
		}
		System.out.println(sb.toString());
	}
}
