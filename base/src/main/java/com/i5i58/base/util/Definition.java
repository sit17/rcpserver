package com.i5i58.base.util;

import java.nio.charset.Charset;

import io.netty.util.CharsetUtil;

public class Definition {
	public static final Charset defaultCharset = CharsetUtil.UTF_8;

	public static final String WEBSOCKET_PATH = "/websocket";

	public static String CLOSED_BY_REMOTE = "远程主机强迫关闭了一个现有的连接。";
}
