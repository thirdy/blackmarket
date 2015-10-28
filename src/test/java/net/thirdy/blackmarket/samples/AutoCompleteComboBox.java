package net.thirdy.blackmarket.samples;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * This class is a TextField which implements an "autocomplete" functionality, based on a supplied list of entries.
 * @author Caleb Brinkman
 */
public class AutoCompleteComboBox extends ComboBox<String>
{
  /** The existing autocomplete entries. */
  private final SortedSet<String> entries;
  /** The popup used to select an entry. */
  private ContextMenu entriesPopup;

  /** Construct a new AutoCompleteTextField. */
  public AutoCompleteComboBox() {
    super();
    setEditable(true);
    entries = new TreeSet<>();
    entries.addAll(Arrays.asList("Test", "Pad", "Shiver", "Loooooooooooooooooooooooooooooooooooog"));
    entriesPopup = new ContextMenu();
    
    getItems().addAll(entries);
    

    getEditor().textProperty().addListener(new ChangeListener<String>()
    {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
        if (getEditor().getText().length() == 0)
        {
          entriesPopup.hide();
        } else
        {
          LinkedList<String> searchResult = new LinkedList<>();
//          searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
          searchResult.addAll(entries.stream()
        		  	.filter( x -> StringUtils.containsIgnoreCase(x, getEditor().getText()) )
        		  	.collect(Collectors.toList())
        		  );
          if (entries.size() > 0)
          {
            populatePopup(searchResult);
            if (!entriesPopup.isShowing())
            {
              entriesPopup.show(AutoCompleteComboBox.this, Side.BOTTOM, 0, 0);
            }
          } else
          {
            entriesPopup.hide();
          }
        }
      }
    });

    focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
        entriesPopup.hide();
      }
    });

  }

  /**
   * Get the existing set of autocomplete entries.
   * @return The existing autocomplete entries.
   */
  public SortedSet<String> getEntries() { return entries; }

  /**
   * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
   * @param searchResult The set of matching strings.
   */
  private void populatePopup(List<String> searchResult) {
    List<CustomMenuItem> menuItems = new LinkedList<>();
    // If you'd like more entries, modify this line.
    int maxEntries = 10;
    int count = Math.min(searchResult.size(), maxEntries);
    for (int i = 0; i < count; i++)
    {
      final String result = searchResult.get(i);
      Label entryLabel = new Label(result);
      CustomMenuItem item = new CustomMenuItem(entryLabel, true);
      item.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent actionEvent) {
        	getEditor().setText(result);
          entriesPopup.hide();
        }
      });
      menuItems.add(item);
    }
    entriesPopup.getItems().clear();
    entriesPopup.getItems().addAll(menuItems);

  }
  
}

