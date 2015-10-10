package net.thirdy.blackmarket;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TranslucentPane extends Application {
  @Override public void start(final Stage stage) throws Exception {
    final ImageView imageView = new ImageView(
      new Image("https://upload.wikimedia.org/wikipedia/commons/b/b7/Idylls_of_the_King_3.jpg")
//      new Image("https://p7p4m6s5.ssl.hwcdn.net/image/Art/2DItems/Armours/BodyArmours/BodyStr3CUnique.png")
//      new Image("https://p7p4m6s5.ssl.hwcdn.net/image/Art/2DItems/Weapons/OneHandWeapons/Daggers/BinosKitchenKnife.png")
//      new Image("http://webcdn.pathofexile.com/image/Art/2DItems/Weapons/OneHandWeapons/OneHandMaces/Mjolner.png")
//      new Image("http://webcdn.pathofexile.com/image/Art/2DItems/Weapons/TwoHandWeapons/Staves/HegemonysEra.png")
    );
//    imageView.setFitWidth(156);
//    imageView.setFitHeight(312);
    imageView.setPreserveRatio(true);
//    imageView.setSmooth(true);
//    imageView.fitWidthProperty().bind(stage.widthProperty()); 
//    imageView.fitHeightProperty().bind(stage.heightProperty()); 
    imageView.setFitHeight(300);
    imageView.setFitWidth(228);

    final Label label = new Label("Camelot");
    final Label label1 = new Label("40% increased Global Critical Strike Chance");
    final Label label2 = new Label("30% increased Damage over Time");
    final Label label3 = new Label("Adds 57-132 Physical Damage");
    final Label label4 = new Label("43% increased Critical Strike Chance");
    final Label label5 = new Label("14% increased Global Critical Strike Multiplier");
    final Label label6 = new Label("+11% to Chaos Resistance");
    final Label label7 = new Label("On Killing a Poisoned Enemy, nearby Enemies are Poisoned");
    final Label label8 = new Label("On Killing a Poisoned Enemy, nearby Allies Regenerate Life");
    label.setStyle("-fx-text-fill: goldenrod; -fx-font-style: italic; -fx-font-weight: bold; -fx-padding: 0 0 20 0;");

    StackPane glass = new StackPane();
    StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
    glass.getChildren().addAll(new VBox(label, label1, label2, label3, label4, label5, label6, label7, label8));
    glass.setStyle("-fx-background-color: rgba(20, 20, 20, 0.5); -fx-background-radius: 10;");
    glass.setMinWidth(300);
    glass.setMinHeight(228);

    final StackPane layout = new StackPane();
    layout.getChildren().addAll(imageView, glass);
    layout.setStyle("-fx-background-color: black; -fx-foreground-color: white; -fx-font-size: 10; -fx-padding: 10;");
    stage.setScene(new Scene(layout));
    stage.show();
  }

  public static void main(String[] args) { launch(args); }
}