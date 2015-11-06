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

import java.awt.Toolkit;
import java.util.regex.Pattern;

import javax.sound.sampled.LineUnavailableException;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jexiletools.es.ExileToolsSearchClient;
import io.jexiletools.es.ExileToolsSearchClient.ExileToolsSearchResult;
import io.jexiletools.es.model.json.ExileToolsHit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import net.thirdy.blackmarket.controls.ControlPane;
import net.thirdy.blackmarket.controls.Dialogs;
import net.thirdy.blackmarket.controls.ItemGridCell;
import net.thirdy.blackmarket.fxcontrols.SlidingPane;
import net.thirdy.blackmarket.fxcontrols.WindowButtons;
import net.thirdy.blackmarket.fxcontrols.WindowResizeButton;
import net.thirdy.blackmarket.service.ExileToolsLadderService;
import net.thirdy.blackmarket.service.ExileToolsLastIndexUpdateService;
import net.thirdy.blackmarket.service.ExileToolsService;
import net.thirdy.blackmarket.service.Ladder;
import net.thirdy.blackmarket.util.ImageCache;
import net.thirdy.blackmarket.util.LangContants;
import net.thirdy.blackmarket.util.SoundUtils;

/**
 * @author thirdy
 *
 */
public class BlackmarketApplication extends Application {

	public static final int ITEM_GRID_CELL_WIDTH = 380;
	public static final int ITEM_GRID_CELL_HEIGHT = 250;

//	private static final int WINDOW_HEIGHT = 738;
//	private static final int WINDOW_WIDTH = 1166;
	
	
	private static final String BLACK_MARKET_API_KEY = "4b1ccf2fce44441365118e9cd7023c38";
	public static final String VERSION = "Version: 0.6.0" + (Main.DEVELOPMENT_MODE ? " [DEVELOPMENT MODE]" : LangContants.STRING_EMPTY);

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static BlackmarketApplication blackmarketApplication;
	private static ExileToolsSearchClient exileToolsESClient;

	private Scene scene;
	private BorderPane root;
	private ToolBar toolBar;

	private WindowResizeButton windowResizeButton;

	private double mouseDragOffsetX = 0;
	private double mouseDragOffsetY = 0;

	// private final ObservableList<ExileToolsHit> searchResultCurrentHits =
	// FXCollections.observableArrayList();

	public static BlackmarketApplication getBlackmarketApplication() {
		return blackmarketApplication;
	}

	public static ExileToolsSearchClient getExileToolsESClient() {
		return exileToolsESClient;
	}
	
	private final ExileToolsLadderService exileToolsLadderService = new ExileToolsLadderService();
	private final ExileToolsService searchService = new ExileToolsService();
	private final ExileToolsLastIndexUpdateService lastIndexUpdateService = new ExileToolsLastIndexUpdateService();

	private GridView<ExileToolsHit> searchResultsPane;

	private Label versionText;
	private Label indexerLastUpdateText;

	private ControlPane controlPane;
	private Button collapseButton;
	private SlidingPane searchPane;

	@Override
	public void stop() throws Exception {
		super.stop();
		exileToolsESClient.shutdown();
	}

	@Override
	public void start(final Stage stage) {
		blackmarketApplication = this;
		exileToolsESClient = new ExileToolsSearchClient(BLACK_MARKET_API_KEY);
		stage.setTitle("Blackmarket");
		stage.getIcons().add(ImageCache.getInstance().get("/Blackmarket-logo.png"));

		// create root stack pane that we use to be able to overlay proxy dialog
		StackPane layerPane = new StackPane();

		Region veilOfTheNight = new Region();

		veilOfTheNight.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");

		ProgressIndicator progressIndicator = new ProgressIndicator(-1.0f);
		progressIndicator.setMaxSize(150, 150);

		stage.initStyle(StageStyle.UNDECORATED);
		// create window resize button
		windowResizeButton = new WindowResizeButton(stage, 200, 200);
		// create root
		root = new BorderPane() {
			@Override
			protected void layoutChildren() {
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

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//		boolean is3dSupported = false;
//		, WINDOW_WIDTH, WINDOW_HEIGHT, is3dSupported
		scene = new Scene(layerPane);
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
		stage.setWidth(screenWidth - (screenWidth * 0.15));
		stage.setHeight(screenHeight);

		scene.getStylesheets().add(this.getClass().getResource("/css/blackmarket.css").toExternalForm());

		// create main toolbar
		setupToolbar(stage);

		this.root.setTop(toolBar);

		controlPane = new ControlPane(this::searchHandler);
		searchPane = new SlidingPane(controlPane);
		searchPane.expandedHeightProperty().bind(scene.heightProperty().subtract(98.0));
		collapseButton = searchPane.getControlButton();

		scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
//			if ((keyEvent.isControlDown() || keyEvent.isAltDown())
//					&& (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN)) {
//				showCollapseButton.fire();
//			}
			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.SPACE) {
				collapseButton.fire();
			}

			if (keyEvent.getCode() == KeyCode.ENTER
					&& keyEvent.isControlDown()
					&& !progressIndicator.isVisible() 
					&& searchPane.isExpanded()) {
				controlPane.fireSearchEvent();
			}
		});

