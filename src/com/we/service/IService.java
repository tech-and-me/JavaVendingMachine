package com.we.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.we.exception.InsufficientFundsException;
import com.we.exception.InvalidItemCodeException;
import com.we.exception.ItemOutOfStockException;
import com.we.model.item.Item;

public interface IService {
	void loadingInventory(File filename,Map<Integer,Item> items);
	void displayInventoryList();
	void writeInventory(File filename);
	void topUp();
	void purchaseItem();
	void displayAvailableItems();
	void checkItemCodeIsValid(int itemCode) throws InvalidItemCodeException;
	void checkItemInStock(int itemCode) throws ItemOutOfStockException;
	void checkBalanceSufficient(int itemCode) throws InsufficientFundsException;
}
