package net.thirdy.blackmarket;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.thirdy.blackmarket.core.PoeTradeHttpClient;


public class MainApp extends Application {
	
	// TODO, is this okay to be a single instance?
	private static PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
	public static PoeTradeHttpClient getPoeTradeHttpClient() {
		return poeTradeHttpClient;
	}

	@Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene_simple.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Blackmarket " + AppConfig.VERSION);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }

}
