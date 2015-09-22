package net.thirdy.blackmarket.swing;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

import com.porty.swing.table.BaseTable;
import com.porty.swing.util.WindowUtils;

import net.thirdy.blackmarket.MainApp;
import net.thirdy.blackmarket.autocomplete.BlackmarketCompletionProvider;
import net.thirdy.blackmarket.core.BlackmarketLanguageParserInstance;
import net.thirdy.blackmarket.core.SearchPageScraper;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPayload;
import net.thirdy.blackmarket.core.util.BlackmarketConfig;
import net.thirdy.blackmarket.core.util.BlackmarketUtil;

@SuppressWarnings("serial")
public class BlackmarketJFrame extends JFrame {

	BaseTable table = new BaseTable();
	JButton searchButton = new JButton("Search");
	JTextField searchField = new JTextField();
	JComboBox<String> leagueComboBox;
	
	JFrame itemViewerWindow = new JFrame();
	ItemViewPanel itemViewPanel = new ItemViewPanel();

	Action searchAction;
	
	public BlackmarketJFrame() {
		super(BlackmarketConfig.TITLE);
		setupIconImage();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Rectangle screenRect = WindowUtils.getScreenRect();
		setSize(screenRect.width - 260, screenRect.height - 30);
//		WindowUtils.centerWindow(this);
		
		itemViewerWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		itemViewerWindow.getContentPane().add(itemViewPanel);
		itemViewerWindow.setAlwaysOnTop(true);
		itemViewerWindow.setSize(260, 390);
		itemViewerWindow.setLocation(screenRect.width - 260, 0);
				
		leagueComboBox = new JComboBox<>(BlackmarketConfig.properties().leagues());
		leagueComboBox.setSelectedItem(BlackmarketConfig.properties().league());
		
//		 setExtendedState( getExtendedState()|JFrame.MAXIMIZED_VERT );
//		table.setFilterHeaderEnabled(false);
		searchAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Search button clicked");
				searchEventHandler();
			}
		};
		searchButton.addActionListener(searchAction);
		searchField.addActionListener(searchAction);
//		searchButton.setPreferredSize(new Dimension(100, 50));
		
		CompletionProvider provider = new BlackmarketCompletionProvider();
		AutoCompletion ac = new AutoCompletion(provider);
	    ac.install(searchField);
		
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
		headerPanel.add(leagueComboBox, BorderLayout.WEST);

		montherPanel.add(headerPanel, BorderLayout.NORTH);
		getContentPane().add(montherPanel);
		setupTableSelectionListener();
		if (BlackmarketConfig.DEVELOPMENT_MODE) {
			loadData(loadSampleData());
		}
		setVisible(true);
	}
	
	private void setupIconImage() {
		try {
			byte[] imgBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/lapulapu-icon.jpg"));
			Image img = Toolkit.getDefaultToolkit().createImage(imgBytes);
			setIconImage(img);
		} catch (IOException e) {
			// won't likely happen since file is in classpath
			e.printStackTrace();
		}
	}

	private void setupTableSelectionListener() {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				int row = table.getSelectedRow();
		        int col = table.getSelectedColumn();
		        BlackmarketTableModel model = (BlackmarketTableModel) table.getModel();
	        	SearchResultItem item = model.getItem(row);
	        	itemViewPanel.setItem(item);
		        if (!itemViewerWindow.isVisible()) {
		        	itemViewerWindow.setVisible(true);
				}
			}
		});
	}

	protected void showAboutDialog() {
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
		String page = BlackmarketUtil.loadFromClassPath(BlackmarketJFrame.class, "/ring-life.txt");
		return page;
	}

	private void searchEventHandler() {
		SwingWorker<Void, SearchResultItem> sw = new SwingWorker<Void, SearchResultItem>() {

			@Override
			protected Void doInBackground() throws Exception {
				System.out.println("searchEventHandler() - doInBackground()");
				try {
					searchButton.setEnabled(false);
					BlackmarketJFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					
					List<SearchResultItem> list = Collections.emptyList();
					System.out.println("loading search params");
					String searchString = searchField.getText();
					searchString = StringUtils.isBlank(searchString) ? BlackmarketConfig.properties().blankSearch() : searchString;
					// String payload = loadDefaultSearchFile();
					BlackmarketLanguageParserInstance bmLang = new BlackmarketLanguageParserInstance();
					String payload = bmLang.parse(searchString);
					
					System.out.println("now formatting payload");
					payload = new SearchPayload(payload).getPayloadFormatted();
					System.out.println("Formatted payload: " + payload);

					System.out.println("now calling backend");
					String searchPage = MainApp.getPoeTradeHttpClient().search(payload, BlackmarketConfig.properties().alwaysSortByBuyout());
					System.out.println("Search done, now parsing");
					
					SearchPageScraper scraper = new SearchPageScraper(searchPage);
					list = scraper.parse();

					// This sends the results to the .process method
					SearchResultItem[] arr = new SearchResultItem[list.size()];
					list.toArray(arr);
					publish(arr);
				} catch (Exception e) {
					// JOptionPane.showMessageDialog(BlackmarketJFrame.this, "error: " + e.getMessage());
					System.out.println(e.getMessage());
					e.printStackTrace();
					publish(new SearchResultItem[] {SearchResultItem.exceptionItem(e)});
					disable(searchButton, 10);
				}

				return null;
			}

			@Override
			protected void process(List<SearchResultItem> chunks) {
				// This updates the UI
				loadData(chunks);
				disable(searchButton, 10);
			}
		};
		sw.execute();
	}

	private void loadData(List<SearchResultItem> data) {
		System.out.println("loading data into Table");
		table.setModel(new BlackmarketTableModel(data));

		Action action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        BlackmarketTableModel model = (BlackmarketTableModel) table.getModel();
		        String result = model.getWantToBuyMessage(modelRow);
				SwingUtil.setClipboard(result);
			}
		};
		table.getColumn("WTB").setCellEditor(new ButtonColumn(table, action, 2));
		table.packAll();
		
		this.setCursor(Cursor.getDefaultCursor());
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

	void disable(final AbstractButton b, final long seconds) {
	    b.setEnabled(false);
	    new SwingWorker() {
	        @Override protected Object doInBackground() throws Exception {
	        	String buttonLabel = b.getText();
	            for (int i = 0; i < seconds; i++) {
	            	b.setText(buttonLabel + "(" + (seconds - i) + ")");
	            	Thread.sleep(1000);
				}
	            b.setText(buttonLabel);
	            return null;
	        }
	        @Override protected void done() {
	            b.setEnabled(true);
	        }
	    }.execute();
	}
}
