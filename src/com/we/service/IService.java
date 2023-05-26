package com.we.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.we.model.item.Item;

public interface IService {
	void loadingInventory(File filename,Map<Integer,Item> items);
	void viewInventory();
	void writeInventory(File filename);
	void topUp();
	void purchaseItem();
}
