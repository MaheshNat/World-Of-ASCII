package utils;

import java.util.*;
import java.io.*;
import main.*;

//***NOTE***: everything which is commented in the class besides descriptive comments is code which is used for debugging, printing each step of the proccess
//The file reader class takes input in the form of text files and returns a room object given a filepath for a text file (used in the rooms class)
public class FileReader {
	
	//get Room method, which returns a room object by reading a text file
	public static Room getRoom(String filepath) throws FileNotFoundException {
		//getting the name and dimensions of the room
		Scanner scan = new Scanner(new File(filepath));
		String name = scan.nextLine();
		int rows = Integer.parseInt(scan.nextLine());
		int cols = Integer.parseInt(scan.nextLine());
		
		//creating an array based on the dimensions of the room
		char[][] rawArray = new char[rows][cols];
		
		System.out.println("name: " + name + ", rows: " + rows + ", cols: " + cols);
		
		//filling the array with the information in the file
		for(int i = 0; i < rows; i++) {
			rawArray[i] = scan.nextLine().toCharArray();
			System.out.println(rawArray[i]);
		}
		
		for(int i = 0; i < rawArray.length; i++) {
			for(int j = 0; j < rawArray[i].length; j++) {
				if(!(rawArray[i][j] == '#' || rawArray[i][j] == '.'))
						System.out.println(rawArray[i][j] + " " + i + " " + j);
			}
		}

		//creating a blank arrray (array with only (# and .) for the room object
		char[][] blankArray = copy(rawArray);
		
		for(int i = 0; i < blankArray.length; i++) {
			for(int j = 0; j < blankArray[i].length; j++) {
				if(blankArray[i][j] != '#' && blankArray[i][j] != '.') {
					if(blankArray[i][j] == 'D')
						blankArray[i][j] = '#';
					else
						blankArray[i][j] = '.';
				}
			}
		}
		
		printArray(blankArray);
		//getting the blocks of the room
		ArrayList<Block> blocks = getBlocks(scan);
		
		//creating and returning the room object
		Room room = new Room(rows, cols, name, rawArray, blankArray, blocks);
		
		room.setFilepath(filepath);
		return room;
	}
	
	//main method for debugging purposes
	public static void main(String[] args) throws FileNotFoundException {
		getRoom("/Users/mahesh/Documents/Eclipse Java Projects/World Of ASCII/src/utils/START");
	}
	
	//printing method for debugging purposes
	public static void printArray(char[][] array) {
		for(int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
	}

	//method to test if a specific character is a boundary (#, D, or @), given a row, col, and an array of characters
	public static boolean isBoundary(int row, int col, char[][] array) {
		return (array[row][col] == '#') || (array[row][col] == 'D') || (array[row][col] == '@');
	}
	
	//method to test if a specific character is a boundary (#, D, or @)
	public static boolean isBoundary(char character) {
		return (character == '#') || (character == 'D') || (character == '@');
	}
	
	//copy method to copy the elements in one char[][] to another
	public static char[][] copy(char[][] input) {
		char[][] output = new char[input.length][input[0].length];
		
		for(int i = 0; i < input.length; i++) {
			for(int j = 0; j < input[i].length; j++) {
				output[i][j] = input[i][j];
			}
		}
		
		return output;
	}

	//method which returns an arraylist of block objects from a file
	public static ArrayList<Block> getBlocks(Scanner scan) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		while(scan.hasNextLine()) {
			String str = scan.nextLine();
			//System.out.println(str);
			String[] parts = str.split(" ");
			int row = Integer.parseInt(parts[1]);
			int col = Integer.parseInt(parts[2]);
			
			//parsing the input in the file based on the first character in each line:
			switch(str.charAt(0)) {
			case 'D':
				String nextRoom = parts[3];
				int nextRoomRow = Integer.parseInt(parts[4]);
				int nextRoomCol = Integer.parseInt(parts[5]);
				Block block = new Block(row, col, nextRoom, nextRoomRow, nextRoomCol);
				blocks.add(block);
				break;
			case 'G':
				int value = Integer.parseInt(parts[3]);
				Block block1 = new Block(row, col, value);
				blocks.add(block1);
				break;
			case 'C':
				Block block2 = new Block(row, col, BlockType.CHECKPOINT);
				blocks.add(block2);
				break;
			case '@':
				Block block3 = new Block(row, col, BlockType.MOVABLE);
				blocks.add(block3);
				break;
			case '+':
				int speed = Integer.parseInt(parts[4]);
				int bulletSpeed = Integer.parseInt(parts[5]);
				Block block4 = new Block(row, col, Direction.valueOf(parts[3]), speed, bulletSpeed);
				blocks.add(block4);
				break;
			case 'o':
				int speed1 = Integer.parseInt(parts[4]);
				Block block5 = new Block(row, col, BlockType.CANNONBALL, Direction.valueOf(parts[3]), speed1);
				blocks.add(block5);
				break;
			case 'S':
				int speed2 = Integer.parseInt(parts[4]);
				Block block6 = new Block(row, col, BlockType.HORIZONTAL_ENEMY, Direction.valueOf(parts[3]), speed2);
				blocks.add(block6);
				break;
			case 'X':
				int speed3 = Integer.parseInt(parts[3]);
				Block block7 = new Block(row, col, BlockType.RANDOM_ENEMY, Direction.UP, speed3);
				blocks.add(block7);
				break;
			case 'T':
				Block block8 = new Block(row, col, BlockType.STATIC_ENEMY);
				blocks.add(block8);
				break;
			}
		}
		return blocks;
	}
}
