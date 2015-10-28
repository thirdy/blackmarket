package net.thirdy.blackmarket.samples;

import java.util.Arrays;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
      primaryStage.setTitle("Hello World!");
      net.thirdy.blackmarket.fxcontrols.AutoCompleteTextField<String> textField = new net.thirdy.blackmarket.fxcontrols.AutoCompleteTextField<>(
    		  Arrays.asList("apple", "ball", "cat", "doll", "elephant", "fight", "georgeous", "height", "ice",
    					"jug", "aplogize", "bank", "call", "done", "ego", "finger", "giant", "hollow", "internet", "jumbo",
    					"kilo", "lion", "for", "length", "primary", "stage", "scene", "zoo", "jumble", "auto", "text", "root",
    					"box", "items", "hip-hop", "himalaya", "nepal", "kathmandu", "kirtipur", "everest", "buddha", "epic",
    					"hotel", 
    					"loooooooooooooo000000000000000000f000000000000000oooong",
    					"loooooooooooooo00000000d000000000000f0000000000000oooong",
    					"loooooooooooooo000000000000000000000d000000000000oooong",
    					"loooooooooooooo00000000000d000000d0000000000000000oooong",
    					"loooooooooooooo0000000000d00f000000000000000000000oooong",
    					"loooooooooooooo0000000000f00000000000000000000000oooong",
    					"loooooooooooooo0000000000000f00000000000000000000oooong",
    					"loooooooooooooo0000000x00000000000000000000000000oooong",
    					"loooooooooooooo00000000000000d0000000000000000000oooong",
    					"loooooooooooooo000000x000000000000000000000000000oooong",
    					"loooooooooooooo00000000000f0000000000000000000000oooong",
    					"loooooooooooooo000000x000000000000000000000000000oooong",
    					"loooooooooooooo00000000000f0000000000000000000000oooong",
    					"loooooooooooooo0000000000x00000000000000000000000oooong",
    					"loooooooooooooo00000000000x0000000000000000000000oooong",
    					"loooooooooooooo000000000000000000000000000000000oooong",
    					"loooooooooooooo000x000000000x000000000000000000000oooong",
    					"looooooooxoooooo000000000000000f000000000000000000oooong",
    					"loooooooooooooo000000000000x000000000000000000000oooong",
    					"loooooooooooooo00000000x000000000000000000000000oooong",
    					"loooooooooooooox0000000000000000000dg0000000000000oooong",
    					"loooooooooooooo000000000000000x000000000000000000oooong",
    					"x"
    					), 450
    		  );
      
      StackPane root = new StackPane();
      root.getChildren().add(new VBox(new TextField("Other Field"), textField, new Button("Clickme")));
      primaryStage.setScene(new Scene(root, 500, 250));
      primaryStage.show();
	}
	
	  public static void main(String[] args) {
		  Application.launch(Main.class, args);
	  }
	  
}