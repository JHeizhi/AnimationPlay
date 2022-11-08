package com.kinoct.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Silolo
 * @version 1.0
 * @ClassName imageTools
 * @description: TODO
 * @date 2022/11/7 20:04
 */
public class ImageTools {

    public void imageConversion(String picPath,String outPath) throws IOException {
        System.out.println("开始图片转换...");
        String chars = "@MWXQRE%#&*=+~!^-.";
        File[] imageFiles = new File(picPath).listFiles();
        if (imageFiles != null) {
            for (File file : imageFiles) {
                BufferedImage image = ImageIO.read(file);
                int w = image.getWidth(), h = image.getHeight();
                // 创建新的灰度图片画板
                BufferedImage newImage = new BufferedImage(w, h, image.getType());
                Graphics2D graphics = newImage.createGraphics();
                graphics.setColor(null);
                graphics.fillRect(0, 0, w, h);
                int size = 12;
                Font font = new Font("宋体", Font.BOLD, size);
                graphics.setFont(font);
                for (int x = 0; x < w; x += size) {
                    for (int y = 0; y < h; y += size) {
                        int pixel = image.getRGB(x, y);
                        int r = (pixel & 0xff0000) >> 16;
                        int g = (pixel & 0xff00) >> 8;
                        int b = (pixel & 0xff);
                        graphics.setColor(new Color(r, g, b));
                        float grey = 0.299f * r + 0.578f * g + 0.114f * b;
                        int index = Math.round(grey * (chars.length() + 1) / 255);
                        String c = index >= chars.length() ? " " : String.valueOf(chars.charAt(index));
                        graphics.drawString(c, x, y);
                    }
                }
                graphics.dispose();
                ImageIO.write(newImage, "jpg", new File(outPath + file.getName()));
            }
        }
        System.out.println("图片转换完成...");
    }
}