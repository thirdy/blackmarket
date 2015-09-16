package net.thirdy.blackmarket.swing;


import com.porty.swing.table.BaseTable;
import com.porty.swing.table.model.BeanPropertyTableModel;
import com.porty.swing.util.WindowUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.thirdy.blackmarket.AppConfig;
import net.thirdy.blackmarket.MainApp;
import net.thirdy.blackmarket.core.SearchPageScraper;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.commons.io.IOUtils;

/**
 * Demoes usage of the advanced version of table, BaseTable.
 *
 * @author iportyankin
 */
@SuppressWarnings("serial")
public class BlackmarketJFrame extends JFrame {
	
	BaseTable table = new BaseTable();
	JButton searchButton = new JButton("Search");
	JTextField searchField = new JTextField();

	public BlackmarketJFrame() {
        super(AppConfig.TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 600);
        WindowUtils.centerWindow(this);
        // setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
        table.setFilterHeaderEnabled(false);
        
        searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				searchEventHandler();
			}
		});
        
        JPanel montherPanel = new JPanel(new BorderLayout());
        montherPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(searchButton, BorderLayout.EAST);
        headerPanel.add(searchField, BorderLayout.CENTER);
        
        montherPanel.add(headerPanel, BorderLayout.NORTH);
        add(montherPanel);
        loadData(loadSampleData());
        setVisible(true);
    }
	
	private void searchEventHandler() {
		SwingWorker<Void, SearchResultItem> sw = new SwingWorker<Void, SearchResultItem>() {

			@Override
			protected Void doInBackground() throws Exception {
				List<SearchResultItem> list = Collections.emptyList();
//				if (AppConfig.TESTING_MODE) {
//					
//				} else {
//					String searchPage = MainApp.getPoeTradeHttpClient().search(payload);
//					SearchPageScraper scraper = new SearchPageScraper(searchPage);
//					list = scraper.parse();
//				}
//				list.stream().forEach(e -> System.out.println(e));
//				if (!list.isEmpty()) {
//					ObservableList<SearchResultItem> fxList = FXCollections.observableArrayList(list);
//					Platform.runLater(() -> itemListView.setItems(fxList));
//				}
				return null;
			}
			
			@Override
			protected void process(List<SearchResultItem> chunks) {
				// TODO Auto-generated method stub
				super.process(chunks);
			}
		};
		sw.execute();
	}

	private void loadData(List<SearchResultItem> sampleData) {
		BeanPropertyTableModel<SearchResultItem> model = new BeanPropertyTableModel<SearchResultItem>(SearchResultItem.class);
		model.addExcludedProperty("explicitMods");
        model.setOrderedProperties(Arrays.asList("id","buyout","wtb"));
        model.setData(sampleData);
        table.setModel(model);
        table.setEditable(true);
        table.getColumn("W T B").setCellRenderer(new ButtonRenderer());
        table.getColumn("W T B").setCellEditor(new ButtonEditor(new JCheckBox()));
	}
	
	private List<SearchResultItem> loadSampleData() {
		String page = null;
		try {
			page = IOUtils.toString(this.getClass().getResourceAsStream("/DefaultWarbands.search.poetrade"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SearchPageScraper scraper = new SearchPageScraper(page);
		return scraper.parse();
	}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new BlackmarketJFrame();
            }
        });
    }
}
