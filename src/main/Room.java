package main;

import java.util.*;
import java.io.FileNotFoundException;

import utils.*;

//class to house the room object, which houses several blocks and handles updating them as well as supplying each block with information about its surroundings
public class Room {
	private int rows, cols; //variables to store the size of the room's character array
	private ArrayList<Block> blocks; //arraylist to store the blocks which the room contains
	private String filepath; //filepath of the room
	private String name; //name of the room
	
	private char[][] rawArray; //array of the room which contains the entire room, including it's blankArray and its blocks
	private char[][] blankArray;
	
	private Player player; //player to which the room belongs
	
	//room constructor
	public Room(int rows, int cols, String name, char[][] rawArray, char[][] blankArray, ArrayList<Block> blocks) {
		this.rows = rows;
		this.cols = cols;
		this.name = name;
		this.rawArray = rawArray;
		this.blankArray = blankArray;
		
		//setting all of its blocks to belong to 'this' room
		for(Block block: blocks) {
			block.setRoom(this);
		}
		
		this.blocks = blocks;
	}
	
	//copy method, used to circumvent weird pointer crap
	public static char[][] copy(char[][] input) {
		char[][] output = new char[input.length][input[0].length];
		
		for(int i = 0; i < input.length; i++) {
			for(int j = 0; j < input[i].length; j++) {
				output[i][j] = input[i][j];
			}
		}
		
		return output;
	}
	
	//method which is used to convert the player's current location along with the room's rawArray into a string for the game to display
	public String arrayToString() {
		String output = "";
		
		for(int i = 0; i < rawArray.length; i++) {
			for(int j = 0; j < rawArray[i].length; j++) {
				if(i == player.getCurrentRow() && j == player.getCurrentCol())
					output += 'P' + " ";
				else
					output += getRawArray()[i][j] + " ";
			}	
			output += "\n";
		}
		return output;
	}
	
	//getters and setters
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public char[][] getRawArray() {
		char[][] rawArray = copy(blankArray);
		
		for(Block block: blocks) {
			rawArray[block.getRow()][block.getCol()] = BlockType.blockType2Character.get(block.getType());
		}
		return rawArray;
	}

	public void setRawArray(char[][] rawArray) {
		this.rawArray = rawArray;
	}
	
	public char[][] getBlankArray() {
		return blankArray;
	}
	
	public void setBlankArray(char[][] blankArray) {
		this.blankArray = blankArray;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		for(Block block: blocks)
			block.setPlayer(player);
	}
	
	public void update() {
		for(int i = 0; i < blocks.size(); i++)
			blocks.get(i).update();
		
	}
}
