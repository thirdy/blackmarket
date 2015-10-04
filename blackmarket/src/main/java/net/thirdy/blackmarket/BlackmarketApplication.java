/*
 * Copyright (C) 2015 thirdy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.thirdy.blackmarket;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.thirdy.blackmarket.controls.SlidingPane;
import net.thirdy.blackmarket.controls.WindowButtons;
import net.thirdy.blackmarket.controls.WindowResizeButton;

/**
 * @author thirdy
 *
 */
public class BlackmarketApplication extends Application {

	private static BlackmarketApplication blackmarketApplication;
	
    private Scene scene;
    private BorderPane root;
    private ToolBar toolBar;

    private WindowResizeButton windowResizeButton;

    private double mouseDragOffsetX = 0;
    private double mouseDragOffsetY = 0;
    
	/**
	 * Get the singleton instance of BlackmarketApplication
	 */
	public static BlackmarketApplication getBlackmarketApplication() {
		return blackmarketApplication;
	}

	@Override
	public void start(final Stage stage) {
		blackmarketApplication = this;
		stage.setTitle("Blackmarket");
		
		// create root stack pane that we use to be able to overlay proxy dialog
        StackPane layerPane = new StackPane();
        
        stage.initStyle(StageStyle.UNDECORATED);
        // create window resize button
        windowResizeButton = new WindowResizeButton(stage, 1020,700);
        // create root
        root = new BorderPane() {
            @Override protected void layoutChildren() {
                super.layoutChildren();
                windowResizeButton.autosize();
                windowResizeButton.setLayoutX(getWidth() - windowResizeButton.getLayoutBounds().getWidth());
                windowResizeButton.setLayoutY(getHeight() - windowResizeButton.getLayoutBounds().getHeight());
            }
        };
        root.getStyleClass().add("application");
        root.setId("root");
        layerPane.setDepthTest(DepthTest.DISABLE);
        layerPane.getChildren().add(root);
        
        boolean is3dSupported = false;
		scene = new Scene(layerPane, 1020, 700, is3dSupported);
		
		scene.getStylesheets().add(this.getClass().getResource("ensemble2.css").toExternalForm());
		
		// create main toolbar
        toolBar = new ToolBar();
        toolBar.setId("mainToolBar");
        
        Image logoImg = new Image(this.getClass().getResourceAsStream("/images/blackmarket-logo.png"));

        int x = 15 ;
        int y = 0 ;
        int width = 178;
        int height = (int) logoImg.getHeight() ;
        logoImg = new WritableImage(logoImg.getPixelReader(), x, y, width, height);

        ImageView logo = new ImageView(logoImg);
//        logo.setFitWidth(.5);
//        logo.setPreserveRatio(true);
//        logo.setFitHeight(36);
        
        HBox.setMargin(logo, new Insets(0,0,0,1));
        toolBar.getItems().add(logo);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getItems().add(spacer);
//        Button highlightsButton = new Button();
//        highlightsButton.setId("highlightsButton");
//        highlightsButton.setMinSize(120, 66);
//        highlightsButton.setPrefSize(120, 66);
//        highlightsButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent event) {
//                goToPage(Pages.HIGHLIGHTS);
//            }
//        });
//        toolBar.getItems().add(highlightsButton);
//        Button newButton = new Button();
//        newButton.setId("newButton");
//        newButton.setMinSize(120,66);
//        newButton.setPrefSize(120,66);
//        newButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent event) {
//                goToPage(Pages.NEW);
//            }
//        });
//        toolBar.getItems().add(newButton);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        toolBar.getItems().add(spacer2);
//        ImageView searchTest = new ImageView(new Image(this.getClass().getResourceAsStream("/images/search-text.png")));
//        toolBar.getItems().add(searchTest);
//        SearchBox searchBox = new SearchBox();
//        HBox.setMargin(searchBox, new Insets(0,5,0,0));
//        toolBar.getItems().add(searchBox);
        toolBar.setPrefHeight(66);
        toolBar.setMinHeight(66);
        toolBar.setMaxHeight(66);
        GridPane.setConstraints(toolBar, 0, 0);
		// add close min max
		final WindowButtons windowButtons = new WindowButtons(stage);
//		Text versionText = new Text("v0.4");
//		toolBar.getItems().add(versionText);
		toolBar.getItems().add(windowButtons);
		// add window header double clicking
		toolBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					windowButtons.toogleMaximized();
				}
			}
		});
		// add window dragging
		toolBar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseDragOffsetX = event.getSceneX();
				mouseDragOffsetY = event.getSceneY();
			}
		});
		toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!windowButtons.isMaximized()) {
					stage.setX(event.getScreenX() - mouseDragOffsetX);
					stage.setY(event.getScreenY() - mouseDragOffsetY);
				}
			}
		});
		
		this.root.setTop(toolBar);
		
		ControlPane controlPane = new ControlPane();
		SlidingPane searchPane = new SlidingPane(250, 40, controlPane);
		Button showCollapseButton = searchPane.getControlButton();
		controlPane.installShowCollapseButton(showCollapseButton);
		searchPane.setId("searchPane");
		
		this.root.setBottom(searchPane);
		
//		this.root.setCenter(backButton);
		

        // add window resize button so its on top
		windowResizeButton.setManaged(false);
        this.root.getChildren().add(windowResizeButton);

        // show stage
        stage.setScene(scene);
        stage.show();
	}
}