package com.we.model.item;

import java.io.Serializable;

public class Item implements Serializable{
	private int code;
	private String name;
	private double cost;
	private int quantity;
	
	public Item(int code,String name, double cost, int quantity) {
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

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
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
		return "Item [name=" + name + ", cost=" + cost + ", quantity=" + quantity + "]";
	}
	
	
}
