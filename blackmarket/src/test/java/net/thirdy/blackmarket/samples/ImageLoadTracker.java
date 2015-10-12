package net.thirdy.blackmarket.samples;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
/** Demo of tracking the loading of background images in JavaFX */
public class ImageLoadTracker extends Application {
  private static final String SHALLOT_IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/8/83/JWW_TheLadyOfShallot_1888.jpg";
  private static final String BROKEN_IMAGE_URL  = "http://error.org/imagedoesnotexist.jpg";
  private static final Image  ERROR_IMAGE       = new Image("http://icons.iconarchive.com/icons/visualpharm/must-have/256/Cancel-icon.png"); // linkware icon from http://www.visualpharm.com/

  @Override public void start(final Stage stage) {
    // a label for reporting the current image loading status.
    final Label statusLabel = new Label();
    
    // a progressbar to monitor the progress of background image loading.
    final Label progressLabel = new Label("Loading progress");
    final ProgressBar progressBar = new ProgressBar();
    
    // a view to hold the loaded image.
    final ImageView imageView = new ImageView();
    
    // radio buttons to control image loading.
    final RadioButton shallotImageSelection = new RadioButton("Load a nice image");
    final RadioButton brokenImageSelection  = new RadioButton("Load a broken image");
    shallotImageSelection.setUserData(SHALLOT_IMAGE_URL);
    brokenImageSelection.setUserData(BROKEN_IMAGE_URL);

    // a toggle group to load images when the user toggles between the toggles.
    ToggleGroup imageToggle = new ToggleGroup();
    shallotImageSelection.setToggleGroup(imageToggle);
    brokenImageSelection.setToggleGroup(imageToggle);
    imageToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      @Override public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        // reset the error text.
        statusLabel.setText("Loading image . . .");
        statusLabel.setStyle("-fx-text-fill: silver;");

        // load an image in the background.
        final String newImageUrl = (String) newValue.getUserData();
        final Image newImage = new Image(newImageUrl, true);
        imageView.setImage(newImage);
        
        // track the image's error property.        
        newImage.errorProperty().addListener(new ChangeListener<Boolean>() {
          @Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean imageError) {
            if (imageError) {
              statusLabel.setText("Oh-oh there was an error loading: " + newImageUrl);
              statusLabel.setStyle("-fx-text-fill: firebrick;");
              imageView.setImage(ERROR_IMAGE);
            }
          }
        });

        // track the image's loading progress.
        progressBar.progressProperty().bind(newImage.progressProperty());
        newImage.progressProperty().addListener(new ChangeListener<Number>() {
          @Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number progress) {
            if ((Double) progress == 1.0 && ! newImage.isError()) {
              statusLabel.setText("Loading complete");
              statusLabel.setStyle("-fx-text-fill: forestgreen;");
            }
          }
        });
      }
    });
    
    // layout the scene.
    StackPane imageLayout = StackPaneBuilder.create().children(imageView).build();
    VBox layout = new VBox(10);
    layout.getChildren().addAll(
      shallotImageSelection, 
      brokenImageSelection, 
      statusLabel, 
      HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).children(progressLabel, progressBar).build(), 
      imageLayout
    );
    VBox.setVgrow(imageLayout, Priority.ALWAYS);
    progressBar.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(progressBar, Priority.ALWAYS);
    layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
    stage.setTitle("Image Loading Tracker");
    stage.setScene(new Scene(layout, 1315, 1000));
    stage.show();

    // trigger the loading of a good image.
    shallotImageSelection.fire();
  }
 
  public static void main(String[] args) { launch(); }
}