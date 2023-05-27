package com.we.model.item;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable{
	private int code;
	private String name;
	private BigDecimal cost;
	private int quantity;
	
	public Item(int code,String name, BigDecimal cost, int quantity) {
		super();
		this.code = code;
		this.name = name;
		this.cost = cost;
		this.quantity = quantity;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "code=" + code + ", name=" + name + ", cost=" + cost;
	}

	
	
	
}
