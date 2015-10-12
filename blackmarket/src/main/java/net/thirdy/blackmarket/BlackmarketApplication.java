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

import static com.google.common.collect.Iterables.toArray;
import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.orFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

import io.jexiletools.es.ExileToolsESClient;
import io.jexiletools.es.ExileToolsESClient.ExileToolsSearchResult;
import io.jexiletools.es.model.json.ExileToolsHit;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import net.thirdy.blackmarket.controls.ControlPane;
import net.thirdy.blackmarket.controls.Dialogs;
import net.thirdy.blackmarket.controls.ItemGridCell;
import net.thirdy.blackmarket.domain.Search;
import net.thirdy.blackmarket.fxcontrols.SlidingPane;
import net.thirdy.blackmarket.fxcontrols.WindowButtons;
import net.thirdy.blackmarket.fxcontrols.WindowResizeButton;
import net.thirdy.blackmarket.service.ExileToolsLastIndexUpdateService;
import net.thirdy.blackmarket.service.ExileToolsSearchService;

/**
 * @author thirdy
 *
 */
public class BlackmarketApplication extends Application {

	public static final String VERSION = "Version: 0.4";

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static BlackmarketApplication blackmarketApplication;
	private static ExileToolsESClient exileToolsESClient;

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

	public static ExileToolsESClient getExileToolsESClient() {
		return exileToolsESClient;
	}

	private final ExileToolsSearchService searchService = new ExileToolsSearchService();
	private final ExileToolsLastIndexUpdateService lastIndexUpdateService = new ExileToolsLastIndexUpdateService();

	private GridView<ExileToolsHit> searchResultsPane;

	private Label versionText;
	private Label indexerLastUpdateText;

	private ControlPane controlPane;

	@Override
	public void stop() throws Exception {
		super.stop();
		exileToolsESClient.shutdown();
	}

	@Override
	public void start(final Stage stage) {
		blackmarketApplication = this;
		exileToolsESClient = new ExileToolsESClient();
		stage.setTitle("Blackmarket");

		// create root stack pane that we use to be able to overlay proxy dialog
		StackPane layerPane = new StackPane();

		Region veilOfTheNight = new Region();

		veilOfTheNight.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");

		ProgressIndicator progressIndicator = new ProgressIndicator(-1.0f);
		progressIndicator.setMaxSize(150, 150);

		stage.initStyle(StageStyle.UNDECORATED);
		// create window resize button
		windowResizeButton = new WindowResizeButton(stage, 1020, 700);
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

		boolean is3dSupported = false;
		scene = new Scene(layerPane, 1020, 700, is3dSupported);

		scene.getStylesheets().add(this.getClass().getResource("blackmarket.css").toExternalForm());

		// create main toolbar
		setupToolbar(stage);

		this.root.setTop(toolBar);

		controlPane = new ControlPane(e -> searchHandler(e));
		SlidingPane searchPane = new SlidingPane(270, 12, controlPane);
		Button showCollapseButton = searchPane.getControlButton();

		scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
//			if ((keyEvent.isControlDown() || keyEvent.isAltDown())
//					&& (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN)) {
//				showCollapseButton.fire();
//			}
			if (keyEvent.getCode() == KeyCode.CONTROL) {
				showCollapseButton.fire();
			}

			if (keyEvent.getCode() == KeyCode.ENTER 
					&& !progressIndicator.isVisible() 
					&& searchPane.isExpanded()) {
				controlPane.fireSearchEvent();
			}
		});

		controlPane.installShowCollapseButton(showCollapseButton);
		searchPane.setId("searchPane");

		AnchorPane centerPane = new AnchorPane();

		// DoubleProperty wProp = DoubleProperty.

		searchResultsPane = new GridView<>();
		searchResultsPane.setHorizontalCellSpacing(9);
		searchResultsPane.setVerticalCellSpacing(5);
		// searchResultsPane.setB
		// searchResultsPane.setCellHeight(300);
		searchResultsPane.setCellWidth(315);
//		DoubleBinding oneThirdWidthBinding = Bindings.createDoubleBinding(
//				() -> (centerPane.getWidth() / 3.4), centerPane.widthProperty());
//		 searchResultsPane.cellWidthProperty().bind(oneThirdWidthBinding);
		searchResultsPane.setCellHeight(200);
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
		AnchorPane.setRightAnchor(searchPane, 10.0);
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
		
		lastIndexUpdateService.restart();
	}

	private void searchSucceeded() {
		ExileToolsSearchResult result = searchService.getValue();
		searchResultsPane.setItems(FXCollections.observableList(result.getExileToolHits()));
		controlPane.setSearchHitCount(result.getSearchResult().getTotal(), result.getExileToolHits().size());
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
		vBox.setPadding(new Insets(5));
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

	private void searchHandler(Search search) {
		logger.debug("Search: " + search.toString());
		String json = search.buildSearchJson();
		searchService.setJson(json);
		searchService.restart();
	}


}