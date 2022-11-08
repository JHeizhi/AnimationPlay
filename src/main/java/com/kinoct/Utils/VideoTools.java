package com.kinoct.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Silolo
 * @version 1.0
 * @ClassName VideoTools
 * @description: TODO
 * @date 2022/11/7 19:43
 */
public class VideoTools {

    /**
     * @param filePath
     * @param picPath
     * @param fps
     * @return void
     * @description: 将视频取帧
     * @author Silolo
     * @date 2022/11/7 19:44
     */
    public void videoToPic(String filePath, String picPath, int fps) throws Exception {
        System.out.println("开始程序...\n");
        System.out.println("开始生成图片...\n");
        File files = new File(picPath + "1.jpg");
        picPath = picPath + "\\%d.jpg";
        if (!files.exists()) {
            List<String> command = Arrays.asList("ffmpeg", "-i", filePath, "-r", String.valueOf(fps), picPath);
            ffmpegTool(command);
            System.out.println("视频转换完成...\n");

        } else {
            System.out.println("图片文件已存在....\n");
        }
    }

    /**
     * @param videoPath
     * @return void
     * @description: 提取音频文件
     * @author Silolo
     * @date 2022/11/7 19:48
     */
    public void extractAudio(String videoPath) throws Exception {
        System.out.println("开始生成音频文件...\n");
        File video = new File(videoPath + ".mp3");
        String videoOutPath = videoPath + ".mp3";
        if (!video.exists()) {
            List<String> command = Arrays.asList("ffmpeg", "-i", videoPath, "-q:a", "0", "-map", "a", videoOutPath);
            ffmpegTool(command);
            System.out.println("音频提取完成...\n");
        } else {
            System.out.println("音频文件已存在！\n");
        }
    }


    /**
     * @param filePath
     * @param picPath
     * @param fps
     * @return void
     * @description: 合成视频
     * @author Silolo
     * @date 2022/11/7 20:23
     */
    public void compositeVideo(String filePath, String picPath, int fps) throws Exception {
        System.out.println("开始合成视频...\n");
        File files = new File(picPath + "\\1.jpg");
        picPath = picPath + "\\%d.jpg";
        filePath = filePath + "_out.mp4";
        if (files.exists()) {
            List<String> command = Arrays.asList("ffmpeg", "-r", String.valueOf(fps), "-f", "image2", "-i", picPath, filePath);
            ffmpegTool(command);
            System.out.println("视频生成完成...\n");
        } else {
            System.out.println("图片文件不存在....\n");
        }
    }

    /**
     * @param videoPath
     * @param audioPath
     * @return void
     * @description: 合并音频和视频
     * @author Silolo
     * @date 2022/11/7 20:40
     */

    public void mergeAudioAndVideo(String videoPath, String audioPath) throws Exception {
        String outPath = videoPath.replace("_out.mp4", "_zifu.mp4");
        List<String> command = Arrays.asList("ffmpeg", "-i", audioPath, "-i", videoPath, "-y", outPath);
        ffmpegTool(command);
        System.out.println("视频生成完成...视频路径为：" + outPath);
    }

    /**
     * @param
     * @return void
     * @description: ffplay自动播放视频
     * @author Silolo
     * @date 2022/11/8 12:31
     */

    public void autoPlay(String videoPath) throws IOException {
        List<String> command = Arrays.asList("ffplay", "-x", "960", "-y", " 720", "-autoexit", videoPath);
        ffmpegTool(command);
        System.out.println("播放结束");
    }

    /**
     * @param command
     * @return void
     * @description: 封装调用ffmpeg方法
     * @author Silolo
     * @date 2022/11/8 13:11
     */

    public void ffmpegTool(List<String> command) throws IOException {
        ProcessBuilder pBuilder = new ProcessBuilder();
        pBuilder.command(command);
        pBuilder.redirectErrorStream(true);
        Process process = pBuilder.start();
        InputStream inputStream = process.getInputStream();
        InputStreamReader iReader = new InputStreamReader(inputStream, "GBK");
        char[] chars = new char[1024];
        int len;
        while ((len = iReader.read(chars)) != -1) {
            String string = new String(chars, 0, len);
            System.out.println(string);
        }
        iReader.close();
        inputStream.close();
    }
}