package com.kinoct.Utils;

import java.io.IOException;

/**
 * @author Silolo
 * @version 1.0
 * @ClassName testTools
 * @description: TODO
 * @date 2022/11/8 12:34
 */
public class testTools {
    public static void main(String[] args) throws IOException {
        VideoTools videoTools = new VideoTools();
        String path = "D:\\docs\\ji3.mp4";
        videoTools.autoPlay(path);
    }
}
