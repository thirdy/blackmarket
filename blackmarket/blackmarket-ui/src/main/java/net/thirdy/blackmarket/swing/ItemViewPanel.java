package net.thirdy.blackmarket.swing;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem.Mod;

import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

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
		
		try {
			byte[] imgBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/sword1.png"));
			lblImg.setIcon(new ImageIcon(imgBytes));
		} catch (IOException e) {
			// won't likely happen since file is in classpath
			e.printStackTrace();
		}
		
//		lblImg.setIcon(new ImageIcon("account-login-8x.png"));
		lblImg.setBounds(35, 36, 176, 100);
		add(lblImg);
		
		explicitModsPanel = new JPanel();
		explicitModsPanel.setBounds(10, 155, 247, 137);
		add(explicitModsPanel);
		explicitModsPanel.setLayout(new BorderLayout(0, 0));
		
		taExplicitMods = new JTextArea();
		taExplicitMods.setRows(7);
		explicitModsPanel.add(taExplicitMods, BorderLayout.CENTER);
		
		JButton btnWtb = new JButton("WTB");
		btnWtb.setBounds(81, 303, 89, 23);
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
		lblImplicitmod.setBounds(10, 122, 247, 14);
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
