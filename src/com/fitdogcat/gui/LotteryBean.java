package com.fitdogcat.gui;

import java.util.Arrays;


public class LotteryBean {
	@Override
	public String toString() {
		return "LotteryBean [name=" + name + ", number="
				+ Arrays.toString(number) + ", remark=" + remark + "]";
	}
	String name;
	String[] number;
	String remark;
	
	public LotteryBean() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getNumber() {
		return number;
	}
	public void setNumber(String[] number) {
		this.number = number;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
