package net.thirdy.blackmarket.samples;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** demonstrates various ways to fill a constrained region with an image */
public class ImageFill extends Application {
  private static final String FX_EXPERIENCE_LOGO_URL = "http://fxexperience.com/wp-content/uploads/2010/06/logo.png";
  final ObjectProperty<Image> poster = new SimpleObjectProperty<Image>(new Image(FX_EXPERIENCE_LOGO_URL));  
  public static void main(String[] args) { launch(args); }
  @Override public void start(Stage primaryStage) {
    // create a pane which constrain the size of the displayed image.
    StackPane constrainingPane = new StackPane();
    // just placing the image in the constraining pane will make the constrainingpane go above it's max size.
    // constrainingPane.getChildren().add(new ImageView(poster.get()));  

    // to get around this, you could embed the image in a scrollpane.
    // constrainingPane.getChildren().add(getScrollPane(poster));
    
    // or perhaps preferably, you could use a slightly customized standard pane 
    // and style it's background to display the image.
    constrainingPane.getChildren().add(
      new ImagePane(FX_EXPERIENCE_LOGO_URL, "-fx-background-size: contain; -fx-background-repeat: no-repeat;")
    );
    constrainingPane.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-border-insets: -2;");
    
    // layout the scene.
    StackPane layout = new StackPane();
    layout.getChildren().add(constrainingPane);
    layout.setStyle("-fx-background-color: whitesmoke;");
    Scene scene = new Scene(layout, 800, 600);

    // clamp the pane to the scene size.
    constrainingPane.maxWidthProperty().bind(scene.widthProperty().divide(2));
    constrainingPane.minWidthProperty().bind(scene.widthProperty().divide(2));
    constrainingPane.maxHeightProperty().bind(scene.heightProperty().divide(2));
    constrainingPane.minHeightProperty().bind(scene.heightProperty().divide(2));

    // show the scene.
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  // size a replaceable image by placing it in a scrollpane.
  private Node getScrollPane(final ObjectProperty<Image> poster) {
    return new ScrollPane() {{
      final ReadOnlyDoubleProperty widthProperty = widthProperty();
      final ReadOnlyDoubleProperty heightProperty = heightProperty();

      setHbarPolicy(ScrollBarPolicy.NEVER);
      setVbarPolicy(ScrollBarPolicy.NEVER);

      setContent(new ImageView() {{
        imageProperty().bind(poster);
        setPreserveRatio(true);
        setSmooth(true);

        fitWidthProperty().bind(widthProperty);
        fitHeightProperty().bind(heightProperty);
      }});
    }};      
  }
}

/** A pane with an image background */
class ImagePane extends Pane {
  // size an image by placing it in a pane.
  ImagePane(String imageLoc) {
    this(imageLoc, "-fx-background-size: cover; -fx-background-repeat: no-repeat;");
  }

  // size an image by placing it in a pane.
  ImagePane(String imageLoc, String style) {
    this(new SimpleStringProperty(imageLoc), new SimpleStringProperty(style));
  }

  // size a replacable image in a pane and add a replaceable style.
  ImagePane(StringProperty imageLocProperty, StringProperty styleProperty) {
    styleProperty().bind(
    new SimpleStringProperty("-fx-background-image: url(\"")
        .concat(imageLocProperty)
        .concat(new SimpleStringProperty("\");"))
        .concat(styleProperty)    
    );
  }
}
