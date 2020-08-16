package main;

import utils.*;
import java.util.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.*;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.*;

public class Main extends Application {
	
	//Logo text
	public static final String LOGO = " __      __            .__       .___ ________   _____     _____    __________________ .___.___ \n" + 
			"/  \\    /  \\___________|  |    __| _/ \\_____  \\_/ ____\\   /  _  \\  /   _____/\\_   ___ \\|   |   |\n" + 
			"\\   \\/\\/   /  _ \\_  __ \\  |   / __ |   /   |   \\   __\\   /  /_\\  \\ \\_____  \\ /    \\  \\/|   |   |\n" + 
			" \\        (  <_> )  | \\/  |__/ /_/ |  /    |    \\  |    /    |    \\/        \\\\     \\___|   |   |\n" + 
			"  \\__/\\  / \\____/|__|  |____/\\____ |  \\_______  /__|    \\____|__  /_______  / \\______  /___|___|\n" + 
			"       \\/                         \\/          \\/                \\/        \\/         \\/         ";
	
	//Legend text
	public static final String LEGEND = "Use Arrow Keys to Move Across the Room.\n\n" +
										"LEGEND:\n" + 
										"'D' -> DOOR (takes the player to other rooms)\n" + 
										"'G' -> GOLD (pieces of gold the player collects throughout their journey to win the game)\n" + 
										"'C' -> CHECKPOINT (once touched by the player, saves the player's checkpoint location to the checkpoint's location)\n" + 
										"'@' -> MOVABLE (a block which can be moved by the player and blocks the paths of enemies (horizontal enemies, cannons, etc.))\n" + 
										"'+' -> CANNON (shoots cannonballs in a specific direction which are deadly to the player)\n" + 
										"'o' -> CANNONBALL (balls shot by cannons which are deadly to the player)\n" + 
										"'S' -> HORIZONTAL_ENEMY (an enemy which moves side to side across the room, and is deadly to the player)\n" + 
										"'X' -> RANDOM_ENEMY (an enemy which moves randomly across the room, and is deadly to the player)\n" + 
										"'T' -> STATIC_ENEMY (an enemy which stands in place, yet still being deadly to the player)";
	
	//javafx objects (stage, scene, borderpane, labels)
	public Stage window = null;
	public Label main = new Label();
	public Label stats = new Label();
	public Label logo = new Label(LOGO);
	public Label legend = new Label(LEGEND);
	public BorderPane root = new BorderPane();
	public Scene scene;
	
	//player object
	public static Player player = new Player();
	
	//control boolean variables
	public static boolean playAgainBoolean = false;
	public static boolean won = false;
	
	//miscellaneous objects / variables
	public static Scanner scan = new Scanner(System.in);
	public static final int MAXIMUM_GOLD = 85;
	public static MusicPlayer musicPlayer = null;
	
