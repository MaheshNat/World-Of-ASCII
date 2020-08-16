package main;

import utils.*;

public class Player {

	private int currentRow, currentCol, checkpointRow, checkpointCol; //variables to hold the location of the player in the room, as well as its previous checkpoint location
	private int gold; //variable to hold the amount of gold the player has; the game ends once the player collects all the gold
	private Room currentRoom; //Room variable stating the current room the player is in
	private Direction moveDirection; //Direction constant to store in which direction the player has moved
	private int lives; //the number of lives the player has; the game ends when the player has 0 lives
	
	private String name; //name of the player
	private int health; //health (from 0 to 100)
	private int endurance; //endurance (from 0 to 10)
	
	//pre-set players
	public static Player brock_o_lee = new Player("Brock O' Lee", 40, 7);
	public static Player kanye_east = new Player("Kanye East", 60, 4);
	public static Player not_so_sherlock = new Player("Not So Sher - lock", 35, 9);
	
	//initialization block, used to set default attributes of player
	{
		currentRoom = Rooms.start;
		gold = 0;
		currentRow = 10;
		currentCol = 10;
		checkpointRow = currentRow;
		checkpointCol = currentCol;
		lives = 20;
		
		for(Room room: Rooms.rooms)
			room.setPlayer(this);
	}
	
	//default constructor
	public Player() {
		health = 500;
		endurance = 500;
		name = "John Smith";
	}
	
	//specialized constructor
	public Player(String name, int health, int endurance) {
		this.name = name;
		this.health = health;
		this.endurance = endurance;
	}
	
	//getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getEndurance() {
		return endurance;
	}

	public void setEndurance(int endurance) {
		this.endurance = endurance;
	}
	
	public int getCurrentRow() {
		return currentRow;
	}
	
	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

	public int getCurrentCol() {
		return currentCol;
	}

	public void setCurrentCol(int currentCol) {
		this.currentCol = currentCol;
	}

	public Direction getMoveDirection() {
		return moveDirection;
	}
	
	public void setMoveDirection(Direction moveDirection) {
		this.moveDirection = moveDirection;
	}

	public int getCheckpointRow() {
		return checkpointRow;
	}

	public void setCheckpointRow(int checkpointRow) {
		this.checkpointRow = checkpointRow;
	}

	public int getCheckpointCol() {
		return checkpointCol;
	}

	public void setCheckpointCol(int checkpointCol) {
		this.checkpointCol = checkpointCol;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}
	
	public boolean isBoundary(int row, int col) {
		return (currentRoom.getRawArray()[row][col] == '#');
	}
	
	public int getLives() {
		return lives;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public void decrementLives() {
		lives = lives - 1;
	}
	
	//move method, used to move the player around the room
	public void move(Direction direction) {
		switch(direction) {
		case UP:
			if(!isBoundary(currentRow - 1, currentCol))
				currentRow--;
			moveDirection = Direction.UP;
			break;
		case DOWN:
			if(!isBoundary(currentRow + 1, currentCol))
				currentRow++;
			moveDirection = Direction.DOWN;
			break;
		case LEFT:
			if(!isBoundary(currentRow, currentCol - 1))
				currentCol--;
			moveDirection = Direction.LEFT;
			break;
		case RIGHT:
			if(!isBoundary(currentRow, currentCol + 1))
				currentCol++;
			moveDirection = Direction.RIGHT;
			break;
		}
	}
	
	//toString method which is used in the beginning (console) part of the game
	public String toString2() {
		return "name: " + name + ", health: " + health + ", endurance: " + endurance;
	}
	
	//toString method which is used during the javafx portion of the game
	@Override
	public String toString() {
		return "row: " + currentRow + "\ncol: " + currentCol + "\ncheckpoint row: " + checkpointRow + "\ncheckpoint col: " + checkpointCol + "\ncurrent room: " + currentRoom.getName() + "\ngold: " + gold + "\nlives: " + lives;
	}
}
