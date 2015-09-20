package net.thirdy.blackmarket.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import com.porty.swing.util.WindowUtils;

import java.awt.Color;

public class ShortListPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public ShortListPanel() {
		setLayout(new GridLayout(1, 4, 0, 0));
		
		ItemViewPanel itemViewPanel = new ItemViewPanel();
		itemViewPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(itemViewPanel);
		
		itemViewPanel.generateAndUseRandomItem();
		
		ItemViewPanel itemViewPanel_1 = new ItemViewPanel();
		itemViewPanel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(itemViewPanel_1);
		
		itemViewPanel_1.generateAndUseRandomItem();
		
		ItemViewPanel itemViewPanel_2 = new ItemViewPanel();
		itemViewPanel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(itemViewPanel_2);
		
		itemViewPanel_2.generateAndUseRandomItem();
		
		ItemViewPanel itemViewPanel_3 = new ItemViewPanel();
		itemViewPanel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(itemViewPanel_3);
		
		itemViewPanel_3.generateAndUseRandomItem();

	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Blackmarket Shortlist Prototype");
		f.add(new ShortListPanel());
		f.setSize(900, 400);
		WindowUtils.centerWindow(f);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
