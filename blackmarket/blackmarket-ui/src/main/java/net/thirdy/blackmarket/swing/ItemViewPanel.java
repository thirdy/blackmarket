package net.thirdy.blackmarket.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem.Mod;
import net.thirdy.blackmarket.swing.SwingUtil.ImageConsumer;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import net.miginfocom.swing.MigLayout;

public class ItemViewPanel extends JPanel {

	private JLabel lblName;
	
	private SearchResultItem item;
	private JLabel lblId;
	private JLabel lblBuyout;
	private JPanel panel;
	private JPanel panel_1;

	private JLabel lblImplicitmod;

	private JLabel lblImg;

	private JTextArea taExplicitMods;
	private JLabel lblSocketRaw;

	/**
	 * Create the panel.
	 */
	public ItemViewPanel() {
		
		String[] images = new String[] {
				"/Ancient_German_armour_helmet.jpg",
				"/lapulapu.jpg"
		};
		String img = images[new Random().nextInt(2)];
		byte[] imgBytes = null;
		try {
			imgBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream(img));
		} catch (IOException e) {
			// won't likely happen since file is in classpath
			e.printStackTrace();
		}
		setLayout(new MigLayout("", "[271.00px]", "[24px][441px][23px]"));
		
		panel = new JPanel();
		add(panel, "cell 0 0,growx,aligny top");
		
		lblId = new JLabel("id");
		panel.add(lblId);
		
		lblBuyout = new JLabel("Buyout");
		panel.add(lblBuyout);
		lblBuyout.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblName = new JLabel("Name");
		panel.add(lblName);
		
		panel_1 = new JPanel(new BorderLayout());
		add(panel_1, "cell 0 2,grow");
		
		JButton btnWtb = new JButton("WTB");
		panel_1.add(btnWtb, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, "cell 0 1,grow");
		
		lblImplicitmod = new JLabel("implicit");
		lblImplicitmod.setHorizontalAlignment(SwingConstants.CENTER);
		lblImplicitmod.setAlignmentX(0.5f);
		
		lblImg = new JLabel("");
		lblImg.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.setLayout(new MigLayout("", "[285px]", "[175.00px][25.00][13.00px][298.00px]"));
		
		lblSocketRaw = new JLabel("");
		lblSocketRaw.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblSocketRaw, "cell 0 1,alignx center,aligny center");
		
		JScrollPane scrollPane = new JScrollPane();
		
		taExplicitMods = new JTextArea();
		taExplicitMods.setRows(7);
		scrollPane.setViewportView(taExplicitMods);
		panel_2.add(scrollPane, "cell 0 3,grow");
		panel_2.add(lblImg, "cell 0 0,grow");
		panel_2.add(lblImplicitmod, "cell 0 2,growx,aligny top");
		if (imgBytes != null) {
			lblImg.setIcon(new ImageIcon(imgBytes));
		}
		btnWtb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.setClipboard(item.getWTB());
			}
		});

	}

	public void setItem(SearchResultItem item) {
		this.item = item;
		final String id = item.getId();
		lblId.setText(item.getId());
		lblBuyout.setText(item.getBuyout());
		Mod implicitMod = item.getImplicitMod();
		if (implicitMod != null) {
			lblImplicitmod.setText(implicitMod.toStringDisplay());
		} else {
			lblImplicitmod.setText("");
		}
		lblName.setText(item.getName());
		lblSocketRaw.setText(item.getSocketsRaw());
		
		List<String> modStrList = Lists.transform(item.getExplicitMods(), new Function<Mod, String>() {

			@Override
			public String apply(Mod input) {
				return input.toStringDisplay();
			}
		});
		
		String s = StringUtils.join(modStrList.toArray(), System.getProperty("line.separator"));
		taExplicitMods.setText(s);
		
		try {
			URL url = new URL(item.getImageUrl());
			new SwingUtil.ImageLoader(new ImageConsumer() {
				
				public void imageLoaded(BufferedImage img) {
					if (lblId.getText().equalsIgnoreCase(id)) {
						lblImg.setIcon(new ImageIcon(img));
					}
				}
			}, url).execute();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void generateAndUseRandomItem() {
		lblName.setText("Random name " + RandomUtils.nextInt(1, 10));
		taExplicitMods.setText(StringUtils.join(new String[] {
				"Explicit mod 1",
				"Explicit mod 2",
				"Explicit mod 3",
				"Explicit mod 4",
				"Explicit mod 5"
		}, System.getProperty("line.separator")));
	}
}
