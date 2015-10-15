/*
 * Copyright (C) 2015 thirdy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.thirdy.blackmarket.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * @author thirdy
 *
 */
public class ImageCache {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	// Private constructor prevents instantiation from other classes
	private ImageCache() {
		imageCache = CacheBuilder.newBuilder()
				.maximumSize(1000)
				.build(new ImageCacheLoader());
	}

	private static final ImageCache INSTANCE = new ImageCache();

	public static ImageCache getInstance() {
		return ImageCache.INSTANCE;
	}

	private LoadingCache<String, Image> imageCache;

	public Image get(String key) {
		Image image = new WritableImage(50, 50);
		if (StringUtils.isNotBlank(key)) {
			try {
				image = imageCache.get(key);
				
//				image = new WritableImage(image.getPixelReader(), 0, 0, 50, 50);
				
			} catch (ExecutionException e) {
				logger.warn("Exception in loading image: " + key + ". Returning default image.",  e);
			}
		} else {
//			logger.warn("Key for image to load is empty. Returning default image.");
		}
		return image;
	}
	
	public void preLoad(String key) {
		get(key);
	}

	private static class ImageCacheLoader extends CacheLoader<String, Image> {

		@Override
		public Image load(String key) throws Exception {
			File imagesDirectory = new File("images");
			if(!imagesDirectory.exists()) imagesDirectory.mkdir();
					
			String fileName = null;
			Image image = null;
			
			// handle classpath
			if (key.startsWith("/")) {
				image = new Image(key, false);
			} else {
				URL url = new URL(key);
				fileName = url.toURI().getRawPath();
				File imageFile = new File(imagesDirectory, fileName);
				if (imageFile.exists()) {
					// Try loading from disk
					String _url = imageFile.toURI().toString();
					image = new Image(_url, false);
				} else {
					// Load from url and save to disk
					image = new Image(key);
					Files.createParentDirs(imageFile);
					BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write( bufferedImage, StringUtils.substringAfterLast(fileName, "."), baos );
					baos.flush();
					byte[] imageInByte = baos.toByteArray();
					baos.close();
					Files.write(imageInByte, imageFile);
				}
			}

			return image;
		}

	}
}
