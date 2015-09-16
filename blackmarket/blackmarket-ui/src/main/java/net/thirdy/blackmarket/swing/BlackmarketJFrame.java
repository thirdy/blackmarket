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
import net.thirdy.blackmarket.core.SearchPayload;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		 setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
//		table.setFilterHeaderEnabled(false);
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchEventHandler();
			}
		});
//		searchButton.setPreferredSize(new Dimension(100, 50));
		
		JButton about = new JButton("?");
		about.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showAboutDialog();
			}
		});
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
		buttonsPanel.add(searchButton);
		buttonsPanel.add(about);

		JPanel montherPanel = new JPanel(new BorderLayout());
		montherPanel.add(new JScrollPane(table), BorderLayout.CENTER);
//		 table.setPreferredScrollableViewportSize(table.getPreferredSize());
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.add(buttonsPanel, BorderLayout.EAST);
		headerPanel.add(searchField, BorderLayout.CENTER);

		montherPanel.add(headerPanel, BorderLayout.NORTH);
		add(montherPanel);
		loadData(loadSampleData());
		setVisible(true);
	}
	
	protected void showAboutDialog() {
		// TODO Auto-generated method stub
//		JButton button = new JButton();
//	    button.setText("<HTML>Click the <FONT color=\"#000099\"><U>link</U></FONT>"
//	        + " to go to the Java website.</HTML>");
//	    button.setHorizontalAlignment(SwingConstants.LEFT);
//	    button.setBorderPainted(false);
//	    button.setOpaque(false);
//	    button.setBackground(Color.WHITE);
//	    button.setToolTipText(uri.toString());
//	    button.addActionListener(new OpenUrlAction());
	    
//	    JOptionPane.showMessageDialog(BlackmarketJFrame.this, AppConfig.getAboutMessage());
		
		String s = "http://thirdy.github.io/blackmarket";
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(s));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(BlackmarketJFrame.this,
						"Error on opening browser, address: " + s + ": " + e.getMessage());
			}
		} else {
			JOptionPane.showMessageDialog(BlackmarketJFrame.this, "Launch browser failed, please manually visit: " + s);
		}
	}

	private String loadDefaultSearchFile() {
		String page = null;
		try {
			page = IOUtils.toString(this.getClass().getResourceAsStream("/ring-life.txt"));
		} catch (IOException e) {
			// won't likely happen since file is in classpath
			e.printStackTrace();
		}
		return page;
	}

	private void searchEventHandler() {
		SwingWorker<Void, SearchResultItem> sw = new SwingWorker<Void, SearchResultItem>() {

			@Override
			protected Void doInBackground() throws Exception {
				List<SearchResultItem> list = Collections.emptyList();
				String payload = loadDefaultSearchFile();
				payload = new SearchPayload(payload).getPayloadFormatted();
				System.out.println(payload);

				String searchPage = MainApp.getPoeTradeHttpClient().search(payload );
				SearchPageScraper scraper = new SearchPageScraper(searchPage);
				list = scraper.parse();

				// This sends the results to the .process method
				SearchResultItem[] arr = new SearchResultItem[list.size()];
				list.toArray(arr);
				publish(arr);

				return null;
			}

			@Override
			protected void process(List<SearchResultItem> chunks) {
				// This updates the UI
				loadData(chunks);
			}
		};
		sw.execute();
	}

	private void loadData(List<SearchResultItem> data) {
		System.out.println("loading data into Table");
		table.setModel(new BlackmarketTableModel(data));
		table.setEditable(true);
		Action action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("click" + e.getSource());
				JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        BlackmarketTableModel model = (BlackmarketTableModel) table.getModel();
		        String result = model.getWantToBuyMessage(modelRow);
				
				    System.out.println("Buy message: " + result);
				    StringSelection stringSelection = new StringSelection(result);
				    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				    clpbrd.setContents(stringSelection, null);
				
			}
		};
		table.getColumn("WTB").setCellEditor(new ButtonColumn(table, action, 2));
		table.packAll();
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
