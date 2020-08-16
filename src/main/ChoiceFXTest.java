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

public class ChoiceFXTest extends Application {
	
	static Stage window = null;
	static Scene scene = null;
	static VBox layout = null;
	
	static Choice choice1, choice2, choice3, choice4;
	
	{
		choice1 = new Choice("This is choice1", "go to choice2", "go to choice3", "go to choice4", "-");
		choice2 = new Choice("This is choice2", "go to choice1", "go to choice3", "go to choice4", "-");
		choice3 = new Choice("This is choice3", "go to choice1", "go to choice2", "go to choice4", "-");
		choice4 = new Choice("This is choice4", "go to choice1", "go to choice2", "go to choice3", "-");
		
		choice1.setChildren(choice2, choice3, choice4);
		choice2.setChildren(choice1, choice3, choice4);
		choice3.setChildren(choice1, choice2, choice4);
		choice4.setChildren(choice1, choice2, choice3);
		
		layout = choice2.choiceLayout;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("ChoiceFXTest");
		
		scene = new Scene(layout, 800, 800);
		window.setScene(scene);
		window.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	static class Choice {
		VBox choiceLayout;
		Label description;
		Button choice1;
		Button choice2;
		Button choice3;
		Button choice4;
		
		HashMap<Button, Choice> choiceMap = new HashMap<Button, Choice>();
		
		public Choice(String descriptionString, String choice1String, String choice2String, String choice3String, String choice4String) {
			description = new Label(descriptionString);
			choice1 = new Button(choice1String);
			choice2 = new Button(choice2String);
			choice3 = new Button(choice3String);
			choice4 = new Button(choice4String);
			choiceLayout = new VBox(20);
			choiceLayout.getChildren().addAll(description, choice1, choice2, choice3, choice4);
			
			choice1.setOnAction(e -> {
				layout = choiceMap.get(choice1).choiceLayout;
				System.out.println("choice 1 chosen");
			});
			choice2.setOnAction(e -> {
				layout = choiceMap.get(choice2).choiceLayout;
				System.out.println("choice 2 chosen");
			});
			choice3.setOnAction(e -> {
				layout = choiceMap.get(choice3).choiceLayout;
				System.out.println("choice 3 chosen");
			});
			choice4.setOnAction(e -> {
				layout = choiceMap.get(choice4).choiceLayout;
				System.out.println("choice 4 chosen");
			});
		}
		
		public void setChildren(Choice childChoice1) {
			choiceMap.put(choice1, childChoice1);
		}
		
		public void setChildren(Choice childChoice1, Choice childChoice2) {
			choiceMap.put(choice1, childChoice1);
			choiceMap.put(choice2, childChoice2);
		}
		
		public void setChildren(Choice childChoice1, Choice childChoice2, Choice childChoice3) {
			choiceMap.put(choice1, childChoice1);
			choiceMap.put(choice2, childChoice2);
			choiceMap.put(choice3, childChoice3);
		}
		
		public void setChildren(Choice childChoice1, Choice childChoice2, Choice childChoice3, Choice childChoice4) {
			choiceMap.put(choice1, childChoice1);
			choiceMap.put(choice2, childChoice2);
			choiceMap.put(choice3, childChoice3);
			choiceMap.put(choice4, childChoice4);
		}
	}
}