		controlPane.installCollapseButton(collapseButton);
		controlPane.getBtnDurianMode().setOnAction(e -> toggleDurianService());
		controlPane.getBtnDurianMode().disableProperty().bind(exileToolsLadderService.sleepingProperty().not());
		controlPane.getBtnSearch().disableProperty().bind(exileToolsLadderService.sleepingProperty().not());
		controlPane.getLblLadderServiceStatus().textProperty().bind(exileToolsLadderService.messageProperty());
		controlPane.getLblLadderServiceStatus().tooltipProperty().get().textProperty()
			.bind(Bindings.createStringBinding(() -> {
				Ladder ladder = exileToolsLadderService.resultProperty().get();
				return ladder != null ? ladder.leagueSizes() : LangContants.STRING_EMPTY;
			}, exileToolsLadderService.resultProperty()));
		searchPane.setId("searchPane");

		AnchorPane centerPane = new AnchorPane();

		// DoubleProperty wProp = DoubleProperty.

		searchResultsPane = new GridView<>();
		searchResultsPane.setHorizontalCellSpacing(9);
		searchResultsPane.setVerticalCellSpacing(5);
		// searchResultsPane.setB
		// searchResultsPane.setCellHeight(300);
//		searchResultsPane.setCellWidth(ITEM_GRID_CELL_WIDTH);
		DoubleBinding oneThirdWidthBinding = Bindings.createDoubleBinding(
				() -> (centerPane.getWidth() / 3) - 25, centerPane.widthProperty());
		searchResultsPane.cellWidthProperty().bind(oneThirdWidthBinding);
		searchResultsPane.setCellHeight(ITEM_GRID_CELL_HEIGHT);
		searchResultsPane.setCellFactory(new Callback<GridView<ExileToolsHit>, GridCell<ExileToolsHit>>() {
			public GridCell<ExileToolsHit> call(GridView<ExileToolsHit> gridView) {
				return new ItemGridCell();
			}
		});

		AnchorPane.setTopAnchor(searchResultsPane, 0.0);
		AnchorPane.setLeftAnchor(searchResultsPane, 0.0);
		AnchorPane.setRightAnchor(searchResultsPane, 0.0);
		AnchorPane.setBottomAnchor(searchResultsPane, 0.0);

		AnchorPane.setBottomAnchor(searchPane, 10.0);
		AnchorPane.setLeftAnchor(searchPane, 10.0);
		AnchorPane.setRightAnchor(searchPane, 23.0);
		centerPane.getChildren().addAll(searchResultsPane, searchPane);

		Label progressIndicatorLabel = new Label();

		progressIndicator.progressProperty().bind(searchService.progressProperty());
		progressIndicatorLabel.textProperty().bind(searchService.messageProperty());
		veilOfTheNight.visibleProperty().bind(searchService.runningProperty());
		progressIndicatorLabel.visibleProperty().bind(searchService.runningProperty());
		progressIndicator.visibleProperty().bind(searchService.runningProperty());

		searchService.setOnSucceeded(e -> searchSucceeded());

		searchService.setOnFailed(e -> Dialogs.showExceptionDialog(searchService.getException()));

		StackPane centerStackPane = new StackPane(centerPane, veilOfTheNight, progressIndicator,
				progressIndicatorLabel);

