package utils;

import java.util.*;

//enumerator to store the blocktypes of all the different types of blocks which can exist, while also allowing to access a blocktype from its 'type' (character representation of itself)
public enum BlockType {
	/*Types:
	 

	 * 'G' - gold
	 * 'C' - checkpoint
	 * 'T' - static enemy

	 * '+' - cannon
	 * 'o' - cannonball 
	

	 * 'S' - horizontally moving enemy
	 * 'X' - randomly moving enemy
	 * '@' - movable`
	 * 'A' - following moving enemy
		

	 * 'D' - door
	*/
	
	//initializing enum fields, and setting their respective characters
	DOOR('D'), GOLD('G'), CHECKPOINT('C'), MOVABLE('@'), CANNON('+'), CANNONBALL('o'), HORIZONTAL_ENEMY('S'), RANDOM_ENEMY('X'), STATIC_ENEMY('T'), FOLLOWING_ENEMY('A');
	
	//type variable (character representation of the enum field)
	private char type;
	
	//maps to store the character to enum relationship of the blocktype fields
	public static HashMap<Character, BlockType> character2BlockType = new HashMap<Character, BlockType>();
	public static HashMap<BlockType, Character> blockType2Character = new HashMap<BlockType, Character>();
	
	//looping through all the blocktypes in the BlockType class's 'EnumSet', and placing them respectively in the maps
	static {
		for(BlockType blockType: EnumSet.allOf(BlockType.class)) {
			character2BlockType.put(blockType.type, blockType);
			blockType2Character.put(blockType, blockType.type);
		}
	}

	//private constructor
	private BlockType(char type) {
		this.type = type;
	}	
}
