package main;

import utils.*;
import java.util.*;

//class to hold block objects which will be in every room, and have special characteristics listed in the PIV's
public class Block {
	//traits applicable for every block
	private int row, col;
	private BlockType type;
	private Room room;
	private Player player;
	
	//traits applicable for moving blocks
	private int speed, counter;
	
	//traits applicable for door blocks
	private String nextRoom;
	private int nextRoomRow, nextRoomCol;
	
	//traits applicable for cannon blocks and / or moving blocks
	private Direction direction;
	private int bulletSpeed;
	
	//traits applicable for gold blocks
	private int value;
	
	//Constructor for type gold block / static enemy block
	public Block(int row, int col, int value) {
		this.row = row;
		this.col = col;
		this.type = type;
		type = BlockType.GOLD;
		this.value = value;
	}

	//Constructor for type checkpoint block / movable block / static enemy block
	public Block(int row, int col, BlockType type) {
		this.row = row;
		this.col = col;
		this.type = type;
	}
	
	//Constructor for type cannon
	public Block(int row, int col, Direction direction, int speed, int bulletSpeed) {
		this.row = row;
		this.col = col;
		type = BlockType.CANNON;
		this.direction = direction;
		this.speed = speed;
		this.bulletSpeed = bulletSpeed;
		counter = 0;
	}
	
