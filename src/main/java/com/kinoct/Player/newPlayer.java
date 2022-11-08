package com.kinoct.Player;

import com.kinoct.Utils.GetVideoInfo;
import com.kinoct.Utils.VideoTools;
import com.kinoct.Utils.ImageTools;
import com.kinoct.Utils.DeleteTempFiles;

import java.io.File;
import java.util.Scanner;

/**
 * @author Silolo
 * @version 1.0
 * @ClassName newPlayer
 * @description: TODO
 * @date 2022/11/7 20:14
 */
public class newPlayer {
    public static void main(String[] args) throws Exception {
        // 图片输出路径，字符输出路径建议使用空文件夹
        // 视频路径，注意使用双斜杠
        Scanner scanner = new Scanner(System.in);
        GetVideoInfo videoInfo = new GetVideoInfo();
        ImageTools imageTools = new ImageTools();
        VideoTools videoTools = new VideoTools();
        DeleteTempFiles deleteTempFiles = new DeleteTempFiles();

        String filePath;
        int fps;
        System.out.println("请输入mp4文件路径(注意使用mp4文件):");
        System.out.println("例如：" + "D:\\java\\test.mp4");
        System.out.print("路径：");
        filePath = scanner.next();
        scanner.close();
        File video = new File(filePath);

        // 文件保存路径
        String picPath = filePath + "_pics\\";
        String fileOutPath = filePath + "_out.mp4";
        String outVideoPath = filePath + "_zifu.mp4";
        String audioPath = filePath + ".mp3";
        String outPicsPath = filePath + "_outPics\\";

        if (!video.isDirectory()) {
            if (!video.exists()) {
                System.out.println("视频文件不存在！！！\n");
            } else {
                //创建图片文件夹
                new File(picPath).mkdirs();
                new File(outPicsPath).mkdirs();

                videoInfo.getVideoFps(filePath);
                fps = videoInfo.getFps();
                videoTools.videoToPic(filePath, picPath, fps);
                videoTools.extractAudio(filePath);

                File pic1 = new File(picPath + "1.jpg");
                if (!pic1.exists()) {
                    System.out.println("图片文件不存在！！！\n");
                } else {
                    imageTools.imageConversion(picPath, outPicsPath);
                }
                videoTools.compositeVideo(filePath, outPicsPath, fps);
                videoTools.mergeAudioAndVideo(fileOutPath, audioPath);

                System.out.println("开始删除缓存文件...");
                deleteTempFiles.deleteFiles(new File(outPicsPath));
                new File(audioPath).delete();
                new File(fileOutPath).delete();
                deleteTempFiles.deleteFiles(new File(picPath));
                System.out.println("过程文件已删除，开始播放视频...");
                videoTools.autoPlay(outVideoPath);
            }
        } else {
            System.out.println("这是文件夹吗？记得加.mp4!");
        }
    }
}
