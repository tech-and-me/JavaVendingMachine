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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.we.exception.ItemOutOfStockException;
import com.we.model.item.Item;

public class ItemDAO implements IItemDAO, Serializable{
	private Map<Integer,Item>items;
	private IAuditDAO auditDAO;

	public ItemDAO(IAuditDAO auditDAO) {
        this.items = new HashMap<>();
        this.auditDAO = auditDAO;
    }
	
	public Map<Integer, Item> getItems() {
		return items;
	}

	public void setItems(Map<Integer, Item> items) {
		this.items = items;
	}


	@Override
	public void addItemToInventory(Item item) {
		items.put(item.getCode(),item);
	}
	
	@Override
	public void addDefaultItems() {
		try {
			this.items.put(100, new Item(100, "Coke", new BigDecimal("1.25"), 2));
		    this.items.put(101, new Item(101, "Chips", new BigDecimal("0.75"), 4));
		    this.items.put(102, new Item(102, "Chocolate", new BigDecimal("1.00"), 1));
		    this.items.put(103, new Item(103, "Water", new BigDecimal("1.00"), 3));
		    this.items.put(106, new Item(106, "Lemon Ice Tea", new BigDecimal("1.75"), 3));
		    this.items.put(107, new Item(107, "Ice Coffee", new BigDecimal("2.00"), 2));
		    auditDAO.logEvent("Default inventory added successfully.");
		}catch(Exception e) {
			String logMessage = "Oooop something went wrong when adding default inventory items";
			System.out.println(logMessage);
			auditDAO.logException(logMessage);
		}
    }

	@Override
	public Map<Integer, Item> loadingInventory(File filename) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		String logMessage = "";

		try {
			fis = new FileInputStream(filename);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();

			if (obj == null) {
				logMessage = "No items in the file. Starting with default inventory.";
				System.out.println(logMessage);
                this.addDefaultItems();
			} else {
				if (obj instanceof Map) {
					HashMap<?, ?> tempMap = (HashMap<?, ?>) obj;
					boolean isCorrectType = tempMap.values().stream().allMatch(Item.class::isInstance);
					if (isCorrectType) {
						this.items.clear(); // Clear the existing items
						this.items.putAll((Map<Integer, Item>) obj); // Update items with loaded data
						logMessage = "Inventory data read from file successfully!";
						System.out.println(logMessage);
					} else {
						logMessage = "Invalid data format. Expected HashMap<Integer, Item>.";
						System.out.println(logMessage);
					}
				} else {
					logMessage = "Invalid data format. Expected HashMap.";
					System.out.println(logMessage);
				}
				auditDAO.logEvent(logMessage);
			}
		} catch (FileNotFoundException e) {
			logMessage = "File not found. Starting with default inventory.";
			addDefaultItems();
			auditDAO.logException(logMessage);
			System.out.println(logMessage);
		} catch (IOException e) {
			logMessage = "Error reading inventory data from file: " + e.getMessage();
			auditDAO.logException(logMessage);
			System.out.println(logMessage);
		} catch (Exception e) {
			logMessage = "Error reading inventory data from file: " + e.getMessage();
			auditDAO.logException(logMessage);
			System.out.println(logMessage);

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
		return items;
	}

	@Override
	public void writeInventoryToFile(File filename) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		
		String logMessage = "";

		try {
			fos = new FileOutputStream(filename);
			bos = new BufferedOutputStream(fos);
			oos = new ObjectOutputStream(bos);

			oos.writeObject(this.items);

			System.out.println("Inventory data written to file successfully!");
			auditDAO.logEvent(logMessage);
		} catch (FileNotFoundException e) {
			logMessage = "File not found: " + e.getMessage();
			System.out.println(logMessage);
			auditDAO.logEvent(logMessage);
		} catch (IOException e) {
			logMessage = "Error writing inventory data to file: " + e.getMessage();
			System.out.println(logMessage);
			auditDAO.logException(logMessage);
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