	//Constructor for moving blocks
	public Block(int row, int col, BlockType type, Direction direction, int speed) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.direction = direction;
		this.speed = speed;
		counter = 0;
	}
	
	//Constructor for door block
	public Block(int row, int col, String nextRoom, int nextRoomRow, int nextRoomCol) {
		this.row = row;
		this.col = col;
		type = BlockType.DOOR;
		this.nextRoom = nextRoom;
		this.nextRoomRow = nextRoomRow;
		this.nextRoomCol = nextRoomCol;
	}
	
	//method to test if the block is touching the player
	public boolean isTouchingPlayer() {
		return (player.getCurrentRow() == row) && (player.getCurrentCol() == col);
	}
	
	//method to test if the block is touching a particular row and coloumn
	public boolean isTouching(int row, int col) {
		return (player.getCurrentRow() == row) && (player.getCurrentCol() == col);
	}
	
	//method to set the player's location back to their last checkpoint, if the block is touching the player (used for enemy blocks)
	public void handleCollisions() {
		if(isTouchingPlayer()) {
			player.setCurrentRow(player.getCheckpointRow());
			player.setCurrentCol(player.getCheckpointCol());
			player.decrementLives();
		}
	}
	
	//method to test if a specific location houses a boundary (#, D, or @)
	public boolean isBoundary(char[][] rawArray, int row, int col) {
		return (rawArray[row][col] == '#') || (rawArray[row][col] == 'D') || (rawArray[row][col] == '@');
	}
	
	public boolean isMovingEnemy(char[][] rawArray, int row, int col) {
		return (rawArray[row][col] == 'S') || (rawArray[row][col] == 'X') || (rawArray[row][col] == 'o');
	}
	
	public boolean canMove(char[][] rawArray, int row, int col) {
		return (rawArray[row][col] != '#') && (rawArray[row][col] != 'D');
	}
	
	//update block, which will be called in the animation timer in the main method
	public void update() {
		char[][] rawArray = room.getRawArray();
		switch(type) {
		//if the block is a door, setting the player's location and current room to that specified in the block variable, as well as setting its checkpoint row and checkpoint coloumn
		case DOOR:
			if(isTouchingPlayer()) {
				player.setCurrentRoom(Rooms.roomsMap.get(nextRoom));
				player.setCurrentRow(nextRoomRow);
				player.setCurrentCol(nextRoomCol);
				player.setCheckpointRow(nextRoomRow);
				player.setCheckpointCol(nextRoomCol);
			}
			break;
		//if the block is gold, increasing the player's gold variable by the value specified by the block, and removing the block from its room all if the block is touching the player
		case GOLD:
			if(isTouchingPlayer()) {
				player.setGold(player.getGold() + value);
				room.getBlocks().remove(this);
			}
			break;
		//if the block is of type checkpoint, setting the player's checkpoint location to the checkpoint block's location and removing it from its room
		case CHECKPOINT:
			if(isTouchingPlayer()) {
				player.setCheckpointRow(row);
				player.setCheckpointCol(col);
				room.getBlocks().remove(this);
			}
			break;
		//if the block is of type movable, and if it is touching the player, moving the block according to the player's last move while also making sure the future move location is not a boundary
		case MOVABLE:
			if(isTouchingPlayer()) {
				switch(player.getMoveDirection()) {
				case UP:
					if(isMovingEnemy(rawArray, row - 1, col)) {
						Block removingBlock = null;
						for(Block block: room.getBlocks()) {
							if(block.row == row - 1 && block.col == col) {
								removingBlock = block;
								break;
							}
						}
						room.getBlocks().remove(removingBlock);
					}
					if(room.getRawArray()[row - 1][col] == '@') {
						player.setCurrentRow(player.getCurrentRow() + 1);
					}
					else if(canMove(rawArray, row - 1, col))
						row--;
					break;
				case DOWN:
					if(isMovingEnemy(rawArray, row + 1, col)) {
						Block removingBlock = null;
						for(Block block: room.getBlocks()) {
							if(block.row == row + 1 && block.col == col) {
								removingBlock = block;
								break;
							}
						}
						room.getBlocks().remove(removingBlock);
					}
					if(room.getRawArray()[row + 1][col] == '@') {
						player.setCurrentRow(player.getCurrentRow() - 1);
					}
					else if(canMove(rawArray, row + 1, col))
						row++;
					break;
				case RIGHT:
					if(isMovingEnemy(rawArray, row, col + 1)) {
						Block removingBlock = null;
						for(Block block: room.getBlocks()) {
							if(block.row == row && block.col == col + 1) {
								removingBlock = block;
								break;
							}
						}
						room.getBlocks().remove(removingBlock);
					}
					
					if(room.getRawArray()[row][col + 1] == '@') 
						player.setCurrentCol(player.getCurrentCol() - 1);
					
					else if(canMove(rawArray, row, col + 1))
						col++;
					break;
				case LEFT:
					if(isMovingEnemy(rawArray, row, col - 1)) {
						Block removingBlock = null;
						for(Block block: room.getBlocks()) {
							if(block.row == row && block.col == col - 1) {
								removingBlock = block;
								break;
							}
						}
						room.getBlocks().remove(removingBlock);
					}
					
					if(room.getRawArray()[row][col - 1] == '@')
						player.setCurrentCol(player.getCurrentCol() + 1);
					
					if(canMove(rawArray, row, col - 1))
						col--;
					break;
				}
			}
			break;
		//if the block is of type cannon, handling collisions and creating cannonball blocks in the order of the direction specified by the block, all if the counter and speed of the block are the same
		case CANNON:
			handleCollisions();
			
			if(speed == counter) {
				switch(direction) {
				case UP:
					Block block = new Block(row - 1, col, BlockType.CANNONBALL, Direction.UP, bulletSpeed);
					block.setRoom(room);
					room.getBlocks().add(block);
					break;
				case DOWN:
					Block block1 = new Block(row + 1, col, BlockType.CANNONBALL, Direction.DOWN, bulletSpeed);
					block1.setRoom(room);
					room.getBlocks().add(block1);
					break;
				case LEFT:
					Block block2 = new Block(row, col - 1, BlockType.CANNONBALL, Direction.LEFT, bulletSpeed);
					block2.setRoom(room);
					room.getBlocks().add(block2);
					break;
				case RIGHT:
					Block block3 = new Block(row, col + 1, BlockType.CANNONBALL, Direction.RIGHT, bulletSpeed);
					block3.setRoom(room);
					room.getBlocks().add(block3);
					break;
				}
				counter = 0;
			} else
				counter++;
			break;
		//if the block is of type cannonball, moving the block in the direction specified by the block only if the specified move location is not a boundary, and if it is, deleting the block from its room
		case CANNONBALL:
			handleCollisions();
			
			if(speed == counter) {
				switch(direction) {
				case UP:
					if(isBoundary(rawArray, row - 1, col)) {
						room.getBlocks().remove(this);
						break;
					} else {
						row--;
					}
					break;
				case DOWN:
					if(isBoundary(rawArray, row + 1, col)) {
						room.getBlocks().remove(this);
					} else {
						row++;
					}
					break;
				case LEFT:
					if(isBoundary(rawArray, row, col - 1)) {
						room.getBlocks().remove(this);
					} else {
						col--;
					}
					break;
				case RIGHT:
					if(isBoundary(rawArray, row, col + 1)) {
						room.getBlocks().remove(this);
					} else {
						col++;
					}
					break;
				}
				counter = 0;
			} else
				counter++;
			break;
		//if the block is of type horizontal enemy, moving the block in it's specified direction only if the specified move location is not a boundary, and if it is, removing the block from its room
		case HORIZONTAL_ENEMY:
			handleCollisions();

			if(speed == counter) {
				if(direction.equals(Direction.RIGHT)) {
					if(isBoundary(rawArray, row, col + 1)) {
						direction = Direction.LEFT;
						break;
					} else
						col++;
				} else {
					if(isBoundary(rawArray, row, col - 1)) {
						direction = Direction.RIGHT;
						break;
					} else
						col--;
				}
				counter = 0;
			} else
				counter++;
			break;
		//if the block is of type random_enemy, moving the block in a random location (being sure to handle potential exceptions with surrounding blocks being out of the room's exterior, as well as being potential boundaries
		//then setting the blocks location to a randomly chosen nextLocation in the arraylist
		case RANDOM_ENEMY:
			handleCollisions();
			
			if(speed == counter) {
				ArrayList<int[]> possibleNextLocations = new ArrayList<int[]>();
				
				for(int i = row - 1; i <= row + 1; i++) {
					for(int j = col - 1; j <= col + 1; j++) {
						int[] pair = {i, j};
						try {
							if(!isBoundary(rawArray, i, j))
								possibleNextLocations.add(pair);
						} catch(ArrayIndexOutOfBoundsException exception) {}
					} 
				}
				int choice = (int) (Math.random() * (possibleNextLocations.size()));
				row = possibleNextLocations.get(choice)[0];
				col = possibleNextLocations.get(choice)[1];
				counter = 0;
			} else
				counter++;
			break;
		//if the block is of type static_enemy, simply handling collisions
		case STATIC_ENEMY:
			handleCollisions();
			break;
		}
	}
	
	//toString method which is used for debugging purposes to print the details of the block
	@Override
	public String toString() {
		String output = "room: " + room.getName() + ", type: " + type + ", row: " + row + ", col: " + col + ", ";
		switch(type) {
		case DOOR:
			output += "nextRoom: " + nextRoom + ", nextRoomRow: " + nextRoomRow + ", nextRoomCol: " + nextRoomCol;
			return output;
		case GOLD:
			output += "value: " + value;
			return output;
		case CHECKPOINT:
			return output;
		case MOVABLE:
			return output;
		case CANNON:
			output += "Direction: " + direction + ", speed: " + speed + ", bulletSpeed: " + bulletSpeed;
			return output;
		case CANNONBALL:
			output += "Direction: " + direction + ", speed: " + speed;
			return output;
		case HORIZONTAL_ENEMY:
			output += "Direction: " + direction + ", speed: " + speed;
			return output;
		case RANDOM_ENEMY:
			output += "speed: " + speed;
			return output;
		case STATIC_ENEMY:
			return output;
		}
		return null;
	}
	
	//getters and setters
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public BlockType getType() {
		return type;
	}

	public void setType(BlockType type) {
		this.type = type;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
		player  = room.getPlayer();
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public String getNextRoom() {
		return nextRoom;
	}

	public void setNextRoom(String nextRoom) {
		this.nextRoom = nextRoom;
	}

	public int getNextRoomRow() {
		return nextRoomRow;
	}

	public void setNextRoomRow(int nextRoomRow) {
		this.nextRoomRow = nextRoomRow;
	}

	public int getNextRoomCol() {
		return nextRoomCol;
	}

	public void setNextRoomCol(int nextRoomCol) {
		this.nextRoomCol = nextRoomCol;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(int bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
