package net.thirdy.blackmarket.swing;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;

import net.thirdy.blackmarket.core.util.BlackmarketConfig;

public class SwingUtil {

	public static void setClipboard(String result) {
		StringSelection stringSelection = new StringSelection(result);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}
	
	public interface ImageConsumer {
	    public void imageLoaded(BufferedImage img);
	}
	
	public static class ImageLoader extends SwingWorker<BufferedImage, BufferedImage> {

        private ImageConsumer consumer;
		private URL url;

        public ImageLoader(ImageConsumer consumer, URL url) {
			this.consumer = consumer;
			this.url = url;
		}

		@Override
        protected BufferedImage doInBackground() throws IOException {
			File configDirectory = BlackmarketConfig.configDirectory();
			String fileName = null;

			try {
				fileName = url.toURI().getRawPath();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			File imageFile = new File(configDirectory, fileName);
			BufferedImage picture = null;
			if (imageFile.exists()) {
				System.out.println("Reading image from file: " + imageFile.getAbsolutePath());
				picture = ImageIO.read(imageFile);
			} else {
				System.out.println("Reading image from url: " + url.toString());
				picture = ImageIO.read(url);
				imageFile.mkdirs();
				imageFile.createNewFile();
				ImageIO.write(picture, StringUtils.substringAfterLast(fileName, "."), imageFile);
			}

            
            return picture;

        }

        protected void done() {
            try {
                BufferedImage img = get();
                consumer.imageLoaded(img);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }           
    }
}
