package com.hieubui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

/**
 * @author hieubui
 *
 */
public class Application {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File dir = new File(args[0]);
		if (!dir.isDirectory()) {
			System.out.println("Error: it's not a directory");
			return;
		}
		
		File outDir = new File(dir.getName() + "-out");
		if (!outDir.exists()) {
			if (!outDir.mkdir()) {
				System.out.println("Error: require permission for create output directory");
				return;
			}
		}
		
		if (!outDir.canWrite()) {
			System.out.println("Error: require permission for output directory");
			return;
		}
		
		for (File base64File : dir.listFiles()) {
			if (base64File.getName().startsWith("base64")) {
				try (FileInputStream fis = new FileInputStream(base64File)) {
					byte[] bytes = new byte[fis.available()];
					fis.read(bytes);
					
					String base64 = new String(bytes);
					base64 = base64.replace("<div id=\"background\" style=\"background-image: url(&quot;data:image/jpeg;base64,", "");
					base64 = base64.replace("&quot;);\"></div>", "");

					byte[] base64Bytes = Base64.getDecoder().decode(base64);

					String outName = outDir.getName() + "/" + base64File.getName() + "-out.png";
					File imageFile = new File(outName);
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(base64Bytes));
					ImageIO.write(image, "png", imageFile);
				}
			}
		}
		System.out.println("Finish");
	}

}
