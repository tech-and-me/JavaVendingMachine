package com.we.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.we.exception.InsufficientFundsException;
import com.we.exception.InvalidItemCodeException;
import com.we.exception.ItemOutOfStockException;
import com.we.model.item.Item;

public interface IService {
	void loadingInventory(File filename);
	void writeInventory(File filename);
	void displayInventoryList(Map<Integer, Item> items);
	void displayAvailableItems(Map<Integer,Item> items);
	void topUp();
	void purchaseItem();
	void checkItemCodeIsValid(int itemCode, Map<Integer,Item> items) throws InvalidItemCodeException;
	void checkItemInStock(int itemCode, Map<Integer,Item> items) throws ItemOutOfStockException;
	void checkBalanceSufficient(int itemCode, Map<Integer,Item> items) throws InsufficientFundsException;
	void viewAuditLog();
}
