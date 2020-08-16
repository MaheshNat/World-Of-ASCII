package utils;

import java.io.FileNotFoundException;
import java.util.*;
import main.*;

//current amount of maximum gold: 72

//class to hold all of the rooms which are specified in the files
public class Rooms {
	private static final String FILEPATH_BASE = "/Users/mahesh/Documents/Eclipse Java Projects/World Of ASCII/src/utils/"; //base location of all files, will needed to be changed if someone else is playing game
	
	//static room objects
	public static Room start;
	public static Room startRight;
	public static Room startUp;
	public static Room hallway;
	public static Room maze;
	public static Room startDown;
	
	//map and list of all rooms to locate a room from its name
	public static HashMap<String, Room> roomsMap = new HashMap<String, Room>();
	public static ArrayList<Room> rooms = new ArrayList<Room>();
	
	//initializing rooms and putting them intside of maps inside of a static initilizer block
	static {
		try {
			start = FileReader.getRoom(FILEPATH_BASE + "START");
			rooms.add(start);
			startRight = FileReader.getRoom(FILEPATH_BASE + "START_RIGHT");
			rooms.add(startRight);
			startUp = FileReader.getRoom(FILEPATH_BASE + "START_UP");
			rooms.add(startUp);
			hallway = FileReader.getRoom(FILEPATH_BASE + "HALLWAY");
			rooms.add(hallway);
			maze = FileReader.getRoom(FILEPATH_BASE + "MAZE");
			rooms.add(maze);
			startDown = FileReader.getRoom(FILEPATH_BASE + "START_DOWN");
			rooms.add(startDown);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		roomsMap.put("start", start);
		roomsMap.put("startRight", startRight);
		roomsMap.put("startUp", startUp);
		roomsMap.put("hallway", hallway);
		roomsMap.put("maze", maze);
		roomsMap.put("startDown", startDown);
	}
	
	public static void reset() {
		
		roomsMap.clear();
		rooms.clear();
		
		try {
			start = FileReader.getRoom(FILEPATH_BASE + "START");
			rooms.add(start);
			startRight = FileReader.getRoom(FILEPATH_BASE + "START_RIGHT");
			rooms.add(startRight);
			startUp = FileReader.getRoom(FILEPATH_BASE + "START_UP");
			rooms.add(startUp);
			hallway = FileReader.getRoom(FILEPATH_BASE + "HALLWAY");
			rooms.add(hallway);
			maze = FileReader.getRoom(FILEPATH_BASE + "MAZE");
			rooms.add(maze);
			startDown = FileReader.getRoom(FILEPATH_BASE + "START_DOWN");
			rooms.add(startDown);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		roomsMap.put("start", start);
		roomsMap.put("startRight", startRight);
		roomsMap.put("startUp", startUp);
		roomsMap.put("hallway", hallway);
		roomsMap.put("maze", maze);
		roomsMap.put("startDown", startDown);
	}
} 
