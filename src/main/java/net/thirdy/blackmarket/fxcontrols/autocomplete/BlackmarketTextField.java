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
package net.thirdy.blackmarket.fxcontrols.autocomplete;

import static net.thirdy.blackmarket.util.LangContants.STRING_EMPTY;
import static org.elasticsearch.common.lang3.StringUtils.trimToNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.controlsfx.control.textfield.CustomTextField;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.skin.AutoCompletePopupSkin;
import javafx.animation.FadeTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * @author thirdy
 *
 */
public class BlackmarketTextField<T> extends CustomTextField {
	
	private static final Duration FADE_DURATION = Duration.millis(350);
	private Collection<T> items;
	
	public BlackmarketTextField(T ... a) {
		this(Arrays.asList(a));
	}
	
	public BlackmarketTextField(Collection<T> items) {
		this(items, 10);
	}
	
	public BlackmarketTextField(Collection<T> items, int rowCount) {
		super();
		this.items = items;
		setupClearButtonField(this, this.rightProperty());
		
//		class CustomAutoCompletionTextFieldBinding extends AutoCompletionTextFieldBinding<T> {
//			public CustomAutoCompletionTextFieldBinding(final TextField textField,
//		            Callback<ISuggestionRequest, Collection<T>> suggestionProvider) {
//		        super(textField, suggestionProvider);
//		    }
//			public void setListViewMinWidth(double width) {
//				Skin<?> skin = getSkin();
//				if(skin instanceof AutoCompletePopupSkin){
//					AutoCompletePopupSkin<?> au = (AutoCompletePopupSkin<?>)skin;
//					ListView<?> li = (ListView<?>)au.getNode();
//					li.setMinWidth(width);
//					li.setPrefWidth(width);
//				}
//			}
//		}

		AutoCompletionTextFieldBinding actfb = new AutoCompletionTextFieldBinding(this,
				BlackmarketSuggestionProvider.create(items));
		actfb.setVisibleRowCount(rowCount);
//		actfb.setAutoCompletionPopupMinWidth(400.0);
		
		// tfName.setOnAction(e -> {
		// String userText =
		// Optional.ofNullable(trimToNull(tfName.getText())).orElse(STRING_EMPTY);
		// System.out.println("Text: " + userText);
		// actfb.setUserInput(userText);
		// });

		focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				String userText = Optional.ofNullable(trimToNull(getText())).orElse(STRING_EMPTY);
				actfb.setUserInput(userText);
			} else {
				actfb.setUserInput(null);
			}
		});

		// tfName.addEventFilter(MouseEvent.MOUSE_CLICKED,
		// new EventHandler<MouseEvent>() {
		//
		// @Override
		// public void handle(MouseEvent event) {
		// String userText =
		// Optional.ofNullable(trimToNull(tfName.getText())).orElse(STRING_EMPTY);
		// System.out.println("Text: " + userText);
		// actfb.setUserInput(userText);
		// }
		//
		// });
	}

	/**
	 * Get the first matching item in the {@link #items}. Make sure to check if {@link #getText()} returns a valid value. 
	 */
    public T item() {
    	return items.stream().filter(item -> item.toString().equalsIgnoreCase(getText())).findFirst().get();
    }
	
    private void setupClearButtonField(TextField inputField, ObjectProperty<Node> rightProperty) {
        inputField.getStyleClass().add("clearable-field"); //$NON-NLS-1$

        Region clearButton = new Region();
        clearButton.getStyleClass().addAll("graphic"); //$NON-NLS-1$
        StackPane clearButtonPane = new StackPane(clearButton);
        clearButtonPane.getStyleClass().addAll("clear-button"); //$NON-NLS-1$
        clearButtonPane.setOpacity(0.0);
        clearButtonPane.setCursor(Cursor.DEFAULT);
        clearButtonPane.setOnMouseReleased(e -> inputField.clear());
        clearButtonPane.managedProperty().bind(inputField.editableProperty());
        clearButtonPane.visibleProperty().bind(inputField.editableProperty());

        rightProperty.set(clearButtonPane);

        final FadeTransition fader = new FadeTransition(FADE_DURATION, clearButtonPane);
        fader.setCycleCount(1);

        inputField.textProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable arg0) {
                String text = inputField.getText();
                boolean isTextEmpty = text == null || text.isEmpty();
                boolean isButtonVisible = fader.getNode().getOpacity() > 0;

                if (isTextEmpty && isButtonVisible) {
                    setButtonVisible(false);
                } else if (!isTextEmpty && !isButtonVisible) {
                    setButtonVisible(true);
                }
            }

            private void setButtonVisible( boolean visible ) {
                fader.setFromValue(visible? 0.0: 1.0);
                fader.setToValue(visible? 1.0: 0.0);
                fader.play();
            }
        });
    }
}
