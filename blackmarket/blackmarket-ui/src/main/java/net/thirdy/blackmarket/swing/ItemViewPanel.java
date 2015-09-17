package net.thirdy.blackmarket.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem.Mod;

public class ItemViewPanel extends JPanel {

	private JLabel lblName;
	private JLabel lblImg;
	private JPanel explicitModsPanel;
	
	private SearchResultItem item;
	private JLabel lblImplicitmod;
	private JLabel lblId;
	private JLabel lblBuyout;
	private JTextArea taExplicitMods;

	/**
	 * Create the panel.
	 */
	public ItemViewPanel() {
		setLayout(null);
		
		lblName = new JLabel("Name");
		lblName.setBounds(57, 11, 210, 14);
		add(lblName);
		
		lblBuyout = new JLabel("Buyout");
		lblBuyout.setHorizontalAlignment(SwingConstants.CENTER);
		lblBuyout.setBounds(1, 11, 46, 14);
		add(lblBuyout);
		
		lblImg = new JLabel("");
		lblImg.setHorizontalAlignment(SwingConstants.CENTER);
		
		String[] images = new String[] {
				"/Ancient_German_armour_helmet.jpg",
				"/lapulapu.jpg"
		};
		String img = images[new Random().nextInt(2)];
		try {
			byte[] imgBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream(img));
			lblImg.setIcon(new ImageIcon(imgBytes));
		} catch (IOException e) {
			// won't likely happen since file is in classpath
			e.printStackTrace();
		}
		
//		lblImg.setIcon(new ImageIcon("account-login-8x.png"));
		lblImg.setBounds(32, 36, 176, 100);
		add(lblImg);
		
		explicitModsPanel = new JPanel();
		explicitModsPanel.setBounds(1, 176, 247, 137);
		add(explicitModsPanel);
		explicitModsPanel.setLayout(new BorderLayout(0, 0));
		
		taExplicitMods = new JTextArea();
		explicitModsPanel.add(taExplicitMods, BorderLayout.CENTER);
		taExplicitMods.setRows(7);
		
		JButton btnWtb = new JButton("WTB");
		btnWtb.setBounds(76, 324, 89, 23);
		add(btnWtb);
		btnWtb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.setClipboard(item.getWTB());
			}
		});
		
		lblImplicitmod = new JLabel("");
		lblImplicitmod.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblImplicitmod.setHorizontalAlignment(SwingConstants.CENTER);
		lblImplicitmod.setBounds(1, 147, 247, 14);
		add(lblImplicitmod);
		
		lblId = new JLabel("id");
		lblId.setBounds(11, 33, 36, 14);
		add(lblId);

	}

	public void setItem(SearchResultItem item) {
		this.item = item;
		lblId.setText(item.getId());
		lblBuyout.setText(item.getBuyout());
		Mod implicitMod = item.getImplicitMod();
		if (implicitMod != null) {
			lblImplicitmod.setText(implicitMod.toStringDisplay());
		} else {
			lblImplicitmod.setText("");
		}
		lblName.setText(item.getName());
		
		List<String> modStrList = Lists.transform(item.getExplicitMods(), new Function<Mod, String>() {

			@Override
			public String apply(Mod input) {
				return input.toStringDisplay();
			}
		});
		
		String s = StringUtils.join(modStrList.toArray(), System.getProperty("line.separator"));
		taExplicitMods.setText(s);
	}
}