	//static initialization to initialize the musicPlayer object and handle its exceptions
	
	
	//javafx start method
	@Override
	public void start(Stage primaryStage) throws Exception {
		do {
			//setting attributes for javafx objects
			window = primaryStage;
			window.setTitle("World Of ASCII");
	
			main.setFont(Font.font("Courier", 12));
			main.setTextFill(Color.BLACK);
			stats.setFont(Font.font("Courier", 14));
			logo.setFont(Font.font("Courier", 20));
			legend.setFont(Font.font("Courier", 20));
	
			root.setCenter(main);
			root.setTop(logo);
			root.setBottom(legend);
			root.setRight(stats);
	
			scene = new Scene(root, 1200, 750, Color.BLACK);
			
			//creating a key listener for the scene to handle arrow key presses in the player's move method
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					switch(event.getCode()) {
					case UP:
						player.move(Direction.UP);
						break;
					case DOWN:
						player.move(Direction.DOWN);
						break;
					case LEFT:
						player.move(Direction.LEFT);
						break;
					case RIGHT:
						player.move(Direction.RIGHT);
						break;
					}
				}
			});
			
			//creating an animation timer to constantly update the javafx screen
			AnimationTimer timer = new AnimationTimer() {
				@Override
				public void handle(long now) {
					//setting the labels on the scene to the player's stats, logo, legend, and the arrayToString() (string representation) of the player's current room's current state
					stats.setText(player.toString());
					player.getCurrentRoom().update();
					String str = player.getCurrentRoom().arrayToString();
					main.setText(str);
					
					//ending the animation timer if the player collects all of the gold
					if(player.getGold() >= MAXIMUM_GOLD) {
						VBox questions = new VBox();
						Label playAgain = new Label("You won the game! Would you like to play again?");
						Button yesButton = new Button("Yes");
						Button noButton = new Button("No");
						questions.getChildren().addAll(playAgain, yesButton, noButton);
						root.setCenter(questions);
						
						yesButton.setOnAction(e -> {
							System.out.println("Button clicked (yes) ");
							reset();
							playAgainBoolean = true;
							root.setCenter(main);
							this.stop();
						});
						
						noButton.setOnAction(e -> {
							System.out.println("Button clicked (no) ");
							playAgainBoolean = false;
							window.close();
							System.exit(1);
							this.stop();
						});
					}
					
					if(player.getLives() == 0) {
						VBox questions = new VBox();
						Label playAgain = new Label("You lost all your lives! Would you like to play again?");
						Button yesButton = new Button("Yes");
						Button noButton = new Button("No");
						questions.getChildren().addAll(playAgain, yesButton, noButton);
						root.setCenter(questions);
						
						yesButton.setOnAction(e -> {
							reset();
							root.setCenter(main);
							this.stop();
						});
						
						noButton.setOnAction(e -> {
							window.close();
							System.exit(1);
						});
					}
				}
			};
			
			//closig the window if the player won
			
			//starting the animation timer
			timer.start();
	
			//showing and setting the scene for the stage (window)
			window.setScene(scene);
			window.show();
		} while(playAgainBoolean);
	}
	
	public static void reset() {
		Rooms.reset();
		player = new Player();
	}
	
	//first half of the game, the console portion: 
	
	public static void main(String[] args) {
		//musicPlayer.play();
		launch(args);
//		do {
//			//playing music and introducing the player to the mechanics of the game.
//			musicPlayer.play();
//			String welcomeText = "Welcome to \"World of Ascii\"!!!\n";
//			welcomeText += "In this game you will put your luck and thinking skills to the test.\n";
//			welcomeText += "The game consists of two main parts: choosing and playing.\n";
//			welcomeText += "\"choosing\" is when you are given choices to pick from which will decide your fate, and is played inside of the console.\n";
//			welcomeText += "\"playing\" is when you are put inside of an ASCII world, where you will collect gold until you are ready to move on to the next stage.\n";
//			welcomeText += "press any key to start the game.";
//			
//			System.out.println(welcomeText);
//			
//			scan.next();
//			
//			//asking the player to choose a character
//			System.out.println("Choose your player from the menu, or create your own:\n");
//			System.out.println("(1): " + Player.brock_o_lee.toString2());
//			System.out.println("(2): " + Player.kanye_east.toString2());
//			System.out.println("(3): " + Player.not_so_sherlock.toString2());
//			System.out.println("(4): Create Your Own Character");
//			int choice = scan.nextInt();
//			
//			switch(choice) {
//			case 1:
//				player = Player.brock_o_lee;
//				break;
//			case 2:
//				player = Player.kanye_east;
//				break;
//			case 3:
//				player = Player.not_so_sherlock;
//				break;
//			default:
//				//asking user for details of custom player
//				System.out.println("Enter your player's name:");
//				String name = scan.next();
//				System.out.println("Enter your player's health: ");
//				int health = scan.nextInt();
//				if(health > 100)
//					health = 100;
//				if(health <= 0)
//					health = 0;
//				System.out.println("Enter your player's endurance: ");
//				int endurance = scan.nextInt();
//				if(endurance > 10)
//					endurance = 10;
//				if(endurance <= 0)
//					endurance = 0;
//				player = new Player(name, health, endurance);
//			}
//			
//			System.out.println("You chose: " + player.toString2());
//			
//			//giving the player the first choice
//			System.out.println();
//			System.out.println("You feel the weight of your eyelids pressing down, your legs aching as if walking a thousand miles.");
//			System.out.println("The blazing sun casts its scorching heat upon your skin, distoring the distance and picking at your integrity.");
//			System.out.println("You come across an oasis, its running stream tempting your shriveled throat.\n");
//			System.out.println("What do you do?");
//			System.out.println("(1): drink the river water to quench your days long thirst");
//			System.out.println("(2): continue on your journey");
//			System.out.println("(3): eat a frog frolicking within the stream");
//			int choice1 = scan.nextInt();
//			switch(choice1) {
//			case 1:
//				choice1();
//				break;
//			case 2:
//				choice2();
//				break;
//			case 3:
//				choice3();
//				break;
//			}
//			
//			//telling the player that they can continue to the javafx portion if their health is > 0, and launching the application
//			if(player.getHealth() > 0) {
//				System.out.println("You have successfully avaided your struggles in the desert, and spot a speck in the distance.");
//				System.out.println("Embarking on a journey, you set out to find what lies in the so called 'speck' which you see.");
//				System.out.println("You soon come to realize that it is a dungeon, and so you walk into it, unaware of the struggles to come.");
//				System.out.println("***THE NEXT PART OF YOUR JOURNEY HAS BEGUN!!! (open the window which has been opened by the program, use arrow keys to move)***");
//				System.out.println("Press any key to continue.");
//				scan.next();
//				launch(args);
//			//if the player's health is <= 0, tells player that they can play again
//			} else {
//				System.out.println("Your player has died!!! Thanks for playing!");
//				System.out.println("Would you like to play again? (Y/N)");
//				String choiceString = scan.next();
//				if(choiceString.equalsIgnoreCase("Y")) {
//					playAgain = true;
//				} else {
//					playAgain = false;
//				}	
//			}
//		} while(playAgain);
	}
	
	//choice1 method, which is called after the player chooses to drink the water
	public static void choice1() {
		System.out.println("\nYou feel the water slipping down your throat as if liquid gold, rejuvinating your soul.");
		System.out.println("But in the midst of your drinking, you spot a native american cheif out of the corner of your eye.");
		System.out.println("He comes marching towards you, clearly not happy with the precious water you drank out of his stream.");
		System.out.println("Behind him is a group of bulky individuals, cracking their knuckles and pounding their fists in preparation for your doom.\n");
		System.out.println("What do you do?");
		System.out.println("(1): fight will all your power back at the gang.");
		System.out.println("(2): run for your life!");
		System.out.println("(3): try to negotiate with the officers.");
		int choice = scan.nextInt();
		switch(choice) {
		case 1:
			//decreasing the player's health and endurance if they choose to fight the gang, and if they have low endurance
			if(player.getEndurance() < 5) {
				System.out.println("You crank back your arms to punch the daylight out of these people.");
				System.out.println("However, as your eyes grow increasingly more red in anger and your fists clench tighter, your endurance proves to low to fight back.");
				System.out.println("The gang pummel you into peices, leaving you a sore, miserable sack of black and blue.");
				player.setHealth(player.getHealth() - 70);
				player.setEndurance(player.getEndurance() - 2);
			}
			//decreasing the player's health and endurance if they have adequate endurance, but not as much if they have low endurance
			else {
				System.out.println("You kick the sand behind you and charge with all your might towards the gang.");
				System.out.println("Their bulging six-packs prove no struggle for you, as you effortlessly toss their bruised bodies behind you.");
				System.out.println("With only a few cuts and scratches on you, you walk triumphantly past the pile of moaning boadies and shake the dust of your hands.");
				player.setHealth(player.getHealth() - 20);
				player.setEndurance(player.getEndurance() - 1);
			}
			break;
		case 2:
			//decreasing the player's health and endurance if they choose to run from the gang, and if they have very low endurance
			if(player.getEndurance() <= 4) {
				System.out.println("You muster up all the energy left inside of you and make a run for it.");
				System.out.println("However, years of sitting on the couch playing video games have taken a toll on your physical capabilities.");
				System.out.println("Soon, the gang catch up with you and beat you until you beg them to stop.");
				player.setHealth(player.getHealth() - 70);
				player.setEndurance(player.getEndurance() - 1);
			//killing the player if they choose to run and if they have medium endurance (this is an example of randomness, since the player will not expect that a higher level of endurance could lead to their downfall
			} else if(player.getEndurance() <= 6) {
				System.out.println("Having run with all your energy for the past 20 minutes, you stop to catch your breath as you feel dribbles of sweat cascade down your face.");
				System.out.println("You seem to have lost the gang of ruthless native american killers.");
				System.out.println("However, in all the hurry you seem to have dropped your materials.");
				System.out.println("No matter how desperately you cry for help, the vast, barren land of the mojave desert does not aid in your favor.");
				System.out.println("The environment gets the best of you, taking you down to nothing but skin and bones as you slowly fade out of existence.");
				player.setHealth(0);
			//letting the player go unharmed if they have high endurance
			} else  {
				System.out.println("After running for nearly half an hour, you looking around you to see if anyone is near.");
				System.out.println("It seems you have lost the pack of ferocious natives.");
				System.out.println("You continue on your journey, unharmed by the experience you just went through.");
			}
			break;
		case 3:
			//killing the player if they choose to negotiate with the natives and if they have low endurance
			if(player.getEndurance() <= 5) {
				System.out.println("You try to make peace with the natives, explaining your difficult situation and need of resources.");
				System.out.println("However, the natives seem adamant to your plead, keeping the same expressions as they started with.");
				System.out.println("You nervously wait for their decision, but your efforts seem to be of no avail, the natives not budging at all.");
				System.out.println("Thanks your lack of skills, you wind up being a sacrifice to the gods, meeting your doom.");
				player.setHealth(0);
			//letting the player go unharmed if they have adequate endurance
			} else {
				System.out.println("As you state the situation you are in, the natives start to sympathize with your difficult disposition and let you go.");
				System.out.println("You walk unharmed from your encounter with the natives.");
			}
			break;
		}
		System.out.println("\n" + player.toString2());
	}
	
	//choice method called if the player chooses to continue on their journey
	public static void choice2() {
		System.out.println("\nYou continue on your quest for survival, searching endlessly for a sign of hope");
		System.out.println("A man approaches you, offering medicinal herbs as well as food and water.");
		System.out.println("Seeming to good to be true, you question the credibility of this person and their true intentions.");
		System.out.println("However, they assure you the only hope the best for you.");
		System.out.println("What do you do?");
		System.out.println("(1): reject the man's offer.");
		System.out.println("(2): accept the man's offer.");
		int choice = scan.nextInt();
		switch(choice) { 
		case 1:
			//killing the player if they choose to reject the man's offer and if they have low endurance
			if(player.getEndurance() < 4) {
				System.out.println("You turn down the man's 'to good to be true' offer, shaking your head to the utopia the man seems to promise.");
				System.out.println("The man reassures you that he is part of a non-profit organization, showing his dirt-covered ID to you.");
				System.out.println("Despite his effort, you stubbornly reject his offer and cross your arms.");
				System.out.println("Suddenly, the man snaps! He jumps onto you, and the rest is history, for you have met the end of your existence.");
				player.setHealth(0);
			//letting the player go free if they choose to reject the man's offer and if they have adequate endurance
			} else {
				System.out.println("You stubbornly reject the man's offer, slapping peculiarly gray bottle of water he holds in front of you onto the ground.");
				System.out.println("Threatened by your hostility, the ragged man backs off and scampers off into the vast desert.");
				System.out.println("You walk off unharmed from your encounter with the old man.");
			}
			break;
		case 2:
			//killing the player if they choose to accept the man's offer and if they have low-medium endurance
			if(player.getEndurance() < 6) {
				System.out.println("Happily accepting the man's scrumptious offerings, you dig into the mounds of food he presents to you.");
				System.out.println("However, in the midst of your greed-induced eating frenzy, you feel your throat starting to close up.");
				System.out.println("You watch helplessly as the man's once friendly smile now contorts into a evil grin.");
				System.out.println("Your throat feels sealed shut as you die an unsettling and easily avoidable death.");
				player.setHealth(0);
			//letting the player go free if they accept the man's offer and if they have high endurance
			} else {
				System.out.println("You accept the man's offerings, though only partially, as you drink only the water and walk away from him.");
				System.out.println("Although you feel slightly nauseous, your wise choice leaves you intact, only slightly hurt by the man's poisoned water.");
				System.out.println("You barely avaded the man's true intentions, which were to have you gobble up the mounds of food he presents to you, eventually meeting your doom.");
				System.out.println("You walk away unharmed from the experience.");
			}
			break;
		}
		System.out.println("\n" + player.toString2());
	}
	
	//choice which will be called when the player chooses to eat the frog
	public static void choice3() {
		System.out.println("\nAs you prepare to devour the tasty looking frog, the natives quickly snatch it out of your grasp and tell you to get a life.");
		System.out.println("In a fit of rage, you violently attack the bunch, losing the frog in the proccess.");
		System.out.println("After catching your breath, you sit down to feast on other organisms sitting in the river, being mindful that the natives are nearly knocked out, not completely dead.");
		System.out.println("What do you do?");
		System.out.println("(1): eat the delicious animals in the river.");
		System.out.println("(2): take the animals and flee.");
		int choice = scan.nextInt();
		
		switch(choice) {
		case 1:
			//killing the player regardless of their health / endurance
			System.out.println("As you sit down to eat the animals you have collected, you feel your knees give in to the delight of sitting, something you having experienced in nearly three days.");
			System.out.println("You devour the creatures, but in the corner of your eye, spot a native regaining conciousness.");
			System.out.println("You brush it off as no big deal, however, the know concious native punches you to pieces, leaving you be decomposed by the very animals you caught in the river.");
			player.setHealth(0);
			break;
		case 2:
			//letting the player go free regardless of their health / endurance if they escape with the animals
			System.out.println("You wisely take the animals and make a run for it, escaping out of the encounter with the natives as well as having a ample amount of food to survive.");
			break;
		}
		System.out.println("\n" + player.toString2());
	}
}
