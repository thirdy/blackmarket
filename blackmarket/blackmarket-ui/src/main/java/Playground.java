import net.thirdy.blackmarket.core.BlackmarketUtil;
import net.thirdy.blackmarket.swing.BlackmarketJFrame;

/**
 * 
 */

/**
 * @author Vincent
 *
 */
public class Playground {
	public static void main(String[] args) {
		String page = BlackmarketUtil.loadFromClassPath(BlackmarketJFrame.class, "/ring-life.txt");
		System.out.println(page);
	}
}
