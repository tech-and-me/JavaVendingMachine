package com.we.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.we.exception.NoItemInventoryException;
import com.we.model.item.Item;

public class ItemDAO implements IItemDAO, Serializable{
	private Map<Integer,Item>items;

	public ItemDAO(File filename, Map<Integer,Item> items) {
        this.items = items;
        loadingInventory(filename);
    }
	

	@Override
	public void addItemToInventory(Item item) {
		items.put(item.getCode(),item);
	}

	@Override
	public void viewInventory() {
		for (Item item : items.values()) {
			System.out.println(item);
		}

	}
	
	@Override
	public void addDefaultItems() {
        this.items.put(100, new Item(100,"Coke", 1.25, 5));
        this.items.put(101, new Item(101,"Chips", 0.75, 10));
        this.items.put(102, new Item(102,"Chocolate", 1.00, 3));
    }

	@Override
	public void loadingInventory(File filename) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;

		try {
			fis = new FileInputStream(filename);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();

			if (obj == null) {
				System.out.println("No items in the file. Starting with default inventory.");
                addDefaultItems();
			} else {
				if (obj instanceof Map) {
					HashMap<?, ?> tempMap = (HashMap<?, ?>) obj;
					boolean isCorrectType = tempMap.values().stream().allMatch(Item.class::isInstance);
					if (isCorrectType) {
						this.items.clear(); // Clear the existing items
						this.items.putAll((Map<Integer, Item>) obj); // Update items with loaded data
						System.out.println("Inventory data read from file successfully!");
					} else {
						System.out.println("Invalid data format. Expected HashMap<Integer, Item>.");
					}
				} else {
					System.out.println("Invalid data format. Expected HashMap.");
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("No items in the file. Starting with default inventory.");
            addDefaultItems();
		} catch (IOException e) {
			System.out.println("Error reading inventory data from file: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Oops! Something went wrong: " + e.getMessage());

		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void writeInventoryToFile(File filename) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream(filename);
			bos = new BufferedOutputStream(fos);
			oos = new ObjectOutputStream(bos);

			oos.writeObject(this.items);

			System.out.println("Inventory data written to file successfully!");
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error writing inventory data to file: " + e.getMessage());
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
