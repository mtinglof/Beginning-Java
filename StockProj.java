import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

import realtimeweb.stockservice.domain.StockService;
import realtimeweb.stockservice.domain.Stock;

/**
 *  Program #8
 *  This program allows users to input stock ticker symbols and receive
 *  real time info about prices and trade times as well as compare symbols on 
 *  greatest percent change since yesterday
 *  CS108-2
 *  12/13/16
 *  @author  Michael Tinglof
 */

public class StockProj {
	
	//variable array
	private static StockService stockService = new StockService();
	private static ArrayList<String> index = new ArrayList<String>();
	private static ArrayList<String> holder = new ArrayList<String>(); 
	private static Scanner input = new Scanner(System.in); 
	private static int arraySize; 
	private static String ticker; 
	private static String change; 
	
	//sets tickers and inputs into an array
	public static void setTickers(){
		System.out.println("Please input stock Tickers you would like to search");
		for(int i=0; i<arraySize; i++){
			ticker = input.next(); 
			index.add(ticker); 
		}
	}
	
	public static void testStockServiceOnline() {
		try{
			//takes user array and returns basic info about each ticker, running a for loop
			for(String t : index){
				Stock stock = stockService.getStockInfo(t);
				System.out.println(stock.getTicker());
				System.out.println("Percent price change since Yesterday " + stock.getPercent_Change() + "%"); 
				System.out.println("Price change since Yesterday $" + stock.getChange());
				System.out.println("This stock can be bought for $" + stock.getLast());
				System.out.println("Last recorded trade: " + stock.getLast_Trade_Date() + " at " + stock.getLast_Trade_Time());
				System.out.println(); 
			}
		}
		//catch block 
		catch(NullPointerException e){
			System.out.println("One of the ticker symbols you entered was incorrect");
			return;
		}
		catch(NumberFormatException e){
			System.out.println("One of the ticker symbols you entered was incorrect");
			return; 
		}
	}
	
	public static void comparePrice(){
		try{
			//returns a compared list of percent changes since yesterday 
			for(String t : index){
			Stock stock = stockService.getStockInfo(t);
			change = (stock.getPercent_Change() + "% " + stock.getTicker()); 
			holder.add(change); 
			}
		}
		//catch block 
		catch(NumberFormatException e){
			System.out.println("Please correct incorrect ticker for stock percent data"); 
			return;
		}
		catch(NullPointerException e){
			System.out.println("Please correct incorrect ticker for the stock percent data");
			return;
		}
		
		//string statement, reverses order to highest percent change is on top of list
		Collections.sort(holder); 
		Collections.reverse(holder);
		System.out.println("Your stocks ordered in greatest percentage change since yesterday:");
		for(String h : holder){
			System.out.println(h);
		}
	}
	
	//main method 
	public static void main(String[] args) {
		System.out.println("Program #8, Michael Tinglof, masc0751");
		System.out.println("How many tickers would you like to search?");
		//try and catch block to allow user to enter tickers 
		try{
			arraySize = input.nextInt(); 
		}
		catch (InputMismatchException e){
			System.out.println("That's definitely not a number"); 
			return;
		}
		//calls methods 
		setTickers(); 
		testStockServiceOnline(); 
		comparePrice(); 
	}

}

