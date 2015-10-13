package net.thirdy.blackmarket.samples;
/**

 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.

 * All rights reserved. Use is subject to license terms.

 */

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.stage.Stage;
import net.thirdy.blackmarket.fxcontrols.IntegerTextField;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;

 

/**

 * This sample demonstrates styling toggle buttons with CSS.

 *

 * @see javafx.scene.control.ToggleButton

 * @related controls/buttons/ToggleButton

 * @resource PillButton.css

 * @resource center-btn.png

 * @resource center-btn-selected.png

 * @resource left-btn.png

 * @resource left-btn-selected.png

 * @resource right-btn.png

 * @resource right-btn-selected.png

 */

public class PillButtonSample extends Application {

 

    private void init(Stage primaryStage) {

        Group root = new Group();

        primaryStage.setScene(new Scene(root));

 

//        String pillButtonCss = PillButtonSample.class.getResource("PillButton.css").toExternalForm();

 

        // create 3 toggle buttons and a toogle group for them

        ToggleButton tb1 = new ToggleButton("Left Button");

        tb1.setId("pill-left");

        ToggleButton tb2 = new ToggleButton("Center Button");

        tb2.setId("pill-center");

        ToggleButton tb3 = new ToggleButton("Right Button");

        tb3.setId("pill-right");

 

        ToggleGroup group = new ToggleGroup();

        tb1.setToggleGroup(group);

        tb2.setToggleGroup(group);

        tb3.setToggleGroup(group);

        // select the first button to start with

        group.selectToggle(tb1);

 

        HBox hBox = new HBox();

        IntegerTextField tf = new IntegerTextField("x");
        
		Button btn = new Button();
		btn.setOnAction(e -> System.out.println(tf.getOptionalValue()));
		hBox.getChildren().addAll(tb1, tb2, tb3, tf, btn);

//        hBox.getStylesheets().add(pillButtonCss);

        root.getChildren().add(hBox);

    }

 

    @Override public void start(Stage primaryStage) throws Exception {

        init(primaryStage);

        primaryStage.show();

    }

    public static void main(String[] args) { launch(args); }

}