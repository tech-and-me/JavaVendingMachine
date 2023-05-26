package com.we.dao;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.we.model.item.Item;

public interface IItemDAO {
	void addDefaultItems();
	void addItemToInventory(Item item);
	void viewInventory();
	void loadingInventory(File filename);
	void writeInventoryToFile(File filename);
}