		this.root.setCenter(centerStackPane);

		// add window resize button so its on top
		windowResizeButton.setManaged(false);
		this.root.getChildren().add(windowResizeButton);

		// show stage
		stage.setScene(scene);
		stage.show();
		durianTimer.setOnSucceeded(e -> durianTimerSucceeded());
		lastIndexUpdateService.restart();
		exileToolsLadderService.restart();
	}

	ScheduledService<Void> durianTimer = new ScheduledService<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					int secs = 60 * 10;
//					int secs = 15;
					for (int i = 0; i < secs; i++) {
						updateMessage("Durian: " + (secs - i) + " sec");
						Thread.sleep(1000);
					}
					return null;
				}
			};
		}
	};
	private void toggleDurianService() {
		ToggleButton btnDurianMode = controlPane.getBtnDurianMode();
		if (btnDurianMode.isSelected()) {
			btnDurianMode.textProperty().bind(durianTimer.messageProperty());
			durianTimer.restart();
		} else {
			btnDurianMode.textProperty().unbind();
			btnDurianMode.textProperty().set("Durian Notifier");
			durianTimer.cancel();
		}
	}
	private void durianTimerSucceeded() {
		if (!searchService.isRunning()) {
			controlPane.fireSearchEvent();
			durianTimer.restart();
		}
	}

	private void searchSucceeded() {
		ExileToolsSearchResult result = searchService.getValue();
		result.getExileToolHits().stream().forEach(e -> 
			exileToolsLadderService.resultProperty().get().addPlayerLadderData(e));
		ObservableList<ExileToolsHit> list = FXCollections.observableList(result.getExileToolHits());
		// add empty row
		list.addAll(ExileToolsHit.EMPTY, ExileToolsHit.EMPTY, ExileToolsHit.EMPTY);
		searchResultsPane.setItems(list);
		// Remove 3 empty hits
		int hitsToShow = result.getExileToolHits().size() - 3;
		controlPane.setSearchHitCount(result.getSearchResult().getTotal(),
				hitsToShow);
		Platform.runLater(() -> searchPane.toggleSlide());
		
		if (controlPane.getBtnDurianMode().isSelected() && hitsToShow > 0) {
			try {
				SoundUtils.tone(2000,100);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	private void setupToolbar(final Stage stage) {
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

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        toolBar.getItems().add(spacer2);

        toolBar.setPrefHeight(66);
        toolBar.setMinHeight(66);
        toolBar.setMaxHeight(66);
        GridPane.setConstraints(toolBar, 0, 0);
        
		// add close min max
		final WindowButtons windowButtons = new WindowButtons(stage);
		versionText = new Label(VERSION);
		indexerLastUpdateText = new Label("Indexer Last Update: ");
		Label indexerLastUpdateValueText = new Label();
		indexerLastUpdateValueText.textProperty().bind(lastIndexUpdateService.messageProperty());
		indexerLastUpdateText.textProperty().bind(lastIndexUpdateService.countdownProperty());
		lastIndexUpdateService.setOnSucceeded(e -> lastIndexUpdateService.restart());
		lastIndexUpdateService.setOnFailed	 (e -> lastIndexUpdateService.restart());
		VBox vBox = new VBox(versionText, indexerLastUpdateText, indexerLastUpdateValueText);
		vBox.setAlignment(Pos.TOP_RIGHT);
		vBox.setPadding(new Insets(1, 5, 1, 5));
		toolBar.getItems().add(vBox);
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
	}

	private void searchHandler(String json, String league, boolean onlineOnly) {
		logger.debug("Search: " + json);
		if (json.isEmpty()) {
			return;
		}
		boolean sizeIsThousands = Pattern.compile("\"size\"\\s:\\s\\d{4,}").matcher(json).find();
		if (sizeIsThousands) {
//			logger.info("Playing /audio/youarebeingselfish.wav");
			try {
				// Doesn't work damn
//				AudioClip malachi = new AudioClip(BlackmarketApplication.class.getResource("/audio/youarebeingselfish.wav").toString());
//				malachi.play(1.0);
				Dialogs.showInfo("You are being selfish!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
		} else {
			searchService.setJson(json);
			searchService.restart();
		} 
	}


}