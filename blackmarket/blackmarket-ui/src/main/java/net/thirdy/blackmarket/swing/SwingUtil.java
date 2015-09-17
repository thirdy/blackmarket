package net.thirdy.blackmarket.swing;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class SwingUtil {

	public static void setClipboard(String result) {
		StringSelection stringSelection = new StringSelection(result);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}
}
