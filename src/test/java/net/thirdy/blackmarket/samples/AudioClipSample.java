package net.thirdy.blackmarket.samples;
/**

 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.

 * All rights reserved. Use is subject to license terms.

 */

import javafx.application.Application;

import javafx.scene.Group;

import javafx.scene.Scene;

import javafx.stage.Stage;

import javafx.event.EventHandler;

import javafx.scene.Group;

import javafx.scene.Node;

import javafx.scene.effect.Lighting;

import javafx.scene.effect.Light;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Pane;

import javafx.scene.media.AudioClip;

import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;

 

/**

 * A sample that demonstrates the basics of AudioClips.

 *

 * @see javafx.scene.media.AudioClip

 * @resource Note1.wav

 * @resource Note2.wav

 * @resource Note3.wav

 * @resource Note4.wav

 * @resource Note5.wav

 * @resource Note6.wav

 * @resource Note7.wav

 * @resource Note8.wav

 */

public final class AudioClipSample extends Application {

    private void init(Stage primaryStage) {

        Group root = new Group();

        primaryStage.setResizable(false);

        primaryStage.setScene(new Scene(root, 400,400));

 

        final AudioClip bar1Note = 

            new AudioClip(AudioClipSample.class.getResource("/audio/TheFinalBoss.mp3").toString());
//        new AudioClip("file:///F:/Mp3/Alternative/Third%20Eye%20Blind/Blue/01%20Anything.mp3");

       

        Group rectangleGroup = new Group();

 

        double xStart = 5.0;

        double xOffset = 30.0;

        double yPos = 180.0;

        double barWidth = 22.0;

        double barDepth = 7.0;

 

        Group base1Group = createRectangle(new Color(0.2, 0.12, 0.1, 1.0), 

                                           xStart + 135, yPos + 20.0, barWidth*11.5, 10.0);

        Group base2Group = createRectangle(new Color(0.2, 0.12, 0.1, 1.0), 

                                           xStart + 135, yPos - 20.0, barWidth*11.5, 10.0);

        Group bar1Group = createRectangle(Color.PURPLE,

                                          xStart + 1*xOffset, yPos, barWidth, 100.0);

        Group bar2Group = createRectangle(Color.BLUEVIOLET,

                                          xStart + 2*xOffset, yPos, barWidth, 95.0);

        Group bar3Group = createRectangle(Color.BLUE,

                                          xStart + 3*xOffset, yPos, barWidth, 90.0);

        Group bar4Group = createRectangle(Color.GREEN,

                                          xStart + 4*xOffset, yPos, barWidth, 85.0);

        Group bar5Group = createRectangle(Color.GREENYELLOW,

                                          xStart + 5*xOffset, yPos, barWidth, 80.0);

        Group bar6Group = createRectangle(Color.YELLOW,

                                          xStart + 6*xOffset, yPos, barWidth, 75.0);

        Group bar7Group = createRectangle(Color.ORANGE,

                                          xStart + 7*xOffset, yPos, barWidth, 70.0);

        Group bar8Group = createRectangle(Color.RED,

                                          xStart + 8*xOffset, yPos, barWidth, 65.0);

 

        bar1Group.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) { 
            	System.out.println(bar1Note.toString());
            	bar1Note.play(1.0); 
            	}

        });

    
 

        Light.Point light = new Light.Point();

        light.setX(-20);

        light.setY(-20);

        light.setZ(100);

 

        Lighting l = new Lighting();

        l.setLight(light);

        l.setSurfaceScale(1.0f);

 

        bar1Group.setEffect(l);

        bar2Group.setEffect(l);

        bar3Group.setEffect(l);

        bar4Group.setEffect(l);

        bar5Group.setEffect(l);

        bar6Group.setEffect(l);

        bar7Group.setEffect(l);

        bar8Group.setEffect(l);

 

        rectangleGroup.getChildren().add(base1Group);

        rectangleGroup.getChildren().add(base2Group);

        rectangleGroup.getChildren().add(bar1Group);

        rectangleGroup.getChildren().add(bar2Group);

        rectangleGroup.getChildren().add(bar3Group);

        rectangleGroup.getChildren().add(bar4Group);

        rectangleGroup.getChildren().add(bar5Group);

        rectangleGroup.getChildren().add(bar6Group);

        rectangleGroup.getChildren().add(bar7Group);

        rectangleGroup.getChildren().add(bar8Group);

        rectangleGroup.setScaleX(1.8);

        rectangleGroup.setScaleY(1.8);

        rectangleGroup.setTranslateX(55.0);

 

        Pane pane = new Pane(); 

        pane.getChildren().add(rectangleGroup);

 

        root.getChildren().add(pane);

 

    }

 

    public static Group createRectangle(Color color, double tx, double ty, double sx, double sy) {

        Group squareGroup = new Group();

        Rectangle squareShape = new Rectangle(1.0, 1.0);

        squareShape.setFill(color);

        squareShape.setTranslateX(-0.5);

        squareShape.setTranslateY(-0.5);

        squareGroup.getChildren().add(squareShape);

        squareGroup.setTranslateX(tx);

        squareGroup.setTranslateY(ty);

        squareGroup.setScaleX(sx);

        squareGroup.setScaleY(sy);

        return squareGroup;

    }

 

    public double getSampleWidth() { return 400; }

 

    public double getSampleHeight() { return 400; }

 

    @Override public void start(Stage primaryStage) throws Exception {

        init(primaryStage);

        primaryStage.show();

    }

    public static void main(String[] args) { launch(args); }

}