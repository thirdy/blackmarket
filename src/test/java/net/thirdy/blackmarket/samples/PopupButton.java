package net.thirdy.blackmarket.samples;
import java.util.Random;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class PopupButton extends Application {
  private static final Random    random  = new Random();
  private static final String[]  animals = { "dog", "cat", "rhino", "hippo", "toad" };
  private static final ImageView wiz     = new ImageView(
    "http://icons.iconarchive.com/icons/aha-soft/free-large-boss/128/Wizard-icon.png"
  ); // icon: Linkware (Backlink to http://www.aha-soft.com required)
  
  public static void main(String[] args) throws Exception { launch(args); }
  
  @Override public void start(final Stage stage) throws Exception {
    // create a popup trigger menu button and it's associated popup content.
    final MenuItem wizPopup = new MenuItem();
    wizPopup.setGraphic(createPopupContent(wiz));

    final MenuButton popupButton = new MenuButton("frobozz");
    popupButton.getItems().setAll(
      wizPopup            
    );

    // show the scene.
    final VBox layout = new VBox(20);
    layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
    layout.getChildren().addAll(popupButton);
    Scene scene = new Scene(layout);
    scene.getStylesheets().add(getClass().getResource("contextcolor.css").toExternalForm());
    stage.setScene(scene);
    stage.show();
  }

  private VBox createPopupContent(final ImageView wiz) {
    final Label unfortunateEvent = new Label();
    unfortunateEvent.setWrapText(true);
    unfortunateEvent.setTextAlignment(TextAlignment.CENTER);
    unfortunateEvent.setMaxWidth(wiz.getImage().getWidth());
    final Button wand = new Button("xyzzy");
    final VBox wizBox = new VBox(5);
    wizBox.setAlignment(Pos.CENTER);
    wizBox.getChildren().setAll(
      wiz,
      wand,
      unfortunateEvent
    );
    wand.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent t) {
        unfortunateEvent.setText(
          "Zap! The wizard has turned you into a\n" + animals[random.nextInt(animals.length)]
        );
      }
    });
    
    return wizBox;
  }
}