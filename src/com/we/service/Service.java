package com.we.service;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.Scanner;

import com.we.dao.IItemDAO;
import com.we.dao.ItemDAO;
import com.we.model.item.Item;

public class Service implements IService, Serializable {
	private Map<Integer,Item> items; //temporary storage
	private IItemDAO dao;
	private double balance;

	public Service(File filename,Map<Integer, Item> items) {
		this.items = items;
		this.dao = new ItemDAO(filename,items);
		this.balance = 0.0;
	}

	@Override
	public void loadingInventory(File filename, Map<Integer,Item> items) {
		dao.loadingInventory(filename);
		System.out.println("Items available:");
		viewInventory();
	}
	
	@Override
	public void viewInventory() {
		for (Map.Entry<Integer, Item> entry : this.items.entrySet()) {
	        Item item = entry.getValue();
	        System.out.println(item);
	    }
	}

	@Override
	public void writeInventory(File filename) {
		dao.writeInventoryToFile(filename);
		
	}
	
	@Override
	public void topUp() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the amount to top up: ");
		double amount = scanner.nextDouble();
		balance += amount;
		System.out.println("Top-up successful. Current balance: $" + balance);
	}
	
	@Override
	public void purchaseItem() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Items available for purchase:");
		for (Item item : items.values()) {
			if (item.getQuantity() > 0) {
				System.out.println(item.getCode() + " - " + item.getName() + " - $" + item.getCost());
			}
		}

		System.out.println("Enter the item code: ");
		int itemCode = scanner.nextInt();
		// Check if item exists and balance is sufficient
		if (items.containsKey(itemCode)) {
			Item item = items.get(itemCode);
			if (balance >= item.getCost()) {
				balance -= item.getCost();
				// Dispense the item
				if (item.getQuantity() > 0) {
					item.setQuantity(item.getQuantity() - 1);
					System.out.println("Item dispensed: " + item.getName());
					System.out.println("Remaining balance: $" + balance);
				} else {
					System.out.println("Item out of stock!");
				}
			} else {
				System.out.println("Insufficient balance. Please top up!");
			}
		} else {
			System.out.println("Invalid item code!");
		}
	}
}
