package net.thirdy.blackmarket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.thirdy.blackmarket.controls.SlidingPane;

/** Example of a sidebar that slides in and out of view */
public class SlideOut extends Application {
  private static final String[] lyrics = {
    "And in the end,\nthe love you take,\nis equal to\nthe love\nyou make.",
    "She came in through\nthe bathroom window\nprotected by\na silver\nspoon.",
    "I've got to admit\nit's getting better,\nA little better\nall the time."
  };

  private static final String[] locs = {
    "http://www.youtube.com/watch?v=osAA8q86COY&feature=player_detailpage#t=367s",
    "http://www.youtube.com/watch?v=IM2Ttov_zR0&feature=player_detailpage#t=30s",
    "http://www.youtube.com/watch?v=Jk0dBZ1meio&feature=player_detailpage#t=25s"
  };
  WebView webView;
  public static void main(String[] args) throws Exception { launch(args); }
  public void start(final Stage stage) throws Exception {
    stage.setTitle("Slide out YouTube demo");

    // create a WebView to show to the right of the SideBar.
//    webView = new WebView();
//    webView.setPrefSize(800, 600);

    // create a sidebar with some content in it.
//    final Pane lyricPane = createSidebarContent();
//    SlidingPane sidebar = new SlidingPane(250, 1);
////    VBox.setVgrow(lyricPane, Priority.ALWAYS);
//
//    // layout the scene.
//    final BorderPane layout = new BorderPane();
//    Pane mainPane = VBoxBuilder.create().spacing(10)
//      .children(
//        sidebar.getControlButton()
//      ).build();
//    layout.setBottom(sidebar);
//    layout.setCenter(mainPane);
//
//    // show the scene.
//    Scene scene = new Scene(layout);
//    scene.getStylesheets().add(getClass().getResource("slideout.css").toExternalForm());
//    stage.setScene(scene);
//    stage.show();
  }

//  private BorderPane createSidebarContent() {// create some content to put in the sidebar.
//    final Text lyric = new Text();
//    lyric.getStyleClass().add("lyric-text");
//    final Button changeLyric = new Button("New Song");
//    changeLyric.getStyleClass().add("change-lyric");
//    changeLyric.setMaxWidth(Double.MAX_VALUE);
//    changeLyric.setOnAction(new EventHandler<ActionEvent>() {
//      int lyricIndex = 0;
//      @Override public void handle(ActionEvent actionEvent) {
//        lyricIndex++;
//        if (lyricIndex == lyrics.length) {
//          lyricIndex = 0;
//        }
//        lyric.setText(lyrics[lyricIndex]);
////        webView.getEngine().load(locs[lyricIndex]);
//      }
//    });
//    changeLyric.fire();
//    final BorderPane lyricPane = new BorderPane();
//    lyricPane.setCenter(lyric);
//    lyricPane.setBottom(changeLyric);
//    return lyricPane;
//  }
  
}