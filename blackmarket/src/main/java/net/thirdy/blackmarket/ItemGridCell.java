package net.thirdy.blackmarket;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.controlsfx.control.GridCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jexiletools.es.model.ExileToolsHit;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import net.thirdy.blackmarket.util.ImageCache;

class ItemGridCell extends GridCell<ExileToolsHit> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private final ImageView imageView;
	
	private final List<Label> explicitMods; 

	StackPane layout;

	public ItemGridCell() {
		// getStyleClass().add("image-grid-cell"); //$NON-NLS-1$

		imageView = new ImageView();
		layout = new StackPane();
		explicitMods = Arrays.asList(
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

		// imageView.fitHeightProperty().bind(heightProperty());
		// imageView.fitWidthProperty().bind(widthProperty());
		// imageView.fitHeightProperty().bind(heightProperty());
		// imageView.fitWidthProperty().bind(widthProperty());

		// imageView.setFitHeight(142);
		// imageView.setFitHeight(312);
		imageView.setScaleX(0.6025641025641026);
		imageView.setScaleY(0.6025641025641026);

		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);
		// imageView.setCache(true); // makes the image blurry
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
			// Setup image
			Image image = ImageCache.getInstance().get(item.getInfo().getIcon());
			imageView.setImage(image);
			imageView.setFitHeight(image.getHeight());
			
			// Setup labels
//			item.getAttributes().get
			
			layout.getChildren().clear();
			layout.getChildren().add(imageView);
			layout.getChildren().addAll(
					explicitMods.stream().filter(l -> !l.getText().isEmpty()).collect(Collectors.toList()));
			
			setGraphic(layout);
		}
	}
}