package com.we.service;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;


import com.we.dao.IItemDAO;
import com.we.dao.ItemDAO;
import com.we.exception.InsufficientFundsException;
import com.we.exception.InvalidItemCodeException;
import com.we.exception.ItemOutOfStockException;
import com.we.model.item.Item;
import com.we.utilities.Change;
import com.we.utilities.Change.ChangeType;
import com.we.utilities.InputUtilities;

public class Service implements IService, Serializable {
	private Map<Integer,Item> items; //temporary storage
	private IItemDAO dao;
	private BigDecimal balance;

	public Service(File filename,Map<Integer, Item> items) {
		this.items = items;
		this.dao = new ItemDAO(filename,items);
		this.balance = BigDecimal.ZERO;
	}

	@Override
	public void loadingInventory(File filename, Map<Integer,Item> items) {
		dao.loadingInventory(filename);
		displayInventoryList();
		
	}
	
	@Override
	public void displayInventoryList() {
		System.out.println("========================================================");
		System.out.println("Inventory list:");
		System.out.println("========================================================");
		for (Map.Entry<Integer, Item> entry : this.items.entrySet()) {
	        Item item = entry.getValue();
	        System.out.println(item.toString() + " , quantity in Stock : " + item.getQuantity());
	    }
		System.out.println("========================================================");
	}

	@Override
	public void writeInventory(File filename) {
		dao.writeInventoryToFile(filename);
		
	}
	
	@Override
	public void topUp() {
        BigDecimal amount = InputUtilities.getInputAsBigDecimal("Amount", "Enter the amount to top up:");
        balance = balance.add(amount);
        System.out.println("Top-up successful. Current balance: $" + balance);
	}
	
	@Override
	public void displayAvailableItems() {
	    items.values().stream()
	            .filter(this::itemIsInStock)
	            .forEach(System.out::println);
	}
	
	@Override
	public void purchaseItem() {
		 if (balance.compareTo(BigDecimal.ZERO) <= 0) {
	            System.out.println("You have zero balance. Please top up first.");
	            return;
	        }
	    System.out.println("Items available for purchase:");
	    displayAvailableItems();

	    int itemCode = InputUtilities.getInputAsInteger("Item code" , "Enter the item code :");
	    try {
	        checkItemCodeIsValid(itemCode);
	        checkItemInStock(itemCode);
	        checkBalanceSufficient(itemCode);

	        Item item = items.get(itemCode);
	        balance = balance.subtract(item.getCost());
	        item.setQuantity(item.getQuantity() - 1);

	        System.out.println("Item dispensed: " + item.getName());
	        System.out.println("Remaining balance: $" + balance);
	        
	        int balanceInPennies = balance.multiply(BigDecimal.valueOf(100)).intValueExact();
	        Map<ChangeType, Integer> change = Change.calculateChange(balanceInPennies);
	        System.out.println("Changes return to customer:");
	        change.forEach((denomination, count) -> System.out.println(denomination + ": " + count));
	        balance = BigDecimal.ZERO;
	        balanceInPennies = 0;
	        System.out.println("Your balance after change returned : " + balance);
	    } catch (InvalidItemCodeException e) {
	        System.out.println("Invalid item code!");
	    } catch (ItemOutOfStockException e) {
	        System.out.println("Item out of stock!");
	    } catch (InsufficientFundsException e) {
	        System.out.println("Insufficient balance. Your current balance is : " + balance);
	    }
	}
	
	@Override
	public void checkItemCodeIsValid(int itemCode) throws InvalidItemCodeException {
	    if (!items.containsKey(itemCode)) {
	        throw new InvalidItemCodeException();
	    }
	}

	@Override
	public void checkItemInStock(int itemCode) throws ItemOutOfStockException {
	    if (!itemIsInStock(items.get(itemCode))) {
	        throw new ItemOutOfStockException();
	    }
	}

	@Override
	public void checkBalanceSufficient(int itemCode) throws InsufficientFundsException {
	    Item item = items.get(itemCode);
	    if (balance.compareTo(item.getCost()) < 0) {
	        throw new InsufficientFundsException();
	    }
	}
	
	public boolean itemIsInStock(Item item) {
	    return item.getQuantity() > 0;
	}
}
