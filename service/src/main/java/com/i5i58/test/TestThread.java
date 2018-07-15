package com.i5i58.test;

public class TestThread implements Runnable {

	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public TestThread(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	@Override
	public void run() {

		System.out.println("Start[ name:" + name + " age:" + age + "]");
		System.out.println("End[ name:" + name + " age:" + age + "]");
	}

}
