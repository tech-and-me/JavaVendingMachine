package com.we.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import com.we.model.item.Item;
import com.we.service.IService;
import com.we.service.Service;
import com.we.utilities.InputUtilities;

public class Controller {
	public static void main(String[] args) {
		File inventoryFile = new File("C:\\C353\\InventoryRecords.txt");
		HashMap<Integer, Item> items = new HashMap<>(); //temp item list
		IService service = new Service(inventoryFile,items);

		service.loadingInventory(inventoryFile,items);
		
		boolean readyToExit = false;
		Scanner scanner = new Scanner(System.in);
		while(!readyToExit) {
			System.out.println("-------------------------");
			System.out.println("Vendng Machine Application");
			System.out.println("-------------------------");
			System.out.println("1 - Top up");
			System.out.println("2 - Purchase item");
			System.out.println("3 - Exit");

			String option = scanner.nextLine();
			
			String username = "";
			String password = "";
			switch(option) {
			case "1": // Top up 
				service.topUp();
				break;
			case "2": // Purchase item
				service.purchaseItem();
				break;
			case "3": // Exit
				service.writeInventory(inventoryFile);
				System.out.println("Goodbye!");
				scanner.close();
				readyToExit = true;
				break;
			default:
				System.out.println("Option not valid - try again !");
			}
		}

	}
} 

