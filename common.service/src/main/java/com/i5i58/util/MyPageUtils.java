package com.i5i58.util;

import org.springframework.data.domain.Page;

public class MyPageUtils {

	public static MyPage getMyPage(Page<?> page) {
		MyPage myPage = new MyPage();
		myPage.setContent(page.getContent());
		myPage.setNum(page.getNumber());
		myPage.setPages(page.getTotalPages());
		myPage.setSize(page.getSize());
		myPage.setCount(page.getTotalElements());
		return myPage;
	}
}
