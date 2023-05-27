package com.we.service;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.we.dao.AuditDAO;
import com.we.dao.IAuditDAO;
import com.we.dao.ItemDAO;
import com.we.exception.InsufficientFundsException;
import com.we.exception.InvalidItemCodeException;
import com.we.exception.ItemOutOfStockException;
import com.we.model.audit.AuditEvent;
import com.we.model.item.Item;
import com.we.utilities.Change;
import com.we.utilities.Change.ChangeType;
import com.we.utilities.InputUtilities;

public class Service implements IService, Serializable {
	private BigDecimal balance;
	private ItemDAO itemDAO;
	private IAuditDAO auditDAO;

	public Service() {
		this.balance = BigDecimal.ZERO;
		this.auditDAO = new AuditDAO();
		this.itemDAO = new ItemDAO(auditDAO);
		
	}

	@Override
	public void loadingInventory(File filename) {
		Map<Integer, Item> items = itemDAO.loadingInventory(filename);
		this.displayInventoryList(items);
	}

	@Override
	public void displayInventoryList(Map<Integer, Item> items) {
		System.out.println("========================================================");
		System.out.println("Inventory list:");
		System.out.println("========================================================");

		for (Item item:items.values()) {
			System.out.println(item.toString() + " , quantity in Stock : " + item.getQuantity());
		}
		System.out.println("========================================================");
	}

	@Override
	public void displayAvailableItems(Map<Integer,Item> items) {
		System.out.println("Items available:");
		items.values().stream()
		.filter(this::itemIsInStock)
		.forEach(System.out::println);
	}

	@Override
	public void writeInventory(File filename) {
		itemDAO.writeInventoryToFile(filename);
	}

	@Override
	public void topUp() {
		BigDecimal amount = InputUtilities.getInputAsBigDecimal("Amount", "Enter the amount to top up:");
		balance = balance.add(amount);
		System.out.println("Top-up successful. Current balance: $" + balance);
	}



	@Override
	public void purchaseItem() {
		String logMessage = "";
		
		//Check that balance > 0 
		if (balance.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("You have zero balance. Please top up first.");
			return;
		}
		// Fetch items from DAO layer
		Map<Integer,Item> items = itemDAO.getItems();
		
		// display menu
		displayAvailableItems(items);
		
		//purchase
		int itemCode = InputUtilities.getInputAsInteger("Item code" , "Enter the item code :");
		try {
			//validation
			checkItemCodeIsValid(itemCode,items);
			checkItemInStock(itemCode,items);
			checkBalanceSufficient(itemCode,items);
			
			//Fetch item from items (of DAO layer)
			Item item = items.get(itemCode);
			
			//Update balance and quantity in DAO layer
			balance = balance.subtract(item.getCost());
			item.setQuantity(item.getQuantity() - 1);

			System.out.println("Item dispensed: " + item.getName());
			System.out.println("Remaining balance: $" + balance);
			
			//Change return
			int balanceInPennies = balance.multiply(BigDecimal.valueOf(100)).intValueExact();
			Map<ChangeType, Integer> change = Change.calculateChange(balanceInPennies);
			System.out.println("Changes return to customer:");
			change.forEach((denomination, count) -> System.out.println(denomination + ": " + count));
			balance = BigDecimal.ZERO;
			balanceInPennies = 0;
			System.out.println("Your balance after change returned : " + balance);

			//Log the event to the audit log
			logMessage = "Item dispenses: " + item.getName();
			auditDAO.logEvent(logMessage);
		} catch (InvalidItemCodeException e) {
			logMessage = "Invalid item code!" + e.getMessage();
			auditDAO.logException(logMessage);
			System.out.println();
		} catch (ItemOutOfStockException e) {
			logMessage = "Item out of stock!" + e.getMessage();
			auditDAO.logException(logMessage);
			System.out.println(logMessage);
		} catch (InsufficientFundsException e) {
			logMessage = "Insufficient balance. Your current balance is : ";
			auditDAO.logException(logMessage);
			System.out.println(logMessage + balance);
		}
	}

	@Override
	public void checkItemCodeIsValid(int itemCode, Map<Integer,Item> items) throws InvalidItemCodeException {
		if (!items.containsKey(itemCode)) {
			throw new InvalidItemCodeException();
		}
	}

	@Override
	public void checkItemInStock(int itemCode, Map<Integer,Item> items) throws ItemOutOfStockException {
		if (!itemIsInStock(items.get(itemCode))) {
			throw new ItemOutOfStockException();
		}
	}

	@Override
	public void checkBalanceSufficient(int itemCode, Map<Integer,Item> items) throws InsufficientFundsException {
		Item item = items.get(itemCode);
		if (balance.compareTo(item.getCost()) < 0) {
			throw new InsufficientFundsException();
		}
	}

	public boolean itemIsInStock(Item item) {
		return item.getQuantity() > 0;
	}

	@Override
	public void viewAuditLog() {
		List<AuditEvent> auditLog = auditDAO.getAuditLog();
		System.out.println("======Audit Log======");
		auditLog.forEach(System.out::println);
		System.out.println("=====================");
	}
}
