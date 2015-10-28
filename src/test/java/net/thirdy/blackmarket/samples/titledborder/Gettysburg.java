package net.thirdy.blackmarket.samples.titledborder;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Displays the Gettysburg Address */
public class Gettysburg extends Application {
  private static final String TITLE   = "The Gettysburg Address";
  private static final String ADDRESS = "Four score and seven years ago our fathers brought forth, on this continent, a new nation, conceived in Liberty, and dedicated to the proposition that all men are created equal.\n\nNow we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. We are met on a great battle-field of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.\n\nBut, in a larger sense, we can not dedicate—we can not consecrate—we can not hallow—this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us—that from these honored dead we take increased devotion to that cause for which they here gave the last full measure of devotion—that we here highly resolve that these dead shall not have died in vain—that this nation, under God, shall have a new birth of freedom—and that government of the people, by the people, for the people, shall not perish from the earth.";
  private static final String DATE    = "November 19, 1863.";
  private static final String ICON    = Gettysburg.class.getPackage().getName().replace(".", "/") + "/scroll.png";
  private static final String PAPER   = Gettysburg.class.getPackage().getName().replace(".", "/") + "/crumpled_paper_273118.JPG";

  public static void main(String[] args) { launch(args); }

  /** render the application on a stage */
  @Override public void start(Stage stage) {
    // place the address content in a bordered title pane.
    Pane titledContent = new BorderedTitledPane(TITLE, getContent());
    titledContent.getStyleClass().add("titled-address");
    titledContent.setPrefSize(800, 745);

    // make some crumpled paper as a background.
    final Image paper = new Image(PAPER);
    final ImageView paperView = new ImageView(paper);
    ColorAdjust colorAdjust = new ColorAdjust(0, -.2, .2, 0);
    paperView.setEffect(colorAdjust);

    // place the address content over the top of the paper.
    StackPane stackedContent = new StackPane();
    stackedContent.getChildren().addAll(paperView, titledContent);

    // manage the viewport of the paper background, to size it to the content.
    paperView.setViewport(new Rectangle2D(0, 0, titledContent.getPrefWidth(), titledContent.getPrefHeight()));
    stackedContent.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
      @Override public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldValue, Bounds newValue) {
        paperView.setViewport(new Rectangle2D(
          newValue.getMinX(), newValue.getMinY(),
          Math.min(newValue.getWidth(), paper.getWidth()), Math.min(newValue.getHeight(), paper.getHeight())
        ));
      }
    });

    // blend the content into the paper and make it look old.
    titledContent.setMaxWidth(paper.getWidth());
    titledContent.setEffect(new SepiaTone());
    titledContent.setBlendMode(BlendMode.MULTIPLY);

    // configure and display the scene and stage.
    Scene scene = new Scene(stackedContent);
    scene.getStylesheets().add(getClass().getResource("gettysburg.css").toExternalForm());
    stage.setTitle(TITLE);
    stage.getIcons().add(new Image(ICON));
    stage.setScene(scene);
    stage.setMinWidth(600); stage.setMinHeight(500);
    stage.show();

    // make the scrollbar in the address scroll pane hide when it is not needed.
    Node lookup = titledContent.lookup(".address > .scroll-pane");
	makeScrollFadeable(lookup);
  }

  /** @return the content of the address, with a signature and portrait attached. */
  private Pane getContent() {
    final VBox content = new VBox();
    content.getStyleClass().add("address");
    final Label address = new Label(ADDRESS);
    address.setWrapText(true);
    ScrollPane addressScroll = makeScrollable(address);

    final ImageView signature = new ImageView(); signature.setId("signature");
    signature.setFitWidth(320);
    signature.setFitHeight(50);
    final ImageView lincolnImage = new ImageView(); lincolnImage.setId("portrait");
    VBox.setVgrow(addressScroll, Priority.ALWAYS);

    final Region spring = new Region();
    HBox.setHgrow(spring, Priority.ALWAYS);
    final Node alignedSignature = HBoxBuilder.create().children(spring, signature).build();
    Label date = new Label(DATE);

    content.getChildren().addAll(
        lincolnImage,
        addressScroll,
        alignedSignature,
        date
    );

    return content;
  }

  /** @return content wrapped in a vertical, pannable scroll pane. */
  private ScrollPane makeScrollable(final Control content) {
    final ScrollPane scroll = new ScrollPane();
    scroll.setContent(content);
    scroll.setPannable(true);
    scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scroll.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
      @Override public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds, Bounds newBounds) {
        content.setPrefWidth(newBounds.getWidth() - 10);
      }
    });

    return scroll;
  }

  /** adds a hiding effect on the scroll bar of a scrollable node so that it is not seen unless
   *  the scrollable node is being hovered with a mouse */
  private void makeScrollFadeable(final Node scroll) {
    final Node scrollbar = scroll.lookup(".scroll-bar:vertical");
    System.out.println(scroll);
    final FadeTransition fader = new FadeTransition(Duration.seconds(1), scrollbar);
    fader.setFromValue(1); fader.setToValue(0);
    fader.setOnFinished(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent actionEvent) {
        if (!scroll.getStyleClass().contains("hide-thumb")) {
          scroll.getStyleClass().add("hide-thumb");
        }
      }
    });
    if (!scroll.isHover()) {
      scroll.getStyleClass().add("hide-thumb");
    }
    scroll.hoverProperty().addListener(new ChangeListener<Boolean>() {
      @Override public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasHover, Boolean isHover) {
        if (!isHover) {
          fader.playFromStart();
        } else {
          fader.stop();
          scrollbar.setOpacity(1);
          scroll.getStyleClass().remove("hide-thumb");
        }
      }
    });
  }

  /** Places content in a bordered pane with a title. */
  class BorderedTitledPane extends StackPane {
    BorderedTitledPane(String titleString, Node content) {
      Label title = new Label("  " + titleString + "  ");
      title.getStyleClass().add("bordered-titled-title");
      StackPane.setAlignment(title, Pos.TOP_CENTER);

      StackPane contentPane = new StackPane();
      content.getStyleClass().add("bordered-titled-content");
      contentPane.getChildren().add(content);

      getStyleClass().add("bordered-titled-border");
      getChildren().addAll(title, contentPane);
    }
  }

}