package net.thirdy.blackmarket.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.controlsfx.control.GridCell;
import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jexiletools.es.model.ExileToolsHit;
import io.jexiletools.es.model.Mod;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.thirdy.blackmarket.util.ImageCache;

public class ItemGridCell extends GridCell<ExileToolsHit> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

//	private final ImageView imageView;
	
	private final List<Label> explicitModsLbls; 
	private final VBox modsPane;

	StackPane layout;

	double scaledWidth = 170 ;
	double scaledHeight = 189 ;

	public ItemGridCell() {
		 getStyleClass().add("image-grid-cell"); //$NON-NLS-1$
		
//		imageView = new ImageView();
		layout = new StackPane();
		modsPane = new VBox();
		
//		 layout.setPrefHeight(10);
//		 layout.setPrefWidth(10);
//		 layout.setMaxHeight(10);
//		 layout.setMaxWidth(10);
//		 layout.setMinHeight(10);
//		 layout.setMinWidth(10);
		 
//		layout.setMaxHeight(10);
//		layout.setMaxWidth(5);
		
		explicitModsLbls = Arrays.asList(
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label(),
				new Label()
				);

		explicitModsLbls.stream()
			.forEach(l -> l.setWrapText(true));
		
//		 imageView.fitHeightProperty().bind(heightProperty());
//		 imageView.fitWidthProperty().bind(widthProperty());
		
		// define crop in image coordinates:
//		Rectangle2D croppedPortion = new Rectangle2D(0, 0, 170, 210);

//		imageView.setViewport(croppedPortion);
		

//		imageView.setFitHeight(image.getHeight());


		// imageView.setCache(true); // makes the image blurry
		
		layout.setStyle("-fx-background-color: rgba(30, 30, 30, 0.5); -fx-background-radius: 5;");
		
//		layout.getChildren().addAll(Borders.wrap(imageView).lineBorder().buildAll());
//		layout.getChildren().addAll(Borders.wrap(imageView).lineBorder().buildAll(), modsPane);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateItem(ExileToolsHit item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setGraphic(null);
		} else {
			ImageView imageView = new ImageView();
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			
			// Setup image
			Image image = ImageCache.getInstance().get(item.getInfo().getIcon());
			imageView.setImage(image);

			if (image.getHeight() > scaledHeight) {
				imageView.setFitWidth(94);
				imageView.setFitHeight(scaledHeight);
			} else {
				imageView.setScaleX(0.6025641025641026);
				imageView.setScaleY(0.6025641025641026);
			}
			
			// Setup labels
			clearModLabels();
			populateModLabels(item);
			
			modsPane.getChildren().clear();
			modsPane.getChildren().addAll(
					explicitModsLbls.stream().filter(l -> StringUtils.isNotBlank(l.getText())).collect(Collectors.toList()));
			
			layout.getChildren().clear();
			layout.getChildren().addAll(imageView, modsPane);
			
			setGraphic(layout);
		}
	}

	private void clearModLabels() {
		explicitModsLbls.stream()
			.forEach(l -> l.setText(""));
	}

	private void populateModLabels(ExileToolsHit item) {
		List<Mod> explicitMods = item.getExplicitMods();
		explicitMods.add(Mod.fromRaw(item.getInfo().getIcon(), Boolean.valueOf(true)));
		IntStream.range(0, explicitMods.size())
			.forEachOrdered(i -> {
				String m = explicitMods.get(i).toDisplay();
				Label l = explicitModsLbls.get(i);
				l.setText(m);
			});
	}
}